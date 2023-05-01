package narasimhaa.com.mitraservice;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.kinda.alert.KAlertDialog;


import narasimhaa.com.mitraservice.Model.ServerResponse;
import narasimhaa.com.mitraservice.Receivers.NetworkChangeListener;
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

public class MainActivity extends AppCompatActivity {
    EditText et_mno, et_password;
    TextView tx_language, forgot_password;
    Button bt_login;
    Activity activity;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    KAlertDialog pDialog;
    private Toolbar toolbar;
    private BroadcastReceiver mNetworkReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (SharedPreferenceUtils.getLoginState(MainActivity.this)==true){
            Intent intent = new Intent(getApplicationContext(), DashBoardActivity.class);
            startActivity(intent);
            finish();
        }

        et_mno = (EditText) findViewById(R.id.et_mno);
        et_password = (EditText) findViewById(R.id.et_password);
        tx_language = (TextView) findViewById(R.id.tx_language);
        forgot_password = (TextView) findViewById(R.id.txt_forgotpassword);
        bt_login = (Button) findViewById(R.id.bt_login);
        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar_extra);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("MrMason LogIn");
        sharedpreferences = getSharedPreferences("LoginPREFERENCES",MODE_PRIVATE);
        editor = sharedpreferences.edit();
        pDialog = new KAlertDialog(this, KAlertDialog.PROGRESS_TYPE);
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ForgotPassword.class);
                startActivity(i);
            }
        });

        tx_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, LanguageActivity.class);
                startActivity(i);
            }
        });

        mNetworkReceiver = new NetworkChangeListener();
        registerNetworkBroadcastForNougat();

        bt_login.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View view){

                if (et_mno.getText().toString().trim().length() == 0) {
                    et_mno.setError("Please Enter EMail");
                    et_mno.requestFocus();
                }else if (et_password.getText().toString().trim().length() == 0) {
                    et_password.setError("Please Enter Password");
                    et_password.requestFocus();
                } else {

                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    pDialog.setTitleText("Loading...");
                    pDialog.setCancelable(false);
                    //pDialog.show();

                    MyUtilities.showAlertDialog(MainActivity.this,KAlertDialog.PROGRESS_TYPE,MyUtilities.KAlertDialogTitleLoding);
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
                        jsonObject.addProperty("EMAIL_ID", et_mno.getText().toString());
                        jsonObject.addProperty("PASSWORD", et_password.getText().toString());
                        editor.putString("userName", et_mno.getText().toString());
                        editor.putString("userPwd", et_password.getText().toString());
                        editor.commit();

                        String jsonStr = String.valueOf(jsonObject);

                        Call<ServerResponse> userCall = apiInterface.getLoginResponsee(jsonObject.toString());

                        userCall.enqueue(new Callback<ServerResponse>() {
                            @Override
                            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {

                                if (response.body()!=null){

                                    Log.d("responsee", "onResponse: " + response.body().getStatus());
                                    Log.e("responsee", "onResponse: " + response.body().getMsg());

                                    if(response.body().getStatus()==true)
                                    {
                                        SharedPreferenceUtils.setValue(MainActivity.this,MyUtilities.PREF_EMAIL,response.body().getData().getEMAILID());
                                        SharedPreferenceUtils.setValue(MainActivity.this,MyUtilities.PREF_USER_NAME,response.body().getData().getNAME());
                                        SharedPreferenceUtils.setValue(MainActivity.this,MyUtilities.PREF_BOD_SEQ_NO,response.body().getData().getBODSEQNO());
                                        SharedPreferenceUtils.setValue(MainActivity.this,MyUtilities.PREF_USER_TYPE,response.body().getData().getUSERTYPE());
                                        SharedPreferenceUtils.setValue(MainActivity.this,MyUtilities.PREF_USER_MOBILE_NO,response.body().getData().getMOBILENO());
                                        SharedPreferenceUtils.setValue(MainActivity.this,MyUtilities.PREF_USER_CITY,response.body().getData().getCITY());
                                        SharedPreferenceUtils.setValue(MainActivity.this,MyUtilities.PREF_USER_ADDRESS,response.body().getData().getADDRESS());
                                        SharedPreferenceUtils.setValue(MainActivity.this,MyUtilities.PREF_USER_PINCODE,response.body().getData().getPINCODENO());

                                        MyUtilities.cancelAlertDialog(MainActivity.this);
                                        Intent intent = new Intent(getApplicationContext(), DashBoardActivity.class);
                                        startActivity(intent);
                                        finish();


                                    }else{


                                        Toast.makeText(MainActivity.this,"Invalid Username or password.",Toast.LENGTH_LONG).show();

                                       MyUtilities.cancelAlertDialog(MainActivity.this);

                                    }
                                }else {

                                    //MyUtilities.showAlertDialog(MainActivity.this,KAlertDialog.ERROR_TYPE,MyUtilities.KAlertDialogTitleError);
                                    MyUtilities.showToast(MainActivity.this,MyUtilities.KAlertDialogTitleError);

                                }

                            }

                            @Override
                            public void onFailure(Call<ServerResponse> call, Throwable t) {

                                Log.e("erro", "onFailure: " + t.getMessage());
                                 //pDialog.cancel();
                                MyUtilities.showToast(MainActivity.this,MyUtilities.KAlertDialogTitleError);
                                MyUtilities.cancelAlertDialog(MainActivity.this);

                                //MyUtilities.showAlertDialog(MainActivity.this,KAlertDialog.ERROR_TYPE,MyUtilities.KAlertDialogTitleError);

                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        MyUtilities.showToast(MainActivity.this,MyUtilities.KAlertDialogTitleError);

                        //MyUtilities.showAlertDialog(MainActivity.this,KAlertDialog.ERROR_TYPE,MyUtilities.KAlertDialogTitleError);
                    }
                }
            }

/*
            @Override
            public void onResponse(Call<narasimhaa.com.mrmason.Model.ServerResponse> call, Response<narasimhaa.com.mrmason.Model.ServerResponse> response) {
            }

            @Override
            public void onFailure(Call<narasimhaa.com.mrmason.Model.ServerResponse> call, Throwable t) {
            }
*/
        });
    }

    public void register(View view) {
        Intent i = new Intent(MainActivity.this, RegistrationActivity.class);
        startActivity(i);
    }


    public void dialog(boolean value){

        if(value){
        /*    tv_check_connection.setText("We are back !!!");
            tv_check_connection.setBackgroundColor(Color.GREEN);
            tv_check_connection.setTextColor(Color.WHITE);*/

            Handler handler = new Handler();
            Runnable delayrunnable = new Runnable() {
                @Override
                public void run() {
                    //  tv_check_connection.setVisibility(View.GONE);

                    //    MyUtilities.showToast(MainActivity.this,"We are back");

//                    Toast.makeText(getApplicationContext(), "We are back", Toast.LENGTH_SHORT).show();
                }
            };
            handler.postDelayed(delayrunnable, 3000);
        }else {
        /*    tv_check_connection.setVisibility(View.VISIBLE);
            tv_check_connection.setText("Could not Connect to internet");
            tv_check_connection.setBackgroundColor(Color.RED);
            tv_check_connection.setTextColor(Color.WHITE);*/
            //Toast.makeText(getApplicationContext(), "Could not Connect to internet", Toast.LENGTH_SHORT).show();
        }
    }


    private void registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    protected void unregisterNetworkChanges() {
        try {
            unregisterReceiver(mNetworkReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterNetworkChanges();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferenceUtils.setValue(MainActivity.this,MyUtilities.PREF_SERVICE_SEARCH,"");
        SharedPreferenceUtils.setValue(MainActivity.this,MyUtilities.PREF_CITY_SEARCH,"");

    }
}



