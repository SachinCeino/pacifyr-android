package com.pacifyr.omhealthcare.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

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
import com.jesusm.holocircleseekbar.lib.HoloCircleSeekBar;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.adapter.CommentsAdapter;
import com.pacifyr.omhealthcare.adapter.EmotionsListAdapter;
import com.pacifyr.omhealthcare.event.AlertTypeEvent;
import com.pacifyr.omhealthcare.event.NotifyEvent;
import com.pacifyr.omhealthcare.model.MComments;
import com.pacifyr.omhealthcare.model.interfaces.CommentClick;
import com.pacifyr.omhealthcare.network.GsonUtils;
import com.pacifyr.omhealthcare.quickblox.App;
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

public class SummaryDetailActivity   extends PacifyrAppCompactActivity {


    boolean showingFirst = true;
    HoloCircleSeekBar holoCircleSeekBar;
    //CircleView effective_circle_view;
    CircleProgressView circleProgressView;

    EmotionsListAdapter emotionsListAdapter;
    RecyclerView recyclerView;

    RecyclerView commentsRcv;
    CommentsAdapter commentsAdapter;

    WrappingSlidingPaneLayout mSlidingPaneLayout;
    boolean onceBackpressed =false;
    private int rating;


    private int effectiveness=0;




    ArrayList<MEmotion> emotions;
    private CircleImageView pacifyrImgCimv;
    private CustomFontTextView pacifyrNameCtxv,callback_ctxv;
    private ImageView beforeFeelDimv;
    private CustomFontTextView durationCtxv;
    private CustomFontTextView pacifyrRateCtxv,before_feel_ctxv,after_feel_ctxv;
    private CustomFontTextView sessionCostCtxv;
    private CustomFontTextView before_emotion_ctxv,after_emotion_ctxv;
    private int oldRating;

    public MSessionDetailRoot mSessionDetailRoot;
    public SeekBarCompat effective_seekbar;
    public ScaleRatingBar rating_bar;
    CustomEditText comments_cedt;
    public boolean isCustomer=false;
    //public LinearLayout rating_llay,before_feel_llay;
    //  private LinearLayout afterFeelSelectionLlay;
    //private LinearLayout  after_feel_llay;
    private RelativeLayout  after_feel_llay;
    private RelativeLayout afterFeelSelectionLlay;
    public RelativeLayout rating_llay,before_feel_llay;
    public static  boolean isAudioCall=true;


    public static String ratingId=null;



    private CustomFontTextView afterFeelSelectionCtxv;
    private ImageView afterFeelDimv;
    private LinearLayout pacifyrEaringsLlay,customer_fees_llay;
    private CustomFontTextView earingstCtxv,comapnyfeesCtxv;

    public static Boolean isValidStatic(Object object) {
        if(object != null) return true;
        return false;

    }


    public static void start(Context context,String sId) {
        Intent intent = new Intent(context, SummaryDetailActivity.class);
       // isEditable = false;
        SummaryDetailActivity.isAudioCall=true;
        sessionid = sId;
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            ratingId = null;
            LogUtils.LOGD("sessionaaSD", " SDA onCreate sessionid " + sessionid);
            if(!isValid(sessionid, "Please check later")) {
                LogUtils.LOGD("session", " SDA onCreate sessionid not valid  " + sessionid);
                //  handlerFinish("Error in last session ");
                handlerFinish("Data not updated in last session ");

            }
            LogUtils.LOGD("session", " SDA onCreate sessionid " + sessionid);
            setContentView(R.layout.activity_summary_detail);

            if(!isCustomer() && !isPacifyer()) {
                ToastHandler.newInstance(this).mustShowToast("Session Expired : Please login again");
                deleteInstallationWeb();
                finish();
            }
            isCustomer = isCustomer();


            // webListEmotions(SummaryDetailActivity.this);
            onCreateView();
            if(isValid(sessionid)) webGetSessionDetail(sessionid);
            else {
                handlerFinish("Data not updated in last session ");
                // finish();
            }

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
            pacifyrImgCimv = (CircleImageView) findViewById(R.id.last_session_cimv);
        /*    rating_llay = (LinearLayout) findViewById(R.id.rating_llay);
            before_feel_llay = (LinearLayout) findViewById(R.id.before_feel_llay);
            after_feel_llay = (LinearLayout) findViewById(R.id.after_feel_llay);
            afterFeelSelectionLlay = (LinearLayout) findViewById(R.id.after_feel_selection_llay);*/

            rating_llay = (RelativeLayout) findViewById(R.id.rating_llay);
            before_feel_llay = (RelativeLayout) findViewById(R.id.before_feel_llay);
            after_feel_llay = (RelativeLayout) findViewById(R.id.after_feel_llay);
            afterFeelSelectionLlay = (RelativeLayout) findViewById(R.id.after_feel_selection_llay);

            before_feel_ctxv = (CustomFontTextView) findViewById(R.id.before_feel_ctxv);
            after_feel_ctxv = (CustomFontTextView) findViewById(R.id.after_feel_ctxv);


            before_emotion_ctxv = (CustomFontTextView) findViewById(R.id.before_emotion_ctxv);
            after_emotion_ctxv = (CustomFontTextView) findViewById(R.id.after_emotion_ctxv);

           /* setBeforeEmotionViews();
            setAfterEmotionViews();*/

            customer_fees_llay = (LinearLayout) findViewById(R.id.customer_fees_llay);
            afterFeelDimv = (ImageView) findViewById(R.id.after_feel_dimv);

            afterFeelSelectionCtxv = (CustomFontTextView) findViewById(R.id.after_feel_selection_ctxv);
            callback_ctxv = (CustomFontTextView) findViewById(R.id.callback_ctxv);
            sessionCostCtxv = (CustomFontTextView) findViewById(R.id.session_cost_ctxv);
            pacifyrEaringsLlay = (LinearLayout) findViewById(R.id.pacifyr_earings_llay);
            earingstCtxv = (CustomFontTextView) findViewById(R.id.earingst_ctxv);
            comapnyfeesCtxv = (CustomFontTextView) findViewById(R.id.comapnyfees_ctxv);
            pacifyrNameCtxv = (CustomFontTextView) findViewById(R.id.pacifyr_name_ctxv);
            beforeFeelDimv = (ImageView) findViewById(R.id.before_feel_dimv);
            recyclerView = (RecyclerView) findViewById(R.id.begning_rcv);
            commentsRcv = (RecyclerView) findViewById(R.id.comments_rcv);
            pacifyrRateCtxv = (CustomFontTextView) findViewById(R.id.pacifyr_rate_ctxv);
            durationCtxv = (CustomFontTextView) findViewById(R.id.duration_ctxv);
            comments_cedt = (CustomEditText) findViewById(R.id.comments_cedt);
            rating_bar = (ScaleRatingBar) findViewById(R.id.rating_bar);
            effective_seekbar = (SeekBarCompat) findViewById(R.id.effective_seekbar);
            //holoCircleSeekBar=(HoloCircleSeekBar) findViewById(R.id.picker);
            //effective_circle_view = (CircleView) findViewById(R.id.effective_circle_view);
            circleProgressView = (CircleProgressView) findViewById(R.id.circle_progress_view);
            rating_llay.setVisibility(View.GONE);
            callback_ctxv.setVisibility(View.GONE);
            if(isCustomer) {
                pacifyrEaringsLlay.setVisibility(View.GONE);
                customer_fees_llay.setVisibility(View.VISIBLE);
                pacifyrRateCtxv.setVisibility(View.VISIBLE);
                rating_llay.setVisibility(View.VISIBLE);
                before_feel_ctxv.setText("How were you feeling before the call?");
                afterFeelSelectionCtxv.setText("How were you feeling after the call ?");
                after_feel_ctxv.setText("");
                after_feel_llay.setVisibility(View.GONE);
            }
            else {
                pacifyrEaringsLlay.setVisibility(View.VISIBLE);
                pacifyrRateCtxv.setVisibility(View.GONE);
                customer_fees_llay.setVisibility(View.GONE);
                callback_ctxv.setVisibility(View.GONE);
                before_feel_ctxv.setText("How was the  customer  feeling before call?");
                after_feel_ctxv.setText("How was the  customer feeling after the call ?");
                after_feel_llay.setVisibility(View.VISIBLE);
                afterFeelSelectionCtxv.setText("How was the customer feeling after the call in your opinion?");
            }

           //  comments_cedt.setVisibility(View.GONE);
            recyclerView.setLayoutManager(new LinearLayoutManager(SummaryDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
            commentsRcv.setLayoutManager(new LinearLayoutManager(SummaryDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
            commentsRcv.setNestedScrollingEnabled(false);
            recyclerView.setNestedScrollingEnabled(false);
            rating=5;
            rating_bar.setNumStars(5);
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

            //effective_circle_view.setProgressStepAsInteger(true);
            // effective_circle_view.setSuffixEnabled(true);
            // effective_circle_view.setSuffixValue("%");
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
            if (isValid(mSessionDetailRoot)) {
                onSetValues();
                setAdapter();
            }

        }catch (Exception e)
        {
            LogUtils.LOGD("session","SDA onCreateView exce " + e.getMessage());
        }
    }

    private void setAfterEmotionViews(MEmotion mEmotion) {
        try{
            if(isValid(mEmotion))
            {
                // after_feel_llay.setVisibility(View.VISIBLE);
                defSetText(after_emotion_ctxv,mEmotion.getTitle());

                if(isCustomer) {
                    afterFeelSelectionCtxv.setText("How were you feeling after the call ?");
                    after_feel_ctxv.setText("");
                    after_feel_llay.setVisibility(View.GONE);
                }
                else {
                    after_feel_ctxv.setText("How was the  customer feeling after the call ?");
                    after_feel_llay.setVisibility(View.VISIBLE);
                    afterFeelSelectionCtxv.setText("How was the customer feeling after the call in your opinion?");
                    String afterImgUrl = GLOBAL_IMAGE_URL + mEmotion.getImage();
                    if(isValid(afterImgUrl))
                        Picasso.with(SummaryDetailActivity.this).load(afterImgUrl).placeholder(R.drawable.sad).into(afterFeelDimv);
                }
            }
            else
            {
                after_feel_llay.setVisibility(View.GONE);
            }
        }catch (Exception e){
            LogUtils.LOGD("session","SDA setAfterEmotionViews exce " + e.getMessage());
        }
    }

    private void setBeforeEmotionViews(MEmotion mEmotion) {
        try{
            if(isValid(mEmotion))
            {
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
                if(isValid(beforeImgUrl)) Picasso.with(SummaryDetailActivity.this).load(beforeImgUrl).placeholder(R.drawable.sad).into(beforeFeelDimv);
            }
            else {
                before_feel_llay.setVisibility(View.GONE);
            }
        }catch (Exception e){
            LogUtils.LOGD("session","SDA setBeforeEmotionViews exce " + e.getMessage());
        }
    }

    public String getEmotionAfterId()
    {
        String id=null;
        try{

            int session_pos=-1;
            String type=isCustomer?"Customer":"pacifyr";
            for(int i=mSessionDetailRoot.getSessionRatingList().size()-1;i>=0;i--)
            {
                MSessionRating rating=mSessionDetailRoot.getSessionRatingList().get(i);
                if(rating.getType().equalsIgnoreCase(type))
                {
                    session_pos=i;   break;
                }
            }


            //   int session_pos=mSessionDetailRoot.getSessionRatingList().size()-1;
            if(session_pos>=0)
            id=mSessionDetailRoot.getSessionRatingList().get(session_pos).getAfterSessionEmotionId();

        }catch (Exception e)
        {
            LogUtils.LOGD("session","SDA getEmotionAfterId "+e.getMessage());
        }
        return id;
    }
    public String getEmotionCustomerAfterId()
    {
        String id=null;
        try{

            int session_pos=-1;
            String type="Customer";
            for(int i=mSessionDetailRoot.getSessionRatingList().size()-1;i>=0;i--)
            {
                MSessionRating rating=mSessionDetailRoot.getSessionRatingList().get(i);
                if(rating.getType().equalsIgnoreCase(type))
                {
                    session_pos=i;   break;
                }
            }


            //   int session_pos=mSessionDetailRoot.getSessionRatingList().size()-1;
            if(session_pos>=0)
            id=mSessionDetailRoot.getSessionRatingList().get(session_pos).getAfterSessionEmotionId();

        }catch (Exception e)
        {
            LogUtils.LOGD("session","SDA getEmotionCustomerAfterId "+e.getMessage());
        }
        return id;
    }
    public String getEmotionBeforeId()
    {
        String id=null;
        try{
            int session_pos=-1;
            String type="Customer";
            for(int i=mSessionDetailRoot.getSessionRatingList().size()-1;i>=0;i--)
            {
                MSessionRating rating=mSessionDetailRoot.getSessionRatingList().get(i);
                if(rating.getType().equalsIgnoreCase(type))
                {
                    session_pos=i;   break;
                }
            }
            //int session_pos=mSessionDetailRoot.getSessionRatingList().size()-1;
            if(session_pos>=0)
            id=mSessionDetailRoot.getSessionRatingList().get(session_pos).getBeforeSessionEmotionId();

        }catch (Exception e)
        {
            LogUtils.LOGD("session","SDA getEmotionBeforeId"+e.getMessage());
        }
        return id;
    }

    public void onSetValues()
    {
        try{
            LogUtils.LOGD("session", " SDA onSetValues ");
            if (isValid(mSessionDetailRoot)) {
                oldRating=0;

                durationCtxv.setText(mSessionDetailRoot.getDuration());
                if(isCustomer)
                {
                    sessionCostCtxv.setText(Utility.getPriceSign(SummaryDetailActivity.this) + mSessionDetailRoot.getSessionCost());
                    if(isValid( mSessionDetailRoot.getPacifyr())) {
                        String pacName = mSessionDetailRoot.getPacifyr().getFirstName() + " " + mSessionDetailRoot.getPacifyr().getLastName();
                        pacifyrNameCtxv.setText(pacName);
                        pacifyrRateCtxv.setText("$ " + mSessionDetailRoot.getPacifyr().getPrice() + " per hour");
                        String  imagePac =  mSessionDetailRoot.getPacifyr().getImageUrl();
                        if (isValid(imagePac))
                            Picasso.with(SummaryDetailActivity.this).load(imagePac).placeholder(R.drawable.placeholder_user).into(pacifyrImgCimv);
                    }

                }
                else
                {
                    defSetText(earingstCtxv,Utility.getPriceSign(SummaryDetailActivity.this) + (mSessionDetailRoot.getPacifyrEarnings()));
                    defSetText(comapnyfeesCtxv,Utility.getPriceSign(SummaryDetailActivity.this) + (mSessionDetailRoot.getSessionCost()-mSessionDetailRoot.getPacifyrEarnings()));
                    if(isValid( mSessionDetailRoot.getUser())) {
                        String pacName = mSessionDetailRoot.getUser().getFirstName() + " " + mSessionDetailRoot.getUser().getLastName();
                        pacifyrNameCtxv.setText(pacName);
                        String imagePac = mSessionDetailRoot.getUser().getImageUrl();
                        if (isValid(imagePac))
                            Picasso.with(SummaryDetailActivity.this).load(imagePac).placeholder(R.drawable.placeholder_user).into(pacifyrImgCimv);
                    }
                }



                if(isValid(mSessionDetailRoot.getSessionRatingList())) {

                    int session_pos=-1;
                    String type=isCustomer?"Customer":"pacifyr";
                    for(int i=mSessionDetailRoot.getSessionRatingList().size()-1;i>=0;i--)
                    {
                        MSessionRating rating=mSessionDetailRoot.getSessionRatingList().get(i);
                        if(rating.getType().equalsIgnoreCase(type))
                        {
                            session_pos=i;   break;
                        }
                    }
                    if(session_pos>=0) {

                        MSessionRating sessionRating = mSessionDetailRoot.getSessionRatingList().get(session_pos);
                        ratingId=sessionRating.getId();
                        oldRating = Integer.parseInt(sessionRating.getRating());
                        if (isValid(sessionRating.getEffectiveness())) {
                            effectiveness = Integer.parseInt(sessionRating.getEffectiveness());
                            effective_seekbar.setProgress(effectiveness);
                            //effective_circle_view.setProgressValue(effectiveness);
                            circleProgressView.setProgress(effectiveness);
                        }
                        ArrayList<String> cmtlist = sessionRating.getCommentList();
                        ArrayList<MComments> exist_cmt_list = new ArrayList<MComments>();
                        exist_cmt_list.add(new MComments("Excellent call"));
                        exist_cmt_list.add(new MComments("Good quality "));
                        exist_cmt_list.add(new MComments("Audio issues"));
                        exist_cmt_list.add(new MComments("Video issues"));
                        exist_cmt_list.add(new MComments("Other"));

                        LogUtils.LOGD("session","LSA cmtlist sizee "+cmtlist.size());
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
                            }
                        }

                        LogUtils.LOGD("session","LSA exist_cmt_list sizee "+exist_cmt_list.size());
                        commentsAdapter = new CommentsAdapter(SummaryDetailActivity.this, exist_cmt_list, click);
                        commentsRcv.setAdapter(commentsAdapter);
                    }

                    else {


                        commentsAdapter = new CommentsAdapter(SummaryDetailActivity.this,click);
                        commentsRcv.setAdapter(commentsAdapter);

                    }

                }
                else {


                    commentsAdapter = new CommentsAdapter(SummaryDetailActivity.this,click);
                    commentsRcv.setAdapter(commentsAdapter);

                }


                if (oldRating > 0) {
                    rating_bar.setRating(oldRating);
                } else {
                    rating_bar.setRating(1);
                }
            }
            else {


                commentsAdapter = new CommentsAdapter(SummaryDetailActivity.this,click);
                commentsRcv.setAdapter(commentsAdapter);

            }
        }catch (Exception e)
        {
            LogUtils.LOGD("session","SDA onSetValues exce " + e.getMessage());
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
            String url = GLOBAL_URL + "emotions/positive";
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
            AQuery aq = new AQuery(SummaryDetailActivity.this);
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
                // finish();
                return;
            }
            AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    hideProgress();
                    LogUtils.LOGD("session", " SDA webGetSessionDetail callback result" + json + " " + status.getMessage());
                    if(!isValid(json,"Network Interupted"))
                    {
                        //finish();
                        handlerFinish("Network Interupted,please check later");
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
                            //finish();
                            LogUtils.LOGD("session", " SDA webGetSessionDetail callback exce" + e.getMessage());
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
            LogUtils.LOGD("session","SDA webGetSessionDetail exce " + e.getMessage());
            //finish();
        }
    }


    public void defSetText(TextView textv, String text) {
        if(isValid(textv))
            textv.setText(defString(text));

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

    public MEmotion before_emotion=null,after_emotion=null;
    public void setAdapter()
    {
        try {
            LogUtils.LOGD("session", "SDA setAdapter ");

            if(isValid(mSessionDetailRoot)) {
                if(isValid(modelAllEmotions)) {
                    int postion = 0;
                    emotions = modelAllEmotions.getResultsList();


                    try {
                        for (int i = 0; i < emotions.size(); i++) {
                            emotions.get(i).setClicked(false);
                        }
                        if(isValid(getEmotionAfterId())) {
                            for (int i = 0; i < emotions.size(); i++) {
                                emotions.get(i).setClicked(false);
                                if(emotions.get(i).getId().equalsIgnoreCase(getEmotionAfterId())) {
                                    postion = i;
                                    emotions.get(i).setClicked(true);
                                    break;
                                }
                            }
                        }
                        else
                        {
                            afterFeelSelectionLlay.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        LogUtils.LOGD("session", "SDA setAdapter " + e.getMessage());
                    }

                    emotionsListAdapter = new EmotionsListAdapter(emotions, SummaryDetailActivity.this);
                    //postiveEmotions.setClickListener(this);
                    recyclerView.setAdapter(emotionsListAdapter);
                    recyclerView.scrollToPosition(postion);
                    // recyclerView.getLayoutManager().scrollToPositionWithOffset(2, 20);
                    recyclerView.getLayoutManager().scrollToPosition(postion);
                    LogUtils.LOGD("session", "LSA scrollto postion " + postion);
                    try {



                        //     if (!before_feel_llay.isShown()) {
                        before_emotion = getEmotion(emotions);

                        setBeforeEmotionViews(before_emotion);
                   /* if(isValid(before_emotion)) {
                        before_feel_llay.setVisibility(View.VISIBLE);
                        // call_session_detail_root.getSession().getUser().getEmotion().getImage()
                        String beforeImgUrl = GLOBAL_IMAGE_URL + before_emotion.getImage();
                        if(isValid(beforeImgUrl)) Picasso.with(SummaryDetailActivity.this).load(beforeImgUrl).placeholder(R.drawable.sad).into(beforeFeelDimv);
                    } else {
                        before_feel_llay.setVisibility(View.GONE);
                    }*/

                        if(isPacifyer()) {
                            after_emotion = getAfterEmotion(emotions);
                            setAfterEmotionViews(after_emotion);
                    /*    if(isValid(after_emotion)) {
                            after_feel_llay.setVisibility(View.VISIBLE);
                            String afterImgUrl = GLOBAL_IMAGE_URL + after_emotion.getImage();
                            if(isValid(afterImgUrl))
                                Picasso.with(SummaryDetailActivity.this).load(afterImgUrl).placeholder(R.drawable.sad).into(afterFeelDimv);
                        } else {
                            after_feel_llay.setVisibility(View.GONE);
                        }*/

                        }

                    } catch (Exception e) {
                        LogUtils.LOGD("session","SDA setAdapter exce " + e.getMessage());
                    }
                }
            }
        }catch (Exception e)
        {
            LogUtils.LOGD("session","SDA setAdapter exce " + e.getMessage());
        }
    }

    private MEmotion getEmotion(ArrayList<MEmotion> emotions) {
        MEmotion mEmotion=null;
        String emotionid=null;
        try {

            emotionid=getEmotionBeforeId();
            LogUtils.LOGD("session","SDA getEmotion  emotionid"+emotionid);
            for(MEmotion item:emotions)
            {
                if(item.getId().equalsIgnoreCase(emotionid))
                {
                    mEmotion=item;break;
                }
            }
        }catch (Exception e)
        {
            LogUtils.LOGD("session","SDA getEmotion exce " + e.getMessage());
        }
        return mEmotion;
    }
    private MEmotion getAfterEmotion(ArrayList<MEmotion> emotions) {
        MEmotion mEmotion=null;
        String emotionid=null;
        try {

            emotionid=getEmotionCustomerAfterId();
            LogUtils.LOGD("session","SDA getAfterEmotion  emotionid"+emotionid);
            for(MEmotion item:emotions)
            {
                if(item.getId().equalsIgnoreCase(emotionid))
                {
                    mEmotion=item;break;
                }
            }
        }catch (Exception e)
        {
            LogUtils.LOGD("session","SDA getAfterEmotion exce " + e.getMessage());
        }
        return mEmotion;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ratingId=null;
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
        actionBarTopLlay = (LinearLayout) findViewById(R.id.action_bar_top_llay);
        bottomPanel = (RelativeLayout) findViewById(R.id.bottom_panel);
        leftLlay = (LinearLayout) findViewById(R.id.left_llay);
        backImv = (DeviceFitImageView) findViewById(R.id.back_imv);
        leftTxv = (CustomFontTextView) findViewById(R.id.left_txv);
        titleTxv = (CustomFontTextView) findViewById(R.id.title_txv);
        rightLlay = (LinearLayout) findViewById(R.id.right_llay);
        rightTxv = (CustomFontTextView) findViewById(R.id.right_txv);
        rightImv = (DeviceFitImageView) findViewById(R.id.right_imv);

        mSlidingPaneLayout = (WrappingSlidingPaneLayout) findViewById(R.id.sliding_pane);
        rightImv.setVisibility(View.INVISIBLE);
        rightTxv.setVisibility(View.INVISIBLE);
        rightLlay.setVisibility(View.INVISIBLE);
        leftTxv.setVisibility(View.INVISIBLE);
        backImv.setVisibility(View.VISIBLE);
        titleTxv.setVisibility(View.VISIBLE);
        leftLlay.setVisibility(View.VISIBLE);
        defSetText(titleTxv, "CALL SUMMARY");
        defSetText(rightTxv,"Submit");

        rightTxv.setBackground(getResources().getDrawable(R.drawable.blue_border_lightblue));
        // backImv.setImageDrawable(getResources().getDrawable(R.drawable.back2x));
        leftLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPacifyer()){
                    Intent intent = new Intent(SummaryDetailActivity.this, PacifyrDashboardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    //PacifyrDashboardActivity.start(SessionSummaryActivity.this);
                }
                else{
                    Intent intent = new Intent(SummaryDetailActivity.this, DashboardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    // DashboardActivity.start(SessionSummaryActivity.this);
                }
                finish();
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
    public void onNotifyEvent(NotifyEvent event) {
        try {
           /* if(event.isSuccess) {
                setAdapter();
            }*/

        }catch (Exception e)
        {
            LogUtils.LOGD("session","SDA onNotifyEvent exce " + e.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();

    }

    @Override
    public void onAlertTypeEvent(AlertTypeEvent event) {

    }
    public void doNothing(View view) {
        //nothing doing
    }

}

