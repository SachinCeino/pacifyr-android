package com.pacifyr.client.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.krishna.fileloader.FileLoader;
import com.krishna.fileloader.listener.FileRequestListener;
import com.krishna.fileloader.pojo.FileResponse;
import com.krishna.fileloader.request.FileLoadRequest;
import com.pacifyr.client.R;
import com.pacifyr.client.base.PacifyrActivity;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.utility.PdfViewer;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;

import java.io.File;

public class TermsOfUseActivity extends PacifyrActivity {

    private static final String TAG = TermsOfUseActivity.class.getSimpleName();
    private WebView webView;
    private String titleText;
    private String spurl;
    //private ImageView backButt;
    //private LinearLayout backbtn;
    private int retryCount = 0;
    private String currentUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pacifyr_webview);
        webView = findViewById(R.id.web_view);
        Bundle bundle = getIntent().getExtras();

        if( bundle!= null){
            spurl = bundle.getString("spurl");
            if (spurl != null && spurl.contains(".pdf")) {

                spurl = "https://drive.google.com/viewerng/viewer?embedded=true&url=" + spurl;
            }
            startWebView(spurl);
            titleText=bundle.getString("title");
        }
        onSetActionBar();



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
        //Create new webview Client to show progress dialog
        //When opening a url or click on link

        webView.setWebViewClient(new WebViewClient() {
            ProgressDialog progressDialog;

            //If you will not use this method url links are opeen in new brower not in webview
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (currentUrl != null && url != null && url.equals(currentUrl)) {
                    view.goBack();
                    return true;
                }
                if (url.contains(".pdf")) {
                    String originalUrl = url;
                    String[] separated = url.split("https://");
                    for (int i = 0; i < separated.length; i++) {
                        if (separated[i].contains(".pdf")) {
                            url = "https://" + separated[i];
                        }

                    }
                    if (originalUrl.equalsIgnoreCase(url)) {
                        String[] separated1 = url.split("http://");
                        for (int i = 0; i < separated1.length; i++) {
                            if (separated1[i].contains(".pdf")) {
                                url = "http://" + separated1[i];
                            }

                        }
                    }


                    if (isValid(url) && !url.contains("dmca")) {
                        url = url.substring(0, url.indexOf(".pdf")) + ".pdf";
                        String url1 = url;
                        String title = url.replace("https://pacifyr.com/", "");
                        loadTemsPDF(url1, title);
                    } else { //load DMCA pdf in Google Drive pdf viewer. Normal PDF viewer is not rendering it properly
                        url = "https://drive.google.com/viewerng/viewer?embedded=true&url=" + url;
                        view.loadUrl(url);
                    }


                } else {
                    view.loadUrl(url);
                }
                currentUrl = url;
                return true;
            }

            //Show loader on url load
            public void onLoadResource (WebView view, String url) {



                if (progressDialog == null) {
                    // in standard case YourActivity.this
                    progressDialog = new ProgressDialog(TermsOfUseActivity.this);
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }

            @Override
            public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError
                    error) {
                final AlertDialog.Builder builder = new AlertDialog.Builder
                        (TermsOfUseActivity.this);
                builder.setMessage("Webview ssl error");
                builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.proceed();
                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.cancel();
                    }
                });
                final AlertDialog dialog = builder.create();
                dialog.show();
            }
            public void onPageFinished(WebView view, String url) {


                int height = view.getContentHeight();
                if (height == 0 && retryCount <= 5) {
                    retryCount++;
                    view.loadUrl(url);
                }

                try{
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();

                    }
                }catch(Exception exception){
                    exception.printStackTrace();
                }
            }

        });

        // Javascript inabled on webview
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.loadUrl(url);

    }



    public void loadTemsPDF(String url,String titleText) {
        setLoading();
        showProgress();
        FileLoader.with(TermsOfUseActivity.this)
                .load(url, false) //2nd parameter is optioal, pass true to force load from network
                .fromDirectory("test4", FileLoader.DIR_CACHE)
                .asFile(new FileRequestListener<File>() {
                    @Override
                    public void onLoad(FileLoadRequest request, FileResponse<File> response) {
                        hideProgress();
                        File loadedFile = response.getBody();
                        // do something with the file
                        Log.d(TAG, "File downloaded ");
                        PdfViewer.pdf = loadedFile;
                        Intent intent = new Intent(TermsOfUseActivity.this, PdfViewer.class);
                        intent.putExtra("title",titleText);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(FileLoadRequest request, Throwable t) {
                        hideProgress();
                        Log.d(TAG, "File download failed ");
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

    @Override
    protected void onResume() {
        super.onResume();
        if (isValid(webView))
            webView.onResume();
    }


}
