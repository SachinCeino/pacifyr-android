package com.pacifyr.client.home;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;
import com.pacifyr.client.R;
import com.pacifyr.client.adapter.LearnMoreAdapter;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.event.AlertTypeEvent;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.model.UserProfile;
import com.pacifyr.client.utility.PacifyrAppCompactActivity;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MUser;

import org.json.JSONObject;

public class LearnMoreActivity extends PacifyrAppCompactActivity  {


    public static final String SIMPLE_THERAPY_GOOGLE_PLAY = "https://play.google" +
            ".com/store/apps/details?id=com.simpletherapy.android.mhealth";
    private MUser pacifyerObject;
    private VideoView mVideoView;
    public static final String MOMSEZE_GOOGLE_PLAY = "https://play.google" +
            ".com/store/apps/details?id=com.mobileapp.mom.momseze";
    private static final String TAG = LearnMoreActivity.class.getSimpleName();
    private RecyclerView content_view;
    private LearnMoreAdapter adapter;
    private TextView button_get_started;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_more);
        button_get_started = findViewById(R.id.button_get_started);
        content_view = findViewById(R.id.contentview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        content_view.setLayoutManager(linearLayoutManager);
        content_view.setItemAnimator(new DefaultItemAnimator());

        onSetActionBar();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (isValid(bundle)) {
            pacifyerObject = (MUser) bundle.getSerializable("Object");
        }
        if (isValid(pacifyerObject)) {
            initUI(pacifyerObject);
        }



    }

    private void initUI(MUser pacifyerObject) {

        setLoading();
        showProgress();
        getUserProfile(pacifyerObject.getId(), new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                hideProgress();
                if (json != null) {
                    UserProfile userProfile = new Gson().fromJson(json.toString(),
                            UserProfile.class);
                    Log.d(TAG, "user " + userProfile.toString());

                    if (userProfile != null && userProfile.getLearnMore() != null) {
                        adapter = new LearnMoreAdapter(LearnMoreActivity.this,
                                userProfile.getLearnMore());
                        if (adapter != null) {
                            content_view.setAdapter(adapter);
                        }

                        button_get_started.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (userProfile.getLearnMore().get(0).getTitle().equalsIgnoreCase(
                                        "simpletherapy")) {
                                    String pkg = SIMPLE_THERAPY_GOOGLE_PLAY.split("=")[1];
                                    try {
                                        startActivity(getPackageManager().getLaunchIntentForPackage(pkg));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(SIMPLE_THERAPY_GOOGLE_PLAY)));
                                    }
                                } else if (userProfile.getLearnMore().get(0).getTitle().equalsIgnoreCase(
                                        "momseze")) {
                                    String pkg = MOMSEZE_GOOGLE_PLAY.split("=")[1];
                                    try {
                                        startActivity(getPackageManager().getLaunchIntentForPackage(pkg));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(MOMSEZE_GOOGLE_PLAY)));
                                    }
                                }

                            }
                        });
                    } else {
                        Log.d(TAG, "user = null");
                    }
                }
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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

    @Override
    protected View getSnackbarAnchorView() {
        return null;
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
        rightImv.setVisibility(View.INVISIBLE);
        rightTxv.setVisibility(View.INVISIBLE);
        rightLlay.setVisibility(View.INVISIBLE);
        leftTxv.setVisibility(View.INVISIBLE);
        backImv.setVisibility(View.VISIBLE);
        titleTxv.setVisibility(View.VISIBLE);
        leftLlay.setVisibility(View.VISIBLE);
        defSetText(titleTxv, getResources().getString(R.string.learnmore));
        Typeface type = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.avantica_bold_header));
        titleTxv.setTypeface(type);
        //backImv.setBackgroundResource(R.drawable.leftmen);
        leftLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }




}
