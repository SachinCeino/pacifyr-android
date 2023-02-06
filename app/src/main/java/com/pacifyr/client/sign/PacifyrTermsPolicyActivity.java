package com.pacifyr.client.sign;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.pacifyr.client.customview.MainTextView;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;
import com.pacifyr.client.R;
import com.pacifyr.client.activity.TermsOfUseActivity;
import com.pacifyr.client.event.AlertTypeEvent;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.home.PacifyrDashboardActivity;
import com.pacifyr.client.network.NetworkService;
import com.pacifyr.client.utility.PacifyrAppCompactActivity;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class PacifyrTermsPolicyActivity extends PacifyrAppCompactActivity {

    private LinearLayout topLlay;
    private CircleImageView profileImageCimv;
    private MainTextView profileNameCtxv;
    private MainTextView pleaseCtxv;
    private MainTextView submitCtxv;
    private boolean termsFlag;
    private boolean privacyFlag;
    private boolean agreementFlag;
    private MainTextView termsCtxv;
    private String  webViewUrl;
    private MainTextView privacyCtxv;
    private MainTextView agreementCtxv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pacifyr_terms_policy);

        try {

            topLlay = (LinearLayout) findViewById(R.id.top_llay);
            profileImageCimv = (CircleImageView) findViewById(R.id.profile_image_cimv);
            profileNameCtxv = (MainTextView) findViewById(R.id.profile_name_ctxv);
            pleaseCtxv = (MainTextView) findViewById(R.id.please_ctxv);
            submitCtxv = (MainTextView) findViewById(R.id.submit_ctxv);
            termsCtxv = (MainTextView) findViewById(R.id.terms_ctxv);
            privacyCtxv = (MainTextView) findViewById(R.id.privacy_ctxv);
            agreementCtxv = (MainTextView) findViewById(R.id.agreement_ctxv);
            submitCtxv.setClickable(false);

            setProfileValues();


            termsCtxv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(PacifyrTermsPolicyActivity.this,TermsOfUseActivity.class);
                    webViewUrl ="https://www.pacifyr.com/terms-of-use";
                    i.putExtra("spurl", webViewUrl);
                    startActivity(i);
                }
            });

            privacyCtxv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(PacifyrTermsPolicyActivity.this,TermsOfUseActivity.class);
                    webViewUrl ="https://www.pacifyr.com/terms-of-use";
                    i.putExtra("spurl", webViewUrl);
                    startActivity(i);
                }
            });

            agreementCtxv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(PacifyrTermsPolicyActivity.this,TermsOfUseActivity.class);
                    webViewUrl ="https://www.pacifyr.com/business-associate-agreement";
                    i.putExtra("spurl", webViewUrl);
                    startActivity(i);
                }
            });

            getTermsCbox().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        if(getAgreementCbox().isChecked() && getPrivacyCbox().isChecked()){
                            submitCtxv.setClickable(true);
                            submitCtxv.setBackground(getResources().getDrawable(R.drawable.blue_border_lightblue));
                        }
                    }
                    else {
                        submitCtxv.setClickable(false);
                        submitCtxv.setBackground(getResources().getDrawable(R.drawable.disabled_blue_backround));
                    }
                }
            });


            getPrivacyCbox().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        if(getAgreementCbox().isChecked() && getTermsCbox().isChecked()){
                            submitCtxv.setClickable(true);
                            submitCtxv.setBackground(getResources().getDrawable(R.drawable.blue_border_lightblue));
                        }
                    } else {
                        submitCtxv.setClickable(false);
                        submitCtxv.setBackground(getResources().getDrawable(R.drawable.disabled_blue_backround));
                    }
                }
            });


            getAgreementCbox().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        if(getPrivacyCbox().isChecked() && getTermsCbox().isChecked()){
                            submitCtxv.setClickable(true);
                            submitCtxv.setBackground(getResources().getDrawable(R.drawable.blue_border_lightblue));
                        }
                    } else {
                        submitCtxv.setClickable(false);
                        submitCtxv.setBackground(getResources().getDrawable(R.drawable.disabled_blue_backround));
                    }
                }
            });

            submitCtxv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setTermsAccepted();
                }
            });

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void setTermsAccepted() {
        try {
            NetworkService.editProfileObject = new JSONObject();
            NetworkService.editProfileObject.putOpt("isTermsAccepted", true);
            editProfileWeb();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void editProfileWeb() {
        try {
            setLoading();
            showProgress();
            PrefManager prefM = PrefManager.getInstance(PacifyrTermsPolicyActivity.this);
            String token = prefM.getAccessToken();
            String userid = prefM.getUserID();
            if (isValid(NetworkService.editProfileObject)) {
                String url = NetworkService.GLOBAL_URL + "user/editprofile/" + userid;
                com.koushikdutta.async.future.FutureCallback<JsonObject> futureCallback = new com.koushikdutta.async.future.FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {
                            hideProgress();
                            if (isValid(result)) {
                                final JSONObject jsonObject = new JSONObject(result.toString());
                                String js = result.toString();
                                String phone = jsonObject.getString("phoneNumber");

                                Intent i = new Intent(PacifyrTermsPolicyActivity.this, PacifyrDashboardActivity.class);
                                startActivity(i);
                                finish();

                            }
                        } catch (Exception eee) {
                            //hideProgress();
                            Toast.makeText(PacifyrTermsPolicyActivity.this, "Network Error, Please try again!", Toast.LENGTH_SHORT).show();
                            eee.printStackTrace();
                        }
                    }
                };
                String json = NetworkService.editProfileObject.toString();
                Ion.with(PacifyrTermsPolicyActivity.this).load(url).setHeader("AccessToken", token).setHeader("enctype", "multipart/form-data").setMultipartParameter("data", json).asJsonObject().setCallback(futureCallback);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    private void setProfileValues() {
        try {
            profileNameCtxv.setText(getCurrentUserName());
            String imageurl=getCurrentUserImageUrl();
            if (isValid(imageurl)) {
                Picasso.with(PacifyrTermsPolicyActivity.this).load(imageurl).placeholder(R.drawable.placeholder_user).into(profileImageCimv);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private CheckBox getTermsCbox(){
        return (CheckBox) findViewById(R.id.terms_cbox);
    }

    private CheckBox getPrivacyCbox(){
        return (CheckBox) findViewById(R.id.privacy_cbox);
    }

    private CheckBox getAgreementCbox(){
        return (CheckBox) findViewById(R.id.agreement_cbox);
    }


    @Override
    public void onSetValues() throws Exception {

    }

    @Subscribe
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
