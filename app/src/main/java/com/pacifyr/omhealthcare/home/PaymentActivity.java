package com.pacifyr.omhealthcare.home;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ceino.utilitylibrary.CustomFontTextView;
import com.ceino.utilitylibrary.DeviceFitImageView;
import com.ceino.utilitylibrary.utils.PrefManager;
import com.pacifyr.omhealthcare.Payment.AddCreditCardActivity;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.adapter.ListSavedCardAdapter;
import com.pacifyr.omhealthcare.event.AlertTypeEvent;
import com.pacifyr.omhealthcare.event.NotifyEvent;
import com.pacifyr.omhealthcare.utility.PacifyrAppCompactActivity;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;

import static com.pacifyr.omhealthcare.network.NetworkService.GLOBAL_URL;

public class PaymentActivity extends PacifyrAppCompactActivity {

    private CustomFontTextView paymentMethodCtxv;
    private CustomFontTextView credictCardCtxv;
    private RecyclerView cardRcv;

    private PrefManager prefM;
    private String userToken,userid;
    private ListSavedCardAdapter listSavedCardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        onSetActionBar();

        prefM = PrefManager.getInstance(PaymentActivity.this);
        paymentMethodCtxv = (CustomFontTextView) findViewById(R.id.payment_method_ctxv);
        credictCardCtxv = (CustomFontTextView) findViewById(R.id.credict_card_ctxv);
        cardRcv = (RecyclerView) findViewById(R.id.card_rcv);

        getSavedCard();

        credictCardCtxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(PaymentActivity.this,AddCreditCardActivity.class);
                startActivity(i);
            }
        });
    }

    private void
    getSavedCard() {


        userToken=prefM.getAccessToken();
        userid=prefM.getUserID();

        String url=GLOBAL_URL+"payment/savedcard/"+userid;
        AjaxCallback cb = new AjaxCallback<JSONArray>() {
            @Override
            public void callback(String url, JSONArray json, AjaxStatus status) {
                try {
                    if(isValid(json)) {
                        String jsonString = json.toString();
                        int l=jsonString.length();
                        if(l>3) {
                            credictCardCtxv.setVisibility(View.GONE);
                            cardRcv.setVisibility(View.VISIBLE);
                            listSavedCardAdapter = new ListSavedCardAdapter(PaymentActivity.this, json);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(PaymentActivity.this);
                            cardRcv.setLayoutManager(mLayoutManager);
                            cardRcv.setItemAnimator(new DefaultItemAnimator());
                            cardRcv.setAdapter(listSavedCardAdapter);
                            //String message=json.getString("message");
                        }
                        else{
                            credictCardCtxv.setVisibility(View.VISIBLE);
                            cardRcv.setVisibility(View.GONE);
                        }
                    }else{
                        credictCardCtxv.setVisibility(View.VISIBLE);
                        cardRcv.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        AQuery aq = new AQuery(PaymentActivity.this);
        cb.header("Content-Type", "application/json");
        cb.header("AccessToken",userToken);
        aq.ajax(url,JSONArray.class, cb);

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


    private LinearLayout actionBarTopLlay;
    private RelativeLayout bottomPanel;
    private LinearLayout leftLlay;
    private DeviceFitImageView backImv;
    private CustomFontTextView leftTxv;
    private CustomFontTextView titleTxv;
    private LinearLayout rightLlay;
    private CustomFontTextView rightTxv;
    private DeviceFitImageView rightImv;

    public void onSetActionBar() {
        try {
            actionBarTopLlay = (LinearLayout) findViewById(R.id.action_bar_top_llay);
            bottomPanel = (RelativeLayout) findViewById(R.id.bottom_panel);
            leftLlay = (LinearLayout) findViewById(R.id.left_llay);
            backImv = (DeviceFitImageView) findViewById(R.id.back_imv);
            leftTxv = (CustomFontTextView) findViewById(R.id.left_txv);
            titleTxv = (CustomFontTextView) findViewById(R.id.title_txv);
            rightLlay = (LinearLayout) findViewById(R.id.right_llay);
            rightTxv = (CustomFontTextView) findViewById(R.id.right_txv);
            rightImv = (DeviceFitImageView) findViewById(R.id.right_imv);
            rightImv.setVisibility(View.VISIBLE);
            rightTxv.setVisibility(View.INVISIBLE);
            rightLlay.setVisibility(View.VISIBLE);
            leftTxv.setVisibility(View.INVISIBLE);
            backImv.setVisibility(View.VISIBLE);
            titleTxv.setVisibility(View.VISIBLE);
            leftLlay.setVisibility(View.VISIBLE);
            defSetText(titleTxv, getResources().getString(R.string.payment));
            Typeface type = Typeface.createFromAsset(getAssets(),"fonts/ITCAvantGardeStd-Bold.otf");
            titleTxv.setTypeface(type);
            titleTxv.setTextColor(getResources().getColor(R.color.black));
            backImv.setVisibility(View.VISIBLE);
            leftTxv.setVisibility(View.GONE);
            backImv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            leftLlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
