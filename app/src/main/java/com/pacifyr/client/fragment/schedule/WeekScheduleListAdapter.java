package com.pacifyr.client.fragment.schedule;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pacifyr.client.R;
import com.pacifyr.client.utils.Utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class WeekScheduleListAdapter extends RecyclerView.Adapter<WeekScheduleListAdapter.MyViewHolder>{

        private List<ScheduleData> weekScheduleData;
        private Context context;
        private boolean isDateIsPassed;


    private ListAdapterListener mListener;

    public interface ListAdapterListener { // create an interface
        void onDateClick(int position); // create callback function
    }





    public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView schedule_day,schedule_date;


            public MyViewHolder(View view) {
                super(view);

                schedule_date= view.findViewById(R.id.schedule_date);
                schedule_day=view.findViewById(R.id.schedule_day);
            }
        }




        public WeekScheduleListAdapter(List<ScheduleData> weekScheduleData, Context context, boolean isDateIsPassed,ListAdapterListener mListener) {
            this.weekScheduleData = weekScheduleData;
            this.context=context;
            this.isDateIsPassed=isDateIsPassed;
            this.mListener = mListener;


        }

    public void setDefaultFirstWeekSelection() {

        long mServerMilliSeconds = 0;
        long mcurrentMilliSeconds = Utility.getCurrentDateinMilliSeconds();

        ArrayList<Long> dates=new ArrayList<Long>();
        for (int i=0;i<weekScheduleData.size();i++)
        {
            mServerMilliSeconds = Utility.getLocalTimeFromUtc(weekScheduleData.get(i).getDate());
            dates.add(mServerMilliSeconds);
        }
        int index=0;
        long distance = Math.abs(dates.get(0) - mcurrentMilliSeconds);
        for(int c = 1; c < dates.size(); c++){
            long cdistance = Math.abs(dates.get(c) - mcurrentMilliSeconds);
            if(cdistance < distance){
                index = c;
                distance = cdistance;
            }
        }
        weekScheduleData.get(index).setSetselected(true);
        mListener.onDateClick(index);

    }

    @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_default_shedule_list, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

            ScheduleData object=weekScheduleData.get(position);
            if (isValid(object))
            {
                if (object.isSetselected())
                {
                    holder.schedule_date.setBackgroundResource(R.drawable.blue_cirlce);
                }
                else {
                    holder.schedule_date.setBackgroundResource(R.color.white);
                }
                if (isValid(object.getDay()))
                {
                    holder.schedule_day.setText(object.getDay());

                }

                if (isValid(object.getDate()))
                {

                    holder.schedule_date.setText(getDateNumber(object.getDate()));
                }

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       if ( object.isSetselected())
                       {
                           object.setSetselected(false);
                       }
                       else
                       {
                           object.setSetselected(true);
                           checkLististhereAnySelection(object);
                       }
                     notifyDataSetChanged();

                    }
                });


                if (object.isSetselected())
                {
                    mListener.onDateClick(position);
                }


            }

        }

    private void checkLististhereAnySelection(ScheduleData objectOrginal) {

            if (isValid(weekScheduleData))
            {
                for (ScheduleData data:weekScheduleData)
                {
                    if (!objectOrginal.equals(data))
                    {
                        data.setSetselected(false);
                    }

                }
            }
    }


    private String convertTime(String time) {

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm a");
        Date dt = null;
        try {
            dt = sdf.parse(time+":00");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return sdfs.format(dt);
    }


    private String getCurrentDay()
    {
        Calendar calendar = Calendar.getInstance();

        Date date = calendar.getTime();

        return new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());
    }


    public Boolean isValid(List list)
    {
        if(list!=null)
            return list.size() > 0;
        return  false;

    }

    public Boolean isValid(List list, int position)
    {
        if(isValid(list))
            return list.size() >= position;
        return  false;

    }

    public Boolean isValid(Object object)
    {
        return object != null;

    }

    public Boolean isValid(String text) {
        if (text != null) return !text.trim().equalsIgnoreCase("");
        return false;

    }
        @Override
        public int getItemCount() {



            return weekScheduleData.size();
        }


    public String getDateNumber(String date)
    {

        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");

            Date d = null;
            try
            {
                d = input.parse(date);
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
            String formatted = output.format(d);
            Log.w("DATE", "" + formatted);

        String[] str = formatted.split("/");

        return str[0];

    }


    }

