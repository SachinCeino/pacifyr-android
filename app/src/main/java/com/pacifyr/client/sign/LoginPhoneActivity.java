package com.pacifyr.client.sign;

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
import com.pacifyrapp.utilitylibrary.CustomEditText;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MRoleModel;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MUser;
import com.pacifyrapp.utilitylibrary.model.pacifyr.ModelPhoneLogin;
import com.pacifyrapp.utilitylibrary.model.pacifyr.ModelUserProfile;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.facebook.login.LoginManager;
import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;
import com.pacifyr.client.R;
import com.pacifyr.client.base.PacifyrActivity;
import com.pacifyr.client.home.DashboardActivity;
import com.pacifyr.client.network.GsonUtils;
import com.pacifyr.client.network.NetworkService;
import com.pacifyr.client.utils.AmazonSNSTokenHandler;
import com.pacifyr.client.utils.FirebaseLogging;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.QBSession;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import org.json.JSONObject;

import java.util.HashMap;

import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.ACCOUNT_KEY;
import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.APP_ID;
import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.AUTH_KEY;
import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.AUTH_SECRET;
import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.GLOBAL_URL;
import static com.pacifyr.client.network.NetworkService.editProfileObject;

public class LoginPhoneActivity extends PacifyrActivity {

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
            postDataParams.putOpt("phoneNumber",phoneNo);
           // postDataParams.putOpt("phoneNumber","+"+phoneNo);
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


    private void quickbloxNewLogin(final QBUser qbUser) {
        try {
            QBSettings.getInstance().init(LoginPhoneActivity.this, APP_ID, AUTH_KEY, AUTH_SECRET);
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
                            getPrefManager(LoginPhoneActivity.this).saveQBID(qbUser.getId().toString());
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
                                        loginTheUser(qbUser);


                                    }
                                }, 2000);

                            } else/* if (logstat.equals("error"))*/ {
                                Toast.makeText(LoginPhoneActivity.this, "Error :" + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    } catch (Exception eee) {
                        hideProgress();
                        Toast.makeText(LoginPhoneActivity.this, "Error :" + eee.getMessage(), Toast.LENGTH_LONG).show();
                        eee.printStackTrace();
                    }
                }
            };
            //AQuery aq = new AQuery(RegisterActivity.this);
            //cb.header("AccessToken", accessToken);
            //cb.header("enctype", "multipart/form-data");
            JSONObject postDataParams = new JSONObject();
            postDataParams.putOpt("qbid", Long.valueOf(qbUser.getId().toString()));
            postDataParams.putOpt("phoneNumber", phoneNo);
            String json = postDataParams.toString();
            HashMap<String, JSONObject> map = new HashMap<String, JSONObject>();
            map.put("data", postDataParams);
            Ion.with(context).load(url).setHeader("AccessToken", prefM.getAccessToken()).setHeader("enctype", "multipart/form-data").setMultipartParameter("data", json).asJsonObject().setCallback(futureCallback);

        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();

        }
    }

    private void signInCreatedUser( ) {
        hideStatus();
        setLoading("Connecting");
        showProgress();
        final  String userID = userModel.getId();
        String mail = modelPhoneLogin.getUser().getEmail();
        final QBUser user = new QBUser(userID, userID);
        user.setEmail(mail);
        if (isValid(modelPhoneLogin.getUser().getFirstName()))
        {
            user.setFullName(modelPhoneLogin.getUser().getFirstName());

        }
        QBSettings.getInstance().init(LoginPhoneActivity.this, APP_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
        QBAuth.createSession().performAsync(new QBEntityCallback<QBSession>() {
            @Override
            public void onSuccess(QBSession qbSession, Bundle bundle) {
                requestExecutor.signInUser(user, new com.pacifyr.client.quickblox.utils.QBEntityCallbackImpl<QBUser>() {
                    @Override
                    public void onSuccess(QBUser qbUser, Bundle params) {
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
                               Intent intent = new Intent(LoginPhoneActivity.this, SignActivity.class);
                               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                               startActivity(intent);
                                finish();
                        }
                    }

                    @Override
                    public void onError(QBResponseException responseException) {
                        hideProgress();
                        if (responseException.getHttpStatusCode() == 401) {
                            /*setAlertOKStatusInRed("Login failed", "You are not registered in our system. Please register.", "OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(LoginPhoneActivity.this, SignActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            });*/
                            saveValuesToPrefM(context, modelPhoneLogin);
                            quickbloxNewLogin(user);
                        } else {
                            prefM.clearSharedAll();
                            fbLogout();
                            setAlertOKStatusInRed("Login failed", responseException.getMessage(), "OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(LoginPhoneActivity.this, SignActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }

                        /*Toaster.longToast("User signInUser failed!"+ responseException.getMessage());
                        Toast.makeText(LoginPhoneActivity.this, "User signInUser failed!" + responseException.getMessage(),
                                Toast.LENGTH_LONG).show();*/
                        /*startActivity(new Intent(LoginPhoneActivity.this,SignActivity.class));
                        finish();*/
                    }
                });
            }

            @Override
            public void onError(QBResponseException e) {
                hideProgress();
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
                    Toast.makeText(LoginPhoneActivity.this, "QB session create failure !" + e.getMessage(), Toast.LENGTH_LONG).show();
                    prefM.clearSharedAll();
                    fbLogout();
                    Intent intent = new Intent(LoginPhoneActivity.this, SignActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
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

}
