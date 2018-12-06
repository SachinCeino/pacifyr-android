package com.pacifyr.omhealthcare.quickblox.services;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ceino.utilitylibrary.network.UtilityNetworkService;
import com.ceino.utilitylibrary.utils.PrefManager;
import com.pacifyr.omhealthcare.quickblox.chat.utils.chat.ChatHelper;
import com.pacifyr.omhealthcare.quickblox.util.ChatPingAlarmManager;
import com.pacifyr.omhealthcare.quickblox.utils.Consts;
import com.pacifyr.omhealthcare.quickblox.utils.SettingsUtil;
import com.pacifyr.omhealthcare.quickblox.utils.WebRtcSessionManager;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.QBSession;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBSignaling;
import com.quickblox.chat.listeners.QBVideoChatSignalingManagerListener;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.model.QBUser;
import com.quickblox.videochat.webrtc.QBRTCClient;
import com.quickblox.videochat.webrtc.QBRTCConfig;

import org.jivesoftware.smackx.ping.PingFailedListener;

/**
 * QuickBlox team
 */
public class CallService extends Service {
    // private static final String TAG = CallService.class.getSimpleName();
    private static final String TAG = "CallService";
    private QBChatService chatService;
    private QBRTCClient rtcClient;
    private PendingIntent pendingIntent;
    private int currentCommand;
    private QBUser currentUser;


    public CallService() {
    }
    public CallService(Context applicationContext) {
    }

    public static void start(Context context, QBUser qbUser, PendingIntent pendingIntent) {
        Intent intent = new Intent(context, CallService.class);
        intent.putExtra(Consts.EXTRA_COMMAND_TO_SERVICE, Consts.COMMAND_LOGIN);
        intent.putExtra(Consts.EXTRA_QB_USER, qbUser);
        intent.putExtra(Consts.EXTRA_PENDING_INTENT, pendingIntent);
        Log.d(TAG, "CallService start method");
        context.startService(intent);
    }

    public static void start(Context context, QBUser qbUser) {
        start(context, qbUser, null);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "CallService onCreate()");
        createChatService();

        Log.d(TAG, "CallService onCreate() end ");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "CallService started");

        parseIntentExtras(intent);

        startSuitableActions();

//        onTaskRemoved(intent);

        return START_STICKY;
    }

    private void parseIntentExtras(Intent intent) {
        if (intent != null && intent.getExtras() != null) {
            currentCommand = intent.getIntExtra(Consts.EXTRA_COMMAND_TO_SERVICE, Consts.COMMAND_NOT_FOUND);
            pendingIntent = intent.getParcelableExtra(Consts.EXTRA_PENDING_INTENT);
            currentUser = (QBUser) intent.getSerializableExtra(Consts.EXTRA_QB_USER);
            Log.d(TAG, "CallService parseIntentExtras getPassword "+currentUser.getPassword());
        }
    }

    private void startSuitableActions() {
        if (currentCommand == Consts.COMMAND_LOGIN) {
            startLoginToChat();
        } else if (currentCommand == Consts.COMMAND_LOGOUT) {
            logout();
        }
    }

    private void createChatService() {
        Log.d(TAG, "CallService createChatService");
        if (chatService == null) {
            QBChatService.setDebugEnabled(true);
            chatService = QBChatService.getInstance();
            Log.d(TAG, "CallService createChatService chatService "+chatService);
        }
    }

    private void startLoginToChat() {
        Log.d(TAG, "CallService startLoginToChat");
        if (!chatService.isLoggedIn()) {
            loginToChat(currentUser);
        } else {
            sendResultToActivity(true, null);
        }
    }

    private void loginToChat(final QBUser qbUser) {
        Log.d(TAG, "CallService loginToChat getEmail "+qbUser.getEmail());
        Log.d(TAG, "CallService loginToChat getId"+qbUser.getId());
        Log.d(TAG, "CallService loginToChat getPassword"+qbUser.getPassword());
        if(qbUser.getPassword()==null){
            qbUser.setPassword(PrefManager.getInstance(getApplicationContext()).getUserID());
        }
        if(qbUser.getLogin()==null){
            qbUser.setLogin(PrefManager.getInstance(getApplicationContext()).getUserID());
        }

        QBAuth.createSession().performAsync(new QBEntityCallback<QBSession>() {
            @Override
            public void onSuccess(QBSession qbSession, Bundle bundle) {
                chatService.login(qbUser, new QBEntityCallback<QBUser>() {
                    @Override
                    public void onSuccess(QBUser qbUser2, Bundle bundle) {
                        Log.d(TAG, "CallService loginToChat onSuccess");
                        startActionsOnSuccessLogin();

                        ChatHelper.getInstance().login(qbUser, new QBEntityCallback<Void>() {
                            @Override
                            public void onSuccess(Void result, Bundle bundle) {
                                //ToastHandler.newInstance(getApplicationContext()).showToast("Now you are ready to call");
                                Log.d(TAG, "CallService ChatHelper loginToChat onSuccess");
                            }

                            @Override
                            public void onError(QBResponseException e) {
                                Log.d(TAG, "CallService ChatHelper loginToChat onError");
                            }
                        });
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Log.d(TAG, "CallService loginToChat onError " + e.getMessage());
                        sendResultToActivity(false, e.getMessage() != null
                                ? e.getMessage()
                                : "Login error");
                    }
                });
            }

            @Override
            public void onError(QBResponseException e) {
                Log.d(TAG, "CallService QBAuth.createSession() error  "+e.getMessage());
            }
        });

    }

    private void startActionsOnSuccessLogin() {
        Log.d(TAG, "CallService startActionsOnSuccessLogin");
        initPingListener();
        initQBRTCClient();
        sendResultToActivity(true, null);
    }

    private void initPingListener() {
        Log.d(TAG, "CallService initPingListener");
        ChatPingAlarmManager.onCreate(this);
        ChatPingAlarmManager.getInstanceFor().addPingListener(new PingFailedListener() {
            @Override
            public void pingFailed() {
                Log.d(TAG, "CallService Ping chat server failed");
            }
        });
    }

    private void initQBRTCClient() {
        try {
            Log.d(TAG, "CallService initQBRTCClient");
            rtcClient = QBRTCClient.getInstance(getApplicationContext());
            // Add signalling manager
            chatService.getVideoChatWebRTCSignalingManager().addSignalingManagerListener(new QBVideoChatSignalingManagerListener() {
                @Override
                public void signalingCreated(QBSignaling qbSignaling, boolean createdLocally) {
                    if (!createdLocally) {
                        rtcClient.addSignaling(qbSignaling);
                    }
                }
            });

            // Configure
            QBRTCConfig.setDebugEnabled(!UtilityNetworkService.isRelease);
            SettingsUtil.configRTCTimers(CallService.this);

            // Add service as callback to RTCClient
            rtcClient.addSessionCallbacksListener(WebRtcSessionManager.getInstance(this));
            rtcClient.prepareToProcessCalls();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendResultToActivity(boolean isSuccess, String errorMessage) {
        if (pendingIntent != null) {
            Log.d(TAG, "CallService sendResultToActivity()");
            try {
                Intent intent = new Intent();
                intent.putExtra(Consts.EXTRA_LOGIN_RESULT, isSuccess);
                intent.putExtra(Consts.EXTRA_LOGIN_ERROR_MESSAGE, errorMessage);

                pendingIntent.send(CallService.this, Consts.EXTRA_LOGIN_RESULT_CODE, intent);
            } catch (PendingIntent.CanceledException e) {
                String errorMessageSendingResult = e.getMessage();
                Log.d(TAG, errorMessageSendingResult != null
                        ? errorMessageSendingResult
                        : "Error sending result to activity");
            }
        }
    }

    public static void logout(Context context) {
        Log.d(TAG, "CallService logout()");
        Intent intent = new Intent(context, CallService.class);
        intent.putExtra(Consts.EXTRA_COMMAND_TO_SERVICE, Consts.COMMAND_LOGOUT);
        context.startService(intent);
    }

    private void logout() {
        destroyRtcClientAndChat();
    }

    private void destroyRtcClientAndChat() {
        Log.d(TAG, "CallService destroyRtcClientAndChat()");
        if (rtcClient != null) {
            rtcClient.destroy();
        }
        ChatPingAlarmManager.onDestroy();
        if (chatService != null) {
            chatService.logout(new QBEntityCallback<Void>() {
                @Override
                public void onSuccess(Void aVoid, Bundle bundle) {
                    chatService.destroy();
                }

                @Override
                public void onError(QBResponseException e) {
                    Log.d(TAG, "CallService logout onError " + e.getMessage());
                    chatService.destroy();
                }
            });
        }
        stopSelf();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "CallService onDestroy()");
        super.onDestroy();
        Intent serviceIntent=new Intent(this,CallService.class);
        stopService(serviceIntent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "CallService onBind)");
        return null;
    }

    /*@Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.d(TAG, "CallService onTaskRemoved()");
        Intent restartservice=new Intent(getApplicationContext(),this.getClass());
        restartservice.setPackage(getPackageName());
        startService(restartservice);

        super.onTaskRemoved(rootIntent);

        //  destroyRtcClientAndChat();
    }*/
}
