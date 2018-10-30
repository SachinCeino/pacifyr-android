package com.pacifyr.omhealthcare.home;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ceino.utilitylibrary.CustomEditText;
import com.ceino.utilitylibrary.CustomFontTextView;
import com.ceino.utilitylibrary.DeviceFitImageView;
import com.ceino.utilitylibrary.ToastHandler;
import com.ceino.utilitylibrary.model.pacifyr.MEmotion;
import com.ceino.utilitylibrary.model.pacifyr.MSessionDetailRoot;
import com.ceino.utilitylibrary.model.pacifyr.MSessionRating;
import com.eralp.circleprogressview.CircleProgressView;
import com.google.gson.Gson;
import com.jesusm.holocircleseekbar.lib.HoloCircleSeekBar;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.adapter.CommentsAdapter;
import com.pacifyr.omhealthcare.adapter.EmotionsListAdapter;
import com.pacifyr.omhealthcare.base.PacifyerActivity;
import com.pacifyr.omhealthcare.event.AlertTypeEvent;
import com.pacifyr.omhealthcare.event.NotifyEvent;
import com.pacifyr.omhealthcare.model.MComments;
import com.pacifyr.omhealthcare.model.interfaces.CommentClick;
import com.pacifyr.omhealthcare.network.GsonUtils;
import com.pacifyr.omhealthcare.quickblox.App;
import com.pacifyr.omhealthcare.quickblox.utils.Consts;
import com.pacifyr.omhealthcare.utility.PacifyrAppCompactActivity;
import com.pacifyr.omhealthcare.utils.LogUtils;
import com.pacifyr.omhealthcare.utils.Utility;
import com.pacifyr.omhealthcare.utils.WrappingSlidingPaneLayout;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;
import com.willy.ratingbar.BaseRatingBar;
import com.willy.ratingbar.ScaleRatingBar;

import org.json.JSONObject;

import java.util.ArrayList;

import app.minimize.com.seek_bar_compat.SeekBarCompat;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.ceino.utilitylibrary.network.UtilityNetworkService.GLOBAL_IMAGE_URL;
import static com.pacifyr.omhealthcare.network.NetworkService.GLOBAL_URL;
import static com.pacifyr.omhealthcare.network.NetworkService.modelAllEmotions;

/**
 * Created by Mani-Ceino on 12/14/2017.
 */

public class SessionSummaryActivity  extends PacifyrAppCompactActivity{

    private static boolean isEditable;
    boolean showingFirst = true;
    //EditableSeekBar esbExample;
    HoloCircleSeekBar holoCircleSeekBar;
    //CircleView effective_circle_view;

    CircleProgressView circleProgressView;

    EmotionsListAdapter emotionsListAdapter;
    RecyclerView recyclerView;

    RecyclerView commentsRcv;
    CommentsAdapter commentsAdapter;

    WrappingSlidingPaneLayout mSlidingPaneLayout;
    boolean onceBackpressed =false;
    private int rating=5;


    private int effectiveness=0;

    private CustomFontTextView before_emotion_ctxv;


    ArrayList<MEmotion> emotions;
    private CircleImageView pacifyrImgCimv;
    private CustomFontTextView pacifyrNameCtxv,callback_ctxv;
    private ImageView beforeFeelDimv;
    private CustomFontTextView durationCtxv;
    private CustomFontTextView pacifyrRateCtxv,before_feel_ctxv;
    private CustomFontTextView sessionCostCtxv;
    private int oldRating=0;

    public MSessionDetailRoot mSessionDetailRoot;
    public SeekBarCompat effective_seekbar;
    public  ScaleRatingBar rating_bar;
    /// public  RatingBar rating_bar;
    CustomEditText comments_cedt;
    public boolean isCustomer=false;

    public static  boolean isAudioCall=true;
    CircleImageView img_blue_bg;


    private CustomFontTextView afterFeelSelectionCtxv;
    private LinearLayout pacifyrEaringsLlay,customer_fees_llay;
    private CustomFontTextView earingstCtxv,comapnyfeesCtxv;

    //public LinearLayout rating_llay,before_feel_llay;
    //  private LinearLayout afterFeelSelectionLlay;
    //private LinearLayout  after_feel_llay;

    // private RelativeLayout  after_feel_llay;
    private RelativeLayout afterFeelSelectionLlay;
    public RelativeLayout rating_llay,before_feel_llay;


    public static void start(Context context, boolean isAudioCall) {
        Intent intent = new Intent(context, SessionSummaryActivity.class);
        //
        // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        //  intent.putExtra(Consts.EXTRA_IS_STARTED_FOR_CALL, isRunForCall);
        SessionSummaryActivity.isAudioCall=isAudioCall;
        isEditable = true;
       /* try {
            Activity activity = null;
            activity = ((AppChatNotUSe) AppChatNotUSe.getInstance()).getCurrentActivity();
            if(isValidStatic(activity))
                if (activity instanceof CallActivity ) {
                    activity.finish();
                }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
//



        context.startActivity(intent);
    }
    public static Boolean isValidStatic(Object object) {
        if(object != null) return true;
        return false;

    }
    public static void start(Context context) {
        Intent intent = new Intent(context, SessionSummaryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // intent.putExtra(Consts.EXTRA_IS_STARTED_FOR_CALL, isRunForCall);
        SessionSummaryActivity.isAudioCall=true;
        isEditable = true;

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

    public static void start(Context context,String sId) {
        Intent intent = new Intent(context, SessionSummaryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        isEditable = false;
        SessionSummaryActivity.isAudioCall=true;
        sessionid = sId;
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.LOGD("session", " SSA onCreate sessionid "+sessionid);
        setContentView(R.layout.activity_session_summary);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);



        if( !isCustomer() && !isPacifyer())
        {
            ToastHandler.newInstance(this).mustShowToast("Session Expired : Please login again" );
            deleteInstallationWeb();
            finish();
        }
        isCustomer=isCustomer();

        if(!isValid(sessionid,"Session id null "))
            handlerFinish("Network Interrupted");

        // webListEmotions(SessionSummaryActivity.this);


        onCreateView();
        if(isValid(sessionid)) {
            webGetSessionDetail(sessionid);
            getPrefManager().saveSessionId(null);
        }
        else
            handlerFinish("Network Interrupted");
        //  finish();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sessionid=null;
        PacifyerActivity.call_emotion=null;
    }

    @Override
    protected void onResume() {
        super.onResume();

        ((App) App.getInstance()).setCurrentActivity(this);
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
            before_emotion=null;
            LogUtils.LOGD("session", " SSA onCreateView ");
            onSetActionBar();

            img_blue_bg=(CircleImageView)findViewById(R.id.img_blue_bg);

            pacifyrImgCimv = (CircleImageView) findViewById(R.id.last_session_cimv);
            before_feel_ctxv = (CustomFontTextView) findViewById(R.id.before_feel_ctxv);
          /*  after_feel_ctxv = (CustomFontTextView) findViewById(R.id.after_feel_ctxv);
            after_feel_llay = (LinearLayout) findViewById(R.id.after_feel_llay);*/

          /*    rating_llay = (LinearLayout) findViewById(R.id.rating_llay);
            before_feel_llay = (LinearLayout) findViewById(R.id.before_feel_llay);
            after_feel_llay = (LinearLayout) findViewById(R.id.after_feel_llay);
            afterFeelSelectionLlay = (LinearLayout) findViewById(R.id.after_feel_selection_llay);*/

            rating_llay = (RelativeLayout) findViewById(R.id.rating_llay);
            before_feel_llay = (RelativeLayout) findViewById(R.id.before_feel_llay);
            //  after_feel_llay = (RelativeLayout) findViewById(R.id.after_feel_llay);
            afterFeelSelectionLlay = (RelativeLayout) findViewById(R.id.after_feel_selection_llay);

            afterFeelSelectionCtxv = (CustomFontTextView) findViewById(R.id.after_feel_selection_ctxv);
            pacifyrEaringsLlay = (LinearLayout) findViewById(R.id.pacifyr_earings_llay);
            earingstCtxv = (CustomFontTextView) findViewById(R.id.earingst_ctxv);
            comapnyfeesCtxv = (CustomFontTextView) findViewById(R.id.comapnyfees_ctxv);
            customer_fees_llay = (LinearLayout) findViewById(R.id.customer_fees_llay);

            before_emotion_ctxv = (CustomFontTextView) findViewById(R.id.before_emotion_ctxv);


            callback_ctxv = (CustomFontTextView) findViewById(R.id.callback_ctxv);
            sessionCostCtxv = (CustomFontTextView) findViewById(R.id.session_cost_ctxv);
            pacifyrNameCtxv = (CustomFontTextView) findViewById(R.id.pacifyr_name_ctxv);
            beforeFeelDimv = (ImageView) findViewById(R.id.before_feel_dimv);
            recyclerView = (RecyclerView) findViewById(R.id.begning_rcv);
            commentsRcv = (RecyclerView) findViewById(R.id.comments_rcv);
            pacifyrRateCtxv = (CustomFontTextView) findViewById(R.id.pacifyr_rate_ctxv);
            durationCtxv = (CustomFontTextView) findViewById(R.id.duration_ctxv);
            comments_cedt = (CustomEditText) findViewById(R.id.comments_cedt);

            rating_bar = (ScaleRatingBar) findViewById(R.id.rating_bar);
            //rating_bar = (RatingBar) findViewById(R.id.rating_bar);
            effective_seekbar = (SeekBarCompat) findViewById(R.id.effective_seekbar);
            //holoCircleSeekBar=(HoloCircleSeekBar) findViewById(R.id.picker);
            //effective_circle_view = (CircleView) findViewById(R.id.effective_circle_view);
            circleProgressView = (CircleProgressView) findViewById(R.id.circle_progress_view);

            effective_seekbar.setProgress(effectiveness);
            circleProgressView.setProgress(effectiveness);
           /* effective_circle_view.setProgressValue(effectiveness);
            effective_circle_view.setSuffixValue("%");*/

            recyclerView.setLayoutManager(new LinearLayoutManager(SessionSummaryActivity.this, LinearLayoutManager.HORIZONTAL, false));
            commentsRcv.setLayoutManager(new LinearLayoutManager(SessionSummaryActivity.this, LinearLayoutManager.HORIZONTAL, false));
            commentsAdapter = new CommentsAdapter(SessionSummaryActivity.this,click,true);
            commentsRcv.setAdapter(commentsAdapter);
            LogUtils.LOGD("session", " SSA onCreateView  before set rating view");
            setRatingViews();


            callback_ctxv.setVisibility(View.GONE);
            if(isCustomer) {

         /*       before_feel_ctxv.setText("How were you feeling before the call?");
                afterFeelSelectionCtxv.setText("How are you feeling now?");*/

                pacifyrEaringsLlay.setVisibility(View.GONE);
                customer_fees_llay.setVisibility(View.VISIBLE);
                pacifyrRateCtxv.setVisibility(View.VISIBLE);
                rating_llay.setVisibility(View.VISIBLE);
                before_feel_ctxv.setText("How were you feeling before the call?");
                // afterFeelSelectionCtxv.setText("How were you feeling after the call ?");
                afterFeelSelectionCtxv.setText("How are you feeling now?");



                if (isValid(ProfileScreenActivity.mpacifyerDetial.getQbid()))
                    callback_ctxv.setVisibility(View.VISIBLE);
                callback_ctxv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            if (checker.lacksPermissions(Consts.PERMISSIONS)) {
                                startPermissionsActivity(false);
                                if (!checker.lacksPermissions(Consts.PERMISSIONS))
                                {
                                    if (isValid(ProfileScreenActivity.mpacifyerDetial.getQbid()))
                                        if(isLoggedInChat()) {
                                            callRequestWeb(ProfileScreenActivity.mpacifyerDetial,listner, true);
                                        }
                                    //ToastHandler.newInstance(ConnectToPacifyrActivity.this).mustShowToast("These Permissions are required to make call");
                                }
                            }
                            else {
                                if (isValid(ProfileScreenActivity.mpacifyerDetial.getQbid()))
                                    if(isLoggedInChat()) {

                                        callRequestWeb(ProfileScreenActivity.mpacifyerDetial,listner, true);

                                        //startCall(true,qbID);
                                    }
                            }
                          /*  if (isValid(ProfileScreenActivity.mpacifyerDetial.getQbid()))

                                if (isLoggedInChat()) {
                                    callRequestWeb(ProfileScreenActivity.mpacifyerDetial,listner, true);
                                    //startSessionWeb(ProfileScreenActivity.mpacifyerDetial, true);
                                    //startCall(true,qbID);
                                }*/
                        }catch (Exception e)
                        {
                            ToastHandler.newInstance(SessionSummaryActivity.this).mustShowToast("Please try later");
                            finish();
                        }
                    }
                });

            }
            else {
                rating_llay.setVisibility(View.GONE);
                callback_ctxv.setVisibility(View.GONE);
                /*//afterFeelSelectionCtxv.setText("How were customer feeling now?");
                before_feel_ctxv.setText("How were customer feeling before the call?");*/

                pacifyrEaringsLlay.setVisibility(View.VISIBLE);
                pacifyrRateCtxv.setVisibility(View.GONE);
                customer_fees_llay.setVisibility(View.GONE);
                callback_ctxv.setVisibility(View.GONE);
                before_feel_ctxv.setText("How was the  customer  feeling before call?");
              /*  after_feel_ctxv.setText("How was the  customer feeling after the call ?");
                after_feel_llay.setVisibility(View.VISIBLE);*/
                afterFeelSelectionCtxv.setText("How was the customer feeling after the call in your opinion?");
            }







            if (isValid(mSessionDetailRoot)) {
                onSetValues();
                setAdapter();
            }

        }catch (Exception e)
        {
            LogUtils.LOGD("session","SSA onCreateView exce " + e.getMessage());
        }
    }
    private void setRatingViews() {
        try{
            rating_bar.setNumStars(5);
            rating =5;
          /*  rating_bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                    rating = (int) ratingBar.getRating();
                }
            });*/
            rating_bar.setStarPadding(10);
            rating_bar.setTouchable(true);
            rating_bar.setClearRatingEnabled(true);
            rating_bar.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
                @Override
                public void onRatingChange(BaseRatingBar baseRatingBar, float v) {
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
        }catch (Exception e)
        {
            LogUtils.LOGD("session","SSA setRatingViews exce " + e.getMessage());
        }
    }

    MEmotion before_emotion=null;
    private void setBeforeEmotionViews(MEmotion mEmotion) {
        try{
            if(isValid(mEmotion))
            {
                before_emotion=mEmotion;
                before_feel_llay.setVisibility(View.VISIBLE);
                defSetText(before_emotion_ctxv,mEmotion.getTitle());
                defSetText(before_feel_ctxv,mEmotion.getTitle());

                if(isCustomer) {
                    before_feel_ctxv.setText("How were you feeling before the call?");
                }
                else {
                    before_feel_ctxv.setText("How was the  customer  feeling before call?");
                }
                String beforeImgUrl = GLOBAL_IMAGE_URL + mEmotion.getImage();
                if(isValid(beforeImgUrl)) Picasso.with(SessionSummaryActivity.this).load(beforeImgUrl).placeholder(R.drawable.sad).into(beforeFeelDimv);
                img_blue_bg.setVisibility(View.VISIBLE);
                Resources res = SessionSummaryActivity.this.getResources();
                final int nColor = res.getColor(R.color.white);
                beforeFeelDimv.setColorFilter(nColor, PorterDuff.Mode.SRC_ATOP);
            }
            else {
                before_feel_llay.setVisibility(View.GONE);
            }
        }catch (Exception e){
            LogUtils.LOGD("session","SSA setBeforeEmotionViews exce " + e.getMessage());
        }
    }
    public void onSetValues()
    {
        try{
            LogUtils.LOGD("session", " SSA onSetValues ");
            if (isValid(mSessionDetailRoot)) {



                oldRating=0;


                durationCtxv.setText(mSessionDetailRoot.getDuration());
                if(isCustomer)
                {
                    sessionCostCtxv.setText(Utility.getPriceSign(SessionSummaryActivity.this) + mSessionDetailRoot.getSessionCost());
                    if(isValid( mSessionDetailRoot.getPacifyr())) {
                        String pacName = mSessionDetailRoot.getPacifyr().getFirstName() + " " + mSessionDetailRoot.getPacifyr().getLastName();
                        pacifyrNameCtxv.setText(pacName);

                        pacifyrRateCtxv.setText("$ " + mSessionDetailRoot.getPacifyr().getPrice() + " per hour");
                        String  imagePac = mSessionDetailRoot.getPacifyr().getImageUrl();
                        if (isValid(imagePac))
                            Picasso.with(SessionSummaryActivity.this).load(imagePac).placeholder(R.drawable.placeholder_user).into(pacifyrImgCimv);

                    }

                }
                else
                {
                    defSetText(earingstCtxv,Utility.getPriceSign(SessionSummaryActivity.this) + (mSessionDetailRoot.getPacifyrEarnings()));
                    defSetText(comapnyfeesCtxv,Utility.getPriceSign(SessionSummaryActivity.this) + (mSessionDetailRoot.getSessionCost()-mSessionDetailRoot.getPacifyrEarnings()));
                    if(isValid( mSessionDetailRoot.getUser())) {
                        String pacName = mSessionDetailRoot.getUser().getFirstName() + " " + mSessionDetailRoot.getUser().getLastName();
                        pacifyrNameCtxv.setText(pacName);
                        String imagePac =  mSessionDetailRoot.getUser().getImageUrl();
                        if (isValid(imagePac))
                            Picasso.with(SessionSummaryActivity.this).load(imagePac).placeholder(R.drawable.placeholder_user).into(pacifyrImgCimv);
                    }
                }
                try {
                    MEmotion emotion=getEmotion();
                    LogUtils.LOGD("session","SSA  onSetValues getEmotion() emotion " +emotion);

                    setBeforeEmotionViews(emotion);
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

                }catch (Exception e)
                {
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
        }catch (Exception e)
        {
            LogUtils.LOGD("session","SSA onSetValues exce " + e.getMessage());
        }
    }
    public int getEmotionList() {
        int i=-1;
        try{

            if (isValid(modelAllEmotions)) {
                emotions = modelAllEmotions.getResultsList();
                i=emotions.size();
            }
        }catch(Exception e)
        {
            i=-1;
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
        }catch (Exception e)
        {
            hideProgress();
            e.printStackTrace();
        }
    }


    public void webGetSessionDetail(final String sessionid) {
        try {
            setLoading();
            showProgress();
            if(!isValid(sessionid,"Network Interupted")) {
                handlerFinish("Network Interupted");
                return;
            }
            AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    hideProgress();
                    LogUtils.LOGD("session", " SSA webGetSessionDetail callback result" + json + " " + status.getMessage());
                    if(!isValid(json,"Network Interupted"))
                    {
                        handlerFinish("Network Interupted");
                    }
                    else {
                        try {
                            mSessionDetailRoot= GsonUtils.getInstance().gsonToMSessionDetailRoot(json);
                            onSetValues();
                            if(getEmotionList()<0)
                            {
                                webGetEmotionList();
                            }
                            else
                                setAdapter();
                        } catch (Exception e){
                            handlerFinish("Network Interupted");
                            LogUtils.LOGD("session", " SSA webGetSessionDetail callback exce" + e.getMessage());
                        }
                    }
                }
            };
            AQuery aq = new AQuery(this);
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", getAccessToken());
            String url = "session/"+sessionid;
            aq.ajax(GLOBAL_URL + url, JSONObject.class, cb);
        } catch (Exception e){
            hideProgress();
            LogUtils.LOGD("session","SSA webGetSessionDetail exce " + e.getMessage());
            handlerFinish("Network Interupted");
        }
    }


    public void defSetText(TextView textv, String text) {
        if(isValid(textv))
            textv.setText(defString(text));

    }

    private MSessionRating getMSessionRating() {
        MSessionRating mSessionRating = new MSessionRating();
        mSessionRating.setUserId(getUserID());
        mSessionRating.setSessionId(sessionid);
        mSessionRating.setEffectiveness(effectiveness+"");
        if(isCustomer)
            mSessionRating.setRating(rating+"");
        ArrayList<String>comments=new ArrayList<String>();
        try {
            comments=commentsAdapter.getSelectedList();
            if (comments_cedt.isShown()) {
                String comment = comments_cedt.getText() + "";
                if (isValid(comment))
                    comments.add(comment);
            }
        }catch (Exception e)
        {
            LogUtils.LOGD("session","SSA getMSessionRating list exce " + e.getMessage());
        }

        if(isValid(comments))
            mSessionRating.setComments(comments);
        if(isValid(emotionsListAdapter))
            if(isValid(emotionsListAdapter.getSelected()))
                mSessionRating.setAfterSessionEmotionId(emotionsListAdapter.getSelected().getId()+"");

        //     if(before_feel_llay.isShown())
        if(isValid(before_emotion))
            mSessionRating.setBeforeSessionEmotionId(before_emotion.getId()+"");
        // qscreate.setDeviceType("android");
        return mSessionRating;

    }

    private void saveSession() {

        try {

            if (!isValid(getAccessToken(), "Token Invalid , Please Re-Login")) {
                handlerFinish("Error : Token Invalid , Please Re-Login");
                return;
            }
            // finish();
            if (!isValid(sessionid, "Session id null ")) {
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
            if(afterFeelSelectionLlay.isShown()) {
                if(!isValid(emotionsListAdapter, "choose an After Emotion  ")) return;
                if(!isValid(emotionsListAdapter.getSelectedIndex() >= 0, "choose an After Emotion   "))
                    return;
                if(!isValid(emotionsListAdapter.getSelected(),"choose an After Emotion   "))
                    return;
            }

            MSessionRating mSessionRating=getMSessionRating();
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
                    try {
                        new Handler().postDelayed(new Runnable()
                        {
                            @Override public void run() {
                                PacifyerActivity.sessionid=null;
                                hideProgress();
                            }
                        }, 1000);

                        if(isPacifyer())
                            PacifyrDashboardActivity.start(SessionSummaryActivity.this);
                        else
                            DashboardActivity.start(SessionSummaryActivity.this);

                        // finish();
                    } catch (Exception e) {
                        LogUtils.LOGD("session","SSA savePacifyerSession  callback  exce " + e.getMessage());
                    }
                }
            };

            AQuery aq = new AQuery(SessionSummaryActivity.this);
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", getAccessToken());
            Gson gson = new Gson();
            String jsonInString = gson.toJson(mSessionRating);
            String url = GLOBAL_URL + "rating/customer/session";
            if(isCustomer)
                url = GLOBAL_URL + "rating/customer/session";
            else
                url = GLOBAL_URL + "rating/pacifyr/session";
            JSONObject jsonObject = new JSONObject(jsonInString);
            aq.post(url, jsonObject, JSONObject.class, cb);


        }catch (Exception e)
        {
            LogUtils.LOGD("session","SSA savePacifyerSession  exce " + e.getMessage());
        }

    }



    CommentClick click=new CommentClick() {
        @Override
        public void CommentCliked(MComments mComments) {
            if(isValid(comments_cedt))
            {
                comments_cedt.setVisibility(mComments.isSelected?View.VISIBLE:View.GONE);
                if(!comments_cedt.isShown())
                {
                    comments_cedt.setText("");
                }
            }
        }
    };

    public void setAdapter()
    {
        try {
            LogUtils.LOGD("session","SSA setAdapter " );
            if (isValid(mSessionDetailRoot)) {
                if(isValid(modelAllEmotions)) {
                    emotions = modelAllEmotions.getResultsList();
                    emotionsListAdapter = new EmotionsListAdapter(emotions, SessionSummaryActivity.this);
                    //postiveEmotions.setClickListener(this);
                    recyclerView.setAdapter(emotionsListAdapter);

                  /*if(!before_feel_llay.isShown())
                  {*/
                    MEmotion emotion = getEmotion(emotions);
                    LogUtils.LOGD("session","SSA  setAdapter emotion " +emotion);
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

        }catch (Exception e)
        {
            LogUtils.LOGD("session","SSA setAdapter exce " + e.getMessage());
        }
    }

    private MEmotion getEmotion(ArrayList<MEmotion> emotions) {
        MEmotion mEmotion=null;
        String emotionid=null;
        try {
            if (isCustomer()) {
                // id= WebRtcSessionManager.userCallInfo.OpponentUserid;
                emotionid=PacifyerActivity.call_emotion.getId();
            }
            else
            {
                emotionid=call_session_detail_root.getUser().getEmotionId();
                mEmotion=call_session_detail_root.getUser().getEmotion();
                LogUtils.LOGD("session","SSA getEmotion(list) mEmotion " + mEmotion);
                LogUtils.LOGD("session","SSA getEmotion (list) call_session_detail_root " + call_session_detail_root);
            }
            if(!isValid(mEmotion)) {
                for (MEmotion item : emotions) {
                    if(item.getId().equalsIgnoreCase(emotionid)) {
                        mEmotion = item;
                        break;
                    }
                }
            }
        }catch (Exception e)
        {
            LogUtils.LOGD("session","SSA getEmotion (list) exce " + e.getMessage());
        }
        return mEmotion;
    }

    private LinearLayout actionBarTopLlay;
    private RelativeLayout bottomPanel;
    private LinearLayout leftLlay;
    private DeviceFitImageView backImv;
    private CustomFontTextView leftTxv;
    private CustomFontTextView titleTxv;
    private LinearLayout rightLlay;
    private CustomFontTextView rightTxv;
    private DeviceFitImageView rightImv;
    CustomFontTextView submit_ctxv;
    public void onSetActionBar() {
        try {
            actionBarTopLlay = (LinearLayout) findViewById(R.id.action_bar_top_llay);
            bottomPanel = (RelativeLayout) findViewById(R.id.bottom_panel);
            leftLlay = (LinearLayout) findViewById(R.id.left_llay);
            backImv = (DeviceFitImageView) findViewById(R.id.back_imv);
            leftTxv = (CustomFontTextView) findViewById(R.id.left_txv);
            titleTxv = (CustomFontTextView) findViewById(R.id.title_txv);
            rightLlay = (LinearLayout) findViewById(R.id.right_llay);
            rightTxv = (CustomFontTextView) findViewById(R.id.right_txv);
            rightImv = (DeviceFitImageView) findViewById(R.id.right_imv);
            submit_ctxv = (CustomFontTextView) findViewById(R.id.submit_ctxv);
            mSlidingPaneLayout = (WrappingSlidingPaneLayout) findViewById(R.id.sliding_pane);
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
                    saveSession();
                }
            });
            submit_ctxv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveSession();
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
        catch (Exception ex)
        {
            LogUtils.LOGD("onSetActionBar EX :"+ex.getMessage());
            ex.printStackTrace();
        }
    }



    @Subscribe
    public void onNotifyEvent(NotifyEvent event) {
        try {
            if(event.isSuccess) {
                setAdapter();
            }
        /*    if (isValid(modelAllEmotions)) {
                emotions = modelAllEmotions.getResultsList();
                emotionsListAdapter = new EmotionsListAdapter(emotions, SessionSummaryActivity.this);
                //postiveEmotions.setClickListener(this);
                recyclerView.setAdapter(emotionsListAdapter);
            }*/
        }catch (Exception e)
        {
            LogUtils.LOGD("session","SSA onNotifyEvent exce " + e.getMessage());
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
