package com.pacifyr.client.sign;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.opensooq.supernova.gligar.GligarPicker;
import com.pacifyr.client.R;
import com.pacifyr.client.adapter.CountrySpinnerAdapter;
import com.pacifyr.client.base.PacifyrActivity;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.home.DashboardActivity;
import com.pacifyr.client.home.PacifyrDashboardActivity;
import com.pacifyr.client.model.ItemData;
import com.pacifyr.client.network.GsonUtils;
import com.pacifyr.client.network.NetworkService;
import com.pacifyr.client.utils.LogUtils;
import com.pacifyrapp.utilitylibrary.CustomEditText;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;
import com.pacifyrapp.utilitylibrary.ToastHandler;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MRoleModel;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MUser;
import com.pacifyrapp.utilitylibrary.model.pacifyr.ModelFacebook;
import com.pacifyrapp.utilitylibrary.model.pacifyr.ModelPhoneLogin;
import com.pacifyrapp.utilitylibrary.model.pacifyr.post.PRegistrationModel;
import com.pacifyrapp.utilitylibrary.model.pacifyr.post.PostRegisterModel;
import com.pacifyrapp.utilitylibrary.network.UtilityNetworkService;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.model.QBUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import static com.pacifyr.client.network.NetworkService.countryData;

public class SignUpActivity extends PacifyrActivity {

    private static final int REQUESTCODE_CAMERA_IMAGE = 101;
    private static final int REQUESTCODE_GALLERY_IMAGE = 102;
    public static final String MY_PREFS_NAME = "AppPrefFile";
    public static final String mypreference = "mypref";


    private RelativeLayout logoRlay;
    public RelativeLayout signup_rlay;

    //SharedPreferences sharedpreferences;

    private RelativeLayout facebookRlay;
   //private TextView fbTcv;
    private LoginButton loginButton;
    CallbackManager callbackManager;
    public String FEmail,FFname,FLname,FName,label;

    PRegistrationModel registrationModel;
    MUser userModel;

    PrefManager prefM;
    String userid,acessToken;

    public Spinner sp;

    ModelFacebook facebookModel;

    //RadioButton radioButton;
    //LinearLayout otherCountryLlay;

    //CountryCodePicker countryCodePicker;
    //boolean isCOuntry;

    private CustomEditText phoneCedit;
    private ModelPhoneLogin modelRegister;
    private String mainLabel;
    private String jsonString;
    Context context;
    private String qbID;
    private MainTextView sms_text_login;
    private TextView TextView_phone_error;
    private LinearLayout LinearLayout_phone;
    //private MainTextView enter_phone_ctxv;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_sign_up);
        try {
            fbLogout();
            setContentView(R.layout.activity_sign_up);
            context = SignUpActivity.this;
            prefM = PrefManager.getInstance(context);
            sms_text_login = findViewById(R.id.sms_text_login);
            TextView_phone_error = findViewById(R.id.TextView_phone_error);
            LinearLayout_phone = findViewById(R.id.LinearLayout_phone);

            /*hideKeyboard();*/
            onSetActionBar();
            FacebookSdk.sdkInitialize(SignUpActivity.this);
            //countryCodePicker=(CountryCodePicker) findViewById(R.id.ccp);
            //radioButton=(RadioButton) findViewById(R.id.other_country_rbtn);
            //isCOuntry=false;
            /*otherCountryLlay=(LinearLayout) findViewById(R.id.other_country_llay);*/
            /*radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!isCOuntry) {
                        otherCountryLlay.setVisibility(View.VISIBLE);
                        isCOuntry=true;
                    }
                    else {
                        isCOuntry=false;
                        otherCountryLlay.setVisibility(View.GONE);
                    }
                }
            });*/

            ArrayList<ItemData> list=new ArrayList<>();
            list.add(new ItemData("+1","US",R.drawable.flag_usa));
            if (!getPackageName().equalsIgnoreCase("com.pacifyr.pam")) {
                list.add(new ItemData("+1", "Canada", R.drawable.canada2));
                list.add(new ItemData("+91", "India", R.drawable.ic_india));
                list.add(new ItemData("+971", "UAE", R.drawable.flag_uae2));
            }
            /*list.add(new ItemData("+971","Kuwait", R.drawable.kuwait2));*/
            sp = findViewById(R.id.spinner);
            CountrySpinnerAdapter adapter=new CountrySpinnerAdapter(this,
                    R.layout.spinner_item,R.id.txt,list);
            sp.setAdapter(adapter);


            String countryCode = prefM.getCountryCode();
            if (!getPackageName().equalsIgnoreCase("com.pacifyr.pam")) {
                if (countryCode.equals("IN")) {
                    sp.setSelection(2);
                } else if (countryCode.equals("US")) {
                    sp.setSelection(0);
                } else if (countryCode.equals("Canada")) {
                    sp.setSelection(1);
                } else if (countryCode.equals("AE")) {
                    sp.setSelection(3);
                }
            }
            else
            {
                if (countryCode.equals("US")){
                    sp.setSelection(0);
                }
            }

            /* enter_phone_ctxv = (MainTextView) findViewById(R.id.enter_phone_ctxv);*/

            /*ItemData it= (ItemData) sp.getSelectedItem();*/
            logoRlay = findViewById(R.id.logo_rlay);
            phoneCedit = findViewById(R.id.phone_cedit);
            loginButton = findViewById(R.id.login_button);
            facebookRlay = findViewById(R.id.facebook_rlay);
            facebookRlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loginButton.performClick();
                }
            });
            if (getPackageName().equalsIgnoreCase(UtilityNetworkService.PACKAGE_PAM)) {
                findViewById(R.id.linearLayout).setVisibility(View.GONE);
            }
            //fbTcv = (TextView) findViewById(R.id.fb_tcv);

            loginButton.setReadPermissions("user_friends", "email");
            callbackManager = CallbackManager.Factory.create();
            loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));
            /*loginButton.setReadPermissions("email");
            loginButton.setReadPermissions(Arrays.asList("user_status"));*/
            // Other app specific specialization
            /*LoginButton loginButton = (LoginButton) findViewById(R.id.usersettings_fragment_login_button);
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() { ... });*/
            // Callback registration

           /* Typeface type = Typeface.createFromAsset(getAssets(),"fonts/Raleway-Bold.ttf");
            enter_phone_ctxv.setTypeface(type);*/

            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    //Toast.makeText(SignUpActivity.this,loginResult.getAccessToken().getUserId().toString(),Toast.LENGTH_LONG).show();
                    // AppChatNotUSe code
                    // Facebook Email address
                    GraphRequest request = GraphRequest.newMeRequest(
                            loginResult.getAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(
                                        JSONObject object,
                                        GraphResponse response) {
                                    Log.v("LoginActivity Response ", response.toString());

                                    try {
                                        String profilePicUrl = null;
                                        JSONObject data = response.getJSONObject();
                                        facebookModel = GsonUtils.getInstance().gsonToModelFacebook(data);
                                        if (data.has("picture")) {
                                            profilePicUrl = data.getJSONObject("picture").getJSONObject("data").getString("url");
                                        }
                                        FName = object.getString("name");
                                        FFname=object.getString("first_name");
                                        FLname=object.getString("last_name");
                                        if(isValid(facebookModel.getEmail())) {
                                            FEmail = facebookModel.getEmail();
                                        }else{
                                            setAlertOKStatusInBlue("Access Error","Please enable email id accessible on your facebook Privacy Settings.","OK");
                                            fbLogout();
                                            return;
                                        }

                                        Log.v("Email = ", " " + FEmail);
                                        //Toast.makeText(SignUpActivity.this, "FEmail " + FEmail, Toast.LENGTH_LONG).show();

                                        PostRegisterModel postRegisterModel = new PostRegisterModel();

                                        postRegisterModel.setFirstName(FFname);
                                        postRegisterModel.setEmail(FEmail);
                                        //postRegisterModel.setPassword(edt_password.getText().toString());
                                        postRegisterModel.setLastName(FLname);
                                        postRegisterModel.setLoginType("facebook");
                                        //registerWeb(postRegisterModel);
                                        Intent i =new Intent(SignUpActivity.this,RegisterActivity.class);
                                        i.putExtra("from","facebook");
                                        i.putExtra("firstName",FFname);
                                        i.putExtra("email",FEmail);
                                        i.putExtra("lastName",FLname);
                                        i.putExtra("imageUrl",profilePicUrl);
                                        startActivity(i);
                                        finish();


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        prefM.clear();
                                        LoginManager mLoginManager = LoginManager.getInstance();
                                        mLoginManager.logOut();
                                    }
                                }
                            });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,name,email,gender,birthday,first_name,last_name,cover,picture.type(large)");
                    request.setParameters(parameters);
                    request.executeAsync();
                }

                @Override
                public void onCancel() {
                    //Toast.makeText(SignUpActivity.this,"on cancel",Toast.LENGTH_LONG).show();
                    // AppChatNotUSe code
                }

                @Override
                public void onError(FacebookException exception) {
                    // AppChatNotUSe code
                    Toast.makeText(SignUpActivity.this,exception.toString(),Toast.LENGTH_LONG).show();

                }
            });
            signup_rlay = findViewById(R.id.request_rlay);
            signup_rlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //  validRegister();

                    phoneVerifyWeb();

                }
            });
            sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    countryData = list.get(position);
                    selectRemoteConfigUrl(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            selectRemoteConfigUrl(sp.getSelectedItemPosition());


            phoneCedit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    TextView_phone_error.setVisibility(View.GONE);
                    makeViewBackgroundBlack(LinearLayout_phone);
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    TextView_phone_error.setVisibility(View.GONE);
                    makeViewBackgroundBlack(LinearLayout_phone);
                }

                @Override
                public void afterTextChanged(Editable s) {
                    TextView_phone_error.setVisibility(View.GONE);
                    makeViewBackgroundBlack(LinearLayout_phone);
                }
            });

            sms_text_login.setText(getResources().getString(R.string.app_name) + " " + getResources().getString(R.string.sms_text_login));
        }catch (Exception e) {    e.printStackTrace();
            //LogUtils.LOGD("sign","lA onCreate exception "+e.getMessage());
        }
    }

    public void  makeViewBackgroundBlack(View mView)
    {
        mView.setBackground(getResources().getDrawable(R.drawable.edittext_black_curve));
    }

    public void  makeViewBackgroundRed(View mView)
    {
        mView.setBackground(getResources().getDrawable(R.drawable.edittext_red_curve));
    }

    private void makeAlertMessageInTextView(TextView textView_name_error, String error) {
        textView_name_error.setText(error);
        textView_name_error.setVisibility(View.VISIBLE);
    }
    @Override
    protected View getSnackbarAnchorView() {
        return null;
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
        try {
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
            defSetText(titleTxv, getResources().getString(R.string.register));
            Typeface type = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.avantica_bold_header));
            titleTxv.setTypeface(type);
            //titleTxv.setText("Add Credit Card");
            //titleTxv.setTextColor(getResources().getColor(R.color.white));
            //backImv.setImageDrawable(getResources().getDrawable(R.drawable.back3x));

            //backImv.setBackgroundResource(R.drawable.leftmen);
            leftLlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

        }
        catch (Exception e){
            e.printStackTrace();
            Log.e("Exception ",e.getMessage());
        }
    }

    private void phoneVerifyWeb() {
        try{
            if(!isValidMobile(phoneCedit,getResources().getString(R.string.Please_enter_your_phone_number)))
                return ;
            countryData = (ItemData) sp.getSelectedItem();
            String code = countryData.getCode();
            RegisterVerifyActivity.phoneRegister = code+/* countryCodePicker.getSelectedCountryCode() +*/ phoneCedit.getText() + "";
            RegisterVerifyActivity.phoneRegisterSub = code+ /*countryCodePicker.getDefaultCountryCode() + " " +*/ phoneCedit.getText() + " ";
            if(NetworkService.isRelease) {
                Intent intent = new Intent(SignUpActivity.this, RegisterVerifyActivity.class);
                intent.putExtra("from", "" + "signup");
                startActivity(intent);
                finish();
            }
            else{
                Intent intent = new Intent(this, RegisterActivity.class);
                intent.putExtra("from", "" + "signup");
                startActivity(intent);
                finish();
            }
            // OnVerificationStateChangedCallbacks
          }
          catch (Exception e)
          {
              e.printStackTrace();
              LogUtils.LOGD("signUP","lA onCreate exception "+e.getMessage());
          }
    }

    public boolean isValidMobile(CustomEditText phone_tv, String error) {
        if (phone_tv != null) {
            if (isValid(phone_tv.getText() + "")) {
                if (isValidMobile(phone_tv.getText().toString())) {
                    return true;
                }
                else {
                    error=getResources().getString(R.string.Invalid_phone_number_format);
                }
            }

        }
        makeAlertMessageInTextView(TextView_phone_error,error);
        makeViewBackgroundRed(LinearLayout_phone);
        return false;

        //String phone=phone_tv.getText().toString();
        //return android.util.Patterns.PHONE.matcher(phone).matches();
    }




   /* private void validRegister() {
        try{
            if(!isValid(edt_name,"Enter Name"))
            return ;
            if(!isValid(edt_email,"Enter Email"))
                return ;
            if(!isValid(edt_password,"Enter password"))
                return;
            PostRegisterModel postRegisterModel=new PostRegisterModel();
            postRegisterModel.setFirstName(edt_name.getText().toString());
            postRegisterModel.setEmail(edt_email.getText().toString());
            postRegisterModel.setPassword(edt_password.getText().toString());
            postRegisterModel.setLastName("");
            postRegisterModel.setLoginType("manual");
            registerWeb(postRegisterModel);

        }catch (Exception e)
        {
           // LogUtils.LOGD("sign","lA validRegister exception "+e.getMessage());
        }
    }*/

    private void registerWeb(PostRegisterModel regpost) {
        try {
            setLoadingWait();
            showProgress();
             // Gson gson = new GsonBuilder().setExclusionStrategies(new TestExclStrat())
             //.serializeNulls() <-- uncomment to serialize NULL fields as well
                    //.create();
            Gson gson = new Gson();
            // gson .setExclusionStrategies(new TestExclStrat())
            String json = gson.toJson(regpost);
            //{"email":"samsad@ceinotech.com","firstName":"samsd","lastName":"","loginType":"manual","password":"samsad"}
            FutureCallback<JsonObject> callback2 = new com.koushikdutta.async.future.FutureCallback<JsonObject>() {
                @Override
                public void onCompleted(Exception e, final JsonObject json) {
                    try {
                        hideProgress();
                        final JSONObject json2 = new JSONObject(json.toString());
                            registrationModel = GsonUtils.getInstance().gsonToRegistrationModel(json2);
                            userModel = registrationModel.getUser();
                            jsonString = json2.toString();
                            //ToastHandler.newInstance(SignUpActivity.this).mustShowToast(sucesss.toString());
                            if (isValid(registrationModel,"Please try again ")) {
                                showProgress();
                                String stat = registrationModel.getStatus();
                                if(isValid(userModel,"Please try again ")) {
                                    if(stat.equals("success")) {
                                        //setStatus("Register Added", R.color.md_green_400);
                                        //showStatus();
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {

                                                //finish();
                                                showProgress();
                                                try {


                                                    //String message = json2.getString("message");
                                                     MRoleModel roleModel = userModel.getRoleModel();
                                                    mainLabel = roleModel.getMainLabel();
                                                    label=roleModel.getLabel();
                                                    createQBUser();

                                                } catch (Exception exx) {
                                                    exx.printStackTrace();
                                                    prefM.clear();
                                                    setStatus("Network Error.", R.color.md_red_100);
                                                    showStatus();
                                                    LoginManager mLoginManager = LoginManager.getInstance();
                                                    mLoginManager.logOut();
                                                }
                                               /* if (label.equals("pacifyr")){
                                                    Intent i = new Intent(SignUpActivity.this, PacifyrDashboardActivity.class);
                                                    startActivity(i);
                                                    finish();
                                                }else if(label.equals("customer")) {
                                                    Intent i = new Intent(SignUpActivity.this, DashboardActivity.class);
                                                    startActivity(i);
                                                    finish();
                                                }*/

                                            }
                                        }, 600);
                                    }
                                    else{
                                        prefM.clear();
                                        //ToastHandler.newInstance(SignUpActivity.this).mustShowToast(sucesss.getMessage());
                                        setStatus(registrationModel.getMessage(), R.color.md_red_100);
                                        showStatus();
                                        LoginManager mLoginManager = LoginManager.getInstance();
                                        if(isValid(mLoginManager))
                                            mLoginManager.logOut();
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                hideStatus();
                                            }
                                        },900);
                                    }
                                }
                            }
                            else {
                                hideProgress();
                                LoginManager mLoginManager = LoginManager.getInstance();
                                mLoginManager.logOut();
                                prefM.clear();
                            }
                    } catch (Exception e2) {
                        hideProgress();
                        prefM.clear();
                        LoginManager mLoginManager = LoginManager.getInstance();
                        mLoginManager.logOut();
                        e2.printStackTrace();
                        ToastHandler.newInstance(SignUpActivity.this).mustShowToast("Please try again");
                    }
                }
            };
         //   String url= "http://testapp.pacifyr.net:8080/pacifyr/v1/register";
            String url= NetworkService.GLOBAL_URL+"register";
            Ion.with(SignUpActivity.this).load(url).setHeader("enctype", "multipart/form-data").setMultipartParameter("data", json).asJsonObject().setCallback(callback2);
        } catch (Exception e){
          e.printStackTrace();
            prefM.clear();
            LoginManager mLoginManager = LoginManager.getInstance();
            mLoginManager.logOut();
        }
    }

    private void saveUserDataToPrefM() {
        prefM.saveJsonKey(jsonString);
        prefM.saveMail(userModel.getEmail());
        prefM.saveAccessToken(registrationModel.getAccessToken());
        prefM.saveUserID(registrationModel.getUser().getId());
        prefM.saveUserFirstName(userModel.getFirstName());
        prefM.saveUserUserLastName(userModel.getLastName());
        prefM.saveUserID(userModel.getId());

        prefM.saveQBID(qbID);

        if(isValid(FName)){
            prefM.savefbName(FName);
        }
    }


    private void createQBUser( ) {
        try {

            //final QBUser user,
            final String userID = userModel.getId();
            final QBUser user = new QBUser(userID, userID);
            if(isValid(userModel.getEmail())){
                String mail = userModel.getEmail();
                user.setEmail(mail);
            }
            setLoading();
            showProgress();
            requestExecutor.signUpNewUser(user, new com.pacifyr.client.quickblox.utils.QBEntityCallbackImpl<QBUser>() {
                @Override
                public void onSuccess(QBUser qbUser, Bundle params) {

                    try {
                        hideProgress();
                        setStatus("Registration Succesfull", R.color.md_green_400);
                        showStatus();

                        qbID = qbUser.getId().toString();
                        saveUserDataToPrefM();

                        saveUserData(qbUser);
                        startLoginService(qbUser);


                        NetworkService.editProfileObject = new JSONObject();
                        NetworkService.editProfileObject.putOpt("qbid", Long.valueOf(qbID));
                        //NetworkService.editProfileObject.putOpt("online", true);
                        NetworkService.startActionEditProfile(SignUpActivity.this);

                        MUser user = registrationModel.getUser();
                        String compID = user.getCompanyId();
                        if (mainLabel.equals("pacifyr")) {
                            Intent i = new Intent(SignUpActivity.this, PacifyrDashboardActivity.class);
                            startActivity(i);
                            finish();
                        } else if (mainLabel.equals("customer")) {
                            //    Intent i = new Intent(SignUpActivity.this, DashboardActivity.class);
                            if (label.equalsIgnoreCase("employee")) {
                                prefM.saveCustomerRole("employee");
                                prefM.saveCompanyID(compID);
                                //PrefManager.getInstance(SignUpActivity.this).saveCompanyID(companyId);
                                DashboardActivity.start(SignUpActivity.this);
                                finishAffinity();
                            } else if (label.equalsIgnoreCase("customer")) {
                                //  ConnectToPacifyrActivity.start(SignUpActivity.this);
                                prefM.saveCustomerRole("customer");
                                DashboardActivity.start(SignUpActivity.this);
                                finishAffinity();
                            } else if (label.equalsIgnoreCase("paidMember")) {
                                //  ConnectToPacifyrActivity.start(SignUpActivity.this);
                                prefM.saveCustomerRole("paidMember");
                                DashboardActivity.start(SignUpActivity.this);
                                finishAffinity();
                            }
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        prefM.clear();
                        fbLogout();
                    }
                }

                @Override
                public void onError(QBResponseException responseException) {
                    try {
                        //  hideProgressDialog();
                        responseException.printStackTrace();
                        // Toaster.longToast(com.quickblox.sample.groupchatwebrtc.R.string.sign_up_error);
                        setStatus("Network intrupted. Please Try again", R.color.endcall);
                        prefM.clear();
                        fbLogout();
                        showStatus();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                hideProgress();
                                hideStatus();
                                startActivity(new Intent(context, SignActivity.class));
                                finish();
                            }
                        }, 3000);
                    }
                    catch (Exception e)
                    {
                        hideProgress();
                        e.printStackTrace();
                        prefM.clear();
                        fbLogout();
                        startActivity(new Intent(context, SignActivity.class));
                    }
                }
            });
        }
        catch (Exception e){
            hideProgress();
            e.printStackTrace();
            prefM.clear();
            fbLogout();
            startActivity(new Intent(context, SignActivity.class));
        }
    }

    public void backpress(View view) {
        finish();
    }

    public void selectImage(View view) {
        try {
            final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Add Photo!");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (items[item].equals("Take Photo")) {
                        new GligarPicker().requestCode(REQUESTCODE_CAMERA_IMAGE).disableCamera(false).cameraDirect(true).limit(1).withActivity(SignUpActivity.this).show();

                    } else if (items[item].equals("Choose from Library")) {
                        new GligarPicker().requestCode(REQUESTCODE_GALLERY_IMAGE).disableCamera(true).limit(1).withActivity(SignUpActivity.this).show();
                    } else if (items[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

            callbackManager.onActivityResult(requestCode, resultCode, imageReturnedIntent);

    }
}
