package com.pacifyr.omhealthcare.sign;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aigestudio.wheelpicker.WheelPicker;
import com.aigestudio.wheelpicker.widgets.WheelYearPicker;
import com.ceino.utilitylibrary.CustomFontTextView;
import com.ceino.utilitylibrary.DeviceFitImageView;
import com.ceino.utilitylibrary.model.pacifyr.Education;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.adapter.ListEducationAdapter;
import com.pacifyr.omhealthcare.base.PacifyerActivity;
import com.pacifyr.omhealthcare.model.ModelPacifyrEducation;

import java.util.ArrayList;

public class AddPacifyrEducationActivity extends PacifyerActivity {

    Education education;
    ArrayList<Education> educationList;

    CustomFontTextView  add_education_btn;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_pacifyr_education);
            onSetActionBar();
            Intent i = getIntent();
            if(isValid(this.getIntent().getParcelableArrayListExtra("EDU_LIST")))
                educationList = i.getParcelableArrayListExtra("EDU_LIST");
            else
                educationList = new ArrayList<Education>();

            mRecyclerView = (RecyclerView) findViewById(R.id.education_list_rcv);
            mLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLayoutManager);
            add_education_btn = (CustomFontTextView) findViewById(R.id.add_education_btn);
            add_education_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showEducationPickerDialog();
                }
            });
            setEducationAdapter();

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setEducationAdapter() {
        if(isValid(educationList)){
            mRecyclerView.setVisibility(View.VISIBLE);
            mAdapter = new ListEducationAdapter(educationList,AddPacifyrEducationActivity.this);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    protected View getSnackbarAnchorView() {
        return null;
    }



    public void showEducationPickerDialog(/*final CustomFontTextView textView*/) {
        try {
            final Integer[] vals = {null};
            //View.OnClickListener oklistner
            View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_pacifyr_education, null, false);

            ImageView closeDimv = (ImageView) view.findViewById(R.id.close_dimv);
            final EditText collageEdt = (EditText) view.findViewById(R.id.collage_edt);
            final EditText locationEdt = (EditText) view.findViewById(R.id.location_edt);
            final CustomFontTextView fromCtxv = (CustomFontTextView) view.findViewById(R.id.from_ctxv);
            final CustomFontTextView toDateCtxv = (CustomFontTextView) view.findViewById(R.id.to_ctxv);
            RelativeLayout saveRlay = (RelativeLayout) view.findViewById(R.id.save_rlay);
            CustomFontTextView saveCtxv = (CustomFontTextView) view.findViewById(R.id.save_ctxv);


            closeDimv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideStatus();

                }
            });
            hideStatus();
            hud_status = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCustomView(view).setWindowColor(this.getResources().getColor(R.color.transparent)).setCancellable(false).setAnimationSpeed(2).setDimAmount(0.5f);
            showStatus();


            fromCtxv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDatePickerDialog(fromCtxv, "from");
                }
            });

            toDateCtxv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDatePickerDialog(toDateCtxv, "to");
                }
            });

            saveRlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isValid(collageEdt,"Please Enter Collage/University"))
                        if(isValid(locationEdt,"Please enter location"))
                            if(isValid(fromCtxv,"Please select from year"))
                                if(isValid(toDateCtxv,"Please select to year"))
                                {
                                    String duration = fromCtxv.getText().toString()+" to "+toDateCtxv.getText().toString();
                                    String location = locationEdt.getText().toString();
                                    String title = collageEdt.getText().toString();
                                    education = new Education(duration,location,title,educationList.size());
                                    /*education.setTitle(collageEdt.getText().toString());
                                    education.setLocation(locationEdt.getText().toString());
                                    education.setDuration( fromCtxv.getText().toString()+" to "+toDateCtxv.getText().toString());
                                    education.setOrder(educationList.size());*/
                                    educationList.add(education);
                                    setEducationAdapter();
                                    hideProgress();
                                    hideStatus();
                                }
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
    public void showDatePickerDialog(final CustomFontTextView textView, String string){
        try {
            final Integer[] vals = {null};
            final Dialog dialog = new Dialog(AddPacifyrEducationActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_show_year_picker);
            WheelYearPicker wheelYearPicker = (WheelYearPicker) dialog.findViewById(R.id.wheel_date_picker_year);
            wheelYearPicker.setSelectedItemTextColor(Color.parseColor("#3F51B5"));
            wheelYearPicker.setCurved(true);
            wheelYearPicker.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
                @Override
                public void onItemSelected(WheelPicker picker, Object data, int position) {
                    vals[0] = (Integer) data;
                }
            });

            TextView selectTxv = (TextView) dialog.findViewById(R.id.select_year_txv);
            selectTxv.setText("Please select " + string + " year");

            TextView cancelTxv = (TextView) dialog.findViewById(R.id.cancel_ctxv);
            cancelTxv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();

            TextView saveTxv = (TextView) dialog.findViewById(R.id.save_ctxv);
            saveTxv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isValid(vals[0]))
                        textView.setText(vals[0].toString());
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    private LinearLayout actionBarTopLlay;
    private RelativeLayout bottomPanel;
    private LinearLayout leftLlay;
    private DeviceFitImageView backImv;
    private CustomFontTextView leftTxv;
    private LinearLayout rightLlay;
    private CustomFontTextView rightTxv;
    private DeviceFitImageView rightImv;

    private void onSetActionBar() {
        actionBarTopLlay = (LinearLayout) findViewById(R.id.action_bar_top_llay);
        bottomPanel = (RelativeLayout) findViewById(R.id.bottom_panel);
        leftLlay = (LinearLayout) findViewById(R.id.left_llay);
        backImv = (DeviceFitImageView) findViewById(R.id.back_imv);
        leftTxv = (CustomFontTextView) findViewById(R.id.left_txv);
        rightLlay = (LinearLayout) findViewById(R.id.right_llay);
        rightTxv = (CustomFontTextView) findViewById(R.id.right_txv);
        rightImv = (DeviceFitImageView) findViewById(R.id.right_imv);

        leftLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rightTxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent output = new Intent();
                output.putParcelableArrayListExtra("eduList", educationList);
                setResult(RESULT_OK, output);
                finish();
            }
        });
    }
}
