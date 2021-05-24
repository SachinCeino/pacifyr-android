package com.pacifyr.client.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.pacifyr.client.R;
import com.pacifyr.client.base.PacifyrActivity;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.model.PreviousPrescription;
import com.pacifyr.client.network.GsonUtils;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;

import org.json.JSONObject;

import static com.pacifyr.client.network.NetworkService.GLOBAL_URL;

public class PrescriptionViewActivity extends PacifyrActivity {


    private String titleText="Prescription";
    private String text;
    private TextView message_edt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_view);
        message_edt = findViewById(R.id.message_edt);
        message_edt.setMovementMethod(new ScrollingMovementMethod());
        Bundle bundle = getIntent().getExtras();
        if( bundle!= null && isValid(bundle.getString("text"))){
            text=bundle.getString("text");
        }
        message_edt.setText(text);
        message_edt.setSelected(true);
        onSetActionBar();

        if (isValid(sessionid))
            getPreviouslyAddedPrescription();


    }

    private void getPreviouslyAddedPrescription() {
        try {
            setLoading();
            showProgress();
            PrefManager mPrefManager = PrefManager.getInstance(getApplicationContext());
            String token = mPrefManager.getAccessToken();
            String url = GLOBAL_URL + "prescription/session/"+sessionid;

            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    try {
                        hideProgress();
                        hideStatus();
                        Log.w("@@@",String.valueOf(json));
                        if (isValid(json))
                        {
                            PreviousPrescription mPreviousPrescription = GsonUtils.getInstance().gsonToPreviousPrescription(json);
                            if (isValid(mPreviousPrescription))
                            {
                                if (isValid(mPreviousPrescription.getPrescription().getText()))
                                {
                                    message_edt.setText(mPreviousPrescription.getPrescription().getText());
                                }
                            }


                        }



                    } catch (Exception e) {

                        hideProgress();
                        e.printStackTrace();
                    }
                }
            };
            AQuery aq = new AQuery(getApplicationContext());
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", token);
            aq.ajax(url, JSONObject.class, cb);
        } catch (Exception ex) {
            hideProgress();
            ex.printStackTrace();
            Log.w("@@@",String.valueOf(ex));
        }
    }


    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    // Detect when the back button is pressed
    public void onBackPressed() {
        finish();
    }


    @Override
    protected View getSnackbarAnchorView() {
        return null;
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

                finish();

            }
        });
    }




}
