package com.pacifyr.client.Payment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.pacifyr.client.R;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.event.AlertTypeEvent;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.quickblox.chat.ui.activity.ChatActivity;
import com.pacifyr.client.receiver.NotificationUtils;
import com.pacifyr.client.twilio.VideoActivity;
import com.pacifyr.client.utility.PacifyrAppCompactActivity;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.quickblox.users.model.QBUser;
import com.squareup.otto.Subscribe;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import static com.pacifyr.client.network.NetworkService.GLOBAL_URL;
import static com.pacifyr.client.twilio.VideoActivity.isCallActive;
import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.STRIPE_PUBLISH_KEY;

/**

 */

public class AlertAddCreditCardActivity extends PacifyrAppCompactActivity {

	RelativeLayout saveRlay;
	private EditText cardNumber;
	private EditText cardMonhEdt,cardYearEdt,cardCvvEdt;

	private String month,year,cvv,cardNo;
	private Token genaratedToken;
	private String userid,userToken;
	private PrefManager prefM;
	public static final int REQUEST_CODE_ADD_CARD = 12;
	public static final int REQUEST_CODE_STRIPE_CARD = 4646;
	public CardAddListener cardAddListener;

	public CardAddListener getCardAddListener() {
		return cardAddListener;
	}

	public void setCardAddListener(CardAddListener cardAddListener) {
		this.cardAddListener = cardAddListener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_credit_card);
		onSetActionBar();
		initializeViews();
		cardAddListener = getIntent() != null ? (CardAddListener) getIntent().getSerializableExtra("listener") :
				null;
		//59ca23f1dc224d0f8008347b
		//151238166488392f68d0fc6f144f58b20c50473451b78

		saveRlay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				if(isValidCard()) {
					performCardSave();
				}
				else{
					Toast.makeText(AlertAddCreditCardActivity.this, "Invalid Card", Toast.LENGTH_LONG).show();

				}
			}
		});

	}

	private void saveCardDetails() {
		prefM = PrefManager.getInstance(AlertAddCreditCardActivity.this);
		userToken = prefM.getAccessToken();
		userid = prefM.getUserID();

		String url = GLOBAL_URL + "payment/saveCard";
		AjaxCallback cb = new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject json, AjaxStatus status) {
				try {
					String message = json.getString("message");
					String my = json.toString();
					if (message.equals("Card added")) {

						setStatus("Successfully added your card", R.color.md_green_400);
						showStatus();
						final Handler handler = new Handler();
						handler.postDelayed(new Runnable() {
							@Override
							public void run() {
								hideStatus();
								Intent intent = new Intent();
								intent.putExtra("CardAdded", false);
								setResult(RESULT_OK, intent);
								finish();
							}
						}, 2000);

					} else {
						setStatus("Error occured. Please try again.", R.color.md_red_500);
						showStatus();
						final Handler handler = new Handler();
						handler.postDelayed(new Runnable() {
							@Override
							public void run() {
								hideStatus();
							}
						}, 2000);

						Intent intent = new Intent();
						intent.putExtra("CardAdded", false);
						setResult(RESULT_CANCELED, intent);
						finish();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		AQuery aq = new AQuery(AlertAddCreditCardActivity.this);
		cb.header("Content-Type", "application/json");
		cb.header("AccessToken", userToken);
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.putOpt("userId", userid);
			jsonObject.putOpt("tokenId", genaratedToken.getId());
			aq.post(url, jsonObject, JSONObject.class, cb);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// {"userId":"594bb69edc224d15fc8cb936",
		// "tokenId":"tok_1Ajl5MEreQwimW2kjnr34bYl" }
	}


	@Override
	protected View getSnackbarAnchorView() {
		return null;
	}

	private boolean isValidCard() {
		boolean ret=true;
		if(!isValid(cardNo))
		{
			//Toast.makeText(AlertAddCreditCardActivity.this, cardNo, Toast.LENGTH_LONG).show();
			cardNumber.setError("Please enter valid card number");
			ret = false;
		}
		if(!isValid(month))
		{
			//Toast.makeText(AlertAddCreditCardActivity.this, month, Toast.LENGTH_LONG).show();
			cardMonhEdt.setError("Please enter valid month");
			ret = false;

		}
		if(!isValid(year))
		{
			//Toast.makeText(AlertAddCreditCardActivity.this, year, Toast.LENGTH_LONG).show();
			cardYearEdt.setError("Please enter valid year");
			ret = false;

		}
		if(!isValid(cvv))
		{
			//Toast.makeText(AlertAddCreditCardActivity.this, cvv, Toast.LENGTH_LONG).show();
			cardCvvEdt.setError("Please enter valid cvv");
			ret = false;

		}
		return ret;
	}

	private void performCardSave() {

		if(!isValid(cvv,"Invalid CVV"))
			return;
		setLoadingWait();
		showProgress();
		//Card card = new Card("4242424242424242", 12, 2018, "123");


		Card card = new Card(cardNo,Integer.parseInt(month), Integer.parseInt(year), cvv);
		// Remember to validate the card object before you use it to save time.
		if (!card.validateCard()) {
			Toast.makeText(AlertAddCreditCardActivity.this, "Invalid Card", Toast.LENGTH_LONG).show();
			hideProgress();
		}
		else {
			showProgress();
			Stripe stripe = new Stripe(AlertAddCreditCardActivity.this,STRIPE_PUBLISH_KEY);
			stripe.createToken(
					card,
					new TokenCallback() {
						public void onSuccess(Token token) {
							// Send token to your server
							String type = token.getType();
							genaratedToken=token;
//							Toast.makeText(AlertAddCreditCardActivity.this, token.getId(), Toast.LENGTH_LONG).show();
							hideProgress();
							saveCardDetails();
						}

						public void onError(Exception error) {
							// Show localized error message
							Toast.makeText(AlertAddCreditCardActivity.this,
									error.getMessage(),
									Toast.LENGTH_LONG
							).show();
							hideProgress();
						}
					}
			);
		}
	}

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
		rightTxv.setVisibility(View.INVISIBLE);
		rightLlay.setVisibility(View.INVISIBLE);
		leftTxv.setVisibility(View.INVISIBLE);
		backImv.setVisibility(View.VISIBLE);
		titleTxv.setVisibility(View.VISIBLE);
		leftLlay.setVisibility(View.VISIBLE);
		defSetText(titleTxv, "Add Credit Card");
		//titleTxv.setText("Add Credit Card");
		titleTxv.setTextColor(getResources().getColor(R.color._color_action_bar_text_color));
		//backImv.setBackgroundResource(R.drawable.leftmen);
		leftLlay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (cardAddListener != null) {
					cardAddListener.onAdded(false);
				}
				Intent intent = new Intent();
				intent.putExtra("CardAdded", false);
				setResult(RESULT_CANCELED, intent);
				finish();

			}
		});
	}

	private void initializeViews() {

		saveRlay = findViewById(R.id.save_rlay);
		cardMonhEdt = findViewById(R.id.card_month_edt);
		cardYearEdt = findViewById(R.id.card_year_edt);
		cardCvvEdt = findViewById(R.id.card_cvv_edt);
		cardNumber = findViewById(R.id.card_number_edt);
		cardNumber.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
               /* if(your_string.equals(String.valueOf(s))) {
                    //do something
                }else{
                    //do something
                }*/
			}

			@Override
			public void afterTextChanged(Editable s) {
				cardNo=s.toString();
			}
		});

		cardMonhEdt.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
               /* if(your_string.equals(String.valueOf(s))) {
                    //do something
                }else{
                    //do something
                }*/
			}

			@Override
			public void afterTextChanged(Editable s) {
				month=s.toString();
			}
		});

		cardCvvEdt.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
               /* if(your_string.equals(String.valueOf(s))) {
                    //do something
                }else{
                    //do something
                }*/
			}

			@Override
			public void afterTextChanged(Editable s) {
				cvv=s.toString();
			}
		});

		cardYearEdt.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
               /* if(your_string.equals(String.valueOf(s))) {
                    //do something
                }else{
                    //do something
                }*/
			}

			@Override
			public void afterTextChanged(Editable s) {
				year=s.toString();
			}
		});
		year=cardYearEdt.getText().toString();
		month=cardMonhEdt.getText().toString();
		cvv=cardCvvEdt.getText().toString();
		cardNo = cardNumber.getText().toString();
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


	private LinearLayout actionBarTopLlay;
	private RelativeLayout bottomPanel;
	private LinearLayout leftLlay;
	private DeviceFitImageView backImv;
    private MainTextView leftTxv;
    private MainTextView titleTxv;
	private LinearLayout rightLlay;
    private MainTextView rightTxv;
	private DeviceFitImageView rightImv;

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if (cardAddListener != null) {
			cardAddListener.onAdded(false);
		}
		Intent intent = new Intent();
		intent.putExtra("CardAdded", false);
		setResult(RESULT_CANCELED, intent);
		finish();
	}

	public interface CardAddListener extends Serializable {
		void onAdded(boolean status);
	}
}
