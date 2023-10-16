package narasimhaa.com.mitraservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.kinda.alert.KAlertDialog;

import narasimhaa.com.mitraservice.Model.MaterialDevelopers.ServicesResponseSizeBrand;
import narasimhaa.com.mitraservice.Model.ServerResponse;
import narasimhaa.com.mitraservice.Utility.MyUtilities;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class UpdatePasswordActivity extends AppCompatActivity {

    private EditText editTextConfirmPassword;
    private EditText editTextPassword;
    private EditText editTextOTP;
    private Button buttonSubmit;
    KAlertDialog pDialog;
    private Toolbar toolbar;
    private  String emailMobileNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextOTP = findViewById(R.id.editTextOTP);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar_extra);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Update Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        emailMobileNo = getIntent().getStringExtra("EMAIL_MOBILE");
        pDialog = new KAlertDialog(this, KAlertDialog.PROGRESS_TYPE);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String confirmPassword = editTextConfirmPassword.getText().toString();
                String newPassword = editTextPassword.getText().toString();
                String otp = editTextOTP.getText().toString();

                if (newPassword.isEmpty() || confirmPassword.isEmpty() || otp.isEmpty()) {
                    Toast.makeText(UpdatePasswordActivity.this, "Please fill all details", Toast.LENGTH_SHORT).show();
                } else if (!newPassword.equals(confirmPassword)) {
                    Toast.makeText(UpdatePasswordActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                } else {
                    validateOtpRequestForUpdatePassword(emailMobileNo, newPassword, otp);
                }

            }
        });
    }

    private void validateOtpRequestForUpdatePassword(String mobile,String newPassword,String otp){
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading...");
        pDialog.setCancelable(false);
        //pDialog.show();

        MyUtilities.showAlertDialog(UpdatePasswordActivity.this, KAlertDialog.PROGRESS_TYPE,MyUtilities.KAlertDialogTitleLoding);
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
            jsonObject.addProperty("NEW_PASSWORD", newPassword);
            jsonObject.addProperty("OTP", otp);

            String jsonStr = String.valueOf(jsonObject);

            Call<ServerResponse> userCall = apiInterface.performUpdatePassword(jsonObject.toString());

            userCall.enqueue(new Callback<ServerResponse>() {
                @Override
                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {

                    if (response.body()!=null){

                        Log.d("responsee", "onResponse: " + response.body().getStatus());

                        if(response.body().getStatus()==true)
                        {

                            MyUtilities.cancelAlertDialog(UpdatePasswordActivity.this);
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            Toast.makeText(UpdatePasswordActivity.this,"Password Updated. Please login again to continue ",Toast.LENGTH_LONG).show();
                            startActivity(intent);
                            finish();


                        }else{


                            Toast.makeText(UpdatePasswordActivity.this,"Invalid Otp",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                            MyUtilities.cancelAlertDialog(UpdatePasswordActivity.this);

                        }
                    }else {

                        //MyUtilities.showAlertDialog(UpdatePasswordActivity.this,KAlertDialog.ERROR_TYPE,MyUtilities.KAlertDialogTitleError);
                        MyUtilities.showToast(UpdatePasswordActivity.this,MyUtilities.KAlertDialogTitleError);

                    }

                }

                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {

                    Log.e("erro", "onFailure: " + t.getMessage());
                    //pDialog.cancel();
                    MyUtilities.showToast(UpdatePasswordActivity.this,MyUtilities.KAlertDialogTitleError);
                    MyUtilities.cancelAlertDialog(UpdatePasswordActivity.this);

                    //MyUtilities.showAlertDialog(UpdatePasswordActivity.this,KAlertDialog.ERROR_TYPE,MyUtilities.KAlertDialogTitleError);

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            MyUtilities.showToast(UpdatePasswordActivity.this,MyUtilities.KAlertDialogTitleError);

            //MyUtilities.showAlertDialog(UpdatePasswordActivity.this,KAlertDialog.ERROR_TYPE,MyUtilities.KAlertDialogTitleError);
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