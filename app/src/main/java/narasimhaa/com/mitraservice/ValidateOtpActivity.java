package narasimhaa.com.mitraservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.kinda.alert.KAlertDialog;

import narasimhaa.com.mitraservice.Model.ServerResponse;
import narasimhaa.com.mitraservice.Utility.MyUtilities;
import narasimhaa.com.mitraservice.Utility.SharedPreferenceUtils;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ValidateOtpActivity extends AppCompatActivity {
    KAlertDialog pDialog;
    private Toolbar toolbar;
    private OtpEntryEditText otpEntryEditText;
    private String mobile = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate_otp);
        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar_extra);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Mr Mason");
        pDialog = new KAlertDialog(this, KAlertDialog.PROGRESS_TYPE);

        mobile = getIntent().getStringExtra("KEY_MOBILE_NO");
        otpEntryEditText = findViewById(R.id.et_otp);

        otpEntryEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() == 6) {

                    validateOtpRequest(mobile,Integer.parseInt(s.toString()));

                }else {

                    //Toast.makeText(ValidateOtpActivity.this, "Enter full otp", Toast.LENGTH_SHORT).show();

                }
            }

        });
    }

    private void validateOtpRequest(String mobile,int otp){
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading...");
        pDialog.setCancelable(false);
        //pDialog.show();

        MyUtilities.showAlertDialog(ValidateOtpActivity.this, KAlertDialog.PROGRESS_TYPE,MyUtilities.KAlertDialogTitleLoding);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiInterface.URL_BASE)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        try {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("MOBILE_NO", mobile);
            jsonObject.addProperty("OTP", otp);

            String jsonStr = String.valueOf(jsonObject);

            Call<ServerResponse> userCall = apiInterface.otpValidate(jsonObject.toString());

            userCall.enqueue(new Callback<ServerResponse>() {
                @Override
                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {

                    if (response.body()!=null){

                        Log.d("responsee", "onResponse: " + response.body().getStatus());
                        Log.e("responsee", "onResponse: " + response.body().getMsg());

                        if(response.body().getStatus()==true)
                        {

                            MyUtilities.cancelAlertDialog(ValidateOtpActivity.this);
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();


                        }else{


                            Toast.makeText(ValidateOtpActivity.this,"Invalid Otp",Toast.LENGTH_LONG).show();

                            MyUtilities.cancelAlertDialog(ValidateOtpActivity.this);

                        }
                    }else {

                        //MyUtilities.showAlertDialog(ValidateOtpActivity.this,KAlertDialog.ERROR_TYPE,MyUtilities.KAlertDialogTitleError);
                        MyUtilities.showToast(ValidateOtpActivity.this,MyUtilities.KAlertDialogTitleError);

                    }

                }

                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {

                    Log.e("erro", "onFailure: " + t.getMessage());
                    //pDialog.cancel();
                    MyUtilities.showToast(ValidateOtpActivity.this,MyUtilities.KAlertDialogTitleError);
                    MyUtilities.cancelAlertDialog(ValidateOtpActivity.this);

                    //MyUtilities.showAlertDialog(ValidateOtpActivity.this,KAlertDialog.ERROR_TYPE,MyUtilities.KAlertDialogTitleError);

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            MyUtilities.showToast(ValidateOtpActivity.this,MyUtilities.KAlertDialogTitleError);

            //MyUtilities.showAlertDialog(ValidateOtpActivity.this,KAlertDialog.ERROR_TYPE,MyUtilities.KAlertDialogTitleError);
        }


/*
            @Override
CF            public void onResponse(Call<narasimhaa.com.mrmason.Model.ServerResponse> call, Response<narasimhaa.com.mrmason.Model.ServerResponse> response) {
            }

            @Override
            public void onFailure(Call<narasimhaa.com.mrmason.Model.ServerResponse> call, Throwable t) {
            }
*/

    }
}