package com.pacifyr.client.network;

import android.content.Context;

import com.pacifyr.client.model.SessionPackInput;
import com.pacifyr.client.model.SessionPacks;
import com.pacifyrapp.utilitylibrary.model.response.ResponseMessage;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;

import retrofit2.Callback;

public class WebService {

    private final Context context;
    private final String accessToken;
    private final String userId;
    private final ApiService client;

    public WebService(Context context) {
        this.context = context;
        this.accessToken = PrefManager.getInstance(context).getAccessToken();
        this.userId = PrefManager.getInstance(context).getUserID();
        this.client = ApiClient.getClient(this.context).create(ApiService.class);
    }

    public void getBundles(int skip, int limit, String providerId, Callback<SessionPacks> callBack) {
        client.getSessionBundles(accessToken, providerId, skip, limit).enqueue(callBack);
    }

    public void subscribeBundle(SessionPackInput input, Callback<ResponseMessage> callBack) {
        client.subscribeSessionBundle(accessToken, input).enqueue(callBack);
    }
}
