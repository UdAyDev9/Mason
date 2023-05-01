package narasimhaa.com.mitraservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;


import org.w3c.dom.Text;

import java.util.List;

import narasimhaa.com.mitraservice.Adapater.AllProductAdapter;
import narasimhaa.com.mitraservice.Model.MaterialDevelopers.MaterialDevelopersServerResponse;
import narasimhaa.com.mitraservice.Model.MaterialDevelopers.DataItem;
import narasimhaa.com.mitraservice.Utility.MyUtilities;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class AllProductsListActivity extends AppCompatActivity {

    AllProductsListActivity context;

    SharedPreferences sharedPreferences;
    private Button updateBtn;
    private Toolbar toolbar;
    private ImageView imgUpdateCert;
    private List<DataItem> data;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    AllProductAdapter allProductDetailsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_products_list);
        sharedPreferences = getSharedPreferences("ServicePREFERENCES",MODE_PRIVATE);
        recyclerView = findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(AllProductsListActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        String uri = sharedPreferences.getString("uri_imagee","");
        Log.d("uri", "onCreate: "+uri);

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


                        MyUtilities.cancelAlertDialog(AllProductsListActivity.this);


                        data = response.body().getData();
                        if (data.size()>0){
                            recyclerView.setVisibility(View.VISIBLE);
                            allProductDetailsAdapter = new AllProductAdapter(data,AllProductsListActivity.this);
                            recyclerView.setAdapter(allProductDetailsAdapter);

                        }else {

                            //tvNoData.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);

                        }

                    } else {
                        MyUtilities.cancelAlertDialog(AllProductsListActivity.this);

                        MyUtilities.showToast(AllProductsListActivity.this, MyUtilities.TOAST_MATERIAL_NOT_ADDED_YET);
                    }

                } else {

                    MyUtilities.cancelAlertDialog(AllProductsListActivity.this);
                    MyUtilities.showToast(AllProductsListActivity.this, MyUtilities.KAlertDialogTitleError);

                }
            }


            @Override
            public void onFailure(Call<MaterialDevelopersServerResponse> call, Throwable t) {

                MyUtilities.showToast(AllProductsListActivity.this,MyUtilities.KAlertDialogTitleError);
                MyUtilities.cancelAlertDialog(AllProductsListActivity.this);

            }
        });
    }


}