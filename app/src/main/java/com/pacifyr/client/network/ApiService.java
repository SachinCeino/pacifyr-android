package com.pacifyr.client.network;

import com.pacifyr.client.model.SessionPackInput;
import com.pacifyr.client.model.SessionPacks;
import com.pacifyrapp.utilitylibrary.model.response.ResponseMessage;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @Multipart
    @POST("requestAccess")
    @Headers("enctype: multipart/form-data")
    Call<ResponseMessage> uploadFile(@Part MultipartBody.Part[] docFile, @Part MultipartBody.Part profileImage, @Part("data") RequestBody description);

    @GET("sessionPack/subscription/list/{userId}")
    Call<ResponseMessage> getSessionBundleSubscriptions(@Header("AccessToken") String token, @Path("userId") String userId,
                                                        @Query("skip") int skip, @Query("limit") int limit);

    @GET("sessionPack/user/{providerId}")
    Call<SessionPacks> getSessionBundles(@Header("AccessToken") String token, @Path("providerId") String providerId,
                                         @Query("skip") int skip, @Query("limit") int limit);

    @POST("payment/sessionPack/subscribe")
    Call<ResponseMessage> subscribeSessionBundle(@Header("AccessToken") String token, @Body SessionPackInput body);

    @GET("sessionPack/subscription/list/{userId}")
    @Headers("Content-Type: application/json")
    Call<SessionPacks> getSubscribedSessionPacks(@Header("AccessToken") String token, @Path("userId") String userId,
                                                 @Query("skip") int skip, @Query("limit") int limit);
}
