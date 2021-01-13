package com.pacifyr.client.orginals;

import android.app.Activity;
import android.content.Intent;
import android.net.Network;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.pacifyr.client.activity.SplashScreen;
import com.pacifyr.client.activity.TermsOfUseActivity;
import com.pacifyr.client.home.PolicyDocumentActivity;
import com.pacifyr.client.sign.SignActivity;
import com.pacifyr.client.utils.Constants;

public class DynamicLink {
    public static String TAG=DynamicLink.class.getName();
    private static Uri TheLink=null;

    private static String screen = "";
    private static String screen_data = "";

    public static void setScreenFromNotification(String scree,String data){
        screen=scree;
        screen_data=data;
    }
    public static String getScreenFromNotification(){
        String s=screen;
        screen="";
        return s;
    }
    public static String getScreenDataFromNotification(){
        String s=screen_data;
        screen_data="";
        return s;
    }
    public static void fetch(Activity activity){
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(activity.getIntent())
                .addOnSuccessListener(activity, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {

                            TheLink = pendingDynamicLinkData.getLink();

                        }else{
                           //do something
                        }
                    }
                })
                .addOnFailureListener(activity, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG,"deep link failed "+e);
                    }
                });
    }
    public static String link(Activity activity){

        if (TheLink != null) {
            
            String activity_name=RemoteConfig.getDynamicLinkMap(TheLink.toString());
            TheLink=null;
          //  getCall(0,activity,activity_name);
            return activity_name;

        }else{
            return "";
        }
    }
    private static void getCall(int n,Activity activity,String className){
        switch (n){
            case 0:
                Intent i= null;
                try {
                    i = new Intent(activity, Class.forName(className));

                    activity.startActivity(i);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }


            default:;
        }
    }


}
