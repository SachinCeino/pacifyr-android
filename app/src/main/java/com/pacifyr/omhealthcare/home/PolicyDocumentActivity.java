package com.pacifyr.omhealthcare.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ceino.utilitylibrary.CustomFontTextView;
import com.ceino.utilitylibrary.DeviceFitImageView;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.activity.TermsOfUseActivity;
import com.pacifyr.omhealthcare.event.AlertTypeEvent;
import com.pacifyr.omhealthcare.event.NotifyEvent;
import com.pacifyr.omhealthcare.utility.PacifyrAppCompactActivity;
import com.pacifyr.omhealthcare.utils.Constants;

import org.greenrobot.eventbus.Subscribe;

/*
import static com.ceino.utilitylibrary.network.UtilityNetworkService.copyrightUrl;
import static com.ceino.utilitylibrary.network.UtilityNetworkService.privacypolicyUrl;
import static com.ceino.utilitylibrary.network.UtilityNetworkService.termOfUseUrl;
*/

public class PolicyDocumentActivity extends PacifyrAppCompactActivity {

    private LinearLayout topLlay;
    private CustomFontTextView termsCtxv;
    private CustomFontTextView privacyCtxv;
    private CustomFontTextView copyrightCtxv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy_document);
        onSetActionBar();
        topLlay = (LinearLayout) findViewById(R.id.top_llay);
        termsCtxv = (CustomFontTextView) findViewById(R.id.terms_ctxv);
        privacyCtxv = (CustomFontTextView) findViewById(R.id.privacy_ctxv);
        copyrightCtxv = (CustomFontTextView) findViewById(R.id.copyright_ctxv);


        termsCtxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(PolicyDocumentActivity.this,TermsOfUseActivity.class);
                i.putExtra("spurl", Constants.URL_TERMS);
                i.putExtra("title","policy documents");
                startActivity(i);

            }
        });

        privacyCtxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(PolicyDocumentActivity.this,TermsOfUseActivity.class);
                i.putExtra("spurl", Constants.URL_PRIVACY_POLICY);
                i.putExtra("title","policy documents");
                startActivity(i);
            }
        });

        copyrightCtxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(PolicyDocumentActivity.this,TermsOfUseActivity.class);
                i.putExtra("spurl", Constants.URL_DISPUTE_POLICY);
                i.putExtra("title","policy documents");
                startActivity(i);
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

    public static void start(Context context) {
        Intent intent = new Intent(context, PolicyDocumentActivity.class);
        context.startActivity(intent);
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

    public void onSetActionBar() {
        try {
            actionBarTopLlay = (LinearLayout) findViewById(R.id.action_bar_top_llay);
            bottomPanel = (RelativeLayout) findViewById(R.id.bottom_panel);
            leftLlay = (LinearLayout) findViewById(R.id.left_llay);
            backImv = (DeviceFitImageView) findViewById(R.id.back_imv);
            leftTxv = (CustomFontTextView) findViewById(R.id.left_txv);
            titleTxv = (CustomFontTextView) findViewById(R.id.title_txv);
            rightLlay = (LinearLayout) findViewById(R.id.right_llay);
            rightTxv = (CustomFontTextView) findViewById(R.id.right_txv);
            rightImv = (DeviceFitImageView) findViewById(R.id.right_imv);

            rightImv.setVisibility(View.INVISIBLE);
            rightTxv.setVisibility(View.INVISIBLE);
            rightLlay.setVisibility(View.INVISIBLE);
            leftTxv.setVisibility(View.INVISIBLE);
            backImv.setVisibility(View.VISIBLE);
            titleTxv.setVisibility(View.VISIBLE);
            leftLlay.setVisibility(View.VISIBLE);
            defSetText(titleTxv,getResources().getString(R.string.policy_documents));
            Typeface type = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.avantica_bold_header));
            titleTxv.setTypeface(type);
            backImv.setImageDrawable(getResources().getDrawable(R.drawable.backarrow));
            //backImv.setBackgroundResource(R.drawable.leftmen);
            leftLlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }


}
