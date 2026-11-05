package com.pacifyr.client.home;

import com.pacifyr.client.utils.MetaEventLogger;
import static com.pacifyr.client.network.NetworkService.GLOBAL_URL;
import static com.pacifyr.client.network.NetworkService.modelAllEmotions;
import static com.pacifyr.client.twiliochat.TwilioConversationActivity.DEFAULT_CONVERSATION_NAME;
import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.GLOBAL_IMAGE_URL;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.eralp.circleprogressview.CircleProgressView;
import com.google.gson.Gson;
import com.pacifyr.client.R;
import com.pacifyr.client.adapter.CommentsAdapter;
import com.pacifyr.client.adapter.EmotionsListAdapter;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.event.AlertTypeEvent;
import com.pacifyr.client.event.EventLoadEmotion;
import com.pacifyr.client.event.InternetConnectionEvent;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.model.MComments;
import com.pacifyr.client.model.interfaces.CommentClick;
import com.pacifyr.client.network.GsonUtils;
import com.pacifyr.client.network.NetworkService;
import com.pacifyr.client.quickblox.App;
import com.pacifyr.client.receiver.NotificationUtils;
import com.pacifyr.client.utility.PacifyrAppCompactActivity;
import com.pacifyr.client.utils.LogUtils;
import com.pacifyr.client.utils.SnackUtils;
import com.pacifyr.client.utils.Utility;
import com.pacifyr.client.utils.WrappingSlidingPaneLayout;
import com.pacifyrapp.utilitylibrary.CustomEditText;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;
import com.pacifyrapp.utilitylibrary.ToastHandler;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MEmotion;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MSessionDetailRoot;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MSessionRating;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;
import com.willy.ratingbar.BaseRatingBar;
import com.willy.ratingbar.ScaleRatingBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import app.minimize.com.seek_bar_compat.SeekBarCompat;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by developer on 12/22/2017.
 */

public class LastSessionActivity extends PacifyrAppCompactActivity {


    private static final String TAG = LastSessionActivity.class.getSimpleName();
    boolean showingFirst = true;


    EmotionsListAdapter emotionsListAdapter;
    RecyclerView recyclerView;

    RecyclerView commentsRcv;
    CommentsAdapter commentsAdapter;

    WrappingSlidingPaneLayout mSlidingPaneLayout;
    boolean onceBackpressed = false;
    private int rating;


    private int effectiveness = 0;

    CircleImageView img_blue_bg, img_blue_bg_after;


    ArrayList<MEmotion> emotions;
    private CircleImageView pacifyrImgCimv;
    private MainTextView pacifyrNameCtxv, callback_ctxv;
    private ImageView beforeFeelDimv;
    private MainTextView durationCtxv;
    private MainTextView pacifyrRateCtxv, before_feel_ctxv, after_feel_ctxv;
    private MainTextView sessionCostCtxv;
    private MainTextView before_emotion_ctxv, after_emotion_ctxv;
    private int oldRating;
    private ImageView callTypeDimv;
    public MSessionDetailRoot mSessionDetailRoot;
    public SeekBarCompat effective_seekbar;
    public ScaleRatingBar rating_bar;
    private TextView mTextView_View_pres;
    // public RatingBar rating_bar;


    CustomEditText comments_cedt;
    public boolean isCustomer = false;
    //public LinearLayout rating_llay,before_feel_llay;
    //  private LinearLayout afterFeelSelectionLlay;
    //private LinearLayout  after_feel_llay;
    private RelativeLayout after_feel_llay;
    private RelativeLayout afterFeelSelectionLlay;
    public RelativeLayout rating_llay, before_feel_llay;
    public static boolean isAudioCall = true;

    private CircleProgressView circleProgressView;


    public static String ratingId = null;

    public static boolean mysessions = false;

    private MainTextView afterFeelSelectionCtxv;
    private ImageView afterFeelDimv;
    private ImageView ImageView_i_icon;
    private LinearLayout pacifyrEaringsLlay, customer_fees_llay;
    private MainTextView earingstCtxv, comapnyfeesCtxv;


    private ImageButton img_notes;
    private boolean editable = true;
    private String mSessionId;

    /*public static void start(Context context, boolean isAudioCall) {
        Intent intent = new Intent(context, LastSessionActivity.class);
        //  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        //  intent.putExtra(Consts.EXTRA_IS_STARTED_FOR_CALL, isRunForCall);
        LastSessionActivity.isAudioCall = isAudioCall;
      *//*  try {
            Activity activity = null;
            activity = ((AppChatNotUSe) AppChatNotUSe.getInstance()).getCurrentActivity();
            if(isValidStatic(activity))
                if (activity instanceof CallActivity) {
                    activity.finish();
                }
        } catch (Exception e) {
            e.printStackTrace();
        }*//*


        context.startActivity(intent);
    }*/

    public static Boolean isValidStatic(Object object) {
        return object != null;

    }

    /*public static void start(Context context) {
        Intent intent = new Intent(context, LastSessionActivity.class);
        //  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        // intent.putExtra(Consts.EXTRA_IS_STARTED_FOR_CALL, isRunForCall);
        LastSessionActivity.isAudioCall = true;


       *//* try {
            Activity activity = null;
            activity = ((AppChatNotUSe) AppChatNotUSe.getInstance()).getCurrentActivity();
            if(isValidStatic(activity))
                if (activity instanceof CallActivity ) {
                    ((AppChatNotUSe) AppChatNotUSe.getInstance()).setCurrentActivity(null);
                    activity.finish();

                }
        } catch (Exception e) {
            e.printStackTrace();
        }*//*
        context.startActivity(intent);
    }*/

    /*public static void start(Context context, String sessionID) {
        Intent intent = new Intent(context, LastSessionActivity.class);
        //  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        // intent.putExtra(Consts.EXTRA_IS_STARTED_FOR_CALL, isRunForCall);
        intent.putExtra("id", sessionID);
        LastSessionActivity.isAudioCall = true;
        mysessions = true;
        sessionid = sessionID;
       *//* try {
            Activity activity = null;
            activity = ((AppChatNotUSe) AppChatNotUSe.getInstance()).getCurrentActivity();
            if(isValidStatic(activity))
                if (activity instanceof CallActivity ) {
                    ((AppChatNotUSe) AppChatNotUSe.getInstance()).setCurrentActivity(null);
                    activity.finish();

                }
        } catch (Exception e) {
            e.printStackTrace();
        }*//*
        context.startActivity(intent);
    }*/

    public static void start(Context context, String sessionID, boolean editable) {
        if (!isValidStatic(sessionID)) {
            Log.e(TAG, "Invalid session Id. Cannot start session summary activity");
            return;
        }
        Intent intent = new Intent(context, LastSessionActivity.class);
        //  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        // intent.putExtra(Consts.EXTRA_IS_STARTED_FOR_CALL, isRunForCall);
        intent.putExtra("id", sessionID);
        intent.putExtra("editable", editable);
        LastSessionActivity.isAudioCall = true;
        mysessions = true;
       // sessionid = sessionID;
       /* try {
            Activity activity = null;
            activity = ((AppChatNotUSe) AppChatNotUSe.getInstance()).getCurrentActivity();
            if(isValidStatic(activity))
                if (activity instanceof CallActivity ) {
                    ((AppChatNotUSe) AppChatNotUSe.getInstance()).setCurrentActivity(null);
                    activity.finish();

                }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        context.startActivity(intent);
    }

 public static void startNew(Context context, String sessionID, boolean editable) {
        if (!isValidStatic(sessionID)) {
            Log.e(TAG, "Invalid session Id. Cannot start session summary activity");
            return;
        }
        Intent intent = new Intent(context, LastSessionActivity.class);
       intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("id", sessionID);
        intent.putExtra("editable", editable);
        LastSessionActivity.isAudioCall = true;
        mysessions = true;

        context.startActivity(intent);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


       /* if (!isValid(lastSession,"Please check later")) {
          // handlerFinish("Error in last session ");
            handlerFinish("No sessions yet, Connect to Pacifyr");
        }
        else {*/
           /* if(mysessions==true){
                //Log.i("sesssionid1","s:  "+sessionid);
            }*/
           /* else {
            sessionid = lastSession.getId();}*/

        if (isValid(getIntent().getExtras().getString("id"))) {
            mSessionId = getIntent().getExtras().getString("id") + "";
           // sessionid = getIntent().getExtras().getString("id") + "";
        }

        if (getIntent().getExtras().containsKey("editable")) {
            editable = getIntent().getExtras().getBoolean("editable");
        }
        ratingId = null;
        LogUtils.LOGD("session", " LSa onCreate sessionid " + mSessionId);
        if (!isValid(mSessionId, "Please check later")) {
            LogUtils.LOGD("session", " LSa onCreate sessionid not valid  " + mSessionId);
            //  handlerFinish("Error in last session ");
            handlerFinish("Data not updated in last session ");

        }
        LogUtils.LOGD("session", " LSa onCreate sessionid " + mSessionId);
        setContentView(R.layout.activity_last_session);

        NetworkService.webListPositiveEmotions(this);
        NetworkService.webListNegativeEmotions(this);

        if (!isCustomer() && !isPacifyer()) {
            ToastHandler.newInstance(this).mustShowToast("Session Expired : Please login again");
            deleteInstallationWeb();
            finish();
        }
        isCustomer = isCustomer();


        // webListEmotions(LastSessionActivity.this);
        onCreateView();
        if (isValid(mSessionId)) webGetSessionDetail(mSessionId);
        else {
            handlerFinish("Data not updated in last session ");
            // finish();
        }



        /* }*/
        comments_cedt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showKeyboard();
            }
        });

        if (getPackageName().equalsIgnoreCase("com.pacifyr.pam"))
        {
            after_feel_llay.setVisibility(View.GONE);
            before_feel_llay.setVisibility(View.GONE);
        }

    }


    public void showKeyboard() {
        comments_cedt.setFocusable(true);
        comments_cedt.setFocusableInTouchMode(true);
        comments_cedt.requestFocus();
        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(comments_cedt, InputMethodManager.SHOW_FORCED);
        }
    }

    private final BroadcastReceiver notificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.hasExtra("message")) {
                String message = intent.getExtras().getString("message");
                String type = intent.getExtras().getString("type");
                if (message != null && message != "") {
                    if(type.equals("twilio.conversations.new_message") || type.equals("twilioChat") ){

                        String conversation_sid = intent.getExtras().getString("conversation_sid");

                        if(isValid(DEFAULT_CONVERSATION_NAME) && isValid(conversation_sid)
                                && conversation_sid.equals(DEFAULT_CONVERSATION_NAME)){

                            return;
                        }

                        SnackUtils.show(actionBarTopLlay,message, LastSessionActivity.this,getLayoutInflater(),conversation_sid);

                    }
                }
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(notificationReceiver, new IntentFilter(NotificationUtils.NOTIFY_INTENT_FOR_NEW_CHAT));
        App.getInstance().setCurrentActivity(this);
    }

    @Override
    protected View getSnackbarAnchorView() {
        return null;
    }

/*    public MEmotion getLastSessionEmotion()
    {
        MEmotion id=null;
        try{

            if (isCustomer()) {
                // id= WebRtcSessionManager.userCallInfo.OpponentUserid;
                // id=pacifyerDetial.getEmotion();
                id= LastSessionActivity.call_emotion;
                // id=call_session_detail_root.getSession().getUser().getEmotion();
            }
            else
            {
                id=mSessionDetailRoot.
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return id;
    }*/

   /* @Override
    public void onSetValues(View view) throws Exception {

    }*/


    public void onCreateView() {
        try {

            LogUtils.LOGD("session", " SSA onCreateView ");
            onSetActionBar();
            circleProgressView = findViewById(R.id.circle_progress_view);
            pacifyrImgCimv = findViewById(R.id.last_session_cimv);
            callTypeDimv = findViewById(R.id.call_type_dimv);
            img_blue_bg = findViewById(R.id.img_blue_bg);
            img_blue_bg_after = findViewById(R.id.img_blue_bg_after);
        /*    rating_llay = (LinearLayout) findViewById(R.id.rating_llay);
            before_feel_llay = (LinearLayout) findViewById(R.id.before_feel_llay);
            after_feel_llay = (LinearLayout) findViewById(R.id.after_feel_llay);
            afterFeelSelectionLlay = (LinearLayout) findViewById(R.id.after_feel_selection_llay);*/

            rating_llay = findViewById(R.id.rating_llay);
            ImageView_i_icon=findViewById(R.id.eye_icon);

            before_feel_llay = findViewById(R.id.before_feel_llay);
            after_feel_llay = findViewById(R.id.after_feel_llay);
            afterFeelSelectionLlay = findViewById(R.id.after_feel_selection_llay);

            before_feel_ctxv = findViewById(R.id.before_feel_ctxv);
            after_feel_ctxv = findViewById(R.id.after_feel_ctxv);


            before_emotion_ctxv = findViewById(R.id.before_emotion_ctxv);
            after_emotion_ctxv = findViewById(R.id.after_emotion_ctxv);

           /* setBeforeEmotionViews();
            setAfterEmotionViews();*/

            customer_fees_llay = findViewById(R.id.customer_fees_llay);
            afterFeelDimv = findViewById(R.id.after_feel_dimv);

            afterFeelSelectionCtxv = findViewById(R.id.after_feel_selection_ctxv);
            callback_ctxv = findViewById(R.id.callback_ctxv);
            sessionCostCtxv = findViewById(R.id.session_cost_ctxv);
            pacifyrEaringsLlay = findViewById(R.id.pacifyr_earings_llay);
            earingstCtxv = findViewById(R.id.earingst_ctxv);
            comapnyfeesCtxv = findViewById(R.id.comapnyfees_ctxv);
            pacifyrNameCtxv = findViewById(R.id.pacifyr_name_ctxv);
            beforeFeelDimv = findViewById(R.id.before_feel_dimv);
            recyclerView = findViewById(R.id.begning_rcv);
            commentsRcv = findViewById(R.id.comments_rcv);
            pacifyrRateCtxv = findViewById(R.id.pacifyr_rate_ctxv);
            durationCtxv = findViewById(R.id.duration_ctxv);
            comments_cedt = findViewById(R.id.comments_cedt);
            rating_bar = findViewById(R.id.rating_bar);
            //  rating_bar = (RatingBar) findViewById(R.id.rating_bar);

            effective_seekbar = findViewById(R.id.effective_seekbar);
            mTextView_View_pres = findViewById(R.id.mTextView_View_pres);
            //holoCircleSeekBar=(HoloCircleSeekBar) findViewById(R.id.picker);
            //effective_circle_view = (CircleView) findViewById(R.id.effective_circle_view);
            rating_llay.setVisibility(View.GONE);
            callback_ctxv.setVisibility(View.GONE);


            img_notes = findViewById(R.id.img_notes);
            img_notes.setVisibility(View.GONE);

            if (PrefManager.getInstance(this).getEnablePrescription()) {
                mTextView_View_pres.setVisibility(View.VISIBLE);
            } else {
                mTextView_View_pres.setVisibility(View.GONE);
            }

            if (isCustomer) {
                pacifyrEaringsLlay.setVisibility(View.GONE);
                customer_fees_llay.setVisibility(View.VISIBLE);
                pacifyrRateCtxv.setVisibility(View.VISIBLE);
                rating_llay.setVisibility(View.VISIBLE);
                before_feel_ctxv.setText(getString(R.string.your_feeling_before_call));
                afterFeelSelectionCtxv.setText(getString(R.string.your_feeling_after_call));
                after_feel_ctxv.setText("");
                after_feel_llay.setVisibility(View.GONE);
            } else {
                pacifyrEaringsLlay.setVisibility(View.VISIBLE);
                pacifyrRateCtxv.setVisibility(View.GONE);
                customer_fees_llay.setVisibility(View.GONE);
                callback_ctxv.setVisibility(View.GONE);
                before_feel_ctxv.setText(getString(R.string.client_feeling_before_call));
                after_feel_ctxv.setText(getString(R.string.client_feeling_after_call));
                after_feel_llay.setVisibility(View.VISIBLE);
                afterFeelSelectionCtxv.setText(getString(R.string.client_feeling_after_call_opinion));
            }

            comments_cedt.setVisibility(View.GONE);
            recyclerView.setLayoutManager(new LinearLayoutManager(LastSessionActivity.this, LinearLayoutManager.HORIZONTAL, false));
            commentsRcv.setLayoutManager(new LinearLayoutManager(LastSessionActivity.this, LinearLayoutManager.HORIZONTAL, false));
            commentsRcv.setNestedScrollingEnabled(false);
            recyclerView.setNestedScrollingEnabled(false);
            rating_bar.setNumStars(5);
            rating = 5;
            rating_bar.setStarPadding(10);
            rating_bar.setClickable(true);
            rating_bar.setClearRatingEnabled(true);
            if (editable) {
                rating_bar.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
                    @Override
                    public void onRatingChange(BaseRatingBar baseRatingBar, float v, boolean fromUser) {
                        //Log.e(TAG, "onRatingChange: " + rating);
                        rating = (int) baseRatingBar.getRating();
                    }
                });
            } else {
                rating_bar.setIsIndicator(true);
            }

            mTextView_View_pres.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isValid(mSessionDetailRoot.getPrescriptionFile()))
                    loadPDF(mSessionDetailRoot.getPrescriptionFile(),getResources().getString(R.string.prescription),false);
                    else
                        Toast.makeText(LastSessionActivity.this, getResources().getString(R.string.no_prescription), Toast.LENGTH_SHORT).show();
                }
            });

       /*     rating_bar.setNumStars(5);
            rating =5;
            rating_bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                    rating = (int) ratingBar.getRating();
                }
            });*/
            //effective_circle_view.setProgressStepAsInteger(true);
            // effective_circle_view.setSuffixEnabled(true);
            // effective_circle_view.setSuffixValue("%");
            if (editable) {
                effective_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                        circleProgressView.setProgress(progress);
                        effectiveness = progress;

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
            } else {
                effective_seekbar.setEnabled(false);
                effective_seekbar.setFocusableInTouchMode(false);
            }
            if (isValid(mSessionDetailRoot)) {
                onSetValues();
                setAdapter();
            }

        } catch (Exception e) {
            LogUtils.LOGD("session", "SSA onCreateView exce " + e.getMessage());
        }
    }



    private void setAfterEmotionViews(MEmotion mEmotion) {
        try {
            if (isValid(mEmotion)) {
                // after_feel_llay.setVisibility(View.VISIBLE);
                defSetText(after_emotion_ctxv, mEmotion.getTitle());

                if (isCustomer) {
                    afterFeelSelectionCtxv.setText(getString(R.string.your_feeling_after_call));
                    after_feel_ctxv.setText("");
                    after_feel_llay.setVisibility(View.GONE);
                } else {
                    after_feel_ctxv.setText(getString(R.string.client_feeling_after_call));
                    after_feel_llay.setVisibility(View.VISIBLE);
                    afterFeelSelectionCtxv.setText(getString(R.string.client_feeling_after_call_opinion));
                    String afterImgUrl = GLOBAL_IMAGE_URL + mEmotion.getImage();
                    if (isValid(afterImgUrl))
                        Picasso.with(LastSessionActivity.this).load(afterImgUrl).placeholder(R.drawable.sad).into(afterFeelDimv);
                    img_blue_bg_after.setVisibility(View.VISIBLE);
                    Resources res = getApplicationContext().getResources();
                    final int nColor = res.getColor(R.color.white);
                    afterFeelDimv.setColorFilter(nColor, PorterDuff.Mode.SRC_ATOP);
                }
            } else {
                after_feel_llay.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            LogUtils.LOGD("session", "SSA setAfterEmotionViews exce " + e.getMessage());
        }
    }

    private void setBeforeEmotionViews(MEmotion mEmotion) {
        try {
            if (isValid(mEmotion)) {
                before_feel_llay.setVisibility(View.VISIBLE);
                defSetText(before_emotion_ctxv, mEmotion.getTitle());
                defSetText(before_feel_ctxv, mEmotion.getTitle());

                if (isCustomer) {
                    before_feel_ctxv.setText(getString(R.string.your_feeling_before_call));
                } else {
                    before_feel_ctxv.setText(getString(R.string.client_feeling_before_call));
                }
                String beforeImgUrl = GLOBAL_IMAGE_URL + mEmotion.getImage();
                if (isValid(beforeImgUrl))
                    Picasso.with(LastSessionActivity.this).load(beforeImgUrl).placeholder(R.drawable.sad).into(beforeFeelDimv);
                img_blue_bg.setVisibility(View.VISIBLE);
                Resources res = getApplicationContext().getResources();
                final int nColor = res.getColor(R.color.white);
                beforeFeelDimv.setColorFilter(nColor, PorterDuff.Mode.SRC_ATOP);
            } else {
                before_feel_llay.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            LogUtils.LOGD("session", "SSA setBeforeEmotionViews exce " + e.getMessage());
        }
    }

    public String getEmotionAfterId() {
        String id = null;
        try {

            int session_pos = -1;
            String type = isCustomer ? "Customer" : "pacifyr";
            for (int i = mSessionDetailRoot.getSessionRatingList().size() - 1; i >= 0; i--) {
                MSessionRating rating = mSessionDetailRoot.getSessionRatingList().get(i);
                if (rating.getType().equalsIgnoreCase(type)) {
                    session_pos = i;
                    break;
                }
            }


            //   int session_pos=mSessionDetailRoot.getSessionRatingList().size()-1;
            if (session_pos >= 0)
                id = mSessionDetailRoot.getSessionRatingList().get(session_pos).getAfterSessionEmotionId();

        } catch (Exception e) {
            LogUtils.LOGD("session", "SSA getEmotionAfterId " + e.getMessage());
        }
        return id;
    }

    public String getEmotionCustomerAfterId() {
        String id = null;
        try {

            int session_pos = -1;
            String type = "Customer";
            for (int i = mSessionDetailRoot.getSessionRatingList().size() - 1; i >= 0; i--) {
                MSessionRating rating = mSessionDetailRoot.getSessionRatingList().get(i);
                if (rating.getType().equalsIgnoreCase(type)) {
                    session_pos = i;
                    break;
                }
            }


            //   int session_pos=mSessionDetailRoot.getSessionRatingList().size()-1;
            if (session_pos >= 0)
                id = mSessionDetailRoot.getSessionRatingList().get(session_pos).getAfterSessionEmotionId();

        } catch (Exception e) {
            LogUtils.LOGD("session", "SSA getEmotionCustomerAfterId " + e.getMessage());
        }
        return id;
    }

    public String getEmotionBeforeId() {
        String id = null;
        try {
            int session_pos = -1;
            // String type=isCustomer?"Customer":"pacifyr";
            String type = "Customer";
            for (int i = mSessionDetailRoot.getSessionRatingList().size() - 1; i >= 0; i--) {
                MSessionRating rating = mSessionDetailRoot.getSessionRatingList().get(i);
                if (rating.getType().equalsIgnoreCase(type)) {
                    session_pos = i;
                    break;
                }
            }
            //int session_pos=mSessionDetailRoot.getSessionRatingList().size()-1;
            if (session_pos >= 0)
                id = mSessionDetailRoot.getSessionRatingList().get(session_pos).getBeforeSessionEmotionId();

        } catch (Exception e) {
            LogUtils.LOGD("session", "SSA getEmotionBeforeId" + e.getMessage());
        }
        return id;
    }

    public void onSetValues() {
        try {
            LogUtils.LOGD("session", " SSA onSetValues ");
            if (isValid(mSessionDetailRoot)) {
                oldRating = 0;
                String callType = mSessionDetailRoot.getSessionType();
                durationCtxv.setText(mSessionDetailRoot.getDuration());
                if (isValid(mSessionDetailRoot.getStartTime()))
                    durationCtxv.setText(Utility.getCorrectRoundedDuration(mSessionDetailRoot.getDuration()) + " " + Utility.getStartFormatDate(mSessionDetailRoot.getStartTime(), getApplicationContext()));


                try {
                    /** implementation of "i" icon **/

                    if (mSessionDetailRoot.getSessionCost()>0
                            &&mSessionDetailRoot.getFreeWalletBalanceUsed()>0 && mSessionDetailRoot.getCreditCardCharges()>0)
                    {
                        ImageView_i_icon.setVisibility(View.VISIBLE);
                    }
                    if (mSessionDetailRoot.getSessionCost()>0
                            &&mSessionDetailRoot.getFreeMinutesUsed()>0 && mSessionDetailRoot.getCreditCardCharges()>0)
                    {
                        ImageView_i_icon.setVisibility(View.VISIBLE);
                    }

                    ImageView_i_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (mSessionDetailRoot.getSessionCost()>0
                                    &&mSessionDetailRoot.getFreeWalletBalanceUsed()>0 && mSessionDetailRoot.getCreditCardCharges()>0)
                            {
                                showIOSDialogOK("","$"+mSessionDetailRoot.getCreditCardCharges()+
                                        " from credit card and $"+mSessionDetailRoot.getFreeWalletBalanceUsed()+" from wallet","Ok");
                            }

                            if (mSessionDetailRoot.getSessionCost()>0
                                    &&mSessionDetailRoot.getFreeMinutesUsed()>0 && mSessionDetailRoot.getCreditCardCharges()>0)
                            {

                                showIOSDialogOK("",mSessionDetailRoot.getFreeMinutesUsed()+
                                        " minutes free session and $"+mSessionDetailRoot.getCreditCardCharges()+" from credit card","Ok");

                            }
     }
                    });
                }catch (Exception e)
                {

                }

                DecimalFormat precision = new DecimalFormat("0.00");

                if (isCustomer) {
                    if (isValid(mSessionDetailRoot.getTimeSlotBookingId()))
                        sessionCostCtxv.setText(Utility.getPriceSign(LastSessionActivity.this) + precision.format(mSessionDetailRoot.getSessionCost())+getResources().getString(R.string.prepaid));
                    else
                        sessionCostCtxv.setText(Utility.getPriceSign(LastSessionActivity.this) + precision.format(mSessionDetailRoot.getSessionCost()));

                    if (isValid(mSessionDetailRoot.getPacifyr())) {
                        String pacName = mSessionDetailRoot.getPacifyr().getFirstName() + " " + mSessionDetailRoot.getPacifyr().getLastName();
                        pacifyrNameCtxv.setText(pacName);
                        pacifyrRateCtxv.setText(Utility.getPriceSign(LastSessionActivity.this) + mSessionDetailRoot.getPacifyr().getPrice() + " " + Utility.getCostLabel(LastSessionActivity.this));
                        String imagePac = mSessionDetailRoot.getPacifyr().getImageUrl();
                        if (isValid(imagePac))
                            Picasso.with(LastSessionActivity.this).load(imagePac).placeholder(com.pacifyrapp.utilitylibrary.R.drawable.placeholder_user).into(pacifyrImgCimv);
                    }


                } else {
                    defSetText(earingstCtxv, Utility.getPriceSign(LastSessionActivity.this) + (mSessionDetailRoot.getPacifyrEarnings()));
                    defSetText(comapnyfeesCtxv, Utility.getPriceSign(LastSessionActivity.this) + (mSessionDetailRoot.getSessionCost() - mSessionDetailRoot.getPacifyrEarnings()));
                    if (isValid(mSessionDetailRoot.getUser())) {
                        String pacName = mSessionDetailRoot.getUser().getFirstName() + " " + mSessionDetailRoot.getUser().getLastName();
                        pacifyrNameCtxv.setText(pacName);
                        String imagePac = mSessionDetailRoot.getUser().getImageUrl();
                        if (isValid(imagePac))
                            Picasso.with(LastSessionActivity.this).load(imagePac).placeholder(com.pacifyrapp.utilitylibrary.R.drawable.placeholder_user).into(pacifyrImgCimv);
                    }
                }
                if (callType.equals("video")) {
                    callTypeDimv.setImageResource(R.drawable.ic_videocam);
                }

                if (isValid(mSessionDetailRoot.getSessionRatingList())) {

                    int session_pos = -1;
                    String type = isCustomer ? "Customer" : "pacifyr";
                    for (int i = mSessionDetailRoot.getSessionRatingList().size() - 1; i >= 0; i--) {
                        MSessionRating rating = mSessionDetailRoot.getSessionRatingList().get(i);
                        if (rating.getType().equalsIgnoreCase(type)) {
                            session_pos = i;
                            break;
                        }
                    }
                    if (session_pos >= 0) {

                        MSessionRating sessionRating = mSessionDetailRoot.getSessionRatingList().get(session_pos);

                        ratingId = sessionRating.getId();
                        oldRating = Integer.parseInt(sessionRating.getRating());
                        if (isValid(sessionRating.getEffectiveness())) {
                            effectiveness = Integer.parseInt(sessionRating.getEffectiveness());
                            effective_seekbar.setProgress(effectiveness);
                            circleProgressView.setProgress(effectiveness);
                            //effective_circle_view.setProgressValue(effectiveness);
                        }


                        ArrayList<String> cmtlist = sessionRating.getCommentList();
                        ArrayList<MComments> exist_cmt_list = new ArrayList<MComments>();
                        exist_cmt_list.add(new MComments("Excellent call"));
                        exist_cmt_list.add(new MComments("Good quality "));
                        exist_cmt_list.add(new MComments("Audio issues"));
                        exist_cmt_list.add(new MComments("Video issues"));
                        exist_cmt_list.add(new MComments("Other"));

                        if (isValid(cmtlist)) {
                            String cmt_edt = "";
                            for (int j = 0; j < cmtlist.size(); j++) {
                                switch (cmtlist.get(j)) {
                                    case "Excellent call":
                                        exist_cmt_list.get(0).isSelected = true;
                                        break;
                                    case "Good quality ":
                                        exist_cmt_list.get(1).isSelected = true;
                                        break;
                                    case "good quality ":
                                        exist_cmt_list.get(1).isSelected = true;
                                        break;
                                    case "Audio issues":
                                        exist_cmt_list.get(2).isSelected = true;
                                        break;
                                    case "Video issues":
                                        exist_cmt_list.get(3).isSelected = true;
                                        break;
                                    case "Other":
                                        exist_cmt_list.get(4).isSelected = true;
                                        break;
                                    default:
                                        exist_cmt_list.get(4).isSelected = true;
                                        cmt_edt = cmtlist.get(j);
                                        break;
                                }
                            }
                            if (isValid(cmt_edt)) {
                                comments_cedt.setText(cmt_edt);
                                comments_cedt.setVisibility(View.VISIBLE);

                                exist_cmt_list.get(4).isSelected = true;
                                exist_cmt_list.get(0).isSelected = false;
                                exist_cmt_list.get(1).isSelected = false;
                                exist_cmt_list.get(2).isSelected = false;
                                exist_cmt_list.get(3).isSelected = false;
                                commentsRcv.scrollToPosition(4);
                                commentsRcv.getLayoutManager().scrollToPosition(4);
                            }
                        }


                        commentsAdapter = new CommentsAdapter(LastSessionActivity.this, exist_cmt_list, click);
                        commentsAdapter.setEditable(editable);
                        commentsRcv.setAdapter(commentsAdapter);
                    } else {


                        commentsAdapter = new CommentsAdapter(LastSessionActivity.this, click);
                        commentsAdapter.setEditable(editable);
                        commentsRcv.setAdapter(commentsAdapter);

                    }

                } else {


                    commentsAdapter = new CommentsAdapter(LastSessionActivity.this, click);
                    commentsAdapter.setEditable(editable);
                    commentsRcv.setAdapter(commentsAdapter);

                }


                if (oldRating > 0) {
                    rating_bar.setRating(oldRating);
                } else {
                    rating_bar.setRating(1);
                }
            } else {


                commentsAdapter = new CommentsAdapter(LastSessionActivity.this, click);
                commentsAdapter.setEditable(editable);
                commentsRcv.setAdapter(commentsAdapter);

            }

            if (!editable)
            {
                if (isValid(comments_cedt))
                {
                    comments_cedt.setFocusable(false);
                    comments_cedt.setCursorVisible(false);
                    comments_cedt.setLongClickable(false);
                    comments_cedt.setClickable(false);
                    comments_cedt.setFocusable(false);
                    comments_cedt.setSelected(false);
                }
            }
        } catch (Exception e) {
            LogUtils.LOGD("session", "SSA onSetValues exce " + e.getMessage());
        }
    }

    @Override
    public void onNotifyEvent(NotifyEvent event) {

    }

    public int getEmotionList() {
        int i = -1;
        try {

            String emotionJson =
                    PrefManager.getInstance(LastSessionActivity.this).getSharedString(PrefManager.JSON_ALL_EMOTIONS, null);
            if (isValid(modelAllEmotions)) {
                emotions = modelAllEmotions.getResultsList();
                i = emotions.size();
            } else if (emotionJson != null) {
                JSONObject emJsonObject = new JSONObject(emotionJson);
                modelAllEmotions = GsonUtils.getInstance().gsonToMListAllEmotionsI(emJsonObject);
                if (modelAllEmotions != null) {
                    emotions = modelAllEmotions.getResultsList();
                    i = emotions.size();
                }
            }
        } catch (Exception e) {
            i = -1;
        }
        return i;
    }

    private void webGetEmotionList() {
        try {
            setLoading();
            showProgress();
            String url = GLOBAL_URL + "emotions/ordered";
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    try {
                        hideProgress();
                        modelAllEmotions = GsonUtils.getInstance().gsonToMListAllEmotionsI(json);
                        setAdapter();
                        // postEventOnMainThread(new NotifyEvent(true, false));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            AQuery aq = new AQuery(LastSessionActivity.this);
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", getAccessToken());
            aq.ajax(url, JSONObject.class, cb);
        } catch (Exception e) {
            hideProgress();
            e.printStackTrace();
        }
    }


    public void webGetSessionDetail(final String sessionid) {
        try {
            setLoading();
            showProgress();
            if (!isValid(sessionid, "Network Interupted")) {
                // finish();
                return;
            }
            AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    hideProgress();
                    LogUtils.LOGD("session", " SSA webGetSessionDetail callback result" + json + " " + status.getMessage());
                    if (!isValid(json, "Network Interupted")) {
                        //finish();
                        handlerFinish("Network Interupted,please check later");
                    } else {
                        try {
                            mSessionDetailRoot = GsonUtils.getInstance().gsonToMSessionDetailRoot(json);
                            onSetValues();
                            if (getEmotionList() < 0) {
                                webGetEmotionList();
                            } else
                                setAdapter();
                        } catch (Exception e) {
                            //finish();
                            LogUtils.LOGD("session", " SSA webGetSessionDetail callback exce" + e.getMessage());
                        }
                    }
                }
            };
            AQuery aq = new AQuery(this);
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", getAccessToken());
            String url = "session/" + sessionid;
            aq.ajax(GLOBAL_URL + url, JSONObject.class, cb);
        } catch (Exception e) {
            hideProgress();
            LogUtils.LOGD("session", "SSA webGetSessionDetail exce " + e.getMessage());
            //finish();
        }
    }


    public void defSetText(TextView textv, String text) {
        if (isValid(textv))
            textv.setText(defString(text));

    }

    private MSessionRating getMSessionRating() {
        MSessionRating mSessionRating = new MSessionRating();
        if (isValid(ratingId))
            mSessionRating.setId(ratingId);

        mSessionRating.setUserId(getUserID());
        mSessionRating.setSessionId(mSessionId);
        mSessionRating.setEffectiveness(effectiveness + "");
        if (isCustomer)
            mSessionRating.setRating(rating + "");
        ArrayList<String> comments = new ArrayList<String>();
        try {
            comments = commentsAdapter.getSelectedList();

            if (comments_cedt.isShown()) {
                String comment = comments_cedt.getText() + "";
                if (isValid(comment)) {
                    comments = new ArrayList<>();
                    comments.add(comment);
                }

            }
        } catch (Exception e) {
            LogUtils.LOGD("session", "SSA getMSessionRating list exce " + e.getMessage());
        }

        if (isValid(comments))
            mSessionRating.setComments(comments);
        if (getPackageName().equalsIgnoreCase("com.pacifyr.pam"))
        {
            mSessionRating.setBeforeSessionEmotionId("");
            mSessionRating.setAfterSessionEmotionId("");
        }
        else
        {
            if (isValid(emotionsListAdapter))
                if (isValid(emotionsListAdapter.getSelected()))
                    mSessionRating.setAfterSessionEmotionId(emotionsListAdapter.getSelected().getId() + "");

            //     if(before_feel_llay.isShown())
            if (isValid(before_emotion))
                mSessionRating.setBeforeSessionEmotionId(before_emotion.getId() + "");
            // qscreate.setDeviceType("android");
        }
        return mSessionRating;

    }

    private void saveSession() {

        try {

            if (!isValid(getAccessToken(), "Token Invalid , Please Re-Login"))
                return;
            if (!isValid(mSessionId, "Session Error , Please Re-Login "))
                return;
            if (!isValid(getUserID(), "User Session Failed, Please Re-Login"))
                return;
            if (afterFeelSelectionCtxv.isShown()) {
                if (!isValid(emotionsListAdapter, getString(R.string.after_call_emotion_err)))
                    return;
                if (!isValid(emotionsListAdapter.getSelectedIndex() >= 0, getString(R.string.after_call_emotion_err)))
                    return;
                if (!isValid(emotionsListAdapter.getSelected(), getString(R.string.after_call_emotion_err)))
                    return;
            }

            if (effectiveness==0)
            {
                setAlertOKStatusInWhite("",getResources().getString(R.string.how_effective_error),"Ok");
                return;
            }

            MSessionRating mSessionRating = getMSessionRating();
            if (!isValid(mSessionRating, "Error , Please Re-Login"))
                return;

            setLoading("Saving...");
            showProgress();

            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    try {

                        if (json != null) {
                            Log.d(TAG, "Save seeion resspomse " + json);
                        }
                        if (isCustomer && json != null && status != null && status.getCode() == 200) {
                            MetaEventLogger.logRate(LastSessionActivity.this,
                                    MetaEventLogger.CONTENT_TYPE_SESSION, rating);
                        }
                        App.getInstance().setCurrentActivity(null);
                        NetworkService.startActionCutomerDashboard(LastSessionActivity.this);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                hideProgress();
                                finish();
                            }
                        }, 2000);


                    } catch (Exception e) {
                        LogUtils.LOGD("session", "SSA savePacifyerSession  callback  exce " + e.getMessage());
                    }
                }
            };

            AQuery aq = new AQuery(LastSessionActivity.this);
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", getAccessToken());
            Gson gson = new Gson();
            String jsonInString = gson.toJson(mSessionRating);
            String url = GLOBAL_URL + "rating/customer/session";
            if (isCustomer)
                url = GLOBAL_URL + "rating/customer/session";
            else
                url = GLOBAL_URL + "rating/pacifyr/session";
            JSONObject jsonObject = new JSONObject(jsonInString);
            aq.post(url, jsonObject, JSONObject.class, cb);


        } catch (Exception e) {
            LogUtils.LOGD("session", "SSA savePacifyerSession  exce " + e.getMessage());
        }

    }

    CommentClick click = new CommentClick() {
        @Override
        public void CommentCliked(MComments mComments) {
            if (isValid(comments_cedt)) {
                if (!mComments.item.equalsIgnoreCase("other"))
                    comments_cedt.setVisibility(View.GONE);
                else
                    comments_cedt.setVisibility(mComments.isSelected ? View.VISIBLE : View.GONE);
                if (!comments_cedt.isShown()) {
                    comments_cedt.setText("");
                }
            }
        }
    };

    public MEmotion before_emotion = null, after_emotion = null;

    public void setAdapter() {
        try {
            LogUtils.LOGD("session", "SSA setAdapter ");

            if (isValid(mSessionDetailRoot)) {
                if (isValid(modelAllEmotions)) {
                    int postion = 0;
                    emotions = modelAllEmotions.getResultsList();


                    try {
                        for (int i = 0; i < emotions.size(); i++) {
                            emotions.get(i).setClicked(false);
                        }
                        if (isValid(getEmotionAfterId())) {

                            for (int i = 0; i < emotions.size(); i++) {

                                if (emotions.get(i).getId().equalsIgnoreCase(getEmotionAfterId())) {
                                    postion = i;
                                    emotions.get(i).setClicked(true);
                                    break;
                                }

                            }
                        }
                    } catch (Exception e) {
                        LogUtils.LOGD("session", "SSA setAdapter " + e.getMessage());
                    }

                    emotionsListAdapter = new EmotionsListAdapter(emotions, LastSessionActivity.this);
                    emotionsListAdapter.setclickable(editable);
                    //postiveEmotions.setClickListener(this);
                    recyclerView.setAdapter(emotionsListAdapter);
                    recyclerView.scrollToPosition(postion);
                    // recyclerView.getLayoutManager().scrollToPositionWithOffset(2, 20);
                    recyclerView.getLayoutManager().scrollToPosition(postion);
                    LogUtils.LOGD("session", "LSA scrollto postion " + postion);
                    try {


                        //     if (!before_feel_llay.isShown()) {
                        before_emotion = getEmotion(emotions);
                        if (!getPackageName().equalsIgnoreCase("com.pacifyr.pam")) {
                            setBeforeEmotionViews(before_emotion);
                        }
                   /* if(isValid(before_emotion)) {
                        before_feel_llay.setVisibility(View.VISIBLE);
                        // call_session_detail_root.getSession().getUser().getEmotion().getImage()
                        String beforeImgUrl = GLOBAL_IMAGE_URL + before_emotion.getImage();
                        if(isValid(beforeImgUrl)) Picasso.with(LastSessionActivity.this).load(beforeImgUrl).placeholder(R.drawable.sad).into(beforeFeelDimv);
                    } else {
                        before_feel_llay.setVisibility(View.GONE);
                    }*/

                        if (isPacifyer()) {
                            after_emotion = getAfterEmotion(emotions);
                            if (!getPackageName().equalsIgnoreCase("com.pacifyr.pam")) {
                                setAfterEmotionViews(after_emotion);
                            }

                    /*    if(isValid(after_emotion)) {
                            after_feel_llay.setVisibility(View.VISIBLE);
                            String afterImgUrl = GLOBAL_IMAGE_URL + after_emotion.getImage();
                            if(isValid(afterImgUrl))
                                Picasso.with(LastSessionActivity.this).load(afterImgUrl).placeholder(R.drawable.sad).into(afterFeelDimv);
                        } else {
                            after_feel_llay.setVisibility(View.GONE);
                        }*/

                        }

                    } catch (Exception e) {
                        LogUtils.LOGD("session", "SSA setAdapter exce " + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            LogUtils.LOGD("session", "SSA setAdapter exce " + e.getMessage());
        }
    }

    private MEmotion getEmotion(ArrayList<MEmotion> emotions) {
        MEmotion mEmotion = null;
        String emotionid = null;
        try {

            emotionid = getEmotionBeforeId();
            LogUtils.LOGD("session", "SSA getEmotion  emotionid" + emotionid);
            for (MEmotion item : emotions) {
                if (item.getId().equalsIgnoreCase(emotionid)) {
                    mEmotion = item;
                    break;
                }
            }
        } catch (Exception e) {
            LogUtils.LOGD("session", "SSA getEmotion exce " + e.getMessage());
        }
        return mEmotion;
    }

    private MEmotion getAfterEmotion(ArrayList<MEmotion> emotions) {
        MEmotion mEmotion = null;
        String emotionid = null;
        try {

            emotionid = getEmotionCustomerAfterId();
            LogUtils.LOGD("session", "SSA getAfterEmotion  emotionid" + emotionid);
            for (MEmotion item : emotions) {
                if (item.getId().equalsIgnoreCase(emotionid)) {
                    mEmotion = item;
                    break;
                }
            }
        } catch (Exception e) {
            LogUtils.LOGD("session", "SSA getAfterEmotion exce " + e.getMessage());
        }
        return mEmotion;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ratingId = null;
    }

    private LinearLayout actionBarTopLlay;
    private RelativeLayout bottomPanel;
    private LinearLayout leftLlay;
    private DeviceFitImageView backImv;
    private MainTextView leftTxv;
    private MainTextView titleTxv;
    private LinearLayout rightLlay;
    private MainTextView rightTxv;
    private DeviceFitImageView rightImv;
    MainTextView submit_ctxv;

    public void onSetActionBar() {
        actionBarTopLlay = findViewById(R.id.action_bar_top_llay);
        bottomPanel = findViewById(R.id.bottom_panel);
        leftLlay = findViewById(R.id.left_llay);
        backImv = findViewById(R.id.back_imv);
        leftTxv = findViewById(R.id.left_txv);
        titleTxv = findViewById(R.id.title_txv);
        rightLlay = findViewById(R.id.right_llay);
        rightTxv = findViewById(R.id.right_txv);
        rightImv = findViewById(R.id.right_imv);
        submit_ctxv = findViewById(R.id.submit_ctxv);
        mSlidingPaneLayout = findViewById(R.id.sliding_pane);
        rightImv.setVisibility(View.INVISIBLE);
        rightTxv.setVisibility(View.VISIBLE);
        rightLlay.setVisibility(View.VISIBLE);
        leftTxv.setVisibility(View.INVISIBLE);
        backImv.setVisibility(View.VISIBLE);
        titleTxv.setVisibility(View.VISIBLE);
        leftLlay.setVisibility(View.VISIBLE);
        defSetText(titleTxv, "CALL SUMMARY");
        defSetText(rightTxv, "Submit");
        rightTxv.setTextColor(getResources().getColor(R.color._color_action_bar_text_color));
        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/ActionBarCommonFont.otf");
        titleTxv.setTypeface(type);

        rightTxv.setBackground(getResources().getDrawable(R.drawable.blue_border_lightblue));
        // backImv.setImageDrawable(getResources().getDrawable(R.drawable.back2x));
        leftLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        if (!editable) {
            rightLlay.setVisibility(View.GONE);
            submit_ctxv.setVisibility(View.GONE);
        }

        rightLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editable) {
                    saveSession();
                } else {
                    Toast.makeText(LastSessionActivity.this, "You cannot submit this session now",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        submit_ctxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editable) {
                    saveSession();
                } else {
                    Toast.makeText(LastSessionActivity.this, "You cannot submit this session now",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        //backImv.setBackgroundResource(R.drawable.leftmen);

      /*  leftLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showingFirst == true){
                    mSlidingPaneLayout.openPane();
                    showingFirst = false;
                }else{
                    mSlidingPaneLayout.closePane();
                    showingFirst = true;
                }
            }
        });*/
    }


    @Subscribe
    public void onEvent(EventLoadEmotion event) {
        try {
            if (event.isLoaded()) {
                setAdapter();
            }

        } catch (Exception e) {
            LogUtils.LOGD("session", "SSA onNotifyEvent exce " + e.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }

    @Override
    public void onAlertTypeEvent(AlertTypeEvent event) {

    }


    @org.greenrobot.eventbus.Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetworkChanged(InternetConnectionEvent event) {
        try {
            if (isValid(event) && isShowDialogs()) {
                if (!event.isConnected())
                {
                    setAlertStatusInWhiteNoInternet("No Internet connection!", "Please turn on internet connection to continue.", "Exit", "Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            hideProgress();
                            hideStatus2();
                            finishAffinity();                     }
                    });
                }
                else
                {
                    hideProgress();
                    hideStatus2();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(notificationReceiver);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

    }
}

