package narasimhaa.com.mitraservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.kinda.alert.KAlertDialog;


import java.util.ArrayList;
import java.util.List;

import narasimhaa.com.mitraservice.Adapater.ImagesAdapter;
import narasimhaa.com.mitraservice.Model.ImagesResponse;
import narasimhaa.com.mitraservice.Model.PathsItem;
import narasimhaa.com.mitraservice.Utility.MyUtilities;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ImagesViewActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button btnImagesUpload;
    RecyclerView recyclerViewImages;
    RecyclerView.LayoutManager layoutManager;
    ImagesAdapter imagesAdapter;
    List<PathsItem> pathsItemList = new ArrayList<>();
    ImageView imgPreview;
    private TextView tvNoData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images_view);
        btnImagesUpload = findViewById(R.id.bt_images);
        imgPreview = findViewById(R.id.img_preview);
        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar_extra);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Pop Images");
        tvNoData = findViewById(R.id.tv_no_data);

        if (getIntent().getExtras()!=null){

            if (getIntent().getExtras().getString(MyUtilities.PREF_EMAIL)!=null){

                getStaffImages(getIntent().getExtras().getString(MyUtilities.PREF_EMAIL));

            }
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerViewImages = findViewById(R.id.recycler_view_images);
        layoutManager = new LinearLayoutManager(ImagesViewActivity.this);
        recyclerViewImages.setLayoutManager(layoutManager);

    }
    private void getStaffImages(String email) {

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
        MyUtilities.showAlertDialog(ImagesViewActivity.this, KAlertDialog.PROGRESS_TYPE,MyUtilities.KAlertDialogTitleLoding);

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        JsonObject paramObject = new JsonObject();

        Call<ImagesResponse> userCall = apiInterface.getImagesResponse(email);

        userCall.enqueue(new Callback<ImagesResponse>() {
            @Override
            public void onResponse(Call<ImagesResponse> call, Response<ImagesResponse> response) {


                if (response.body() != null) {

                    if (response.body().isStatus() == true) {
                        MyUtilities.cancelAlertDialog(ImagesViewActivity.this);

                        pathsItemList = response.body().getData().getPaths();

                        if (pathsItemList.size()>0){
                            imagesAdapter = new ImagesAdapter(pathsItemList, ImagesViewActivity.this,true);
                            recyclerViewImages.setAdapter(imagesAdapter);
                        }else {

                            tvNoData.setVisibility(View.VISIBLE);
                            recyclerViewImages.setVisibility(View.GONE);

                        }

                    } else {
                        MyUtilities.cancelAlertDialog(ImagesViewActivity.this);

                        MyUtilities.showToast(ImagesViewActivity.this, MyUtilities.KAlertDialogTitleError);
                    }


                    //Log.i("Baba", "Res ponse is " + homePageBeanData.toString());
                    //  Toast.makeText(Home.this,full,Toast.LENGTH_LONG).show();


                    //  tvAddress.setText(name);
                    //Log.e("responsee result", "onResponse: " + name + phone + city + homePageBeanData.getSTATUS());

                } else {

                    MyUtilities.cancelAlertDialog(ImagesViewActivity.this);
                    MyUtilities.showToast(ImagesViewActivity.this, MyUtilities.KAlertDialogTitleError);

                }
            }


            @Override
            public void onFailure(Call<ImagesResponse> call, Throwable t) {

                MyUtilities.showToast(ImagesViewActivity.this,MyUtilities.KAlertDialogTitleError);
                MyUtilities.cancelAlertDialog(ImagesViewActivity.this);

            }
        });
    }

}