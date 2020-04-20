package com.pacifyr.client.sign;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.pacifyr.client.activity.TermsOfUseActivity;
import com.pacifyr.client.base.PacifyrActivity;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.pacifyr.client.R;

import org.json.JSONObject;

import java.util.Locale;


public class SignActivity extends PacifyrActivity {

    MainTextView pacifyrRequestTxv;
    RelativeLayout relative_bg;

    MainTextView emergencyCtxv,covidMessage_ctxv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);


        relative_bg = (RelativeLayout) findViewById(R.id.relative_bg);
        if (!getPackageName().equalsIgnoreCase("com.pacifyr.pam"))
            relative_bg.setBackground(getResources().getDrawable(R.drawable.loginbg));
        emergencyCtxv = (MainTextView) findViewById(R.id.emergency_ctxv);
        covidMessage_ctxv = findViewById(R.id.covidMessage_ctxv);
        getCountryData();
        String crisisString = getResources().getString(R.string.if_emergency2);
        //If in emergency call 911 or rush to an emergency room. Crisis Info
        emergencyCtxv.setText(Html.fromHtml("<font color=" + getResources().getColor(R.color.om_blue) + ">" + crisisString + "</font><u> Crisis Info</u>"));
        emergencyCtxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignActivity.this, CrisisActivity.class);
                startActivity(intent);
            }
        });

        covidMessage_ctxv.setText(Html.fromHtml("<font color=" + getResources().getColor(R.color._color_thirty_free) + ">" + getResources().getString(R.string.covid_message) + "</font><font color=" + getResources().getColor(R.color.colorPrimary) + "><u> COVID Info.</u>"));
        covidMessage_ctxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(SignActivity.this,TermsOfUseActivity.class);
                i.putExtra("title", "Covid-19");
                i.putExtra("spurl", "https://www.cdc.gov/coronavirus/2019-ncov/downloads/sick-with-2019-nCoV-fact-sheet.pdf");
                startActivity(i);
            }
        });

        fbLogout();


    }



    @Override
    protected View getSnackbarAnchorView() {
        return null;
    }


    public void showSignUp(View view) {
        Intent i =new Intent(this,SignUpActivity.class);
        startActivity(i);
    }

    public void showLogin(View view) {
        Intent i =new Intent(this,LoginActivity.class);
        startActivity(i);
    }

    private void getCountryData(){

        final PrefManager prefM = PrefManager.getInstance(SignActivity.this);

        TelephonyManager tm = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        String countryCodeValue = tm.getNetworkCountryIso().toUpperCase();

        if (isValid(countryCodeValue.toUpperCase()))
            prefM.saveCountryCode(countryCodeValue.toUpperCase());
        Log.w("@@@@",prefM.getCountryCode());
        if (!prefM.getCountryCode().equals("US")){
            emergencyCtxv.setVisibility(View.GONE);
        }

        /*String url = "http://ip-api.com/json";
        AjaxCallback cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                try {
                   String js=status.getMessage();
                   String countryCode = json.getString("countryCode");
                   if (!countryCode.equals("US")){
                       emergencyCtxv.setVisibility(View.GONE);
                   }
                   prefM.saveCountryCode(countryCode);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        try {
            AQuery aq = new AQuery(SignActivity.this);
            aq.ajax(url,JSONObject.class, cb);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }*/
    }
}
