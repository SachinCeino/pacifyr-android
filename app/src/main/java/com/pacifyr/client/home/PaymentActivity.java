package com.pacifyr.client.home;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.event.RateSessionEvent;
import com.pacifyr.client.model.SubscriptionSavedCard;
import com.pacifyr.client.network.GsonUtils;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.pacifyr.client.Payment.AddCreditCardActivity;
import com.pacifyr.client.R;
import com.pacifyr.client.adapter.ListSavedCardAdapter;
import com.pacifyr.client.event.AlertTypeEvent;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.utility.PacifyrAppCompactActivity;
import com.squareup.otto.Subscribe;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;

import java.util.List;

import static com.pacifyr.client.fragment.schedule.ScheduleBottomSheetFragment.isCardAddedFromAppointment;
import static com.pacifyr.client.network.NetworkService.GLOBAL_URL;
import static com.pacifyr.client.utils.Constants.PAYMENT_CARD_ADDED;
import static com.pacifyr.client.utils.Constants.PAYMENT_CARD_ADDED_FINISH;

public class PaymentActivity extends PacifyrAppCompactActivity {

    private MainTextView paymentMethodCtxv;
    private MainTextView credictCardCtxv;
    private RecyclerView cardRcv;

    private PrefManager prefM;
    private String userToken,userid;
    private ListSavedCardAdapter listSavedCardAdapter;
    private boolean isFromBooking=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        onSetActionBar();

        prefM = PrefManager.getInstance(PaymentActivity.this);
        paymentMethodCtxv = findViewById(R.id.payment_method_ctxv);
        credictCardCtxv = findViewById(R.id.credict_card_ctxv);
        cardRcv = findViewById(R.id.card_rcv);

        if (isValid(getIntent())&&isValid(getIntent().getExtras()) && isValid(getIntent().getExtras().getBoolean("isFromBooking")))
        {
            isFromBooking=getIntent().getExtras().getBoolean("isFromBooking");
        }



        credictCardCtxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(PaymentActivity.this,AddCreditCardActivity.class);
                i.putExtra("isFromBooking",isFromBooking);
                startActivity(i);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        getSavedCard();
    }

    private void getSavedCard() {


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
                        List<SubscriptionSavedCard> mListCards= GsonUtils.getInstance().gsonToModelSubscriptionSavedCardModel(json);
                        Log.w("BRUCELEEE","getSavedCard"+ jsonString);
                        if (isValidSize(mListCards))
                        {
                            credictCardCtxv.setVisibility(View.GONE);
                            cardRcv.setVisibility(View.VISIBLE);
                            listSavedCardAdapter = new ListSavedCardAdapter(PaymentActivity.this, mListCards);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(PaymentActivity.this);
                            cardRcv.setLayoutManager(mLayoutManager);
                            cardRcv.setItemAnimator(new DefaultItemAnimator());
                            cardRcv.setAdapter(listSavedCardAdapter);
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
    private MainTextView leftTxv;
    private MainTextView titleTxv;
    private LinearLayout rightLlay;
    private MainTextView rightTxv;
    private DeviceFitImageView rightImv;

    public void onSetActionBar() {
        try {
            actionBarTopLlay = findViewById(R.id.action_bar_top_llay);
            bottomPanel = findViewById(R.id.bottom_panel);
            leftLlay = findViewById(R.id.left_llay);
            backImv = findViewById(R.id.back_imv);
            leftTxv = findViewById(R.id.left_txv);
            titleTxv = findViewById(R.id.title_txv);
            rightLlay = findViewById(R.id.right_llay);
            rightTxv = findViewById(R.id.right_txv);
            rightImv = findViewById(R.id.right_imv);
            rightImv.setVisibility(View.VISIBLE);
            rightTxv.setVisibility(View.INVISIBLE);
            rightLlay.setVisibility(View.VISIBLE);
            leftTxv.setVisibility(View.INVISIBLE);
            backImv.setVisibility(View.VISIBLE);
            titleTxv.setVisibility(View.VISIBLE);
            leftLlay.setVisibility(View.VISIBLE);
            defSetText(titleTxv, getResources().getString(R.string.payment));
            Typeface type = Typeface.createFromAsset(getAssets(),"fonts/ActionBarCommonFont.otf");
            titleTxv.setTypeface(type);
            titleTxv.setTextColor(getResources().getColor(R.color._color_action_bar_text_color));
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

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @org.greenrobot.eventbus.Subscribe(threadMode = ThreadMode.MAIN)
    public void onCardAddedForAppointment(String event) {
        try {
           if (isValid(event) && event.equals(PAYMENT_CARD_ADDED))
           {
               isCardAddedFromAppointment=true;
               finish();
           }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
