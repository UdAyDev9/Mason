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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

public class AllOrdersDetailsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    RecyclerView recyclerView;
    AllOrdersAdapter allOrdersAdapter;
    List<OrdersDataItem> data = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    private Toolbar toolbar;
    private TextView tvNoData;
    String tempUseType = "";

    private AutoCompleteTextView ll_status_type;
    private EditText date;

    final Calendar myCalendar = Calendar.getInstance();

    FloatingActionButton fab_filter;

    ImageView imgFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_orders_details);
        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar_extra);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Request Orders");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();
/*
        fab_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilterDialog(view);
            }
        });
*/

        imgFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilterDialog(view);
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(AllOrdersDetailsActivity.this);
        recyclerView.setLayoutManager(layoutManager);
    }

    public void getData(String email, String userType, boolean isFromChangeStatus) {
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
        String sDate1 = date.getText().toString();
        Date date1 = null;
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd").parse(sDate1);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(date1);

        Call<AllOrdersResponse> userCall;
        if (isFromChangeStatus) {

            userCall = apiInterface.getAllOrders(email, userType, "", strDate, "", "", "", "");

        } else {
            userCall = apiInterface.getAllOrders(email, userType, ll_status_type.getText().toString(), strDate, "", "", "", "");

        }

        userCall.enqueue(new Callback<AllOrdersResponse>() {
            @Override
            public void onResponse(Call<AllOrdersResponse> call, Response<AllOrdersResponse> response) {

                if (response.body() != null) {

                    if (response.body().isStatus() == true) {


                        //MyUtilities.cancelAlertDialog(AllOrdersDetailsActivity.this);


                        data = response.body().getData();
                        if (data.size() > 0) {
                            recyclerView.setVisibility(View.VISIBLE);
                            allOrdersAdapter = new AllOrdersAdapter(data, AllOrdersDetailsActivity.this);
                            recyclerView.setAdapter(allOrdersAdapter);

                        } else {

                            tvNoData.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }

                    } else {
                        MyUtilities.cancelAlertDialog(AllOrdersDetailsActivity.this);

                        MyUtilities.showToast(AllOrdersDetailsActivity.this, response.body().getMessage());
                        recyclerView.setVisibility(View.GONE);

                    }

                } else {

                    MyUtilities.cancelAlertDialog(AllOrdersDetailsActivity.this);
                    MyUtilities.showToast(AllOrdersDetailsActivity.this, MyUtilities.KAlertDialogTitleError);
                    recyclerView.setVisibility(View.GONE);


                }
            }


            @Override
            public void onFailure(Call<AllOrdersResponse> call, Throwable t) {

                MyUtilities.showToast(AllOrdersDetailsActivity.this, MyUtilities.KAlertDialogTitleError);
                MyUtilities.cancelAlertDialog(AllOrdersDetailsActivity.this);

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (SharedPreferenceUtils.getValue(AllOrdersDetailsActivity.this, MyUtilities.PREF_USER_TYPE).equals("Dealer")
                || SharedPreferenceUtils.getValue(AllOrdersDetailsActivity.this, MyUtilities.PREF_USER_TYPE).equals("Material Supplier")
                || SharedPreferenceUtils.getValue(AllOrdersDetailsActivity.this, MyUtilities.PREF_USER_TYPE).equals("Developer")) {
            tempUseType = "DEALER";
        } else if (SharedPreferenceUtils.getValue(AllOrdersDetailsActivity.this, MyUtilities.PREF_USER_TYPE).equals("Individual")
                || SharedPreferenceUtils.getValue(AllOrdersDetailsActivity.this, MyUtilities.PREF_USER_TYPE).equals("Retailer")) {
            tempUseType = "Retailer";
        }
        getData(SharedPreferenceUtils.getValue(AllOrdersDetailsActivity.this, MyUtilities.PREF_EMAIL), tempUseType, false);

    }

    public void onClickUpdateOrderStatus(String orderId, String status) {

        MyUtilities.showAlertDialog(AllOrdersDetailsActivity.this, KAlertDialog.PROGRESS_TYPE, MyUtilities.KAlertDialogTitleLoding);

        changeStatus(orderId, status);

    }


    public void changeStatus(String orderId, String status) {
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
        paramObject.addProperty("ORD_ID", orderId);
        paramObject.addProperty("STATUS", status);

        Call<ServerResponse> userCall = apiInterface.changeOrderStatus(String.valueOf(paramObject));

        userCall.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {

                if (response.body() != null) {

                    if (response.body().isStatus() == true) {


                        //MyUtilities.cancelAlertDialog(AllOrdersDetailsActivity.this);
                        MyUtilities.cancelAlertDialog(AllOrdersDetailsActivity.this);
                        getData(SharedPreferenceUtils.getValue(AllOrdersDetailsActivity.this, MyUtilities.PREF_EMAIL), tempUseType, true);

                    } else {
                        MyUtilities.cancelAlertDialog(AllOrdersDetailsActivity.this);

                        MyUtilities.showToast(AllOrdersDetailsActivity.this, MyUtilities.KAlertDialogTitleError);
                        MyUtilities.cancelAlertDialog(AllOrdersDetailsActivity.this);

                    }

                } else {

                    MyUtilities.cancelAlertDialog(AllOrdersDetailsActivity.this);
                    MyUtilities.showToast(AllOrdersDetailsActivity.this, MyUtilities.KAlertDialogTitleError);
                    MyUtilities.cancelAlertDialog(AllOrdersDetailsActivity.this);

                }
            }


            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                MyUtilities.showToast(AllOrdersDetailsActivity.this, MyUtilities.KAlertDialogTitleError);
                MyUtilities.cancelAlertDialog(AllOrdersDetailsActivity.this);

            }
        });
    }

    private void initViews() {
        ll_status_type = findViewById(R.id.ll_status_type);
        fab_filter = findViewById(R.id.filter_fab);
        imgFilter = findViewById(R.id.img_filter);
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
                    getData(SharedPreferenceUtils.getValue(AllOrdersDetailsActivity.this, MyUtilities.PREF_EMAIL), tempUseType, false);
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
            getData(SharedPreferenceUtils.getValue(AllOrdersDetailsActivity.this, MyUtilities.PREF_EMAIL), tempUseType, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showFilterDialog(View view) {
        // Create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.custom_alert_dialog_search, null);

        EditText dialogNameEt = customLayout.findViewById(R.id.dialogNameEt);
        EditText dialogEmailEt = customLayout.findViewById(R.id.dialogEmailEt);
        EditText dialogPasswEt = customLayout.findViewById(R.id.dialogPasswEt);
        EditText dialogBrandEt = customLayout.findViewById(R.id.dialogBrandEt);
        Button dialogSearchBtn = customLayout.findViewById(R.id.dialogSearchBtn);


        builder.setView(customLayout);
        //EditText editText = customLayout.findViewById(R.id.editText);


        AlertDialog dialog = builder.create();
        dialog.show();

        dialogSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String location = dialogNameEt.getText().toString();

                String businessName = dialogEmailEt.getText().toString();

                String productName = dialogPasswEt.getText().toString();

                String brandName = dialogBrandEt.getText().toString();

                getDataWithFilterResults(SharedPreferenceUtils.getValue(AllOrdersDetailsActivity.this, MyUtilities.PREF_EMAIL), tempUseType, brandName, businessName, productName, location);

                dialog.cancel();
            }
        });

    }

    public void getDataWithFilterResults(String email, String userType, String brand_name, String b_name, String s_name, String city) {
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
        String sDate1 = date.getText().toString();
        Date date1 = null;
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd").parse(sDate1);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(date1);

        Call<AllOrdersResponse> userCall;
        userCall = apiInterface.getAllOrders(email, userType, ll_status_type.getText().toString(), strDate, brand_name, b_name, s_name, city);

        userCall.enqueue(new Callback<AllOrdersResponse>() {
            @Override
            public void onResponse(Call<AllOrdersResponse> call, Response<AllOrdersResponse> response) {

                if (response.body() != null) {

                    if (response.body().isStatus() == true) {


                        //MyUtilities.cancelAlertDialog(AllOrdersDetailsActivity.this);


                        data = response.body().getData();
                        if (data.size() > 0) {
                            recyclerView.setVisibility(View.VISIBLE);
                            allOrdersAdapter = new AllOrdersAdapter(data, AllOrdersDetailsActivity.this);
                            recyclerView.setAdapter(allOrdersAdapter);

                        } else {

                            tvNoData.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }

                    } else {
                        MyUtilities.cancelAlertDialog(AllOrdersDetailsActivity.this);

                        MyUtilities.showToast(AllOrdersDetailsActivity.this, response.body().getMessage());
                        recyclerView.setVisibility(View.GONE);

                    }

                } else {

                    MyUtilities.cancelAlertDialog(AllOrdersDetailsActivity.this);
                    MyUtilities.showToast(AllOrdersDetailsActivity.this, MyUtilities.KAlertDialogTitleError);
                    recyclerView.setVisibility(View.GONE);


                }
            }


            @Override
            public void onFailure(Call<AllOrdersResponse> call, Throwable t) {

                MyUtilities.showToast(AllOrdersDetailsActivity.this, MyUtilities.KAlertDialogTitleError);
                MyUtilities.cancelAlertDialog(AllOrdersDetailsActivity.this);

            }
        });
    }

}