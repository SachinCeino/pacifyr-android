package com.pacifyr.client.sign;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aigestudio.wheelpicker.WheelPicker;
import com.aigestudio.wheelpicker.widgets.WheelYearPicker;
import com.pacifyr.client.base.PacifyrActivity;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.pacifyr.client.R;
import com.pacifyr.client.adapter.ListCertificatesAdapter;
import com.pacifyr.client.model.ModelPacifyrCertificates;

import java.util.ArrayList;

public class AddPacifyrCertificatesActivity extends PacifyrActivity {

    ModelPacifyrCertificates certificates;
    ArrayList<ModelPacifyrCertificates> certificatesList;

    private MainTextView addEducationBtn;
    private RecyclerView certificateListRcv;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_pacifyr_certificates);
            onSetActionBar();
            certificatesList = new ArrayList<ModelPacifyrCertificates>();
            Intent i = getIntent();
            if(isValid(this.getIntent().getParcelableArrayListExtra("CERT_LIST")))
                certificatesList = i.getParcelableArrayListExtra("CERT_LIST");
            //this.getIntent().getParcelableArrayListExtra("extraextra");
            mLayoutManager = new LinearLayoutManager(this);
            addEducationBtn = findViewById(R.id.add_education_btn);
            certificateListRcv = findViewById(R.id.certificate_list_rcv);
            certificateListRcv.setLayoutManager(mLayoutManager);

            addEducationBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showCertificatesDialog();
                }
            });
            setCertificateAdapter();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    protected View getSnackbarAnchorView() {
        return null;
    }

    private void setCertificateAdapter() {
        try {
            if (isValid(certificatesList)) {
                certificateListRcv.setVisibility(View.VISIBLE);
                mLayoutManager = new LinearLayoutManager(this);
                mAdapter = new ListCertificatesAdapter(certificatesList, AddPacifyrCertificatesActivity.this);
                certificateListRcv.setAdapter(mAdapter);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void showCertificatesDialog(/*final MainTextView textView*/) {
        try {
            final Integer[] vals = {null};
            final String[] title = {null};
            final String[] year = {null};
            //View.OnClickListener oklistner
            View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_pacifyr_certificates, null, false);

            ImageView closeDimv = view.findViewById(R.id.close_dimv);
            final EditText titleEdt = view.findViewById(R.id.title_edt);
            final MainTextView dateCtxv = view.findViewById(R.id.date_ctxv);
            RelativeLayout saveRlay = view.findViewById(R.id.save_rlay);
            MainTextView saveCtxv = view.findViewById(R.id.save_ctxv);
            closeDimv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideStatus();

                }
            });
            hideStatus();
            hud_status = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCustomView(view).setWindowColor(this.getResources().getColor(R.color.transparent)).setCancellable(false).setAnimationSpeed(2).setDimAmount(0.5f);
            showStatus();
            dateCtxv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(dateCtxv, "");
                }
            });
            saveRlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isValid(titleEdt,"Please enter title"))
                        if(isValid(dateCtxv,"Please select Year"))
                        {
                            title[0] = titleEdt.getText().toString();
                            year[0] = dateCtxv.getText().toString();
                            certificates = new ModelPacifyrCertificates(title[0], year[0]);
                            certificatesList.add(certificates);
                            setCertificateAdapter();
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

    public void showDialog(final MainTextView textView, String string) {
        try {
            final Integer[] vals = {null};
            final Dialog dialog = new Dialog(AddPacifyrCertificatesActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_show_year_picker);
            WheelYearPicker wheelYearPicker = dialog.findViewById(R.id.wheel_date_picker_year);
            wheelYearPicker.setSelectedItemTextColor(Color.parseColor("#3F51B5"));
            wheelYearPicker.setCurved(true);
            wheelYearPicker.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
                @Override
                public void onItemSelected(WheelPicker picker, Object data, int position) {
                    vals[0] = (Integer) data;
                }
            });

            TextView selectTxv = dialog.findViewById(R.id.select_year_txv);
            selectTxv.setText("Please select " + string + " year");
            TextView cancelTxv = dialog.findViewById(R.id.cancel_ctxv);
            cancelTxv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
            TextView saveTxv = dialog.findViewById(R.id.save_ctxv);
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
    private MainTextView leftTxv;
    private LinearLayout rightLlay;
    private MainTextView rightTxv;
    private DeviceFitImageView rightImv;
    private MainTextView headerCtxv;

    private void onSetActionBar() {
        try {
            headerCtxv = findViewById(R.id.header_ctxv);
            actionBarTopLlay = findViewById(R.id.action_bar_top_llay);
            bottomPanel = findViewById(R.id.bottom_panel);
            leftLlay = findViewById(R.id.left_llay);
            backImv = findViewById(R.id.back_imv);
            leftTxv = findViewById(R.id.left_txv);
            rightLlay = findViewById(R.id.right_llay);
            rightTxv = findViewById(R.id.right_txv);
            rightImv = findViewById(R.id.right_imv);

            headerCtxv.setText("Certificates");

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
                    output.putParcelableArrayListExtra("certList", certificatesList);
                    setResult(RESULT_OK, output);
                    finish();
                    //Snackbar.make(getCurrentFocus(),"Progress",Snackbar.LENGTH_LONG).show();
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
