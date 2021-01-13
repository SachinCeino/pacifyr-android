package com.pacifyr.client.sign;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.google.gson.Gson;
import com.pacifyr.client.R;
import com.pacifyr.client.event.AlertTypeEvent;
import com.pacifyr.client.event.BusProvider;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.home.WelcomeTourActivity;
import com.pacifyr.client.model.ApiResponse;
import com.pacifyr.client.orginals.Auth;
import com.pacifyr.client.network.GsonUtils;
import com.pacifyr.client.network.NetworkService;
import com.pacifyr.client.utility.PacifyrAppCompactActivity;
import com.pacifyr.client.utils.FirebaseLogging;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;

import org.json.JSONObject;

public class DiscountCodeActivity extends PacifyrAppCompactActivity {
    private static final String TAG = DiscountCodeActivity.class.getSimpleName();
    private RelativeLayout button_validate;
    private EditText edit_discount;
    private TextView skip;

    @Override
    protected void onStart() {
        super.onStart();
        BusProvider.getInstance().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        BusProvider.getInstance().unregister(this);
    }

    @Override
    protected View getSnackbarAnchorView() {
        return null;
    }

    String from;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discount_activity);

        edit_discount = findViewById(R.id.edit_discount_code);
        button_validate = findViewById(R.id.button_validate);
        skip = findViewById(R.id.text_skip);
        String couponCode = PrefManager.getInstance(DiscountCodeActivity.this).getSharedString(PrefManager.COUPON_CODE, null);

        button_validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit_discount.getText().length() == 0) {
                    edit_discount.setError(getString(R.string.please_enter_a_valid_coupon_code));
                    return;
                }
                edit_discount.setError(null);
                setLoading();
                showProgress();
                String code = edit_discount.getText().toString();
                new NetworkService().validateDiscountCode(code, new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject json, AjaxStatus status) {
                        /*
                         *this must be the first line of any httpCallBacks
                         */
                        Auth.httpStatusFilter(getApplicationContext(),status.getCode());
                        if (json == null) {
                            hideProgress();
                            Toast.makeText(DiscountCodeActivity.this, getString(R.string.invalid_coupon_code), Toast.LENGTH_SHORT).show();
                            Bundle args =
                                    FirebaseLogging.getConstantArgsLoggedIn(DiscountCodeActivity.this);

                            args.putString(FirebaseLogging.EC, code + " : failed");

                            FirebaseLogging.sendLog(DiscountCodeActivity.this,
                                    FirebaseLogging.EVENT_APPLY_COUPON_CODE,
                                    args);
                        } else {
//                            Log.d(TAG, "Resp " + json.toString());
                            PrefManager.getInstance(DiscountCodeActivity.this).putString(PrefManager.COUPON_CODE, null);
                            final ApiResponse apiResponse = new Gson().fromJson(json.toString(), ApiResponse.class);
                            if (apiResponse.isSuccess()) {
                                getUserProfile(getUserID(), new AjaxCallback<JSONObject>() {
                                    @Override
                                    public void callback(String url, JSONObject json, AjaxStatus status) {
                                        hideProgress();
                                        try {
                                            if (json != null) {
                                                NetworkService.mCurrentUser = GsonUtils.getInstance().gsonToModelUserProfile(json);
                                                //ProfileScreenActivity.pacifyerDetial = GsonUtils.getInstance().gsonToModelUserProfile(json);
                                                if (NetworkService.mCurrentUser != null) {
                                                    if (NetworkService.mCurrentUser.getRoleModel().getLabel().equals("employee")) {
                                                        PrefManager prefManager = PrefManager.getInstance(DiscountCodeActivity.this);
                                                        prefManager.saveCompanyID(NetworkService.mCurrentUser.getCompanyId());
                                                        prefManager.saveCustomerRole("employee");
                                                        if (NetworkService.mCurrentUser != null && NetworkService.mCurrentUser.getCompany() != null){
                                                            prefManager.saveCompanyLogo(NetworkService.mCurrentUser.getCompany().getLogo());
                                                        }
                                                        showIOSDialogOK("Success", apiResponse.getMessage(), "ok", new iOSDialogClickListener() {
                                                            @Override
                                                            public void onClick(iOSDialog dialog) {
                                                                //Toast.makeText(PacifyrActivity.this,"Clicked!",Toast.LENGTH_LONG).show();
                                                                dialog.dismiss();
                                                                Intent i = new Intent(DiscountCodeActivity.this, WelcomeTourActivity.class);
                                                                    startActivity(i);
                                                                    finishAffinity();


                                                            }
                                                        });

                                                    }
                                                }

                                            } else {
                                                Toast.makeText(DiscountCodeActivity.this, getResources().getString(R.string.invalid_coupon_code), Toast.LENGTH_LONG).show();
                                                Bundle args =
                                                        FirebaseLogging.getConstantArgsLoggedIn(DiscountCodeActivity.this);

                                                args.putString(FirebaseLogging.EC, code + " : failed");

                                                FirebaseLogging.sendLog(DiscountCodeActivity.this,
                                                        FirebaseLogging.EVENT_APPLY_COUPON_CODE,
                                                        args);
                                            }


                                        } catch (Exception e) {
                                            Toast.makeText(DiscountCodeActivity.this, getResources().getString(R.string.invalid_coupon_code), Toast.LENGTH_LONG).show();
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                Bundle args =
                                        FirebaseLogging.getConstantArgsLoggedIn(DiscountCodeActivity.this);

                                args.putString(FirebaseLogging.EC, "Success : " + code);

                                FirebaseLogging.sendLog(DiscountCodeActivity.this,
                                        FirebaseLogging.EVENT_APPLY_COUPON_CODE,
                                        args);
                            } else if (apiResponse.getMessage() != null) {
                                hideProgress();
                                Toast.makeText(DiscountCodeActivity.this, apiResponse.getMessage(), Toast.LENGTH_LONG).show();

                                Bundle args =
                                        FirebaseLogging.getConstantArgsLoggedIn(DiscountCodeActivity.this);

                                args.putString(FirebaseLogging.EC, code + apiResponse.getMessage());

                                FirebaseLogging.sendLog(DiscountCodeActivity.this,
                                        FirebaseLogging.EVENT_APPLY_COUPON_CODE,
                                        args);

                            }
                        }


                    }
                });

            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent i = new Intent(DiscountCodeActivity.this, WelcomeTourActivity.class);
                    startActivity(i);
                    finishAffinity();

            }
        });

        if (isValid(couponCode)) {
            edit_discount.setText(couponCode);
            button_validate.performClick();
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

}
