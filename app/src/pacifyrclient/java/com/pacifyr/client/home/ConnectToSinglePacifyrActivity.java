package com.pacifyr.client.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.android.material.snackbar.Snackbar;
import com.pacifyr.client.R;
import com.pacifyr.client.adapter.EmotionSpecialityAdapter;
import com.pacifyr.client.adapter.NegativeEmotionsAdapter;
import com.pacifyr.client.adapter.PostiveEmotionsAdapter;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.event.AlertTypeEvent;
import com.pacifyr.client.event.CallEvent;
import com.pacifyr.client.event.InternetConnectionEvent;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.fragment.schedule.ScheduleBottomSheetFragment;
import com.pacifyr.client.model.ModelSpecialityByEmotion;
import com.pacifyr.client.network.GsonUtils;
import com.pacifyr.client.network.NetworkService;
import com.pacifyr.client.quickblox.activities.PermissionsActivity;
import com.pacifyr.client.quickblox.utils.Consts;
import com.pacifyr.client.utility.PacifyrAppCompactActivity;
import com.pacifyr.client.utils.LogUtils;
import com.pacifyr.client.utils.Utility;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;
import com.pacifyrapp.utilitylibrary.model.pacifyr.ChatSubscription;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MListNegativeEmotions;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MListPositiveEmotions;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MPacifyr;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MUser;
import com.pacifyrapp.utilitylibrary.model.pacifyr.ModelUserProfile;
import com.pacifyrapp.utilitylibrary.network.UtilityNetworkService;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.pacifyr.client.network.NetworkService.GLOBAL_URL;
import static com.pacifyr.client.network.NetworkService.modelCustomerDashboard2;
import static com.pacifyr.client.quickblox.activities.CallActivity.isCallActive;

//import com.pacifyr.pacifyrapp.Fragment.SelecetFeelingFragment;

public class ConnectToSinglePacifyrActivity  extends PacifyrAppCompactActivity {

    private String pacifyrName;

    public DeviceFitImageView sadDimv;
    public DeviceFitImageView happyDimv;

    private boolean isClick=false;
    private String type;

    public RelativeLayout selectFeelingRlay;
    public RelativeLayout selectEmotionRlay;
    public RelativeLayout selectEmotionDetailRlay;
    public RelativeLayout listPacifyrRlay;

    RecyclerView emotionRcv;
    RecyclerView emotionDetailRcv;
    private MainTextView userNameCtxv;

    Button button1,button2,button3,button4;
    Button moreButton;
    public PrefManager prefM;
    public String token,userid;
    //private RecyclerView specialityRcv;
    public DeviceFitImageView emotionDimv;
    public ImageView status_indicator_dimv;
    private RelativeLayout topRlay;

    private LinearLayout chatLlay;
    public static MPacifyr modelPacifyr;
    private MainTextView chatPriceCtxv;
    private Context mContext;

    private MainTextView txt_emergency_usa;

    private ConnectToSinglePacifyrActivity activity;

    CircleImageView pacifyer_cimv;
    public MainTextView pac_name_ctxv,pac_des_ctxv,pac_rate_ctxv,status_indicator_ctxv;
    //static  RespSearchPacifyrWOption.Results pacifyer_obj=null;
    static MUser pacifyer_obj=null;
    RelativeLayout videocall_rlay,audiocall_rlay,chat_rlay,schedule_rlay;
   // private PermissionsChecker checker;
    public SwipeRefreshLayout pacifyr_swipelay;
    private RelativeLayout buttonRlay;
    private Timer trackTimer_check_online;
    private TimerTask trackTimerTask_check_online;
    private ImageView image_logo;

    public static void start(Context context, MUser results) {
        Intent intent = new Intent(context, ConnectToSinglePacifyrActivity.class);
        pacifyer_obj=null;
        EmotionSpecialityAdapter.reason_forcall=null;
        if(  results!=null)
        intent.putExtra("display",results.getFirstName());
        pacifyer_obj=results;
        NetworkService.isConnectSinglePacifyer=true;
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void start(Context context, MUser results, MPacifyr pacifyr) {
        Intent intent = new Intent(context, ConnectToSinglePacifyrActivity.class);
        pacifyer_obj=null;
        modelPacifyr = pacifyr;
        EmotionSpecialityAdapter.reason_forcall=null;
        if(  results!=null)
            intent.putExtra("display",results.getFirstName());
        pacifyer_obj=results;
        NetworkService.isConnectSinglePacifyer=true;
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void start(Context context, MUser results, boolean isConnectSinglePacifyer) {
        Intent intent = new Intent(context, ConnectToSinglePacifyrActivity.class);
        pacifyer_obj=null;
        EmotionSpecialityAdapter.reason_forcall=null;
        if(  results!=null)
        intent.putExtra("display",results.getFirstName());
        pacifyer_obj=results;
        NetworkService.isConnectSinglePacifyer=isConnectSinglePacifyer;
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_connect_to_single_pacifyr);
        mContext = ConnectToSinglePacifyrActivity.this;



        //permissionCheck();
        //ToastHandler.newInstance(ConnectToSinglePacifyrActivity.this).mustShowToast("Works under Progress")
        //
        Bundle extras = getIntent().getExtras();
        pacifyrName = extras.getString("display");
        PrefManager prefManager=PrefManager.getInstance(ConnectToSinglePacifyrActivity.this);
        prefM= PrefManager.getInstance(ConnectToSinglePacifyrActivity.this);
        token=prefM.getAccessToken();
        userid=prefM.getUserID();

        activity=this;

        onSetActionBar();
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        topRlay = findViewById(R.id.connectsingle_top_rlay);
        buttonRlay = findViewById(R.id.top_rlay);
        chatLlay = findViewById(R.id.chat_llay);
        chatPriceCtxv = findViewById(R.id.chat_price_ctxv);
        moreButton = findViewById(R.id.more_button);
        userNameCtxv = findViewById(R.id.user_name_ctxv);
        sadDimv = findViewById(R.id.sad_dimv);
        happyDimv = findViewById(R.id.happy_dimv);
        selectFeelingRlay = findViewById(R.id.select_feeling_rlay);
        selectEmotionRlay = findViewById(R.id.select_emotion_rlay);
        listPacifyrRlay = findViewById(R.id.select_list_pacifyr_rlay);

        image_logo = findViewById(R.id.image_logo);
        pacifyr_swipelay = findViewById(R.id.pacifyr_swipelay);
        selectEmotionDetailRlay = findViewById(R.id.select_emotion_detail_rlay);
        emotionRcv = findViewById(R.id.emotion_rcv);
        emotionDetailRcv = findViewById(R.id.emotion_detail_rcv);


        chat_rlay = findViewById(R.id.chat_rlay);
        audiocall_rlay = findViewById(R.id.audiocall_rlay);
        videocall_rlay = findViewById(R.id.videocall_rlay);
        pacifyer_cimv = findViewById(R.id.pacifyer_cimv);
        pac_name_ctxv = findViewById(R.id.pac_name_ctxv);
        pac_des_ctxv = findViewById(R.id.pac_des_ctxv);
        pac_rate_ctxv = findViewById(R.id.pac_rate_ctxv);
        schedule_rlay= findViewById(R.id.schedule_rlay);

        status_indicator_dimv = findViewById(R.id.status_indicator_dimv);
        status_indicator_ctxv = findViewById(R.id.status_indicator_ctxv);
        listPacifyrRlay.setVisibility(View.GONE);
        setPacifyerValues();

        buttonRlay.bringToFront();

        txt_emergency_usa = findViewById(R.id.txt_emergency_usa);
        Utility gps=new Utility();
        String country = Utility.getUserCountry(ConnectToSinglePacifyrActivity.this);
        if(isValid(country)){
            if(country.equals("us"))
                txt_emergency_usa.setVisibility(View.VISIBLE);
        }

        defSetText(userNameCtxv,"Hello "+getUserName());

        emotionDimv = findViewById(R.id.emotion_dimv);
        //specialityRcv = (RecyclerView) findViewById(R.id.speciality_rcv);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prefM != null && prefM.getCompanyLogo() != null) {
                    showImageWithAnim(prefM.getCompanyLogo(), image_logo);
                }
                button1.setBackgroundResource(R.drawable.circle_click_btn);
                button2.setBackgroundResource(R.drawable.circle_button);
                button3.setBackgroundResource(R.drawable.circle_button);
                button4.setBackgroundResource(R.drawable.circle_button);
                button1.setBackgroundResource(R.drawable.circle_click_btn);
                selectFeelingRlay.setVisibility(View.VISIBLE);
                selectEmotionRlay.setVisibility(View.GONE);
                selectEmotionDetailRlay.setVisibility(View.GONE);
                listPacifyrRlay.setVisibility(View.GONE);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectEmotionRlay.getVisibility()==View.VISIBLE ||selectEmotionDetailRlay.getVisibility()==View.VISIBLE||listPacifyrRlay.getVisibility() == View.VISIBLE) {
                button2.setBackgroundResource(R.drawable.circle_click_btn);
                button1.setBackgroundResource(R.drawable.circle_button);
                button3.setBackgroundResource(R.drawable.circle_button);
                button4.setBackgroundResource(R.drawable.circle_button);
                selectFeelingRlay.setVisibility(View.GONE);
                selectEmotionRlay.setVisibility(View.VISIBLE);
                selectEmotionDetailRlay.setVisibility(View.GONE);
                listPacifyrRlay.setVisibility(View.GONE);
                }
                else {
                    Toast.makeText(v.getContext(), "Please select option", Toast.LENGTH_SHORT).show();
                }
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectEmotionDetailRlay.getVisibility()==View.VISIBLE ||listPacifyrRlay.getVisibility()==View.VISIBLE)
                {
                button3.setBackgroundResource(R.drawable.circle_click_btn);
                button2.setBackgroundResource(R.drawable.circle_button);
                button1.setBackgroundResource(R.drawable.circle_button);
                button4.setBackgroundResource(R.drawable.circle_button);

                selectFeelingRlay.setVisibility(View.GONE);
                selectEmotionRlay.setVisibility(View.GONE);
                selectEmotionDetailRlay.setVisibility(View.VISIBLE);
                listPacifyrRlay.setVisibility(View.GONE);
                }
                else
                {
                    Toast.makeText(v.getContext(), "No emotion selected", Toast.LENGTH_SHORT).show();
                }
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listPacifyrRlay.getVisibility()==View.VISIBLE)
                {
                button4.setBackgroundResource(R.drawable.circle_click_btn);
                button2.setBackgroundResource(R.drawable.circle_button);
                button3.setBackgroundResource(R.drawable.circle_button);
                button1.setBackgroundResource(R.drawable.circle_button);

                selectFeelingRlay.setVisibility(View.GONE);
                selectEmotionRlay.setVisibility(View.GONE);
                selectEmotionDetailRlay.setVisibility(View.GONE);
                listPacifyrRlay.setVisibility(View.VISIBLE);
                }
                else
                {
                    Toast.makeText(v.getContext(), "Select any option", Toast.LENGTH_SHORT).show();
                }
            }
        });

        sadDimv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                happyDimv.clearColorFilter();
                sadDimv.setColorFilter(getResources().getColor( R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);
                type="negative";

                button1.setBackgroundResource(R.drawable.circle_button);
                button2.setBackgroundResource(R.drawable.circle_click_btn);
                 //callFragmant();
                changeToSecondScreen();

            }
        });

        happyDimv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sadDimv.clearColorFilter();
                happyDimv.setColorFilter(getResources().getColor( R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);
                type="positive";
                button1.setBackgroundResource(R.drawable.circle_button);
                button2.setBackgroundResource(R.drawable.circle_click_btn);
                //callFragmant();
                changeToSecondScreen();

            }
        });

        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLoading();
                showProgress();
                moreButton.setVisibility(View.GONE);
                String url = GLOBAL_URL+"specialities/getCategories";
                AjaxCallback cb = new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject json, AjaxStatus status) {
                        try {
                            hideProgress();
                            ModelSpecialityByEmotion modelSpeciltyByEmotion= GsonUtils.getInstance().gsonToModelSpeciltyByEmotion(json);
                            RecyclerView detailRcv = activity.findViewById(R.id.emotion_detail_rcv);
                            detailRcv.setLayoutManager(new LinearLayoutManager(ConnectToSinglePacifyrActivity.this, LinearLayoutManager.VERTICAL, false));
                            EmotionSpecialityAdapter adapter=new EmotionSpecialityAdapter(ConnectToSinglePacifyrActivity.this,activity,modelSpeciltyByEmotion.getResultsList());
                            detailRcv.setAdapter(adapter);
                        } catch (Exception e) {
                            hideProgress();
                            e.printStackTrace();
                        }
                    }
                };
                AQuery aq = new AQuery(ConnectToSinglePacifyrActivity.this);
                cb.header("Content-Type", "application/json");
                cb.header("AccessToken",token);
                aq.ajax(url,JSONObject.class, cb);
            }
        });

        selectFeelingRlay.setVisibility(View.VISIBLE);

        if (prefM != null && prefM.getCompanyLogo() != null) {
            showImageWithAnim(prefM.getCompanyLogo(), image_logo);
        }
        else{
            hideImageWithAnim(image_logo);
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        
    }

    @Override
    protected void onPause() {
        super.onPause();
        trackTimer_check_online.cancel();
        trackTimerTask_check_online.cancel();
    }


    @Override
    protected void onResume() {
        super.onResume();
        try {

            /** refresh pacifyr object**/
            /**
             * here we are checking the pacifyrs are status online or offline  **/

            trackTimer_check_online = new Timer();
            trackTimerTask_check_online = new TimerTask() {
                @Override
                public void run() {

                    NetworkService.startActionCutomerDashboard((getApplicationContext()));

                }
            };
            trackTimer_check_online.scheduleAtFixedRate(trackTimerTask_check_online, 0, 10000);


        }
        catch (Exception e)
        {
            Log.w("QQQQ",String.valueOf(e));
        }

        if (isValid(isCallActive) && isCallActive) {
            showSnackbarPopup();
        } else {
            dismissSnackbarPopup();
        }
    }



    @Subscribe
    public void onNotifyEvent(NotifyEvent event) {

        if (event.isSuccess)
        {

            if (isValid(modelCustomerDashboard2))
            {
                for (int i=0;i<modelCustomerDashboard2.getPreferredPacifyrs().length;i++)
                {
                    MUser[] m= modelCustomerDashboard2.getPreferredPacifyrs();
                    if (isValid(m) && isValid(pacifyer_obj))
                    {
                        if (m[i].getId().equals(pacifyer_obj.getId()))
                        {
                            pacifyer_obj=m[i];
                            boolean isOnline=pacifyer_obj.getOnline();
                            if(isOnline){
                                status_indicator_dimv.setImageDrawable(getResources().getDrawable(R.drawable.online));
                                status_indicator_ctxv.setText("ONLINE");
                                if(!pacifyer_obj.getCurrentlyAvailable())
                                {
                                    status_indicator_dimv.setImageDrawable(getResources().getDrawable(R.drawable.online));
                                    status_indicator_ctxv.setText("In-Session");
                                }
                            }
                            else{
                                status_indicator_dimv.setImageDrawable(getResources().getDrawable(R.drawable.offline));
                                status_indicator_ctxv.setText("OFFLINE");
                            }

                            chatSubsCall(pacifyer_obj);
                        }
                    }

                }
            }


        }

    }

    private void setPacifyerValues() {
        try{
            if(isValid(pacifyer_obj))
            {
                if(isValid(pacifyer_obj.getImage()))
                Picasso.with(ConnectToSinglePacifyrActivity.this).load(NetworkService.GLOBAL_IMAGE_URL+pacifyer_obj.getImage()).placeholder(R.drawable.placeholder_user).into(pacifyer_cimv);

                defSetText(pac_name_ctxv,pacifyer_obj.getFirstName()+" "+pacifyer_obj.getLastName());
                defSetText(pac_des_ctxv,pacifyer_obj.getDescription());
                prefM = PrefManager.getInstance(ConnectToSinglePacifyrActivity.this);
                String customerRole = prefM.getCustomerRole();
                if(customerRole.equals("employee")) {
                    if (pacifyer_obj.isSessionChargeable()){
                        defSetText(pac_rate_ctxv, pacifyer_obj.getPriceWithSign() + " " + Utility.getCostLabel(ConnectToSinglePacifyrActivity.this));

                    }else {
                        defSetText(pac_rate_ctxv,"Employer Designated Pacifyr");
                    }
                }else if(customerRole.equals("customer")){
                    defSetText(pac_rate_ctxv, pacifyer_obj.getPriceWithSign() + " " + Utility.getCostLabel(ConnectToSinglePacifyrActivity.this));
                }
                else if(customerRole.equals("paidMember")){
                    defSetText(pac_rate_ctxv,"Employer Designated Pacifyr");
                }

                videocall_rlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (isValid(isCallActive) && isCallActive)
                            return;

                        boolean isOnline = pacifyer_obj.getOnline();
                        String reason = "";
                        if (isOnline) {
                            if (!pacifyer_obj.getCurrentlyAvailable()) {
                                reason = " in a session";
                            }
                        } else {
                            reason = "offline";
                        }
                        if(!pacifyer_obj.isReadyToCall()) {

                            if (reason.equals("offline"))
                            {
                                startChat(pacifyer_obj);
                            }
                            else {
                                Snackbar offlineSnackbar = Snackbar.make(topRlay,
                                        pacifyer_obj.getName() + " is currently " + reason + ". You can " +
                                                "either chat with " + pacifyer_obj.getName() + " or try again later.", Snackbar.LENGTH_LONG);
                                View snackbarView = offlineSnackbar.getView();
                                TextView snackTextView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
                                snackTextView.setMaxLines(4);
                                offlineSnackbar.show();
                                return;
                            }
                        }

                       else {
                            if (checker.lacksPermissions(Consts.PERMISSIONS)) {

                                startPermissionsActivity(false,ConnectToSinglePacifyrActivity.this);

                                if (checker.lacksPermissions(Consts.PERMISSIONS)) {

                                    if(isLoggedInChat()) {

                                        setAlertStartCall(pacifyer_obj , new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                hideStatus();
                                                checkRoleRequest(pacifyer_obj, listner, true);
                                            }
                                        });

                                        //callRequestWeb(pacifyer_obj, listner, true);
                                    }

                                }

                            }
                            else {
                                if(isLoggedInChat()) {

                                    setAlertStartCall(pacifyer_obj, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            hideStatus();
                                            checkRoleRequest(pacifyer_obj, listner, true);
                                        }
                                    });

                                    //callRequestWeb(pacifyer_obj, listner, true);
                                }
                            }

                        }
                    }
                });

                audiocall_rlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isValid(isCallActive) && isCallActive)
                            return;

                        if(!pacifyer_obj.isReadyToCall()) {
                            boolean isOnline = pacifyer_obj.getOnline();
                            String reason = "";
                            if (isOnline) {
                                if (!pacifyer_obj.getCurrentlyAvailable()) {
                                    reason = " in a session";
                                }
                            } else {
                                reason = "offline";
                            }
                          if (reason.equals("offline"))
                          {
                              startChat(pacifyer_obj);
                          }
                          else {
                              Snackbar offlineSnackbar = Snackbar.make(topRlay,
                                      pacifyer_obj.getName() + " is currently " + reason + ". You " +
                                              "can " +
                                              "either chat with " + pacifyer_obj.getName() + " or try again later.", Snackbar.LENGTH_LONG);
                              View snackbarView = offlineSnackbar.getView();
                              TextView snackTextView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
                              snackTextView.setMaxLines(4);
                              offlineSnackbar.show();
                              return;
                          }
                        }
                        else {
                            if (checker.lacksPermissions(Consts.PERMISSIONS[1])) {

                                startPermissionsActivity(true,ConnectToSinglePacifyrActivity.this);

                                if(isLoggedInChat()) {

                                    setAlertStartCall(pacifyer_obj, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            hideStatus();
                                            checkRoleRequest(pacifyer_obj, listner, true);
                                        }
                                    });

                                    //callRequestWeb(pacifyer_obj, listner, true);
                                }
                            }
                            else {

                                if(isLoggedInChat()) {

                                    setAlertStartCall(pacifyer_obj, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            hideStatus();
                                            checkRoleRequest(pacifyer_obj, listner, false);
                                        }
                                    });

                                    //callRequestWeb(pacifyer_obj, listner, true);
                                }
                            }
                        }



                    }
                });
                chat_rlay.setVisibility(View.VISIBLE);

             /*   if(pacifyer_obj.isChatEnabled())
                    chat_rlay.setVisibility(View.VISIBLE);*/

                chat_rlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        boolean isChatSubscriptionRequired = pacifyer_obj.isChatSubscriptionRequired();
                        int priceForChat = 0;
                        try {
                            priceForChat = Integer.parseInt(pacifyer_obj.getPriceForChat());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if(!isChatSubscriptionRequired)
                        {
                            startChat(pacifyer_obj);
                            //showIOSDialogOK("","Please purchase unlimited chat per day","OK");

                        }else if(priceForChat==0){
                            startChat(pacifyer_obj);
                        }else {
                            //chatLlay.setVisibility(View.VISIBLE);

                            if (pacifyer_obj.getChatSubscription() == null) {
                                showIOSDialogOK("","Please purchase unlimited chat per day","OK");
                                //chat_price_ctxv.setText("Chat for " + Utility.getPriceSign(ProfileScreenActivity.this) + priceForChat + " a day");
                            } else {
                                startChat(pacifyer_obj);
                            }
                        }



                        //startChat(pacifyer_obj);
                     //   ToastHandler.newInstance(ConnectToSinglePacifyrActivity.this).mustShowToast("Works Under Progress");

                    }
                });

                pacifyr_swipelay.setColorSchemeResources(R.color.md_blue_300, R.color.green, R.color.red, R.color.md_amber_300);
                pacifyr_swipelay.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        pacifyr_swipelay.setRefreshing(true);
                        refreshPacifyr();
                       // NetworkService.startActionListCustomer(ConnectToSinglePacifyrActivity.this, 0, 10);

                    }


                });
                boolean isOnline=pacifyer_obj.getOnline();
                if(isOnline){
                    status_indicator_dimv.setImageDrawable(getResources().getDrawable(R.drawable.online));
                    status_indicator_ctxv.setText("ONLINE");
                    if(!pacifyer_obj.getCurrentlyAvailable())
                    {
                        status_indicator_dimv.setImageDrawable(getResources().getDrawable(R.drawable.online));
                        status_indicator_ctxv.setText("In-Session");
                    }
                }
                else{
                    status_indicator_dimv.setImageDrawable(getResources().getDrawable(R.drawable.offline));
                    status_indicator_ctxv.setText("OFFLINE");
                }

                chatSubsCall(pacifyer_obj);
                //schedule_rlay.setVisibility(View.GONE);
                schedule_rlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callPacifyrForRefreshBeforeScheduleUI(pacifyer_obj.getId());
                    }
                });

            }
        }catch (Exception e)
        {
            e.printStackTrace();
            LogUtils.LOGD("session","CSPA setPacifyerValues exception"+e.getMessage());
        }

    }

    private void chatSubsCall(final MUser pacifyer_obj) {
        try{
            String url = GLOBAL_URL+"user/profile/"+pacifyer_obj.getId();

            PrefManager prefM= PrefManager.getInstance(mContext);
            String token=prefM.getAccessToken();

            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    try {
                        MUser user = GsonUtils.getInstance().gsonToMUser(json);
                        String price="0";
                        if (json.has("priceForChat"))
                            price  = json.getString("priceForChat");
                        String js=status.getMessage();
                        String jk=status.getError();
                        callSubs(Integer.parseInt(price),user);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            try {
                AQuery aq = new AQuery(mContext);
                cb.header("Content-Type", "application/json");
                cb.header("AccessToken", token);
                //aq.post(url, jsonObject, JSONObject.class, cb);
                aq.ajax(url,JSONObject.class, cb);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }



        }catch (Exception e)
        {

        }
    }

    private void callSubs(int priceforChat,MUser user){
        try {
            boolean isChatSubscriptionRequired = pacifyer_obj.isChatSubscriptionRequired();
            //Integer price = priceforChat;
            int priceForChat = priceforChat;

            LinearLayout chatLlay = findViewById(R.id.chat_llay);
            MainTextView chat_price_ctxv = findViewById(R.id.chat_price_ctxv);

            chatLlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    getSavedCardWebChat(pacifyer_obj);
                    //callChatSubScription(mpacifyerDetial.getId());
                }
            });


            if (!isChatSubscriptionRequired) {
                chatLlay.setVisibility(View.GONE);

            } else if (priceForChat == 0) {
                chatLlay.setVisibility(View.GONE);
            } else {
                chatLlay.setVisibility(View.VISIBLE);

                if (user.getChatSubscription() == null) {
                    chat_price_ctxv.setText("Chat for " + Utility.getPriceSign(ConnectToSinglePacifyrActivity.this) + priceForChat + " a day");
                    chatLlay.setClickable(true);
                } else {
                    try {
                        ChatSubscription subscription = user.getChatSubscription();
                        String expiryDate = subscription.getExpiryDate();

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.mmm'Z'");
                        Date parsedTime = sdf.parse(expiryDate);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.mmm'Z'");
                        Date date = new Date();
                        System.out.println(dateFormat.format(date));

                        long diff = parsedTime.getTime() - date.getTime();

                        long diffSeconds = diff / 1000 % 60;
                        long diffMinutes = diff / (60 * 1000) % 60;
                        long diffHours = diff / (60 * 60 * 1000) % 24;
                        long diffDays = diff / (24 * 60 * 60 * 1000);

                        if (diffDays > 0) {
                            chat_price_ctxv.setText("Unlimited chat remaining for " + diffDays + " days");
                        } else if (diffHours > 1) {
                            chat_price_ctxv.setText("Unlimited chat remaining for " + diffHours + " hours and " + diffMinutes + " minutes");
                        } else if (diffMinutes > 0) {
                            chat_price_ctxv.setText("Unlimited chat remaining for " + diffMinutes + " minutes");
                        }
                        chatLlay.setClickable(false);

                        //chatPriceCtxv.setText("Unlimited chat remainng for ");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //  status_indicator_dimv.
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        /*selectFeelingRlay.setVisibility(View.VISIBLE);
        selectEmotionRlay.setVisibility(View.GONE);
        selectEmotionDetailRlay.setVisibility(View.GONE);
        listPacifyrRlay.setVisibility(View.GONE);
        button4.setBackgroundResource(R.drawable.circle_button);
        button3.setBackgroundResource(R.drawable.circle_button);
        button2.setBackgroundResource(R.drawable.circle_button);
        button1.setBackgroundResource(R.drawable.circle_click_btn);
        button2.setClickable(false);
        button3.setClickable(false);
        button4.setClickable(false);
        happyDimv.clearColorFilter();
        sadDimv.clearColorFilter();*/
    }


    public void changeToSecondScreen() {

        hideImageWithAnim(image_logo);
        selectEmotionRlay.setVisibility(View.VISIBLE);
        selectFeelingRlay.setVisibility(View.GONE);
        setLoading();
        showProgress();

        if(type.equals("positive")){

            String url = GLOBAL_URL+"emotions/positive";
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    try {
                        hideProgress();
                        MListPositiveEmotions modelListPosEmotions= GsonUtils.getInstance().gsonToMListPositiveEmotions(json);
                        emotionRcv.setLayoutManager(new GridLayoutManager(ConnectToSinglePacifyrActivity.this, 4));
                        PostiveEmotionsAdapter adapter = new PostiveEmotionsAdapter(ConnectToSinglePacifyrActivity.this,activity,modelListPosEmotions.getResultsList());
                        emotionRcv.setAdapter(adapter);

                    } catch (Exception e) {
                        hideProgress();
                        e.printStackTrace();
                    }
                }
            };
            AQuery aq = new AQuery(ConnectToSinglePacifyrActivity.this);
            cb.header("Content-Type", "application/json");
            //cb.header("AccessToken",token);
            cb.header("AccessToken",token);
            aq.ajax(url,JSONObject.class, cb);

        }else if (type.equals("negative")){

            String url = GLOBAL_URL+"emotions/negative";
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    try {
                        hideProgress();
                        MListNegativeEmotions modelListNegEmotions= GsonUtils.getInstance().gsonToMListNegativeEmotions(json);
                        emotionRcv.setLayoutManager(new GridLayoutManager(ConnectToSinglePacifyrActivity.this, 4));
                        NegativeEmotionsAdapter adapter = new NegativeEmotionsAdapter(ConnectToSinglePacifyrActivity.this,activity,modelListNegEmotions.getResultsList());
                        emotionRcv.setAdapter(adapter);
                    } catch (Exception e) {
                        hideProgress();
                        e.printStackTrace();
                    }
                }
            };
            AQuery aq = new AQuery(ConnectToSinglePacifyrActivity.this);
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken",token);
            aq.ajax(url,JSONObject.class, cb);

        }

    }
    private void refreshPacifyr() {
        try {
            //showProgress();

            PrefManager prefM = PrefManager.getInstance(ConnectToSinglePacifyrActivity.this);
            String token = prefM.getAccessToken();

            String url = UtilityNetworkService.GLOBAL_URL + "user/profile/" + pacifyer_obj.getId();
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    try {
                       // hideProgress();
                        if (isValid(pacifyr_swipelay)) {
                            if (pacifyr_swipelay.isRefreshing()) {
                                pacifyr_swipelay.setRefreshing(false);

                            }
                        }

                        if(json!=null) {
                           ModelUserProfile pacifyer_obj2 = GsonUtils.getInstance().gsonToModelUserProfile(json);

                            pacifyer_obj=convertModel(pacifyer_obj2);
                            setPacifyerValues();
                        }
                        else
                        {

                        }


                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }
            };

            AQuery aq = new AQuery(ConnectToSinglePacifyrActivity.this);
            //cb.header("Content-Type", "application/json");
            cb.header("AccessToken", token);
            aq.ajax(url, JSONObject.class, cb);

        }
        catch (Exception ex)
        {

            ex.printStackTrace();
        }
    }
    @Override
    protected View getSnackbarAnchorView() {
        return null;
    }

    @Override
    public void onSetValues() throws Exception {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // check if the request code is same as what is passed  here it is 2
           LogUtils.LOGD("session","CPA  onActivityResult requestCode " + requestCode);
            if(requestCode == PermissionsActivity.PERMISSION_EXTERNAL_REQUEST_CODE) {
                boolean granted = data.getBooleanExtra("permissions", false);
                boolean audioonly = data.getBooleanExtra(PermissionsActivity.CHECK_ONLY_AUDIO, false);
                LogUtils.LOGD("session","CPA  onActivityResult granted " + granted);
                LogUtils.LOGD("session","CPA  onActivityResult audioonly " + audioonly);

                    if(granted) {
                        if(audioonly) {
                            if(checker.lacksPermissions(Consts.PERMISSIONS[1])) {
                                //callRequestWeb(pacifyer_obj, listner, false);

                           /*     setAlertStartCall("Start", "Call now....", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                       *//* hideStatus();
                                        deleteInstallationWeb();*//*

                                    }
                                });*/

//                                checkRoleRequest(pacifyer_obj, listner, false);


                            }
                        }
                        else {
                            if(checker.lacksPermissions(Consts.PERMISSIONS)) {

                              /*  setAlertStartCall("Start", "Call now....", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                       *//* hideStatus();
                                        deleteInstallationWeb();*//*

                                    }
                                });*/
                                //callRequestWeb(pacifyer_obj, listner, true);
//                                checkRoleRequest(pacifyer_obj, listner, true);

                            }
                        }
                    }else{

                        LogUtils.LOGD("session","CPA  onActivityResult not granted  ");
                    }

            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    @Override
    public void onAlertTypeEvent(AlertTypeEvent event) {

    }
    @Subscribe
    public void onCallEvent(CallEvent event) {
        try{
           /* if(!isValid(pacifyer_obj,"Network Error"))
                return;
         *//*   if(isValid(event.qbID))
            {*//*
                if (isLoggedInChat()) {
                    callRequestWeb(pacifyer_obj,listner,event.isVideoCall);
                    //startCall(true,qbID);
                }
                if (checker.lacksPermissions(Consts.PERMISSIONS)) {
                    startPermissionsActivity(false);
                }
           *//* }*//*
*/
        }catch (Exception e)
        {

        }

    }

    @Override
    public void onBackPressed() {
        //
        DashboardActivity.start(ConnectToSinglePacifyrActivity.this);
    }

    private LinearLayout actionBarTopLlay;
    private RelativeLayout bottomPanel;
    private LinearLayout leftLlay;
    private ImageView backImv;
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
        defSetText(titleTxv, "CONNECT");
        leftTxv.setVisibility(View.GONE);
        ///leftTxv.setText("Cancel");

        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/ActionBarCommonFont.otf");
        /*Typeface typeface = Typeface.createFromAsset(ConnectToPacifyrActivity.this.getAssets(), "ActionBarCommonFont.otf");*/
        titleTxv.setTypeface(type);

        backImv.setVisibility(View.VISIBLE);
        backImv.setImageDrawable(getResources().getDrawable(R.drawable.ic_cancel2));
        backImv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //backImv.setBackgroundResource(R.drawable.leftmen);
        leftTxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DashboardActivity.start(ConnectToSinglePacifyrActivity.this);
               /* Intent intent = new Intent(ConnectToSinglePacifyrActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();*/
            }
        });
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
                            finishAffinity();
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


}
