package narasimhaa.com.mitraservice;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.kinda.alert.KAlertDialog;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

public class AllUserListAdminActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    RecyclerView recyclerView;
    AllUserListAdminAdapter materialRelsutsAdapter;
    List<DataItem> data = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    private Toolbar toolbar;
    private TextView tvNoData;
    private AutoCompleteTextView ll_user_type;
    private EditText date;

    final Calendar myCalendar = Calendar.getInstance();
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
        initViews();
        try {
            getData("","","");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(AllUserListAdminActivity.this);
        recyclerView.setLayoutManager(layoutManager);

    }

    public void getData(String service,String city,String businessType) throws ParseException {
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

        String sDate1=date.getText().toString();
        Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(sDate1);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(date1);

        Call<MaterialFilterResponseFull> userCall;
        if (ll_user_type.getText().toString().equalsIgnoreCase("Retailer")){

            userCall = apiInterface.getAllUsers("Consumer",strDate);
        }else {
            userCall = apiInterface.getAllUsers(ll_user_type.getText().toString(),strDate);
        }

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
                            Log.e("Size is ", "onResponse: "+data.size());
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


    public void updateStatus(String email,String ll_user_type) {

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
        paramObject.addProperty("STATUS", ll_user_type);

        Call<ServerResponse> userCall = apiInterface.changeUserStatus(paramObject.toString());
        //Call<ServerResponse> userCall = apiInterface.changeUserStatus(paramObject.toString());

        userCall.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {


                if (response.body() != null) {

                    if (response.body().getStatus() == true) {

                        MyUtilities.cancelAlertDialog(AllUserListAdminActivity.this);

                        try {
                            getData("","","");
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
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


    public void onClickUpdateDealerActiveState(String email,String ll_user_type){{


        updateStatus(email,ll_user_type);

        //MyUtilities.showToast(AllUserListAdminActivity.this,email + "   " +ll_user_type);

    }

    }

    private void initViews() {
        ll_user_type = findViewById(R.id.ll_user_type);
        date = findViewById(R.id.date);
        ll_user_type.setText(getResources().getStringArray(R.array.user_types_array)[0]);
        date.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AllUserListAdminActivity.this, AllUserListAdminActivity.this, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        ll_user_type.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.user_types_array)));

       ll_user_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ll_user_type.getAdapter() != null) {
                    ll_user_type.showDropDown();
                }
            }
        });
        ll_user_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    getData("","","");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        date.setText(datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth());
        try {
            getData("","","");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}