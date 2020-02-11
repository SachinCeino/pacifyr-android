package com.pacifyr.client.home;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pacifyr.client.customview.MainTextView;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MUser;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pacifyr.client.fragment.schedule.ScheduleBottomSheetFragment;
import com.pacifyr.client.R;
import com.pacifyr.client.event.AlertTypeEvent;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.utility.PacifyrAppCompactActivity;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.pacifyr.client.network.NetworkService.GLOBAL_IMAGE_URL;

public class DoctorsMoreInformationActivity extends PacifyrAppCompactActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MUser pacifyerObject;


    private TextView pacifyr_name;
    private TextView pacifyr_description;
    private CircleImageView pacifyr_image;
    private TextView pacifyr_number;
    private TextView pacifyr_address;
    private TextView pacifyr_facility_hours;
    private TextView pacifyr_webisite;
    private TextView pacifyr_prices;
    private TextView pacifyr_educations;
    private TextView pacifyr_sex;
    private TextView pacifyr_specialities;
    private TextView pacifyr_cirtification;
    private TextView pacifyr_affiliations;
    private TextView pacifyr_language_spoken;
    private TextView pacifyr_price_booking;
    private TextView requestBook;
    private LinearLayout ratingLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);
        onSetActionBar();

        pacifyr_name = findViewById(R.id.pacifyr_name);
        pacifyr_description = findViewById(R.id.pacifyr_description);
        pacifyr_number = findViewById(R.id.pacifyr_number);
        pacifyr_image = findViewById(R.id.pacifyr_image);
        pacifyr_address = findViewById(R.id.pacifyr_address);
        pacifyr_facility_hours = findViewById(R.id.pacifyr_facility_hours);
        pacifyr_webisite = findViewById(R.id.pacifyr_webisite);
        pacifyr_prices = findViewById(R.id.pacifyr_prices);
        pacifyr_educations = findViewById(R.id.pacifyr_educations);
        pacifyr_sex = findViewById(R.id.pacifyr_sex);
        pacifyr_specialities = findViewById(R.id.pacifyr_specialities);
        pacifyr_cirtification = findViewById(R.id.pacifyr_cirtification);
        pacifyr_affiliations = findViewById(R.id.pacifyr_affiliations);
        pacifyr_language_spoken = findViewById(R.id.pacifyr_language_spoken);
        pacifyr_price_booking = findViewById(R.id.pacifyr_price_booking);
        requestBook = findViewById(R.id.requestBook);
        ratingLayout = findViewById(R.id.ratingLayout);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (isValid(bundle)) {
            pacifyerObject = (MUser) bundle.getSerializable("Object");
        }
        if (isValid(pacifyerObject)) {
            initUI(pacifyerObject);
        }


        requestBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showScheduleDialogueFragment(pacifyerObject);

            }
        });

        ratingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), DoctorsRatingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Object", pacifyerObject);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
    }


    private void initUI(MUser pacifyerObject) {
        String namePacifyr = pacifyerObject.getFirstName() + " " + pacifyerObject.getLastName();

        if (isValid(namePacifyr))
            pacifyr_name.setText(namePacifyr);
        if (isValid(pacifyerObject.getQualifications()))
            pacifyr_description.setText(pacifyerObject.getQualifications());

        if (isValid(pacifyerObject.getImage()))
            Picasso.with(getApplicationContext()).load(GLOBAL_IMAGE_URL + pacifyerObject.getImage()).placeholder(R.drawable.placeholder_user).into(pacifyr_image);
        else
            pacifyr_image.setImageResource(R.drawable.placeholder_user);

        if (isValid(pacifyerObject.getPhoneNumber()))
            pacifyr_number.setText(pacifyerObject.getPhoneNumber());
        if (isValid(pacifyerObject.getCountry()))
            pacifyr_address.setText(pacifyerObject.getCountry());
        if (isValid(pacifyerObject.getAvailableMinutes()))
            pacifyr_facility_hours.setText(pacifyerObject.getCountry());
        if (isValid(pacifyerObject.getWebsiteURL()))
            pacifyr_webisite.setText(pacifyerObject.getWebsiteURL());
        else
            pacifyr_webisite.setText("Not Avaialbe");

        if (isValid(pacifyerObject.getPriceWithSign()))
            pacifyr_prices.setText(pacifyerObject.getPriceWithSign());
        if (isValid(pacifyerObject.getEducation())) {
            StringBuffer buffer = new StringBuffer();

            for (int i = 0; i < pacifyerObject.getEducation().length; i++) {
                buffer.append(pacifyerObject.getEducation()[i].getTitle() + "\n");
            }
            if (isValid(buffer))
                pacifyr_educations.setText(buffer);
            else
                pacifyr_educations.setText("Not Avaialbe");
        } else {
            pacifyr_educations.setText("Not Avaialbe");
        }

        if (isValid(pacifyerObject.getSpecialities())) {
            StringBuffer buffer = new StringBuffer();

            for (int i = 0; i < pacifyerObject.getSpecialities().length; i++) {
                buffer.append(pacifyerObject.getSpecialities()[i] + "\n");
            }
            if (isValid(buffer))
                pacifyr_specialities.setText(buffer);
            else
                pacifyr_specialities.setText("Not Avaialbe");
        } else {
            pacifyr_specialities.setText("Not Avaialbe");
        }

        if (isValid(pacifyerObject.getCertification())) {
            StringBuffer buffer = new StringBuffer();

            for (int i = 0; i < pacifyerObject.getCertification().length; i++) {
                buffer.append(pacifyerObject.getCertification()[i] + "\n");
            }
            if (isValid(buffer))
                pacifyr_cirtification.setText(buffer);
            else
                pacifyr_cirtification.setText("Not Avaialbe");
        } else {
            pacifyr_cirtification.setText("Not Avaialbe");
        }

        if (isValid(pacifyerObject.getCertification())) {
            StringBuffer buffer = new StringBuffer();

            for (int i = 0; i < pacifyerObject.getCertification().length; i++) {
                buffer.append(pacifyerObject.getCertification()[i] + "\n");
            }
            if (isValid(buffer))
                pacifyr_affiliations.setText(buffer);
            else
                pacifyr_affiliations.setText("Not Avaialbe");
        } else {
            pacifyr_affiliations.setText("Not Avaialbe");
        }

        if (isValid(pacifyerObject.getDescription())) {
            pacifyr_language_spoken.setText(pacifyerObject.getDescription());
        } else {
            pacifyr_language_spoken.setText("Not Avaialbe");
        }

        if (isValid(pacifyerObject.getPriceWithSign()))
            pacifyr_price_booking.setText(pacifyerObject.getPriceWithSign());

        if (isValid(pacifyerObject.getGender()))
            pacifyr_sex.setText(pacifyerObject.getGender());
        else
            pacifyr_sex.setText("Not Avaialbe");


    }

    private void setUpMarkerOnMap(MUser pacifyrItem) {
        if (isValid(mMap)) {

            LatLng mPacifyrPoint = new LatLng(pacifyrItem.getLatitude(), pacifyrItem.getLongitude());
            mMap.addMarker(new MarkerOptions()
                    .position(mPacifyrPoint)
                    .title(pacifyrItem.getFirstName() + ""))
                    .setIcon(BitmapDescriptorFactory
                            .fromResource(R.drawable.icon_google_map_marker));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(mPacifyrPoint));

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
        defSetText(titleTxv, getResources().getString(R.string.doctor_profile));
        Typeface type = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.avantica_bold_header));
        titleTxv.setTypeface(type);
        //backImv.setBackgroundResource(R.drawable.leftmen);
        leftLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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
        if (isValid(pacifyerObject)) {
            setUpMarkerOnMap(pacifyerObject);
        }

    }

    public void showScheduleDialogueFragment(MUser pacifyer_obj) {
        if (isValid(pacifyer_obj)) {
            if (isValid(getPrefManager().getGENDER()) && isValid(getPrefManager().getZIP())) {
                ScheduleBottomSheetFragment bottomSheetFragment = new ScheduleBottomSheetFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("OBJECT", pacifyer_obj);
                bottomSheetFragment.setArguments(bundle);
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
            }
            else
            { showMandatoryFieldsPopUp();
            }
        }


    }
}
