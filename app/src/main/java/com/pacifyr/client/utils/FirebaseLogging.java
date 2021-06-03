package com.pacifyr.client.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.pm.PackageInfoCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

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
    public static final String EVENT_ON_SCREEN = "on_screen";
    public static final String EVENT_AUTO_APPLY_COUPON_CODE = "auto_apply_coupon_code";
    public static final String EVENT_APPLY_COUPON_CODE = "apply_coupon_code";
    public static final String EVENT_CALL_STARTED = "Call_started";
    public static final String EVENT_QUICKBLOX_ISSUE = "quickBlox_isuue";
    public static final String EVENT_QUICKBLOX_LOGIN_SUCCESS = "quickBlox_success";
    public static final String EVENT_QUICKBLOX_LOGIN_FAIL = "quickBlox_fail";
    public static String FIREBASE_LOG_USERS_TRACKED_TABLE = "device_users_tracked";
    private static final String FIREBASE_LOG_TABLE = "device_logs";
    private static final String TAG = FirebaseLogging.class.getSimpleName();
    public static String FIREBASE_CONFIG_TABLE = "Config";


    public static Bundle getConstantArgsLoggedIn(Context context) {
        String userId = PrefManager.getInstance(context).getUserID();
        String userName = PrefManager.getInstance(context).getUserName();
        if (userId == null || userId.isEmpty()) {
            Log.e(TAG, "UserId not found cannot add logs");
            return null;
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

    public static String getAppVersion(Context context) {
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

    public static String getAndroidVersion() {
        return Build.VERSION.SDK_INT + "";
    }

    public static void sendLog(Context context, String event, Bundle args) {
        if (args == null) {
            return;
        }
        FirebaseAnalytics analytics = FirebaseAnalytics.getInstance(context);
        analytics.logEvent(event, args);

        String userId = args.getString("userId");

        if (userId != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            int realtimeLogging = PrefManager.getInstance(context).getSharedInteger(Constants.KEY_REALTIME_LOGGING, -1);
            if (realtimeLogging == 1) {
                String params = null;
                JSONObject ev = new JSONObject();
                try {
                    ev.put(event, args);
                    ev.put("timeStamp", Utility.getTimeStamp());
                    params = new Gson().toJson(ev);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                DatabaseReference myRef = database.getReference(FIREBASE_LOG_TABLE);
                DatabaseReference childRef = myRef.child(userId).push();
                childRef.setValue(params).addOnFailureListener((Activity) context, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Log.d(TAG, "save log failed..." + e.getMessage());
                        e.printStackTrace();
                    }
                }).addOnSuccessListener((Activity) context, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "saved log..");
                    }
                });
            }

        }
    }

    public static long getAppVersionCode(Context context) {
        try {
            PackageInfo pInfo =
                    context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return PackageInfoCompat.getLongVersionCode(pInfo);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
