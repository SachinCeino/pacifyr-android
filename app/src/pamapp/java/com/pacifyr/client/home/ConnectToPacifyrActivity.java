package com.pacifyr.client.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.daprlabs.cardstack.SwipeDeck;
import com.pacifyr.client.R;
import com.pacifyr.client.adapter.CategoryListAdapter;
import com.pacifyr.client.adapter.EmotionSpecialityAdapter;
import com.pacifyr.client.adapter.SwipeDeckConnectPacifyrAdapter;
import com.pacifyr.client.base.PacifyrActivity;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.event.AlertTypeEvent;
import com.pacifyr.client.event.CallEvent;
import com.pacifyr.client.event.ChatEvent;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.fragment.schedule.ScheduleBottomSheetFragment;
import com.pacifyr.client.model.ModelPamTemplates;
import com.pacifyr.client.model.ModelSpecialityByCategory;
import com.pacifyr.client.model.interfaces.CallChatEventListner;
import com.pacifyr.client.network.GsonUtils;
import com.pacifyr.client.network.NetworkService;
import com.pacifyr.client.quickblox.activities.PermissionsActivity;
import com.pacifyr.client.quickblox.utils.Consts;
import com.pacifyr.client.utility.PacifyrAppCompactActivity;
import com.pacifyr.client.utils.LogUtils;
import com.pacifyr.client.utils.Utility;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MEmotion;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MUser;
import com.pacifyrapp.utilitylibrary.model.pacifyr.RespSearchPacifyrWOption;
import com.pacifyrapp.utilitylibrary.network.UtilityNetworkService;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.pacifyr.client.network.NetworkService.GLOBAL_URL;
import static com.pacifyr.client.network.NetworkService.pSearchPacifyrWOptionsResponse;

public class ConnectToPacifyrActivity extends PacifyrAppCompactActivity implements CategoryListAdapter.EmotionClickkListener {

    private String pacifyrName;

    public DeviceFitImageView sadDimv;
    public DeviceFitImageView happyDimv;

    private boolean isClick = false;
    private String type;

    public RelativeLayout FirstScreenLayout;
    public RelativeLayout second_screen_layout;
    public RelativeLayout third_Screen_layout;
    public RelativeLayout listPacifyrRlay;
    private MainTextView noPacifyrCtxv;
    private MainTextView txt_emergency_usa;

    private LinearLayout showallLlay;
    private MainTextView showallCtxv;

    RecyclerView emotionRcv;
    RecyclerView emotionDetailRcv;
    private MainTextView userNameCtxv;

    Button button1, button2, button3, button4;
    Button moreButton;
    public PrefManager prefM;
    public String token, userid, companyID;
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
    private boolean showAllFlag;
    private CategoryListAdapter categoryListAdapter;
    private boolean selfSelectCategory = false;
    private RecyclerView rcv_secondScreen_Expandable, rcv_thirdScreen_instruct, rcv_thirdScreen_second_instruct;
    private SecondScreenInstructionAdapter mInstructionSecondAdapter;
    private ThirdScreenInstructionAdapter mInstructionAdapter;
    //private ThirdScreenInstructionSubAdapter mInstructionBAdapter;


    public static void start(Context context) {
        EmotionSpecialityAdapter.reason_forcall = null;
        Intent intent = new Intent(context, ConnectToPacifyrActivity.class);
        intent.putExtra("display", "Pacifyr");
        NetworkService.isConnectSinglePacifyer = false;
        context.startActivity(intent);
    }

    public static void start(Activity activity) {
        EmotionSpecialityAdapter.reason_forcall = null;
        Intent intent = new Intent(activity, ConnectToPacifyrActivity.class);
        intent.putExtra("display", "Pacifyr");
        NetworkService.isConnectSinglePacifyer = false;
        activity.startActivity(intent);
        activity.finish();
    }

    public static void start(Context context, boolean isConnectSinglePacifyer) {
        EmotionSpecialityAdapter.reason_forcall = null;
        Intent intent = new Intent(context, ConnectToPacifyrActivity.class);
        intent.putExtra("display", "Pacifyr");
        NetworkService.isConnectSinglePacifyer = isConnectSinglePacifyer;
        context.startActivity(intent);
    }

    public static void start(Activity activity, boolean isConnectSinglePacifyer) {
        EmotionSpecialityAdapter.reason_forcall = null;
        Intent intent = new Intent(activity, ConnectToPacifyrActivity.class);
        intent.putExtra("display", "Pacifyr");
        NetworkService.isConnectSinglePacifyer = isConnectSinglePacifyer;
        activity.startActivity(intent);
        activity.finish();
    }

    private Timer trackTimer_check_online;
    private TimerTask trackTimerTask_check_online;
    private int card_position = 0;
    private TextView text_feel_ctxv;
    private List<ModelPamTemplates> mModelPamTemplates;
    private RecyclerView RecyclerView_conditions;
    private FirstScreenConditionAdapter mFirstScreenConditionAdapter;
    private SecondScreenInstructionAdapter.SecondScreenNestedAdapter mSecondScreenNestedAdapter;
    private TextView third_sub_title;
    private LinearLayout submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_to_pacifyr);
        // checker = new PermissionsChecker(ConnectToPacifyrActivity.this);
        // permissionCheck();

        // ToastHandler.newInstance(ConnectToPacifyrActivity.this).mustShowToast("Works under Progress");
        Bundle extras = getIntent().getExtras();
        pacifyrName = extras.getString("display");
        prefM = getPrefManager();
        token = prefM.getAccessToken();
        userid = prefM.getUserID();
        customerRole = prefM.getCustomerRole();
        companyID = prefM.getCompanyId();

        activity = this;

        onSetActionBar();
        noPacifyrCtxv = findViewById(R.id.no_pacifyr_ctxv);
        imgRcv = findViewById(R.id.image_rcv);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        //button4 = findViewById(R.id.button4);
        // moreButton = findViewById(R.id.more_button);
        userNameCtxv = findViewById(R.id.user_name_ctxv);
        // happyDimv = findViewById(R.id.happy_dimv);
        FirstScreenLayout = findViewById(R.id.FirstScreenLayout);
        second_screen_layout = findViewById(R.id.second_screen_layout);
        listPacifyrRlay = findViewById(R.id.select_list_pacifyr_rlay);
        third_Screen_layout = findViewById(R.id.third_Screen_layout);
        // emotionRcv = findViewById(R.id.emotion_rcv);
        // emotionDetailRcv = findViewById(R.id.emotion_detail_rcv);
        morePacifyrsRlay = findViewById(R.id.more_pacifyrs_rlay);
        swipeDeck = findViewById(R.id.swipe_deck);
        emotionDimv = findViewById(R.id.emotion_dimv);
        showallLlay = findViewById(R.id.showall_llay);
        showallCtxv = findViewById(R.id.showall_ctxv);
        topRlay = findViewById(R.id.top_rlay);
        morePacifyrsRlay.setVisibility(View.GONE);


        topRlay.bringToFront();
        text_feel_ctxv = findViewById(R.id.text_feel_ctxv);
        text_feel_ctxv.setText(getResources().getString(R.string.get_ready_feel_better));
        txt_emergency_usa = findViewById(R.id.txt_emergency_usa);
        submit = findViewById(R.id.submit);
        Utility gps = new Utility();
        String country = Utility.getUserCountry(ConnectToPacifyrActivity.this);
        if (isValid(country)) {
            country = prefM.getCountry();
            if (country.equalsIgnoreCase("us")) {
                //txt_emergency_usa.setVisibility(View.VISIBLE);
            }
        }

        //specialityRcv = (RecyclerView) findViewById(R.id.speciality_rcv);
        defSetText(userNameCtxv, "Hello " + getUserName());
        FirstScreenLayout.setVisibility(View.VISIBLE);

        listPacifyrRlay.setVisibility(View.GONE);
        rcv_thirdScreen_instruct = findViewById(R.id.rcv_thirdScreen_instruct);
        rcv_secondScreen_Expandable = findViewById(R.id.rcv_secondScreen_Expandable);
        third_sub_title=findViewById(R.id.third_sub_title);
        RecyclerView_conditions = findViewById(R.id.RecyclerView_conditions);
        // makeThirdScreenInstructionList();
        makeFirstScreenInstructionList();

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button1.setBackgroundResource(R.drawable.circle_click_btn);
                button2.setBackgroundResource(R.drawable.circle_button);
                button3.setBackgroundResource(R.drawable.circle_button);
               // button4.setBackgroundResource(R.drawable.circle_button);
                FirstScreenLayout.setVisibility(View.VISIBLE);
                second_screen_layout.setVisibility(View.GONE);
                third_Screen_layout.setVisibility(View.GONE);
                listPacifyrRlay.setVisibility(View.GONE);
                morePacifyrsRlay.setVisibility(View.GONE);

                if (isValid(mModelPamTemplates.get(0).getTitle()))
                    text_feel_ctxv.setText(mModelPamTemplates.get(0).getTitle());
                else
                if (isValid(mModelPamTemplates.get(0).getQuestion()))
                    text_feel_ctxv.setText(mModelPamTemplates.get(0).getQuestion());
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (second_screen_layout.getVisibility() == View.VISIBLE || third_Screen_layout.getVisibility() == View.VISIBLE || listPacifyrRlay.getVisibility() == View.VISIBLE) {
                    button2.setBackgroundResource(R.drawable.circle_click_btn);
                    button1.setBackgroundResource(R.drawable.circle_button);
                    button3.setBackgroundResource(R.drawable.circle_button);
                   // button4.setBackgroundResource(R.drawable.circle_button);
                    FirstScreenLayout.setVisibility(View.GONE);
                    second_screen_layout.setVisibility(View.VISIBLE);

                    third_Screen_layout.setVisibility(View.GONE);
                    listPacifyrRlay.setVisibility(View.GONE);
                    morePacifyrsRlay.setVisibility(View.GONE);
                } else {
                    Toast.makeText(v.getContext(), "Please select option", Toast.LENGTH_SHORT).show();
                }

                if (isValid(mModelPamTemplates.get(1).getTitle()))
                    text_feel_ctxv.setText(mModelPamTemplates.get(1).getTitle());
                else
                if (isValid(mModelPamTemplates.get(1).getQuestion()))
                    text_feel_ctxv.setText(mModelPamTemplates.get(1).getQuestion());

            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (third_Screen_layout.getVisibility() == View.VISIBLE || listPacifyrRlay.getVisibility() == View.VISIBLE) {
                    button3.setBackgroundResource(R.drawable.circle_click_btn);
                    button2.setBackgroundResource(R.drawable.circle_button);
                    button1.setBackgroundResource(R.drawable.circle_button);
                   // button4.setBackgroundResource(R.drawable.circle_button);
                    FirstScreenLayout.setVisibility(View.GONE);
                    second_screen_layout.setVisibility(View.GONE);
                    third_Screen_layout.setVisibility(View.VISIBLE);
                    listPacifyrRlay.setVisibility(View.GONE);
                    morePacifyrsRlay.setVisibility(View.GONE);
                    text_feel_ctxv.setText(getResources().getString(R.string.we_are_almost_ready));
                } else {
                    Toast.makeText(v.getContext(), "Please select option", Toast.LENGTH_SHORT).show();
                }
                if (isValid(mModelPamTemplates.get(2).getTitle()))
                    text_feel_ctxv.setText(mModelPamTemplates.get(2).getTitle());
                else
                if (isValid(mModelPamTemplates.get(2).getQuestion()))
                    text_feel_ctxv.setText(mModelPamTemplates.get(2).getQuestion());

            }
        });
       /* button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listPacifyrRlay.getVisibility() == View.VISIBLE) {
                    button4.setBackgroundResource(R.drawable.circle_click_btn);
                    button2.setBackgroundResource(R.drawable.circle_button);
                    button3.setBackgroundResource(R.drawable.circle_button);
                    button1.setBackgroundResource(R.drawable.circle_button);
                    FirstScreenLayout.setVisibility(View.GONE);
                    second_screen_layout.setVisibility(View.GONE);
                    third_Screen_layout.setVisibility(View.GONE);
                    listPacifyrRlay.setVisibility(View.VISIBLE);
                    text_feel_ctxv.setText(getResources().getString(R.string.you_are_all_set));
                } else {
                    Toast.makeText(v.getContext(), "Please select option", Toast.LENGTH_SHORT).show();
                }

            }
        });*/

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ConnectToPacifyrActivity.this, ListPacifyrScreenActivity.class);
                startActivity(i);

              /* // button4.setBackgroundResource(R.drawable.circle_click_btn);
                button2.setBackgroundResource(R.drawable.circle_button);
                button3.setBackgroundResource(R.drawable.circle_button);
                button1.setBackgroundResource(R.drawable.circle_button);
                FirstScreenLayout.setVisibility(View.GONE);
                second_screen_layout.setVisibility(View.GONE);
                third_Screen_layout.setVisibility(View.GONE);
                listPacifyrRlay.setVisibility(View.VISIBLE);
                text_feel_ctxv.setText(getResources().getString(R.string.you_are_all_set));*/
            }
        });


        /*sadDimv.setOnClickListener(new View.OnClickListener() {
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
        });*/

      /*  happyDimv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  sadDimv.clearColorFilter();
                happyDimv.setColorFilter(getResources().getColor( R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);
                type="positive";
                button1.setBackgroundResource(R.drawable.circle_button);
                button2.setBackgroundResource(R.drawable.circle_click_btn);
                //callFragmant();
                PacifyrActivity.call_emotion=new MEmotion();
                PacifyrActivity.call_emotion.setId("5971c472734d1d6202a81958");
                changeToSecondScreen();

            }
        });*/

      /*  moreButton.setOnClickListener(new View.OnClickListener() {
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
*/
        morePacifyrsRlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid(PacifyrActivity.call_emotion)) {
                    //ToastHandler.newInstance(ConnectToPacifyrActivity.this).showToast(PacifyrActivity.call_emotion.getTitle());
                }
                ListPacifyrScreenActivity.start(ConnectToPacifyrActivity.this, ConnectToPacifyrActivity.customerEmotion, showAllFlag);
            }
        });

        cardStack = activity.findViewById(R.id.swipe_deck);

        if (customerRole.equals("customer")) {
            employeeFlag = false;
            showallLlay.setVisibility(View.GONE);
        } else if (customerRole.equals("employee") || customerRole.equals("unpaidEmployee")) {
            employeeFlag = true;
            showallLlay.setVisibility(View.VISIBLE);
        } else if (customerRole.equals("paidMember")) {
            employeeFlag = false;
            showallLlay.setVisibility(View.GONE);
        } else if (customerRole.equals("unpaidEmployee")) {
            employeeFlag = true;
        }

        showCategories();
        getShowallCbox().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    showAllFlag = true;
                    webSearchPacifyrWOptions(customerEmotion);
                } else {
                    showAllFlag = false;
                    webSearchPacifyrWOptions(customerEmotion);
                }
            }
        });

        cardStack.setEventCallback(new SwipeDeck.SwipeEventCallback() {
            @Override
            public void cardSwipedLeft(int position) {
                Log.i("MainActivity", "card was swiped left, position in adapter: " + position);
                card_position = position;
            }

            @Override
            public void cardSwipedRight(int position) {
                Log.i("MainActivity", "card was swiped right, position in adapter: " + position);
                card_position = position;
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


    }

    private void makeFirstScreenInstructionList() {

        try {
            PrefManager prefM = PrefManager.getInstance(ConnectToPacifyrActivity.this);
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
            AQuery aq = new AQuery(ConnectToPacifyrActivity.this);
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", token);
            aq.ajax(url, JSONArray.class, cb);
        } catch (Exception e) {
            hideProgress();
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


    public CheckBox getShowallCbox() {
        return (CheckBox) findViewById(R.id.showall_cbox);
    }

    public void changeToSecondScreen() {


        second_screen_layout.setVisibility(View.VISIBLE);
        FirstScreenLayout.setVisibility(View.GONE);
        text_feel_ctxv.setText(getResources().getString(R.string.do_you_have_any_symptoms));
        //setLoading();
        //showProgress();

        if (type.equals("positive")) {

           /* String url = GLOBAL_URL+"emotions/positive";
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
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
            aq.ajax(url,JSONObject.class, cb);*/

        } else if (type.equals("negative")) {

           /* String url = GLOBAL_URL+"emotions/negative";
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
            aq.ajax(url,JSONObject.class, cb);*/

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // check if the request code is same as what is passed  here it is 2
            LogUtils.LOGD("session", "CPA  onActivityResult requestCode " + requestCode);
            if (requestCode == PermissionsActivity.PERMISSION_EXTERNAL_REQUEST_CODE) {
                boolean granted = data.getBooleanExtra("permissions", false);
                boolean audioonly = data.getBooleanExtra(PermissionsActivity.CHECK_ONLY_AUDIO, false);
                LogUtils.LOGD("session", "CPA  onActivityResult granted " + granted);
                LogUtils.LOGD("session", "CPA  onActivityResult audioonly " + audioonly);
                LogUtils.LOGD("session", "CPA  onActivityResult event_call " + event_call);
                if (isValid(event_call))
                    if (granted) {
                        if (audioonly) {
                            if (checker.lacksPermissions(Consts.PERMISSIONS[1])) {
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
                        } else {
                            if (checker.lacksPermissions(Consts.PERMISSIONS)) {
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
        } catch (Exception e) {
            LogUtils.LOGD("session", "CPA  onActivityResult exce " + e.getMessage());
        }
    }

    @Subscribe
    public void onNotifyEvent(NotifyEvent event) {

    }

    @Override
    public void onAlertTypeEvent(AlertTypeEvent event) {

    }

    CallEvent event_call = null;

    @Subscribe
    public void onCallEvent(final CallEvent event) {
        try {
            LogUtils.LOGD("session", "CPA onCallEvent called ");
            if (!isValid(event.mpacifyer, "Please try again"))
                return;
            event_call = event;
            if (event_call.isVideoCall) {
                if (checker.lacksPermissions(Consts.PERMISSIONS)) {
                    event_call = event;

                    startPermissionsActivity(false, ConnectToPacifyrActivity.this);

                    if (isLoggedInChat()) {

                        setAlertStartCall(event.mpacifyer, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                hideStatus();
                                checkRoleRequest(event.mpacifyer, listner, event.isVideoCall);
                            }
                        });

                        //callRequestWeb(pacifyer_obj, listner, true);
                    }
                } else {
                    if (isLoggedInChat()) {

                        setAlertStartCall(event.mpacifyer, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                hideStatus();
                                checkRoleRequest(event.mpacifyer, listner, event.isVideoCall);
                            }
                        });

                            /*
                            //callRequestWeb(event.mpacifyer, listner, event.isVideoCall);
                            checkRoleRequest(event.mpacifyer, listner, event.isVideoCall);*/
                    }
                }
            } else {
                if (checker.lacksPermissions(Consts.PERMISSIONS[1])) {
                    event_call = event;

                    startPermissionsActivity(true, ConnectToPacifyrActivity.this);

                    if (isLoggedInChat()) {

                        setAlertStartCall(event.mpacifyer, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                hideStatus();
                                checkRoleRequest(event.mpacifyer, listner, event.isVideoCall);
                            }
                        });

                        //callRequestWeb(pacifyer_obj, listner, true);
                    }

                } else {
                    if (isLoggedInChat()) {
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
                }
            }

        } catch (Exception e) {
            LogUtils.LOGD("session", "CPA onCallEvent exce " + e.getMessage());
        }

    }

    @Subscribe
    public void onChatEvent(ChatEvent event) {
        try {
            LogUtils.LOGD("session", "CPA onCallEvent called ");
            if (!isValid(event.mpacifyer, "Network Error"))
                return;
            boolean isChatSubscriptionRequired = event.mpacifyer.isChatSubscriptionRequired();
            int priceForChat = Integer.parseInt(event.mpacifyer.getPriceForChat());

            if (!isChatSubscriptionRequired) {
                startChat(event.mpacifyer);
                //showIOSDialogOK("","Please purchase unlimited chat per day","OK");

            } else if (priceForChat == 0) {
                startChat(event.mpacifyer);
            } else {
                //chatLlay.setVisibility(View.VISIBLE);

                if (event.mpacifyer.getChatSubscription() == null) {
                    showIOSDialogOK("", "Please purchase unlimited chat per day", "OK");
                    //chat_price_ctxv.setText("Chat for " + Utility.getPriceSign(ProfileScreenActivity.this) + priceForChat + " a day");
                } else {
                    startChat(event.mpacifyer);
                }
            }
            //startChat(event.mpacifyer);

        } catch (Exception e) {
            LogUtils.LOGD("session", "CPA ChatEvent exce " + e.getMessage());
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
        PacifyrActivity.call_emotion=new MEmotion();
        PacifyrActivity.call_emotion.setId("5971c472734d1d6202a81958");
        webSearchPacifyrWOptions(customerEmotion);
    }

    @Override
    public void onBackPressed() {
        //
        DashboardActivity.start(ConnectToPacifyrActivity.this);
       /* Intent intent = new Intent(ConnectToPacifyrActivity.this, DashboardActivity.class);
        startActivity(intent);
        finish();*/
    }

    public CallChatEventListner callchatEventListner = new CallChatEventListner() {
        @Override
        public void onCallEventClick(CallEvent event) {
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
            hideProgress();
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    try {
                        pSearchPacifyrWOptionsResponse = GsonUtils.getInstance().gsonToRespSearchPacifyrWOption(json);
                        //searchResults=modelSearchPacifyr.getResultsList();
                        pSearchPacifyrWOptionsResponse.toString();
                        setAdapterSearchOption();
                        morePacifyrsRlay.setVisibility(View.GONE);
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
            jsonObject.putOpt("includeChargeablePacifyrs", showAllFlag);
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
        } catch (Exception ex) {
            hideProgress();
            ex.printStackTrace();
        }
    }

    public SwipeDeckConnectPacifyrAdapter adapter;

    private void setAdapterSearchOption() {

        if (!NetworkService.isConnectSinglePacifyer) {
            if (isValid(pSearchPacifyrWOptionsResponse) && isValid(pSearchPacifyrWOptionsResponse.getResultsList())) {
                cardStack.setVisibility(View.VISIBLE);
                noPacifyrCtxv.setVisibility(View.GONE);
                adapter = new SwipeDeckConnectPacifyrAdapter(pSearchPacifyrWOptionsResponse.getResultsList(), ConnectToPacifyrActivity.this, activity);
                cardStack.setAdapter(adapter);
            } else {
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

                        RespSearchPacifyrWOption mRespSearchPacifyrWOption = GsonUtils.getInstance().gsonToRespSearchPacifyrWOption(json);

                        if (isValid(mRespSearchPacifyrWOption.getResultsList())) {


                            if (isValid(pSearchPacifyrWOptionsResponse.getResultsList())) {

                                for (int i = 0; i < mRespSearchPacifyrWOption.getResultsList().size(); i++) {
                                    for (int j = 0; j < pSearchPacifyrWOptionsResponse.getResultsList().size(); j++) {
                                        if (mRespSearchPacifyrWOption.getResultsList().get(i).getId().equals(pSearchPacifyrWOptionsResponse.getResultsList().get(j).getId())) {

                                            if (mRespSearchPacifyrWOption.getResultsList().get(i).getOnline() != pSearchPacifyrWOptionsResponse.getResultsList().get(j).getOnline()) {
                                                pSearchPacifyrWOptionsResponse.getResultsList().get(j).setOnline(mRespSearchPacifyrWOption.getResultsList().get(i).getOnline());
                                                adapter.notifyDataSetChanged();
                                                adapter.notifyDataSetInvalidated();
                                                cardStack.setSelection(card_position + 1);


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

            url = GLOBAL_URL + "search/pacifyrs";
            companyID = prefM.getCompanyId();
            jsonObject.putOpt("companyId", companyID);
            jsonObject.putOpt("includeChargeablePacifyrs", showAllFlag);
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
        } catch (Exception ex) {
            hideProgress();
            ex.printStackTrace();
        }
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void showCategories() {
        try {
            String url = GLOBAL_URL + "specialities/byAppSource?appSource=leadersup";
            // String url = GLOBAL_URL + "specialities/getCategories/byEmotion?emotionId="+emotionID;
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    try {
                        hideProgress();
                        ModelSpecialityByCategory model = GsonUtils.getInstance().gsonToModelSpecialityByCategory(json);
                        categoryListAdapter = new CategoryListAdapter(model.getResults(), ConnectToPacifyrActivity.this);
                        imgRcv.setLayoutManager(new LinearLayoutManager(ConnectToPacifyrActivity.this, LinearLayoutManager.HORIZONTAL, false));
                        imgRcv.setAdapter(categoryListAdapter);
                        categoryListAdapter.addListener(ConnectToPacifyrActivity.this);
                        categoryListAdapter.setSelected(0);

                    /* String imgURL = GLOBAL_IMAGE_URL + res[i].getImage();
                    String title = res[i].getTitle();
                    if (isValidhtml(imgURL))
                        Picasso.with(context).load(imgURL).placeholder(R.drawable.placeholder_user).into(emotionDimv);*/


                        //emotionDimv  = (ImageView) activity.findViewById(R.id.emotion_dimv);

                        //modelAllEmployees= GsonUtils.getInstance().gsonToMListEmployees(json);
                        //postEventOnMainThread(new NotifyEvent(true, false));
                        //EventBus.getDefault().post(new EventMessage(getString(R.string.list_all_employees)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            AQuery aq = new AQuery(ConnectToPacifyrActivity.this);
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", token);
            aq.ajax(url, JSONObject.class, cb);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        if (morePacifyrsRlay != null) {
            morePacifyrsRlay.setVisibility(View.GONE);
        }

        trackTimer_check_online = new Timer();

        trackTimerTask_check_online = new TimerTask() {
            @Override
            public void run() {

                if (isValid(listPacifyrRlay)) {
                    if (listPacifyrRlay.getVisibility() == View.VISIBLE) {
                        checkOnLineOrOffline();

                    }
                }

            }
        };
        trackTimer_check_online.scheduleAtFixedRate(trackTimerTask_check_online, 0, 10000);


    }


    public void showScheduleDialogueFragment(MUser pacifyer_obj) {
        if (isValid(pacifyer_obj)) {

            ScheduleBottomSheetFragment bottomSheetFragment = new ScheduleBottomSheetFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("OBJECT", pacifyer_obj);
            bottomSheetFragment.setArguments(bundle);
            bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());

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

            holder.itemView.setOnClickListener(new View.OnClickListener() {
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
                holder.tv_instruction.setOnClickListener(new View.OnClickListener() {
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
                       // button4.setBackgroundResource(R.drawable.circle_button);
                        button2.setClickable(true);

                        button1.setClickable(true);
                        button3.setClickable(true);
                        //button4.setClickable(true);

                        FirstScreenLayout.setVisibility(View.GONE);
                        second_screen_layout.setVisibility(View.GONE);
                        third_Screen_layout.setVisibility(View.VISIBLE);
                        listPacifyrRlay.setVisibility(View.GONE);
                        morePacifyrsRlay.setVisibility(View.GONE);

                        setUpThirdScreen();
                    }
                });

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

    private void setUpThirdScreen() {
        if (isValid(mModelPamTemplates)
                &&isValid(mModelPamTemplates.get(2))&&isValid(mModelPamTemplates.get(2).getOptions()))
        {
            mInstructionAdapter = new ThirdScreenInstructionAdapter(getApplicationContext(),mModelPamTemplates.get(2).getOptions());
            RecyclerView.LayoutManager mLayout = new LinearLayoutManager(getApplicationContext(),
                    RecyclerView.VERTICAL, false);
            rcv_thirdScreen_instruct.setLayoutManager(mLayout);
            rcv_thirdScreen_instruct.setAdapter(mInstructionAdapter);
            rcv_thirdScreen_instruct.setItemAnimator(new DefaultItemAnimator());
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

            if (position < (options.size() - 2)) {
                holder.tv_answer_template.setImeOptions(EditorInfo.IME_ACTION_NEXT);
            } else if (position == options.size() - 1) {
                holder.tv_answer_template.setImeOptions(EditorInfo.IME_ACTION_DONE);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    /*button1.setBackgroundResource(R.drawable.circle_button);
                    button2.setBackgroundResource(R.drawable.circle_button);
                    button3.setBackgroundResource(R.drawable.circle_button);
                   // button4.setBackgroundResource(R.drawable.circle_click_btn);


                    button1.setClickable(true);
                    button2.setClickable(true);
                    button3.setClickable(true);
                    //button4.setClickable(true);

                    FirstScreenLayout.setVisibility(View.GONE);
                    second_screen_layout.setVisibility(View.GONE);
                    third_Screen_layout.setVisibility(View.GONE);
                    listPacifyrRlay.setVisibility(View.GONE);
                    morePacifyrsRlay.setVisibility(View.GONE);*/
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
                    morePacifyrsRlay.setVisibility(View.GONE);
                }
            });

        }


        @Override
        public int getItemCount() {
            return list.size();
        }

        public class MyViewHolderr extends RecyclerView.ViewHolder {
            public MainTextView tv_instruction;


            public MyViewHolderr(View view) {
                super(view);
                tv_instruction=view.findViewById(R.id.tv_instruction);
            }
        }
    }
*/




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
                    //button4.setBackgroundResource(R.drawable.circle_button);


                    button1.setClickable(true);
                    button2.setClickable(true);
                    button3.setClickable(true);
                    //button4.setClickable(true);

                    FirstScreenLayout.setVisibility(View.GONE);
                    second_screen_layout.setVisibility(View.VISIBLE);
                    third_Screen_layout.setVisibility(View.GONE);
                    listPacifyrRlay.setVisibility(View.GONE);
                    morePacifyrsRlay.setVisibility(View.GONE);

                    setUpSecondScreenUI();
                }
            });

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

}