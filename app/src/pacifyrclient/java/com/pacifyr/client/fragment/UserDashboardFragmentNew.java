package com.pacifyr.client.fragment;

import static com.pacifyr.client.R.id.grid_balls;
import static com.pacifyr.client.R.id.last_session_rlay;
import static com.pacifyr.client.R.id.viewpager;
import static com.pacifyr.client.R.id.visible;
import static com.pacifyr.client.base.PacifyrActivity.call_emotion;
import static com.pacifyr.client.network.NetworkService.lastSession;
import static com.pacifyr.client.network.NetworkService.modelAllEmotions;
import static com.pacifyr.client.network.NetworkService.modelCustomerDashboard2;
import static com.pacifyr.client.twilio.VideoActivity.isCallActive;
import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.GLOBAL_IMAGE_URL;
import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.GLOBAL_URL;
import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.TOKEN_EXPIRY_TIME;
import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.TOKEN_REFRESH_TIME;

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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.pacifyr.client.R;
import com.pacifyr.client.adapter.ResourcesDashBoardAdapter;
import com.pacifyr.client.adapter.ResourcesListAdapter;
import com.pacifyr.client.adapter.ResourcesListMainAdapter;
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
import com.pacifyr.client.model.Banners;
import com.pacifyr.client.model.DashBoardResource;
import com.pacifyr.client.model.ResourceMain;
import com.pacifyr.client.model.ResourceSpecialityList;
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
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserDashboardFragmentNew extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {


    private static final String TAG = "DashboardFragmentNew";
    private PrefManager prefM;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    private void getPermissions() {
        Utility.checkPermission(getActivity(), "essential");
    }
    Timer _TimerRefreshDashBoard=null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefM = PrefManager.getInstance(getContext());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    private LinearLayout session_llay;
    private TextView Text_appointmentTime,MyLastSessionTitle;
    private RelativeLayout appointMentBackground;
    private ImageView feelImageDimv;
    private MainTextView feelTitleCtxv,feellabel;
    private MainTextView feel_err_text;
    private CircleImageView pacifyr1Dimv;
    private MainTextView lastSsnPacfyrCtxv,no_pacifyr_ctxv,no_lastsession_ctxv,fav_provider_name;
    private MainTextView sessionsTxv,sessionCostTotal;
    private MainTextView daysTxv;
    private CircleImageView lastSessionDimv;
    private ImageView ImageView_right_arrow;
    private TextView TextView_msg_Provider_name,TextView_msg,TextView_msg_time,unread_chat_ctxv,next_app_provider_name,next_app_category,next_app_time;
    private CardView CardView_unReadMessageLayout;
    private RecyclerView resources_list_rcv;
    private CircleImageView Img_next_app_provider;
    private CardView CardView_nextAppointment;
    private TextView TextView_ViewAll_appointment,TextView_ViewAll;

    private MUser prefpac;
    private LinearLayout LinearLayout_Active_Session;
    TextView JoinChat,JoinCall;
    private ViewPager ViewPager_bannerLayout;
    private CircleImageView ImageView_next,ImageView_msg_Provider;
    private FloatingActionButton connect_action_fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_user_dashboard_new,container,false);


        feelImageDimv = view.findViewById(R.id.feel_image_dimv);
        feelTitleCtxv = view.findViewById(R.id.feel_title_ctxv);
        feellabel= view.findViewById(R.id.feellabel);
        feel_err_text = view.findViewById(R.id.text_feel_error);
        pacifyr1Dimv = view.findViewById(R.id.pacifyr1_dimv);
        no_pacifyr_ctxv = view.findViewById(R.id.no_pacifyr_ctxv);
        no_lastsession_ctxv = view.findViewById(R.id.no_lastsession_ctxv);
        fav_provider_name= view.findViewById(R.id.fav_provider_name);

        sessionsTxv = view.findViewById(R.id.sessions_txv);
        sessionCostTotal = view.findViewById(R.id.sessionCostTotal);
        daysTxv = view.findViewById(R.id.days_txv);
        lastSessionDimv = view.findViewById(R.id.last_session_dimv);
        ImageView_msg_Provider= view.findViewById(R.id.ImageView_msg_Provider);
        TextView_msg_Provider_name= view.findViewById(R.id.TextView_msg_Provider_name);
        TextView_msg= view.findViewById(R.id.TextView_msg);
        TextView_msg_time= view.findViewById(R.id.TextView_msg_time);
        unread_chat_ctxv= view.findViewById(R.id.unread_chat_ctxv);
        ImageView_right_arrow= view.findViewById(R.id.ImageView_right_arrow);
        CardView_unReadMessageLayout= view.findViewById(R.id.CardView_unReadMessageLayout);
        resources_list_rcv= view.findViewById(R.id.resources_list_rcv);
        Img_next_app_provider= view.findViewById(R.id.Img_next_app_provider);
        next_app_provider_name= view.findViewById(R.id.next_app_provider_name);
        next_app_category= view.findViewById(R.id.next_app_category);
        next_app_time= view.findViewById(R.id.next_app_time);
        CardView_nextAppointment=view.findViewById(R.id.CardView_nextAppointment);
        TextView_ViewAll_appointment=view.findViewById(R.id.TextView_ViewAll_appointment);
        TextView_ViewAll=view.findViewById(R.id.TextView_ViewAll);

        LinearLayout_Active_Session=view.findViewById(R.id.LinearLayout_Active_Session);
        JoinCall=view.findViewById(R.id.JoinCall);
        JoinChat=view.findViewById(R.id.JoinChat);
        ViewPager_bannerLayout=view.findViewById(R.id.bannerLayout);
        ImageView_next=view.findViewById(R.id.ImageView_next);
        connect_action_fab=view.findViewById(R.id.connect_action_fab);


        CardView_unReadMessageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //DialogsActivity.start(getActivity());
                if (!((DashboardActivity)getActivity()).isInternetAvailable()) {
                    ((DashboardActivity) getActivity()).showInternetNotAvailablePopup();
                    return;
                }


                TwilioCoversationListActivity.start(getActivity(),false);
                unreadCount=0;
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
                if (isValid(modelCustomerDashboard2)&&isValid(modelCustomerDashboard2.getPreferredPacifyrs()[modelCustomerDashboard2.getPreferredPacifyrs().length-1]))
                {
                    prefpac = modelCustomerDashboard2.getPreferredPacifyrs()[modelCustomerDashboard2.getPreferredPacifyrs().length-1];
                    if (isValid(prefpac))
                        ConnectToSinglePacifyrActivity.start(getActivity(), prefpac);
                }



                // showTheRoundDialog(0, prefpac);
            }
        });

        feelImageDimv.setOnClickListener(new View.OnClickListener() {
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
        TextView_ViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!((DashboardActivity)getActivity()).isInternetAvailable()) {
                    ((DashboardActivity)getActivity()).showInternetNotAvailablePopup();
                    return;
                }
                ((DashboardActivity)getActivity()).hideRightLayActionbar();
                ((DashboardActivity)getActivity()).showFragment(new ResourcesFragment());
            }
        });

        lastSessionDimv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (!((DashboardActivity)getActivity()).isInternetAvailable()) {
                        ((DashboardActivity) getActivity()).showInternetNotAvailablePopup();
                        return;
                    }
                    if (isValid(lastSession)&&isValid(lastSession.getId()))
                    {
                        String sessionID = lastSession.getId();
                        LastSessionActivity.start(getActivity(), sessionID, true);
                    }
                    else {
                        ConnectToPacifyrActivity.start(getActivity());
                    }



                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }

            }
        });

        TextView_ViewAll_appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValid(modelCustomerDashboard2.getNextAppointment()))
                {
                    ((DashboardActivity)getActivity()).showAppointmentPage();
                }
            }
        });


        sessionsTxv.setOnClickListener(new View.OnClickListener() {
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

        ImageView_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValid(ViewPager_bannerLayout) )
                {
                    if(isValid(ViewPager_bannerLayout.getCurrentItem())) {
                        if (ViewPager_bannerLayout.getCurrentItem() < ViewPager_bannerLayout.getAdapter().getCount() - 1)
                            ViewPager_bannerLayout.setCurrentItem(ViewPager_bannerLayout.getCurrentItem() + 1);
                        else
                            ViewPager_bannerLayout.setCurrentItem(0, true);
                    }
                    else {
                        ViewPager_bannerLayout.setCurrentItem(1, true);
                    }

                }
            }
        });

        connect_action_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!((DashboardActivity)getActivity()).isInternetAvailable()) {
                    ((DashboardActivity) getActivity()).showInternetNotAvailablePopup();
                    return;
                }
                ((DashboardActivity) getActivity()).hideRightLayActionbar();
                ((DashboardActivity) getActivity()).closePane(true);
                ConnectToPacifyrActivity.start(getActivity());
            }
        });
        return view;
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

    private static boolean isVisible=false;
    @Override
    public void onResume() {
        super.onResume();
        isVisible=true;
        ((DashboardActivity) getActivity()).changeActionBarTitleForFragments(getResources().getString(R.string.nav_mydashbord_title));
        ((DashboardActivity) getActivity()).showConnectButton();
        if (getActivity() instanceof DashboardActivity) {
            ((DashboardActivity) getActivity()).changeBottomMenuIcon(0);
        }
        try {

            checkLastTimeLoginFiftyDaysExcceed();
            loadResources();
            /**refresh dashboard every 5 seconds for checking available active sessions **/

            _TimerRefreshDashBoard = new Timer();
            _TimerRefreshDashBoard.scheduleAtFixedRate(new TimerTask() {

                @Override
                public void run() {
                   checkIsThereAnyActiveSessionsAvailable();
                    handleActionCustomerDashboard();
                   // getChatUnreadCount();
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
                                    //bannerLayout.setVisibility(View.GONE);
                                   // TextViewHideBanner.setVisibility(View.INVISIBLE);

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

                        /*if (LinearLayout_Active_Session.getVisibility() == View.GONE &&
                               // bannerLayout.getVisibility() == View.GONE) {
                            //setUpBanner();
                        }*/

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


    public Integer unreadCount =0;
    private boolean isClickedOnChat=false;
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
                            if (isValid(unreadCount)){
                                if(unreadCount > 0)
                                {
                                    unread_chat_ctxv.setVisibility(View.VISIBLE);
                                    unread_chat_ctxv.setText(unreadCount + "Missed Messages");
                                    CardView_unReadMessageLayout.setVisibility(View.VISIBLE);


                                }else {
                                    isClickedOnChat=true;
                                    unread_chat_ctxv.setVisibility(View.GONE);
                                    CardView_unReadMessageLayout.setVisibility(View.GONE);
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

    List<ResourceSpecialityList.Result> resourcesList;
    private void loadResources() {
        try {
            setLoading();
            showProgress();
            prefM = PrefManager.getInstance(getContext());
            String token = prefM.getAccessToken();
            String url = GLOBAL_URL + "dashboard/resources/"+prefM.getUserID();

            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    Auth.httpStatusFilter(getContext(), status.getCode());
                    try {
                        hideProgress();
                        resourcesList = GsonUtils.getInstance().gsonToModelResourcesForDashBoard(json).getResults();
                        if (isValid(resourcesList)) {
                            resources_list_rcv.setVisibility(View.VISIBLE);
                            setResourcesAdapter();
                        }
                        else
                        {
                            resources_list_rcv.setVisibility(View.GONE);
                        }

                    } catch (Exception e) {
                        hideProgress();
                        e.printStackTrace();
                        resources_list_rcv.setVisibility(View.GONE);
                    }
                }
            };
            AQuery aq = new AQuery(getContext());
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

    private void setResourcesAdapter() {
        try{
            hideProgress();
            ResourcesDashBoardAdapter resourcesAdapter = new ResourcesDashBoardAdapter(resourcesList, getActivity());
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false);
            resources_list_rcv.setLayoutManager(mLayoutManager);
            resources_list_rcv.setItemAnimator(new DefaultItemAnimator());
            resources_list_rcv.setAdapter(resourcesAdapter);
        }catch (Exception e)
        {
            hideProgress();
            e.printStackTrace();
        }
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

    public String EmotionTitle="";
    private void onResumeCommonAPICalls() {
        // setLoading();
        // showProgress();
        if (isValid(call_emotion)) {
            // feel_err_text.setVisibility(View.GONE);
            String emotionImg = GLOBAL_IMAGE_URL + call_emotion.getImage();
            String emotionTitle = call_emotion.getTitle();
            EmotionTitle = emotionTitle;
            if (isValidhtml(emotionImg)) {
                Glide.with(getActivity()).load(emotionImg).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .transition(DrawableTransitionOptions.withCrossFade()).into(feelImageDimv);
                feelTitleCtxv.setText(emotionTitle);
                if (isValid(feel_err_text)) {
                    feel_err_text.setVisibility(View.GONE);
                    feellabel.setVisibility(View.VISIBLE);

                }

            }
        } else {
            //feel_err_text.setVisibility(View.VISIBLE);
        }
        ((DashboardActivity)getActivity()).getUnratedSessions();
        //setChatCountInfo();
        // getChatUnreadCount();
       /* if (prefM != null && prefM.getCompanyLogo() != null) {
            Glide.with(getActivity())
                    .load(UtilityNetworkService.GLOBAL_IMAGE_URL + prefM.getCompanyLogo())
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(image_logo);
        }
        else{
            image_logo.setVisibility(View.GONE);
        }*/
    }


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
                        feellabel.setVisibility(View.GONE);
                    }
                }

            }

            if (isValid(modelCustomerDashboard2)) {
                final MUser[] pfc = modelCustomerDashboard2.getPreferredPacifyrs();
                ModelCustomerDashboard2.ThisWeekStatistics lastWeek = modelCustomerDashboard2.getThisWeekStatistics();
                lastSession = modelCustomerDashboard2.getLastSession();
                if (isValid(modelCustomerDashboard2.getUser().getEmotion())) {
                    emotionImg = GLOBAL_IMAGE_URL + modelCustomerDashboard2.getUser().getEmotion().getImage();
                    emotionTitle = modelCustomerDashboard2.getUser().getEmotion().getTitle();
                    EmotionTitle = emotionTitle;

                    if (isValidhtml(emotionImg)) {
                        feelImageDimv.setVisibility(View.VISIBLE);
                        feellabel.setVisibility(View.VISIBLE);
                        Picasso.with(getActivity()).load(emotionImg).into(feelImageDimv);
                        feelTitleCtxv.setText(emotionTitle);
                        if (isValid(feel_err_text)) {
                            feel_err_text.setVisibility(View.GONE);


                        }
                    } else {
                        //  feel_err_text.setVisibility(View.VISIBLE);
                    }
                }


                if (isValid(lastSession)) {
                  //  last_session_top_llay.setVisibility(View.VISIBLE);
                    no_lastsession_ctxv.setVisibility(View.GONE);
                  ///  lastSessionDimv.setVisibility(View.VISIBLE);
                   // lastSessionRlay.setVisibility(View.VISIBLE);

                    String type = lastSession.getSessionType();
                    if (type.equals("audio")){
                       // videoCamImg.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.connectaudio1));
                    }
                    else if(type.equals("video")){
                       // videoCamImg.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_video_cam_blue));
                    }

                    defSetText(daysTxv,lastSession.getDaysSincePosted());
                    showPrefferedPacifyrIntoGlideView(GLOBAL_IMAGE_URL+lastSession.getPacifyr().getImage(), lastSessionDimv);
                    if (isValid(modelCustomerDashboard2.getNextAppointment()))
                    {
                        try {

                            String appointMentTime=modelCustomerDashboard2.getNextAppointment().getStartTimeGMT();
                            String time = Utility.getStartFormatDate(appointMentTime,getActivity());
                            next_app_time.setText(time+"");
                            if (isValid(modelCustomerDashboard2.getNextAppointment().getPacifyr())
                                    && isValid(modelCustomerDashboard2.getNextAppointment().getPacifyr().getImage())) {
                                Glide.with(getContext()).load(GLOBAL_IMAGE_URL + modelCustomerDashboard2.getNextAppointment().getPacifyr().getImage()).transition(DrawableTransitionOptions.withCrossFade()).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(Img_next_app_provider);
                                next_app_provider_name.setText(modelCustomerDashboard2.getNextAppointment().getPacifyr().getFirstName() + " " + modelCustomerDashboard2.getNextAppointment().getPacifyr().getLastName());
                                CardView_nextAppointment.setVisibility(View.VISIBLE);
                            }
                            if (isValid(modelCustomerDashboard2.getNextAppointment().getPacifyr()))
                            {
                                if (isValid(modelCustomerDashboard2.getNextAppointment().getPacifyr().getSpecialities())){
                                    StringBuffer buffer=new StringBuffer();
                                    for (int i=0;i<modelCustomerDashboard2.getNextAppointment().getPacifyr().getSpecialities().length;i++ )
                                    {
                                        buffer.append(modelCustomerDashboard2.getNextAppointment().getPacifyr().getSpecialities()[i]);
                                        if (i!=modelCustomerDashboard2.getNextAppointment().getPacifyr().getSpecialities().length-1)
                                        {
                                            buffer.append(", ");
                                        }

                                    }
                                    next_app_category.setText(buffer.toString());
                                }
                            }

                        }catch (Exception e)
                        {

                        }
                    }
                    else {
                        CardView_nextAppointment.setVisibility(View.GONE);
                        String duration = lastSession.getDuration();
                      //  lastSessionMinuteTxv.setText(Utility.getCorrectRoundedDuration(duration));
                       // lastSessionMinuteTxv.setVisibility(View.VISIBLE);
                       // videoCamImg.setVisibility(View.VISIBLE);
                        daysTxv.setVisibility(View.VISIBLE);


                    }

                }
                else {
                    no_lastsession_ctxv.setVisibility(View.VISIBLE);
                    sessionsTxv.setVisibility(View.GONE);
                    daysTxv.setVisibility(View.GONE);
                    defSetText(no_lastsession_ctxv,getResources().getString(R.string.no_sessions_yet));

                }
                if (isValid(lastWeek)) {
                    String[] split = lastWeek.getDuration().split(":");
                    sessionsTxv.setText(lastWeek.getTotalSessions());
                    String hrs=split[0];
                    String mintd=" "+split[1];
                    PrefManager prefM = PrefManager.getInstance(getContext());
                    String country = prefM.getCountry();
                    String sign ="$";
                    if(country.equals("India")){
                        sign=getContext().getResources().getString(R.string.rupee);
                    }else {
                        sign=getContext().getResources().getString(R.string.dollar);
                    }
                    sessionCostTotal.setText(hrs+" hrs. "+sign+lastWeek.getSpendings());
                }
                else {

                }

                if (isValid(modelCustomerDashboard2.getTotalUnreadMessages()))
                {
                    unreadCount=modelCustomerDashboard2.getTotalUnreadMessages();
                    if (isValid(unreadCount)){
                        if(unreadCount > 0)
                        {
                            unread_chat_ctxv.setVisibility(View.VISIBLE);
                            unread_chat_ctxv.setText(unreadCount + " Missed Messages");
                            CardView_unReadMessageLayout.setVisibility(View.VISIBLE);

                            if (isValid(modelCustomerDashboard2.getLatestUnreadConversation()) &&
                            isValid(modelCustomerDashboard2.getLatestUnreadConversation().getPacifyr()))
                            {
                                ModelCustomerDashboard2.Pacifyr pacifyr=modelCustomerDashboard2.getLatestUnreadConversation().getPacifyr();

                                showPrefferedPacifyrIntoGlideView(GLOBAL_IMAGE_URL+pacifyr.getImage(), ImageView_msg_Provider);
                                if (isValid(modelCustomerDashboard2.getLatestUnreadConversation().getLastMessage()))
                                {
                                    TextView_msg.setText(modelCustomerDashboard2.getLatestUnreadConversation().getLastMessage());
                                }

                                if (isValid(modelCustomerDashboard2.getLatestUnreadConversation().getLastMessageAt()))
                                {
                                   TextView_msg_time.setText(Utility.getFormattedDate(getActivity(),modelCustomerDashboard2.getLatestUnreadConversation().getLastMessageAt()));
                                }
                                TextView_msg_Provider_name.setText(pacifyr.getFirstName()+" "+pacifyr.getLastName()+"");
                            }





                        }else {
                            isClickedOnChat=true;
                            unread_chat_ctxv.setVisibility(View.GONE);
                            CardView_unReadMessageLayout.setVisibility(View.GONE);
                            // ShortcutBadger.applyCount(getActivity(), 0);
                        }
                    }
                }

                    if (isValid(pfc)) {

                        Log.i("preffffffa1", "p: " + pfc);
                        if (isValid(pfc.length)) {
                            Log.i("preffffffb1", "p: " + pfc.length);
                            no_pacifyr_ctxv.setVisibility(View.GONE);

                            if (!isValid(pfc.length)) {

                                pacifyr1Dimv.setVisibility(View.GONE);
                                no_pacifyr_ctxv.setVisibility(View.VISIBLE);
                                defSetText(no_pacifyr_ctxv, getResources().getString(R.string.no_prefered_pacifyr));
                            } else {
                                pacifyr1Dimv.setVisibility(View.VISIBLE);
                                showPrefferedPacifyrIntoGlideView(pfc[pfc.length-1].getImageUrl(), pacifyr1Dimv);
                                fav_provider_name.setText(pfc[pfc.length-1].getFirstName()+"");

                            }

                        } else {
                            no_pacifyr_ctxv.setVisibility(View.VISIBLE);
                            defSetText(no_pacifyr_ctxv, getResources().getString(R.string.no_prefered_pacifyr));
                        }
                    } else {

                        no_pacifyr_ctxv.setVisibility(View.VISIBLE);
                        defSetText(no_pacifyr_ctxv, getResources().getString(R.string.no_prefered_pacifyr));
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


    @Override
    public void onStart() {
        super.onStart();
         }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onPause() {
        super.onPause();

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


    @Subscribe
    public void onNotifyEvent(NotifyEvent event) {



    }







    @Override
    public void onSetValues(View view) throws Exception {

    }





    @org.greenrobot.eventbus.Subscribe(threadMode = ThreadMode.MAIN)
    public void messageEventFromService(EventMessage event){
       /* @Subscribe
        public void onNotifyEvent(NotifyEvent event) {*/

    }




    @Override
    public void onRefresh() {

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

    List<Banners> banners=new ArrayList<>();
    private void setUpBanner() {

        if (isValidSize(banners))
        {
            setUpViewPager(banners);
        }else {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("banners")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    Banners mBanner=new Banners();
                                    try {

                                        String bannerUrl = null;
                                        String buttonText=null;
                                        if (isValid(document.get("imageUrl")))
                                        {
                                            bannerUrl=document.get("imageUrl").toString();
                                            mBanner.setBannerUrl(bannerUrl);
                                        }
                                        if(isValid(document.get("buttonText")))
                                        {
                                            buttonText=document.get("buttonText").toString();
                                            mBanner.setButtonText(buttonText);
                                        }


                                        String webinarUrl=null;
                                        if (isValid(document.get("url")))
                                        {
                                            webinarUrl=document.get("url").toString();
                                            mBanner.setWebinarUrl(webinarUrl);
                                        }


                                        if (isValid(document.get("text").toString()))
                                        {
                                            mBanner.setText(document.get("text").toString());
                                            // bannerTitle.setText(document.get("text").toString());
                                            //bannerTitle.setVisibility(View.VISIBLE);
                                        }

                                        List<String> countrys = (List<String>) document.get("country");
                                        if (isValid(countrys))
                                        {
                                            mBanner.setCountrys(countrys);
                                            if (countrys.contains(getPrefManager().getCountry()) ||
                                                    getPrefManager().getCountry().equals("Wakanda")) {
                                                Timestamp startDate = document.getTimestamp("startDate");
                                                Timestamp endDate = document.getTimestamp("endDate");

                                            }
                                        }

                                        banners.add(mBanner);

                                    }catch (Exception e)
                                    {
                                        Log.w(TAG, "Error getting documents.", task.getException());
                                    }
                                }
                            } else {
                                Log.w(TAG, "Error getting documents.", task.getException());
                            }

                            if (isValidSize(banners))
                            {
                                setUpViewPager(banners);
                            }
                        }
                    });
        }


    }

    ViewPagerAdapter  mViewPagerAdapter;
    private void setUpViewPager(List<Banners> banners) {
        mViewPagerAdapter = new ViewPagerAdapter(getActivity());
        ViewPager_bannerLayout.setAdapter(mViewPagerAdapter);
    }


    class ViewPagerAdapter extends PagerAdapter {

        // Context object
        Context context;
        // Layout Inflater
        LayoutInflater mLayoutInflater;


        // Viewpager Constructor
        public ViewPagerAdapter(Context context) {
            this.context = context;
            mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // return the number of images
            return banners.size();
        }


        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {
            // inflating the item.xml
            View itemView = mLayoutInflater.inflate(R.layout.item_layout_banner, container, false);

            // referencing the image view from the item.xml file
            ImageView bannerLogo = (ImageView) itemView.findViewById(R.id.bannerLogo);
            TextView bannerTitle = (TextView) itemView.findViewById(R.id.bannerTitle);
            TextView TextView_buttonText = (TextView) itemView.findViewById(R.id.TextView_buttonText);
            LinearLayout LinearLayout_webinar = (LinearLayout) itemView.findViewById(R.id.LinearLayout_webinar);
            ProgressBar _ProgressBar_load_image=(ProgressBar) itemView.findViewById(R.id._ProgressBar_load_image);

            Banners mBanner=banners.get(position);

            if (isValid(mBanner))
            {
                if (isValid(mBanner.getBannerUrl()))
                {
                    Glide.with(context)
                            .load(mBanner.getBannerUrl())
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    _ProgressBar_load_image.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    _ProgressBar_load_image.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .into(bannerLogo);
                }

                if (isValid(mBanner.getText()))
                {
                    bannerTitle.setText(mBanner.getText());
                }
                else {
                    bannerTitle.setVisibility(View.GONE);
                }

                if (isValid(mBanner.getButtonText()))
                {
                    TextView_buttonText.setText(mBanner.getButtonText());
                }

                if (isValid(mBanner.getWebinarUrl()))
                {
                    LinearLayout_webinar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mBanner.getWebinarUrl()));
                            browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(browserIntent);
                        }
                    });

                }

            }
            container.addView(itemView);
            return itemView;
        }



        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view==object;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }
    }

    public Boolean isValid(String text) {
        if (text != null) return !text.trim().equalsIgnoreCase("");
        return false;

    }

    public Boolean isValid(List list) {
        if (list != null) return list.size() > 0;
        return false;

    }

    public Boolean isValid(List list,int pos) {
        if (isValid(list))
            return list.size() >= pos;
        return false;

    }
    public Boolean isValidPos(List list,int pos) {
        if (isValid(list))
            return list.size() >= pos;
        return false;

    }
    public Boolean isValidNotNull(List list) {
        return list != null;

    }

    public Boolean isValidSize(List list) {
        if (list != null) return list.size() > 0;
        return false;

    }

    public Boolean isValid(Object object) {
        return object != null;

    }

    public Boolean isValid(int object) {
        return object > 0;

    }
}
