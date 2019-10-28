package com.pacifyr.omhealthcare.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ceino.utilitylibrary.model.pacifyr.MUser;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.activity.TermsOfUseActivity;
import com.pacifyr.omhealthcare.fragment.schedule.AppointmentModel;
import com.pacifyr.omhealthcare.home.ProfileScreenActivity;
import com.pacifyr.omhealthcare.model.ModelResources;
import com.pacifyr.omhealthcare.model.Pacifyr;
import com.pacifyr.omhealthcare.network.NetworkService;
import com.pacifyr.omhealthcare.utils.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ceino on 18/5/18.
 */

public class AppointmentsListAdapter extends RecyclerView.Adapter<AppointmentsListAdapter.ViewHolder> {

    private List<AppointmentModel.Result> mAppointmentList;
    private Context mContext;
    private ScheduleCancelListner mScheduleCancelListner;
    private ReSchedulelListner mReSchedulelListner;

    public AppointmentsListAdapter(List<AppointmentModel.Result> mAppointmentList, Context mContext, ScheduleCancelListner mScheduleCancelListner, ReSchedulelListner mReSchedulelListner) {
        this.mAppointmentList = mAppointmentList;
        this.mContext = mContext;
        this.mScheduleCancelListner = mScheduleCancelListner;
        this.mReSchedulelListner = mReSchedulelListner;
    }

    public interface ScheduleCancelListner {
        void onScheduleCancel(AppointmentModel.Result mObject, int position);
    }

    public interface ReSchedulelListner {
        void onReshecdule(AppointmentModel.Result mObject, int position);
    }

    @Override
    public AppointmentsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_appointmentlist_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    private AppointmentModel.Result getItem(int position) {
        return mAppointmentList.get(position);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final AppointmentModel.Result mObject = getItem(position);
        if (isValid(mObject)) {
            if (isValid(mObject.getPacifyr())) {
                MUser mPacifyr = mObject.getPacifyr();
                if (isValid(mPacifyr)) {
                    holder.mTextView_PacifyrName.setText(mPacifyr.getFirstName()
                            + " " + mPacifyr.getLastName());

                    if (isValid(mPacifyr.getImage()))
                        Picasso.with(mContext).load(NetworkService.GLOBAL_IMAGE_URL + mPacifyr.getImage())
                                .placeholder(R.drawable.placeholder).into(holder.mCircleImageView_Appointment_pacifyr);

                    if (isValid(mPacifyr.getSpecialities())) {
                        StringBuffer mStringBuffer = new StringBuffer();
                        for (String mSpecialityItem : mPacifyr.getSpecialities()) {
                            mStringBuffer.append(mSpecialityItem + ",");
                        }
                        if (isValid(mStringBuffer))
                            holder.mTextView_PacifyrSpecialities.setText(mStringBuffer);
                    }

                    if (isValid(mObject.getStartTimeGMT())) {
                        String mDisplayDateAndTime = Utility.getDateAndTimeForAppointmentList(mObject.getStartTimeGMT());
                        holder.mTextView_ScheduleTime.setText(mDisplayDateAndTime);
                    }


                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ProfileScreenActivity.start(mContext, mPacifyr.getId(), true);
                        }
                    });
                    if (scheduleIsPassed(mObject.getStartTimeGMT())) {
                        holder.itemView.setClickable(false);
                        holder.mainLayout.setBackgroundColor(mContext.getResources().getColor(R.color.appointment_no_click));
                        holder.mButton_Cancel.setBackground(null);
                        holder.mButton_Reschedule.setBackground(null);

                    } else {
                        holder.itemView.setClickable(true);
                        holder.mainLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                        holder.mButton_Cancel.setBackground(mContext.getResources().getDrawable(R.drawable.blue_border_white_background_round));
                        holder.mButton_Reschedule.setBackground(mContext.getResources().getDrawable(R.drawable.blue_border_white_background_round));
                    }
                }

            }

            holder.mButton_Reschedule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!scheduleIsPassed(mObject.getStartTimeGMT())) {
                        mReSchedulelListner.onReshecdule(mObject, position);
                    }


                }
            });

            holder.mButton_Cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!scheduleIsPassed(mObject.getStartTimeGMT())) {
                        mScheduleCancelListner.onScheduleCancel(mObject, position);
                    }

                }
            });

        }



    }

    @Override
    public int getItemCount() {
        return mAppointmentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView mCircleImageView_Appointment_pacifyr;
        private TextView mTextView_PacifyrName;
        private TextView mTextView_PacifyrSpecialities;
        private TextView mTextView_ScheduleTime;
        private Button mButton_Reschedule;
        private Button mButton_Cancel;
        private LinearLayout mainLayout;


        public ViewHolder(View itemView) {
            super(itemView);
            mCircleImageView_Appointment_pacifyr = itemView.findViewById(R.id.mCircleImageView_Appointment_pacifyr);
            mTextView_PacifyrName = itemView.findViewById(R.id.mTextView_PacifyrName);
            mTextView_PacifyrSpecialities = itemView.findViewById(R.id.mTextView_PacifyrSpecialities);
            mTextView_ScheduleTime = itemView.findViewById(R.id.mTextView_ScheduleTime);
            mButton_Reschedule = itemView.findViewById(R.id.mButton_Reschedule);
            mButton_Cancel = itemView.findViewById(R.id.mButton_Cancel);
            mainLayout = itemView.findViewById(R.id.mainLayout);

        }
    }


    public Boolean isValid(List list) {
        if (list != null)
            return list.size() > 0;
        return false;

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


    public boolean scheduleIsPassed(String date) {
        long mcurrentMilliSeconds = Utility.getCurrentDateinMilliSeconds();
        long mServerMilliSeconds = Utility.converToMilliSecondsFormatb(date);

        if (mcurrentMilliSeconds >= mServerMilliSeconds)
            return true;
        else
            return false;

    }

}
