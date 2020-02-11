package com.pacifyr.client.home;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pacifyr.client.R;
import com.pacifyr.client.adapter.RatingGraphAdapter;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.event.AlertTypeEvent;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.fragment.schedule.ScheduleBottomSheetFragment;
import com.pacifyr.client.utility.PacifyrAppCompactActivity;
import com.pacifyr.client.utils.Utility;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MUser;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.pacifyr.client.network.NetworkService.GLOBAL_IMAGE_URL;

public class DoctorsRatingActivity extends PacifyrAppCompactActivity{


    private MUser pacifyerObject;


    private TextView pacifyr_name;
    private TextView pacifyr_description;
    private CircleImageView pacifyr_image;
    private TextView pacifyr_address;
    private TextView pacifyr_affiliations;
    private TextView pacifyr_language_spoken;
    private TextView requestBook;
    private RecyclerView Rcvrating;
    private TextView txtv_psiNumber;
    private TextView recentMentionCount;
    private TextView Provider_pass_succes,Thorougness_of_examination,
            Inclusion_in_decisions,Providers_Attitude,Timely_return_results
            ,Providers_reputation,Ability_answer_questions,Provider_followup,clarity_of_instructions,
            Amount_time_with_patient,Feedback_volume,Feedback_latency,date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_rating);
        onSetActionBar();

        pacifyr_name = findViewById(R.id.pacifyr_name);
        pacifyr_description = findViewById(R.id.pacifyr_description);
        pacifyr_image = findViewById(R.id.pacifyr_image);
        pacifyr_address = findViewById(R.id.pacifyr_address);
        requestBook = findViewById(R.id.requestBook);
        Rcvrating = findViewById(R.id.Rcvrating);
        txtv_psiNumber =findViewById(R.id.txtv_psiNumber);
        recentMentionCount =findViewById(R.id.recentMentionCount);

        Provider_pass_succes =findViewById(R.id.Provider_pass_succes);
        Inclusion_in_decisions = findViewById(R.id.Inclusion_in_decisions);
        Providers_Attitude = findViewById(R.id.Providers_Attitude);
        Timely_return_results = findViewById(R.id.Timely_return_results);
        Providers_reputation = findViewById(R.id.Providers_reputation);
        Ability_answer_questions = findViewById(R.id.Ability_answer_questions);
        Provider_followup = findViewById(R.id.Provider_followup);
        clarity_of_instructions = findViewById(R.id.clarity_of_instructions);
        Amount_time_with_patient = findViewById(R.id.Amount_time_with_patient);
        Feedback_volume =findViewById(R.id.Feedback_volume);
        Thorougness_of_examination =findViewById(R.id.Thorougness_of_examination);
        Feedback_latency = findViewById(R.id.Feedback_latency);
        date = findViewById(R.id.date);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (isValid(bundle)) {
            pacifyerObject = (MUser) bundle.getSerializable("Object");
        }
        if (isValid(pacifyerObject)) {
            initUI(pacifyerObject);
        }

        if (isValid(pacifyerObject.getPsi()))
        setUpRatingGraphUI(Integer.parseInt(pacifyerObject.getPsi()));

        if (isValid(pacifyerObject.getRecentMentionCount()))
            recentMentionCount.setText("Based on "+ String.valueOf(pacifyerObject.getRecentMentionCount())+" mentions");

        if (isValid(pacifyerObject.getThoroughnessOfExamination()))
            Thorougness_of_examination.setText(pacifyerObject.getThoroughnessOfExamination());
        if (isValid(pacifyerObject.getInclusionInDecisions()))
            Inclusion_in_decisions.setText(pacifyerObject.getInclusionInDecisions());

        if (isValid(pacifyerObject.getProvidersAttitude()))
            Providers_Attitude.setText(pacifyerObject.getProvidersAttitude());

        if (isValid(pacifyerObject.getAbilityToAnswerQuestions()))
            Ability_answer_questions.setText(pacifyerObject.getAbilityToAnswerQuestions());

        if (isValid(pacifyerObject.getProvidersFollowUp()))
            Provider_followup.setText(pacifyerObject.getProvidersFollowUp());

        if (isValid(pacifyerObject.getClarityOfInstructions()))
            clarity_of_instructions.setText(pacifyerObject.getClarityOfInstructions());

        if (isValid(pacifyerObject.getAmountOfTimeWithPatient()))
            Amount_time_with_patient.setText(pacifyerObject.getAmountOfTimeWithPatient());

        if (isValid(pacifyerObject.getD1Volume()))
            Feedback_volume.setText(pacifyerObject.getD1Volume());

        if (isValid(pacifyerObject.getD2Latency()))
            Feedback_latency.setText(pacifyerObject.getD2Latency());

        Utility.getDate(pacifyerObject.getUpdatedAt(),false);


    }

    private void setUpRatingGraphUI(int value) {
        txtv_psiNumber.setText(String.valueOf(value));
        RecyclerView.LayoutManager mLayoutManager=new LinearLayoutManager(getApplicationContext(),RecyclerView.HORIZONTAL,false);
        RatingGraphAdapter mRatingGraphAdapter=new RatingGraphAdapter(getApplicationContext(),value);
        Rcvrating.setLayoutManager(mLayoutManager);
        Rcvrating.setAdapter(mRatingGraphAdapter);

        int position=0;
        if (value<=100)
        {
            position=0;
        }
        else if (value<=200)
        {
            position=1;
        }
        else if (value<=300)
        {
            position=2;
        }
        else if (value<=400)
        {
            position=3;
        }
        else if (value<=500)
        {
            position=4;
        }
        else if (value<=600)
        {
            position=5;
        }
        else if (value<=700)
        {
            position=6;
        }
        else if (value<=800)
        {
            position=7;
        }
        else if (value<=900)
        {
            position=8;
        }
        else {
            position=9;
        }
        mRatingGraphAdapter.setMarker(position);
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


        if (isValid(pacifyerObject.getCountry()))
            pacifyr_address.setText(pacifyerObject.getCountry());




/*

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
*/

     /*   if (isValid(pacifyerObject.getCertification())) {
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
*/




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
        defSetText(titleTxv, getResources().getString(R.string.doctor_rating));
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
