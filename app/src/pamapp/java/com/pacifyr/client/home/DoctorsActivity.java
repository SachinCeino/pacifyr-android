package com.pacifyr.client.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Typeface;
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

import static com.pacifyr.client.network.NetworkService.GLOBAL_URL;

public class DoctorsActivity extends PacifyrAppCompactActivity implements DoctorsSpecialityAdapter.SpecialityItemClick {

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


        mSegmentedGroup = (RadioGroup) findViewById(R.id.segmentedgroup);
        mRadioButtonB = (RadioButton) findViewById(R.id.rb1);
        mRadioButtonA = (RadioButton) findViewById(R.id.rb3);

        fillColour_ForRadioButton(mRadioButtonA);
        fillNoColor_ForRadioButton(mRadioButtonB);

        mSegmentedGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb1:

                        fillColour_ForRadioButton(mRadioButtonB);
                        fillNoColor_ForRadioButton(mRadioButtonA);
                        primaryLayout.setVisibility(View.VISIBLE);
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
                String specialityItem = ETV_showSpecialities.getText().toString();
                if (!specialityItem.equals("")) {
                    Intent intent = new Intent(getApplicationContext(), DoctorsSearchResultActivity.class);
                    intent.putExtra("Speciality", specialityItem);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Select one Speciality", Toast.LENGTH_SHORT).show();
                }


            }
        });



        etv_CurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

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


}
