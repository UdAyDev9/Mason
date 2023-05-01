package narasimhaa.com.mitraservice;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;


import java.io.IOException;
import java.util.List;

import narasimhaa.com.mitraservice.Adapater.MaterialDevelopersAdapter;
import narasimhaa.com.mitraservice.Model.ContentProfile;
import narasimhaa.com.mitraservice.Model.MaterialDevelopers.MaterialDevelopersServerResponse;
import narasimhaa.com.mitraservice.Model.MaterialDevelopers.DataItem;
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

public class MaterialUpdateActivity extends AppCompatActivity {

    MaterialUpdateActivity context;

    SharedPreferences sharedPreferences;
    private TextView tvServiceType,tvYearOfxp,tvEducation,tvPersoalCert,tvRange;
    private Button updateBtn;
    private Toolbar toolbar;
    private ImageView imgUpdateCert;
    private List<DataItem> data;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    MaterialDevelopersAdapter materialDevelopersAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_update);
        sharedPreferences = getSharedPreferences("ServicePREFERENCES",MODE_PRIVATE);
        tvServiceType = (TextView)findViewById(R.id.textView9);
        tvYearOfxp = (TextView)findViewById(R.id.textView10);
        tvEducation = (TextView)findViewById(R.id.textView11);
        tvPersoalCert = (TextView)findViewById(R.id.textView12);
        tvRange = (TextView)findViewById(R.id.textView13);
        tvRange = (TextView)findViewById(R.id.textView13);
        imgUpdateCert = (ImageView) findViewById(R.id.iv_update_certi);
        tvYearOfxp.setText(sharedPreferences.getString("yoe",""));
        tvEducation.setText(sharedPreferences.getString("edu",""));
        tvPersoalCert.setText(sharedPreferences.getString("certi",""));
        tvRange.setText(sharedPreferences.getString("range",""));
        recyclerView = findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(MaterialUpdateActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        String uri = sharedPreferences.getString("uri_imagee","");
        Log.d("uri", "onCreate: "+uri);
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(uri));
            imgUpdateCert.setImageBitmap(bitmap);


        } catch (IOException e) {
            e.printStackTrace();
        }
        tvServiceType.setText(sharedPreferences.getString("service",""));

        //updateBtn = (Button)findViewById(R.id.update_btn);
        context = this;
        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar_extra);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_material_update);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //setupActionBar();
/*        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send_data_to_server();
                // startActivity(new Intent(ServiceUpdateActivity.this,AddMyServiceActivity.class));
                Intent intent = new Intent(MaterialUpdateActivity.this,AddMyServiceActivity.class);

                intent.putExtra("service_yoe" ,         sharedPreferences.getString("yoe"    ,""));
                intent.putExtra("service_edu" ,         sharedPreferences.getString("edu"    ,""));
                intent.putExtra("service_certi"  ,      sharedPreferences.getString("certi"  ,""));
                intent.putExtra("service_range" ,       sharedPreferences.getString("range"  ,""));
                intent.putExtra("service_service"  ,    sharedPreferences.getString("service",""));

                startActivity(intent);
            }
        });*/
        getData();
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

                    List<ContentProfile> homePageBeanData = response.body().getContent();

                    Log.e("op", "onResponse: "+response.body().getStatus());
                    Log.e("op[[", "onResponse: "+response.body().getContent());

                }

                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {
                    MyUtilities.showToast(MaterialUpdateActivity.this,MyUtilities.KAlertDialogTitleError);
                    MyUtilities.cancelAlertDialog(MaterialUpdateActivity.this);
                    Log.e("erro", "onFailure: "+t.getMessage() );
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getData() {
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

        JsonObject paramObject = new JsonObject();

        //paramObject.addProperty("EMAIL_ID", );

        Call<MaterialDevelopersServerResponse> userCall = apiInterface.getMaterialDevelopersList(SharedPreferenceUtils.getValue(MaterialUpdateActivity.this, MyUtilities.PREF_EMAIL));

        userCall.enqueue(new Callback<MaterialDevelopersServerResponse>() {
            @Override
            public void onResponse(Call<MaterialDevelopersServerResponse> call, Response<MaterialDevelopersServerResponse> response) {

                if (response.body() != null) {

                    if (response.body().isStatus() == true) {


                        MyUtilities.cancelAlertDialog(MaterialUpdateActivity.this);


                        data = response.body().getData();
                        if (data.size()>0){
                            recyclerView.setVisibility(View.VISIBLE);
                            materialDevelopersAdapter = new MaterialDevelopersAdapter(data,MaterialUpdateActivity.this);
                            recyclerView.setAdapter(materialDevelopersAdapter);

                        }else {

                            //tvNoData.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);

                        }

                    } else {
                        MyUtilities.cancelAlertDialog(MaterialUpdateActivity.this);

                        MyUtilities.showToast(MaterialUpdateActivity.this, MyUtilities.TOAST_MATERIAL_NOT_ADDED_YET);
                    }

                } else {

                    MyUtilities.cancelAlertDialog(MaterialUpdateActivity.this);
                    MyUtilities.showToast(MaterialUpdateActivity.this, MyUtilities.KAlertDialogTitleError);

                }
            }


            @Override
            public void onFailure(Call<MaterialDevelopersServerResponse> call, Throwable t) {

                MyUtilities.showToast(MaterialUpdateActivity.this,MyUtilities.KAlertDialogTitleError);
                MyUtilities.cancelAlertDialog(MaterialUpdateActivity.this);

            }
        });
    }


}