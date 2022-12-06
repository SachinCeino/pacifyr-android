package com.pacifyr.client.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.google.gson.Gson;
import com.pacifyr.client.R;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.home.DashboardActivity;
import com.pacifyr.client.model.ApiResponse;
import com.pacifyr.client.network.GsonUtils;
import com.pacifyr.client.network.NetworkService;
import com.pacifyr.client.orginals.Auth;
import com.pacifyr.client.utility.BaseFragment;
import com.pacifyr.client.utils.FirebaseLogging;
import com.pacifyr.client.utils.Utility;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;


public class CouponCodeFragment extends BaseFragment {


    private RelativeLayout button_validate;
    private EditText edit_discount;
    private String code="";
    public CouponCodeFragment() {
        // Required empty public constructor
    }

    public static CouponCodeFragment setCoupen(String code){
        CouponCodeFragment c=new CouponCodeFragment();
        c.code=code;
        return c;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userDetailWeb(getUserID(), new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {
                super.callback(url, object, status);
                Auth.httpStatusFilter(getContext(), status.getCode());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DashboardActivity) getActivity()).hideConnectButton();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_coupon_code, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edit_discount = view.findViewById(R.id.edit_discount_code);
        if(!code.equals(""))edit_discount.setText(code);
        button_validate = view.findViewById(R.id.button_validate);
        button_validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.preventDoubleClickButtons(v);
                if (edit_discount.getText().length() == 0) {
                    edit_discount.setError(getString(R.string.please_enter_a_valid_coupon_code));
                    edit_discount.requestFocus();
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
                        Auth.httpStatusFilter(getActivity(),status.getCode());
                        if (json == null) {
                            hideProgress();
                            Toast.makeText(getActivity(), getString(R.string.invalid_coupon_code), Toast.LENGTH_SHORT).show();
                            Bundle args =
                                    FirebaseLogging.getConstantArgsLoggedIn(getActivity());

                            args.putString(FirebaseLogging.EC, code + " : failed");

                            FirebaseLogging.sendLog(getActivity(),
                                    FirebaseLogging.EVENT_APPLY_COUPON_CODE,
                                    args);
                        } else {
//                            Log.d(TAG, "Resp " + json.toString());
                            final ApiResponse apiResponse = new Gson().fromJson(json.toString(), ApiResponse.class);
                            if (apiResponse.isSuccess()) {
                                PrefManager.getInstance(getContext()).putSharedString(PrefManager.APPLIED_COUPON_CODE, code);
                                PrefManager.getInstance(getContext()).putSharedString(PrefManager.COUPON_CODE, code);
                                ((DashboardActivity)getActivity()).getUserProfile(getUserID(), new AjaxCallback<JSONObject>() {
                                    @Override
                                    public void callback(String url, JSONObject json, AjaxStatus status) {
                                        hideProgress();
                                        try {
                                            if (json != null) {
                                                NetworkService.mCurrentUser = GsonUtils.getInstance().gsonToModelUserProfile(json);
                                                PrefManager prefManager = PrefManager.getInstance(getActivity());
                                                if (NetworkService.mCurrentUser != null) {
                                                    if (NetworkService.mCurrentUser.getRoleModel().getLabel().equals("employee")) {
                                                        prefManager.saveCompanyID(NetworkService.mCurrentUser.getCompanyId());
                                                        if (NetworkService.mCurrentUser != null && NetworkService.mCurrentUser.getCompany() != null){
                                                            prefManager.saveCompanyLogo(NetworkService.mCurrentUser.getCompany().getLogo());
                                                            prefManager.setEnablePrescription(NetworkService.mCurrentUser.getCompany().getEnablePrescription());
                                                        }
                                                        prefManager.saveCustomerRole("employee");
                                                        /*showIOSDialogOK("Success", apiResponse.getMessage(), "ok", new iOSDialogClickListener() {
                                                            @Override
                                                            public void onClick(iOSDialog dialog) {
                                                                dialog.dismiss();
                                                            }
                                                        });*/

                                                    }
                                                    else {
                                                        prefManager.saveCompanyID(null);
                                                        prefManager.saveCompanyLogo(null);
                                                        prefManager.setEnablePrescription(true);
                                                        prefManager.saveCustomerRole(NetworkService.mCurrentUser.getRoleModel().getLabel());
                                                    }
                                                        showIOSDialogOK("Success", apiResponse.getMessage(), "ok", new iOSDialogClickListener() {
                                                            @Override
                                                            public void onClick(iOSDialog dialog) {
                                                              try {
                                                                  dialog.dismiss();
                                                                  ((DashboardActivity)getActivity()).showFragment(new UserDashboardFragmentNew());
                                                              }catch (Exception e)
                                                              {

                                                              }
                                                            }
                                                        });


                                                }
                                            } else {
                                                Toast.makeText(getActivity(), getResources().getString(R.string.invalid_coupon_code), Toast.LENGTH_LONG).show();
                                            }


                                        } catch (Exception e) {
                                            Toast.makeText(getActivity(),  getResources().getString(R.string.invalid_coupon_code), Toast.LENGTH_LONG).show();
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                Bundle args =
                                        FirebaseLogging.getConstantArgsLoggedIn(getActivity());

                                args.putString(FirebaseLogging.EC, "Success : " + code);

                                FirebaseLogging.sendLog(getActivity(),
                                        FirebaseLogging.EVENT_APPLY_COUPON_CODE,
                                        args);
                            } else if (apiResponse.getMessage() != null) {
                                hideProgress();
                                Toast.makeText(getActivity(), apiResponse.getMessage(), Toast.LENGTH_LONG).show();

                                Bundle args =
                                        FirebaseLogging.getConstantArgsLoggedIn(getActivity());

                                args.putString(FirebaseLogging.EC, code + apiResponse.getMessage());

                                FirebaseLogging.sendLog(getActivity(),
                                        FirebaseLogging.EVENT_APPLY_COUPON_CODE,
                                        args);
                            }
                        }


                    }
                });

            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        ((DashboardActivity) getActivity()).changeActionBarTitleForFragments(getResources().getString(R.string.nav_support_coupon_code));
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Subscribe
    public void onNotifyEvent(NotifyEvent event) {

    }

    @Override
    public void onSetValues(View view) throws Exception {

    }

}
