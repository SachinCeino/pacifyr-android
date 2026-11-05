package com.pacifyr.client.sign;

import com.pacifyr.client.utils.MetaEventLogger;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.pacifyr.client.R;
import com.pacifyr.client.activity.TermsOfUseActivity;
import com.pacifyr.client.base.PacifyrActivity;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;

public class CrisisActivity extends PacifyrActivity {

    private MainTextView suicideUrlCtxv;
    private MainTextView veteransUrlCtxv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crisis);
        onSetActionBar();
        suicideUrlCtxv = findViewById(R.id.suicide_url_ctxv);
        veteransUrlCtxv = findViewById(R.id.veterans_url_ctxv);

        suicideUrlCtxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MetaEventLogger.logContact(CrisisActivity.this, MetaEventLogger.SOURCE_CRISIS_HOTLINE);
                Intent i = new Intent(CrisisActivity.this, TermsOfUseActivity.class);
                i.putExtra("spurl", getResources().getString(R.string.suicide_http_url));
                startActivity(i);
            }
        });

        veteransUrlCtxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MetaEventLogger.logContact(CrisisActivity.this, MetaEventLogger.SOURCE_CRISIS_HOTLINE);
                Intent i = new Intent(CrisisActivity.this, TermsOfUseActivity.class);
                i.putExtra("spurl", getResources().getString(R.string.veterans_http_url));
                startActivity(i);
            }
        });
        userDetailWeb(getUserID());

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
        defSetText(titleTxv, getResources().getString(R.string.crisis_info));
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
