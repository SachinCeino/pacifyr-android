package com.pacifyr.client.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.pacifyr.client.R;
import com.pacifyr.client.activity.SplashScreen;
import com.pacifyr.client.base.PacifyrActivity;
import com.pacifyr.client.event.CallEvent;
import com.pacifyr.client.event.InternetConnectionEvent;
import com.pacifyr.client.event.RateSessionEvent;
import com.pacifyr.client.home.DashboardActivity;
import com.pacifyr.client.orginals.DynamicLink;
import com.pacifyr.client.quickblox.activities.CallActivity;
import com.pacifyr.client.quickblox.chat.ui.activity.DialogsActivity;
import com.pacifyr.client.quickblox.services.CallService;
import com.pacifyr.client.quickblox.utils.WebRtcSessionManager;
import com.pacifyr.client.receiver.NotificationUtils;
import com.pacifyr.client.receiver.PacifyrReceiver;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import me.leolin.shortcutbadger.ShortcutBadger;

import static androidx.core.app.NotificationCompat.BADGE_ICON_SMALL;
import static com.pacifyr.client.receiver.NotificationUtils.isAppIsInBackground;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private static boolean isCall = false;
    String typeId = null;
    private PrefManager prefM;

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        PrefManager.getInstance(getApplicationContext()).putSharedString(PrefManager.FCM_TOKEN, s);
        sendRegistrationToServer(s);
    }

    public Boolean isValid(Object object) {
        return object != null;

    }

    public String getAccessToken() {
        return PrefManager.getInstance(getApplicationContext()).getSharedString(PrefManager.ACCESSTOKEN, null);

    }

    public String getUserID() {
        return PrefManager.getInstance(getApplicationContext()).getSharedString(PrefManager.USERID, null);
    }

    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
        if (!isValid(getUserID())) {
            Log.d(TAG, "Invalid userId cancelling sendRegistrationToServer");
            return;
        }
        if (!isValid(getAccessToken())) {
            Log.d(TAG, "Invalid access token cancelling sendRegistrationToServer");
            return;
        }
        new AmazonSNSTokenHandler(getApplicationContext()).register();
    }

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages
        // are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data
        // messages are the type
        // traditionally used with GCM. Notification messages are only received here in
        // onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated
        // notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages
        // containing both notification
        // and data payloads are treated as notification messages. The Firebase console always
        // sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        prefM = PrefManager.getInstance(getApplicationContext());
        String data = remoteMessage.getData().toString();
        Log.d("@@@@/noti", "From: " + remoteMessage.toString());
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            Log.d("@@@@/noti", "From: " + remoteMessage.getData());

            if(data.contains("screen")){
                try {
                    JSONObject j= new JSONObject(data);
                  //  DynamicLink.setScreenFromNotification(j.getString("screen"),j.getString("screen_data"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            else{
                handleNow(remoteMessage);
            }


        }
        //2020-07-09 12:00:00.786 31831-4123/com.pacifyr.pacifyrapp D/@@@@/noti: From: {typeId=5f06b2e0afdb7539ae8b1d66, type=reminder, sound=default, message=Please start your session with provider "Niva Live Provider"}

        // Check if message contains a notification payload.


        if (remoteMessage.getNotification() != null) {
            String title = "";
            String body = "";
            String type = null;
            if (remoteMessage.getNotification().getTitle() != null) {
                title = remoteMessage.getNotification().getTitle();
            }

            if (remoteMessage.getNotification().getBody() != null) {
                body = remoteMessage.getNotification().getBody();
            }
            if (remoteMessage.getData().containsKey("type")) {
                type = remoteMessage.getData().get("type");
            }
            if (type != null && type.equalsIgnoreCase("chat")) {
                return;
            }
            if (!title.isEmpty() && !body.isEmpty()) {
                Intent intent = new Intent(NotificationUtils.NOTIFY_INTENT_GENERAL);
                intent.putExtra("title", title);
                intent.putExtra("body", body);
                LocalBroadcastManager.getInstance(MyFirebaseMessagingService.this).sendBroadcast(intent);
            }
        }
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendCardAddedNotification method below.
    }

    private void handleNow(RemoteMessage remoteMessage) {
        final Context context = getApplicationContext();
        if (remoteMessage != null) {

            if (isValid(PrefManager.getInstance(context).getUserID())) {

                try {
                    String message = null;
                    String _id = null;
                    String actual_type = null;

                    if (remoteMessage.getData().containsKey("message")) {
                        message = remoteMessage.getData().get("message") + "";
                    }
                    if (remoteMessage.getData().containsKey("id")) {
                        _id = remoteMessage.getData().get("id");
                    }
                    if (remoteMessage.getData().containsKey("type")) {
                        actual_type = remoteMessage.getData().get("type");
                    }
                    if (remoteMessage.getData().containsKey("typeId")) {
                        typeId = remoteMessage.getData().get("typeId");
                    }

                    /*if (actual_type.equals("reminder") && isValid(typeId))
                    {
                        Intent i=new Intent(context, DashboardActivity.class);
                        i.putExtra("typeId",typeId);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    }*/

                    if (actual_type != null && (actual_type.equals("billing") )) {
                        Intent callIntent = new Intent(CallEvent.CALL_EVENT);
                        callIntent.putExtra("type", actual_type);
                        callIntent.putExtra("id", _id);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(callIntent);
                    }
                    else if (actual_type != null && actual_type.equals("rating")){

                        EventBus.getDefault().post(new RateSessionEvent(_id,message));
                    }

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

                                            showNotification(context, _id, message, actual_type);

                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    LogUtils.LOGD("app_reca", "app live notification tempseesionid cleaed");
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

                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    LogUtils.LOGD("app_recb", "app live notification tempseesionid cleaed");
                                                }
                                            }, 10000);
                                        }
                                    }
                                } else if (actual_type.equalsIgnoreCase("chat")
                                        ||actual_type.equalsIgnoreCase("confirmBooking") || actual_type.equalsIgnoreCase("rejectBooking")) {
                                    Intent intent = new Intent(NotificationUtils.NOTIFY_INTENT);
                                    intent.putExtra("type", actual_type);
                                    intent.putExtra("message", message);
                                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                                }

                                else if (actual_type.equalsIgnoreCase("payment")) {
                                    Log.w("@@@","PAYTM_PAYMENT_EVENT");
                                    Intent intent = new Intent(NotificationUtils.PAYTM_PAYMENT_EVENT);
                                    intent.putExtra("type", actual_type);
                                    intent.putExtra("message", message);
                                    intent.putExtra("id", _id);
                                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                                }
                                else if (actual_type.equals("reminder") && isValid(typeId))
                                {
                                    Intent intent = new Intent(NotificationUtils.APPOINTMENT_EVENT);
                                    intent.putExtra("typeId", typeId);
                                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                                }
                                //{type=sessionDeclined, sound=default, message=Your call was declined by the provider "VINEESH PROVIDER "}

                                else if (actual_type.equals("sessionDeclined"))
                                {
                                    Intent intent = new Intent(NotificationUtils.SESSION_DECLAINED);
                                    intent.putExtra("message", message);
                                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                                }

                                else if (actual_type.equalsIgnoreCase("chat")
                                        ||actual_type.equalsIgnoreCase("confirmBooking")
                                        || actual_type.equalsIgnoreCase("rejectBooking")
                                        || actual_type.equalsIgnoreCase("reminder")) {
                                    Intent intent = new Intent(NotificationUtils.NOTIFY_INTENT);
                                    intent.putExtra("type", actual_type);
                                    intent.putExtra("message", message);
                                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
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

                                            //  showNotification(context,_id,message,actual_type);
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    LogUtils.LOGD("app_recc", "app live notification tempseesionid cleaed");
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

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                LogUtils.LOGD("app_recd", "app live notification tempseesionid cleaed");
                                            }
                                        }, 5000);
                                    }

                                }
                                else if (actual_type.equals("sessionDeclined"))
                                {
                                    Intent intent = new Intent(NotificationUtils.SESSION_DECLAINED);
                                    intent.putExtra("message", message);
                                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                                }
                            }
                        }

                    }

                    if (!isAppIsInBackground(context)) {
                        Intent background = new Intent(context, CallService.class);
                        context.startService(background);
                    }
                    LogUtils.LOGD("app_rec", "PR  onReceive End ");
                } catch (Exception e) {
                    LogUtils.LOGD("app_rec", "notification" + " exception: " + e.getMessage());
                }
            }
        }
    }
    // [END receive_message]


    // [START on_new_token]


    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    public Boolean isValid(String text) {
        if (text != null) return !text.trim().equalsIgnoreCase("");
        return false;

    }

    private void showNotification(Context context, String qsid, String message, String actualType) {

        PrefManager prefM=PrefManager.getInstance(context);
        int count=0;
        if (prefM.getBadgeValue()!=0)
        {
            count=prefM.getBadgeValue()+1;
            prefM.saveBadgeValue(count);
        }
        else
        {
            count=1;
            prefM.saveBadgeValue(count);
        }



        Intent cIntent = null;
        PendingIntent pendingIntent = null;
        if (actualType.equals("chat"))
        {

            cIntent = new Intent(context, DialogsActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addNextIntentWithParentStack(cIntent);
            pendingIntent =
                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);
        }
        else if (actualType.equals("reminder") && isValid(typeId)){

            cIntent = new Intent(context, DashboardActivity.class);
            cIntent.putExtra("typeId", typeId);
            cIntent.putExtra("type", "reminder");
            cIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            cIntent.setAction(Long.toString(System.currentTimeMillis()));

            pendingIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(),
                    cIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        else {
            boolean isNeedToOpenAppointments=false;
            if (actualType.equals("confirmBooking") ||actualType.equalsIgnoreCase("rejectBooking")) {
                isNeedToOpenAppointments=true;
            }

            cIntent = new Intent(context, SplashScreen.class);
            Bundle b = new Bundle();
            // b.putString("type", type);
            b.putString("id", qsid);
            b.putString("actualType", actualType);
            cIntent.putExtra("Bundle", b);
            cIntent.putExtra("isNeedToOpenAppointments",isNeedToOpenAppointments);
            cIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            cIntent.setAction(Long.toString(System.currentTimeMillis()));

            pendingIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(),
                    cIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        // cIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,Constants.getChannelId(context))
                .setSmallIcon(R.drawable.ic_pacifyr_notification)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message))
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentIntent(pendingIntent)
                .setSound(notificationSound)
                .setBadgeIconType(BADGE_ICON_SMALL)
                .setNumber(prefM.getBadgeValue())
                .setAutoCancel(true);


        Notification notification = builder.build();
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //notification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;
        final int mNotificationId = (int) System.currentTimeMillis();
        mNotificationManager.notify(mNotificationId, notification);

       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isMarshmallowabove=true;
        }*/
        if (isCall) {
            isCall = false;
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {

                    try {
                        if (PacifyrReceiver.notificationManager != null) {
                            PacifyrReceiver.notificationManager.cancel(PacifyrReceiver.notificationIdInt);
                            PacifyrReceiver.notificationManager = null;
                        }
                        //setScreenLockOff();
                    } catch (Exception e) {

                    }
                }
            }, 2000);
          /*  if(!isMarshmallowabove)
                mNotificationManager.notify(notificationIdInt, notification);
        } else {
            mNotificationManager.notify(notificationIdInt, notification);*/
        }

    }


}