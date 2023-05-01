package narasimhaa.com.mitraservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.kinda.alert.KAlertDialog;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import narasimhaa.com.mitraservice.Adapater.ImagesAdapter;
import narasimhaa.com.mitraservice.Model.PathsItem;
import narasimhaa.com.mitraservice.Model.ImagesResponse;
import narasimhaa.com.mitraservice.Model.ServerResponse;
import narasimhaa.com.mitraservice.Utility.MyUtilities;
import narasimhaa.com.mitraservice.Utility.SharedPreferenceUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ImagesActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button btnImagesUpload;
    RecyclerView recyclerViewImages;
    RecyclerView.LayoutManager layoutManager;
    ImagesAdapter imagesAdapter;
    List<PathsItem> pathsItemList = new ArrayList<>();
    ImageView imgPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
        btnImagesUpload = findViewById(R.id.bt_images);
        imgPreview = findViewById(R.id.img_preview);
        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar_extra);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Images");
        getStaffImages();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerViewImages = findViewById(R.id.recycler_view_images);
        layoutManager = new LinearLayoutManager(ImagesActivity.this);
        recyclerViewImages.setLayoutManager(layoutManager);


        btnImagesUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraUpload();
            }
        });


    }

    private void cameraUpload() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(ImagesActivity.this);
    }


    private void uploadImageToServer(File file) {

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

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part profilePhoto = MultipartBody.Part.createFormData("IMAGES[]", file.getName(), requestFile);
        RequestBody email = RequestBody.create(MediaType.parse("multipart/form-data"), SharedPreferenceUtils.getValue(ImagesActivity.this, MyUtilities.PREF_EMAIL));
        Call<ServerResponse> call = apiInterface.uploadImagesToServer(profilePhoto, email);
        MyUtilities.showAlertDialog(ImagesActivity.this, KAlertDialog.PROGRESS_TYPE, MyUtilities.KAlertDialogTitleLoding);

        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.body() != null) {

                    if (response.body().isStatus() == true) {
                        MyUtilities.cancelAlertDialog(ImagesActivity.this);
                        getStaffImages();

                    } else {
                        MyUtilities.cancelAlertDialog(ImagesActivity.this);

                        MyUtilities.showToast(ImagesActivity.this, MyUtilities.KAlertDialogTitleError);
                    }


                    //Log.i("Baba", "Res ponse is " + homePageBeanData.toString());
                    //  Toast.makeText(Home.this,full,Toast.LENGTH_LONG).show();


                    //  tvAddress.setText(name);
                    //Log.e("responsee result", "onResponse: " + name + phone + city + homePageBeanData.getSTATUS());

                } else {

                    MyUtilities.cancelAlertDialog(ImagesActivity.this);
                    MyUtilities.showToast(ImagesActivity.this, MyUtilities.KAlertDialogTitleError);

                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                MyUtilities.showToast(ImagesActivity.this,MyUtilities.KAlertDialogTitleError);
                MyUtilities.cancelAlertDialog(ImagesActivity.this);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
      /*  if(requestCode==GALLERY_PICK && resultCode==RESULT_OK)
        {
            Uri imgUri=data.getData();
            CropImage.activity(imgUri)
                    .setAspectRatio(1,1)
                    .start(this);

            //Toast.makeText(SettingsActivity.this,imgUri,Toast.LENGTH_LONG).show();
        }*/
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                Uri resultUri = result.getUri();


                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    imgPreview.setImageBitmap(bitmap);


                } catch (IOException e) {
                    e.printStackTrace();
                }


                File thumb_filePath = new File(resultUri.getPath());
                uploadImageToServer(thumb_filePath);


                Log.i("IMG", thumb_filePath.toString().substring(3, 12).concat(".jpg"));

                Log.d("path", "onActivityResult: " + thumb_filePath + resultUri.toString());

            }
        }
    }


    private void getStaffImages() {

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
        MyUtilities.showAlertDialog(ImagesActivity.this, KAlertDialog.PROGRESS_TYPE, MyUtilities.KAlertDialogTitleLoding);

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        JsonObject paramObject = new JsonObject();
        Call<ImagesResponse> userCall = apiInterface.getImagesResponse(SharedPreferenceUtils.getValue(ImagesActivity.this, MyUtilities.PREF_EMAIL));

        userCall.enqueue(new Callback<ImagesResponse>() {
            @Override
            public void onResponse(Call<ImagesResponse> call, Response<ImagesResponse> response) {


                if (response.body() != null) {

                    if (response.body().isStatus() == true) {
                        MyUtilities.cancelAlertDialog(ImagesActivity.this);

                        pathsItemList = response.body().getData().getPaths();
                        imagesAdapter = new ImagesAdapter(pathsItemList, ImagesActivity.this, false);
                        recyclerViewImages.setAdapter(imagesAdapter);

                    } else {
                        MyUtilities.cancelAlertDialog(ImagesActivity.this);

                        MyUtilities.showToast(ImagesActivity.this, MyUtilities.KAlertDialogTitleError);
                    }


                    //Log.i("Baba", "Res ponse is " + homePageBeanData.toString());
                    //  Toast.makeText(Home.this,full,Toast.LENGTH_LONG).show();


                    //  tvAddress.setText(name);
                    //Log.e("responsee result", "onResponse: " + name + phone + city + homePageBeanData.getSTATUS());

                } else {

                    MyUtilities.cancelAlertDialog(ImagesActivity.this);
                    MyUtilities.showToast(ImagesActivity.this, MyUtilities.KAlertDialogTitleError);

                }
            }


            @Override
            public void onFailure(Call<ImagesResponse> call, Throwable t) {

                Log.e("erro", "onFailure: " + t.getMessage());
                MyUtilities.showToast(ImagesActivity.this,MyUtilities.KAlertDialogTitleError);
                MyUtilities.cancelAlertDialog(ImagesActivity.this);

            }
        });
    }

    public void onClickDeleteImage(String path) {

        deleteImage(path);

    }

    public void deleteImage(String path) {

        MyUtilities.showAlertDialog(ImagesActivity.this, KAlertDialog.PROGRESS_TYPE, MyUtilities.KAlertDialogTitleLoding);
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
        paramObject.addProperty("EMAIL_ID", SharedPreferenceUtils.getValue(ImagesActivity.this, MyUtilities.PREF_EMAIL));
        paramObject.addProperty("IMAGE_PATH", path);

        Call<ServerResponse> userCall = apiInterface.deleteImage(paramObject.toString());

        userCall.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {


                if (response.body() != null) {

                    if (response.body().getStatus() == true) {

                        MyUtilities.cancelAlertDialog(ImagesActivity.this);
                        getStaffImages();


                    } else {


                        MyUtilities.cancelAlertDialog(ImagesActivity.this);
                        MyUtilities.showToast(ImagesActivity.this, MyUtilities.KAlertDialogTitleSwitch);

                    }

                } else {

                    MyUtilities.cancelAlertDialog(ImagesActivity.this);
                    MyUtilities.showToast(ImagesActivity.this, MyUtilities.KAlertDialogTitleSwitch);

                }
            }


            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                MyUtilities.cancelAlertDialog(ImagesActivity.this);
                MyUtilities.showToast(ImagesActivity.this, MyUtilities.KAlertDialogTitleError);

            }
        });
    }

}