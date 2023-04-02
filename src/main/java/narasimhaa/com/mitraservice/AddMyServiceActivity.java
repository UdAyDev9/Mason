package narasimhaa.com.mitraservice;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.anurag.multiselectionspinner.MultiSelectionSpinnerDialog;
import com.anurag.multiselectionspinner.MultiSpinner;
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
import java.util.concurrent.TimeUnit;

import narasimhaa.com.mitraservice.Model.ContentProfile;
import narasimhaa.com.mitraservice.Model.service.ServicesDataItem;
import narasimhaa.com.mitraservice.Model.ServerResponse;
import narasimhaa.com.mitraservice.Model.ServicesResponse;
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

public class AddMyServiceActivity extends AppCompatActivity implements MultiSelectionSpinnerDialog.OnMultiSpinnerSelectionListener {
    private Toolbar toolbar;
    AddMyServiceActivity context;
    Spinner spinner;
    //MultiSelectionSpinner spinner;
    MultiSpinner multiSpinner;
    private Button btnSubmit;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    private EditText etYearOfxp, etEducation, etRange;
    private TextView tvPersoalCert, tvCertiCamera;
    private String strYearOfxp = "", strEducation = "", strrPersoalCert = "", strRangee = "", strServiceType = "";
    private ImageView imgCierti;
    String name = "";
    String mail = "";
    String phone = "";
    String status = "";
    String bodSeqNo = "";
    String city = "";
    String address = "";
    String state = "";
    String district = "";
    String pincode = "";
    List<ServicesDataItem> dataList = new ArrayList<>();

    List<String> servicesStringList = new ArrayList<>();
    private boolean isFromUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_my_service);

        multiSpinner = (MultiSpinner) findViewById(R.id.spinnerMultiSpinner);
        getData(getIntent().getBooleanExtra(MyUtilities.IS_DEVELOPER, false));
        btnSubmit = (Button) findViewById(R.id.bt_submit);
        etYearOfxp = (EditText) findViewById(R.id.et_yoe);
        etEducation = (EditText) findViewById(R.id.et_education);
        tvPersoalCert = (TextView) findViewById(R.id.tv_certification);
        tvCertiCamera = (TextView) findViewById(R.id.tv_certi_action);
        etRange = (EditText) findViewById(R.id.et_range);
        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar_extra);
        imgCierti = (ImageView) findViewById(R.id.iv_certi);
        setSupportActionBar(toolbar);

        if (getIntent().getBooleanExtra(MyUtilities.IS_DEVELOPER, false)) {

            getSupportActionBar().setTitle(R.string.title_AddMyservice);

        } else {

            /*getSupportActionBar().setTitle(R.string.title_add_material);
            etEducation.setHint("Min. Price");*/

            // Digits

        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = this;
        //setupActionBar();
        sharedpreferences = getSharedPreferences("ServicePREFERENCES", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        editor.putString("key1", "value1");
        editor.commit();


        Bundle bundle = getIntent().getExtras();
       /* if (bundle != null) {
            etYearOfxp.setText(bundle.getString("service_yoe"));
            etEducation.setText(bundle.getString("service_edu"));
            tvPersoalCert.setText(bundle.getString("service_certi"));
            //et_distract.setText(bundle.getString(   "service_range" ,           ));
            etRange.setText(bundle.getString("service_range"));
            // et_state.setText(bundle.getString("profile_state"   ));
            //et_mbl.setText(bundle.getString("profile_mobile"  ));
        }*/

        if (getIntent().getExtras() != null) {

            if (getIntent().getExtras().getString("service_update_yes_or_no") != null) {

                if (getIntent().getExtras().getString("service_update_yes_or_no").equals("yes")) {
                    //tvYearOfxp.setText   (getIntent().getExtras().getString("service_names" ,  ""));
                    etYearOfxp.setText(getIntent().getExtras().getString("service_yoe", ""));
                    etEducation.setText(getIntent().getExtras().getString("service_charge", ""));
                    etRange.setText(getIntent().getExtras().getString("service_range", ""));
                    getSupportActionBar().setTitle(R.string.title_update_Myservice);
                    isFromUpdate = true;
                }
            }
        }



        tvCertiCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(AddMyServiceActivity.this);
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                strYearOfxp = etYearOfxp.getText().toString();
                strEducation = etEducation.getText().toString();
                strrPersoalCert = "Certificate";
                strRangee = etRange.getText().toString();



               /* if (strYearOfxp.equals("") || strEducation.equals("")||strrPersoalCert.equals("")|| strRangee.equals("")){

                    MyUtilities.showToast(AddMyServiceActivity.this,"Please fill all fields...");

                }else{

                    editor.putString("yoe", strYearOfxp);
                    editor.putString("edu", strEducation);
                    editor.putString("certi", strrPersoalCert);
                    editor.putString("range", strRangee);

                    editor.commit();

                    send_data_to_server();


                }
*/

                if (strYearOfxp.equals("") || strEducation.equals("") || strrPersoalCert.equals("") || strRangee.equals("")) {

                    MyUtilities.showToast(AddMyServiceActivity.this, "Please fill all fields...");

                } else {

                    addOrUpdateService(isFromUpdate);

                }
            }
        });


    }


    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(context.getResources().getString(R.string.title_AddMyservice));
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {

            finish();

        }

        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void OnMultiSpinnerItemSelected(List<String> chosenItems) {
/*
        for (int i : chosenItems!!.indices){

            Log.e("chosenItems",chosenItems[i])

        }*/


        for (int i = 0; i < chosenItems.size(); i++) {

            Log.d("df", "OnMultiSpinnerItemSelected: " + chosenItems.get(i));
            editor.putString("service", chosenItems.get(i));
            strServiceType = chosenItems.get(i);
            servicesStringList.add(chosenItems.get(i));
            MyUtilities.showToast(this, chosenItems.get(i));

        }

        Log.i("servicesString", "OnMultiSpinnerItemSelected: " + String.join(", ", servicesStringList));
    }

    private void send_data_to_server() {


        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(ApiInterface.URL_BASE)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MyUtilities.showAlertDialog(AddMyServiceActivity.this, KAlertDialog.PROGRESS_TYPE, "Loading...");


        ApiInterface apiInterface = retrofit.create(ApiInterface.class);


        try {


            JsonObject paramObject = new JsonObject();
            paramObject.addProperty("SERVICE_TYPE", strServiceType);
            paramObject.addProperty("EXPERIENCE", strYearOfxp);
            paramObject.addProperty("QUALIFICATION", strEducation);
            paramObject.addProperty("CERTIFICATE", strrPersoalCert);
            paramObject.addProperty("WITH_IN_RANGE", strRangee);
            paramObject.addProperty("NAME", name);
            paramObject.addProperty("CONTACT_NO", phone);
            paramObject.addProperty("EMAIL_ID", mail);
            paramObject.addProperty("CITY", city);
            paramObject.addProperty("STATE", state);
            paramObject.addProperty("DISTRICT", district);
            paramObject.addProperty("PINCODE", pincode);
            Call<ServerResponse> userCall = apiInterface.addService(paramObject.toString());

            userCall.enqueue(new Callback<ServerResponse>() {
                @Override
                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {

                    if (response.body() != null) {


                        if (response.body().getStatus() == true) {

                            MyUtilities.cancelAlertDialog(AddMyServiceActivity.this);
                            Log.d("responsee", "onResponse: " + response.body().getStatus());
                            Log.e("responsee", "onResponse: " + response.body().getMsg());
                            MyUtilities.showToast(AddMyServiceActivity.this, "Service updated successfully!!!");

                            //Log.e("responsee", "onResponse: "+response.body().getMsg());
                            Intent it = new Intent(getApplicationContext(), AddMyServiceActivity.class);
                            startActivity(it);
                            finish();
                        } else {

                            MyUtilities.showToast(AddMyServiceActivity.this, response.body().getMsg().toString());
                            MyUtilities.showAlertDialog(AddMyServiceActivity.this, KAlertDialog.ERROR_TYPE, MyUtilities.KAlertDialogTitleLoding);

                        }


                    } else {

                        MyUtilities.showAlertDialog(AddMyServiceActivity.this, KAlertDialog.ERROR_TYPE, MyUtilities.KAlertDialogTitleLoding);

                        MyUtilities.showToast(AddMyServiceActivity.this, "Something went wrong! Please try later");

                    }


                }

                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {

                    MyUtilities.showToast(AddMyServiceActivity.this, MyUtilities.KAlertDialogTitleError);
                    MyUtilities.cancelAlertDialog(AddMyServiceActivity.this);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

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
                editor.putString("uri_imagee", resultUri.toString());


                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    imgCierti.setImageBitmap(bitmap);


                } catch (IOException e) {
                    e.printStackTrace();
                }


                File thumb_filePath = new File(resultUri.getPath());
                tvPersoalCert.setText(thumb_filePath.toString().substring(3, 12).concat(".jpg"));

                Log.d("path", "onActivityResult: " + thumb_filePath + resultUri.toString());

            }
        }
    }


    public void getData_() {


        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(ApiInterface.URL_BASE)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);


        // prepare call in Retrofit 2.0
        try {
        /*    JSONObject paramObject = new JSONObject();
            paramObject.put("EMAIL_ID", "krishna@appcare.co.in");*/


            JsonObject paramObject = new JsonObject();
            //paramObject.addProperty("EMAIL_ID", "krishna@appcare.co.in");
            paramObject.addProperty("EMAIL_ID", sharedpreferences.getString("userName", ""));
            //paramObject.put("PASSWORD", "12345");
            /* paramObject.put("USERNAME", et_mno.getText().toString().trim());
            paramObject.put("PASSWORD", et_password.getText().toString().trim());*/
            Call<ServerResponse> userCall = apiInterface.getProfileResponse(paramObject.toString());

            userCall.enqueue(new Callback<ServerResponse>() {
                @Override
                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {

                         /*   if (response.body().getStatus().equals("1")){


                            }
*/

                    List<ContentProfile> homePageBeanData = response.body().getContent();

                    Log.i("Baba", "Res ponse is " + homePageBeanData.toString());
                    //  Toast.makeText(Home.this,full,Toast.LENGTH_LONG).show();

                    for (int i = 0; i < homePageBeanData.size(); i++) {

                        name = homePageBeanData.get(i).getNAME();
                        mail = homePageBeanData.get(i).getEMAILID();
                        phone = homePageBeanData.get(i).getMOBILENO();
                        status = homePageBeanData.get(i).getSTATUS();
                        bodSeqNo = homePageBeanData.get(i).getBODSEQNO();
                        city = homePageBeanData.get(i).getCITY();
                        address = homePageBeanData.get(i).getADDRESS();
                        state = homePageBeanData.get(i).getSTATE();
                        district = homePageBeanData.get(i).getDISTRICT();
                        pincode = homePageBeanData.get(i).getPINCODENO();

                    }

                    Log.e("op", "onResponse: " + response.body().getStatus());
                    Log.e("op[[", "onResponse: " + response.body().getContent());

                }

                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {

                    MyUtilities.showToast(AddMyServiceActivity.this, MyUtilities.KAlertDialogTitleError);
                    MyUtilities.cancelAlertDialog(AddMyServiceActivity.this);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getData(boolean isDeveloper) {
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
        //paramObject.addProperty("EMAIL_ID", "krishna@appcare.co.in");
        paramObject.addProperty("BOD_SEQ_NO", SharedPreferenceUtils.getValue(AddMyServiceActivity.this, MyUtilities.PREF_BOD_SEQ_NO));
        //paramObject.put("PASSWORD", "12345");
           /* paramObject.put("USERNAME", et_mno.getText().toString().trim());
            paramObject.put("PASSWORD", et_password.getText().toString().trim());
*/

        Call<ServicesResponse> userCall;
        if (isDeveloper) {

            userCall = apiInterface.getServices();

        } else {
            userCall = apiInterface.getServices();

        }
        //Call<ServicesResponse> userCall = apiInterface.getServiceTypes();

        userCall.enqueue(new Callback<ServicesResponse>() {
            @Override
            public void onResponse(Call<ServicesResponse> call, Response<ServicesResponse> response) {


                if (response.body() != null) {

                    if (response.body().getStatus() == true) {

                        MyUtilities.cancelAlertDialog(AddMyServiceActivity.this);


                        dataList = response.body().getData();

                        List<String> tempList = new ArrayList<>();

                        for (int i = 0; i < dataList.size(); i++) {

                            tempList.add(dataList.get(i).getSERVICE_NAME());

                        }


                        multiSpinner.setAdapterWithOutImage(AddMyServiceActivity.this, tempList, AddMyServiceActivity.this);
                        multiSpinner.initMultiSpinner(AddMyServiceActivity.this, multiSpinner);


                    } else {
                        MyUtilities.cancelAlertDialog(AddMyServiceActivity.this);

                        MyUtilities.showToast(AddMyServiceActivity.this, MyUtilities.KAlertDialogTitleError);
                    }

                } else {

                    MyUtilities.cancelAlertDialog(AddMyServiceActivity.this);
                    MyUtilities.showToast(AddMyServiceActivity.this, MyUtilities.KAlertDialogTitleError);

                }
            }


            @Override
            public void onFailure(Call<ServicesResponse> call, Throwable t) {

                MyUtilities.showToast(AddMyServiceActivity.this, MyUtilities.KAlertDialogTitleError);
                MyUtilities.cancelAlertDialog(AddMyServiceActivity.this);

            }
        });
    }


    private void addOrUpdateService(boolean isFromUpdate) {

        MyUtilities.showAlertDialog(AddMyServiceActivity.this, KAlertDialog.PROGRESS_TYPE, MyUtilities.KAlertDialogTitleLoding);

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

            jsonReg.addProperty("NAME", SharedPreferenceUtils.getValue(AddMyServiceActivity.this, MyUtilities.PREF_USER_NAME));
            jsonReg.addProperty("CONTACT_NO", SharedPreferenceUtils.getValue(AddMyServiceActivity.this, MyUtilities.PREF_USER_MOBILE_NO));
            jsonReg.addProperty("EMAIL_ID", SharedPreferenceUtils.getValue(AddMyServiceActivity.this, MyUtilities.PREF_EMAIL));
            jsonReg.addProperty("STATE", "");
            jsonReg.addProperty("DISTRICT", SharedPreferenceUtils.getValue(AddMyServiceActivity.this, MyUtilities.PREF_USER_ADDRESS));
            jsonReg.addProperty("CITY", SharedPreferenceUtils.getValue(AddMyServiceActivity.this, MyUtilities.PREF_USER_CITY));
            jsonReg.addProperty("PINCODE", SharedPreferenceUtils.getValue(AddMyServiceActivity.this, MyUtilities.PREF_USER_PINCODE));
            jsonReg.addProperty("WITH_IN_RANGE", strRangee);
            jsonReg.addProperty("EXPERIENCE", strYearOfxp);
            jsonReg.addProperty("REGISTERED_BY", "");
            jsonReg.addProperty("CERTIFICATE", "");
            jsonReg.addProperty("SERVICE_NAME", String.join(",", servicesStringList));
            jsonReg.addProperty("QUALIFICATION", strEducation);//Price

            Call<ServerResponse> userCall;

            if (isFromUpdate == true) {

                jsonReg.addProperty("SER_PER_SEQ_ID", SharedPreferenceUtils.getValue(AddMyServiceActivity.this, MyUtilities.PREF_SER_PER_SEQ_ID));

                userCall = apiInterface.updateService(jsonReg.toString());

            } else {

                userCall = apiInterface.addService(jsonReg.toString());

            }

            userCall.enqueue(new Callback<ServerResponse>() {
                @Override
                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {


                    if (response.body() != null) {


                        if (response.body().getStatus() == true) {

                            SharedPreferenceUtils.setValue(AddMyServiceActivity.this, MyUtilities.PREF_SER_PER_SEQ_ID, response.body().getData().getSER_PER_SEQ_ID());

                            if (isFromUpdate) {

                                MyUtilities.showToast(AddMyServiceActivity.this, "Service updated successfully!!!");

                            } else {

                                MyUtilities.showToast(AddMyServiceActivity.this, "Service added successfully!!!");

                            }

                            MyUtilities.cancelAlertDialog(AddMyServiceActivity.this);


                            Intent intent = new Intent(AddMyServiceActivity.this, DashBoardActivity.class);
                            startActivity(intent);
                            finish();

                        } else {

                            MyUtilities.showToast(AddMyServiceActivity.this, response.body().getMsg().toString());
                            MyUtilities.cancelAlertDialog(AddMyServiceActivity.this);

                        }


                    } else {

                        MyUtilities.cancelAlertDialog(AddMyServiceActivity.this);

                        MyUtilities.showToast(AddMyServiceActivity.this, "Something went wrong! Please try later");

                    }


                }

                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {
                    MyUtilities.showToast(AddMyServiceActivity.this, MyUtilities.KAlertDialogTitleError);
                    MyUtilities.cancelAlertDialog(AddMyServiceActivity.this);
                }
            });
            //samreg.put("servicetype", sp_servicertype);
        } catch (Exception e) {
            e.printStackTrace();
            MyUtilities.cancelAlertDialog(AddMyServiceActivity.this);

        }


    }


}
