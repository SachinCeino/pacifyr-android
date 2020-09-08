package com.pacifyr.client.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.pacifyr.client.R;
import com.pacifyr.client.base.PacifyrActivity;
import com.pacifyr.client.fragment.schedule.AppointmentModel;
import com.pacifyr.client.network.NetworkService;
import com.pacifyr.client.utils.Utility;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MUser;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ceino on 18/5/18.
 */


public class AppointmentsCancelRejecListAdapter extends RecyclerView.Adapter<AppointmentsCancelRejecListAdapter.ViewHolder> {

    private List<AppointmentModel.Result> mAppointmentList;
    private Context mContext;


    public AppointmentsCancelRejecListAdapter(List<AppointmentModel.Result> mAppointmentList, Context mContext) {
        this.mAppointmentList = mAppointmentList;
        this.mContext = mContext;

    }


    @Override
    public AppointmentsCancelRejecListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_appointmen_cancel_reject_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    private AppointmentModel.Result getItem(int position) {
        return mAppointmentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
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

                    try {
                        if (isValid(mPacifyr.getSpecialities())) {
                            StringBuffer mStringBuffer = new StringBuffer();
                            for (String mSpecialityItem : mPacifyr.getSpecialities()) {
                                mStringBuffer.append(mSpecialityItem + ",");
                            }
                            mStringBuffer.setLength(Math.max(mStringBuffer.length() - 1, 0));
                            if (isValid(mStringBuffer))
                                holder.mTextView_PacifyrSpecialities.setText(mStringBuffer);
                        }

                    } catch (Exception e) {
                        Log.w("ERROR", String.valueOf(e));
                    }

                    if (isValid(mObject.getStartTimeGMT())) {
                       /* String mDisplayDateAndTime = Utility.getDateAndTimeForAppointmentList(mObject.getStartTimeGMT());
                        holder.mTextView_ScheduleTime.setText(mDisplayDateAndTime);*/
                        String t =mObject.getStartTimeGMT();
                        String time = Utility.getStartFormatDate(t,mContext);
                        holder.mTextView_ScheduleTime.setText(time);
                    }

                    if (isValid(mObject.getCancelled()) && mObject.getCancelled())
                    {
                        holder.TextView_cancelled.setVisibility(View.VISIBLE);
                        holder.TextView_Rejected.setVisibility(View.GONE);
                    }

                    if (isValid(mObject.getRejected()) && mObject.getRejected())
                    {
                        holder.TextView_Rejected.setVisibility(View.VISIBLE);
                        holder.TextView_cancelled.setVisibility(View.GONE);
                    }

                }
            }


            holder.TextView_Rejected.setOnTouchListener(new View.OnTouchListener() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        int[] textLocation = new int[2];
                        holder.TextView_Rejected.getLocationOnScreen(textLocation);

                        if (event.getRawX() <=  textLocation[0] + holder.TextView_Rejected.getTotalPaddingLeft()) {
                            if (isValid(mObject.getRejectReason()))
                            {
                                showRejectedReasonPopUp(mObject.getRejectReason());
                            } else {
                                Toast.makeText(mContext, "Not available", Toast.LENGTH_LONG).show();
                            }
                            return true;
                        }

                    }
                    return true;
                }
            });

            holder.TextView_cancelled.setOnTouchListener(new View.OnTouchListener() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        int[] textLocation = new int[2];
                        holder.TextView_cancelled.getLocationOnScreen(textLocation);

                        if (event.getRawX() <=  textLocation[0] + holder.TextView_cancelled.getTotalPaddingLeft()) {
                            if (isValid(mObject.getCancelReason()))
                                showCancelledNotPopUp(mObject.getCancelReason());
                            return true;
                        }

                    }
                    return true;
                }
            });

        }



    }



    private void showRejectedReasonPopUp(Object rejectReason) {
        View myView = LayoutInflater.from(mContext).inflate(R.layout.reasons_and_notes_popup, null);
        final TextView title = myView.findViewById(R.id.title);
        final TextView message_edt = myView.findViewById(R.id.message_edt);
        final LinearLayout close_layout = myView.findViewById(R.id.close_layout);
        title.setText("Reason for rejection");
        message_edt.setText(rejectReason.toString());
        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
        alert.setView(myView);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(false);
        AlertDialog dialog = alert.create();
        dialog.show();
        close_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
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
        private TextView TextView_Rejected,TextView_cancelled;



        public ViewHolder(View itemView) {
            super(itemView);
            mCircleImageView_Appointment_pacifyr = itemView.findViewById(R.id.mCircleImageView_Appointment_pacifyr);
            mTextView_PacifyrName = itemView.findViewById(R.id.mTextView_PacifyrName);
            mTextView_PacifyrSpecialities = itemView.findViewById(R.id.mTextView_PacifyrSpecialities);
            mTextView_ScheduleTime = itemView.findViewById(R.id.mTextView_ScheduleTime);

            TextView_Rejected = itemView.findViewById(R.id.TextView_Rejected);
            TextView_cancelled = itemView.findViewById(R.id.TextView_cancelled);

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
        //add 15 minuts more
        long mcurrentMilliSeconds =900000+ Utility.getCurrentDateinMilliSeconds();
        long mServerMilliSeconds = Utility.getLocalTimeFromUtc(date);
        return mcurrentMilliSeconds >= mServerMilliSeconds;

    }
    public boolean scheduleIsFullyPassed(String date) {
        //add 15 minuts more
        long mcurrentMilliSeconds =Utility.getCurrentDateinMilliSeconds();
        long mServerMilliSeconds = Utility.getLocalTimeFromUtc(date);
        return mcurrentMilliSeconds >= mServerMilliSeconds;

    }
    public boolean AppointMentCalUIForAppointMentTime(String date) {
        long mcurrentMilliSeconds = Utility.getCurrentDateinMilliSeconds();
        long mServerMilliSeconds = Utility.getLocalTimeFromUtc(date);
        long TimDifference=mcurrentMilliSeconds - mServerMilliSeconds;
        if (TimDifference<1800000)
        {
            return true;
        }
        else {
            return false;
        }

    }

    private void showCancelledNotPopUp(Object preVisitNote) {
        View myView = LayoutInflater.from(mContext).inflate(R.layout.reasons_and_notes_popup, null);
        final TextView title = myView.findViewById(R.id.title);
        final TextView message_edt = myView.findViewById(R.id.message_edt);
        final LinearLayout close_layout = myView.findViewById(R.id.close_layout);
        title.setText(mContext.getResources().getString(R.string.cancelled_reason));
        message_edt.setText(preVisitNote.toString());
        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
        alert.setView(myView);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(false);
        AlertDialog dialog = alert.create();
        dialog.show();
        close_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }


}
