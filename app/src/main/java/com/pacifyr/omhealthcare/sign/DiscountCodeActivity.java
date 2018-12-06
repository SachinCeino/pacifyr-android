package com.pacifyr.omhealthcare.sign;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ceino.utilitylibrary.utils.PrefManager;
import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.google.gson.Gson;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.base.PacifyerActivity;
import com.pacifyr.omhealthcare.event.BusProvider;
import com.pacifyr.omhealthcare.home.WelcomeTourActivity;
import com.pacifyr.omhealthcare.model.ApiResponse;
import com.pacifyr.omhealthcare.network.GsonUtils;
import com.pacifyr.omhealthcare.network.NetworkService;

import org.json.JSONObject;

public class DiscountCodeActivity extends PacifyerActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discount_activity);

        edit_discount = findViewById(R.id.edit_discount_code);
        button_validate = findViewById(R.id.button_validate);
        skip = findViewById(R.id.text_skip);

        button_validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit_discount.getText().length() == 0) {
                    edit_discount.setError("required");
                    return;
                }
                edit_discount.setError(null);
                setLoading();
                showProgress();
                String code = edit_discount.getText().toString();
                new NetworkService().validateDiscountCode(code, new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject json, AjaxStatus status) {
                        if (json == null) {
                            hideProgress();
                            Toast.makeText(DiscountCodeActivity.this, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                        } else {
//                            Log.d(TAG, "Resp " + json.toString());
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
                                                        showIOSDialogOK("Success", apiResponse.getMessage(), "ok", new iOSDialogClickListener() {
                                                            @Override
                                                            public void onClick(iOSDialog dialog) {
                                                                //Toast.makeText(PacifyerActivity.this,"Clicked!",Toast.LENGTH_LONG).show();
                                                                dialog.dismiss();
                                                                Intent i = new Intent(DiscountCodeActivity.this, WelcomeTourActivity.class);
                                                                startActivity(i);
                                                                finishAffinity();
                                                            }
                                                        });

                                                    }
                                                }
                                            } else {
                                                Toast.makeText(DiscountCodeActivity.this, "Something went wrong. Please try again later", Toast.LENGTH_LONG).show();
                                            }


                                        } catch (Exception e) {
                                            Toast.makeText(DiscountCodeActivity.this, "Something went wrong. Please try again later", Toast.LENGTH_LONG).show();
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            } else if (apiResponse.getMessage() != null) {
                                hideProgress();
                                Toast.makeText(DiscountCodeActivity.this, apiResponse.getMessage(), Toast.LENGTH_LONG).show();

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
    }

}
