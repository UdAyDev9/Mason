package narasimhaa.com.mitraservice;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.kinda.alert.KAlertDialog;

import java.util.ArrayList;
import java.util.List;

import narasimhaa.com.mitraservice.Adapater.PlacesAutoCompleteAdapter;
import narasimhaa.com.mitraservice.Model.MaterialDevelopers.ServicesDataItemSize;
import narasimhaa.com.mitraservice.Model.MaterialDevelopers.ServicesResponseSizeBrand;
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


public class MaterialFragment extends Fragment {

    Spinner spinner_service_types;

    Button btn_search;

    ImageView imgItemType;

    EditText et_city;

    List<ServicesDataItem> dataList = new ArrayList<>();
    List<ServicesDataItemSize> dataListSizeBrand = new ArrayList<>();
    List<ServicesDataItemSize> dataListSizeBrandOnly = new ArrayList<>();
    List<ServicesDataItemSize> dataListShapes = new ArrayList<>();
    List<ServicesDataItemSize> dataListSubCategories = new ArrayList<>();


    List<String> subList = new ArrayList<>();

    String uname, umobile, uemail, ustate, upincode, udistract, ucity, uservecetype, upassword,itemType,itemBusinessType;
    private Spinner spinnerBusinessType,spinnerBrands,spinnerSizes,spinnerShapes;
    private RecyclerView recyclerViewCities = null;
    private PlacesAutoCompleteAdapter mAutoCompleteAdapterCities = null;
    public MaterialFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_material, container, false);
        spinner_service_types = (Spinner) myView.findViewById(R.id.spinner_service_types);
        spinnerBrands = (Spinner) myView.findViewById(R.id.spinner_brands);
        spinnerSizes = (Spinner) myView.findViewById(R.id.spinner_sizes);
        spinnerShapes = (Spinner) myView.findViewById(R.id.spinner_shape);
        imgItemType = myView.findViewById(R.id.img_item_type);
        et_city = myView.findViewById(R.id.et_city);
        populateCities(myView);
        spinnerBusinessType = myView.findViewById(R.id.spinner_business_types);

       /* ArrayAdapter<CharSequence> businessTypesAdapter = ArrayAdapter.createFromResource(getContext(), R.array.business_types_array, android.R.layout.simple_spinner_dropdown_item);

        businessTypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerBusinessType.setAdapter(businessTypesAdapter);
        subList.add("Sri Chakra");
        subList.add("Sri Chakra");
*/
        getData();
        getBrandsData(false);
        getBrandsSizeData(false);
        getShapes(false);
        getSubCategories(false);
        spinner_service_types.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String type = dataList.get(i).getSERVICE_NAME();
                switch (type) {

                    case "AGGREGATE STONES":
                        imgItemType.setImageResource(R.drawable.aggregate_stones);
                        break;

                    case "BRICKS":
                        imgItemType.setImageResource(R.drawable.bricks_cement);
                        break;

                    case "CEMENT":
                        imgItemType.setImageResource(R.drawable.cement);
                        break;

                    case "COLORS":
                        imgItemType.setImageResource(R.drawable.paints);
                        break;

                    case "GRANITE":
                        imgItemType.setImageResource(R.drawable.granite);
                        break;

                    case "IRON":
                        imgItemType.setImageResource(R.drawable.iron);
                        break;

                    case "RED BRICKS":
                        imgItemType.setImageResource(R.drawable.bricks);
                        break;

                    case "SAND":
                        imgItemType.setImageResource(R.drawable.sand);
                        break;
                    default:

                }
                itemType = dataList.get(i).getSERVICE_NAME();
                Log.d("log_id", "onItemSelected: " + dataList.get(i).getID() + "  " + dataList.get(i).getSERVICE_NAME());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerBusinessType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                itemBusinessType = adapterView.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        btn_search = myView.findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ucity = et_city.getText().toString().trim();



                try {
                    if (itemType==null || itemType.equals("") || ucity ==null || ucity.equals("")) {

                        MyUtilities.showToast(getContext(),"Please select material type and city");

                    } else {


                        //addOrUpdateService(true);

                        Intent intent = new Intent(getContext(), MaterialFilteredResultsActivity.class);
                        SharedPreferenceUtils.setValue(getContext(),MyUtilities.PREF_SERVICE_SEARCH,itemType);
                        SharedPreferenceUtils.setValue(getContext(),MyUtilities.PREF_MATERIAL_BUSINESS_TYPE,itemBusinessType);
                        SharedPreferenceUtils.setValue(getContext(),MyUtilities.PREF_CITY_SEARCH,ucity);
                        //SharedPreferenceUtils.setValue(getContext(),MyUtilities.PREF_BRAND_SEARCH,ucity);
                        getContext().startActivity(intent);


                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
        return myView;
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


        JsonObject paramObject = new JsonObject();
        //paramObject.addProperty("EMAIL_ID", "krishna@appcare.co.in");
        paramObject.addProperty("BOD_SEQ_NO", SharedPreferenceUtils.getValue(getContext(), MyUtilities.PREF_BOD_SEQ_NO));
        //paramObject.put("PASSWORD", "12345");
           /* paramObject.put("USERNAME", et_mno.getText().toString().trim());
            paramObject.put("PASSWORD", et_password.getText().toString().trim());
*/
        Call<ServicesResponse> userCall = apiInterface.getMaterials();

        userCall.enqueue(new Callback<ServicesResponse>() {
            @Override
            public void onResponse(Call<ServicesResponse> call, Response<ServicesResponse> response) {


                if (response.body() != null) {

                    if (response.body().getStatus() == true) {

                        MyUtilities.cancelAlertDialog(getContext());
                        dataList = response.body().getData();

                        List<String> tempList = new ArrayList<>();

                        for (int i = 0; i < dataList.size(); i++) {

                            tempList.add(dataList.get(i).getSERVICE_NAME());

                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                                android.R.layout.simple_spinner_item, tempList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        spinner_service_types.setAdapter(adapter);
                    } else {
                        MyUtilities.cancelAlertDialog(getContext());

                        MyUtilities.showToast(getContext(), MyUtilities.KAlertDialogTitleError);
                    }

                } else {

                    MyUtilities.cancelAlertDialog(getContext());
                    MyUtilities.showToast(getContext(), MyUtilities.KAlertDialogTitleError);

                }
            }


            @Override
            public void onFailure(Call<ServicesResponse> call, Throwable t) {

                Log.e("erro", "onFailure: " + t.getMessage());
                MyUtilities.showToast(getContext(),MyUtilities.KAlertDialogTitleError);
                MyUtilities.cancelAlertDialog(getContext());

            }
        });
    }
    private void populateCities(View view) {

        if (!Places.isInitialized()) {
            Places.initialize(getContext(), MyUtilities.PLACES_API_KEY);

        }
        recyclerViewCities = view.findViewById(R.id.rv_cities);
        et_city.addTextChangedListener(filterTextWatcherCities);

        mAutoCompleteAdapterCities = new PlacesAutoCompleteAdapter(getContext(), TypeFilter.CITIES);
        final LinearLayoutManager layoutManagerCities = new LinearLayoutManager(getContext());
        final LinearLayoutManager layoutManagerAddress = new LinearLayoutManager(getContext());
        recyclerViewCities.setLayoutManager(new LinearLayoutManager(getContext()));
        mAutoCompleteAdapterCities.setClickListener(new PlacesAutoCompleteAdapter.ClickListener() {
            @Override
            public void click(Place place) {
                et_city.setText(place.getAddress());
                et_city.setSelection(et_city.getText().toString().length());
                recyclerViewCities.setVisibility(View.GONE);
                Log.d("plx", "place:" + place.getName());
            }
        });
        recyclerViewCities.setAdapter(mAutoCompleteAdapterCities);
        recyclerViewCities.addItemDecoration(new DividerItemDecoration(getContext(), layoutManagerCities.getOrientation()));

    }

    private TextWatcher filterTextWatcherCities = new TextWatcher() {
        public void afterTextChanged(Editable s) {
            if (s != null && !s.toString().equals("")) {
                mAutoCompleteAdapterCities.getFilter().filter(s.toString());
                if (et_city.getText().hashCode() == s.hashCode()) {
                    if (recyclerViewCities.getVisibility() == View.GONE) {
                        recyclerViewCities.setVisibility(View.VISIBLE);
                    }
                }
            } else {
                if (recyclerViewCities.getVisibility() == View.VISIBLE) {
                    recyclerViewCities.setVisibility(View.GONE);
                }
            }
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    };



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
        paramObject.addProperty("BOD_SEQ_NO", SharedPreferenceUtils.getValue(getContext(), MyUtilities.PREF_BOD_SEQ_NO));

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

                        MyUtilities.cancelAlertDialog(getContext());


                        dataListSizeBrandOnly = response.body().getData();

                        List<String> tempList = new ArrayList<>();

                        for (int i = 0; i < dataListSizeBrandOnly.size(); i++) {

                            tempList.add(dataListSizeBrandOnly.get(i).getSERVICE_NAME());

                        }


                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                                android.R.layout.simple_spinner_item, tempList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        spinnerBrands.setAdapter(adapter);


                    } else {
                        MyUtilities.cancelAlertDialog(getContext());

                        MyUtilities.showToast(getContext(), MyUtilities.KAlertDialogTitleError);
                    }

                } else {

                    MyUtilities.cancelAlertDialog(getContext());
                    MyUtilities.showToast(getContext(), MyUtilities.KAlertDialogTitleError);

                }
            }


            @Override
            public void onFailure(Call<ServicesResponseSizeBrand> call, Throwable t) {

                Log.e("erro", "onFailure: " + t.getMessage());
                MyUtilities.showToast(getContext(), MyUtilities.KAlertDialogTitleError);
                MyUtilities.cancelAlertDialog(getContext());

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
        paramObject.addProperty("BOD_SEQ_NO", SharedPreferenceUtils.getValue(getContext(), MyUtilities.PREF_BOD_SEQ_NO));

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

                        MyUtilities.cancelAlertDialog(getContext());


                        dataListSizeBrand = response.body().getData();

                        List<String> tempList = new ArrayList<>();

                        for (int i = 0; i < dataListSizeBrand.size(); i++) {

                            tempList.add(dataListSizeBrand.get(i).getSERVICE_NAME());


                        }


                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                                android.R.layout.simple_spinner_item, tempList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        spinnerSizes.setAdapter(adapter);


                    } else {
                        MyUtilities.cancelAlertDialog(getContext());

                        MyUtilities.showToast(getContext(), MyUtilities.KAlertDialogTitleError);
                    }

                } else {

                    MyUtilities.cancelAlertDialog(getContext());
                    MyUtilities.showToast(getContext(), MyUtilities.KAlertDialogTitleError);

                }
            }


            @Override
            public void onFailure(Call<ServicesResponseSizeBrand> call, Throwable t) {

                Log.e("erro", "onFailure: " + t.getMessage());
                MyUtilities.showToast(getContext(), MyUtilities.KAlertDialogTitleError);
                MyUtilities.cancelAlertDialog(getContext());

            }
        });
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
        paramObject.addProperty("BOD_SEQ_NO", SharedPreferenceUtils.getValue(getContext(), MyUtilities.PREF_BOD_SEQ_NO));

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

                        MyUtilities.cancelAlertDialog(getContext());


                        dataListShapes= response.body().getData();

                        List<String> tempList = new ArrayList<>();

                        for (int i = 0; i < dataListShapes.size(); i++) {

                            tempList.add(dataListShapes.get(i).getSERVICE_NAME());


                        }


                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                                android.R.layout.simple_spinner_item, tempList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        spinnerShapes.setAdapter(adapter);


                    } else {
                        MyUtilities.cancelAlertDialog(getContext());

                        MyUtilities.showToast(getContext(), MyUtilities.KAlertDialogTitleError);
                    }

                } else {

                    MyUtilities.cancelAlertDialog(getContext());
                    MyUtilities.showToast(getContext(), MyUtilities.KAlertDialogTitleError);

                }
            }


            @Override
            public void onFailure(Call<ServicesResponseSizeBrand> call, Throwable t) {

                Log.e("erro", "onFailure: " + t.getMessage());
                MyUtilities.showToast(getContext(), MyUtilities.KAlertDialogTitleError);
                MyUtilities.cancelAlertDialog(getContext());

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
        paramObject.addProperty("BOD_SEQ_NO", SharedPreferenceUtils.getValue(getContext(), MyUtilities.PREF_BOD_SEQ_NO));

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

                        MyUtilities.cancelAlertDialog(getContext());


                        dataListSubCategories= response.body().getData();

                        List<String> tempList = new ArrayList<>();

                        for (int i = 0; i < dataListSubCategories.size(); i++) {

                            tempList.add(dataListSubCategories.get(i).getSERVICE_NAME());


                        }


                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                                android.R.layout.simple_spinner_item, tempList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        spinnerBusinessType.setAdapter(adapter);


                    } else {
                        MyUtilities.cancelAlertDialog(getContext());

                        MyUtilities.showToast(getContext(), MyUtilities.KAlertDialogTitleError);
                    }

                } else {

                    MyUtilities.cancelAlertDialog(getContext());
                    MyUtilities.showToast(getContext(), MyUtilities.KAlertDialogTitleError);

                }
            }


            @Override
            public void onFailure(Call<ServicesResponseSizeBrand> call, Throwable t) {

                Log.e("erro", "onFailure: " + t.getMessage());
                MyUtilities.showToast(getContext(), MyUtilities.KAlertDialogTitleError);
                MyUtilities.cancelAlertDialog(getContext());

            }
        });
    }


    private void addOrUpdateService(boolean isFromUpdate){

        MyUtilities.showAlertDialog(getActivity(), KAlertDialog.PROGRESS_TYPE,MyUtilities.KAlertDialogTitleLoding);

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

            jsonReg.addProperty("NAME", "");
            jsonReg.addProperty("CONTACT_NO", "");
            jsonReg.addProperty("EMAIL_ID", SharedPreferenceUtils.getValue(getContext(),MyUtilities.PREF_BOD_SEQ_NO));
            jsonReg.addProperty("STATE", "");
            jsonReg.addProperty("DISTRICT", "");
            jsonReg.addProperty("CITY", "");
            jsonReg.addProperty("PINCODE", "");
            jsonReg.addProperty("WITH_IN_RANGE", "");
            jsonReg.addProperty("EXPERIENCE", "");
            jsonReg.addProperty("REGISTERED_BY", "");
            jsonReg.addProperty("CERTIFICATE", "");
            jsonReg.addProperty("SERVICE_NAME", itemType);
            jsonReg.addProperty("QUALIFICATION", "");

            Call<ServerResponse> userCall ;
            if (isFromUpdate==true){
                userCall = apiInterface.updateUser(jsonReg.toString());

            }else {
                userCall = apiInterface.addService(jsonReg.toString());
            }

            userCall.enqueue(new Callback<ServerResponse>() {
                @Override
                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {


                    if (response.body() != null) {


                        if(response.body().getStatus()==true){

                            MyUtilities.showToast(getActivity(), "Updated successfully!!!");

                            MyUtilities.cancelAlertDialog(getActivity());

                        } else {

                            MyUtilities.showToast(getActivity(), response.body().getMsg().toString());
                            MyUtilities.cancelAlertDialog(getActivity());

                        }


                    } else {

                        MyUtilities.cancelAlertDialog(getActivity());

                        MyUtilities.showToast(getActivity(), "Something went wrong! Please try later");

                    }


                }

                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {
                    MyUtilities.showToast(getContext(),MyUtilities.KAlertDialogTitleError);
                    MyUtilities.cancelAlertDialog(getContext());

                    Log.e("erro", "onFailure: " + t.getMessage());
                }
            });
            //samreg.put("servicetype", sp_servicertype);
        } catch (Exception e) {
            e.printStackTrace();
            MyUtilities.cancelAlertDialog(getActivity());

        }


    }


}