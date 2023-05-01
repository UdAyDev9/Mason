package narasimhaa.com.mitraservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.List;

import narasimhaa.com.mitraservice.Adapater.AllUserListAdminAdapter;
import narasimhaa.com.mitraservice.Model.MaterialFilter.DataItem;
import narasimhaa.com.mitraservice.Model.MaterialFilter.MaterialFilterResponseFull;
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

public class NewAllUsersListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    AllUserListAdminAdapter allUsersAdapter;
    RecyclerView.LayoutManager layoutManager;
    private Toolbar toolbar;
    private TextView tvNoData;
    private Button deleteBtn;
    private List<DataItem> data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_all_users_list);
        recyclerView = findViewById(R.id.recycler_view);
        getAllUsers(false);
        toolbar = (Toolbar) findViewById(R.id.toolbar_extra);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Dealer List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(NewAllUsersListActivity.this);
        recyclerView.setLayoutManager(layoutManager);

    }

    public void getAllUsers(boolean isDeveloper) {
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

        paramObject.addProperty("BOD_SEQ_NO", SharedPreferenceUtils.getValue(NewAllUsersListActivity.this, MyUtilities.PREF_BOD_SEQ_NO));

        Call<MaterialFilterResponseFull> userCall;

            userCall = apiInterface.getAllUsers("Dealer","");

        userCall.enqueue(new Callback<MaterialFilterResponseFull>() {
            @Override
            public void onResponse(Call<MaterialFilterResponseFull> call, Response<MaterialFilterResponseFull> response) {


                if (response.body() != null) {

                    if (response.body().isStatus() == true) {

                        MyUtilities.cancelAlertDialog(NewAllUsersListActivity.this);
                        data = response.body().getData();
                        if (data.size()>0){
                            recyclerView.setVisibility(View.VISIBLE);
                            allUsersAdapter = new AllUserListAdminAdapter(data, NewAllUsersListActivity.this);
                            recyclerView.setAdapter(allUsersAdapter);
                            //allUsersAdapter.notifyDataSetChanged();

                        }else {

                            tvNoData.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }

                    } else {
                        MyUtilities.cancelAlertDialog(NewAllUsersListActivity.this);

                        MyUtilities.showToast(NewAllUsersListActivity.this, MyUtilities.KAlertDialogTitleError);
                    }

                } else {

                    MyUtilities.cancelAlertDialog(NewAllUsersListActivity.this);
                    MyUtilities.showToast(NewAllUsersListActivity.this, MyUtilities.KAlertDialogTitleError);

                }
            }


            @Override
            public void onFailure(Call<MaterialFilterResponseFull> call, Throwable t) {

                Log.e("erro", "onFailure: " + t.getMessage());
                MyUtilities.showToast(NewAllUsersListActivity.this, MyUtilities.KAlertDialogTitleError);
                MyUtilities.cancelAlertDialog(NewAllUsersListActivity.this);

            }
        });
    }

}