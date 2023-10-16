package narasimhaa.com.mitraservice;

import android.content.Intent;

import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.kinda.alert.KAlertDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import narasimhaa.com.mitraservice.Model.ResponseStatus;
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

public class ForgotPassword extends AppCompatActivity {
    ForgotPassword context;
    EditText et_mobile_no, et_password;
    Button forgetBtn;
    KAlertDialog pDialog;
    private Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        context = this;
        initUI();
        //setEvents();
        performForgetPassword();
        setupActionBar();

    }


    private void initUI() {
        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar_extra);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Forgot Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        et_mobile_no = findViewById(R.id.et_mobile_no);
        et_password = findViewById(R.id.et_password);
        forgetBtn = (Button) findViewById(R.id.buttonSubmit);
        pDialog = new KAlertDialog(this, KAlertDialog.PROGRESS_TYPE);


    }

    private void setEvents() {

        forgetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // email mobile

                String emailMobileNo = et_mobile_no.getText() + "";
                String password = et_password.getText() + "";

                if (emailMobileNo.equals("") || password.equals("")) {

                    MyUtilities.showToast(ForgotPassword.this, "Please fill all fields.");

                } else {
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
                        jsonObject.addProperty("EMAIL_ID", emailMobileNo);
                        jsonObject.addProperty("NEW_PASSWORD", password);

                        String jsonStr = String.valueOf(jsonObject);

                        Call<ServerResponse> userCall = apiInterface.forgotPassword(jsonObject.toString());

                        userCall.enqueue(new Callback<ServerResponse>() {
                            @Override
                            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {

                                if (response.body() != null) {

                                    if (response.body().getStatus() == true) {

                                        MyUtilities.cancelAlertDialog(ForgotPassword.this);
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                        MyUtilities.showToast(ForgotPassword.this, "Password changed");


                                    } else {

                                        MyUtilities.cancelAlertDialog(ForgotPassword.this);

                                        MyUtilities.showToast(ForgotPassword.this, "Please enter valid email id/phone no");
                                    }
                                } else {

                                    MyUtilities.showAlertDialog(ForgotPassword.this, KAlertDialog.ERROR_TYPE, MyUtilities.KAlertDialogTitleError);

                                }

                            }

                            @Override
                            public void onFailure(Call<ServerResponse> call, Throwable t) {

                                Log.e("erro", "onFailure: " + t.getMessage());
                                MyUtilities.showToast(ForgotPassword.this, MyUtilities.KAlertDialogTitleError);
                                MyUtilities.cancelAlertDialog(ForgotPassword.this);

                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        MyUtilities.showAlertDialog(ForgotPassword.this, KAlertDialog.ERROR_TYPE, MyUtilities.KAlertDialogTitleError);
                    }

                }


            }
        });
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
            // actionBar.setTitle(context.getResources().getString(R.string.title_ForgetPassword));
        }

    }

    private void performForgetPassword() {

        forgetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // email mobile

                String emailMobileNo = et_mobile_no.getText() + "";

                if (emailMobileNo.equals("")) {

                    MyUtilities.showToast(ForgotPassword.this, "Please enter email/phone no.");

                } else {
                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    pDialog.setTitleText("Loading...");
                    pDialog.setCancelable(false);
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
                        jsonObject.addProperty("MOBILE_NO", emailMobileNo);

                        String jsonStr = String.valueOf(jsonObject);

                        Call<ServerResponse> userCall = apiInterface.performForgetPasswordNew(jsonObject.toString());

                        userCall.enqueue(new Callback<ServerResponse>() {
                            @Override
                            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {

                                if (response.body() != null) {

                                    if (response.body().getStatus() == true) {

                                        MyUtilities.cancelAlertDialog(ForgotPassword.this);
                                        Intent intent = new Intent(getApplicationContext(), UpdatePasswordActivity.class);
                                        intent.putExtra("EMAIL_MOBILE",emailMobileNo);
                                        startActivity(intent);
                                        finish();
                                        MyUtilities.showToast(ForgotPassword.this, "Otp sent");


                                    } else {

                                        MyUtilities.cancelAlertDialog(ForgotPassword.this);

                                        MyUtilities.showToast(ForgotPassword.this, "Please enter valid email id/phone no");
                                    }
                                } else {

                                    MyUtilities.showAlertDialog(ForgotPassword.this, KAlertDialog.ERROR_TYPE, MyUtilities.KAlertDialogTitleError);

                                }

                            }

                            @Override
                            public void onFailure(Call<ServerResponse> call, Throwable t) {

                                Log.e("erro", "onFailure: " + t.getMessage());
                                MyUtilities.showToast(ForgotPassword.this, MyUtilities.KAlertDialogTitleError);
                                MyUtilities.cancelAlertDialog(ForgotPassword.this);

                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        MyUtilities.showAlertDialog(ForgotPassword.this, KAlertDialog.ERROR_TYPE, MyUtilities.KAlertDialogTitleError);
                    }

                }


            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {

            finish();

        }

        return super.onOptionsItemSelected(item);
    }


    public void completedTask(ResponseStatus res, int type) {

        try {

            if (res.isSucces()) {//ForgetPasswordResultActivity

                String otp = "";
                String uid = "";

                JSONArray jsonArray = new JSONArray(res.getContent() + "");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    otp = jsonObject.get("otp") + "";
                    uid = jsonObject.get("uid") + "";
                }
                Intent intent = new Intent(context, ForgetPasswordResultActivity.class);
                intent.putExtra("otp", otp);
                intent.putExtra("uid", uid);
                startActivity(intent);


            }

            MyUtilities.showToast(context, res.getMssg());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}


