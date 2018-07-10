package com.pacifyr.omhealthcare.network;

import com.ceino.utilitylibrary.model.response.ResponseMessage;
import com.pacifyr.omhealthcare.model.PostPacifyrRequest;

import okhttp3.Response;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Part;
/**
 * Created by samsad on 7/6/18.
 */

public interface ApiService {
    @Multipart
    @POST("requestAccess")
    @Headers("enctype: multipart/form-data")
    Call<ResponseMessage> uploadFile(@Part MultipartBody.Part[] docFile, @Part MultipartBody.Part profileImage, @Part("data") RequestBody description);
}
