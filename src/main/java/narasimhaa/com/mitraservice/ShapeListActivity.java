package narasimhaa.com.mitraservice;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
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

import narasimhaa.com.mitraservice.Adapater.ShapelistAdapter;
import narasimhaa.com.mitraservice.Adapater.SubcategoryAdapter;
import narasimhaa.com.mitraservice.Model.MaterialDevelopers.ServicesDataItemSize;
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

public class ShapeListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ShapelistAdapter shapelistAdapter;
    List<ServicesDataItemSize> data = new ArrayList<>();

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
        getSupportActionBar().setTitle("Shape List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.recycler_view);

        tvNoData = findViewById(R.id.tv_no_data);
        getShapesList(false);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(ShapeListActivity.this);
        recyclerView.setLayoutManager(layoutManager);
    }



    public void getShapesList(boolean isDeveloper) {
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
        paramObject.addProperty("BOD_SEQ_NO", SharedPreferenceUtils.getValue(ShapeListActivity.this, MyUtilities.PREF_BOD_SEQ_NO));

        Call<ServicesResponseSizeBrand> userCall;
        if (isDeveloper) {

            userCall = apiInterface.getShapes();

        } else {
            userCall = apiInterface.getShapes();

        }

        userCall.enqueue(new Callback<ServicesResponseSizeBrand>() {
            @Override
            public void onResponse(Call<ServicesResponseSizeBrand> call, Response<ServicesResponseSizeBrand> response) {

                if (response.body() != null) {

                    if (response.body().getStatus() == true) {

                        MyUtilities.cancelAlertDialog(ShapeListActivity.this);

                        data.clear();
                        data = response.body().getData();
                        if (data.size() > 0) {
                            recyclerView.setVisibility(View.VISIBLE);
                            shapelistAdapter = new ShapelistAdapter(data, ShapeListActivity.this);
                            recyclerView.setAdapter(shapelistAdapter);

                            //brandSizeAdapter.notifyDataSetChanged();

                        } else {

                            tvNoData.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }

                    } else {
                        MyUtilities.cancelAlertDialog(ShapeListActivity.this);

                        MyUtilities.showToast(ShapeListActivity.this, MyUtilities.KAlertDialogTitleError);
                    }

                } else {

                    MyUtilities.cancelAlertDialog(ShapeListActivity.this);
                    MyUtilities.showToast(ShapeListActivity.this, MyUtilities.KAlertDialogTitleError);

                }
            }


            @Override
            public void onFailure(Call<ServicesResponseSizeBrand> call, Throwable t) {

                Log.e("erro", "onFailure: " + t.getMessage());
                MyUtilities.showToast(ShapeListActivity.this, MyUtilities.KAlertDialogTitleError);
                MyUtilities.cancelAlertDialog(ShapeListActivity.this);

            }
        });
    }

    public void onClickDeleteShape(String material) {

        deleteShape(material);

    }

    public void deleteShape(String material) {

        MyUtilities.showAlertDialog(ShapeListActivity.this, KAlertDialog.PROGRESS_TYPE, MyUtilities.KAlertDialogTitleLoding);
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
        paramObject.addProperty("HEIGHT", material);

        Call<ServicesResponseSizeBrand> userCall = apiInterface.deleteShape(paramObject.toString());

        userCall.enqueue(new Callback<ServicesResponseSizeBrand>() {
            @Override
            public void onResponse(Call<ServicesResponseSizeBrand> call, Response<ServicesResponseSizeBrand> response) {


                if (response.body() != null) {

                    if (response.body().getStatus() == true) {

                        MyUtilities.cancelAlertDialog(ShapeListActivity.this);
                        getShapesList(false);

                    } else {


                        MyUtilities.cancelAlertDialog(ShapeListActivity.this);
                        MyUtilities.showToast(ShapeListActivity.this, MyUtilities.KAlertDialogTitleSwitch);

                    }

                } else {

                    MyUtilities.cancelAlertDialog(ShapeListActivity.this);
                    MyUtilities.showToast(ShapeListActivity.this, MyUtilities.KAlertDialogTitleSwitch);

                }
            }


            @Override
            public void onFailure(Call<ServicesResponseSizeBrand> call, Throwable t) {

                MyUtilities.cancelAlertDialog(ShapeListActivity.this);
                MyUtilities.showToast(ShapeListActivity.this, MyUtilities.KAlertDialogTitleError);

            }
        });
    }

}