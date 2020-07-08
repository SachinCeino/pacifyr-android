package com.pacifyr.client.fragment.schedule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.pacifyr.client.customview.MainTextView;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.pacifyr.client.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class ScheduleTimeSlotesAdapter extends RecyclerView.Adapter<ScheduleTimeSlotesAdapter.MyViewHolder> {

    private List<Slot> mSlotesData;
    private Context context;
    private boolean Available;
    private boolean isDateIsPassed;
    PrefManager pref;
    private SloteBookedListener mListener;


    public interface SloteBookedListener { // create an interface
        void onSloteBooked(int position); // create callback function
    }


    public Boolean isValid(List list) {
        if (list != null)
            return list.size() > 0;
        return false;

    }


    public ScheduleTimeSlotesAdapter(Context context, List<Slot> mSlotesData, boolean isDateIsPassed, SloteBookedListener mListener) {
        this.mSlotesData = mSlotesData;
        // this.Available=Available;
        this.context = context;
        this.isDateIsPassed = isDateIsPassed;
        this.mListener = mListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_slotes_list, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        pref = PrefManager.getInstance(context);
        Slot object = mSlotesData.get(position);
        if (isValid(object)) {

            if (isValid(object.getStartTime()) && isValid(object.getEndTime())) {
                String S_time = convertTime(object.getStartTime());
                String E_time = convertTime(object.getEndTime());

                holder.schedule_timelotes.setText(S_time + "\t-\t " + E_time);

            }


            if (object.getBooked()) {
                if (isValid(object.getBookedUserId()) && isValid(pref.getUserID())) {
                    if (object.getBookedUserId().equals(pref.getUserID())) {
                        holder.itemView.setBackgroundResource(R.drawable.blue_background_white_border);
                        if (!object.isHideSlote()) {
                            holder.img_cancel_slot.setVisibility(View.GONE);
                        }

                    } else {
                        holder.itemView.setBackgroundResource(R.drawable.blue_background_white_border);
                        holder.img_cancel_slot.setVisibility(View.GONE);
                        holder.itemView.setAlpha((float) 0.7);


                    }
                }


            } else if (object.isHideSlote()) {
                holder.itemView.setBackgroundResource(R.drawable.grey_border_grey_solid_rectangle_sharp);
                holder.img_cancel_slot.setVisibility(View.GONE);
            } else if (object.isClicked()) {
                holder.itemView.setBackgroundResource(R.drawable.blue_background_white_border);
                holder.itemView.setAlpha((float) 0.5);
            } else {

                holder.itemView.setBackgroundResource(R.drawable.black_border_white_back);
                holder.img_cancel_slot.setVisibility(View.GONE);

            }


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!object.isHideSlote()) {
                        mListener.onSloteBooked(position);
                        for (int i = 0; i < mSlotesData.size(); i++) {
                            mSlotesData.get(i).clicked = false;
                        }
                        object.clicked = true;

                    }


                }
            });


        }

    }

    private String convertTime(String time) {

        if (time.equals("12:00")){
            return "12:00 PM";}
        else if (time.equals("12:30")){
            return "12:30 PM";}
        else {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
            SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm a");
            Date dt = null;
            try {
                dt = sdf.parse(time + ":00");
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return sdfs.format(dt);
        }
    }

    public Boolean isValid(List list, int position) {
        if (isValid(list))
            return list.size() >= position;
        return false;

    }

    public Boolean isValid(Object object) {
        return object != null;

    }

    public Boolean isValid(String text) {
        if (text != null) return !text.trim().equalsIgnoreCase("");
        return false;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MainTextView schedule_timelotes;
        private ImageView img_cancel_slot;


        public MyViewHolder(View view) {
            super(view);

            schedule_timelotes = view.findViewById(R.id.schedule_timelotes);
            img_cancel_slot = view.findViewById(R.id.img_cancel_slot);

        }
    }

    @Override
    public int getItemCount() {
        return mSlotesData.size();
    }

}