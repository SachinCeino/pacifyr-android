package com.pacifyr.client.home;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.daprlabs.cardstack.SwipeDeck;
import com.pacifyr.client.R;
import com.pacifyr.client.adapter.CategoryListAdapter;
import com.pacifyr.client.adapter.EmotionSpecialityAdapter;
import com.pacifyr.client.adapter.NegativeEmotionsAdapter;
import com.pacifyr.client.adapter.PostiveEmotionsAdapter;
import com.pacifyr.client.adapter.SwipeDeckConnectPacifyrAdapter;
import com.pacifyr.client.base.PacifyrActivity;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.event.AlertTypeEvent;
import com.pacifyr.client.event.CallEvent;
import com.pacifyr.client.event.ChatEvent;
import com.pacifyr.client.event.InternetConnectionEvent;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.model.ModelSpecialityByCategory;
import com.pacifyr.client.model.ModelSpecialityByEmotion;
import com.pacifyr.client.model.interfaces.CallChatEventListner;
import com.pacifyr.client.network.GsonUtils;
import com.pacifyr.client.network.NetworkService;
import com.pacifyr.client.orginals.Auth;
import com.pacifyr.client.quickblox.activities.PermissionsActivity;
import com.pacifyr.client.quickblox.utils.Consts;
import com.pacifyr.client.receiver.NotificationUtils;
import com.pacifyr.client.utility.PacifyrAppCompactActivity;
import com.pacifyr.client.utils.FirebaseLogging;
import com.pacifyr.client.utils.LogUtils;
import com.pacifyr.client.utils.SnackUtils;
import com.pacifyr.client.utils.Utility;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MListNegativeEmotions;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MListPositiveEmotions;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MUser;
import com.pacifyrapp.utilitylibrary.model.pacifyr.RespSearchPacifyrWOption;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.squareup.otto.Subscribe;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.pacifyr.client.network.NetworkService.GLOBAL_URL;
import static com.pacifyr.client.network.NetworkService.pSearchPacifyrWOptionsResponse;
import static com.pacifyr.client.twilio.VideoActivity.isCallActive;

public class ConnectToPacifyrActivity extends PacifyrAppCompactActivity implements CategoryListAdapter.EmotionClickkListener {

    private String pacifyrName;

    public DeviceFitImageView sadDimv;
    public DeviceFitImageView happyDimv;

    private final boolean isClick = false;
    private String type;

    public RelativeLayout selectFeelingRlay;
    public RelativeLayout selectEmotionRlay;
    public RelativeLayout selectEmotionDetailRlay;
    public RelativeLayout listPacifyrRlay;
    private MainTextView noPacifyrCtxv;
    private MainTextView txt_emergency_usa;

    private LinearLayout showallLlay;
    private MainTextView showallCtxv;

    RecyclerView emotionRcv;
    RecyclerView emotionDetailRcv;
    private MainTextView userNameCtxv;

    Button button1,button2,button3,button4;
    Button moreButton;
    public PrefManager prefM;
    public String token,userid,companyID;
    //private RecyclerView specialityRcv;
    public DeviceFitImageView emotionDimv;
    private SwipeDeck swipeDeck;
    private RelativeLayout morePacifyrsRlay;
    private ConnectToPacifyrActivity activity;

    private RelativeLayout topRlay;

    RecyclerView imgRcv;
    private String customerRole;
    private boolean employeeFlag;
    private SwipeDeck cardStack;
    public static String customerEmotion;
    private boolean showAllFlag=false;
    private CategoryListAdapter categoryListAdapter;
    private boolean selfSelectCategory = false;
    private ImageView image_logo;

    public static void start(Context context) {
        EmotionSpecialityAdapter.reason_forcall=null;
        Intent intent = new Intent(context, ConnectToPacifyrActivity.class);
        intent.putExtra("display","Pacifyr");
        NetworkService.isConnectSinglePacifyer=false;
        context.startActivity(intent);
    }
    public static void start(Activity activity) {
        EmotionSpecialityAdapter.reason_forcall=null;
        Intent intent = new Intent(activity, ConnectToPacifyrActivity.class);
        intent.putExtra("display","Pacifyr");
        NetworkService.isConnectSinglePacifyer=false;
        activity.startActivity(intent);
        activity.finish();
    }
    public static void start(Context context, boolean isConnectSinglePacifyer) {
        EmotionSpecialityAdapter.reason_forcall=null;
        Intent intent = new Intent(context, ConnectToPacifyrActivity.class);
        intent.putExtra("display","Pacifyr");
        NetworkService.isConnectSinglePacifyer=isConnectSinglePacifyer;
        context.startActivity(intent);
    }
    public static void start(Activity activity, boolean isConnectSinglePacifyer) {
        EmotionSpecialityAdapter.reason_forcall=null;
        Intent intent = new Intent(activity, ConnectToPacifyrActivity.class);
        intent.putExtra("display","Pacifyr");
        NetworkService.isConnectSinglePacifyer=isConnectSinglePacifyer;
        activity.startActivity(intent);
        activity.finish();
    }

    private Timer trackTimer_check_online;
    private TimerTask trackTimerTask_check_online;
    private int card_position=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_to_pacifyr);
       // checker = new PermissionsChecker(ConnectToPacifyrActivity.this);
        // permissionCheck();
//        UtilityNetworkService.init(this);
        // ToastHandler.newInstance(ConnectToPacifyrActivity.this).mustShowToast("Works under Progress");
        Bundle extras = getIntent().getExtras();
        pacifyrName = extras.getString("display");
        prefM= getPrefManager();
        token=prefM.getAccessToken();
        userid=prefM.getUserID();
        customerRole = prefM.getCustomerRole();
        companyID = prefM.getCompanyId();

        activity=this;

        onSetActionBar();
        noPacifyrCtxv = findViewById(R.id.no_pacifyr_ctxv);
        imgRcv = findViewById(R.id.image_rcv);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        moreButton = findViewById(R.id.more_button);
        userNameCtxv = findViewById(R.id.user_name_ctxv);
        sadDimv = findViewById(R.id.sad_dimv);
        happyDimv = findViewById(R.id.happy_dimv);
        selectFeelingRlay = findViewById(R.id.select_feeling_rlay);
        selectEmotionRlay = findViewById(R.id.select_emotion_rlay);
        listPacifyrRlay = findViewById(R.id.select_list_pacifyr_rlay);
        selectEmotionDetailRlay = findViewById(R.id.select_emotion_detail_rlay);
        emotionRcv = findViewById(R.id.emotion_rcv);
        emotionDetailRcv = findViewById(R.id.emotion_detail_rcv);
        morePacifyrsRlay = findViewById(R.id.more_pacifyrs_rlay);
        swipeDeck = findViewById(R.id.swipe_deck);
        emotionDimv = findViewById(R.id.emotion_dimv);
        showallLlay = findViewById(R.id.showall_llay);
        showallCtxv = findViewById(R.id.showall_ctxv);
        topRlay = findViewById(R.id.top_rlay);
        morePacifyrsRlay.setVisibility(View.GONE);
        image_logo = findViewById(R.id.image_logo);

        topRlay.bringToFront();

        txt_emergency_usa = findViewById(R.id.txt_emergency_usa);
        Utility gps=new Utility();
        String country = Utility.getUserCountry(ConnectToPacifyrActivity.this);
        if(isValid(country)) {
            country = prefM.getCountry();
            if (country.equalsIgnoreCase("us")) {
                txt_emergency_usa.setVisibility(View.VISIBLE);
            }
        }

        //specialityRcv = (RecyclerView) findViewById(R.id.speciality_rcv);
        defSetText(userNameCtxv,"Hello "+getUserName());
        selectFeelingRlay.setVisibility(View.VISIBLE);

        listPacifyrRlay.setVisibility(View.GONE);
        userDetailWeb(getUserID());

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
                selectFeelingRlay.setVisibility(View.VISIBLE);
                selectEmotionRlay.setVisibility(View.GONE);
                selectEmotionDetailRlay.setVisibility(View.GONE);
                listPacifyrRlay.setVisibility(View.GONE);
                morePacifyrsRlay.setVisibility(View.GONE);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectEmotionRlay.getVisibility()==View.VISIBLE ||selectEmotionDetailRlay.getVisibility()==View.VISIBLE||listPacifyrRlay.getVisibility() == View.VISIBLE)
                {
                    button2.setBackgroundResource(R.drawable.circle_click_btn);
                    button1.setBackgroundResource(R.drawable.circle_button);
                    button3.setBackgroundResource(R.drawable.circle_button);
                    button4.setBackgroundResource(R.drawable.circle_button);
                    selectFeelingRlay.setVisibility(View.GONE);
                    selectEmotionRlay.setVisibility(View.VISIBLE);
                    selectEmotionDetailRlay.setVisibility(View.GONE);
                    listPacifyrRlay.setVisibility(View.GONE);
                    morePacifyrsRlay.setVisibility(View.GONE);
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
                    morePacifyrsRlay.setVisibility(View.GONE);
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
                        Auth.httpStatusFilter(getApplicationContext(), status.getCode());
                        try {
                            hideProgress();
                            ModelSpecialityByEmotion modelSpeciltyByEmotion= GsonUtils.getInstance().gsonToModelSpeciltyByEmotion(json);
                            RecyclerView detailRcv = activity.findViewById(R.id.emotion_detail_rcv);
                            detailRcv.setLayoutManager(new LinearLayoutManager(ConnectToPacifyrActivity.this, LinearLayoutManager.VERTICAL, false));
                            EmotionSpecialityAdapter adapter=new EmotionSpecialityAdapter(ConnectToPacifyrActivity.this,activity,modelSpeciltyByEmotion.getResultsList());
                            detailRcv.setAdapter(adapter);
                        } catch (Exception e) {
                            hideProgress();
                            e.printStackTrace();
                        }
                    }
                };
                AQuery aq = new AQuery(ConnectToPacifyrActivity.this);
                cb.header("Content-Type", "application/json");
                cb.header("AccessToken",token);
                aq.ajax(url,JSONObject.class, cb);
            }
        });

        morePacifyrsRlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValid(PacifyrActivity.call_emotion)){
                    //ToastHandler.newInstance(ConnectToPacifyrActivity.this).showToast(PacifyrActivity.call_emotion.getTitle());
                }
                ListPacifyrScreenActivity.start(ConnectToPacifyrActivity.this, ConnectToPacifyrActivity.customerEmotion,showAllFlag);
            }
        });

        cardStack = activity.findViewById(R.id.swipe_deck);

        if(customerRole.equals("customer")){
            employeeFlag = false;
            //showallLlay.setVisibility(View.GONE);
        }
        else if(customerRole.equals("employee")||customerRole.equals("unpaidEmployee")){
            employeeFlag = true;
            showallLlay.setVisibility(View.VISIBLE);
        }
        else if(customerRole.equals("paidMember")){
            employeeFlag = false;
            //showallLlay.setVisibility(View.GONE);
        }else if(customerRole.equals("unpaidEmployee")){
            employeeFlag = true;
        }


        getShowallCbox().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showAllFlag = isChecked;
                UpdateSpecialityAdapter(showAllFlag);
            }
        });

        cardStack.setEventCallback(new SwipeDeck.SwipeEventCallback() {
            @Override
            public void cardSwipedLeft(int position) {
                Log.i("MainActivity", "card was swiped left, position in adapter: " + position);
                card_position=position;
            }

            @Override
            public void cardSwipedRight(int position) {
                Log.i("MainActivity", "card was swiped right, position in adapter: " + position);
                card_position=position;
            }


            @Override
            public void cardsDepleted() {
                Log.i("MainActivity", "no more cards");
                setAdapterSearchOption();
            }

            @Override
            public void cardActionDown() {

            }

            @Override
            public void cardActionUp() {

            }
        });



        if (prefM != null && prefM.getCompanyLogo() != null) {
            showImageWithAnim(prefM.getCompanyLogo(), image_logo);
        }
        else{
           hideImageWithAnim(image_logo);
        }


    }

    public void UpdateSpecialityAdapter(boolean param_showAllFlag) {

        try {
            String url = GLOBAL_URL + "specialities/byCategory?category=all";
            if (isEmployee()) {
                url = GLOBAL_URL + "specialities/byCategory?category=all&includeChargeablePacifyrs=" + param_showAllFlag;
            }

            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    Auth.httpStatusFilter(getApplicationContext(), status.getCode());
                    try {
                        String js = json.toString();

                        ModelSpecialityByCategory model = GsonUtils.getInstance().gsonToModelSpecialityByCategory(json);
                        ModelSpecialityByCategory.Results[] item = model.getResults();

                        if (item.length>0) {
                            imgRcv.setVisibility(View.VISIBLE);
                            CategoryListAdapter categoryListAdapter = new CategoryListAdapter(item, getApplicationContext());
                            imgRcv.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                            imgRcv.setAdapter(categoryListAdapter);
                            categoryListAdapter.addListener(activity);

                            int selectedCatogoryPosition = getPositionFromSeletedCatogory(item);
                            categoryListAdapter.setSelected(selectedCatogoryPosition);
                            imgRcv.scrollToPosition(selectedCatogoryPosition);
                        }
                        else
                        {
                            if (noPacifyrCtxv != null) {
                                noPacifyrCtxv.setVisibility(View.VISIBLE);
                            }
                            if ( cardStack!=null)
                            {
                                cardStack.setVisibility(View.GONE);
                            }
                            if (imgRcv!=null)
                            {
                                imgRcv.setVisibility(View.GONE);
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        if (noPacifyrCtxv != null) {
                            noPacifyrCtxv.setVisibility(View.VISIBLE);
                        }

                    }
                }
            };
            AQuery aq = new AQuery(getApplicationContext());
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", getPrefManager().getAccessToken());
            aq.ajax(url, JSONObject.class, cb);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getPositionFromSeletedCatogory(ModelSpecialityByCategory.Results[] item) {
        int selectedCatogoryPosition = 0;
        for (int i=0;i<item.length;i++)
            if (item[i].getTitle().equals(customerEmotion)) selectedCatogoryPosition = i;
        return selectedCatogoryPosition;
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


    public CheckBox getShowallCbox(){
        return (CheckBox) findViewById(R.id.showall_cbox);
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
                    Auth.httpStatusFilter(getApplicationContext(), 200);
                    try {
                        hideProgress();
                        MListPositiveEmotions modelListPosEmotions= GsonUtils.getInstance().gsonToMListPositiveEmotions(json);
                        emotionRcv.setLayoutManager(new GridLayoutManager(ConnectToPacifyrActivity.this, 4));
                        PostiveEmotionsAdapter adapter = new PostiveEmotionsAdapter(ConnectToPacifyrActivity.this,activity,modelListPosEmotions.getResultsList());
                        emotionRcv.setAdapter(adapter);

                    } catch (Exception e) {
                        hideProgress();
                        e.printStackTrace();
                    }
                }
            };
            AQuery aq = new AQuery(ConnectToPacifyrActivity.this);
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
                        emotionRcv.setLayoutManager(new GridLayoutManager(ConnectToPacifyrActivity.this, 4));
                        NegativeEmotionsAdapter adapter = new NegativeEmotionsAdapter(ConnectToPacifyrActivity.this,activity,modelListNegEmotions.getResultsList());
                        emotionRcv.setAdapter(adapter);
                    } catch (Exception e) {
                        hideProgress();
                        e.printStackTrace();
                    }
                }
            };
            AQuery aq = new AQuery(ConnectToPacifyrActivity.this);
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken",token);
            aq.ajax(url,JSONObject.class, cb);

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
                LogUtils.LOGD("session","CPA  onActivityResult event_call " + event_call);
                if(isValid(event_call))
                if(granted) {
                    if(audioonly) {
                        if(checker.lacksPermissions(Consts.PERMISSIONS[1])) {
                            Toast.makeText(activity, "call to do 1 in onactivtyresult1", Toast.LENGTH_SHORT).show();

                            /*setAlertStartCall("Start", "Call now....", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                        hideStatus();
                                        deleteInstallationWeb();

                                }
                            });*/
                            //checkRoleRequest(event_call.mpacifyer, listner, event_call.isVideoCall);
                        }
                    }
                    else {
                        if(checker.lacksPermissions(Consts.PERMISSIONS)) {
                            Toast.makeText(activity, "call to do 2 in onactivtyresult1", Toast.LENGTH_SHORT).show();



                         /*   setAlertStartCall("Start", "Call now....", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                       *//* hideStatus();
                                        deleteInstallationWeb();*//*

                                }
                            });*/
                           // checkRoleRequest(event_call.mpacifyer, listner, event_call.isVideoCall);
                        }
                    }

                }
            }
        }catch (Exception e)
        {
            LogUtils.LOGD("session","CPA  onActivityResult exce " + e.getMessage());
        }
    }
    @Subscribe
    public void onNotifyEvent(NotifyEvent event) {

    }

    @Override
    public void onAlertTypeEvent(AlertTypeEvent event) {

    }
    CallEvent event_call=null;
    @Subscribe
    public void onCallEvent(final CallEvent event) {
        try{
            LogUtils.LOGD("session","CPA onCallEvent called " );
              if(!isValid(event.mpacifyer,"Please try again"))
                return;
               event_call=event;
                if( event_call.isVideoCall) {

                        setAlertStartCall(event.mpacifyer, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                hideStatus();
                                checkRoleRequest(event.mpacifyer, listner, event.isVideoCall);
                            }
                        });

                        //callRequestWeb(pacifyer_obj, listner, true);

                    /*if (checker.lacksPermissions(Consts.PERMISSIONS)) {
                        event_call=event;

                        startPermissionsActivity(false,ConnectToPacifyrActivity.this);

                        if(isLoggedInChat()) {

                            setAlertStartCall(event.mpacifyer, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            hideStatus();
                                            checkRoleRequest(event.mpacifyer, listner, event.isVideoCall);
                                        }
                                    });

                            //callRequestWeb(pacifyer_obj, listner, true);
                        }
                    }
                    else {
                        if(isLoggedInChat()) {

                            setAlertStartCall(event.mpacifyer, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            hideStatus();
                                            checkRoleRequest(event.mpacifyer, listner, event.isVideoCall);
                                        }
                                    });

                            *//*
                            //callRequestWeb(event.mpacifyer, listner, event.isVideoCall);
                            checkRoleRequest(event.mpacifyer, listner, event.isVideoCall);*//*
                        }
                    }*/
                }
                else {


                        setAlertStartCall(event.mpacifyer, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                hideStatus();
                                checkRoleRequest(event.mpacifyer, listner, event.isVideoCall);
                            }
                        });

                        //callRequestWeb(pacifyer_obj, listner, true);

                    /*if (checker.lacksPermissions(Consts.PERMISSIONS[1])) {
                        event_call=event;

                        startPermissionsActivity(true,ConnectToPacifyrActivity.this);

                        if(isLoggedInChat()) {

                            setAlertStartCall(event.mpacifyer, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            hideStatus();
                                            checkRoleRequest(event.mpacifyer, listner, event.isVideoCall);
                                        }
                                    });

                            //callRequestWeb(pacifyer_obj, listner, true);
                        }

                    }
                    else {
                        if(isLoggedInChat()) {
                            setAlertStartCall(event.mpacifyer, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            hideStatus();
                                            checkRoleRequest(event.mpacifyer, listner, event.isVideoCall);
                                        }
                                    });


                            //callRequestWeb(event.mpacifyer, listner, event.isVideoCall);
//                            checkRoleRequest(event.mpacifyer, listner, event.isVideoCall);
                        }
                    }*/
                }

        }catch (Exception e)
        {
            LogUtils.LOGD("session","CPA onCallEvent exce " + e.getMessage());
        }

    }

    @Subscribe
    public void onChatEvent(ChatEvent event) {
        try{
            LogUtils.LOGD("session","CPA onCallEvent called " );
            if(!isValid(event.mpacifyer,"Network Error"))
                return;
            boolean isChatSubscriptionRequired = event.mpacifyer.isChatSubscriptionRequired();
            int priceForChat = Integer.parseInt(event.mpacifyer.getPriceForChat());

            if(!isChatSubscriptionRequired)
            {
                startChat(event.mpacifyer);
                //showIOSDialogOK("","Please purchase unlimited chat per day","OK");

            }else if(priceForChat==0){
                startChat(event.mpacifyer);
            }else {
                //chatLlay.setVisibility(View.VISIBLE);

                if (event.mpacifyer.getChatSubscription() == null) {
                    showIOSDialogOK("","Please purchase unlimited chat per day","OK");
                    //chat_price_ctxv.setText("Chat for " + Utility.getPriceSign(ProfileScreenActivity.this) + priceForChat + " a day");
                } else {
                    startChat(event.mpacifyer);
                }
            }
            //startChat(event.mpacifyer);

        }catch (Exception e)
        {
            LogUtils.LOGD("session","CPA ChatEvent exce " + e.getMessage());
        }

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
        leftTxv.setVisibility(View.GONE);
        backImv.setVisibility(View.VISIBLE);
        titleTxv.setVisibility(View.VISIBLE);
        leftLlay.setVisibility(View.VISIBLE);
        /*defSetText(titleTxv,"Connect to "+pacifyrName);*/
        defSetText(titleTxv, "CONNECT");
        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/ActionBarCommonFont.otf");
        titleTxv.setTypeface(type);


        backImv.setVisibility(View.VISIBLE);
        backImv.setImageDrawable(getResources().getDrawable(R.drawable.ic_cancel2));
        //backImv.setBackgroundResource(R.drawable.leftmen);
        backImv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            /*    Intent intent = new Intent(ConnectToPacifyrActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();*/
                DashboardActivity.start(ConnectToPacifyrActivity.this);
            }
        });
    }
    @Override
    public void onBackPressed() {
        //
        DashboardActivity.start(ConnectToPacifyrActivity.this);
       /* Intent intent = new Intent(ConnectToPacifyrActivity.this, DashboardActivity.class);
        startActivity(intent);
        finish();*/
    }

   public CallChatEventListner callchatEventListner =new CallChatEventListner() {
        @Override
        public void onCallEventClick(CallEvent event) {
            if (isValid(isCallActive) && isCallActive)
                return;
            onCallEvent(event);
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


    @Override
    public void OnEmotionClickkListener() {
        showProgress();
        if (adapter != null) {
            adapter.clear();
        }
        //showCategories(customerEmotion);
        webSearchPacifyrWOptions(customerEmotion);
    }


    public void webSearchPacifyrWOptions(String emotion) {
        try {
            setLoading("loading...");
            showProgress();
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    try {
                        hideProgress();
                        pSearchPacifyrWOptionsResponse= GsonUtils.getInstance().gsonToRespSearchPacifyrWOption(json);
                        //searchResults=modelSearchPacifyr.getResultsList();
                        pSearchPacifyrWOptionsResponse.toString();
                        setAdapterSearchOption();
                        morePacifyrsRlay.setVisibility(View.VISIBLE);
                        try {
                            if (adapter != null && adapter.getCount() == 0 && !selfSelectCategory) {
                                selfSelectCategory = true;
                                ((CategoryListAdapter) imgRcv.getAdapter()).setSelected(0);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();//RespSearchPacifyrWOption
                    }
                }
            };

            String url;
            AQuery aq = new AQuery(ConnectToPacifyrActivity.this);
            JSONObject jsonObject;
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", token);
            jsonObject = new JSONObject();

            url = GLOBAL_URL + getRoleSearchMap().get(prefM.getCustomerRole());
            companyID = prefM.getCompanyId();
            jsonObject.putOpt("companyId", companyID);
            if (prefM.getCustomerRole().equals("employee")) {
                jsonObject.putOpt("includeChargeablePacifyrs", showAllFlag);
            }
            jsonObject.putOpt("initiatorId", userid);
            jsonObject.putOpt("country", prefM.getCountry());
            List<String> list = new ArrayList<String>();
            list.add(emotion);
            //list.add("b");
            JSONArray array = new JSONArray();
            for (int i = 0; i < list.size(); i++) {
                array.put(list.get(i));
            }
            jsonObject.putOpt("specialities", array);
            aq.post(url, jsonObject, JSONObject.class, cb);
            //aq.ajax(url,JSONObject.class, cb);
        }
        catch (Exception ex)
        {
            hideProgress();
            ex.printStackTrace();
        }
    }

    public SwipeDeckConnectPacifyrAdapter adapter;

    private void setAdapterSearchOption() {

        if(!NetworkService.isConnectSinglePacifyer) {
            if (isValid(pSearchPacifyrWOptionsResponse) && isValid(pSearchPacifyrWOptionsResponse.getResultsList())) {
                cardStack.setVisibility(View.VISIBLE);
                noPacifyrCtxv.setVisibility(View.GONE);
                adapter = new SwipeDeckConnectPacifyrAdapter(pSearchPacifyrWOptionsResponse.getResultsList(), ConnectToPacifyrActivity.this,activity);
                cardStack.setAdapter(adapter);
            }else{
                cardStack.setVisibility(View.GONE);
                noPacifyrCtxv.setVisibility(View.VISIBLE);
            }
        }

    }

    private void checkOnLineOrOffline() {


        webCheckOnlineOffline(customerEmotion);
    }


    private void webCheckOnlineOffline(String emotion) {

        try {
            hideProgress();
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    try {

                        RespSearchPacifyrWOption mRespSearchPacifyrWOption=GsonUtils.getInstance().gsonToRespSearchPacifyrWOption(json);

                        if (isValid(mRespSearchPacifyrWOption.getResultsList()))
                        {


                            if (isValid(pSearchPacifyrWOptionsResponse.getResultsList()))
                            {

                                for (int i=0;i<mRespSearchPacifyrWOption.getResultsList().size();i++)
                                {
                                    for (int j=0;j<pSearchPacifyrWOptionsResponse.getResultsList().size();j++)
                                    {
                                        if (mRespSearchPacifyrWOption.getResultsList().get(i).getId().equals(pSearchPacifyrWOptionsResponse.getResultsList().get(j).getId()))
                                        {

                                            if (mRespSearchPacifyrWOption.getResultsList().get(i).getOnline()!=pSearchPacifyrWOptionsResponse.getResultsList().get(j).getOnline())
                                            {
                                                pSearchPacifyrWOptionsResponse.getResultsList().get(j).setOnline(mRespSearchPacifyrWOption.getResultsList().get(i).getOnline());
                                                adapter.notifyDataSetChanged();
                                                adapter.notifyDataSetInvalidated();
                                                cardStack.setSelection(card_position+1);


                                            }

                                        }
                                    }

                                }
                            }

                        }



                    } catch (Exception e) {
                        e.printStackTrace();//RespSearchPacifyrWOption
                    }
                }
            };

            String url;
            AQuery aq = new AQuery(ConnectToPacifyrActivity.this);
            JSONObject jsonObject;
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", token);
            jsonObject = new JSONObject();

            url = GLOBAL_URL + getRoleSearchMap().get(prefM.getCustomerRole());
            companyID = prefM.getCompanyId();
            jsonObject.putOpt("companyId", companyID);
            if (prefM.getCustomerRole().equals("employee")) {
                jsonObject.putOpt("includeChargeablePacifyrs", showAllFlag);
            }
            jsonObject.putOpt("initiatorId", userid);
            jsonObject.putOpt("country", prefM.getCountry());
            List<String> list = new ArrayList<String>();
            list.add(emotion);
            //list.add("b");
            JSONArray array = new JSONArray();
            for (int i = 0; i < list.size(); i++) {
                array.put(list.get(i));
            }
            jsonObject.putOpt("specialities", array);
            aq.post(url, jsonObject, JSONObject.class, cb);
            //aq.ajax(url,JSONObject.class, cb);
        }
        catch (Exception ex)
        {
            hideProgress();
            ex.printStackTrace();
        }
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

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

    private final BroadcastReceiver notificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.hasExtra("message")) {
                String message = intent.getExtras().getString("message");
                String type = intent.getExtras().getString("type");
                if (message != null && message != "") {
                    if (type.equals("chat")) {
                        SnackUtils.show(actionBarTopLlay,message,ConnectToPacifyrActivity.this,getLayoutInflater());
                    }
                }
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(notificationReceiver, new IntentFilter(NotificationUtils.NOTIFY_INTENT));
        if (swipeDeck.getVisibility()==View.VISIBLE && listPacifyrRlay.getVisibility()==View.VISIBLE)
        {
            morePacifyrsRlay.setVisibility(View.VISIBLE);
        }
        else {
            morePacifyrsRlay.setVisibility(View.GONE);
        }

        trackTimer_check_online = new Timer();

            trackTimerTask_check_online = new TimerTask() {
                @Override
                public void run() {

                    if (isValid(listPacifyrRlay))
                    {
                        if (listPacifyrRlay.getVisibility()==View.VISIBLE)
                        {
                            checkOnLineOrOffline();

                        }
                    }

                }
            };
            trackTimer_check_online.scheduleAtFixedRate(trackTimerTask_check_online, 0, 10000);


        if (isValid(isCallActive) && isCallActive) {
            showSnackbarPopup();
        } else {
            dismissSnackbarPopup();
        }

        Bundle args =
                FirebaseLogging.getConstantArgsLoggedIn(ConnectToPacifyrActivity.this);

        args.putString(FirebaseLogging.EC, ConnectToPacifyrActivity.class.getSimpleName());

        FirebaseLogging.sendLog(ConnectToPacifyrActivity.this,
                FirebaseLogging.EVENT_ON_SCREEN,
                args);

    }


    public void showScheduleDialogueFragment(MUser pacifyer_obj) {
        if (isValid(pacifyer_obj))
        {
            callPacifyrForRefreshBeforeScheduleUI(pacifyer_obj.getId());
        }

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
        Auth.foreground("");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(notificationReceiver);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        Auth.foreground(ConnectToPacifyrActivity.class.getSimpleName());

    }
}
