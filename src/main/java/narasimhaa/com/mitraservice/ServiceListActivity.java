package narasimhaa.com.mitraservice;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.kinda.alert.KAlertDialog;

import java.util.ArrayList;
import java.util.List;

import narasimhaa.com.mitraservice.Adapater.AllUserListAdminAdapter;
import narasimhaa.com.mitraservice.Adapater.SiviceListAdapter;

import narasimhaa.com.mitraservice.Model.MaterialDevelopers.ServicesResponseSizeBrand;
import narasimhaa.com.mitraservice.Model.ServicesResponse;
import narasimhaa.com.mitraservice.Model.service.ServicesDataItem;
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

public class ServiceListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SiviceListAdapter siviceListAdapter;
    List<ServicesDataItem> data = new ArrayList<>();

    RecyclerView.LayoutManager layoutManager;
    private Toolbar toolbar;
    private TextView tvNoData;
    private Button deleteBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serivelist);
        toolbar = (Toolbar) findViewById(R.id.toolbar_extra);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Service List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.recycler_view);

        tvNoData = findViewById(R.id.tv_no_data);
        getMaterialsData(false);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(ServiceListActivity.this);
        recyclerView.setLayoutManager(layoutManager);
    }

    public void getMaterialsData(boolean isDeveloper) {
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

        paramObject.addProperty("BOD_SEQ_NO", SharedPreferenceUtils.getValue(ServiceListActivity.this, MyUtilities.PREF_BOD_SEQ_NO));

        Call<ServicesResponse> userCall;
        if (isDeveloper) {

            userCall = apiInterface.getMaterials();

        } else {
            userCall = apiInterface.getMaterials();

        }

        userCall.enqueue(new Callback<ServicesResponse>() {
            @Override
            public void onResponse(Call<ServicesResponse> call, Response<ServicesResponse> response) {


                if (response.body() != null) {

                    if (response.body().getStatus() == true) {

                        MyUtilities.cancelAlertDialog(ServiceListActivity.this);

                        data.clear();
                        data = response.body().getData();
                        if (data.size() > 0) {
                            recyclerView.setVisibility(View.VISIBLE);
                            siviceListAdapter = new SiviceListAdapter(data, ServiceListActivity.this);
                            recyclerView.setAdapter(siviceListAdapter);

                            //brandSizeAdapter.notifyDataSetChanged();

                        } else {

                            tvNoData.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }

                    } else {
                        MyUtilities.cancelAlertDialog(ServiceListActivity.this);

                        MyUtilities.showToast(ServiceListActivity.this, MyUtilities.KAlertDialogTitleError);
                    }

                } else {

                    MyUtilities.cancelAlertDialog(ServiceListActivity.this);
                    MyUtilities.showToast(ServiceListActivity.this, MyUtilities.KAlertDialogTitleError);

                }
            }


            @Override
            public void onFailure(Call<ServicesResponse> call, Throwable t) {

                Log.e("erro", "onFailure: " + t.getMessage());
                MyUtilities.showToast(ServiceListActivity.this, MyUtilities.KAlertDialogTitleError);
                MyUtilities.cancelAlertDialog(ServiceListActivity.this);

            }
        });
    }

    public void onClickDeleteMaterial(String material) {

        deleteMaterial(material);

    }

    public void deleteMaterial(String material) {

        MyUtilities.showAlertDialog(ServiceListActivity.this, KAlertDialog.PROGRESS_TYPE, MyUtilities.KAlertDialogTitleLoding);
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
        paramObject.addProperty("MATERIAL", material);

        Call<ServicesResponseSizeBrand> userCall = apiInterface.deleteMaterial(paramObject.toString());

        userCall.enqueue(new Callback<ServicesResponseSizeBrand>() {
            @Override
            public void onResponse(Call<ServicesResponseSizeBrand> call, Response<ServicesResponseSizeBrand> response) {


                if (response.body() != null) {

                    if (response.body().getStatus() == true) {

                        MyUtilities.cancelAlertDialog(ServiceListActivity.this);
                        getMaterialsData(false);

                    } else {


                        MyUtilities.cancelAlertDialog(ServiceListActivity.this);
                        MyUtilities.showToast(ServiceListActivity.this, MyUtilities.KAlertDialogTitleSwitch);

                    }

                } else {

                    MyUtilities.cancelAlertDialog(ServiceListActivity.this);
                    MyUtilities.showToast(ServiceListActivity.this, MyUtilities.KAlertDialogTitleSwitch);

                }
            }


            @Override
            public void onFailure(Call<ServicesResponseSizeBrand> call, Throwable t) {

                MyUtilities.cancelAlertDialog(ServiceListActivity.this);
                MyUtilities.showToast(ServiceListActivity.this, MyUtilities.KAlertDialogTitleError);

            }
        });
    }

}