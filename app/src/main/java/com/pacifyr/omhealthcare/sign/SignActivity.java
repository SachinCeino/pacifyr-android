package com.pacifyr.omhealthcare.sign;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.RelativeLayout;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ceino.utilitylibrary.CustomFontTextView;
import com.ceino.utilitylibrary.utils.PrefManager;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.activity.SplachScreen;
import com.pacifyr.omhealthcare.base.PacifyerActivity;

import org.json.JSONObject;

public class SignActivity extends PacifyerActivity {

    CustomFontTextView pacifyrRequestTxv;
    RelativeLayout relative_bg;

    CustomFontTextView emergencyCtxv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        getCountryData();

        relative_bg = (RelativeLayout) findViewById(R.id.relative_bg);
        relative_bg.setBackground(getResources().getDrawable(R.drawable.loginbg));
        emergencyCtxv = (CustomFontTextView) findViewById(R.id.emergency_ctxv);

        String crisisString = getResources().getString(R.string.if_emergency2);
        //If in emergency call 911 or rush to an emergency room. Crisis Info
        SpannableString spannableString = new SpannableString(crisisString);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(SignActivity.this,CrisisActivity.class);
               startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.om_blue));
                //ds.setUnderlineText(false);
            }
        };

        spannableString.setSpan(clickableSpan,74,85, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        emergencyCtxv.setText(spannableString);
        emergencyCtxv.setMovementMethod(LinkMovementMethod.getInstance());

        /*pacifyrRequestTxv = (CustomFontTextView) findViewById(R.id.pacifyr_request_txv);
        pacifyrRequestTxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignActivity.this,PacifyrRequestAccessActivity.class));
            }
        });*/
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

        String url = "http://ip-api.com/json";
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
        }
    }
}
