package com.pacifyr.omhealthcare.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ceino.utilitylibrary.CustomFontTextView;
import com.ceino.utilitylibrary.DeviceFitImageView;
import com.ceino.utilitylibrary.model.pacifyr.MListNegativeEmotions;
import com.ceino.utilitylibrary.model.pacifyr.MListPositiveEmotions;
import com.ceino.utilitylibrary.utils.PrefManager;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.adapter.GetNegEmotionsWhileCallAdapter;
import com.pacifyr.omhealthcare.adapter.GetPosEmotionWhileCallAdapter;
import com.pacifyr.omhealthcare.event.AlertTypeEvent;
import com.pacifyr.omhealthcare.event.NotifyEvent;
import com.pacifyr.omhealthcare.network.GsonUtils;
import com.pacifyr.omhealthcare.utility.PacifyrAppCompactActivity;

import org.json.JSONObject;

import static com.pacifyr.omhealthcare.network.NetworkService.GLOBAL_URL;


public class ActivityChngEmotionFromCall extends PacifyrAppCompactActivity {

    private RecyclerView mPositiveRcv;
    private RecyclerView mNegativeRcv;
    private PrefManager prefM;
    private MListPositiveEmotions modelListPosEmotions = new MListPositiveEmotions();
    private MListNegativeEmotions modelListNegEmotions = new MListNegativeEmotions();
    private LinearLayout actionBarTopLlay;
    private RelativeLayout bottomPanel;
    private LinearLayout leftLlay;
    private DeviceFitImageView backImv;
    private CustomFontTextView leftTxv;
    private CustomFontTextView titleTxv;
    private LinearLayout rightLlay;
    private CustomFontTextView rightTxv;
    private DeviceFitImageView rightImv;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragent_list_call_change_emotion);
        mNegativeRcv = findViewById(R.id.negative_emotions_rcv);
        mPositiveRcv = findViewById(R.id.positive_emotions_rcv);
        prefM = PrefManager.getInstance(getApplicationContext());

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
                    mNegativeRcv.setLayoutManager(new GridLayoutManager(getApplicationContext(), 6));
                    if (modelListNegEmotions != null && modelListNegEmotions.getResultsList() != null && modelListNegEmotions.getResultsList().size() > 0) {
                        mNegativeRcv.setAdapter(new GetNegEmotionsWhileCallAdapter(ActivityChngEmotionFromCall.this, modelListNegEmotions.getResultsList()));
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
                    mPositiveRcv.setLayoutManager(new GridLayoutManager(getApplicationContext(), 6));
                    if (modelListPosEmotions != null && modelListPosEmotions.getResultsList() != null && modelListPosEmotions.getResultsList().size() > 0)

                    {
                        mPositiveRcv.setAdapter(new GetPosEmotionWhileCallAdapter(ActivityChngEmotionFromCall.this, modelListPosEmotions.getResultsList()));
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
            rightLlay.setVisibility(View.VISIBLE);
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
                    Toast.makeText(ActivityChngEmotionFromCall.this, "Done", Toast.LENGTH_SHORT).show();
                }
            });


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
