package com.pacifyr.omhealthcare.utils;

import android.provider.Settings;

import static com.facebook.FacebookSdk.getApplicationContext;

public class Constants {
    public static final String CHANNEL_ID = "om_general_notifications";
    public static final String APP_SOURCE = "om";
    public static final String URL_PRIVACY_POLICY = "https://omhealthcare.com/privacyPolicy";
    public static final String URL_TERMS = "https://omhealthcare.com/termsOfUse";
    public static final String URL_DISPUTE_POLICY = "https://omhealthcare.com/copyrightDisputePolicy";

    public static String getUDID() {
        String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        return android_id;
    }
}
