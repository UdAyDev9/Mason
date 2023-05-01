package narasimhaa.com.mitraservice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.JsonObject;

import narasimhaa.com.mitraservice.Model.ServerResponse;
import narasimhaa.com.mitraservice.Utility.MyUtilities;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }

    public void getData(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiInterface.URL_BASE)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);


        // prepare call in Retrofit 2.0
        try {
            JsonObject paramObject = new JsonObject();
                    paramObject.addProperty("EMAIL_ID", "krishna@appcare.aco.in");
                    //paramObject.put("PASSWORD", "12345");
           /* paramObject.put("USERNAME", et_mno.getText().toString().trim());
            paramObject.put("PASSWORD", et_password.getText().toString().trim());
*/
            Call<ServerResponse> userCall = apiInterface.getProfileResponse(paramObject.toString());

            userCall.enqueue(new Callback<ServerResponse>() {
                @Override
                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {

                         /*   if (response.body().getStatus().equals("1")){


                            }
*/
                    Log.e("responsee", "onResponse: "+response.body().getStatus());
                    Log.e("responsee", "onResponse: "+response.body());

                }

                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {

                    MyUtilities.showToast(ProfileActivity.this,MyUtilities.KAlertDialogTitleError);
                    MyUtilities.cancelAlertDialog(ProfileActivity.this);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

}
