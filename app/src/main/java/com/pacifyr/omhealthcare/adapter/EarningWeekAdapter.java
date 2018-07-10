package com.pacifyr.omhealthcare.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ceino.utilitylibrary.CustomFontTextView;
import com.ceino.utilitylibrary.model.pacifyr.ModelWeekDetail;
import com.ceino.utilitylibrary.utils.PrefManager;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.event.BusProvider;
import com.pacifyr.omhealthcare.event.WeekEarningsClickEvent;
import com.pacifyr.omhealthcare.network.NetworkService;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by ceino on 25/1/18.
 */

public class EarningWeekAdapter extends RecyclerView.Adapter<EarningWeekAdapter.MyViewHolder>{
//http://testapp.pacifyr.net:8080/pacifyr/v1/earnings?pacifyrId=594cdac0dc224d0f482f2842&fromDate=2018-01-28&toDate=2018-01-31
    public String token;
    public String userid;
  /*  public int skip=0;
    public int limit=10;*/
    public PrefManager prefM;
    private ArrayList<String > data;
    private ArrayList<ModelWeekDetail> modelWeekDetailList=new ArrayList<ModelWeekDetail>();
    Context context;
    ModelWeekDetail modelWeekDetail;

    public EarningWeekAdapter(Context context) {
        this.context=context;
        data = new ArrayList<>();
        modelWeekDetail = new ModelWeekDetail();
 /*       skip=0;
        limit=10;*/
        createWeekList();
    }
    private void createWeekList() {
        try {
            modelWeekDetail = new ModelWeekDetail();
            LocalDate now1 = new LocalDate();
            LocalDate backNow = now1.minusDays(7);
            LocalDate startofWeek = backNow.withDayOfWeek(DateTimeConstants.SUNDAY);
            LocalDate now = now1.withDayOfWeek(DateTimeConstants.SATURDAY);
            LocalDate startofWeek_sun = startofWeek.minusDays(1);

            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");

            DateTimeFormatter displayFormatter = DateTimeFormat.forPattern("MMM dd");

            DateTimeFormatter displaymonth = DateTimeFormat.forPattern("MMM");
            DateTimeFormatter displayFormatterdateonly = DateTimeFormat.forPattern("- dd");
            String fromDateWeek = formatter.print(startofWeek_sun);

            String displayFrom = displayFormatter.print(startofWeek_sun);
            String displayTo = displayFormatter.print(now);


            String start_day_month= displaymonth.print(startofWeek);
            String end_day_month= displaymonth.print(now);
            displayFrom = displayFormatter.print(startofWeek_sun);
            if(start_day_month.equalsIgnoreCase(end_day_month))
                displayTo = displayFormatterdateonly.print(now);
            else
                displayTo = displayFormatter.print(now);


            String toDate = formatter.print(now);

            prefM = PrefManager.getInstance(context);
            token = prefM.getAccessToken();
            userid = prefM.getUserID();
           // String thisWeekUrl = GLOBAL_URL + "session/customer/all?userId=" + userid + "&skip=" + skip + "&limit=" + limit + "&fromDate=" + fromDateWeek + "&toDate=" + toDate;
            String thisWeekUrl= NetworkService.GLOBAL_URL+"earnings?pacifyrId="+userid+"&fromDate="+fromDateWeek+"&toDate="+toDate;

            modelWeekDetail.setFromDate(fromDateWeek);
            modelWeekDetail.setToDate(toDate);
            modelWeekDetail.setData(thisWeekUrl);
            modelWeekDetail.setFromDateDisplay(displayFrom);
            modelWeekDetail.setToDateDisplay(displayTo);

            modelWeekDetailList.add(modelWeekDetail);
            data.add(thisWeekUrl);

            LocalDate startofSecndWeek = startofWeek_sun.minusDays(7);
            LocalDate secondTo = startofWeek_sun.minusDays(1);

            for(int i=0;i<50;i++)
            {
                modelWeekDetail= new ModelWeekDetail();
                //startofSecndWeek = startofWeek_sun.minusDays(7);
                //secondTo = startofWeek_sun.minusDays(1);

                String fromDateWeek2 = formatter.print(startofSecndWeek);
                String toDate2 = formatter.print(secondTo);
            /*    String thisWeekUrl2 = GLOBAL_URL + "session/customer/all?userId="
                        + userid + "&skip=" + skip + "&limit=" + limit + "&fromDate="
                        + fromDateWeek2 + "&toDate=" + toDate2;*/

                String thisWeekUrl2= NetworkService.GLOBAL_URL+"earnings?pacifyrId="+userid+"&fromDate="+fromDateWeek2+"&toDate="+toDate2;
                displayFrom = displayFormatter.print(startofSecndWeek);
                displayTo = displayFormatter.print(secondTo);


                 start_day_month= displaymonth.print(startofSecndWeek);
                 end_day_month= displaymonth.print(secondTo);
                displayFrom = displayFormatter.print(startofSecndWeek);
                if(start_day_month.equalsIgnoreCase(end_day_month))
                    displayTo = displayFormatterdateonly.print(secondTo);
                else
                    displayTo = displayFormatter.print(secondTo);

                modelWeekDetail.setFromDateDisplay(displayFrom);
                modelWeekDetail.setToDateDisplay(displayTo);
                modelWeekDetail.setFromDate(fromDateWeek2);
                modelWeekDetail.setToDate(toDate2);
                modelWeekDetail.setData(thisWeekUrl2);
                modelWeekDetailList.add(modelWeekDetail);
                data.add(thisWeekUrl2);
                secondTo = startofSecndWeek.minusDays(1);
                startofSecndWeek = startofSecndWeek.minusDays(7);

            }
            modelWeekDetailList.get(0).setSelect(true);
            Collections.reverse(modelWeekDetailList);
            int mm=modelWeekDetailList.size();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

    public EarningWeekAdapter(Context context, ArrayList<String> strings) {
        this.data = strings;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_sort_date, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        if (modelWeekDetailList.get(position).isSelect()){
            holder.topRlay.setBackground(context.getResources().getDrawable(R.drawable.blue_line_bottom));
            holder.titleCtxv.setTextColor(context.getResources().getColor(R.color.blue_pacifyr));
        }
        else{
            holder.topRlay.setBackground(null);
            holder.titleCtxv.setTextColor(context.getResources().getColor(R.color.black));
        }

        holder.titleCtxv.setText(modelWeekDetailList.get(position).getFromDateDisplay() +" "+modelWeekDetailList.get(position).getToDateDisplay());
        holder.topLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = modelWeekDetailList.get(position).getData();
                postEventOnMainThread(new WeekEarningsClickEvent(url));
                if(!modelWeekDetailList.get(position).isSelect()){
                   // modelWeekDetailList.get(position).setSelect(true);
                    for(int i=0;i<modelWeekDetailList.size();i++){
                        if(i==position)
                        {
                            modelWeekDetailList.get(position).setSelect(true);
                        }
                       else{
                            modelWeekDetailList.get(i).setSelect(false);
                        }
                    }
                }
                notifyDataSetChanged();
                //android:background="@drawable/blue_line_bottom"

            }
        });
    }

    @Override
    public int getItemCount() {
        return modelWeekDetailList.size();
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
}
