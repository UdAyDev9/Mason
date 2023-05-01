package narasimhaa.com.mitraservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class AddSizesAndBrandsActivity extends AppCompatActivity {

    private EditText etSize, etBrand;
    private Button btnSubmit;
    private String sizeStr = "", brandStr = "";
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        etSize = (EditText) findViewById(R.id.et_size);
        etBrand = (EditText) findViewById(R.id.et_brand);
        btnSubmit = (Button) findViewById(R.id.bt_submit);
        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar_extra);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_add_material);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                sizeStr = etSize.getText().toString();
                brandStr = etBrand.getText().toString();

                if (!sizeStr.equals("") || !brandStr.equals("")){

                    addSizeAndBrand(sizeStr, brandStr);

                }else {

                    MyUtilities.showToast(AddSizesAndBrandsActivity.this, "Please enter Size or Brand");


                }


            }
        });
    }

    private void addSizeAndBrand(String strSize, String strBrand) {

        MyUtilities.showAlertDialog(AddSizesAndBrandsActivity.this, KAlertDialog.PROGRESS_TYPE, MyUtilities.KAlertDialogTitleLoding);

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

            jsonReg.addProperty("SIZE", strSize);
            jsonReg.addProperty("BRAND", strBrand);

            Call<ServerResponse> userCall;


            userCall = apiInterface.insertSizeAndBrand(jsonReg.toString());

            userCall.enqueue(new Callback<ServerResponse>() {
                @Override
                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {


                    if (response.body() != null) {


                        if (response.body().getStatus() == true) {

                            //SharedPreferenceUtils.setValue(AddSizesAndBrandsActivity.this, MyUtilities.PREF_SER_PER_SEQ_ID, response.body().getData().getSER_PER_SEQ_ID());

                            MyUtilities.showToast(AddSizesAndBrandsActivity.this, "Material added successfully!!!");

                            MyUtilities.cancelAlertDialog(AddSizesAndBrandsActivity.this);

                            Intent intent = new Intent(AddSizesAndBrandsActivity.this, DashBoardActivity.class);
                            startActivity(intent);
                            finish();

                        } else {

                            MyUtilities.showToast(AddSizesAndBrandsActivity.this, response.body().getMsg().toString());
                            MyUtilities.cancelAlertDialog(AddSizesAndBrandsActivity.this);

                        }


                    } else {

                        MyUtilities.cancelAlertDialog(AddSizesAndBrandsActivity.this);

                        MyUtilities.showToast(AddSizesAndBrandsActivity.this, "Something went wrong! Please try later");

                    }


                }

                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {
                    MyUtilities.showToast(AddSizesAndBrandsActivity.this, MyUtilities.KAlertDialogTitleError);
                    MyUtilities.cancelAlertDialog(AddSizesAndBrandsActivity.this);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            MyUtilities.cancelAlertDialog(AddSizesAndBrandsActivity.this);

        }
    }
}