package com.pacifyr.client.quickblox.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.pacifyr.client.R;
import com.pacifyr.client.activity.ActivityChangeEmotionFromCall;
import com.pacifyr.client.adapter.EmotionSpecialityAdapter;
import com.pacifyr.client.base.PacifyrActivity;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.event.CallChangeEmotion;
import com.pacifyr.client.event.CallEvent;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.event.PicInPic;
import com.pacifyr.client.home.CustomerDetailActivity;
import com.pacifyr.client.home.ProfileScreenActivity;
import com.pacifyr.client.interfaces.EmpMinutesListener;
import com.pacifyr.client.network.NetworkService;
import com.pacifyr.client.quickblox.activities.CallActivity;
import com.pacifyr.client.quickblox.utils.CollectionsUtils;
import com.pacifyr.client.utils.LogUtils;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MEmotion;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MUser;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.quickblox.users.model.QBUser;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by tereha on 25.05.16.
 */
public class AudioConversationFragment extends BaseConversationFragment implements CallActivity.OnChangeDynamicToggle {
    private static final String TAG = AudioConversationFragment.class.getSimpleName();

    private ToggleButton audioSwitchToggleButton;
    private TextView alsoOnCallText;
    // private TextView firstOpponentNameTextView;
    private TextView otherOpponentsTextView,connectionStatusLocal;
    private boolean headsetPlugged;
    private DeviceFitImageView emotion_dimv;

//    private CircleImageView callerAvatarImageView;

    private RelativeLayout emotion_rlay;
    private LinearLayout info_overlay_llay;
    private RelativeLayout userRlay;
    private MainTextView callCtxv;
    private CircleImageView userDimv;
    private MainTextView usernameCtxv;
    private DeviceFitImageView moreDimv;
    private RelativeLayout more_rlay;
    private  MainTextView reason_ctxv,emotion_ctxv;
    private MainTextView billingStatus;

    private  Switch billingSwitch;
    private TextView text_remaining_minutes;
    private String change_Emotion_name;
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
            if (getActivity() != null && getActivity() instanceof PacifyrActivity) {
                ((PacifyrActivity) getActivity()).showIOSDialogOKCancel("Warning", "Your monthly free sessions is expiring soon. Please enter your Credit Card to continue service.", "Add Card", new iOSDialogClickListener() {
                    @Override
                    public void onClick(iOSDialog dialog) {
                        dialog.dismiss();
                        //Toast.makeText(PacifyrActivity.this,"Clicked!",Toast.LENGTH_LONG).show();
                        setAlertAddCardInBlue2();

                    }
                }, "Cancel");
            }
            /*setAlertStatusInBlue2("Warning", "Your monthly free sessions is expiring soon. Please enter your Credit Card to continue service.",
                    "Add Card", "Cancel", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setAlertAddCardInBlue2();
                        }
                    });*/
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
    private View cal_layout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(eventReceiver, new IntentFilter(CallEvent.CALL_EVENT));
        conversationFragmentCallbackListener.addOnChangeDynamicToggle(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void configureOutgoingScreen() {
        outgoingOpponentsRelativeLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
        allOpponentsTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_color_outgoing_opponents_names_audio_call));
        ringingTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_color_call_type));
    }

    @Override
    protected void configureToolbar() {
        toolbar.setVisibility(View.VISIBLE);
        toolbar.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
        toolbar.setTitleTextColor(ContextCompat.getColor(getActivity(), R.color.toolbar_title_color));
        toolbar.setSubtitleTextColor(ContextCompat.getColor(getActivity(), R.color.toolbar_subtitle_color));
    }

    @Override
    protected void configureActionBar() {
        //    if(currentUser.g)
        //actionBar.setTitle(currentUser.getTags().get(0));
        actionBar.setTitle("Audio Call");
        if(isValid( actionBar))
            actionBar.hide();
        // actionBar.setSubtitle(String.format(getString(R.string.subtitle_text_logged_in_as), currentUser.getFullName()));
    }

    @Override
    protected void initViews(View view) {
        super.initViews(view);
        if(isValid( actionBar))
            actionBar.hide();
        timerChronometer = view.findViewById(R.id.chronometer_timer_audio_call);
        emotion_dimv = view.findViewById(R.id.emotion_dimv);
        emotion_rlay = view.findViewById(R.id.emotion_rlay);
        reason_ctxv = view.findViewById(R.id.reason_ctxv);
        connectionStatusLocal = view.findViewById(R.id.connectionStatusLocal);
        emotion_ctxv = view.findViewById(R.id.emotion_ctxv);
        billingStatus = view.findViewById(R.id.billing_status);
        cal_layout = view.findViewById(R.id.call_action_butons);
        text_remaining_minutes = view.findViewById(R.id.text_remaining_minutes);
//        billingSwitch = (Switch) view.findViewById(R.id.billing_switch);
        emotion_dimv.setVisibility(View.INVISIBLE);
        reason_ctxv.setVisibility(View.INVISIBLE);
        emotion_ctxv.setVisibility(View.INVISIBLE);
        text_remaining_minutes.setVisibility(View.GONE);
//        callerAvatarImageView = (CircleImageView) view.findViewById(R.id.image_caller_avatar);
        //  firstOpponentNameTextView = (TextView) view.findViewById(R.id.text_caller_name);
        //    firstOpponentNameTextView.setText(opponents.get(0).getFullName());
        // firstOpponentNameTextView.setVisibility(View.GONE);
        onUserInfoViews(view);
        setEmotionViews();

        /*billingSwitch.setVisibility(View.VISIBLE);
        billingSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    billingSwitch.setClickable(false);
                    billingSwitch.setSelected(true);
                    startBilling();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });*/

      /*  ImageView firstOpponentAvatarImageView = (ImageView) view.findViewById(R.id.image_caller_avatar);
        firstOpponentAvatarImageView.setBackgroundDrawable(UiUtils.getColorCircleDrawable(opponents.get(0).getId()));
*/
        //setUserInfoViews();
        alsoOnCallText = view.findViewById(R.id.text_also_on_call);
        setVisibilityAlsoOnCallTextView();


        otherOpponentsTextView = view.findViewById(R.id.text_other_inc_users);
        otherOpponentsTextView.setText(getOtherOpponentsNames());
        otherOpponentsTextView.setVisibility(View.GONE);


        audioSwitchToggleButton = view.findViewById(R.id.toggle_speaker);
        audioSwitchToggleButton.setVisibility(View.VISIBLE);

        actionButtonsEnabled(false);

        emotion_rlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityChangeEmotionFromCall.class);
                if (isValid(change_Emotion_name))
                {
                    intent.putExtra("emotion",change_Emotion_name);
                }
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

    /*private void startBilling() {
        try{
            PrefManager prefM= PrefManager.getInstance(getContext());
            String token=prefM.getAccessToken();
            //String userID = prefM.getUserID();
            String url = GLOBAL_URL+"session/startbilling/"+sessionid;
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    try {
                        String stat = status.getMessage();
                        String jsonStrng = json.toString();
                        billingSwitch.setText("Billing started");
                        Toast.makeText(getContext(),"Billing started",Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            AQuery aq = new AQuery(getContext());
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken",token);
            aq.ajax(url,JSONObject.class, cb);

        }catch (Exception e){
            e.printStackTrace();
        }
    }*/


    public void onUserInfoViews(View view) {
        try {
            if(isValid( actionBar))
                actionBar.hide();
            info_overlay_llay = view.findViewById(R.id.info_overlay_llay);
            userRlay = view.findViewById(R.id.user_rlay);
            callCtxv = view.findViewById(R.id.call_ctxv);
            userDimv = view.findViewById(R.id.user_dimv);
            usernameCtxv = view.findViewById(R.id.username_ctxv);
            more_rlay = view.findViewById(R.id.more_rlay);
            moreDimv = view.findViewById(R.id.more_dimv);
            moreDimv.setVisibility(View.VISIBLE);
            more_rlay.setVisibility(View.GONE);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void setUserInfoViews() {
        try{

            if(isValid(call_particpants_list))
                if(isCustomer())
                {
                    MUser paacifyr=call_particpants_list.get(1);
                    if(!isValid(paacifyr))
                        return;
                    /*if(isValid(paacifyr.getImageUrl()))
                        Picasso.with(getActivity()).load(paacifyr.getImageUrl()).into(callerAvatarImageView);*/
                 /*  defSetText(firstOpponentNameTextView,paacifyr.getName());
                    firstOpponentNameTextView.setVisibility(View.VISIBLE);*/
                }else
                {
                    MUser customer=call_particpants_list.get(0);
                    if(!isValid(customer))
                        return;
                 /*   defSetText(firstOpponentNameTextView,customer.getName());
                    firstOpponentNameTextView.setVisibility(View.VISIBLE);*/
                    /*if(isValid(customer.getImageUrl()))
                        Picasso.with(getActivity()).load(customer.getImageUrl()).into(callerAvatarImageView);*/

                }
          /*  if(isCustomer())
            {
                call_particpants_list=new ArrayList<MUser>();
                call_particpants_list.add( convertModel(NetworkService.mCurrentUser));
                call_particpants_list.add(CallActivity.mPacifyr);
                MUser mUser=CallActivity.mPacifyr;
                if(isValid(mUser))
                {
                    defSetText(callerNameTextView,""+mUser.getName());
                    if(isValid(mUser.getImageUrl()))
                        Picasso.with(getActivity()).load(mUser.getImageUrl()).into(callerAvatarImageView);
                }
            }*/

            if(isValid( actionBar))
                actionBar.hide();
        }catch (Exception e)
        {
            LogUtils.LOGD("exception", " ACF setUserInfoViews Exception "+e.getMessage());
        }
    }


    private void setResaonViews() {
        try{
            reason_ctxv.setVisibility(View.VISIBLE);
            if(isCustomer())
                defSetText(reason_ctxv, EmotionSpecialityAdapter.reason_forcall);
            else {
                defSetText(reason_ctxv, CallActivity.mSessionDetailRoot.getReasonForTheCall());
            }

            if(isValid( actionBar))
                actionBar.hide();
        }catch (Exception e)
        {
            reason_ctxv.setVisibility(View.GONE);
            LogUtils.LOGD("exception", " ACF setResaonViews Exception "+e.getMessage());
        }
    }
    private void setEmotionViews() {
        try{
            setResaonViews();
            String image=null;
            MEmotion emotion=null;

            if(isCustomer()) {
                emotion=PacifyrActivity.call_emotion;
                image= PacifyrActivity.call_emotion.getImage();
                call_particpants_list=new ArrayList<MUser>();
                call_particpants_list.add( convertModel(NetworkService.mCurrentUser));
                call_particpants_list.add(CallActivity.mPacifyr);
            }
            else {
                MUser mUser = getCallUser();
                call_particpants_list=getCallParticipantlist();
                emotion_dimv.setVisibility(View.GONE);
                // reason_ctxv.setVisibility(View.GONE);
                if(!isValid(mUser)) return;
                if(isValid(mUser.getEmotion())) {
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
            setUserRayInfoViews();
            setUserInfoViews();
            if(isValid(image)) {
                emotion_dimv.setVisibility(View.VISIBLE);
                Picasso.with(getActivity()).load(NetworkService.GLOBAL_IMAGE_URL + image).placeholder(R.drawable.placeholder_image).into(emotion_dimv);
            }

            if(isValid(emotion)) {
                emotion_ctxv.setVisibility(View.VISIBLE);
                change_Emotion_name=emotion.getId();
                defSetText(emotion_ctxv,emotion.getTitle());
            }
            if(isValid( actionBar))
                actionBar.hide();
        }catch (Exception e)
        {
            LogUtils.LOGD("exception", " ACF setEmotionViews Exception "+e.getMessage());
        }
    }

    private void setUserRayInfoViews() {
        try{
            if(isValid(actionBar))
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
                    final MUser muser=  getCallUser();
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
                    MUser muser=  getCallPacifyr();
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
   /* private void setEmotionViews() {
        try{
            setResaonViews();
            MUser mUser=getCallUser();
            String image=null;
            emotion_dimv.setVisibility(View.GONE);

            if(!isValid(mUser))
                return;
            if(isValid(mUser.getEmotion())) {
                image= mUser.getEmotion().getImage();
                if(!isValid(image))
                {
                    if(isValid(mUser.getEmotionId()))
                    {
                        MEmotion mEmotion= getEmotion(mUser.getEmotionId())  ;
                        image= mEmotion.getImage();

                    }
                }
            }
            else if(isValid(mUser.getEmotionId()))
            {
                MEmotion mEmotion= getEmotion(mUser.getEmotionId())  ;
                image= mEmotion.getImage();

            }


            if(isValid(image)) {
                emotion_dimv.setVisibility(View.VISIBLE);
                Picasso.with(getActivity()).load(NetworkService.GLOBAL_IMAGE_URL + image).placeholder(R.drawable.placeholder_image).into(emotion_dimv);
            }

        }catch (Exception e)
        {

        }
    }*/

    private void setVisibilityAlsoOnCallTextView() {
        if (opponents.size() < 2) {
            alsoOnCallText.setVisibility(View.INVISIBLE);
        }
    }

    private String getOtherOpponentsNames() {
        ArrayList<QBUser> otherOpponents = new ArrayList<>();
        otherOpponents.addAll(opponents);
        otherOpponents.remove(0);

        return CollectionsUtils.makeStringFromUsersFullNames(otherOpponents);
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(eventReceiver);
        conversationFragmentCallbackListener.removeOnChangeDynamicToggle(this);
    }

    @Override
    protected void initButtonsListener() {
        super.initButtonsListener();

        audioSwitchToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conversationFragmentCallbackListener.onSwitchAudio();
            }
        });
    }

    @Override
    protected void actionButtonsEnabled(boolean inability) {
        super.actionButtonsEnabled(inability);
        if (!headsetPlugged) {
            audioSwitchToggleButton.setEnabled(inability);
        }
        audioSwitchToggleButton.setActivated(inability);
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
    @Override
    int getFragmentLayout() {
        return R.layout.fragment_audio_conversation;
    }

    @Override
    public void onOpponentsListUpdated(ArrayList<QBUser> newUsers) {
        super.onOpponentsListUpdated(newUsers);
        //  firstOpponentNameTextView.setText(opponents.get(0).getFullName());
        // otherOpponentsTextView.setText(getOtherOpponentsNames());
    }

    @Override
    public void enableDynamicToggle(boolean plugged, boolean previousDeviceEarPiece) {
        headsetPlugged = plugged;

        if (isStarted) {
            audioSwitchToggleButton.setEnabled(!plugged);

            if (plugged) {
                audioSwitchToggleButton.setChecked(true);
            }else if(previousDeviceEarPiece){
                audioSwitchToggleButton.setChecked(true);
            } else {
                audioSwitchToggleButton.setChecked(false);
            }
        }
    }

    @Override
    public void onSetValues(View view) throws Exception {

    }
    @Subscribe
    public void onNotifyEvent(NotifyEvent event) {
        try {
            if(isValid(event.isSuccess))
                setEmotionViews();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
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

        info_overlay_llay.setVisibility(View.GONE);
        emotion_rlay.setVisibility(View.GONE);
        userRlay.setVisibility(View.GONE);
        billingStatus.setVisibility(View.GONE);
        cal_layout.setVisibility(View.INVISIBLE);
        Log.d(TAG, "hideToolBarAndButtons");

    }

    private void showToolBarAndButtons() {
        // actionBar.show();
        actionBar.hide();
        info_overlay_llay.setVisibility(View.VISIBLE);
        emotion_rlay.setVisibility(View.VISIBLE);
        userRlay.setVisibility(View.VISIBLE);
        cal_layout.setVisibility(View.VISIBLE);
        if (getActivity() instanceof PacifyrActivity) {
            if (((PacifyrActivity) getActivity()).hasBillingStarted()) {
                billingStatus.setVisibility(View.VISIBLE);
            } else {
                billingStatus.setVisibility(View.INVISIBLE);
            }
        }

        Log.d(TAG, "showToolBarAndButtons");

    }



}
