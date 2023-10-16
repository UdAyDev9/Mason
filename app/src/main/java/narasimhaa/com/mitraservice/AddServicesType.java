package narasimhaa.com.mitraservice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

public class AddServicesType extends AppCompatActivity {

    private EditText et_servicename;
    private Button btnSubmit;
    private String servicestr = "";
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_type);
        et_servicename = (EditText) findViewById(R.id.et_servicename);

        btnSubmit = (Button) findViewById(R.id.bt_submit);
        toolbar = (Toolbar) findViewById(R.id.toolbar_extra);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_add_material);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                servicestr = et_servicename.getText().toString();

                addservicename(servicestr);

            }



        });
    }

    private void addservicename(String servicestr) {

        MyUtilities.showAlertDialog(AddServicesType.this, KAlertDialog.PROGRESS_TYPE, MyUtilities.KAlertDialogTitleLoding);

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

            JsonObject jsonReg = new JsonObject();

            jsonReg.addProperty("MATERIAL", servicestr);


            Call<ServerResponse> userCall;


            userCall = apiInterface.insertMaterial(jsonReg.toString());

            userCall.enqueue(new Callback<ServerResponse>() {
                @Override
                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {


                    if (response.body() != null) {


                        if (response.body().getStatus() == true) {

                            //SharedPreferenceUtils.setValue(AddSizesAndBrandsActivity.this, MyUtilities.PREF_SER_PER_SEQ_ID, response.body().getData().getSER_PER_SEQ_ID());

                            MyUtilities.showToast(AddServicesType.this, "Material added successfully!!!");

                            MyUtilities.cancelAlertDialog(AddServicesType.this);

                            Intent intent = new Intent(AddServicesType.this, DashBoardActivity.class);
                            startActivity(intent);
                            finish();

                        } else {

                            MyUtilities.showToast(AddServicesType.this, response.body().getMsg().toString());
                            MyUtilities.cancelAlertDialog(AddServicesType.this);

                        }


                    } else {

                        MyUtilities.cancelAlertDialog(AddServicesType.this);

                        MyUtilities.showToast(AddServicesType.this, "Something went wrong! Please try later");

                    }


                }

                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {
                    MyUtilities.showToast(AddServicesType.this, MyUtilities.KAlertDialogTitleError);
                    MyUtilities.cancelAlertDialog(AddServicesType.this);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            MyUtilities.cancelAlertDialog(AddServicesType.this);

        }
    }






}