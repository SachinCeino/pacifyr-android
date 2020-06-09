package com.pacifyr.client.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MListNegativeEmotions;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MListPositiveEmotions;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.pacifyr.client.R;
import com.pacifyr.client.adapter.GetNegEmotionsWhileCallAdapter;
import com.pacifyr.client.adapter.GetPosEmotionWhileCallAdapter;
import com.pacifyr.client.event.AlertTypeEvent;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.network.GsonUtils;
import com.pacifyr.client.utility.PacifyrAppCompactActivity;

import org.json.JSONObject;

import static com.pacifyr.client.network.NetworkService.GLOBAL_URL;


public class ActivityChangeEmotionFromCall extends PacifyrAppCompactActivity {

    private RecyclerView mPositiveRcv;
    private RecyclerView mNegativeRcv;
    private PrefManager prefM;
    private MListPositiveEmotions modelListPosEmotions = new MListPositiveEmotions();
    private MListNegativeEmotions modelListNegEmotions = new MListNegativeEmotions();
    private LinearLayout actionBarTopLlay;
    private RelativeLayout bottomPanel;
    private LinearLayout leftLlay;
    private DeviceFitImageView backImv;
    private MainTextView leftTxv;
    private MainTextView titleTxv;
    private LinearLayout rightLlay;
    private MainTextView rightTxv;
    private DeviceFitImageView rightImv;
    private String current_emotion;
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.w("@@@","onStop");
        /**pip mode open so call activity is opened ,
         * that time this activity is no need
         * so goback.**/
        super.onBackPressed();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragent_list_call_change_emotion);
        mNegativeRcv = findViewById(R.id.negative_emotions_rcv);
        mPositiveRcv = findViewById(R.id.positive_emotions_rcv);
        prefM = PrefManager.getInstance(getApplicationContext());
        if(isValid(getIntent().getExtras().get("emotion")))
        {
            current_emotion=getIntent().getExtras().get("emotion").toString();
        }
        onSetActionBar();
        setNegativeAdapter();
        setPositiveAdapter();


    }

    @Override
    protected View getSnackbarAnchorView() {
        return null;
    }

    @Override
    public void onSetValues() throws Exception {

    }

    @Override
    public void onNotifyEvent(NotifyEvent event) {

    }

    @Override
    public void onAlertTypeEvent(AlertTypeEvent event) {

    }

    public void setNegativeAdapter() {
        showProgress();
        String url = GLOBAL_URL + "emotions/negative";
        AjaxCallback cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                try {
                    hideProgress();
                    modelListNegEmotions = GsonUtils.getInstance().gsonToMListNegativeEmotions(json);
                    mNegativeRcv.setLayoutManager(new GridLayoutManager(getApplicationContext(), 5));
                    if (modelListNegEmotions != null && modelListNegEmotions.getResultsList() != null && modelListNegEmotions.getResultsList().size() > 0) {
                        mNegativeRcv.setAdapter(new GetNegEmotionsWhileCallAdapter(ActivityChangeEmotionFromCall.this, modelListNegEmotions.getResultsList()));

                        ((GetNegEmotionsWhileCallAdapter)mNegativeRcv.getAdapter()).setSelected(current_emotion);
                    }


                } catch (Exception e) {
                    //hideProgress();
                    e.printStackTrace();
                }
            }
        };
        AQuery aq = new AQuery(getApplicationContext());
        cb.header("Content-Type", "application/json");
        cb.header("AccessToken", prefM.getAccessToken());
        aq.ajax(url, JSONObject.class, cb);
    }

    public void setPositiveAdapter() {
        showProgress();
        String url = GLOBAL_URL + "emotions/positive";
        AjaxCallback cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                hideProgress();
                try {
                    modelListPosEmotions = GsonUtils.getInstance().gsonToMListPositiveEmotions(json);
                    mPositiveRcv.setLayoutManager(new GridLayoutManager(getApplicationContext(), 5));
                    if (modelListPosEmotions != null && modelListPosEmotions.getResultsList() != null && modelListPosEmotions.getResultsList().size() > 0)

                    {
                        mPositiveRcv.setAdapter(new GetPosEmotionWhileCallAdapter(ActivityChangeEmotionFromCall.this, modelListPosEmotions.getResultsList()));
                        ((GetPosEmotionWhileCallAdapter)mPositiveRcv.getAdapter()).setSelected(current_emotion);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        AQuery aq = new AQuery(getApplicationContext());
        cb.header("Content-Type", "application/json");
        //cb.header("AccessToken",token);
        cb.header("AccessToken", prefM.getAccessToken());
        aq.ajax(url, JSONObject.class, cb);
    }

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

            rightImv.setVisibility(View.INVISIBLE);
            rightTxv.setVisibility(View.VISIBLE);
            rightLlay.setVisibility(View.GONE);
            leftTxv.setVisibility(View.INVISIBLE);
            backImv.setVisibility(View.VISIBLE);
            titleTxv.setVisibility(View.VISIBLE);
            leftLlay.setVisibility(View.VISIBLE);
            defSetText(titleTxv, "Change Emotion");
            defSetText(rightTxv, "Done");
            backImv.setImageDrawable(getResources().getDrawable(R.drawable.backarrow));
            //backImv.setBackgroundResource(R.drawable.leftmen);
            leftLlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            rightLlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(ActivityChangeEmotionFromCall.this, "Done", Toast.LENGTH_SHORT).show();
                }
            });


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
