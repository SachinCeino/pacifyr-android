package com.pacifyr.omhealthcare.quickblox.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ceino.utilitylibrary.model.pacifyr.MUser;
import com.ceino.utilitylibrary.utils.PrefManager;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.base.PacifyerActivity;
import com.pacifyr.omhealthcare.event.CallTimerEvent;
import com.pacifyr.omhealthcare.interfaces.EmpMinutesListener;
import com.pacifyr.omhealthcare.network.NetworkService;
import com.pacifyr.omhealthcare.quickblox.activities.CallActivity;
import com.pacifyr.omhealthcare.quickblox.db.QbUsersDbManager;
import com.pacifyr.omhealthcare.quickblox.utils.Consts;
import com.pacifyr.omhealthcare.quickblox.utils.UsersUtils;
import com.pacifyr.omhealthcare.quickblox.utils.WebRtcSessionManager;
import com.pacifyr.omhealthcare.utils.Constants;
import com.quickblox.chat.QBChatService;
import com.quickblox.users.model.QBUser;
import com.quickblox.videochat.webrtc.QBRTCSession;
import com.quickblox.videochat.webrtc.QBRTCTypes;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.pacifyr.omhealthcare.utils.Constants.getCountryChecks;

/*import com.quickblox.sample.groupchatwebrtc.R;
import com.quickblox.sample.groupchatwebrtc.activities.CallActivity;
import com.quickblox.sample.groupchatwebrtc.db.QbUsersDbManager;
import com.quickblox.sample.groupchatwebrtc.utils.CollectionsUtils;
import com.quickblox.sample.groupchatwebrtc.utils.Consts;
import com.quickblox.sample.groupchatwebrtc.utils.UsersUtils;
import com.quickblox.sample.groupchatwebrtc.utils.WebRtcSessionManager;*/
/*import com.quickblox.videochat.webrtc.BaseSession;*/

public abstract class BaseConversationFragment extends BaseToolBarFragment implements CallActivity.CurrentCallStateCallback {

    private static final String TAG = BaseConversationFragment.class.getSimpleName();
    protected QbUsersDbManager dbManager;
    protected WebRtcSessionManager sessionManager;
    public boolean isIncomingCall;
    protected QBRTCSession currentSession;
    protected ArrayList<QBUser> opponents;
    private QBRTCTypes.QBConferenceType qbConferenceType;
    String addNewNotes = "";
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
    public ArrayList<MUser> call_particpants_list = new ArrayList<MUser>();
    private static final String FREE_MINUTES_10 = "freeMinutes10";
    private static final String FREE_MINUTES_5 = "freeMinutes5";
    private static final String FREE_MINUTES_OVER = "freeMinutesOver";
    private static final String PAYTM_PAYMENT_DONE = "cardSetup";
    boolean freeMinuteAlert10, freeMinuteAlert5, freeMinuteAlert0, freeMinuteAlertMore10 = false; // track which of the 3 alerts are shown. 10, 5 or 0;
    private CountDownTimer countDownTimer;
    private EmpMinutesListener empMinutesListener;

    public EmpMinutesListener getEmpMinutesListener() {
        return empMinutesListener;
    }

    public void setEmpMinutesListener(EmpMinutesListener empMinutesListener) {
        this.empMinutesListener = empMinutesListener;
    }


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
        try {
            allOpponentsTextView.setVisibility(View.VISIBLE);

            if (isValid(call_particpants_list, 2)) {
                defSetText(allOpponentsTextView, call_particpants_list.get(1).getName());
            } else {
                defSetText(allOpponentsTextView, CallActivity.mPacifyr.getName());
            }

        } catch (Exception e) {
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
        micToggleVideoCall = view.findViewById(R.id.toggle_mic);
        handUpVideoCall = view.findViewById(R.id.button_hangup_call);
        outgoingOpponentsRelativeLayout = view.findViewById(R.id.layout_background_outgoing_screen);
        allOpponentsTextView = view.findViewById(R.id.text_outgoing_opponents_names);
        ringingTextView = view.findViewById(R.id.text_ringing);

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

    public boolean isEmployee() {
        String role = PrefManager.getInstance(getActivity()).getCustomerRole();
        if (role.equals("") || !role.equals("employee")) {
            return false;
        } else return role.equals("employee");
    }

    public void checkEmpAvailableMinutes() {
        boolean cardExist = isCardExist();
        if (!cardExist && isEmployee()) {
            if (NetworkService.mCurrentUser == null) {
                return;
            }
            long freeMinutes = NetworkService.mCurrentUser.getAvailableMinutes();
            if (freeMinutes > 0) {
                long freeMillis = freeMinutes * 60 * 1000;
                final long ONE_MINUTE = 60 * 1000;
                countDownTimer = new CountDownTimer(freeMillis, ONE_MINUTE) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        long minutesLeft = millisUntilFinished / (60 * 1000);

                        if (empMinutesListener != null) {
                            empMinutesListener.tick(minutesLeft);
                        }
                        if (minutesLeft <= 0 && !freeMinuteAlert0) {
                            if (empMinutesListener != null) {
                                empMinutesListener.expired();
                            }
                            if (getCallPacifyr() != null) {
                                new NetworkService().sendCardAddedNotification(getCallPacifyr().getId(), FREE_MINUTES_OVER, new AjaxCallback<JSONObject>() {
                                    @Override
                                    public void callback(String url, JSONObject json, AjaxStatus status) {
                                        if (json != null) {
                                            freeMinuteAlert0 = true;
                                        }
                                    }
                                });
                            }
                        } else if (minutesLeft <= 5 && !freeMinuteAlert5) {
                            if (empMinutesListener != null) {
                                empMinutesListener.onMinutesLeft(minutesLeft);
                            }
                            if (getCallPacifyr() != null) {
                                new NetworkService().sendCardAddedNotification(getCallPacifyr().getId(), FREE_MINUTES_5, new AjaxCallback<JSONObject>() {
                                    @Override
                                    public void callback(String url, JSONObject json, AjaxStatus status) {
                                        if (json != null) {
                                            freeMinuteAlert5 = true;
                                        }
                                    }
                                });
                            }
                        } else if (minutesLeft <= 10 && !freeMinuteAlert10) {
                            if (empMinutesListener != null) {
                                empMinutesListener.onMinutesLeft(minutesLeft);
                            }
                            if (getCallPacifyr() != null) {
                                new NetworkService().sendCardAddedNotification(getCallPacifyr().getId(), FREE_MINUTES_10, new AjaxCallback<JSONObject>() {
                                    @Override
                                    public void callback(String url, JSONObject json, AjaxStatus status) {
                                        if (json != null) {
                                            freeMinuteAlert10 = true;
                                        }

                                    }
                                });
                            }
                        } else {
                            Log.d(TAG, minutesLeft + " minutes left");
                        }
                    }

                    @Override
                    public void onFinish() {
                        if (empMinutesListener != null) {
                            empMinutesListener.onMinutesLeft(0);
                        }
                    }
                };
                countDownTimer.start();
            }
        }
    }

    public void startTimer() {
        if (!isStarted) {
            timerChronometer.setVisibility(View.VISIBLE);
            timerChronometer.setBase(SystemClock.elapsedRealtime());
            timerChronometer.start();
            if (isCustomer())
                if (!isCardExist()) {
                    if (isValid(NetworkService.mCurrentUser)) {
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

    boolean isFirsttime = false;

    public void setAvaiableTimer(long availableseconds) {
        try {
            isFirsttime = false;
            if (availableseconds < 30) {
                if (isIncomingCall) {

                    if (!getCountryChecks().contains(getPrefManager().getCountry())) {
                        setIncommingPacifyrCallAlertCardStatusInBlue("Add Card", "Your free minutes are expired. Please continue enjoying our services by updating your credit card information", "Ok", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                hideProgress();
                                hideStatus2();
                                handUpVideoCall.performClick();
                            }
                        });
                    }

                } else {
                    if (!getCountryChecks().contains(getPrefManager().getCountry()))
                        postEventOnMainThread(new CallTimerEvent(true));
                    return;
                }
            }
            availableseconds = availableseconds * 1000;
            Log.d("session", "BCF setAvaiableTimer availableseconds = " + availableseconds);
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
            //Log.d("session", "BCF setAvaiableTimer availableseconds = " + availableseconds);
            timer = new CountDownTimer(availableseconds, 10000) {

                public void onTick(long millisUntilFinished) {
                    Log.d("session", "BCF CountDownTimer millisUntilFinished = " + millisUntilFinished);
                    if ((300 >= (millisUntilFinished / 10000) && !isFirsttime)) {
                        isFirsttime = true;
                        setAlertStatusInBlue2("Add Card", "Your free minutes are expired. Please continue enjoying our services by updating your credit card information", "Ok", "NO", new View.OnClickListener() {
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
                    Log.d("session", "BCF CountDownTimer onFinish  ");
                    postEventOnMainThread(new CallTimerEvent(true));
                }

            };
        } catch (Exception e) {
            Log.d("session", "BCF setAvaiableTimer exception = " + e.getMessage());
        }
    }

    public void stopTimer() {
        if (timerChronometer != null) {
            timerChronometer.stop();
            isStarted = false;
        }
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private void hideOutgoingScreen() {
        outgoingOpponentsRelativeLayout.setVisibility(View.GONE);
    }

    @Override
    public void onCallStarted() {
        Log.d(TAG, "onCallStarted ");
        CallActivity.isOpponenetConnected = true;
        // ((PacifyerActivity)getActivity()) .connectedSessionWeb(PacifyerActivity.sessionid);
        if (isCustomer())
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
        Log.d(TAG, "onCallStopped ");
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


    public void showaddNotePopupDialogue() {


        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = getActivity().findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.add_note_dialog_popup, viewGroup, false);


        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        EditText et_notes = dialogView.findViewById(R.id.txtv_note);
        TextView submit_note = dialogView.findViewById(R.id.submit_note);
        et_notes.setText(addNewNotes);
        submit_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewNotes = et_notes.getText() + "";
                if (isValid(addNewNotes))
                    SubmitNoteToServer(addNewNotes);
                else
                    et_notes.setError("No Notes Available");
                return;
            }
        });

        et_notes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                addNewNotes = et_notes.getText() + "";
            }
        });

        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void SubmitNoteToServer(String notes) {


        try {
            PrefManager prefM = PrefManager.getInstance(getActivity());
            String userToken = prefM.getAccessToken();
            String userid = prefM.getUserID();
            String url = NetworkService.GLOBAL_URL + "notes/add";
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    try {
                        Toast.makeText(getActivity(), "Notes Updated", Toast.LENGTH_SHORT).show();
                        /*  String my=json.toString();
                        String message=json.toString();

                      if (json.has("status"))
                        {
                            String statusMessage= "";
                            try {
                                statusMessage = json
                                        .getString("status");
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }

                            if (isValid(statusMessage) && statusMessage.equals("success"))
                            {
                                if (json.has("message"))
                                {String mesg="";
                                    try {
                                        mesg=json.getString("message");
                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                    }
                                    hideStatus2();
                                    setStatus(mesg, R.color.md_green_400);
                                    showStatus();
                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            hideStatus();
                                        }
                                    }, 2000);
                                }

                            }
                            else
                            {
                                if (json.has("message"))
                                {String mesg="";
                                    try {
                                        mesg=json.getString("message");
                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                    }
                                    hideStatus2();
                                    setStatus(mesg, R.color.md_green_400);
                                    showStatus();
                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            hideStatus();
                                            getActivity().onBackPressed();
                                        }
                                    }, 2000);
                                }

                            }
                        }
*/


                    } catch (Exception e) {
                        errorStatus();
                    }
                }
            };
            AQuery aq = new AQuery(getActivity());
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", userToken);

            JSONObject jsonObject = new JSONObject();
            jsonObject.putOpt("userId", userid);
            jsonObject.putOpt("sessionId", PacifyerActivity.sessionid);
            jsonObject.putOpt("text", notes);
            aq.post(url, jsonObject, JSONObject.class, cb);
        } catch (Exception ex) {
            errorStatus();
        }
    }


}