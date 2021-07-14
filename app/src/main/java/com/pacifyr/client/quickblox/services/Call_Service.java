package com.pacifyr.client.quickblox.services;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.pacifyr.client.R;
import com.pacifyr.client.quickblox.chat.utils.chat.ChatHelper;
import com.pacifyr.client.quickblox.utils.Consts;
import com.pacifyr.client.utils.Constants;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.QBSession;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBSignaling;
import com.quickblox.chat.listeners.QBVideoChatSignalingManagerListener;
import com.quickblox.core.LogLevel;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.sample.core.utils.SharedPrefsHelper;
import com.quickblox.users.model.QBUser;
import com.quickblox.videochat.webrtc.BaseSession;
import com.quickblox.videochat.webrtc.QBRTCClient;
import com.quickblox.videochat.webrtc.QBRTCSession;
import com.quickblox.videochat.webrtc.callbacks.QBRTCClientSessionCallbacks;
import com.quickblox.videochat.webrtc.callbacks.QBRTCClientVideoTracksCallbacks;
import com.quickblox.videochat.webrtc.view.QBRTCVideoTrack;

import java.util.Map;

/**
 * QuickBlox team
 */
public class Call_Service extends Service implements QBRTCClientSessionCallbacks,
        QBRTCClientVideoTracksCallbacks {
    private static String TAG = Call_Service.class.getSimpleName();
    public static QBRTCSession incommingSession;
    private static int FOREGROUND_NOTIFICATION_ID = 111;
    private QBUser currentUser;
    private QBChatService chatService;
    private PrefManager prefManager;

    public static void logout(Context context) {
        Log.d(TAG, "logout");
        if (QBChatService.getInstance() != null) {
            try {
                QBChatService.getInstance().logout();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Intent intent = new Intent(context, Call_Service.class);
        context.stopService(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        QBSettings.getInstance().setLogLevel(LogLevel.DEBUG);
        QBChatService.setDebugEnabled(true);
        QBChatService.setConfigurationBuilder(ChatHelper.buildChatConfigs());
        SharedPrefsHelper sharedPrefsHelper = SharedPrefsHelper.getInstance();
        if (sharedPrefsHelper.hasQbUser()) {
            Log.d("HHH", "App have logined user");
//            currentUser = sharedPrefsHelper.getQbUser();
        }

        if (intent != null && intent.hasExtra(Consts.EXTRA_QB_USER)) {
            currentUser = (QBUser) intent.getSerializableExtra(Consts.EXTRA_QB_USER);
        }
        if (currentUser != null) {
            currentUser.setTags(null);

            // CREATE SESSION WITH USER
            // If you use create session with user data,
            // then the user will be logged in automatically
            prefManager = PrefManager.getInstance(Call_Service.this);
           // createSession();

            QBRTCClient.getInstance(this).prepareToProcessCalls();
            QBRTCClient.getInstance(Call_Service.this).addSessionCallbacksListener(this);

        }
        return START_STICKY;
    }

    private void createSession() {
        QBAuth.createSession(currentUser).performAsync(new QBEntityCallback<QBSession>() {
            @Override
            public void onSuccess(QBSession qbSession, Bundle bundle) {
//                currentUser.setId(qbSession.getUserId());
                loginToChat();

            }

            @Override
            public void onError(QBResponseException e) {
                Log.e(TAG, "createSession exception " + e != null ? e.getMessage() : "null");
                e.printStackTrace();
            }
        });
    }

    private void addQbSignalling() {
        try {
            QBChatService.getInstance().getVideoChatWebRTCSignalingManager()
                    .addSignalingManagerListener(new QBVideoChatSignalingManagerListener() {
                        @Override
                        public void signalingCreated(QBSignaling qbSignaling, boolean createdLocally) {
                            if (!createdLocally) {
                                QBRTCClient.getInstance(Call_Service.this).addSignaling(qbSignaling);
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*@Subscribe
    public void onQbChatLogin(QbChatLogin qbChatLogin) {
        if (QBChatService.getInstance().isLoggedIn()) {
            boolean callsAvail = prefManager.getIsOnline() != null && prefManager.getIsOnline().equalsIgnoreCase(
                    "true");
            if (callsAvail) {
                startForeground(FOREGROUND_NOTIFICATION_ID,
                        createNotification(Call_Service.this, "You are available for calls " +
                                "and chat"));
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForeground(FOREGROUND_NOTIFICATION_ID,
                            createNotification(Call_Service.this, "You are available for chat " +
                                    "only"));
                }
            }
        } else {
            createSession();
        }
    }*/

    private void loginToChat() {
        Log.d(TAG, "loginToChat()");
        chatService = QBChatService.getInstance();
        if (chatService.isLoggedIn()) {
            addQbSignalling();
            Log.d(TAG, "Already logged In");
            return;
        }
        chatService.login(currentUser, new QBEntityCallback<QBUser>() {

            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                Log.d(TAG, "Successfully logged In");
                /*QbChatLogin qbChatLogin = new QbChatLogin();
                qbChatLogin.setLoggedIn(true);
                BusProvider.getInstance().post(qbChatLogin);*/
                addQbSignalling();
               /* if (prefManager.getIsOnline() != null && prefManager.getIsOnline().equalsIgnoreCase(
                        "true")) {
                    startForeground(FOREGROUND_NOTIFICATION_ID,
                            createNotification(Call_Service.this, "You are available for calls " +
                                    "and chat"));
                } else if (prefManager.getIsChatSubscriptionRequired() != null && prefManager.getIsChatSubscriptionRequired().equalsIgnoreCase(
                        "true")) {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForeground(FOREGROUND_NOTIFICATION_ID,
                                createNotification(Call_Service.this, "You are available for chat " +
                                        "only"));
                    }
                }
                else{
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForeground(FOREGROUND_NOTIFICATION_ID,
                                createNotification(Call_Service.this, "You are offline"));
                    }
                }*/
            }

            @Override
            public void onError(QBResponseException e) {
//                Log.e(TAG, "Chat login error ->" + e != null ? e.getMessage() : "");
                e.printStackTrace();
                try {
                    if (e.getHttpStatusCode() == -1) {
                       /* QbChatLogin qbChatLogin = new QbChatLogin();
                        qbChatLogin.setLoggedIn(true);
                        BusProvider.getInstance().post(qbChatLogin);*/
                        addQbSignalling();
                    }
                } catch (Exception e22) {
                    Log.w("Exception ", e22.toString());
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
        try {
            /*if (chatService != null) {
                chatService.logout();
            }*/
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                stopForeground(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Notification createNotification(Context context, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,
                Constants.getChannelId(context))
                .setSmallIcon(R.drawable.ic_pacifyr_notification)
                .setContentText("")
                .setContentTitle(message);
        return builder.build();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onReceiveNewSession(QBRTCSession qbrtcSession) {

//        Toast.makeText(Call_Service.this, "Incoming session", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onUserNoActions(QBRTCSession qbrtcSession, Integer integer) {

    }

    @Override
    public void onSessionStartClose(QBRTCSession qbrtcSession) {
        if (qbrtcSession != null && qbrtcSession.getSessionID() != null && incommingSession != null && incommingSession.getSessionID() != null) {
            if (incommingSession.getSessionID().equalsIgnoreCase(qbrtcSession.getSessionID())) {
                incommingSession = null;
            }
        }
    }

    @Override
    public void onLocalVideoTrackReceive(BaseSession baseSession, QBRTCVideoTrack qbrtcVideoTrack) {

    }

    @Override
    public void onRemoteVideoTrackReceive(BaseSession baseSession, QBRTCVideoTrack qbrtcVideoTrack, Integer integer) {

    }

    @Override
    public void onUserNotAnswer(QBRTCSession qbrtcSession, Integer integer) {
        if (!qbrtcSession.equals(incommingSession)) {
            return;
        }

    }

    @Override
    public void onCallRejectByUser(QBRTCSession qbrtcSession, Integer integer, Map<String, String> map) {

    }


    @Override
    public void onCallAcceptByUser(QBRTCSession qbrtcSession, Integer integer, Map<String, String> map) {

    }

    @Override
    public void onReceiveHangUpFromUser(QBRTCSession qbrtcSession, Integer integer, Map<String, String> map) {

    }

    @Override
    public void onSessionClosed(QBRTCSession qbrtcSession) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean stopService(Intent name) {
        return super.stopService(name);
    }
}
