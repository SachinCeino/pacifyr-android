package com.ceino.utilitylibrary.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ceino.utilitylibrary.CustomFontTextView;
import com.ceino.utilitylibrary.DeviceFitImageView;
import com.ceino.utilitylibrary.LogUtilsutility;
import com.ceino.utilitylibrary.R;
import com.ceino.utilitylibrary.base.BaseAppCompactActivity;

/**
 * Created by ceino on 23/7/15.
 */
public class WebActivity extends BaseAppCompactActivity {
    String url;
    final String mimeType = "text/html";
    WebView wv;
    final String encoding = "UTF-8";
   // ProgressDialog pd;
    ImageView backarrow;
    RelativeLayout backarrow_layout;
    public static final String URL="web_url";


    private LinearLayout actionBarTopLlay;
    private RelativeLayout bottomPanel;
    private LinearLayout leftLlay;
    private DeviceFitImageView backImv;
    private CustomFontTextView leftTxv;
    private CustomFontTextView titleTxv;
    private LinearLayout rightLlay;
    private DeviceFitImageView logoImv;
    private DeviceFitImageView rightImv;


    public static void startWebActivity(Context context, String url) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(URL, url);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webactivityframe);
        Intent intent = getIntent();

        setActionBar();


       setLoading();
        url=intent.getStringExtra(URL);
       /* pd=new ProgressDialog(this);
        pd.show();*/

        wv = (WebView) findViewById(R.id.webview1);

        wv.setInitialScale(0);
//		  wv.getSettings().setBuiltInZoomControls(true);
        wv.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
             /*   if(pd.isShowing()&&pd!=null)
                {
                    pd.hide();
                }*/
                hideProgress();

            }

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                showProgress();
               // pd.show();
                // to kill activity
                view.loadUrl(url);
//	                pd.show();
                return true;
            }


        });
        wv.setWebChromeClient(new WebChromeClient(){
            public void onProgressChanged(WebView view, int progress) {
                // Activities and WebViews measure progress with different scales.
                // The progress meter will automatically disappear when we reach 100%
//				     activity.setProgress(progress * 1000);
                showProgress();
             /*   pd.show();
                pd.setProgress(0);
                pd.setProgress(progress * 1000);

                pd.incrementProgressBy(progress);
                if (progress == 100 && pd.isShowing())
                    pd.dismiss();*/
                if (progress == 100)
                    hideProgress();


            }
        });

        wv.getSettings().setJavaScriptEnabled(true);



        wv.loadUrl(url);
//		 wv.requestFocus(View.FOCUS_DOWN);
//		 wv.requestFocus();
        wv.requestFocus(View.FOCUS_DOWN|View.FOCUS_UP);
//		 wv.requestFocus(View.FOCUSABLES_ALL);


        WebSettings wbsettings = wv.getSettings();
        wbsettings.setBuiltInZoomControls(true);
    }

    private void setActionBar() {
        try{
            actionBarTopLlay = (LinearLayout) findViewById(R.id.action_bar_top_llay);
            bottomPanel = (RelativeLayout) findViewById(R.id.bottom_panel);
            leftLlay = (LinearLayout) findViewById(R.id.left_llay);
            backImv = (DeviceFitImageView) findViewById(R.id.back_imv);
            leftTxv = (CustomFontTextView) findViewById(R.id.left_txv);
            titleTxv = (CustomFontTextView) findViewById(R.id.title_txv);
            rightLlay = (LinearLayout) findViewById(R.id.right_llay);
            logoImv = (DeviceFitImageView) findViewById(R.id.logo_imv);
            rightImv = (DeviceFitImageView) findViewById(R.id.right_imv);

            rightLlay.setVisibility(View.VISIBLE);
         //   rightTxv.setVisibility(View.INVISIBLE);
            rightImv.setVisibility(View.VISIBLE);
            logoImv.setVisibility(View.VISIBLE);
            titleTxv.setVisibility(View.INVISIBLE);
            leftLlay.setVisibility(View.VISIBLE);
            backImv.setVisibility(View.VISIBLE);
            leftTxv.setVisibility(View.VISIBLE);

            leftTxv.setText("Back");
            leftTxv.setTextColor(getResources().getColor(R.color.color_scheme));
            backImv.getDrawable().setColorFilter(getResources().getColor(R.color.color_scheme), PorterDuff.Mode.SRC_ATOP);
            backImv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                }
            });
            leftLlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                }
            });
            rightImv.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {

                    wv.reload();

                }
            });
        }catch (Exception e)
        {
            LogUtilsutility.LOGD("exception"," webview exception "+e.getMessage());
        }
    }
}
