package com.pacifyr.omhealthcare.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ceino.utilitylibrary.CustomFontTextView;
import com.ceino.utilitylibrary.DeviceFitImageView;
import com.ceino.utilitylibrary.utils.PrefManager;
import com.facebook.login.LoginManager;
import com.pacifyr.omhealthcare.Fragment.EarningsFragment;
import com.pacifyr.omhealthcare.Fragment.PacifyrDashboardFragment;
import com.pacifyr.omhealthcare.Fragment.PacifyrSessionsFragment;
import com.pacifyr.omhealthcare.Fragment.SupportFragment;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.event.ActionbarEvent;
import com.pacifyr.omhealthcare.event.AlertTypeEvent;
import com.pacifyr.omhealthcare.event.CallEvent;
import com.pacifyr.omhealthcare.event.ChatEvent;
import com.pacifyr.omhealthcare.event.NotifyEvent;
import com.pacifyr.omhealthcare.home.account.AccountEditActivity;
import com.pacifyr.omhealthcare.home.account.PacifyrAccountFragment;
import com.pacifyr.omhealthcare.model.interfaces.CallChatEventListner;
import com.pacifyr.omhealthcare.quickblox.chat.ui.activity.DialogsActivity;
import com.pacifyr.omhealthcare.utility.PacifyrAppCompactActivity;
import com.pacifyr.omhealthcare.utils.LogUtils;
import com.pacifyr.omhealthcare.utils.PagerEnabledSlidingPaneLayout;
import com.pacifyr.omhealthcare.utils.ToastHandler;
import com.pacifyr.omhealthcare.utils.WrappingSlidingPaneLayout;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.pacifyr.omhealthcare.network.NetworkService.webListEmotions;

public class PacifyrDashboardActivity extends PacifyrAppCompactActivity {

    boolean showingFirst = true;

    WrappingSlidingPaneLayout mSlidingPaneLayout;
    boolean onceBackpressed =false;
    private PagerEnabledSlidingPaneLayout slidingPane;
    private LinearLayout leftPane;
    private LinearLayout detailLlay;
    private RelativeLayout account_rlay;
    private CustomFontTextView detailNameCtxv;
    private CustomFontTextView detailMailCtxv;
    private LinearLayout dashboardLlay;
    private DeviceFitImageView dashboardDimv;
    private CustomFontTextView dashboardCtxv;
    private LinearLayout mysessionsLlay;
    private DeviceFitImageView mesessionsDimv;
    private CustomFontTextView mysessionsCtxv;
    private LinearLayout earningsLlay;
    private DeviceFitImageView earningsDimv;
    private CustomFontTextView earningsCtxv;
    private LinearLayout logoutLlay;
    private DeviceFitImageView logoutDimv;
    private CustomFontTextView logoutCtxv;
    private LinearLayout supportLlay;
    private LinearLayout messagesLlay;
    private CircleImageView profile_cimv;
    private  LinearLayout privacyPolicyLlay;

    private PrefManager prefM;

    public static void start(Context context
    ) {
     /*   mSessionDetailRoot=null;
        isConnected=false;isMissed=false;isDeclined=false;isOpponenetConnected=false;isOppenentHangUP=false;*/
        Intent intent = new Intent(context, PacifyrDashboardActivity.class);
        // intent.putExtra(Consts.EXTRA_IS_INCOMING_CALL, isIncomingCall);

        context.startActivity(intent);
    }
    public static void start(Activity activity
    ) {
     /*   mSessionDetailRoot=null;
        isConnected=false;isMissed=false;isDeclined=false;isOpponenetConnected=false;isOppenentHangUP=false;*/
        Intent intent = new Intent(activity, PacifyrDashboardActivity.class);
        // intent.putExtra(Consts.EXTRA_IS_INCOMING_CALL, isIncomingCall);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
        activity.finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pacifyr_dashboard);

        try {

            configPushNotifications();
            // permissionCheck();
            webListEmotions(getApplicationContext());
            leftPane = findViewById(R.id.left_pane);
            privacyPolicyLlay = findViewById(R.id.policy_document_llay);
            detailLlay = findViewById(R.id.detail_llay);
            account_rlay = findViewById(R.id.account_rlay);
            detailNameCtxv = findViewById(R.id.detail_name_ctxv);
            detailMailCtxv = findViewById(R.id.detail_mail_ctxv);
            dashboardLlay = findViewById(R.id.dashboard_llay);
            dashboardDimv = findViewById(R.id.dashboard_dimv);
            dashboardCtxv = findViewById(R.id.dashboard_ctxv);
            mysessionsLlay = findViewById(R.id.mysessions_llay);
            mesessionsDimv = findViewById(R.id.mesessions_dimv);
            mysessionsCtxv = findViewById(R.id.mysessions_ctxv);
            earningsLlay = findViewById(R.id.earnings_llay);
            earningsDimv = findViewById(R.id.earnings_dimv);
            earningsCtxv = findViewById(R.id.earnings_ctxv);
            logoutLlay = findViewById(R.id.logout_llay);
            logoutDimv = findViewById(R.id.logout_dimv);
            logoutCtxv = findViewById(R.id.logout_ctxv);
            supportLlay = findViewById(R.id.support_llay);
            messagesLlay = findViewById(R.id.mssages_llay);
            profile_cimv = findViewById(R.id.profile_cimv);
            prefM = getPrefManager();
            String name = prefM.getUserFirstName();
            String mail = prefM.geteMail();
                  /*  detailNameCtxv.setText(name);
                    detailMailCtxv.setText(mail);
            */
            messagesLlay.setVisibility(View.VISIBLE);
            setProfileValues();

       /* earningsLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(), VideoReceiverActivity.class);
                startActivity(i);
            }
        });*/

            showFragment();
            MultiDex.install(this);
            onSetActionBar();
            hideRightLayActionbar();
            mSlidingPaneLayout = findViewById(R.id.pacifyr_sliding_pane);
            mSlidingPaneLayout.setSliderFadeColor(getResources().getColor(android.R.color.transparent));
            mSlidingPaneLayout.setParallaxDistance(350);


            mSlidingPaneLayout.setPanelSlideListener(new WrappingSlidingPaneLayout.PanelSlideListener() {
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
                    showFragment(new PacifyrDashboardFragment());
                    defSetText(titleTxv, "My Dash board");
                }
            });
            profile_cimv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showRightLayActionbar();
                    showFragment(new PacifyrAccountFragment());
                    defSetText(titleTxv, "Account");
                }
            });
            earningsLlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideRightLayActionbar();
                    showFragment(new EarningsFragment());
                    defSetText(titleTxv, "Earnings");
                }
            });


            mysessionsLlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideRightLayActionbar();
                    showFragment(new PacifyrSessionsFragment());
                    defSetText(titleTxv, "My Sessions");
                }
            });


            messagesLlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideRightLayActionbar();
                    DialogsActivity.start(PacifyrDashboardActivity.this);
              /*  showFragment(new PacifyrMessageFragment(/));*/
                    defSetText(titleTxv, "Messages");
                }
            });
            supportLlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideRightLayActionbar();
                    showFragment(new SupportFragment());
                    defSetText(titleTxv, "Support");
                }
            });

            privacyPolicyLlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideRightLayActionbar();
                    PolicyDocumentActivity.start(PacifyrDashboardActivity.this);
              /*  showFragment(new PacifyrMessageFragment());*/
                    defSetText(titleTxv, "POLICY DOCUMENTS");
                }
            });

            logoutLlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideRightLayActionbar();
                    setAlertStatusInBlue("Logout", "Do you want to Sign out?", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                hideStatus();
                                LoginManager mLoginManager = LoginManager.getInstance();
                                if (isValid(mLoginManager)) mLoginManager.logOut();
                                logoutEditProfile(PacifyrDashboardActivity.this);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                Toast.makeText(PacifyrDashboardActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    //  Toast.makeText(getActivity(),"Logout",Toast.LENGTH_LONG).show();
                }
            });

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void setProfileValues() {
        try {
            detailNameCtxv.setText(getCurrentUserName());
            detailMailCtxv.setText(getCurrentUserMail());
            String imageurl=getCurrentUserImageUrl();
            if (isValid(imageurl)) {
                Picasso.with(PacifyrDashboardActivity.this).load(imageurl).placeholder(R.drawable.placeholder_user).into(profile_cimv);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    @Override
    protected View getSnackbarAnchorView() {
        return null;
    }

    public void showFragment() {
        try {
            FragmentManager fm = PacifyrDashboardActivity.this.getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            PacifyrDashboardFragment fragment = new PacifyrDashboardFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.pacifyr_content_fragment, fragment).commit();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void showFragment(Fragment fragment)
    {
        /*FragmentManager fm=DashboardActivity.this.getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        UserDashboardFragment fragment=new UserDashboardFragment();*/
        getSupportFragmentManager().beginTransaction().replace(R.id.pacifyr_content_fragment,fragment).commit();
        mSlidingPaneLayout.closePane();
    }

    private LinearLayout actionBarTopLlay;
    private RelativeLayout bottomPanel;
    private LinearLayout leftLlay;
    private DeviceFitImageView backImv;
    private CustomFontTextView leftTxv;
    public CustomFontTextView titleTxv;
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
        hideRightLayActionbar();
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
    }
    private void showRightLayActionbar() {

        try {
            rightTxv.setVisibility(View.VISIBLE);
            rightLlay.setVisibility(View.VISIBLE);
            rightImv.setVisibility(View.INVISIBLE);
            defSetText(rightTxv,"edit");
            rightTxv.setTextColor(getResources().getColor(R.color._color_action_bar_text_color));
            rightLlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(PacifyrDashboardActivity.this, AccountEditActivity.class);
                    //  intent.putExtra("display","Pacifyr");
                    startActivity(intent);
                }
            });

        }catch (Exception e)
        {
            LogUtils.LOGD("session","PDA showRightLayActionbar exce " + e.getMessage());
        }

    }

    private void hideRightLayActionbar() {

        try {
            rightTxv.setVisibility(View.INVISIBLE);
            rightLlay.setVisibility(View.INVISIBLE);
            rightImv.setVisibility(View.INVISIBLE);


        }catch (Exception e)
        {
            LogUtils.LOGD("session","PDA hideRightLayActionbar exce " + e.getMessage());
        }

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
    /*public void quicktest(View view) {
        Intent i=new Intent(this, QBLoginActivity.class);
        startActivity(i);
    }*/

    public void showCustomers(View view) {

    }

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

}
