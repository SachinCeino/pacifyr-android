package com.pacifyr.client.network;

import com.pacifyrapp.utilitylibrary.model.response.ResponseMessage;

import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by samsad on 7/6/18.
 */

public interface ApiService {
    @Multipart
    @POST("requestAccess")
    @Headers("enctype: multipart/form-data")
    Call<ResponseMessage> uploadFile(@Part MultipartBody.Part[] docFile, @Part MultipartBody.Part profileImage, @Part("data") RequestBody description);
}
