package com.pacifyr.omhealthcare.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static com.ceino.utilitylibrary.network.UtilityNetworkService.GLOBAL_URL;

/**
 * Created by ceino on 29/5/18.
 */

public class ApiClient {

    public static final String BASE_URL = GLOBAL_URL;
    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
