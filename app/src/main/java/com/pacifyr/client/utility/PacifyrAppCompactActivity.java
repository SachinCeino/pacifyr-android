package com.pacifyr.client.utility;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PersistableBundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.koushikdutta.ion.Ion;
import com.pacifyr.client.R;
import com.pacifyr.client.base.PacifyrActivity;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.event.AlertTypeEvent;
import com.pacifyr.client.event.BusProvider;
import com.pacifyr.client.event.CallDetailEvent;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.fragment.schedule.ScheduleBottomSheetFragment;
import com.pacifyr.client.orginals.Auth;
import com.pacifyr.client.network.GsonUtils;
import com.pacifyr.client.network.NetworkService;
import com.pacifyr.client.sign.SignActivity;
import com.pacifyr.client.twilio.VideoActivity;
import com.pacifyr.client.utils.AmazonSNSTokenHandler;
import com.pacifyr.client.utils.FirebaseLogging;
import com.pacifyr.client.utils.LogUtils;
import com.pacifyrapp.utilitylibrary.ToastHandler;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MEmotion;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MUser;
import com.pacifyrapp.utilitylibrary.model.pacifyr.ModelUserProfile;
import com.pacifyrapp.utilitylibrary.network.UtilityNetworkService;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.squareup.otto.Subscribe;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.pacifyr.client.network.NetworkService.GLOBAL_URL;
import static com.pacifyr.client.network.NetworkService.modelCustomerDashboard2;
import static com.pacifyr.client.twilio.VideoActivity.isCallActive;
import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.ACCOUNT_KEY;
import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.APP_ID;
import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.AUTH_KEY;
import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.AUTH_SECRET;
import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.GLOBAL_IMAGE_URL;

//import com.google.android.gms.gcm.GoogleCloudMessaging;
//import com.pacifyr.pacifyrapp.utils.LogUtils;
/*import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;*/


/**
 * Created by developer on 12/6/17.
 */

public abstract class PacifyrAppCompactActivity  extends PacifyrActivity {
    private static final String TAG = PacifyrAppCompactActivity.class.getSimpleName();
    PowerManager.WakeLock wakeLock;
    private Snackbar mSnackbar;

    @SuppressLint("InvalidWakeLockTag")
    public void setScreenLockOff()
    {
        try{
            PowerManager powerManager = (PowerManager)getApplicationContext().getSystemService(Activity.POWER_SERVICE);
            // wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "VK_LOCK");
            //  PowerManager powerManager = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
            wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "My Lock");
            wakeLock.acquire();
            LogUtils.LOGD("jithish", "PAA Acquire wake lock");
            // wakeLock.acquire();
        }catch(Exception e)
        {
            LogUtils.LOGD("exception", "PAA setScreenLockOff exception "+e.getMessage());
        }


    }

    public void setScreenLockRelease()
    {
        try{
            if (wakeLock.isHeld())
                wakeLock.release();
            else{
                LogUtils.LOGD("jithish", "PAA setScreenLockON else Wake lock is already released, doing nothing");
            }
        }catch(Exception e)
        {
            LogUtils.LOGD("exception",  "PAA setScreenLockON "+e.getMessage());
        }


    }
    public PacifyrAppCompactActivity() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BusProvider.getInstance().register(this);

        signInCreatedUser(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        BusProvider.getInstance().register(this);

        signInCreatedUser(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //  BusProvider.getInstance().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // BusProvider.getInstance().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
    }

    /**
     * Shared Preference
     */

    public String getSharedString(String KEY, String defValue) {
        return PrefManager.getInstance(this).getSharedString(KEY, defValue);
    }

    public void putSharedString(String KEY, String defValue) {
        PrefManager.getInstance(this).putSharedString(KEY, defValue);
    }

    public String getPrefUDID() {
        return getSharedString(PrefManager.UDID, "");
    }

    public void saveUDID(String valude) {
        putSharedString(PrefManager.UDID, valude);
    }

    public void saveProfileImageURL(String url) {
        putSharedString(PrefManager.PROFILE_IMAGE_URL, url);

    }

    public String getProfileImageURL() {
        return getSharedString(PrefManager.PROFILE_IMAGE_URL, "");

    }

    public String getUserID() {
        return getSharedString(PrefManager.USERID, "");

    }
    public String getCurrentUserName()
    {
        String name="";
        try{
            if(isValid(NetworkService.mCurrentUser))
                name=NetworkService.mCurrentUser.getFirstName() +""+ defString(NetworkService.mCurrentUser.getLastName());
            else
                name=getProfileName();

        }catch (Exception e)
        {
            name="";
        }
        return name;
    }
    public String getCurrentUserImageUrl()
    {
        String name=null;
        try{
            if(isValid(NetworkService.mCurrentUser))
                name=NetworkService.mCurrentUser.getImageUrl() ;
            else
                name=getProfileImageURL();
            if(isValid(name))
                if(!isValidhtml(name))
                    name=NetworkService.GLOBAL_IMAGE_URL+name;

        }catch (Exception e)
        {
            name=null;
        }
        return name;
    }
    public String getCurrentUserMail()
    {
        String name="";
        try{
            if(isValid(NetworkService.mCurrentUser))
                name=NetworkService.mCurrentUser.getEmail() ;
            else
                name=getPrefManager().geteMail();

        }catch (Exception e)
        {
            name="";
        }
        return name;
    }
    public MEmotion getEmotion()
    {
        MEmotion id=null;
        try{

            if (isCustomer()) {
                // id= WebRtcSessionManager.userCallInfo.OpponentUserid;
                // id=pacifyerDetial.getEmotion();
                id= PacifyrActivity.call_emotion;
                LogUtils.LOGD("session","PACA  getEmotion obj call_emotion " +id);
                // id=call_session_detail_root.getSession().getUser().getEmotion();
            }
            else
            {
                id=call_session_detail_root.getUser().getEmotion();
                LogUtils.LOGD("session","PACA  getEmotion obj call_session_detail_root " +call_session_detail_root);
                // id=call_session_detail_root.getSession().getUser().getEmotion();
            }
        }catch (Exception e)
        {
            LogUtils.LOGD("session","PACA  getEmotion obj exce " + e.getMessage());

        }
        return id;
    }

    public boolean isPacifyer()
    {
        boolean isPacifyer=false;
        try{

            try {

                String label=PrefManager.getInstance(this).getUserType();

                if(label.equalsIgnoreCase("pacifyr"))
                    isPacifyer=true;
                else if(label.equals("customer")) {
                    isPacifyer=false;
                }
            } catch (Exception t) {
                isPacifyer=false;
                //  LogUtils.LOGD("session","SSA getMSessionRating list exce " + t.getMessage());
            }
        }catch (Exception e)
        {
            isPacifyer=false;
        }
        return isPacifyer;
    }
    public boolean isCustomer()
    {
        boolean isCustomer=false;
        try{

            try {

                String label=PrefManager.getInstance(this).getUserType();
                if(label.equals("customer")) {
                    isCustomer=true;
                }
                else if(label.equalsIgnoreCase("pacifyr"))
                    isCustomer=false;

            } catch (Exception t) {
                isCustomer=false;
                //  LogUtils.LOGD("session","SSA getMSessionRating list exce " + t.getMessage());
            }
        }catch (Exception e)
        {
            isCustomer=false;
        }
        return isCustomer;
    }

    public String getProfileName() {
        return getProfileFName() + " " + getProfileLName();

    }

    public String getProfileFName() {
        return getSharedString(PrefManager.UserFirstName, "");

    }

    public String getProfileLName() {
        return getSharedString(PrefManager.UserLastName, "");

    }

    public String getGCMToken() {
        return getSharedString(PrefManager.FCM_TOKEN, "");
    }

    public String getAWSEndPoint() {
        return getSharedString(PrefManager.AWS_ENDPOINT, "");
    }

    /**
     * GCM
     */

    /**
     * GCM
     */


    public void configPushNotifications() {

       // new AmazonSNSTokenHandler(this).register();
        /*try {
            LogUtils.LOGD("install", "configPushNotifications");
            String gcmToken = getGCMToken();
            if(TextUtils.isEmpty(gcmToken)) {
                LogUtils.LOGD("install", "configPushNotifications  register ic_call_round Gcmtoken");
                register();

            } else {
                LogUtils.LOGD("install", "configPushNotifications  register already Gcmtoken ");
                if(!isValid(getAWSEndPoint()))
                    createEndPointTask(AWSManager.ARN, gcmToken, getProfileName());
            }
        } catch (Exception e){
            //     Crashlytics.logException(e);
            LogUtils.LOGD("install", "configPushNotifications Exception " + e.getMessage());
        }
*/
    }

    //////////////////////////////////////////////
    public void clearSharedAll() {

        try {
            PrefManager prefM = PrefManager.getInstance(getApplicationContext());
            prefM.clearSharedAll();

        }catch (Exception e)
        {
            LogUtils.LOGD("install", "clearSharedAll Exception : " + e.getMessage());
        }

    }


    public void clearServices() {
        try {
            //  clearLayerDatas();
            NetworkService.modelAllEmployees = null;

            NetworkService.mCurrentUser = null;

        }catch (Exception e)
        {
            LogUtils.LOGD("install", "clearServices Exception : " + e.getMessage());
        }


    }
    private void showSignActivty() {
        Intent i = new Intent(getApplicationContext(), SignActivity.class);
        startActivity(i);
        finish();
    }

    public void handlerLogoutFinish(String s) {
        try{
            setStatus(s);
            showStatus();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    //  setStatus("Logout",R.color.color_blue_accent);
                    //  showStatus();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            clearSharedAll();
                            clearServices();
                            LoginManager mLoginManager = LoginManager.getInstance();
                            if(isValid(mLoginManager))
                                mLoginManager.logOut();
                            hideStatus();
                            //   NetworkService.GLOBALURL=NetworkService.SIGN_GLOBALURL;
                            showSignActivty();
                        }
                    }, 500);
                    hideStatus();
                    finish();
                }
            },2000);

        }catch (Exception e)
        {

        }
    }


    //////logoutPacifyr//////
    public void logoutEditProfile(Context context) {
        try {
            setLoading();
            showProgress();
            String  token = getPrefManager().getAccessToken();
            String  userid = getPrefManager().getUserID();
            JSONObject editProfileObject=new JSONObject();
            editProfileObject = new JSONObject();
            editProfileObject.putOpt("isOnline", false);

            String url = UtilityNetworkService.GLOBAL_URL + "user/editprofile/" + userid;
            com.koushikdutta.async.future.FutureCallback<JsonObject> futureCallback = new com.koushikdutta.async.future.FutureCallback<JsonObject>() {
                @Override
                public void onCompleted(Exception e, JsonObject result) {
                    try {
                        hideProgress();
                        if (isValid(result)) {
                            deleteInstallationWeb();
                        }
                    } catch (Exception eee) {
                        hideProgress();
                        //hideProgress();
                        Toast.makeText(getApplicationContext(), "Error :" + e.getMessage() + ", ex :" + eee.getMessage(), Toast.LENGTH_LONG).show();
                        eee.printStackTrace();
                    }
                }
            };
            String json = editProfileObject.toString();
            Ion.with(context).load(url).setHeader("AccessToken", token).setHeader("enctype", "multipart/form-data").setMultipartParameter("data", json).asJsonObject().setCallback(futureCallback);
        }catch (Exception e)
        {   e.printStackTrace();
            hideProgress();}
    }
    //////////////////////////////////////////////
    public void deleteInstallationWeb() {
        try {
            LogUtils.LOGD("install", "deleteInstallation " );

            setLoading();
            showProgress();
            AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    hideProgress();
                    LogUtils.LOGD("install", "deleteInstallation json " +json);
                    LogUtils.LOGD("install", "deleteInstallation status" +status.getMessage());

                    if(json == null) {
                        ToastHandler.newInstance(PacifyrAppCompactActivity.this).mustShowToast("Network Interupted");

                    } else {
                        try {
                            clearSharedAll();
                            clearServices();
                            fbLogout();
                            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            mNotificationManager.cancelAll();
                            deleteCache(getApplicationContext());
                            clearApplicationData();



                            setStatus("You have been logged out",R.color.colorPrimary);
                            showStatus();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    hideStatus();
                                    //NetworkService.GLOBALURL=NetworkService.SIGN_GLOBALURL;
                                    showSignActivty();
                                    if (isValid(modelCustomerDashboard2))
                                        modelCustomerDashboard2 = null;

                                }
                            }, 500);

                        } catch (Exception e){
                            //Crashlytics.logException(e);
                            hideStatus();
                            ToastHandler.newInstance(PacifyrAppCompactActivity.this).mustShowToast("Network Interupted");
                            e.printStackTrace();
                        }
                    }


                }


            };
            LogUtils.LOGD("install", "deleteInstallation getPrefUDID " +getPrefUDID());
            String udid=isValid(getPrefUDID())?getPrefUDID():getUDID();

            AQuery aq = new AQuery(PacifyrAppCompactActivity.this);
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", getAccessToken());
            String url = NetworkService.GLOBAL_URL + "installation/" + udid+ "";
            LogUtils.LOGD("install", "deleteInstallation " +url);
            aq.delete(url, JSONObject.class, cb);
            //aq.d
        } catch (Exception e){
            //Crashlytics.logException(e);
            hideProgress();
            LogUtils.LOGD("install", "deleteInstallation exception : " + e.getMessage());
        }
    }


   /* public void installationWeb() {
        SharedPreferences sp = getSharedPreferences(PrefManager.LOGIN_PREFERENCES, 0);
        final String udid = getUDID();

        saveUDID(udid);

        LogUtils.LOGD("install","install"+"installationWeb");
        HttpPost request = GetPostHttp(sp.getString("Userid", null), PrefManager.getInstance(getApplicationContext()).getSharedString(PrefManager.AWS_ENDPOINT, null)
                ,udid, PrefManager.getInstance(getApplicationContext()).getSharedString(PrefManager.FCM_TOKEN, null), PrefManager.DEVICETYPE);
        HttpResponse response = null;
        HttpClient client = new DefaultHttpClient();
        String xmlString = null;
        String result = null;
        InputStream inputStream = null;
        JSONObject json = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            response = client.execute(request);
            if (response != null) {
                inputStream = response.getEntity().getContent();
                if (inputStream != null) {
                    result = convertInputStreamToString(inputStream);// ic_call_round this method declar the result in to global
                    LogUtils.LOGD("install","install"+"installationWeb1 : " + json);
                    json = new JSONObject(result);
                    if (json == null) {
                        LogUtils.LOGD("install","install"+"installationWeb2 : " + json);
                    } else if (json != null) {
                        LogUtils.LOGD("install","install"+"installationWeb suceess : " + json);
//                        postEventOnMainThread(new RedeemTokenEvent());
                    }
                }
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/


    /*private HttpPost GetPostHttp(String userid, String arn, String udid, String devicetoken, String devicetype) {
        HttpPost request = new HttpPost(NetworkService.GLOBAL_URL+ "installation");
        LogUtils.LOGD("install","install"+" installation url: " + NetworkService.GLOBAL_URL+ "installation");
        String param  = "{\"userid\":\""+userid+"\",\"arn\":\""+ arn +"\",\"udid\":\""+ udid +"\",\"devicetoken\":\""+devicetoken+"\",\"deviceType\":\""+ devicetype +"\"}";

        String accesstoken= PrefManager.getInstance(getApplicationContext()).getAccessToken();
        request.setHeader("Content-Type", "application/json");
        request.setHeader("topschool-app-id", "appid");
        request.setHeader("topschool-app-key", "appkey");
        request.setHeader("AccessToken",""+accesstoken);
        try {
            StringEntity entity = new StringEntity(param);
            request.setEntity(entity);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return request;
    }*/

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }

    public String getUDID() {
        String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        return android_id;
    }



    public void setAlertOKStatusInBlue(AlertTypeEvent alert) {
        //View.OnClickListener oklistner
        //LogUtils.LOGD("receiver", "topapp setAlertOKStatusInBlue " + alert.getType());

        String type = alert.getType();
        String collection = alert.getCollection();
        long minutes=getElapsedMinutes(alert.receiveddate);
        if(minutes<3) {

            if (isValid(collection)) {
                if (collection.equalsIgnoreCase("hangout")) {
                   /* NetworkService.startActionhangoutLive(this, 0, 10);
                    NetworkService.starthangoutRunning(this, 0, 10);*/
                }


            }
            if (isValid(type)) {
                if (type.equalsIgnoreCase("join")) {
                    setAlertJoin(alert);

                }
                else if((type.equalsIgnoreCase("start")))
                {
                    setAlertStart(alert);
                }
                else {
                    View view = LayoutInflater.from(this).inflate(com.pacifyrapp.utilitylibrary.R.layout.alert_ok, null, false);
                    MainTextView title_dtxv = view.findViewById(com.pacifyrapp.utilitylibrary.R.id.title_dtxv);
                    MainTextView desc_dtxv = view.findViewById(com.pacifyrapp.utilitylibrary.R.id.desc_dtxv);
                    MainTextView ok_dtxv = view.findViewById(com.pacifyrapp.utilitylibrary.R.id.ok_dtxv);
                    type = capitalizeString(type);
                    defSetText(title_dtxv, type + "!");
                    defSetText(desc_dtxv, alert.getDesc());
                    ok_dtxv.setText(" OK ");
                    hideStatus();
                    hud_status = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCustomView(view).setWindowColor(this.getResources().getColor(com.pacifyrapp.utilitylibrary.R.color.transparent)).setCancellable(true).setAnimationSpeed(2).setDimAmount(0.5f);
                    showStatus();
                    //  scheduleDismiss();
                    ok_dtxv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            hideStatus();


                        }
                    });
                }
            }

        }

    }
    public void setAlertJoin(final AlertTypeEvent alert) {
        //View.OnClickListener oklistner
        LogUtils.LOGD("receiver", "topapp setAlertJoin " + alert.getType());
        View view = LayoutInflater.from(this).inflate(R.layout.alert_two_options, null, false);
        MainTextView title_dtxv = view.findViewById(com.pacifyrapp.utilitylibrary.R.id.title_dtxv);
        MainTextView desc_dtxv = view.findViewById(com.pacifyrapp.utilitylibrary.R.id.desc_dtxv);
        MainTextView ok_dtxv = view.findViewById(com.pacifyrapp.utilitylibrary.R.id.ok_dtxv);

        MainTextView dismissDtxv = view.findViewById(R.id.dismiss_dtxv);
        String type = alert.getType();
       /* String collection = alert.getCollection();
        if (isValid(collection)) {
            if (collection.equalsIgnoreCase("hangout")) {
                NetworkService.startActionhangoutLive(this, 0, 10);
                NetworkService.startActionhangoutExpire(this, 0, 10);
            }


        }
        if (isValid(type))
            type = capitalizeString(type);*/

        defSetText(title_dtxv,"Program "+"!");
        defSetText(desc_dtxv,alert.getDesc());
        ok_dtxv.setText(" Join ");
        defSetText(dismissDtxv,"Ignore");
        hideStatus();
        hud_status = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCustomView(view).setWindowColor(this.getResources().getColor(com.pacifyrapp.utilitylibrary.R.color.transparent)).setCancellable(true).setAnimationSpeed(2).setDimAmount(0.5f);
        showStatus();
        //scheduleDismiss();
        ok_dtxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideStatus();
                hangoutJoinWeb(alert.getId());
                /*Intent intent = new Intent(getApplicationContext(), VideoActivity.class);
                intent.putExtra("roomname", alert.getId() + "");
                startActivity(intent);*/


            }
        });
        dismissDtxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideStatus();


            }
        });
    }
    public void setAlertStart(final AlertTypeEvent alert) {
        //View.OnClickListener oklistner
        LogUtils.LOGD("receiver", "topapp setAlertStart " + alert.getType());
        View view = LayoutInflater.from(this).inflate(R.layout.alert_two_options, null, false);
        MainTextView title_dtxv = view.findViewById(com.pacifyrapp.utilitylibrary.R.id.title_dtxv);
        MainTextView desc_dtxv = view.findViewById(com.pacifyrapp.utilitylibrary.R.id.desc_dtxv);
        MainTextView ok_dtxv = view.findViewById(com.pacifyrapp.utilitylibrary.R.id.ok_dtxv);

        MainTextView dismissDtxv = view.findViewById(R.id.dismiss_dtxv);
        String type = alert.getType();
       /* String collection = alert.getCollection();
        if (isValid(collection)) {
            if (collection.equalsIgnoreCase("hangout")) {
                NetworkService.startActionhangoutLive(this, 0, 10);
                NetworkService.startActionhangoutExpire(this, 0, 10);
            }


        }
        if (isValid(type))
            type = capitalizeString(type);*/

        defSetText(title_dtxv,"Program "+"!");
        defSetText(desc_dtxv,alert.getDesc());
        ok_dtxv.setText(" Start ");
        defSetText(dismissDtxv,"Ignore");
        hideStatus();
        hud_status = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCustomView(view).setWindowColor(this.getResources().getColor(com.pacifyrapp.utilitylibrary.R.color.transparent)).setCancellable(true).setAnimationSpeed(2).setDimAmount(0.5f);
        showStatus();
        //scheduleDismiss();
        ok_dtxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideStatus();
                hangoutStartWeb(alert.getId());
                /*Intent intent = new Intent(getApplicationContext(), VideoActivity.class);
                intent.putExtra("roomname", alert.getId() + "");
                startActivity(intent);*/


            }
        });
        dismissDtxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideStatus();


            }
        });
    }

    public void hangoutStartWeb(final String id) {
        try {
            String userid= getPrefManager().getUserID();
            String accesstoken= getPrefManager().getAccessToken();
            //      LogUtils.LOGD("auction", "bidPostWeb auctionid  " + auctionid);
            LogUtils.LOGD("hangout", "hangoutStartWeb userid " + userid);
            if(!isValid(userid,getResources().getString(R.string.session_expired))) return;
            if(!isValid(id,getResources().getString(R.string.session_expired))) return;
            if(!isValid(accesstoken,getResources().getString(R.string.session_expired))) return;
            //  setLoadingWait();
            // showProgress();
            JSONObject input = new JSONObject();
            input.put("userid", userid);
            input.put("devicetype", "android");


            AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    LogUtils.LOGD("hangout", "Ah hangoutStartWeb  callback json "+json);

                }
            };
            AQuery aq = new AQuery(getApplicationContext());
            cb.header("topschool-app-id", "appid");
            cb.header("topschool-app-key", "appkey");
            cb.header("AccessToken",""+accesstoken);
            String url=GLOBAL_IMAGE_URL + "hangout/start/"+id;
            aq.post(url, input, JSONObject.class, cb);

            LogUtils.LOGD("hangout", "Ah hangoutStartWeb url "+url);
            /*Intent intent = new Intent(getApplicationContext(), VideoActivity.class);
            intent.putExtra("roomname", id + "");
            startActivity(intent);*/

        } catch (Exception e){
            hideProgress();
            LogUtils.LOGD("hangout", "Ah hangoutStartWeb exception : " + e.getMessage());
        }
    }
    public void hangoutJoinWeb(final String id) {
        try {
            String userid= getPrefManager().getUserID();
            String accesstoken= getPrefManager().getAccessToken();
            //      LogUtils.LOGD("auction", "bidPostWeb auctionid  " + auctionid);
            LogUtils.LOGD("hangout", "TSMENU hangoutJoinWeb userid " + userid);
            if(!isValid(userid,"Please  Re-Login")) return;
            if(!isValid(id,"Please  Re-Login")) return;
            if(!isValid(accesstoken,"Please  Re-Login")) return;
            //  setLoadingWait();
            // showProgress();
            JSONObject input = new JSONObject();
            input.put("userid", userid);
            input.put("devicetype", "android");


            AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    LogUtils.LOGD("hangout", "TSMENU hangoutJoinWeb  callback json "+json);

                }
            };

            AQuery aq = new AQuery(getApplicationContext());
            cb.header("topschool-app-id", "appid");
            cb.header("topschool-app-key", "appkey");
            cb.header("AccessToken",""+accesstoken);
            String url=GLOBAL_IMAGE_URL + "hangout/join/"+id;
            aq.post(url, input, JSONObject.class, cb);

            LogUtils.LOGD("hangout", "TSMENU hangoutJoinWeb url "+url);

           /* Intent intent = new Intent(getApplicationContext(), VideoActivity.class);
            intent.putExtra("roomname", id + "");
            startActivity(intent);*/

        } catch (Exception e){
            hideProgress();
            LogUtils.LOGD("hangout", "TSMENU hangoutJoinWeb exception : " + e.getMessage());
        }
    }
    private void scheduleDismiss() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideStatus();
            }
        }, 18000);
    }
    /*  public long getElapsedMinutes(Date date2) {

          Date nowdate = new Date();
          long mili = 0;
          long elapsedminutes=0;
          try {
              LogUtils.LOGD("install", "  TACA date2 time " + date2.getTime());
              LogUtils.LOGD("install", "  TACA nowdate time " + nowdate.getTime());
              long different = date2.getTime() - nowdate.getTime();
              LogUtils.LOGD("install", "  TACA different time " + different);
  //return differents;
              long secondsInMilli = 1000;
              long minutesInMilli = secondsInMilli * 60;
              long hoursInMilli = minutesInMilli * 60;
              long daysInMilli = hoursInMilli * 24;

              elapsedminutes = different / minutesInMilli;
          }
          catch (Exception e)
          {
              LogUtils.LOGD("exception","  TACA time "+e.getMessage());
          }
          return elapsedminutes;
      }*/
    public Date cvtToGmt(Date date ){
        TimeZone tz = TimeZone.getDefault();
        Date ret = new Date( date.getTime() - tz.getRawOffset() );

        // if we are now in DST, back off by the delta.  Note that we are checking the GMT date, this is the KEY.
        if ( tz.inDaylightTime( ret )){
            Date dstDate = new Date( ret.getTime() - tz.getDSTSavings() );

            // check to make sure we have not crossed back into standard time
            // this happens when we are on the cusp of DST (7pm the day before the change for PDT)
            if ( tz.inDaylightTime( dstDate )){
                ret = dstDate;
            }
        }
        return ret;
    }

    public abstract void onSetValues() throws Exception;


    public abstract void onNotifyEvent(NotifyEvent event);

    @Subscribe
    public abstract void onAlertTypeEvent(AlertTypeEvent event);

   /* @Subscribe
    public  void onAlertTypeEvent(AlertTypeEvent event)
    {
        try {
            //LogUtils.LOGD("receiver","topapp onAlertTypeEvent "+event);
            if (isValid(event)) {
                if (!isValid(PacifyrActivity.sessionid)
                PacifyrActivity.sessionid=event.getId();
               // setAlertOKStatusInBlue(event);
            }
        }catch (Exception e)
        {

        }
    }
*/



    public void webGetSessionDetail(final String sessionid) {
        try {
            setLoading();
            showProgress();



            if(!isValid(sessionid,"Network Interupted")) {
                finish();
                return;
            }
            AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    hideProgress();
                    LogUtils.LOGD("session", " PACA webGetSessionDetail callback result" + json + " " + status.getMessage());
                    if(!isValid(json,"Network Interupted"))
                    {
                        finish();
                    }
                    else {
                        try {
                            VideoActivity.mSessionDetailRoot= GsonUtils.getInstance().gsonToMSessionDetailRoot(json);

                            PacifyrActivity.call_session_detail_root= VideoActivity.mSessionDetailRoot;
                            postEventOnMainThread(new CallDetailEvent());
                        } catch (Exception e){
                            Bundle args =
                                    FirebaseLogging.getConstantArgsLoggedIn(PacifyrAppCompactActivity.this);
                            if (status != null) {
                                args.putString(FirebaseLogging.EC,
                                        "webGetSessionDetail=null,err=" + e.getMessage());
                            }
                            FirebaseLogging.sendLog(PacifyrAppCompactActivity.this,
                                    FirebaseLogging.EVENT_INCOMING_CALL_FAIL,
                                    args);
                            finish();
                            LogUtils.LOGD("session", " PACA webGetSessionDetail callback exce" + e.getMessage());
                        }
                    }
                }
            };
            AQuery aq = new AQuery(this);
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", getAccessToken());
            String url = "session/"+sessionid;
            aq.ajax(GLOBAL_URL + url, JSONObject.class, cb);
        } catch (Exception e){
            hideProgress();
            LogUtils.LOGD("session","PACA webGetSessionDetail exce " + e.getMessage());
            finish();
        }
    }



    public void postEventOnMainThread(final Object event) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                BusProvider.getInstance().post(event);
                // bus.post(event);
            }
        });

    }


    public MUser getCallPacifyr() {
        MUser mUser=null;
        try{

            mUser= VideoActivity.mSessionDetailRoot.getUser();
        }catch (Exception e)
        {
            mUser=null;
        }
        return mUser;
    }
    public MUser getCallUser() {
        MUser mUser=null;
        try{
            mUser= VideoActivity.mSessionDetailRoot.getUser();
        }catch (Exception e)
        {
            mUser=null;
        }
        return mUser;
    }

    public HashMap<String , String> getRoleSearchMap() {
        HashMap<String , String> roleSearchApiMap = new HashMap<String , String>();

        roleSearchApiMap.put("customer", "search/pacifyrs");
        roleSearchApiMap.put("employee", "search/pacifyrs/forEmployees");
        roleSearchApiMap.put("paidMember", "search/pacifyrs/forPaidMembers");
        roleSearchApiMap.put("unpaidEmployee", "search/pacifyrs/forEmployees");

        return roleSearchApiMap;
    }


    private void signInCreatedUser(final Context context ) {
        try {
            try {

                Auth.scanUpdates(PacifyrAppCompactActivity.this);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            hideKeyboard();

                        } catch (Exception e) {
                            e.printStackTrace();
                            hideProgress();

                        }
                    }
                }, 900);


            } catch (Exception e) {
                hideProgress();
                hideStatus();

            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void showSnackbarPopup() {
        Log.d(TAG, "showSnackbarPopup");
        mSnackbar = Snackbar.make(findViewById(android.R.id.content), "Tap open to return to call.",
                Snackbar.LENGTH_INDEFINITE);

        mSnackbar.setAction("Open", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call your action method here
                if (isCallActive) {
                    mSnackbar.dismiss();
                    Intent intent = new Intent(PacifyrAppCompactActivity.this, VideoActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                }
                else {
                    mSnackbar.dismiss();
                }
            }
        });
        mSnackbar.show();
    }


    public void dismissSnackbarPopup() {
        Log.d(TAG, "dismissSnackbarPopup");
        if (isValid(mSnackbar) && mSnackbar.isShown()) {
            mSnackbar.dismiss();
        }
    }


    public void showMandatoryFieldsPopUp() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(PacifyrAppCompactActivity.this);
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = getLayoutInflater();
        AlertDialog alertDialog;
        View dialogView = inflater.inflate(R.layout.alert_appointment_mandatory_fields, null);
        dialogBuilder.setView(dialogView);
        TextView txtv_gender = dialogView.findViewById(R.id.txtv_gender);
        TextView zipCode = dialogView.findViewById(R.id.zipCode);
        LinearLayout LinearLayout_Submit=dialogView.findViewById(R.id.LinearLayout_Submit);
        alertDialog = dialogBuilder.create();
        if (isValid(getPrefManager().getGENDER()))
            txtv_gender.setVisibility(GONE);
        else
            txtv_gender.setVisibility(View.VISIBLE);

        if (isValid(getPrefManager().getZIP()))
            zipCode.setVisibility(GONE);
        else
            zipCode.setVisibility(View.VISIBLE);

        txtv_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getGenderListItems(txtv_gender);
            }
        });

        LinearLayout_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject editProfileObject=new JSONObject();
                editProfileObject = new JSONObject();
                try {
                    if (txtv_gender.getVisibility()==VISIBLE) {
                        if (isValid(txtv_gender.getText().toString())) {
                            editProfileObject.putOpt("gender", txtv_gender.getText().toString());
                        }
                        else {
                            txtv_gender.setError("select gender");
                            return;
                        }
                    }

                    if (zipCode.getVisibility()==VISIBLE) {
                        if (isValid(zipCode.getText().toString())) {
                            editProfileObject.putOpt("zipCode", zipCode.getText().toString());
                        } else {
                            zipCode.setError("Enter zipCode");
                            return ;
                        }
                    }


                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

                UpdateUserProfile(editProfileObject,alertDialog);
            }
        });


        alertDialog.show();
    }

    private void getGenderListItems(TextView customEditText_gender) {

        String title="Select your gender";
        ArrayList<String> genderList=new ArrayList();
        genderList.add("Male");
        genderList.add("Female");
        showListPopup(title,genderList,customEditText_gender);
    }


    private void showListPopup(String title,ArrayList<String> list,TextView customEditText_view) {

        // setup the alert builder
        String[] dataList=null;
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(PacifyrAppCompactActivity.this);
        builder.setTitle(title);

        if (isValid(list))
        {
            dataList=new String[list.size()];
            for (int i=0;i<list.size();i++)
            {
                dataList[i]=list.get(i);
            }
        }
        String[] finalDataList = dataList;
        builder.setItems(dataList, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                customEditText_view.setText(finalDataList[which]+"");
            }
        });

// create and show the alert dialog
        android.app.AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void UpdateUserProfile(JSONObject editProfileObject, AlertDialog alertDialog) {
        try {
            setLoading();
            showProgress();
            String  token = getPrefManager().getAccessToken();
            String  userid = getPrefManager().getUserID();
            String url = UtilityNetworkService.GLOBAL_URL + "user/editprofile/" + userid;
            com.koushikdutta.async.future.FutureCallback<JsonObject> futureCallback = new com.koushikdutta.async.future.FutureCallback<JsonObject>() {
                @Override
                public void onCompleted(Exception e, JsonObject result) {
                    try {
                        hideProgress();
                        if (isValid(result))
                        {
                            if (result.has("zipCode"))
                            {
                                String zipCode=result.get("zipCode").toString();
                                getPrefManager().saveZIP(zipCode);
                            }

                            if (result.has("gender"))
                            {
                                String gender=result.get("gender").toString();
                                getPrefManager().saveGENDER(gender);
                            }

                            if (isValid(alertDialog))
                                alertDialog.cancel();
                            Log.w("@@@",String.valueOf(result));

                        }

                    } catch (Exception eee) {
                        hideProgress();
                        //hideProgress();
                        Toast.makeText(getApplicationContext(), "Error :" + e.getMessage() + ", ex :" + eee.getMessage(), Toast.LENGTH_LONG).show();
                        eee.printStackTrace();
                    }
                }
            };
            String json = editProfileObject.toString();
            Ion.with(getApplicationContext()).load(url).setHeader("AccessToken", token).setHeader("enctype", "multipart/form-data").setMultipartParameter("data", json).asJsonObject().setCallback(futureCallback);
        }catch (Exception e)
        {   e.printStackTrace();
            hideProgress();}


    }

    public void showImageWithAnim(String url, ImageView image_logo){
        if (url == null){
            return;
        }
        image_logo.setVisibility(View.VISIBLE);
        image_logo.animate().alpha(1.0f).setDuration(500).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                image_logo.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        Glide.with(this).load(UtilityNetworkService.GLOBAL_IMAGE_URL + url).into(image_logo);
    }

    public void hideImageWithAnim(ImageView image_logo){
        if (image_logo != null){
            image_logo.animate().alpha(0.0f).setDuration(500).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    image_logo.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

        }
    }


    public void callPacifyrForRefreshBeforeScheduleUI(String id)
    {
        if (isCallActive)
        {
            showIOSDialogOK("Sorry","You can't schedule while session is going on.","OK");
            return;
        }
        try {
            setLoading("Loading...");
           showProgress();
            PrefManager prefM = PrefManager.getInstance(PacifyrAppCompactActivity.this);
            String token = prefM.getAccessToken();

            String url = UtilityNetworkService.GLOBAL_URL + "user/profile/" + id;
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    try {
                        hideProgress();
                        if(json!=null) {
                            ModelUserProfile pacifyer_obj2 = GsonUtils.getInstance().gsonToModelUserProfile(json);

                            MUser pacifyer_obj=convertModel(pacifyer_obj2);
                           if (isValid(pacifyer_obj)){
                               ScheduleBottomSheetFragment bottomSheetFragment = new ScheduleBottomSheetFragment();
                               bottomSheetFragment.setCancelable(false);
                               Bundle bundle=new Bundle();
                               bundle.putSerializable("OBJECT",pacifyer_obj);
                               bottomSheetFragment.setArguments(bundle);
                               bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());

                           }
                           else {
                               return;
                           }
                        }
                        else
                        {
                        return;
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        hideProgress();
                    }
                }
            };

            AQuery aq = new AQuery(PacifyrAppCompactActivity.this);
            //cb.header("Content-Type", "application/json");
            cb.header("AccessToken", token);
            aq.ajax(url, JSONObject.class, cb);

        }
        catch (Exception ex)
        {
            hideProgress();
            ex.printStackTrace();
        }

    }
}
