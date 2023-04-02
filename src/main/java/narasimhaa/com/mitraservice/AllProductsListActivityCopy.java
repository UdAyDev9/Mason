package narasimhaa.com.mitraservice;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kinda.alert.KAlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import narasimhaa.com.mitraservice.Adapater.AllProductAdapter;
import narasimhaa.com.mitraservice.Adapater.AllProductAdapterCopy;
import narasimhaa.com.mitraservice.Model.MaterialDevelopers.DataItem;
import narasimhaa.com.mitraservice.Model.MaterialDevelopers.MaterialDevelopersServerResponse;
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

public class AllProductsListActivityCopy extends AppCompatActivity {

    AllProductsListActivityCopy context;

    SharedPreferences sharedPreferences;
    private Button btnPlaceOrder;
    private Toolbar toolbar;
    private ImageView imgUpdateCert;
    private List<DataItem> data;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    AllProductAdapterCopy allProductDetailsAdapter;
    TextView tvTotalPrice;
    private List<Double> selectedPrices = new ArrayList<>();
    private List<Integer> selectedQuantities = new ArrayList<>();
    double totalPrice = 0;
    String deliveryType = "Door Delivery";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_products_list);
        tvTotalPrice = findViewById(R.id.tvTotalValue);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar_extra);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Steel List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sharedPreferences = getSharedPreferences("ServicePREFERENCES", MODE_PRIVATE);
        recyclerView = findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(AllProductsListActivityCopy.this);
        recyclerView.setLayoutManager(layoutManager);
        String uri = sharedPreferences.getString("uri_imagee", "");
        Log.d("uri", "onCreate: " + uri);
        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeliveryTypeDialog();
            }
        });

        context = this;

        if (getIntent().getExtras() != null) {

            if (getIntent().getExtras().getString(MyUtilities.PREF_EMAIL) != null) {

                getData(getIntent().getExtras().getString(MyUtilities.PREF_EMAIL));

            }
        }

    }

    public void getData(String email) {
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

        //paramObject.addProperty("EMAIL_ID", );

        Call<MaterialDevelopersServerResponse> userCall = apiInterface.getMaterialDevelopersList(email);

        userCall.enqueue(new Callback<MaterialDevelopersServerResponse>() {
            @Override
            public void onResponse(Call<MaterialDevelopersServerResponse> call, Response<MaterialDevelopersServerResponse> response) {

                if (response.body() != null) {

                    if (response.body().isStatus() == true) {


                        MyUtilities.cancelAlertDialog(AllProductsListActivityCopy.this);


                        data = response.body().getData();
                        if (data.size() > 0) {
                            recyclerView.setVisibility(View.VISIBLE);
                            allProductDetailsAdapter = new AllProductAdapterCopy(AllProductsListActivityCopy.this, data);
                            recyclerView.setAdapter(allProductDetailsAdapter);

                        } else {

                            //tvNoData.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);

                        }

                    } else {
                        MyUtilities.cancelAlertDialog(AllProductsListActivityCopy.this);

                        MyUtilities.showToast(AllProductsListActivityCopy.this, MyUtilities.TOAST_MATERIAL_NOT_ADDED_YET);
                    }

                } else {

                    MyUtilities.cancelAlertDialog(AllProductsListActivityCopy.this);
                    MyUtilities.showToast(AllProductsListActivityCopy.this, MyUtilities.KAlertDialogTitleError);

                }
            }


            @Override
            public void onFailure(Call<MaterialDevelopersServerResponse> call, Throwable t) {

                MyUtilities.showToast(AllProductsListActivityCopy.this, MyUtilities.KAlertDialogTitleError);
                MyUtilities.cancelAlertDialog(AllProductsListActivityCopy.this);

            }
        });
    }

    public void updateTotalPrice() {
        double total = 0;

        for (DataItem product : data) {
            if (product.isSelected()) {
                total += Double.parseDouble(product.getPRICE()) * product.getQuantity();
            }
        }
        totalPrice = total;
        tvTotalPrice.setText(String.format("Total Price: Rs. %.2f", total));
    }

    private String getSelectedItemsJson(String deliveryType) {

        final JSONObject jsonObject = new JSONObject();
        try {
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < data.size(); i++) {
                JSONObject jsonObject1 = new JSONObject();
                DataItem productlistdata1 = data.get(i);
                float dis;
                jsonObject1.put("EMAIL_ID", SharedPreferenceUtils.getValue(AllProductsListActivityCopy.this, MyUtilities.PREF_EMAIL));
                jsonObject1.put("RET_EMAIL_ID", productlistdata1.getEMAILID());
                jsonObject1.put("S_NAME", productlistdata1.getSERVICETYPE());
                jsonObject1.put("B_NAME", productlistdata1.getBUSINESSNAME());
                jsonObject1.put("BRAND_NAME", productlistdata1.getBRANDNAME());
                jsonObject1.put("SIZE", productlistdata1.getWEIGHT());
                jsonObject1.put("N_ITEMS", productlistdata1.getWEIGHT());
                jsonObject1.put("SIZE_QNTY", "NO_IDEA");
                jsonObject1.put("QNTY", productlistdata1.getQuantity());
                jsonObject1.put("I_PRICE", productlistdata1.getPRICE());
                jsonObject1.put("T_PRICE", Double.parseDouble(productlistdata1.getPRICE()) * productlistdata1.getQuantity());
                jsonObject1.put("STATUS", "PENDING");
                jsonObject1.put("DELIVERY_TYPE", deliveryType);
                jsonArray.put(jsonObject1);
            }
            jsonObject.put("DATA", jsonArray);
            Log.d("data_json", String.valueOf(jsonObject));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return String.valueOf(jsonObject);

    }

    private void placeOrderApi(String orderJson) {

        MyUtilities.showAlertDialog(AllProductsListActivityCopy.this, KAlertDialog.PROGRESS_TYPE, MyUtilities.KAlertDialogTitleLoding);

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

        try {


            Call<ServerResponse> userCall;

            userCall = apiInterface.placeOrder(orderJson);

            userCall.enqueue(new Callback<ServerResponse>() {
                @Override
                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {

                    if (response.body() != null) {

                        if (response.body().getStatus() == true) {

                            //SharedPreferenceUtils.setValue(AddSizesAndBrandsActivity.this, MyUtilities.PREF_SER_PER_SEQ_ID, response.body().getData().getSER_PER_SEQ_ID());

                            MyUtilities.showToast(AllProductsListActivityCopy.this, "Order placed successfully!!!");

                            MyUtilities.cancelAlertDialog(AllProductsListActivityCopy.this);

                            Intent intent = new Intent(AllProductsListActivityCopy.this, DashBoardActivity.class);
                            startActivity(intent);
                            finish();

                        } else {

                            MyUtilities.showToast(AllProductsListActivityCopy.this, response.body().getMsg().toString());
                            MyUtilities.cancelAlertDialog(AllProductsListActivityCopy.this);

                        }

                    } else {

                        MyUtilities.cancelAlertDialog(AllProductsListActivityCopy.this);

                        MyUtilities.showToast(AllProductsListActivityCopy.this, "Something went wrong! Please try later");

                    }

                }

                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {
                    MyUtilities.showToast(AllProductsListActivityCopy.this, MyUtilities.KAlertDialogTitleError);
                    MyUtilities.cancelAlertDialog(AllProductsListActivityCopy.this);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            MyUtilities.cancelAlertDialog(AllProductsListActivityCopy.this);

        }
    }

    private void showDeliveryTypeDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AllProductsListActivityCopy.this);
        alertDialog.setTitle("Delivery Type");
        String[] items = {"Door Delivery", "Pick-up at Shop"};

        int checkedItem = 1;
        alertDialog.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: {
                        deliveryType = "Door Delivery";

                        break;
                    }

                    case 1: {
                        deliveryType = "Pick-up at Shop";
                        break;

                    }

                }
            }
        });

        alertDialog.setPositiveButton("Confirm Order", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                placeOrderApi(getSelectedItemsJson(deliveryType));
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }
}
