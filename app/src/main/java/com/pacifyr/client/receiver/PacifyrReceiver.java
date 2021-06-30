package com.pacifyr.client.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.core.app.NotificationCompat;
import androidx.legacy.content.WakefulBroadcastReceiver;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.pacifyr.client.activity.SplashScreen;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.pacifyr.client.R;
import com.pacifyr.client.base.PacifyrActivity;
import com.pacifyr.client.event.BusProvider;
import com.pacifyr.client.event.CallEvent;
import com.pacifyr.client.quickblox.activities.CallActivity;
import com.pacifyr.client.quickblox.services.CallService;
import com.pacifyr.client.quickblox.utils.WebRtcSessionManager;
import com.pacifyr.client.utils.Constants;
import com.pacifyr.client.utils.LogUtils;
import com.pacifyr.client.utils.ToastHandler;

import org.json.JSONObject;

import static com.pacifyr.client.receiver.NotificationUtils.isAppIsInBackground;

/**
 * Created by developer on 21/6/17.
 */

public class PacifyrReceiver extends WakefulBroadcastReceiver {

    public static boolean fromBg = false;
    public static String temp_sessionid=null;
    static String notificationId = "";


    // private Api api;
    // private ArrayList<News_Object> news;
    private final String TAG = PacifyrReceiver.class.getSimpleName();
    JSONObject object;
    // private String type;
    private String id;
    private String alert;
    private Intent parseIntent;
    private NotificationUtils notificationUtils;

    public  static boolean isMarshmallowabove=false;
    public  static NotificationManager notificationManager;
    public  static    int notificationIdInt = 1;
    private  static boolean isCall=false;
    Context context;

    public PacifyrReceiver() {
        super();
    }

    private static void postEventOnMainThread(final Object event) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                BusProvider.getInstance().post(event);
                // bus.post(event);
            }
        });
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtils.LOGD("app_rec","notification" + " intent:" + intent);
        if(isValid(PrefManager.getInstance(context).getUserID())) {

            this.context=context;

            if (intent != null) {

                Bundle extras = intent.getExtras();

           /* for (String key : extras.keySet()) {
                LogUtils.LOGD("jithish","key value..... :" + key + " => " + extras.get(key) + ";");
            }*/

                try {
                    String message = extras.getString("message") + "";
                    String _id = extras.getString("id", "");
                    //   String type = extras.getString("collection", "");
                    String actual_type = extras.getString("type", "");
//                String commentValue = message.replaceAll("%20", "\\+");
                    if (actual_type != null && (actual_type.equals("billing") || actual_type.equals("rating"))) {
                        Intent callIntent = new Intent(CallEvent.CALL_EVENT);
                        callIntent.putExtra("type", actual_type);
                        callIntent.putExtra("id", _id);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(callIntent);
                    }

                    Intent background = new Intent(context, CallService.class);
                    context.startService(background);

                    LogUtils.LOGD("app_rec", "PR  onReceive message " + message);
                    LogUtils.LOGD("app_rec", "PR  onReceive actual_type " + actual_type);
                    LogUtils.LOGD("app_rec", "PR  onReceive _id " + _id);

                    if (actual_type != null) {
                        if (message != null && !message.equalsIgnoreCase("") && !message.equalsIgnoreCase("null")) {


                            SplashScreen.trigger = "fromNotification";


                            if (isAppIsInBackground(context)) {
                                if (actual_type.equalsIgnoreCase("session")) {
                                    isCall = true;
                                    LogUtils.LOGD("app_reca", " app live  notification session " + WebRtcSessionManager.currentSession);
                                    if (WebRtcSessionManager.currentSession == null) {
                                        LogUtils.LOGD("app_reca", " app live  notification session " + WebRtcSessionManager.tempSession);

                                        if (WebRtcSessionManager.tempSession != null) {
                                            PacifyrActivity.sessionid = _id;
                                            WebRtcSessionManager.currentSession = WebRtcSessionManager.tempSession;
                                            WebRtcSessionManager.tempSession = null;
                                            CallActivity.start(context, true);
                                            ToastHandler.newInstance(context).showToast("call data recevied from backend 1");

                                        } else {
                                            temp_sessionid = _id;



                                            showNotification(context, _id, message, actual_type);

                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    LogUtils.LOGD("app_reca", "app live notification tempseesionid cleaed");
                                                    temp_sessionid = null;
                                                }
                                            }, 10000);


                                        }
                                    }
                                } else {
                                    isCall = false;
                                    showNotification(context, _id, message, actual_type);
                                }
                            } else {

                                LogUtils.LOGD("app_recb", " app live notification ");
                                isCall = false;
                                if (actual_type.equalsIgnoreCase("session")) {
                                    isCall = true;
                                    LogUtils.LOGD("app_recb", " app live  notification session " + WebRtcSessionManager.tempSession);
                                    if (WebRtcSessionManager.currentSession == null) {
                                        LogUtils.LOGD("app_recbbb1", " app live  notification session " + WebRtcSessionManager.currentSession);
                                        if (WebRtcSessionManager.tempSession != null) {
                                            LogUtils.LOGD("app_recb", " app live  notification session " + WebRtcSessionManager.tempSession);
                                            LogUtils.LOGD("app_recb", " app live notification Call ");
                                            PacifyrActivity.sessionid = _id;
                                            WebRtcSessionManager.currentSession = WebRtcSessionManager.tempSession;
                                            WebRtcSessionManager.tempSession = null;
                                            ToastHandler.newInstance(context).showToast("call data recevied from backend 1");
                                            CallActivity.start(context, true);
                                        } else {

                                            temp_sessionid = _id;
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    LogUtils.LOGD("app_recb", "app live notification tempseesionid cleaed");
                                                    temp_sessionid = null;
                                                }
                                            }, 10000);
                                        }
                                    }
                                }
                            }
                        } else {
                            if (isAppIsInBackground(context)) {
                                isCall = false;
                                if (actual_type.equalsIgnoreCase("session")) {
                                    isCall = true;
                                    LogUtils.LOGD("app_recc", " app live  notification session " + WebRtcSessionManager.tempSession);
                                    if (WebRtcSessionManager.currentSession == null) {
                                        if (WebRtcSessionManager.tempSession != null) {

                                            LogUtils.LOGD("app_rec", " app live notification Call ");
                                            PacifyrActivity.sessionid = _id;
                                            WebRtcSessionManager.currentSession = WebRtcSessionManager.tempSession;
                                            WebRtcSessionManager.tempSession = null;
                                            ToastHandler.newInstance(context).showToast("call data recevied from backend 1");
                                            CallActivity.start(context, true);
                                        } else {

                                            temp_sessionid = _id;
                                            //  showNotification(context,_id,message,actual_type);
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    LogUtils.LOGD("app_recc", "app live notification tempseesionid cleaed");
                                                    temp_sessionid = null;
                                                }
                                            }, 10000);
                                        }
                                    }
                                }


                            } else {
                                isCall = false;
                                if (actual_type.equalsIgnoreCase("session")) {
                                    isCall = true;
                                    if (WebRtcSessionManager.tempSession != null) {
                                        LogUtils.LOGD("app_recd", " app live notification Call ");
                                        PacifyrActivity.sessionid = _id;
                                        WebRtcSessionManager.currentSession = WebRtcSessionManager.tempSession;
                                        WebRtcSessionManager.tempSession = null;
                                        ToastHandler.newInstance(context).showToast("call data recevied from backend 2 ");
                                        CallActivity.start(context, true);
                                    } else {

                                        temp_sessionid = _id;
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                LogUtils.LOGD("app_recd", "app live notification tempseesionid cleaed");
                                                temp_sessionid = null;
                                            }
                                        }, 5000);
                                    }

                                }
                            }
                        }

                    }

                    LogUtils.LOGD("app_rec", "PR  onReceive End ");
                } catch (Exception e) {
                    LogUtils.LOGD("app_rec", "notification" + " exception: " + e.getMessage());
                }
            }
        }
    }

    public Boolean isValid(String text) {
        if (text != null) return !text.trim().equalsIgnoreCase("");
        return false;

    }
    private void showNotification(Context context, String qsid,  String message, String actualType) {
        Intent cIntent = new Intent(context, SplashScreen.class);
        Bundle b = new Bundle();
        // b.putString("type", type);
        b.putString("id", qsid);
        b.putString("actualType",actualType);
        cIntent.putExtra("Bundle", b);
        cIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        cIntent.setAction(Long.toString(System.currentTimeMillis()));

        PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(),
                cIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        // cIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Constants.getChannelId(context))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(message)
                .setContentTitle(context.getResources().getString(R.string.app_name))
                .setContentIntent(pendingIntent)
                .setSound(notificationSound);


        Notification notification = builder.build();
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //notification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        final int mNotificationId = (int) System.currentTimeMillis();
        mNotificationManager.notify(mNotificationId, notification);

       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isMarshmallowabove=true;
        }*/
        if (isCall) {
            notificationManager = mNotificationManager;
            notificationIdInt = mNotificationId;
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {

                    try {
                        if (PacifyrReceiver.notificationManager!=null) {
                            PacifyrReceiver.notificationManager.cancel(PacifyrReceiver.notificationIdInt);
                            PacifyrReceiver.notificationManager=null;
                        }
                        //setScreenLockOff();
                    }catch (Exception e)
                    {

                    }
                }
            }, 2000);
          /*  if(!isMarshmallowabove)
                mNotificationManager.notify(notificationIdInt, notification);
        } else {
            mNotificationManager.notify(notificationIdInt, notification);*/
        }

    }


/*
    private void signInCreatedUser(final Context context ) {
        try {
           final PrefManager prefManager = PrefManager.getInstance(context);
            String userid = prefManager.getUserID();
            final String userID = PrefManager.getInstance(context).getUserID();
            //String mail = modelPhoneLogin.getUser().getEmail();
            final QBUser user = new QBUser(userID, userID);
            //user.setEmail(mail);
            QBSettings.getInstance().init(context, APP_ID, AUTH_KEY, AUTH_SECRET);
            QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
            if( !QBAuth.getSession().isCanceled()) {
                QBAuth.createSession().performAsync(new QBEntityCallback<QBSession>() {
                    @Override
                    public void onSuccess(QBSession qbSession, Bundle bundle) {
                        App.getInstance().getQbResRequestExecutor().signInUser(user, new com.pacifyr.pacifyrapp.quickblox.utils.QBEntityCallbackImpl<QBUser>() {
                            @Override
                            public void onSuccess(QBUser qbUser, Bundle params) {
                                qbUser.setPassword(userID);
                                Log.d("jp", "LPA signInCreatedUser getPassword " + qbUser.getPassword());
                                saveUserData(qbUser);
                                startLoginService(qbUser);
                                try {
                                    String basetoken = BaseService.getBaseService().getToken();

                                    String qbID = qbUser.getId().toString();
                                    prefManager.saveQBID(qbUser.getId().toString());
                                    getPrefManager().saveBaseToken(basetoken);
                                    hideProgress();

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                hideKeyboard();
                                                com.pacifyrapp.utilitylibrary.ToastHandler.newInstance(context).showToast("Logged into qb");

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                hideProgress();

                                            }
                                        }
                                    }, 900);


                                } catch (Exception e) {
                                    hideProgress();
                                    hideStatus();
                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                    getPrefManager().clearSharedAll();
                                    LoginManager mLoginManager = LoginManager.getInstance();
                                    if (isValid(mLoginManager))
                                        mLoginManager.logOut();
                                    Intent intent = new Intent(context, SignActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }

                            @Override
                            public void onError(QBResponseException responseException) {
                                //  hideProgressDialog();
                                //Toaster.longToast("User signInUser failed!" + responseException.getMessage());

                                Toast.makeText(context, "Session Expired...! Please login again." + responseException.getMessage(),
                                        Toast.LENGTH_LONG).show();
                                // responseException.printStackTrace();
                                getPrefManager().clearSharedAll();
                                //LoginManager.getInstance().logOut();
                                LoginManager mLoginManager = LoginManager.getInstance();
                                if (isValid(mLoginManager))
                                    mLoginManager.logOut();
                                Intent intent = new Intent(context, SignActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
*/

}

