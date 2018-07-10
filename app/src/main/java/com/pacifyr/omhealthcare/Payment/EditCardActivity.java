package com.pacifyr.omhealthcare.Payment;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ceino.utilitylibrary.CustomEditText;
import com.ceino.utilitylibrary.CustomFontTextView;
import com.ceino.utilitylibrary.DeviceFitImageView;
import com.ceino.utilitylibrary.utils.PrefManager;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.event.AlertTypeEvent;
import com.pacifyr.omhealthcare.event.NotifyEvent;
import com.pacifyr.omhealthcare.home.DashboardActivity;
import com.pacifyr.omhealthcare.network.NetworkService;
import com.pacifyr.omhealthcare.utility.PacifyrAppCompactActivity;
import com.squareup.otto.Subscribe;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import org.json.JSONObject;

import static com.ceino.utilitylibrary.network.UtilityNetworkService.GLOBAL_URL;
import static com.ceino.utilitylibrary.network.UtilityNetworkService.STRIPE_PUBLISH_KEY;

public class EditCardActivity extends PacifyrAppCompactActivity {

    private CustomEditText cardCedt;
    private CustomEditText expDateCedt;
    private CustomEditText cvvCedt;
    private String cardNumber;
    private String month;
    private String year;
    private String cvv;
    private Token genaratedToken;
    private PrefManager prefM;
    private String userid,token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_card);
        onSetActionBar();
        Bundle b=getIntent().getExtras();
        String exp = b.getString("expiry");
        String number = b.getString("number");

        cardCedt = (CustomEditText) findViewById(R.id.card_cedt);
        expDateCedt = (CustomEditText) findViewById(R.id.exp_date_cedt);
        cvvCedt = (CustomEditText) findViewById(R.id.cvv_cedt);
        cardCedt.setText("****"+number);
        expDateCedt.setText(exp);


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
        actionBarTopLlay = (LinearLayout) findViewById(R.id.action_bar_top_llay);
        bottomPanel = (RelativeLayout) findViewById(R.id.bottom_panel);
        leftLlay = (LinearLayout) findViewById(R.id.left_llay);
        backImv = (DeviceFitImageView) findViewById(R.id.back_imv);
        leftTxv = (CustomFontTextView) findViewById(R.id.left_txv);
        titleTxv = (CustomFontTextView) findViewById(R.id.title_txv);
        rightLlay = (LinearLayout) findViewById(R.id.right_llay);
        rightTxv = (CustomFontTextView) findViewById(R.id.right_txv);
        rightImv = (DeviceFitImageView) findViewById(R.id.right_imv);
        rightImv.setVisibility(View.INVISIBLE);
       /* rightTxv.setVisibility(View.INVISIBLE);
        rightLlay.setVisibility(View.INVISIBLE);*/
        leftTxv.setVisibility(View.INVISIBLE);
        backImv.setVisibility(View.VISIBLE);
        titleTxv.setVisibility(View.VISIBLE);
        leftLlay.setVisibility(View.VISIBLE);
        defSetText(titleTxv,"Edit Card");
        rightTxv.setText("Done");
        rightTxv.setTextColor(getResources().getColor(R.color.color_primary));
        titleTxv.setTextColor(getResources().getColor(R.color.black));
        //backImv.setBackgroundResource(R.drawable.leftmen);
        leftLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rightTxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cardNumber = cardCedt.getEditableText().toString();
                String ex = expDateCedt.getEditableText().toString();
                cvv = cvvCedt.getEditableText().toString();
                month = ex.substring(0, 2);
                year = ex.substring(3, 7);

                if(isValidCard()) {
                    setLoading();
                    showProgress();

                    Card card = new Card(cardNumber, Integer.parseInt(month), Integer.parseInt(year), cvv);
                    if (!card.validateCard()) {
                        Toast.makeText(EditCardActivity.this, "Invalid Card", Toast.LENGTH_LONG
                        ).show();
                        hideProgress();
                    } else {
                        Stripe stripe = new Stripe(EditCardActivity.this, STRIPE_PUBLISH_KEY);
                        stripe.createToken(
                                card,
                                new TokenCallback() {
                                    public void onSuccess(Token token) {
                                        // Send token to your server
                                        String type = token.getType();
                                        genaratedToken = token;
                                        //Toast.makeText(EditCardActivity.this, token.getId(), Toast.LENGTH_LONG).show();
                                        deletOldCard();
                                    }

                                    public void onError(Exception error) {
                                        // Show localized error message
                                        Toast.makeText(EditCardActivity.this,
                                                error.getMessage(),
                                                Toast.LENGTH_LONG
                                        ).show();
                                        hideProgress();
                                    }
                                }
                        );
                    }
                }
            }
        });
    }

    private boolean isValidCard() {
        boolean ret=true;
        if(!isValid(cardNumber))
        {
            //Toast.makeText(EditCardActivity.this, cardNo, Toast.LENGTH_LONG).show();
            cardCedt.setError("Please enter valid card number");
            ret = false;
        }
        if(!isValid(month))
        {
            //Toast.makeText(EditCardActivity.this, month, Toast.LENGTH_LONG).show();
            expDateCedt.setError("Please enter valid Expiry Date");
            ret = false;

        }
        if(!isValid(year))
        {
            //Toast.makeText(EditCardActivity.this, year, Toast.LENGTH_LONG).show();
            expDateCedt.setError("Please enter valid Expiry Date");
            ret = false;

        }
        if(!isValid(cvv))
        {
            //Toast.makeText(EditCardActivity.this, cvv, Toast.LENGTH_LONG).show();
            cvvCedt.setError("Please enter valid cvv");
            ret = false;

        }
        return ret;
    }

    private void deletOldCard() {
        try {
            prefM = PrefManager.getInstance(EditCardActivity.this);
            token = prefM.getAccessToken();
            userid = prefM.getUserID();
            String url = GLOBAL_URL + "payment/deletecard/" + userid;
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    try {
                        //String message=json.getString("message");
                        String my=json.toString();
                        String  st=status.getMessage();
                        saveCardDetails();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            AQuery aq = new AQuery(EditCardActivity.this);
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken",token);
            aq.delete(url,JSONObject.class, cb);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void saveCardDetails() {
        prefM= PrefManager.getInstance(EditCardActivity.this);
        token=prefM.getAccessToken();
        userid=prefM.getUserID();

        String url = NetworkService.GLOBAL_URL+"payment/saveCard";
        AjaxCallback cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                try {
                    String message=json.getString("message");
                    String my=json.toString();
                    if(message.equals("Card added")){
                        hideProgress();
                        setStatus("Succesfully added your card", R.color.md_green_400);
                        showStatus();
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                hideStatus();
                                Intent i= new Intent(EditCardActivity.this,DashboardActivity.class);
                                startActivity(i);
                            }
                        }, 2000);

                    }else {
                        hideProgress();
                        setStatus("Error occured. Please try again.", R.color.md_red_500);
                        showStatus();
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                hideStatus();
                            }
                        }, 2000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        AQuery aq = new AQuery(EditCardActivity.this);
        cb.header("Content-Type", "application/json");
        cb.header("AccessToken",token);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.putOpt("userId", userid);
            jsonObject.putOpt("tokenId", genaratedToken.getId());
            aq.post(url, jsonObject, JSONObject.class, cb);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        // {"userId":"594bb69edc224d15fc8cb936",
        // "tokenId":"tok_1Ajl5MEreQwimW2kjnr34bYl" }
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
