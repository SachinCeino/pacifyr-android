package com.pacifyr.client.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.pacifyr.client.R;
import com.pacifyr.client.event.AlertTypeEvent;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.events.fragment.EmployersList;
import com.pacifyr.client.model.ModelEvent;
import com.pacifyr.client.model.QRSuccess;
import com.pacifyr.client.network.GsonUtils;
import com.pacifyr.client.network.NetworkService;
import com.pacifyr.client.utility.PacifyrAppCompactActivity;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Sachin Thampan on 23/10/19.
 */
public class EmployerList extends PacifyrAppCompactActivity {

    private List<ModelEvent.Employer> employers;
    private View back;
    private TextView title;

    public void setTitle(String title) {
        this.title.setText(title != null ? title : "");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employers_list);
        title = findViewById(R.id.title_txv);
        back = findViewById(R.id.back_imv);
        title.setText("Employers List");

        back.setVisibility(View.VISIBLE);
        if (getIntent().getExtras() != null) {
            employers = (List<ModelEvent.Employer>) getIntent().getExtras().getSerializable("data");
            if (employers != null) {
                Bundle args = new Bundle();
                args.putSerializable("data", (Serializable) employers);
                EmployersList employersList = new EmployersList();
                employersList.setArguments(args);
                getSupportFragmentManager().beginTransaction().replace(R.id.employers_container,
                        employersList).addToBackStack(null).commit();
            }
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        } else {
            super.onBackPressed();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) { //Employer check in
            mcallAPIWithCodeFromQRScanner(result.getContents());
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

    public void mcallAPIWithCodeFromQRScanner(String code) {

        try {
            PrefManager prefM = PrefManager.getInstance(getApplicationContext());
            String userToken = prefM.getAccessToken();
            String userid = prefM.getUserID();
            setLoading();
            showProgress();
            String url = NetworkService.GLOBAL_URL + "events/checkinUsingCode";
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    try {
                        hideProgress();
                        QRSuccess data = GsonUtils.getInstance().gsonToModelQRCodeResult(json);
                        if (isValid(data))
                            mshowPopup(data);

                    } catch (Exception e) {
                        errorStatus();
                    }
                }
            };
            AQuery aq = new AQuery(getApplicationContext());
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", userToken);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId", prefM.getUserID());
            jsonObject.put("code", String.valueOf(code));
            aq.post(url, jsonObject, JSONObject.class, cb);
        } catch (Exception ex) {
            errorStatus();
        }

    }

    public void mshowPopup(QRSuccess data) {

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.popup_qr_code_success_, null);
        final TextView message = alertLayout.findViewById(R.id.message);
        final TextView done = alertLayout.findViewById(R.id.done);
        /*final EditText etEmail = alertLayout.findViewById(R.id.et_email);
        final CheckBox cbToggle = alertLayout.findViewById(R.id.cb_show_pass);
*/

        message.setText(data.getMessage() + "");
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        AlertDialog dialog = alert.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid(data.getEventId())) {
                    Intent intent = new Intent(EmployerList.this,
                            CheckinActivity.class);
                    intent.putExtra("eventId", data.getEventId());
                    startActivity(intent);
                    dialog.dismiss();
                } else {
                    dialog.dismiss();
                }

            }
        });
    }
}
