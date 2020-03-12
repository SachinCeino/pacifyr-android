package com.pacifyr.client.home;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MUser;
import com.pacifyrapp.utilitylibrary.model.pacifyr.RespSearchPacifyrWOption;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.daprlabs.cardstack.SwipeDeck;
import com.pacifyr.client.R;
import com.pacifyr.client.adapter.SpecialistAdapter;
import com.pacifyr.client.adapter.SwipeDeckBrowsePacifyerAdapter;
import com.pacifyr.client.event.AlertTypeEvent;
import com.pacifyr.client.event.CallEvent;
import com.pacifyr.client.event.ChatEvent;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.fragment.schedule.ScheduleBottomSheetFragment;
import com.pacifyr.client.model.interfaces.CallChatEventListner;
import com.pacifyr.client.network.GsonUtils;
import com.pacifyr.client.network.NetworkService;
import com.pacifyr.client.quickblox.utils.Consts;
import com.pacifyr.client.utility.EventMessage;
import com.pacifyr.client.utility.PacifyrAppCompactActivity;
import com.pacifyr.client.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.pacifyr.client.network.NetworkService.GLOBAL_URL;
import static com.pacifyr.client.network.NetworkService.modelSpecialist;
import static com.pacifyr.client.network.NetworkService.pSearchPacifyrWOptionsResponse;
import static com.pacifyr.client.quickblox.activities.CallActivity.isCallActive;

public class ListPacifyrScreenActivity extends PacifyrAppCompactActivity implements SpecialistAdapter.EmotionClickkListener {


    SwipeDeck cardStack;
    public SwipeDeckBrowsePacifyerAdapter adapter;

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    public static String customerEmotion;
    PrefManager prefM;
    String companyID;
    String userID;
    String accessToken;

    private LinearLayout showallLlay;
    private MainTextView showallCtxv;

    private CharSequence mTitle;
    private boolean showAllFlag;
    private String customerRole;
    private MainTextView noPacifyrCtxv;
    CallEvent event_call = null;

    private Timer trackTimer_check_online;
    private TimerTask trackTimerTask_check_online;
    private int card_position=0;

    private SearchView pacifyr_SearchView;

    private String searchQuery="";
    ArrayList<MUser>  pacifyrList=new ArrayList<>();

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }




    public CallChatEventListner callchatEventListner = new CallChatEventListner() {
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
    private String emoSelected;
    private SpecialistAdapter specialistAdapter;

    public CheckBox getShowallCbox() {
        return (CheckBox) findViewById(R.id.showall_cbox);
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


    }

    @Override
    public void onAlertTypeEvent(AlertTypeEvent event) {

    }

    public static void start(Context context, String emoSelected,boolean showAllFlag) {
        Intent intent = new Intent(context, ListPacifyrScreenActivity.class);
        intent.putExtra("emo_selected", emoSelected);
        intent.putExtra("showAllFlag",showAllFlag);
        context.startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void messageEventFromService(EventMessage event) {
        try {
            hideProgress();
            String msg = event.getNotification();
            if (msg.equals("Event Pacifyr Search")) {

                setAdapterSearchOption();

            } else if (msg.equals("Event")) {

                setAdapterSpecial();
                if (modelSpecialist != null && modelSpecialist.getResultsList() != null &&
                        modelSpecialist.getResultsList().size() > 0 && emoSelected == null) {
                    customerEmotion = modelSpecialist.getResultsList().get(0).getTitle();
                }
                webSearchPacifyrWOptions(customerEmotion);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setAdapterSpecial() {
        try {
            if (modelSpecialist != null)
                if (modelSpecialist.getResults() != null) {
                    recyclerView = findViewById(R.id.special_recycle);
                    recyclerView.setHasFixedSize(true);
                    layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                    recyclerView.setLayoutManager(layoutManager);
                    specialistAdapter = new SpecialistAdapter(modelSpecialist.getResultsList(), ListPacifyrScreenActivity.this);
                    recyclerView.setAdapter(specialistAdapter);
                    specialistAdapter.addListener(ListPacifyrScreenActivity.this);
                    if (emoSelected != null) {
                        int emoPosition = -1;
                        for (int i = 0; i < modelSpecialist.getResultsList().size(); i++) {
                            if (modelSpecialist.getResultsList().get(i).getTitle() != null
                                    && modelSpecialist.getResultsList().get(i).getTitle().equalsIgnoreCase(emoSelected)) {
                                emoPosition = i;
                                break;
                            }
                        }
                        if (emoPosition > -1 && emoPosition < specialistAdapter.getItemCount()) {
                            specialistAdapter.setSelected(emoPosition);
                            recyclerView.scrollToPosition(emoPosition);
                        } else {
                            specialistAdapter.setSelected(0);
                        }
                    } else {
                        specialistAdapter.setSelected(0);
                    }
                }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pacifyr);
        prefM = PrefManager.getInstance(ListPacifyrScreenActivity.this);
        companyID = prefM.getCompanyId();
        customerRole = prefM.getCustomerRole();
        accessToken = prefM.getAccessToken();
        userID = prefM.getUserID();
        setLoading();
        showProgress();
        NetworkService.startActionSpecialist(this);
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("emo_selected")) {
            emoSelected = getIntent().getExtras().getString("emo_selected");
        }

        if (isValid(getIntent().getExtras().get("showAllFlag"))&&customerRole.equals("employee"))
        {
            showAllFlag=getIntent().getBooleanExtra("showAllFlag",false);
            getShowallCbox().setChecked(showAllFlag);
        }

        onSetActionBar();




        //NetworkService.startActionSearchPacifyr(this);

        showallLlay = findViewById(R.id.showall_llay);
        showallCtxv = findViewById(R.id.showall_ctxv);
        noPacifyrCtxv = findViewById(R.id.no_pacifyr_ctxv);
        pacifyr_SearchView=findViewById(R.id.pacifyr_SearchView);

        recyclerView = findViewById(R.id.special_recycle);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        if(customerRole.equals("employee")||customerRole.equals("unpaidEmployee")){
            showallLlay.setVisibility(View.VISIBLE);
        }

        //Default emotion set as Anger


        //webSearchPacifyrWOptions(customerEmotion);

        getShowallCbox().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (getSearchQuery().equals(""))
                {
                    if(isChecked){
                        showAllFlag = true;
                        webSearchPacifyrWOptions(customerEmotion);
                    }else {
                        showAllFlag = false;
                        webSearchPacifyrWOptions(customerEmotion);
                    }
                }
                else {

                    if(isChecked){
                        showAllFlag = true;
                        if (isValid(getSearchQuery())&&isValid(customerEmotion))
                            webSearchPacifyrFromSearchView(customerEmotion);
                    }else {
                        showAllFlag = false;
                        if (isValid(getSearchQuery())&&isValid(customerEmotion))
                            webSearchPacifyrFromSearchView(customerEmotion);
                    }
                }
            }
        });

        setAdapterSpecial();

//        webSearchPacifyrWOptions(customerEmotion);

        /*final ArrayList<String> testData = new ArrayList<>();
        for(int i=0;i<50;i++)
        {
            testData.add(Integer.toString(i));
        }*/


        cardStack = findViewById(R.id.swipe_deck);


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
                if (getSearchQuery().equals(""))
                    setAdapterSearchOption();
                if (!getSearchQuery().equals(""))
                    setAdapterPacifyrSearchFRomSearchView(pacifyrList);
            }

            @Override
            public void cardActionDown() {

            }

            @Override
            public void cardActionUp() {

            }
        });



        pacifyr_SearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pacifyr_SearchView.setIconified(false);
            }
        });

        pacifyr_SearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                //Log.w("QQQQ","onQueryTextSubmit");
                setSearchQuery(query);
                if (isValid(query)&&isValid(customerEmotion))
                    webSearchPacifyrFromSearchView(customerEmotion);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Log.w("QQQQ","onQueryTextChange");
                setSearchQuery(newText);
                if (getSearchQuery().equals(""))
                {
                    if(getShowallCbox().isChecked()){
                        showAllFlag = true;
                        webSearchPacifyrWOptions(customerEmotion);
                    }else {
                        showAllFlag = false;
                        webSearchPacifyrWOptions(customerEmotion);
                    }
                }
                return false;
            }
        });




    }

    private void setAdapterSearchOption() {
        if (isValid(pSearchPacifyrWOptionsResponse)) {
            ArrayList<MUser> list = new ArrayList<MUser>();
            list.clear();
            list = pSearchPacifyrWOptionsResponse.getResultsList();
            if(isValid(list)) {
                noPacifyrCtxv.setVisibility(View.GONE);
                adapter = new SwipeDeckBrowsePacifyerAdapter(list, ListPacifyrScreenActivity.this, emoSelected != null);
                cardStack.setAdapter(adapter);
                cardStack.setVisibility(View.VISIBLE);
            }else {
                cardStack.setVisibility(View.GONE);
                noPacifyrCtxv.setVisibility(View.VISIBLE);
            }
        }
    }

    @com.squareup.otto.Subscribe
    public void onCallEvent(final CallEvent event) {
        try {
            LogUtils.LOGD("session", "CPA onCallEvent called ");
            if (!isValid(event.mpacifyer, "Please try again"))
                return;
            event_call = event;
            if (event_call.isVideoCall) {
                if (checker.lacksPermissions(Consts.PERMISSIONS)) {
                    event_call = event;

                    startPermissionsActivity(false, ListPacifyrScreenActivity.this);

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

                    startPermissionsActivity(true, ListPacifyrScreenActivity.this);

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

    @com.squareup.otto.Subscribe
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

        } catch (Exception e)
        {
            LogUtils.LOGD("session", "CPA ChatEvent exce " + e.getMessage());
        }

    }



    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }
    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private LinearLayout actionBarTopLlay;
    private RelativeLayout bottomPanel;
    private LinearLayout leftLlay;
    private DeviceFitImageView backImv;
    private MainTextView leftTxv;
    private MainTextView titleTxv;
    private LinearLayout rightLlay;
    private MainTextView rightTxv;
    private DeviceFitImageView rightImv;

    public void onSetActionBar() {
        try {
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
            defSetText(titleTxv, "PACIFYR");
            Typeface type = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.avantica_bold_header));
            titleTxv.setTypeface(type);
            backImv.setImageDrawable(getResources().getDrawable(R.drawable.backarrow));
            //backImv.setBackgroundResource(R.drawable.leftmen);
            leftLlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void OnEmotionClickkListener() {
        if (adapter != null) {
            adapter.clear();
        }
        if (getSearchQuery().equals(""))
            webSearchPacifyrWOptions(customerEmotion);
        else
            webSearchPacifyrFromSearchView(customerEmotion);

    }

    public void webSearchPacifyrWOptions(String emotion) {
        try{


            setLoading("loading...");
            showProgress();
            String url = GLOBAL_URL + getRoleSearchMap().get(prefM.getCustomerRole());

            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    try {
                        pSearchPacifyrWOptionsResponse = GsonUtils.getInstance().gsonToRespSearchPacifyrWOption(json);
                        //searchResults=modelSearchPacifyr.getResultsList();
                        pSearchPacifyrWOptionsResponse.toString();
                        String js = json.toString();
                        hideProgress();
                        setAdapterSearchOption();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            AQuery aq = new AQuery(ListPacifyrScreenActivity.this);
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", accessToken);

            JSONObject jsonObject = new JSONObject();
            jsonObject.putOpt("initiatorId", userID);
            jsonObject.putOpt("companyId", companyID);
            jsonObject.putOpt("includeChargeablePacifyrs", showAllFlag);
            jsonObject.putOpt("country", prefM.getCountry());

            List<String> list = new ArrayList<String>();
            list.add(emotion);
            JSONArray array = new JSONArray();
            for (int i = 0; i < list.size(); i++) {
                array.put(list.get(i));
            }
            jsonObject.putOpt("specialities", array);
            aq.post(url, jsonObject, JSONObject.class, cb);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
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

            trackTimer_check_online = new Timer();
            trackTimerTask_check_online = new TimerTask() {
                @Override
                public void run() {

                    checkOnLineOrOffline();

                }
            };
            trackTimer_check_online.scheduleAtFixedRate(trackTimerTask_check_online, 0, 10000);

        }
        catch (Exception e)
        {
            Log.w("QQQQ",String.valueOf(e));
        }
    }

    private void checkOnLineOrOffline() {

        webCheckOnlineOffline(customerEmotion);
    }

    private void webCheckOnlineOffline(String emotion) {

        try{

            String url = GLOBAL_URL + getRoleSearchMap().get(prefM.getCustomerRole());

            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    try {

                        RespSearchPacifyrWOption mRespSearchPacifyrWOption=GsonUtils.getInstance().gsonToRespSearchPacifyrWOption(json);

                        if (isValid(mRespSearchPacifyrWOption.getResultsList()) &&isValid(adapter))
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
                        e.printStackTrace();
                    }
                }
            };

            AQuery aq = new AQuery(ListPacifyrScreenActivity.this);
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", accessToken);

            JSONObject jsonObject = new JSONObject();
            jsonObject.putOpt("initiatorId", userID);
            jsonObject.putOpt("companyId",companyID);
            jsonObject.putOpt("includeChargeablePacifyrs",showAllFlag);
            jsonObject.putOpt("country",prefM.getCountry());

            List<String> list = new ArrayList<String>();
            list.add(emotion);
            JSONArray array = new JSONArray();
            for (int i = 0; i < list.size(); i++) {
                array.put(list.get(i));
            }
            jsonObject.putOpt("specialities",array);
            aq.post(url, jsonObject, JSONObject.class, cb);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }


    }

    public void showScheduleDialogueFragment(MUser pacifyer_obj) {
        if (isValid(pacifyer_obj))
        {

            ScheduleBottomSheetFragment bottomSheetFragment = new ScheduleBottomSheetFragment();
            Bundle bundle=new Bundle();
            bundle.putSerializable("OBJECT",pacifyer_obj);
            bottomSheetFragment.setArguments(bundle);
            bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
        }

    }




    public void webSearchPacifyrFromSearchView(String emotion) {
        try{

            String url = GLOBAL_URL + getRoleSearchMap().get(prefM.getCustomerRole());

            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    try {
                        hideProgress();
                        RespSearchPacifyrWOption  mSearchResult= GsonUtils.getInstance().gsonToRespSearchPacifyrWOption(json);
                        if (isValid(mSearchResult))
                        {
                            if (isValid(mSearchResult.getResultsList()))
                            {
                                pacifyrList=mSearchResult.getResultsList();
                                if (isValid(pacifyrList))
                                {
                                    setAdapterPacifyrSearchFRomSearchView(pacifyrList);
                                }
                            }
                            else {
                                showNoPacifyrAvailbleAfterSearch();
                            }
                        } else {
                            showNoPacifyrAvailbleAfterSearch();
                        }




                    } catch (Exception e) {
                        e.printStackTrace();
                        showNoPacifyrAvailbleAfterSearch();
                    }
                }
            };

            AQuery aq = new AQuery(ListPacifyrScreenActivity.this);
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", accessToken);

            JSONObject jsonObject = new JSONObject();
            jsonObject.putOpt("initiatorId", userID);
            jsonObject.putOpt("companyId",companyID);
            jsonObject.putOpt("includeChargeablePacifyrs",showAllFlag);
            jsonObject.putOpt("country",prefM.getCountry());
            if (searchQuery != null && !searchQuery.isEmpty()) {
                jsonObject.putOpt("searchQuery", searchQuery);
            }

            List<String> list = new ArrayList<String>();
            list.add(emotion);
            JSONArray array = new JSONArray();
            for (int i = 0; i < list.size(); i++) {
                array.put(list.get(i));
            }
            jsonObject.putOpt("specialities",array);
            aq.post(url, jsonObject, JSONObject.class, cb);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void setAdapterPacifyrSearchFRomSearchView(ArrayList<MUser> pacifyrList) {
        if(isValid(pacifyrList)) {
            noPacifyrCtxv.setVisibility(View.GONE);
            cardStack.setVisibility(View.VISIBLE);
            adapter = new SwipeDeckBrowsePacifyerAdapter(pacifyrList, ListPacifyrScreenActivity.this,emoSelected != null);
            cardStack.setAdapter(adapter);
//            adapter.filter(getSearchQuery());
        }else {
            cardStack.setVisibility(View.GONE);
            noPacifyrCtxv.setVisibility(View.VISIBLE);
        }
    }

    public void showNoPacifyrAvailbleAfterSearch()
    {
        cardStack.setVisibility(View.GONE);
        noPacifyrCtxv.setVisibility(View.VISIBLE);
    }





}
