package narasimhaa.com.mitraservice;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.kinda.alert.KAlertDialog;

import narasimhaa.com.mitraservice.Model.ContentProfile;
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

public class MyprofileActivity extends AppCompatActivity {
    private Toolbar toolbar;
    MyprofileActivity myprofileActivity;
    Spinner spinner;
    Context context;
    SharedPreferences sharedPreferences;
    private TextView tvUname, tvUmail, tvBodSeqNo, tvUstatus, tvUMobileNo, tvCity, tvAddress, tvState, tvDistrict, tvPincode, tvUtype;
    private Button btnUpdate;
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
    String password = "";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile);

        tvUname = (TextView) findViewById(R.id.tv_uname_);
        tvUmail = (TextView) findViewById(R.id.tv_email_);
        //  tvBodSeqNo = (TextView)findViewById(R.id.tv_bodseq);
        //  tvUstatus = (TextView)findViewById(R.id.tv_ustatus);
        tvUMobileNo = (TextView) findViewById(R.id.mobileNumber);
        tvCity = (TextView) findViewById(R.id.tv_city_);
        tvAddress = (TextView) findViewById(R.id.tv_address_);
        tvState = (TextView) findViewById(R.id.tv_state_);
        tvDistrict = (TextView) findViewById(R.id.tv_district_);
        tvPincode = (TextView) findViewById(R.id.tv_pincode);
        btnUpdate = (Button) findViewById(R.id.btn_update);
        toolbar = (Toolbar) findViewById(R.id.toolbar_extra);
        tvUtype = (TextView) findViewById(R.id.tv_utype);

        if (!SharedPreferenceUtils.getValue(MyprofileActivity.this, MyUtilities.PREF_USER_TYPE).equals("Individual")) {
            tvUtype.setText(SharedPreferenceUtils.getValue(MyprofileActivity.this, MyUtilities.PREF_USER_TYPE));
            tvUtype.setVisibility(View.VISIBLE);
        }

        //    toolbar = (Toolbar) findViewById(R.id.toolbar_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_UpdateProfile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //gets

        context = this;

        //  setupActionBar();
        MyUtilities.showAlertDialog(MyprofileActivity.this, KAlertDialog.PROGRESS_TYPE, MyUtilities.KAlertDialogTitleLoding);

        getData();

        sharedPreferences = getSharedPreferences("LoginPREFERENCES", MODE_PRIVATE);


        /*spinner=(Spinner)findViewById(R.id.sp_servicertype);
        final String[] select_qualification = {
                "Select Service  Type", "Material supplier","Service Personnel", "Developer", "Individual",
        };
        ArrayList<StateVO> listVOs = new ArrayList<>();

        for (int i = 0; i < select_qualification.length; i++) {
            StateVO stateVO = new StateVO();
            stateVO.setTitle(select_qualification[i]);
            stateVO.setSelected(false);
            listVOs.add(stateVO);
        }
        MyAdapter myAdapter = new MyAdapter(MyprofileActivity.this, 0,
                listVOs);
        spinner.setAdapter(myAdapter);*/

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MyprofileActivity.this, RegistrationActivity.class);

                intent.putExtra("profile_name", name);
                intent.putExtra("profile_mail", mail);
                intent.putExtra("profile_address", address);
                intent.putExtra("profile_city", city);
                intent.putExtra("profile_district", district);
                intent.putExtra("profile_pincode", pincode);
                intent.putExtra("profile_state", state);
                intent.putExtra("profile_mobile", phone);
                intent.putExtra("profile_password", sharedPreferences.getString("userPwd", ""));

                startActivity(intent);
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setupActionBar() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_UpdateProfile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {

            finish();

        }

        return super.onOptionsItemSelected(item);


    }

    public void getData() {
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

        sharedPreferences = getSharedPreferences("LoginPREFERENCES", MODE_PRIVATE);

        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("BOD_SEQ_NO", SharedPreferenceUtils.getValue(MyprofileActivity.this, MyUtilities.PREF_BOD_SEQ_NO));
        Call<ServerResponse> userCall = apiInterface.getProfileResponse_(SharedPreferenceUtils.getValue(MyprofileActivity.this, MyUtilities.PREF_BOD_SEQ_NO));

        userCall.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {


                if (response.body() != null) {

                    if (response.body().getStatus() == true) {
                        MyUtilities.cancelAlertDialog(MyprofileActivity.this);

                        ContentProfile homePageBeanData = response.body().getData();
                        name = homePageBeanData.getNAME();
                        mail = homePageBeanData.getEMAILID();
                        phone = homePageBeanData.getMOBILENO();
                        status = homePageBeanData.getSTATUS();
                        bodSeqNo = homePageBeanData.getBODSEQNO();
                        city = homePageBeanData.getCITY();
                        address = homePageBeanData.getADDRESS();
                        state = homePageBeanData.getSTATE();
                        district = homePageBeanData.getDISTRICT();
                        pincode = homePageBeanData.getPINCODENO();

                        tvUMobileNo.setText(phone);
                        tvUmail.setText(mail);
                        tvAddress.setText(address);
                        tvCity.setText(city);
                        tvState.setText(state);
                        tvDistrict.setText(district);
                        tvPincode.setText(pincode);
                        tvUname.setText(name);


                    } else {
                        MyUtilities.cancelAlertDialog(MyprofileActivity.this);

                        MyUtilities.showToast(MyprofileActivity.this, MyUtilities.KAlertDialogTitleError);
                    }


                    //Log.i("Baba", "Res ponse is " + homePageBeanData.toString());
                    //  Toast.makeText(Home.this,full,Toast.LENGTH_LONG).show();


                    //  tvAddress.setText(name);
                    //Log.e("responsee result", "onResponse: " + name + phone + city + homePageBeanData.getSTATUS());

                } else {

                    MyUtilities.cancelAlertDialog(MyprofileActivity.this);
                    MyUtilities.showToast(MyprofileActivity.this, MyUtilities.KAlertDialogTitleError);

                }
            }


            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                MyUtilities.showToast(MyprofileActivity.this,MyUtilities.KAlertDialogTitleError);
                MyUtilities.cancelAlertDialog(MyprofileActivity.this);

            }
        });
    }



}