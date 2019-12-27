package com.pacifyr.client.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.pacifyr.client.customview.MainTextView;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;
import com.pacifyr.client.R;
import com.pacifyr.client.base.PacifyrActivity;

import java.net.URISyntaxException;

public class TermsOfUseActivity extends PacifyrActivity {

    private WebView webView;
    private String titleText;
    private String spurl;
    //private ImageView backButt;
    //private LinearLayout backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pacifyr_webview);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            spurl = bundle.getString("spurl");
            if (isValid(bundle.getString("title"))) {
                titleText = bundle.getString("title");
            }
        }
        onSetActionBar();
        webView = findViewById(R.id.web_view);
        startWebView(spurl);

    }

    @Override
    public void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    // Detect when the back button is pressed
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            // Let the system handle the back button
            finish();
        }
    }


    @Override
    protected View getSnackbarAnchorView() {
        return null;
    }


    private void startWebView(String url) {
        webView.loadUrl(url);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);

                if (url.startsWith("http") || url.startsWith("https")) {
                    return true;
                } else {
                    webView.stopLoading();
                    webView.goBack();
                    Toast.makeText(TermsOfUseActivity.this, "Unknown Link, unable to handle", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }

    ///ACTION BAR FEATURES
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
        Typeface type = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.avantica_bold_header));
        titleTxv.setTypeface(type);
        if(isValid(titleText))
            titleTxv.setText(titleText);
        leftLlay.setVisibility(View.VISIBLE);
        leftLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(webView.canGoBack()) {
                    webView.goBack();
                } else {
                    // Let the system handle the back button
                    webView.onPause();
                    finish();
                }
            }
        });
    }
}
