package com.pacifyr.client.home;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.pacifyr.client.adapter.EmotionSpecialityAdapter;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;
import com.pacifyrapp.utilitylibrary.model.pacifyr.ModelSpecialist;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.pacifyr.client.R;
import com.pacifyr.client.adapter.DoctorsSpecialityAdapter;
import com.pacifyr.client.base.PacifyrActivity;
import com.pacifyr.client.event.AlertTypeEvent;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.network.GsonUtils;
import com.pacifyr.client.utility.EventMessage;
import com.pacifyr.client.utility.PacifyrAppCompactActivity;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static com.pacifyr.client.network.NetworkService.GLOBAL_URL;

public class DoctorsActivity extends PacifyrAppCompactActivity implements DoctorsSpecialityAdapter.SpecialityItemClick,LocationListener {

    private LinearLayout specialitiesLayout;
    private RecyclerView specialitiesRCV;
    private RadioGroup mSegmentedGroup;
    private RadioButton mRadioButtonA, mRadioButtonB;
    private TextView ETV_showSpecialities;
    private ModelSpecialist modelSpecialist;
    public ModelSpecialist.Results[] modelResults;
    private DoctorsSpecialityAdapter mDoctorsSpecialityAdapter;
    private LinearLayout primaryLayout;
    private LinearLayout submit;
    private EditText etv_CurrentLocation;
    private boolean isPrimary=true;

    private LocationManager locationManager;
    private Location location;
    private final int REQUEST_LOCATION = 200;

    public double mLatitude,mLongitude;
    public static String currentLocation=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_find_care);
        specialitiesLayout = findViewById(R.id.specialitiesLayout);
        specialitiesRCV = findViewById(R.id.specialitiesRCV);
        ETV_showSpecialities = findViewById(R.id.ETV_showSpecialities);
        primaryLayout = findViewById(R.id.primaryLayout);
        submit = findViewById(R.id.submit);
       etv_CurrentLocation=findViewById(R.id.etv_CurrentLocation);


        onSetActionBar();
        locationManager = (LocationManager) getSystemService(Service.LOCATION_SERVICE);
        checkPremissionFineLocation();

      /*  if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
        }
        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        if (autocompleteFragment != null) {
            autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG));
        }

        if (autocompleteFragment != null) {
            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(Place place) {

                    // TODO: Get info about the selected place.
                    latitude= String.valueOf(place.getLatLng().latitude);
                    longitude= String.valueOf(place.getLatLng().longitude);
                    Log.w( "@@@place", String.valueOf(place.getLatLng().latitude));
                    Log.w( "@@@place", String.valueOf(place.getLatLng().longitude));




                }

                @Override
                public void onError(Status status) {
                    // TODO: Handle the error.
                    Log.w( "@@@staus",String.valueOf(status));
                }
            });
        }
*/

        mSegmentedGroup = (RadioGroup) findViewById(R.id.segmentedgroup);
        mRadioButtonB = (RadioButton) findViewById(R.id.rb1);
        mRadioButtonA = (RadioButton) findViewById(R.id.rb3);

        fillColour_ForRadioButton(mRadioButtonB);
        fillNoColor_ForRadioButton(mRadioButtonA);
        handleActionSpecilaist();
        mSegmentedGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb1:

                        fillColour_ForRadioButton(mRadioButtonB);
                        fillNoColor_ForRadioButton(mRadioButtonA);
                        primaryLayout.setVisibility(View.GONE);
                        isPrimary=true;
                        handleActionSpecilaist();
                        break;


                    case R.id.rb3:

                        fillColour_ForRadioButton(mRadioButtonA);
                        fillNoColor_ForRadioButton(mRadioButtonB);
                        primaryLayout.setVisibility(View.GONE);
                        isPrimary=false;
                        handleActionSpecilaist();
                        break;

                }
            }
        });

        ETV_showSpecialities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (specialitiesLayout.getVisibility() == View.VISIBLE)
                    specialitiesLayout.setVisibility(View.GONE);
                else
                    specialitiesLayout.setVisibility(View.VISIBLE);

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isValid(etv_CurrentLocation.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Select your location", Toast.LENGTH_SHORT).show();
                    checkPremissionFineLocation();
                    return;
                }
                String specialityItem = ETV_showSpecialities.getText().toString();
                if (!specialityItem.equals("")) {
                    Intent intent = new Intent(getApplicationContext(), DoctorsSearchResultActivity.class);
                    intent.putExtra("Speciality", specialityItem);
                    intent.putExtra("latitude", mLatitude);
                    intent.putExtra("longitude", mLongitude);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Select one Speciality", Toast.LENGTH_SHORT).show();
                }


            }
        });



    }


    public void checkPremissionFineLocation() {
        if (ActivityCompat.checkSelfPermission(DoctorsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                        DoctorsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DoctorsActivity.this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
            }, REQUEST_LOCATION);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 2, (LocationListener) this);
            if (locationManager != null)
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (location != null) {
                Log.w("@@@@", "latitude=" + location.getLatitude());
                Log.w("@@@@", "longitude=" + location.getLongitude());

                mLatitude=location.getLatitude();
                mLongitude=location.getLongitude();
                setAddressNameToUI(mLatitude,mLongitude);

            }
        } else {
            showGPSDisabledAlertToUser();
        }
    }

    private void setAddressNameToUI(double mLatitude, double mLongitude) {
        try {

            Geocoder geo = new Geocoder(this.getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geo.getFromLocation(mLatitude, mLongitude, 1);
            if (addresses.isEmpty()) {
                etv_CurrentLocation.setText("Waiting for Location");
            }
            else {
                if (addresses.size() > 0) {
                    Log.w("@@@",addresses.get(0).getFeatureName() + ", " + addresses.get(0).getLocality() +", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName());
                    etv_CurrentLocation.setText(addresses.get(0).getFeatureName() + ", " + addresses.get(0).getLocality() +", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName());
                    currentLocation=etv_CurrentLocation.getText().toString();
                }
            }
        }
        catch (Exception e)
        {
            Log.w("@@@",String.valueOf(e));
        }
    }

    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DoctorsActivity.this);
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


    private void handleActionSpecilaist() {

        PrefManager prefM = PrefManager.getInstance(getApplicationContext());
        String token = prefM.getAccessToken();
        showProgress();
        String catogoryType="";
            if (isPrimary)
                catogoryType="primaryCare";
            else
                catogoryType="other";
        String url = GLOBAL_URL + "specialities/byCategory?category="+catogoryType;

        AjaxCallback cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                try {
                    hideProgress();
                    modelSpecialist = GsonUtils.getInstance().gsonToModelSpecialist(json);
                    modelResults = modelSpecialist.getResults();
                    modelSpecialist.toString();

                    if (isValid(modelSpecialist)&&isValid(modelSpecialist.getResultsList())
                    &&isValid(modelSpecialist.getResultsList().get(0))&& isValid(modelSpecialist.getResultsList().get(0).getTitle()))
                    {

                        ETV_showSpecialities.setText(modelSpecialist.getResultsList().get(0).getTitle() + "");
                    }
                    setUpAdapter();
                } catch (Exception e) {
                    e.printStackTrace();
                    hideProgress();
                }
            }
        };
        AQuery aq = new AQuery(getApplicationContext());
        cb.header("Content-Type", "application/json");
        cb.header("AccessToken", token);
        aq.ajax(url, JSONObject.class, cb);

    }

    private void setUpAdapter() {

        specialitiesRCV.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        specialitiesRCV.setLayoutManager(layoutManager);
        mDoctorsSpecialityAdapter = new DoctorsSpecialityAdapter(modelSpecialist.getResultsList(), getApplicationContext());
        mDoctorsSpecialityAdapter.addListener(this);
        specialitiesRCV.setAdapter(mDoctorsSpecialityAdapter);
    }

    /**
     * OnSpecialitySelected
     * once user pick the Speciality do remaining things here
     **/
    @Override
    public void OnSpecialitySelected(ModelSpecialist.Results results) {

        if (isValid(results)) {
            ModelSpecialist.Results item = results;
            if (isValid(item.getTitle()))
                ETV_showSpecialities.setText(item.getTitle() + "");
            /**once assigned the Speciality to the textview then listview view should be gone**/
            specialitiesLayout.setVisibility(View.GONE);
        }
    }


    /**
     * set background
     * as filling a colour
     **/

    private void fillColour_ForRadioButton(RadioButton _RadioButton) {


        if (_RadioButton.getId() == R.id.rb1) {

            _RadioButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.segment_background_radiobutton_fill_right));
        } else {
            _RadioButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.segment_background_radiobutton_fill_left));

        }

        _RadioButton.setTextColor(getResources().getColor(R.color.RadioButtonSelectedColour));
    }

    /**
     * set background
     * as no colour
     **/

    private void fillNoColor_ForRadioButton(RadioButton _RadioButton) {
        if (_RadioButton.getId() == R.id.rb1) {

            _RadioButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.segment_background_radiobutton_empty_right_button));

        } else {

            _RadioButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.segment_background_radiobutton_empty_left_button));

        }

        _RadioButton.setTextColor(getResources().getColor(R.color.RadioButtonUnSelectedColour));
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
        defSetText(titleTxv, getResources().getString(R.string.find_care));
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


    @Override
    public void onLocationChanged(Location location) {
        Log.w("@@@@1", "latitude=" + location.getLatitude());
        Log.w("@@@@1", "longitude=" + location.getLongitude());

        mLatitude=location.getLatitude();
        mLongitude=location.getLongitude();
        setAddressNameToUI(mLatitude,mLongitude);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.w("@@@","onStatusChanged");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.w("@@@","onProviderEnabled");
    }

    @Override
    public void onProviderDisabled(String provider) {
        if (provider.equals(LocationManager.GPS_PROVIDER)) {
            showGPSDisabledAlertToUser();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.w("@@@","permission was granted");
                    checkPremissionFineLocation();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.w("@@@","permission denied");
                    finish();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
}
