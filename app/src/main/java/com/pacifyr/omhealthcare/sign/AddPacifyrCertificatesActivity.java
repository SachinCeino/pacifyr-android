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
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ceino.utilitylibrary.CustomFontTextView;
import com.ceino.utilitylibrary.DeviceFitImageView;
import com.ceino.utilitylibrary.model.pacifyr.ModelSpecialist;
import com.ceino.utilitylibrary.utils.PrefManager;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.adapter.ListCertificatesAdapter;
import com.pacifyr.omhealthcare.base.PacifyerActivity;
import com.pacifyr.omhealthcare.model.ModelPacifyrCertificates;
import com.pacifyr.omhealthcare.network.GsonUtils;
import com.pacifyr.omhealthcare.utility.EventMessage;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.pacifyr.omhealthcare.network.NetworkService.GLOBAL_URL;

public class AddPacifyrCertificatesActivity extends PacifyerActivity {

    ModelPacifyrCertificates certificates;
    ArrayList<ModelPacifyrCertificates> certificatesList;

    private CustomFontTextView addEducationBtn;
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
            addEducationBtn = (CustomFontTextView) findViewById(R.id.add_education_btn);
            certificateListRcv = (RecyclerView) findViewById(R.id.certificate_list_rcv);
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

    public void showCertificatesDialog(/*final CustomFontTextView textView*/) {
        try {
            final Integer[] vals = {null};
            final String[] title = {null};
            final String[] year = {null};
            //View.OnClickListener oklistner
            View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_pacifyr_certificates, null, false);

            ImageView closeDimv = (ImageView) view.findViewById(R.id.close_dimv);
            final EditText titleEdt = (EditText) view.findViewById(R.id.title_edt);
            final CustomFontTextView dateCtxv = (CustomFontTextView) view.findViewById(R.id.date_ctxv);
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

    public void showDialog(final CustomFontTextView textView, String string){
        try {
            final Integer[] vals = {null};
            final Dialog dialog = new Dialog(AddPacifyrCertificatesActivity.this);
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
    private CustomFontTextView headerCtxv;

    private void onSetActionBar() {
        try {
            headerCtxv = (CustomFontTextView) findViewById(R.id.header_ctxv);
            actionBarTopLlay = (LinearLayout) findViewById(R.id.action_bar_top_llay);
            bottomPanel = (RelativeLayout) findViewById(R.id.bottom_panel);
            leftLlay = (LinearLayout) findViewById(R.id.left_llay);
            backImv = (DeviceFitImageView) findViewById(R.id.back_imv);
            leftTxv = (CustomFontTextView) findViewById(R.id.left_txv);
            rightLlay = (LinearLayout) findViewById(R.id.right_llay);
            rightTxv = (CustomFontTextView) findViewById(R.id.right_txv);
            rightImv = (DeviceFitImageView) findViewById(R.id.right_imv);

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
