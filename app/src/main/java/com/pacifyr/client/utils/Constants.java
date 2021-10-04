package com.pacifyr.client.utils;

import android.content.Context;

import com.pacifyr.client.R;

import java.util.ArrayList;

public class Constants {

    public static final String KEY_REALTIME_LOGGING = "realtime_logging";
    public static final String CHANNEL_ID_DEFAULT = "pacifyr_general_notifications";
    public static final String PAYMENT_DONE ="payment_done" ;
    public static final String PAYMENT_CARD_ADDED ="PAYMENT_CARD_ADDED" ;
    public static final String PAYMENT_CARD_ADDED_FINISH ="PAYMENT_CARD_ADDED_FINISH" ;
    public static final String TWILIO_CHAT_TOKEN_URL = "twilio/chat/generateToken";
    public static final Object CLOSECHILDACTIIVTY ="CLOSECHILDACTIIVTY" ;
    public static final String ADD_RESOURCES_LINK ="https://bpihsfbbrk5.typeform.com/to/fIPMYk3X" ;
    public static final String DestroySubScreenAfterEndCall = "DestroySubScreenAfterEndCall";
    public static final String PAYMENT_DONE ="payment_done" ;
    public static final String TWILIO_CHAT_TOKEN_URL = "twilio/chat/generateToken";
    public static final Object CLOSE_CHAT_ACTIVITY ="close_chat_activity_from_call" ;

    public static String getPacifyrVideoTutorialLink(Context context) {
        return context.getResources().getString(R.string.tutorial_VideoLink);
    }

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
/**for indian users **/
    public static String getUrlPrivacyPolicyIndia(Context context) {
        return context.getResources().getString(R.string.URL_PRIVACY_POLICY_INDIA);
    }

    public static String getUrlTermsIndia(Context context) {
        return context.getResources().getString(R.string.URL_TERMS_INDIA);
    }

    public static String getUrlDisputePolicyIndia(Context context) {
        return context.getResources().getString(R.string.URL_DISPUTE_POLICY_INDIA);
    }

    public static ArrayList<String> getCountryChecks() {

        ArrayList<String> COUNTRY_CHECKS = new ArrayList<String>();
        COUNTRY_CHECKS.add("India");
        COUNTRY_CHECKS.add("Wakanda");
        return COUNTRY_CHECKS;
    }
}
