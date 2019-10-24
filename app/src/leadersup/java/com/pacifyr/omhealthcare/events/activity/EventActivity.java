package com.pacifyr.omhealthcare.events.activity;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ceino.utilitylibrary.CustomFontTextView;
import com.ceino.utilitylibrary.DeviceFitImageView;
import com.ceino.utilitylibrary.utils.PrefManager;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.base.PacifyerActivity;
import com.pacifyr.omhealthcare.events.fragment.AttendingEmployerFragment;
import com.pacifyr.omhealthcare.events.fragment.EventsDetailFragment;
import com.pacifyr.omhealthcare.events.fragment.EventsListFragment;
import com.pacifyr.omhealthcare.home.CheckinActivity;
import com.pacifyr.omhealthcare.model.ModelEvent;
import com.pacifyr.omhealthcare.model.QRSuccess;
import com.pacifyr.omhealthcare.network.GsonUtils;
import com.pacifyr.omhealthcare.network.NetworkService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.pacifyr.omhealthcare.network.NetworkService.GLOBAL_URL;

public class EventActivity extends PacifyerActivity implements LocationListener {

    public static void start(Context context) {
        Intent intent = new Intent(context, EventActivity.class);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private String titleText;
    private RelativeLayout empty_layout;
    private LocationManager locationManager;
    private Location location;
    private final int REQUEST_LOCATION = 200;

    public double mLatitude,mLongitude;
    private int QRScanType = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        empty_layout = findViewById(R.id.empty_layout);
        empty_layout.setVisibility(View.GONE);
        Bundle bundle = getIntent().getExtras();
        titleText = "EVENTS";
        locationManager = (LocationManager) getSystemService(Service.LOCATION_SERVICE);

        onSetActionBar();

        getWebData();

        checkPremissionFineLocation();



    }

    public void checkPremissionFineLocation() {
        if (ActivityCompat.checkSelfPermission(EventActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                        EventActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(EventActivity.this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
            }, REQUEST_LOCATION);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 2, this);
            if (locationManager != null)
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (location != null) {
                Log.w("@@@@", "latitude=" + location.getLatitude());
                Log.w("@@@@", "longitude=" + location.getLongitude());

                mLatitude=location.getLatitude();
                mLongitude=location.getLongitude();

            }
        } else {
            showGPSDisabledAlertToUser();
        }
    }

    public void ShowEventFragments(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.event_fragment_containe, fragment);
        fragmentTransaction.addToBackStack(fragment.getClass().getName());
        fragmentTransaction.commit();

    }

    public void changeTitle(String title) {

        if (isValid(titleTxv) && isValid(title)) {
            titleTxv.setVisibility(View.VISIBLE);
            defSetText(titleTxv, title);
        }

    }


    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    // Detect when the back button is pressed
    public void onBackPressed() {

        Fragment f = getSupportFragmentManager().findFragmentById(R.id.event_fragment_containe);
        if (f instanceof EventsListFragment || empty_layout.getVisibility() == View.VISIBLE)
            finish();

        else {
            getSupportFragmentManager().popBackStackImmediate();
        }

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
    private CustomFontTextView leftTxv;
    private CustomFontTextView titleTxv;
    private LinearLayout rightLlay;
    private CustomFontTextView rightTxv;
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
        Typeface type = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.avantica_bold_header));
        titleTxv.setTypeface(type);
        if (isValid(titleText))
            titleTxv.setText(titleText);
        leftLlay.setVisibility(View.VISIBLE);
        leftLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment f = getSupportFragmentManager().findFragmentById(R.id.event_fragment_containe);
                if (f instanceof EventsListFragment || empty_layout.getVisibility() == View.VISIBLE)
                    finish();

                else {
                    getSupportFragmentManager().popBackStackImmediate();
                }

            }
        });
    }


    public void getWebData() {

        setLoading();
        showProgress();

        String token = getPrefManager().getAccessToken();

        AjaxCallback cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                try {

                    hideProgress();
                    if (isValid(json)) {
                        ModelEvent data = GsonUtils.getInstance().gsonToModelEvent(json);
                        if (isValid(data)) {
                            List<ModelEvent.Result> listEvent = data.getResults();
                            /**show EventsListFragment  **/
                            if (isValid(listEvent)) {
                                empty_layout.setVisibility(View.GONE);
                                Fragment mEventsListFragment = new EventsListFragment();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("data", (Serializable) listEvent);
                                mEventsListFragment.setArguments(bundle);
                                ShowEventFragments(mEventsListFragment);

                            } else {
                                empty_layout.setVisibility(View.VISIBLE);
                            }

                        }
                    } else {
                        if (isValid(status.getMessage())) {
                            empty_layout.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(), status.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                } catch (Exception e) {

                    hideProgress();
                    empty_layout.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                }
            }
        };
        try {
            AQuery aq = new AQuery(getApplicationContext());

            cb.header("Content-Type", "application/json");

            cb.header("AccessToken", token);

            aq.ajax(GLOBAL_URL + "events/list?skip=0&limit=10", JSONObject.class, cb);

        } catch (Exception ex)

        {
            hideProgress();

            ex.printStackTrace();
        }

    }



    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EventActivity.this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. \n Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Enable GPS", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(callGPSSettingIntent);
                    }
                });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @Override
    public void onLocationChanged(Location location) {
       /* Log.w("@@@@1", "latitude=" + location.getLatitude());
        Log.w("@@@@1", "longitude=" + location.getLongitude());
*/
        mLatitude=location.getLatitude();
        mLongitude=location.getLongitude();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        if (provider.equals(LocationManager.GPS_PROVIDER)) {
            showGPSDisabledAlertToUser();
        }

    }

    public double getmLatitude()
    {
        return mLatitude;
    }

    public double getmLongitude()
    {

        return mLongitude;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {

            } else {
                try {
                    JSONObject obj = new JSONObject(result.getContents());
                    if (isValid(obj))
                        if (obj.has("code")) {
                            if (getQRScanType() != -1 && getQRScanType() == AttendingEmployerFragment.EMPLOYER_SCAN) {
                                callAPIWithCodeFromQRScanner(obj.get("code").toString());
                            } else if (getQRScanType() != -1 && getQRScanType() == EventsDetailFragment.EVENT_SCAN) {
                                callAPIWithCodeFromQRScanner(obj.get("code").toString());
                            } else {
                                Toast.makeText(EventActivity.this, "No actions available",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (getQRScanType() != -1 && getQRScanType() == AttendingEmployerFragment.EMPLOYER_SCAN) {
                        callAPIWithCodeFromQRScanner(result.getContents());
                    } else if (getQRScanType() != -1 && getQRScanType() == EventsDetailFragment.EVENT_SCAN) {
                        callAPIWithCodeFromQRScanner(result.getContents());
                    } else {
                        Toast.makeText(EventActivity.this, "No actions available",
                                Toast.LENGTH_SHORT).show();
                    }
                    //  Toast.makeText(getApplicationContext(), result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    /**
     * Employer Check in
     **/

    private void callAPIWithCodeFromQRScanner(String code) {

        try {
            PrefManager prefM = PrefManager.getInstance(getApplicationContext());
            String userToken = prefM.getAccessToken();
            String userid = prefM.getUserID();
            setLoading();
            showProgress();
            String url = NetworkService.GLOBAL_URL + "events/checkinUsingCode";
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    try {
                        hideProgress();
                        QRSuccess data = GsonUtils.getInstance().gsonToModelQRCodeResult(json);
                        if (isValid(data))
                            showPopup(data);

                    } catch (Exception e) {
                        errorStatus();
                    }
                }
            };
            AQuery aq = new AQuery(getApplicationContext());
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", userToken);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId", prefM.getUserID());
            jsonObject.put("code", String.valueOf(code));
            aq.post(url, jsonObject, JSONObject.class, cb);
        } catch (Exception ex) {
            errorStatus();
        }

    }

    private void showPopup(QRSuccess data) {

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.popup_qr_code_success_, null);
        final TextView message = alertLayout.findViewById(R.id.message);
        final TextView done = alertLayout.findViewById(R.id.done);
        /*final EditText etEmail = alertLayout.findViewById(R.id.et_email);
        final CheckBox cbToggle = alertLayout.findViewById(R.id.cb_show_pass);
*/

        message.setText(data.getMessage() + "");
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        AlertDialog dialog = alert.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid(data.getEventId())) {
                    Intent intent = new Intent(EventActivity.this,
                            CheckinActivity.class);
                    intent.putExtra("eventId", data.getEventId());
                    startActivity(intent);
                    dialog.dismiss();
                } else {
                    dialog.dismiss();
                }

            }
        });
    }

    public int getQRScanType() {
        return QRScanType;
    }

    public void setQRScanType(int QRScanType) {
        this.QRScanType = QRScanType;
    }
}
