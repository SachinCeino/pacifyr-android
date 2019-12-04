package com.pacifyr.client.home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MSessionDetailRoot;
import com.pacifyrapp.utilitylibrary.model.pacifyr.ModelSessonsOfCustomer;
import com.pacifyrapp.utilitylibrary.model.pacifyr.ModelStatistics;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.pacifyr.client.R;
import com.pacifyr.client.adapter.MySessionAdapter;
import com.pacifyr.client.adapter.SessionWeekCustomerAdapter;
import com.pacifyr.client.event.AlertTypeEvent;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.event.WeekSetSessionEvent;
import com.pacifyr.client.network.GsonUtils;
import com.pacifyr.client.utility.PacifyrAppCompactActivity;
import com.pacifyr.client.utils.Utility;
import com.squareup.otto.Subscribe;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.GLOBAL_URL;

public class MySessionsActivity extends PacifyrAppCompactActivity {


    private LinearLayout mySessionsTopLlay;
    private RelativeLayout upperRlay;
    private MainTextView sessionNoCtxv;
    private MainTextView hoursNoCtxv;
    private MainTextView minutesNoCtxv;
    private MainTextView earningsNoCtxv;
    private MainTextView thisWkCtxv;
    private MainTextView thisMntCtxv;
    private MainTextView thisYearCtxv;
    private RecyclerView dateListRcv;
    private MainTextView noDataCtxv;
    private RecyclerView weekSessionsRcv;
    private RecyclerView monthSessionsRcv;
    private RecyclerView yearSessionsRcv;

    private PrefManager prefM;
    private String accessToken,userid;

    int skip,limit;

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, MySessionsActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sessions);

        try {
            onSetActionBar();
            onSetValues();


            skip=0;
            limit=10;

            LocalDate now = new LocalDate();
            LocalDate startofWeek=now.withDayOfWeek(DateTimeConstants.MONDAY);
            LocalDate startofMonth = now.withDayOfMonth(DateTimeConstants.JANUARY);
            LocalDate startofYear = now.withDayOfYear(DateTimeConstants.JANUARY);

            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
            String fromDateWeek = formatter.print(startofWeek);
            String fromDateMonth = formatter.print(startofMonth);
            String fromDateYear = formatter.print(startofYear);
            String toDate = formatter.print(now);

            final String thisWeekUrl=GLOBAL_URL+"session/customer/all?userId="+userid+"&skip="+skip+"&limit="+limit+"&fromDate="+fromDateWeek+"&toDate="+toDate;
            final String thisMonthUrl=GLOBAL_URL+"session/customer/all?userId="+userid+"&skip="+skip+"&limit="+limit+"&fromDate="+fromDateMonth+"&toDate="+toDate;
            final String thisYearUrl=GLOBAL_URL+"session/customer/all?userId="+userid+"&skip="+skip+"&limit="+limit+"&fromDate="+fromDateYear+"&toDate="+toDate;

            final String weekStatUrl = GLOBAL_URL+"dashboard/customer/"+userid+"/sessionStatistics?fromDate="+fromDateWeek+"&toDate="+toDate;
            final String monthStatUrl = GLOBAL_URL+"dashboard/customer/"+userid+"/sessionStatistics?fromDate="+fromDateMonth+"&toDate="+toDate;
            final String yearStatUrl = GLOBAL_URL+"dashboard/customer/"+userid+"/sessionStatistics?fromDate="+fromDateYear+"&toDate="+toDate;
            getStatistics(weekStatUrl);
            callThisWeekSession(thisWeekUrl);
            SessionWeekCustomerAdapter adapter =new SessionWeekCustomerAdapter(MySessionsActivity.this,"week");
            LinearLayoutManager manager
                    = new LinearLayoutManager(MySessionsActivity.this, LinearLayoutManager.HORIZONTAL, false);
            //manager.setReverseLayout(true);
            dateListRcv.setLayoutManager(manager);
            dateListRcv.setAdapter(adapter);
            dateListRcv.scrollToPosition(dateListRcv.getAdapter().getItemCount() - 1);
            //listAllEmotion();
            thisWkCtxv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getStatistics(weekStatUrl);
                    callThisWeekSession(thisWeekUrl);
                    SessionWeekCustomerAdapter adapter =new SessionWeekCustomerAdapter(MySessionsActivity.this,"week");
                    LinearLayoutManager manager
                            = new LinearLayoutManager(MySessionsActivity.this, LinearLayoutManager.HORIZONTAL, false);
                    //manager.setReverseLayout(true);
                    dateListRcv.setLayoutManager(manager);
                    dateListRcv.setAdapter(adapter);
                    dateListRcv.scrollToPosition(dateListRcv.getAdapter().getItemCount() - 1);
                    thisWkCtxv.setBackground(getResources().getDrawable(R.drawable.blue_line_bottom));
                    thisYearCtxv.setBackground(getResources().getDrawable(R.drawable.grey_bg));
                    thisMntCtxv.setBackground(getResources().getDrawable(R.drawable.grey_bg));
                    thisWkCtxv.setTextColor(getResources().getColor(R.color.blue_pacifyr));
                    thisMntCtxv.setTextColor(getResources().getColor(R.color.grey));
                    thisYearCtxv.setTextColor(getResources().getColor(R.color.grey));
                    weekSessionsRcv.setVisibility(View.VISIBLE);
                    monthSessionsRcv.setVisibility(View.GONE);
                    yearSessionsRcv.setVisibility(View.GONE);
                }
            });
            thisMntCtxv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getStatistics(monthStatUrl);
                    callThisMonthSession(thisMonthUrl);
                    SessionWeekCustomerAdapter adapter =new SessionWeekCustomerAdapter(MySessionsActivity.this,"month");
                    LinearLayoutManager manager
                            = new LinearLayoutManager(MySessionsActivity.this, LinearLayoutManager.HORIZONTAL, false);
                    //manager.setReverseLayout(true);
                    dateListRcv.setLayoutManager(manager);
                    dateListRcv.setAdapter(adapter);
                    dateListRcv.scrollToPosition(dateListRcv.getAdapter().getItemCount() - 1);
                    thisYearCtxv.setBackground(getResources().getDrawable(R.drawable.grey_bg));
                    thisWkCtxv.setBackground(getResources().getDrawable(R.drawable.grey_bg));
                    thisMntCtxv.setBackground(getResources().getDrawable(R.drawable.blue_line_bottom));
                    thisMntCtxv.setTextColor(getResources().getColor(R.color.blue_pacifyr));
                    thisWkCtxv.setTextColor(getResources().getColor(R.color.grey));
                    thisYearCtxv.setTextColor(getResources().getColor(R.color.grey));
                    monthSessionsRcv.setVisibility(View.VISIBLE);
                    weekSessionsRcv.setVisibility(View.GONE);
                    yearSessionsRcv.setVisibility(View.GONE);
                }
            });
            thisYearCtxv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getStatistics(yearStatUrl);
                    callThisYearSession(thisYearUrl);
                    SessionWeekCustomerAdapter adapter =new SessionWeekCustomerAdapter(MySessionsActivity.this,"year");
                    LinearLayoutManager manager
                            = new LinearLayoutManager(MySessionsActivity.this, LinearLayoutManager.HORIZONTAL, false);
                    //manager.setReverseLayout(true);
                    dateListRcv.setLayoutManager(manager);
                    dateListRcv.setAdapter(adapter);
                    dateListRcv.scrollToPosition(dateListRcv.getAdapter().getItemCount() - 1);
                    thisYearCtxv.setBackground(getResources().getDrawable(R.drawable.blue_line_bottom));
                    thisWkCtxv.setBackground(getResources().getDrawable(R.drawable.grey_bg));
                    thisMntCtxv.setBackground(getResources().getDrawable(R.drawable.grey_bg));
                    thisWkCtxv.setTextColor(getResources().getColor(R.color.grey));
                    thisMntCtxv.setTextColor(getResources().getColor(R.color.grey));
                    thisYearCtxv.setTextColor(getResources().getColor(R.color.blue_pacifyr));
                    yearSessionsRcv.setVisibility(View.VISIBLE);
                    weekSessionsRcv.setVisibility(View.GONE);
                    monthSessionsRcv.setVisibility(View.GONE);
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void getStatistics(String url) {
        try
        {
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    try {
                        ModelStatistics model = GsonUtils.getInstance().gsonToModelStatistics(json);
                        sessionNoCtxv.setText(model.getTotalSessions());
                        earningsNoCtxv.setText(Utility.getPriceSign(MySessionsActivity.this) + model.getSpendings());
                        String duration = model.getDuration();
                        String hours = duration.substring(3,5);
                        String mint = duration.substring(6,8);
                        hoursNoCtxv.setText(mint);
                        minutesNoCtxv.setText(hours);
                    }
                    catch(Exception excp){
                        excp.printStackTrace();
                    }
                }
            };
            AQuery aq = new AQuery(MySessionsActivity.this);
            cb.header("Content-Type", "application/json");
            //cb.header("AccessToken",token);
            cb.header("AccessToken",accessToken);
            aq.ajax(url,JSONObject.class, cb);
        }
        catch (Exception e) {
            hideProgress();
            e.printStackTrace();
        }
    }

    private void callThisYearSession(String url) {
        try
        {
            weekSessionsRcv.setVisibility(View.GONE);
            monthSessionsRcv.setVisibility(View.GONE);
            yearSessionsRcv.setVisibility(View.GONE);
            noDataCtxv.setVisibility(View.GONE);
            setLoading();
            showProgress();
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    hideProgress();
                    ModelSessonsOfCustomer model= GsonUtils.getInstance().gsonToModelSessonsOfCustomer(json);
                    ArrayList<MSessionDetailRoot> list=model.getResultsList();

                    if(isValid(list)) {
                        MySessionAdapter adapter = new MySessionAdapter(MySessionsActivity.this, list, userid, accessToken);
                        LinearLayoutManager verticalLayoutmanager
                                = new LinearLayoutManager(MySessionsActivity.this, LinearLayoutManager.VERTICAL, false);
                        yearSessionsRcv.setLayoutManager(verticalLayoutmanager);
                        yearSessionsRcv.setVisibility(View.VISIBLE);
                        yearSessionsRcv.setAdapter(adapter);
                    }else{
                        noDataCtxv.setVisibility(View.VISIBLE);
                        noDataCtxv.setText("There are no sessions this Year.");
                    }
                }
            };
            AQuery aq = new AQuery(MySessionsActivity.this);
            cb.header("Content-Type", "application/json");
            //cb.header("AccessToken",token);
            cb.header("AccessToken",accessToken);
            aq.ajax(url,JSONObject.class, cb);
        }
        catch (Exception e) {
            hideProgress();
            e.printStackTrace();
        }
    }

    private void callThisMonthSession(String url) {
        try {
            weekSessionsRcv.setVisibility(View.GONE);
            monthSessionsRcv.setVisibility(View.GONE);
            yearSessionsRcv.setVisibility(View.GONE);
            noDataCtxv.setVisibility(View.GONE);
            setLoading();
            showProgress();
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    hideProgress();
                    ModelSessonsOfCustomer model= GsonUtils.getInstance().gsonToModelSessonsOfCustomer(json);
                    ArrayList<MSessionDetailRoot> list=model.getResultsList();
                    if(isValid(list)) {
                        MySessionAdapter adapter = new MySessionAdapter(MySessionsActivity.this, list, userid, accessToken);
                        LinearLayoutManager verticalLayoutmanager = new LinearLayoutManager(MySessionsActivity.this, LinearLayoutManager.VERTICAL, false);
                        monthSessionsRcv.setLayoutManager(verticalLayoutmanager);
                        monthSessionsRcv.setVisibility(View.VISIBLE);
                        monthSessionsRcv.setAdapter(adapter);
                    }else {
                        noDataCtxv.setVisibility(View.VISIBLE);
                        noDataCtxv.setText("There are no sessions this Month.");
                    }
                }
            };
            AQuery aq = new AQuery(MySessionsActivity.this);
            cb.header("Content-Type", "application/json");
            //cb.header("AccessToken",token);
            cb.header("AccessToken",accessToken);
            aq.ajax(url,JSONObject.class, cb);
        } catch (Exception e) {
            hideProgress();
            e.printStackTrace();
        }
    }

    private void callThisWeekSession(String url) {
        try {
            weekSessionsRcv.setVisibility(View.GONE);
            monthSessionsRcv.setVisibility(View.GONE);
            yearSessionsRcv.setVisibility(View.GONE);
            noDataCtxv.setVisibility(View.GONE);
            setLoading();
            showProgress();
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    hideProgress();
                    ModelSessonsOfCustomer model= GsonUtils.getInstance().gsonToModelSessonsOfCustomer(json);
                    ArrayList<MSessionDetailRoot> list=model.getResultsList();
                    if(isValid(list)) {
                        MySessionAdapter adapter = new MySessionAdapter(MySessionsActivity.this, list, userid, accessToken);
                        LinearLayoutManager verticalLayoutmanager
                                = new LinearLayoutManager(MySessionsActivity.this, LinearLayoutManager.VERTICAL, false);
                        weekSessionsRcv.setLayoutManager(verticalLayoutmanager);
                        weekSessionsRcv.setVisibility(View.VISIBLE);
                        weekSessionsRcv.setAdapter(adapter);
                    }else {
                        noDataCtxv.setVisibility(View.VISIBLE);
                        noDataCtxv.setText("There are no sessions this Week.");
                    }
                }
            };
            AQuery aq = new AQuery(MySessionsActivity.this);
            cb.header("Content-Type", "application/json");
            //cb.header("AccessToken",token);
            cb.header("AccessToken",accessToken);
            aq.ajax(url,JSONObject.class, cb);
        } catch (Exception e) {
            hideProgress();
            e.printStackTrace();
        }
    }

    @Override
    public void onSetValues(){
        try {
            prefM= PrefManager.getInstance(MySessionsActivity.this);
            accessToken=prefM.getAccessToken();
            userid=prefM.getUserID();

            mySessionsTopLlay = findViewById(R.id.my_sessions_top_llay);
            upperRlay = findViewById(R.id.upper_rlay);
            sessionNoCtxv = findViewById(R.id.session_no_ctxv);
            hoursNoCtxv = findViewById(R.id.hours_no_ctxv);
            minutesNoCtxv = findViewById(R.id.minutes_no_ctxv);
            earningsNoCtxv = findViewById(R.id.earnings_no_ctxv);
            thisWkCtxv = findViewById(R.id.this_wk_ctxv);
            thisMntCtxv = findViewById(R.id.this_mnt_ctxv);
            thisYearCtxv = findViewById(R.id.this_year_ctxv);
            dateListRcv = findViewById(R.id.date_list_rcv);
            noDataCtxv = findViewById(R.id.no_data_ctxv);
            weekSessionsRcv = findViewById(R.id.week_sessions_rcv);
            monthSessionsRcv = findViewById(R.id.month_sessions_rcv);
            yearSessionsRcv = findViewById(R.id.year_sessions_rcv);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onNotifyEvent(NotifyEvent event) {

    }

    @Override
    public void onAlertTypeEvent(AlertTypeEvent event) {

    }

    @Override
    protected View getSnackbarAnchorView() {
        return null;
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

            //leftLlay.setVisibility(View.VISIBLE);
            defSetText(titleTxv, getResources().getString(R.string.mysessions));
            Typeface type = Typeface.createFromAsset(getAssets(),"fonts/ActionBarCommonFont.otf");
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

    @Subscribe
    public void onWeekSetSessionEvent(WeekSetSessionEvent event) {
        try {
            if(event.getWeekType().equals("week")) {
                callThisWeekSession(event.getUrl());
                getStatistics(event.getStatUrl());
            }
            else if(event.getWeekType().equals("month")){
                callThisMonthSession(event.getUrl());
                getStatistics(event.getStatUrl());
            }
            else if(event.getWeekType().equals("year")){
                callThisYearSession(event.getUrl());
                getStatistics(event.getStatUrl());
            }
        }
        catch (Exception e){
            hideProgress();
            e.printStackTrace();
        }

    }
}
