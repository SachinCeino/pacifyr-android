package com.pacifyr.client.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.pacifyr.client.home.ProfileScreenActivity;
import com.pacifyr.client.network.NetworkService;
import com.pacifyr.client.utils.Utility;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MUser;
import com.squareup.picasso.Picasso;

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
                            mStringBuffer.setLength(Math.max(mStringBuffer.length()-1,0));
                            if (isValid(mStringBuffer))
                                holder.mTextView_PacifyrSpecialities.setText(mStringBuffer);
                        }

                    }catch (Exception e)
                    {
                        Log.w("ERROR",String.valueOf(e));
                    }

                    if (isValid(mObject.getStartTimeGMT())) {
                        /*String mDisplayDateAndTime = Utility.getDateAndTimeForAppointmentList(mObject.getStartTimeGMT());
                        holder.mTextView_ScheduleTime.setText(mDisplayDateAndTime);*/

                        String t =mObject.getStartTimeGMT();
                        String time = Utility.getStartFormatDate(t,mContext);
                        holder.mTextView_ScheduleTime.setText(time);
                    }


                    if (scheduleIsPassed(mObject.getStartTimeGMT())) {
                        if (AppointMentCalUIForAppointMentTime(mObject.getStartTimeGMT()) == true && mObject.getConfirmed() ==true)
                        {
                            /**show call UI **/
                            if (mObject.getRejected())
                            {
                                holder.mButton_Cancel.setText("Rejected");
                                holder.mButton_RejestReason.setVisibility(View.VISIBLE);
                                holder.call_layout.setVisibility(View.GONE);
                            }
                            else if(isValid(mObject.getCancelled()) && mObject.getCancelled()==true)
                            {
                                //cancelled
                                holder.mButton_Cancel.setVisibility(View.VISIBLE);
                                holder.mButton_Cancel.setText("Cancelled");
                            }
                            else
                            {
                                holder.mButton_Cancel.setVisibility(View.GONE);
                                holder.call_layout.setVisibility(View.VISIBLE);

                            }

                            holder.ImageView_PrevistNote.setVisibility(View.VISIBLE);
                            holder.accepted_textView.setVisibility(View.GONE);
                            holder.ImageView_PrevistNote.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    showPreVistNotPopUp(mObject.getPreVisitNote());
                                }
                            });

                            holder.videocall_rlay.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ((PacifyrActivity)mContext).startSessionWebforAppoinmetCall(mObject.getPacifyr(), true,mObject.getId());
                                }
                            });

                            holder.audiocall_rlay.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ((PacifyrActivity)mContext).startSessionWebforAppoinmetCall(mObject.getPacifyr(), false,mObject.getId());

                                }
                            });

                            holder.chat_rlay.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                               ((PacifyrActivity)mContext).startChat(mObject.getPacifyr());
                                }
                            });

                        }
                        else {
                            if (scheduleIsFullyPassed(mObject.getStartTimeGMT()))
                            {
                                holder.mainLayout.setBackgroundColor(mContext.getResources().getColor(R.color.appointment_no_click));
                                holder.mButton_Cancel.setVisibility(View.GONE);
                                holder.ImageView_PrevistNote.setVisibility(View.GONE);
                                holder.accepted_textView.setVisibility(View.GONE);
                            }
                            else
                            {
                                holder.mainLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                                holder.mButton_Cancel.setBackground(mContext.getResources().getDrawable(R.drawable.blue_border_white_background_round));
                                holder.mButton_Cancel.setVisibility(View.VISIBLE);
                            }

                        }

                    } else {

                        holder.mainLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                        holder.mButton_Cancel.setBackground(mContext.getResources().getDrawable(R.drawable.blue_border_white_background_round));
                        holder.mCircleImageView_Appointment_pacifyr.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //ProfileScreenActivity.start(mContext, mPacifyr.getId(), true);
                            }
                        });
                    }
                }

            }

            holder.mButton_RejestReason.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (isValid(mObject.getRejectReason()))
                    {
                        showRejectedReasonPopUp(mObject.getRejectReason());
                    } else {
                        Toast.makeText(mContext, "Not available", Toast.LENGTH_LONG).show();
                    }
                }
            });

            if (isValid(mObject.getCancelled())&& mObject.getCancelled())
            {
                holder.mButton_Cancel.setText("Cancelled");

            }
            else if (isValid(mObject.getRejected())&&mObject.getRejected())
            {
                holder.mButton_Cancel.setText("Rejected");
                holder.mButton_RejestReason.setVisibility(View.VISIBLE);

            }
            else if (isValid(mObject.getConfirmed())&&mObject.getConfirmed()
            && isValid(mObject.getCompleted())&&mObject.getCompleted())
            {
                holder.mButton_Cancel.setText("Completed");
                holder.mButton_RejestReason.setVisibility(View.GONE);

            }

            else
            {

                if (isValid(mObject.getConfirmed()) && mObject.getConfirmed()==true)
                {
                    holder.mButton_Cancel.setVisibility(View.GONE);
                    if (!scheduleIsFullyPassed(mObject.getStartTimeGMT()))
                    {
                        holder.ImageView_PrevistNote.setVisibility(View.VISIBLE);
                        holder.accepted_textView.setVisibility(View.VISIBLE);
                        holder.ImageView_PrevistNote.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                showPreVistNotPopUp(mObject.getPreVisitNote());
                            }
                        });
                    }

                }
                else
                {
                    holder.mButton_Cancel.setText("Cancel");
                    if (!scheduleIsFullyPassed(mObject.getStartTimeGMT()))
                    {
                        holder.mTextView_warning_Confirm.setVisibility(View.VISIBLE);
                        holder.mTextView_warning_Confirm.setText(mContext.getResources().getString(R.string.waiting_for_confirmation));
                    }
                }
            }
            if (isValid(mObject.getCancelled())&& mObject.getCancelled()
            || isValid(mObject.getRejected())&&mObject.getRejected())
            {
                //do do nothing disable click
            }
            else
            {
                holder.mButton_Cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isValid(mObject.getConfirmed())&&mObject.getConfirmed()
                                && isValid(mObject.getCompleted())&&mObject.getCompleted())
                        {
                            if (isValid(mObject.getVisitNote()))
                            showCompletedPopUp(mObject.getVisitNote());
                        }
                        else if (!scheduleIsFullyPassed(mObject.getStartTimeGMT())) {
                            mScheduleCancelListner.onScheduleCancel(mObject, position);
                        }

                    }
                });
            }


        }



    }

    private void showPreVistNotPopUp(Object preVisitNote) {
        View myView = LayoutInflater.from(mContext).inflate(R.layout.reasons_and_notes_popup, null);
        final TextView title = myView.findViewById(R.id.title);
        final TextView message_edt = myView.findViewById(R.id.message_edt);
        final LinearLayout close_layout = myView.findViewById(R.id.close_layout);
        title.setText("Previsit notes");
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

    private void showCompletedPopUp(Object notes) {
        View myView = LayoutInflater.from(mContext).inflate(R.layout.reasons_and_notes_popup, null);
        final TextView title = myView.findViewById(R.id.title);
        final TextView message_edt = myView.findViewById(R.id.message_edt);
        final LinearLayout close_layout = myView.findViewById(R.id.close_layout);
        title.setText("Visit notes");
        message_edt.setText(notes.toString());
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
        private TextView mButton_RejestReason,mTextView_warning_Confirm;
        private Button mButton_Cancel;
        private ImageView ImageView_PrevistNote;
        private LinearLayout mainLayout,call_layout;
        private RelativeLayout videocall_rlay,audiocall_rlay,chat_rlay;
        private TextView accepted_textView;


        public ViewHolder(View itemView) {
            super(itemView);
            mCircleImageView_Appointment_pacifyr = itemView.findViewById(R.id.mCircleImageView_Appointment_pacifyr);
            mTextView_PacifyrName = itemView.findViewById(R.id.mTextView_PacifyrName);
            mTextView_PacifyrSpecialities = itemView.findViewById(R.id.mTextView_PacifyrSpecialities);
            mTextView_ScheduleTime = itemView.findViewById(R.id.mTextView_ScheduleTime);
            mButton_RejestReason = itemView.findViewById(R.id.mButton_RejestReason);
            mButton_Cancel = itemView.findViewById(R.id.mButton_Cancel);
            ImageView_PrevistNote = itemView.findViewById(R.id.ImageView_PrevistNote);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            call_layout = itemView.findViewById(R.id.call_layout);
            videocall_rlay = itemView.findViewById(R.id.videocall_rlay);
            audiocall_rlay = itemView.findViewById(R.id.audiocall_rlay);
            chat_rlay = itemView.findViewById(R.id.chat_rlay);
            mTextView_warning_Confirm = itemView.findViewById(R.id.mTextView_warning_Confirm);
            accepted_textView = itemView.findViewById(R.id.accepted_textView);

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

}
