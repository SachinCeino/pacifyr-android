package com.pacifyr.omhealthcare.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ceino.utilitylibrary.CustomFontTextView;
import com.ceino.utilitylibrary.DeviceFitImageView;
import com.ceino.utilitylibrary.model.pacifyr.MUser;
import com.ceino.utilitylibrary.model.pacifyr.ModelSpecialist;
import com.ceino.utilitylibrary.model.pacifyr.RespSearchPacifyrWOption;
import com.ceino.utilitylibrary.utils.PrefManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pacifyr.omhealthcare.Fragment.schedule.ScheduleBottomSheetFragment;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.adapter.DoctorsSpecialityAdapter;
import com.pacifyr.omhealthcare.adapter.PacifyrResultListViewAdapter;
import com.pacifyr.omhealthcare.base.PacifyerActivity;
import com.pacifyr.omhealthcare.event.AlertTypeEvent;
import com.pacifyr.omhealthcare.event.NotifyEvent;
import com.pacifyr.omhealthcare.fragment.DoctorsFilterFragment;
import com.pacifyr.omhealthcare.network.GsonUtils;
import com.pacifyr.omhealthcare.utility.PacifyrAppCompactActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.pacifyr.omhealthcare.network.NetworkService.GLOBAL_URL;
import static com.pacifyr.omhealthcare.network.NetworkService.pSearchPacifyrWOptionsResponse;

public class DoctorsSearchResultActivity extends PacifyrAppCompactActivity implements OnMapReadyCallback {


    private String Speciality = "";
    private RespSearchPacifyrWOption pSearchPacifyrWOptionsResponse;
    private RecyclerView rcv_SearchResult;
    private PacifyrResultListViewAdapter mPacifyrResultListViewAdapter;
    private TextView total_results;
    private RadioGroup mSegmentedGroup;
    private RadioButton mRadioButtonA, mRadioButtonB;
    private LinearLayout listViewTypeLayout, mapViewTypeLayout;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<MUser> list;
    private  TextView txtv_filter;


    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_search_result);
        onSetActionBar();
        rcv_SearchResult = findViewById(R.id.rcv_SearchResult);
        total_results = findViewById(R.id.total_results);
        mSegmentedGroup = (RadioGroup) findViewById(R.id.segmentedgroup);
        mRadioButtonB = (RadioButton) findViewById(R.id.rblistView);
        mRadioButtonA = (RadioButton) findViewById(R.id.rbMapView);
        listViewTypeLayout = findViewById(R.id.listViewTypeLayout);
        mapViewTypeLayout = findViewById(R.id.mapViewTypeLayout);
        txtv_filter=findViewById(R.id.txtv_filter);
        fillColour_ForRadioButton(mRadioButtonB);
        fillNoColor_ForRadioButton(mRadioButtonA);


        mSegmentedGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rblistView:

                        fillColour_ForRadioButton(mRadioButtonB);
                        fillNoColor_ForRadioButton(mRadioButtonA);
                        listViewTypeLayout.setVisibility(View.VISIBLE);
                        mapViewTypeLayout.setVisibility(View.GONE);
                        setPacifyrsListView();
                        break;


                    case R.id.rbMapView:

                        fillColour_ForRadioButton(mRadioButtonA);
                        fillNoColor_ForRadioButton(mRadioButtonB);
                        mapViewTypeLayout.setVisibility(View.VISIBLE);
                        setPacifyrsMapView();
                        break;

                }
            }
        });

        if (isValid(getIntent().getExtras().get("Speciality"))) {
            Speciality = getIntent().getExtras().get("Speciality").toString();
        }

        if (isValid(Speciality))
            webSearchPacifyrWOptions(Speciality);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        txtv_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment mDoctorsFilterFragment = new DoctorsFilterFragment();
                ShowFilterFragment(mDoctorsFilterFragment);
            }
        });

    }


    public void changeTitle(String title) {

        if (isValid(titleTxv) && isValid(title)) {
            titleTxv.setVisibility(View.VISIBLE);
            defSetText(titleTxv, title);
        }

    }

    public void ShowFilterFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.filter_fragment_container, fragment);
        fragmentTransaction.addToBackStack(fragment.getClass().getName());
        fragmentTransaction.commit();
    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
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


    /**
     * set background
     * as filling a colour
     **/

    private void fillColour_ForRadioButton(RadioButton _RadioButton) {


        if (_RadioButton.getId() == R.id.rblistView) {

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
        if (_RadioButton.getId() == R.id.rblistView) {

            _RadioButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.segment_background_radiobutton_empty_right_button));

        } else {

            _RadioButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.segment_background_radiobutton_empty_left_button));

        }

        _RadioButton.setTextColor(getResources().getColor(R.color.RadioButtonUnSelectedColour));
    }


    public void webSearchPacifyrWOptions(String Speciality) {
        try {


            setLoading("loading...");
            showProgress();
            String url = GLOBAL_URL + "search/pacifyrs/byLocation";

            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    try {
                        pSearchPacifyrWOptionsResponse = GsonUtils.getInstance().gsonToRespSearchPacifyrWOption(json);
                        pSearchPacifyrWOptionsResponse.toString();
                        String js = json.toString();
                        hideProgress();
                        setPacifyrsListView();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            AQuery aq = new AQuery(DoctorsSearchResultActivity.this);
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", getPrefManager().getAccessToken());

            JSONObject jsonObject = new JSONObject();
            jsonObject.putOpt("initiatorId", getPrefManager().getUserID());
            jsonObject.putOpt("latitude", "11.25416");
            jsonObject.putOpt("longitude", "75.83717");
            aq.post(url, jsonObject, JSONObject.class, cb);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setPacifyrsListView() {
        if (isValid(pSearchPacifyrWOptionsResponse)) {
            list = new ArrayList<MUser>();
            list.clear();
            list = pSearchPacifyrWOptionsResponse.getResultsList();
            if (isValid(list)) {
                total_results.setText(list.size() + " result");
                mPacifyrResultListViewAdapter = new PacifyrResultListViewAdapter(list, DoctorsSearchResultActivity.this);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                rcv_SearchResult.setLayoutManager(layoutManager);
                rcv_SearchResult.setAdapter(mPacifyrResultListViewAdapter);
                rcv_SearchResult.setItemAnimator(new DefaultItemAnimator());
                setUpMarkerOnMap(list);
            } else {

            }
        }
    }

    private void setUpMarkerOnMap(ArrayList<MUser> list) {

        if (isValid(mMap)) {
            for (int i = 0; i < list.size(); i++) {
                LatLng mPacifyrPoint = new LatLng(list.get(i).getLatitude(), list.get(i).getLongitude());
                mMap.addMarker(new MarkerOptions().position(mPacifyrPoint).title(list.get(i).getFirstName() + "")).showInfoWindow();
                mMap.moveCamera(CameraUpdateFactory.newLatLng(mPacifyrPoint));

            }
        }


    }

    private void setPacifyrsMapView() {
        if (isValid(pSearchPacifyrWOptionsResponse)) {
            ArrayList<MUser> list = new ArrayList<MUser>();
            list.clear();
            list = pSearchPacifyrWOptionsResponse.getResultsList();
            if (isValid(list)) {
                total_results.setText(list.size() + " result");
                mPacifyrResultListViewAdapter = new PacifyrResultListViewAdapter(list, DoctorsSearchResultActivity.this);
                layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                rcv_SearchResult.setLayoutManager(layoutManager);
                rcv_SearchResult.setAdapter(mPacifyrResultListViewAdapter);
                rcv_SearchResult.setItemAnimator(new DefaultItemAnimator());
            } else {

            }
        }
    }

    @Override
    protected View getSnackbarAnchorView() {
        return null;
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
        defSetText(titleTxv, getResources().getString(R.string.search_result));
        Typeface type = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.avantica_bold_header));
        titleTxv.setTypeface(type);
        //backImv.setBackgroundResource(R.drawable.leftmen);
        leftLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment f = getSupportFragmentManager().findFragmentById(R.id.filter_fragment_container);
                if (isValid(f))
                {
                    if (f instanceof DoctorsFilterFragment)

                        getSupportFragmentManager().popBackStackImmediate();
                    else {
                        onBackPressed();
                    }
                }
                else {
                    finish();
                }

            }
        });

    }

    @Override
    // Detect when the back button is pressed
    public void onBackPressed() {

        Fragment f = getSupportFragmentManager().findFragmentById(R.id.filter_fragment_container);
        if (isValid(f))
        {
            if (f instanceof DoctorsFilterFragment)

                getSupportFragmentManager().popBackStackImmediate();
            else {
                onBackPressed();
            }
        }
        else {
            finish();
        }

    }


    public void showScheduleDialogueFragment(MUser pacifyer_obj) {
        if (isValid(pacifyer_obj)) {

            ScheduleBottomSheetFragment bottomSheetFragment = new ScheduleBottomSheetFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("OBJECT", pacifyer_obj);
            bottomSheetFragment.setArguments(bundle);
            bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
        }

    }
}
