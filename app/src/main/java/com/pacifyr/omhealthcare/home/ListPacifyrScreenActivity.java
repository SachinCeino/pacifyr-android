package com.pacifyr.omhealthcare.home;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ceino.utilitylibrary.CustomFontTextView;
import com.ceino.utilitylibrary.DeviceFitImageView;
import com.ceino.utilitylibrary.model.pacifyr.MUser;
import com.ceino.utilitylibrary.utils.PrefManager;
import com.daprlabs.cardstack.SwipeDeck;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.adapter.SpecialistAdapter;
import com.pacifyr.omhealthcare.adapter.SwipeDeckBrowsePacifyerAdapter;
import com.pacifyr.omhealthcare.event.AlertTypeEvent;
import com.pacifyr.omhealthcare.event.NotifyEvent;
import com.pacifyr.omhealthcare.network.GsonUtils;
import com.pacifyr.omhealthcare.network.NetworkService;
import com.pacifyr.omhealthcare.utility.EventMessage;
import com.pacifyr.omhealthcare.utility.PacifyrAppCompactActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.pacifyr.omhealthcare.network.NetworkService.GLOBAL_URL;
import static com.pacifyr.omhealthcare.network.NetworkService.modelSpecialist;
import static com.pacifyr.omhealthcare.network.NetworkService.pSearchPacifyrWOptionsResponse;

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
    private CustomFontTextView showallCtxv;

    private CharSequence mTitle;
    private boolean showAllFlag=false;
    private String customerRole;
    private CustomFontTextView noPacifyrCtxv;



    public static void start(Context context) {
        Intent intent = new Intent(context, ListPacifyrScreenActivity.class);
        context.startActivity(intent);
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

        NetworkService.startActionSpecialist(this);


        onSetActionBar();
        SpecialistAdapter.addListener(this);




        //NetworkService.startActionSearchPacifyr(this);

        showallLlay = (LinearLayout) findViewById(R.id.showall_llay);
        showallCtxv = (CustomFontTextView) findViewById(R.id.showall_ctxv);
        noPacifyrCtxv = (CustomFontTextView) findViewById(R.id.no_pacifyr_ctxv);

        recyclerView = (RecyclerView) findViewById(R.id.special_recycle);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        if(customerRole.equals("employee")||customerRole.equals("unpaidEmployee")){
            showallLlay.setVisibility(View.VISIBLE);
        }

        //Default emotion set as Anger
        showAllFlag = false;


        //webSearchPacifyrWOptions(customerEmotion);

        getShowallCbox().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    showAllFlag = true;
                    webSearchPacifyrWOptions(customerEmotion);
                }else {
                    showAllFlag = false;
                    webSearchPacifyrWOptions(customerEmotion);
                }
            }
        });

        setAdapterSpecial();

        webSearchPacifyrWOptions(customerEmotion);

        final ArrayList<String> testData = new ArrayList<>();
        for(int i=0;i<50;i++)
        {
            testData.add(Integer.toString(i));
        }



        cardStack = (SwipeDeck) findViewById(R.id.swipe_deck);


        cardStack.setEventCallback(new SwipeDeck.SwipeEventCallback() {
            @Override
            public void cardSwipedLeft(int position) {
                Log.i("MainActivity", "card was swiped left, position in adapter: " + position);
            }

            @Override
            public void cardSwipedRight(int position) {
                Log.i("MainActivity", "card was swiped right, position in adapter: " + position);
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

    public CheckBox getShowallCbox(){
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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void messageEventFromService(EventMessage event){
        try {

            String msg = event.getNotification();
            if (msg.equals("Event Pacifyr Search")) {

                setAdapterSearchOption();

            } else if (msg.equals("Event")) {

                setAdapterSpecial();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void setAdapterSearchOption() {
        Log.i("eeemmmm12345678","e: "+pSearchPacifyrWOptionsResponse);
        if (isValid(pSearchPacifyrWOptionsResponse)) {
            ArrayList<MUser> list = new ArrayList<MUser>();
            list.clear();
            list = pSearchPacifyrWOptionsResponse.getResultsList();
            Log.i("eeemmmm123456789","e: "+list);
            if(isValid(list)) {
                noPacifyrCtxv.setVisibility(View.GONE);
                adapter = new SwipeDeckBrowsePacifyerAdapter(list, ListPacifyrScreenActivity.this);
                cardStack.setAdapter(adapter);
            }else {
                cardStack.setVisibility(View.GONE);
                noPacifyrCtxv.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setAdapterSpecial() {
        try {
            if(modelSpecialist!=null)
                if(modelSpecialist.getResults()!=null) {
                    recyclerView = (RecyclerView) findViewById(R.id.special_recycle);
                    recyclerView.setHasFixedSize(true);
                    layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                    recyclerView.setLayoutManager(layoutManager);
                    RecyclerView.Adapter adapter = new SpecialistAdapter(modelSpecialist.getResultsList(), ListPacifyrScreenActivity.this);
                    recyclerView.setAdapter(adapter);
                }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
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
    private CustomFontTextView leftTxv;
    private CustomFontTextView titleTxv;
    private LinearLayout rightLlay;
    private CustomFontTextView rightTxv;
    private DeviceFitImageView rightImv;

    public void onSetActionBar() {
        try {
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
            defSetText(titleTxv, "Pacifyr");
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
        showProgress();
        webSearchPacifyrWOptions(customerEmotion);
        cardStack.setVisibility(View.VISIBLE);
        Log.i("eeemmmm12345","e: "+customerEmotion);
    }

    public void webSearchPacifyrWOptions(String emotion) {
        try{

        String url = GLOBAL_URL + getRoleSearchMap().get(prefM.getCustomerRole());
            Log.i("eeemmmm123456","e: "+url);

        AjaxCallback cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                try {
                    pSearchPacifyrWOptionsResponse= GsonUtils.getInstance().gsonToRespSearchPacifyrWOption(json);
                    Log.i("eeemmmm1234567","e: "+pSearchPacifyrWOptionsResponse);
                    //searchResults=modelSearchPacifyr.getResultsList();
                    pSearchPacifyrWOptionsResponse.toString();
                    String js=json.toString();
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



}
