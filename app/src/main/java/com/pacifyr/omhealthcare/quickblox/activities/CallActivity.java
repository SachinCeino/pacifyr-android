package com.pacifyr.omhealthcare.quickblox.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ceino.utilitylibrary.model.pacifyr.MSessionDetailRoot;
import com.ceino.utilitylibrary.model.pacifyr.MUser;
import com.ceino.utilitylibrary.network.UtilityNetworkService;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.event.AlertTypeEvent;
import com.pacifyr.omhealthcare.event.CallTimerEvent;
import com.pacifyr.omhealthcare.event.NotifyEvent;
import com.pacifyr.omhealthcare.home.PacifyrDashboardActivity;
import com.pacifyr.omhealthcare.home.SessionSummaryActivity;
import com.pacifyr.omhealthcare.quickblox.App;
import com.pacifyr.omhealthcare.quickblox.db.QbUsersDbManager;
import com.pacifyr.omhealthcare.quickblox.fragments.AudioConversationFragment;
import com.pacifyr.omhealthcare.quickblox.fragments.BaseConversationFragment;
import com.pacifyr.omhealthcare.quickblox.fragments.ConversationFragmentCallbackListener;
import com.pacifyr.omhealthcare.quickblox.fragments.IncomeCallFragment;
import com.pacifyr.omhealthcare.quickblox.fragments.IncomeCallFragmentCallbackListener;
import com.pacifyr.omhealthcare.quickblox.fragments.OnCallEventsController;
import com.pacifyr.omhealthcare.quickblox.fragments.ScreenShareFragment;
import com.pacifyr.omhealthcare.quickblox.fragments.VideoConversationFragment;
import com.pacifyr.omhealthcare.quickblox.util.NetworkConnectionChecker;
import com.pacifyr.omhealthcare.quickblox.utils.Consts;
import com.pacifyr.omhealthcare.quickblox.utils.FragmentExecuotr;
import com.pacifyr.omhealthcare.quickblox.utils.PermissionsChecker;
import com.pacifyr.omhealthcare.quickblox.utils.QBEntityCallbackImpl;
import com.pacifyr.omhealthcare.quickblox.utils.RingtonePlayer;
import com.pacifyr.omhealthcare.quickblox.utils.SettingsUtil;
import com.pacifyr.omhealthcare.quickblox.utils.UsersUtils;
import com.pacifyr.omhealthcare.quickblox.utils.WebRtcSessionManager;
import com.pacifyr.omhealthcare.receiver.PacifyrReceiver;
import com.pacifyr.omhealthcare.utility.PacifyrAppCompactActivity;
import com.pacifyr.omhealthcare.utils.LogUtils;
import com.pacifyr.omhealthcare.utils.ToastHandler;
import com.quickblox.chat.QBChatService;
import com.quickblox.sample.core.utils.Toaster;
import com.quickblox.users.model.QBUser;
import com.quickblox.videochat.webrtc.AppRTCAudioManager;
import com.quickblox.videochat.webrtc.BaseSession;
import com.quickblox.videochat.webrtc.QBRTCCameraVideoCapturer;
import com.quickblox.videochat.webrtc.QBRTCClient;
import com.quickblox.videochat.webrtc.QBRTCConfig;
import com.quickblox.videochat.webrtc.QBRTCScreenCapturer;
import com.quickblox.videochat.webrtc.QBRTCSession;
import com.quickblox.videochat.webrtc.QBRTCTypes;
import com.quickblox.videochat.webrtc.QBSignalingSpec;
import com.quickblox.videochat.webrtc.callbacks.QBRTCClientSessionCallbacks;
import com.quickblox.videochat.webrtc.callbacks.QBRTCSessionEventsCallback;
import com.quickblox.videochat.webrtc.callbacks.QBRTCSessionStateCallback;
import com.quickblox.videochat.webrtc.callbacks.QBRTCSignalingCallback;
import com.quickblox.videochat.webrtc.exception.QBRTCSignalException;
import com.squareup.otto.Subscribe;

import org.jivesoftware.smack.AbstractConnectionListener;
import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.CameraVideoCapturer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ceino.utilitylibrary.network.UtilityNetworkService.GLOBAL_URL;
import static com.pacifyr.omhealthcare.home.ProfileScreenActivity.pacifyerDetial;

/*import com.quickblox.videochat.webrtc.BaseSession;*/

/**
 * QuickBlox team
 */
public class CallActivity extends PacifyrAppCompactActivity implements QBRTCClientSessionCallbacks, QBRTCSessionStateCallback, QBRTCSignalingCallback,
        OnCallEventsController, IncomeCallFragmentCallbackListener, ConversationFragmentCallbackListener, NetworkConnectionChecker.OnConnectivityChangedListener, ScreenShareFragment.OnSharingEvents {
    //PacifyrAppCompactActivity
    //BaseActivity
    private static final String TAG = "call";

    public static final String OPPONENTS_CALL_FRAGMENT = "opponents_call_fragment";
    public static final String INCOME_CALL_FRAGMENT = "income_call_fragment";
    public static final String CONVERSATION_CALL_FRAGMENT = "conversation_call_fragment";
    public static final String CALLER_NAME = "caller_name";
    public static final String SESSION_ID = "sessionID";
    public static final String START_CONVERSATION_REASON = "start_conversation_reason";

    private static final int REQUEST_MEDIA_PROJECTION = 1;

    private QBRTCSession currentSession;
    public List<QBUser> opponentsList;
    private Runnable showIncomingCallWindowTask;
    private Handler showIncomingCallWindowTaskHandler;
    private boolean closeByWifiStateAllow = true;
    private String hangUpReason;
    private boolean isInCommingCall;
    private QBRTCClient rtcClient;
    private OnChangeDynamicToggle onChangeDynamicCallback;
    private ConnectionListener connectionListener;
    private boolean wifiEnabled = true;
    private SharedPreferences sharedPref;
    private RingtonePlayer ringtonePlayer;
    private LinearLayout connectionView;
    private AppRTCAudioManager audioManager;
    private NetworkConnectionChecker networkConnectionChecker;
    private WebRtcSessionManager sessionManager;
    private QbUsersDbManager dbManager;
    private ArrayList<CurrentCallStateCallback> currentCallStateCallbackList = new ArrayList<>();
    private List<Integer> opponentsIdsList;
    private boolean callStarted;
    private boolean isVideoCall;
    private long expirationReconnectionTime;
    private int reconnectHangUpTimeMillis;
    private boolean headsetPlugged;
    private boolean previousDeviceEarPiece;
    private boolean showToastAfterHeadsetPlugged = true;
    private PermissionsChecker checker;
    private MediaProjectionManager mMediaProjectionManager;
    public static  MUser mPacifyr;
    public static  MSessionDetailRoot mSessionDetailRoot;
    public static boolean isConnected=false,isMissed=false,isDeclined=false,isOpponenetConnected=false,isOppenentHangUP=false;

    public static void start(Context context,boolean isIncomingCall) {
        mSessionDetailRoot=null;
        isConnected=false;isMissed=false;isDeclined=false;isOpponenetConnected=false;isOppenentHangUP=false;
        Intent intent = new Intent(context, CallActivity.class);
        intent.putExtra(Consts.EXTRA_IS_INCOMING_CALL, isIncomingCall);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    public static void start(Activity activity,
                             boolean isIncomingCall) {
        try {
            mSessionDetailRoot = null;
            isConnected = false;
            isMissed = false;
            isDeclined = false;
            isOpponenetConnected = false;
            isOppenentHangUP = false;
            Intent intent = new Intent(activity, CallActivity.class);
            intent.putExtra(Consts.EXTRA_IS_INCOMING_CALL, isIncomingCall);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
            //activity.finish();
        }catch (Exception e)
        {

        }
    }


    /*    public static void start(Activity activity,
                                 boolean isIncomingCall) {
            try {
                mSessionDetailRoot = null;
                isConnected = false;
                isMissed = false;
                isDeclined = false;
                isOpponenetConnected = false;
                isOppenentHangUP = false;
                Intent intent = new Intent(activity, CallActivity.class);
                intent.putExtra(Consts.EXTRA_IS_INCOMING_CALL, isIncomingCall);

                activity.startActivity(intent);
                //activity.finish();
            }catch (Exception e)
            {

            }
        }*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            if (isValid(PacifyrReceiver.notificationManager)) {
                PacifyrReceiver.notificationManager.cancel(PacifyrReceiver.notificationIdInt);
                PacifyrReceiver.notificationManager=null;
            }
            setScreenLockOff();
        }catch (Exception e)
        {

        }

        isConnected=false;isMissed=false;isDeclined=false;
        isOpponenetConnected=false;


        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        parseIntentExtras();
        Log.d("app_recgec","call"+sessionManager);
        sessionManager = WebRtcSessionManager.getInstance(this);

        if (!currentSessionExist()) {
//            we have already currentSession == null, so it's no reason to do further initialization
            finish();
            Log.d(TAG, "finish CallActivity");
            return;
        }
        if(isValid(sessionid))
            webGetSessionDetail(sessionid);

        initFields();
        initCurrentSession(currentSession);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        initQBRTCClient();
        initAudioManager();
        initWiFiManagerListener();

        //  ringtonePlayer = new RingtonePlayer(this, R.raw.beep);
        ringtonePlayer = new RingtonePlayer(this, R.raw.pacifyr_3);
        connectionView = (LinearLayout) View.inflate(this, R.layout.connection_popup, null);
        checker = new PermissionsChecker(CallActivity.this);

        startSuitableFragment(isInCommingCall);
    }


    private void startScreenSharing(final Intent data){
        ScreenShareFragment screenShareFragment = ScreenShareFragment.newIntstance();
        FragmentExecuotr.addFragmentWithBackStack(getSupportFragmentManager(), R.id.fragment_container, screenShareFragment, ScreenShareFragment.TAG);
        currentSession.getMediaStreamManager().setVideoCapturer(new QBRTCScreenCapturer(data, null));
    }

    private void returnToCamera() {
        try {
            currentSession.getMediaStreamManager().setVideoCapturer(new QBRTCCameraVideoCapturer(this, null));
        } catch (QBRTCCameraVideoCapturer.QBRTCCameraCapturerException e) {
            Log.d(TAG, "Error: device doesn't have camera");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,final Intent data) {
        Log.d(TAG, "onActivityResult requestCode="+requestCode +", resultCode= " + resultCode);
        if (requestCode == QBRTCScreenCapturer.REQUEST_MEDIA_PROJECTION) {
            if (resultCode == Activity.RESULT_OK) {
                startScreenSharing(data);
                Log.d(TAG, "Starting screen capture");
            }
            else {

            }
        }
    }

    private void startSuitableFragment(boolean isInComingCall) {
        if (isInComingCall) {
            ringtonePlayer = new RingtonePlayer(this, R.raw.pacifyr_3);
            if (UtilityNetworkService.isRelease) {
                ringtonePlayer.play(false);
            }
            initIncomingCallTask();
            startLoadAbsentUsers();
            addIncomeCallFragment();
            checkPermission();
        } else {
            ringtonePlayer = new RingtonePlayer(this, R.raw.beep);
            if (UtilityNetworkService.isRelease) {
                ringtonePlayer.play(true);
            }
            addConversationFragment(isInComingCall);
        }
    }

    private void checkPermission() {
        if (checker.lacksPermissions(Consts.PERMISSIONS)) {
            startPermissionsActivity(!isVideoCall);
        }
    }

    public void startPermissionsActivity(boolean checkOnlyAudio) {
        PermissionsActivity.startActivity(this, checkOnlyAudio, Consts.PERMISSIONS);
    }

    private void startLoadAbsentUsers() {
        ArrayList<QBUser> usersFromDb = dbManager.getAllUsers();
        ArrayList<Integer> allParticipantsOfCall = new ArrayList<>();
        allParticipantsOfCall.addAll(opponentsIdsList);

        if (isInCommingCall) {
            allParticipantsOfCall.add(currentSession.getCallerID());
        }

        ArrayList<Integer> idsUsersNeedLoad = UsersUtils.getIdsNotLoadedUsers(usersFromDb, allParticipantsOfCall);
        if (!idsUsersNeedLoad.isEmpty()) {
            requestExecutor.loadUsersByIds(idsUsersNeedLoad, new QBEntityCallbackImpl<ArrayList<QBUser>>() {
                @Override
                public void onSuccess(ArrayList<QBUser> result, Bundle params) {
                    dbManager.saveAllUsers(result, false);
                    needUpdateOpponentsList(result);
                }
            });
        }
    }

    private void needUpdateOpponentsList(ArrayList<QBUser> newUsers) {
        notifyCallStateListenersNeedUpdateOpponentsList(newUsers);
    }

    private boolean currentSessionExist() {
        currentSession = sessionManager.getCurrentSession();
        return currentSession != null;
    }

    private void initFields() {
        dbManager = QbUsersDbManager.getInstance(CallActivity.this);
        opponentsIdsList = currentSession.getOpponents();
    }

    @Override
    protected View getSnackbarAnchorView() {
        return null;
    }

    private void parseIntentExtras() {
        isInCommingCall = getIntent().getExtras().getBoolean(Consts.EXTRA_IS_INCOMING_CALL);
    }

    private void initAudioManager() {
        audioManager = AppRTCAudioManager.create(this, new AppRTCAudioManager.OnAudioManagerStateListener() {
            @Override
            public void onAudioChangedState(AppRTCAudioManager.AudioDevice audioDevice) {
                if (callStarted) {
                    if (audioManager.getSelectedAudioDevice() == AppRTCAudioManager.AudioDevice.EARPIECE) {
                        previousDeviceEarPiece = true;
                    } else if (audioManager.getSelectedAudioDevice() == AppRTCAudioManager.AudioDevice.SPEAKER_PHONE) {
                        previousDeviceEarPiece = false;
                    }
                    if (showToastAfterHeadsetPlugged) {
                       // Toaster.shortToast("Audio device switched to  " + audioDevice);
                    }
                }
            }
        });

        isVideoCall = QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO.equals(currentSession.getConferenceType());
        if (isVideoCall) {
            audioManager.setDefaultAudioDevice(AppRTCAudioManager.AudioDevice.SPEAKER_PHONE);
            Log.d(TAG, "AppRTCAudioManager.AudioDevice.SPEAKER_PHONE");
        } else {
            audioManager.setDefaultAudioDevice(AppRTCAudioManager.AudioDevice.EARPIECE);
            previousDeviceEarPiece = true;
            Log.d(TAG, "AppRTCAudioManager.AudioDevice.EARPIECE");
        }

        audioManager.setOnWiredHeadsetStateListener(new AppRTCAudioManager.OnWiredHeadsetStateListener() {
            @Override
            public void onWiredHeadsetStateChanged(boolean plugged, boolean hasMicrophone) {
                headsetPlugged = plugged;
                if (callStarted) {
                    Toaster.shortToast("Headset " + (plugged ? "plugged" : "unplugged"));
                }
                if (onChangeDynamicCallback != null) {
                    if (!plugged) {
                        showToastAfterHeadsetPlugged = false;
                        if (previousDeviceEarPiece) {
                            setAudioDeviceDelayed(AppRTCAudioManager.AudioDevice.EARPIECE);
                        } else {
                            setAudioDeviceDelayed(AppRTCAudioManager.AudioDevice.SPEAKER_PHONE);
                        }
                    }
                    onChangeDynamicCallback.enableDynamicToggle(plugged, previousDeviceEarPiece);
                }
            }
        });
        audioManager.init();
    }

    private void setAudioDeviceDelayed(final AppRTCAudioManager.AudioDevice audioDevice) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showToastAfterHeadsetPlugged = true;
                audioManager.setAudioDevice(audioDevice);
                }
        }, 500);
    }

    private void initQBRTCClient() {
        rtcClient = QBRTCClient.getInstance(this);

        rtcClient.setCameraErrorHandler(new CameraVideoCapturer.CameraEventsHandler() {
            @Override
            public void onCameraError(final String s) {

                showToast("Camera error: " + s);
            }

            @Override
            public void onCameraDisconnected() {
                showToast("Camera onCameraDisconnected: ");
            }

            @Override
            public void onCameraFreezed(String s) {
                showToast("Camera freezed: " + s);
                // hangUpCurrentSession();
                hangUpCurrentSessionMe();
            }

            @Override
            public void onCameraOpening(String s) {
              //  showToast("Camera aOpening: " + s);
            }

            @Override
            public void onFirstFrameAvailable() {
                //showToast("onFirstFrameAvailable: ");
            }

            @Override
            public void onCameraClosed() {
            }
        });


        // Configure
        //
        QBRTCConfig.setMaxOpponentsCount(Consts.MAX_OPPONENTS_COUNT);
        SettingsUtil.setSettingsStrategy(opponentsIdsList, sharedPref, CallActivity.this);
        SettingsUtil.configRTCTimers(CallActivity.this);
        QBRTCConfig.setDebugEnabled(true);


        // Add activity as callback to RTCClient
        rtcClient.addSessionCallbacksListener(this);
        // Start mange QBRTCSessions according to VideoCall parser's callbacks
        rtcClient.prepareToProcessCalls();
        connectionListener = new ConnectionListener();
        QBChatService.getInstance().addConnectionListener(connectionListener);

    }

    private void setExpirationReconnectionTime() {
        reconnectHangUpTimeMillis = SettingsUtil.getPreferenceInt(sharedPref, this, R.string.pref_disconnect_time_interval_key,
                R.string.pref_disconnect_time_interval_default_value) * 1000;
        expirationReconnectionTime = System.currentTimeMillis() + reconnectHangUpTimeMillis;
    }

    private void hangUpAfterLongReconnection() {
        Log.d(TAG, "hangUpAfterLongReconnection");
        if (expirationReconnectionTime < System.currentTimeMillis()) {
            hangUpCurrentSessionMe();
        }
    }

    @Override
    public void connectivityChanged(boolean availableNow) {
        Log.d(TAG, "connectivityChanged");
        if (callStarted && !availableNow) {
            showToast("Internet connection " + (availableNow ? "available" : " unavailable"));
        }
    }

    private void showNotificationPopUp(final int text, final boolean show) {
        Log.d(TAG, "showNotificationPopUp");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (show) {
                    ((TextView) connectionView.findViewById(R.id.notification)).setText(text);
                    if (connectionView.getParent() == null) {
                        ((ViewGroup) CallActivity.this.findViewById(R.id.fragment_container)).addView(connectionView);
                    }
                } else {
                    ((ViewGroup) CallActivity.this.findViewById(R.id.fragment_container)).removeView(connectionView);
                }
            }
        });

    }

    private void initWiFiManagerListener() {
        Log.d(TAG, "initWiFiManagerListener");
        networkConnectionChecker = new NetworkConnectionChecker(getApplication());
    }

    private void initIncomingCallTask() {
        Log.d(TAG, "initIncomingCallTask");
        showIncomingCallWindowTaskHandler = new Handler(Looper.myLooper());
        showIncomingCallWindowTask = new Runnable() {
            @Override
            public void run() {
                if (currentSession == null) {
                    return;
                }

                QBRTCSession.QBRTCSessionState currentSessionState = currentSession.getState();
                if (QBRTCSession.QBRTCSessionState.QB_RTC_SESSION_NEW.equals(currentSessionState)) {
                    rejectCurrentSession();
                } else {
                    ringtonePlayer.stop();
                    hangUpCurrentSessionMe();
                }
                Toaster.longToast("Call was stopped by timer");
            }
        };
    }


    private QBRTCSession getCurrentSession() {
        return currentSession;
    }

    public void rejectCurrentSession() {
        ringtonePlayer.stop();
        //showIOSDialogOK("","Pacifyr is Buzy","OK");
        //ToastHandler.newInstance(CallActivity.this).mustShowToast("Pacifyr is buzy");

        LogUtils.LOGD("call","rejectCurrentSession ");
        if (getCurrentSession() != null) {
            getCurrentSession().rejectCall(new HashMap<String, String>());
        }
        //jpppppppppppppp
        try {
            declineSessionWeb(sessionid);
            ///  stopSessionWeb(pacifyerDetial,sessionid);
            //  LogUtils.LOGD("call","rejectCurrentSession sessionid"+call_session_root.getSession().getId());
            //declineSessionWeb(call_session_root.getSession().getId());
        }
        catch (Exception e)
        {
            LogUtils.LOGD("call","rejectCurrentSession Exception e "+ e.getMessage());
        }
        //jpppppppppppppppppp
    }
    public String getOpponentID()
    {
        String id=null;
        try{

            if (isInCommingCall) {
                id=WebRtcSessionManager.userCallInfo.OpponentUserid;
            }
            else
            {
                id=pacifyerDetial.getId();
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return id;
    }
    public String getSessionID()
    {
        String id=null;
        try{

            if (isInCommingCall) {
                id=WebRtcSessionManager.userCallInfo.sessionId;
            }
            else
            {
                id=sessionid;
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return id;
    }
    /* @Override
     protected void onResume() {
         super.onResume();

         ((AppChatNotUSe) AppChatNotUSe.getInstance()).setCurrentActivity(this);
     }*/
    public void hangUpCurrentSession() {
        ringtonePlayer.stop();
        LogUtils.LOGD("call","hangUpCurrentSession");
        ringtonePlayer.stop();
        if (getCurrentSession() != null) {
            getCurrentSession().hangUp(new HashMap<String, String>());
        }
        //jpppppppppppppp
        try {
            //stopSessionWeb(getOpponentID(), sessionid);
            //  stopSessionWeb(sessionid);
            LogUtils.LOGD("call","hangUpCurrentSession sessionid"+sessionid);
            if(!isOpponenetConnected)
            {
                missedSessionWeb(sessionid);
            }
            finish();

           /* if (isInCommingCall) {
                stopSessionWeb(getOpponentID(), sessionid);
            }
            else
            {
                stopSessionWeb(getOpponentID(),sessionid);
            }*/
            // declineSessionWeb(sessionid);
            //
            // LogUtils.LOGD("call","hangUpCurrentSession sessionid"+call_session_root.getSession().getId());

            //  stopSessionWeb(pacifyerDetial,call_session_root.getSession().getId());
        }
        catch (Exception e)
        {
            LogUtils.LOGD("call","hangUpCurrentSession Exception e "+ e.getMessage());
        }
        //jpppppppppppppppppp

    }


    public void hangUpCurrentSessionMe() {
        LogUtils.LOGD("call","hangUpCurrentSession");
        ringtonePlayer.stop();
        if (getCurrentSession() != null) {
            getCurrentSession().hangUp(new HashMap<String, String>());
        }
        //jpppppppppppppp
        try {
            LogUtils.LOGD("call","CA hangUpCurrentSessionMe sessionid"+sessionid);
            if(!isOpponenetConnected)
            {
                missedSessionWeb(sessionid);
            } else if (!hasBillingStarted()) {
                showToast("Thank you for the call. You are not billed as Service was not provided");
            }
            finish();
        }
        catch (Exception e)
        {
            LogUtils.LOGD("call","CA hangUpCurrentSessionMe Exception e "+ e.getMessage());
        }
        //jpppppppppppppppppp

    }
    public void hangUpCurrentSessionOther() {
       // ToastHandler.newInstance(CallActivity.this).mustShowToast("Pacifyr is buzy");
        LogUtils.LOGD("call","hangUpCurrentSession");
        ringtonePlayer.stop();
        if (getCurrentSession() != null) {
            getCurrentSession().hangUp(new HashMap<String, String>());
        }
        //jpppppppppppppp
        try {

            isOppenentHangUP=true;
            //ToastHandler.newInstance(this).mustShowToast("User is hangs up the call");
            LogUtils.LOGD("call","CA hangUpCurrentSessionOther sessionid"+sessionid);

            if (!hasBillingStarted()) {
                showToast("Thank you for the call. You are not billed as Service was not provided");
            }
            finish();
        }
        catch (Exception e)
        {
            LogUtils.LOGD("call","CA hangUpCurrentSessionOther Exception e "+ e.getMessage());
        }
        //jpppppppppppppppppp
    }

    private void setAudioEnabled(boolean isAudioEnabled) {
        if (currentSession != null && currentSession.getMediaStreamManager() != null) {
            currentSession.getMediaStreamManager().getLocalAudioTrack().setEnabled(isAudioEnabled);
        }
    }

    private void setVideoEnabled(boolean isVideoEnabled) {
        if (currentSession != null && currentSession.getMediaStreamManager() != null) {
            currentSession.getMediaStreamManager().getLocalVideoTrack().setEnabled(isVideoEnabled);
        }
    }

    private void startIncomeCallTimer(long time) {
        showIncomingCallWindowTaskHandler.postAtTime(showIncomingCallWindowTask, SystemClock.uptimeMillis() + time);
    }

    private void stopIncomeCallTimer() {
        Log.d(TAG, "stopIncomeCallTimer");
        showIncomingCallWindowTaskHandler.removeCallbacks(showIncomingCallWindowTask);
    }


    @Override
    protected void onResume() {
        super.onResume();
       networkConnectionChecker.registerListener(this);
        ((App) App.getInstance()).setCurrentActivity(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        networkConnectionChecker.unregisterListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        QBChatService.getInstance().removeConnectionListener(connectionListener);
    }


    private void forbiddenCloseByWifiState() {
        closeByWifiStateAllow = false;
    }


    public void initCurrentSession(QBRTCSession session) {
        if (session != null) {
            Log.d(TAG, "Init new QBRTCSession");
            this.currentSession = session;
            this.currentSession.addSessionCallbacksListener(CallActivity.this);
            this.currentSession.addSignalingCallback(CallActivity.this);
        }
    }

    public void releaseCurrentSession() {
        Log.d(TAG, "Release current session");
        if (currentSession != null) {
            this.currentSession.removeSessionCallbacksListener(CallActivity.this);
            this.currentSession.removeSignalingCallback(CallActivity.this);
            rtcClient.removeSessionsCallbacksListener(CallActivity.this);
            this.currentSession = null;
        }
    }

    // ---------------Chat callback methods implementation  ----------------------//

    @Override
    public void onReceiveNewSession(final QBRTCSession session) {
        Log.d("app_rectest", "Session " + session.getSessionID() + " are income");

        if (getCurrentSession() != null) {
            Log.d(TAG, "Stop new session. Device now is busy");
            session.rejectCall(null);
        }
    }

    @Override
    public void onUserNotAnswer(QBRTCSession session, Integer userID) {
        if (!session.equals(getCurrentSession())) {
            return;
        }
        LogUtils.LOGD("call", "onUserNotAnswer getUserID "+ userID);
        missedSessionWeb(sessionid);
        ringtonePlayer.stop();
    }
    //dd
    @Override
    public void onUserNoActions(QBRTCSession qbrtcSession, Integer userID) {
        startIncomeCallTimer(0);
        LogUtils.LOGD("call", "onUserNoActions getUserID "+ userID);
    }

    @Override
    public void onCallAcceptByUser(QBRTCSession session, Integer userId, Map<String, String> userInfo) {
        /*if(isValid(userInfo.get("CALL_TYPE"))) {
            String callType = userInfo.get("CALL_TYPE");
            getPrefManager().saveCallType(callType);
        }*/
        ringtonePlayer.stop();
        if (!session.equals(getCurrentSession())) {
            return;
        }
        LogUtils.LOGD("call", "onCallAcceptByUser getUserID "+ userId);
        ringtonePlayer.stop();
    }

    @Override
    public void onCallRejectByUser(QBRTCSession session, Integer userID, Map<String, String> userInfo) {
        ringtonePlayer.stop();
        if (!session.equals(getCurrentSession())) {
            return;
        }
        LogUtils.LOGD("call", "onCallRejectByUser getUserID  "+ userID);
        //  LogUtils.LOGD("call", "onCallRejectByUser userInfo.size()  "+ userInfo.size());
        ringtonePlayer.stop();

        try {
        AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                CallActivity.isDeclined=true;
                if (json == null) {
                    com.ceino.utilitylibrary.ToastHandler.newInstance(CallActivity.this).showToast("Please Try again");
                    LogUtils.LOGD("call", "declineSessionWeb json null ");
                } else {
                    if (json != null) try {
                        LogUtils.LOGD("call", "declineSessionWeb json  " + json);
                    } catch (Exception e){
                        com.ceino.utilitylibrary.ToastHandler.newInstance(CallActivity.this).showToast("Please Try again");
                        LogUtils.LOGD("call", "declineSessionWeb exception : " + e.getMessage());
                    }
                }
            }
        };
        AQuery aq = new AQuery(this);
        cb.header("AccessToken", getAccessToken());
        cb.header("Content-Type", "application/json");
        JSONObject input = new JSONObject();
            input.putOpt("id", sessionid);
        aq.post(GLOBAL_URL + "session/declined", input, JSONObject.class, cb);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Toast.makeText(CallActivity.this,"Pacifyr is busy with another call",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStateChanged(BaseSession baseSession, BaseSession.QBRTCSessionState qbrtcSessionState) {

    }

    @Override
    public void onConnectedToUser(BaseSession baseSession, Integer integer) {
        ringtonePlayer.stop();
        callStarted = true;
        notifyCallStateListenersCallStarted();
        forbiddenCloseByWifiState();
        if (isInCommingCall) {
            stopIncomeCallTimer();
        }
        Log.d(TAG, "onConnectedToUser() is started");
    }

    @Override
    public void onDisconnectedFromUser(BaseSession baseSession, Integer integer) {
        ringtonePlayer.stop();
        Log.d(TAG, "onDisconnectedFromUser");
    }

    @Override
    public void onConnectionClosedForUser(BaseSession baseSession, Integer integer) {
        ringtonePlayer.stop();
        Log.d(TAG, "onConnectionClosedForUser");
// Close app after session close of network was disabled
        if (hangUpReason != null && hangUpReason.equals(Consts.WIFI_DISABLED)) {
            Intent returnIntent = new Intent();
            setResult(Consts.CALL_ACTIVITY_CLOSE_WIFI_DISABLED, returnIntent);
            finish();
        }
    }

    /* @Override
     public void onConnectionClosedForUser(QBRTCSession session, Integer userID) {
         // Close app after session close of network was disabled
         if (hangUpReason != null && hangUpReason.equals(Consts.WIFI_DISABLED)) {
             Intent returnIntent = new Intent();
             setResult(Consts.CALL_ACTIVITY_CLOSE_WIFI_DISABLED, returnIntent);
             finish();
         }
     }

     @Override
     public void onConnectedToUser(QBRTCSession session, final Integer userID) {
         callStarted = true;
         notifyCallStateListenersCallStarted();
         forbiddenCloseByWifiState();
         if (isInCommingCall) {
             stopIncomeCallTimer();
         }
         Log.d(TAG, "onConnectedToUser() is started");
     }
  @Override
  public void onDisconnectedFromUser(QBRTCSession session, Integer userID) {

  }*/
    @Override
    public void onSessionClosed(final QBRTCSession session) {
        ringtonePlayer.stop();
        Log.d(TAG, "Session " + session.getSessionID() + " start stop session");

        if (!hasBillingStarted()) {
            showToast("Thank you for the call. You are not billed as Service was not provided");
        }
        if (session.equals(getCurrentSession())) {
            Log.d(TAG, "Stop session");

            if (audioManager != null) {
                audioManager.close();
            }
            releaseCurrentSession();
            stopSessionTracking();

            closeByWifiStateAllow = true;

            try {
                mSessionDetailRoot = null;
                LogUtils.LOGD("call", "onDestroy sessionid" + sessionid);

                setScreenLockRelease();
                if(isValid(callTimerEvent))
                {
//                    stopSessionWeb(sessionid);
                    getPrefManager().saveSessionId(null);
              /*  if(isCustomer())
                {
                    stopSessionWeb(sessionid);
                }
                else {
                    SessionSummaryActivity.start(CallActivity.this);
                }*/
                }
                else if( !isMissed && !isDeclined && isOpponenetConnected &&!isOppenentHangUP) {
                    ToastHandler.newInstance(CallActivity.this).showToast("Calling Stop api "+sessionid);
//                    stopSessionWeb(sessionid);
                    getPrefManager().saveSessionId(null);
                    finish();
                }
                else if(!isMissed && !isDeclined && isOpponenetConnected&&isOppenentHangUP)
                {
                    SessionSummaryActivity.start(CallActivity.this);
                    getPrefManager().saveSessionId(null);
                }
                else
                {
                    if(isPacifyer())
                        PacifyrDashboardActivity.start(CallActivity.this);
                    else {
//                        stopSessionWeb(getCallPacifyr());
                        ToastHandler.newInstance(CallActivity.this).showToast("Pacifyr is buzy. Please try again");
                        //DashboardActivity.start(CallActivity.this);
                    }
                }



                isConnected=false;
                isMissed=false;
                isDeclined=false;
                isOpponenetConnected=false;
                isOppenentHangUP=false;


    /*    Intent intent=new Intent(CallActivity.this, SessionSummaryActivity.class);
        startActivity(intent);*/
            }catch (Exception e)
            {
                setScreenLockRelease();
                LogUtils.LOGD("call", "onDestroy exception" + e.getMessage());
            }
            finish();
            //  onDestroy();
        }
    }

    @Override
    public void onSessionStartClose(final QBRTCSession session) {
        if (session.equals(getCurrentSession())) {
            session.removeSessionCallbacksListener(CallActivity.this);
            notifyCallStateListenersCallStopped();
        }
    }



    private void showToast(final int message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toaster.shortToast(message);
            }
        });
    }

    private void showToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toaster.shortToast(message);
            }
        });
    }

    @Override
    public void onReceiveHangUpFromUser(final QBRTCSession session, final Integer userID, Map<String, String> map) {
        ringtonePlayer.stop();
        if (session.equals(getCurrentSession())) {

            if (userID.equals(session.getCallerID())) {
                // hangUpCurrentSession();

                Log.d(TAG, "initiator hung up the call");
                hangUpCurrentSessionOther();
            }

            QBUser participant = dbManager.getUserById(userID);
            final String participantName = participant != null ? participant.getFullName() : String.valueOf(userID);
//            String n=participant.getFullName();
         //   if(participant!=null){
        //    showToast("User " + participantName + " " + getString(R.string.text_status_hang_up) + " conversation");}
        }
    }

    private android.support.v4.app.Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.fragment_container);
    }

    private void addIncomeCallFragment() {
        Log.d(TAG, "QBRTCSession in addIncomeCallFragment is " + currentSession);

        if (currentSession != null) {
            IncomeCallFragment fragment = new IncomeCallFragment();
            FragmentExecuotr.addFragment(getSupportFragmentManager(), R.id.fragment_container, fragment, INCOME_CALL_FRAGMENT);
        } else {
            Log.d(TAG, "SKIP addIncomeCallFragment method");
        }

    }

    private void addConversationFragment(boolean isIncomingCall) {
        BaseConversationFragment conversationFragment = BaseConversationFragment.newInstance(
                isVideoCall
                        ? new VideoConversationFragment()
                        : new AudioConversationFragment(),
                isIncomingCall);
        FragmentExecuotr.addFragment(getSupportFragmentManager(), R.id.fragment_container, conversationFragment, conversationFragment.getClass().getSimpleName());
    }

    public SharedPreferences getDefaultSharedPrefs() {
        return sharedPref;
    }

    @Override
    public void onSuccessSendingPacket(QBSignalingSpec.QBSignalCMD qbSignalCMD, Integer integer) {
    }

    @Override
    public void onErrorSendingPacket(QBSignalingSpec.QBSignalCMD qbSignalCMD, Integer userId, QBRTCSignalException e) {
        //showToast(R.string.dlg_signal_error);
    }


    public void onUseHeadSet(boolean use) {
        audioManager.setManageHeadsetByDefault(use);
    }

    public void sendHeadsetState() {
        if (isInCommingCall) {
            onChangeDynamicCallback.enableDynamicToggle(headsetPlugged, previousDeviceEarPiece);
        }
    }

    ////////////////////////////// IncomeCallFragmentCallbackListener ////////////////////////////

    @Override
    public void onAcceptCurrentSession() {
        if (currentSession != null) {
            addConversationFragment(true);
            //  connectedSessionWeb(sessionid);
            // connectedSessionDetailWeb(sessionid);
        } else {
            Log.d(TAG, "SKIP addConversationFragment method");
        }
    }

    @Override
    public void onRejectCurrentSession() {
        rejectCurrentSession();
    }
    //////////////////////////////////////////   end   /////////////////////////////////////////////


    @Override
    public void onBackPressed() {
        android.support.v4.app.Fragment fragmentByTag = getSupportFragmentManager().findFragmentByTag(ScreenShareFragment.TAG);
        if (fragmentByTag instanceof ScreenShareFragment) {
            returnToCamera();
            super.onBackPressed();
        }
    }





    ////////////////////////////// ConversationFragmentCallbackListener ////////////////////////////

    @Override
    public void addTCClientConnectionCallback(QBRTCSessionStateCallback clientConnectionCallbacks) {
        if (currentSession != null) {
            currentSession.addSessionCallbacksListener(clientConnectionCallbacks);
        }
    }

    @Override
    public void addRTCSessionEventsCallback(QBRTCSessionEventsCallback eventsCallback) {
        Log.d("app_recgec","e: "+eventsCallback);
        QBRTCClient.getInstance(this).addSessionCallbacksListener(eventsCallback);
    }

    @Override
    public void onSetAudioEnabled(boolean isAudioEnabled) {
        setAudioEnabled(isAudioEnabled);
    }

    @Override
    public void onHangUpCurrentSession() {
        ///hangUpCurrentSession();
        hangUpCurrentSessionMe();
    }

    @TargetApi(21)
    @Override
    public void onStartScreenSharing() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }
        QBRTCScreenCapturer.requestPermissions(CallActivity.this);
    }

    @Override
    public void onSwitchCamera(CameraVideoCapturer.CameraSwitchHandler cameraSwitchHandler) {
        ((QBRTCCameraVideoCapturer)(currentSession.getMediaStreamManager().getVideoCapturer()))
                .switchCamera(cameraSwitchHandler);
    }

    @Override
    public void onSetVideoEnabled(boolean isNeedEnableCam) {
        setVideoEnabled(isNeedEnableCam);
    }

    @Override
    public void onSwitchAudio() {
        if (audioManager.getSelectedAudioDevice() == AppRTCAudioManager.AudioDevice.WIRED_HEADSET
                || audioManager.getSelectedAudioDevice() == AppRTCAudioManager.AudioDevice.EARPIECE) {
            audioManager.setAudioDevice(AppRTCAudioManager.AudioDevice.SPEAKER_PHONE);
        } else {
            audioManager.setAudioDevice(AppRTCAudioManager.AudioDevice.EARPIECE);
        }
    }

    @Override
    public void removeRTCClientConnectionCallback(QBRTCSessionStateCallback clientConnectionCallbacks) {
        if (currentSession != null) {
            currentSession.removeSessionCallbacksListener(clientConnectionCallbacks);
        }
    }

    @Override
    public void removeRTCSessionEventsCallback(QBRTCSessionEventsCallback eventsCallback) {
        QBRTCClient.getInstance(this).removeSessionsCallbacksListener(eventsCallback);
    }

    @Override
    public void addCurrentCallStateCallback(CurrentCallStateCallback currentCallStateCallback) {
        currentCallStateCallbackList.add(currentCallStateCallback);
    }

    @Override
    public void removeCurrentCallStateCallback(CurrentCallStateCallback currentCallStateCallback) {
        currentCallStateCallbackList.remove(currentCallStateCallback);
    }

    @Override
    public void addOnChangeDynamicToggle(OnChangeDynamicToggle onChangeDynamicCallback) {
        this.onChangeDynamicCallback = onChangeDynamicCallback;
        sendHeadsetState();
    }

    @Override
    public void removeOnChangeDynamicToggle(OnChangeDynamicToggle onChangeDynamicCallback) {
        this.onChangeDynamicCallback = null;
    }

    @Override
    public void onStopPreview() {
        onBackPressed();
    }




    //////////////////////////////////////////   end   /////////////////////////////////////////////
    private class ConnectionListener extends AbstractConnectionListener {
        @Override
        public void connectionClosedOnError(Exception e) {
            //showNotificationPopUp(R.string.connection_was_lost, true);
            setExpirationReconnectionTime();
        }

        @Override
        public void reconnectionSuccessful() {
            //showNotificationPopUp(R.string.connection_was_lost, false);
        }

        @Override
        public void reconnectingIn(int seconds) {
            Log.d(TAG, "reconnectingIn " + seconds);
            if (!callStarted) {
                hangUpAfterLongReconnection();
            }
        }
    }

    public interface OnChangeDynamicToggle {
        void enableDynamicToggle(boolean plugged, boolean wasEarpiece);
    }


    public interface CurrentCallStateCallback {
        void onCallStarted();

        void onCallStopped();

        void onOpponentsListUpdated(ArrayList<QBUser> newUsers);
    }

    private void notifyCallStateListenersCallStarted() {
        for (CurrentCallStateCallback callback : currentCallStateCallbackList) {
            callback.onCallStarted();
        }
    }

    private void notifyCallStateListenersCallStopped() {
        for (CurrentCallStateCallback callback : currentCallStateCallbackList) {
            callback.onCallStopped();
        }
    }

    private void notifyCallStateListenersNeedUpdateOpponentsList(final ArrayList<QBUser> newUsers) {
        for (CurrentCallStateCallback callback : currentCallStateCallbackList) {
            callback.onOpponentsListUpdated(newUsers);
        }
    }
/////////////////////////////////////

    @Override
    protected void onDestroy() {
        stopSessionTracking();
        super.onDestroy();
    }


    @Override
    public void onSetValues() throws Exception {

    }

    @Subscribe
    public void onNotifyEvent(NotifyEvent event) {

    }

    @Subscribe
    public void onAlertTypeEvent(AlertTypeEvent event) {

    }

    public CallTimerEvent callTimerEvent =null;
    @Subscribe
    public void onCallTimerEvent(CallTimerEvent event) {
        try{
            if(event.isFinishCall) {
                callTimerEvent=event;
                finish();
            }
        }catch (Exception e)
        {
            Log.d("session", "CA onCallTimerEvent exception = " + e.getMessage());
        }

    }


}