package narasimhaa.com.mitraservice;

import android.os.Bundle;
import android.view.View;
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
import narasimhaa.com.mitraservice.Model.MaterialFilter.DataItem;
import narasimhaa.com.mitraservice.Model.MaterialFilter.MaterialFilterResponseFull;
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

public class AllUserListAdminActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    AllUserListAdminAdapter materialRelsutsAdapter;
    List<DataItem> data = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    private Toolbar toolbar;
    private TextView tvNoData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_user_list_admin);
        toolbar = (Toolbar) findViewById(R.id.toolbar_extra);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("All Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.recycler_view);
        tvNoData = findViewById(R.id.tv_no_data);
        getData("","","");

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(AllUserListAdminActivity.this);
        recyclerView.setLayoutManager(layoutManager);

    }

    public void getData(String service,String city,String businessType) {
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

        paramObject.addProperty("USER_TYPE", SharedPreferenceUtils.getValue(AllUserListAdminActivity.this, MyUtilities.PREF_USER_TYPE));

        Call<MaterialFilterResponseFull> userCall = apiInterface.getAllUsers();

        userCall.enqueue(new Callback<MaterialFilterResponseFull>() {
            @Override
            public void onResponse(Call<MaterialFilterResponseFull> call, Response<MaterialFilterResponseFull> response) {

                if (response.body() != null) {

                    if (response.body().isStatus() == true) {


                        MyUtilities.cancelAlertDialog(AllUserListAdminActivity.this);


                        data.clear();
                        data = response.body().getData();
                        if (data.size()>0){
                            recyclerView.setVisibility(View.VISIBLE);
                            materialRelsutsAdapter = new AllUserListAdminAdapter(data, AllUserListAdminActivity.this);
                            recyclerView.setAdapter(materialRelsutsAdapter);
                            //materialRelsutsAdapter.notifyDataSetChanged();

                        }else {

                            tvNoData.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }

                    } else {
                        MyUtilities.cancelAlertDialog(AllUserListAdminActivity.this);

                        MyUtilities.showToast(AllUserListAdminActivity.this, MyUtilities.KAlertDialogTitleError);
                    }

                } else {

                    MyUtilities.cancelAlertDialog(AllUserListAdminActivity.this);
                    MyUtilities.showToast(AllUserListAdminActivity.this, MyUtilities.KAlertDialogTitleError);

                }
            }


            @Override
            public void onFailure(Call<MaterialFilterResponseFull> call, Throwable t) {

                MyUtilities.showToast(AllUserListAdminActivity.this,MyUtilities.KAlertDialogTitleError);
                MyUtilities.cancelAlertDialog(AllUserListAdminActivity.this);

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        //getData(SharedPreferenceUtils.getValue(AllUserListAdminActivity.this,MyUtilities.PREF_SERVICE_SEARCH),SharedPreferenceUtils.getValue(AllUserListAdminActivity.this,MyUtilities.PREF_CITY_SEARCH),SharedPreferenceUtils.getValue(AllUserListAdminActivity.this,MyUtilities.PREF_MATERIAL_BUSINESS_TYPE));

    }


    public void updateStatus(String email,String status) {

        MyUtilities.showAlertDialog(AllUserListAdminActivity.this, KAlertDialog.PROGRESS_TYPE, MyUtilities.KAlertDialogTitleLoding);
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
        paramObject.addProperty("EMAIL_ID", email);
        paramObject.addProperty("STATUS", status);

        Call<ServerResponse> userCall = apiInterface.changeUserStatus(paramObject.toString());
        //Call<ServerResponse> userCall = apiInterface.changeUserStatus(paramObject.toString());

        userCall.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {


                if (response.body() != null) {

                    if (response.body().getStatus() == true) {

                        MyUtilities.cancelAlertDialog(AllUserListAdminActivity.this);

                        getData("","","");
                    } else {

                        MyUtilities.cancelAlertDialog(AllUserListAdminActivity.this);
                        MyUtilities.showToast(AllUserListAdminActivity.this, MyUtilities.KAlertDialogTitleSwitch);

                    }

                } else {

                    MyUtilities.cancelAlertDialog(AllUserListAdminActivity.this);
                    MyUtilities.showToast(AllUserListAdminActivity.this, MyUtilities.KAlertDialogTitleSwitch);

                }
            }


            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                MyUtilities.cancelAlertDialog(AllUserListAdminActivity.this);
                MyUtilities.showToast(AllUserListAdminActivity.this, MyUtilities.KAlertDialogTitleError);
            }
        });
    }


    public void onClickUpdateDealerActiveState(String email,String status){{


        updateStatus(email,status);

        //MyUtilities.showToast(AllUserListAdminActivity.this,email + "   " +status);

    }

    }



}