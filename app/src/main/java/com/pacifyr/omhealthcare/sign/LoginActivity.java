package com.pacifyr.omhealthcare.sign;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ceino.utilitylibrary.CustomEditText;
import com.ceino.utilitylibrary.CustomFontTextView;
import com.ceino.utilitylibrary.DeviceFitImageView;
import com.ceino.utilitylibrary.model.pacifyr.LoginModel;
import com.ceino.utilitylibrary.model.pacifyr.MRoleModel;
import com.ceino.utilitylibrary.model.pacifyr.MUser;
import com.ceino.utilitylibrary.model.pacifyr.ModelFacebook;
import com.ceino.utilitylibrary.model.pacifyr.post.PostRegisterModel;
import com.ceino.utilitylibrary.utils.PrefManager;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.adapter.CountrySpinnerAdapter;
import com.pacifyr.omhealthcare.base.PacifyerActivity;
import com.pacifyr.omhealthcare.home.DashboardActivity;
import com.pacifyr.omhealthcare.model.ItemData;
import com.pacifyr.omhealthcare.network.GsonUtils;
import com.pacifyr.omhealthcare.network.NetworkService;
import com.pacifyr.omhealthcare.quickblox.chat.utils.qb.callback.QbEntityCallbackImpl;
import com.pacifyr.omhealthcare.utils.FirebaseLogging;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.QBSession;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static com.ceino.utilitylibrary.network.UtilityNetworkService.ACCOUNT_KEY;
import static com.ceino.utilitylibrary.network.UtilityNetworkService.APP_ID;
import static com.ceino.utilitylibrary.network.UtilityNetworkService.AUTH_KEY;
import static com.ceino.utilitylibrary.network.UtilityNetworkService.AUTH_SECRET;
import static com.ceino.utilitylibrary.network.UtilityNetworkService.GLOBAL_URL;
import static com.pacifyr.omhealthcare.network.NetworkService.countryData;

public class LoginActivity extends PacifyerActivity {

    private EditText edt_password,edt_email;

    private RelativeLayout facebookRlay;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    public static CallbackManager callbackmanager2;
    public PostRegisterModel postRegisterModel;
    public  String FEmail,FName;
    public String label="error",main_label="error";
    private Spinner spinner;
    private MUser userModel;
    private LoginModel loginModel;
    private PrefManager prefM;
    private String companyId;
    private ModelFacebook facebookMoel;
    public static String alreadyRegisteresPhone;
    private RelativeLayout loginRlay;
    private CustomEditText phoneCedit;
    private AccessTokenTracker accessTokenTracker;
    private LoginManager loginManager;
    private String jsonStrng;
    private String qbID;
    private Context context;
    private CustomFontTextView sms_text_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);
        try {
            fbLogout();
            setLoading();
            hideProgress();
            FacebookSdk.sdkInitialize(LoginActivity.this);
            onSetActionBar();
            context = LoginActivity.this;
            prefM = PrefManager.getInstance(context);
            sms_text_login = findViewById(R.id.sms_text_login);

            spinner = findViewById(R.id.spinner);
            phoneCedit = findViewById(R.id.phone_cedit);
            facebookRlay = findViewById(R.id.facebook_rlay);
            facebookRlay.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    fbLogout();
                    showProgress();
                    loginButton.performClick();
                }
            });

            loginButton = findViewById(R.id.login_button);
            loginButton.setReadPermissions("user_friends", "email");
            callbackManager = CallbackManager.Factory.create();
            loginButton = findViewById(R.id.login_button);

            if (isValid(alreadyRegisteresPhone)) {
                phoneCedit.setText(alreadyRegisteresPhone.substring(2));
            }
            ArrayList<ItemData> list = new ArrayList<>();
            list.add(new ItemData("+1", "US", R.drawable.flag_usa));
            list.add(new ItemData("+1", "Canada", R.drawable.canada2));
            list.add(new ItemData("+91", "India", R.drawable.ic_india));
            list.add(new ItemData("+971","UAE", R.drawable.flag_uae2));
            /*list.add(new ItemData("+971","Kuwait", R.drawable.kuwait2));*/

            CountrySpinnerAdapter adapter = new CountrySpinnerAdapter(this,
                    R.layout.spinner_item, R.id.txt, list);
            spinner.setAdapter(adapter);

            String countryCode = prefM.getCountryCode();

            if(countryCode.equals("IN")){
                spinner.setSelection(2);
            }else if (countryCode.equals("US")){
                spinner.setSelection(0);
            }else if (countryCode.equals("CA")){
                spinner.setSelection(1);
            }
            else if (countryCode.equals("AE")){
                spinner.setSelection(3);
            }

            loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));

            // Callback registration
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    GraphRequest request = GraphRequest.newMeRequest(
                            loginResult.getAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(
                                        JSONObject object,
                                        GraphResponse response) {
                                    Log.v("LoginActivity Response ", response.toString());

                                    try {
                                        facebookMoel = GsonUtils.getInstance().gsonToModelFacebook(object);
                                        FName = object.getString("name");
                                        if(isValid(facebookMoel.getEmail())) {
                                            Log.v("Email = ", " " + FEmail);
                                            FEmail = object.getString("email");
                                            postRegisterModel = new PostRegisterModel();
                                            postRegisterModel.setEmail(FEmail);
                                            postRegisterModel.setLoginType("facebook");
                                            callLogin(-1);
                                        }else{
                                            //showIOSDialogOK("Privacy Issue!","Please enable email id accessible on your facebook Privacy Settings.","OK");
                                            setAlertOKStatusInBlue("Access Error","Please enable email id accessible on your facebook Privacy Settings.","OK");
                                            fbLogout();
                                            return;
                                        }
                                    } catch (JSONException e) {
                                        hideProgress();
                                        fbLogout();
                                        e.printStackTrace();
                                        Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_LONG);

                                    }
                                }
                            });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,name,email,gender, birthday");
                    request.setParameters(parameters);
                    request.executeAsync();
                }

                @Override
                public void onCancel() {
                    Toast.makeText(LoginActivity.this, "on cancel", Toast.LENGTH_LONG).show();
                    fbLogout();
                    // AppChatNotUSe code
                }

                @Override
                public void onError(FacebookException exception) {
                    // AppChatNotUSe code
                    fbLogout();
                    Toast.makeText(LoginActivity.this, exception.toString(), Toast.LENGTH_LONG).show();
                }
            });

            loginRlay = findViewById(R.id.login_rlay);
            loginRlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //  validRegister();
                    phoneVerifyWeb();

                }
            });

            sms_text_login.setText(getResources().getString(R.string.app_name) + " " + getResources().getString(R.string.sms_text_login));


        }
        catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_LONG);

        }

    }


    @Override
    protected View getSnackbarAnchorView() {
        return findViewById(R.id.percent_rlay);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    private void phoneVerifyWeb() {
        try {
            setLoading();
            showProgress();
            if (!isValidMobile(phoneCedit, "Please enter your mobile number"))
            {
                hideProgress();
                return;
            }
            hideProgress();

            countryData = (ItemData) spinner.getSelectedItem();
            String code = countryData.getCode();
            LogInCodeVerifyActivity. phonenumber =  code + phoneCedit.getText()+"";
            LogInCodeVerifyActivity.phoneVerifyTXT = code + phoneCedit.getText();

            prefM.saveCountry(countryData.getCountry());
            if(NetworkService.isRelease) {
                Intent intent = new Intent(this, LogInCodeVerifyActivity.class);
                intent.putExtra("from", "login");
                startActivity(intent);
                //OnVerificationStateChangedCallbacks
            }
            else {
                Intent intent = new Intent(this, LoginPhoneActivity.class);
                intent.putExtra("phone", LogInCodeVerifyActivity. phonenumber);
                startActivity(intent);
                finish();
            }
        }catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_LONG);
            //  LogUtils.LOGD("sign","lA onCreate exception "+e.getMessage());
        }
    }

    public void backpress(View view) {
        finish();
    }

    public void callLogin(int checkVal)
    {
        showProgress();
        try {
            String url = GLOBAL_URL + "login";
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, final JSONObject json, final AjaxStatus status) {
                    try {
                        jsonStrng = json.toString();
                        loginModel = GsonUtils.getInstance().gsonToLoginModel(json);


                        String logstat = loginModel.getStatus();
                        if (isValid(logstat)) {
                            if (logstat.equals("success")) {
                                userModel = loginModel.getUser();
                                companyId = userModel.getCompanyId();
                                prefM.saveCompanyID(companyId);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            hideStatus();
                                            signInCreatedUser();

                                        } catch (Exception exc) {
                                            exc.printStackTrace();
                                            Toast.makeText(LoginActivity.this,exc.getMessage(),Toast.LENGTH_LONG);
                                            main_label="error";
                                            label="error";
                                            hideProgress();
                                        }
                                    }
                                }, 3000);
                            } else {
                                hideProgress();
//                                Toast.makeText(LoginActivity.this,loginModel.getMessage(),Toast.LENGTH_LONG);
                                prefM.clear();
                                LoginManager mLoginManager = LoginManager.getInstance();
                                mLoginManager.logOut();
                                setAlertOKStatusInRed("Error", loginModel.getMessage(), "OK", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        hideStatus();
                                        finish();
                                    }
                                });
                               /* setStatus(loginModel.getMessage(), R.color.md_red_400);
                                showStatus();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        hideStatus();
                                        finish();
                                        *//*hideKeyboard();*//*
                                    }
                                }, 3000);*/
                            }
                        }
                    } catch (Exception e) {
                        hideProgress();
                        Toast.makeText(LoginActivity.this,/*loginModel.getMessage()+"/n"+*/e.getMessage(), Toast.LENGTH_LONG).show();
                        LoginManager mLoginManager = LoginManager.getInstance();
                        mLoginManager.logOut();
                        main_label="error";
                        label="error";
                        e.printStackTrace();
                    }
                }
            };
            AQuery aq = new AQuery(LoginActivity.this);
            cb.header("Content-Type", "application/json");
            JSONObject postDataParams = new JSONObject();
            if(checkVal==1) {
                postDataParams.putOpt("email", edt_email.getText().toString());
                postDataParams.putOpt("password", edt_password.getText().toString());
            }else{
                postDataParams.putOpt("email", postRegisterModel.getEmail());
            }
            aq.post(url, postDataParams, JSONObject.class, cb);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            Toast.makeText(LoginActivity.this,ex.getMessage(),Toast.LENGTH_LONG);
        }
    }


    private void signInCreatedUser( ) {
        try {
            //final QBUser user,
            hideStatus();
            showProgress();
            final String userID = loginModel.getUser().getId();
            String mail = loginModel.getUser().getEmail();
            // Register new user
            final QBUser user = new QBUser(userID, userID);
            user.setEmail(mail);
            if (isValid(loginModel.getUser().getFirstName()))
            {
                user.setFullName(loginModel.getUser().getFirstName());
            }

            QBSettings.getInstance().init(LoginActivity.this, APP_ID, AUTH_KEY, AUTH_SECRET);
            QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
            QBAuth.createSession().performAsync(new QbEntityCallbackImpl<QBSession>() {
                @Override
                public void onSuccess(QBSession result, Bundle bundle) {
                    super.onSuccess(result, bundle);
                    requestExecutor.signInUser(user, new com.pacifyr.omhealthcare.quickblox.utils.QBEntityCallbackImpl<QBUser>() {
                        @Override
                        public void onSuccess(QBUser qbUser, Bundle params) {

                            try {

                                MRoleModel roleModel = userModel.getRoleModel();
                                label = roleModel.getLabel();
                                main_label = roleModel.getMainLabel();
                                companyId = userModel.getCompanyId();
                                prefM.saveCompanyID(companyId);
                                qbID = qbUser.getId().toString();

                                if (main_label.equals("pacifyr")) {
                                    hideProgress();
                                    setStatus("Login Failed. Please Try again", R.color.md_red_400);
                                    showStatus();
                                    fbLogout();
                                    prefM.clearSharedAll();
                                    prefM.clear();
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            hideProgress();
                                            hideStatus();
                                            Intent i = new Intent(LoginActivity.this, SignActivity.class);
                                            startActivity(i);
                                            finish();
                                        }
                                    }, 3000);
                                    Bundle args =
                                            FirebaseLogging.getConstantArgsLoggedOut(LoginActivity.this);
                                    args.putString(FirebaseLogging.EC, "Tried to login from " +
                                            "Councelor account" +
                                            " " +
                                            "Account");
                                    FirebaseLogging.sendLog(LoginActivity.this,
                                            FirebaseLogging.EVENT_LOGIN_FAIL, args);
                                } else if (main_label.equals("customer")) {
                                    saveToPrefM();

                                    saveUserData(qbUser);
                                    startLoginService(qbUser);

                                    NetworkService.editProfileObject = new JSONObject();
                                    NetworkService.editProfileObject.putOpt("qbid", Long.valueOf(qbID));
                                    //NetworkService.editProfileObject.putOpt("online", true);
                                    NetworkService.startActionEditProfile(LoginActivity.this);
                                    Boolean flag = userModel.isTermsAccepted();
                                    hideProgress();
                                    setStatus("Login Succesfull", R.color.md_green_400);
                                    showStatus();

                                    if (label.equalsIgnoreCase("employee")) {
                                        saveCustomerData(LoginActivity.this, "employee");

                                    } else if (label.equalsIgnoreCase("unpaidEmployee")) {
                                        saveCustomerData(LoginActivity.this, "unpaidEmployee");

                                    } else if (label.equalsIgnoreCase("customer")) {
                                        saveCustomerData(LoginActivity.this, "customer");

                                    } else if (label.equalsIgnoreCase("paidMember")) {
                                        saveCustomerData(LoginActivity.this, "paidMember");
                                    }

                                    Bundle args =
                                            FirebaseLogging.getConstantArgsLoggedIn(LoginActivity.this);

                                    if (postRegisterModel != null)
                                        args.putString(FirebaseLogging.EVENT_LOGIN_TYPE,
                                                postRegisterModel.getLoginType());
                                    FirebaseLogging.sendLog(LoginActivity.this,
                                            FirebaseLogging.EVENT_LOGIN_SUCCESS, args);
                                }

                            } catch (Exception ex) {
                                ex.printStackTrace();
                                fbLogout();
                                prefM.clear();
                                Toast.makeText(LoginActivity.this, ex.getMessage(), Toast.LENGTH_LONG);
                            }

                 /* if (deleteCurrentUser) {
                    removeAllUserData(result);
                   } else {
                    startOpponentsActivity();
                   }*/
                        }

                        @Override
                        public void onError(QBResponseException responseException) {
                            if (responseException.getHttpStatusCode() == 401) {
                                quickbloxNewLogin(user);
                            } else {
                                String res = responseException.getMessage();
                                Toast.makeText(LoginActivity.this, responseException.getMessage(), Toast.LENGTH_LONG);
                                //  hideProgressDialog();
                                // Toaster.longToast(com.quickblox.sample.groupchatwebrtc.R.string.sign_up_error);
                                setStatus("Login Failed. Please Try again", R.color.endcall);
                                showStatus();
                                prefM.clearSharedAll();
                                prefM.clear();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        hideProgress();
                                        hideStatus();
                                        Intent i = new Intent(LoginActivity.this, SignActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                }, 3000);
                                Bundle args =
                                        FirebaseLogging.getConstantArgsLoggedOut(LoginActivity.this);
                                args.putString(FirebaseLogging.EC, responseException.getMessage());
                                FirebaseLogging.sendLog(LoginActivity.this,
                                        FirebaseLogging.EVENT_LOGIN_FAIL, args);
                            }
                        }
                    });
                }

                @Override
                public void onError(QBResponseException e) {
                    super.onError(e);
                }
            });


        }
        catch (Exception e)
        {
            prefM.clear();
            e.printStackTrace();
            Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_LONG);
        }
    }


    private void quickbloxNewLogin(final QBUser qbUser) {
        try {
            QBSettings.getInstance().init(LoginActivity.this, APP_ID, AUTH_KEY, AUTH_SECRET);
            QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
            //create a quickblox application session
            QBAuth.createSession().performAsync(new QBEntityCallback<QBSession>() {
                @Override
                public void onSuccess(QBSession qbSession, Bundle bundle) {
                    // Register new user

                    QBUsers.signUp(qbUser).performAsync(new QBEntityCallback<QBUser>() {
                        @Override
                        public void onSuccess(QBUser qbUser, Bundle bundle) {

                            saveUserData(qbUser);
                            startLoginService(qbUser);
                            getPrefManager(LoginActivity.this).saveQBID(qbUser.getId().toString());
                            saveToPrefM();
                            updateProfileWeb(qbUser);
                        }

                        @Override
                        public void onError(QBResponseException responseException) {
                            responseException.printStackTrace();
                            Toast.makeText(context, responseException.getMessage(), Toast.LENGTH_LONG).show();
                            hideProgress();
                            LoginManager mLoginManager = LoginManager.getInstance();
                            mLoginManager.logOut();
                            prefM.clear();
                            startActivity(new Intent(context, SignActivity.class));
                        }
                    });

                }

                @Override
                public void onError(QBResponseException e) {
                    e.printStackTrace();
                    if (e.getHttpStatusCode() == 422 && e.getMessage().toLowerCase().contains("bad timestamp")) {
                        iOSDialogClickListener listener = new iOSDialogClickListener() {
                            @Override
                            public void onClick(iOSDialog dialog) {
                                dialog.dismiss();
                                prefM.clearSharedAll();
                                fbLogout();
                                startActivity(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS));
                                finish();
                            }
                        };
                        showIOSDialogOK("", "Your device date & time is not correct. Please correct it and try sign in.", "Settings", listener);

                    } else {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                        hideProgress();
                        fbLogout();
                        prefM.clear();
                        startActivity(new Intent(context, SignActivity.class));
                    }
                }
            });

        } catch (Exception exc) {
            hideProgress();
            exc.printStackTrace();
            Toast.makeText(context, exc.getMessage(), Toast.LENGTH_LONG).show();
            fbLogout();
            prefM.clear();
            startActivity(new Intent(context, SignActivity.class));

        }

    }

    private void updateProfileWeb(QBUser qbUser) {
        try {
            String url = GLOBAL_URL + "user/editprofile/" + qbUser.getLogin();
            com.koushikdutta.async.future.FutureCallback<JsonObject> futureCallback = new com.koushikdutta.async.future.FutureCallback<JsonObject>() {
                @Override
                public void onCompleted(Exception e, JsonObject result) {
                    try {
                        if (isValid(result)) {

                            final JSONObject jsonObject = new JSONObject(result.toString());
                            //String js = result.toString();
                            String phone = jsonObject.getString("phoneNumber");
                            //String messag= json.getString("message");
                            //String logstat = status.getMessage();
                            if (isValid(phone)) {
                                hideProgress();
                                setStatus("User registered Successfully", R.color.md_green_400);
                                showStatus();

                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                       /* Intent i = new Intent(RegisterActivity.this, WelcomeTourActivity.class);
                                        startActivity(i);
                                        finish();*/
                                        hideStatus();
                                        signInCreatedUser();

                                    }
                                }, 2000);

                            } else/* if (logstat.equals("error"))*/ {
                                Toast.makeText(LoginActivity.this, "Error :" + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    } catch (Exception eee) {
                        hideProgress();
                        Toast.makeText(LoginActivity.this, "Error :" + eee.getMessage(), Toast.LENGTH_LONG).show();
                        eee.printStackTrace();
                    }
                }
            };
            //AQuery aq = new AQuery(RegisterActivity.this);
            //cb.header("AccessToken", accessToken);
            //cb.header("enctype", "multipart/form-data");
            JSONObject postDataParams = new JSONObject();
            postDataParams.putOpt("qbid", Long.valueOf(qbUser.getId().toString()));
            String json = postDataParams.toString();
            HashMap<String, JSONObject> map = new HashMap<String, JSONObject>();
            map.put("data", postDataParams);
            Ion.with(context).load(url).setHeader("AccessToken", prefM.getAccessToken()).setHeader("enctype", "multipart/form-data").setMultipartParameter("data", json).asJsonObject().setCallback(futureCallback);

        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();

        }
    }


    private void saveCustomerData(Context context,String role) {
        if(isValid(companyId))
            prefM.saveCompanyID(companyId);
        prefM.saveCustomerRole(role);
        DashboardActivity.start(context);
        finishAffinity();
    }

    private void saveToPrefM() {
        prefM.saveJsonKey(jsonStrng);
        prefM.saveAccessToken(loginModel.getAccessToken());
        prefM.saveUserID(loginModel.getUser().getId());
        prefM.saveUserFirstName(userModel.getFirstName());
        prefM.saveUserUserLastName(userModel.getLastName());
        prefM.saveMail(userModel.getEmail());
        prefM.saveCountry(userModel.getCountry());
        prefM.saveCountry(userModel.getCountry());
        prefM.saveCompanyID(userModel.getCompanyId());
        if (isValid(userModel.getPhoneNumber()))
            prefM.savePhoneNumber(userModel.getPhoneNumber());
        prefM.saveQBID(qbID);

        if(isValid(FName)){
            prefM.savefbName(FName);
        }else{
            prefM.savefbName(userModel.getFirstName()+" "+userModel.getLastName());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //callbackmanager2.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
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
        defSetText(titleTxv, "Sign in");
        Typeface type = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.avantica_bold_header));
        titleTxv.setTypeface(type);
        //backImv.setBackgroundResource(R.drawable.leftmen);
        leftLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
