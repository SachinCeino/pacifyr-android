package com.pacifyr.client.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MReviews;
import com.pacifyr.client.R;
import com.pacifyr.client.adapter.ReviewListAdapter;
import com.pacifyr.client.event.AlertTypeEvent;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.network.GsonUtils;
import com.pacifyr.client.utility.PacifyrAppCompactActivity;
import com.squareup.otto.Subscribe;

import org.json.JSONObject;

import java.util.ArrayList;

import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.GLOBAL_URL;

public class ReviewListingActivity extends PacifyrAppCompactActivity {

    String url;
    private ArrayList<MReviews.Results> reviewsList;

    RecyclerView reviewListRcv;
    ReviewListAdapter reviewListAdapter;
    RecyclerView.LayoutManager layoutManager;
    String mPacifyrId;
    TextView EmptyLayout;

    public static void start(Context context, String mPacifyrId) {
        Intent intent = new Intent(context, ReviewListingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("mPacifyrId",mPacifyrId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_listing);
        reviewListRcv = findViewById(R.id.review_list_rcv);
        EmptyLayout =findViewById(R.id.EmptyLayout);
        onSetActionBar();

        if (isValid(getIntent()) && isValid(getIntent().getExtras())
        && isValid(getIntent().getExtras().get("mPacifyrId")))
        {
            mPacifyrId=getIntent().getExtras().get("mPacifyrId").toString();
        }

        callApi();

    }

    private void callApi() {
        try{
            if (!isValid(mPacifyrId))
                return;
            setLoading();
            showProgress();
            url = GLOBAL_URL+"rating/getAllReviews/"+mPacifyrId;
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    try {
                        hideProgress();
                        MReviews reviews = GsonUtils.getInstance().gsonToMReviews(json);
                        reviewsList = reviews.getResultsList();
                        if(isValid(reviewsList)) {
                            EmptyLayout.setVisibility(View.GONE);
                            reviewListAdapter = new ReviewListAdapter(ReviewListingActivity.this,reviewsList);
                            layoutManager = new LinearLayoutManager(ReviewListingActivity.this);
                            reviewListRcv.setLayoutManager(layoutManager);
                            reviewListRcv.setAdapter(reviewListAdapter);
                        }
                        else
                        {
                            EmptyLayout.setVisibility(View.VISIBLE);
                        }

                    } catch (Exception e) {
                        hideProgress();
                        EmptyLayout.setVisibility(View.VISIBLE);
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
            EmptyLayout.setVisibility(View.VISIBLE);
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
        defSetText(titleTxv,"REVIEWS");
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
