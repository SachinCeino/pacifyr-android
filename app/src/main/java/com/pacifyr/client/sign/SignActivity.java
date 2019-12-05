package com.pacifyr.client.sign;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.RelativeLayout;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.pacifyr.client.base.PacifyrActivity;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.pacifyr.client.R;

import org.json.JSONObject;

public class SignActivity extends PacifyrActivity {

    MainTextView pacifyrRequestTxv;
    RelativeLayout relative_bg;

    MainTextView emergencyCtxv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        getCountryData();

        relative_bg = (RelativeLayout) findViewById(R.id.relative_bg);
        relative_bg.setBackground(getResources().getDrawable(R.drawable.loginbg));
        emergencyCtxv = (MainTextView) findViewById(R.id.emergency_ctxv);

        String crisisString = getResources().getString(R.string.if_emergency2);
        //If in emergency call 911 or rush to an emergency room. Crisis Info
        emergencyCtxv.setText(Html.fromHtml("<u><font color=" + getResources().getColor(R.color.om_blue) + ">" + crisisString + "</font></u>"));
        emergencyCtxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignActivity.this, CrisisActivity.class);
                startActivity(intent);
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
