package com.pacifyr.client.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DefaultItemAnimator;
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
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.event.AlertTypeEvent;
import com.pacifyr.client.event.CallEvent;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.fragment.schedule.ScheduleBottomSheetFragment;
import com.pacifyr.client.model.ModelPamTemplates;
import com.pacifyr.client.network.GsonUtils;
import com.pacifyr.client.network.NetworkService;
import com.pacifyr.client.quickblox.activities.PermissionsActivity;
import com.pacifyr.client.quickblox.utils.Consts;
import com.pacifyr.client.utility.PacifyrAppCompactActivity;
import com.pacifyr.client.utils.LogUtils;
import com.pacifyr.client.utils.Utility;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;
import com.pacifyrapp.utilitylibrary.model.pacifyr.ChatSubscription;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MPacifyr;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MUser;
import com.pacifyrapp.utilitylibrary.model.pacifyr.ModelUserProfile;
import com.pacifyrapp.utilitylibrary.network.UtilityNetworkService;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.pacifyr.client.network.NetworkService.GLOBAL_URL;
import static com.pacifyr.client.network.NetworkService.modelCustomerDashboard2;

//import com.pacifyr.pacifyrapp.Fragment.SelecetFeelingFragment;

public class ConnectToSinglePacifyrActivity  extends PacifyrAppCompactActivity {

    private String pacifyrName;

    public DeviceFitImageView sadDimv;
    public DeviceFitImageView happyDimv;

    private boolean isClick=false;
    private String type;

    public RelativeLayout FirstScreenLayout;
    public RelativeLayout second_screen_layout;
    public RelativeLayout third_Screen_layout;
    public RelativeLayout listPacifyrRlay;
    public RelativeLayout third_Screen_second_layout;


    
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


    private TextView text_feel_ctxv;
    private List<ModelPamTemplates> mModelPamTemplates;
    private RecyclerView RecyclerView_conditions;
    private FirstScreenConditionAdapter mFirstScreenConditionAdapter;
    private SecondScreenInstructionAdapter.SecondScreenNestedAdapter mSecondScreenNestedAdapter;
    private TextView third_sub_title;
    private LinearLayout submit;
    private RecyclerView rcv_secondScreen_Expandable, rcv_thirdScreen_instruct, rcv_thirdScreen_second_instruct;
    private ThirdScreenInstructionAdapter mInstructionAdapter;
    private SecondScreenInstructionAdapter mInstructionSecondAdapter;
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
        chatPriceCtxv.setVisibility(View.GONE);
        moreButton = findViewById(R.id.more_button);
        userNameCtxv = findViewById(R.id.user_name_ctxv);
       // sadDimv = findViewById(R.id.sad_dimv);
       // happyDimv = findViewById(R.id.happy_dimv);
        FirstScreenLayout = findViewById(R.id.FirstScreenLayout);
        second_screen_layout = findViewById(R.id.second_screen_layout);
        listPacifyrRlay = findViewById(R.id.select_list_pacifyr_rlay);
       // third_Screen_second_layout=findViewById(R.id.third_Screen_second_layout);

        pacifyr_swipelay = findViewById(R.id.pacifyr_swipelay);
        third_Screen_layout = findViewById(R.id.third_Screen_layout);
       // emotionRcv = findViewById(R.id.emotion_rcv);
        emotionDetailRcv = findViewById(R.id.emotion_detail_rcv);


        chat_rlay = findViewById(R.id.chat_rlay);
        audiocall_rlay = findViewById(R.id.audiocall_rlay);
        videocall_rlay = findViewById(R.id.videocall_rlay);
        pacifyer_cimv = findViewById(R.id.pacifyer_cimv);
        pac_name_ctxv = findViewById(R.id.pac_name_ctxv);
        pac_des_ctxv = findViewById(R.id.pac_des_ctxv);
        pac_rate_ctxv = findViewById(R.id.pac_rate_ctxv);
        schedule_rlay= findViewById(R.id.schedule_rlay);
        pac_rate_ctxv.setVisibility(View.GONE);
        status_indicator_dimv = findViewById(R.id.status_indicator_dimv);
        status_indicator_ctxv = findViewById(R.id.status_indicator_ctxv);
        listPacifyrRlay.setVisibility(View.GONE);
        setPacifyerValues();

        buttonRlay.bringToFront();

        txt_emergency_usa = findViewById(R.id.txt_emergency_usa);
        Utility gps=new Utility();
        String country = Utility.getUserCountry(ConnectToSinglePacifyrActivity.this);
        if(isValid(country)){
            if(country.equals("us"));
            {
                // txt_emergency_usa.setVisibility(View.VISIBLE);
            }

        }

        defSetText(userNameCtxv,"Hello "+getUserName());

        emotionDimv = findViewById(R.id.emotion_dimv);
        //specialityRcv = (RecyclerView) findViewById(R.id.speciality_rcv);

        listPacifyrRlay.setVisibility(View.GONE);
        rcv_thirdScreen_instruct=findViewById(R.id.rcv_thirdScreen_instruct);
        text_feel_ctxv=findViewById(R.id.text_feel_ctxv);
        //rcv_thirdScreen_second_instruct=findViewById(R.id.rcv_thirdScreen_second_instruct);
       /// rcv_secondScreen_instruction=findViewById(R.id.rcv_secondScreen_instruction);



        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button1.setBackgroundResource(R.drawable.circle_click_btn);
                button2.setBackgroundResource(R.drawable.circle_button);
                button3.setBackgroundResource(R.drawable.circle_button);
                button4.setBackgroundResource(R.drawable.circle_button);
                button1.setBackgroundResource(R.drawable.circle_click_btn);
                FirstScreenLayout.setVisibility(View.VISIBLE);
                second_screen_layout.setVisibility(View.GONE);
                third_Screen_layout.setVisibility(View.GONE);
                listPacifyrRlay.setVisibility(View.GONE);
                third_Screen_second_layout.setVisibility(View.GONE);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(second_screen_layout.getVisibility()==View.VISIBLE ||third_Screen_layout.getVisibility()==View.VISIBLE||listPacifyrRlay.getVisibility() == View.VISIBLE) {
                button2.setBackgroundResource(R.drawable.circle_click_btn);
                button1.setBackgroundResource(R.drawable.circle_button);
                button3.setBackgroundResource(R.drawable.circle_button);
                button4.setBackgroundResource(R.drawable.circle_button);
                FirstScreenLayout.setVisibility(View.GONE);
                second_screen_layout.setVisibility(View.VISIBLE);
                third_Screen_layout.setVisibility(View.GONE);
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
                if (third_Screen_layout.getVisibility()==View.VISIBLE ||listPacifyrRlay.getVisibility()==View.VISIBLE)
                {
                button3.setBackgroundResource(R.drawable.circle_click_btn);
                button2.setBackgroundResource(R.drawable.circle_button);
                button1.setBackgroundResource(R.drawable.circle_button);
                button4.setBackgroundResource(R.drawable.circle_button);

                FirstScreenLayout.setVisibility(View.GONE);
                second_screen_layout.setVisibility(View.GONE);
                third_Screen_layout.setVisibility(View.VISIBLE);
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

                FirstScreenLayout.setVisibility(View.GONE);
                second_screen_layout.setVisibility(View.GONE);
                third_Screen_layout.setVisibility(View.GONE);
                listPacifyrRlay.setVisibility(View.VISIBLE);
                }
                else
                {
                    Toast.makeText(v.getContext(), "Select any option", Toast.LENGTH_SHORT).show();
                }
            }
        });
        rcv_thirdScreen_instruct = findViewById(R.id.rcv_thirdScreen_instruct);
        rcv_secondScreen_Expandable = findViewById(R.id.rcv_secondScreen_Expandable);
        third_sub_title=findViewById(R.id.third_sub_title);
        RecyclerView_conditions = findViewById(R.id.RecyclerView_conditions);
        makeFirstScreenInstructionList();

/*
        sadDimv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                happyDimv.clearColorFilter();
                sadDimv.setColorFilter(getResources().getColor( R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);
                type="negative";

                button1.setBackgroundResource(R.drawable.circle_button);
                button2.setBackgroundResource(R.drawable.circle_click_btn);
                 //callFragmant();
                PacifyrActivity.call_emotion=new MEmotion();
                PacifyrActivity.call_emotion.setId("5971c472734d1d6202a81958");
                changeToSecondScreen();

            }
        });
*/

/*
        happyDimv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sadDimv.clearColorFilter();
                happyDimv.setColorFilter(getResources().getColor( R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);
                type="positive";
                button1.setBackgroundResource(R.drawable.circle_button);
                button2.setBackgroundResource(R.drawable.circle_click_btn);
                //callFragmant();
                PacifyrActivity.call_emotion=new MEmotion();
                PacifyrActivity.call_emotion.setId("5971c472734d1d6202a81958");
                changeToSecondScreen();

            }
        });
*/

        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*setLoading();
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
                aq.ajax(url,JSONObject.class, cb);*/
            }
        });

        submit=findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                button4.setBackgroundResource(R.drawable.circle_click_btn);
                button2.setBackgroundResource(R.drawable.circle_button);
                button3.setBackgroundResource(R.drawable.circle_button);
                button1.setBackgroundResource(R.drawable.circle_button);
                FirstScreenLayout.setVisibility(View.GONE);
                second_screen_layout.setVisibility(View.GONE);
                third_Screen_layout.setVisibility(View.GONE);
                listPacifyrRlay.setVisibility(View.VISIBLE);
                text_feel_ctxv.setText(getResources().getString(R.string.you_are_all_set));
            }
        });



        FirstScreenLayout.setVisibility(View.VISIBLE);



    }

    @Override
    protected void onStop() {
        super.onStop();

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

                   // NetworkService.startActionCutomerDashboard((getApplicationContext()));

                }
            };
            trackTimer_check_online.scheduleAtFixedRate(trackTimerTask_check_online, 0, 10000);


        }
        catch (Exception e)
        {
            Log.w("QQQQ",String.valueOf(e));
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
                    if (isValid(m))
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

                defSetText(pac_name_ctxv,pacifyer_obj.getFirstName()+""+pacifyer_obj.getLastName());
                defSetText(pac_des_ctxv,pacifyer_obj.getDescription());
                prefM = PrefManager.getInstance(ConnectToSinglePacifyrActivity.this);
                String customerRole = prefM.getCustomerRole();

                pac_rate_ctxv.setVisibility(View.GONE);

                if(customerRole.equals("employee")) {
                    if (pacifyer_obj.isSessionChargeable()){
                        defSetText(pac_rate_ctxv,pacifyer_obj.getPriceWithSign()+" per hour");

                    }else {
                        defSetText(pac_rate_ctxv,"Employer Designated Pacifyr");
                    }
                }else if(customerRole.equals("customer")){
                    defSetText(pac_rate_ctxv,pacifyer_obj.getPriceWithSign()+" per hour");
                }
                else if(customerRole.equals("paidMember")){
                    defSetText(pac_rate_ctxv,"Employer Designated Pacifyr");
                }

                videocall_rlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(!pacifyer_obj.isReadyToCall()) {
                            Snackbar offlineSnackbar = Snackbar.make(topRlay, pacifyer_obj.getName() + " is currently offline. You can either chat with " + pacifyer_obj.getName() + " or try again later.", Snackbar.LENGTH_LONG);
                            offlineSnackbar.show();
                            //ToastHandler.newInstance(ConnectToSinglePacifyrActivity.this).mustShowToast(pacifyer_obj.getName() + " is currently offline. You can either chat with " + pacifyer_obj.getName() + " or try agein later");
                            return;
                        }

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
                });

                audiocall_rlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(!pacifyer_obj.isReadyToCall()) {
                            Snackbar offlineSnackbar = Snackbar.make(topRlay, pacifyer_obj.getName() + " is currently offline. You can either chat with " + pacifyer_obj.getName() + " or try again later.", Snackbar.LENGTH_LONG);
                            offlineSnackbar.show();
                            //ToastHandler.newInstance(ConnectToSinglePacifyrActivity.this).mustShowToast(pacifyer_obj.getName() + " is currently offline. You can either chat with " + pacifyer_obj.getName() + " or try agein later");
                            return;
                        }

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
                });


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
                schedule_rlay.setVisibility(View.VISIBLE);
                schedule_rlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showScheduleDialogueFragment(pacifyer_obj);
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
                        String price = json.getString("priceForChat");
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

            chatLlay.setVisibility(View.GONE);
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
                chatLlay.setVisibility(View.GONE);
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
        /*FirstScreenLayout.setVisibility(View.VISIBLE);
        second_screen_layout.setVisibility(View.GONE);
        third_Screen_layout.setVisibility(View.GONE);
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

        second_screen_layout.setVisibility(View.VISIBLE);
        FirstScreenLayout.setVisibility(View.GONE);
        /*setLoading();
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

        }*/

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

    private void showScheduleDialogueFragment(MUser pacifyer_obj) {
        if (isValid(pacifyer_obj)) {
            if (isValid(getPrefManager().getGENDER()) && isValid(getPrefManager().getZIP())) {
                ScheduleBottomSheetFragment bottomSheetFragment = new ScheduleBottomSheetFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("OBJECT", pacifyer_obj);
                bottomSheetFragment.setArguments(bundle);
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
            }
            else
            { showMandatoryFieldsPopUp();
            }
        }


    }















/*
    private class ThirdScreenInstructionSubAdapter extends RecyclerView.Adapter<ThirdScreenInstructionSubAdapter.MyViewHolderr>{

        List<String> list;
        Context context;


        public ThirdScreenInstructionSubAdapter(Context context) {
            list=new ArrayList<String>();
            list.add("I need work clothes");
            list.add("I need transportation support");
            list.add("Gain more workplace skills");
            list.add("I want to build my career");
            this.context=context;
        }


        @Override
        public MyViewHolderr onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_item_instruction, parent, false);

            return new MyViewHolderr(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolderr holder, final int position) {

            holder.tv_instruction.setText(list.get(position));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    button1.setBackgroundResource(R.drawable.circle_button);
                    button2.setBackgroundResource(R.drawable.circle_button);
                    button3.setBackgroundResource(R.drawable.circle_button);
                    button4.setBackgroundResource(R.drawable.circle_click_btn);


                    button1.setClickable(true);
                    button2.setClickable(true);
                    button3.setClickable(true);
                    button4.setClickable(true);

                    FirstScreenLayout.setVisibility(View.GONE);
                    second_screen_layout.setVisibility(View.GONE);
                    third_Screen_layout.setVisibility(View.GONE);
                    third_Screen_second_layout.setVisibility(View.GONE);
                    listPacifyrRlay.setVisibility(View.VISIBLE);
                   // morePacifyrsRlay.setVisibility(View.GONE);
                }
            });

        }
*/




    private void makeFirstScreenInstructionList() {

        try {
            PrefManager prefM = PrefManager.getInstance(ConnectToSinglePacifyrActivity.this);
            String token = prefM.getAccessToken();
            String url = UtilityNetworkService.GLOBAL_URL + "template";
            setLoading();
            showProgress();
            AjaxCallback cb = new AjaxCallback<JSONArray>() {
                @Override
                public void callback(String url, JSONArray json, AjaxStatus status) {
                    try {
                        hideProgress();
                        mModelPamTemplates = GsonUtils.getInstance().gsonToModelPamTemplates(json);
                        if (isValid(mModelPamTemplates))
                            if (isValid(mModelPamTemplates.get(0).getQuestion())) {
                                if (isValid(mModelPamTemplates.get(0).getTitle()))
                                    text_feel_ctxv.setText(mModelPamTemplates.get(0).getTitle());
                                else
                                if (isValid(mModelPamTemplates.get(0).getQuestion()))
                                    text_feel_ctxv.setText(mModelPamTemplates.get(0).getQuestion());
                                if (isValid(mModelPamTemplates.get(0).getOptions())) {
                                    /** FirstScreen Conditions **/
                                    FirstScreenLayout.setVisibility(View.VISIBLE);
                                    mFirstScreenConditionAdapter = new FirstScreenConditionAdapter(getApplicationContext(), mModelPamTemplates.get(0).getOptions());
                                    GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 3, GridLayoutManager.VERTICAL, false);
                                    RecyclerView_conditions.setLayoutManager(manager);
                                    RecyclerView_conditions.setAdapter(mFirstScreenConditionAdapter);
                                    RecyclerView_conditions.setItemAnimator(new DefaultItemAnimator());
                                }
                            }

                    } catch (Exception e) {
                        hideProgress();
                        e.printStackTrace();
                    }
                }
            };
            AQuery aq = new AQuery(ConnectToSinglePacifyrActivity.this);
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", token);
            aq.ajax(url, JSONArray.class, cb);
        } catch (Exception e) {
            hideProgress();
            e.printStackTrace();
        }

    }


    private class FirstScreenConditionAdapter extends RecyclerView.Adapter<FirstScreenConditionAdapter.MyViewHolderr>{

        List<ModelPamTemplates.Option> options;
        Context context;


        public FirstScreenConditionAdapter(Context context, List<ModelPamTemplates.Option> options) {
            this.options=options;
            this.context=context;
        }


        @Override
        public MyViewHolderr onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_item_instruction, parent, false);

            return new MyViewHolderr(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolderr holder, final int position) {

            holder.tv_instruction.setText(options.get(position).getOption());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    button1.setBackgroundResource(R.drawable.circle_button);
                    button2.setBackgroundResource(R.drawable.circle_click_btn);
                    button3.setBackgroundResource(R.drawable.circle_button);
                    button4.setBackgroundResource(R.drawable.circle_button);


                    button1.setClickable(true);
                    button2.setClickable(true);
                    button3.setClickable(true);
                    button4.setClickable(true);

                    FirstScreenLayout.setVisibility(View.GONE);
                    second_screen_layout.setVisibility(View.VISIBLE);
                    third_Screen_layout.setVisibility(View.GONE);
                    listPacifyrRlay.setVisibility(View.GONE);


                    setUpSecondScreenUI();
                }
            });

        }

        private void setUpSecondScreenUI() {
            if(isValid(mModelPamTemplates) && isValid(mModelPamTemplates.get(1)))
            {
                if (isValid(mModelPamTemplates.get(1).getTitle()))
                    text_feel_ctxv.setText(mModelPamTemplates.get(1).getTitle());
                else
                if (isValid(mModelPamTemplates.get(1).getQuestion()))
                    text_feel_ctxv.setText(mModelPamTemplates.get(1).getQuestion());

                if (isValid(mModelPamTemplates.get(1).getOptions()))
                {
                    mInstructionSecondAdapter =new SecondScreenInstructionAdapter(getApplicationContext(),mModelPamTemplates.get(1).getOptions());
                    RecyclerView.LayoutManager mLayoutsecond=new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
                    rcv_secondScreen_Expandable.setLayoutManager(mLayoutsecond);
                    rcv_secondScreen_Expandable.setAdapter(mInstructionSecondAdapter);
                    rcv_secondScreen_Expandable.setItemAnimator(new DefaultItemAnimator());
                }
            }



        }



        @Override
        public int getItemCount() {
            return options.size();
        }

        public class MyViewHolderr extends RecyclerView.ViewHolder {
            public MainTextView tv_instruction;


            public MyViewHolderr(View view) {
                super(view);
                tv_instruction=view.findViewById(R.id.tv_instruction);
            }
        }
    }
    private class SecondScreenInstructionAdapter extends RecyclerView.Adapter<SecondScreenInstructionAdapter.MyViewHolderr> {

        List<ModelPamTemplates.Option> list;
        Context context;


        public SecondScreenInstructionAdapter(Context context, List<ModelPamTemplates.Option> list) {
            this.list = list;
            this.context = context;
        }


        @Override
        public MyViewHolderr onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_item_instruction_second_screen, parent, false);

            return new MyViewHolderr(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolderr holder, final int position) {

            holder.tv_instruction.setText(list.get(position).getOption());
            if (holder.RCV_nestedRecyclerView.getVisibility()==View.VISIBLE)
                holder.tv_instruction.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.svg_down_arrow,0);
            else
                holder.tv_instruction.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.svg_up_arrow,0);

            holder.tv_instruction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.RCV_nestedRecyclerView.getVisibility()==View.VISIBLE)
                        holder.tv_instruction.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.svg_down_arrow,0);
                    else
                        holder.tv_instruction.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.svg_up_arrow,0);

                    if (holder.RCV_nestedRecyclerView.getVisibility()==View.VISIBLE)
                    {
                        holder.RCV_nestedRecyclerView.setVisibility(View.GONE);
                    }
                    else
                    {

                        if (isValid(list.get(position).getDescription()))
                        {
                            String description=list.get(position).getDescription();
                            List<String> descriptionList = new ArrayList<String>(Arrays.asList(description.split(",")));
                            if (isValid(descriptionList))
                                setUpNestedGridView(holder,descriptionList);
                        }
                    }


                }
            });

        }

        private void setUpNestedGridView(MyViewHolderr holder, List<String> descriptionList) {
            mSecondScreenNestedAdapter = new SecondScreenNestedAdapter(getApplicationContext(), descriptionList);
            RecyclerView.LayoutManager manager=new LinearLayoutManager(context);
            holder.RCV_nestedRecyclerView.setLayoutManager(manager);
            holder.RCV_nestedRecyclerView.setAdapter(mSecondScreenNestedAdapter);
            holder.RCV_nestedRecyclerView.setItemAnimator(new DefaultItemAnimator());
            holder.RCV_nestedRecyclerView.setVisibility(View.VISIBLE);
            mSecondScreenNestedAdapter.notifyDataSetChanged();
            if (isValid(mModelPamTemplates.get(2).getTitle()))
                text_feel_ctxv.setText(mModelPamTemplates.get(2).getTitle());

            if (isValid(mModelPamTemplates.get(2).getQuestion()))
                third_sub_title.setText(mModelPamTemplates.get(2).getQuestion());
        }


        @Override
        public int getItemCount() {
            return list.size();
        }

        public class MyViewHolderr extends RecyclerView.ViewHolder {
            public MainTextView tv_instruction;
            private RecyclerView RCV_nestedRecyclerView;


            public MyViewHolderr(View view) {
                super(view);
                tv_instruction = view.findViewById(R.id.tv_instruction);
                RCV_nestedRecyclerView =view.findViewById(R.id.RCV_nestedRecyclerView);
            }
        }


        private class SecondScreenNestedAdapter extends RecyclerView.Adapter<SecondScreenNestedAdapter.MyViewHolderrr>{

            List<String> descriptionList;
            Context context;


            public SecondScreenNestedAdapter(Context context, List<String> descriptionList) {
                this.context=context;
                this.descriptionList=descriptionList;
            }


            @Override
            public MyViewHolderrr onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.adapter_item_nested_list, parent, false);

                return new MyViewHolderrr(itemView);
            }

            @Override
            public void onBindViewHolder(final MyViewHolderrr holder, final int position) {

                holder.tv_instruction.setText(descriptionList.get(position));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       /*button1.setBackgroundResource(R.drawable.circle_button);
                        button2.setBackgroundResource(R.drawable.circle_button);
                        button3.setBackgroundResource(R.drawable.circle_button);
                        button4.setBackgroundResource(R.drawable.circle_click_btn);


                        button1.setClickable(true);
                        button2.setClickable(true);
                        button3.setClickable(true);
                        button4.setClickable(true);

                        FirstScreenLayout.setVisibility(View.GONE);
                        second_screen_layout.setVisibility(View.GONE);
                        third_Screen_layout.setVisibility(View.GONE);
                        third_Screen_second_layout.setVisibility(View.GONE);
                        listPacifyrRlay.setVisibility(View.VISIBLE);
                        morePacifyrsRlay.setVisibility(View.GONE);*/

                        button1.setBackgroundResource(R.drawable.circle_button);
                        button2.setBackgroundResource(R.drawable.circle_button);
                        button3.setBackgroundResource(R.drawable.circle_click_btn);
                        button4.setBackgroundResource(R.drawable.circle_button);
                        button2.setClickable(true);

                        button1.setClickable(true);
                        button3.setClickable(true);
                        button4.setClickable(true);

                        FirstScreenLayout.setVisibility(View.GONE);
                        second_screen_layout.setVisibility(View.GONE);
                        third_Screen_layout.setVisibility(View.VISIBLE);
                        listPacifyrRlay.setVisibility(View.GONE);
                        EmotionSpecialityAdapter.reason_forcall=descriptionList.get(position);
                        setUpThirdScreen();
                    }
                });

            }
            private void setUpThirdScreen() {
                if (isValid(mModelPamTemplates)
                        &&isValid(mModelPamTemplates.get(2))&&isValid(mModelPamTemplates.get(2).getOptions()))
                {
                    mInstructionAdapter = new ThirdScreenInstructionAdapter(getApplicationContext(),mModelPamTemplates.get(2).getOptions());
                    RecyclerView.LayoutManager mLayout = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                    rcv_thirdScreen_instruct.setLayoutManager(mLayout);
                    rcv_thirdScreen_instruct.setAdapter(mInstructionAdapter);
                    rcv_thirdScreen_instruct.setItemAnimator(new DefaultItemAnimator());
                }

            }


            @Override
            public int getItemCount() {
                return descriptionList.size();
            }

            public class MyViewHolderrr extends RecyclerView.ViewHolder {
                public MainTextView tv_instruction;


                public MyViewHolderrr(View view) {
                    super(view);
                    tv_instruction=view.findViewById(R.id.tv_instruction);
                }
            }
        }

    }

    private class ThirdScreenInstructionAdapter extends RecyclerView.Adapter<ThirdScreenInstructionAdapter.MyViewHolderr>{

        private List<ModelPamTemplates.Option> options;
        Context context;


        public ThirdScreenInstructionAdapter(Context context, List<ModelPamTemplates.Option> options) {
            this.options=options;
            this.context=context;
        }


        @Override
        public MyViewHolderr onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_item_screen_third, parent, false);

            return new MyViewHolderr(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolderr holder, final int position) {

            holder.tv_instruction.setText(options.get(position).getOption());
            String[] separated = options.get(position).getOption().split("\\?");
            if (isValid(separated)&&separated.length==2)
                holder.tv_answer_template.setHint(separated[1]);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    button1.setBackgroundResource(R.drawable.circle_button);
                    button2.setBackgroundResource(R.drawable.circle_button);
                    button3.setBackgroundResource(R.drawable.circle_button);
                    button4.setBackgroundResource(R.drawable.circle_click_btn);


                    button1.setClickable(true);
                    button2.setClickable(true);
                    button3.setClickable(true);
                    button4.setClickable(true);

                    FirstScreenLayout.setVisibility(View.GONE);
                    second_screen_layout.setVisibility(View.GONE);
                    third_Screen_layout.setVisibility(View.GONE);
                    listPacifyrRlay.setVisibility(View.VISIBLE);

                }
            });

        }


        @Override
        public int getItemCount() {
            return options.size();
        }

        public class MyViewHolderr extends RecyclerView.ViewHolder {
            public MainTextView tv_instruction;
            private EditText tv_answer_template;


            public MyViewHolderr(View view) {
                super(view);
                tv_instruction=view.findViewById(R.id.tv_instruction);
                tv_answer_template=view.findViewById(R.id.tv_answer_template);
            }
        }
    }

}
