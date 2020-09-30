package com.pacifyr.client.home;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PorterDuff;
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

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.eralp.circleprogressview.CircleProgressView;
import com.google.gson.Gson;
import com.pacifyr.client.R;
import com.pacifyr.client.activity.PrescriptionViewActivity;
import com.pacifyr.client.adapter.CommentsAdapter;
import com.pacifyr.client.adapter.EmotionsListAdapter;
import com.pacifyr.client.base.PacifyrActivity;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.event.AlertTypeEvent;
import com.pacifyr.client.event.EventLoadEmotion;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.model.MComments;
import com.pacifyr.client.model.PrescriptionList;
import com.pacifyr.client.model.interfaces.CommentClick;
import com.pacifyr.client.network.GsonUtils;
import com.pacifyr.client.network.NetworkService;
import com.pacifyr.client.quickblox.App;
import com.pacifyr.client.quickblox.utils.Consts;
import com.pacifyr.client.utility.PacifyrAppCompactActivity;
import com.pacifyr.client.utils.FirebaseLogging;
import com.pacifyr.client.utils.LogUtils;
import com.pacifyr.client.utils.Utility;
import com.pacifyr.client.utils.WrappingSlidingPaneLayout;
import com.pacifyrapp.utilitylibrary.CustomEditText;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;
import com.pacifyrapp.utilitylibrary.ToastHandler;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MEmotion;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MSessionDetailRoot;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MSessionRating;
import com.pacifyrapp.utilitylibrary.network.UtilityNetworkService;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;
import com.willy.ratingbar.BaseRatingBar;
import com.willy.ratingbar.ScaleRatingBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.minimize.com.seek_bar_compat.SeekBarCompat;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.pacifyr.client.network.NetworkService.GLOBAL_URL;
import static com.pacifyr.client.network.NetworkService.modelAllEmotions;
import static com.pacifyr.client.network.NetworkService.modelListPosEmotions;
import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.GLOBAL_IMAGE_URL;

/**
 * Created by Mani-Ceino on 12/14/2017.
 */

public class SessionSummaryActivity extends PacifyrAppCompactActivity {

    private static final String TAG = SessionSummaryActivity.class.getSimpleName();
    private static boolean isEditable;
    boolean showingFirst = true;
    //EditableSeekBar esbExample;
    //CircleView effective_circle_view;

    CircleProgressView circleProgressView;

    EmotionsListAdapter emotionsListAdapter;
    RecyclerView recyclerView;

    RecyclerView commentsRcv;
    CommentsAdapter commentsAdapter;

    WrappingSlidingPaneLayout mSlidingPaneLayout;
    boolean onceBackpressed = false;
    private int rating = 5;


    private int effectiveness = 0;

    private MainTextView before_emotion_ctxv;


    ArrayList<MEmotion> emotions;
    private CircleImageView pacifyrImgCimv;
    private MainTextView pacifyrNameCtxv, callback_ctxv;
    private ImageView beforeFeelDimv;
    private MainTextView durationCtxv;
    private MainTextView pacifyrRateCtxv, before_feel_ctxv;
    private MainTextView sessionCostCtxv;
    private int oldRating = 0;

    public MSessionDetailRoot mSessionDetailRoot;
    public SeekBarCompat effective_seekbar;
    public ScaleRatingBar rating_bar;
    /// public  RatingBar rating_bar;
    CustomEditText comments_cedt;
    public boolean isCustomer = false;

    public static boolean isAudioCall = true;
    CircleImageView img_blue_bg;


    private MainTextView afterFeelSelectionCtxv;
    private LinearLayout pacifyrEaringsLlay, customer_fees_llay;
    private MainTextView earingstCtxv, comapnyfeesCtxv;

    //public LinearLayout rating_llay,before_feel_llay;
    //  private LinearLayout afterFeelSelectionLlay;
    //private LinearLayout  after_feel_llay;

    // private RelativeLayout  after_feel_llay;
    private RelativeLayout afterFeelSelectionLlay;
    public RelativeLayout rating_llay, before_feel_llay;

    private RelativeLayout notes_Layout;
    private CustomEditText etv_notes;
    private String mSessionId;
    private  TextView mTextView_View_pres;

    /*public static void start(Context context, boolean isAudioCall) {
        Intent intent = new Intent(context, SessionSummaryActivity.class);
        //
        // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        //  intent.putExtra(Consts.EXTRA_IS_STARTED_FOR_CALL, isRunForCall);
        SessionSummaryActivity.isAudioCall = isAudioCall;
        isEditable = true;
       *//* try {
            Activity activity = null;
            activity = ((AppChatNotUSe) AppChatNotUSe.getInstance()).getCurrentActivity();
            if(isValidStatic(activity))
                if (activity instanceof CallActivity ) {
                    activity.finish();
                }
        } catch (Exception e) {
            e.printStackTrace();
        }*//*
//


        context.startActivity(intent);
    }*/

    public static Boolean isValidStatic(Object object) {
        return object != null;

    }

    /*public static void start(Context context) {
        Intent intent = new Intent(context, SessionSummaryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // intent.putExtra(Consts.EXTRA_IS_STARTED_FOR_CALL, isRunForCall);
        SessionSummaryActivity.isAudioCall = true;
        isEditable = true;

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
    }
*/
    public static void start(Context context, String sId) {
        if (!isValidStatic(sId)) {
            Log.e(TAG, "Invalid session Id. Cannot start session summary activity");
            return;
        }
        Intent intent = new Intent(context, SessionSummaryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        isEditable = false;
        SessionSummaryActivity.isAudioCall = true;
        sessionid = sId;
        intent.putExtra("id", sId);
        context.startActivity(intent);
    }

    private ImageButton img_notes;
    private ImageView callTypeDimv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.LOGD("session", " SSA onCreate sessionid " + sessionid);
        setContentView(R.layout.activity_session_summary);

        /*this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
*/

        img_notes = findViewById(R.id.img_notes);
        img_notes.setVisibility(View.GONE);
        img_blue_bg = findViewById(R.id.img_blue_bg);
        callTypeDimv = findViewById(R.id.call_type_dimv);
        pacifyrImgCimv = findViewById(R.id.last_session_cimv);
        before_feel_ctxv = findViewById(R.id.before_feel_ctxv);
          /*  after_feel_ctxv = (MainTextView) findViewById(R.id.after_feel_ctxv);
            after_feel_llay = (LinearLayout) findViewById(R.id.after_feel_llay);*/

          /*    rating_llay = (LinearLayout) findViewById(R.id.rating_llay);
            before_feel_llay = (LinearLayout) findViewById(R.id.before_feel_llay);
            after_feel_llay = (LinearLayout) findViewById(R.id.after_feel_llay);
            afterFeelSelectionLlay = (LinearLayout) findViewById(R.id.after_feel_selection_llay);*/

        rating_llay = findViewById(R.id.rating_llay);
        before_feel_llay = findViewById(R.id.before_feel_llay);
        //  after_feel_llay = (RelativeLayout) findViewById(R.id.after_feel_llay);
        afterFeelSelectionLlay = findViewById(R.id.after_feel_selection_llay);

        afterFeelSelectionCtxv = findViewById(R.id.after_feel_selection_ctxv);
        pacifyrEaringsLlay = findViewById(R.id.pacifyr_earings_llay);
        earingstCtxv = findViewById(R.id.earingst_ctxv);
        comapnyfeesCtxv = findViewById(R.id.comapnyfees_ctxv);
        customer_fees_llay = findViewById(R.id.customer_fees_llay);

        before_emotion_ctxv = findViewById(R.id.before_emotion_ctxv);
        callback_ctxv = findViewById(R.id.callback_ctxv);
        sessionCostCtxv = findViewById(R.id.session_cost_ctxv);
        pacifyrNameCtxv = findViewById(R.id.pacifyr_name_ctxv);
        beforeFeelDimv = findViewById(R.id.before_feel_dimv);
        recyclerView = findViewById(R.id.begning_rcv);
        commentsRcv = findViewById(R.id.comments_rcv);
        pacifyrRateCtxv = findViewById(R.id.pacifyr_rate_ctxv);
        durationCtxv = findViewById(R.id.duration_ctxv);
        comments_cedt = findViewById(R.id.comments_cedt);

        rating_bar = findViewById(R.id.rating_bar);
        //rating_bar = (RatingBar) findViewById(R.id.rating_bar);
        effective_seekbar = findViewById(R.id.effective_seekbar);
        //holoCircleSeekBar=(HoloCircleSeekBar) findViewById(R.id.picker);
        //effective_circle_view = (CircleView) findViewById(R.id.effective_circle_view);
        circleProgressView = findViewById(R.id.circle_progress_view);


        if (!isCustomer() && !isPacifyer()) {
            ToastHandler.newInstance(this).mustShowToast("Session Expired : Please login again");
            deleteInstallationWeb();
            finish();
        }
        isCustomer = isCustomer();
        if (isValid(getIntent().getExtras().getString("id"))) {
            mSessionId = getIntent().getExtras().getString("id") + "";
            if (isValid(mSessionId)) {
                sessionid = mSessionId;
            }
        }

        if (!isValid(mSessionId, "Session id null "))
            handlerFinish("Network Interrupted");

        // webListEmotions(SessionSummaryActivity.this);

        NetworkService.webListPositiveEmotions(this);
        NetworkService.webListNegativeEmotions(this);
        onCreateView();
        if (isValid(mSessionId)) {
            webGetSessionDetail(mSessionId);
            getPrefManager().saveSessionId(null);
        } else
            handlerFinish("Network Interrupted");
        //  finish();


        comments_cedt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showKeyboard();
            }
        });




        if (getPackageName().equalsIgnoreCase("com.pacifyr.pam"))
        {
            afterFeelSelectionLlay.setVisibility(View.GONE);
            before_feel_llay.setVisibility(View.GONE);
        }
        mTextView_View_pres = findViewById(R.id.mTextView_View_pres);
        mTextView_View_pres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValid(mSessionDetailRoot.getPrescriptionFile()))
                    loadPDF(mSessionDetailRoot.getPrescriptionFile());
                else
                    callPrescriptionData();
            }
        });
    }

    private void callPrescriptionData() {

        try {
            setLoading();
            showProgress();
            PrefManager mPrefManager = PrefManager.getInstance(SessionSummaryActivity.this);
            String token = mPrefManager.getAccessToken();
            String url = UtilityNetworkService.GLOBAL_URL + "prescription/list/user/"+mPrefManager.getUserID();

            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    try {
                        hideProgress();
                        hideStatus();
                        Log.w("@@@",String.valueOf(json));
                        if (isValid(json))
                        {
                            PrescriptionList mPrescriptionList = GsonUtils.getInstance().gsonToPrescriptionList(json);
                            if (isValidSize(mPrescriptionList.getResults())) {

                                if (isValid(mPrescriptionList.getResults().get(0)) && isValid(mPrescriptionList.getResults().get(0).getText()))
                                {
                                    if (mPrescriptionList.getResults().get(0).getSessionId().equals(sessionid))
                                    {
                                        String mPrescriptionFile=mPrescriptionList.getResults().get(0).getText();
                                        if(isValid(mPrescriptionFile))
                                        {
                                           Intent intent =  new Intent(SessionSummaryActivity.this, PrescriptionViewActivity.class);
                                           intent.putExtra("text",mPrescriptionFile);
                                           intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                           startActivity(intent);

                                        }
                                        else
                                        {
                                            Toast.makeText(SessionSummaryActivity.this, getResources().getString(R.string.no_prescription), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else
                                    {
                                        Toast.makeText(SessionSummaryActivity.this, getResources().getString(R.string.no_prescription), Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else
                                {
                                    Toast.makeText(SessionSummaryActivity.this, getResources().getString(R.string.no_prescription), Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(SessionSummaryActivity.this, getResources().getString(R.string.no_prescription), Toast.LENGTH_SHORT).show();
                            }

                        }
                        else
                        {
                            Toast.makeText(SessionSummaryActivity.this, getResources().getString(R.string.no_prescription), Toast.LENGTH_SHORT).show();

                        }


                    } catch (Exception e) {
                        hideProgress();
                        e.printStackTrace();
                    }
                }
            };
            AQuery aq = new AQuery(SessionSummaryActivity.this);
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", token);
            aq.ajax(url, JSONObject.class, cb);
        } catch (Exception ex) {
            hideProgress();
            ex.printStackTrace();
            Log.w("@@@",String.valueOf(ex));
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sessionid = null;
        PacifyrActivity.call_emotion = null;
    }

    @Override
    protected void onResume() {
        super.onResume();

        App.getInstance().setCurrentActivity(this);

        Bundle args =
                FirebaseLogging.getConstantArgsLoggedIn(SessionSummaryActivity.this);

        args.putString(FirebaseLogging.EC, SessionSummaryActivity.class.getSimpleName());

        FirebaseLogging.sendLog(SessionSummaryActivity.this,
                FirebaseLogging.EVENT_ON_SCREEN,
                args);
    }

    @Override
    protected View getSnackbarAnchorView() {
        return null;
    }



   /* @Override
    public void onSetValues(View view) throws Exception {

    }*/


    public void onCreateView() {
        try {
            before_emotion = null;
            LogUtils.LOGD("session", " SSA onCreateView ");
            onSetActionBar();



            effective_seekbar.setProgress(effectiveness);
            circleProgressView.setProgress(effectiveness);
           /* effective_circle_view.setProgressValue(effectiveness);
            effective_circle_view.setSuffixValue("%");*/

            recyclerView.setLayoutManager(new LinearLayoutManager(SessionSummaryActivity.this, LinearLayoutManager.HORIZONTAL, false));
            commentsRcv.setLayoutManager(new LinearLayoutManager(SessionSummaryActivity.this, LinearLayoutManager.HORIZONTAL, false));
            commentsAdapter = new CommentsAdapter(SessionSummaryActivity.this, click, true);
            commentsRcv.setAdapter(commentsAdapter);
            LogUtils.LOGD("session", " SSA onCreateView  before set rating view");
            setRatingViews();


            callback_ctxv.setVisibility(View.GONE);
            if (isCustomer) {

         /*       before_feel_ctxv.setText("How were you feeling before the call?");
                afterFeelSelectionCtxv.setText("How are you feeling now?");*/

                pacifyrEaringsLlay.setVisibility(View.GONE);
                customer_fees_llay.setVisibility(View.VISIBLE);
                pacifyrRateCtxv.setVisibility(View.VISIBLE);
                rating_llay.setVisibility(View.VISIBLE);
                before_feel_ctxv.setText(getString(R.string.your_feeling_before_call));
                // afterFeelSelectionCtxv.setText("How were you feeling after the call ?");
                afterFeelSelectionCtxv.setText(getString(R.string.your_feeling_now));


               /* if (isValid(ProfileScreenActivity.mpacifyerDetial.getQbid()))
                    callback_ctxv.setVisibility(View.VISIBLE);*/
                callback_ctxv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            if (checker.lacksPermissions(Consts.PERMISSIONS)) {
                                startPermissionsActivity(false);
                                if (!checker.lacksPermissions(Consts.PERMISSIONS)) {
                                    if (isValid(ProfileScreenActivity.mpacifyerDetial.getQbid()))
                                        if (isLoggedInChat()) {
                                            callRequestWeb(ProfileScreenActivity.mpacifyerDetial, listner, true);
                                        }
                                    //ToastHandler.newInstance(ConnectToPacifyrActivity.this).mustShowToast("These Permissions are required to make call");
                                }
                            } else {
                                if (isValid(ProfileScreenActivity.mpacifyerDetial.getQbid()))
                                    if (isLoggedInChat()) {

                                        callRequestWeb(ProfileScreenActivity.mpacifyerDetial, listner, true);

                                        //startCall(true,qbID);
                                    }
                            }
                          /*  if (isValid(ProfileScreenActivity.mpacifyerDetial.getQbid()))

                                if (isLoggedInChat()) {
                                    callRequestWeb(ProfileScreenActivity.mpacifyerDetial,listner, true);
                                    //startSessionWeb(ProfileScreenActivity.mpacifyerDetial, true);
                                    //startCall(true,qbID);
                                }*/
                        } catch (Exception e) {
                            ToastHandler.newInstance(SessionSummaryActivity.this).mustShowToast("Please try later");
                            finish();
                        }
                    }
                });

            } else {
                rating_llay.setVisibility(View.GONE);
                callback_ctxv.setVisibility(View.GONE);
                /*//afterFeelSelectionCtxv.setText("How were customer feeling now?");
                before_feel_ctxv.setText("How were customer feeling before the call?");*/

                pacifyrEaringsLlay.setVisibility(View.VISIBLE);
                pacifyrRateCtxv.setVisibility(View.GONE);
                customer_fees_llay.setVisibility(View.GONE);
                callback_ctxv.setVisibility(View.GONE);
                before_feel_ctxv.setText(getString(R.string.client_feeling_before_call));
              /*  after_feel_ctxv.setText("How was the  customer feeling after the call ?");
                after_feel_llay.setVisibility(View.VISIBLE);*/
                afterFeelSelectionCtxv.setText(getString(R.string.client_feeling_after_call_opinion));
            }


            if (isValid(mSessionDetailRoot)) {
                onSetValues();
                setAdapter();
            }

        } catch (Exception e) {
            LogUtils.LOGD("session", "SSA onCreateView exce " + e.getMessage());
        }
    }

    private void setRatingViews() {
        try {
            rating_bar.setNumStars(5);
            rating = 5;
          /*  rating_bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                    rating = (int) ratingBar.getRating();
                }
            });*/
            rating_bar.setStarPadding(10);
            rating_bar.setClickable(true);
            rating_bar.setClearRatingEnabled(true);
            rating_bar.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
                @Override
                public void onRatingChange(BaseRatingBar baseRatingBar, float v, boolean fromUser) {
                    //Log.e(TAG, "onRatingChange: " + rating);
                    rating = (int) baseRatingBar.getRating();
                }
            });

            //    effective_circle_view.setSuffixEnabled(true);
            // effective_circle_view.setSuffixValue("%");
            //  effective_circle_view.setProgressStepAsInteger(true);
            effective_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    //effective_circle_view.setProgressValue(progress);
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
        } catch (Exception e) {
            LogUtils.LOGD("session", "SSA setRatingViews exce " + e.getMessage());
        }
    }

    MEmotion before_emotion = null;

    private void setBeforeEmotionViews(MEmotion mEmotion) {
        try {
            if (isValid(mEmotion)) {
                before_emotion = mEmotion;
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
                    Picasso.with(SessionSummaryActivity.this).load(beforeImgUrl).placeholder(R.drawable.sad).into(beforeFeelDimv);
                img_blue_bg.setVisibility(View.VISIBLE);
                Resources res = SessionSummaryActivity.this.getResources();
                final int nColor = res.getColor(R.color.white);
                beforeFeelDimv.setColorFilter(nColor, PorterDuff.Mode.SRC_ATOP);
            } else {
                before_feel_llay.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            LogUtils.LOGD("session", "SSA setBeforeEmotionViews exce " + e.getMessage());
        }
    }

    public void onSetValues() {
        try {
            LogUtils.LOGD("session", " SSA onSetValues ");
            if (isValid(mSessionDetailRoot)) {


                oldRating = 0;
                String callType = mSessionDetailRoot.getSessionType();

                durationCtxv.setText(mSessionDetailRoot.getDuration());
                if (isValid(mSessionDetailRoot.getEndTime()))
                    durationCtxv.setText(mSessionDetailRoot.getDuration() + " " + Utility.getStartFormatDate(mSessionDetailRoot.getEndTime(), getApplicationContext()));

                if (isCustomer) {
                    sessionCostCtxv.setText(Utility.getPriceSign(SessionSummaryActivity.this) + mSessionDetailRoot.getSessionCost());
                    if (isValid(mSessionDetailRoot.getPacifyr())) {
                        String pacName = mSessionDetailRoot.getPacifyr().getFirstName() + " " + mSessionDetailRoot.getPacifyr().getLastName();
                        pacifyrNameCtxv.setText(pacName);

                        pacifyrRateCtxv.setText(Utility.getPriceSign(SessionSummaryActivity.this) + mSessionDetailRoot.getPacifyr().getPrice() + " " + Utility.getCostLabel(SessionSummaryActivity.this));
                        String imagePac = mSessionDetailRoot.getPacifyr().getImageUrl();
                        if (isValid(imagePac))
                            Picasso.with(SessionSummaryActivity.this).load(imagePac).placeholder(R.drawable.placeholder_user).into(pacifyrImgCimv);

                    }


                    /*if (isValid(mSessionDetailRoot.get.getNotes()))
                    {
                        notes_Layout.setVisibility(View.VISIBLE);
                        etv_notes.setText(sessionRating.getNotes());
                    }
                    else
                    {
                        notes_Layout.setVisibility(View.GONE);
                    }
*/
                } else {
                    defSetText(earingstCtxv, Utility.getPriceSign(SessionSummaryActivity.this) + (mSessionDetailRoot.getPacifyrEarnings()));
                    defSetText(comapnyfeesCtxv, Utility.getPriceSign(SessionSummaryActivity.this) + (mSessionDetailRoot.getSessionCost() - mSessionDetailRoot.getPacifyrEarnings()));
                    if (isValid(mSessionDetailRoot.getUser())) {
                        String pacName = mSessionDetailRoot.getUser().getFirstName() + " " + mSessionDetailRoot.getUser().getLastName();
                        pacifyrNameCtxv.setText(pacName);
                        String imagePac = mSessionDetailRoot.getUser().getImageUrl();
                        if (isValid(imagePac))
                            Picasso.with(SessionSummaryActivity.this).load(imagePac).placeholder(R.drawable.placeholder_user).into(pacifyrImgCimv);
                    }
                }

                if (callType.equals("video")) {
                    callTypeDimv.setImageResource(R.drawable.ic_videocam);
                }

                try {
                    MEmotion emotion = getEmotion();
                    LogUtils.LOGD("session", "SSA  onSetValues getEmotion() emotion " + emotion);

                    getUserProfile(getUserID(), new AjaxCallback<JSONObject>() {
                        @Override
                        public void callback(String url, JSONObject json, AjaxStatus status) {
                            hideProgress();
                            try {
                                if (json != null) {
                                    NetworkService.mCurrentUser = GsonUtils.getInstance().gsonToModelUserProfile(json);
                                    //ProfileScreenActivity.pacifyerDetial = GsonUtils.getInstance().gsonToModelUserProfile(json);
                                    if (NetworkService.mCurrentUser != null && NetworkService.mCurrentUser.getEmotionId() != null) {
                                        if (!getPackageName().equalsIgnoreCase("com.pacifyr.pam")) {
                                            MEmotion emo = getEmotion(NetworkService.mCurrentUser.getEmotionId());
                                            setBeforeEmotionViews(emo);
                                        }
                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });


                   /* if(isValid(emotion))
                    {
                        before_feel_llay.setVisibility(View.VISIBLE);
                            // call_session_detail_root.getSession().getUser().getEmotion().getImage()
                            String beforeImgUrl = GLOBAL_IMAGE_URL + emotion.getImage();
                            if (isValid(beforeImgUrl))
                                Picasso.with(SessionSummaryActivity.this).load(beforeImgUrl).placeholder(R.drawable.sad).into(beforeFeelDimv);
                        }
                    else
                    {
                        LogUtils.LOGD("session","SSA  onSetValues getEmotion() before_feel_llay gone " +emotion);
                       before_feel_llay.setVisibility(View.GONE);
                    }*/

                } catch (Exception e) {
                    e.printStackTrace();
                }
                /*if(isValid(mSessionDetailRoot.getSessionRatingList())) {
                    oldRating = Integer.parseInt(mSessionDetailRoot.getSessionRatingList().get(0).getRating());
                    if (isValid(mSessionDetailRoot.getSessionRatingList().get(0).getEffectiveness())) {
                        effectiveness = Integer.parseInt(mSessionDetailRoot.getSessionRatingList().get(0).getEffectiveness());
                        effective_seekbar.setProgress(effectiveness);
                        effective_circle_view.setProgressValue(effectiveness);
                    }
                }*/
                if (oldRating > 0) {
                    rating_bar.setRating(oldRating);
                } else {
                    rating_bar.setRating(1);
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
        String emotionJson =
                PrefManager.getInstance(SessionSummaryActivity.this).getSharedString(PrefManager.JSON_ALL_EMOTIONS
                        , null);

        if (isValid(modelAllEmotions)) {
            emotions = modelAllEmotions.getResultsList();
            i = emotions.size();
        } else if (emotionJson != null) {
            JSONObject emJsonObject = null;
            try {
                emJsonObject = new JSONObject(emotionJson);
                modelAllEmotions = GsonUtils.getInstance().gsonToMListAllEmotionsI(emJsonObject);
                if (modelAllEmotions != null) {
                    emotions = modelAllEmotions.getResultsList();
                    i = emotions.size();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return i;
    }

    private void webGetEmotionList() {
        try {
            setLoading();
            showProgress();
            String url = GLOBAL_URL + "emotions";
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
            AQuery aq = new AQuery(SessionSummaryActivity.this);
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
                handlerFinish("Network Interupted");
                return;
            }
            AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    hideProgress();
                    LogUtils.LOGD("session", " SSA webGetSessionDetail callback result" + json + " " + status.getMessage());
                    if (!isValid(json, "Network Interupted")) {
                        handlerFinish("Network Interupted");
                    } else {
                        mSessionDetailRoot = GsonUtils.getInstance().gsonToMSessionDetailRoot(json);
                        onSetValues();
                        if (getEmotionList() < 0) {
                            webGetEmotionList();
                        } else
                            setAdapter();

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
            handlerFinish("Network Interupted");
        }
    }


    public void defSetText(TextView textv, String text) {
        if (isValid(textv))
            textv.setText(defString(text));

    }

    private MSessionRating getMSessionRating(String sessionId) {
        MSessionRating mSessionRating = new MSessionRating();
        mSessionRating.setUserId(getUserID());
        mSessionRating.setSessionId(sessionId);
        mSessionRating.setEffectiveness(effectiveness + "");
        if (isCustomer)
            mSessionRating.setRating(rating + "");
        ArrayList<String> comments = new ArrayList<String>();
        try {
            comments = commentsAdapter.getSelectedList();
            if (comments_cedt.isShown()) {
                String comment = comments_cedt.getText() + "";
                if (isValid(comment))
                    comments.add(comment);
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

    private void saveSession(String sessionId) {

        if (!isValid(getAccessToken(), "Token Invalid , Please Re-Login")) {
            handlerFinish("Error : Token Invalid , Please Re-Login");
            return;
        }
        // finish();
        if (!isValid(sessionId, "Session id null ")) {
            handlerFinish("Error : Session id null , Please Re-Login");
            return;
        }
        // finish();
        if (!isValid(getUserID(), "User id null ")) {
            handlerFinish("Error : User id null, Please Re-Login");
            return;
        }
        // finish();
        // before_feel_llay.setVisibility(View.GONE);
        // after_feel_ctxv.setVisibility(View.GONE)
        if (afterFeelSelectionLlay.isShown()) {
            if (!isValid(emotionsListAdapter, "Choose an After Emotion  ")) return;
            if (!isValid(emotionsListAdapter.getSelectedIndex() >= 0, "Choose an After Emotion   "))
                return;
            if (!isValid(emotionsListAdapter.getSelected(), "Choose an After Emotion   "))
                return;
        }

        MSessionRating mSessionRating = getMSessionRating(sessionId);
        if (!isValid(mSessionRating, "Error")) {
            handlerFinish("Error :Token Invalid , Please Re-Login");
            return;
        }
        //  finish();

        setLoading();
        showProgress();

        AjaxCallback cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        PacifyrActivity.sessionid = null;
                        hideProgress();
                    }
                }, 1000);

                if (isPacifyer())
                    PacifyrDashboardActivity.start(SessionSummaryActivity.this);
                else
                    DashboardActivity.start(SessionSummaryActivity.this);

                // finish();
            }
        };

        AQuery aq = new AQuery(SessionSummaryActivity.this);
        cb.header("Content-Type", "application/json");
        cb.header("AccessToken", getAccessToken());
        Gson gson = new Gson();
        String jsonInString = gson.toJson(mSessionRating);
        String url = GLOBAL_URL + "rating/customer/session";
        if (isCustomer)
            url = GLOBAL_URL + "rating/customer/session";
        else
            url = GLOBAL_URL + "rating/pacifyr/session";
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonInString);
            aq.post(url, jsonObject, JSONObject.class, cb);
        } catch (JSONException e) {
            handlerFinish("Error : " + e.getMessage());
            e.printStackTrace();
        }


    }


    CommentClick click = new CommentClick() {
        @Override
        public void CommentCliked(MComments mComments) {
            if (isValid(comments_cedt)) {
                comments_cedt.setVisibility(mComments.isSelected ? View.VISIBLE : View.GONE);
                if (!comments_cedt.isShown()) {
                    comments_cedt.setText("");
                }
            }
        }
    };

    public void setAdapter() {
        LogUtils.LOGD("session", "SSA setAdapter ");
        if (isValid(NetworkService.modelListNegEmotions) && isValid(NetworkService.modelListPosEmotions)) {
            emotions = modelListPosEmotions.getResultsList();
            emotions.addAll(NetworkService.modelListNegEmotions.getResultsList());
            emotionsListAdapter = new EmotionsListAdapter(emotions, this);
            recyclerView.setAdapter(emotionsListAdapter);
            afterFeelSelectionLlay.setVisibility(View.VISIBLE);
            if (getPackageName().equalsIgnoreCase("com.pacifyr.pam")) {
                afterFeelSelectionLlay.setVisibility(View.GONE);
                before_feel_llay.setVisibility(View.GONE);
            }
        } else if (isValid(mSessionDetailRoot)) {
            if (isValid(modelAllEmotions)) {
                emotions = modelAllEmotions.getResultsList();
                emotionsListAdapter = new EmotionsListAdapter(emotions, SessionSummaryActivity.this);
                //postiveEmotions.setClickListener(this);
                recyclerView.setAdapter(emotionsListAdapter);
                afterFeelSelectionLlay.setVisibility(View.VISIBLE);
                if (getPackageName().equalsIgnoreCase("com.pacifyr.pam")) {
                    afterFeelSelectionLlay.setVisibility(View.GONE);
                    before_feel_llay.setVisibility(View.GONE);
                }

                  /*if(!before_feel_llay.isShown())
                  {*/
                MEmotion emotion = getEmotion(emotions);
                LogUtils.LOGD("session", "SSA  setAdapter emotion " + emotion);
                setBeforeEmotionViews(emotion);
               /*     if(isValid(emotion)) {
                        before_feel_llay.setVisibility(View.VISIBLE);
                        // call_session_detail_root.getSession().getUser().getEmotion().getImage()
                        String beforeImgUrl = GLOBAL_IMAGE_URL + emotion.getImage();
                        if(isValid(beforeImgUrl)) Picasso.with(SessionSummaryActivity.this).load(beforeImgUrl).placeholder(R.drawable.sad).into(beforeFeelDimv);
                    } else {
                        LogUtils.LOGD("session","SSA  setAdapter getEmotion() before_feel_llay gone " +emotion);
                      before_feel_llay.setVisibility(View.GONE);
                    }*/
                /* }*/
            } else {
                //  before_feel_llay.setVisibility(View.GONE);
                afterFeelSelectionLlay.setVisibility(View.GONE);
            }
        }

    }

    private MEmotion getEmotion(ArrayList<MEmotion> emotions) {
        MEmotion mEmotion = null;
        String emotionid = null;
        if (isCustomer()) {
            // id= WebRtcSessionManager.userCallInfo.OpponentUserid;
            emotionid = PacifyrActivity.call_emotion.getId();
        } else {
            emotionid = call_session_detail_root.getUser().getEmotionId();
            mEmotion = call_session_detail_root.getUser().getEmotion();
            LogUtils.LOGD("session", "SSA getEmotion(list) mEmotion " + mEmotion);
            LogUtils.LOGD("session", "SSA getEmotion (list) call_session_detail_root " + call_session_detail_root);
        }
        if (!isValid(mEmotion)) {
            for (MEmotion item : emotions) {
                if (item.getId().equalsIgnoreCase(emotionid)) {
                    mEmotion = item;
                    break;
                }
            }
        }
        return mEmotion;
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
        try {
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
            if (isEditable) {
                rightTxv.setVisibility(View.VISIBLE);
                rightLlay.setVisibility(View.VISIBLE);
                defSetText(rightTxv, "Submit");
                backImv.setVisibility(View.INVISIBLE);
            } else {
                rightTxv.setVisibility(View.INVISIBLE);
                rightLlay.setVisibility(View.INVISIBLE);
                leftLlay.setVisibility(View.VISIBLE);
                backImv.setVisibility(View.VISIBLE);
                backImv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
            }
            leftTxv.setVisibility(View.INVISIBLE);
            rightTxv.setBackground(getResources().getDrawable(R.drawable.blue_border_lightblue));
            titleTxv.setVisibility(View.VISIBLE);
            //leftLlay.setVisibility(View.INVISIBLE);
            defSetText(titleTxv, "session summary");
            // backImv.setImageDrawable(getResources().getDrawable(R.drawable.back2x));
            rightLlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveSession(mSessionId);
                }
            });
            submit_ctxv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveSession(mSessionId);
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
        } catch (Exception ex) {
            LogUtils.LOGD("onSetActionBar EX :" + ex.getMessage());
            ex.printStackTrace();
        }
    }


    @Subscribe
    public void onEventLoadEmotion(EventLoadEmotion event) {
        try {
            if (event.isLoaded()) {
                setAdapter();
            }
        /*    if (isValid(modelAllEmotions)) {
                emotions = modelAllEmotions.getResultsList();
                emotionsListAdapter = new EmotionsListAdapter(emotions, SessionSummaryActivity.this);
                //postiveEmotions.setClickListener(this);
                recyclerView.setAdapter(emotionsListAdapter);
            }*/
        } catch (Exception e) {
            LogUtils.LOGD("session", "SSA onNotifyEvent exce " + e.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        //  super.onBackPressed();
        Toast.makeText(SessionSummaryActivity.this, "Please submit your feedback", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAlertTypeEvent(AlertTypeEvent event) {

    }


   /* public interface  CommentClick
    {
        public abstract void CommentCliked(MComments mComments);
    }*/

   /*  if(emotions.get(position).getClicked())
            {
                if(selectedIndex==-1) {
                    selectedIndex = position;
                    holder.emotionIMG.clearColorFilter();
                }
            }
            else {
                holder.emotionIMG.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
            }

            if(selectedIndex == position){
                holder.emotionIMG.clearColorFilter();
            }
            else{
                holder.emotionIMG.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
            }

            holder.emotionIMG.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    for (int i=0;i<emotions.size();i++)
                    {
                        if (i==position){
                            emotions.get(position).setClicked(true);
                            holder.emotionIMG.clearColorFilter();
                            selEmotion = emotions.get(position);
                        }
                        else
                            emotions.get(i).setClicked(false);

                    }
                    setSelectedIndex(position);
                    notifyDataSetChanged();
                }
            });*/
}
