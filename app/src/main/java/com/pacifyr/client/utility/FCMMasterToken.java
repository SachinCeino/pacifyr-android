package com.pacifyr.client.utility;

import android.content.Context;
import android.content.SharedPreferences;

public class FCMMasterToken {
    Context mContext;
    String token;
    SharedPreferences sharedpreferences;
    public static final String FCMPREFFERENCE="fcmKeys";
    public FCMMasterToken(Context mContext,String token) {
        this.mContext=mContext;
        this.token=token;

        sharedpreferences = mContext.getSharedPreferences(FCMPREFFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("FCMTOKEN", token);
        editor.commit();
    }

    public static String getFCMTokenFromUTility(Context mContext)
    {
        SharedPreferences sharedpreferences=mContext.getSharedPreferences(FCMPREFFERENCE, Context.MODE_PRIVATE);
        String FCMTOKEN = (sharedpreferences.getString("FCMTOKEN", ""));
        return FCMTOKEN;
    }
}
