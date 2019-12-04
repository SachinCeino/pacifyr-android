package com.pacifyr.client.sign;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.pacifyr.client.customview.MainTextView;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;
import com.pacifyr.client.R;
import com.pacifyr.client.activity.TermsOfUseActivity;
import com.pacifyr.client.base.PacifyerActivity;

public class CrisisActivity extends PacifyerActivity {

    private MainTextView suicideUrlCtxv;
    private MainTextView veteransUrlCtxv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crisis);
        onSetActionBar();
        suicideUrlCtxv = (MainTextView) findViewById(R.id.suicide_url_ctxv);
        veteransUrlCtxv = (MainTextView) findViewById(R.id.veterans_url_ctxv);

        suicideUrlCtxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(CrisisActivity.this,TermsOfUseActivity.class);
                i.putExtra("spurl", getResources().getString(R.string.suicide_http_url));
                startActivity(i);
            }
        });

        veteransUrlCtxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(CrisisActivity.this,TermsOfUseActivity.class);
                i.putExtra("spurl", getResources().getString(R.string.veterans_http_url));
                startActivity(i);
            }
        });

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
        actionBarTopLlay = (LinearLayout) findViewById(R.id.action_bar_top_llay);
        bottomPanel = (RelativeLayout) findViewById(R.id.bottom_panel);
        leftLlay = (LinearLayout) findViewById(R.id.left_llay);
        backImv = (DeviceFitImageView) findViewById(R.id.back_imv);
        leftTxv = (MainTextView) findViewById(R.id.left_txv);
        titleTxv = (MainTextView) findViewById(R.id.title_txv);
        rightLlay = (LinearLayout) findViewById(R.id.right_llay);
        rightTxv = (MainTextView) findViewById(R.id.right_txv);
        rightImv = (DeviceFitImageView) findViewById(R.id.right_imv);
        rightImv.setVisibility(View.INVISIBLE);
        rightTxv.setVisibility(View.INVISIBLE);
        rightLlay.setVisibility(View.INVISIBLE);
        leftTxv.setVisibility(View.INVISIBLE);
        backImv.setVisibility(View.VISIBLE);
        titleTxv.setVisibility(View.VISIBLE);
        leftLlay.setVisibility(View.VISIBLE);
        defSetText(titleTxv,getResources().getString(R.string.crisis_info));
        Typeface type = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.avantica_bold_header));
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
