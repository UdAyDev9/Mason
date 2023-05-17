package narasimhaa.com.mitraservice;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;


import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.kinda.alert.KAlertDialog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import narasimhaa.com.mitraservice.Adapater.PlacesAutoCompleteAdapter;
import narasimhaa.com.mitraservice.Listener.GenericSelectListner;
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

public class RegistrationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText et_username, et_email, et_mbl, et_state, et_pincode, et_distract, et_City, et_password, et_name, etAddress;
    private Spinner serviceType, districtSpinner, stateSpinner;
    private Toolbar toolbar;
    RegistrationActivity context;

    private String selectedItem = "", selectedDistrict = "", selectedState = "";
    Button bt_reg;
    String uname, umobile, uemail, ustate, upincode, udistract, ucity, uservecetype, upassword;
    String respone = "";
    private RecyclerView rvCities = null,rvAddress= null;
    private PlacesAutoCompleteAdapter mAutoCompleteAdapterCities = null;

    JSONObject jsonoBject = null;
    private GenericSelectListner listner;
    private LinearLayout districtSpinnerLayout;
    private boolean isFromUpdate = false;
    private TextView tvIam;
    private LinearLayout llUserType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        context = this;
        //setupActionBar();
        Bundle bundle = getIntent().getExtras();

        et_username = (EditText) findViewById(R.id.et_name);
        et_email = (EditText) findViewById(R.id.et_email);
        et_mbl = (EditText) findViewById(R.id.et_mbl);
        et_state = (EditText) findViewById(R.id.et_state);
        et_pincode = (EditText) findViewById(R.id.et_pincode);
        et_distract = (EditText) findViewById(R.id.et_distract);
        et_City = (EditText) findViewById(R.id.et_city);
        et_password = (EditText) findViewById(R.id.et_password);
        etAddress = (EditText) findViewById(R.id.et_address);
        serviceType = (Spinner) findViewById(R.id.sp_servicertype);
        districtSpinner = (Spinner) findViewById(R.id.district_spinner);
        stateSpinner = (Spinner) findViewById(R.id.state_spinner);
        tvIam = (TextView) findViewById(R.id.tv_iam);
        llUserType = (LinearLayout) findViewById(R.id.ll_user_type);
        districtSpinnerLayout = (LinearLayout) findViewById(R.id.district_spinner_layout);
        stateSpinner.setOnItemSelectedListener(this);

        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar_extra);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Registration");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bt_reg = (Button) findViewById(R.id.bt_reg);
        et_name = (EditText) findViewById(R.id.et_name);
        serviceType = (Spinner) findViewById(R.id.sp_servicertype);
        populateCities();

        if (bundle != null) {

            isFromUpdate = true;
            et_username.setText(bundle.getString("profile_name"));
            et_email.setText(bundle.getString("profile_mail"));
            etAddress.setText(bundle.getString("profile_address"));
            et_City.setText(bundle.getString("profile_city"));
            //et_distract.setText(bundle.getString("profile_district"));
            et_pincode.setText(bundle.getString("profile_pincode"));
            // et_state.setText(bundle.getString("profile_state"   ));
            et_mbl.setText(bundle.getString("profile_mobile"));
            et_password.setText(bundle.getString("profile_password"));
            getSupportActionBar().setTitle("Update Profile");
            et_email.setFocusable(false);
            tvIam.setVisibility(View.GONE);
            llUserType.setVisibility(View.GONE);
            bt_reg.setText("Update");

        }


        districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(parent.getContext(), "Selected: " + districtSpinner.getSelectedItem().toString(), Toast.LENGTH_LONG).show();

                selectedDistrict = districtSpinner.getSelectedItem().toString();
                Log.d("spinner", "onItemSelected: " + districtSpinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(parent.getContext(), "Nothingfgggg: ", Toast.LENGTH_LONG).show();
                selectedDistrict = "";
            }
        });


        bt_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uname = et_username.getText().toString().trim();
                //et_username.setError("dsdsf");
                umobile = et_mbl.getText().toString().trim();
                ustate = et_state.getText().toString().trim();
                uemail = et_email.getText().toString().trim();
                upincode = et_pincode.getText().toString().trim();
                udistract = et_distract.getText().toString().trim();
                ucity = et_City.getText().toString().trim();
                upassword = et_password.getText().toString().trim();

                try {
                    if (isFromUpdate) {

                        if (uname.equals("") || umobile.equals("") || uemail.equals("") || upincode.equals("") || upassword.equals("") || et_City.getText().toString().equals("") || etAddress.getText().toString().equals("")) {

                            Toast.makeText(RegistrationActivity.this, "Please Fill All Fields", Toast.LENGTH_LONG).show();

                        }else {
                            registerOrUpdate(isFromUpdate);

                        }
                    }else{

                        if (uname.equals("") || umobile.equals("") || uemail.equals("") || upincode.equals("") || upassword.equals("") || et_City.getText().toString().equals("") || etAddress.getText().toString().equals("") || selectedItem.equals("")) {
                            Toast.makeText(RegistrationActivity.this, "Please Fill All Fields", Toast.LENGTH_LONG).show();

                        } else {

                            registerOrUpdate(isFromUpdate);

                        }

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


        List<String> categories = new ArrayList<String>();
        //categories.add("Material Supplier");
        //categories.add("Developer");
        categories.add("Retailer");
        categories.add("Admin");
        categories.add("Dealer");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categories);

        //  dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        serviceType.setAdapter(dataAdapter);
        //Toast.makeText(RegistrationActivity.this, "Selected: " + selectedItem, Toast.LENGTH_LONG).show();

        serviceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedItem = serviceType.getSelectedItem().toString();
                // Toast.makeText(RegistrationActivity.this, " " +selectedItem, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });


        ArrayAdapter<CharSequence> stateAdapter = ArrayAdapter.createFromResource(this, R.array.state_array, android.R.layout.simple_spinner_dropdown_item);
        stateSpinner.setAdapter(stateAdapter);


    }

    //DropDown dropDown=new DropDown();


    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);

            actionBar.setTitle(context.getResources().getString(R.string.title_Registration));

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                break;


            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    public class Reg extends AsyncTask<String, String, String> {

        String url = "http://androindian.com/apps/example_app/api.php";

        @Override
        protected String doInBackground(String... params) {

            jsonoBject = JsonFunction.getJsonFromUrlparam(url, params[0]);
            Log.i("json", "" + jsonoBject);
            return String.valueOf(jsonoBject);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(jsonoBject.toString());
                respone = jsonObject.getString("response");

                if (respone.trim().equals("success")) {

                    String status = jsonObject.getString("user");
                    Toast.makeText(getApplicationContext(), "" + status, Toast.LENGTH_LONG).show();

                    Intent regintent = new Intent(RegistrationActivity.this, ValidateOtpActivity.class);
                    regintent.putExtra("KEY_MOBILE_NO",umobile);
                    startActivity(regintent);

                } else if (respone.trim().equals("failed")) {

                    String status1 = jsonObject.getString("user");
                    Toast.makeText(getApplicationContext(), "" + status1, Toast.LENGTH_LONG).show();


                } else if (respone.trim().equals("error")) {

                    String status2 = jsonObject.getString("user");
                    Toast.makeText(getApplicationContext(), "" + status2, Toast.LENGTH_LONG).show();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void populateCities() {

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), MyUtilities.PLACES_API_KEY);
        }

        rvCities = findViewById(R.id.rv_cities);
        rvAddress = findViewById(R.id.rv_address);
        et_City.addTextChangedListener(mTextWatcherCities);

        mAutoCompleteAdapterCities = new PlacesAutoCompleteAdapter(this, TypeFilter.CITIES);
        final LinearLayoutManager layoutManagerCities = new LinearLayoutManager(this);
        final LinearLayoutManager layoutManagerAddress = new LinearLayoutManager(this);
        rvCities.setLayoutManager(new LinearLayoutManager(this));
        mAutoCompleteAdapterCities.setClickListener(new PlacesAutoCompleteAdapter.ClickListener() {
            @Override
            public void click(Place place) {
                et_City.setText(place.getAddress());
                rvCities.setVisibility(View.GONE);
                Log.d("plx", "place:" + place.getName());
            }
        });
        rvCities.setAdapter(mAutoCompleteAdapterCities);
        rvCities.addItemDecoration(new DividerItemDecoration(this, layoutManagerCities.getOrientation()));

    }

    private TextWatcher mTextWatcherCities = new TextWatcher() {
        public void afterTextChanged(Editable s) {
            if (s != null && !s.toString().equals("")) {
                mAutoCompleteAdapterCities.getFilter().filter(s.toString());
                if (et_City.getText().hashCode() == s.hashCode()) {
                    if (rvCities.getVisibility() == View.GONE) {
                        rvCities.setVisibility(View.VISIBLE);
                    }
                }
            } else {
                if (rvCities.getVisibility() == View.VISIBLE) {
                    rvCities.setVisibility(View.GONE);
                }
            }
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    };

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item

        parent.getItemAtPosition(position);
        if (position == 0) {

            districtSpinner.setEnabled(false);
            selectedDistrict = "";
            selectedState = "";
            Log.d("spinner nothing", "onItemSelected: " + selectedDistrict + "State" + selectedState);
        } else if (position == 1) {
            String item = parent.getItemAtPosition(position).toString();
            selectedState = item;

            districtSpinnerLayout.setVisibility(View.VISIBLE);
            districtSpinner.setEnabled(true);

            ArrayAdapter<CharSequence> districtAdapter = ArrayAdapter.createFromResource(this, R.array.ap_city_array, android.R.layout.simple_spinner_dropdown_item);
            districtSpinner.setAdapter(districtAdapter);
            Log.d("spinner ap", "onItemSelected: " + selectedState);


        } else if (position == 2) {

            districtSpinnerLayout.setVisibility(View.VISIBLE);
            districtSpinner.setEnabled(true);

            String items = parent.getItemAtPosition(position).toString();
            selectedState = items;
            Log.d("spinner ts", "onItemSelected: " + selectedState);


            ArrayAdapter<CharSequence> districtAdapter = ArrayAdapter.createFromResource(this, R.array.ts_city_array, android.R.layout.simple_spinner_dropdown_item);
            districtSpinner.setAdapter(districtAdapter);


        }

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    private void registerOrUpdate(boolean isFromUpdate) {

        MyUtilities.showAlertDialog(RegistrationActivity.this, KAlertDialog.PROGRESS_TYPE, MyUtilities.KAlertDialogTitleLoding);

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

            jsonReg.addProperty("NAME", et_name.getText().toString());
            jsonReg.addProperty("MOBILE_NO", umobile);
            jsonReg.addProperty("EMAIL_ID", uemail);
            jsonReg.addProperty("STATE", selectedState);
            jsonReg.addProperty("DISTRICT", selectedDistrict);
            jsonReg.addProperty("CITY", et_City.getText().toString());
            jsonReg.addProperty("PINCODE_NO", upincode);
            jsonReg.addProperty("PASSWORD", upassword);
            jsonReg.addProperty("USER_TYPE", selectedItem);
            jsonReg.addProperty("ADDRESS", etAddress.getText().toString());
            jsonReg.addProperty("BUSINESS_NAME", "");
            if (isFromUpdate == true) {
                jsonReg.addProperty("BOD_SEQ_NO", SharedPreferenceUtils.getValue(RegistrationActivity.this, MyUtilities.PREF_BOD_SEQ_NO));

            }

            Log.d("ssss", "onClick: " + selectedItem);

            Call<ServerResponse> userCall;
            if (isFromUpdate == true) {
                userCall = apiInterface.updateUser(jsonReg.toString());

            } else {
                userCall = apiInterface.registerUser(jsonReg.toString());
            }

            userCall.enqueue(new Callback<ServerResponse>() {
                @Override
                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {

                         /*   if (response.body().getStatus().equals("1")){

                            }
*/
                    if (response.body() != null) {


                        if (response.body().getStatus() == true) {

                            if (isFromUpdate){

                                MyUtilities.showToast(RegistrationActivity.this, "Updated Successfully!!!");

                            }else {

                                MyUtilities.showToast(RegistrationActivity.this, "Registered Successfully!!!");

                            }

                            MyUtilities.cancelAlertDialog(RegistrationActivity.this);
                            Log.d("responsee", "onResponse: " + response.body().getStatus());
                            Log.e("responsee", "onResponse: " + response.body().getMsg());

                            Intent regintent = new Intent(RegistrationActivity.this, ValidateOtpActivity.class);
                            regintent.putExtra("KEY_MOBILE_NO",umobile);
                            startActivity(regintent);

                            finish();
                        } else {

                            MyUtilities.showToast(RegistrationActivity.this, response.body().getMsg().toString());
                            MyUtilities.cancelAlertDialog(RegistrationActivity.this);

                        }


                    } else {

                        MyUtilities.cancelAlertDialog(RegistrationActivity.this);

                        MyUtilities.showToast(RegistrationActivity.this, "Something went wrong! Please try later");

                    }


                }

                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {
                    MyUtilities.showToast(RegistrationActivity.this,MyUtilities.KAlertDialogTitleError);
                    MyUtilities.cancelAlertDialog(RegistrationActivity.this);
                }
            });
            //samreg.put("servicetype", sp_servicertype);
        } catch (Exception e) {
            e.printStackTrace();
            MyUtilities.cancelAlertDialog(RegistrationActivity.this);

        }


    }

}



