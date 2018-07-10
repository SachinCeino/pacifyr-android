package com.pacifyr.omhealthcare.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ceino.utilitylibrary.CustomFontTextView;
import com.ceino.utilitylibrary.model.pacifyr.ModelRatingDateDetail;
import com.ceino.utilitylibrary.utils.PrefManager;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.event.BusProvider;
import com.pacifyr.omhealthcare.event.WeekSetSessionEvent;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collections;

import static com.ceino.utilitylibrary.network.UtilityNetworkService.GLOBAL_URL;

/**
 * Created by ceino on 25/1/18.
 */

public class SessionWeekCustomerAdapter extends RecyclerView.Adapter<SessionWeekCustomerAdapter.MyViewHolder>{

    public String token;
    public String dateType;
    public String userid;
    public PrefManager prefM;
    private ArrayList<ModelRatingDateDetail> modelWeekList;
    private ArrayList<ModelRatingDateDetail> modelMonthList;
    private ArrayList<ModelRatingDateDetail> modelYearList;
    Context context;
    ModelRatingDateDetail modelRatingDateDetail;
    private String statUrl;
    private String ratingUrl;
    String fromDWeek,toDWeek;

    public SessionWeekCustomerAdapter(Context context) {
        this.context=context;
        modelRatingDateDetail = new ModelRatingDateDetail();
        createWeekList();
    }

    public SessionWeekCustomerAdapter(Context context, String type) {
        this.context=context;
        modelRatingDateDetail = new ModelRatingDateDetail();
        prefM = PrefManager.getInstance(context);
        token = prefM.getAccessToken();
        userid = prefM.getUserID();
        dateType = type;

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
                fromDWeek=modelList.get(position).getFromDate();
                toDWeek= modelList.get(position).getToDate();
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
                postEventOnMainThread(new WeekSetSessionEvent(ratingUrl,statUrl,dateType,fromDWeek,toDWeek));
                notifyDataSetChanged();
                Log.i("uuuurrrrraac1","u:  "+ratingUrl);

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


            String monthStat = GLOBAL_URL+"dashboard/customer/"+userid+"/sessionStatistics?fromDate="+fromDateWeek+"&toDate="+toDate;
            String thisMonthUrl = GLOBAL_URL + "session/customer/all?userId=" + userid + "&fromDate=" + fromDateWeek + "&toDate=" + toDate;
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
                String thisWeekUrl2 = GLOBAL_URL + "session/customer/all?userId="
                        + userid + "&fromDate=" + fromDateMonth + "&toDate=" + toDate2;
                String  weekStatUrl2 = GLOBAL_URL+"dashboard/customer/"+userid+
                        "/sessionStatistics?fromDate="+fromDateMonth+"&toDate="+toDate2;

                displayFrom = displayFormatter.print(startofSecndMonth);
                displayTo = displayFormatter.print(secondTo);
                modelRatingDateDetail.setFromDateDisplay(displayFrom);
                modelRatingDateDetail.setToDateDisplay(displayTo);
                modelRatingDateDetail.setFromDate(fromDateMonth);
                modelRatingDateDetail.setToDate(toDate2);
                modelRatingDateDetail.setRatingUrl(thisWeekUrl2);
                modelRatingDateDetail.setStatUrl(weekStatUrl2);
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
            String fromDateWeek = formatter.print(startofWeek);
            String displayFrom = displayFormatter.print(startofWeek);
            String displayTo = displayFormatter.print(now);
            String toDate = formatter.print(now);



            DateTimeFormatter displaymonth = DateTimeFormat.forPattern("MMM");
            DateTimeFormatter displayFormatterdateonly = DateTimeFormat.forPattern("- dd");
            String start_day_month= displaymonth.print(startofWeek);
            Log.i("dddddaaaa1","d: "+start_day_month);
            String end_day_month= displaymonth.print(now);
            Log.i("dddddaaaa12","d: "+end_day_month);
            displayFrom = displayFormatter.print(startofWeek);
            Log.i("dddddaaaa123","d: "+displayFrom);
            if(start_day_month.equalsIgnoreCase(end_day_month)){
                displayTo = displayFormatterdateonly.print(now);
                Log.i("dddddaaaa1234","d: "+displayTo);}
            else
            { displayTo = displayFormatter.print(now);
                Log.i("dddddaaaa12345","d: "+displayTo);}

            Log.i("dddddaaaa123456","d: "+displayTo);

            String weekStatUrl = GLOBAL_URL+"dashboard/customer/"+userid+"/sessionStatistics?fromDate="+fromDateWeek+"&toDate="+toDate;
            String thisWeekUrl = GLOBAL_URL + "session/customer/all?userId=" + userid + "&fromDate=" + fromDateWeek + "&toDate=" + toDate;
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
                String thisWeekUrl2 = GLOBAL_URL + "session/customer/all?userId="
                        + userid + "&fromDate=" + fromDateWeek2 + "&toDate=" + toDate2;

                String  weekStatUrl2 = GLOBAL_URL+"dashboard/customer/"+userid+
                        "/sessionStatistics?fromDate="+fromDateWeek2+"&toDate="+toDate2;

                displayFrom = displayFormatter.print(startofSecndWeek);
                displayTo = displayFormatter.print(secondTo);


                 start_day_month= displaymonth.print(startofSecndWeek);
                 end_day_month= displaymonth.print(secondTo);
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

    public SessionWeekCustomerAdapter(Context context, ArrayList<String> strings) {
        this.context=context;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CustomFontTextView titleCtxv;
        public LinearLayout topLlay;
        public RelativeLayout topRlay;

        public MyViewHolder(View view) {
            super(view);
            topRlay = (RelativeLayout) view.findViewById(R.id.top_rlay);
            topLlay=(LinearLayout) view.findViewById(R.id.top_llay);
            titleCtxv = (CustomFontTextView) view.findViewById(R.id.title_ctxv);
        }
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

    public Boolean isValid(Object object) {
        if (object != null)
            return true;
        return false;

    }
}
