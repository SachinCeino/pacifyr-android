package com.pacifyr.omhealthcare.home.account;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ceino.utilitylibrary.CustomFontTextView;
import com.ceino.utilitylibrary.DeviceFitImageView;
import com.ceino.utilitylibrary.utils.PrefManager;
import com.facebook.login.LoginManager;
import com.pacifyr.omhealthcare.Fragment.MySessionFragment;
import com.pacifyr.omhealthcare.Fragment.SupportFragment;
import com.pacifyr.omhealthcare.fragment.UserDashboardFragment;
import com.pacifyr.omhealthcare.Fragment.WelcomeTourFragment;
import com.pacifyr.omhealthcare.Payment.PaymentFragment;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.event.AlertTypeEvent;
import com.pacifyr.omhealthcare.event.NotifyEvent;
import com.pacifyr.omhealthcare.home.ConnectToPacifyrActivity;
import com.pacifyr.omhealthcare.home.ListPacifyrScreenActivity;
import com.pacifyr.omhealthcare.quickblox.App;
import com.pacifyr.omhealthcare.utility.PacifyrAppCompactActivity;
import com.pacifyr.omhealthcare.utils.LogUtils;
import com.pacifyr.omhealthcare.utils.PagerEnabledSlidingPaneLayout;
import com.pacifyr.omhealthcare.utils.ToastHandler;
import com.pacifyr.omhealthcare.utils.WrappingSlidingPaneLayout;
import com.squareup.otto.Subscribe;

import de.hdodenhof.circleimageview.CircleImageView;


public class DashboardActivity extends PacifyrAppCompactActivity{

    boolean showingFirst = true;

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
        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            LogUtils.LOGD("install", "DashboardActivity");
            configPushNotifications();
            setContentView(R.layout.activity_dashboard);
            hideKeyboard();
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

            logoutLlay = findViewById(R.id.logout_llay);
            logoutDimv = findViewById(R.id.logout_dimv);
            logoutCtxv = findViewById(R.id.logout_ctxv);


            //NetworkService.startActionCutomerDashboard(this);
            showFragment();
            MultiDex.install(this);
            onSetActionBar();
            hideRightLayActionbar();
            //menu_llay=(LinearLayout) findViewById(R.id.menu_llay);
            PrefManager prefM = PrefManager.getInstance(DashboardActivity.this);
            String name=prefM.getUserFirstName();
            String mail=prefM.geteMail();
            String json=prefM.getJsonKey();
            String qbid=prefM.getQBID();

            setProfileValues();

            mSlidingPaneLayout = findViewById(R.id.sliding_pane);
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
                    defSetText(titleTxv, "My Dash board");
                }
            });
            detailLlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showRightLayActionbar();
                    showFragment(new AccountFragment());
                    defSetText(titleTxv,"Account");
                }
            });
            connectToPacifyr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideRightLayActionbar();
                    ConnectToPacifyrActivity.start(DashboardActivity.this);
             /*       Intent intent=new Intent(DashboardActivity.this, ConnectToPacifyrActivity.class);
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
                    //defSetText(titleTxv,"Dashboard");
                }
            });
            browsePacifyr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideRightLayActionbar();
                    Intent i=new Intent(DashboardActivity.this, ListPacifyrScreenActivity.class);
                    startActivity(i);
                }
            });

            paymentLlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideRightLayActionbar();
                    showFragment(new PaymentFragment());
                    defSetText(titleTxv,"Payment");
                }
            });


            mysessionsLlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideRightLayActionbar();
                    showFragment(new MySessionFragment());
                    Log.i("emooooooojiib","e:");
                    defSetText(titleTxv,"My Sessions");
                }
            });

            supportLlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideRightLayActionbar();
                    showFragment(new SupportFragment());
                    defSetText(titleTxv,"Support");
                }
            });

            logoutLlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideRightLayActionbar();
                    setAlertStatusInBlue("Logout", "Do you want to Sign out?", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            hideStatus();
                            /*PrefManager prefM= PrefManager.getInstance(DashboardActivity.this);
                            prefM.clearSharedAll();*/
try {
    LoginManager mLoginManager = LoginManager.getInstance();
    if(isValid(mLoginManager)) mLoginManager.logOut();
}catch (Exception e)
{
    e.printStackTrace();
}

                            deleteInstallationWeb();
                           /* LoginManager mLoginManager = LoginManager.getInstance();
                            if(isValid(mLoginManager))
                                mLoginManager.logOut();


                            Intent i = new Intent(DashboardActivity.this, SignActivity.class);
                            startActivity(i);
                            finish();*/
                        }
                    });
                    //Toast.makeText(DashboardActivity.this,"Logout",Toast.LENGTH_LONG).show();
                }
            });


    }
//https://pacifyrtest.s3.amazonaws.com/Q1514964254954.jpg
    private void setProfileValues() {
            try {
                detailNameCtxv.setText(getCurrentUserName());
                detailMailCtxv.setText(getCurrentUserMail());
                String imageurl=getCurrentUserImageUrl();
                LogUtils.LOGD("session","DA setProfileValues imageurl " + imageurl);

                if (isValid(imageurl)) {
                    LogUtils.LOGD("session","DA setProfileValues loading imageurl " + imageurl);
                 //   setAqueryImageUrl(profile_cimv,imageurl, R.drawable.placeholder_user);
                    App.getPicasso(). load(imageurl).into(profile_cimv);
                 /*   AppChatNotUSe.getPicasso(). load(imageurl).into(new Target() {
                                                              @Override
                                                              public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                                                  profile_cimv.setImageBitmap(bitmap);
                                                              }

                                                              @Override
                                                              public void onBitmapFailed(Drawable errorDrawable) {

                                                              }

                                                              @Override
                                                              public void onPrepareLoad(Drawable placeHolderDrawable) {

                                                              }
                                                          });*/
                            // Picasso.with(DashboardActivity.this).load(imageurl).placeholder(R.drawable.placeholder_user).into(profile_cimv);
                }
            }catch (Exception e)
            {
                LogUtils.LOGD("session","DA setProfileValues exce " + e.getMessage());
            }
    }

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
        defSetText(titleTxv, "My Dash board");
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
            defSetText(rightTxv,"Edit");
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
                        finish();
                    }
            }
        }
    }

    /*public void showQuickblox(View view) {
        Intent i=new Intent(this, VideoChatActivity.class);
        startActivity(i);
    }*/
}




