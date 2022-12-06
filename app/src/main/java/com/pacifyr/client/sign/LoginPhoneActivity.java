package com.pacifyr.client.sign;

import static com.pacifyr.client.network.NetworkService.editProfileObject;
import static com.pacifyr.client.network.NetworkService.isRelease;
import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.GLOBAL_URL;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.pacifyr.client.R;
import com.pacifyr.client.base.PacifyrActivity;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.home.DashboardActivity;
import com.pacifyr.client.network.GsonUtils;
import com.pacifyr.client.network.NetworkService;
import com.pacifyr.client.orginals.Auth;
import com.pacifyr.client.utility.AWSManager;
import com.pacifyr.client.utils.AmazonSNSTokenHandler;
import com.pacifyr.client.utils.FirebaseLogging;
import com.pacifyrapp.utilitylibrary.CustomEditText;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MRoleModel;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MUser;
import com.pacifyrapp.utilitylibrary.model.pacifyr.ModelPhoneLogin;
import com.pacifyrapp.utilitylibrary.model.pacifyr.ModelUserProfile;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;

import org.json.JSONObject;

public class LoginPhoneActivity extends PacifyrActivity {

    public Bundle bundle;
    public String phoneNo,token;
    public ModelPhoneLogin modelPhoneLogin;
    public CustomEditText phone_tv;
    public String main_label="error",label="error";
    JSONObject userObject;
    private MUser userModel;
    private MRoleModel roleModel;
    private String jsonStrng;
    private Context context;

    private PrefManager prefM;
    private static final String TAG = LoginPhoneActivity.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fbLogout();

        setContentView(R.layout.activity_sign_in_code_verify);
        onSetActionBar();
        context = LoginPhoneActivity.this;
        prefM = PrefManager.getInstance(context);
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        bundle=getIntent().getExtras();
        phoneNo=bundle.getString("phone");
        if (isValid(bundle.getString("token")))
            token=bundle.getString("token");
        loginUsingPhone();


    }

    @Override
    protected View getSnackbarAnchorView() {
        return null;
    }

    public void loginUsingPhone() {
        try {
            setLoading();
            showProgress();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                }
            }, 300);
            try {

                callLogin();


            } catch (Exception e) {
                hideProgress();
                e.printStackTrace();
            }
        }
        catch(Exception ex)
        {   hideProgress();
            ex.printStackTrace();}
    }

    public void callLogin()
    {
        Log.d(TAG, "callLogin: ");
        try {
            String url;
            if (isRelease){
                url = GLOBAL_URL + "login/firebaseAuth";
            }
            else {
                url = GLOBAL_URL + "login/phonenumber";
            }
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, final JSONObject json, final AjaxStatus status) {
                    try {
                        if (json != null && json.has("status") && json.getString("status").equals("error")) {
                            setAlertOKStatusInRed("Login failed", json.getString("message"), "OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(LoginPhoneActivity.this, SignActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                            Bundle args =
                                    FirebaseLogging.getConstantArgsLoggedOut(LoginPhoneActivity.this);
                            args.putString(FirebaseLogging.EC,
                                    phoneNo + " tried Login. Status code:" + status.getCode());
                            FirebaseLogging.sendLog(LoginPhoneActivity.this,
                                    FirebaseLogging.EVENT_LOGIN_FAIL, args);
                            return;
                        }
                        modelPhoneLogin = GsonUtils.getInstance().gsonToModelPhoneLogin(json);
                        userModel = modelPhoneLogin.getUser();
                        if (userModel != null && userModel.getCompany() != null){
                            prefM.saveCompanyLogo(userModel.getCompany().getLogo());
                            prefM.setEnablePrescription(userModel.getCompany().getEnablePrescription());
                        }
                        prefM.saveCompanyID(userModel.getCompanyId());
                        jsonStrng = json.toString();
                        String logstat = modelPhoneLogin.getStatus();


                        ModelUserProfile mCurrentUser = GsonUtils.getInstance().gsonToModelUserProfile(json.getJSONObject("user"));
                        if(isValid(mCurrentUser))
                            NetworkService.mCurrentUser=mCurrentUser;
                        else {

                            String image = json.getString("image");
                            NetworkService.mCurrentUser.setImage(image);
                            String firstName = json.getString("firstName");
                            NetworkService.mCurrentUser.setFirstName(firstName);
                            String email = json.getString("email");
                            NetworkService.mCurrentUser.setEmail(email);
                            String description = json.getString("description");
                            NetworkService.mCurrentUser.setFirstName(description);
                        }

                        if (isValid(logstat)) {
                            if (logstat.equals("success")) {

                                if (main_label.equals("pacifyr")){
                                    hideProgress();
                                    setAlertOKStatusInBlue("Info", getResources().getString(R.string.user_type_error), "OK", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent i = new Intent(LoginPhoneActivity.this, SignActivity.class);
                                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(i);
                                            finish();
                                        }
                                    });
                                    /*setStatus(getResources().getString(R.string.user_type_error), R.color.md_red_400);
                                    showStatus();*/
                                    fbLogout();
                                    prefM.clearSharedAll();
                                    prefM.clear();
                                    /*new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            hideProgress();
                                            hideStatus();
                                            Intent i = new Intent(LoginPhoneActivity.this, SignActivity.class);
                                            startActivity(i);
                                            finish();
                                        }
                                    }, 3000);*/
                                }else{
                                    signInCreatedUser();
                                    Bundle args =
                                            FirebaseLogging.getConstantArgsLoggedIn(LoginPhoneActivity.this);
                                    args.putString(FirebaseLogging.EC,
                                            phoneNo + " tried Login. Status code:" + status.getCode());
                                    FirebaseLogging.sendLog(LoginPhoneActivity.this,
                                            FirebaseLogging.EVENT_LOGIN_SUCCESS, args);
                                }
                            } else {
                                hideProgress();
                               /* setStatus("Login failed : \n,"+modelPhoneLogin.getMessage(), R.color.endcall);
                                showStatus();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                       // hideProgress();
                                        hideStatus();
                                        finish();
                                        Intent intent=new Intent(LoginPhoneActivity.this,SignActivity.class);
                                        startActivity(intent);
                                    }
                                }, 900);*/
                                setAlertOKStatusInRed("Login failed", modelPhoneLogin.getMessage(), "OK", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(LoginPhoneActivity.this, SignActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        hideProgress();
                        e.printStackTrace();
                        hideProgress();
                        /*setStatus("Login failed : \n,"+modelPhoneLogin.getMessage(), R.color.endcall);
                        showStatus();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // hideProgress();
                                hideStatus();
                                finish();
                                Intent intent=new Intent(LoginPhoneActivity.this,SignActivity.class);
                                startActivity(intent);
                            }
                        }, 900);*/
                        setAlertOKStatusInRed("Login failed", "Please contact your administrator", "OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(LoginPhoneActivity.this, SignActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                }
            };
            AQuery aq = new AQuery(LoginPhoneActivity.this);
            cb.header("Content-Type", "application/json");
            JSONObject postDataParams = new JSONObject();
           // postDataParams.putOpt("phoneNumber",phoneNo);
           // postDataParams.putOpt("phoneNumber","+"+phoneNo);

            if (isRelease){
                postDataParams.putOpt("applicationName", AWSManager.getProjectName(context));
                postDataParams.putOpt("token",token);
            }
            else {
                postDataParams.putOpt("phoneNumber",phoneNo);
            }

            aq.post(url, postDataParams, JSONObject.class, cb);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            hideProgress();
            setAlertOKStatusInRed("Login failed", "Please contact your administrator", "OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginPhoneActivity.this, SignActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            });
/*            setStatus("Login failed : \n,"+modelPhoneLogin.getMessage(), R.color.endcall);
            showStatus();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // hideProgress();
                    hideStatus();
                    finish();
                    Intent intent=new Intent(LoginPhoneActivity.this,SignActivity.class);
                    startActivity(intent);
                }
            }, 900);*/
        }
    }




    private void signInCreatedUser( ) {
        hideStatus();
        setLoading("Connecting");
        showProgress();
        final  String userID = userModel.getId();
        String mail = modelPhoneLogin.getUser().getEmail();

        try {

            saveValuesToPrefM(context,modelPhoneLogin);
            loginTheUser();
            Bundle args =
                    FirebaseLogging.getConstantArgsLoggedIn(LoginPhoneActivity.this);
            args.putString(FirebaseLogging.EC,
                    FirebaseLogging.EVENT_QUICKBLOX_LOGIN_SUCCESS + "onSuccess");
            FirebaseLogging.sendLog(LoginPhoneActivity.this,
                    FirebaseLogging.EVENT_LOGIN_SUCCESS, args);
        }
        catch (Exception e) {
            hideProgress();
            hideStatus();
            Toast.makeText(LoginPhoneActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
            e.printStackTrace();
            prefM.clearSharedAll();
            fbLogout();
            Intent intent = new Intent(LoginPhoneActivity.this, SignActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();

            Bundle args =
                    FirebaseLogging.getConstantArgsLoggedOut(LoginPhoneActivity.this);
            args.putString(FirebaseLogging.EC,
                    FirebaseLogging.EVENT_QUICKBLOX_LOGIN_FAIL + "Exception"+e);
            FirebaseLogging.sendLog(LoginPhoneActivity.this,
                    FirebaseLogging.EVENT_LOGIN_FAIL, args);
        }



    }

    private void loginTheUser() {
        try{
            roleModel = userModel.getRoleModel();
            main_label = roleModel.getMainLabel();
            label = roleModel.getLabel();

            //NetworkService.mCurrentUser.setImage(userModel.getImage());
//
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                    //ToastHandler.newInstance(LoginPhoneActivity.this).showToast("main_label :"+main_label+" label"+label);
                    if (main_label.equals("pacifyr")){
                        hideProgress();
                        setStatus(getResources().getString(R.string.user_type_error), R.color.md_red_400);
                        showStatus();
                        fbLogout();
                        prefM.clearSharedAll();
                        prefM.clear();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                hideProgress();
                                hideStatus();
                                Intent i = new Intent(LoginPhoneActivity.this, SignActivity.class);
                                startActivity(i);
                                finish();
                            }
                        }, 3000);


                    }else if(main_label.equals("customer")){

                        /**
                         * after 50days need to refresh the token**/
                        long mTimeStamp = System.currentTimeMillis();
                        prefM.saveLoginTimeStamp(String.valueOf(mTimeStamp));
                        prefM.putSharedBoolean("isFirstTimeLogin",true);
                        editProfileObject=new JSONObject();
                        //NetworkService.editProfileObject.putOpt("online", true);
                        hideProgress();
                        setStatus("Login Success ", R.color.md_green_400);
                        showStatus();

                        new AmazonSNSTokenHandler(getApplicationContext()).register();
                        if (label.equalsIgnoreCase("employee")) {
                            prefM.saveCompanyID(userModel.getCompanyId());
                            saveUserRoleGO("employee");
                        } else if (label.equalsIgnoreCase("customer")) {
                            saveUserRoleGO("customer");
                        }
                        else if (label.equalsIgnoreCase("paidMember")) {
                            saveUserRoleGO("paidMember");
                        }else if(label.equalsIgnoreCase("unpaidEmployee")){
                            saveUserRoleGO("unpaidEmployee");
                        }else {
                            hideStatus();
                        }

                        NetworkService.startActionEditProfile(LoginPhoneActivity.this);
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    hideProgress();
                    Toast.makeText(LoginPhoneActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    prefM.clearSharedAll();
                    fbLogout();
                    startActivity(new Intent(LoginPhoneActivity.this,SignActivity.class));
                    finish();
                }

            }
        },500);

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    private void saveUserRoleGO(String role) {
        try {
            hideStatus();
            prefM.saveCustomerRole(role);
            hideKeyboard();
            Auth.setActiveOnFDB(LoginPhoneActivity.this);
            Auth.scanUpdates(LoginPhoneActivity.this);
            Log.d(TAG,"Firebase - user id logged");

            DashboardActivity.start(context);
            finishAffinity();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void saveValuesToPrefM(Context context,ModelPhoneLogin modelPhone) {
        try {
            MUser userModel = modelPhone.getUser();

            prefM.saveJsonKey(jsonStrng);
            prefM.saveAccessToken(modelPhone.getAccessToken());
            prefM.saveUserID(userModel.getId());
            prefM.saveUserFirstName(userModel.getFirstName());
            prefM.saveUserUserLastName(userModel.getLastName());
            prefM.saveMail(userModel.getEmail());
            prefM.savefbName(userModel.getName());
            prefM.saveCompanyID(userModel.getCompanyId());
            prefM.saveCountry(userModel.getCountry());
            if (isValid(userModel.getPhoneNumber()))
                prefM.savePhoneNumber(userModel.getPhoneNumber());
            if (isValid(userModel.getImage())) {
                prefM.saveProfileImageURL(userModel.getImage());
                NetworkService.mCurrentUser.setImage(userModel.getImage());
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }
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
        defSetText(titleTxv, getResources().getString(R.string.sign_in));
        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/ActionBarCommonFont.otf");
        titleTxv.setTypeface(type);
        leftLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
