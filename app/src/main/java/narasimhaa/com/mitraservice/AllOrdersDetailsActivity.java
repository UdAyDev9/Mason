package narasimhaa.com.mitraservice;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

public class AllOrdersDetailsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    RecyclerView recyclerView;
    AllOrdersAdapter allOrdersAdapter;
    List<OrdersDataItem> data = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    private Toolbar toolbar;
    private TextView tvNoData;
    String tempUseType= "";

    private AutoCompleteTextView ll_status_type;
    private EditText date;

    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_orders_details);
        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar_extra);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Request Orders");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();

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
        String sDate1=date.getText().toString();
        Date date1= null;
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd").parse(sDate1);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(date1);

        Call<AllOrdersResponse> userCall;
        userCall = apiInterface.getAllOrders(email,userType,ll_status_type.getText().toString(),strDate);

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
    private void initViews() {
        ll_status_type = findViewById(R.id.ll_status_type);
        date = findViewById(R.id.date);
        ll_status_type.setText(getResources().getStringArray(R.array.order_status_array)[0]);
        date.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AllOrdersDetailsActivity.this, AllOrdersDetailsActivity.this, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        ll_status_type.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.order_status_array)));

        ll_status_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ll_status_type.getAdapter() != null) {
                    ll_status_type.showDropDown();
                }
            }
        });
        ll_status_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    getData("","");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        date.setText(datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth());
        try {
            getData("","");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}