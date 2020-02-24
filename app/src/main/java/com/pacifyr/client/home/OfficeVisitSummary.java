package com.pacifyr.client.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.eralp.circleprogressview.CircleProgressView;
import com.pacifyr.client.R;
import com.pacifyr.client.adapter.CommentsAdapter;
import com.pacifyr.client.adapter.EmotionsListAdapter;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.event.AlertTypeEvent;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.network.GsonUtils;
import com.pacifyr.client.quickblox.App;
import com.pacifyr.client.utility.PacifyrAppCompactActivity;
import com.pacifyr.client.utils.LogUtils;
import com.pacifyr.client.utils.Utility;
import com.pacifyr.client.utils.WrappingSlidingPaneLayout;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;
import com.pacifyrapp.utilitylibrary.ToastHandler;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MEmotion;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MSessionDetailRoot;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MSessionRating;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.pacifyr.client.network.NetworkService.GLOBAL_URL;
import static com.pacifyr.client.network.NetworkService.modelAllEmotions;

/**
 * Created by Mani-Ceino on 12/22/2017.
 */

public class OfficeVisitSummary extends PacifyrAppCompactActivity {


    private static final String TAG = OfficeVisitSummary.class.getSimpleName();
    public static boolean isAudioCall = true;
    public static String ratingId = null;
    public static boolean mysessions = false;
    public MSessionDetailRoot mSessionDetailRoot;
    public boolean isCustomer = false;
    boolean showingFirst = true;
    EmotionsListAdapter emotionsListAdapter;
    RecyclerView recyclerView;
    RecyclerView commentsRcv;
    CommentsAdapter commentsAdapter;
    WrappingSlidingPaneLayout mSlidingPaneLayout;
    boolean onceBackpressed = false;
    CircleImageView img_blue_bg, img_blue_bg_after;
    ArrayList<MEmotion> emotions;
    private int rating;
    private int effectiveness = 0;
    private CircleImageView pacifyrImgCimv;
    private MainTextView pacifyrNameCtxv;
    private MainTextView durationCtxv;
    private int oldRating;
    private ImageView callTypeDimv;
    private CircleProgressView circleProgressView;
    private ImageButton img_notes;
    private boolean editable = true;
    private MainTextView text_notes;
    private LinearLayout actionBarTopLlay;
    private RelativeLayout bottomPanel;
    private LinearLayout leftLlay;
    private DeviceFitImageView backImv;
    private MainTextView leftTxv;
    private MainTextView titleTxv;
    private LinearLayout rightLlay;
    private MainTextView rightTxv;
    private DeviceFitImageView rightImv;

    public static void start(Context context, boolean isAudioCall) {
        Intent intent = new Intent(context, OfficeVisitSummary.class);
        //  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        //  intent.putExtra(Consts.EXTRA_IS_STARTED_FOR_CALL, isRunForCall);
        OfficeVisitSummary.isAudioCall = isAudioCall;
      /*  try {
            Activity activity = null;
            activity = ((AppChatNotUSe) AppChatNotUSe.getInstance()).getCurrentActivity();
            if(isValidStatic(activity))
                if (activity instanceof CallActivity) {
                    activity.finish();
                }
        } catch (Exception e) {
            e.printStackTrace();
        }*/


        context.startActivity(intent);
    }

    public static Boolean isValidStatic(Object object) {
        return object != null;

    }

    public static void start(Context context) {
        Intent intent = new Intent(context, OfficeVisitSummary.class);
        //  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        // intent.putExtra(Consts.EXTRA_IS_STARTED_FOR_CALL, isRunForCall);
        OfficeVisitSummary.isAudioCall = true;


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

    public static void start(Context context, String sessionID) {
        Intent intent = new Intent(context, OfficeVisitSummary.class);
        //  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        // intent.putExtra(Consts.EXTRA_IS_STARTED_FOR_CALL, isRunForCall);
        intent.putExtra("id", sessionID);
        OfficeVisitSummary.isAudioCall = true;
        mysessions = true;
        sessionid = sessionID;
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

    public static void start(Context context, String sessionID, boolean editable) {
        Intent intent = new Intent(context, OfficeVisitSummary.class);
        //  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        // intent.putExtra(Consts.EXTRA_IS_STARTED_FOR_CALL, isRunForCall);
        intent.putExtra("id", sessionID);
        intent.putExtra("editable", editable);
        OfficeVisitSummary.isAudioCall = true;
        mysessions = true;
        sessionid = sessionID;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (isValid(getIntent().getExtras().getString("id"))) {
            sessionid = getIntent().getExtras().getString("id") + "";
        }

        if (getIntent().getExtras().containsKey("editable")) {
            editable = getIntent().getExtras().getBoolean("editable");
        }
        ratingId = null;
        LogUtils.LOGD("session", " LSa onCreate sessionid " + sessionid);
        if (!isValid(sessionid, "Please check later")) {
            LogUtils.LOGD("session", " LSa onCreate sessionid not valid  " + sessionid);
            //  handlerFinish("Error in last session ");
            handlerFinish("Data not updated in last session ");

        }
        LogUtils.LOGD("session", " LSa onCreate sessionid " + sessionid);
        setContentView(R.layout.activity_office_visit_summary);

        if (!isCustomer() && !isPacifyer()) {
            ToastHandler.newInstance(this).mustShowToast("Session Expired : Please login again");
            deleteInstallationWeb();
            finish();
        }
        isCustomer = isCustomer();


        // webListEmotions(LastSessionActivity.this);
        onCreateView();
        if (isValid(sessionid)) webGetSessionDetail(sessionid);
        else {
            handlerFinish("Data not updated in last session ");
            // finish();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        App.getInstance().setCurrentActivity(this);
    }

    @Override
    protected View getSnackbarAnchorView() {
        return null;
    }

    public void onCreateView() {
        try {

            LogUtils.LOGD("session", " SSA onCreateView ");
            onSetActionBar();
            circleProgressView = findViewById(R.id.circle_progress_view);
            pacifyrImgCimv = findViewById(R.id.last_session_cimv);
            callTypeDimv = findViewById(R.id.call_type_dimv);
            img_blue_bg = findViewById(R.id.img_blue_bg);
            img_blue_bg_after = findViewById(R.id.img_blue_bg_after);

            pacifyrNameCtxv = findViewById(R.id.pacifyr_name_ctxv);
            durationCtxv = findViewById(R.id.duration_ctxv);


            img_notes = findViewById(R.id.img_notes);
            img_notes.setVisibility(View.GONE);


            recyclerView.setLayoutManager(new LinearLayoutManager(OfficeVisitSummary.this, LinearLayoutManager.HORIZONTAL, false));
            commentsRcv.setLayoutManager(new LinearLayoutManager(OfficeVisitSummary.this, LinearLayoutManager.HORIZONTAL, false));
            commentsRcv.setNestedScrollingEnabled(false);
            recyclerView.setNestedScrollingEnabled(false);
            rating = 5;

            if (isValid(mSessionDetailRoot)) {
                onSetValues();
            }

        } catch (Exception e) {
            LogUtils.LOGD("session", "SSA onCreateView exce " + e.getMessage());
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
                    if (isValid(mSessionDetailRoot.getPacifyr())) {
                        String pacName = mSessionDetailRoot.getPacifyr().getFirstName() + " " + mSessionDetailRoot.getPacifyr().getLastName();
                        pacifyrNameCtxv.setText(pacName);
                        String imagePac = mSessionDetailRoot.getPacifyr().getImageUrl();
                        if (isValid(imagePac))
                            Picasso.with(OfficeVisitSummary.this).load(imagePac).placeholder(R.drawable.placeholder_user).into(pacifyrImgCimv);
                    }


                } else {
                    if (isValid(mSessionDetailRoot.getUser())) {
                        String pacName = mSessionDetailRoot.getUser().getFirstName() + " " + mSessionDetailRoot.getUser().getLastName();
                        pacifyrNameCtxv.setText(pacName);
                        String imagePac = mSessionDetailRoot.getUser().getImageUrl();
                        if (isValid(imagePac))
                            Picasso.with(OfficeVisitSummary.this).load(imagePac).placeholder(R.drawable.placeholder_user).into(pacifyrImgCimv);

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

                        if (isValid(sessionRating.getComment())) {
                            text_notes.setText(sessionRating.getComment());
                        }
                        ratingId = sessionRating.getId();
                        oldRating = Integer.parseInt(sessionRating.getRating());
                        if (isValid(sessionRating.getEffectiveness())) {
                            effectiveness = Integer.parseInt(sessionRating.getEffectiveness());
                            //effective_circle_view.setProgressValue(effectiveness);
                        }

                    }

                }

            }
        } catch (Exception e) {
            LogUtils.LOGD("session", "SSA onSetValues exce " + e.getMessage());
        }
    }

    public int getEmotionList() {
        int i = -1;
        try {

            if (isValid(modelAllEmotions)) {
                emotions = modelAllEmotions.getResultsList();
                i = emotions.size();
            }
        } catch (Exception e) {
            i = -1;
        }
        return i;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ratingId = null;
    }

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
        text_notes = findViewById(R.id.text_notes);
        mSlidingPaneLayout = findViewById(R.id.sliding_pane);
        rightImv.setVisibility(View.INVISIBLE);
        rightTxv.setVisibility(View.VISIBLE);
        rightLlay.setVisibility(View.VISIBLE);
        leftTxv.setVisibility(View.INVISIBLE);
        backImv.setVisibility(View.VISIBLE);
        titleTxv.setVisibility(View.VISIBLE);
        leftLlay.setVisibility(View.VISIBLE);
        defSetText(titleTxv, "OFFICE VISIT");
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

        rightTxv.setVisibility(View.GONE);
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


}

