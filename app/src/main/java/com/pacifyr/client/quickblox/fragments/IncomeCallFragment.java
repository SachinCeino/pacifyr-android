package com.pacifyr.client.quickblox.fragments;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.pacifyrapp.utilitylibrary.model.pacifyr.MUser;
import com.pacifyr.client.event.CallDetailEvent;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.quickblox.activities.CallActivity;
import com.pacifyr.client.quickblox.db.QbUsersDbManager;
import com.pacifyr.client.quickblox.utils.CollectionsUtils;
import com.pacifyr.client.quickblox.utils.RingtonePlayer;
import com.pacifyr.client.quickblox.utils.UsersUtils;
import com.pacifyr.client.quickblox.utils.WebRtcSessionManager;
import com.pacifyr.client.utility.BaseFragment;
import com.quickblox.chat.QBChatService;
import com.quickblox.sample.core.utils.UiUtils;
import com.quickblox.sample.groupchatwebrtc.R;
import com.quickblox.users.model.QBUser;
import com.quickblox.videochat.webrtc.QBRTCSession;
import com.quickblox.videochat.webrtc.QBRTCTypes;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * QuickBlox team
 */
public class IncomeCallFragment extends BaseFragment implements Serializable, View.OnClickListener {

    private static final String TAG = IncomeCallFragment.class.getSimpleName();
    private static final long CLICK_DELAY = TimeUnit.SECONDS.toMillis(2);
    private TextView callTypeTextView;
    private ImageButton rejectButton;
    private ImageButton takeButton;

    private List<Integer> opponentsIds;
    private Vibrator vibrator;
    private QBRTCTypes.QBConferenceType conferenceType;
    private long lastClickTime = 0l;
    private RingtonePlayer ringtonePlayer;
    private IncomeCallFragmentCallbackListener incomeCallFragmentCallbackListener;
    private QBRTCSession currentSession;
    private QbUsersDbManager qbUserDbManager;
    private TextView alsoOnCallText;

    public MUser getCallPacifyr() {
        MUser mUser=null;
        try{

            mUser= CallActivity.mSessionDetailRoot.getUser();
        }catch (Exception e)
        {
            mUser=null;
        }
        return mUser;
    }
    public MUser getCallUser() {
        MUser mUser=null;
        try{
            mUser= CallActivity.mSessionDetailRoot.getUser();
        }catch (Exception e)
        {
            mUser=null;
        }
        return mUser;
    }

    public MUser getPacifyrForNameAndImage()
    {
        MUser mUser=null;
        try{

            mUser= CallActivity.mSessionDetailRoot.getPacifyr();
        }catch (Exception e)
        {
            mUser=null;
        }
        return mUser;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            incomeCallFragmentCallbackListener = (IncomeCallFragmentCallbackListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnCallEventsController");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRetainInstance(true);

        Log.d(TAG, "onCreate() from IncomeCallFragment");
        super.onCreate(savedInstanceState);
    }

    @Subscribe
    public void onNotifyEvent(NotifyEvent event) {

    }
    @Subscribe
    public void onCallDetailEvent(CallDetailEvent event) {
        try {
            MUser mUser = getPacifyrForNameAndImage();
            if (isValid(mUser)) {
                defSetText(callerNameTextView, "" + mUser.getName());
                if (isValid(mUser.getImageUrl()))
                    Picasso.with(getActivity()).load(mUser.getImageUrl()).into(callerAvatarImageView);
                //.placeholder(R.drawable.placeholder_user)
            }
        }catch (Exception e)
        {

        }
    }
    @Override
    public void onSetValues(View view) throws Exception {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_income_call, container, false);

        initFields();
        hideToolBar();

        if (currentSession != null) {
            initUI(view);
            setDisplayedTypeCall(conferenceType);
            initButtonsListener();
        }
        if(!isValid(ringtonePlayer))
            ringtonePlayer = new RingtonePlayer(getActivity(),R.raw.pacifyr_3);
        //  ringtonePlayer = new RingtonePlayer(getActivity());
        return view;
    }

    private void initFields() {
        currentSession = WebRtcSessionManager.getInstance(getActivity()).getCurrentSession();
        qbUserDbManager = QbUsersDbManager.getInstance(getActivity());

        if (currentSession != null) {
            opponentsIds = currentSession.getOpponents();
            conferenceType = currentSession.getConferenceType();
            Log.d(TAG, conferenceType.toString() + "From onCreateView()");
        }
    }

    public void hideToolBar() {
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar_call);
        toolbar.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        //startCallNotification();
    }

    private void initButtonsListener() {
        rejectButton.setOnClickListener(this);
        takeButton.setOnClickListener(this);
    }
    TextView callerNameTextView;
    CircleImageView callerAvatarImageView;
    private void initUI(View view) {
        callTypeTextView = view.findViewById(R.id.call_type);

        callerAvatarImageView = view.findViewById(R.id.image_caller_avatar);


        callerNameTextView = view.findViewById(R.id.text_caller_name);

        MUser mUser=getCallUser();
        if(isValid(mUser))
        {
            defSetText(callerNameTextView,""+mUser.getName());
            if(isValid(mUser.getImageUrl()))
                Picasso.with(getActivity()).load(mUser.getImageUrl()).into(callerAvatarImageView);
            //.placeholder(R.drawable.placeholder_user).
        }
        else {
            QBUser callerUser = qbUserDbManager.getUserById(currentSession.getCallerID());
            callerNameTextView.setText(UsersUtils.getUserNameOrId(callerUser, currentSession.getCallerID()));
            callerAvatarImageView.setBackgroundDrawable(getBackgroundForCallerAvatar(currentSession.getCallerID()));
        }


        TextView otherIncUsersTextView = view.findViewById(R.id.text_other_inc_users);
        otherIncUsersTextView.setText(getOtherIncUsersNames());

        alsoOnCallText = view.findViewById(R.id.text_also_on_call);
        setVisibilityAlsoOnCallTextView();

        rejectButton = view.findViewById(R.id.image_button_reject_call);
        takeButton = view.findViewById(R.id.image_button_accept_call);
    }

    private void setVisibilityAlsoOnCallTextView() {
        if (opponentsIds.size() < 2) {
            alsoOnCallText.setVisibility(View.INVISIBLE);
        }
    }

    private Drawable getBackgroundForCallerAvatar(int callerId) {
        return UiUtils.getColorCircleDrawable(callerId);
    }

    public void startCallNotification() {
        Log.d(TAG, "startCallNotification()");

        if(isValid(ringtonePlayer))
            ringtonePlayer.play(false);
            //jch bjdcb  jcb ndcjhd chb vhjhg vha
        else
        {
            ringtonePlayer = new RingtonePlayer(getActivity(),R.raw.pacifyr_3);
            ringtonePlayer.play(false);
        }

        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        long[] vibrationCycle = {0, 1000, 1000};
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(vibrationCycle, 1);
        }

    }

    private void stopCallNotification() {
        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        Log.d(TAG, "stopCallNotification()");

        if (ringtonePlayer != null) {
            ringtonePlayer.stop();
        }

        if (vibrator != null) {
            vibrator.cancel();
        }
    }

    private String getOtherIncUsersNames() {
        ArrayList<QBUser> usersFromDb = qbUserDbManager.getUsersByIds(opponentsIds);
        ArrayList<QBUser> opponents = new ArrayList<>();
        opponents.addAll(UsersUtils.getListAllUsersFromIds(usersFromDb, opponentsIds));

        opponents.remove(QBChatService.getInstance().getUser());
        Log.d(TAG, "opponentsIds = " + opponentsIds);
        return CollectionsUtils.makeStringFromUsersFullNames(opponents);
    }

    private void setDisplayedTypeCall(QBRTCTypes.QBConferenceType conferenceType) {
        boolean isVideoCall = conferenceType == QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO;

        callTypeTextView.setText(isVideoCall ? R.string.text_incoming_video_call : R.string.text_incoming_audio_call);
        takeButton.setImageResource(isVideoCall ? R.drawable.ic_video_white : R.drawable.ic_call);
    }

    @Override
    public void onStop() {
        stopCallNotification();
        super.onStop();
        Log.d(TAG, "onStop() from IncomeCallFragment");
    }

    @Override
    public void onClick(View v) {

        if ((SystemClock.uptimeMillis() - lastClickTime) < CLICK_DELAY) {
            return;
        }
        lastClickTime = SystemClock.uptimeMillis();

        switch (v.getId()) {
            case R.id.image_button_reject_call:
                reject();
                break;

            case R.id.image_button_accept_call:
                pt_call:
                accept();
                break;

            default:
                break;
        }
    }

    private void accept() {
        enableButtons(false);
        stopCallNotification();

        incomeCallFragmentCallbackListener.onAcceptCurrentSession();
        Log.d(TAG, "Call is started");
    }

    private void reject() {
        enableButtons(false);
        stopCallNotification();

        incomeCallFragmentCallbackListener.onRejectCurrentSession();
        Log.d(TAG, "Call is rejected");
    }

    private void enableButtons(boolean enable) {
        takeButton.setEnabled(enable);
        rejectButton.setEnabled(enable);
    }
}
