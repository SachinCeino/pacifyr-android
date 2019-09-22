package com.pacifyr.omhealthcare.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

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
import com.ceino.utilitylibrary.CustomFontTextView;
import com.ceino.utilitylibrary.DeviceFitImageView;
import com.ceino.utilitylibrary.model.pacifyr.ModelSpecialist;
import com.ceino.utilitylibrary.utils.PrefManager;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.adapter.DoctorsSpecialityAdapter;
import com.pacifyr.omhealthcare.base.PacifyerActivity;
import com.pacifyr.omhealthcare.event.AlertTypeEvent;
import com.pacifyr.omhealthcare.event.NotifyEvent;
import com.pacifyr.omhealthcare.network.GsonUtils;
import com.pacifyr.omhealthcare.utility.EventMessage;
import com.pacifyr.omhealthcare.utility.PacifyrAppCompactActivity;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import static com.pacifyr.omhealthcare.network.NetworkService.GLOBAL_URL;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_find_care);
        specialitiesLayout = findViewById(R.id.specialitiesLayout);
        specialitiesRCV = findViewById(R.id.specialitiesRCV);
        ETV_showSpecialities = findViewById(R.id.ETV_showSpecialities);
        primaryLayout = findViewById(R.id.primaryLayout);
        submit = findViewById(R.id.submit);


        onSetActionBar();
        handleActionSpecilaist();

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
                        break;


                    case R.id.rb3:

                        fillColour_ForRadioButton(mRadioButtonA);
                        fillNoColor_ForRadioButton(mRadioButtonB);
                        primaryLayout.setVisibility(View.GONE);
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

        String url = GLOBAL_URL + "specialities/byCategory?category=all";
        AjaxCallback cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                try {
                    hideProgress();
                    modelSpecialist = GsonUtils.getInstance().gsonToModelSpecialist(json);
                    modelResults = modelSpecialist.getResults();
                    modelSpecialist.toString();
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
