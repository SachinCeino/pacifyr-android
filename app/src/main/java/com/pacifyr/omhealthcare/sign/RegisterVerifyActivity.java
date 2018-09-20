package com.pacifyr.omhealthcare.sign;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ceino.utilitylibrary.CustomFontTextView;
import com.ceino.utilitylibrary.DeviceFitImageView;
import com.goodiebag.pinview.Pinview;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.base.PacifyerActivity;
import com.pacifyr.omhealthcare.event.NotifyEvent;
import com.squareup.otto.Subscribe;

import org.json.JSONObject;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.ceino.utilitylibrary.network.UtilityNetworkService.GLOBAL_URL;

public class RegisterVerifyActivity extends PacifyerActivity {

    public static String TAG = RegisterVerifyActivity.class.getSimpleName();
    private ImageView imgBack;
    private RelativeLayout logoRlay;
    private ImageView logoBlue;
    private LinearLayout hintLlay;
    private CustomFontTextView otpsendText;
    private LinearLayout createAcccountLlay;
    //private CustomEditText otpRecieveEditText;
    private RelativeLayout createAccountRlay;
    private CustomFontTextView createAccountCtxv;
    private CustomFontTextView resendCtxv;

    public String phoneText;

    public static String phoneRegister;

    public static String phoneRegisterSub;

    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    // [START declare_auth]
    private FirebaseAuth mAuth;
    public Bundle bundle;

    Pinview pinview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_register_verify);
            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            showProgress();
            /*hideKeyboard();*/

            onSetActionBar();
            initialiseViews();
            mAuth = FirebaseAuth.getInstance();

            otpsendText.setText(phoneText+" "+phoneRegisterSub);

            createAccountRlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(!isValid(mVerificationId,"Incorrect Confirmation Code"))
                        return;
                    if(!isValid(pinview.getValue(),"Invalid code "))
                        return;
                    verifyPhoneNumberWithCode(mVerificationId, pinview.getValue()+"");

                }
            });

            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                @Override
                public void onVerificationCompleted(final PhoneAuthCredential credential) {
                    try {
                        hideKeyboard();
                        showProgress();
                        mVerificationInProgress = false;
                        //otpRecieveEditText.setText(credential.getSmsCode());
                        String url = GLOBAL_URL + "login/phonenumber";

                        AjaxCallback cb = new AjaxCallback<JSONObject>() {
                            @Override
                            public void callback(String url, final JSONObject json, final AjaxStatus status) {
                                try {
                                    String stat= json.getString("status");
                                    //String messag= json.getString("message");
                                    String logstat = status.getMessage();
                                    if (isValid(stat)) {
                                        if (stat.equals("success")) {

                                            //Toast.makeText(RegisterVerifyActivity.this, "Mobile number already registered", Toast.LENGTH_LONG).show();
                                            setAlertOKStatusInWhite(getResources().getString(R.string.registration_failed), getResources().getString(R.string.failed_register_message),"Ok");



                                        }else if (logstat.equals("error")) {

                                            Toast.makeText(RegisterVerifyActivity.this, "Mobile number not registered", Toast.LENGTH_LONG).show();

                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {

                                                    if (isValid(credential.getSmsCode())) {
                                                        pinview.setValue(credential.getSmsCode());
                                                    } else {
                                                        Random rnd = new Random();
                                                        int n = 100000 + rnd.nextInt(900000);
                                                        pinview.setValue(Integer.toString(n));
                                                    }
                                                    signInWithPhoneAuthCredential(credential);
                                                    hideProgress();

                                                }
                                            }, 900);
                                        }
                                    }
                                } catch (Exception e) {
                                    hideProgress();
                                    e.printStackTrace();
                                }
                            }
                        };
                        AQuery aq = new AQuery(RegisterVerifyActivity.this);
                        cb.header("Content-Type", "application/json");
                        JSONObject postDataParams = new JSONObject();
                        postDataParams.putOpt("phoneNumber",  phoneRegister);
                        aq.post(url, postDataParams, JSONObject.class, cb);
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }


                   /* final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                        }
                    }, 5000);*/
                }

                @Override
                public void onVerificationFailed(FirebaseException e) {
                    // This callback is invoked in an invalid request for verification is made,
                    // for instance if the the phone number format is not valid.
                    Log.d(TAG, "Firebase exception " + e.getMessage());
                    e.printStackTrace();
                    Log.d("jithish", "onVerificationFailed", e);
                    mVerificationInProgress = false;

                    if (e instanceof FirebaseAuthInvalidCredentialsException) {
                        // Invalid request
                        //otpRecieveEditText.setError("Invalid phone number format. Check the phone number you entered and the country code.");
                        Toast.makeText(RegisterVerifyActivity.this,"Invalid phone number format. Check the phone number you entered and the country code.",Toast.LENGTH_LONG).show();
                    } else if (e instanceof FirebaseTooManyRequestsException) {
                        // The SMS quota for the project has been exceeded
                        Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                                Snackbar.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCodeSent(String verificationId,
                                       PhoneAuthProvider.ForceResendingToken token) {
                    mVerificationId = verificationId;
                    mResendToken = token;
                }
            };


            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phoneRegister,        // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    this,               // Activity (for callback binding)
                    mCallbacks);        // OnVerificationStateChangedCallbacks
            mVerificationInProgress = true;

           /* if (isValid(phoneRegister)) {
                startPhoneNumberVerification(phoneRegister);

            }*/
      }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    protected View getSnackbarAnchorView() {
        return null;
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("LOG:::", "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();

                           /* if(bundle.getString("from").equals("signup")) {*/
                                Intent intent = new Intent(RegisterVerifyActivity.this, RegisterActivity.class);
                                startActivity(intent);
                            /*}
                            else
                            {
                                Intent intent = new Intent(RegisterVerifyActivity.this, LoginPhoneActivity.class);
                                intent.putExtra("phone",phoneRegister);
                                startActivity(intent);
                                finish();
                            }*/
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("LOG", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                               //otpRecieveEditText.setError("Invalid code.");
                                //Toast.makeText(RegisterVerifyActivity.this,"Invalid code ",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
        });
    }

    private void initialiseViews() {
        try {
            pinview = (Pinview) findViewById(R.id.pinview);
            pinview.setInputType(Pinview.InputType.NUMBER);
            imgBack = (ImageView) findViewById(R.id.img_back);
            logoRlay = (RelativeLayout) findViewById(R.id.logo_rlay);
            logoBlue = (ImageView) findViewById(R.id.logo_blue);
            hintLlay = (LinearLayout) findViewById(R.id.hint_llay);
            otpsendText = (CustomFontTextView) findViewById(R.id.otpsend_phone_tv);
            createAcccountLlay = (LinearLayout) findViewById(R.id.create_acccount_llay);
            //otpRecieveEditText = (CustomEditText) findViewById(R.id.otp_recieve_edittext);
            createAccountRlay = (RelativeLayout) findViewById(R.id.create_account_rlay);
            createAccountCtxv = (CustomFontTextView) findViewById(R.id.create_account_ctxv);
            resendCtxv = (CustomFontTextView) findViewById(R.id.resend_ctxv);
            phoneText=otpsendText.getText().toString();
            /*pinview.setPinViewEventListener(new Pinview.PinViewEventListener() {
                @Override
                public void onDataEntered(Pinview pinview, boolean fromUser) {
                    //Make api calls here or what not
                    //verifyPhoneNumberWithCode(mVerificationId, pinview.getValue()+"");
                    //Toast.makeText(RegisterVerifyActivity.this, pinview.getValue(), Toast.LENGTH_SHORT).show();
                }
            });*/
            resendCtxv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(RegisterVerifyActivity.this,"SMS Send",Toast.LENGTH_LONG).show();
                    startPhoneNumberVerification(phoneRegister);
                    showProgress();

                }
            });
        }
        catch (Exception ee)
        {
            ee.printStackTrace();
        }
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks

        mVerificationInProgress = true;
    }


    @Subscribe
    public void onNotifyEvent(NotifyEvent event) {

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
        rightTxv.setVisibility(View.INVISIBLE);
        rightLlay.setVisibility(View.INVISIBLE);
        leftTxv.setVisibility(View.INVISIBLE);
        backImv.setVisibility(View.VISIBLE);
        titleTxv.setVisibility(View.VISIBLE);
        leftLlay.setVisibility(View.VISIBLE);
        defSetText(titleTxv,getResources().getString(R.string.sign_up));
        Typeface type = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.avantica_bold_header));
        titleTxv.setTypeface(type);
        leftLlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
    }


    public void setAlertStatusInWhite(String title,String desc,String ok,String dismiss,View.OnClickListener oklistner) {
        View view = LayoutInflater.from(this).inflate(R.layout.alert_white_yes_no, null, false);
        CustomFontTextView title_dtxv = (CustomFontTextView) view.findViewById(com.ceino.utilitylibrary.R.id.title_dtxv);
        CustomFontTextView desc_dtxv = (CustomFontTextView) view.findViewById(com.ceino.utilitylibrary.R.id.desc_dtxv);
        CustomFontTextView dismis_dtxv = (CustomFontTextView) view.findViewById(com.ceino.utilitylibrary.R.id.dismis_dtxv);
        CustomFontTextView ok_dtxv = (CustomFontTextView) view.findViewById(com.ceino.utilitylibrary.R.id.ok_dtxv);
        if(isValid(title))
            defSetText(title_dtxv,title);
        else
            title_dtxv.setVisibility(View.GONE);
        defSetText(desc_dtxv,desc);

        defSetText(dismis_dtxv,dismiss,"NO");
        defSetText(ok_dtxv,ok,"Yes");

        hideStatus();
        hideStatus2();
        hud_status2 = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCustomView(view).setWindowColor(this.getResources().getColor(com.ceino.utilitylibrary.R.color.transparent)).setCancellable(false).setAnimationSpeed(2).setDimAmount(0.5f);
        showStatus2();
        ok_dtxv.setOnClickListener(oklistner);
        dismis_dtxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideStatus2();
                finish();
                Intent intent=new Intent(RegisterVerifyActivity.this,SignUpActivity.class);
                startActivity(intent);


            }
        });
    }

    public void setAlertOKStatusInWhite(String title,String desc,String ok) {
        //View.OnClickListener oklistner
        View view = LayoutInflater.from(this).inflate(R.layout.alert_white_ok, null, false);
        CustomFontTextView title_dtxv = (CustomFontTextView) view.findViewById(com.ceino.utilitylibrary.R.id.title_dtxv);
        CustomFontTextView desc_dtxv = (CustomFontTextView) view.findViewById(com.ceino.utilitylibrary.R.id.desc_dtxv);
        CustomFontTextView ok_dtxv = (CustomFontTextView) view.findViewById(com.ceino.utilitylibrary.R.id.ok_dtxv);

        if(isValid(title))
            defSetText(title_dtxv,title);
        else
            title_dtxv.setVisibility(View.GONE);
        defSetText(desc_dtxv,desc);
        ok_dtxv.setText(ok);
        hideStatus();
        hud_status = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCustomView(view).setWindowColor(this.getResources().getColor(com.ceino.utilitylibrary.R.color.transparent)).setCancellable(false).setAnimationSpeed(2).setDimAmount(0.5f);
        showStatus();

        ok_dtxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                hideStatus();
            }
        });
    }
}
