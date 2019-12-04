package com.pacifyr.client.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.pacifyr.client.customview.MainTextView;
import com.pacifyrapp.utilitylibrary.model.pacifyr.ModelRatingDateDetail;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.pacifyr.client.R;
import com.pacifyr.client.event.BusProvider;
import com.pacifyr.client.event.WeekSetEvent;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collections;

import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.GLOBAL_URL;

/**
 * Created by ceino on 25/1/18.
 */

public class SortWeekAdapter extends RecyclerView.Adapter<SortWeekAdapter.MyViewHolder>{

    public String token;
    public String dateType, ratingType;
    public String userid;
    public PrefManager prefM;
    private ArrayList<ModelRatingDateDetail> modelWeekList;
    private ArrayList<ModelRatingDateDetail> modelMonthList;
    private ArrayList<ModelRatingDateDetail> modelYearList;
    Context context;
    ModelRatingDateDetail modelRatingDateDetail;
    private String statUrl;
    private String ratingUrl;

    public SortWeekAdapter(Context context) {
        this.context=context;
        modelRatingDateDetail = new ModelRatingDateDetail();
        createWeekList();
    }

    public SortWeekAdapter(Context context, String type, String pageType) {
        this.context=context;
        modelRatingDateDetail = new ModelRatingDateDetail();
        prefM = PrefManager.getInstance(context);
        token = prefM.getAccessToken();
        userid = prefM.getUserID();
        dateType = type;
        this.ratingType =pageType;

        if (type.equals("week"))
        {
            if(!isValid(modelWeekList)) {
                modelWeekList = new ArrayList<ModelRatingDateDetail>();
                createWeekList();
            }
        }
        else if(type.equals("month"))
        {
            if(!isValid(modelMonthList)) {
                modelMonthList = new ArrayList<ModelRatingDateDetail>();
                createMonthList();
            }
        }else{
            if(!isValid(modelYearList)) {
                modelYearList = new ArrayList<ModelRatingDateDetail>();
                createYearList();
            }
        }

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_sort_date, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        if(dateType.equals("week")) {
            holder.titleCtxv.setText(modelWeekList.get(position).getFromDateDisplay() + " " + modelWeekList.get(position).getToDateDisplay());
            setViewHolder(holder,modelWeekList,position);
        }
        else if (dateType.equals("month")){
            holder.titleCtxv.setText(modelMonthList.get(position).getFromDateDisplay() );
            setViewHolder(holder,modelMonthList,position);
        }else if(dateType.equals("year")){
            holder.titleCtxv.setText(modelYearList.get(position).getFromDateDisplay() );
            setViewHolder(holder,modelYearList,position);

        }

       // postEventOnMainThread(new WeekSetEvent(ratingUrl,statUrl,dateType));

    }

    private void setViewHolder(MyViewHolder holder, final ArrayList<ModelRatingDateDetail> modelList,final int position) {
        if (modelList.get(position).isSelect()){
            holder.topRlay.setBackground(context.getResources().getDrawable(R.drawable.blue_line_bottom));
            holder.titleCtxv.setTextColor(context.getResources().getColor(R.color.blue_pacifyr));
        } else{
            holder.topRlay.setBackground(null);
            holder.titleCtxv.setTextColor(context.getResources().getColor(R.color.black));
        }
        holder.topLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingUrl = modelList.get(position).getRatingUrl();
                statUrl = modelList.get(position).getStatUrl();
                if(!modelList.get(position).isSelect()){
                    // modelRatingDateDetailList.get(position).setSelect(true);
                    for(int i = 0; i< modelList.size(); i++){
                        if(i==position)
                        {
                            modelList.get(position).setSelect(true);
                        }
                        else{
                            modelList.get(i).setSelect(false);
                        }
                    }
                }
                postEventOnMainThread(new WeekSetEvent(ratingUrl,statUrl,dateType));
                notifyDataSetChanged();

            }
        });
    }

    @Override
    public int getItemCount() {
        if(dateType.equals("week")) {
            return modelWeekList.size();
        }
        else if(dateType.equals("month")){
            return modelMonthList.size();

        }else{
            return modelYearList.size();
        }
    }

    private void createYearList() {
        try {
            LocalDate currentDate = new LocalDate();
            LocalDate startofCurrentYear = currentDate.withDayOfYear(DateTimeConstants.JANUARY);

            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");

            DateTimeFormatter displayFormatter = DateTimeFormat.forPattern("YYYY");

            String fromDate = formatter.print(startofCurrentYear);
            String toDate = formatter.print(currentDate);

            String displayFrom = displayFormatter.print(startofCurrentYear);
            String displayTo = displayFormatter.print(currentDate);

            String yearStat = GLOBAL_URL + "dashboard/pacifyr/" + userid + "/sessionStatistics?fromDate=" + fromDate + "&toDate=" + toDate;
            String thisYearUrl = GLOBAL_URL + "session/pacifyr/all?userId=" + userid + "&fromDate=" + fromDate + "&toDate=" + toDate;

            modelRatingDateDetail.setFromDate(fromDate);
            modelRatingDateDetail.setToDate(toDate);
            modelRatingDateDetail.setRatingUrl(thisYearUrl);
            modelRatingDateDetail.setFromDateDisplay(displayFrom);
            modelRatingDateDetail.setToDateDisplay(displayTo);
            modelRatingDateDetail.setStatUrl(yearStat);
            modelYearList.add(modelRatingDateDetail);

            LocalDate startofSecndYear = startofCurrentYear.minusYears(1);
            //LocalDate secondTo = startofSecndYear.withDayOfYear(DateTimeConstants.JANUARY);
            LocalDate endOfSecondYear = startofCurrentYear.minusDays(1);


            for(int i=0;i<11;i++)
            {
                modelRatingDateDetail = new ModelRatingDateDetail();
                String fromDateMonth = formatter.print(startofSecndYear);
                String toDate2 = formatter.print(endOfSecondYear);

                String thisWeekUrl2 = GLOBAL_URL + "session/pacifyr/all?userId=" + userid + "&fromDate="
                        + fromDateMonth + "&toDate=" + toDate2;
                String  weekStatUrl2 = GLOBAL_URL+"dashboard/pacifyr/"+userid+
                        "/sessionStatistics?fromDate="+fromDateMonth+"&toDate="+toDate2;

                displayFrom = displayFormatter.print(startofSecndYear);
                displayTo = displayFormatter.print(endOfSecondYear);

                modelRatingDateDetail.setFromDateDisplay(displayFrom);
                modelRatingDateDetail.setToDateDisplay(displayTo);
                modelRatingDateDetail.setFromDate(fromDateMonth);
                modelRatingDateDetail.setToDate(toDate2);
                modelRatingDateDetail.setRatingUrl(thisWeekUrl2);
                modelRatingDateDetail.setStatUrl(weekStatUrl2);
                modelYearList.add(modelRatingDateDetail);
                endOfSecondYear = endOfSecondYear.minusYears(1);
                startofSecndYear = startofSecndYear.minusYears(1);

            }
            modelYearList.get(0).setSelect(true);
            Collections.reverse(modelYearList);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void createMonthList() {

        try {
            LocalDate now = new LocalDate();
            LocalDate startofMonth = now.withDayOfMonth(DateTimeConstants.JANUARY);
            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
            DateTimeFormatter displayFormatter = DateTimeFormat.forPattern("MMM");
            String fromDateWeek = formatter.print(startofMonth);
            String displayFrom = displayFormatter.print(startofMonth);
            String displayTo = displayFormatter.print(now);
            String toDate = formatter.print(now);
            String monthStat,thisMonthUrl;

            if(ratingType.equals("fiveStar")){

                monthStat = "fiveStar";
                thisMonthUrl = GLOBAL_URL + "session/pacifyr/rated?userId=" + userid + "&fromDate=" + fromDateWeek + "&toDate=" + toDate+"&rating=5";

            }else {
                monthStat = GLOBAL_URL + "dashboard/pacifyr/" + userid + "/ratingStatistics?fromDate=" + fromDateWeek + "&toDate=" + toDate;
                thisMonthUrl = GLOBAL_URL + "session/pacifyr/rated?userId=" + userid + "&fromDate=" + fromDateWeek + "&toDate=" + toDate;
            }
            modelRatingDateDetail.setFromDate(fromDateWeek);
            modelRatingDateDetail.setToDate(toDate);
            modelRatingDateDetail.setRatingUrl(thisMonthUrl);
            modelRatingDateDetail.setFromDateDisplay(displayFrom);
            modelRatingDateDetail.setToDateDisplay(displayTo);
            modelRatingDateDetail.setStatUrl(monthStat);
            modelMonthList.add(modelRatingDateDetail);

            LocalDate startofSecndMonth = startofMonth.minusMonths(1);
            LocalDate secondTo = startofSecndMonth.dayOfMonth().withMaximumValue();

            for(int i=0;i<11;i++)
            {
                modelRatingDateDetail = new ModelRatingDateDetail();
                String fromDateMonth = formatter.print(startofSecndMonth);
                String toDate2 = formatter.print(secondTo);
                String monthStatUrl2,thisMonthUrl2;
                if(ratingType.equals("fiveStar")){

                    monthStatUrl2 = "fiveStar";
                    thisMonthUrl2 = GLOBAL_URL + "session/pacifyr/rated?userId=" + userid + "&fromDate=" + fromDateWeek + "&toDate=" + toDate+"&rating=5";

                }else {
                    thisMonthUrl2 = GLOBAL_URL + "session/pacifyr/rated?userId="
                            + userid + "&fromDate=" + fromDateMonth + "&toDate=" + toDate2;
                    monthStatUrl2 = GLOBAL_URL + "dashboard/pacifyr/" + userid +
                            "/ratingStatistics?fromDate=" + fromDateMonth + "&toDate=" + toDate2;
                }

                displayFrom = displayFormatter.print(startofSecndMonth);
                displayTo = displayFormatter.print(secondTo);
                modelRatingDateDetail.setFromDateDisplay(displayFrom);
                modelRatingDateDetail.setToDateDisplay(displayTo);
                modelRatingDateDetail.setFromDate(fromDateMonth);
                modelRatingDateDetail.setToDate(toDate2);
                modelRatingDateDetail.setRatingUrl(thisMonthUrl2);
                modelRatingDateDetail.setStatUrl(monthStatUrl2);
                modelMonthList.add(modelRatingDateDetail);
                startofSecndMonth = startofSecndMonth.minusMonths(1);
                secondTo = startofSecndMonth.dayOfMonth().withMaximumValue();

            }
            modelMonthList.get(0).setSelect(true);
            Collections.reverse(modelMonthList);
        }
        catch (Exception ex)
        {ex.printStackTrace();}
    }

    private void createWeekList() {

        try {
            LocalDate now1 = new LocalDate();
            LocalDate backNow = now1.minusDays(7);
            LocalDate startofWeek = backNow.withDayOfWeek(DateTimeConstants.SUNDAY);
            LocalDate now = now1.withDayOfWeek(DateTimeConstants.SATURDAY);
            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");

            DateTimeFormatter displayFormatter = DateTimeFormat.forPattern("MMM dd");
            DateTimeFormatter displaymonth = DateTimeFormat.forPattern("MMM");
            DateTimeFormatter displayFormatterdateonly = DateTimeFormat.forPattern("- dd");


            String fromDateWeek = formatter.print(startofWeek);
            String displayFrom = displayFormatter.print(startofWeek);
            String displayTo = displayFormatter.print(now);
            String toDate = formatter.print(now);

            String start_day_month= displaymonth.print(startofWeek);
            String end_day_month= displaymonth.print(now);
            displayFrom = displayFormatter.print(startofWeek);
            if(start_day_month.equalsIgnoreCase(end_day_month))
                displayTo = displayFormatterdateonly.print(now);
            else
                displayTo = displayFormatter.print(now);


            String weekStatUrl;
            String thisWeekUrl;

            if(ratingType.equals("fiveStar")){
                weekStatUrl = "fiveStar";
                thisWeekUrl = GLOBAL_URL + "session/pacifyr/rated?userId=" + userid + "&fromDate=" + fromDateWeek + "&toDate=" + toDate+"&rating=5";


            }else {

                weekStatUrl = GLOBAL_URL + "dashboard/pacifyr/" + userid + "/ratingStatistics?fromDate=" + fromDateWeek + "&toDate=" + toDate;
                thisWeekUrl = GLOBAL_URL + "session/pacifyr/rated?userId=" + userid + "&fromDate=" + fromDateWeek + "&toDate=" + toDate;
            }
            modelRatingDateDetail.setFromDate(fromDateWeek);
            modelRatingDateDetail.setToDate(toDate);
            modelRatingDateDetail.setRatingUrl(thisWeekUrl);
            modelRatingDateDetail.setFromDateDisplay(displayFrom);
            modelRatingDateDetail.setToDateDisplay(displayTo);
            modelRatingDateDetail.setStatUrl(weekStatUrl);
            modelWeekList.add(modelRatingDateDetail);

            LocalDate startofSecndWeek = startofWeek.minusDays(7);
            LocalDate secondTo = startofWeek.minusDays(1);

            for(int i=0;i<50;i++)
            {
                modelRatingDateDetail = new ModelRatingDateDetail();
                //startofSecndWeek = startofWeek.minusDays(7);
                //secondTo = startofWeek.minusDays(1);



                String fromDateWeek2 = formatter.print(startofSecndWeek);
                String toDate2 = formatter.print(secondTo);
                String thisWeekUrl2,weekStatUrl2;

                if(ratingType.equals("fiveStar")){
                    thisWeekUrl2 = GLOBAL_URL + "session/pacifyr/rated?userId="
                            + userid + "&fromDate=" + fromDateWeek2 + "&toDate=" + toDate2+"&rating=5";
                    weekStatUrl2 = "fiveStar";
                }
                else {
                    thisWeekUrl2 = GLOBAL_URL + "session/pacifyr/rated?userId=" + userid + "&fromDate=" + fromDateWeek2 + "&toDate=" + toDate2;

                    weekStatUrl2 = GLOBAL_URL + "dashboard/pacifyr/" + userid + "/ratingStatistics?fromDate=" + fromDateWeek2 + "&toDate=" + toDate2;
                }
                displayFrom = displayFormatter.print(startofSecndWeek);
                if(start_day_month.equalsIgnoreCase(end_day_month))
                displayTo = displayFormatterdateonly.print(secondTo);
                else
                displayTo = displayFormatter.print(secondTo);

                modelRatingDateDetail.setFromDateDisplay(displayFrom);
                modelRatingDateDetail.setToDateDisplay(displayTo);
                modelRatingDateDetail.setFromDate(fromDateWeek2);
                modelRatingDateDetail.setToDate(toDate2);
                modelRatingDateDetail.setRatingUrl(thisWeekUrl2);
                modelRatingDateDetail.setStatUrl(weekStatUrl2);
                modelWeekList.add(modelRatingDateDetail);
                secondTo = startofSecndWeek.minusDays(1);
                startofSecndWeek = startofSecndWeek.minusDays(7);

            }
            modelWeekList.get(0).setSelect(true);
            Collections.reverse(modelWeekList);

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

    public SortWeekAdapter(Context context, ArrayList<String> strings) {
        this.context=context;
    }


    public Boolean isValid(Object object) {
        return object != null;

    }

    void postEventOnMainThread(final Object event) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                BusProvider.getInstance().post(event);
                // bus.post(event);
            }
        });

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MainTextView titleCtxv;
        public LinearLayout topLlay;
        public RelativeLayout topRlay;

        public MyViewHolder(View view) {
            super(view);
            topRlay = view.findViewById(R.id.top_rlay);
            topLlay = view.findViewById(R.id.top_llay);
            titleCtxv = view.findViewById(R.id.title_ctxv);
        }
    }
}
