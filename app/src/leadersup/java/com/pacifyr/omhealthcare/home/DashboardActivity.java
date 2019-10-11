package com.pacifyr.omhealthcare.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.multidex.MultiDex;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ceino.utilitylibrary.CustomFontTextView;
import com.ceino.utilitylibrary.DeviceFitImageView;
import com.ceino.utilitylibrary.model.pacifyr.MUser;
import com.ceino.utilitylibrary.utils.PrefManager;
import com.github.javiersantos.appupdater.AppUpdater;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.pacifyr.omhealthcare.fragment.AppointmentsFragment;
import com.pacifyr.omhealthcare.fragment.ResourcesFragment;
import com.pacifyr.omhealthcare.fragment.SupportFragment;
import com.pacifyr.omhealthcare.base.PacifyerActivity;
import com.pacifyr.omhealthcare.events.activity.CaptureActivityPortrait;
import com.pacifyr.omhealthcare.events.activity.EventActivity;
import com.pacifyr.omhealthcare.events.activity.OffersActivity;
import com.pacifyr.omhealthcare.events.model.QRSuccess;
import com.pacifyr.omhealthcare.fragment.UserDashboardFragment;
import com.pacifyr.omhealthcare.fragment.WelcomeTourFragment;
import com.pacifyr.omhealthcare.Payment.PaymentFragment;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.activity.TermsOfUseActivity;
import com.pacifyr.omhealthcare.event.ActionbarEvent;
import com.pacifyr.omhealthcare.event.AlertTypeEvent;
import com.pacifyr.omhealthcare.event.CallEvent;
import com.pacifyr.omhealthcare.event.ChatEvent;
import com.pacifyr.omhealthcare.event.NotifyEvent;
import com.pacifyr.omhealthcare.home.account.AccountEditActivity;
import com.pacifyr.omhealthcare.home.account.AccountFragment;
import com.pacifyr.omhealthcare.model.interfaces.CallChatEventListner;
import com.pacifyr.omhealthcare.network.GsonUtils;
import com.pacifyr.omhealthcare.network.NetworkService;
import com.pacifyr.omhealthcare.quickblox.chat.ui.activity.DialogsActivity;
import com.pacifyr.omhealthcare.sign.CrisisActivity;
import com.pacifyr.omhealthcare.utility.PacifyrAppCompactActivity;
import com.pacifyr.omhealthcare.utils.AmazonSNSTokenHandler;
import com.pacifyr.omhealthcare.utils.LogUtils;
import com.pacifyr.omhealthcare.utils.PagerEnabledSlidingPaneLayout;
import com.pacifyr.omhealthcare.utils.ToastHandler;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.ceino.utilitylibrary.network.UtilityNetworkService.GLOBAL_URL;


public class DashboardActivity extends PacifyrAppCompactActivity{

    boolean showingFirst = true;
    private Context context;

    PagerEnabledSlidingPaneLayout mSlidingPaneLayout;
    boolean onceBackpressed =false;
    private PagerEnabledSlidingPaneLayout slidingPane;
    private LinearLayout leftPane;
    private LinearLayout detailLlay;
    private CustomFontTextView detailNameCtxv;
    private CustomFontTextView detailMailCtxv;
    private LinearLayout dashboardLlay;
    private DeviceFitImageView dashboardDimv;
    private CustomFontTextView dashboardCtxv;
    private LinearLayout goToELearning;
    private DeviceFitImageView mesessionsDimv;
    private CustomFontTextView mysessionsCtxv;
    private LinearLayout paymentLlay;

    private LinearLayout connectToPacifyr;
    private LinearLayout browsePacifyr;

    private LinearLayout supportLlay;
    //private DeviceFitImageView earningsDimv;
    //private CustomFontTextView earningsCtxv;
    private LinearLayout logoutLlay;
    private LinearLayout tutotial_llay;
    private RelativeLayout account_rlay;
    private DeviceFitImageView logoutDimv;
    private CustomFontTextView logoutCtxv;
    private CircleImageView profile_cimv;

    private LinearLayout policyDocumentLlay;
    private LinearLayout crisisLlay;
    private CustomFontTextView crisisCtxv;
    private DeviceFitImageView crisisDimv;
    private View crisisView;
    private CustomFontTextView resourcesCtxv;
    private LinearLayout resourcesLlay;
    private DeviceFitImageView resourcesDimv;

    private LinearLayout event_llay, offer_llay;
    private LinearLayout linear_layout_check_in_laoyut;
    private LinearLayout mLinearLayoutAppointments;


    private LinearLayout messagesLlay;
    private LinearLayout button_close;

    //qr code scanner object
    private IntentIntegrator qrScan;

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
    protected void onDestroy() {
        super.onDestroy();
        if (isValid(appUpdater))
        {
            appUpdater.stop();
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
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
        goToELearning = findViewById(R.id.mysessions_llay);
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

        event_llay = findViewById(R.id.event_llay);
        offer_llay = findViewById(R.id.offer_llay);




        messagesLlay = findViewById(R.id.message_llay);
        button_close=findViewById(R.id.button_close);

        linear_layout_check_in_laoyut = findViewById(R.id.check_in_laoyut);
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


        mSlidingPaneLayout.setPanelSlideListener(new PagerEnabledSlidingPaneLayout.PanelSlideListener()
        {
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
                defSetText(titleTxv,getResources().getString(R.string.mydashboard));
            }
        });
        detailLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRightLayActionbar();
                showFragment(new AccountFragment());
                defSetText(titleTxv,"PROFILE");
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
                defSetText(titleTxv,"TUTORIAL");
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

        paymentLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideRightLayActionbar();
                showFragment(new PaymentFragment());
                defSetText(titleTxv,"payment");
            }
        });

        messagesLlay.setVisibility(View.VISIBLE);
        messagesLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideRightLayActionbar();
                DialogsActivity.start(context);
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

        goToELearning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),TermsOfUseActivity.class);
                i.putExtra("spurl", getResources().getString(R.string.go_to_e_learning_url));
                i.putExtra("title", "E-LEARNING");
                startActivity(i);
            }
        });

        supportLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideRightLayActionbar();
                showFragment(new SupportFragment());
                defSetText(titleTxv,getResources().getString(R.string.contact_us));
            }
        });

        crisisLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this,CrisisActivity.class);
                startActivity(intent);
                /*hideRightLayActionbar();
                showFragment(new CrisisFragment());
                defSetText(titleTxv, getResources().getString(R.string.crisis_info));*/
            }
        });
        resourcesLlay.setVisibility(View.VISIBLE);
        resourcesLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideRightLayActionbar();
                showFragment(new ResourcesFragment());
                defSetText(titleTxv, getResources().getString(R.string.resources));
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


        button_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();

            }
        });


        event_llay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EventActivity.start(getApplicationContext());
            }
        });

        offer_llay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OffersActivity.start(getApplicationContext());
            }
        });

        linear_layout_check_in_laoyut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                qrScan = new IntentIntegrator(DashboardActivity.this);


                qrScan.setPrompt("Scan a barcode");
                qrScan.setCameraId(0);  // Use a specific camera of the device
                qrScan.setOrientationLocked(true);
                qrScan.setBeepEnabled(true);
                qrScan.setCaptureActivity(CaptureActivityPortrait.class);
                qrScan.initiateScan();
                qrScan.initiateScan();

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
            if(event.isSuccess)
                setProfileValues();
        }catch (Exception e)
        {
            LogUtils.LOGD("session","DA onNotifyEvent exce " + e.getMessage());
        }

    }

    @Subscribe
    public void onAlertTypeEvent(AlertTypeEvent event) {

    }
    @Subscribe
    public void onActionbarEvent(ActionbarEvent event) {
        try {
            defSetText(titleTxv, "My Dash board");
        }catch (Exception e)
        {
            LogUtils.LOGD("session","DA onActionbarEvent exce " + e.getMessage());
        }
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
        defSetText(titleTxv,getResources().getString(R.string.mydashboard));
        Typeface type = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.avantica_bold_header));
        titleTxv.setTypeface(type);
        titleTxv.setTextColor(getResources().getColor(R.color._color_action_bar_text_color));
        backImv.setImageDrawable(getResources().getDrawable(R.drawable.leftmen));
        //backImv.setBackgroundResource(R.drawable.leftmen);
        leftLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showingFirst == true){
                    mSlidingPaneLayout.openPane();
                    showingFirst = false;
                }else{
                    mSlidingPaneLayout.closePane();
                    showingFirst = true;
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
            defSetText(rightTxv,getResources().getString(R.string.edit));
            rightTxv.setTextColor(getResources().getColor(R.color._color_action_bar_text_color));
            rightLlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(DashboardActivity.this, AccountEditActivity.class);
                    //  intent.putExtra("display","Pacifyr");
                    startActivity(intent);
                }
            });

        }catch (Exception e)
        {
            LogUtils.LOGD("session","DA showRightLayActionbar exce " + e.getMessage());
        }

    }

    private void hideRightLayActionbar() {

        try {
            rightTxv.setVisibility(View.INVISIBLE);
            rightLlay.setVisibility(View.INVISIBLE);
            rightImv.setVisibility(View.INVISIBLE);


        }catch (Exception e)
        {
            LogUtils.LOGD("session","DA hideRightLayActionbar exce " + e.getMessage());
        }

    }
    public void showFragment()
    {
        FragmentManager fm=DashboardActivity.this.getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        UserDashboardFragment fragment=new UserDashboardFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_main_content_fragment,fragment).commit();
    }

    public void showFragment(Fragment fragment)
    {
        /*FragmentManager fm=DashboardActivity.this.getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        UserDashboardFragment fragment=new UserDashboardFragment();*/
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_main_content_fragment,fragment).commit();
        mSlidingPaneLayout.closePane();
    }

    public void showSearchPacifyrs(View view) {
        Intent i=new Intent(this, ListPacifyrScreenActivity.class);
        startActivity(i);
    }

    @Override public void onBackPressed()
    {
        if (mSlidingPaneLayout.isOpen())
        {
            mSlidingPaneLayout.closePane();
        }
        else {

            if(getSupportFragmentManager().getBackStackEntryCount()>0)
                getSupportFragmentManager().popBackStackImmediate();
            else {
                if(!onceBackpressed) {
                    ToastHandler.newInstance(this).mustShowToast("" + getText(R.string.press_back_again_to_exit));
                    onceBackpressed = true;
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable()
                    {
                        @Override public void run() {
                            onceBackpressed = false;
                        }
                    }, 5000);
                }
                else {
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
        try{
            LogUtils.LOGD("session","CPA onCallEvent called " );
            if(!isValid(event.mpacifyer,"Network Error"))
                return;
            startChat(event.mpacifyer);



        }catch (Exception e)
        {
            LogUtils.LOGD("session","CPA ChatEvent exce " + e.getMessage());
        }

    }


    public CallChatEventListner callchatEventListner =new CallChatEventListner() {
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

                    } catch (Exception e) {
                        hideProgress();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
            } else {
                try {
                    JSONObject obj = new JSONObject(result.getContents());
                    if (isValid(obj))
                        if (obj.has("code")) {
                            String code = obj.get("code").toString();
                            callAPIWithCodeFromQRScanner(code);
                        }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (isValid(result.getContents()))
                        callAPIWithCodeFromQRScanner(result.getContents());
                    //  Toast.makeText(getApplicationContext(), result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * event attending scan popup
     **/

    private void callAPIWithCodeFromQRScanner(String code) {

        try {
            PrefManager prefM = PrefManager.getInstance(getApplicationContext());
            String userToken = prefM.getAccessToken();
            String userid = prefM.getUserID();
            setLoading();
            showProgress();
            String url = NetworkService.GLOBAL_URL + "events/checkinUsingCode";
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    try {
                        hideProgress();
                        QRSuccess data = GsonUtils.getInstance().gsonToModelQRCodeResult(json);
                        if (isValid(data))
                            showPopup(data.getMessage() + "");

                    } catch (Exception e) {
                        errorStatus();
                    }
                }
            };
            AQuery aq = new AQuery(getApplicationContext());
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", userToken);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId", prefM.getUserID());
            jsonObject.put("code", code);
            aq.post(url, jsonObject, JSONObject.class, cb);
        } catch (Exception ex) {
            errorStatus();
        }

    }

    private void showPopup(String data) {

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.popup_qr_code_success_, null);
        final TextView message = alertLayout.findViewById(R.id.message);
        final TextView done = alertLayout.findViewById(R.id.done);
        /*final EditText etEmail = alertLayout.findViewById(R.id.et_email);
        final CheckBox cbToggle = alertLayout.findViewById(R.id.cb_show_pass);
*/

        message.setText(data);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        AlertDialog dialog = alert.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}





