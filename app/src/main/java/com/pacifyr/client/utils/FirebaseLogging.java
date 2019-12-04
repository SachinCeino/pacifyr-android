package com.pacifyr.client.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by Sachin Thampan on 28/10/19.
 */
public class FirebaseLogging {

    public static final String EC = "error/comment";

    public static final String EVENT_SIGNUP_SUCCESS = "signup_success";
    public static final String EVENT_SIGNUP_FAIL = "signup_fail";
    public static final String EVENT_LOGIN_SUCCESS = "login_success";
    public static final String EVENT_LOGIN_FAIL = "login_fail";
    public static final String EVENT_LOGIN_TYPE = "login_type";
    public static final String EVENT_LOGIN_OTP_SENT = "otp_sent";
    public static final String EVENT_LOGIN_OTP_RESENT = "otp_resent";
    public static final String EVENT_LOGIN_OTP_VERIFIED = "otp_verified";
    public static final String EVENT_LOGIN_OTP_VERIFICATION_FAIL = "otp_verification_failed";
    public static final String EVENT_CHAT_HISTORY_LOADED = "chat_history_loaded";
    public static final String EVENT_CHAT_HISTORY_LOAD_FAILED = "chat_history_load_failed";
    public static final String EVENT_INCOMING_CALL_FAIL = "call_fail_incoming";
    public static final String EVENT_AUDIOCALL_STARTED = "call_audio_started";
    public static final String EVENT_AUDIOCALL_STOPPED = "call_audio_stopped";
    public static final String EVENT_VIDEOCALL_STARTED = "call_video_started";
    public static final String EVENT_VIDEOCALL_STOPPED = "call_video_stopped";
    public static final String EVENT_CLIENT_PAYMENT_DONE = "client_done_payment";
    public static final String EVENT_CLIENT_PAYMENT_FAILED = "client_payment_failed";
    public static final String EVENT_COUNCELOR_SESSION_STARTED = "councelor_session_started";


    public static Bundle getConstantArgsLoggedIn(Context context) {
        String userId = PrefManager.getInstance(context).getUserID();
        String userName = PrefManager.getInstance(context).getUserName();
        if (userId == null || userId.isEmpty()) {
            throw new RuntimeException("Not logged in. Cannot get userId");
        }
        Bundle args = new Bundle();
        args.putString("device", "Android");
        args.putString("app_version", getAppVersion(context));
        args.putString("os_version", getAndroidVersion());
        args.putString("userId", userId);
        args.putString("userName", userName);

        return args;
    }

    public static Bundle getConstantArgsLoggedOut(Context context) {
        String userId = PrefManager.getInstance(context).getUserID();
        if (userId != null && !userId.isEmpty()) {
            throw new RuntimeException("Already logged in. Use getConstantArgsLoggedIn() method");
        }
        Bundle args = new Bundle();
        args.putString("device", "Android");
        args.putString("app_version", getAppVersion(context));
        args.putString("os_version", getAndroidVersion());

        return args;
    }

    private static String getAppVersion(Context context) {
        try {
            PackageInfo pInfo =
                    context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String version = pInfo.versionName;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "NA";
    }

    private static String getAndroidVersion() {
        return Build.VERSION.SDK_INT + "";
    }

    public static void sendLog(Context context, String event, Bundle args) {
        FirebaseAnalytics analytics = FirebaseAnalytics.getInstance(context);
        analytics.logEvent(event, args);
    }
}
