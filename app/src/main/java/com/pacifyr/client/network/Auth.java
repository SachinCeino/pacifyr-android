package com.pacifyr.client.network;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pacifyr.client.R;
import com.pacifyr.client.activity.SplashScreen;
import com.pacifyr.client.quickblox.App;
import com.pacifyr.client.sign.LoginActivity;
import com.pacifyr.client.sign.SignActivity;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;

public class Auth {
    private static String TAG=Auth.class.getName();
    private static String ANDROID_ID;
    private static String VISIBLE_ACTIVITY = "";
    private static final boolean enableSessionManagement = false;
    public static void httpStatusFilter(Context c,int status){
        switch(status){
            case 401://Forbidden
                if(VISIBLE_ACTIVITY.equals(SignActivity.class.getName() )
                        || VISIBLE_ACTIVITY.equals(LoginActivity.class.getName())
                        || VISIBLE_ACTIVITY.equals(SplashScreen.class.getName())) {
                    Log.d(TAG, "httpStatusFilter: login page already shown"+status);
                }else {
                    PrefManager.getInstance(c).clear();
                    Intent intent = new Intent(c, SignActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("EXIT", true);
                    c.startActivity(intent);
                    Toast.makeText(c, c.getString(R.string.please_login_to_continue), Toast.LENGTH_LONG).show();

                }

                break;
            default:
                Log.d(TAG, "httpStatusFilter: "+status);
        }
    }
    public static void setActiveOnFDB(Context context) {
        if (!enableSessionManagement) {
            return;
        }
        PrefManager prefM = PrefManager.getInstance(context);
        String token = prefM.getAccessToken();
        String userId = prefM.getUserID();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("sessions").child(userId);
        myRef.setValue(getDeviceId(context));
        scanUpdates(context);
        Log.d(TAG, "setActiveOnFDB: " + userId + "-" + getDeviceId(context));
    }

    public static void scanUpdates(Context context) {
        if (!enableSessionManagement) {
            return;
        }
        Log.d(TAG, "scanUpdates: calling");
        ANDROID_ID = getDeviceId(context);
        String userId = PrefManager.getInstance(context).getUserID();
        Log.d(TAG, "scanUpdates: user id :" + userId);
        if (!userId.equals("")) {
            Log.d(TAG, "scanUpdates: scanning :" + userId);
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("sessions").child(userId);
            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String userId = PrefManager.getInstance(context).getUserID();
                    String deviceId = dataSnapshot.getValue(String.class);
                    Log.d(TAG, "onDataChange: firebase device id"+deviceId);
                    if(!userId.equals("")){
                    Log.d(TAG, "onDataChange:"+deviceId);
                        if (!ANDROID_ID.equals(deviceId)) {
                            Auth.httpStatusFilter(App.getInstance(), 401);
                        }
                    }else Log.d(TAG, "onDataChange: null user id");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d(TAG, databaseError.getMessage()); //Log errors
                }
            };
            myRef.addValueEventListener(valueEventListener);
        }
    }
    public static String getDeviceId(Context context){
       return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /*
     *used to know is any of login activity on front
     */
    public static void foreground(String activity){
        VISIBLE_ACTIVITY=activity;
    }
}
