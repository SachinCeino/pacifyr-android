package com.pacifyr.omhealthcare.sign;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.base.PacifyerActivity;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by ceino on 11/7/17.
 */

public class LogInCodeVerifyActivity extends PacifyerActivity {

    private ImageView imgBack;
    private RelativeLayout logoRlay;
    private ImageView logoBlue;
    private LinearLayout hintLlay;
    private LinearLayout createLlay;
    private RelativeLayout createaccountRlay;
    private CustomFontTextView cAccountCtxv,resend_ctxv,otp_phone_tv;
    public String phoneText;
    public static String phonenumber;
    public static String phoneVerifyTXT;
    //private CustomEditText otpEditText;


    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    // [START declare_auth]
    private FirebaseAuth mAuth;
    public Bundle bundle;

    Pinview pin;
    // [END declare_auth]

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            fbLogout();
            setContentView(R.layout.activity_sign_in_code_verify);
            onSetActionBar();
            mAuth = FirebaseAuth.getInstance();
            /*hideKeyboard();*/

            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

            imgBack = (ImageView) findViewById(R.id.img_back);
            logoRlay = (RelativeLayout) findViewById(R.id.logo_rlay);
            logoBlue = (ImageView) findViewById(R.id.logo_blue);

            pin= (Pinview) findViewById(R.id.pinview);
            pin.setHint("_");


            hideKeyboard();

            //otpEditText = (CustomEditText) findViewById(R.id.otp_edtit_text);
            hintLlay = (LinearLayout) findViewById(R.id.hint_llay);
            createLlay = (LinearLayout) findViewById(R.id.create_llay);
            createaccountRlay = (RelativeLayout) findViewById(R.id.createaccount_rlay);
            cAccountCtxv = (CustomFontTextView) findViewById(R.id.c_account_ctxv);
            resend_ctxv = (CustomFontTextView) findViewById(R.id.resend_ctxv);
            otp_phone_tv=(CustomFontTextView) findViewById(R.id.otp_phone_tv);
            phoneText=otp_phone_tv.getText().toString();


            bundle = getIntent().getExtras();


            if (isValid(phonenumber)) {
                setClicks();

                otp_phone_tv.setText(phoneText+" "+phoneVerifyTXT);

            }
            else{
                Toast.makeText(this,"invalid no: "+phonenumber,Toast.LENGTH_LONG).show();
            }
            resend_ctxv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(LogInCodeVerifyActivity.this,"SMS Sent",Toast.LENGTH_LONG).show();
                    startPhoneNumberVerification(phonenumber);
                    showProgress();

                }
            });

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    

    @Override
    protected View getSnackbarAnchorView() {
        return null;
    }

    private void setClicks() {
        try{
            // Initialize phone auth callbacks
            // [START phone_auth_callbacks]
            showProgress();
            hideKeyboard();
            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                @Override
                public void onVerificationCompleted(final PhoneAuthCredential credential) {
                    // This callback will be invoked in two situations:
                    // 1 - Instant verification. In some cases the phone number can be instantly
                    //     verified without needing to send or enter a verification code.
                    // 2 - Auto-retrieval. On some devices Google Play services can automatically
                    //     detect the incoming verification SMS and perform verificaiton without
                    //     user action.
                    Log.d("jithish", "onVerificationCompleted:" + credential);
                    // [START_EXCLUDE silent]
                    mVerificationInProgress = false;
                    showProgress();
                    // [END_EXCLUDE]

                    // [START_EXCLUDE silent]
                    // Update the UI and attempt sign in with the phone credential
                   // updateUI(STATE_VERIFY_SUCCESS, credential);
                    // [END_EXCLUDE]
                    //otpEditText.setText(credential.getSmsCode());
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(isValid(credential.getSmsCode())){
                                pin.setValue(credential.getSmsCode());
                            }else{
                                Random rnd = new Random();
                                int n = 100000 + rnd.nextInt(900000);
                                pin.setValue(Integer.toString(n));
                            }
                            signInWithPhoneAuthCredential(credential);
                            hideProgress();
                        }
                    }, 5000);


                }

                @Override
                public void onVerificationFailed(FirebaseException e) {
                    // This callback is invoked in an invalid request for verification is made,
                    // for instance if the the phone number format is not valid.
                    Log.d("jithish", "onVerificationFailed", e);
                    // [START_EXCLUDE silent]
                    mVerificationInProgress = false;
                    // [END_EXCLUDE]

                    if (e instanceof FirebaseAuthInvalidCredentialsException) {
                        // Invalid request
                        // [START_EXCLUDE]
                        //otpEditText.setError("Invalid phone number format. Check the phone number you entered and the country code.");
                        Toast.makeText(LogInCodeVerifyActivity.this,"Invalid phone number format. Check the phone number you entered and the country code.",Toast.LENGTH_LONG).show();
                        // [END_EXCLUDE]
                    } else if (e instanceof FirebaseTooManyRequestsException) {
                        // The SMS quota for the project has been exceeded
                        // [START_EXCLUDE]
                        Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                                Snackbar.LENGTH_SHORT).show();
                        // [END_EXCLUDE]
                    }

                    // Show a message and update the UI
                    // [START_EXCLUDE]
                  //  updateUI(STATE_VERIFY_FAILED);
                    // [END_EXCLUDE]
                }

                @Override
                public void onCodeSent(String verificationId,
                                       PhoneAuthProvider.ForceResendingToken token) {
                    // The SMS verification code has been sent to the provided phone number, we
                    // now need to ask the user to enter the code and then construct a credential
                    // by combining the code with a verification ID.
                    Log.d("jithish",  "onCodeSent:" + verificationId);

                    // Save verification ID and resending token so we can use them later
                    mVerificationId = verificationId;
                    mResendToken = token;

                    // [START_EXCLUDE]
                    // Update UI
                 //   updateUI(STATE_CODE_SENT);
                    // [END_EXCLUDE]
                }
            };
            // [END phone_auth_callbacks]


            createaccountRlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hideKeyboard();
                    setLoading();
                    showProgress();
                    if(!isValid(mVerificationId,"Incorrect Confirmation Code")){
                        hideProgress();
                        return;
                    }

                    if(!isValid(pin.getValue(),"Invalid code "))
                    {
                        hideProgress();
                        return;
                    }

                    verifyPhoneNumberWithCode(mVerificationId, pin.getValue()+"");
                  /*  if (isValid(phonenumber))
                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                phonenumber,        // Phone number to verify
                                60,                 // Timeout duration
                                TimeUnit.SECONDS,   // Unit of timeout
                                this,               // Activity ( if (isValid(phonenumber))
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        phonenumber,        // Phone number to verify
                        60,                 // Timeout duration
                        TimeUnit.SECONDS,   // Unit of timeout
                        this,               // Activity (for callback binding)
                        mCallbacks);for callback binding)
                                mCallbacks);*/
                }
            });

            startPhoneNumberVerification(phonenumber);


        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void startPhoneNumberVerification(String phoneNumber) {
        try {
            // [START start_phone_auth]
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phoneNumber,        // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    this,               // Activity (for callback binding)
                    mCallbacks);        // OnVerificationStateChangedCallbacks
            // [END start_phone_auth]

            mVerificationInProgress = true;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    // [START resend_verification]
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }


    // [START sign_in_with_phone]
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("jithish", "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();

                            if(bundle.getString("from").equals("signup")) {
                                Intent intent = new Intent(LogInCodeVerifyActivity.this, RegisterActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
                                Intent intent = new Intent(LogInCodeVerifyActivity.this, LoginPhoneActivity.class);
                                intent.putExtra("phone",phonenumber);
                                startActivity(intent);
                            }
                            // [START_EXCLUDE]
                         //   updateUI(STATE_SIGNIN_SUCCESS, user);
                            // [END_EXCLUDE]
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("jithish", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                // [START_EXCLUDE silent]
                                //otpEditText.setError("Invalid code.");
                                Toast.makeText(LogInCodeVerifyActivity.this,"Invalid code.",Toast.LENGTH_LONG).show();
                                // [END_EXCLUDE]
                            }
                            // [START_EXCLUDE silent]
                            // Update UI
                           // updateUI(STATE_SIGNIN_FAILED);
                            // [END_EXCLUDE]
                        }
                    }
                });
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }
    // [END sign_in_with_phone]
    private void enableViews(View... views) {
        for (View v : views) {
            v.setEnabled(true);
        }
    }

    private void disableViews(View... views) {
        for (View v : views) {
            v.setEnabled(false);
        }
    }
    private void signOut() {
        mAuth.signOut();
       // updateUI(STATE_INITIALIZED);
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
        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/ITCAvantGardeStd-Bold.otf");
        titleTxv.setTypeface(type);
        leftLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
