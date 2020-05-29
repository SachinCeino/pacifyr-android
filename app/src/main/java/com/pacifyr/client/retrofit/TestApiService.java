package com.pacifyr.client.retrofit;

import com.google.gson.JsonObject;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by samsad on 7/6/18.
 */

public interface TestApiService {

    @Multipart
    @POST("upload/file")
    @Headers("enctype: multipart/form-data")
    Call<JsonObject> editProfileWithDocuments(@Part("file") RequestBody file,
                                              @Part("format") RequestBody format,
                                              @Header("AccessToken") String auth);




}
