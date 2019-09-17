package com.pacifyr.omhealthcare.Fragment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ceino.utilitylibrary.CustomFontTextView;
import com.ceino.utilitylibrary.model.pacifyr.MSessionDetailRoot;
import com.ceino.utilitylibrary.model.pacifyr.ModelSessonsOfCustomer;
import com.ceino.utilitylibrary.model.pacifyr.ModelStatistics;
import com.ceino.utilitylibrary.utils.PrefManager;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.adapter.MySessionAdapter;
import com.pacifyr.omhealthcare.adapter.SessionWeekCustomerAdapter;
import com.pacifyr.omhealthcare.event.NotifyEvent;
import com.pacifyr.omhealthcare.event.WeekSetSessionEvent;
import com.pacifyr.omhealthcare.network.GsonUtils;
import com.pacifyr.omhealthcare.network.NetworkService;
import com.pacifyr.omhealthcare.quickblox.util.NestedScroll;
import com.pacifyr.omhealthcare.utility.BaseFragment;
import com.pacifyr.omhealthcare.utils.LogUtils;
import com.pacifyr.omhealthcare.utils.Utility;
import com.squareup.otto.Subscribe;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.ceino.utilitylibrary.network.UtilityNetworkService.GLOBAL_URL;
import static com.pacifyr.omhealthcare.base.PacifyerActivity.sessionid;

public class MySessionFragment extends BaseFragment {

    private LinearLayout mySessionsTopLlay;
    private RelativeLayout upperRlay;
    private CustomFontTextView sessionNoCtxv;
    private CustomFontTextView hoursNoCtxv;
    private CustomFontTextView earningsNoCtxv;
    private CustomFontTextView thisWkCtxv;
    private CustomFontTextView thisMntCtxv;
    private CustomFontTextView thisYearCtxv;
    private RecyclerView weekSessionsRcv;
    private RecyclerView monthSessionsRcv;
    private RecyclerView yearSessionsRcv;
    private PrefManager prefM;
    private String token,userid;
    private CustomFontTextView minutesNoCtxv;
    private  int skip,limit;
    private RecyclerView dateListRcv;
    private CustomFontTextView noDataCtxv;
    String fromDWeek,fromDMonth,fromDYear;
    String toDWeek,toDMonth,toDYear;

    ArrayList<MSessionDetailRoot> list=new ArrayList<>();
    ArrayList<MSessionDetailRoot> listTemp=new ArrayList<>();
    int tab=0;//0:Week,1:Month,2:Year
    NestedScrollView nestedscrollView;
    private boolean firstItemEditable = true;


    //public static ArrayList<ModelListEmotion.Results> emotionList;

    public MySessionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_session, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        Log.i("sesssurlwa12","w: "+limit);
        Log.i("emoooooooojiiii","l:  ");

        if(isValid(sessionid)) webGetSessionDetail(sessionid);
        else {
            //handlerFinish("Data not updated in last session ");
            // finish();
        }

        prefM= PrefManager.getInstance(getActivity());
        token=prefM.getAccessToken();
        userid=prefM.getUserID();

        nestedscrollView = view.findViewById(R.id.nestedscrollView);

        mySessionsTopLlay = view.findViewById(R.id.my_sessions_top_llay);
        upperRlay = view.findViewById(R.id.upper_rlay);
        sessionNoCtxv = view.findViewById(R.id.session_no_ctxv);
        hoursNoCtxv = view.findViewById(R.id.hours_no_ctxv);
        minutesNoCtxv = view.findViewById(R.id.minutes_no_ctxv);
        earningsNoCtxv = view.findViewById(R.id.earnings_no_ctxv);
        thisWkCtxv = view.findViewById(R.id.this_wk_ctxv);
        thisMntCtxv = view.findViewById(R.id.this_mnt_ctxv);
        thisYearCtxv = view.findViewById(R.id.this_year_ctxv);
        weekSessionsRcv = view.findViewById(R.id.my_sessions_rcv);
        monthSessionsRcv = view.findViewById(R.id.month_sessions_rcv);
        yearSessionsRcv = view.findViewById(R.id.year_sessions_rcv);
        dateListRcv = view.findViewById(R.id.date_list_rcv);
        noDataCtxv = view.findViewById(R.id.no_data_ctxv);

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
        final String toDate = formatter.print(now1);

        fromDWeek=fromDateWeek;
        toDWeek=toDate;
        toDMonth=toDate;
        toDYear=toDate;
        fromDMonth=fromDateMonth;
        fromDYear=fromDateYear;

        final String thisWeekUrl=GLOBAL_URL+"session/customer/all?userId="+userid+"&skip="+skip+"&limit="+limit+"&fromDate="+fromDWeek+"&toDate="+toDWeek;
        final String thisMonthUrl=GLOBAL_URL+"session/customer/all?userId="+userid+"&skip="+skip+"&limit="+limit+"&fromDate="+fromDMonth+"&toDate="+toDMonth;
        final String thisYearUrl=GLOBAL_URL+"session/customer/all?userId="+userid+"&skip="+skip+"&limit="+limit+"&fromDate="+fromDYear+"&toDate="+toDYear;

        final String weekStatUrl = GLOBAL_URL+"dashboard/customer/"+userid+"/sessionStatistics?fromDate="+fromDWeek+"&toDate="+toDWeek;
        final String monthStatUrl = GLOBAL_URL+"dashboard/customer/"+userid+"/sessionStatistics?fromDate="+fromDMonth+"&toDate="+toDMonth;
        final String yearStatUrl = GLOBAL_URL+"dashboard/customer/"+userid+"/sessionStatistics?fromDate="+fromDYear+"&toDate="+toDYear;
//        setLoading();

        getStatistics(weekStatUrl);
        callThisWeekSession(thisWeekUrl);
        Log.i("sesssurlw","w: "+thisWeekUrl);
        Log.i("sesssurlws","w: "+weekStatUrl);
        SessionWeekCustomerAdapter adapter =new SessionWeekCustomerAdapter(getActivity(),"week");
        LinearLayoutManager manager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
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
                    if(listTemp.size()==0){
                        thisYearCtxv.performClick();
                    }else
                        Log.i("sesssuruuuab3","u: "+skip);
                    final String thisYearUrl=GLOBAL_URL+"session/customer/all?userId="+userid+"&skip="+skip+"&limit="+limit+"&fromDate="+fromDYear+"&toDate="+toDYear;
                    final String yearStatUrl = GLOBAL_URL+"dashboard/customer/"+userid+"/sessionStatistics?fromDate="+fromDYear+"&toDate="+toDYear;

                    getStatistics(yearStatUrl);
                    callThisYearSession(thisYearUrl);
                    dateListRcv.scrollToPosition(dateListRcv.getAdapter().getItemCount() - 1);
                    thisYearCtxv.setBackground(getResources().getDrawable(R.drawable.blue_line_bottom));
                    thisMntCtxv.setBackground(getResources().getDrawable(R.color.grey_hex_f8));
                    thisWkCtxv.setBackground(getResources().getDrawable(R.color.grey_hex_f8));
                    thisWkCtxv.setTextColor(getResources().getColor(R.color.grey_background));
                    thisMntCtxv.setTextColor(getResources().getColor(R.color.grey_background));
                    thisYearCtxv.setTextColor(getResources().getColor(R.color.blue_pacifyr));
                    yearSessionsRcv.setVisibility(View.VISIBLE);
                    weekSessionsRcv.setVisibility(View.GONE);
                    monthSessionsRcv.setVisibility(View.GONE);
                }
                else if(tab==1){
                    if(listTemp.size()==0){
                        thisMntCtxv.performClick();
                    }else
                        Log.i("sesssuruuuab2","u: "+skip);
                    final String thisMonthUrl=GLOBAL_URL+"session/customer/all?userId="+userid+"&skip="+skip+"&limit="+limit+"&fromDate="+fromDMonth+"&toDate="+toDMonth;
                    final String monthStatUrl = GLOBAL_URL+"dashboard/customer/"+userid+"/sessionStatistics?fromDate="+fromDMonth+"&toDate="+toDMonth;

                    getStatistics(monthStatUrl);
                    callThisMonthSession(thisMonthUrl);

                    dateListRcv.scrollToPosition(dateListRcv.getAdapter().getItemCount() - 1);
                    thisWkCtxv.setBackground(getResources().getDrawable(R.color.grey_hex_f8));
                    thisYearCtxv.setBackground(getResources().getDrawable(R.color.grey_hex_f8));
                    thisMntCtxv.setBackground(getResources().getDrawable(R.drawable.blue_line_bottom));
                    thisMntCtxv.setTextColor(getResources().getColor(R.color.blue_pacifyr));
                    thisWkCtxv.setTextColor(getResources().getColor(R.color.grey_background));
                    thisYearCtxv.setTextColor(getResources().getColor(R.color.grey_background));
                    monthSessionsRcv.setVisibility(View.VISIBLE);
                    weekSessionsRcv.setVisibility(View.GONE);
                    yearSessionsRcv.setVisibility(View.GONE);
                }
                else if(tab==0){
                    if(listTemp.size()==0){
                        thisWkCtxv.performClick();
                    }else
                        Log.i("sesssuruuuab1","u: "+skip);
                    final String thisWeekUrl=GLOBAL_URL+"session/customer/all?userId="+userid+"&skip="+skip+"&limit="+limit+"&fromDate="+fromDWeek+"&toDate="+toDWeek;
                    final String weekStatUrl = GLOBAL_URL+"dashboard/customer/"+userid+"/sessionStatistics?fromDate="+fromDWeek+"&toDate="+toDWeek;

                    getStatistics(weekStatUrl);
                    callThisWeekSession(thisWeekUrl);

                    dateListRcv.scrollToPosition(dateListRcv.getAdapter().getItemCount() - 1);
                    thisWkCtxv.setBackground(getResources().getDrawable(R.drawable.blue_line_bottom));
                    thisMntCtxv.setBackground(getResources().getDrawable(R.color.grey_hex_f8));
                    thisYearCtxv.setBackground(getResources().getDrawable(R.color.grey_hex_f8));
                    thisWkCtxv.setTextColor(getResources().getColor(R.color.blue_pacifyr));
                    thisMntCtxv.setTextColor(getResources().getColor(R.color.grey_background));
                    thisYearCtxv.setTextColor(getResources().getColor(R.color.grey_background));
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
                SessionWeekCustomerAdapter adapter =new SessionWeekCustomerAdapter(getActivity(),"week");
                LinearLayoutManager manager
                        = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                //manager.setReverseLayout(true);
                dateListRcv.setLayoutManager(manager);
                dateListRcv.setAdapter(adapter);
                dateListRcv.scrollToPosition(dateListRcv.getAdapter().getItemCount() - 1);
                thisWkCtxv.setBackground(getResources().getDrawable(R.drawable.blue_line_bottom));
                thisMntCtxv.setBackground(getResources().getDrawable(R.drawable.grey_bg));
                thisYearCtxv.setBackground(getResources().getDrawable(R.drawable.grey_bg));
                thisWkCtxv.setTextColor(ContextCompat.getColor(getActivity(), R.color.blue_pacifyr));
                thisMntCtxv.setTextColor(ContextCompat.getColor(getActivity(), R.color.grey));
                thisYearCtxv.setTextColor(ContextCompat.getColor(getActivity(), R.color.grey));
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
                SessionWeekCustomerAdapter adapter =new SessionWeekCustomerAdapter(getActivity(),"month");
                LinearLayoutManager manager
                        = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                //manager.setReverseLayout(true);
                dateListRcv.setLayoutManager(manager);
                dateListRcv.setAdapter(adapter);
                dateListRcv.scrollToPosition(dateListRcv.getAdapter().getItemCount() - 1);
                thisWkCtxv.setBackground(getResources().getDrawable(R.drawable.grey_bg));
                thisYearCtxv.setBackground(getResources().getDrawable(R.drawable.grey_bg));
                thisMntCtxv.setBackground(getResources().getDrawable(R.drawable.blue_line_bottom));
                thisMntCtxv.setTextColor(ContextCompat.getColor(getActivity(), R.color.blue_pacifyr));
                thisWkCtxv.setTextColor(ContextCompat.getColor(getActivity(), R.color.grey));
                thisYearCtxv.setTextColor(ContextCompat.getColor(getActivity(), R.color.grey));
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
                SessionWeekCustomerAdapter adapter =new SessionWeekCustomerAdapter(getActivity(),"year");
                LinearLayoutManager manager
                        = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                //manager.setReverseLayout(true);
                dateListRcv.setLayoutManager(manager);
                dateListRcv.setAdapter(adapter);
                dateListRcv.scrollToPosition(dateListRcv.getAdapter().getItemCount() - 1);
                thisYearCtxv.setBackground(getResources().getDrawable(R.drawable.blue_line_bottom));
                thisMntCtxv.setBackground(getResources().getDrawable(R.drawable.grey_bg));
                thisWkCtxv.setBackground(getResources().getDrawable(R.drawable.grey_bg));
                thisWkCtxv.setTextColor(ContextCompat.getColor(getActivity(), R.color.grey));
                thisMntCtxv.setTextColor(ContextCompat.getColor(getActivity(), R.color.grey));
                thisYearCtxv.setTextColor(ContextCompat.getColor(getActivity(), R.color.blue_pacifyr));
                yearSessionsRcv.setVisibility(View.VISIBLE);
                weekSessionsRcv.setVisibility(View.GONE);
                monthSessionsRcv.setVisibility(View.GONE);
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
                        earningsNoCtxv.setText(Utility.getPriceSign(getActivity()) + model.getSpendings());
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
            AQuery aq = new AQuery(getActivity());
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
                    list.clear();
                    skip=skip+10;
                    hideProgress();
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

    private void setAdapter(ArrayList<MSessionDetailRoot> list,RecyclerView recyclerView){
        try {
            Log.i("sesssssss11","l:  "+userid+"   "+token);
            MySessionAdapter adapter = new MySessionAdapter(getActivity(), list, userid, token);
            adapter.setFirstItemEditable(firstItemEditable);
            LinearLayoutManager verticalLayoutmanager
                    = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
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
                    list.clear();
                    skip=skip+10;
                    hideProgress();
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

    private void callThisWeekSession(String url) {
        try {
            Log.i("weeeeek1","w:  "+url);
            weekSessionsRcv.setVisibility(View.GONE);
            noDataCtxv.setVisibility(View.GONE);
            setLoading();
            showProgress();
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    hideProgress();
                    Log.i("weeeeek12","w:  "+json);
                    list.clear();
                    skip=skip+10;
                    hideProgress();
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
            AQuery aq = new AQuery(getActivity());
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Subscribe
    public void onNotifyEvent(NotifyEvent event) {

    }

    @Override
    public void onSetValues(View view) throws Exception {

    }

    @Subscribe
    public void onWeekSetSessionEvent(WeekSetSessionEvent event) {
        firstItemEditable = event.getEditable();
        try {

            if(event.getWeekType().equals("week")) {
                fromDWeek=event.getFromDWeek();
                toDWeek=event.getToDWeek();
                listTemp.clear();
                list.clear();
                callThisWeekSession(event.getUrl());
                getStatistics(event.getStatUrl());
            }
            else if(event.getWeekType().equals("month")){
                fromDMonth=event.getFromDWeek();
                toDMonth=event.getToDWeek();
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
            AQuery aq = new AQuery(getActivity());
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



}


