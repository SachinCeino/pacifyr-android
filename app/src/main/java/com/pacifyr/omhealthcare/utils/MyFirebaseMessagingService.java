package com.pacifyr.omhealthcare.utils;

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
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.ceino.utilitylibrary.utils.PrefManager;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.activity.SplachScreen;
import com.pacifyr.omhealthcare.base.PacifyerActivity;
import com.pacifyr.omhealthcare.event.CallEvent;
import com.pacifyr.omhealthcare.quickblox.activities.CallActivity;
import com.pacifyr.omhealthcare.quickblox.services.CallService;
import com.pacifyr.omhealthcare.quickblox.utils.WebRtcSessionManager;
import com.pacifyr.omhealthcare.receiver.NotificationUtils;
import com.pacifyr.omhealthcare.receiver.PacifyrReceiver;

import static com.pacifyr.omhealthcare.receiver.NotificationUtils.isAppIsInBackground;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private static boolean isCall = false;

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

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            handleNow(remoteMessage);
            /* if (*//* Check if data needs to be processed by long running job *//* true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                //scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow();
            }*/

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
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

                    if (actual_type != null && (actual_type.equals("billing") || actual_type.equals("rating"))) {
                        Intent callIntent = new Intent(CallEvent.CALL_EVENT);
                        callIntent.putExtra("type", actual_type);
                        callIntent.putExtra("id", _id);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(callIntent);
                    }

                    LogUtils.LOGD("app_rec", "PR  onReceive message " + message);
                    LogUtils.LOGD("app_rec", "PR  onReceive actual_type " + actual_type);
                    LogUtils.LOGD("app_rec", "PR  onReceive _id " + _id);

                    if (actual_type != null) {
                        if (message != null && !message.equalsIgnoreCase("") && !message.equalsIgnoreCase("null")) {


                            SplachScreen.trigger = "fromNotification";


                            if (isAppIsInBackground(context)) {
                                if (actual_type.equalsIgnoreCase("session")) {
                                    isCall = true;
                                    LogUtils.LOGD("app_reca", " app live  notification session " + WebRtcSessionManager.currentSession);
                                    if (WebRtcSessionManager.currentSession == null) {
                                        LogUtils.LOGD("app_reca", " app live  notification session " + WebRtcSessionManager.tempSession);

                                        if (WebRtcSessionManager.tempSession != null) {
                                            PacifyerActivity.sessionid = _id;
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
                                            PacifyerActivity.sessionid = _id;
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
                                } else if (actual_type.equalsIgnoreCase("chat")) {
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
                                            PacifyerActivity.sessionid = _id;
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
                                        PacifyerActivity.sessionid = _id;
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
        Intent cIntent = new Intent(context, SplachScreen.class);
        Bundle b = new Bundle();
        // b.putString("type", type);
        b.putString("id", qsid);
        b.putString("actualType", actualType);
        cIntent.putExtra("Bundle", b);
        cIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        cIntent.setAction(Long.toString(System.currentTimeMillis()));

        PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(),
                cIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        // cIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Constants.CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(message)
                .setContentTitle("Pacifyr")
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