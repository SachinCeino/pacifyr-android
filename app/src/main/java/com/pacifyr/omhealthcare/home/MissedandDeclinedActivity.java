package com.pacifyr.omhealthcare.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ceino.utilitylibrary.CustomFontTextView;
import com.ceino.utilitylibrary.DeviceFitImageView;
import com.ceino.utilitylibrary.model.pacifyr.MSessionDetailRoot;
import com.ceino.utilitylibrary.model.pacifyr.ModelSessonsOfCustomer;
import com.ceino.utilitylibrary.utils.PrefManager;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.adapter.MissedCallsAdapter;
import com.pacifyr.omhealthcare.event.AlertTypeEvent;
import com.pacifyr.omhealthcare.event.NotifyEvent;
import com.pacifyr.omhealthcare.network.GsonUtils;
import com.pacifyr.omhealthcare.utility.PacifyrAppCompactActivity;

import org.json.JSONObject;

import java.util.ArrayList;

import static com.ceino.utilitylibrary.network.UtilityNetworkService.GLOBAL_URL;

public class MissedandDeclinedActivity extends PacifyrAppCompactActivity {

    private RelativeLayout buttonRlay;
    private CustomFontTextView missedCtxv;
    private CustomFontTextView declinedCtxv;
    private RecyclerView missedRcv;
    private RecyclerView declinedRcv;
    private LinearLayoutManager layoutManager;

    private Context context;
    private ModelSessonsOfCustomer missedSession;
    private ArrayList<MSessionDetailRoot> missedList;
    private ArrayList<MSessionDetailRoot> declinedList;

    String typeMissed;
    String typeDeclined;


    public static void start(Context context) {
        Intent intent = new Intent(context, MissedandDeclinedActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missedand_declined);
        onSetActionBar();
        context = MissedandDeclinedActivity.this;
        buttonRlay = findViewById(R.id.button_rlay);

        missedCtxv = findViewById(R.id.missed_ctxv);
        declinedCtxv = findViewById(R.id.declined_ctxv);
        missedRcv = findViewById(R.id.missed_rcv);
        declinedRcv = findViewById(R.id.declined_rcv);
        typeMissed = "missed_unread";
        typeDeclined = "declined_unread";
        declinedRcv.setVisibility(View.GONE);
        getSession(typeMissed);
        declinedCtxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                missedRcv.setVisibility(View.GONE);
                missedCtxv.setTextColor(ContextCompat.getColor(context, R.color.blue_miss_decline));
                missedCtxv.setBackground(getResources().getDrawable(R.drawable.white_background_right_square));
                declinedCtxv.setTextColor(ContextCompat.getColor(context, R.color.white));
                declinedCtxv.setBackground(getResources().getDrawable(R.drawable.blue_background_left_square));
                getSession(typeDeclined);
            }
        });

        missedCtxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                declinedRcv.setVisibility(View.GONE);
                missedCtxv.setTextColor(ContextCompat.getColor(context, R.color.white));
                missedCtxv.setBackground(getResources().getDrawable(R.drawable.blue_background_right_square));
                declinedCtxv.setTextColor(ContextCompat.getColor(context, R.color.blue_miss_decline));
                declinedCtxv.setBackground(getResources().getDrawable(R.drawable.white_background_left_square));
                getSession(typeMissed);
            }
        });


    }

    private void setMissedCallsAdapter() {
        try {
            if(isValid(missedList)) {
                    missedRcv.setVisibility(View.VISIBLE);
                    declinedRcv.setVisibility(View.GONE);
                    missedRcv.setHasFixedSize(true);
                    layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                    missedRcv.setLayoutManager(layoutManager);
                    RecyclerView.Adapter adapter = new MissedCallsAdapter(missedList, context);
                    missedRcv.setAdapter(adapter);
            }else{

            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void setDeclinedCallAdapter() {
        try {
            if(isValid(declinedList)) {
                missedRcv.setVisibility(View.GONE);
                declinedRcv.setVisibility(View.VISIBLE);
                declinedRcv.setHasFixedSize(true);
                layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                declinedRcv.setLayoutManager(layoutManager);
                RecyclerView.Adapter adapter = new MissedCallsAdapter(declinedList, context);
                declinedRcv.setAdapter(adapter);
            }else{

            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }





    private void getSession(final String type){
        showProgress();
        PrefManager prefM= PrefManager.getInstance(context);
        String token=prefM.getAccessToken();
        String userid=prefM.getUserID();
        int skip =0;
        int limit = 0;
        //declined_unread
        //missed_unread
        String url=GLOBAL_URL+"session/pacifyr/all?userId="+userid+"&skip=0&limit=10&type="+type;
        AjaxCallback cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                try {
                    hideProgress();
                    missedSession = GsonUtils.getInstance().gsonToModelSessonsOfUser(json);

                    if(type.equals(typeDeclined)){
                        declinedList=missedSession.getResultsList();
                        setDeclinedCallAdapter();
                    }else{
                        missedList=missedSession.getResultsList();
                        setMissedCallsAdapter();
                    }

                    //EventBus.getDefault().post(new EventMessage(getString(R.string.event_customer_dashboard)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        try {
            AQuery aq = new AQuery(context);
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", token);
            //aq.post(url, jsonObject, JSONObject.class, cb);
            aq.ajax(url,JSONObject.class, cb);
        }
        catch (Exception ex)
        {
            hideProgress();
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
    public void onNotifyEvent(NotifyEvent event) {

    }

    @Override
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
            rightImv.setVisibility(View.VISIBLE);
            rightTxv.setVisibility(View.INVISIBLE);
            rightLlay.setVisibility(View.VISIBLE);
            leftTxv.setVisibility(View.INVISIBLE);
            backImv.setVisibility(View.VISIBLE);
            titleTxv.setVisibility(View.VISIBLE);
            leftLlay.setVisibility(View.VISIBLE);
            defSetText(titleTxv, "");
            titleTxv.setTextColor(getResources().getColor(R.color.white));
            backImv.setVisibility(View.VISIBLE);
            leftTxv.setVisibility(View.GONE);
            backImv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            leftLlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }



    /*private void getMissedSession(){

        PrefManager prefM= PrefManager.getInstance(context);
        String token=prefM.getAccessToken();
        String userid=prefM.getUserID();
        int skip =0;
        int limit = 0;
        String url=GLOBAL_URL+"session/pacifyr/all?userId="+userid+"&skip=0&limit=10&type=missed_unread";
        AjaxCallback cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                try {
                    missedSession = GsonUtils.getInstance().gsonToModelSessonsOfUser(json);
                    missedList=missedSession.getResultsList();
                    String js=status.getMessage();
                    String jk=status.getError();
                    setMissedCallsAdapter();
                    //EventBus.getDefault().post(new EventMessage(getString(R.string.event_customer_dashboard)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        try {
            AQuery aq = new AQuery(context);
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", token);
            //aq.post(url, jsonObject, JSONObject.class, cb);
            aq.ajax(url,JSONObject.class, cb);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }*/



}
