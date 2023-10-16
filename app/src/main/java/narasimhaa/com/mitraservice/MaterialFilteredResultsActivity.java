package narasimhaa.com.mitraservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.kinda.alert.KAlertDialog;

import java.util.ArrayList;
import java.util.List;

import narasimhaa.com.mitraservice.Adapater.MaterialFilteredResultsAdapter;
import narasimhaa.com.mitraservice.Model.MaterialFilter.DataItem;
import narasimhaa.com.mitraservice.Model.MaterialFilter.MaterialFilterResponseFull;
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

public class MaterialFilteredResultsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MaterialFilteredResultsAdapter materialRelsutsAdapter;
    List<DataItem> data = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    private Toolbar toolbar;
    private TextView tvNoData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_filtered_results);
        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar_extra);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Steel List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recycler_view);
        tvNoData = findViewById(R.id.tv_no_data);
        layoutManager = new LinearLayoutManager(MaterialFilteredResultsActivity.this);
        recyclerView.setLayoutManager(layoutManager);

    }

    public void getData(String service,String city,String businessType) {

        MyUtilities.showAlertDialog(MaterialFilteredResultsActivity.this, KAlertDialog.PROGRESS_TYPE, MyUtilities.KAlertDialogTitleLoding);

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

        paramObject.addProperty("BOD_SEQ_NO", SharedPreferenceUtils.getValue(MaterialFilteredResultsActivity.this, MyUtilities.PREF_BOD_SEQ_NO));

        Call<MaterialFilterResponseFull> userCall = apiInterface.getMaterialFilteredList(service,city,"");

        userCall.enqueue(new Callback<MaterialFilterResponseFull>() {
            @Override
            public void onResponse(Call<MaterialFilterResponseFull> call, Response<MaterialFilterResponseFull> response) {

                if (response.body() != null) {

                    if (response.body().isStatus() == true) {


                        MyUtilities.cancelAlertDialog(MaterialFilteredResultsActivity.this);


                        data = response.body().getData();
                        if (data.size()>0){
                            recyclerView.setVisibility(View.VISIBLE);
                            materialRelsutsAdapter = new MaterialFilteredResultsAdapter(data,MaterialFilteredResultsActivity.this);
                            recyclerView.setAdapter(materialRelsutsAdapter);

                        }else {

                            tvNoData.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }

                    } else {
                        MyUtilities.cancelAlertDialog(MaterialFilteredResultsActivity.this);

                        MyUtilities.showToast(MaterialFilteredResultsActivity.this, response.body().getMessage());

                    }

                } else {

                    MyUtilities.cancelAlertDialog(MaterialFilteredResultsActivity.this);
                    MyUtilities.showToast(MaterialFilteredResultsActivity.this, MyUtilities.KAlertDialogTitleError);

                }
            }


            @Override
            public void onFailure(Call<MaterialFilterResponseFull> call, Throwable t) {

                MyUtilities.showToast(MaterialFilteredResultsActivity.this,MyUtilities.KAlertDialogTitleError);
                MyUtilities.cancelAlertDialog(MaterialFilteredResultsActivity.this);

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (MyUtilities.isNetworkAvailable(MaterialFilteredResultsActivity.this)) {

            getData(SharedPreferenceUtils.getValue(MaterialFilteredResultsActivity.this,MyUtilities.PREF_SERVICE_SEARCH),SharedPreferenceUtils.getValue(MaterialFilteredResultsActivity.this,MyUtilities.PREF_CITY_SEARCH),SharedPreferenceUtils.getValue(MaterialFilteredResultsActivity.this,MyUtilities.PREF_MATERIAL_BUSINESS_TYPE));
        } else {
            MyUtilities.showToast(MaterialFilteredResultsActivity.this, getString(R.string.please_check_your_internet_connection));

        }              // getData(SharedPreferenceUtils.getValue(MaterialFilteredResultsActivity.this,MyUtilities.PREF_SERVICE_SEARCH),SharedPreferenceUtils.getValue(MaterialFilteredResultsActivity.this,MyUtilities.PREF_CITY_SEARCH),SharedPreferenceUtils.getValue(MaterialFilteredResultsActivity.this,MyUtilities.PREF_MATERIAL_BUSINESS_TYPE),SharedPreferenceUtils.getValue(MaterialFilteredResultsActivity.this,MyUtilities.PREF_BRAND_SEARCH));

    }
}