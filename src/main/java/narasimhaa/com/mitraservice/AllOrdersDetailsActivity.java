package narasimhaa.com.mitraservice;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import narasimhaa.com.mitraservice.Adapater.AllOrdersAdapter;
import narasimhaa.com.mitraservice.Adapater.AllOrdersAdapter;
import narasimhaa.com.mitraservice.Model.OrdersDataItem;
import narasimhaa.com.mitraservice.Model.AllOrdersResponse;
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

public class AllOrdersDetailsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    AllOrdersAdapter allOrdersAdapter;
    List<OrdersDataItem> data = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    private Toolbar toolbar;
    private TextView tvNoData;
    String tempUseType= "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_orders_details);
        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar_extra);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Request Orders");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(AllOrdersDetailsActivity.this);
        recyclerView.setLayoutManager(layoutManager);
    }
    public void getData(String email,String userType) {
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

        Call<AllOrdersResponse> userCall = apiInterface.getAllOrders(email,userType);

        userCall.enqueue(new Callback<AllOrdersResponse>() {
            @Override
            public void onResponse(Call<AllOrdersResponse> call, Response<AllOrdersResponse> response) {

                if (response.body() != null) {

                    if (response.body().isStatus() == true) {


                        //MyUtilities.cancelAlertDialog(AllOrdersDetailsActivity.this);


                        data = response.body().getData();
                        if (data.size()>0){
                            recyclerView.setVisibility(View.VISIBLE);
                            allOrdersAdapter = new AllOrdersAdapter(data,AllOrdersDetailsActivity.this);
                            recyclerView.setAdapter(allOrdersAdapter);

                        }else {

                            tvNoData.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }

                    } else {
                        MyUtilities.cancelAlertDialog(AllOrdersDetailsActivity.this);

                        MyUtilities.showToast(AllOrdersDetailsActivity.this, MyUtilities.KAlertDialogTitleError);
                    }

                } else {

                    MyUtilities.cancelAlertDialog(AllOrdersDetailsActivity.this);
                    MyUtilities.showToast(AllOrdersDetailsActivity.this, MyUtilities.KAlertDialogTitleError);

                }
            }


            @Override
            public void onFailure(Call<AllOrdersResponse> call, Throwable t) {

                MyUtilities.showToast(AllOrdersDetailsActivity.this,MyUtilities.KAlertDialogTitleError);
                MyUtilities.cancelAlertDialog(AllOrdersDetailsActivity.this);

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        if(SharedPreferenceUtils.getValue(AllOrdersDetailsActivity.this, MyUtilities.PREF_USER_TYPE).equals("Dealer")
        || SharedPreferenceUtils.getValue(AllOrdersDetailsActivity.this, MyUtilities.PREF_USER_TYPE).equals("Material Supplier")
        || SharedPreferenceUtils.getValue(AllOrdersDetailsActivity.this, MyUtilities.PREF_USER_TYPE).equals("Developer")){

            tempUseType= "DEALER";
        }else if (SharedPreferenceUtils.getValue(AllOrdersDetailsActivity.this, MyUtilities.PREF_USER_TYPE).equals("Individual")
                || SharedPreferenceUtils.getValue(AllOrdersDetailsActivity.this, MyUtilities.PREF_USER_TYPE).equals("Retailer")){
            tempUseType= "CONSUMER";
        }
        getData(SharedPreferenceUtils.getValue(AllOrdersDetailsActivity.this,MyUtilities.PREF_EMAIL),tempUseType);

    }

    public void onClickUpdateOrderStatus(String orderId,String status){

        changeStatus(orderId,status);

    }


    public void changeStatus(String orderId,String status) {
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
        paramObject.addProperty("ORD_ID",  orderId);
        paramObject.addProperty("STATUS", status);

        Call<ServerResponse> userCall = apiInterface.changeOrderStatus(String.valueOf(paramObject));

        userCall.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {

                if (response.body() != null) {

                    if (response.body().isStatus() == true) {


                        //MyUtilities.cancelAlertDialog(AllOrdersDetailsActivity.this);
                        getData(SharedPreferenceUtils.getValue(AllOrdersDetailsActivity.this,MyUtilities.PREF_EMAIL),tempUseType);

                    } else {
                        MyUtilities.cancelAlertDialog(AllOrdersDetailsActivity.this);

                        MyUtilities.showToast(AllOrdersDetailsActivity.this, MyUtilities.KAlertDialogTitleError);
                    }

                } else {

                    MyUtilities.cancelAlertDialog(AllOrdersDetailsActivity.this);
                    MyUtilities.showToast(AllOrdersDetailsActivity.this, MyUtilities.KAlertDialogTitleError);

                }
            }


            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                MyUtilities.showToast(AllOrdersDetailsActivity.this,MyUtilities.KAlertDialogTitleError);
                MyUtilities.cancelAlertDialog(AllOrdersDetailsActivity.this);

            }
        });
    }

}