package com.pacifyr.omhealthcare.quickblox.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.ceino.utilitylibrary.ToastHandler;
import com.ceino.utilitylibrary.model.pacifyr.MUser;
import com.pacifyr.omhealthcare.Payment.AlertAddCrediCardActivty;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.base.PacifyerActivity;
import com.pacifyr.omhealthcare.event.CallTimerEvent;
import com.pacifyr.omhealthcare.network.NetworkService;
import com.pacifyr.omhealthcare.quickblox.activities.CallActivity;
import com.pacifyr.omhealthcare.quickblox.db.QbUsersDbManager;
import com.pacifyr.omhealthcare.quickblox.utils.CollectionsUtils;
import com.pacifyr.omhealthcare.quickblox.utils.Consts;
import com.pacifyr.omhealthcare.quickblox.utils.UsersUtils;
import com.pacifyr.omhealthcare.quickblox.utils.WebRtcSessionManager;
import com.quickblox.chat.QBChatService;
/*import com.quickblox.sample.groupchatwebrtc.R;
import com.quickblox.sample.groupchatwebrtc.activities.CallActivity;
import com.quickblox.sample.groupchatwebrtc.db.QbUsersDbManager;
import com.quickblox.sample.groupchatwebrtc.utils.CollectionsUtils;
import com.quickblox.sample.groupchatwebrtc.utils.Consts;
import com.quickblox.sample.groupchatwebrtc.utils.UsersUtils;
import com.quickblox.sample.groupchatwebrtc.utils.WebRtcSessionManager;*/
import com.quickblox.users.model.QBUser;
/*import com.quickblox.videochat.webrtc.BaseSession;*/
import com.quickblox.videochat.webrtc.QBRTCSession;
import com.quickblox.videochat.webrtc.QBRTCTypes;

import java.util.ArrayList;

public abstract class BaseConversationFragment extends BaseToolBarFragment implements CallActivity.CurrentCallStateCallback {

    private static final String TAG = BaseConversationFragment.class.getSimpleName();
    protected QbUsersDbManager dbManager;
    protected WebRtcSessionManager sessionManager;
    private boolean isIncomingCall;
    protected QBRTCSession currentSession;
    protected ArrayList<QBUser> opponents;
    private QBRTCTypes.QBConferenceType qbConferenceType;

    private ToggleButton micToggleVideoCall;
    private ImageButton handUpVideoCall;
    protected ConversationFragmentCallbackListener conversationFragmentCallbackListener;
    protected Chronometer timerChronometer;
    public CountDownTimer timer;
    private boolean isMessageProcessed;
    protected boolean isStarted;
    protected View outgoingOpponentsRelativeLayout;
    public TextView allOpponentsTextView;
    protected TextView ringingTextView;
    protected QBUser currentUser;
    public ArrayList<MUser>call_particpants_list=new ArrayList<MUser>();


    public static BaseConversationFragment newInstance(BaseConversationFragment baseConversationFragment, boolean isIncomingCall) {
        Log.d(TAG, "isIncomingCall =  " + isIncomingCall);
        Bundle args = new Bundle();
        args.putBoolean(Consts.EXTRA_IS_INCOMING_CALL, isIncomingCall);

        baseConversationFragment.setArguments(args);

        return baseConversationFragment;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            conversationFragmentCallbackListener = (ConversationFragmentCallbackListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ConversationFragmentCallbackListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        conversationFragmentCallbackListener.addCurrentCallStateCallback(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        sessionManager = WebRtcSessionManager.getInstance(getActivity());
        currentSession = sessionManager.getCurrentSession();
        if (currentSession == null) {
            Log.d(TAG, "currentSession = null onCreateView");
            return view;
        }
        initFields();
        initViews(view);
        initActionBar();
        initButtonsListener();
        prepareAndShowOutgoingScreen();

        return view;
    }

    private void prepareAndShowOutgoingScreen() {
        configureOutgoingScreen();
      setPartipantViews();
        //allOpponentsTextView.setText(CollectionsUtils.makeStringFromUsersFullNames(opponents));
    }

    public void setPartipantViews() {
        try{
            allOpponentsTextView.setVisibility(View.VISIBLE);

            if(isValid(call_particpants_list,2)) {
                defSetText(allOpponentsTextView, call_particpants_list.get(1).getName());
            }
            else
            {
                defSetText(allOpponentsTextView, CallActivity.mPacifyr.getName());
            }

        }catch (Exception e)
        {
           e.printStackTrace();
        }
    }
    protected abstract void configureOutgoingScreen();

    private void initActionBar() {
        configureToolbar();
        configureActionBar();
    }

    protected abstract void configureActionBar();

    protected abstract void configureToolbar();

    protected void initFields() {
        currentUser = QBChatService.getInstance().getUser();
        dbManager = QbUsersDbManager.getInstance(getActivity());
        sessionManager = WebRtcSessionManager.getInstance(getActivity());
        currentSession = sessionManager.getCurrentSession();

        if (getArguments() != null) {
            isIncomingCall = getArguments().getBoolean(Consts.EXTRA_IS_INCOMING_CALL);
        }

        initOpponentsList();

        qbConferenceType = currentSession.getConferenceType();

        Log.d(TAG, "opponents: " + opponents.toString());
        Log.d(TAG, "currentSession " + currentSession.toString());
    }

    @Override
    public void onStart() {
        super.onStart();
        if (currentSession == null) {
            Log.d(TAG, "currentSession = null onStart");
            return;

        }
/*jppp*/
        if (currentSession.getState() != QBRTCSession.QBRTCSessionState.QB_RTC_SESSION_CONNECTED) {
            if (isIncomingCall) {
                currentSession.acceptCall(null);
            } else {
                currentSession.startCall(null);
            }
            isMessageProcessed = true;
        }

      /*  if (currentSession.getState() != QBRTCSession.QBRTCSessionState.QB_RTC_SESSION_ACTIVE) {
            if (isIncomingCall) {
                currentSession.acceptCall(null);
            } else {
                currentSession.startCall(null);
            }
            isMessageProcessed = true;
        }*/
    }

    @Override
    public void onDestroy() {
        conversationFragmentCallbackListener.removeCurrentCallStateCallback(this);
        super.onDestroy();
    }

    protected void initViews(View view) {
        micToggleVideoCall = (ToggleButton) view.findViewById(R.id.toggle_mic);
        handUpVideoCall = (ImageButton) view.findViewById(R.id.button_hangup_call);
        outgoingOpponentsRelativeLayout = view.findViewById(R.id.layout_background_outgoing_screen);
        allOpponentsTextView = (TextView) view.findViewById(R.id.text_outgoing_opponents_names);
        ringingTextView = (TextView) view.findViewById(R.id.text_ringing);

        if (isIncomingCall) {
            hideOutgoingScreen();
        }
    }

    protected void initButtonsListener() {

        micToggleVideoCall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                conversationFragmentCallbackListener.onSetAudioEnabled(isChecked);
            }
        });

        handUpVideoCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    actionButtonsEnabled(false);
                    handUpVideoCall.setEnabled(false);
                    handUpVideoCall.setActivated(false);

                    conversationFragmentCallbackListener.onHangUpCurrentSession();
                    Log.d(TAG, "Call is stopped");
            }
        });
    }

    protected void actionButtonsEnabled(boolean inability) {

        micToggleVideoCall.setEnabled(inability);

        // inactivate toggle buttons
        micToggleVideoCall.setActivated(inability);
    }

    private void startTimer() {
        if (!isStarted) {
            timerChronometer.setVisibility(View.VISIBLE);
            timerChronometer.setBase(SystemClock.elapsedRealtime());
            timerChronometer.start();
            if(isCustomer())
            if(!isCardExist()) {
                if(isValid(NetworkService.mCurrentUser)) {
                    long time = NetworkService.mCurrentUser.getAvailableMinutes();
                    setAvaiableTimer((time * 60) - 15);
               /* timerChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {

                    @Override
                    public void onChronometerTick(Chronometer chronometer) {
                        // TODO Auto-generated method stub
                        //    chronometer.g
                        String currentTime = timerChronometer.getText().toString();
                        if(currentTime.equals("03:00")) //put time according to you
                        {
                            chrono.stop();
                        }
                    }
                });*/
                }


            }
            isStarted = true;
        }
    }
    boolean isFirsttime=false;
   public  void setAvaiableTimer(long availableseconds)
    {
        try {
            isFirsttime=false;
            if(availableseconds<30) {
                postEventOnMainThread(new CallTimerEvent(true));
                return;
            }
            availableseconds=availableseconds*1000;
            Log.d("session", "BCF setAvaiableTimer availableseconds = " + availableseconds);
            if(timer != null) {
                timer.cancel();
                timer = null;
            }
            //Log.d("session", "BCF setAvaiableTimer availableseconds = " + availableseconds);
            timer = new CountDownTimer(availableseconds, 10000) {

                public void onTick(long millisUntilFinished) {
                    Log.d("session", "BCF CountDownTimer millisUntilFinished = " + millisUntilFinished);
                    if((300>=(millisUntilFinished/10000)&&!isFirsttime)) {
                        isFirsttime=true;
                        setAlertStatusInBlue2("Add Card", "Your free minutes are expired. Please continue enjoying ourhi services by updating your credit card information", "Ok", "NO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                hideProgress();
                                hideStatus2();
                                setAlertAddCardInBlue2();
                              //  ToastHandler.newInstance(getActivity()).mustShowToast("Works Under progress");

                                // AlertAddCrediCardActivty.start(getActivity());
                            }
                        });
                    }

                }

                public void onFinish() {
                    Log.d("session", "BCF CountDownTimer onFinish  " );
                    postEventOnMainThread(new CallTimerEvent(true));
                }

            };
        }catch (Exception e)
        {
            Log.d("session", "BCF setAvaiableTimer exception = " + e.getMessage());
        }
    }
    private void stopTimer() {
        if (timerChronometer != null) {
            timerChronometer.stop();
            isStarted = false;
        }
    }

    private void hideOutgoingScreen() {
        outgoingOpponentsRelativeLayout.setVisibility(View.GONE);
    }

    @Override
    public void onCallStarted() {
        Log.d(TAG, "onCallStarted " );
        CallActivity.isOpponenetConnected=true;
       // ((PacifyerActivity)getActivity()) .connectedSessionWeb(PacifyerActivity.sessionid);
        if(isCustomer())
        customerconnectedSessionWeb(PacifyerActivity.sessionid);
        onCallStatus("Connected");
        hideOutgoingScreen();
        startTimer();
        getPrefManager().saveSessionId(PacifyerActivity.sessionid);
        actionButtonsEnabled(true);
    }

    public abstract void onCallStatus(String status);
    @Override
    public void onCallStopped() {
        onCallStatus("Call Stopped");
        Log.d(TAG, "onCallStopped " );
        if (currentSession == null) {
            Log.d(TAG, "currentSession = null onCallStopped");
            return;
        }
        stopTimer();
        actionButtonsEnabled(false);
    }

    @Override
    public void onOpponentsListUpdated(ArrayList<QBUser> newUsers) {
        initOpponentsList();
    }

    private void initOpponentsList() {
        Log.v("UPDATE_USERS", "super initOpponentsList()");
        ArrayList<QBUser> usersFromDb = dbManager.getUsersByIds(currentSession.getOpponents());
        opponents = UsersUtils.getListAllUsersFromIds(usersFromDb, currentSession.getOpponents());

        QBUser caller = dbManager.getUserById(currentSession.getCallerID());
        if (caller == null) {
            caller = new QBUser(currentSession.getCallerID());
            caller.setFullName(String.valueOf(currentSession.getCallerID()));
        }

        if (isIncomingCall) {
            opponents.add(caller);
            opponents.remove(QBChatService.getInstance().getUser());
        }
    }

}