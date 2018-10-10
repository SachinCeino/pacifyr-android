package com.pacifyr.omhealthcare.utils;

import android.util.Log;

import com.ceino.utilitylibrary.utils.PrefManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaaseInstanceIdService extends FirebaseInstanceIdService {

    public static final String TAG = MyFirebaaseInstanceIdService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String token = FirebaseInstanceId.getInstance().getToken();
        PrefManager.getInstance(getApplicationContext()).putSharedString(PrefManager.FCM_TOKEN, token);
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
        if (!isValid(getUserID())) {
            Log.d(TAG, "Invalid userId cancelling sendRegistrationToServer");
            return;
        }
        if (!isValid(getAccessToken())) {
            Log.d(TAG, "Invalid access token cancelling sendRegistrationToServer");
            return;
        }
        new AmazonSNSTokenHandler(getApplicationContext()).register();
    }

    public Boolean isValid(Object object) {
        if (object != null)
            return true;
        return false;

    }

    public String getAccessToken() {
        return PrefManager.getInstance(getApplicationContext()).getSharedString(PrefManager.ACCESSTOKEN, null);

    }

    public String getUserID() {
        return PrefManager.getInstance(getApplicationContext()).getSharedString(PrefManager.USERID, null);
    }


}
