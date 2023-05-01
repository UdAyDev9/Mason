package narasimhaa.com.mitraservice;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.kinda.alert.KAlertDialog;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import narasimhaa.com.mitraservice.Model.ContentProfile;
import narasimhaa.com.mitraservice.Model.MaterialDevelopers.ServicesDataItemSize;
import narasimhaa.com.mitraservice.Model.MaterialDevelopers.ServicesResponseSizeBrand;
import narasimhaa.com.mitraservice.Model.ServerResponse;
import narasimhaa.com.mitraservice.Model.ServicesResponse;
import narasimhaa.com.mitraservice.Model.service.ServicesDataItem;
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

public class AddMaterialActivity extends AppCompatActivity {

    private Toolbar toolbar;
    AddMaterialActivity context;
    Spinner spinner;
    //MultiSelectionSpinner spinner;
    //MultiSpinner multiSpinner;
    private static final String TAG = "AddMaterialActivity";
    private Button btnSubmit;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    private EditText etBusinessName, etDoorDelivery, etDescription, etMRP, etPrice, etWidth, etHeight, etBrandName;
    private TextView tvPersoalCert, tvCertiCamera;
    private String strBusinessName = "", strShapeType = "", strMaterialType = "", strMaterialTypeBrand = "",strMaterialTypeSize = "", strDoorDelivery = "", strDescription = "", strMRP = "", strPrice = "", strWidth = "", strSubCategory = "", strBrandName = "";
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
     List<ServicesDataItemSize> dataListSizeBrand = new ArrayList<>();
     List<ServicesDataItemSize> dataListShapes = new ArrayList<>();
     List<ServicesDataItemSize> dataListSubCategory = new ArrayList<>();
    List<ServicesDataItemSize> dataListSizeBrandOnly = new ArrayList<>();

    List<String> servicesStringList = new ArrayList<>();

    LinearLayout linearLayoutWidthHeight;

    private boolean isBricks = false;
    private boolean isCementOrPaints = false;


    private boolean isFromUpdate = false;
    private Spinner spinnerShapeType, spinnerMaterialType, spinnerBrands, spinnerMaterialSize,spinner_sub_category,spinner_material_shape;
    private String ID =" ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_material);

        //multiSpinner = (MultiSpinner) findViewById(R.id.spinnerMultiSpinner);
        spinner_sub_category =(Spinner) findViewById(R.id.spinner_sub_category);
        //spinner_material_shape =(Spinner)findViewById(R.id.spinner_material_shape);
        spinnerMaterialType = (Spinner) findViewById(R.id.material_types);
        spinnerBrands = (Spinner) findViewById(R.id.spinner_material_brand);
        spinnerMaterialSize = (Spinner) findViewById(R.id.spinner_material_size);
        getData(getIntent().getBooleanExtra(MyUtilities.IS_DEVELOPER, false));
        getBrandsData(getIntent().getBooleanExtra(MyUtilities.IS_DEVELOPER, false));
        getBrandsSizeData(getIntent().getBooleanExtra(MyUtilities.IS_DEVELOPER, false));
        getShapes(getIntent().getBooleanExtra(MyUtilities.IS_DEVELOPER, false));
        getSubCategories(getIntent().getBooleanExtra(MyUtilities.IS_DEVELOPER, false));

        btnSubmit = (Button) findViewById(R.id.bt_submit);
        etBusinessName = (EditText) findViewById(R.id.et_business_name);
        etPrice = (EditText) findViewById(R.id.et_price);
        etMRP = (EditText) findViewById(R.id.et_mrp);
        etDoorDelivery = (EditText) findViewById(R.id.et_door_delivery);
        etDescription = (EditText) findViewById(R.id.et_description);
        etWidth = (EditText) findViewById(R.id.et_width);
        etHeight = (EditText) findViewById(R.id.et_height);
        etBrandName = (EditText) findViewById(R.id.et_brand_name);
        linearLayoutWidthHeight = (LinearLayout) findViewById(R.id.linear_width_height);
        tvPersoalCert = (TextView) findViewById(R.id.tv_certification);
        tvCertiCamera = (TextView) findViewById(R.id.tv_certi_action);
        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar_extra);
        imgCierti = (ImageView) findViewById(R.id.iv_certi);
        spinnerShapeType = findViewById(R.id.spinner_shape);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_add_material);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().getExtras() != null) {

            if (getIntent().getExtras().getString("material_update_yes_or_no") != null) {

                if (getIntent().getExtras().getString("material_update_yes_or_no").equals("yes")) {

                    Log.i(TAG, "onCreate: "+getIntent().getExtras().getString(MyUtilities.INTENT_KEY_MATERIAL_TYPE));
                    Log.i(TAG, "onCreate: "+getIntent().getExtras().getString(MyUtilities.INTENT_KEY_BUSINESS_TYPE));
                   etBusinessName.setText(getIntent().getExtras().getString(MyUtilities.INTENT_KEY_BUSINESS_NAME));
                   etDescription.setText(getIntent().getExtras().getString(MyUtilities.INTENT_KEY_DESC));
                   etDoorDelivery.setText(getIntent().getExtras().getString(MyUtilities.INTENT_KEY_DOOR_DELIVERY));
                   etPrice.setText(getIntent().getExtras().getString(MyUtilities.INTENT_KEY_OFFER_PRICE));
                   etMRP.setText(getIntent().getExtras().getString(MyUtilities.INTENT_KEY_PRICE));
                   etDoorDelivery.setText(getIntent().getExtras().getString(MyUtilities.INTENT_KEY_DOOR_DELIVERY));
                    ID = getIntent().getExtras().getString(MyUtilities.INTENT_KEY_ID);
                    Log.i(TAG, "onCreate: "+getIntent().getExtras().getString(MyUtilities.INTENT_KEY_DOOR_DELIVERY));
                    Log.i(TAG, "onCreate: "+getIntent().getExtras().getString(MyUtilities.INTENT_KEY_DESC));
                    Log.i(TAG, "onCreate: "+getIntent().getExtras().getString(MyUtilities.INTENT_KEY_BRAND_NAME));

                    getSupportActionBar().setTitle(R.string.title_material_update);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                    isFromUpdate = true;
                }
            }
        }else {

            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(R.string.title_add_material);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        }


        sharedpreferences = getSharedPreferences("ServicePREFERENCES", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        editor.putString("key1", "value1");
        editor.commit();

        spinnerMaterialType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                strMaterialType = dataList.get(i).getSERVICE_NAME();

                if (strMaterialType.equals("BRICKS")) {

                    isBricks = true;
                    linearLayoutWidthHeight.setVisibility(View.VISIBLE);


                } else {
                    isBricks = false;
                    linearLayoutWidthHeight.setVisibility(View.GONE);

                }

                if (strMaterialType.equals("CEMENT") || strMaterialType.equals("COLORS")) {


                    etBrandName.setVisibility(View.VISIBLE);

                    isCementOrPaints = true;
                } else {
                    etBrandName.setVisibility(View.GONE);

                    isCementOrPaints = false;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerBrands.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                strMaterialTypeBrand = dataListSizeBrandOnly.get(i).getSERVICE_NAME();



            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_sub_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                strSubCategory = dataListSubCategory.get(i).getSERVICE_NAME();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        spinnerMaterialSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                strMaterialTypeSize = dataListSizeBrand.get(i).getSERVICE_NAME();



            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerShapeType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                /*if (isFromUpdate){



                }*/

                strShapeType = adapterView.getItemAtPosition(i).toString();



            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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




       /* tvCertiCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(AddMaterialActivity.this);
            }
        });*/
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                strBusinessName = etBusinessName.getText().toString();
                strDescription = etDescription.getText().toString();
                strDoorDelivery = etDoorDelivery.getText().toString();
                strMRP = etMRP.getText().toString();
                strPrice = etPrice.getText().toString();
                //strrPersoalCert = "Certificate";


                if (isBricks) {

                    if (strDescription.equals("") || strMRP.equals("") || strPrice.equals("") || strDoorDelivery.equals("") || strBusinessName.equals("") || etWidth.getText().toString().equals("") || etWidth.getText().toString().equals("")) {

                        MyUtilities.showToast(AddMaterialActivity.this, "Please fill all fields...");

                        //addOrUpdateService(true);

                    } else {

                        addOrUpdateService(isFromUpdate);
                    }

                } else if (isCementOrPaints) {

                    if (strDescription.equals("") || strMRP.equals("") || strPrice.equals("") || strDoorDelivery.equals("") || strBusinessName.equals("") || etBrandName.getText().toString().equals("")) {

                        MyUtilities.showToast(AddMaterialActivity.this, "Please fill all fields...");
//                    addOrUpdateService(true);

                    } else {

                        addOrUpdateService(isFromUpdate);
                    }

                } else {

                    if (strDescription.equals("") || strMRP.equals("") || strPrice.equals("") || strDoorDelivery.equals("") || strBusinessName.equals("")) {

                        MyUtilities.showToast(AddMaterialActivity.this, "Please fill all fields...");
//                    addOrUpdateService(true);

                    } else {

                        addOrUpdateService(isFromUpdate);
                    }

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

    /*  @RequiresApi(api = Build.VERSION_CODES.O)
      @Override
      public void OnMultiSpinnerItemSelected(List<String> chosenItems) {

          for (int i : chosenItems!!.indices){

              Log.e("chosenItems",chosenItems[i])

          }


          for (int i = 0; i < chosenItems.size(); i++) {

              Log.d("df", "OnMultiSpinnerItemSelected: " + chosenItems.get(i));
              editor.putString("service", chosenItems.get(i));
              strServiceType = chosenItems.get(i);
              servicesStringList.add(chosenItems.get(i));
              MyUtilities.showToast(this, chosenItems.get(i));

          }

          Log.i("servicesString", "OnMultiSpinnerItemSelected: " + String.join(", ", servicesStringList));
      }
  */
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
        MyUtilities.showAlertDialog(AddMaterialActivity.this, KAlertDialog.PROGRESS_TYPE, "Loading...");


        ApiInterface apiInterface = retrofit.create(ApiInterface.class);


        try {


            JsonObject paramObject = new JsonObject();
            paramObject.addProperty("SERVICE_TYPE", strMaterialType);
            paramObject.addProperty("BUSINESS_NAME", strBusinessName);
            ///Here sending Shape as BUSINESS_TYPE
            paramObject.addProperty("BUSINESS_TYPE", strShapeType);
            paramObject.addProperty("BRAND_NAME", strBrandName);
            paramObject.addProperty("DOOR_DELIVERY", strDoorDelivery);
            paramObject.addProperty("DESCRIPTION", strDescription);
            paramObject.addProperty("WEIGHT", "");
            //Here sending SubCategory as HEIGHT
            paramObject.addProperty("HEIGHT", strSubCategory);
            paramObject.addProperty("MRP", strMRP);
            paramObject.addProperty("PRICE", strPrice);

           /*paramObject.addProperty("NAME", name);
            paramObject.addProperty("CONTACT_NO", phone);
            paramObject.addProperty("EMAIL_ID", mail);
            paramObject.addProperty("CITY", city);
            paramObject.addProperty("STATE", state);
            paramObject.addProperty("DISTRICT", district);
            paramObject.addProperty("PINCODE", pincode);*/
            Call<ServerResponse> userCall = apiInterface.addService(paramObject.toString());

            userCall.enqueue(new Callback<ServerResponse>() {
                @Override
                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {

                    if (response.body() != null) {


                        if (response.body().getStatus() == true) {

                            MyUtilities.cancelAlertDialog(AddMaterialActivity.this);
                            Log.d("responsee", "onResponse: " + response.body().getStatus());
                            Log.e("responsee", "onResponse: " + response.body().getMsg());
                            MyUtilities.showToast(AddMaterialActivity.this, "Service updated successfully!!!");

                            //Log.e("responsee", "onResponse: "+response.body().getMsg());
                            Intent it = new Intent(getApplicationContext(), AddMaterialActivity.class);
                            startActivity(it);
                            finish();
                        } else {

                            MyUtilities.showToast(AddMaterialActivity.this, response.body().getMsg().toString());
                            //MyUtilities.showAlertDialog(AddMaterialActivity.this, KAlertDialog.ERROR_TYPE, MyUtilities.KAlertDialogTitleLoding);

                        }


                    } else {

                        MyUtilities.showAlertDialog(AddMaterialActivity.this, KAlertDialog.ERROR_TYPE, MyUtilities.KAlertDialogTitleLoding);

                        MyUtilities.showToast(AddMaterialActivity.this, "Something went wrong! Please try later");

                    }


                }

                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {

                    Log.e("erro", "onFailure: " + t.getMessage());
                    MyUtilities.showToast(AddMaterialActivity.this, MyUtilities.KAlertDialogTitleError);
                    MyUtilities.cancelAlertDialog(AddMaterialActivity.this);

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

                    MyUtilities.showToast(AddMaterialActivity.this, MyUtilities.KAlertDialogTitleError);
                    MyUtilities.cancelAlertDialog(AddMaterialActivity.this);
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
        paramObject.addProperty("BOD_SEQ_NO", SharedPreferenceUtils.getValue(AddMaterialActivity.this, MyUtilities.PREF_BOD_SEQ_NO));
        //paramObject.put("PASSWORD", "12345");
           /* paramObject.put("USERNAME", et_mno.getText().toString().trim());
            paramObject.put("PASSWORD", et_password.getText().toString().trim());
*/

        Call<ServicesResponse> userCall;
        if (isDeveloper) {

            userCall = apiInterface.getMaterials();

        } else {
            userCall = apiInterface.getMaterials();

        }
        //Call<ServicesResponse> userCall = apiInterface.getServiceTypes();

        userCall.enqueue(new Callback<ServicesResponse>() {
            @Override
            public void onResponse(Call<ServicesResponse> call, Response<ServicesResponse> response) {

                if (response.body() != null) {

                    if (response.body().getStatus() == true) {

                        MyUtilities.cancelAlertDialog(AddMaterialActivity.this);

                        dataList = response.body().getData();

                        List<String> tempList = new ArrayList<>();

                        for (int i = 0; i < dataList.size(); i++) {

                            tempList.add(dataList.get(i).getSERVICE_NAME());

                        }


                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddMaterialActivity.this,
                                android.R.layout.simple_spinner_item, tempList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        spinnerMaterialType.setAdapter(adapter);

                       /* multiSpinner.setAdapterWithOutImage(AddMaterialActivity.this, tempList, AddMaterialActivity.this);
                        multiSpinner.initMultiSpinner(AddMaterialActivity.this, multiSpinner);
*/

                    } else {
                        MyUtilities.cancelAlertDialog(AddMaterialActivity.this);

                        MyUtilities.showToast(AddMaterialActivity.this, MyUtilities.KAlertDialogTitleError);
                    }

                } else {

                    MyUtilities.cancelAlertDialog(AddMaterialActivity.this);
                    MyUtilities.showToast(AddMaterialActivity.this, MyUtilities.KAlertDialogTitleError);

                }
            }


            @Override
            public void onFailure(Call<ServicesResponse> call, Throwable t) {

                Log.e("erro", "onFailure: " + t.getMessage());
                MyUtilities.showToast(AddMaterialActivity.this, MyUtilities.KAlertDialogTitleError);
                MyUtilities.cancelAlertDialog(AddMaterialActivity.this);

            }
        });
    }

    public void getBrandsData(boolean isDeveloper) {
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
        paramObject.addProperty("BOD_SEQ_NO", SharedPreferenceUtils.getValue(AddMaterialActivity.this, MyUtilities.PREF_BOD_SEQ_NO));

        Call<ServicesResponseSizeBrand> userCall;
        if (isDeveloper) {

            userCall = apiInterface.getServiceTypesBrands();

        } else {
            userCall = apiInterface.getServiceTypesBrands();

        }

        userCall.enqueue(new Callback<ServicesResponseSizeBrand>() {
            @Override
            public void onResponse(Call<ServicesResponseSizeBrand> call, Response<ServicesResponseSizeBrand> response) {


                if (response.body() != null) {

                    if (response.body().getStatus() == true) {

                        MyUtilities.cancelAlertDialog(AddMaterialActivity.this);


                        dataListSizeBrandOnly = response.body().getData();

                        List<String> tempList = new ArrayList<>();

                        for (int i = 0; i < dataListSizeBrandOnly.size(); i++) {

                            tempList.add(dataListSizeBrandOnly.get(i).getSERVICE_NAME());

                        }


                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddMaterialActivity.this,
                                android.R.layout.simple_spinner_item, tempList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        spinnerBrands.setAdapter(adapter);


                    } else {
                        MyUtilities.cancelAlertDialog(AddMaterialActivity.this);

                        MyUtilities.showToast(AddMaterialActivity.this, MyUtilities.KAlertDialogTitleError);
                    }

                } else {

                    MyUtilities.cancelAlertDialog(AddMaterialActivity.this);
                    MyUtilities.showToast(AddMaterialActivity.this, MyUtilities.KAlertDialogTitleError);

                }
            }


            @Override
            public void onFailure(Call<ServicesResponseSizeBrand> call, Throwable t) {

                Log.e("erro", "onFailure: " + t.getMessage());
                MyUtilities.showToast(AddMaterialActivity.this, MyUtilities.KAlertDialogTitleError);
                MyUtilities.cancelAlertDialog(AddMaterialActivity.this);

            }
        });
    }

    public void getBrandsSizeData(boolean isDeveloper) {
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
        paramObject.addProperty("BOD_SEQ_NO", SharedPreferenceUtils.getValue(AddMaterialActivity.this, MyUtilities.PREF_BOD_SEQ_NO));

        Call<ServicesResponseSizeBrand> userCall;
        if (isDeveloper) {

            userCall = apiInterface.getServiceTypeSize();

        } else {
            userCall = apiInterface.getServiceTypeSize();

        }

        userCall.enqueue(new Callback<ServicesResponseSizeBrand>() {
            @Override
            public void onResponse(Call<ServicesResponseSizeBrand> call, Response<ServicesResponseSizeBrand> response) {


                if (response.body() != null) {

                    if (response.body().getStatus() == true) {

                        MyUtilities.cancelAlertDialog(AddMaterialActivity.this);


                        dataListSizeBrand = response.body().getData();

                        List<String> tempList = new ArrayList<>();

                        for (int i = 0; i < dataListSizeBrand.size(); i++) {

                            tempList.add(dataListSizeBrand.get(i).getSERVICE_NAME());


                        }


                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddMaterialActivity.this,
                                android.R.layout.simple_spinner_item, tempList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        spinnerMaterialSize.setAdapter(adapter);


                    } else {
                        MyUtilities.cancelAlertDialog(AddMaterialActivity.this);

                        MyUtilities.showToast(AddMaterialActivity.this, MyUtilities.KAlertDialogTitleError);
                    }

                } else {

                    MyUtilities.cancelAlertDialog(AddMaterialActivity.this);
                    MyUtilities.showToast(AddMaterialActivity.this, MyUtilities.KAlertDialogTitleError);

                }
            }


            @Override
            public void onFailure(Call<ServicesResponseSizeBrand> call, Throwable t) {

                Log.e("erro", "onFailure: " + t.getMessage());
                MyUtilities.showToast(AddMaterialActivity.this, MyUtilities.KAlertDialogTitleError);
                MyUtilities.cancelAlertDialog(AddMaterialActivity.this);

            }
        });
    }


    private void addOrUpdateService(boolean isFromUpdate) {

        MyUtilities.showAlertDialog(AddMaterialActivity.this, KAlertDialog.PROGRESS_TYPE, MyUtilities.KAlertDialogTitleLoding);

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

            jsonReg.addProperty("CONTACT_NO", SharedPreferenceUtils.getValue(AddMaterialActivity.this, MyUtilities.PREF_USER_MOBILE_NO));
            jsonReg.addProperty("EMAIL_ID", SharedPreferenceUtils.getValue(AddMaterialActivity.this, MyUtilities.PREF_EMAIL));
            jsonReg.addProperty("SERVICE_TYPE", strMaterialType);
            jsonReg.addProperty("BUSINESS_NAME", strBusinessName);
            //Here sending SubCategory Shape as BUSINESS_TYPE
            jsonReg.addProperty("STEEL_SUB_CATEGORY", strSubCategory);
            jsonReg.addProperty("BRAND_NAME", strMaterialTypeBrand);
            jsonReg.addProperty("DOOR_DELIVERY", strDoorDelivery);
            jsonReg.addProperty("DESCRIPTION", strDescription);
            jsonReg.addProperty("STEEL_SIZE", strMaterialTypeSize);
            //Here sending Shape as HEIGHT
            jsonReg.addProperty("STEEL_SHAPE", strShapeType);
            jsonReg.addProperty("MRP", strMRP);
            jsonReg.addProperty("PRICE", strPrice);
            Call<ServerResponse> userCall;

            if (isFromUpdate == true) {

                jsonReg.addProperty("ID", ID);

                userCall = apiInterface.updateMaterialType(jsonReg.toString());

            } else {

                userCall = apiInterface.addMaterialType(jsonReg.toString());

            }

            userCall.enqueue(new Callback<ServerResponse>() {
                @Override
                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {


                    if (response.body() != null) {


                        if (response.body().getStatus() == true) {

                            //SharedPreferenceUtils.setValue(AddMaterialActivity.this, MyUtilities.PREF_SER_PER_SEQ_ID, response.body().getData().getSER_PER_SEQ_ID());

                            if(isFromUpdate){

                                MyUtilities.showToast(AddMaterialActivity.this, "Material updated successfully!!!");

                            }else {

                                MyUtilities.showToast(AddMaterialActivity.this, "Material added successfully!!!");

                            }

                            MyUtilities.cancelAlertDialog(AddMaterialActivity.this);

                            Intent intent = new Intent(AddMaterialActivity.this, DashBoardActivity.class);
                            startActivity(intent);
                            finish();

                        } else {

                            MyUtilities.showToast(AddMaterialActivity.this, response.body().getMsg().toString());
                            MyUtilities.cancelAlertDialog(AddMaterialActivity.this);

                        }


                    } else {

                        MyUtilities.cancelAlertDialog(AddMaterialActivity.this);

                        MyUtilities.showToast(AddMaterialActivity.this, "Something went wrong! Please try later");

                    }


                }

                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {
                    MyUtilities.showToast(AddMaterialActivity.this, MyUtilities.KAlertDialogTitleError);
                    MyUtilities.cancelAlertDialog(AddMaterialActivity.this);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            MyUtilities.cancelAlertDialog(AddMaterialActivity.this);

        }
    }

    public void getShapes(boolean isDeveloper) {
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
        paramObject.addProperty("BOD_SEQ_NO", SharedPreferenceUtils.getValue(AddMaterialActivity.this, MyUtilities.PREF_BOD_SEQ_NO));

        Call<ServicesResponseSizeBrand> userCall;
        if (isDeveloper) {

            userCall = apiInterface.getShapes();

        } else {
            userCall = apiInterface.getShapes();

        }

        userCall.enqueue(new Callback<ServicesResponseSizeBrand>() {
            @Override
            public void onResponse(Call<ServicesResponseSizeBrand> call, Response<ServicesResponseSizeBrand> response) {


                if (response.body() != null) {

                    if (response.body().getStatus() == true) {

                        MyUtilities.cancelAlertDialog(AddMaterialActivity.this);


                        dataListShapes= response.body().getData();

                        List<String> tempList = new ArrayList<>();

                        for (int i = 0; i < dataListShapes.size(); i++) {

                            tempList.add(dataListShapes.get(i).getSERVICE_NAME());


                        }


                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddMaterialActivity.this,
                                android.R.layout.simple_spinner_item, tempList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        spinnerShapeType.setAdapter(adapter);


                    } else {
                        MyUtilities.cancelAlertDialog(AddMaterialActivity.this);

                        MyUtilities.showToast(AddMaterialActivity.this, MyUtilities.KAlertDialogTitleError);
                    }

                } else {

                    MyUtilities.cancelAlertDialog(AddMaterialActivity.this);
                    MyUtilities.showToast(AddMaterialActivity.this, MyUtilities.KAlertDialogTitleError);

                }
            }


            @Override
            public void onFailure(Call<ServicesResponseSizeBrand> call, Throwable t) {

                Log.e("erro", "onFailure: " + t.getMessage());
                MyUtilities.showToast(AddMaterialActivity.this, MyUtilities.KAlertDialogTitleError);
                MyUtilities.cancelAlertDialog(AddMaterialActivity.this);

            }
        });
    }

    public void getSubCategories(boolean isDeveloper) {
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
        paramObject.addProperty("BOD_SEQ_NO", SharedPreferenceUtils.getValue(AddMaterialActivity.this, MyUtilities.PREF_BOD_SEQ_NO));

        Call<ServicesResponseSizeBrand> userCall;
        if (isDeveloper) {

            userCall = apiInterface.getSubCategories();

        } else {
            userCall = apiInterface.getSubCategories();

        }

        userCall.enqueue(new Callback<ServicesResponseSizeBrand>() {
            @Override
            public void onResponse(Call<ServicesResponseSizeBrand> call, Response<ServicesResponseSizeBrand> response) {


                if (response.body() != null) {

                    if (response.body().getStatus() == true) {

                        MyUtilities.cancelAlertDialog(AddMaterialActivity.this);


                        dataListSubCategory= response.body().getData();

                        List<String> tempList = new ArrayList<>();

                        for (int i = 0; i < dataListSubCategory.size(); i++) {

                            tempList.add(dataListSubCategory.get(i).getSERVICE_NAME());


                        }


                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddMaterialActivity.this,
                                android.R.layout.simple_spinner_item, tempList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        spinner_sub_category.setAdapter(adapter);


                    } else {
                        MyUtilities.cancelAlertDialog(AddMaterialActivity.this);

                        MyUtilities.showToast(AddMaterialActivity.this, MyUtilities.KAlertDialogTitleError);
                    }

                } else {

                    MyUtilities.cancelAlertDialog(AddMaterialActivity.this);
                    MyUtilities.showToast(AddMaterialActivity.this, MyUtilities.KAlertDialogTitleError);

                }
            }


            @Override
            public void onFailure(Call<ServicesResponseSizeBrand> call, Throwable t) {

                Log.e("erro", "onFailure: " + t.getMessage());
                MyUtilities.showToast(AddMaterialActivity.this, MyUtilities.KAlertDialogTitleError);
                MyUtilities.cancelAlertDialog(AddMaterialActivity.this);

            }
        });
    }


}