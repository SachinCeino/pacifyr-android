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
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.adapter.CountrySpinnerAdapter;
import com.pacifyr.omhealthcare.base.PacifyerActivity;
import com.pacifyr.omhealthcare.home.DashboardActivity;
import com.pacifyr.omhealthcare.model.ItemData;
import com.pacifyr.omhealthcare.network.GsonUtils;
import com.pacifyr.omhealthcare.network.NetworkService;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.model.QBUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

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
            spinner = (Spinner) findViewById(R.id.spinner);
            phoneCedit = (CustomEditText) findViewById(R.id.phone_cedit);
            facebookRlay = (RelativeLayout) findViewById(R.id.facebook_rlay);
            facebookRlay.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    fbLogout();
                    showProgress();
                    loginButton.performClick();
                }
            });

            loginButton = (LoginButton) findViewById(R.id.login_button);
            loginButton.setReadPermissions("user_friends", "email");
            callbackManager = CallbackManager.Factory.create();
            loginButton = (LoginButton) findViewById(R.id.login_button);

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

            loginRlay = (RelativeLayout) findViewById(R.id.login_rlay);
            loginRlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //  validRegister();
                    phoneVerifyWeb();

                }
            });

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
                        userModel = loginModel.getUser();
                        companyId = userModel.getCompanyId();
                        prefM.saveCompanyID(companyId);




                        String logstat = loginModel.getStatus().toString();
                        if (isValid(logstat)) {
                            if (logstat.equals("success")) {

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
                                Toast.makeText(LoginActivity.this,loginModel.getMessage(),Toast.LENGTH_LONG);
                                prefM.clear();
                                LoginManager mLoginManager = LoginManager.getInstance();
                                mLoginManager.logOut();
                                setStatus(loginModel.getMessage(), R.color.md_red_400);
                                showStatus();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        hideStatus();
                                        finish();
                                        /*hideKeyboard();*/
                                    }
                                }, 3000);
                            }
                        }
                    } catch (Exception e) {
                        hideProgress();
                        Toast.makeText(LoginActivity.this,/*loginModel.getMessage()+"/n"+*/e.getMessage(),Toast.LENGTH_LONG);
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
            String fullname = loginModel.getUser().getFirstName();
            // Register new user
            final QBUser user = new QBUser(userID, userID);
            user.setEmail(mail);

            requestExecutor.signInUser(user, new com.pacifyr.omhealthcare.quickblox.utils.QBEntityCallbackImpl<QBUser>() {
                @Override
                public void onSuccess(QBUser qbUser, Bundle params) {

                    try {

                        MRoleModel roleModel = userModel.getRoleModel();
                        label = roleModel.getLabel();
                        main_label=roleModel.getMainLabel();
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
                                saveCustomerData(LoginActivity.this,"employee");

                            }
                            else if (label.equalsIgnoreCase("unpaidEmployee")) {
                                saveCustomerData(LoginActivity.this,"unpaidEmployee");

                            }
                            else if (label.equalsIgnoreCase("customer")) {
                                saveCustomerData(LoginActivity.this,"customer");

                            } else if (label.equalsIgnoreCase("paidMember")) {
                                saveCustomerData(LoginActivity.this,"paidMember");
                            }
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        fbLogout();
                        prefM.clear();
                        Toast.makeText(LoginActivity.this,ex.getMessage(),Toast.LENGTH_LONG);
                    }

                 /* if (deleteCurrentUser) {
                    removeAllUserData(result);
                   } else {
                    startOpponentsActivity();
                   }*/
                }

                @Override
                public void onError(QBResponseException responseException) {
                    String res = responseException.getMessage();
                    Toast.makeText(LoginActivity.this,responseException.getMessage(),Toast.LENGTH_LONG);
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
        defSetText(titleTxv,"log in");
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
