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
import android.widget.Spinner;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import narasimhaa.com.mitraservice.Adapater.PlacesAutoCompleteAdapter;
import narasimhaa.com.mitraservice.Model.service.ServicesDataItem;
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


public class DevelopersFragment extends Fragment {


    Spinner spinner_services_names;
    Button btn_search;

    List<ServicesDataItem> dataList = new ArrayList<>();
    String selectedService,selectedCity="";
    private EditText et_city;
    private RecyclerView recyclerViewCities = null;

    private PlacesAutoCompleteAdapter mAutoCompleteAdapterCities = null;


    public DevelopersFragment() {
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
        View myView = inflater.inflate(R.layout.fragment_developers, container, false);
        spinner_services_names = myView.findViewById(R.id.spinner_services_names);
        et_city = myView.findViewById(R.id.et_city);
        recyclerViewCities = myView.findViewById(R.id.rv_cities);
        et_city.addTextChangedListener(filterTextWatcherCities);
        populateCities(myView);

        getData();

        spinner_services_names.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                selectedService = dataList.get(i).getSERVICE_NAME();
                //Toast.makeText(getActivity(),dataList.get(i).getID() , Toast.LENGTH_SHORT).show();
                Log.d("log_id", "onItemSelected: "+dataList.get(i).getID() + "  " + dataList.get(i).getSERVICE_NAME());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btn_search = myView.findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try{
                    selectedCity = et_city.getText().toString();

                    if (!selectedService.equals("")&&!selectedCity.equals("")){
                        Intent intent = new Intent(getContext(),SearchResultsDisplayActivity.class);
                        intent.putExtra("SERVICE_SEARCH",selectedService);
                        intent.putExtra("CITY_SEARCH",selectedCity);

                        SharedPreferenceUtils.setValue(getContext(),MyUtilities.PREF_SERVICE_SEARCH,selectedService);
                        SharedPreferenceUtils.setValue(getContext(),MyUtilities.PREF_CITY_SEARCH,selectedCity);
                        SharedPreferenceUtils.setValue(getContext(),MyUtilities.PREF_IS_FROM_ADMIN,MyUtilities.CONST_NO);

                        getContext().startActivity(intent);

                    }else {
                        MyUtilities.showToast(getContext(),"Please select service and city");
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
        Call<ServicesResponse> userCall = apiInterface.getServices();

        userCall.enqueue(new Callback<ServicesResponse>() {
            @Override
            public void onResponse(Call<ServicesResponse> call, Response<ServicesResponse> response) {


                if (response.body() != null) {

                    if (response.body().getStatus() == true){

                        MyUtilities.cancelAlertDialog(getContext());
                        dataList = response.body().getData();

                        List<String> tempList = new ArrayList<>();

                        for(int i = 0; i< dataList.size();i++){

                            tempList.add(dataList.get(i).getSERVICE_NAME());

                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                                android.R.layout.simple_spinner_item, tempList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        spinner_services_names.setAdapter(adapter);
                    }else {
                        MyUtilities.cancelAlertDialog(getContext());

                        MyUtilities.showToast(getContext(),MyUtilities.KAlertDialogTitleError);
                    }

                } else {

                    MyUtilities.cancelAlertDialog(getContext());
                    MyUtilities.showToast(getContext(),MyUtilities.KAlertDialogTitleError);

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

}