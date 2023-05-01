package narasimhaa.com.mitraservice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.kinda.alert.KAlertDialog;

import narasimhaa.com.mitraservice.Model.ServerResponse;
import narasimhaa.com.mitraservice.Utility.MyUtilities;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class AddOthersActivity extends AppCompatActivity {

    private EditText et_perimeter, et_length, et_weight, et_thickness;
    private Button btnSubmitPerimeter, btnSubmitLength, btnSubmitWeight, btnSubmitThickness;
    private String strPerimeter = "", strLength = "", strWeight = "", strThickness = "";
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perimeter_length_weight);
        et_perimeter = (EditText) findViewById(R.id.et_perimeter);
        et_length = (EditText) findViewById(R.id.et_length);
        et_weight = (EditText) findViewById(R.id.et_weight);
        et_thickness = (EditText) findViewById(R.id.et_thickness);

        btnSubmitPerimeter = (Button) findViewById(R.id.bt_submit_perimeter);
        btnSubmitLength = (Button) findViewById(R.id.bt_submit_length);
        btnSubmitWeight = (Button) findViewById(R.id.bt_submit_weight);
        btnSubmitThickness = (Button) findViewById(R.id.bt_submit_thickness);
        toolbar = (Toolbar) findViewById(R.id.toolbar_extra);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_add_others);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnSubmitPerimeter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                strPerimeter = et_perimeter.getText().toString();

                addPerimeter(strPerimeter);
            }


        });

        btnSubmitLength.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                strLength = et_length.getText().toString();

                addLength(strLength);
            }


        });

        btnSubmitWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                strWeight = et_weight.getText().toString();

                addWeight(strWeight);
            }


        });


        btnSubmitThickness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                strThickness = et_thickness.getText().toString();

                addThickness(strWeight);
            }


        });


    }

    private void addPerimeter(String servicestr) {

        MyUtilities.showAlertDialog(AddOthersActivity.this, KAlertDialog.PROGRESS_TYPE, MyUtilities.KAlertDialogTitleLoding);

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

            JsonObject jsonReg = new JsonObject();

            jsonReg.addProperty("PERIMETER", servicestr);


            Call<ServerResponse> userCall;


            userCall = apiInterface.addPerimeter(jsonReg.toString());

            userCall.enqueue(new Callback<ServerResponse>() {
                @Override
                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {


                    if (response.body() != null) {


                        if (response.body().getStatus() == true) {

                            //SharedPreferenceUtils.setValue(AddSizesAndBrandsActivity.this, MyUtilities.PREF_SER_PER_SEQ_ID, response.body().getData().getSER_PER_SEQ_ID());

                            MyUtilities.showToast(AddOthersActivity.this, "Perimeter added successfully!!!");

                            MyUtilities.cancelAlertDialog(AddOthersActivity.this);

                            Intent intent = new Intent(AddOthersActivity.this, DashBoardActivity.class);
                            startActivity(intent);
                            finish();

                        } else {

                            MyUtilities.showToast(AddOthersActivity.this, response.body().getMsg().toString());
                            MyUtilities.cancelAlertDialog(AddOthersActivity.this);

                        }


                    } else {

                        MyUtilities.cancelAlertDialog(AddOthersActivity.this);

                        MyUtilities.showToast(AddOthersActivity.this, "Something went wrong! Please try later");

                    }


                }

                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {
                    MyUtilities.showToast(AddOthersActivity.this, MyUtilities.KAlertDialogTitleError);
                    MyUtilities.cancelAlertDialog(AddOthersActivity.this);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            MyUtilities.cancelAlertDialog(AddOthersActivity.this);

        }
    }

    private void addLength(String servicestr) {

        MyUtilities.showAlertDialog(AddOthersActivity.this, KAlertDialog.PROGRESS_TYPE, MyUtilities.KAlertDialogTitleLoding);

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

            JsonObject jsonReg = new JsonObject();

            jsonReg.addProperty("LENGTH", servicestr);


            Call<ServerResponse> userCall;


            userCall = apiInterface.addLength(jsonReg.toString());

            userCall.enqueue(new Callback<ServerResponse>() {
                @Override
                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {


                    if (response.body() != null) {


                        if (response.body().getStatus() == true) {

                            //SharedPreferenceUtils.setValue(AddSizesAndBrandsActivity.this, MyUtilities.PREF_SER_PER_SEQ_ID, response.body().getData().getSER_PER_SEQ_ID());

                            MyUtilities.showToast(AddOthersActivity.this, "Length added successfully!!!");

                            MyUtilities.cancelAlertDialog(AddOthersActivity.this);

                            Intent intent = new Intent(AddOthersActivity.this, DashBoardActivity.class);
                            startActivity(intent);
                            finish();

                        } else {

                            MyUtilities.showToast(AddOthersActivity.this, response.body().getMsg().toString());
                            MyUtilities.cancelAlertDialog(AddOthersActivity.this);

                        }


                    } else {

                        MyUtilities.cancelAlertDialog(AddOthersActivity.this);

                        MyUtilities.showToast(AddOthersActivity.this, "Something went wrong! Please try later");

                    }


                }

                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {
                    MyUtilities.showToast(AddOthersActivity.this, MyUtilities.KAlertDialogTitleError);
                    MyUtilities.cancelAlertDialog(AddOthersActivity.this);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            MyUtilities.cancelAlertDialog(AddOthersActivity.this);

        }
    }

    private void addWeight(String servicestr) {

        MyUtilities.showAlertDialog(AddOthersActivity.this, KAlertDialog.PROGRESS_TYPE, MyUtilities.KAlertDialogTitleLoding);

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

            JsonObject jsonReg = new JsonObject();

            jsonReg.addProperty("WEIGHT", servicestr);


            Call<ServerResponse> userCall;


            userCall = apiInterface.addWeight(jsonReg.toString());

            userCall.enqueue(new Callback<ServerResponse>() {
                @Override
                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {


                    if (response.body() != null) {


                        if (response.body().getStatus() == true) {

                            //SharedPreferenceUtils.setValue(AddSizesAndBrandsActivity.this, MyUtilities.PREF_SER_PER_SEQ_ID, response.body().getData().getSER_PER_SEQ_ID());

                            MyUtilities.showToast(AddOthersActivity.this, "Weight added successfully!!!");

                            MyUtilities.cancelAlertDialog(AddOthersActivity.this);

                            Intent intent = new Intent(AddOthersActivity.this, DashBoardActivity.class);
                            startActivity(intent);
                            finish();

                        } else {

                            MyUtilities.showToast(AddOthersActivity.this, response.body().getMsg().toString());
                            MyUtilities.cancelAlertDialog(AddOthersActivity.this);

                        }


                    } else {

                        MyUtilities.cancelAlertDialog(AddOthersActivity.this);

                        MyUtilities.showToast(AddOthersActivity.this, "Something went wrong! Please try later");

                    }


                }

                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {
                    MyUtilities.showToast(AddOthersActivity.this, MyUtilities.KAlertDialogTitleError);
                    MyUtilities.cancelAlertDialog(AddOthersActivity.this);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            MyUtilities.cancelAlertDialog(AddOthersActivity.this);

        }
    }

    private void addThickness(String servicestr) {

        MyUtilities.showAlertDialog(AddOthersActivity.this, KAlertDialog.PROGRESS_TYPE, MyUtilities.KAlertDialogTitleLoding);

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

            JsonObject jsonReg = new JsonObject();

            jsonReg.addProperty("THICKNESS", servicestr);


            Call<ServerResponse> userCall;


            userCall = apiInterface.addThickness(jsonReg.toString());

            userCall.enqueue(new Callback<ServerResponse>() {
                @Override
                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {


                    if (response.body() != null) {


                        if (response.body().getStatus() == true) {

                            //SharedPreferenceUtils.setValue(AddSizesAndBrandsActivity.this, MyUtilities.PREF_SER_PER_SEQ_ID, response.body().getData().getSER_PER_SEQ_ID());

                            MyUtilities.showToast(AddOthersActivity.this, "Thickness added successfully!!!");

                            MyUtilities.cancelAlertDialog(AddOthersActivity.this);

                            Intent intent = new Intent(AddOthersActivity.this, DashBoardActivity.class);
                            startActivity(intent);
                            finish();

                        } else {

                            MyUtilities.showToast(AddOthersActivity.this, response.body().getMsg().toString());
                            MyUtilities.cancelAlertDialog(AddOthersActivity.this);

                        }


                    } else {

                        MyUtilities.cancelAlertDialog(AddOthersActivity.this);

                        MyUtilities.showToast(AddOthersActivity.this, "Something went wrong! Please try later");

                    }


                }

                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {
                    MyUtilities.showToast(AddOthersActivity.this, MyUtilities.KAlertDialogTitleError);
                    MyUtilities.cancelAlertDialog(AddOthersActivity.this);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            MyUtilities.cancelAlertDialog(AddOthersActivity.this);
        }
    }
}