package com.pacifyr.client.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.multidex.MultiDex;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.github.javiersantos.appupdater.AppUpdater;
import com.pacifyr.client.Payment.PaymentFragment;
import com.pacifyr.client.R;
import com.pacifyr.client.activity.SplashScreen;
import com.pacifyr.client.base.PacifyrActivity;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.event.ActionbarEvent;
import com.pacifyr.client.event.AlertTypeEvent;
import com.pacifyr.client.event.CallEvent;
import com.pacifyr.client.event.ChatEvent;
import com.pacifyr.client.event.InternetConnectionEvent;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.fragment.AppointmentsFragment;
import com.pacifyr.client.fragment.CouponCodeFragment;
import com.pacifyr.client.fragment.MySessionFragment;
import com.pacifyr.client.fragment.ResourcesFragment;
import com.pacifyr.client.fragment.SupportFragment;
import com.pacifyr.client.fragment.UserDashboardFragment;
import com.pacifyr.client.fragment.WelcomeTourFragment;
import com.pacifyr.client.fragment.schedule.AppointmentModel;
import com.pacifyr.client.home.account.AccountEditActivity;
import com.pacifyr.client.home.account.AccountFragment;
import com.pacifyr.client.model.AppoinmentNotification;
import com.pacifyr.client.model.CallStatus;
import com.pacifyr.client.model.interfaces.CallChatEventListner;
import com.pacifyr.client.netconnectionlistener.CheckNetworkJob;
import com.pacifyr.client.netconnectionlistener.ConnectionCheckBroadcast;
import com.pacifyr.client.network.GsonUtils;
import com.pacifyr.client.quickblox.chat.ui.activity.DialogsActivity;
import com.pacifyr.client.quickblox.services.CallService;
import com.pacifyr.client.sign.CrisisActivity;
import com.pacifyr.client.sign.DiscountCodeActivity;
import com.pacifyr.client.utility.PacifyrAppCompactActivity;
import com.pacifyr.client.utils.AmazonSNSTokenHandler;
import com.pacifyr.client.utils.LogUtils;
import com.pacifyr.client.utils.PagerEnabledSlidingPaneLayout;
import com.pacifyr.client.utils.ToastHandler;
import com.pacifyr.client.utils.Utility;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MRoleModel;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MUser;
import com.pacifyrapp.utilitylibrary.network.UtilityNetworkService;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.quickblox.sample.core.utils.SharedPrefsHelper;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.pacifyr.client.quickblox.activities.CallActivity.isCallActive;
import static com.pacifyr.client.utils.Constants.getCountryChecks;
import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.GLOBAL_URL;


public class DashboardActivity extends PacifyrAppCompactActivity{

    boolean showingFirst = true;
    private Context context;

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


    AppUpdater appUpdater;


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
        if (isValid(appUpdater))
        {
            appUpdater.stop();
        }

    }


    public static String CreatedDay = "";


    private void loginToQuickblox() {
        SharedPrefsHelper sharedPrefsHelper = SharedPrefsHelper.getInstance();
        if (sharedPrefsHelper.hasQbUser()) {
            CallService.start(DashboardActivity.this, sharedPrefsHelper.getQbUser());
        }
    }



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
        defSetText(titleTxv, getResources().getString(R.string.mydashboard));
        Typeface type = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.avantica_bold_header));
        titleTxv.setTypeface(type);
        titleTxv.setTextColor(getResources().getColor(R.color._color_action_bar_text_color));
        backImv.setImageDrawable(getResources().getDrawable(R.drawable.leftmen));
        //backImv.setBackgroundResource(R.drawable.leftmen);
        leftLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSlidingPaneLayout.isOpen()) {
                    mSlidingPaneLayout.closePane();

                } else {
                    mSlidingPaneLayout.openPane();
                }
            }
        });
        showRightLayActionbar();
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
                    Intent intent = new Intent(DashboardActivity.this, AccountEditActivity.class);
                    //  intent.putExtra("display","Pacifyr");
                    startActivity(intent);
                }
            });

        } catch (Exception e) {
            LogUtils.LOGD("session", "DA showRightLayActionbar exce " + e.getMessage());
        }

    }

    private void hideRightLayActionbar() {

        try {
            rightTxv.setVisibility(View.INVISIBLE);
            rightLlay.setVisibility(View.INVISIBLE);
            rightImv.setVisibility(View.INVISIBLE);


        } catch (Exception e) {
            LogUtils.LOGD("session", "DA hideRightLayActionbar exce " + e.getMessage());
        }

    }

    public void showFragment() {
        FragmentManager fm = DashboardActivity.this.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        UserDashboardFragment fragment = new UserDashboardFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_main_content_fragment, fragment).commit();
    }

    public void showFragment(Fragment fragment) {
        /*FragmentManager fm=DashboardActivity.this.getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        UserDashboardFragment fragment=new UserDashboardFragment();*/
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_main_content_fragment, fragment).commit();
        mSlidingPaneLayout.closePane();
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

            if (getSupportFragmentManager().getBackStackEntryCount() > 0)
                getSupportFragmentManager().popBackStackImmediate();
            else {
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.LOGD("install", "DashboardActivity");
        configPushNotifications();
        setContentView(R.layout.activity_dashboard);
        hideKeyboard();

        context = DashboardActivity.this;
        //    permissionCheck();
        /**the library will work in background for checking new update available or not**/
        appUpdater = new AppUpdater(this);
        appUpdater.start();

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


        messagesLlay = findViewById(R.id.message_llay);

        coupon_llay = findViewById(R.id.coupon_llay);
        coupon_view = findViewById(R.id.coupon_view);
        mLinearLayoutAppointments = findViewById(R.id.mLinearLayoutAppointments);




        //NetworkService.startActionCutomerDashboard(this);
        showFragment();
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
            }

            @Override
            public void onPanelClosed(View panel) {

            }
        });

        dashboardLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideRightLayActionbar();
                showFragment(new UserDashboardFragment());
            }
        });
        detailLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showRightLayActionbar();
                showFragment(new AccountFragment());
            }
        });
        connectToPacifyr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideRightLayActionbar();
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

                hideRightLayActionbar();
                showFragment(new WelcomeTourFragment());

            }
        });
        browsePacifyr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideRightLayActionbar();
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

                hideRightLayActionbar();
                showFragment(new PaymentFragment());

            }
        });

        messagesLlay.setVisibility(View.VISIBLE);
        messagesLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideRightLayActionbar();
                //isFromSlideMenu true for subscription check validation
                boolean isFromSlideMenu=true;
                DialogsActivity.start(context,isFromSlideMenu);
                /*showFragment(new PacifyrMessageFragment());*/
                //defSetText(titleTxv,"");
            }
        });

        policyDocumentLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideRightLayActionbar();
                PolicyDocumentActivity.start(context);
                /*  showFragment(new PacifyrMessageFragment());*/
            }
        });

        mysessionsLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideRightLayActionbar();
                showFragment(new MySessionFragment());

            }
        });

        supportLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideRightLayActionbar();
                showFragment(new SupportFragment());
            }
        });

        crisisLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideRightLayActionbar();
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

                hideRightLayActionbar();
                showFragment(new ResourcesFragment());

            }
        });

        logoutLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideRightLayActionbar();
                setAlertStatusInBlue("log out", getResources().getString(R.string.signout_message), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideStatus();
                            /*PrefManager prefM= PrefManager.getInstance(getActivity());
                            prefM.clearSharedAll();*/
                        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        if (mNotificationManager != null) {
                            mNotificationManager.cancelAll();
                        }
                        deleteInstallationWeb();
                           /* LoginManager mLoginManager = LoginManager.getInstance();
                            if(isValid(mLoginManager))
                                mLoginManager.logOut();
                            Intent i = new Intent(getActivity(), SignActivity.class);
                            startActivity(i);
                            finish();*/
                    }
                });
                //Toast.makeText(getActivity(),"Logout",Toast.LENGTH_LONG).show();
            }
        });

        mLinearLayoutAppointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideRightLayActionbar();
                showFragment(new AppointmentsFragment());
                defSetText(titleTxv, getResources().getString(R.string.appointments));
            }
        });

        new AmazonSNSTokenHandler(context).register();
        loginToQuickblox();

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
                hideRightLayActionbar();
                showFragment(new CouponCodeFragment());

            }
        });

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
                    try {
                        hideProgress();
                        MUser user  = GsonUtils.getInstance().gsonToMUser(json);
                        Utility.getUserCreatedDayMonthYear(user.getCreatedAt());
                        detailNameCtxv.setText(user.getName());
                        detailMailCtxv.setText(user.getEmail());
                        String imageUrl = user.getImageUrl();
                        if(isValid(imageUrl)) {
                            Picasso.with(DashboardActivity.this).load(imageUrl).placeholder(R.drawable.placeholder_image).into(profile_cimv);
                        }else {
                            imageUrl = user.getImage();
                            if(isValid(imageUrl))
                                Picasso.with(DashboardActivity.this).load(imageUrl).placeholder(R.drawable.placeholder_image).into(profile_cimv);
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
                        coupon_llay.setVisibility(View.GONE);
                        coupon_view.setVisibility(View.GONE);
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


    @org.greenrobot.eventbus.Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetworkChanged(InternetConnectionEvent event) {
        try {
            if (isValid(event) && isShowDialogs()) {
                if (!event.isConnected())
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
                else
                {
                    hideProgress();
                    hideStatus2();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.w("APPOINMETNOTI","DashboardActivty");
    }
}





