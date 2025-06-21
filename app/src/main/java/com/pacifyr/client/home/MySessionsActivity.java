package com.pacifyr.client.home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.pacifyr.client.R;
import com.pacifyr.client.adapter.MySessionAdapter;
import com.pacifyr.client.adapter.SessionWeekCustomerAdapter;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.event.AlertTypeEvent;
import com.pacifyr.client.event.CallEvent;
import com.pacifyr.client.event.ChatEvent;
import com.pacifyr.client.event.InternetConnectionEvent;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.event.WeekSetSessionEvent;
import com.pacifyr.client.model.interfaces.CallChatEventListner;
import com.pacifyr.client.network.GsonUtils;
import com.pacifyr.client.network.NetworkService;
import com.pacifyr.client.orginals.Auth;
import com.pacifyr.client.quickblox.util.NestedScroll;
import com.pacifyr.client.utility.PacifyrAppCompactActivity;
import com.pacifyr.client.utils.LogUtils;
import com.pacifyr.client.utils.Utility;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MSessionDetailRoot;
import com.pacifyrapp.utilitylibrary.model.pacifyr.ModelSessonsOfCustomer;
import com.pacifyrapp.utilitylibrary.model.pacifyr.ModelStatistics;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.squareup.otto.Subscribe;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
    String WeekStartDate,FirstDateOfMonth,fromDYear;
    String WeekEndDate,lastMonthDate,toDYear;

    private String token;

    ArrayList<MSessionDetailRoot> list=new ArrayList<>();
    ArrayList<MSessionDetailRoot> listTemp=new ArrayList<>();
    int tab=0;//0:Week,1:Month,2:Year
    NestedScrollView nestedscrollView;
    private boolean firstItemEditable = true;

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
            Log.i("sesssurlwa12","w: "+limit);
            Log.i("emoooooooojiiii","l:  ");

            if(isValid(sessionid)) webGetSessionDetail(sessionid);
            else {
                //handlerFinish("Data not updated in last session ");
                // finish();
            }

            prefM= PrefManager.getInstance(getApplicationContext());
            token=prefM.getAccessToken();
            userid=prefM.getUserID();

            nestedscrollView = findViewById(R.id.nestedscrollView);

            mySessionsTopLlay = findViewById(R.id.my_sessions_top_llay);
            upperRlay = findViewById(R.id.upper_rlay);
            sessionNoCtxv = findViewById(R.id.session_no_ctxv);
            hoursNoCtxv = findViewById(R.id.hours_no_ctxv);
            minutesNoCtxv = findViewById(R.id.minutes_no_ctxv);
            earningsNoCtxv = findViewById(R.id.earnings_no_ctxv);
            thisWkCtxv = findViewById(R.id.this_wk_ctxv);
            thisMntCtxv = findViewById(R.id.this_mnt_ctxv);
            thisYearCtxv = findViewById(R.id.this_year_ctxv);
            weekSessionsRcv = findViewById(R.id.my_sessions_rcv);
            monthSessionsRcv = findViewById(R.id.month_sessions_rcv);
            yearSessionsRcv = findViewById(R.id.year_sessions_rcv);
            dateListRcv = findViewById(R.id.date_list_rcv);
            noDataCtxv = findViewById(R.id.no_data_ctxv);

            weekSessionsRcv.setNestedScrollingEnabled(false);
            monthSessionsRcv.setNestedScrollingEnabled(false);
            yearSessionsRcv.setNestedScrollingEnabled(false);
            dateListRcv.setNestedScrollingEnabled(false);




            skip=0;
            limit=10;
            tab=0;
            list.clear();
            listTemp.clear();

            Log.i("sesssurlwa1","w: "+limit);
            LocalDate now1 = new LocalDate();
            LocalDate backNow = now1.minusDays(7);
            LocalDate startofWeek = backNow.withDayOfWeek(DateTimeConstants.SUNDAY);
            LocalDate now = now1.withDayOfWeek(DateTimeConstants.SATURDAY);
            LocalDate startofMonth = now.withDayOfMonth(DateTimeConstants.JANUARY);
            LocalDate startofYear = now.withDayOfYear(DateTimeConstants.JANUARY);

            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
            final String fromDateWeek = formatter.print(startofWeek);
            final String fromDateMonth = formatter.print(startofMonth);
            final String fromDateYear = formatter.print(startofYear);
            fromDYear= String.valueOf(Calendar.getInstance().get(Calendar.YEAR)+"-01-01");

/** getting last day of year**/

            Calendar calyear = Calendar.getInstance();
            calyear.set(Calendar.DAY_OF_YEAR, calyear.getActualMaximum(Calendar.DAY_OF_YEAR));
            Date yend = calyear.getTime();
            SimpleDateFormat dfyear = new SimpleDateFormat("yyyy-MM-dd");
            toDYear = dfyear.format(yend);

            /** get first day and last day of current month**/
            Calendar cal = Calendar.getInstance();
            int res = cal.getActualMaximum(Calendar.DATE);
            cal.set(Calendar.DATE,res);
            Date date = cal.getTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            lastMonthDate = df.format(date);


            int firstdayMonth = cal.getActualMinimum(Calendar.DATE);
            cal.set(Calendar.DATE,firstdayMonth);
            date = cal.getTime();
            FirstDateOfMonth = df.format(date);

            /** getting first day and last  day of week**/
            // get Current Week of the year
            Calendar calendar=Calendar.getInstance();
            calendar.setFirstDayOfWeek(Calendar.SUNDAY);
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            DateFormat dff = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            WeekStartDate = dff.format(calendar.getTime());
            calendar.add(Calendar.DATE, 6);
            WeekEndDate = dff.format(calendar.getTime());

            final String thisWeekUrl=GLOBAL_URL+"session/customer/all?userId="+userid+"&skip="+skip+"&limit="+limit+"&fromDate="+WeekStartDate+"&toDate="+WeekEndDate;
            final String thisMonthUrl=GLOBAL_URL+"session/customer/all?userId="+userid+"&skip="+skip+"&limit="+limit+"&fromDate="+FirstDateOfMonth+"&toDate="+lastMonthDate;
            final String thisYearUrl=GLOBAL_URL+"session/customer/all?userId="+userid+"&skip="+skip+"&limit="+limit+"&fromDate="+fromDYear+"&toDate="+toDYear;

            final String weekStatUrl = GLOBAL_URL+"dashboard/customer/"+userid+"/sessionStatistics?fromDate="+WeekStartDate+"&toDate="+WeekEndDate;
            final String monthStatUrl = GLOBAL_URL+"dashboard/customer/"+userid+"/sessionStatistics?fromDate="+FirstDateOfMonth+"&toDate="+lastMonthDate;
            final String yearStatUrl = GLOBAL_URL+"dashboard/customer/"+userid+"/sessionStatistics?fromDate="+fromDYear+"&toDate="+toDYear;
//        setLoading();

            getStatistics(weekStatUrl);
            callThisWeekSession(thisWeekUrl);
            Log.i("sesssurlw","w: "+thisWeekUrl);
            Log.i("sesssurlws","w: "+weekStatUrl);
            SessionWeekCustomerAdapter adapter =new SessionWeekCustomerAdapter(getApplicationContext(),"week");
            LinearLayoutManager manager
                    = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
            //manager.setReverseLayout(true);
            dateListRcv.setLayoutManager(manager);
            dateListRcv.setAdapter(adapter);
            dateListRcv.scrollToPosition(dateListRcv.getAdapter().getItemCount() - 1);
            //listAllEmotion();


            nestedscrollView.setOnScrollChangeListener(new NestedScroll() {
                @Override
                public void onScroll() {
                    Log.i("sesssuruuuab","u: "+skip);
                    if(tab==2){
                        if (listTemp.size() == 0) {
                            thisYearCtxv.performClick();
                        } else
                            Log.i("sesssuruuuab3", "u: " + skip);
                        final String thisYearUrl = GLOBAL_URL + "session/customer/all?userId=" + userid + "&skip=" + skip + "&limit=" + limit + "&fromDate=" + fromDYear + "&toDate=" + toDYear;
                        final String yearStatUrl = GLOBAL_URL + "dashboard/customer/" + userid + "/sessionStatistics?fromDate=" + fromDYear + "&toDate=" + toDYear;

                        callThisYearSession(thisYearUrl);
                        thisYearCtxv.setBackground(getResources().getDrawable(R.drawable.blue_line_bottom));
                        thisMntCtxv.setBackground(getResources().getDrawable(com.pacifyrapp.utilitylibrary.R.color.grey_hex_f8));
                        thisWkCtxv.setBackground(getResources().getDrawable(com.pacifyrapp.utilitylibrary.R.color.grey_hex_f8));
                        thisWkCtxv.setTextColor(getResources().getColor(R.color.grey));
                        thisMntCtxv.setTextColor(getResources().getColor(R.color.grey));
                        thisYearCtxv.setTextColor(getResources().getColor(R.color.blue_pacifyr));
                        yearSessionsRcv.setVisibility(View.VISIBLE);
                        weekSessionsRcv.setVisibility(View.GONE);
                        monthSessionsRcv.setVisibility(View.GONE);
                    }
                    else if(tab==1) {
                        if (listTemp.size() == 0) {
                            thisMntCtxv.performClick();
                        } else
                            Log.i("sesssuruuuab2", "u: " + skip);
                        final String thisMonthUrl = GLOBAL_URL + "session/customer/all?userId=" + userid + "&skip=" + skip + "&limit=" + limit + "&fromDate=" + FirstDateOfMonth + "&toDate=" + lastMonthDate;
                        final String monthStatUrl = GLOBAL_URL + "dashboard/customer/" + userid + "/sessionStatistics?fromDate=" + FirstDateOfMonth + "&toDate=" + lastMonthDate;

                        callThisMonthSession(thisMonthUrl);
                        thisWkCtxv.setBackground(getResources().getDrawable(com.pacifyrapp.utilitylibrary.R.color.grey_hex_f8));
                        thisYearCtxv.setBackground(getResources().getDrawable(com.pacifyrapp.utilitylibrary.R.color.grey_hex_f8));
                        thisMntCtxv.setBackground(getResources().getDrawable(R.drawable.blue_line_bottom));
                        thisMntCtxv.setTextColor(getResources().getColor(R.color.blue_pacifyr));
                        thisWkCtxv.setTextColor(getResources().getColor(R.color.grey));
                        thisYearCtxv.setTextColor(getResources().getColor(R.color.grey));
                        monthSessionsRcv.setVisibility(View.VISIBLE);
                        weekSessionsRcv.setVisibility(View.GONE);
                        yearSessionsRcv.setVisibility(View.GONE);
                    }
                    else if(tab==0) {
                        if (listTemp.size() == 0) {
                            thisWkCtxv.performClick();
                        } else
                            Log.i("sesssuruuuab1", "u: " + skip);
                        final String thisWeekUrl = GLOBAL_URL + "session/customer/all?userId=" + userid + "&skip=" + skip + "&limit=" + limit + "&fromDate=" + WeekStartDate + "&toDate=" + WeekEndDate;
                        final String weekStatUrl = GLOBAL_URL + "dashboard/customer/" + userid + "/sessionStatistics?fromDate=" + WeekStartDate + "&toDate=" + WeekEndDate;

                        callThisWeekSession(thisWeekUrl);
                        thisWkCtxv.setBackground(getResources().getDrawable(R.drawable.blue_line_bottom));
                        thisMntCtxv.setBackground(getResources().getDrawable(com.pacifyrapp.utilitylibrary.R.color.grey_hex_f8));
                        thisYearCtxv.setBackground(getResources().getDrawable(com.pacifyrapp.utilitylibrary.R.color.grey_hex_f8));
                        thisWkCtxv.setTextColor(getResources().getColor(R.color.blue_pacifyr));
                        thisMntCtxv.setTextColor(getResources().getColor(R.color.grey));
                        thisYearCtxv.setTextColor(getResources().getColor(R.color.grey));
                        weekSessionsRcv.setVisibility(View.VISIBLE);
                        monthSessionsRcv.setVisibility(View.GONE);
                        yearSessionsRcv.setVisibility(View.GONE);
                    }
                }
            });




            thisWkCtxv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    skip=0;
                    limit=10;
                    tab=0;
                    list.clear();
                    listTemp.clear();
                    firstItemEditable = true;
                    getStatistics(weekStatUrl);
                    callThisWeekSession(thisWeekUrl);
                    SessionWeekCustomerAdapter adapter =new SessionWeekCustomerAdapter(getApplicationContext(),"week");
                    LinearLayoutManager manager
                            = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                    //manager.setReverseLayout(true);
                    dateListRcv.setLayoutManager(manager);
                    dateListRcv.setAdapter(adapter);
                    dateListRcv.scrollToPosition(dateListRcv.getAdapter().getItemCount() - 1);
                    thisWkCtxv.setBackground(getResources().getDrawable(R.drawable.blue_line_bottom));
                    thisMntCtxv.setBackground(getResources().getDrawable(R.drawable.grey_bg));
                    thisYearCtxv.setBackground(getResources().getDrawable(R.drawable.grey_bg));
                    thisWkCtxv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.blue_pacifyr));
                    thisMntCtxv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));
                    thisYearCtxv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));
                    weekSessionsRcv.setVisibility(View.VISIBLE);
                    monthSessionsRcv.setVisibility(View.GONE);
                    yearSessionsRcv.setVisibility(View.GONE);
                    Log.i("emoooooooojiii","l:  ");
                }
            });
            thisMntCtxv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    skip=0;
                    limit=10;
                    tab=1;
                    list.clear();
                    listTemp.clear();
                    firstItemEditable = true;
                    getStatistics(monthStatUrl);
                    callThisMonthSession(thisMonthUrl);
                    SessionWeekCustomerAdapter adapter =new SessionWeekCustomerAdapter(getApplicationContext(),"month");
                    LinearLayoutManager manager
                            = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                    //manager.setReverseLayout(true);
                    dateListRcv.setLayoutManager(manager);
                    dateListRcv.setAdapter(adapter);
                    dateListRcv.scrollToPosition(dateListRcv.getAdapter().getItemCount() - 1);
                    thisWkCtxv.setBackground(getResources().getDrawable(R.drawable.grey_bg));
                    thisYearCtxv.setBackground(getResources().getDrawable(R.drawable.grey_bg));
                    thisMntCtxv.setBackground(getResources().getDrawable(R.drawable.blue_line_bottom));
                    thisMntCtxv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.blue_pacifyr));
                    thisWkCtxv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));
                    thisYearCtxv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));
                    monthSessionsRcv.setVisibility(View.VISIBLE);
                    weekSessionsRcv.setVisibility(View.GONE);
                    yearSessionsRcv.setVisibility(View.GONE);
                }
            });
            thisYearCtxv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    skip=0;
                    limit=10;
                    tab=2;
                    list.clear();
                    listTemp.clear();
                    firstItemEditable = true;
                    getStatistics(yearStatUrl);
                    callThisYearSession(thisYearUrl);
                    SessionWeekCustomerAdapter adapter =new SessionWeekCustomerAdapter(getApplicationContext(),"year");
                    LinearLayoutManager manager
                            = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                    //manager.setReverseLayout(true);
                    dateListRcv.setLayoutManager(manager);
                    dateListRcv.setAdapter(adapter);
                    dateListRcv.scrollToPosition(dateListRcv.getAdapter().getItemCount() - 1);
                    thisYearCtxv.setBackground(getResources().getDrawable(R.drawable.blue_line_bottom));
                    thisMntCtxv.setBackground(getResources().getDrawable(R.drawable.grey_bg));
                    thisWkCtxv.setBackground(getResources().getDrawable(R.drawable.grey_bg));
                    thisWkCtxv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));
                    thisMntCtxv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));
                    thisYearCtxv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.blue_pacifyr));
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
                    Auth.httpStatusFilter(getApplicationContext(), status.getCode());
                    try {
                        ModelStatistics model = GsonUtils.getInstance().gsonToModelStatistics(json);
                        sessionNoCtxv.setText(model.getTotalSessions());
                        earningsNoCtxv.setText(Utility.getPriceSign(getApplicationContext()) + model.getSpendings());
                        String duration = model.getDuration();
                        String hours = duration.substring(0,2);
                        String minute = duration.substring(3,5);
                        hoursNoCtxv.setText(hours);
                        minutesNoCtxv.setText(minute);
                    }
                    catch(Exception excp){
                        excp.printStackTrace();
                    }
                }
            };
            AQuery aq = new AQuery(getApplicationContext());
            cb.header("Content-Type", "application/json");
            //cb.header("AccessToken",token);
            cb.header("AccessToken",token);
            aq.ajax(url,JSONObject.class, cb);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callThisYearSession(String url) {
        try
        {
            yearSessionsRcv.setVisibility(View.GONE);
            noDataCtxv.setVisibility(View.GONE);
            setLoading();
            showProgress();
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    hideProgress();
                    Auth.httpStatusFilter(getApplicationContext(), status.getCode());
                    list.clear();
                    skip=skip+10;
                    ModelSessonsOfCustomer model= GsonUtils.getInstance().gsonToModelSessonsOfCustomer(json);
                    list=model.getResultsList();
                    if(isValid(list)) {
                        listTemp.addAll(list);
                        monthSessionsRcv.setVisibility(View.GONE);
                        weekSessionsRcv.setVisibility(View.GONE);
                        yearSessionsRcv.setVisibility(View.VISIBLE);
                        setAdapter(listTemp,yearSessionsRcv);
                        /* MySessionAdapter adapter = new MySessionAdapter(getActivity(), list, userid, token);
                        LinearLayoutManager verticalLayoutmanager
                                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                        yearSessionsRcv.setLayoutManager(verticalLayoutmanager);
                        yearSessionsRcv.setAdapter(adapter);*/
                    } else {
                        if(listTemp.size()==0){
                            noDataCtxv.setVisibility(View.VISIBLE);
                            yearSessionsRcv.setVisibility(View.GONE);
                            weekSessionsRcv.setVisibility(View.GONE);
                            monthSessionsRcv.setVisibility(View.GONE);
                            noDataCtxv.setText("There are no sessions this Year.");}

                    }
                }
            };
            AQuery aq = new AQuery(getApplicationContext());
            cb.header("Content-Type", "application/json");
            //cb.header("AccessToken",token);
            cb.header("AccessToken",token);
            aq.ajax(url,JSONObject.class, cb);
        }
        catch (Exception e) {
            hideProgress();
            e.printStackTrace();
        }
    }

    private void setAdapter(ArrayList<MSessionDetailRoot> list,RecyclerView recyclerView){
        try {
            Log.i("sesssssss11","l:  "+userid+"   "+token);
            MySessionAdapter adapter = new MySessionAdapter(MySessionsActivity.this, list, userid, token);
            adapter.setFirstItemEditable(firstItemEditable);
            LinearLayoutManager verticalLayoutmanager
                    = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(verticalLayoutmanager);
            recyclerView.setAdapter(adapter);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    private void callThisMonthSession(String url) {
        try {
            noDataCtxv.setVisibility(View.GONE);
            monthSessionsRcv.setVisibility(View.GONE);
            setLoading();
            showProgress();
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    hideProgress();
                    Auth.httpStatusFilter(getApplicationContext(), status.getCode());
                    list.clear();
                    skip=skip+10;
                    ModelSessonsOfCustomer model= GsonUtils.getInstance().gsonToModelSessonsOfCustomer(json);
                    list=model.getResultsList();
                    if(isValid(list)) {
                        listTemp.addAll(list);
                        yearSessionsRcv.setVisibility(View.GONE);
                        weekSessionsRcv.setVisibility(View.GONE);
                        monthSessionsRcv.setVisibility(View.VISIBLE);
                        setAdapter(listTemp,monthSessionsRcv);
                    /*MySessionAdapter adapter = new MySessionAdapter(getActivity(), list, userid, token);
                    LinearLayoutManager verticalLayoutmanager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                    monthSessionsRcv.setLayoutManager(verticalLayoutmanager);
                    monthSessionsRcv.setAdapter(adapter);*/
                    } else {
                        if(listTemp.size()==0){
                            noDataCtxv.setVisibility(View.VISIBLE);
                            yearSessionsRcv.setVisibility(View.GONE);
                            weekSessionsRcv.setVisibility(View.GONE);
                            monthSessionsRcv.setVisibility(View.GONE);
                            noDataCtxv.setText("There are no sessions this Month.");}
                    }
                }};
            AQuery aq = new AQuery(getApplicationContext());
            cb.header("Content-Type", "application/json");
            //cb.header("AccessToken",token);
            cb.header("AccessToken",token);
            aq.ajax(url,JSONObject.class, cb);
        }
        catch (Exception e) {
            hideProgress();
            e.printStackTrace();
        }
    }

    private void callThisWeekSession(String url) {
        try {
            Log.i("weeeeek1","w:  "+url);
            weekSessionsRcv.setVisibility(View.GONE);
            noDataCtxv.setVisibility(View.GONE);
            hideProgress();
            setLoading();
            showProgress();
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    hideProgress();
                    Auth.httpStatusFilter(getApplicationContext(), status.getCode());
                    Log.i("weeeeek12", "w:  " + json);
                    list.clear();
                    skip=skip+10;
                    ModelSessonsOfCustomer model= GsonUtils.getInstance().gsonToModelSessonsOfCustomer(json);
                    list=model.getResultsList();
                    Log.i("weeeeek123","w:  "+list);
                    if(isValid(list)) {
                        Log.i("weeeeek124","w:  "+list);
                        listTemp.addAll(list);
                        monthSessionsRcv.setVisibility(View.GONE);
                        yearSessionsRcv.setVisibility(View.GONE);
                        weekSessionsRcv.setVisibility(View.VISIBLE);
                        setAdapter(listTemp,weekSessionsRcv);
                        /*MySessionAdapter adapter = new MySessionAdapter(getActivity(), list, userid, token);
                        LinearLayoutManager verticalLayoutmanager
                                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                        weekSessionsRcv.setLayoutManager(verticalLayoutmanager);
                        weekSessionsRcv.setAdapter(adapter);*/
                    } else {
                        if(listTemp.size()==0){
                            Log.i("weeeeek125","w:  "+list);
                            noDataCtxv.setVisibility(View.VISIBLE);
                            yearSessionsRcv.setVisibility(View.GONE);
                            weekSessionsRcv.setVisibility(View.GONE);
                            monthSessionsRcv.setVisibility(View.GONE);
                            noDataCtxv.setText("There are no sessions this Week.");}
                    }
                }
            };
            AQuery aq = new AQuery(getApplicationContext());
            cb.header("Content-Type", "application/json");
            //cb.header("AccessToken",token);
            //
            cb.header("AccessToken",token);
            aq.ajax(url,JSONObject.class, cb);

        } catch (Exception e) {
            hideProgress();
            e.printStackTrace();
        }
    }

    @Override
    public void onSetValues(){
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
        firstItemEditable = event.getEditable();
        try {

            if(event.getWeekType().equals("week")) {
                WeekStartDate=event.getFromDWeek();
                WeekEndDate=event.getToDWeek();
                listTemp.clear();
                list.clear();
                callThisWeekSession(event.getUrl());
                getStatistics(event.getStatUrl());

            }
            else if(event.getWeekType().equals("month")){
                FirstDateOfMonth=event.getFromDWeek();
                lastMonthDate=event.getToDWeek();
                listTemp.clear();
                list.clear();
                callThisMonthSession(event.getUrl());
                getStatistics(event.getStatUrl());

            }
            else if(event.getWeekType().equals("year")){
                fromDYear=event.getFromDWeek();
                toDYear=event.getToDWeek();
                listTemp.clear();
                list.clear();
                callThisYearSession(event.getUrl());
                getStatistics(event.getStatUrl());
            }
        }
        catch (Exception e){
            hideProgress();
            e.printStackTrace();
        }

    }


    public void webGetSessionDetail(final String sessionid) {
        try {
          /*  setLoading("Loading sessions detail");
            showProgress();*/
            if(!isValid(sessionid,"Network Interupted")) {
                // finish();
                return;
            }
            AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
//                    hideProgress();
                    Auth.httpStatusFilter(getApplicationContext(), status.getCode());
                    LogUtils.LOGD("session", " SSA webGetSessionDetail callback result" + json + " " + status.getMessage());
                    if(!isValid(json,"Network Interupted"))
                    {
                        //finish();
                    }
                    else {
                        try {

                        } catch (Exception e){
                            //finish();
                            LogUtils.LOGD("session", " SSA webGetSessionDetail callback exce" + e.getMessage());
                        }
                    }
                }
            };
            AQuery aq = new AQuery(getApplicationContext());
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", getAccessToken());
            String url = "session/"+sessionid;
            aq.ajax(NetworkService.GLOBAL_URL + url, JSONObject.class, cb);
        } catch (Exception e){
//            hideProgress();
            LogUtils.LOGD("session","SSA webGetSessionDetail exce " + e.getMessage());
            //finish();
        }
    }




    @Subscribe
    public void onChatEvent(ChatEvent event) {
        try {
            LogUtils.LOGD("session", "CPA onCallEvent called ");
            if (!isValid(event.mpacifyer, "Network Error"))
                return;
            startChat(event.mpacifyer);


        } catch (Exception e) {
            LogUtils.LOGD("session", "CPA ChatEvent exce " + e.getMessage());
        }

    }

    public CallChatEventListner callchatEventListner = new CallChatEventListner() {
        @Override
        public void onCallEventClick(CallEvent event) {

            //onCallEvent(event);
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
