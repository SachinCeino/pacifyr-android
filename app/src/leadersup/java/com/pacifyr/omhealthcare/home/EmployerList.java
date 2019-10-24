package com.pacifyr.omhealthcare.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.event.AlertTypeEvent;
import com.pacifyr.omhealthcare.event.NotifyEvent;
import com.pacifyr.omhealthcare.events.fragment.EmployersList;
import com.pacifyr.omhealthcare.model.ModelEvent;
import com.pacifyr.omhealthcare.utility.PacifyrAppCompactActivity;

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
}
