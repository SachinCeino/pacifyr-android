package com.pacifyr.omhealthcare.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.pacifyr.omhealthcare.customview.MainTextView;
import com.ceino.utilitylibrary.DeviceFitImageView;
import com.ceino.utilitylibrary.model.pacifyr.MReviews;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.adapter.ReviewListAdapter;
import com.pacifyr.omhealthcare.event.AlertTypeEvent;
import com.pacifyr.omhealthcare.event.NotifyEvent;
import com.pacifyr.omhealthcare.network.GsonUtils;
import com.pacifyr.omhealthcare.utility.PacifyrAppCompactActivity;
import com.squareup.otto.Subscribe;

import org.json.JSONObject;

import java.util.ArrayList;

import static com.ceino.utilitylibrary.network.UtilityNetworkService.GLOBAL_URL;

public class ReviewListingActivity extends PacifyrAppCompactActivity {

    String url;
    private ArrayList<MReviews.Results> reviewsList;

    RecyclerView reviewListRcv;
    ReviewListAdapter reviewListAdapter;
    RecyclerView.LayoutManager layoutManager;

    public static void start(Context context) {
        Intent intent = new Intent(context, ReviewListingActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_listing);
        reviewListRcv = findViewById(R.id.review_list_rcv);
        onSetActionBar();

        callApi();

    }

    private void callApi() {
        try{
            setLoading();
            showProgress();
            url = GLOBAL_URL+"rating/getAllReviews/"+getPrefManager().getUserID();
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    try {
                        hideProgress();
                        MReviews reviews = GsonUtils.getInstance().gsonToMReviews(json);
                        reviewsList = reviews.getResultsList();
                        if(isValid(reviewsList)) {
                            reviewListAdapter = new ReviewListAdapter(ReviewListingActivity.this,reviewsList);
                            layoutManager = new LinearLayoutManager(ReviewListingActivity.this);
                            reviewListRcv.setLayoutManager(layoutManager);
                            reviewListRcv.setAdapter(reviewListAdapter);
                        }

                    } catch (Exception e) {
                        hideProgress();
                        e.printStackTrace();
                    }
                }
            };
            AQuery aq = new AQuery(ReviewListingActivity.this);
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken",getPrefManager().getAccessToken());
            aq.ajax(url,JSONObject.class, cb);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.d("API CALL",e.getMessage());
        }
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
        rightImv.setVisibility(View.VISIBLE);
        rightTxv.setVisibility(View.INVISIBLE);
        rightLlay.setVisibility(View.VISIBLE);
        leftTxv.setVisibility(View.INVISIBLE);
        backImv.setVisibility(View.VISIBLE);
        titleTxv.setVisibility(View.VISIBLE);
        leftLlay.setVisibility(View.VISIBLE);
        defSetText(titleTxv,"Review");
        titleTxv.setTextColor(getResources().getColor(R.color.white));
        backImv.setVisibility(View.VISIBLE);
        leftTxv.setVisibility(View.GONE);
        backImv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        leftLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onSetValues() throws Exception {

    }

    @Subscribe
    public void onNotifyEvent(NotifyEvent event) {

    }

    @Override
    public void onAlertTypeEvent(AlertTypeEvent event) {

    }

    @Override
    protected View getSnackbarAnchorView() {
        return null;
    }
}
