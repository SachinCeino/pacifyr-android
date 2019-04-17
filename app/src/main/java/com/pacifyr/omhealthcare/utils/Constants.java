package com.pacifyr.omhealthcare.utils;

import android.content.Context;

import com.pacifyr.omhealthcare.R;

public class Constants {


    public static String getChannelId(Context context) {
        return context.getResources().getString(R.string.CHANNEL_ID);
    }

    public static String getAppSource(Context context) {
        return context.getResources().getString(R.string.APP_SOURCE);
    }

    public static String getUrlPrivacyPolicy(Context context) {
        return context.getResources().getString(R.string.URL_PRIVACY_POLICY);
    }

    public static String getUrlTerms(Context context) {
        return context.getResources().getString(R.string.URL_TERMS);
    }

    public static String getUrlDisputePolicy(Context context) {
        return context.getResources().getString(R.string.URL_DISPUTE_POLICY);
    }
}
