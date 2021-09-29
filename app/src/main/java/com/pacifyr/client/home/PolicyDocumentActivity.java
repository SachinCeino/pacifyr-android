package com.pacifyr.client.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.pacifyr.client.R;
import com.pacifyr.client.activity.TermsOfUseActivity;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.event.AlertTypeEvent;
import com.pacifyr.client.event.InternetConnectionEvent;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.receiver.NotificationUtils;
import com.pacifyr.client.utility.PacifyrAppCompactActivity;
import com.pacifyr.client.utils.Constants;
import com.pacifyr.client.utils.SnackUtils;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/*
import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.copyrightUrl;
import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.privacypolicyUrl;
import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.termOfUseUrl;
*/

public class PolicyDocumentActivity extends PacifyrAppCompactActivity {

    private LinearLayout topLlay;
    private MainTextView termsCtxv;
    private MainTextView privacyCtxv;
    private MainTextView copyrightCtxv;

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(notificationReceiver, new IntentFilter(NotificationUtils.NOTIFY_INTENT));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy_document);
        onSetActionBar();
        topLlay = findViewById(R.id.top_llay);
        termsCtxv = findViewById(R.id.terms_ctxv);
        privacyCtxv = findViewById(R.id.privacy_ctxv);
        copyrightCtxv = findViewById(R.id.copyright_ctxv);


        termsCtxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(PolicyDocumentActivity.this,TermsOfUseActivity.class);
                if (getPrefManager().getCountry().equals("India") || getPrefManager().getCountry().equals("Wakanda"))
                i.putExtra("spurl", Constants.getUrlTermsIndia(getApplicationContext()));
                else
                    i.putExtra("spurl", Constants.getUrlTerms(getApplicationContext()));
                i.putExtra("title", "Terms of Use");
                startActivity(i);

            }
        });

        privacyCtxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(PolicyDocumentActivity.this,TermsOfUseActivity.class);
                if (getPrefManager().getCountry().equals("India") || getPrefManager().getCountry().equals("Wakanda"))
                i.putExtra("spurl", Constants.getUrlPrivacyPolicyIndia(getApplicationContext()));
                else
                    i.putExtra("spurl", Constants.getUrlPrivacyPolicy(getApplicationContext()));
                i.putExtra("title", "Privacy policy");
                startActivity(i);
            }
        });

        copyrightCtxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(PolicyDocumentActivity.this,TermsOfUseActivity.class);
                if (getPrefManager().getCountry().equals("India") || getPrefManager().getCountry().equals("Wakanda"))
                i.putExtra("spurl", Constants.getUrlDisputePolicyIndia(getApplicationContext()));
                else
                    i.putExtra("spurl", Constants.getUrlDisputePolicy(getApplicationContext()));
                i.putExtra("title", "Copyright");
                startActivity(i);
            }
        });

        topLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        userDetailWeb(getUserID());
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
    private MainTextView leftTxv;
    private MainTextView titleTxv;
    private LinearLayout rightLlay;
    private MainTextView rightTxv;
    private DeviceFitImageView rightImv;

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
            rightTxv.setVisibility(View.INVISIBLE);
            rightLlay.setVisibility(View.INVISIBLE);
            leftTxv.setVisibility(View.INVISIBLE);
            backImv.setVisibility(View.VISIBLE);
            titleTxv.setVisibility(View.VISIBLE);
            leftLlay.setVisibility(View.VISIBLE);
            defSetText(titleTxv, getResources().getString(R.string.Policy_Documents));
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


    @org.greenrobot.eventbus.Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetworkChanged(InternetConnectionEvent event) {
        try {
            if (isValid(event) && isShowDialogs()) {
                if (!event.isConnected())
                {
                    setAlertStatusInWhiteNoInternet("No Internet connection!", "Please turn on internet connection to continue.", "Exit", "Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            hideProgress();
                            hideStatus2();
                            finishAffinity();
                        }
                    });
                }
                else
                {
                    hideProgress();
                    hideStatus2();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private final BroadcastReceiver notificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.hasExtra("message")) {
                String message = intent.getExtras().getString("message");
                String type = intent.getExtras().getString("type");
                if (message != null && message != "") {
                    if (type.equals("chat")) {
                        SnackUtils.show(actionBarTopLlay,message,PolicyDocumentActivity.this);
                    }
                }
            }
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(notificationReceiver);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

    }

}
