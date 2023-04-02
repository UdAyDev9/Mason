package narasimhaa.com.mitraservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.kinda.alert.KAlertDialog;

import java.util.ArrayList;
import java.util.List;

import narasimhaa.com.mitraservice.Adapater.SizeBrandsAdapter;
import narasimhaa.com.mitraservice.Model.MaterialDevelopers.ServicesDataItemSize;
import narasimhaa.com.mitraservice.Model.MaterialDevelopers.ServicesResponseSizeBrand;
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

public class SizesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
        SizeBrandsAdapter materialRelsutsAdapter;
        List<ServicesDataItemSize> data = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager;
        private Toolbar toolbar;
        private TextView tvNoData;



        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_sizes);
            toolbar = (Toolbar) findViewById(R.id.toolbar_extra);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Sizes");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            recyclerView = findViewById(R.id.recycler_view);
            tvNoData = findViewById(R.id.tv_no_data);

            getSizesData(false);
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(narasimhaa.com.mitraservice.SizesActivity.this);
            recyclerView.setLayoutManager(layoutManager);
        }

        public void getSizesData(boolean isDeveloper) {
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

            paramObject.addProperty("BOD_SEQ_NO", SharedPreferenceUtils.getValue(narasimhaa.com.mitraservice.SizesActivity.this, MyUtilities.PREF_BOD_SEQ_NO));

            Call<ServicesResponseSizeBrand> userCall;
            if (isDeveloper) {

                userCall = apiInterface.getServiceTypeSize();

            } else {
                userCall = apiInterface.getServiceTypeSize();

            }

            userCall.enqueue(new Callback<ServicesResponseSizeBrand>() {
                @Override
                public void onResponse(Call<ServicesResponseSizeBrand> call, Response<ServicesResponseSizeBrand> response) {


                    if (response.body() != null) {

                        if (response.body().getStatus() == true) {

                            MyUtilities.cancelAlertDialog(narasimhaa.com.mitraservice.SizesActivity.this);
                            data.clear();
                            data = response.body().getData();
                            if (data.size()>0){
                                recyclerView.setVisibility(View.VISIBLE);
                                materialRelsutsAdapter = new SizeBrandsAdapter(data, SizesActivity.this,true);
                                recyclerView.setAdapter(materialRelsutsAdapter);
                                //materialRelsutsAdapter.notifyDataSetChanged();

                            }else {

                                tvNoData.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            }
                        } else {
                            MyUtilities.cancelAlertDialog(narasimhaa.com.mitraservice.SizesActivity.this);

                            MyUtilities.showToast(narasimhaa.com.mitraservice.SizesActivity.this, MyUtilities.KAlertDialogTitleError);
                        }

                    } else {

                        MyUtilities.cancelAlertDialog(narasimhaa.com.mitraservice.SizesActivity.this);
                        MyUtilities.showToast(narasimhaa.com.mitraservice.SizesActivity.this, MyUtilities.KAlertDialogTitleError);

                    }
                }


                @Override
                public void onFailure(Call<ServicesResponseSizeBrand> call, Throwable t) {

                    Log.e("erro", "onFailure: " + t.getMessage());
                    MyUtilities.showToast(narasimhaa.com.mitraservice.SizesActivity.this, MyUtilities.KAlertDialogTitleError);
                    MyUtilities.cancelAlertDialog(narasimhaa.com.mitraservice.SizesActivity.this);

                }
            });
        }
    public void onClickDeleteSize(String size) {

        deleteSize(size);

    }

    public void deleteSize(String size) {

        MyUtilities.showAlertDialog(SizesActivity.this, KAlertDialog.PROGRESS_TYPE, MyUtilities.KAlertDialogTitleLoding);
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
        paramObject.addProperty("SIZE", size);

        Call<ServicesResponseSizeBrand> userCall = apiInterface.deleteSize(paramObject.toString());

        userCall.enqueue(new Callback<ServicesResponseSizeBrand>() {
            @Override
            public void onResponse(Call<ServicesResponseSizeBrand> call, Response<ServicesResponseSizeBrand> response) {


                if (response.body() != null) {

                    if (response.body().getStatus() == true) {

                        MyUtilities.cancelAlertDialog(SizesActivity.this);
//                        getStaffImages();
                        getSizesData(false);


                    } else {


                        MyUtilities.cancelAlertDialog(SizesActivity.this);
                        MyUtilities.showToast(SizesActivity.this, MyUtilities.KAlertDialogTitleSwitch);

                    }

                } else {

                    MyUtilities.cancelAlertDialog(SizesActivity.this);
                    MyUtilities.showToast(SizesActivity.this, MyUtilities.KAlertDialogTitleSwitch);

                }
            }


            @Override
            public void onFailure(Call<ServicesResponseSizeBrand> call, Throwable t) {

                MyUtilities.cancelAlertDialog(SizesActivity.this);
                MyUtilities.showToast(SizesActivity.this, MyUtilities.KAlertDialogTitleError);

            }
        });
    }

    }