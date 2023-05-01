package narasimhaa.com.mitraservice.apis;

import narasimhaa.com.mitraservice.Model.Result;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIService {

    @FormUrlEncoded
    @POST("login.php")
    Call<Result> userLogin(
            @Field("USERNAME") String email,
            @Field("PASSWORD") String password);

}
