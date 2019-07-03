package com.pacifyr.omhealthcare.home.notes;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
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
import com.ceino.utilitylibrary.utils.PrefManager;
import com.eralp.circleprogressview.CircleProgressView;
import com.google.gson.Gson;
import com.jesusm.holocircleseekbar.lib.HoloCircleSeekBar;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.adapter.CommentsAdapter;
import com.pacifyr.omhealthcare.adapter.EmotionsListAdapter;
import com.pacifyr.omhealthcare.event.AlertTypeEvent;
import com.pacifyr.omhealthcare.event.NotifyEvent;
import com.pacifyr.omhealthcare.model.MComments;
import com.pacifyr.omhealthcare.model.interfaces.CommentClick;
import com.pacifyr.omhealthcare.network.GsonUtils;
import com.pacifyr.omhealthcare.network.NetworkService;
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

/**
 * Created by Mani-Ceino on 12/22/2017.
 */

public class NotesActivity extends PacifyrAppCompactActivity {

    public static Boolean isValidStatic(Object object) {
        if (object != null) return true;
        return false;

    }

    public static void start(Context context, String sessionID) {
        Intent intent = new Intent(context, NotesActivity.class);
        intent.putExtra("id", sessionID);
        context.startActivity(intent);
    }

    private String sessionid;

    private CustomFontTextView submit_notes;
    private CustomEditText etv_notes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        submit_notes = findViewById(R.id.submit_notes);
        etv_notes = findViewById(R.id.etv_notes);
        if (isValid(getIntent().getExtras().getString("id"))) {
            sessionid = getIntent().getExtras().getString("id") + "";
        }


        onCreateView();


        if (isValid(sessionid))
            webGetNotes(sessionid);
        submit_notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text = etv_notes.getText().toString();
                if (isValid(text))
                    SubmitNoteToServer(text);
                else
                    etv_notes.setError("Add notes!");
            }
        });
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


    public void onCreateView() {
        try {


            onSetActionBar();


        } catch (Exception e) {
            LogUtils.LOGD("session", "SSA onCreateView exce " + e.getMessage());
        }
    }


    public void defSetText(TextView textv, String text) {
        if (isValid(textv))
            textv.setText(defString(text));

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onSetValues() throws Exception {

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
        submit_ctxv = (CustomFontTextView) findViewById(R.id.submit_ctxv);
        rightImv.setVisibility(View.INVISIBLE);
        rightTxv.setVisibility(View.VISIBLE);
        rightLlay.setVisibility(View.VISIBLE);
        leftTxv.setVisibility(View.INVISIBLE);
        backImv.setVisibility(View.VISIBLE);
        titleTxv.setVisibility(View.VISIBLE);
        leftLlay.setVisibility(View.VISIBLE);
        defSetText(titleTxv, "Notes");
        defSetText(rightTxv, "Update");
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
        rightLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit_notes.performClick();
            }
        });


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


    public void webGetNotes(final String sessionid) {
        try {
            setLoading();
            showProgress();
            String user_Id = getPrefManager().getUserID();
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

                            Notes notes = GsonUtils.getInstance().gsonToNotes(json);
                            if (isValid(notes.getResults())) {
                                StringBuffer notesText = new StringBuffer();
                                for (int i = 0; i < notes.getResults().size(); i++) {
                                    if (isValid(notes.getResults().get(i).getText())) {
                                        notesText.append(notes.getResults().get(i).getText());
                                    }
                                }

                                if (isValid(notesText)) {
                                    //notes_Layout.setVisibility(View.VISIBLE);
                                    etv_notes.setText(notesText);
                                }

                            } else {
                                // notes_Layout.setVisibility(View.GONE);
                            }
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
            String url = "notes/list?userId=" + user_Id + "&sessionId=" + sessionid;
            aq.ajax(GLOBAL_URL + url, JSONObject.class, cb);
        } catch (Exception e) {
            hideProgress();
            LogUtils.LOGD("session", "SSA webGetSessionDetail exce " + e.getMessage());
            //finish();
        }
    }


    private void SubmitNoteToServer(String notes) {


        try {
            PrefManager prefM = PrefManager.getInstance(getApplicationContext());
            String userToken = prefM.getAccessToken();
            String userid = prefM.getUserID();
            String url = NetworkService.GLOBAL_URL + "notes/add";
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    try {
                        Toast.makeText(getApplicationContext(), "Notes Updated", Toast.LENGTH_SHORT).show();

                        finish();

                    } catch (Exception e) {
                        errorStatus();
                    }
                }
            };
            AQuery aq = new AQuery(getApplicationContext());
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", userToken);

            JSONObject jsonObject = new JSONObject();
            jsonObject.putOpt("userId", userid);
            jsonObject.putOpt("sessionId", sessionid);
            jsonObject.putOpt("text", notes);
            aq.post(url, jsonObject, JSONObject.class, cb);
        } catch (Exception ex) {
            errorStatus();
        }
    }


}

