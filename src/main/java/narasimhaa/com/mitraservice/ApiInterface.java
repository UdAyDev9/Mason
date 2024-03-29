package narasimhaa.com.mitraservice;

import narasimhaa.com.mitraservice.Model.AllOrdersResponse;
import narasimhaa.com.mitraservice.Model.Filter.FilterResponseFull;
import narasimhaa.com.mitraservice.Model.ImagesResponse;
import narasimhaa.com.mitraservice.Model.MaterialDevelopers.MaterialDevelopersServerResponse;
import narasimhaa.com.mitraservice.Model.MaterialDevelopers.ServicesResponseSizeBrand;
import narasimhaa.com.mitraservice.Model.MaterialFilter.MaterialFilterResponseFull;
import narasimhaa.com.mitraservice.Model.OrdersDataItem;
import narasimhaa.com.mitraservice.Model.ServerResponse;
import narasimhaa.com.mitraservice.Model.ServicesResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterface {

    String URL_BASE = "http://65.1.178.54/app/index.php/";

    @POST("Users/login.php")
    Call<ServerResponse> getLoginResponsee(@Body String body);

    @POST("Users/validateOTP")
    Call<ServerResponse> otpValidate(@Body String body);


    @POST("Users/getDetails.php")
    Call<ServerResponse> getProfileResponse(@Body String body);

    @GET("Users/getDetails.php")
    Call<ServerResponse> getProfileResponse_(@Query("BOD_SEQ_NO") String body);

    @GET("Staff/getDetails.php")
    Call<ServerResponse> getStaffProfileResponse(@Query("EMAIL_ID") String body);

    @GET("Users/getImages.php")
    Call<ImagesResponse> getImagesResponse(@Query("EMAIL_ID") String body);

    @Multipart
    @POST("Users/uploadImages")
    Call<ServerResponse> uploadImagesToServer(@Part MultipartBody.Part  part, @Part("EMAIL_ID") RequestBody EMAIL_ID);

    @GET("Services/getAllActiveServiceNames")
    Call<ServicesResponse> getServices();

    @GET("Services/getAllActiveServiceTypes")
    Call<ServicesResponse> getServiceTypes();

    @GET("Admin/getMaterials")
    Call<ServicesResponse> getMaterials();

    @GET("Admin/getSizes")
    Call<ServicesResponseSizeBrand> getServiceTypeSize();

    // shape// HEIGHT

    @GET("Admin/getHeights")
    Call<ServicesResponseSizeBrand> getShapes();

    @POST("Admin/insertHeight")
    Call<ServerResponse> addShape(@Body String body);

    @POST("Admin/deleteHeight")
    Call<ServicesResponseSizeBrand> deleteShape(@Body String body);

    // sub categeory// BTYPE

    @GET("Admin/getBusinessTypes")
    Call<ServicesResponseSizeBrand> getSubCategories();

    @POST("Admin/insertBusinessType")
    Call<ServerResponse> addSubCategory(@Body String body);

    @POST("Admin/deleteBusinessType")
    Call<ServicesResponseSizeBrand> deleteSubCatagory(@Body String body);


    @GET("Admin/getBrands")
    Call<ServicesResponseSizeBrand> getServiceTypesBrands();

    @POST("Admin/deleteSize")
    Call<ServicesResponseSizeBrand> deleteSize(@Body String body);

    @POST("Admin/deleteBrand")
    Call<ServicesResponseSizeBrand> deleteBrand(@Body String body);

    @POST("Admin/deleteMaterial")
    Call<ServicesResponse> deleteMatril(@Body String body);


    @GET("Staff/getFilteredServicePersons")
    Call<FilterResponseFull> getFilteredList(@Query("SERVICE_NAME") String SERVICE_NAME, @Query("CITY") String CITY);

   /* @GET("Staff/getFilteredServicePersons")
    Call<FilterResponseFull> getFilteredList(@Query("SERVICE_NAME") String SERVICE_NAME, @Query("CITY") String CITY,@Query("SIZE") String SIZE);
*/
    @GET("Services/getFilteredServiceTypePersons")
    Call<MaterialFilterResponseFull> getMaterialFilteredList(@Query("SERVICE_TYPE") String SERVICE_NAME, @Query("CITY") String CITY , @Query("BUSINESS_TYPE") String BUSINESS_TYPE);

    @GET("Services/getAllActiveServiceTypes")
    Call<MaterialDevelopersServerResponse> getMaterialDevelopersList(@Query("EMAIL_ID") String EMAIL_ID);

    @POST("Staff/changeStatus")
    Call<ServerResponse> changeStatus(@Body String body);

    @POST("Users/changeStatus")
    Call<ServerResponse> changeUserStatus(@Body String body);


    @POST("Users/deleteImage")
    Call<ServerResponse> deleteImage(@Body String body);


    @POST("Users/register")
    Call<ServerResponse> registerUser(@Body String body);

    @POST("Staff/register")
    Call<ServerResponse> addService(@Body String body);

    @POST("Staff/update")
    Call<ServerResponse> updateService(@Body String body);

    @POST("Users/update")
    Call<ServerResponse> updateUser(@Body String body);

    @POST("Services/insertType")
    Call<ServerResponse> addMaterialType(@Body String body);

    @POST("Services/updateType")
    Call<ServerResponse> updateMaterialType(@Body String body);

    /*@Headers("Content-Type: application/json")
    @POST("servicePersonDataInsertUpdate.php")
    Call<ServerResponse> addService(@Body String body);*/

    @Headers("Content-Type: application/json")
    @POST("updateAvailability.php")
    Call<ServerResponse> updateAvailability(@Body String body);

    @POST("Users/forgotPassword")
    Call<ServerResponse> forgotPassword(@Body String body);

    @GET("Users/getAllUsers")
    Call<MaterialFilterResponseFull> getAllUsers(@Query("USER_TYPE") String USER_TYPE);

    @POST("Admin/insertSizeBrand")
    Call<ServerResponse> insertSizeAndBrand(@Body String body);

    @POST("Admin/insertMaterial")
    Call<ServerResponse> insertMaterial(@Body String body);

    @POST("Admin/deleteMaterial")
    Call<ServicesResponseSizeBrand> deleteMaterial(@Body String body);

    @POST("Orders/insert")
    Call<ServerResponse> placeOrder(@Body String body);

    @GET("Orders/getDetails")
    Call<AllOrdersResponse> getAllOrders(@Query("EMAIL_ID") String EMAIL_ID, @Query("COMING_FROM") String COMING_FROM);
// order status // ORD_ID
    @POST("Orders/changeStatus")
    Call<ServerResponse> changeOrderStatus(@Body String body);

}
