package com.pacifyr.client.quickblox.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.pacifyr.client.activity.ActivityChangeEmotionFromCall;
import com.pacifyr.client.base.PacifyrActivity;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MEmotion;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MUser;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.pacifyr.client.R;
import com.pacifyr.client.adapter.EmotionSpecialityAdapter;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.event.CallChangeEmotion;
import com.pacifyr.client.event.CallEvent;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.event.PicInPic;
import com.pacifyr.client.home.CustomerDetailActivity;
import com.pacifyr.client.home.ProfileScreenActivity;
import com.pacifyr.client.interfaces.EmpMinutesListener;
import com.pacifyr.client.network.GsonUtils;
import com.pacifyr.client.network.NetworkService;
import com.pacifyr.client.quickblox.activities.CallActivity;
import com.pacifyr.client.quickblox.adapters.OpponentsFromCallAdapter;
import com.pacifyr.client.utils.FirebaseLogging;
import com.pacifyr.client.utils.LogUtils;
import com.quickblox.sample.core.utils.Toaster;
import com.quickblox.users.model.QBUser;
import com.quickblox.videochat.webrtc.BaseSession;
import com.quickblox.videochat.webrtc.QBRTCSession;
import com.quickblox.videochat.webrtc.QBRTCTypes;
import com.quickblox.videochat.webrtc.callbacks.QBRTCClientVideoTracksCallbacks;
import com.quickblox.videochat.webrtc.callbacks.QBRTCSessionEventsCallback;
import com.quickblox.videochat.webrtc.callbacks.QBRTCSessionStateCallback;
import com.quickblox.videochat.webrtc.view.QBRTCSurfaceView;
import com.quickblox.videochat.webrtc.view.QBRTCVideoTrack;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;
import org.webrtc.CameraVideoCapturer;
import org.webrtc.RendererCommon;
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoRenderer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL;

/*import com.quickblox.videochat.webrtc.BaseSession;*/


/**
 * QuickBlox team
 */
public class VideoConversationFragment extends BaseConversationFragment implements Serializable, QBRTCClientVideoTracksCallbacks,
        QBRTCSessionStateCallback, QBRTCSessionEventsCallback, OpponentsFromCallAdapter.OnAdapterEventListener {

    private static final int DEFAULT_ROWS_COUNT = 2;
    private static final int DEFAULT_COLS_COUNT = 3;
    private static final long TOGGLE_CAMERA_DELAY = 1000;
    private static final long LOCAL_TRACk_INITIALIZE_DELAY = 500;
    private static final int RECYCLE_VIEW_PADDING = 2;
    private static final long UPDATING_USERS_DELAY = 2000;
    private static final long FULL_SCREEN_CLICK_DELAY = 1000;
    private static final int REQUEST_CODE_ATTACHMENT = 100;

    private String TAG = "call";

    private ToggleButton cameraToggle;
    private View view;
    private LinearLayout actionVideoButtonsLayout;
    private QBRTCSurfaceView remoteFullScreenVideoView;
    private QBRTCSurfaceView localVideoView;
    private CameraState cameraState = CameraState.DISABLED_FROM_USER;
    private RecyclerView recyclerView;
    private SparseArray<OpponentsFromCallAdapter.ViewHolder> opponentViewHolders;
    private boolean isPeerToPeerCall;
    private QBRTCVideoTrack localVideoTrack;
    private TextView connectionStatusLocal;
    private DeviceFitImageView emotion_dimv;

    private Map<Integer, QBRTCVideoTrack> videoTrackMap;
    private OpponentsFromCallAdapter opponentsAdapter;
    private LocalViewOnClickListener localViewOnClickListener;
    private boolean isRemoteShown;

    private int amountOpponents;
    private int userIDFullScreen;
    private List<QBUser> allOpponents;
    private boolean connectionEstablished;
    private boolean allCallbacksInit;
    private boolean isCurrentCameraFront;
    private boolean isLocalVideoFullScreen;
    private MainTextView reason_ctxv,emotion_ctxv;
    private RelativeLayout emotion_rlay;
    private LinearLayout info_overlay_llay;

    // private  MainTextView participants_ctxv;


    private RelativeLayout userRlay;
    private MainTextView callCtxv;
    private CircleImageView userDimv;
    private MainTextView usernameCtxv;
    private DeviceFitImageView moreDimv;
    private RelativeLayout more_rlay;
    private TextView billingStatus;
    private TextView text_remaining_minutes;
    private String change_Emotion_name;
    private ImageButton img_button_add_note;
    private BroadcastReceiver eventReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String type = intent.getStringExtra("type");
            Log.d("receiver", "Got message: " + type);
            if (type != null && type.equals("billing")) {
                billingStatus.setVisibility(View.VISIBLE);
                ((PacifyrActivity) getActivity()).setBillingStarted(true);
                if (isEmployee()) {
                    setEmpMinutesListener(empMinutesListener);
                    checkEmpAvailableMinutes();
                }
                try {
                    stopTimer();
                    startTimer();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };
    private EmpMinutesListener empMinutesListener = new EmpMinutesListener() {
        @Override
        public void onMinutesLeft(long minutes) {
           /* setAlertStatusInBlue2("Warning", "Your monthly free sessions is expiring soon. Please enter your Credit Card to continue service.",
                    "Add Card", "Cancel", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setAlertAddCardInBlue2();
                        }
                    });*/
            if (getActivity() != null && getActivity() instanceof PacifyrActivity) {
                ((PacifyrActivity) getActivity()).showIOSDialogOKCancel("Warning", "Your monthly free sessions is expiring soon. Please enter your Credit Card to continue service.", "Add Card", new iOSDialogClickListener() {
                    @Override
                    public void onClick(iOSDialog dialog) {
                        //Toast.makeText(PacifyrActivity.this,"Clicked!",Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                        setAlertAddCardInBlue2();

                    }
                }, "Cancel");
            }
        }

        @Override
        public void expired() {
            try {


                    Toast.makeText(getActivity(), "Your monthly free sessions has expired. Please enter your Credit Card to continue.", Toast.LENGTH_LONG).show();
                    if (conversationFragmentCallbackListener != null)
                        conversationFragmentCallbackListener.onHangUpCurrentSession();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void tick(long minutes) {
            text_remaining_minutes.setText(minutes + " free minutes remaining");
            text_remaining_minutes.setVisibility(View.VISIBLE);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      //  view = super.onCreateView(inflater, container, savedInstanceState);
       // return view;

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void configureOutgoingScreen() {
        outgoingOpponentsRelativeLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.grey_transparent_50));
        allOpponentsTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        ringingTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
    }

    @Override
    protected void configureActionBar() {
        actionBar = ((AppCompatActivity) getActivity()).getDelegate().getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
	    actionBar.hide();
    }

    @Override
    protected void configureToolbar() {
        toolbar.setVisibility(View.GONE);
        toolbar.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.black_transparent_50));
        toolbar.setTitleTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        toolbar.setSubtitleTextColor(ContextCompat.getColor(getActivity(), R.color.white));
    }

    @Override
    int getFragmentLayout() {
        return R.layout.fragment_video_conversation;
    }

    @Override
    protected void initFields() {
        super.initFields();
        localViewOnClickListener = new LocalViewOnClickListener();
        amountOpponents = opponents.size();
        allOpponents = Collections.synchronizedList(new ArrayList<QBUser>(opponents.size()));
        allOpponents.addAll(opponents);

    //    timerChronometer = (Chronometer) getActivity().findViewById(R.id.timer_chronometer_action_bar);

        isPeerToPeerCall = opponents.size() == 1;
    }

    public void setDuringCallActionBar() {
        try{
	        actionBar.hide();
        actionBar.setDisplayShowTitleEnabled(true);
      //  actionBar.setTitle(currentUser.getFullName());
        actionBar.setTitle(getUserName());
      //  actionBar.setSubtitle(getString(R.string.opponent, opponents.get(0).getFullName()));

        if(isValid(call_particpants_list))
        {
            actionBar.setTitle(call_particpants_list.get(0).getName());
            actionBar.setSubtitle(getString(R.string.opponent, call_particpants_list.get(1).getName()));
        }
      //commented jan 16 /2018
        /*  if (isPeerToPeerCall) {
            actionBar.setSubtitle(getString(R.string.opponent, opponents.get(0).getFullName()));
        } else {
            actionBar.setSubtitle(getString(R.string.opponents));
           // actionBar.setSubtitle(getString(R.string.opponents, amountOpponents));
        }*/

        actionButtonsEnabled(true);
        }catch (Exception e)
        {
              e.printStackTrace();
        }
    }

    private void setUserInfoViews() {
        try{
	        actionBar.hide();
           // actionBar.setDisplayShowTitleEnabled(true);
          //  actionBar.setTitle(getUserName());
            more_rlay.setVisibility(View.GONE);
            userRlay.setVisibility(View.GONE);
            if(isValid(call_particpants_list)) {
                if(isPacifyer()) {

                    userRlay.setVisibility(View.VISIBLE);
                    defSetText(usernameCtxv, call_particpants_list.get(0).getName());
                    String image = call_particpants_list.get(0).getImageUrl();
                    userDimv.setVisibility(View.GONE);
                    if(isValid(image)) {
                        userDimv.setVisibility(View.VISIBLE);
                        Picasso.with(getActivity()).load(image).placeholder(R.drawable.placeholder_image).into(userDimv);
                    }

                    if(isValid(call_particpants_list.get(0)))
                    {

                        more_rlay.setVisibility(View.VISIBLE);
                        more_rlay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                CustomerDetailActivity.start(getActivity(),call_particpants_list.get(0));
                            }
                        });

                    }
                }
                else if(isCustomer())
                {
                userRlay.setVisibility(View.VISIBLE);
                defSetText(usernameCtxv, call_particpants_list.get(1).getName());
                String image = call_particpants_list.get(1).getImageUrl();
                userDimv.setVisibility(View.GONE);
                if(isValid(image)) {
                    userDimv.setVisibility(View.VISIBLE);
                    Picasso.with(getActivity()).load(image).placeholder(R.drawable.placeholder_image).into(userDimv);
                }
               }
            }
            else {
                if(isPacifyer()) {
                    final MUser muser;
                    if (isIncomingCall)
                        muser = getCallPacifyr();
                    else
                        muser = getCallUser();
                   if(isValid(muser)) {
                       userRlay.setVisibility(View.VISIBLE);
                       defSetText(usernameCtxv, muser.getName());
                       String image = muser.getImageUrl();
                       userDimv.setVisibility(View.GONE);
                       if(isValid(image)) {
                           userDimv.setVisibility(View.VISIBLE);
                           Picasso.with(getActivity()).load(image).placeholder(R.drawable.placeholder_image).into(userDimv);
                       }




                       more_rlay.setVisibility(View.VISIBLE);
                       more_rlay.setOnClickListener(new View.OnClickListener() {
                               @Override
                               public void onClick(View view) {

                                   CustomerDetailActivity.start(getActivity(),muser);
                               }
                           });

                   }
                }
                else  if(isCustomer()) {
                    final MUser muser;
                    if (isIncomingCall)
                        muser = getCallPacifyr();
                    else
                        muser = getCallUser();
                    if(isValid(muser)) {
                        userRlay.setVisibility(View.VISIBLE);
                        defSetText(usernameCtxv, muser.getName());
                        String image = muser.getImageUrl();
                        userDimv.setVisibility(View.GONE);
                        if(isValid(image)) {
                            userDimv.setVisibility(View.VISIBLE);
                            Picasso.with(getActivity()).load(image).placeholder(R.drawable.placeholder_image).into(userDimv);
                        }
                    }
                }




            }


        }catch (Exception e)
        {
            LogUtils.LOGD("exception","VCF setUserInfoViews exce " + e.getMessage());
        }
    }
    public void setParticipantBar() {
        try{
	        actionBar.hide();
            setUserInfoViews();
        /*    actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(getUserName());

            if(isValid(call_particpants_list))
            {
                actionBar.setTitle(call_particpants_list.get(0).getName());
                actionBar.setSubtitle(getString(R.string.opponent, call_particpants_list.get(1).getName()));
            }*/

        }catch (Exception e)
        {
            LogUtils.LOGD("exception","VCF setParticipantBar exce " + e.getMessage());
        }
    }
    private void initVideoTrackSListener() {
        if (currentSession != null) {
            currentSession.addVideoTrackCallbacksListener(this);
        }
    }

    private void removeVideoTrackSListener() {
        if (currentSession != null) {
            currentSession.removeVideoTrackCallbacksListener(this);
        }
    }

    @Override
    protected void actionButtonsEnabled(boolean inability) {
        super.actionButtonsEnabled(inability);
        cameraToggle.setEnabled(inability);
        // inactivate toggle buttons
        cameraToggle.setActivated(inability);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
        if (!allCallbacksInit) {
            conversationFragmentCallbackListener.addTCClientConnectionCallback(this);
            conversationFragmentCallbackListener.addRTCSessionEventsCallback(this);
            initVideoTrackSListener();
            allCallbacksInit = true;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        setHasOptionsMenu(true);

        Bundle args =
                FirebaseLogging.getConstantArgsLoggedIn(getActivity());
        if (getCallPacifyr() != null) {
            args.putString(FirebaseLogging.EC, getCallPacifyr().getId());
        }
        FirebaseLogging.sendLog(getActivity(),
                FirebaseLogging.EVENT_VIDEOCALL_STARTED,
                args);
    }


    //public ArrayList<MUser>call_user_list=new ArrayList<MUser>();


    public void onUserInfoViews(View view) {
      try {

          info_overlay_llay = view.findViewById(R.id.info_overlay_llay);
        //  localVideoView = (QBRTCSurfaceView) view.findViewById(R.id.local_video_view);
          userRlay = view.findViewById(R.id.user_rlay);
          callCtxv = view.findViewById(R.id.call_ctxv);
          userDimv = view.findViewById(R.id.user_dimv);
          usernameCtxv = view.findViewById(R.id.username_ctxv);
          moreDimv = view.findViewById(R.id.more_dimv);
          more_rlay = view.findViewById(R.id.more_rlay);
 moreDimv.setVisibility(View.VISIBLE);
          more_rlay.setVisibility(View.GONE);
      }catch (Exception e)
      {
          e.printStackTrace();
      }
    }

    @Override
    protected void initViews(View view) {
        super.initViews(view);
        Log.i(TAG, "initViews");
         opponentViewHolders = new SparseArray<>(opponents.size());
         isRemoteShown = false;
         isCurrentCameraFront = true;
        timerChronometer = view.findViewById(R.id.chronometer_timer_video_call);
        text_remaining_minutes = view.findViewById(R.id.text_remaining_minutes);
        emotion_dimv = view.findViewById(R.id.emotion_dimv);
        emotion_rlay = view.findViewById(R.id.emotion_rlay);
        reason_ctxv = view.findViewById(R.id.reason_ctxv);
        emotion_ctxv = view.findViewById(R.id.emotion_ctxv);
        billingStatus = view.findViewById(R.id.billing_status);
        emotion_rlay = view.findViewById(R.id.emotion_rlay);

        text_remaining_minutes.setVisibility(View.GONE);
        onUserInfoViews(view);
 //  participants_ctxv = (MainTextView) view.findViewById(R.id.participants_ctxv);
        emotion_dimv.setVisibility(View.GONE);
        //emotion_rlay.setVisibility(View.GONE);
     //   participants_ctxv.setVisibility(View.GONE);
        reason_ctxv.setVisibility(View.GONE);
        emotion_ctxv.setVisibility(View.GONE);
        setEmotionViews();
        if (isIncomingCall)
            getCurrentUserInformation();

        localVideoView = view.findViewById(R.id.local_video_view);
        initCorrectSizeForLocalView();
        localVideoView.setZOrderMediaOverlay(true);

        remoteFullScreenVideoView = view.findViewById(R.id.remote_video_view);
        remoteFullScreenVideoView.setOnClickListener(null);

        if (!isPeerToPeerCall) {
            recyclerView = view.findViewById(R.id.grid_opponents);

            recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), R.dimen.grid_item_divider));
            recyclerView.setHasFixedSize(true);
            final int columnsCount = defineColumnsCount();
            LinearLayoutManager layoutManager
                    = new LinearLayoutManager(getActivity(), HORIZONTAL, false);
            recyclerView.setLayoutManager(layoutManager);

//          for correct removing item in adapter
            recyclerView.setItemAnimator(null);
            recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    setGrid(columnsCount);
                    recyclerView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            });
        }
        connectionStatusLocal = view.findViewById(R.id.connectionStatusLocal);

        cameraToggle = view.findViewById(R.id.toggle_camera);
        cameraToggle.setVisibility(View.VISIBLE);

        actionVideoButtonsLayout = view.findViewById(R.id.element_set_video_buttons);

        actionButtonsEnabled(false);
        restoreSession();

        emotion_rlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityChangeEmotionFromCall.class);
                if (isValid(change_Emotion_name))
                {
                    intent.putExtra("emotion",change_Emotion_name);
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        userDimv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid(getOpponentId())) {
                    ProfileScreenActivity.start(getActivity(), getOpponentId(), false);
                }
            }
        });

       // ArrayList<MUser>call_user_list=getCallParticipantlist();
    }


    private void getCurrentUserInformation() {
        if (isValid(getCallUser()) && isValid(getCallUser().getId())) {
            new NetworkService().incomingVideoCallUserEmotion(getCallUser().getId(), new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    MEmotion emotion = null;
                    String image = null;

                    if (isValid(json)) {

                        NetworkService.mCurrentUser = GsonUtils.getInstance().gsonToModelUserProfile(json);
                        if (isValid(NetworkService.mCurrentUser)) {
                            setEmotionViews();
                        }
                    }
                }
            });
        }

    }

    private String getOpponentId() {
        String mUserId = PrefManager.getInstance(getActivity()).getUserID();
        if (isValid(call_particpants_list)) {
            for (int i = 0; i < call_particpants_list.size(); i++) {
                if (!call_particpants_list.get(i).getId().equals(mUserId)) {
                    return call_particpants_list.get(i).getId();
                }
            }
        }
        return null;
    }


    private void setResaonViews() {
        try{

            reason_ctxv.setVisibility(View.VISIBLE);
            if(isCustomer())
                defSetText(reason_ctxv, EmotionSpecialityAdapter.reason_forcall);
            else {
                defSetText(reason_ctxv, CallActivity.mSessionDetailRoot.getReasonForTheCall());
            }
        }catch (Exception e)
        {
            reason_ctxv.setVisibility(View.GONE);
        }
    }
   /* public void setPartipantViews() {
        try{
         *//*   participants_ctxv.setVisibility(View.VISIBLE);
            defSetText(participants_ctxv,getUserName());
            if(isValid(call_particpants_list)) {
                defSetText(participants_ctxv, call_particpants_list.get(0).getName()+" and "+call_particpants_list.get(1).getName());
            }*//*

        }catch (Exception e)
        {
           // participants_ctxv.setVisibility(View.GONE);
        }
    }*/

    private void setEmotionViews() {
        try{
            setResaonViews();
            String image=null;
            MEmotion emotion=null;
            if(isCustomer()) {
                emotion= PacifyrActivity.call_emotion;
                image= PacifyrActivity.call_emotion.getImage();
                call_particpants_list=new ArrayList<MUser>();
                call_particpants_list.add( convertModel(NetworkService.mCurrentUser));
                call_particpants_list.add(CallActivity.mPacifyr);

            }
            else {
                call_particpants_list=getCallParticipantlist();
                final MUser mUser;
                if (isIncomingCall)
                    mUser = getCallPacifyr();
                else
                    mUser = getCallUser();

                emotion_dimv.setVisibility(View.GONE);
               // reason_ctxv.setVisibility(View.GONE);
                if(!isValid(mUser)) return;
                if(isValid(mUser.getEmotion())) {
                    emotion=mUser.getEmotion();
                    image = mUser.getEmotion().getImage();
                    if(!isValid(image)) {
                        if(isValid(mUser.getEmotionId())) {
                            MEmotion mEmotion = getEmotion(mUser.getEmotionId());
                            emotion=mEmotion;
                            image = mEmotion.getImage();

                        }
                    }
                } else if(isValid(mUser.getEmotionId())) {
                    MEmotion mEmotion = getEmotion(mUser.getEmotionId());
                    emotion=mEmotion;
                    image = mEmotion.getImage();

                }
            }
            setPartipantViews();
         //   setParticipantBar();
            setUserInfoViews();
           if(isValid(image)) {
                 emotion_dimv.setVisibility(View.VISIBLE);
                 Picasso.with(getActivity()).load(NetworkService.GLOBAL_IMAGE_URL + image).placeholder(R.drawable.placeholder_image).into(emotion_dimv);
             }
            if(isValid(emotion)) {
                change_Emotion_name=emotion.getId();
                emotion_ctxv.setVisibility(View.VISIBLE);
                 defSetText(emotion_ctxv,emotion.getTitle());
            }


        }catch (Exception e)
        {
          e.printStackTrace();
        }
    }



    private void restoreSession() {
        Log.d(TAG, "restoreSession ");
        /*jppppppp*/
        if (currentSession.getState() != QBRTCSession.QBRTCSessionState.QB_RTC_SESSION_CONNECTED) {
            return;
        }
       /* if (currentSession.getState() != QBRTCSession.QBRTCSessionState.QB_RTC_SESSION_ACTIVE) {
            return;
        }*/
        onCallStarted();
        Map<Integer, QBRTCVideoTrack> videoTrackMap = getVideoTrackMap();
        if (!videoTrackMap.isEmpty()) {
            for (final Iterator<Map.Entry<Integer, QBRTCVideoTrack>> entryIterator
                 = videoTrackMap.entrySet().iterator(); entryIterator.hasNext();){
                final Map.Entry<Integer, QBRTCVideoTrack> entry = entryIterator.next();
                Log.d(TAG, "check ability to restoreSession for user:"+entry.getKey());
                //if connection with peer wasn't closed do restore it otherwise remove from collection
                if (currentSession.getPeerChannel(entry.getKey()).getState()!=
                        QBRTCTypes.QBRTCConnectionState.QB_RTC_CONNECTION_CLOSED){
                    Log.d(TAG, "execute restoreSession for user:"+entry.getKey());
                    mainHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                            onConnectedToUser(currentSession, entry.getKey());
                            onRemoteVideoTrackReceive(currentSession, entry.getValue(), entry.getKey());
                        }
                        }, LOCAL_TRACk_INITIALIZE_DELAY);
                } else {
                    entryIterator.remove();
                }
            }
        }
    }

    private void initCorrectSizeForLocalView() {
        ViewGroup.LayoutParams params = localVideoView.getLayoutParams();
        DisplayMetrics displaymetrics = getResources().getDisplayMetrics();

        int screenWidthPx = displaymetrics.widthPixels;
        Log.d(TAG, "screenWidthPx " + screenWidthPx);
        params.width = (int) (screenWidthPx * 0.3);
        params.height = (params.width / 2) * 3;
        localVideoView.setLayoutParams(params);
    }

    private void setGrid(int columnsCount) {
        int gridWidth = view.getMeasuredWidth();
        Log.i(TAG, "onGlobalLayout : gridWidth=" + gridWidth + " columnsCount= " + columnsCount);
        float itemMargin = getResources().getDimension(R.dimen.grid_item_divider);
        int cellSizeWidth = defineSize(gridWidth, columnsCount, itemMargin);
        Log.i(TAG, "onGlobalLayout : cellSize=" + cellSizeWidth);
/*        opponentsAdapter = new OpponentsFromCallAdapter(getActivity(), currentSession, opponents, cellSizeWidth,
                (int) getResources().getDimension(R.dimen.item_height));*/
         if(call_particpants_list!=null) {
             opponentsAdapter = new OpponentsFromCallAdapter(getActivity(), currentSession, call_particpants_list, cellSizeWidth, (int) getResources().getDimension(R.dimen.item_height));
             opponentsAdapter.setAdapterListener(VideoConversationFragment.this);
             recyclerView.setAdapter(opponentsAdapter);
         }
    }

    private Map<Integer, QBRTCVideoTrack> getVideoTrackMap() {
        if (videoTrackMap == null) {
            videoTrackMap = new HashMap<>();
        }
        return videoTrackMap;
    }

    private int defineSize(int measuredWidth, int columnsCount, float padding) {
        return measuredWidth / columnsCount - (int) (padding * 2) - RECYCLE_VIEW_PADDING;
    }

    private int defineColumnsCount() {
        return opponents.size() - 1;
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        // If user changed camera state few times and last state was CameraState.ENABLED_FROM_USER
        // than we turn on cam, else we nothing change
        if (cameraState != CameraState.DISABLED_FROM_USER) {
            toggleCamera(true);
        }
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(eventReceiver, new IntentFilter(CallEvent.CALL_EVENT));
    }


    @Override
    public void onPause() {
       /* // If camera state is CameraState.ENABLED_FROM_USER or CameraState.NONE
        // than we turn off cam
        if (cameraState != CameraState.DISABLED_FROM_USER) {
            toggleCamera(false);
        }*/
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(eventReceiver);
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
        if (connectionEstablished) {
            allCallbacksInit = false;
        } else {
            Log.d(TAG, "We are in dialing process yet!");
        }
    }

    private void removeVideoTrackRenderers() {
        Log.d(TAG, "removeVideoTrackRenderers");
        Log.d(TAG, "remove opponents video Tracks");
        Map<Integer, QBRTCVideoTrack> videoTrackMap = getVideoTrackMap();
        for (QBRTCVideoTrack videoTrack : videoTrackMap.values()) {
            if (videoTrack.getRenderer() != null) {
                Log.d(TAG, "remove opponent video Tracks");
                videoTrack.removeRenderer(videoTrack.getRenderer());
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView");
        releaseViewHolders();
        removeConnectionStateListeners();
        removeVideoTrackSListener();
        removeVideoTrackRenderers();
        releaseViews();
    }

    private void releaseViewHolders() {
        opponentViewHolders.clear();
    }

    private void removeConnectionStateListeners(){
        conversationFragmentCallbackListener.removeRTCClientConnectionCallback(this);
        conversationFragmentCallbackListener.removeRTCSessionEventsCallback(this);
    }

    private void releaseViews() {
        if (localVideoView != null){
            localVideoView.release();
        }
        if (remoteFullScreenVideoView != null) {
            remoteFullScreenVideoView.release();
        }
        remoteFullScreenVideoView = null;
        if (!isPeerToPeerCall){
           releseOpponentsViews();
        }
    }

    @Override
    public void onCallStopped() {
        super.onCallStopped();
        Log.i(TAG, "onCallStopped");
    }

    protected void initButtonsListener() {
        super.initButtonsListener();

        cameraToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (cameraState != CameraState.DISABLED_FROM_USER) {
                    switchCamera();
                }
            }
        });
       /* cameraToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cameraState != CameraState.DISABLED_FROM_USER) {
                    toggleCamera(isChecked);
                }
            }
        });*/
    }


    private void switchCamera() {
        if (cameraState == CameraState.DISABLED_FROM_USER) {
            return;
        }
       // cameraToggle.setEnabled(false);
        conversationFragmentCallbackListener.onSwitchCamera(new CameraVideoCapturer.CameraSwitchHandler() {
            @Override
            public void onCameraSwitchDone(boolean b) {
                Log.d(TAG, "camera switched, bool = " + b);
                isCurrentCameraFront = b;
             //   updateSwitchCameraIcon(item);
                toggleCameraInternal();
            }

            @Override
            public void onCameraSwitchError(String s) {
                Log.d(TAG, "camera switch error " + s);
                Toaster.shortToast(getString(R.string.camera_swicth_failed) + s);
                cameraToggle.setEnabled(true);
            }
        });
    }
    private void switchCamera(final MenuItem item) {
        if (cameraState == CameraState.DISABLED_FROM_USER) {
            return;
        }
        cameraToggle.setEnabled(false);
        conversationFragmentCallbackListener.onSwitchCamera(new CameraVideoCapturer.CameraSwitchHandler() {
            @Override
            public void onCameraSwitchDone(boolean b) {
                Log.d(TAG, "camera switched, bool = " + b);
                isCurrentCameraFront = b;
                updateSwitchCameraIcon(item);
                toggleCameraInternal();
            }

            @Override
            public void onCameraSwitchError(String s) {
                Log.d(TAG, "camera switch error " + s);
                Toaster.shortToast(getString(R.string.camera_swicth_failed) + s);
                cameraToggle.setEnabled(true);
            }
        });
    }

    private void updateSwitchCameraIcon(final MenuItem item) {
        if (isCurrentCameraFront) {
            Log.d(TAG, "CameraFront now!");
            item.setIcon(R.drawable.ic_camera_front);
        } else {
            Log.d(TAG, "CameraRear now!");
            item.setIcon(R.drawable.ic_camera_rear);
        }
    }

    private void toggleCameraInternal() {
        Log.d(TAG, "Camera was switched!");
        updateVideoView(isLocalVideoFullScreen ? remoteFullScreenVideoView : localVideoView, isCurrentCameraFront);
        toggleCamera(true);
    }

    private void runOnUiThread(Runnable runnable) {
        mainHandler.post(runnable);
    }

    private void toggleCamera(boolean isNeedEnableCam) {
        if (currentSession != null && currentSession.getMediaStreamManager() != null) {
            conversationFragmentCallbackListener.onSetVideoEnabled(isNeedEnableCam);
        }
        if (connectionEstablished && !cameraToggle.isEnabled()) {
            cameraToggle.setEnabled(true);
        }
    }


    //last opponent view is bind
    @Override
    public void OnBindLastViewHolder(final OpponentsFromCallAdapter.ViewHolder holder, final int position) {
        Log.i(TAG, "OnBindLastViewHolder position=" + position);

    }


    @Override
    public void onItemClick(int position) {
        int userId = opponentsAdapter.getItemQbID(position);
        Log.d(TAG, "USer onItemClick= " + userId);
        if (!getVideoTrackMap().containsKey(userId) ||
                currentSession.getPeerChannel(userId).getState().ordinal() == QBRTCTypes.QBRTCConnectionState.QB_RTC_CONNECTION_CLOSED.ordinal()) {
            return;
        }

        replaceUsersInAdapter(position);

        updateViewHolders(position);

        swapUsersFullscreenToPreview(userId);
    }

    private void replaceUsersInAdapter(int position) {
      /*  for (QBUser qbUser : allOpponents) {
            if (qbUser.getId() == userIDFullScreen) {
                opponentsAdapter.replaceUsers(position, qbUser);
                break;
            }
        }*/
        for (MUser qbUser : call_particpants_list) {
            if (qbUser.getQbid() == userIDFullScreen) {
                opponentsAdapter.replaceUsers(position, qbUser);
                break;
            }
        }
    }

    private void updateViewHolders(int position) {
        View childView = recyclerView.getChildAt(position);
        OpponentsFromCallAdapter.ViewHolder childViewHolder = (OpponentsFromCallAdapter.ViewHolder) recyclerView.getChildViewHolder(childView);
        opponentViewHolders.put(position, childViewHolder);
    }

    @SuppressWarnings("ConstantConditions")
    private void swapUsersFullscreenToPreview(int userId) {
//      get opponentVideoTrack - opponent's video track from recyclerView
        QBRTCVideoTrack opponentVideoTrack = getVideoTrackMap().get(userId);

//      get mainVideoTrack - opponent's video track from full screen
        QBRTCVideoTrack mainVideoTrack = getVideoTrackMap().get(userIDFullScreen);

        QBRTCSurfaceView remoteVideoView = findHolder(userId).getOpponentView();

        if (mainVideoTrack != null) {
            fillVideoView(0, remoteVideoView, mainVideoTrack);
            Log.d(TAG, "_remoteVideoView enabled");
        }
        if (opponentVideoTrack != null) {
            fillVideoView(userId, remoteFullScreenVideoView, opponentVideoTrack);
            Log.d(TAG, "fullscreen enabled");
        }
    }


    private void setRemoteViewMultiCall(int userID, QBRTCVideoTrack videoTrack) {
        Log.d(TAG, "setRemoteViewMultiCall fillVideoView");
        final OpponentsFromCallAdapter.ViewHolder itemHolder = getViewHolderForOpponent(userID);
        if (itemHolder == null) {
            Log.d(TAG, "itemHolder == null - true");
            return;
        }
        final QBRTCSurfaceView remoteVideoView = itemHolder.getOpponentView();

        if (remoteVideoView != null) {
            remoteVideoView.setZOrderMediaOverlay(true);
            updateVideoView(remoteVideoView, false);

            Log.d(TAG, "onRemoteVideoTrackReceive fillVideoView");
            if (isRemoteShown) {
                Log.d(TAG, "USer onRemoteVideoTrackReceive = " + userID);
                fillVideoView(userID, remoteVideoView, videoTrack, true);
            } else {
                isRemoteShown = true;
                opponentsAdapter.removeItem(itemHolder.getAdapterPosition());
                setDuringCallActionBar();
                setRecyclerViewVisibleState();
                //setOpponentsVisibility(View.VISIBLE);
                fillVideoView(userID, remoteFullScreenVideoView, videoTrack);
                updateVideoView(remoteFullScreenVideoView, false);
            }
        }
    }

    private void setRecyclerViewVisibleState() {
        ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
        params.height = (int) getResources().getDimension(R.dimen.item_height);
        recyclerView.setLayoutParams(params);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private OpponentsFromCallAdapter.ViewHolder getViewHolderForOpponent(Integer userID) {
        OpponentsFromCallAdapter.ViewHolder holder = opponentViewHolders.get(userID);
        if (holder == null) {
            Log.d(TAG, "holder not found in cache");
            holder = findHolder(userID);
            if (holder != null) {
                opponentViewHolders.append(userID, holder);
            }
        }
        return holder;
    }

    private OpponentsFromCallAdapter.ViewHolder findHolder(Integer userID) {
        Log.d(TAG, "findHolder for "+userID);
        int childCount = recyclerView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = recyclerView.getChildAt(i);
            OpponentsFromCallAdapter.ViewHolder childViewHolder = (OpponentsFromCallAdapter.ViewHolder) recyclerView.getChildViewHolder(childView);
            if (userID.equals(childViewHolder.getUserId())) {
                return childViewHolder;
            }
        }
        return null;
    }

    private void releseOpponentsViews(){
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int childCount = layoutManager.getChildCount();
        Log.d(TAG, " releseOpponentsViews for  "+childCount + " views");
        for (int i = 0; i < childCount; i++) {
            View childView = layoutManager.getChildAt(i);
            Log.d(TAG, " relese View for  " + i +", "+childView);
            OpponentsFromCallAdapter.ViewHolder childViewHolder = (OpponentsFromCallAdapter.ViewHolder) recyclerView.getChildViewHolder(childView);
            childViewHolder.getOpponentView().release();
        }
    }

    /**
     * @param userId set userId if it from fullscreen videoTrack
     */
    private void fillVideoView(int userId, QBRTCSurfaceView videoView, QBRTCVideoTrack videoTrack,
                               boolean remoteRenderer) {
        videoTrack.removeRenderer(videoTrack.getRenderer());
        videoTrack.addRenderer(new VideoRenderer(videoView));
        if (userId != 0) {
            getVideoTrackMap().put(userId, videoTrack);
        }
        if (!remoteRenderer) {
            updateVideoView(videoView, isCurrentCameraFront);
        }
        Log.d(TAG, (remoteRenderer ? "remote" : "local") + " Track is rendering");
    }

    private void fillVideoView(QBRTCSurfaceView videoView, QBRTCVideoTrack videoTrack, boolean remoteRenderer) {
       fillVideoView(0, videoView, videoTrack, remoteRenderer);
    }

    /**
     * @param userId set userId if it from fullscreen videoTrack
     */
    private void fillVideoView(int userId, QBRTCSurfaceView videoView, QBRTCVideoTrack videoTrack) {
        if (userId != 0) {
            userIDFullScreen = userId;
        }
        fillVideoView(userId, videoView, videoTrack, true);
    }

    protected void updateVideoView(SurfaceViewRenderer surfaceViewRenderer, boolean mirror) {
        updateVideoView(surfaceViewRenderer, mirror, RendererCommon.ScalingType.SCALE_ASPECT_FILL);
    }

    protected void updateVideoView(SurfaceViewRenderer surfaceViewRenderer, boolean mirror, RendererCommon.ScalingType scalingType) {
        Log.i(TAG, "updateVideoView mirror:" + mirror + ", scalingType = " + scalingType);
        surfaceViewRenderer.setScalingType(scalingType);
        surfaceViewRenderer.setMirror(mirror);
        surfaceViewRenderer.requestLayout();
    }
    @Override
    public  void onCallStatus(String status)
    {
        try
        {
            //   if(isValid(status))
            defSetText(connectionStatusLocal,status);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void setStatusForOpponent(int userId, final String status) {
        if (isPeerToPeerCall) {
            connectionStatusLocal.setText(status);
            return;
        }

        final OpponentsFromCallAdapter.ViewHolder holder = findHolder(userId);
        if (holder == null) {
            return;
        }

        holder.setStatus(status);
    }

    private void updateNameForOpponent(int userId, String newUserName) {
        if (isPeerToPeerCall) {
           //jp commented jan-16-2018
          //actionBar.setSubtitle(getString(R.string.opponent, newUserName));
        } else {
            OpponentsFromCallAdapter.ViewHolder holder = findHolder(userId);
            if (holder == null) {
                Log.d("UPDATE_USERS", "holder == null");
                return;
            }

            Log.d("UPDATE_USERS", "holder != null");
            holder.setUserName(newUserName);
        }
    }

    private void setProgressBarForOpponentGone(int userId) {
        if (isPeerToPeerCall) {
            return;
        }
        final OpponentsFromCallAdapter.ViewHolder holder = getViewHolderForOpponent(userId);
        if (holder == null) {
            return;
        }

        holder.getProgressBar().setVisibility(View.GONE);

    }

    private void setBackgroundOpponentView(final Integer userId) {
        final OpponentsFromCallAdapter.ViewHolder holder = findHolder(userId);
        if (holder == null) {
            return;
        }

        if (userId != userIDFullScreen) {
            holder.getOpponentView().setBackgroundColor(Color.parseColor("#000000"));
        }
    }

    ///////////////////////////////  QBRTCSessionConnectionCallbacks ///////////////////////////
    //jppppppp
/*    @Override
    public void onConnectedToUser(QBRTCSession qbrtcSession, final Integer userId) {
        connectionEstablished = true;
        setStatusForOpponent(userId, getString(R.string.text_status_connected));
        setProgressBarForOpponentGone(userId);
    }

    @Override
    public void onConnectionClosedForUser(QBRTCSession qbrtcSession, Integer userId) {
        setStatusForOpponent(userId, getString(R.string.text_status_closed));
        if (!isPeerToPeerCall) {
            Log.d(TAG, "onConnectionClosedForUser videoTrackMap.remove(userId)= " + userId);
            getVideoTrackMap().remove(userId);
            setBackgroundOpponentView(userId);
        }
    }

    @Override
    public void onDisconnectedFromUser(QBRTCSession qbrtcSession, Integer integer) {
        setStatusForOpponent(integer, getString(R.string.text_status_disconnected));
    }*/

    //////////////////////////////////   end     //////////////////////////////////////////



    ////////////////////////////  callbacks from QBRTCClientVideoTracksCallbacks ///////////////////
/*    @Override
    public void onLocalVideoTrackReceive(QBRTCSession qbrtcSession, final QBRTCVideoTrack videoTrack) {
        Log.d(TAG, "onLocalVideoTrackReceive() run");
        localVideoTrack = videoTrack;
        isLocalVideoFullScreen = true;
        cameraState = CameraState.NONE;


        if (remoteFullScreenVideoView != null) {
            fillVideoView(remoteFullScreenVideoView, localVideoTrack, false);
        }
    }

    @Override
    public void onRemoteVideoTrackReceive(QBRTCSession session, final QBRTCVideoTrack videoTrack, final Integer userID) {
        Log.d(TAG, "onRemoteVideoTrackReceive for opponent= " + userID);

        if (localVideoTrack != null) {
            fillVideoView(localVideoView, localVideoTrack, false);
        }
        isLocalVideoFullScreen = false;

        if (isPeerToPeerCall) {
            setDuringCallActionBar();
            fillVideoView(userID, remoteFullScreenVideoView, videoTrack, true);
            updateVideoView(remoteFullScreenVideoView, false);
        } else {
            mainHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setRemoteViewMultiCall(userID, videoTrack);
                }
            }, LOCAL_TRACk_INITIALIZE_DELAY);
        }
    }*/
    /////////////////////////////////////////    end    ////////////////////////////////////////////


    //jpppppppppppppp
    @Override
    public void onLocalVideoTrackReceive(BaseSession baseSession, QBRTCVideoTrack videoTrack) {
        Log.d(TAG, "onLocalVideoTrackReceive() run");
        localVideoTrack = videoTrack;
        isLocalVideoFullScreen = true;
        cameraState = CameraState.NONE;


        if (remoteFullScreenVideoView != null) {
            fillVideoView(remoteFullScreenVideoView, localVideoTrack, false);
        }
    }

    @Override
    public void onRemoteVideoTrackReceive(BaseSession baseSession, final QBRTCVideoTrack videoTrack, final Integer userID) {
        Log.d(TAG, "onRemoteVideoTrackReceive for opponent= " + userID);


        if (localVideoTrack != null) {
            fillVideoView(localVideoView, localVideoTrack, false);
        }
        isLocalVideoFullScreen = false;
        CallActivity.isOpponenetConnected=true;
        if (isPeerToPeerCall) {
            setDuringCallActionBar();
            fillVideoView(userID, remoteFullScreenVideoView, videoTrack, true);
            updateVideoView(remoteFullScreenVideoView, false);
        } else {
            mainHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setRemoteViewMultiCall(userID, videoTrack);
                }
            }, LOCAL_TRACk_INITIALIZE_DELAY);
        }
    }



    @Override
    public void onStateChanged(BaseSession baseSession, BaseSession.QBRTCSessionState qbrtcSessionState) {

    }

    @Override
    public void onConnectedToUser(BaseSession baseSession, Integer userId) {
        connectionEstablished = true;
        setStatusForOpponent(userId, getString(R.string.text_status_connected));
        setProgressBarForOpponentGone(userId);
    }

    @Override
    public void onDisconnectedFromUser(BaseSession baseSession, Integer integer) {
        setStatusForOpponent(integer, getString(R.string.text_status_disconnected));
    }

    @Override
    public void onConnectionClosedForUser(BaseSession baseSession, Integer userId) {
        setStatusForOpponent(userId, getString(R.string.text_status_closed));
        if (!isPeerToPeerCall) {
            Log.d(TAG, "onConnectionClosedForUser videoTrackMap.remove(userId)= " + userId);
            getVideoTrackMap().remove(userId);
            setBackgroundOpponentView(userId);
        }
    }
    //jppppppppppppppppppppp


    /////////////////// Callbacks from CallActivity.QBRTCSessionUserCallback //////////////////////
    @Override
    public void onUserNotAnswer(QBRTCSession session, Integer userId) {
        setProgressBarForOpponentGone(userId);
        setStatusForOpponent(userId, getString(R.string.text_status_no_answer));
    }

    @Override
    public void onCallRejectByUser(QBRTCSession session, Integer userId, Map<String, String> userInfo) {
        setStatusForOpponent(userId, getString(R.string.text_status_rejected));
    }

    @Override
    public void onCallAcceptByUser(QBRTCSession session, Integer userId, Map<String, String> userInfo) {
        setStatusForOpponent(userId, getString(R.string.accepted));
    }

    @Override
    public void onReceiveHangUpFromUser(QBRTCSession session, Integer userId, Map<String, String> userInfo) {
        setStatusForOpponent(userId, getString(R.string.text_status_hang_up));
        Log.d(TAG, "onReceiveHangUpFromUser userId= " + userId);
        if (!isPeerToPeerCall) {
            if (userId == userIDFullScreen) {
                Log.d(TAG, "setAnotherUserToFullScreen call userId= " + userId);
                setAnotherUserToFullScreen();
            }
        }
    }

    @Override
    public void onSessionClosed(QBRTCSession session) {

    }

    //////////////////////////////////   end     //////////////////////////////////////////

    private void setAnotherUserToFullScreen() {
        if (opponentsAdapter.getOpponents().isEmpty()) {
            return;
        }
        int userId = opponentsAdapter.getItemQbID(0);
//      get opponentVideoTrack - opponent's video track from recyclerView
        QBRTCVideoTrack opponentVideoTrack = getVideoTrackMap().get(userId);
        if (opponentVideoTrack == null) {
            Log.d(TAG, "setAnotherUserToFullScreen opponentVideoTrack == null");
            return;
        }

        fillVideoView(userId, remoteFullScreenVideoView, opponentVideoTrack);
        Log.d(TAG, "fullscreen enabled");

        OpponentsFromCallAdapter.ViewHolder itemHolder = findHolder(userId);
        if (itemHolder != null) {
            opponentsAdapter.removeItem(itemHolder.getAdapterPosition());
            itemHolder.getOpponentView().release();
            Log.d(TAG, "onConnectionClosedForUser opponentsAdapter.removeItem= " + userId);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
       /* inflater.inflate(R.menu.conversation_fragment, menu);*/
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       /* switch (item.getItemId()) {
            case R.id.camera_switch:
                Log.d("Conversation", "camera_switch");
                switchCamera(item);
                return true;
            case R.id.screen_share:
              //  startScreenSharing();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }*/
        return super.onOptionsItemSelected(item);
    }

    private void startScreenSharing() {
        conversationFragmentCallbackListener.onStartScreenSharing();
    }

    @Override
    public void onOpponentsListUpdated(ArrayList<QBUser> newUsers) {
        super.onOpponentsListUpdated(newUsers);
        updateAllOpponentsList(newUsers);
        Log.d(TAG, "updateOpponentsList(), newUsers = " + newUsers);
        runUpdateUsersNames(newUsers);
    }

    private void updateAllOpponentsList(ArrayList<QBUser> newUsers) {

        for (int i = 0; i < allOpponents.size(); i++) {
            for (QBUser updatedUser : newUsers) {
                if (updatedUser.equals(allOpponents.get(i))) {
                    allOpponents.set(i, updatedUser);
                }
            }
        }
    }

    private void runUpdateUsersNames(final ArrayList<QBUser> newUsers) {
        //need delayed for synchronization with recycler view initialization
        mainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (QBUser user : newUsers) {
                    Log.d(TAG, "runUpdateUsersNames. foreach, user = " + user.getFullName());
                    updateNameForOpponent(user.getId(), user.getFullName());
                }
            }
        }, UPDATING_USERS_DELAY);
    }


    private enum CameraState {
        NONE,
        DISABLED_FROM_USER,
        ENABLED_FROM_USER
    }


    class DividerItemDecoration extends RecyclerView.ItemDecoration {

        private int space;

        public DividerItemDecoration(@NonNull Context context, @DimenRes int dimensionDivider) {
            this.space = context.getResources().getDimensionPixelSize(dimensionDivider);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.set(space, space, space, space);
        }
    }

    class LocalViewOnClickListener implements View.OnClickListener {
        private long lastFullScreenClickTime = 0L;

        @Override
        public void onClick(View v) {
            if ((SystemClock.uptimeMillis() - lastFullScreenClickTime) < FULL_SCREEN_CLICK_DELAY) {
                return;
            }
            lastFullScreenClickTime = SystemClock.uptimeMillis();

            if (connectionEstablished) {
                setFullScreenOnOff();
            }
        }

        private void setFullScreenOnOff() {

	        if (info_overlay_llay.isShown()||emotion_rlay.isShown()||userRlay.isShown()) {
		        hideToolBarAndButtons();
	        } else {
		        showToolBarAndButtons();
	        }


         /*   if (actionBar.isShowing()) {
                hideToolBarAndButtons();
            } else {
                showToolBarAndButtons();
            }*/
        }

        private void hideToolBarAndButtons() {
            actionBar.hide();

            localVideoView.setVisibility(View.INVISIBLE);
            info_overlay_llay.setVisibility(View.GONE);
            actionVideoButtonsLayout.setVisibility(View.GONE);
            emotion_rlay.setVisibility(View.GONE);
            userRlay.setVisibility(View.GONE);
            if (!isPeerToPeerCall) {
                shiftBottomListOpponents();
            }
        }

        private void showToolBarAndButtons() {
           // actionBar.show();
	        actionBar.hide();
            localVideoView.setVisibility(View.VISIBLE);
            info_overlay_llay.setVisibility(View.VISIBLE);
            actionVideoButtonsLayout.setVisibility(View.VISIBLE);
            emotion_rlay.setVisibility(View.VISIBLE);
            userRlay.setVisibility(View.VISIBLE);
            if (!isPeerToPeerCall) {
                shiftMarginListOpponents();
            }
        }

        private void shiftBottomListOpponents() {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) recyclerView.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            params.setMargins(0, 0, 0, 0);

            recyclerView.setLayoutParams(params);
        }

        private void shiftMarginListOpponents() {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) recyclerView.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
            params.setMargins(0, 0, 0, (int) getResources().getDimension(R.dimen.margin_common));

            recyclerView.setLayoutParams(params);
        }
    }


    @Subscribe
    public void onNotifyEvent(NotifyEvent event) {
        try {
            if(isValid(event.isSuccess)) {
                setEmotionViews();
               // setParticipantBar();
                //setDuringCallActionBar();
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void onSetValues(View view) throws Exception {

    }

    @Subscribe
    public void onChangedEmotionEvent(CallChangeEmotion event) {
        try {
            if (isValid(event.getEmotion()))
                if (isValid(event.getEmotion().getImage())) {
                    emotion_dimv.setVisibility(View.VISIBLE);
                    Picasso.with(getActivity()).load(NetworkService.GLOBAL_IMAGE_URL + event.getEmotion().getImage()).placeholder(R.drawable.placeholder_image).into(emotion_dimv);
                }

            if (isValid(event.getEmotion().getTitle())) {
                emotion_ctxv.setVisibility(View.VISIBLE);
                change_Emotion_name=event.getEmotion().getId();
                defSetText(emotion_ctxv, event.getEmotion().getTitle());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onPictureInPicture(PicInPic picInPic) {
        if (picInPic.isEnabled()) {
            hideToolBarAndButtons();
        } else {
            showToolBarAndButtons();
        }
    }

    private void hideToolBarAndButtons() {
        actionBar.hide();

        localVideoView.setVisibility(View.VISIBLE);
        info_overlay_llay.setVisibility(View.GONE);
        actionVideoButtonsLayout.setVisibility(View.GONE);
        emotion_rlay.setVisibility(View.GONE);
        userRlay.setVisibility(View.GONE);
        billingStatus.setVisibility(View.INVISIBLE);
        localVideoView.setVisibility(View.INVISIBLE);
        if (!isPeerToPeerCall) {
            shiftBottomListOpponents();
        }
    }

    private void showToolBarAndButtons() {
        // actionBar.show();
        actionBar.hide();
        localVideoView.setVisibility(View.VISIBLE);
        info_overlay_llay.setVisibility(View.VISIBLE);
        actionVideoButtonsLayout.setVisibility(View.VISIBLE);
        emotion_rlay.setVisibility(View.VISIBLE);
        userRlay.setVisibility(View.VISIBLE);
        localVideoView.setVisibility(View.VISIBLE);
        if (getActivity() instanceof PacifyrActivity) {
            if (((PacifyrActivity) getActivity()).hasBillingStarted()) {
                billingStatus.setVisibility(View.VISIBLE);
            } else {
                billingStatus.setVisibility(View.INVISIBLE);
            }
        }
        if (!isPeerToPeerCall) {
            shiftMarginListOpponents();
        }
    }

    private void shiftBottomListOpponents() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) recyclerView.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.setMargins(0, 0, 0, 0);

        recyclerView.setLayoutParams(params);
    }

    private void shiftMarginListOpponents() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) recyclerView.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
        params.setMargins(0, 0, 0, (int) getResources().getDimension(R.dimen.margin_common));

        recyclerView.setLayoutParams(params);
    }
}


