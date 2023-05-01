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

import java.util.ArrayList;
import java.util.List;

import narasimhaa.com.mitraservice.Adapater.ResultsAdapter;
import narasimhaa.com.mitraservice.Model.Filter.FilterDataItem;
import narasimhaa.com.mitraservice.Model.Filter.FilterResponseFull;
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

public class SearchResultsDisplayActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ResultsAdapter resultsAdapter;
    List<FilterDataItem> data = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    private Toolbar toolbar;
    private TextView tvNoData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serach_results_display);
        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar_extra);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Available");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recycler_view);
        tvNoData = findViewById(R.id.tv_no_data);
        layoutManager = new LinearLayoutManager(SearchResultsDisplayActivity.this);
        recyclerView.setLayoutManager(layoutManager);

    }

    public void getData(String service,String city,String brand) {
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

        paramObject.addProperty("BOD_SEQ_NO", SharedPreferenceUtils.getValue(SearchResultsDisplayActivity.this, MyUtilities.PREF_BOD_SEQ_NO));

        Call<FilterResponseFull> userCall = apiInterface.getFilteredList(service,city);
        //Call<FilterResponseFull> userCall = apiInterface.getFilteredList(service,city,brand);

        userCall.enqueue(new Callback<FilterResponseFull>() {
            @Override
            public void onResponse(Call<FilterResponseFull> call, Response<FilterResponseFull> response) {

                if (response.body() != null) {

                    if (Boolean.parseBoolean(response.body().getStatus()) == true) {


                        MyUtilities.cancelAlertDialog(SearchResultsDisplayActivity.this);


                        data = response.body().getData().getDATA();
                        if (data.size()>0){
                            recyclerView.setVisibility(View.VISIBLE);
                            resultsAdapter = new ResultsAdapter(data,SearchResultsDisplayActivity.this);
                            recyclerView.setAdapter(resultsAdapter);

                        }else {

                            tvNoData.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }

                    } else {
                        MyUtilities.cancelAlertDialog(SearchResultsDisplayActivity.this);

                        MyUtilities.showToast(SearchResultsDisplayActivity.this, MyUtilities.KAlertDialogTitleError);
                    }

                } else {

                    MyUtilities.cancelAlertDialog(SearchResultsDisplayActivity.this);
                    MyUtilities.showToast(SearchResultsDisplayActivity.this, MyUtilities.KAlertDialogTitleError);

                }
            }


            @Override
            public void onFailure(Call<FilterResponseFull> call, Throwable t) {

                MyUtilities.showToast(SearchResultsDisplayActivity.this,MyUtilities.KAlertDialogTitleError);
                MyUtilities.cancelAlertDialog(SearchResultsDisplayActivity.this);

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        getData(SharedPreferenceUtils.getValue(SearchResultsDisplayActivity.this,MyUtilities.PREF_SERVICE_SEARCH),SharedPreferenceUtils.getValue(SearchResultsDisplayActivity.this,MyUtilities.PREF_CITY_SEARCH),"");
        //getData(SharedPreferenceUtils.getValue(SearchResultsDisplayActivity.this,MyUtilities.PREF_SERVICE_SEARCH),SharedPreferenceUtils.getValue(SearchResultsDisplayActivity.this,MyUtilities.PREF_CITY_SEARCH),SharedPreferenceUtils.getValue(SearchResultsDisplayActivity.this,MyUtilities.PREF_BRAND_SEARCH));


    }
}