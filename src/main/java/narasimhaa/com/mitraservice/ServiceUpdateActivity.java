package narasimhaa.com.mitraservice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.List;

import narasimhaa.com.mitraservice.Model.ContentProfile;
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

public class ServiceUpdateActivity extends AppCompatActivity {

    ServiceUpdateActivity context;

    SharedPreferences sharedPreferences;
    private TextView tvServiceType,tvYearOfxp,tvEducation,tvPersoalCert,tvRange;
    private Button updateBtn;
    private Toolbar toolbar;
    private ImageView imgUpdateCert;
    private String strServiceType,strYOE,strRange,strCharge="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_update);
        sharedPreferences = getSharedPreferences("ServicePREFERENCES",MODE_PRIVATE);
        tvServiceType = (TextView)findViewById(R.id.textView9);
        tvYearOfxp = (TextView)findViewById(R.id.textView10);
        tvEducation = (TextView)findViewById(R.id.textView11);
        tvPersoalCert = (TextView)findViewById(R.id.textView12);
        tvRange = (TextView)findViewById(R.id.textView13);
        imgUpdateCert = (ImageView) findViewById(R.id.iv_update_certi);



        String uri = sharedPreferences.getString("uri_imagee","");
        Log.d("uri", "onCreate: "+uri);
        Bitmap bitmap = null;
        getStaffData();
        try {

            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(uri));
            imgUpdateCert.setImageBitmap(bitmap);

        } catch (IOException e) {
            e.printStackTrace();
        }
        tvServiceType.setText(sharedPreferences.getString("service",""));

        updateBtn = (Button)findViewById(R.id.update_btn);
        context = this;
        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar_extra);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_ServiceUpdate);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //setupActionBar();
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send_data_to_server();
               // startActivity(new Intent(ServiceUpdateActivity.this,ServiceUpdateActivity.class));
                Intent intent = new Intent(ServiceUpdateActivity.this,AddMyServiceActivity.class);

                intent.putExtra("service_names" ,       strServiceType);
                intent.putExtra("service_yoe" ,         strYOE);
                intent.putExtra("service_charge"  ,     strCharge);
                intent.putExtra("service_range" ,       strRange);
                intent.putExtra("service_update_yes_or_no" ,       "yes");

                startActivity(intent);
            }
        });
    }
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(context.getResources().getString(R.string.title_ServiceUpdate));
        }
    }
    private void send_data_to_server(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiInterface.URL_BASE)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);


        try {


            JsonObject paramObject = new JsonObject();
            paramObject.addProperty("EMAIL_ID", "krishna@appcare.co.in");

            Call<ServerResponse> userCall = apiInterface.addService(paramObject.toString());

            userCall.enqueue(new Callback<ServerResponse>() {
                @Override
                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {

                    MyUtilities.showToast(ServiceUpdateActivity.this, "Service Updated Successfully!!!");

                    List<ContentProfile> homePageBeanData = response.body().getContent();

                    Log.e("op", "onResponse: "+response.body().getStatus());
                    Log.e("op[[", "onResponse: "+response.body().getContent());

                }

                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {

                    MyUtilities.showToast(ServiceUpdateActivity.this,MyUtilities.KAlertDialogTitleError);
                    MyUtilities.cancelAlertDialog(ServiceUpdateActivity.this);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getStaffData() {

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

        sharedPreferences = getSharedPreferences("LoginPREFERENCES", MODE_PRIVATE);

        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("BOD_SEQ_NO", SharedPreferenceUtils.getValue(ServiceUpdateActivity.this, MyUtilities.PREF_BOD_SEQ_NO));
        Call<ServerResponse> userCall = apiInterface.getStaffProfileResponse(SharedPreferenceUtils.getValue(ServiceUpdateActivity.this, MyUtilities.PREF_EMAIL));

        userCall.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {


                if (response.body() != null) {

                    if (response.body().getStatus() == true) {

                        MyUtilities.cancelAlertDialog(ServiceUpdateActivity.this);

                        ContentProfile homePageBeanData = response.body().getData();
                        tvServiceType.setText(homePageBeanData.getSERVICE_NAME());
                        tvYearOfxp.setText(homePageBeanData.getEXPERIENCE());
                        tvRange.setText(homePageBeanData.getWITH_IN_RANGE());
                        tvEducation.setText(homePageBeanData.getQUALIFICATION());

                        strServiceType =homePageBeanData.getSERVICE_NAME();
                        strYOE =homePageBeanData.getEXPERIENCE();
                        strRange =homePageBeanData.getWITH_IN_RANGE();
                        strCharge =homePageBeanData.getQUALIFICATION();


                    } else {
                        MyUtilities.cancelAlertDialog(ServiceUpdateActivity.this);

                        MyUtilities.showToast(ServiceUpdateActivity.this, MyUtilities.TOAST_SERVICE_NOT_ADDED_YET);

                    }


                    //Log.i("Baba", "Res ponse is " + homePageBeanData.toString());

                } else {

                    MyUtilities.cancelAlertDialog(ServiceUpdateActivity.this);
                    MyUtilities.showToast(ServiceUpdateActivity.this, MyUtilities.KAlertDialogTitleError);

                }
            }


            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                MyUtilities.showToast(ServiceUpdateActivity.this,MyUtilities.KAlertDialogTitleError);
                MyUtilities.cancelAlertDialog(ServiceUpdateActivity.this);

            }
        });
    }

}