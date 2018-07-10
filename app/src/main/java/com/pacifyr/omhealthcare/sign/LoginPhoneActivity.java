package com.pacifyr.omhealthcare.sign;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ceino.utilitylibrary.CustomEditText;
import com.ceino.utilitylibrary.model.pacifyr.MRoleModel;
import com.ceino.utilitylibrary.model.pacifyr.MUser;
import com.ceino.utilitylibrary.model.pacifyr.ModelPhoneLogin;
import com.ceino.utilitylibrary.utils.PrefManager;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.base.PacifyerActivity;
import com.pacifyr.omhealthcare.home.ConnectToPacifyrActivity;
import com.pacifyr.omhealthcare.home.DashboardActivity;
import com.pacifyr.omhealthcare.network.GsonUtils;
import com.pacifyr.omhealthcare.network.NetworkService;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.QBSession;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.sample.core.utils.Toaster;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import org.json.JSONObject;

import static com.ceino.utilitylibrary.network.UtilityNetworkService.ACCOUNT_KEY;
import static com.ceino.utilitylibrary.network.UtilityNetworkService.APP_ID;
import static com.ceino.utilitylibrary.network.UtilityNetworkService.AUTH_KEY;
import static com.ceino.utilitylibrary.network.UtilityNetworkService.AUTH_SECRET;
import static com.ceino.utilitylibrary.network.UtilityNetworkService.GLOBAL_URL;
import static com.pacifyr.omhealthcare.network.NetworkService.editProfileObject;

public class LoginPhoneActivity extends PacifyerActivity {

    public Bundle bundle;
    public String phoneNo;
    public ModelPhoneLogin modelPhoneLogin;
    public CustomEditText phone_tv;
    public String main_label="error",label="error";
    JSONObject userObject;
    private MUser userModel;
    private MRoleModel roleModel;
    private String jsonStrng;
    private Context context;

    private PrefManager prefM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fbLogout();

        setContentView(R.layout.activity_sign_in_code_verify);
        context = LoginPhoneActivity.this;
        prefM = PrefManager.getInstance(context);
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        bundle=getIntent().getExtras();
        phoneNo=bundle.getString("phone");

        loginUsingPhone();


    }

    @Override
    protected View getSnackbarAnchorView() {
        return findViewById(com.quickblox.sample.groupchatwebrtc.R.id.scrollView);
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
        try {
            String url = GLOBAL_URL + "login/phonenumber";
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, final JSONObject json, final AjaxStatus status) {
                    try {
                        modelPhoneLogin = GsonUtils.getInstance().gsonToModelPhoneLogin(json);
                        userModel = modelPhoneLogin.getUser();
                        prefM.saveCompanyID(userModel.getCompanyId());
                        jsonStrng = json.toString();
                        String logstat = modelPhoneLogin.getStatus().toString();
                        if (isValid(logstat)) {
                            if (logstat.equals("success")) {
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
                                }else{
                                    signInCreatedUser();
                                }
                            } else {
                                hideProgress();
                                setStatus("Login failed : \n,"+modelPhoneLogin.getMessage(), R.color.endcall);
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
                                }, 900);
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        hideProgress();
                        e.printStackTrace();
                        hideProgress();
                        setStatus("Login failed : \n,"+modelPhoneLogin.getMessage(), R.color.endcall);
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
                        }, 900);
                    }
                }
            };
            AQuery aq = new AQuery(LoginPhoneActivity.this);
            cb.header("Content-Type", "application/json");
            JSONObject postDataParams = new JSONObject();
            postDataParams.putOpt("phoneNumber",phoneNo);
           // postDataParams.putOpt("phoneNumber","+"+phoneNo);
            aq.post(url, postDataParams, JSONObject.class, cb);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            hideProgress();
            setStatus("Login failed : \n,"+modelPhoneLogin.getMessage(), R.color.endcall);
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
            }, 900);
        }
    }



    private void signInCreatedUser( ) {
        hideStatus();
        setLoading("Connecting");
        showProgress();
        final  String userID = userModel.getId();
        String mail = modelPhoneLogin.getUser().getEmail();
        Log.i("sigggghhhnnnn1","d:"+userID);
        Log.i("sigggghhhnnnn12","d:"+mail);
        final QBUser user = new QBUser(userID, userID);
        user.setEmail(mail);
        QBSettings.getInstance().init(LoginPhoneActivity.this, APP_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
        Log.i("sigggghhhnnnn123","d:"+user+"  "+ACCOUNT_KEY);
        QBAuth.createSession().performAsync(new QBEntityCallback<QBSession>() {
            @Override
            public void onSuccess(QBSession qbSession, Bundle bundle) {
                requestExecutor.signInUser(user, new com.pacifyr.omhealthcare.quickblox.utils.QBEntityCallbackImpl<QBUser>() {
                    @Override
                    public void onSuccess(QBUser qbUser, Bundle params) {
                        Log.i("sigggghhhnnnn3","d:"+user);
                        qbUser.setPassword(userID);
                        Log.d("jp", "LPA signInCreatedUser getPassword "+qbUser.getPassword());
                        try {
                            /**/
                                saveUserData(qbUser);
                                startLoginService(qbUser);
                                saveValuesToPrefM(context,modelPhoneLogin);
                                loginTheUser(qbUser);
                           }
                           catch (Exception e) {
                                hideProgress();
                                hideStatus();
                                Toast.makeText(LoginPhoneActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                                prefM.clearSharedAll();
                                fbLogout();
                                startActivity(new Intent(LoginPhoneActivity.this,SignActivity.class));
                                finish();
                        }
                    }

                    @Override
                    public void onError(QBResponseException responseException) {
                        Log.i("sigggghhhnnnn4","d:"+responseException);
                        hideProgress();
                        Toaster.longToast("User signInUser failed!"+ responseException.getMessage());
                        Toast.makeText(LoginPhoneActivity.this, "User signInUser failed!" + responseException.getMessage(),
                                Toast.LENGTH_LONG).show();
                        prefM.clearSharedAll();
                        fbLogout();
                        startActivity(new Intent(LoginPhoneActivity.this,SignActivity.class));
                        finish();
                    }
                });
            }

            @Override
            public void onError(QBResponseException e) {
                hideProgress();
                Toast.makeText(LoginPhoneActivity.this, "QB sessron create failure !" + e.getMessage(),Toast.LENGTH_LONG).show();
                prefM.clearSharedAll();
                fbLogout();
                startActivity(new Intent(LoginPhoneActivity.this,SignActivity.class));
                finish();
            }
        });

    }

    private void loginTheUser(final QBUser qbUser) {
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
                        String qbID = qbUser.getId().toString();
                        prefM.saveQBID(qbID);
                        editProfileObject=new JSONObject();
                        editProfileObject.putOpt("qbid", Long.valueOf(qbID));
                        //NetworkService.editProfileObject.putOpt("online", true);
                        hideProgress();
                        setStatus("Login Success ", R.color.md_green_400);
                        showStatus();

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
            ConnectToPacifyrActivity.start(context);
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
            if (isValid(userModel.getImage())) {
                prefM.saveProfileImageURL(userModel.getImage());
                NetworkService.mCurrentUser.setImage(userModel.getImage());
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
