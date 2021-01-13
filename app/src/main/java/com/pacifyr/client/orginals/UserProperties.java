package com.pacifyr.client.orginals;

import android.content.Context;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;

public class UserProperties {
    public static String _classification="classification";
    public static String _role="role";
    public static void setProperty(Context context){
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        PrefManager prefM= PrefManager.getInstance(context);

        mFirebaseAnalytics.setUserProperty(_role,prefM.getCustomerRole() );
        mFirebaseAnalytics.setUserProperty(_classification,"com.pacifyr.normal");

       // mFirebaseAnalytics.setUserProperty("classification","com.pacifyr.pam");
    }
}
