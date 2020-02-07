package com.pacifyr.client.home;

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
import com.pacifyr.client.customview.MainTextView;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MUser;
import com.pacifyrapp.utilitylibrary.model.pacifyr.RespSearchPacifyrWOption;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pacifyr.client.fragment.schedule.ScheduleBottomSheetFragment;
import com.pacifyr.client.R;
import com.pacifyr.client.adapter.PacifyrResultListViewAdapter;
import com.pacifyr.client.event.AlertTypeEvent;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.fragment.DoctorsFilterFragment;
import com.pacifyr.client.network.GsonUtils;
import com.pacifyr.client.utility.PacifyrAppCompactActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.pacifyr.client.network.NetworkService.GLOBAL_URL;

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

    private String latitude,longitude;
    private GoogleMap mMap;
    private TextView desclaimertextView,viewMore;
    private boolean isCheck=true;
    private RelativeLayout viewLayout;

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
        desclaimertextView=findViewById(R.id.desclaimertextView);
        viewMore=findViewById(R.id.viewMore);
        viewLayout=findViewById(R.id.viewLayout);
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
        if (isValid(getIntent().getExtras().get("latitude"))) {
            latitude = getIntent().getExtras().get("latitude").toString();
        }
        if (isValid(getIntent().getExtras().get("longitude"))) {
            longitude = getIntent().getExtras().get("longitude").toString();
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
                Bundle mBundle = new Bundle();
                mBundle.putString("Speciality", Speciality);
                mBundle.putString("longitude", longitude);
                mBundle.putString("latitude", latitude);
                mDoctorsFilterFragment.setArguments(mBundle);
                ShowFilterFragment(mDoctorsFilterFragment);
            }
        });
        viewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isCheck) {
                    desclaimertextView.setMaxLines(20);
                    isCheck = false;
                    viewMore.setText("View less");
                } else {
                    desclaimertextView.setMaxLines(3);
                    isCheck = true;
                    viewMore.setText("View more");
                }

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
                        String js = json.toString();
                        pSearchPacifyrWOptionsResponse = GsonUtils.getInstance().gsonToRespSearchPacifyrWOption(json);
                        pSearchPacifyrWOptionsResponse.toString();
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
            jsonObject.putOpt("latitude", latitude);
            jsonObject.putOpt("longitude", longitude);
            jsonObject.putOpt("visitType", "office");
            List<String> list = new ArrayList<String>();
            list.add(Speciality);
            //list.add("b");
            JSONArray array = new JSONArray();
            for (int i = 0; i < list.size(); i++) {
                array.put(list.get(i));
            }
            jsonObject.putOpt("specialities", array);
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
                mPacifyrResultListViewAdapter = new PacifyrResultListViewAdapter(list,
                        DoctorsSearchResultActivity.this, new PacifyrResultListViewAdapter.OnFavoriteCliked() {
                    @Override
                    public void onAddtofavoriteint(int position,MUser object) {
                        callAddToFavOrRemoveFavDoctorAPI(position,object);
                    }
                });
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                rcv_SearchResult.setLayoutManager(layoutManager);
                rcv_SearchResult.setAdapter(mPacifyrResultListViewAdapter);
                rcv_SearchResult.setItemAnimator(new DefaultItemAnimator());
                setUpMarkerOnMap(list);
                viewLayout.setVisibility(View.VISIBLE);

            } else {

            }
        }
    }

    private void callAddToFavOrRemoveFavDoctorAPI(int updateIndex, MUser object) {


        if (object.isFavorite()==false)
        {
            /**add to favorite **/
            try {
                setLoading("loading...");
                showProgress();
                String url = GLOBAL_URL + "favorite";

                AjaxCallback cb = new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject json, AjaxStatus status) {
                        try {
                            hideProgress();
                            String js = json.toString();
                            String message="";
                            if (isValid(json))
                            {
                                if (json.has("message"))
                                    message=json.getString("message").toString();
                                if(isValid(message))
                                    Toast.makeText(DoctorsSearchResultActivity.this, message, Toast.LENGTH_SHORT).show();
                                object.setFavorite(true);
                                list.set(updateIndex, object);
                                mPacifyrResultListViewAdapter.notifyItemChanged(updateIndex);
                            }
                            else
                            {
                                Toast.makeText(DoctorsSearchResultActivity.this, "something went wrong!", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            hideProgress();
                        }
                    }
                };

                AQuery aq = new AQuery(DoctorsSearchResultActivity.this);
                cb.header("Content-Type", "application/json");
                cb.header("AccessToken", getPrefManager().getAccessToken());
                JSONObject jsonObject = new JSONObject();
                jsonObject.putOpt("userId", getPrefManager().getUserID());
                jsonObject.putOpt("pacifyrId", object.getId());
                aq.post(url, jsonObject, JSONObject.class, cb);
            } catch (Exception ex) {
                ex.printStackTrace();
                hideProgress();
            }

        }
        else
        {
            /**remove from favorite **/
            try {
                setLoading("loading...");
                showProgress();
                String url = GLOBAL_URL + "favorite?userId="+getPrefManager().getUserID()+"&pacifyrId="+object.getId();

                AjaxCallback cb = new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject json, AjaxStatus status) {
                        try {
                            hideProgress();
                            String js = json.toString();
                            String message="";
                            if (isValid(json))
                            {
                                if (json.has("message"))
                                    message=json.getString("message").toString();
                                if(isValid(message))
                                    Toast.makeText(DoctorsSearchResultActivity.this, message, Toast.LENGTH_SHORT).show();
                                object.setFavorite(false);
                                list.set(updateIndex, object);
                                mPacifyrResultListViewAdapter.notifyItemChanged(updateIndex);
                            }
                            else
                            {
                                Toast.makeText(DoctorsSearchResultActivity.this, "something went wrong!", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            hideProgress();
                        }
                    }
                };

                AQuery aq = new AQuery(DoctorsSearchResultActivity.this);
                cb.header("Content-Type", "application/json");
                cb.header("AccessToken", getPrefManager().getAccessToken());
                aq.delete(url, JSONObject.class, cb);
            } catch (Exception ex) {
                ex.printStackTrace();
                hideProgress();
            }


        }


    }

    private void setUpMarkerOnMap(ArrayList<MUser> list) {

        if (isValid(mMap)) {
            for (int i = 0; i < list.size(); i++) {
                LatLng mPacifyrPoint = new LatLng(list.get(i).getLatitude(), list.get(i).getLongitude());
                mMap.addMarker(new MarkerOptions()
                        .position(mPacifyrPoint)
                        .title(list.get(i).getFirstName() + ""))
                        .setIcon(BitmapDescriptorFactory
                                .fromResource(R.drawable.icon_google_map_marker));
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
                mPacifyrResultListViewAdapter = new PacifyrResultListViewAdapter(list,
                        DoctorsSearchResultActivity.this, new PacifyrResultListViewAdapter.OnFavoriteCliked() {
                    @Override
                    public void onAddtofavoriteint(int position,MUser object) {

                        callAddToFavOrRemoveFavDoctorAPI(position,object);

                    }
                });

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
        if (isValid(getPrefManager().getGENDER()) && isValid(getPrefManager().getZIP())) {
            ScheduleBottomSheetFragment bottomSheetFragment = new ScheduleBottomSheetFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("OBJECT", pacifyer_obj);
            bottomSheetFragment.setArguments(bundle);
            bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
        }
        else
        {
            showMandatoryFieldsPopUp();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @org.greenrobot.eventbus.Subscribe(threadMode = ThreadMode.MAIN)
    public void messageEventFromService(RespSearchPacifyrWOption mFilteredData) {
        if (isValid(mFilteredData)) {
            pSearchPacifyrWOptionsResponse = mFilteredData;
            setPacifyrsListView();
            setPacifyrsMapView();
        }


    }
}
