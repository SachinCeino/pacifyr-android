package com.pacifyr.client.Payment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.pacifyr.client.model.SubscriptionSavedCard;
import com.pacifyrapp.utilitylibrary.CustomEditText;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.pacifyr.client.R;
import com.pacifyr.client.event.AlertTypeEvent;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.home.DashboardActivity;
import com.pacifyr.client.network.NetworkService;
import com.pacifyr.client.utility.PacifyrAppCompactActivity;
import com.squareup.otto.Subscribe;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import org.json.JSONObject;

import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.GLOBAL_URL;
import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.STRIPE_PUBLISH_KEY;

public class EditCardActivity extends PacifyrAppCompactActivity {

    private EditText EditText_cardNumber;
    private EditText EditText_cardMonhEdt,EditText_cardYearEdt,EditText_cardCvvEdt;
    private String cardNumber;
    private String month;
    private String year;
    private String cvv;
    private Token genaratedToken;
    private PrefManager prefM;
    private String userid,token;

    SubscriptionSavedCard object;
    RelativeLayout save_rlay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_card);
        onSetActionBar();


           if ( isValid(getIntent()) && isValid(getIntent().getExtras())
           && isValid(getIntent().getExtras().getSerializable("object")))
           {
               object= (SubscriptionSavedCard) getIntent().getExtras().getSerializable("object");
           }

        EditText_cardMonhEdt = (EditText) findViewById(R.id.card_month_edt);
        EditText_cardYearEdt = (EditText) findViewById(R.id.card_year_edt);
        EditText_cardCvvEdt = (EditText) findViewById(R.id.cvv_cedt);
        save_rlay=findViewById(R.id.save_rlay);

        EditText_cardNumber = (EditText) findViewById(R.id.card_cedt);

        if (isValid(object))
        {
            if (isValid(object.getCardLast4()))
                EditText_cardNumber.setText("****"+object.getCardLast4());
            if (isValid(object.getCardExpiryMonth()))
                EditText_cardMonhEdt.setText(object.getCardExpiryMonth()+"");
            if (isValid(object.getCardExpiryYear()))
                EditText_cardYearEdt.setText(object.getCardExpiryYear()+"");
            EditText_cardCvvEdt.setText("***");

        }


        save_rlay.setVisibility(View.GONE);
        save_rlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateAndSubmitEditedCard();
            }
        });




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
        actionBarTopLlay = findViewById(R.id.action_bar_top_llay);
        bottomPanel = findViewById(R.id.bottom_panel);
        leftLlay = findViewById(R.id.left_llay);
        backImv = findViewById(R.id.back_imv);
        leftTxv = findViewById(R.id.left_txv);
        titleTxv = findViewById(R.id.title_txv);
        rightLlay = findViewById(R.id.right_llay);
        rightTxv = findViewById(R.id.right_txv);
        rightImv = findViewById(R.id.right_imv);
        rightImv.setVisibility(View.INVISIBLE);
       /* rightTxv.setVisibility(View.INVISIBLE);
        rightLlay.setVisibility(View.INVISIBLE);*/
        leftTxv.setVisibility(View.INVISIBLE);
        backImv.setVisibility(View.VISIBLE);
        titleTxv.setVisibility(View.VISIBLE);
        leftLlay.setVisibility(View.VISIBLE);
        defSetText(titleTxv,"EDIT CARD");
        Typeface type = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.avantica_bold_header));
        titleTxv.setTypeface(type);
        rightTxv.setText("Done");
        rightTxv.setTextColor(getResources().getColor(R.color._color_action_bar_text_color));
        titleTxv.setTextColor(getResources().getColor(R.color._color_action_bar_text_color));
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

               ValidateAndSubmitEditedCard();
            }
        });
    }

    private void ValidateAndSubmitEditedCard() {
        cardNumber = EditText_cardNumber.getEditableText().toString();
        cvv = EditText_cardCvvEdt.getEditableText().toString();
        month = EditText_cardMonhEdt.getEditableText().toString();
        year = EditText_cardYearEdt.getEditableText().toString();
        setLoading("loading...");
        showProgress();
        if(isValidCard()) {
            Card card = new Card(cardNumber, Integer.parseInt(month), Integer.parseInt(year), cvv);
            if (!card.validateCard()) {
                hideProgress();
                Toast.makeText(EditCardActivity.this, "Invalid card details", Toast.LENGTH_LONG).show();
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
                                hideProgress();
                                deletOldCard();
                            }

                            public void onError(Exception error) {
                                // Show localized error message
                                hideProgress();
                                Toast.makeText(EditCardActivity.this,
                                        error.getMessage(),
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        }
                );
            }
        }
    }

    private boolean isValidCard() {
        boolean ret=true;
        if(!isValid(cardNumber))
        {
            //Toast.makeText(EditCardActivity.this, cardNo, Toast.LENGTH_LONG).show();
            EditText_cardNumber.setError("Enter valid card number");
            ret = false;
        }
        if(!isValid(month))
        {
            //Toast.makeText(EditCardActivity.this, month, Toast.LENGTH_LONG).show();
            EditText_cardMonhEdt.setError("Enter valid month");
            ret = false;

        }
        if(!isValid(year))
        {
            //Toast.makeText(EditCardActivity.this, year, Toast.LENGTH_LONG).show();
            EditText_cardYearEdt.setError("Enter valid year");
            ret = false;

        }
        if(!isValid(cvv))
        {
            //Toast.makeText(EditCardActivity.this, cvv, Toast.LENGTH_LONG).show();
            EditText_cardCvvEdt.setError("Enter valid cvv");
            ret = false;

        }
        return ret;
    }

    private void deletOldCard() {
        try {
            setLoading("loading...");
            showProgress();
            prefM = PrefManager.getInstance(EditCardActivity.this);
            token = prefM.getAccessToken();
            userid = prefM.getUserID();
            String url = GLOBAL_URL + "payment/deletecard/" + userid;
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    try {
                        hideProgress();
                        //String message=json.getString("message");
                        String my=json.toString();
                        String  st=status.getMessage();
                        saveCardDetails();

                    } catch (Exception e) {
                        hideProgress();
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
            hideProgress();
            ex.printStackTrace();
        }
    }

    private void saveCardDetails() {
        prefM= PrefManager.getInstance(EditCardActivity.this);
        token=prefM.getAccessToken();
        userid=prefM.getUserID();
        setLoading("loading...");
        showProgress();
        String url = NetworkService.GLOBAL_URL+"payment/saveCard";
        AjaxCallback cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                try {
                    String message=json.getString("message");
                    String my=json.toString();
                    if(message.equals("Card added")){
                        hideProgress();
                        setStatus(getResources().getString(R.string.successfully_updated_your_card), com.pacifyrapp.utilitylibrary.R.color.md_green_400);
                        showStatus();
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                hideStatus();
                                finish();
                            }
                        }, 2000);

                    }else {
                        hideProgress();
                        setStatus("Error occured. Please try again.", com.pacifyrapp.utilitylibrary.R.color.md_red_500);
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
                    hideStatus();
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
