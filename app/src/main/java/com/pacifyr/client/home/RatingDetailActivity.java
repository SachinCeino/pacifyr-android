package com.pacifyr.client.home;

import android.content.Intent;
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
import com.pacifyrapp.utilitylibrary.model.pacifyr.MRatingDetail;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MRatingStat;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.pacifyr.client.R;
import com.pacifyr.client.adapter.ListRatingDetailAdapter;
import com.pacifyr.client.adapter.SortWeekAdapter;
import com.pacifyr.client.event.AlertTypeEvent;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.event.WeekSetEvent;
import com.pacifyr.client.network.GsonUtils;
import com.pacifyr.client.utility.PacifyrAppCompactActivity;
import com.squareup.otto.Subscribe;

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

public class RatingDetailActivity extends PacifyrAppCompactActivity {

    private LinearLayout mySessionsTopLlay;
    private MainTextView thisWkCtxv;
    private MainTextView thisMntCtxv;
    private MainTextView thisYearCtxv;
    private RecyclerView listWeeksRcv;
    private RelativeLayout upperRlay;
    private MainTextView sessionNoCtxv;
    private MainTextView noDataCtxv;
    private RecyclerView weekRatingRcv;
    private RecyclerView monthRatingRcv;
    private RecyclerView yearRatingRcv;
    private PrefManager prefM;
    private String token,userid;
    private String ratingType;
    private String weekStat,monthStat,yearStat;
    public String thisWeekUrl,thisMonthUrl,thisYearUrl;


    String WeekStartDate,fromDYear;
    String WeekEndDate;
    String YearEndDate,FirstDateOfMonth,LastDateMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_detail);
        try {
            initisalizeView();
            onSetActionBar();
            Bundle b = new Bundle();
            Intent intent = getIntent();
            ratingType = intent.getStringExtra("type");
            prefM = PrefManager.getInstance(RatingDetailActivity.this);
            token = prefM.getAccessToken();
            userid = prefM.getUserID();

            Calendar calendar = Calendar.getInstance();
            int dayIsundaorNot=calendar.get(Calendar.DAY_OF_WEEK);

            LocalDate now1 = new LocalDate();
            LocalDate now = new LocalDate();

            if (dayIsundaorNot==Calendar.SUNDAY){
                now1.withDayOfWeek(DateTimeConstants.SATURDAY);
            }
            else
            {
                now = now1.withDayOfWeek(DateTimeConstants.SATURDAY);
            }

            LocalDate startofYear = now.withDayOfYear(DateTimeConstants.JANUARY);

            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
            final String fromDateYear = formatter.print(startofYear);
            fromDYear=fromDateYear;


/** getting last day of month**/
            Calendar cal = Calendar.getInstance();
            int res = cal.getActualMaximum(Calendar.DATE);
            cal.set(Calendar.DATE,res);
            Date date = cal.getTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            LastDateMonth = df.format(date);

            int firstdayMonth = cal.getActualMinimum(Calendar.DATE);
            cal.set(Calendar.DATE,firstdayMonth);
            date = cal.getTime();
            FirstDateOfMonth = df.format(date);

            /** getting last day of year**/

            Calendar calyear = Calendar.getInstance();
            calyear.set(Calendar.DAY_OF_YEAR, calyear.getActualMaximum(Calendar.DAY_OF_YEAR));
            Date yend = calyear.getTime();
            YearEndDate = df.format(yend);


            /** getting first day and last  day of week**/
            // get Current Week of the year
            Calendar calendarweeak=Calendar.getInstance();
            calendarweeak.setFirstDayOfWeek(Calendar.SUNDAY);
            calendarweeak.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            DateFormat dff = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            WeekStartDate = dff.format(calendarweeak.getTime());
            calendarweeak.add(Calendar.DATE, 6);
            WeekEndDate = dff.format(calendarweeak.getTime());

            if(ratingType.equals("fiveStar")) {

                weekStat = "fiveStar";
                monthStat = "fiveStar";
                yearStat = "fiveStar";

                thisWeekUrl = GLOBAL_URL + "session/pacifyr/rated?userId=" + userid + "&fromDate=" + WeekStartDate + "&toDate=" + WeekEndDate+"&rating=5";

                thisMonthUrl = GLOBAL_URL+"session/pacifyr/rated?userId="+userid+"&fromDate="+FirstDateOfMonth+"&toDate="+LastDateMonth+"&rating=5";

                thisYearUrl = GLOBAL_URL+"session/pacifyr/rated?userId="+userid+"&fromDate="+fromDateYear+"&toDate="+YearEndDate+"&rating=5";
            }
            else{

                thisWeekUrl = GLOBAL_URL + "session/pacifyr/rated?userId=" + userid + "&fromDate=" + WeekStartDate + "&toDate=" + WeekEndDate;
                thisMonthUrl = GLOBAL_URL+"session/pacifyr/rated?userId="+userid+"&fromDate="+FirstDateOfMonth+"&toDate="+LastDateMonth;
                thisYearUrl = GLOBAL_URL+"session/pacifyr/rated?userId="+userid+"&fromDate="+fromDateYear+"&toDate="+YearEndDate;
                weekStat = GLOBAL_URL+"dashboard/pacifyr/"+userid+"/ratingStatistics?fromDate="+WeekStartDate+"&toDate="+WeekEndDate;
                monthStat = GLOBAL_URL+"dashboard/pacifyr/"+userid+"/ratingStatistics?fromDate="+FirstDateOfMonth+"&toDate="+LastDateMonth;
                yearStat = GLOBAL_URL+"dashboard/pacifyr/"+userid+"/ratingStatistics?fromDate="+fromDateYear+"&toDate="+YearEndDate;

            }
            setLoading();
            showProgress();
            getStatistics(weekStat);
            callThisWeekRating(thisWeekUrl);
            SortWeekAdapter adapter =new SortWeekAdapter(RatingDetailActivity.this,"week",ratingType);
            LinearLayoutManager manager
                    = new LinearLayoutManager(RatingDetailActivity.this, LinearLayoutManager.HORIZONTAL, false);
            //manager.setReverseLayout(true);
            listWeeksRcv.setLayoutManager(manager);
            listWeeksRcv.setAdapter(adapter);
            listWeeksRcv.scrollToPosition(listWeeksRcv.getAdapter().getItemCount() - 1);


            thisWkCtxv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showProgress();
                    getStatistics(weekStat);
                    callThisWeekRating(thisWeekUrl);
                    thisWkCtxv.setBackground(getResources().getDrawable(R.drawable.blue_line_bottom));
                    thisMntCtxv.setBackground(getResources().getDrawable(com.pacifyrapp.utilitylibrary.R.color.grey_hex_f8));
                    thisYearCtxv.setBackground(getResources().getDrawable(com.pacifyrapp.utilitylibrary.R.color.grey_hex_f8));
                    thisWkCtxv.setTextColor(getResources().getColor(R.color.blue_pacifyr));
                    thisMntCtxv.setTextColor(getResources().getColor(com.pacifyrapp.utilitylibrary.R.color.grey_hex_c7));
                    thisYearCtxv.setTextColor(getResources().getColor(com.pacifyrapp.utilitylibrary.R.color.grey_hex_c7));

                    SortWeekAdapter adapter = new SortWeekAdapter(RatingDetailActivity.this, "week", ratingType);
                    LinearLayoutManager manager
                            = new LinearLayoutManager(RatingDetailActivity.this, LinearLayoutManager.HORIZONTAL, false);
                    listWeeksRcv.setLayoutManager(manager);
                    listWeeksRcv.setAdapter(adapter);
                    listWeeksRcv.scrollToPosition(listWeeksRcv.getAdapter().getItemCount() - 1);

                    weekRatingRcv.setVisibility(View.VISIBLE);
                    monthRatingRcv.setVisibility(View.GONE);
                    yearRatingRcv.setVisibility(View.GONE);
                    //getStatistics(weekStatUrl);
                    //callThisWeekRating(thisWeekUrl);
                    //etStatistics(weekStat);
                    //callThisWeekRating(thisWeekUrl);

                }
            });
            thisMntCtxv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        showProgress();
                        getStatistics(monthStat);
                        callThisMonthRating(thisMonthUrl);
                        thisWkCtxv.setBackground(getResources().getDrawable(com.pacifyrapp.utilitylibrary.R.color.grey_hex_f8));
                        thisYearCtxv.setBackground(getResources().getDrawable(com.pacifyrapp.utilitylibrary.R.color.grey_hex_f8));
                        thisMntCtxv.setBackground(getResources().getDrawable(R.drawable.blue_line_bottom));
                        thisMntCtxv.setTextColor(getResources().getColor(R.color.blue_pacifyr));
                        thisWkCtxv.setTextColor(getResources().getColor(com.pacifyrapp.utilitylibrary.R.color.grey_hex_c7));
                        thisYearCtxv.setTextColor(getResources().getColor(com.pacifyrapp.utilitylibrary.R.color.grey_hex_c7));

                        SortWeekAdapter adapter = new SortWeekAdapter(RatingDetailActivity.this, "month", ratingType);
                        LinearLayoutManager manager
                                = new LinearLayoutManager(RatingDetailActivity.this, LinearLayoutManager.HORIZONTAL, false);
                        listWeeksRcv.setLayoutManager(manager);
                        listWeeksRcv.setAdapter(adapter);
                        listWeeksRcv.scrollToPosition(listWeeksRcv.getAdapter().getItemCount() - 1);

                        monthRatingRcv.setVisibility(View.VISIBLE);
                        weekRatingRcv.setVisibility(View.GONE);
                        yearRatingRcv.setVisibility(View.GONE);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
            thisYearCtxv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        showProgress();
                        thisYearCtxv.setBackground(getResources().getDrawable(R.drawable.blue_line_bottom));
                        thisMntCtxv.setBackground(getResources().getDrawable(com.pacifyrapp.utilitylibrary.R.color.grey_hex_f8));
                        thisWkCtxv.setBackground(getResources().getDrawable(com.pacifyrapp.utilitylibrary.R.color.grey_hex_f8));
                        thisWkCtxv.setTextColor(getResources().getColor(com.pacifyrapp.utilitylibrary.R.color.grey_hex_c7));
                        thisMntCtxv.setTextColor(getResources().getColor(com.pacifyrapp.utilitylibrary.R.color.grey_hex_c7));
                        thisYearCtxv.setTextColor(getResources().getColor(R.color.blue_pacifyr));
                        getStatistics(yearStat);
                        callThisYearRating(thisYearUrl);
                        SortWeekAdapter adapter = new SortWeekAdapter(RatingDetailActivity.this, "year", ratingType);
                        LinearLayoutManager manager
                                = new LinearLayoutManager(RatingDetailActivity.this, LinearLayoutManager.HORIZONTAL, false);
                        listWeeksRcv.setLayoutManager(manager);
                        listWeeksRcv.setAdapter(adapter);
                        listWeeksRcv.scrollToPosition(listWeeksRcv.getAdapter().getItemCount() - 1);

                        yearRatingRcv.setVisibility(View.VISIBLE);
                        weekRatingRcv.setVisibility(View.GONE);
                        monthRatingRcv.setVisibility(View.GONE);
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }
            });
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
        }
    }

    private void getStatistics(String url) {
        try
        {
            if(url.equals("fiveStar")){
                sessionNoCtxv.setText("5");
            }
            else {
                AjaxCallback cb = new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject json, AjaxStatus status) {
                        try {
                            MRatingStat model = GsonUtils.getInstance().gsonToMRatingStat(json);
                            sessionNoCtxv.setText(model.getAverageRating());
                        } catch (Exception excp) {
                            hideProgress();
                            excp.printStackTrace();
                        }
                    }
                };
                AQuery aq = new AQuery(RatingDetailActivity.this);
                cb.header("Content-Type", "application/json");
                //cb.header("AccessToken",token);
                cb.header("AccessToken", token);
                aq.ajax(url, JSONObject.class, cb);
            }
        }
        catch (Exception e) {
            hideProgress();
            e.printStackTrace();
        }
    }


    private void callThisMonthRating(String thisMonthUrl) {
        try {
            noDataCtxv.setVisibility(View.GONE);
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    hideProgress();
                    MRatingDetail model= GsonUtils.getInstance().gsonToMRatingDetail(json);
                    ArrayList<MRatingDetail.Results> list=model.getResultList();
                    ListRatingDetailAdapter adapter =new ListRatingDetailAdapter(RatingDetailActivity.this,list,userid,token);
                    LinearLayoutManager manager
                            = new LinearLayoutManager(RatingDetailActivity.this, LinearLayoutManager.VERTICAL, false);
                    monthRatingRcv.setLayoutManager(manager);
                    monthRatingRcv.setVisibility(View.VISIBLE);
                    weekRatingRcv.setVisibility(View.GONE);
                    yearRatingRcv.setVisibility(View.GONE);
                    monthRatingRcv.setAdapter(adapter);
                    if(list.size()==0){
                        noDataCtxv.setVisibility(View.VISIBLE);
                        noDataCtxv.setText("No rating this period");
                    }
                }
            };
            AQuery aq = new AQuery(RatingDetailActivity.this);
            cb.header("Content-Type", "application/json");
            //cb.header("AccessToken",token);
            cb.header("AccessToken",token);
            aq.ajax(thisMonthUrl,JSONObject.class, cb);
        } catch (Exception e) {
            hideProgress();
            e.printStackTrace();
        }
    }

    private  void callThisWeekRating(String thisWeekUrl) {
        try {
            noDataCtxv.setVisibility(View.GONE);
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    hideProgress();
                    MRatingDetail model= GsonUtils.getInstance().gsonToMRatingDetail(json);
                    ArrayList<MRatingDetail.Results> list=model.getResultList();
                    ListRatingDetailAdapter adapter =new ListRatingDetailAdapter(RatingDetailActivity.this,list,userid,token);
                    LinearLayoutManager verticalLayoutmanager
                            = new LinearLayoutManager(RatingDetailActivity.this, LinearLayoutManager.VERTICAL, false);
                    weekRatingRcv.setLayoutManager(verticalLayoutmanager);
                    weekRatingRcv.setVisibility(View.VISIBLE);
                    monthRatingRcv.setVisibility(View.GONE);
                    yearRatingRcv.setVisibility(View.GONE);
                    weekRatingRcv.setAdapter(adapter);
                    if(list.size()==0){
                        noDataCtxv.setVisibility(View.VISIBLE);
                        noDataCtxv.setText("No rating this period");
                    }
                }
            };
            AQuery aq = new AQuery(RatingDetailActivity.this);
            cb.header("Content-Type", "application/json");
            //cb.header("AccessToken",token);
            cb.header("AccessToken",token);
            aq.ajax(thisWeekUrl,JSONObject.class, cb);
        } catch (Exception e) {
            hideProgress();
            e.printStackTrace();
        }
    }

    private void callThisYearRating(String url) {
        try {
            noDataCtxv.setVisibility(View.GONE);
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    hideProgress();
                    MRatingDetail model= GsonUtils.getInstance().gsonToMRatingDetail(json);
                    ArrayList<MRatingDetail.Results> list=model.getResultList();
                    ListRatingDetailAdapter adapter =new ListRatingDetailAdapter(RatingDetailActivity.this,list,userid,token);
                    LinearLayoutManager verticalLayoutmanager
                            = new LinearLayoutManager(RatingDetailActivity.this, LinearLayoutManager.VERTICAL, false);
                    yearRatingRcv.setLayoutManager(verticalLayoutmanager);
                    yearRatingRcv.setVisibility(View.VISIBLE);
                    monthRatingRcv.setVisibility(View.GONE);
                    monthRatingRcv.setVisibility(View.GONE);
                    yearRatingRcv.setAdapter(adapter);
                    if(list.size()==0){
                        noDataCtxv.setVisibility(View.VISIBLE);
                        noDataCtxv.setText("No rating this period");
                    }
                }
            };
            AQuery aq = new AQuery(RatingDetailActivity.this);
            cb.header("Content-Type", "application/json");
            //cb.header("AccessToken",token);
            cb.header("AccessToken",token);
            aq.ajax(url,JSONObject.class, cb);
        } catch (Exception e) {
            hideProgress();
            e.printStackTrace();
        }
    }


    private void initisalizeView() {
        try {
            mySessionsTopLlay = findViewById(R.id.my_sessions_top_llay);
            thisWkCtxv = findViewById(R.id.this_wk_ctxv);
            thisMntCtxv = findViewById(R.id.this_mnt_ctxv);
            thisYearCtxv = findViewById(R.id.this_year_ctxv);
            listWeeksRcv = findViewById(R.id.list_weeks_rcv);
            upperRlay = findViewById(R.id.upper_rlay);
            sessionNoCtxv = findViewById(R.id.session_no_ctxv);
            noDataCtxv = findViewById(R.id.no_data_ctxv);
            weekRatingRcv = findViewById(R.id.week_rating_rcv);
            monthRatingRcv = findViewById(R.id.month_rating_rcv);
            yearRatingRcv = findViewById(R.id.year_rating_rcv);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
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
            defSetText(titleTxv,"MY RATINGS");
            backImv.setImageDrawable(getResources().getDrawable(R.drawable.backarrow));
            //backImv.setBackgroundResource(R.drawable.leftmen);
            leftLlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public void onSetValues() throws Exception {

    }

    @Subscribe
    public void onNotifyEvent(NotifyEvent event) {

    }


    @Subscribe
    public void onWeekSetEvent(WeekSetEvent event) {
        try {
            showProgress();
            if(event.getWeekType().equals("week")) {
                callThisWeekRating(event.getUrl());
                getStatistics(event.getStatUrl());
            }
            else if(event.getWeekType().equals("month")){
                callThisMonthRating(event.getUrl());
                getStatistics(event.getStatUrl());
            }
            else if(event.getWeekType().equals("year")){
                callThisYearRating(event.getUrl());
                getStatistics(event.getStatUrl());
            }
        }
        catch (Exception e){
            hideProgress();
            e.printStackTrace();
        }

    }

    @Override
    public void onAlertTypeEvent(AlertTypeEvent event) {

    }

    @Override
    protected View getSnackbarAnchorView() {
        return null;
    }
}
