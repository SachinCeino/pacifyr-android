package com.pacifyr.omhealthcare.sign;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ceino.utilitylibrary.CustomEditText;
import com.ceino.utilitylibrary.CustomFontTextView;
import com.ceino.utilitylibrary.base.BaseAppCompactActivity;
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

import java.util.concurrent.TimeUnit;

/**
 * Created by ceino on 11/7/17.
 */

public class CodeVerifyActivity extends BaseAppCompactActivity {

    private ImageView imgBack;
    private RelativeLayout logoRlay;
    private ImageView logoBlue;
    private LinearLayout hintLlay;
    private LinearLayout createLlay;
    private RelativeLayout createaccountRlay;
    private CustomFontTextView cAccountCtxv,resend_ctxv,otp_phone_tv;
    public String phoneText;
    public static String phonenumber;
    private CustomEditText otp_cedt;


    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    // [START declare_auth]
    private FirebaseAuth mAuth;
    public Bundle bundle;
    // [END declare_auth]

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_code_verify);

            imgBack = (ImageView) findViewById(R.id.img_back);
            logoRlay = (RelativeLayout) findViewById(R.id.logo_rlay);
            logoBlue = (ImageView) findViewById(R.id.logo_blue);
            otp_cedt = (CustomEditText) findViewById(R.id.otp_cedt);
            hintLlay = (LinearLayout) findViewById(R.id.hint_llay);
            createLlay = (LinearLayout) findViewById(R.id.create_llay);
            createaccountRlay = (RelativeLayout) findViewById(R.id.createaccount_rlay);
            cAccountCtxv = (CustomFontTextView) findViewById(R.id.c_account_ctxv);
            resend_ctxv = (CustomFontTextView) findViewById(R.id.resend_ctxv);
            otp_phone_tv=(CustomFontTextView) findViewById(R.id.otp_phone_tv);
            phoneText=otp_phone_tv.getText().toString();
            setClicks();

            bundle = getIntent().getExtras();




        }catch (Exception e)
        {

        }
    }

    private void setClicks() {
        try{
            mAuth = FirebaseAuth.getInstance();
            // Initialize phone auth callbacks
            // [START phone_auth_callbacks]
            showProgress();
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
                    otp_cedt.setText(credential.getSmsCode());

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            hideProgress();
                            signInWithPhoneAuthCredential(credential);
                        }
                    }, 1500);


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
                        otp_cedt.setError("Invalid phone number format. Check the phone number you entered and the country code.");
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

                    if(!isValid(mVerificationId,"Incorrect Confirmation Code"))
                        return;
                    if(!isValid(otp_cedt,"Invalid code "))
                        return;
                    verifyPhoneNumberWithCode(mVerificationId, otp_cedt.getText()+"");
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

            if (isValid(phonenumber)) {
                startPhoneNumberVerification(phonenumber);
                otp_phone_tv.setText(phoneText+phonenumber);
            }


        }catch (Exception e)
        {

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
        // [END start_phone_auth]

        mVerificationInProgress = true;
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
                                Intent intent = new Intent(CodeVerifyActivity.this, RegisterActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
                                Intent intent = new Intent(CodeVerifyActivity.this, LoginPhoneActivity.class);
                                intent.putExtra("phone",phonenumber);
                                startActivity(intent);
                                finish();
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
                                otp_cedt.setError("Invalid code.");
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


}
