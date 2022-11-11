package com.pacifyr.client.home;

import static com.pacifyr.client.twilio.VideoActivity.isCallActive;
import static com.pacifyr.client.twiliochat.TwilioConversationActivity.DEFAULT_CONVERSATION_NAME;
import static com.pacifyr.client.utils.AmazonSNSTokenHandler.installationIsSuccess;
import static com.pacifyr.client.utils.Constants.getCountryChecks;
import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.GLOBAL_URL;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.multidex.MultiDex;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.gson.Gson;
import com.pacifyr.client.Payment.PaymentFragment;
import com.pacifyr.client.R;
import com.pacifyr.client.activity.TermsOfUseActivity;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.event.ActionbarEvent;
import com.pacifyr.client.event.AlertTypeEvent;
import com.pacifyr.client.event.CallEvent;
import com.pacifyr.client.event.ChatEvent;
import com.pacifyr.client.event.InternetConnectionEvent;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.event.RateSessionEvent;
import com.pacifyr.client.fragment.AppointmentsFragment;
import com.pacifyr.client.fragment.AppointmentsTabViewFragment;
import com.pacifyr.client.fragment.CouponCodeFragment;
import com.pacifyr.client.fragment.MessagesFragment;
import com.pacifyr.client.fragment.MySessionFragment;
import com.pacifyr.client.fragment.PrescriptionListFragment;
import com.pacifyr.client.fragment.ProvidersListFragment;
import com.pacifyr.client.fragment.ResourcesFragment;
import com.pacifyr.client.fragment.SupportFragment;
import com.pacifyr.client.fragment.UserDashboardFragmentNew;
import com.pacifyr.client.fragment.WelcomeTourFragment;
import com.pacifyr.client.home.account.AccountEditActivity;
import com.pacifyr.client.home.account.AccountFragment;
import com.pacifyr.client.model.CallStatus;
import com.pacifyr.client.model.Session;
import com.pacifyr.client.model.interfaces.CallChatEventListner;
import com.pacifyr.client.netconnectionlistener.CheckNetworkJob;
import com.pacifyr.client.netconnectionlistener.ConnectionCheckBroadcast;
import com.pacifyr.client.network.GsonUtils;
import com.pacifyr.client.network.NetworkService;
import com.pacifyr.client.orginals.Auth;
import com.pacifyr.client.orginals.DynamicLink;
import com.pacifyr.client.orginals.RemoteConfig;
import com.pacifyr.client.receiver.NotificationUtils;
import com.pacifyr.client.sign.CrisisActivity;
import com.pacifyr.client.twilio.VideoActivity;
import com.pacifyr.client.twiliochat.QuickstartConversationsManager;
import com.pacifyr.client.twiliochat.QuickstartConversationsManagerListener;
import com.pacifyr.client.twiliochat.TokenResponseListener;
import com.pacifyr.client.twiliochat.TwilioCoversationListActivity;
import com.pacifyr.client.twiliochat.TwilioMainConversationsClient;
import com.pacifyr.client.utility.FCMMasterToken;
import com.pacifyr.client.utility.PacifyrAppCompactActivity;
import com.pacifyr.client.utils.AmazonSNSTokenHandler;
import com.pacifyr.client.utils.Constants;
import com.pacifyr.client.utils.FirebaseLogging;
import com.pacifyr.client.utils.LogUtils;
import com.pacifyr.client.utils.PagerEnabledSlidingPaneLayout;
import com.pacifyr.client.utils.SnackUtils;
import com.pacifyr.client.utils.ToastHandler;
import com.pacifyr.client.utils.Utility;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;
import com.pacifyrapp.utilitylibrary.Dialog;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MRoleModel;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MUser;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;
import com.twilio.conversations.ConversationsClient;
import com.twilio.conversations.Message;
import com.twilio.conversations.StatusListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

//import com.github.javiersantos.appupdater.AppUpdater;


public class DashboardActivity extends PacifyrAppCompactActivity implements QuickstartConversationsManagerListener, BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = DashboardActivity.class.getSimpleName();
    private static final long BOTTOM_MENU_VISIBILITY_DELAY = 1000;
    boolean showingFirst = true;
    private Context context;
    public static boolean sessioSummaryPageShown=false;
    PagerEnabledSlidingPaneLayout mSlidingPaneLayout;
    boolean onceBackpressed =false;
    private PagerEnabledSlidingPaneLayout slidingPane;
    private LinearLayout leftPane;
    private LinearLayout detailLlay;
    private MainTextView detailNameCtxv;
    private MainTextView detailMailCtxv;
    private LinearLayout dashboardLlay;
    private DeviceFitImageView dashboardDimv;
    private MainTextView dashboardCtxv;
    private LinearLayout mysessionsLlay;
    private DeviceFitImageView mesessionsDimv;
    private MainTextView mysessionsCtxv;
    private LinearLayout paymentLlay;

    private LinearLayout connectToPacifyr;
    private LinearLayout browsePacifyr;

    private LinearLayout supportLlay;
    //private DeviceFitImageView earningsDimv;
    //private MainTextView earningsCtxv;
    private LinearLayout logoutLlay;
    private LinearLayout tutotial_llay;
    private RelativeLayout account_rlay;
    private DeviceFitImageView logoutDimv;
    private MainTextView logoutCtxv;
    private CircleImageView profile_cimv;

    private LinearLayout policyDocumentLlay;
    private LinearLayout crisisLlay;
    private MainTextView crisisCtxv;
    private DeviceFitImageView crisisDimv;
    private View crisisView;
    private MainTextView resourcesCtxv;
    private LinearLayout resourcesLlay;
    private DeviceFitImageView resourcesDimv;


    private LinearLayout messagesLlay;
    public LinearLayout mLinearLayoutAppointments;

    String mainLabel;
    String label;
    LinearLayout coupon_llay;
    View coupon_view;
    boolean isNeedToOpenAppointments=false;
    public static void start(Activity context,boolean isNeedToOpenAppointments) {
        Intent intent = new Intent(context, DashboardActivity.class);
        intent.putExtra("isNeedToOpenAppointments",isNeedToOpenAppointments);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_left);
        context.startActivity(intent);
    }
    // private PermissionsChecker checker;
    public static void start(Context context
    ) {
     /*   mSessionDetailRoot=null;
        isConnected=false;isMissed=false;isDeclined=false;isOpponenetConnected=false;isOppenentHangUP=false;*/
        Intent intent = new Intent(context, DashboardActivity.class);
        // intent.putExtra(Consts.EXTRA_IS_INCOMING_CALL, isIncomingCall);

        context.startActivity(intent);
    }
    public static void start(Activity activity) {
     /*   mSessionDetailRoot=null;
        isConnected=false;isMissed=false;isDeclined=false;isOpponenetConnected=false;isOppenentHangUP=false;*/
        Intent intent = new Intent(activity, DashboardActivity.class);
        // intent.putExtra(Consts.EXTRA_IS_INCOMING_CALL, isIncomingCall);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
        activity.finish();
    }


//    AppUpdater appUpdater;

    private QuickstartConversationsManager quickstartConversationsManager;
    @Override
    protected void onResume() {
        super.onResume();

        if (isValid(isCallActive) && isCallActive) {
            showSnackbarPopup();
        } else {
            dismissSnackbarPopup();
        }

        if (ConnectionCheckBroadcast.CheckInternetConnection(getApplicationContext()))
        {
            hideProgress();
            hideStatus2();
        }
        else
        {
            setAlertStatusInWhiteNoInternet("No Internet connection!", "Please turn on internet connection to continue.", "Exit", "Ok", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideProgress();
                    hideStatus2();
                    finish();
                }
            });
        }


        Bundle args =
                FirebaseLogging.getConstantArgsLoggedIn(DashboardActivity.this);

        args.putString(FirebaseLogging.EC, DashboardActivity.class.getSimpleName());

        FirebaseLogging.sendLog(DashboardActivity.this,
                FirebaseLogging.EVENT_ON_SCREEN,
                args);

        userDetailWeb(getUserID(), new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                try {
                    if (json != null) {
                        NetworkService.mCurrentUser = GsonUtils.getInstance().gsonToModelUserProfile(json);

                    } else {
                        LogUtils.LOGD("base", "bAF userDetailWeb json null  : " + status.getMessage());
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtils.LOGD("base", "bAF userDetailWeb callback exception : " + e.getMessage());
                }
            }
        });

        setProfileValues();

       /* if (isValid(bottomNavigationView))
        {
            bottomNavigationView.setSelectedItemId(R.id.bnm_dashboard);
            bottomNavigationView.setVisibility(View.VISIBLE);
        }*/
        LocalBroadcastManager.getInstance(this).registerReceiver(notificationReceiver, new IntentFilter(NotificationUtils.NOTIFY_INTENT_FOR_NEW_CHAT));
    }



    public void userDetailWeb(String id, AjaxCallback cb) {
        try {
            //showProgress();

            PrefManager prefM = PrefManager.getInstance(getApplicationContext());
            String token = prefM.getAccessToken();

            String url = GLOBAL_URL + "user/profile/" + id;


            AQuery aq = new AQuery(getApplicationContext());
            //cb.header("Content-Type", "application/json");
            cb.header("AccessToken", token);
            aq.ajax(url, JSONObject.class, cb);

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            LogUtils.LOGD("base", "bAF userDetailWeb  exception : " + ex.getMessage());
        }
    }


    @Subscribe
    public void onCallStatusChanged(CallStatus callStatus) {
        if (callStatus.isActive()) {
            showSnackbarPopup();
        } else {
            dismissSnackbarPopup();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VideoActivity.WalletBalnce=0;
        /*if (isValid(appUpdater))
        {
            appUpdater.stop();
        }*/

    }


    public static String CreatedDay = "";




    /*    public static showActionBar()
    {
        try {
            hideRightLayActionbar();
            showFragment(new UserDashboardFragment());
            defSetText(titleTxv,"Dashboard");
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }*/

    //https://pacifyrtest.s3.amazonaws.com/Q1514964254954.jpg
   /* private void setProfileValues() {
        try {
            detailNameCtxv.setText(getCurrentUserName());
            detailMailCtxv.setText(getCurrentUserMail());
            String imageurl=getCurrentUserImageUrl();
            LogUtils.LOGD("session","DA setProfileValues imageurl " + imageurl);

            if (isValid(imageurl)) {
                LogUtils.LOGD("session","DA setProfileValues loading imageurl " + imageurl);
                //   setAqueryImageUrl(profile_cimv,imageurl, R.drawable.placeholder_user);
                App.getPicasso(). load(imageurl).into(profile_cimv);

            }
        }catch (Exception e)
        {
            LogUtils.LOGD("session","DA setProfileValues exce " + e.getMessage());
        }
    }*/

    @Override
    protected View getSnackbarAnchorView() {
        return null;
    }

    @Override
    public void onSetValues() throws Exception {

    }

    @Subscribe
    public void onNotifyEvent(NotifyEvent event) {
        try {
            if (event.isSuccess)
                setProfileValues();
        } catch (Exception e) {
            LogUtils.LOGD("session", "DA onNotifyEvent exce " + e.getMessage());
        }

    }

    @Subscribe
    public void onAlertTypeEvent(AlertTypeEvent event) {

    }

    @Subscribe
    public void onActionbarEvent(ActionbarEvent event) {
        try {
            defSetText(titleTxv, "MY DASHBOARD");
        } catch (Exception e) {
            LogUtils.LOGD("session", "DA onActionbarEvent exce " + e.getMessage());
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
    private LinearLayout mLinearLayoutPrescription;
    private CircleImageView connectProviderActioBar;

    public void onSetActionBar() {
        actionBarTopLlay = findViewById(R.id.action_bar_top_llay);
        bottomPanel = findViewById(R.id.bottom_panel);
        leftLlay = findViewById(R.id.left_llay);
        backImv = findViewById(R.id.back_imv);
        leftTxv = findViewById(R.id.left_txv);
        titleTxv = findViewById(R.id.title_txv);
        titleTxv.setAllCaps(true);
        rightLlay = findViewById(R.id.right_llay);
        rightTxv = findViewById(R.id.right_txv);
        rightImv = findViewById(R.id.right_imv);
        mLinearLayoutPrescription = findViewById(R.id.mLinearLayoutPrescription);
        rightImv.setVisibility(View.INVISIBLE);
        rightTxv.setVisibility(View.INVISIBLE);
        rightLlay.setVisibility(View.INVISIBLE);
        leftTxv.setVisibility(View.INVISIBLE);
        backImv.setVisibility(View.VISIBLE);
        titleTxv.setVisibility(View.VISIBLE);
        leftLlay.setVisibility(View.VISIBLE);
        defSetText(titleTxv, getResources().getString(R.string.mydashboard));
        connectProviderActioBar=findViewById(R.id.connectProviderActioBar);
        Typeface type = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.avantica_bold_header));
        titleTxv.setTypeface(type);
        titleTxv.setTextColor(getResources().getColor(R.color._color_action_bar_text_color));
        backImv.setImageDrawable(getResources().getDrawable(R.drawable.leftmen));
        //backImv.setBackgroundResource(R.drawable.leftmen);
        leftLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closePane(mSlidingPaneLayout.isOpen());
            }
        });
        showRightLayActionbar();
        connectProviderActioBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isInternetAvailable()) {
                    showInternetNotAvailablePopup();
                    return;
                }
                account_rlay.performClick();

              /*  *//**actionbar connectprovider from dashboard actionbar event **//*
                if (connectProviderActioBar.getText().equals("Connect"))
                EventBus.getDefault().post(new ConnectEvent(true));
                *//*if (connectProviderActioBar.getText().equals("Add"))
                {
                    Intent i=new Intent(getApplicationContext(), TermsOfUseActivity.class);
                    i.putExtra("spurl", Constants.ADD_RESOURCES_LINK);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }*/
            }
        });

    }
    public void closePane(boolean close){
        if(close){
            mSlidingPaneLayout.closePane();
        }else{
            mSlidingPaneLayout.openPane();
        }
    }
    private void showRightLayActionbar() {

        try {
            rightTxv.setVisibility(View.VISIBLE);
            rightLlay.setVisibility(View.VISIBLE);
            rightImv.setVisibility(View.INVISIBLE);
            defSetText(rightTxv, getResources().getString(R.string.edit));
            rightTxv.setTextColor(getResources().getColor(R.color._color_action_bar_text_color));
            rightLlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isInternetAvailable()) {
                        showInternetNotAvailablePopup();
                        return;
                    }
                    Intent intent = new Intent(DashboardActivity.this, AccountEditActivity.class);
                    //  intent.putExtra("display","Pacifyr");
                    startActivity(intent);
                }
            });

        } catch (Exception e) {
            LogUtils.LOGD("session", "DA showRightLayActionbar exce " + e.getMessage());
        }

    }

    public void hideRightLayActionbar() {

        try {
            rightTxv.setVisibility(View.INVISIBLE);
            rightLlay.setVisibility(View.INVISIBLE);
            rightImv.setVisibility(View.INVISIBLE);


        } catch (Exception e) {
            LogUtils.LOGD("session", "DA hideRightLayActionbar exce " + e.getMessage());
        }

    }
    UserDashboardFragmentNew userDashboardFragment = null;
    boolean dashBoardVisibility=true;
    public void showFragment() {
        FragmentManager fm = DashboardActivity.this.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        if (userDashboardFragment==null){
            userDashboardFragment=new UserDashboardFragmentNew();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_main_content_fragment, userDashboardFragment).commit();

        dashBoardVisibility=true;
    }

    public void showFragment(Fragment fragment) {
        try {
            closePane(true);
        }catch (Exception e)
        {

        }

        String backStateName = fragment.getClass().getName();

        FragmentManager fragmentManager = getSupportFragmentManager();
        boolean fragmentPopped = fragmentManager.popBackStackImmediate (backStateName, 0);

        if (!fragmentPopped){ //fragment not in back stack, create it.
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();
            fragmentTransaction.replace(R.id.activity_main_content_fragment,fragment);
            fragmentTransaction.addToBackStack(backStateName);
            fragmentTransaction.commit();

        }
    }

    public void showSearchPacifyrs(View view) {
        Intent i = new Intent(this, ListPacifyrScreenActivity.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        if (mSlidingPaneLayout.isOpen()) {
            mSlidingPaneLayout.closePane();
        } else {

           //if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            //   getSupportFragmentManager().popBackStackImmediate();
              // Fragment fragmentCurrent = getSupportFragmentManager().findFragmentById(R.id.activity_main_content_fragment);
               if(bottomNavigationView.getVisibility()==View.VISIBLE){
                   if (!onceBackpressed) {
                       ToastHandler.newInstance(this).mustShowToast("" + getText(R.string.press_back_again_to_exit));
                       onceBackpressed = true;
                       Handler handler = new Handler();
                       handler.postDelayed(new Runnable() {
                           @Override
                           public void run() {
                               onceBackpressed = false;
                           }
                       }, 5000);
                   } else {

                       finishAffinity();
                       finish();

                   }
               }


        }
    }

    /*public void showQuickblox(View view) {
        Intent i=new Intent(this, VideoChatActivity.class);
        startActivity(i);
    }*/

    @Subscribe
    public void onChatEvent(ChatEvent event) {
        try {
            LogUtils.LOGD("session", "CPA onCallEvent called ");
            if (!isValid(event.mpacifyer, "Network Error"))
                return;
            startChat(event.mpacifyer);


        } catch (Exception e) {
            LogUtils.LOGD("session", "CPA ChatEvent exce " + e.getMessage());
        }

    }


    public CallChatEventListner callchatEventListner = new CallChatEventListner() {
        @Override
        public void onCallEventClick(CallEvent event) {

            //onCallEvent(event);
        }

        @Override
        public void onChatEventClick(ChatEvent event) {
            onChatEvent(event);
        }

        @Override
        public void onException(Exception e) {

        }

        @Override
        public void onError(String e) {

        }
    };
    public static String CreatedYear = "";



    private String AppointMentId = null;

    LinearLayout LinearLayout_Active_Session;
    BottomNavigationView bottomNavigationView;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.LOGD("install", "DashboardActivity");
        configPushNotifications();
        setContentView(R.layout.activity_dashboard);
        hideKeyboard();
        if (!installationIsSuccess)
        {
            new AmazonSNSTokenHandler(getBaseContext()).register();
        }
        context = DashboardActivity.this;
        //    permissionCheck();
        /**the library will work in background for checking new update available or not**/
        /*appUpdater = new AppUpdater(this);
        appUpdater.start();*/

        connectToPacifyr = findViewById(R.id.connect_pacifyr_llay);
        leftPane = findViewById(R.id.left_pane);
        account_rlay = findViewById(R.id.account_rlay);
        detailLlay = findViewById(R.id.detail_llay);
        detailNameCtxv = findViewById(R.id.detail_name_ctxv);
        detailMailCtxv = findViewById(R.id.detail_mail_ctxv);
        dashboardLlay = findViewById(R.id.dashboard_llay);
        dashboardDimv = findViewById(R.id.dashboard_dimv);
        profile_cimv = findViewById(R.id.profile_cimv);
        dashboardCtxv = findViewById(R.id.dashboard_ctxv);
        mysessionsLlay = findViewById(R.id.mysessions_llay);
        mesessionsDimv = findViewById(R.id.mesessions_dimv);
        mysessionsCtxv = findViewById(R.id.mysessions_ctxv);
        paymentLlay = findViewById(R.id.payment_llay);
        browsePacifyr = findViewById(R.id.browse_llay);
        browsePacifyr.setVisibility(View.GONE);
        tutotial_llay = findViewById(R.id.tutotial_llay);
        supportLlay = findViewById(R.id.support_llay);
        policyDocumentLlay = findViewById(R.id.policy_document_llay);

        logoutLlay = findViewById(R.id.logout_llay);
        logoutDimv = findViewById(R.id.logout_dimv);
        logoutCtxv = findViewById(R.id.logout_ctxv);

        crisisCtxv = findViewById(R.id.crisis_ctxv);
        crisisDimv = findViewById(R.id.crisis_dimv);
        crisisLlay = findViewById(R.id.crisis_llay);
        crisisView = findViewById(R.id.crisis_view);


        resourcesCtxv = findViewById(R.id.resourses_ctxv);
        resourcesDimv = findViewById(R.id.resourses_dimv);
        resourcesLlay = findViewById(R.id.resourses_llay);
        resourcesLlay.setVisibility(View.GONE);


        messagesLlay = findViewById(R.id.message_llay);
        messagesLlay.setVisibility(View.GONE);
        coupon_llay = findViewById(R.id.coupon_llay);
        coupon_view = findViewById(R.id.coupon_view);
        mLinearLayoutAppointments = findViewById(R.id.mLinearLayoutAppointments);





        //NetworkService.startActionCutomerDashboard(this);
        showFragment(new UserDashboardFragmentNew());
        MultiDex.install(this);
        onSetActionBar();
        hideRightLayActionbar();
        //menu_llay=(LinearLayout) findViewById(R.id.menu_llay);
        PrefManager prefM = getPrefManager();
        String name=prefM.getUserFirstName();
        String key = prefM.getAccessToken();
        String mail=prefM.geteMail();
        String json=prefM.getJsonKey();
        String country=prefM.getCountry();


        if(country.equals("US")){
            crisisLlay.setVisibility(View.VISIBLE);
            crisisView.setVisibility(View.VISIBLE);
        }else {
            crisisLlay.setVisibility(View.GONE);
            crisisView.setVisibility(View.GONE);

        }

        setProfileValues();


        mSlidingPaneLayout = findViewById(R.id.sliding_pane);
        mSlidingPaneLayout.setSliderFadeColor(getResources().getColor(android.R.color.transparent));
        mSlidingPaneLayout.setParallaxDistance(480);
        String type=null;
        if (isValid(getIntent()) && isValid(getIntent().getExtras())&&isValid(getIntent().getExtras().get("type"))) {
            type = getIntent().getExtras().get("type").toString();
        }
        if (isValid(getIntent()) && isValid(getIntent().getExtras())&&isValid(getIntent().getExtras().get("typeId"))) {
            AppointMentId = getIntent().getExtras().get("typeId").toString();
        }

        if(isValid(type) && type.equals("reminder"))
        {
            if (isValid(AppointMentId))
            {
                webGetAppointmentDetails(AppointMentId);
            }
            else
            {
                hideRightLayActionbar();
                showFragment(new AppointmentsFragment());
                defSetText(titleTxv, getResources().getString(R.string.appointments));
            }
        }

        mSlidingPaneLayout.setPanelSlideListener(new PagerEnabledSlidingPaneLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelOpened(View panel) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                hideSoftKeyboard();
            }

            @Override
            public void onPanelClosed(View panel) {

            }
        });

        dashboardLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isInternetAvailable()) {
                    showInternetNotAvailablePopup();
                    return;
                }
                hideRightLayActionbar();
                closePane(true);
                showFragment(new UserDashboardFragmentNew());
            }
        });
        account_rlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isInternetAvailable()) {
                    showInternetNotAvailablePopup();
                    return;
                }
                showRightLayActionbar();
                showFragment(new AccountFragment());
                if (isValid(bottomNavigationView))
                {
                    bottomNavVisibility(false);
                }
            }
        });
        connectToPacifyr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isInternetAvailable()) {
                    showInternetNotAvailablePopup();
                    return;
                }
                hideRightLayActionbar();
                closePane(true);
                ConnectToPacifyrActivity.start(context);
                /*Intent intent=new Intent(getActivity(), ConnectToPacifyrActivity.class);
                  intent.putExtra("display","Pacifyr");
                  startActivity(intent);*/
                //defSetText(titleTxv,"Connect to Pacifyr");
            }
        });
        tutotial_llay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isInternetAvailable()) {
                    showInternetNotAvailablePopup();
                    return;
                }
                hideRightLayActionbar();
                showFragment(new WelcomeTourFragment());
                if (isValid(bottomNavigationView))
                {
                    bottomNavVisibility(false);
                }

            }
        });
        browsePacifyr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isInternetAvailable()) {
                    showInternetNotAvailablePopup();
                    return;
                }
                hideRightLayActionbar();
                closePane(true);
                Intent i=new Intent(context, ListPacifyrScreenActivity.class);
                startActivity(i);
            }
        });

        if (getCountryChecks().contains(getPrefManager().getCountry()))
            paymentLlay.setVisibility(View.GONE);
        else
            paymentLlay.setVisibility(View.VISIBLE);


        paymentLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isInternetAvailable()) {
                    showInternetNotAvailablePopup();
                    return;
                }
                hideRightLayActionbar();
                showFragment(new PaymentFragment());
                if (isValid(bottomNavigationView))
                {
                    bottomNavVisibility(false);
                }

            }
        });

        messagesLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isInternetAvailable()) {
                    showInternetNotAvailablePopup();
                    return;
                }
                hideRightLayActionbar();
                closePane(true);
                //isFromSlideMenu true for subscription check validation
                boolean isFromSlideMenu=true;
                TwilioCoversationListActivity.start(context,isFromSlideMenu);
                /*showFragment(new PacifyrMessageFragment());*/
                //defSetText(titleTxv,"");

            }
        });

        policyDocumentLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isInternetAvailable()) {
                    showInternetNotAvailablePopup();
                    return;
                }
                hideRightLayActionbar();
                closePane(true);
                PolicyDocumentActivity.start(context);
                /*  showFragment(new PacifyrMessageFragment());*/
            }
        });

        mysessionsLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isInternetAvailable()) {
                    showInternetNotAvailablePopup();
                    return;
                }
                hideRightLayActionbar();
                showFragment(new MySessionFragment());
                if (isValid(bottomNavigationView))
                {
                    bottomNavVisibility(false);
                }

            }
        });

        supportLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isInternetAvailable()) {
                    showInternetNotAvailablePopup();
                    return;
                }
                hideRightLayActionbar();
                showFragment(new SupportFragment());
                if (isValid(bottomNavigationView))
                {
                    bottomNavVisibility(false);
                }
            }
        });

        crisisLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isInternetAvailable()) {
                    showInternetNotAvailablePopup();
                    return;
                }
                hideRightLayActionbar();
                closePane(true);
                Intent intent = new Intent(DashboardActivity.this,CrisisActivity.class);
                startActivity(intent);
                /*hideRightLayActionbar();
                showFragment(new CrisisFragment());
                defSetText(titleTxv, getResources().getString(R.string.crisis_info));*/
            }
        });

        resourcesLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isInternetAvailable()) {
                    showInternetNotAvailablePopup();
                    return;
                }
                hideRightLayActionbar();
                showFragment(new ResourcesFragment());

            }
        });

        logoutLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               closePane(true);
                if (!isInternetAvailable()) {
                    showInternetNotAvailablePopup();
                    return;
                }
                hideRightLayActionbar();
                setAlertStatusInBlue("Logout", getResources().getString(R.string.signout_message), new Dialog.onClick() {
                    @Override
                    public void onAction(Dialog d) {
                        hideStatus();
                        try {
                            if (TwilioMainConversationsClient.getInstance()!=null)
                            {
                                String token = FCMMasterToken.getFCMTokenFromUTility(context);
                                TwilioMainConversationsClient.getInstance().unregisterFCMToken(new ConversationsClient.FCMToken(token), new StatusListener() {
                                    @Override
                                    public void onSuccess() {
                                        Log.w("EEEE","www");
                                    }
                                });
                                TwilioMainConversationsClient.mConversationsClient=null;
                            }
                        }
                        catch (Exception e)
                        {
                            TwilioMainConversationsClient.mConversationsClient=null;
                            Log.w("EEEE",e.toString());
                        }

                            /*PrefManager prefM= PrefManager.getInstance(getActivity());
                            prefM.clearSharedAll();*/
                        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        if (mNotificationManager != null) {
                            mNotificationManager.cancelAll();
                        }
                        deleteInstallationWeb();

                    }
                });
                //Toast.makeText(getActivity(),"Logout",Toast.LENGTH_LONG).show();
            }
        });

        mLinearLayoutAppointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isInternetAvailable()) {
                    showInternetNotAvailablePopup();
                    return;
                }
                showAppointmentPage();
                if (isValid(bottomNavigationView))
                {
                    bottomNavVisibility(false);
                }
            }
        });


        if (!isValid(FCMMasterToken.getFCMTokenFromUTility(context)))
        {
            FirebaseInstallations.getInstance().getToken(true).addOnCompleteListener(task -> {
                // get token
                String tok = task.getResult().getToken();
                Log.w("FIREBASETOKEN_dash", tok);
                PrefManager.getInstance(context).putSharedString(PrefManager.FCM_TOKEN,tok);
                new FCMMasterToken(context,tok);
               // new AmazonSNSTokenHandler(context).register();
                if (!isValid(getPrefManager().getTWILIOAccessToken()))
                {
                    quickstartConversationsManager = new QuickstartConversationsManager();
                    retrieveTokenFromServer();
                }

            });


        }
        quickstartConversationsManager = new QuickstartConversationsManager();
        retrieveTokenFromServer();



        /**
         * network connectivity check JobScheduler
         *  **/
        @SuppressLint("MissingPermission")
        JobInfo myJob = new JobInfo.Builder(0, new ComponentName(this, CheckNetworkJob.class))
                .setMinimumLatency(1000)
                .setOverrideDeadline(2000)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .build();

        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(myJob);

        coupon_llay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isInternetAvailable()) {
                    showInternetNotAvailablePopup();
                    return;
                }
                hideRightLayActionbar();
                showFragment(new CouponCodeFragment());
                if (isValid(bottomNavigationView))
                {
                    bottomNavVisibility(false);
                }
            }
        });

        mLinearLayoutPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isInternetAvailable()) {
                    showInternetNotAvailablePopup();
                    return;
                }
                hideRightLayActionbar();
                showFragment(new PrescriptionListFragment());
                if (isValid(bottomNavigationView))
                {
                    bottomNavVisibility(false);
                }
            }
        });

        if (isValid(getIntent()) && isValid(getIntent().getExtras())&&isValid(getIntent().getExtras().getBoolean("isNeedToOpenAppointments",false))) {
            isNeedToOpenAppointments = getIntent().getExtras().getBoolean("isNeedToOpenAppointments",false);
            if (isValid(isNeedToOpenAppointments) && isNeedToOpenAppointments)
            {
                hideRightLayActionbar();
                bottomNavVisibility(false);
                showFragment(new AppointmentsTabViewFragment());
                defSetText(titleTxv, getResources().getString(R.string.appointments));
            }
        }
        checkAndApplyCode();
        Browser(DynamicLink.link(this),true);
        String s=DynamicLink.getScreenFromNotification();
        if(s!=null && !s.equals(""))Browser(s,false);
        int realTimeLogging = PrefManager.getInstance(DashboardActivity.this).getSharedInteger(Constants.KEY_REALTIME_LOGGING, -1);
        if (realTimeLogging == 0) {
            Utility.getRealTimeLogging(DashboardActivity.this);
        }




        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        //bottomNavigationView.getMenu().getItem(0).setChecked(true);

    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.bnm_dashboard:
                if (!isInternetAvailable()) {
                    showInternetNotAvailablePopup();
                    return false;
                }
                hideRightLayActionbar();
                showFragment(new UserDashboardFragmentNew());
                return true;
            case R.id.bnm_resources:
            if (!isInternetAvailable()) {
                showInternetNotAvailablePopup();
                return false;
            }
            hideRightLayActionbar();
            showFragment(new ResourcesFragment());
                return true;
            case R.id.bnm_messages:
                if (!isInternetAvailable()) {
                    showInternetNotAvailablePopup();
                    return false;
                }
                hideRightLayActionbar();
                showFragment(new MessagesFragment());
                return true;

            case R.id.bnm_providers:
                if (!isInternetAvailable()) {
                    showInternetNotAvailablePopup();
                    return false;
                }
                hideRightLayActionbar();
                showFragment(new ProvidersListFragment());
                return true;

        }
        return false;
    }

    private void retrieveTokenFromServer() {
        quickstartConversationsManager.retrieveAccessTokenFromServer(this, "identity", new TokenResponseListener() {
            @Override
            public void receivedTokenResponse(boolean success, @Nullable Exception exception, String token) {
                if (success) {
                    getPrefManager().saveTwilioAccessToken(token);
                    quickstartConversationsManager.initializeWithAccessToken(getApplicationContext(),token);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                }
                else {
                    String errorMessage = getString(R.string.error_retrieving_access_token);
                    if (exception != null) {
                        errorMessage = errorMessage + " " + exception.getLocalizedMessage();
                    }

                }
            }
        });
    }


    public void showAppointmentPage()
    {
        bottomNavVisibility(false);
        hideRightLayActionbar();
        showFragment(new AppointmentsTabViewFragment());
        defSetText(titleTxv, getResources().getString(R.string.appointments));

    }
    private void Browser(String screeName,boolean isLink){
        switch (screeName){
            case "PrescriptionListFragment":
                mLinearLayoutPrescription.callOnClick();
                break;
            case "AppointmentsTabViewFragment":
                mLinearLayoutAppointments.callOnClick();
                break;
            case "TermsOfUseActivity": {
                String spurl = "", title="";

                    try {
                        JSONObject j =  new JSONObject(isLink?RemoteConfig.Config().getString(screeName):
                                DynamicLink.getScreenDataFromNotification());
                        spurl = j.getString("url");
                        title = j.getString("title");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                if (spurl.equals("")) {
                    resourcesLlay.callOnClick();
                } else {
                    Intent i = new Intent(this, TermsOfUseActivity.class);
                    i.putExtra("spurl", spurl);
                    i.putExtra("title", title);
                    startActivity(i);
                }
            }
                break;
            case "CouponCodeFragment": {
                String code = "";
                try {
                    JSONObject j = new JSONObject(isLink ? RemoteConfig.Config().getString(screeName) : DynamicLink.getScreenDataFromNotification());
                    code = j.getString("code");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hideRightLayActionbar();
                showFragment(CouponCodeFragment.setCoupen(code));
            }
                break;
            case "ListPacifyrScreenActivity":{
                browsePacifyr.callOnClick();
            }
                break;
            default:
                LogUtils.LOGD(TAG,"deepLink returned no action screenName : "+screeName);
        }
    }

    private void setProfileValues() {
        try {
            PrefManager prefM = PrefManager.getInstance(DashboardActivity.this);
            String token = prefM.getAccessToken();
            String userid = prefM.getUserID();
            String url = GLOBAL_URL + "user/" + userid;
            setLoading();
            showProgress();
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    Auth.httpStatusFilter(getApplicationContext(), status.getCode());
                    try {
                        hideProgress();
                        MUser user  = GsonUtils.getInstance().gsonToMUser(json);
                        Utility.getUserCreatedDayMonthYear(user.getCreatedAt());
                        detailNameCtxv.setText(user.getName());
                        detailMailCtxv.setText(user.getEmail());
                        String imageUrl ="";
                        if (isValid(user.getImageUrl()))
                            imageUrl=user.getImageUrl();
                        if(isValid(imageUrl)) {
                            Picasso.with(DashboardActivity.this).load(imageUrl).placeholder(R.drawable.placeholder_user).into(profile_cimv);
                            Picasso.with(DashboardActivity.this).load(imageUrl).placeholder(R.drawable.placeholder_user).into(connectProviderActioBar);
                        }else {
                            if (isValid(user.getImage()))
                                imageUrl=user.getImage();
                            if(isValid(imageUrl))
                                Picasso.with(DashboardActivity.this).load(imageUrl).placeholder(R.drawable.placeholder_user).into(profile_cimv);
                            Picasso.with(DashboardActivity.this).load(imageUrl).placeholder(R.drawable.placeholder_user).into(connectProviderActioBar);
                        }

                        MRoleModel roleModel = user.getRoleModel();
                        mainLabel = roleModel.getMainLabel();
                        label=roleModel.getLabel();
                        if (mainLabel.equals("customer"))
                        { if (label.equalsIgnoreCase("customer")) {
                            coupon_llay.setVisibility(View.VISIBLE);
                            coupon_view.setVisibility(View.VISIBLE);
                            PrefManager.getInstance(DashboardActivity.this).saveCustomerRole("customer");

                        } else if (label.equalsIgnoreCase("paidMember")) {
                            coupon_llay.setVisibility(View.VISIBLE);
                            coupon_view.setVisibility(View.VISIBLE);
                            PrefManager.getInstance(DashboardActivity.this).saveCustomerRole("paidMember");
                            }
                        else if (label.equalsIgnoreCase("employee")) {
                            coupon_llay.setVisibility(View.VISIBLE);
                            coupon_view.setVisibility(View.VISIBLE);
                            PrefManager.getInstance(DashboardActivity.this).saveCustomerRole("employee");
                        }
                            else
                        {
                            coupon_llay.setVisibility(View.GONE);
                            coupon_view.setVisibility(View.GONE);
                        }
                        }

                    } catch (Exception e) {
                        hideProgress();
                        //coupon_llay.setVisibility(View.GONE);
                       // coupon_view.setVisibility(View.GONE);
                        e.printStackTrace();
                    }try {
                        hideProgress();
                        MUser user  = GsonUtils.getInstance().gsonToMUser(json);
                        Utility.getUserCreatedDayMonthYear(user.getCreatedAt());
                        detailNameCtxv.setText(user.getName());
                        detailMailCtxv.setText(user.getEmail());
                        String imageUrl ="";
                        if (isValid(user.getImageUrl()))
                            imageUrl=user.getImageUrl();
                        if(isValid(imageUrl)) {
                            Picasso.with(DashboardActivity.this).load(imageUrl).placeholder(R.drawable.placeholder_user).into(profile_cimv);
                            Picasso.with(DashboardActivity.this).load(imageUrl).placeholder(R.drawable.placeholder_user).into(connectProviderActioBar);
                        }else {
                            if (isValid(user.getImage()))
                                imageUrl=user.getImage();
                            if(isValid(imageUrl))
                                Picasso.with(DashboardActivity.this).load(imageUrl).placeholder(R.drawable.placeholder_user).into(profile_cimv);
                            Picasso.with(DashboardActivity.this).load(imageUrl).placeholder(R.drawable.placeholder_user).into(connectProviderActioBar);
                        }

                        MRoleModel roleModel = user.getRoleModel();
                        mainLabel = roleModel.getMainLabel();
                        label=roleModel.getLabel();
                        if (mainLabel.equals("customer"))
                        { if (label.equalsIgnoreCase("customer")) {
                            coupon_llay.setVisibility(View.VISIBLE);
                            coupon_view.setVisibility(View.VISIBLE);
                            PrefManager.getInstance(DashboardActivity.this).saveCustomerRole("customer");

                        } else if (label.equalsIgnoreCase("paidMember")) {
                            coupon_llay.setVisibility(View.VISIBLE);
                            coupon_view.setVisibility(View.VISIBLE);
                            PrefManager.getInstance(DashboardActivity.this).saveCustomerRole("paidMember");
                            }
                        else if (label.equalsIgnoreCase("employee")) {
                            coupon_llay.setVisibility(View.VISIBLE);
                            coupon_view.setVisibility(View.VISIBLE);
                            PrefManager.getInstance(DashboardActivity.this).saveCustomerRole("employee");
                        }
                            else
                        {
                            coupon_llay.setVisibility(View.GONE);
                            coupon_view.setVisibility(View.GONE);
                        }
                        }

                    } catch (Exception e) {
                        hideProgress();
                        //coupon_llay.setVisibility(View.GONE);
                       // coupon_view.setVisibility(View.GONE);
                        e.printStackTrace();
                    }
                }
            };
            AQuery aq = new AQuery(DashboardActivity.this);
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", token);
            aq.ajax(url, JSONObject.class, cb);
        }catch (Exception e){
            hideProgress();
            e.printStackTrace();
        }
    }


    /**
     * set Action bar title for fragments whic are loading from Navigation Drawer menu
     **/

    public void changeActionBarTitleForFragments(String mActionBarTitle) {
        defSetText(titleTxv, mActionBarTitle);
    }

    public void changeBottomMenuIcon(int id) {
        if (isValid(bottomNavigationView)) {
            bottomNavigationView.getMenu().getItem(id).setChecked(true);
            bottomNavVisibility(true);

        }
    }

    public void bottomNavVisibility(boolean visible) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            bottomNavigationView.setVisibility(visible ? View.VISIBLE : View.GONE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        }, BOTTOM_MENU_VISIBILITY_DELAY);
    }


    public boolean isInternetAvailable = true;

    public boolean isInternetAvailable() {
        return isInternetAvailable;
    }

    public void setInternetAvailable(boolean internetAvailable) {
        isInternetAvailable = internetAvailable;
    }

    public void showInternetNotAvailablePopup()
    {
        setAlertStatusInWhiteNoInternet("No Internet connection!", "Please turn on internet connection to continue.", "Exit", "Ok", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideProgress();
                hideStatus2();
                finish();
            }
        });
    }

    @org.greenrobot.eventbus.Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetworkChanged(InternetConnectionEvent event) {
        try {
            if (isValid(event) && isShowDialogs()) {
                if (!event.isConnected())
                {
                    setInternetAvailable(false);
                    setAlertStatusInWhiteNoInternet("No Internet connection!", "Please turn on internet connection to continue.", "Exit", "Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            hideProgress();
                            hideStatus2();
                            finish();
                        }
                    });
                }
                else
                {
                    setInternetAvailable(true);
                    hideProgress();
                    hideStatus2();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @org.greenrobot.eventbus.Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewSessionRatingCame(RateSessionEvent event) {
        try {
            if (isValid(event) && isShowDialogs()) {
                if (isValid(event.getmSessionId()))
                {

                 // getUnratedSessions();

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getUnratedSessions() {
        AjaxCallback cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                /*
                 *this must be the first line of any httpCallBacks
                 */
                Auth.httpStatusFilter(DashboardActivity.this,status.getCode());
                try {
                    if (json != null) {
                        JSONArray results = json.getJSONArray("results");
                        if (results != null && results.length() > 0) {
                            Session session = new Gson().fromJson(results.getJSONObject(0).toString(), Session.class);
                            if (session != null) {
                                if (sessioSummaryPageShown)
                                    return;
                                SessionSummaryActivity.start(DashboardActivity.this, session.getId());
                                sessioSummaryPageShown=true;

                            } else {
                                Log.d(TAG, " Unrated : Session object not available");
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, " Unrated : " + e.getMessage());
                    // hideProgress();
                }
            }
        };
        new NetworkService().unratedSessions(cb);
    }



    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        Auth.foreground("");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(notificationReceiver);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        checkForAppUpdate(DashboardActivity.this, APP_UPDATE_REQUEST_CODE);
        Auth.foreground(DashboardActivity.class.getSimpleName());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent");
        checkAndApplyCode();
        Log.w("APPOINMETNOTI","DashboardActivty");

    }

    public void checkAndApplyCode() {
        Log.d(TAG, "checkAndApplyCode");
        String couponCode = PrefManager.getInstance(DashboardActivity.this).getSharedString(PrefManager.COUPON_CODE, null);
        Log.d(TAG, "checkAndApplyCode couponcode = " + couponCode);
        if (couponCode != null) {
            String appliedCouponCode = PrefManager.getInstance(DashboardActivity.this).getSharedString(PrefManager.APPLIED_COUPON_CODE, null);
            Log.d(TAG, "checkAndApplyCode applied couponcode = " + appliedCouponCode);
            if (!isValid(appliedCouponCode) || !appliedCouponCode.equalsIgnoreCase(couponCode)) {
                applyCouponCode(DashboardActivity.this, couponCode);
            }
        }
    }


    public void showConnectButton() {
       connectProviderActioBar.setVisibility(View.VISIBLE);

    }
    public void hideConnectButton() {
        connectProviderActioBar.setVisibility(View.GONE);
    }

    public void showAddButton() {
        connectProviderActioBar.setVisibility(View.VISIBLE);

    }
    public void hideAddButton() {
        connectProviderActioBar.setVisibility(View.INVISIBLE);
    }



    private final BroadcastReceiver notificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.hasExtra("message")) {
                String message = intent.getExtras().getString("message");
                String type = intent.getExtras().getString("type");
                if (message != null && message != "") {
                    if(type.equals("twilio.conversations.new_message") || type.equals("twilioChat") ){

                        String conversation_sid = intent.getExtras().getString("conversation_sid");

                        if(isValid(DEFAULT_CONVERSATION_NAME) && isValid(conversation_sid)
                                && conversation_sid.equals(DEFAULT_CONVERSATION_NAME)){

                            return;
                        }

                        SnackUtils.show(actionBarTopLlay,message, DashboardActivity.this,getLayoutInflater(),conversation_sid);

                    }
                }
            }
        }
    };

    @Override
    public void receivedNewMessage(Message message) {

    }

    @Override
    public void messageSentCallback() {

    }

    @Override
    public void reloadMessages(List<Message> result) {

    }

    @Override
    public void messageTyping(Boolean visible) {

    }

    @Override
    public void uploadProgress(Boolean visible) {

    }

    @Override
    public void sendMediaSuccess(String type) {

    }

    @Override
    public void sendMediaError(String error) {

    }

    @Override
    public void setProgressValue(long value) {

    }


}





