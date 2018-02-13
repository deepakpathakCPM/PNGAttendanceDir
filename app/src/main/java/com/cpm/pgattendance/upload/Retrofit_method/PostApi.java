package com.cpm.pgattendance.upload.Retrofit_method;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/*import retrofit.http.Body;
import retrofit.http.POST;*/


/**
 * Created by upendrak on 16-05-2017.
 */

public interface PostApi {

    @POST("LoginDetaillatest")
    Call<ResponseBody> getLogindetail(@Body RequestBody request);
   /* @Multipart
    @POST("Uploadimages")
    Call<ResponseBody> getUploadImage(@Body RequestBody request);*/

   /* @POST("Uploadimages")
    Call<String> getUploadImage(@Body RequestBody request);*/

    @retrofit.http.POST("Uploadimages")
    retrofit.Call<String> getUploadImageRetrofitOne(@retrofit.http.Body com.squareup.okhttp.RequestBody body1);

    @Multipart
    @POST("Uploadimages")
    Call<String> getUploadImage(@Part MultipartBody.Part file, @Part("file") RequestBody filename, @Part("Foldername") RequestBody foldername);

    @POST("DownloadAll")
    Call<ResponseBody> getDownloadAll(@Body RequestBody request);

    @POST("CoverageDetail_latest")
    Call<ResponseBody> getCoverageDetail(@Body RequestBody request);

    @POST("UploadJCPDetail")
    Call<ResponseBody> getUploadJCPDetail(@Body RequestBody request);

    @POST("UploadJsonDetail")
    Call<ResponseBody> getUploadJsonDetail(@Body RequestBody request);

    @POST("CoverageStatusDetail")
    Call<ResponseBody> getCoverageStatusDetail(@Body RequestBody request);

    @POST("Upload_StoreGeoTag_IMAGES")
    Call<ResponseBody> getGeoTagImage(@Body RequestBody request);

    @POST("CheckoutDetail")
    Call<ResponseBody> getCheckout(@Body RequestBody request);

    @POST("DeleteCoverage")
    Call<ResponseBody> deleteCoverageData(@Body RequestBody request);

    @POST("CoverageNonworking")
    Call<ResponseBody> setCoverageNonWorkingData(@Body RequestBody request);
}
