package com.pacifyr.omhealthcare.utils;

import android.provider.Settings;

import static com.facebook.FacebookSdk.getApplicationContext;

public class Constants {
    public static final String CHANNEL_ID = "om_general_notifications";

    public static String getUDID() {
        String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        return android_id;
    }
}
