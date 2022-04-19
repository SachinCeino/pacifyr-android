package com.pacifyr.client.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.pacifyr.client.R;
import com.pacifyr.client.base.PacifyrActivity;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.event.ConnectEvent;
import com.pacifyr.client.event.InternetConnectionEvent;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.event.RateSessionEvent;
import com.pacifyr.client.home.ConnectToPacifyrActivity;
import com.pacifyr.client.home.ConnectToSinglePacifyrActivity;
import com.pacifyr.client.home.DashboardActivity;
import com.pacifyr.client.home.LastSessionActivity;
import com.pacifyr.client.home.MySessionsActivity;
import com.pacifyr.client.home.SessionSummaryActivity;
import com.pacifyr.client.model.Session;
import com.pacifyr.client.model.UnreadCountModel;
import com.pacifyr.client.network.GsonUtils;
import com.pacifyr.client.network.NetworkService;
import com.pacifyr.client.orginals.Auth;
import com.pacifyr.client.receiver.NotificationUtils;
import com.pacifyr.client.twiliochat.TwilioCoversationListActivity;
import com.pacifyr.client.utility.BaseFragment;
import com.pacifyr.client.utility.EventMessage;
import com.pacifyr.client.utils.Toaster;
import com.pacifyr.client.utils.Utility;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MSessionDetailRoot;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MUser;
import com.pacifyrapp.utilitylibrary.model.pacifyr.ModelCustomerDashboard2;
import com.pacifyrapp.utilitylibrary.model.pacifyr.ModelSessonsOfCustomer;
import com.pacifyrapp.utilitylibrary.network.UtilityNetworkService;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.squareup.otto.Subscribe;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.pacifyr.client.R.id.grid_balls;
import static com.pacifyr.client.R.id.last_session_rlay;
import static com.pacifyr.client.base.PacifyrActivity.call_emotion;
import static com.pacifyr.client.network.NetworkService.lastSession;
import static com.pacifyr.client.network.NetworkService.modelAllEmotions;
import static com.pacifyr.client.network.NetworkService.modelCustomerDashboard2;
import static com.pacifyr.client.twilio.VideoActivity.isCallActive;
import static com.pacifyr.client.twiliochat.TwilioConversationActivity.DEFAULT_CONVERSATION_NAME;
import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.GLOBAL_IMAGE_URL;
import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.GLOBAL_URL;
import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.TOKEN_EXPIRY_TIME;
import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.TOKEN_REFRESH_TIME;

public class UserDashboardFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = UserDashboardFragment.class.getSimpleName();
    private RelativeLayout feelRlay,this_week_rlay,connect_btn_rlay;
    //private DeviceFprefMitImageView feelImage;
    private RelativeLayout lastSessionRlay;
    private CircleImageView lastSessionDimv;
    private MainTextView lastSsnPacfyrCtxv,no_pacifyr_ctxv,no_lastsession_ctxv;
    private LinearLayout last_session_top_llay;
    private DeviceFitImageView videoCamImg;
    private MainTextView lastSessionMinuteTxv;
    private MainTextView daysTxv;
    private RelativeLayout cirPrefPacifyr;
    private LinearLayout llayTop, llay_bottom;
    private CircleImageView pacifyr1Dimv;
    private CircleImageView pacifyr2Dimv;
    private CircleImageView pacifyr4Dimv;
    private LinearLayout llayBottom;
    private CircleImageView pacifyr3Dimv;
    //private DeviceFitImageView searchDimv;
    private RelativeLayout thisWeekRlay;
    private MainTextView sessionsTxv;
    private MainTextView hoursTxv;
    private MainTextView paidTxv;
    private DeviceFitImageView feelImageDimv;
    private MainTextView feelTitleCtxv;
    private PrefManager prefM;
    private MUser prefpac;
    private MainTextView minutesCtxv;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MainTextView feel_err_text;
    public String EmotionTitle="";

    private MainTextView unreadChatCtxv;
    public Integer unreadCount =0;

    private boolean isClickedOnChat=false;
    private int badgeCount=0;
    private ImageView image_logo;

    private static boolean isVisible=false;
    public static boolean getVisibility(){
        return isVisible;
    }
    public UserDashboardFragment() {
        // Required empty public constructor
    }
    private LinearLayout LinearLayout_Active_Session;
    TextView JoinChat,JoinCall;
    private ImageView bannerLogo;
    LinearLayout ActionButtonJoin,bannerLogoll;
    private TextView bannerTitle,TextViewHideBanner,TextView_buttonText;
    private RelativeLayout bannerLayout;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getActivity() instanceof DashboardActivity) {
            ((DashboardActivity) getActivity()).changeBottomMenuIcon(R.id.bnm_dashboard);
        }
    }

    private void getPermissions() {
        Utility.checkPermission(getActivity(), "essential");
    }
    Timer _TimerRefreshDashBoard=null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefM = PrefManager.getInstance(getContext());
        //ShortcutBadger.applyCount(getActivity(), prefM.getBadgeValue()); //for 1.1.4+


        if(savedInstanceState==null)
        {
          //  modelCustomerDashboard2=null;
            NetworkService.startActionCutomerDashboard(getActivity());
//            getPermissions();
            setMyAdapter();
        }



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isValid(_TimerRefreshDashBoard)) {
            _TimerRefreshDashBoard.cancel();
            Log.w("TIMERRUNNING","NO");
        }
    }

    private LinearLayout session_llay;
    private TextView Text_appointmentTime,MyLastSessionTitle;
    private RelativeLayout appointMentBackground;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_user_dashboard,container,false);
        intializeViews(view);
        //NetworkService.startActionCutomerDashboard(getContext());


        bannerLogo=view.findViewById(R.id.bannerLogo);
        bannerTitle=view.findViewById(R.id.bannerTitle);
        ActionButtonJoin=view.findViewById(R.id.ActionButtonJoin);
        bannerLayout=view.findViewById(R.id.bannerLayout);
        bannerLogoll=view.findViewById(R.id.bannerLogoll);
        TextViewHideBanner=view.findViewById(R.id.TextViewHideBanner);
        TextView_buttonText=view.findViewById(R.id.TextView_buttonText);
        session_llay=view.findViewById(R.id.session_llay);
        Text_appointmentTime=view.findViewById(R.id.Text_appointmentTime);
        MyLastSessionTitle=view.findViewById(R.id.MyLastSessionTitle);
        appointMentBackground=view.findViewById(R.id.appointMentBackground);


        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);



        unreadChatCtxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DialogsActivity.start(getActivity());
                if (!((DashboardActivity)getActivity()).isInternetAvailable()) {
                    ((DashboardActivity) getActivity()).showInternetNotAvailablePopup();
                    return;
                }


                TwilioCoversationListActivity.start(getActivity(),false);
                unreadCount=0;
                /*showFragment(new PacifyrMessageFragment(/));*/
                //defSetText(titleTxv, "Messages");

            }
        });


        lastSessionRlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (!((DashboardActivity)getActivity()).isInternetAvailable()) {
                        ((DashboardActivity) getActivity()).showInternetNotAvailablePopup();
                        return;
                    }

                    if (isValid(modelCustomerDashboard2.getNextAppointment()))
                    {
                        ((DashboardActivity)getActivity()).showAppointmentPage();
                    }
                    else {
                        if (isValid(lastSession)&&isValid(lastSession.getId()))
                        {
                            String sessionID = lastSession.getId();
                            LastSessionActivity.start(getActivity(), sessionID, true);
                        }
                        else {
                            ConnectToPacifyrActivity.start(getActivity());
                        }
                    }


                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }

            }
        });

        feelRlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent i= new Intent(getContext(),FeelSelectActivity.class);
                startActivity(i);*/
                if (!((DashboardActivity)getActivity()).isInternetAvailable()) {
                    ((DashboardActivity) getActivity()).showInternetNotAvailablePopup();
                    return;
                }
                ConnectToPacifyrActivity.start(getActivity());
             /*    Intent intent= new Intent(getContext(),ConnectToPacifyrActivity.class);
                intent.putExtra("display","Pacifyr");
                startActivity(intent);*/

            }
        });

        thisWeekRlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!((DashboardActivity)getActivity()).isInternetAvailable()) {
                        ((DashboardActivity) getActivity()).showInternetNotAvailablePopup();
                        return;
                    }
                    if (isValid(modelAllEmotions))
                        MySessionsActivity.start(getActivity());
                    else
                        callAllEmotions();


                   /* getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.activity_main_content_fragment, new MySessionFragment()).commit();
                    //showFragment(new PacifyrSessionsFragment());
                    MainTextView titleTxv = (MainTextView) getActivity().findViewById(R.id.title_txv);
                    defSetText(titleTxv, "My Sessions");
                    //Toast.makeText(getContext(),"sessions rating",Toast.LENGTH_LONG).show();*/
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        connect_btn_rlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!((DashboardActivity)getActivity()).isInternetAvailable()) {
                    ((DashboardActivity) getActivity()).showInternetNotAvailablePopup();
                    return;
                }
                ConnectToPacifyrActivity.start(getActivity());
                /*Intent intent=new Intent(getActivity(), ConnectToPacifyrActivity.class);
                intent.putExtra("display","Pacifyr");
                startActivity(intent);*/
            }
        });

        pacifyr1Dimv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Log.i("preffffff11","p: "+prefpac);
                if (!((DashboardActivity)getActivity()).isInternetAvailable()) {
                    ((DashboardActivity) getActivity()).showInternetNotAvailablePopup();
                    return;
                }
                if (isValid(modelCustomerDashboard2)&&isValid(modelCustomerDashboard2.getPreferredPacifyrs()[0]))
                {
                    prefpac = modelCustomerDashboard2.getPreferredPacifyrs()[0];
                    if (isValid(prefpac))
                        ConnectToSinglePacifyrActivity.start(getActivity(), prefpac);
                }



                // showTheRoundDialog(0, prefpac);
            }
        });
        pacifyr2Dimv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!((DashboardActivity)getActivity()).isInternetAvailable()) {
                    ((DashboardActivity) getActivity()).showInternetNotAvailablePopup();
                    return;
                }
                if (isValid(modelCustomerDashboard2)&&isValid(modelCustomerDashboard2.getPreferredPacifyrs()[1]))
                {
                    prefpac = modelCustomerDashboard2.getPreferredPacifyrs()[1];
                    if (isValid(prefpac))
                        ConnectToSinglePacifyrActivity.start(getActivity(), prefpac);
                }

            }
        });
        pacifyr3Dimv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!((DashboardActivity)getActivity()).isInternetAvailable()) {
                    ((DashboardActivity) getActivity()).showInternetNotAvailablePopup();
                    return;
                }
                if (isValid(modelCustomerDashboard2)&&isValid(modelCustomerDashboard2.getPreferredPacifyrs()[2]))
                {
                    prefpac = modelCustomerDashboard2.getPreferredPacifyrs()[2];
                    if (isValid(prefpac))
                        ConnectToSinglePacifyrActivity.start(getActivity(), prefpac);
                }

            }
        });

        pacifyr4Dimv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!((DashboardActivity)getActivity()).isInternetAvailable()) {
                    ((DashboardActivity) getActivity()).showInternetNotAvailablePopup();
                    return;
                }
                if (isValid(modelCustomerDashboard2)&&isValid(modelCustomerDashboard2.getPreferredPacifyrs()[3]))
                {
                    prefpac = modelCustomerDashboard2.getPreferredPacifyrs()[3];
                    if (isValid(prefpac))
                        ConnectToSinglePacifyrActivity.start(getActivity(), prefpac);
                }

            }
        });

        if (prefM != null && prefM.getCompanyLogo() != null) {
            Glide.with(getActivity()).load(UtilityNetworkService.GLOBAL_IMAGE_URL + prefM.getCompanyLogo()).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(image_logo);
        }
        else{
            image_logo.setVisibility(View.GONE);
        }

        //NetworkService.webListEmotions(getActivity());


        TextViewHideBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextViewHideBanner.getText().equals("Hide"))
                {
                    bannerLayout.setVisibility(View.GONE);
                    TextViewHideBanner.setText("Show Banner");
                    getPrefManager().putSharedBoolean("showBanner",false);
                }
                else {
                    bannerLayout.setVisibility(View.VISIBLE);
                    adjustGridBalls();
                    TextViewHideBanner.setText("Hide");
                    getPrefManager().putSharedBoolean("showBanner",true);
                }
            }
        });
        return view;
    }


    private void setUpBanner() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("banners")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                try {

                                    String bannerUrl = null;
                                    String buttonText=null;
                                    if (isValid(document.get("imageUrl")))
                                    {
                                        bannerUrl=document.get("imageUrl").toString();
                                    }
                                    if(isValid(document.get("buttonText")))
                                    {
                                        buttonText=document.get("buttonText").toString();
                                    }
                                    if (isValid(buttonText))
                                    {
                                        TextView_buttonText.setText(buttonText);
                                    }

                                    String webinarUrl=null;
                                    if (isValid(document.get("url")))
                                    {
                                        webinarUrl=document.get("url").toString();
                                    }


                                    if (isValid(document.get("text").toString()))
                                    {
                                        bannerTitle.setText(document.get("text").toString());
                                        bannerTitle.setVisibility(View.VISIBLE);
                                    }
                                    else {
                                        bannerTitle.setVisibility(View.GONE);
                                    }
                                    if (isValid(bannerUrl)){
                                        bannerLogoll.setVisibility(View.VISIBLE);
                                        showPrefferedPacifyrIntoGlideView(bannerUrl,bannerLogo);
                                    }
                                    else{
                                        bannerLogoll.setVisibility(View.GONE);
                                    }


                                    if (isValid(webinarUrl))
                                    {
                                        ActionButtonJoin.setVisibility(View.VISIBLE);
                                    }else {
                                        ActionButtonJoin.setVisibility(View.GONE);
                                    }


                                    String finalWebinarUrl = webinarUrl;
                                    ActionButtonJoin.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(finalWebinarUrl));
                                            browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(browserIntent);
                                        }
                                    });


                                    List<String> countrys = (List<String>) document.get("country");

                                    if (isValid(countrys))
                                    {

                                        if (countrys.contains(getPrefManager().getCountry()) ||
                                                getPrefManager().getCountry().equals("Wakanda")) {
                                            Timestamp startDate = document.getTimestamp("startDate");
                                            Timestamp endDate = document.getTimestamp("endDate");


                                            boolean flag = false;
                                            if (isValid(startDate) && isValid(endDate)) {
                                                flag = checkCurrentDateIsInBetWeenTheseTwoDates(startDate, endDate);
                                            }
                                            if (flag) {
                                                if (LinearLayout_Active_Session.getVisibility() == View.GONE) {


                                                        if (getPrefManager().getSharedBoolean("isFirstTimeLogin",true)){
                                                            bannerLayout.setVisibility(View.VISIBLE);
                                                            TextViewHideBanner.setText("Hide");
                                                            TextViewHideBanner.setVisibility(View.VISIBLE);
                                                            adjustGridBalls();
                                                            getPrefManager().putSharedBoolean("isFirstTimeLogin",false);
                                                            getPrefManager().putSharedBoolean("showBanner",true);
                                                        }
                                                        else {
                                                            if (!getPrefManager().getSharedBoolean("showBanner",false)) {


                                                                    bannerLayout.setVisibility(View.GONE);
                                                                    TextViewHideBanner.setText("Show Banner");
                                                                    TextViewHideBanner.setVisibility(View.VISIBLE);
                                                                    adjustGridBalls();



                                                            }
                                                            else {
                                                                bannerLayout.setVisibility(View.VISIBLE);
                                                                TextViewHideBanner.setText("Hide");
                                                                TextViewHideBanner.setVisibility(View.VISIBLE);
                                                                adjustGridBalls();
                                                            }
                                                        }


                                                }

                                            }
                                            else {
                                                bannerLayout.setVisibility(View.GONE);
                                                TextViewHideBanner.setVisibility(View.INVISIBLE);
                                                adjustGridBalls();
                                                return;
                                            }


                                        }
                                    }

                                }catch (Exception e)
                                {
                                    Log.w(TAG, "Error getting documents.", task.getException());
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }


    private void adjustGridBalls() {
        // do this adjustment for only h480dp layout
        if (!getString(R.string.density).equalsIgnoreCase("480")) {
            return;
        }
        GridLayout gridBalls = getView().findViewById(grid_balls);
        if (bannerLayout.getVisibility() == View.VISIBLE) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.BELOW, R.id.missed_chat_rlay);
            gridBalls.setLayoutParams(params);

            RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params2.addRule(RelativeLayout.BELOW, R.id.percent_layout);
            params2.topMargin = 10;
            connect_btn_rlay.setLayoutParams(params2);


        } else {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.BELOW, R.id.missed_chat_rlay);
            gridBalls.setLayoutParams(params);

            RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params2.addRule(RelativeLayout.BELOW, R.id.percent_layout);
            params2.topMargin = 50;
            connect_btn_rlay.setLayoutParams(params2);
        }
    }

    private boolean checkCurrentDateIsInBetWeenTheseTwoDates(Timestamp startDate, Timestamp endDate) {

        long startDateInMilli = startDate.getSeconds();
        long endDateInMilli = endDate.getSeconds();

        Date date = Calendar.getInstance().getTime();
        Timestamp m = new Timestamp(date);
        long currentDateInMilli = m.getSeconds();
        return currentDateInMilli > startDateInMilli && currentDateInMilli < endDateInMilli;

    }

    private void callAllEmotions() {
        PrefManager prefM = PrefManager.getInstance(getActivity());
        String token = prefM.getAccessToken();
        String userid = prefM.getUserID();
        String url = GLOBAL_URL + "emotions/ordered";
        AjaxCallback cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                try {
                    modelAllEmotions = GsonUtils.getInstance().gsonToMListAllEmotionsI(json);
                    MySessionsActivity.start(getActivity());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        AQuery aq = new AQuery(getActivity());
        cb.header("Content-Type", "application/json");
        cb.header("AccessToken", token);
        aq.ajax(url, JSONObject.class, cb);
    }

    @Override
    public void onResume() {
        super.onResume();
        isVisible=true;
        ((DashboardActivity) getActivity()).changeActionBarTitleForFragments(getResources().getString(R.string.nav_mydashbord_title));
        ((DashboardActivity) getActivity()).showConnectButton();

        try {

            checkLastTimeLoginFiftyDaysExcceed();
            /**refresh dashboard every 5 seconds for checking available active sessions **/

            _TimerRefreshDashBoard = new Timer();
            _TimerRefreshDashBoard.scheduleAtFixedRate(new TimerTask() {

                @Override
                public void run() {
                    checkIsThereAnyActiveSessionsAvailable();
                    getChatUnreadCount();
                    Log.w("TIMERRUNNING","YES");
                }

            },0,5000);
            if (isCallActive)
                LinearLayout_Active_Session.setVisibility(View.GONE);
                setUpBanner();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.d("ON RESUME;",e.getMessage());
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(notificationReceiver, new IntentFilter(NotificationUtils.NOTIFY_INTENT));
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(notificationReceiver);

        EventBus.getDefault().unregister(this);
        ((DashboardActivity) getActivity()).hideConnectButton();
        isVisible=false;
        if (isValid(_TimerRefreshDashBoard)) {
            _TimerRefreshDashBoard.cancel();
            Log.w("TIMERRUNNING","NO");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isValid(_TimerRefreshDashBoard)) {
            _TimerRefreshDashBoard.cancel();
            Log.w("TIMERRUNNING","NO");
        }
    }

    @org.greenrobot.eventbus.Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetworkChanged(InternetConnectionEvent event) {
        try {
            if (isValid(event) && ((DashboardActivity)getActivity()).isShowDialogs()) {
                if (event.isConnected())
                {
                    checkIsThereAnyActiveSessionsAvailable();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @org.greenrobot.eventbus.Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewSessionRatingCame(RateSessionEvent event) {
        try {
            if (isValid(event) ) {
                if (isValid(event.getmSessionId()))
                {
                  //  getUnratedSessions();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    MSessionDetailRoot mMSessionDetailRoot=null;
    private void checkIsThereAnyActiveSessionsAvailable() {
        try {
            if (isCallActive)return;
            PrefManager prefM = PrefManager.getInstance(getActivity());
            String token = prefM.getAccessToken();
            String userid = prefM.getUserID();
            String url = GLOBAL_URL + "session/customer/active";
            setLoading();
            showProgress();
            Log.w("TOKEN_AND_USERID",token+":"+userid);
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                   // Auth.httpStatusFilter(getActivity(), status.getCode());
                    try {
                        hideProgress();
                        if (isValid(json)) {
                            ModelSessonsOfCustomer model = GsonUtils.getInstance().gsonToModelSessonsOfCustomer(json);

                            if (isValid(model) && isValid(model.getResultsList())&&isValid(model.getResultsList().get(0))) {
                                mMSessionDetailRoot=model.getResultsList().get(0);
                                if (isValid(mMSessionDetailRoot)&&isValid(mMSessionDetailRoot.getPacifyr())) {
                                    LinearLayout_Active_Session.setVisibility(View.VISIBLE);
                                    bannerLayout.setVisibility(View.GONE);
                                    TextViewHideBanner.setVisibility(View.INVISIBLE);
                                    adjustGridBalls();
                                    boolean isVideoCall = false;
                                    isVideoCall = !mMSessionDetailRoot.getSessionType().equals("audio");
                                    boolean finalIsVideoCall = isVideoCall;
                                    JoinCall.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            checkActiveSessions(true);


                                        }
                                    });

                                    JoinChat.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            checkActiveSessions(false);

                                        }
                                    });
                                }
                                else {
                                    LinearLayout_Active_Session.setVisibility(View.GONE);
                                }
                            }
                            else {
                                LinearLayout_Active_Session.setVisibility(View.GONE);
                                if (isValid(mMSessionDetailRoot))
                                {
                                    if (!mMSessionDetailRoot.isBilledSession())
                                    {
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toaster.shortToast("Thank you for the call. You are not billed as service was not provided");
                                            }
                                        });

                                    }
                                    mMSessionDetailRoot=null;
                                }
                            }
                        }
                        else {
                            LinearLayout_Active_Session.setVisibility(View.GONE);
                            if (isValid(mMSessionDetailRoot))
                            {
                                if (!mMSessionDetailRoot.isBilledSession())
                                {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toaster.shortToast("Thank you for the call. You are not billed as service was not provided");
                                        }
                                    });                                }
                                mMSessionDetailRoot=null;
                            }
                        }

                        if (LinearLayout_Active_Session.getVisibility() == View.GONE &&
                                bannerLayout.getVisibility() == View.GONE) {
                            setUpBanner();
                        }

                    } catch (Exception e) {
                        hideProgress();
                        e.printStackTrace();
                    }
                }
            };
            AQuery aq = new AQuery(getActivity());
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", token);
            aq.ajax(url, JSONObject.class, cb);
        }catch (Exception e){
            hideProgress();
            e.printStackTrace();
        }
    }



    private void checkActiveSessions(boolean isCall) {
        try {

            PrefManager prefM = PrefManager.getInstance(getActivity());
            String token = prefM.getAccessToken();
            String userid = prefM.getUserID();
            String url = GLOBAL_URL + "session/customer/active";
            setLoading();
            showProgress();
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    Auth.httpStatusFilter(getActivity(), status.getCode());
                    try {
                        hideProgress();
                        if (isValid(json)) {
                            ModelSessonsOfCustomer model = GsonUtils.getInstance().gsonToModelSessonsOfCustomer(json);

                            if (isValid(model) && isValid(model.getResultsList())&&isValid(model.getResultsList().get(0))) {
                                MSessionDetailRoot mMSessionDetailRoot=model.getResultsList().get(0);
                                if (isValid(mMSessionDetailRoot)&&isValid(mMSessionDetailRoot.getPacifyr()))
                                {
                                    LinearLayout_Active_Session.setVisibility(View.VISIBLE);
                                    boolean isVideoCall=false;
                                    isVideoCall = !mMSessionDetailRoot.getSessionType().equals("audio");
                                    boolean finalIsVideoCall = isVideoCall;
                                   if (isCall)
                                   {
                                       ((DashboardActivity)getActivity()).rejoinToTwilioRoom(mMSessionDetailRoot.getPacifyr(),finalIsVideoCall,false,null,mMSessionDetailRoot.getTwilioRoomName(),mMSessionDetailRoot.getId());

                                   }
                                   else {
                                       ((DashboardActivity)getActivity()).startChat(mMSessionDetailRoot.getPacifyr());
                                   }
                                }
                                else {
                                    LinearLayout_Active_Session.setVisibility(View.GONE);
                                    Toast.makeText(getActivity(), getString(R.string.no_active_session), Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                LinearLayout_Active_Session.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), getString(R.string.no_active_session), Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            LinearLayout_Active_Session.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), getString(R.string.no_active_session), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        hideProgress();
                        e.printStackTrace();
                    }
                }
            };
            AQuery aq = new AQuery(getActivity());
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", token);
            aq.ajax(url, JSONObject.class, cb);
        }catch (Exception e){
            hideProgress();
            e.printStackTrace();
        }


    }

    private void getUnratedSessions() {
        AjaxCallback cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                /*
                 *this must be the first line of any httpCallBacks
                 */
                Auth.httpStatusFilter(getActivity(),status.getCode());
                try {
                    if (json != null) {
                        JSONArray results = json.getJSONArray("results");
                        if (results != null && results.length() > 0) {
                            Session session = new Gson().fromJson(results.getJSONObject(0).toString(), Session.class);
                            if (session != null) {
                                SessionSummaryActivity.start(getContext(), session.getId());
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


    private void checkLastTimeLoginFiftyDaysExcceed() {
       try{
            long mLastTimeLoginTimeStamp=prefM.getLoginTimeStamp();//1606362531198
        long mCurrentDateTimeStamp=System.currentTimeMillis();

        long defference=mCurrentDateTimeStamp - mLastTimeLoginTimeStamp;

        if (defference > Long.parseLong(TOKEN_EXPIRY_TIME))// 60 days over
        {
            Auth.httpStatusFilter(getActivity(), 401);//logout user
        } else if (defference > Long.parseLong(TOKEN_REFRESH_TIME)) {
            //50days esceded
            callRefreshAccessToken();
        } else {

           onResumeCommonAPICalls();
        }
       }catch (Exception e)
       {
           Log.w("wwww",e.toString());
       }
    }

    private void onResumeCommonAPICalls() {
       // setLoading();
      // showProgress();
        handleActionCustomerDashboard();
        if (isValid(call_emotion)) {
            // feel_err_text.setVisibility(View.GONE);
            String emotionImg = GLOBAL_IMAGE_URL + call_emotion.getImage();
            String emotionTitle = call_emotion.getTitle();
            EmotionTitle = emotionTitle;
            if (isValidhtml(emotionImg)) {
                Glide.with(getActivity()).load(emotionImg).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .transition(DrawableTransitionOptions.withCrossFade()).into(feelImageDimv);
                float brightness = (float) 100;
                float[] colorMatrix = {
                        0.33f, 0.33f, 0.33f, 0, brightness, //red
                        0.33f, 0.33f, 0.33f, 0, brightness, //green
                        0.33f, 0.33f, 0.33f, 0, brightness, //blue
                        0, 0, 0, 1, 0    //alpha
                };
                ColorMatrix matrix = new ColorMatrix();
                matrix.setSaturation(0);
                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
                feelImageDimv.setColorFilter(filter);
                feelTitleCtxv.setText(emotionTitle);
                if (isValid(feel_err_text)) {
                    feel_err_text.setVisibility(View.GONE);

                }

            }
        } else {
            //feel_err_text.setVisibility(View.VISIBLE);
        }
        ((DashboardActivity)getActivity()).getUnratedSessions();
        //setChatCountInfo();
       // getChatUnreadCount();
        if (prefM != null && prefM.getCompanyLogo() != null) {
            Glide.with(getActivity())
                    .load(UtilityNetworkService.GLOBAL_IMAGE_URL + prefM.getCompanyLogo())
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(image_logo);
        }
        else{
            image_logo.setVisibility(View.GONE);
        }
    }


    private void callRefreshAccessToken() {
        try {
            showProgress();
            PrefManager prefM = PrefManager.getInstance(getActivity());
            String token = prefM.getAccessToken();
            String url = NetworkService.GLOBAL_URL + "refreshToken";
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    try {
                        hideProgress();
                        if(json!=null) {
                            if (json.has("accessToken"))
                            {
                                String updatedAccessToken=json.getString("accessToken");
                                prefM.saveAccessToken(updatedAccessToken);
                                /**
                                 * update timestamp after 50days need to refresh the token**/
                                long mTimeStamp = System.currentTimeMillis();
                                prefM.saveLoginTimeStamp(String.valueOf(mTimeStamp));
                                onResumeCommonAPICalls();
                            }
                            else {
                                if (json.has("status"))
                                {
                                    String mStatus=json.getString("status");
                                    if (mStatus.equals("error"))
                                    {
                                        Auth.httpStatusFilter(getActivity(), status.getCode());
                                    }
                                }
                            }
                            Log.w("EWERE",String.valueOf(json));
                            Log.w("EWERE",String.valueOf(json));
                        }
                    } catch (Exception e) {
                        hideProgress();
                        Auth.httpStatusFilter(getActivity(), status.getCode());
                    }
                }
            };
            AQuery aq = new AQuery(getActivity());
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", token);
            aq.ajax(url, JSONObject.class, cb);
        }
        catch (Exception ex)
        {
            hideProgress();
            ex.printStackTrace();
        }



    }
boolean itIsFirstCallFrom=true;
    private void handleActionCustomerDashboard(){

        prefM= PrefManager.getInstance(getContext());
        String token=prefM.getAccessToken();
        String userid=prefM.getUserID();
        String url = GLOBAL_URL+"dashboard/customer/"+userid;

        AjaxCallback cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {

                Auth.httpStatusFilter(getContext(), status.getCode());
                try {
                    hideProgress();
                    ModelCustomerDashboard2 m = modelCustomerDashboard2;
                    modelCustomerDashboard2 = GsonUtils.getInstance().gsonToModelCustomerDashboard2(json);
                   // if (!m.equals(modelCustomerDashboard2))
                    setMyAdapter();


                    } catch(Exception e){
                        e.printStackTrace();
                        // hideProgress();
                    }

            }
        };
        try {
            hideProgress();
            AQuery aq = new AQuery(getContext());
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", token);
            //aq.post(url, jsonObject, JSONObject.class, cb);
            aq.ajax(url,JSONObject.class, cb);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
           //hideProgress();
        }
        hideProgress();
    }

    @Subscribe
    public void onNotifyEvent(NotifyEvent event) {

        try {
           //Toast.makeText(getContext(),"hiii",Toast.LENGTH_LONG).show();
         //   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
               setMyAdapter();
          //  }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Log.d("ON RESUME;",ex.getMessage());
        }

    }

  //@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setMyAdapter() {
        try {

            String emotionImg = null;
            String emotionTitle = null;

            if (isValid(feelTitleCtxv))
            {
                if (EmotionTitle.equals(""))
                {
                    if(isValid(feel_err_text))
                    {feel_err_text.setVisibility(View.VISIBLE);
                        defSetText(feel_err_text,"Please select an emotion");
                    }
                }

            }

            if (isValid(modelCustomerDashboard2)) {
                final MUser[] pfc = modelCustomerDashboard2.getPreferredPacifyrs();
                ModelCustomerDashboard2.ThisWeekStatistics lastWeek = modelCustomerDashboard2.getThisWeekStatistics();
                lastSession = modelCustomerDashboard2.getLastSession();
                if(isValid(modelCustomerDashboard2.getUser().getEmotion())){
                    emotionImg =   GLOBAL_IMAGE_URL+modelCustomerDashboard2.getUser().getEmotion().getImage();
                    emotionTitle = modelCustomerDashboard2.getUser().getEmotion().getTitle();
                    EmotionTitle=emotionTitle;

                    if(isValidhtml(emotionImg)){
                       // feel_err_text.setVisibility(View.GONE);
                        Glide.with(getActivity()).load(emotionImg).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                .transition(DrawableTransitionOptions.withCrossFade()).into(feelImageDimv);

                        float brightness = (float)100;

                        float[] colorMatrix = {
                                0.33f, 0.33f, 0.33f, 0, brightness, //red
                                0.33f, 0.33f, 0.33f, 0, brightness, //green
                                0.33f, 0.33f, 0.33f, 0, brightness, //blue
                                0, 0, 0, 1, 0    //alpha
                        };

                        ColorMatrix matrix = new ColorMatrix();
                        matrix.setSaturation(0);
                        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
                        feelImageDimv.setColorFilter(filter);

                        feelTitleCtxv.setText(emotionTitle);
                        if(isValid(feel_err_text))
                        {
                            feel_err_text.setVisibility(View.GONE);

                        }
                    } else {
                      //  feel_err_text.setVisibility(View.VISIBLE);
                    }
                }

                /*for(int i=0;i<lastSession.getSessionRatings().length;i++)
                {
                    if(lastSession.getSessionRatings()[i].getType().equals("customer"))
                    {
                        emotionImg = lastSession.get;
                        emotionTitle;
                    }
                }*/




                if (isValid(lastSession)) {
                    last_session_top_llay.setVisibility(View.VISIBLE);
                    no_lastsession_ctxv.setVisibility(View.GONE);
                    /*lastSessionDimv.setVisibility(View.VISIBLE);
                    lastSessionRlay.setVisibility(View.VISIBLE);*/

                    String type = lastSession.getSessionType();
                    if (type.equals("audio")){
                        videoCamImg.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.connectaudio1));
                    }
                    else if(type.equals("video")){
                        videoCamImg.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_video_cam_blue));
                    }

                    defSetText(daysTxv,lastSession.getDaysSincePosted());

                    if (isValid(modelCustomerDashboard2.getNextAppointment()))
                    {
                       try {
                           lastSessionMinuteTxv.setVisibility(View.GONE);
                           videoCamImg.setVisibility(View.GONE);
                           daysTxv.setVisibility(View.GONE);
                           Text_appointmentTime.setVisibility(View.VISIBLE);
                           String appointMentTime=modelCustomerDashboard2.getNextAppointment().getStartTimeGMT();
                           String time = Utility.getStartFormatDate(appointMentTime,getActivity());
                           Text_appointmentTime.setText(time+"");
                           MyLastSessionTitle.setText(getResources().getString(R.string.next_appointment));
                           appointMentBackground.setBackground(getResources().getDrawable(R.drawable.solid_circle_red));
                           if (isValid(modelCustomerDashboard2.getNextAppointment().getPacifyr())
                           && isValid(modelCustomerDashboard2.getNextAppointment().getPacifyr().getImage())) {
                               Glide.with(getContext()).load(GLOBAL_IMAGE_URL + modelCustomerDashboard2.getNextAppointment().getPacifyr().getImage()).transition(DrawableTransitionOptions.withCrossFade()).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(lastSessionDimv);
                               lastSsnPacfyrCtxv.setText(modelCustomerDashboard2.getNextAppointment().getPacifyr().getFirstName() + " " + modelCustomerDashboard2.getNextAppointment().getPacifyr().getLastName());
                           }
                       }catch (Exception e)
                       {

                       }
                    }
                    else {
                        String duration = lastSession.getDuration();
                        lastSessionMinuteTxv.setText(Utility.getCorrectRoundedDuration(duration));
                        lastSessionMinuteTxv.setVisibility(View.VISIBLE);
                        videoCamImg.setVisibility(View.VISIBLE);
                        daysTxv.setVisibility(View.VISIBLE);
                        Text_appointmentTime.setVisibility(View.GONE);
                        MyLastSessionTitle.setText(getResources().getString(R.string.my_last_session));
                        appointMentBackground.setBackground(getResources().getDrawable(R.drawable.solid_circle));
                        lastSsnPacfyrCtxv.setText(lastSession.getPacifyr().getFirstName() + " " + lastSession.getPacifyr().getLastName());
                        Glide.with(getContext()).load(GLOBAL_IMAGE_URL+lastSession.getPacifyr().getImage()).transition(DrawableTransitionOptions.withCrossFade()).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(lastSessionDimv);

                    }

                }
                else {
                    last_session_top_llay.setVisibility(View.GONE);
                    no_lastsession_ctxv.setVisibility(View.VISIBLE);
                /*    lastSessionDimv.setVisibility(View.INVISIBLE);
                    lastSessionRlay.setVisibility(View.INVISIBLE);
                    daysTxv.setVisibility(View.INVISIBLE);
                    lastSsnPacfyrCtxv.setVisibility(View.VISIBLE);*/
                    defSetText(no_lastsession_ctxv,getResources().getString(R.string.no_sessions_yet));

                }

                if (isValid(lastWeek)) {
                    String[] split = lastWeek.getDuration().split(":");
                    sessionsTxv.setText(lastWeek.getTotalSessions());
                    hoursTxv.setText(split[0]);
                    minutesCtxv.setText(" "+split[1]);
                    PrefManager prefM = PrefManager.getInstance(getContext());
                    String country = prefM.getCountry();
                    String sign ="$";
                    if(country.equals("India")){
                        sign=getContext().getResources().getString(R.string.rupee);
                    }else {
                        sign=getContext().getResources().getString(R.string.dollar);
                    }
                    paidTxv.setText(sign+lastWeek.getSpendings());
                }
                else {

                }

                if(isValid(pfc)) {

                    Log.i("preffffffa1","p: "+pfc);
                    if(isValid(pfc.length)) {
                        Log.i("preffffffb1","p: "+pfc.length);
                        llayTop.setVisibility(View.VISIBLE);
                        llay_bottom.setVisibility(View.VISIBLE);
                        no_pacifyr_ctxv.setVisibility(View.GONE);

                        if (!isValid(pfc.length))
                        {
                            pacifyr3Dimv.setVisibility(View.GONE);
                            pacifyr2Dimv.setVisibility(View.GONE);
                            pacifyr1Dimv.setVisibility(View.GONE);
                            pacifyr4Dimv.setVisibility(View.GONE);
                            llayTop.setVisibility(View.GONE);
                            llay_bottom.setVisibility(View.GONE);
                            no_pacifyr_ctxv.setVisibility(View.VISIBLE);
                            defSetText(no_pacifyr_ctxv,getResources().getString(R.string.no_prefered_pacifyr));
                        }
                        else {
                            pacifyr3Dimv.setVisibility(View.GONE);
                            pacifyr2Dimv.setVisibility(View.GONE);
                            pacifyr1Dimv.setVisibility(View.GONE);
                            pacifyr4Dimv.setVisibility(View.GONE);
                            for (int i=0;i<pfc.length;i++)
                            {
                                if (i==0)
                                {
                                    pacifyr1Dimv.setVisibility(View.VISIBLE);
                                    showPrefferedPacifyrIntoGlideView(pfc[0].getImageUrl(),pacifyr1Dimv);
                                }
                                else if (i==1)
                                {
                                    pacifyr2Dimv.setVisibility(View.VISIBLE);
                                    showPrefferedPacifyrIntoGlideView(pfc[1].getImageUrl(),pacifyr2Dimv);
                                }
                                else if (i==2)
                                {
                                    pacifyr3Dimv.setVisibility(View.VISIBLE);
                                    showPrefferedPacifyrIntoGlideView(pfc[2].getImageUrl(),pacifyr3Dimv);
                                }
                                else if (i==3)
                                {
                                    pacifyr4Dimv.setVisibility(View.VISIBLE);
                                    showPrefferedPacifyrIntoGlideView(pfc[3].getImageUrl(),pacifyr4Dimv);
                                }
                                else {
                                    return;
                                }
                            }
                        }

                    }
                    else {
                        llayTop.setVisibility(View.GONE);
                        llay_bottom.setVisibility(View.GONE);
                        no_pacifyr_ctxv.setVisibility(View.VISIBLE);
                        defSetText(no_pacifyr_ctxv,getResources().getString(R.string.no_prefered_pacifyr));
                    }
                }
                else {
                    llayTop.setVisibility(View.GONE);
                    llay_bottom.setVisibility(View.GONE);
                    no_pacifyr_ctxv.setVisibility(View.VISIBLE);
                    defSetText(no_pacifyr_ctxv,getResources().getString(R.string.no_prefered_pacifyr));
                }
            }

        }catch (Exception ex)
        {
            ex.printStackTrace();
            Log.d("ON RESUME;",ex.getMessage());
        }
    }

    private void showPrefferedPacifyrIntoGlideView(String imageURl,ImageView imageView) {

        Glide.with(getActivity())
                .load(imageURl)
                .listener(new RequestListener<Drawable>() {

                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .dontTransform()
                .into(imageView);
    }


    /*private void showTheRoundDialog(int position, MUser prefpac) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.selction_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        // set the custom dialog components - text, image and button
        MainTextView text = (MainTextView) dialog.findViewById(R.id.name_in_dialog_ctxv);
        MainTextView sel_speciality_ctxv = (MainTextView) dialog.findViewById(R.id.sel_speciality_ctxv);
        MainTextView no_review_ctxv = (MainTextView) dialog.findViewById(R.id.no_review_ctxv);
        RatingBar dialog_rating=(RatingBar) dialog.findViewById(R.id.dialog_rating);

        no_review_ctxv.setText(prefpac.getNumberOfReviews()+getResources().getString(R.string.reviews));
        text.setText(prefpac.getFirstName()+" "+prefpac.getLastName());
        CircleImageView image = (CircleImageView) dialog.findViewById(R.id.sel_user_dimv);
        String spec="";
        for(int i=0;i<prefpac.getSpecialities().length;i++)
        {
            spec=spec+prefpac.getSpecialities()[i]+",";
        }
        sel_speciality_ctxv.setText(spec);
        dialog_rating.setRating(Float.parseFloat(prefpac.getRating()));
        Glide.with(getContext()).load( prefpac.getImageUrl()).into(image);
        dialog.show();
    }*/


    @Override
    public void onSetValues(View view) throws Exception {

    }


    public void intializeViews(View view)
    {
        feelRlay = view.findViewById(R.id.feel_rlay);
        feel_err_text = view.findViewById(R.id.text_feel_error);
        connect_btn_rlay = view.findViewById(R.id.connect_btn_rlay);
        //feelImage = (DeviceFitImageView) view.findViewById(R.id.feel_image_dimv);
        lastSessionRlay = view.findViewById(last_session_rlay);
        lastSessionDimv = view.findViewById(R.id.last_session_dimv);
        swipeRefreshLayout = view.findViewById(R.id.user_swipe_layout);
        last_session_top_llay = view.findViewById(R.id.last_session_top_llay);
        no_pacifyr_ctxv = view.findViewById(R.id.no_pacifyr_ctxv);
        no_lastsession_ctxv = view.findViewById(R.id.no_lastsession_ctxv);
        lastSsnPacfyrCtxv = view.findViewById(R.id.lastSsn_pacfyr_ctxv);
        videoCamImg = view.findViewById(R.id.video_cam_img);
        lastSessionMinuteTxv = view.findViewById(R.id.last_session_minute_txv);
        daysTxv = view.findViewById(R.id.days_txv);
        cirPrefPacifyr = view.findViewById(R.id.cir_pref_pacifyr);
        llayTop = view.findViewById(R.id.llay_top);
        image_logo = view.findViewById(R.id.image_logo);

        llay_bottom = view.findViewById(R.id.llay_bottom);
        pacifyr1Dimv = view.findViewById(R.id.pacifyr1_dimv);
        pacifyr2Dimv = view.findViewById(R.id.pacifyr2_dimv);
        llayBottom = view.findViewById(R.id.llay_bottom);
        pacifyr3Dimv = view.findViewById(R.id.pacifyr3_dimv);
        pacifyr4Dimv = view.findViewById(R.id.pacifyr4_dimv);
        thisWeekRlay = view.findViewById(R.id.this_week_rlay);
        sessionsTxv = view.findViewById(R.id.sessions_txv);
        hoursTxv = view.findViewById(R.id.hours_txv);
        minutesCtxv = view.findViewById(R.id.min_ctxv);
        paidTxv = view.findViewById(R.id.paid_amt_ctxv);
        this_week_rlay = view.findViewById(R.id.this_week_rlay);

        feelImageDimv = view.findViewById(R.id.feel_image_dimv);
        feelTitleCtxv = view.findViewById(R.id.feel_title_ctxv);
        unreadChatCtxv = view.findViewById(R.id.unread_chat_ctxv);
        LinearLayout_Active_Session=view.findViewById(R.id.LinearLayout_Active_Session);
        JoinCall=view.findViewById(R.id.JoinCall);
        JoinChat=view.findViewById(R.id.JoinChat);
        defSetText(no_pacifyr_ctxv,"");
        //setChatCountInfo();
        getChatUnreadCount();
    }

    public void getChatUnreadCount(){
        //setLoading();
        //showProgress();



        String userToken=prefM.getAccessToken();


        String url = NetworkService.GLOBAL_URL+"messages/unreadCount";
        AjaxCallback cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                try {
                   // hideProgress();
                    Log.d("TWILIO", "Response from server: " + json);
                    Log.d("TWILIO", "unreadCount userToken: " + userToken+ "url: " +url);
                    Gson gson = new Gson();
                    if (isValid(json))
                    {
                        UnreadCountModel unreadCountModel = gson.fromJson(json.toString(), UnreadCountModel.class);

                        if (isValid(unreadCountModel) && unreadCountModel.status.equals("success"))
                        {
                            unreadCount=unreadCountModel.count;
                            badgeCount = 0;
                            if (isValid(unreadCount)){
                                if(unreadCount > 0)
                                {
                                    unreadChatCtxv.setVisibility(View.VISIBLE);
                                    unreadChatCtxv.setText(unreadCount + " unread messages");
                                    badgeCount+=unreadCount;
                                    prefM.saveBadgeValue(badgeCount);
                                    // ShortcutBadger.applyCount(getActivity(), prefM.getBadgeValue());

                                }else {
                                    isClickedOnChat=true;
                                    unreadChatCtxv.setVisibility(View.GONE);
                                    prefM.saveBadgeValue(badgeCount);
                                    // ShortcutBadger.applyCount(getActivity(), 0);
                                }
                            }
                        }

                    }


                } catch (Exception e) {
                   // hideProgress();
                    e.printStackTrace();

                }
            }
        };
        AQuery aq = new AQuery(getActivity());
        cb.header("Content-Type", "application/json");
        cb.header("AccessToken",userToken);
        try {
            aq.ajax(url, JSONObject.class, cb);

        }
        catch (Exception ex)
        {
            //hideProgress();
            ex.printStackTrace();
        }
    }


    @org.greenrobot.eventbus.Subscribe(threadMode = ThreadMode.MAIN)
    public void messageEventFromService(EventMessage event){
       /* @Subscribe
        public void onNotifyEvent(NotifyEvent event) {*/

    }

    private final BroadcastReceiver notificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.hasExtra("message")) {
                String message = intent.getExtras().getString("message");
                String type = intent.getExtras().getString("type");
                if (message != null && message != "") {
                    if(type.equals("twilio.conversations.new_message") || type.equals("twilioChat") ){
                        getChatUnreadCount();
                    }

                }
            }
        }
    };



    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                try {
                    Log.w("hai","onRefresh........");
                    swipeRefreshLayout.setRefreshing(false);
                    setLoading();
                    showProgress();
                    checkIsThereAnyActiveSessionsAvailable();
                    handleActionCustomerDashboard();
                    //setChatCountInfo();
                    getChatUnreadCount();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 2000);
    }

/**actionbar connectprovider from dashboard actionbar event **/

    @org.greenrobot.eventbus.Subscribe(threadMode = ThreadMode.MAIN)
    public void onConnectProviderClicked(ConnectEvent event) {
        try {
            if (isValid(event) && event.isConnect()) {

                ConnectToPacifyrActivity.start(getActivity());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
