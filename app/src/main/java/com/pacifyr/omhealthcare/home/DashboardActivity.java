package com.pacifyr.omhealthcare.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.amazonaws.services.sns.model.DeleteEndpointRequest;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ceino.utilitylibrary.CustomFontTextView;
import com.ceino.utilitylibrary.DeviceFitImageView;
import com.ceino.utilitylibrary.WrappingSlidingPaneLayout;
import com.ceino.utilitylibrary.model.pacifyr.MUser;
import com.ceino.utilitylibrary.utils.PrefManager;
import com.pacifyr.omhealthcare.Fragment.MySessionFragment;
import com.pacifyr.omhealthcare.Fragment.ResourcesFragment;
import com.pacifyr.omhealthcare.Fragment.SupportFragment;
import com.pacifyr.omhealthcare.Fragment.UserDashboardFragment;
import com.pacifyr.omhealthcare.Fragment.WelcomeTourFragment;
import com.pacifyr.omhealthcare.Payment.PaymentFragment;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.event.ActionbarEvent;
import com.pacifyr.omhealthcare.event.AlertTypeEvent;
import com.pacifyr.omhealthcare.event.CallEvent;
import com.pacifyr.omhealthcare.event.ChatEvent;
import com.pacifyr.omhealthcare.event.NotifyEvent;
import com.pacifyr.omhealthcare.home.account.AccountEditActivity;
import com.pacifyr.omhealthcare.home.account.AccountFragment;
import com.pacifyr.omhealthcare.model.interfaces.CallChatEventListner;
import com.pacifyr.omhealthcare.network.GsonUtils;
import com.pacifyr.omhealthcare.quickblox.chat.ui.activity.DialogsActivity;
import com.pacifyr.omhealthcare.sign.CrisisActivity;
import com.pacifyr.omhealthcare.utility.AWSManager;
import com.pacifyr.omhealthcare.utility.PacifyrAppCompactActivity;
import com.pacifyr.omhealthcare.utils.LogUtils;
import com.pacifyr.omhealthcare.utils.PagerEnabledSlidingPaneLayout;
import com.pacifyr.omhealthcare.utils.ToastHandler;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.ceino.utilitylibrary.network.UtilityNetworkService.GLOBAL_URL;


public class DashboardActivity extends PacifyrAppCompactActivity{

    boolean showingFirst = true;
    private Context context;

    WrappingSlidingPaneLayout mSlidingPaneLayout;
    boolean onceBackpressed =false;
    private PagerEnabledSlidingPaneLayout slidingPane;
    private LinearLayout leftPane;
    private LinearLayout detailLlay;
    private CustomFontTextView detailNameCtxv;
    private CustomFontTextView detailMailCtxv;
    private LinearLayout dashboardLlay;
    private DeviceFitImageView dashboardDimv;
    private CustomFontTextView dashboardCtxv;
    private LinearLayout mysessionsLlay;
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


    private LinearLayout messagesLlay;


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

        connectToPacifyr=(LinearLayout) findViewById(R.id.connect_pacifyr_llay);
        leftPane = (LinearLayout) findViewById(R.id.left_pane);
        account_rlay=(RelativeLayout) findViewById(R.id.account_rlay);
        detailLlay = (LinearLayout) findViewById(R.id.detail_llay);
        detailNameCtxv = (CustomFontTextView) findViewById(R.id.detail_name_ctxv);
        detailMailCtxv = (CustomFontTextView) findViewById(R.id.detail_mail_ctxv);
        dashboardLlay = (LinearLayout) findViewById(R.id.dashboard_llay);
        dashboardDimv = (DeviceFitImageView) findViewById(R.id.dashboard_dimv);
        profile_cimv = (CircleImageView) findViewById(R.id.profile_cimv);
        dashboardCtxv = (CustomFontTextView) findViewById(R.id.dashboard_ctxv);
        mysessionsLlay = (LinearLayout) findViewById(R.id.mysessions_llay);
        mesessionsDimv = (DeviceFitImageView) findViewById(R.id.mesessions_dimv);
        mysessionsCtxv = (CustomFontTextView) findViewById(R.id.mysessions_ctxv);
        paymentLlay = (LinearLayout) findViewById(R.id.payment_llay);
        browsePacifyr=(LinearLayout) findViewById(R.id.browse_llay);
        tutotial_llay = (LinearLayout) findViewById(R.id.tutotial_llay);
        supportLlay=(LinearLayout) findViewById(R.id.support_llay);
        policyDocumentLlay = (LinearLayout) findViewById(R.id.policy_document_llay);

        logoutLlay = (LinearLayout) findViewById(R.id.logout_llay);
        logoutDimv = (DeviceFitImageView) findViewById(R.id.logout_dimv);
        logoutCtxv = (CustomFontTextView) findViewById(R.id.logout_ctxv);

        crisisCtxv = (CustomFontTextView) findViewById(R.id.crisis_ctxv);
        crisisDimv = (DeviceFitImageView) findViewById(R.id.crisis_dimv);
        crisisLlay = (LinearLayout) findViewById(R.id.crisis_llay);
        crisisView = (View) findViewById(R.id.crisis_view);


        resourcesCtxv = (CustomFontTextView) findViewById(R.id.resourses_ctxv);
        resourcesDimv = (DeviceFitImageView) findViewById(R.id.resourses_dimv);
        resourcesLlay = (LinearLayout) findViewById(R.id.resourses_llay);




        messagesLlay =(LinearLayout) findViewById(R.id.message_llay);

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



        mSlidingPaneLayout = (WrappingSlidingPaneLayout) findViewById(R.id.sliding_pane);
        mSlidingPaneLayout.setSliderFadeColor(getResources().getColor(android.R.color.transparent));
        mSlidingPaneLayout.setParallaxDistance(480);


        mSlidingPaneLayout.setPanelSlideListener(new WrappingSlidingPaneLayout.PanelSlideListener()
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
        profile_cimv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRightLayActionbar();
                showFragment(new AccountFragment());
                defSetText(titleTxv,"profile");
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
                defSetText(titleTxv,"tutorial");
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

        mysessionsLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideRightLayActionbar();
                showFragment(new MySessionFragment());
                defSetText(titleTxv,getResources().getString(R.string.mysessions));
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
                        new DeleteEndPointTask().execute();
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


    }


    private void deleteAwsEndpoint() {
        try {
            DeleteEndpointRequest deleteEndpointRequest = new DeleteEndpointRequest();
            String endPoint = getSharedString(PrefManager.AWS_ENDPOINT, null);
            if (endPoint != null) {
                deleteEndpointRequest.setEndpointArn(endPoint);
                AWSManager.getSNSClient().deleteEndpoint(deleteEndpointRequest);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
            defSetText(titleTxv, "My Dashboard");
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
        defSetText(titleTxv,getResources().getString(R.string.mydashboard));
        Typeface type = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.avantica_bold_header));
        titleTxv.setTypeface(type);
        titleTxv.setTextColor(getResources().getColor(R.color.grey_dark_text));
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
            rightTxv.setTextColor(getResources().getColor(R.color.black));
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

    public class DeleteEndPointTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            deleteAwsEndpoint();
            return null;
        }
    }
}





