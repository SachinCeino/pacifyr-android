package com.pacifyr.omhealthcare.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ceino.utilitylibrary.model.response.ResponseMessage;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.event.AlertTypeEvent;
import com.pacifyr.omhealthcare.event.NotifyEvent;
import com.pacifyr.omhealthcare.events.activity.CaptureActivityPortrait;
import com.pacifyr.omhealthcare.fragment.CheckInFragment;
import com.pacifyr.omhealthcare.model.InterviewModel;
import com.pacifyr.omhealthcare.model.MarkEvent;
import com.pacifyr.omhealthcare.network.NetworkService;
import com.pacifyr.omhealthcare.utility.PacifyrAppCompactActivity;
import com.squareup.otto.Subscribe;

import org.json.JSONObject;

/**
 * Created by Sachin Thampan on 18/10/19.
 */
public class CheckinActivity extends PacifyrAppCompactActivity {

    private TextView title;
    private ImageView back;
    private String eventId;
    private IntentIntegrator _QrScanner;
    private MarkEvent markEvent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin);

        eventId = getIntent().getExtras() != null ? getIntent().getExtras().getString("eventId") :
                null;
        back = findViewById(R.id.back_imv);
        title = findViewById(R.id.title_txv);
        title.setText("Check In");
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getPendingCheckins();


    }

    private void getPendingCheckins() {
        if (eventId != null) {
            setLoading();
            showProgress();
            new NetworkService().getIncompleteCheckinSteps(eventId,
                    new AjaxCallback<JSONObject>() {

                        @Override
                        public void callback(String url, JSONObject json,
                                             AjaxStatus status) {
                            hideProgress();
                            if (json != null) {
                                InterviewModel.Result result =
                                        new Gson().fromJson(json.toString(),
                                                InterviewModel.Result.class);
                                if (result != null) {
                                    Bundle args = new Bundle();
                                    args.putSerializable("pending", result);
                                    CheckInFragment checkInFragment = new CheckInFragment();
                                    checkInFragment.setArguments(args);
                                    getSupportFragmentManager().beginTransaction().replace(R.id.container_checkin,
                                            checkInFragment).addToBackStack(CheckInFragment.class.getSimpleName()).commit();

                                } else {
                                    setStatus("No data found", R.color.text_color_black, R.color.white);
                                    showStatus();
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            hideStatus();
                                            finish();
                                        }
                                    }, 3000);
                                }
                            } else {
                                setStatus("No data found", R.color.text_color_black, R.color.white);
                                showStatus();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        hideStatus();
                                        finish();
                                    }
                                }, 3000);
                            }
                        }
                    });
        }
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

    @Subscribe
    public void onMarkEvent(MarkEvent markEvent) {
        this.markEvent = markEvent;
        _QrScanner = new IntentIntegrator(this);
        _QrScanner.setPrompt("Scan a barcode");
        _QrScanner.setCameraId(0);  // Use a specific camera of the device
        _QrScanner.setOrientationLocked(true);
        _QrScanner.setBeepEnabled(true);
        _QrScanner.setCaptureActivity(CaptureActivityPortrait.class);
        _QrScanner.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null && markEvent != null) {
            new NetworkService().markStepComplete(markEvent.getEventId(), result.getContents(),
                    markEvent.getType(), new AjaxCallback<JSONObject>() {
                        @Override
                        public void callback(String url, JSONObject json,
                                             AjaxStatus status) {
                            if (json != null) {
                                ResponseMessage responseMessage =
                                        new Gson().fromJson(json.toString(), ResponseMessage.class);
                                if (responseMessage != null) {
                                    if (responseMessage.getStatus().equals("error")) {
                                        setAlertOKStatusInRed(responseMessage.getStatus(),
                                                "Invalid QR code", "Close",
                                                new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        hideStatus();
                                                    }
                                                });
                                    } else {
                                        setAlertOKStatusInWhite(responseMessage.getStatus(),
                                                "Check in complete!", "Close");
                                        getPendingCheckins();
                                    }
                                }
                            }

                        }
                    });
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        }
    }
}
