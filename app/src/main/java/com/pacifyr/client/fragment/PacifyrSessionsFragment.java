package com.pacifyr.client.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MSessionDetailRoot;
import com.pacifyrapp.utilitylibrary.model.pacifyr.ModelSessonsOfCustomer;
import com.pacifyrapp.utilitylibrary.model.pacifyr.ModelStatistics;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.pacifyr.client.R;
import com.pacifyr.client.adapter.PacifyrSessionAdapter;
import com.pacifyr.client.adapter.SessionWeekAdapter;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.event.WeekSetSessionEvent;
import com.pacifyr.client.network.GsonUtils;
import com.pacifyr.client.quickblox.util.NestedScroll;
import com.pacifyr.client.utility.BaseFragment;
import com.pacifyr.client.utils.Utility;
import com.squareup.otto.Subscribe;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.GLOBAL_URL;


public class PacifyrSessionsFragment extends BaseFragment {

    private PrefManager prefM;
    private String token,userid;
    private  int skip,limit;

    private LinearLayout tabRlay;
    private MainTextView thisWkCtxv;
    private MainTextView thisMntCtxv;
    private MainTextView thisYearCtxv;
    private LinearLayout mySessionsTopLlay;
    private RelativeLayout upperRlay;
    private MainTextView sessionNoCtxv;
    private MainTextView hoursNoCtxv;
    private MainTextView minutesNoCtxv;
    private MainTextView earningsNoCtxv;
    private RelativeLayout missedRlay;
    private MainTextView missedCallCtxv;
    private MainTextView declinedCallCtxv;
    private MainTextView noDataCtxv;
    private RecyclerView mySessionsRcv;
    private RecyclerView monthSessionsRcv;
    private RecyclerView yearSessionsRcv;
    private RecyclerView unreadSessionsRcv;
    private MainTextView missedCtxv;
    private MainTextView declinedCtxv;
    private RecyclerView dateTypeRcv;
    int time=0;//0;week,1:Month,2:Year



    ArrayList<MSessionDetailRoot> list=new ArrayList<>();
    ArrayList<MSessionDetailRoot> listTemp=new ArrayList<>();
    int tab=0;//0:Week,1:Month,2:Year
    NestedScrollView nestedscrollView;
    LinearLayout  linearUnreadMissed,linearUnreadDeclined;
    String fromDWeek,fromDMonth,fromDYear;
    String toDWeek,toDMonth,toDYear;
    int posiWeekAda=0;


    public PacifyrSessionsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pacifyr_sessions, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nestedscrollView = view.findViewById(R.id.nestedscrollView);
        linearUnreadMissed = view.findViewById(R.id.linear_unread_missed);
        linearUnreadDeclined = view.findViewById(R.id.linear_unread_declined);
        tabRlay = view.findViewById(R.id.tab_rlay);
        thisWkCtxv = view.findViewById(R.id.this_wk_ctxv);
        thisMntCtxv = view.findViewById(R.id.this_mnt_ctxv);
        thisYearCtxv = view.findViewById(R.id.this_year_ctxv);
        mySessionsTopLlay = view.findViewById(R.id.my_sessions_top_llay);
        upperRlay = view.findViewById(R.id.upper_rlay);
        sessionNoCtxv = view.findViewById(R.id.session_no_ctxv);
        hoursNoCtxv = view.findViewById(R.id.hours_no_ctxv);
        minutesNoCtxv = view.findViewById(R.id.minutes_no_ctxv);
        earningsNoCtxv = view.findViewById(R.id.earnings_no_ctxv);
        missedRlay = view.findViewById(R.id.missed_rlay);
        missedCallCtxv = view.findViewById(R.id.missed_call_ctxv);
        declinedCallCtxv = view.findViewById(R.id.declined_call_ctxv);
        noDataCtxv = view.findViewById(R.id.no_data_ctxv);
        mySessionsRcv = view.findViewById(R.id.my_sessions_rcv);
        monthSessionsRcv = view.findViewById(R.id.month_sessions_rcv);
        yearSessionsRcv = view.findViewById(R.id.year_sessions_rcv);
        unreadSessionsRcv = view.findViewById(R.id.unread_sessions_rcv);
        missedCtxv = view.findViewById(R.id.missed_call_ctxv);
        declinedCtxv = view.findViewById(R.id.declined_call_ctxv);
        dateTypeRcv = view.findViewById(R.id.date_type_rcv);

        prefM= PrefManager.getInstance(getActivity());
        token=prefM.getAccessToken();
        userid=prefM.getUserID();

        mySessionsRcv.setNestedScrollingEnabled(false);
        monthSessionsRcv.setNestedScrollingEnabled(false);
        yearSessionsRcv.setNestedScrollingEnabled(false);
        unreadSessionsRcv.setNestedScrollingEnabled(false);
        


        skip=0;
        limit=10;
        tab=0;
        list.clear();
        listTemp.clear();
        time=0;

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
        final String toDate = formatter.print(now);

        fromDWeek=fromDateWeek;
        toDWeek=toDate;
        toDMonth=toDate;
        toDYear=toDate;
        fromDMonth=fromDateMonth;
        fromDYear=fromDateYear;



        final String thisWeekUrl=GLOBAL_URL+"session/pacifyr/all?userId="+userid+"&skip="+skip+"&limit="+limit+"&fromDate="+fromDateWeek+"&toDate="+toDate;
        final String thisMonthUrl=GLOBAL_URL+"session/pacifyr/all?userId="+userid+"&skip="+skip+"&limit="+limit+"&fromDate="+fromDateMonth+"&toDate="+toDate;
        final String thisYearUrl=GLOBAL_URL+"session/pacifyr/all?userId="+userid+"&skip="+skip+"&limit="+limit+"&fromDate="+fromDateYear+"&toDate="+toDate;

        final String weekStatUrl = GLOBAL_URL+"dashboard/pacifyr/"+userid+"/sessionStatistics?fromDate="+fromDateWeek+"&toDate="+toDate;
        final String monthStatUrl = GLOBAL_URL+"dashboard/pacifyr/"+userid+"/sessionStatistics?fromDate="+fromDateMonth+"&toDate="+toDate;
        final String yearStatUrl = GLOBAL_URL+"dashboard/pacifyr/"+userid+"/sessionStatistics?fromDate="+fromDateYear+"&toDate="+toDate;
        setLoading();
        showProgress();
        getStatistics(weekStatUrl);
        callThisWeekSession(thisWeekUrl);
        Log.i("sesssuw141","d: "+thisWeekUrl);
        Log.i("sesssurlpcw","w: "+thisWeekUrl);
        Log.i("sesssurlpcws","w: "+weekStatUrl);

        SessionWeekAdapter adapter =new SessionWeekAdapter(getActivity(),"week");
        LinearLayoutManager manager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        //manager.setReverseLayout(true);
        dateTypeRcv.setLayoutManager(manager);
        dateTypeRcv.setAdapter(adapter);
        dateTypeRcv.scrollToPosition(dateTypeRcv.getAdapter().getItemCount() - 1);



        LinearLayoutManager mLayoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
//        mySessionsRcv.setOnScrollListener(new EndlessRecyclerOnScrollListener((LinearLayoutManager) mLayoutManager) {
//            @Override
//            public void onLoadMore(int cu, int current_listitem) {
//
//            }
//        });

        nestedscrollView.setOnScrollChangeListener(new NestedScroll() {
            @Override
            public void onScroll() {
                Log.i("sesssuruuuab","u: "+skip+"  "+tab);
                if(tab==2){
                    if(listTemp.size()>0){
                        Log.i("sesssuruuuab3", "u: " + skip);
                        final String thisYearUrl = GLOBAL_URL + "session/pacifyr/all?userId=" + userid + "&skip=" + skip + "&limit=" + limit + "&fromDate=" + fromDYear + "&toDate=" + toDYear;
                        final String yearStatUrl = GLOBAL_URL + "dashboard/pacifyr/" + userid + "/sessionStatistics?fromDate=" + fromDYear + "&toDate=" + toDYear;
                        setLoading();
                        showProgress();
                        getStatistics(yearStatUrl);
                        callThisYearSession(thisYearUrl);
                        dateTypeRcv.scrollToPosition(dateTypeRcv.getAdapter().getItemCount() - 1);
                        thisYearCtxv.setBackground(getResources().getDrawable(R.drawable.blue_line_bottom));
                        thisMntCtxv.setBackground(getResources().getDrawable(com.pacifyrapp.utilitylibrary.R.color.grey_hex_f8));
                        thisWkCtxv.setBackground(getResources().getDrawable(com.pacifyrapp.utilitylibrary.R.color.grey_hex_f8));
                        thisWkCtxv.setTextColor(getResources().getColor(com.pacifyrapp.utilitylibrary.R.color.grey_hex_c7));
                        thisMntCtxv.setTextColor(getResources().getColor(com.pacifyrapp.utilitylibrary.R.color.grey_hex_c7));
                        thisYearCtxv.setTextColor(getResources().getColor(R.color.blue_pacifyr));
                        yearSessionsRcv.setVisibility(View.VISIBLE);
                        mySessionsRcv.setVisibility(View.GONE);
                        monthSessionsRcv.setVisibility(View.GONE);
                    }
                }
                else if(tab==1){
                    if(listTemp.size()>0) {
                        Log.i("sesssuruuuab2", "u: " + skip);
                        final String thisMonthUrl = GLOBAL_URL + "session/pacifyr/all?userId=" + userid + "&skip=" + skip + "&limit=" + limit + "&fromDate=" + fromDMonth + "&toDate=" + toDMonth;
                        final String monthStatUrl = GLOBAL_URL + "dashboard/pacifyr/" + userid + "/sessionStatistics?fromDate=" + fromDMonth + "&toDate=" + toDMonth;
                        setLoading();
                        showProgress();
                        getStatistics(monthStatUrl);
                        callThisMonthSession(thisMonthUrl);
                        dateTypeRcv.scrollToPosition(dateTypeRcv.getAdapter().getItemCount() - 1);
                        thisWkCtxv.setBackground(getResources().getDrawable(com.pacifyrapp.utilitylibrary.R.color.grey_hex_f8));
                        thisYearCtxv.setBackground(getResources().getDrawable(com.pacifyrapp.utilitylibrary.R.color.grey_hex_f8));
                        thisMntCtxv.setBackground(getResources().getDrawable(R.drawable.blue_line_bottom));
                        thisMntCtxv.setTextColor(getResources().getColor(R.color.blue_pacifyr));
                        thisWkCtxv.setTextColor(getResources().getColor(com.pacifyrapp.utilitylibrary.R.color.grey_hex_c7));
                        thisYearCtxv.setTextColor(getResources().getColor(com.pacifyrapp.utilitylibrary.R.color.grey_hex_c7));
                        monthSessionsRcv.setVisibility(View.VISIBLE);
                        mySessionsRcv.setVisibility(View.GONE);
                        yearSessionsRcv.setVisibility(View.GONE);
                    }
                }
                else if(tab==0){
                    if(listTemp.size()>0){
                        Log.i("sesssuruuuab1", "u: " + skip);
                        final String thisWeekUrl = GLOBAL_URL + "session/pacifyr/all?userId=" + userid + "&skip=" + skip + "&limit=" + limit + "&fromDate=" + fromDWeek + "&toDate=" + toDWeek;
                        final String weekStatUrl = GLOBAL_URL + "dashboard/pacifyr/" + userid + "/sessionStatistics?fromDate=" + fromDWeek + "&toDate=" + toDWeek;
                        Log.i("sesssuruuuab1a", "u: " + thisWeekUrl);
                        setLoading();
                        showProgress();
                        getStatistics(weekStatUrl);
                        callThisWeekSession(thisWeekUrl);
                        dateTypeRcv.scrollToPosition(dateTypeRcv.getAdapter().getItemCount() - 1);
                        thisWkCtxv.setBackground(getResources().getDrawable(R.drawable.blue_line_bottom));
                        thisMntCtxv.setBackground(getResources().getDrawable(com.pacifyrapp.utilitylibrary.R.color.grey_hex_f8));
                        thisYearCtxv.setBackground(getResources().getDrawable(com.pacifyrapp.utilitylibrary.R.color.grey_hex_f8));
                        thisWkCtxv.setTextColor(getResources().getColor(R.color.blue_pacifyr));
                        thisMntCtxv.setTextColor(getResources().getColor(com.pacifyrapp.utilitylibrary.R.color.grey_hex_c7));
                        thisYearCtxv.setTextColor(getResources().getColor(com.pacifyrapp.utilitylibrary.R.color.grey_hex_c7));
                        mySessionsRcv.setVisibility(View.VISIBLE);
                        monthSessionsRcv.setVisibility(View.GONE);
                        yearSessionsRcv.setVisibility(View.GONE);
                    }
                }
                else if(tab==3){
                    if(listTemp.size()==0){
                        linearUnreadMissed.performClick();
                    }else{
                        final String unreadMissedUrl;
                        if(time==2){
                            unreadMissedUrl=GLOBAL_URL+"session/pacifyr/all?userId="+userid+"&skip="+skip+"&limit="+limit+"&fromDate="+fromDYear+"&toDate="+toDYear+"&type=missed";}
                        else if(time==1){
                            unreadMissedUrl=GLOBAL_URL+"session/pacifyr/all?userId="+userid+"&skip="+skip+"&limit="+limit+"&fromDate="+fromDMonth+"&toDate="+toDMonth+"&type=missed";}
                        else {
                            unreadMissedUrl=GLOBAL_URL+"session/pacifyr/all?userId="+userid+"&skip="+skip+"&limit="+limit+"&fromDate="+fromDWeek+"&toDate="+toDWeek+"&type=missed";}

                        setLoading();
                    showProgress();
                    Log.i("unread1","m: "+unreadMissedUrl);
                    callUnreadMissedSession(unreadMissedUrl);
                    unreadSessionsRcv.setVisibility(View.VISIBLE);
                    mySessionsRcv.setVisibility(View.GONE);
                    monthSessionsRcv.setVisibility(View.GONE);
                    yearSessionsRcv.setVisibility(View.GONE);}
                }
                else if(tab==4){
                    if(listTemp.size()==0){
                        linearUnreadDeclined.performClick();
                    }else{
                        setLoading();
                        showProgress();
                        final String unreadDeclinedUrl;
                        if(time==2){
                            unreadDeclinedUrl=GLOBAL_URL+"session/pacifyr/all?userId="+userid+"&skip="+skip+"&limit="+limit+"&fromDate="+fromDYear+"&toDate="+toDYear+"&type=declined";}
                        else if(time==1){
                            unreadDeclinedUrl=GLOBAL_URL+"session/pacifyr/all?userId="+userid+"&skip="+skip+"&limit="+limit+"&fromDate="+fromDMonth+"&toDate="+toDMonth+"&type=declined";}
                        else {
                            unreadDeclinedUrl=GLOBAL_URL+"session/pacifyr/all?userId="+userid+"&skip="+skip+"&limit="+limit+"&fromDate="+fromDWeek+"&toDate="+toDWeek+"&type=declined";}

                        Log.i("unread2","m: "+unreadDeclinedUrl);
                        callUnreadMissedSession(unreadDeclinedUrl);
                        unreadSessionsRcv.setVisibility(View.VISIBLE);
                        mySessionsRcv.setVisibility(View.GONE);
                        monthSessionsRcv.setVisibility(View.GONE);
                        yearSessionsRcv.setVisibility(View.GONE);
                    }
                }
            }
        });

        linearUnreadMissed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skip=0;
                limit=10;
                tab=3;
                list.clear();
                listTemp.clear();
                final String unreadMissedUrl;
                if(time==2){
                    unreadMissedUrl=GLOBAL_URL+"session/pacifyr/all?userId="+userid+"&skip="+skip+"&limit="+limit+"&fromDate="+fromDYear+"&toDate="+toDYear+"&type=missed";}
                else if(time==1){
                    unreadMissedUrl=GLOBAL_URL+"session/pacifyr/all?userId="+userid+"&skip="+skip+"&limit="+limit+"&fromDate="+fromDMonth+"&toDate="+toDMonth+"&type=missed";}
                else {
                    unreadMissedUrl=GLOBAL_URL+"session/pacifyr/all?userId="+userid+"&skip="+skip+"&limit="+limit+"&fromDate="+fromDWeek+"&toDate="+toDWeek+"&type=missed";}

                setLoading();
                showProgress();
                Log.i("unread1","m: "+unreadMissedUrl);
                callUnreadMissedSession(unreadMissedUrl);
                unreadSessionsRcv.setVisibility(View.VISIBLE);
                mySessionsRcv.setVisibility(View.GONE);
                monthSessionsRcv.setVisibility(View.GONE);
                yearSessionsRcv.setVisibility(View.GONE);
            }
        });
        linearUnreadDeclined.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skip=0;
                limit=10;
                tab=4;
                list.clear();
                listTemp.clear();
                final String unreadDeclinedUrl;
                if(time==2){
                    unreadDeclinedUrl=GLOBAL_URL+"session/pacifyr/all?userId="+userid+"&skip="+skip+"&limit="+limit+"&fromDate="+fromDYear+"&toDate="+toDYear+"&type=declined";}
                else if(time==1){
                    unreadDeclinedUrl=GLOBAL_URL+"session/pacifyr/all?userId="+userid+"&skip="+skip+"&limit="+limit+"&fromDate="+fromDMonth+"&toDate="+toDMonth+"&type=declined";}
                else {
                    unreadDeclinedUrl=GLOBAL_URL+"session/pacifyr/all?userId="+userid+"&skip="+skip+"&limit="+limit+"&fromDate="+fromDWeek+"&toDate="+toDWeek+"&type=declined";}

                setLoading();
                showProgress();
                Log.i("unread2","m: "+unreadDeclinedUrl);
                callUnreadMissedSession(unreadDeclinedUrl);
                unreadSessionsRcv.setVisibility(View.VISIBLE);
                mySessionsRcv.setVisibility(View.GONE);
                monthSessionsRcv.setVisibility(View.GONE);
                yearSessionsRcv.setVisibility(View.GONE);
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
                time=0;
                setLoading();
                showProgress();
                getStatistics(weekStatUrl);
                callThisWeekSession(thisWeekUrl);
                SessionWeekAdapter adapter = new SessionWeekAdapter(getActivity(), "week");
                LinearLayoutManager manager
                        = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                //manager.setReverseLayout(true);
                dateTypeRcv.setLayoutManager(manager);
                dateTypeRcv.setAdapter(adapter);
                dateTypeRcv.scrollToPosition(dateTypeRcv.getAdapter().getItemCount() - 1);
                thisWkCtxv.setBackground(getResources().getDrawable(R.drawable.blue_line_bottom));
                thisMntCtxv.setBackground(getResources().getDrawable(com.pacifyrapp.utilitylibrary.R.color.grey_hex_f8));
                thisYearCtxv.setBackground(getResources().getDrawable(com.pacifyrapp.utilitylibrary.R.color.grey_hex_f8));
                thisWkCtxv.setTextColor(getResources().getColor(R.color.blue_pacifyr));
                thisMntCtxv.setTextColor(getResources().getColor(com.pacifyrapp.utilitylibrary.R.color.grey_hex_c7));
                thisYearCtxv.setTextColor(getResources().getColor(com.pacifyrapp.utilitylibrary.R.color.grey_hex_c7));
                mySessionsRcv.setVisibility(View.VISIBLE);
                monthSessionsRcv.setVisibility(View.GONE);
                yearSessionsRcv.setVisibility(View.GONE);
                unreadSessionsRcv.setVisibility(View.GONE);
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
                time=1;
                setLoading();
                showProgress();
                getStatistics(monthStatUrl);
                callThisMonthSession(thisMonthUrl);
                SessionWeekAdapter adapter = new SessionWeekAdapter(getActivity(), "month");
                LinearLayoutManager manager
                        = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                //manager.setReverseLayout(true);
                dateTypeRcv.setLayoutManager(manager);
                dateTypeRcv.setAdapter(adapter);
                dateTypeRcv.scrollToPosition(dateTypeRcv.getAdapter().getItemCount() - 1);
                thisWkCtxv.setBackground(getResources().getDrawable(com.pacifyrapp.utilitylibrary.R.color.grey_hex_f8));
                thisYearCtxv.setBackground(getResources().getDrawable(com.pacifyrapp.utilitylibrary.R.color.grey_hex_f8));
                thisMntCtxv.setBackground(getResources().getDrawable(R.drawable.blue_line_bottom));
                thisMntCtxv.setTextColor(getResources().getColor(R.color.blue_pacifyr));
                thisWkCtxv.setTextColor(getResources().getColor(com.pacifyrapp.utilitylibrary.R.color.grey_hex_c7));
                thisYearCtxv.setTextColor(getResources().getColor(com.pacifyrapp.utilitylibrary.R.color.grey_hex_c7));
                monthSessionsRcv.setVisibility(View.VISIBLE);
                monthSessionsRcv.setVisibility(View.GONE);
                yearSessionsRcv.setVisibility(View.GONE);
                unreadSessionsRcv.setVisibility(View.GONE);
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
                time=2;
                setLoading();
                showProgress();
                getStatistics(yearStatUrl);
                callThisYearSession(thisYearUrl);
                SessionWeekAdapter adapter = new SessionWeekAdapter(getActivity(), "year");
                LinearLayoutManager manager
                        = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                //manager.setReverseLayout(true);
                dateTypeRcv.setLayoutManager(manager);
                dateTypeRcv.setAdapter(adapter);
                dateTypeRcv.scrollToPosition(dateTypeRcv.getAdapter().getItemCount() - 1);
                thisYearCtxv.setBackground(getResources().getDrawable(R.drawable.blue_line_bottom));
                thisMntCtxv.setBackground(getResources().getDrawable(com.pacifyrapp.utilitylibrary.R.color.grey_hex_f8));
                thisWkCtxv.setBackground(getResources().getDrawable(com.pacifyrapp.utilitylibrary.R.color.grey_hex_f8));
                thisWkCtxv.setTextColor(getResources().getColor(com.pacifyrapp.utilitylibrary.R.color.grey_hex_c7));
                thisMntCtxv.setTextColor(getResources().getColor(com.pacifyrapp.utilitylibrary.R.color.grey_hex_c7));
                thisYearCtxv.setTextColor(getResources().getColor(R.color.blue_pacifyr));
                yearSessionsRcv.setVisibility(View.VISIBLE);
                yearSessionsRcv.setVisibility(View.GONE);
                monthSessionsRcv.setVisibility(View.GONE);
                unreadSessionsRcv.setVisibility(View.GONE);
            }
        });
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
                        earningsNoCtxv.setText(Utility.getPriceSign(getActivity()) + model.getEarnings());
                        String duration = model.getDuration();
                        String minute = duration.substring(3,5);
                        String hours = duration.substring(0,2);
                        minutesNoCtxv.setText(minute);
                        hoursNoCtxv.setText(hours);
                        missedCallCtxv.setText(model.getNumberOfMissedCalls()+ " MISSED CALLS");
                        declinedCallCtxv.setText(model.getNumberOfDeclinedCalls()+ " DECLINED CALLS");
                    }
                    catch(Exception excp){
                        excp.printStackTrace();
                    }
                }
            };
            AQuery aq = new AQuery(getActivity());
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


    private void callThisYearSession(String url) {
        try
        {



            noDataCtxv.setVisibility(View.GONE);
            yearSessionsRcv.setVisibility(View.GONE);
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    list.clear();
                    skip=skip+10;
                    hideProgress();
                    ModelSessonsOfCustomer model= GsonUtils.getInstance().gsonToModelSessonsOfCustomer(json);
                    list=model.getResultsList();
                    if (isValid(list)) {
                        listTemp.addAll(list);
                        PacifyrSessionAdapter adapter = new PacifyrSessionAdapter(getActivity(), listTemp, userid, token);
                        LinearLayoutManager verticalLayoutmanager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                        yearSessionsRcv.setLayoutManager(verticalLayoutmanager);
                        yearSessionsRcv.setVisibility(View.VISIBLE);
                        monthSessionsRcv.setVisibility(View.GONE);
                        mySessionsRcv.setVisibility(View.GONE);
                        unreadSessionsRcv.setVisibility(View.GONE);
                        yearSessionsRcv.setAdapter(adapter);
                    }
                    else {
                        if(listTemp.size()==0){
                        noDataCtxv.setVisibility(View.VISIBLE);
                        mySessionsRcv.setVisibility(View.GONE);
                        monthSessionsRcv.setVisibility(View.GONE);
                        yearSessionsRcv.setVisibility(View.GONE);
                        unreadSessionsRcv.setVisibility(View.GONE);
                        noDataCtxv.setText("There are no sessions this Year.");}
                    }
                }
            };
            AQuery aq = new AQuery(getActivity());
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

    private void callThisMonthSession(String url) {
        try {


            monthSessionsRcv.setVisibility(View.GONE);
            noDataCtxv.setVisibility(View.GONE);
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    list.clear();
                    skip=skip+10;
                    hideProgress();
                    ModelSessonsOfCustomer model= GsonUtils.getInstance().gsonToModelSessonsOfCustomer(json);
                    list=model.getResultsList();
                    if(isValid(list)) {
                        mySessionsRcv.setVisibility(View.GONE);
                        yearSessionsRcv.setVisibility(View.GONE);
                        unreadSessionsRcv.setVisibility(View.GONE);
                        listTemp.addAll(list);
                        PacifyrSessionAdapter adapter = new PacifyrSessionAdapter(getActivity(), listTemp, userid, token);
                        LinearLayoutManager verticalLayoutmanager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                        monthSessionsRcv.setLayoutManager(verticalLayoutmanager);
                        monthSessionsRcv.setVisibility(View.VISIBLE);
                        monthSessionsRcv.setAdapter(adapter);
                    }
                    else {
                        if(listTemp.size()==0){
                        noDataCtxv.setVisibility(View.VISIBLE);
                        mySessionsRcv.setVisibility(View.GONE);
                        monthSessionsRcv.setVisibility(View.GONE);
                        yearSessionsRcv.setVisibility(View.GONE);
                        unreadSessionsRcv.setVisibility(View.GONE);
                        noDataCtxv.setText("There are no sessions this Month.");}
                    }
                }
            };
            AQuery aq = new AQuery(getActivity());
            cb.header("Content-Type", "application/json");
            //cb.header("AccessToken",token);
            cb.header("AccessToken",token);
            aq.ajax(url,JSONObject.class, cb);
        } catch (Exception e) {
            hideProgress();
            e.printStackTrace();
        }
    }

    private void callThisWeekSession(String url) {
        try {
            Log.i("sesssuw","w: "+url);

            mySessionsRcv.setVisibility(View.GONE);
            noDataCtxv.setVisibility(View.GONE);
            unreadSessionsRcv.setVisibility(View.GONE);
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    list.clear();
                    skip=skip+10;
                    hideProgress();
                    Log.i("sesssuun12345","m: "+json+"   "+status);
                    ModelSessonsOfCustomer model= GsonUtils.getInstance().gsonToModelSessonsOfCustomer(json);
                    list=model.getResultsList();
                    if(isValid(list)) {
                        Log.i("sesssuun12345a","m: "+list);
                        Log.i("sesssuun12345b","m: "+listTemp);

                        listTemp.addAll(list);
                        PacifyrSessionAdapter adapter = new PacifyrSessionAdapter(getActivity(), listTemp, userid, token);
                        LinearLayoutManager verticalLayoutmanager
                                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                        mySessionsRcv.setLayoutManager(verticalLayoutmanager);
                        mySessionsRcv.setVisibility(View.VISIBLE);
                        monthSessionsRcv.setVisibility(View.GONE);
                        yearSessionsRcv.setVisibility(View.GONE);
                        unreadSessionsRcv.setVisibility(View.GONE);
                        mySessionsRcv.setAdapter(adapter);


                    }
                    else {
                        if(listTemp.size()==0){
                        noDataCtxv.setVisibility(View.VISIBLE);
                        mySessionsRcv.setVisibility(View.GONE);
                        monthSessionsRcv.setVisibility(View.GONE);
                        yearSessionsRcv.setVisibility(View.GONE);
                        unreadSessionsRcv.setVisibility(View.GONE);
                        noDataCtxv.setText("There are no sessions this Week.");}
                    }
                }
            };
            AQuery aq = new AQuery(getActivity());
            cb.header("Content-Type", "application/json");
            //cb.header("AccessToken",token);
            cb.header("AccessToken",token);
            aq.ajax(url,JSONObject.class, cb);
        } catch (Exception e) {
            hideProgress();
            e.printStackTrace();
        }
    }

    private void callUnreadMissedSession(String url) {
        try {
            mySessionsRcv.setVisibility(View.GONE);
            noDataCtxv.setVisibility(View.GONE);
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    list.clear();
                    skip=skip+10;
                    hideProgress();
                    Log.i("unread1234","m: "+json+"   "+status);
                    ModelSessonsOfCustomer model= GsonUtils.getInstance().gsonToModelSessonsOfCustomer(json);
                    list=model.getResultsList();
                    Log.i("unread123","m: "+list);
                    if(isValid(list)) {
                        listTemp.addAll(list);
                        PacifyrSessionAdapter adapter = new PacifyrSessionAdapter(getActivity(), listTemp, userid, token);
                        LinearLayoutManager verticalLayoutmanager
                                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                        unreadSessionsRcv.setLayoutManager(verticalLayoutmanager);
                        unreadSessionsRcv.setVisibility(View.VISIBLE);
                        mySessionsRcv.setVisibility(View.GONE);
                        monthSessionsRcv.setVisibility(View.GONE);
                        yearSessionsRcv.setVisibility(View.GONE);
                        unreadSessionsRcv.setAdapter(adapter);


                    }
                    else {
                        if(listTemp.size()==0){
                        noDataCtxv.setVisibility(View.VISIBLE);
                        mySessionsRcv.setVisibility(View.GONE);
                        monthSessionsRcv.setVisibility(View.GONE);
                        yearSessionsRcv.setVisibility(View.GONE);
                        unreadSessionsRcv.setVisibility(View.GONE);
                        noDataCtxv.setText("There are no sessions.");}
                    }
                }
            };
            AQuery aq = new AQuery(getActivity());
            cb.header("Content-Type", "application/json");
            //cb.header("AccessToken",token);
            cb.header("AccessToken",token);
            aq.ajax(url,JSONObject.class, cb);
        } catch (Exception e) {
            hideProgress();
            e.printStackTrace();
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Subscribe
    public void onNotifyEvent(NotifyEvent event) {

    }

    @Subscribe
    public void onWeekSetSessionEvent(WeekSetSessionEvent event) {
        try {
            showProgress();
            if(event.getWeekType().equals("week")) {
                fromDWeek=event.getFromDWeek();
                toDWeek=event.getToDWeek();
                listTemp.clear();
                list.clear();
                callThisWeekSession(event.getUrl());
                Log.i("sesssuw14","d: "+event.getUrl());
                getStatistics(event.getStatUrl());
            }
            else if(event.getWeekType().equals("month")){
               // fromDMonth=fromDateMonth;
                fromDMonth=event.getFromDWeek();
                toDMonth=event.getToDWeek();
                listTemp.clear();
                list.clear();
                callThisMonthSession(event.getUrl());
                getStatistics(event.getStatUrl());
            }
            else if(event.getWeekType().equals("year")){
               // fromDYear=fromDateYear;
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

    @Override
    public void onSetValues(View view) throws Exception {

    }

}
