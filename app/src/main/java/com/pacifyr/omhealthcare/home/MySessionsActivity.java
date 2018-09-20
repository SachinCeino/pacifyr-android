package com.pacifyr.omhealthcare.home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ceino.utilitylibrary.CustomFontTextView;
import com.ceino.utilitylibrary.DeviceFitImageView;
import com.ceino.utilitylibrary.model.pacifyr.MSessionDetailRoot;
import com.ceino.utilitylibrary.model.pacifyr.ModelSessonsOfCustomer;
import com.ceino.utilitylibrary.model.pacifyr.ModelStatistics;
import com.ceino.utilitylibrary.utils.PrefManager;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.adapter.MySessionAdapter;
import com.pacifyr.omhealthcare.adapter.SessionWeekCustomerAdapter;
import com.pacifyr.omhealthcare.event.AlertTypeEvent;
import com.pacifyr.omhealthcare.event.NotifyEvent;
import com.pacifyr.omhealthcare.event.WeekSetSessionEvent;
import com.pacifyr.omhealthcare.network.GsonUtils;
import com.pacifyr.omhealthcare.utility.PacifyrAppCompactActivity;
import com.pacifyr.omhealthcare.utils.Utility;
import com.squareup.otto.Subscribe;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.ceino.utilitylibrary.network.UtilityNetworkService.GLOBAL_URL;

public class MySessionsActivity extends PacifyrAppCompactActivity {


    private LinearLayout mySessionsTopLlay;
    private RelativeLayout upperRlay;
    private CustomFontTextView sessionNoCtxv;
    private CustomFontTextView hoursNoCtxv;
    private CustomFontTextView minutesNoCtxv;
    private CustomFontTextView earningsNoCtxv;
    private CustomFontTextView thisWkCtxv;
    private CustomFontTextView thisMntCtxv;
    private CustomFontTextView thisYearCtxv;
    private RecyclerView dateListRcv;
    private CustomFontTextView noDataCtxv;
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
            setLoading();
            showProgress();
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
                    setLoading();
                    showProgress();
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
                    setLoading();
                    showProgress();
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
                    setLoading();
                    showProgress();
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

            mySessionsTopLlay = (LinearLayout) findViewById(R.id.my_sessions_top_llay);
            upperRlay = (RelativeLayout) findViewById(R.id.upper_rlay);
            sessionNoCtxv = (CustomFontTextView) findViewById(R.id.session_no_ctxv);
            hoursNoCtxv = (CustomFontTextView) findViewById(R.id.hours_no_ctxv);
            minutesNoCtxv = (CustomFontTextView) findViewById(R.id.minutes_no_ctxv);
            earningsNoCtxv = (CustomFontTextView) findViewById(R.id.earnings_no_ctxv);
            thisWkCtxv = (CustomFontTextView) findViewById(R.id.this_wk_ctxv);
            thisMntCtxv = (CustomFontTextView) findViewById(R.id.this_mnt_ctxv);
            thisYearCtxv = (CustomFontTextView) findViewById(R.id.this_year_ctxv);
            dateListRcv = (RecyclerView) findViewById(R.id.date_list_rcv);
            noDataCtxv = (CustomFontTextView) findViewById(R.id.no_data_ctxv);
            weekSessionsRcv = (RecyclerView) findViewById(R.id.week_sessions_rcv);
            monthSessionsRcv = (RecyclerView) findViewById(R.id.month_sessions_rcv);
            yearSessionsRcv = (RecyclerView) findViewById(R.id.year_sessions_rcv);
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

            //leftLlay.setVisibility(View.VISIBLE);
            defSetText(titleTxv, getResources().getString(R.string.mysessions));
            Typeface type = Typeface.createFromAsset(getAssets(),"fonts/ITCAvantGardeStd-Bold.otf");
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
            showProgress();
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
