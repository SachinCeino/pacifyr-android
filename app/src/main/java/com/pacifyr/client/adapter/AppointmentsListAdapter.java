package com.pacifyr.client.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.pacifyr.client.activity.AppointmentDetailsActivity;
import com.pacifyr.client.base.PacifyrActivity;
import com.pacifyr.client.fragment.schedule.AppointmentModel;
import com.pacifyr.client.home.ProfileScreenActivity;
import com.pacifyr.client.netconnectionlistener.ConnectionCheckBroadcast;
import com.pacifyr.client.network.NetworkService;
import com.pacifyr.client.utils.Utility;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MSessionDetailRoot;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MUser;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**

 */

public class AppointmentsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder rcv=null;
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_appointmentlist_item, parent, false);

        if (viewType==1) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event_empty, parent, false);
            rcv = new ViewHolderEmpty(itemView);
        }
        else
        {
            rcv = new MyViewHolder(itemView);
        }

        return rcv;
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
        int ViewType = 0;
        if (mAppointmentList.get(position).getPacifyr()==null)
        {
            ViewType =1;
        }
        return ViewType;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        if (holder instanceof MyViewHolder) {
            MyViewHolder mMyViewHolder = (MyViewHolder) holder;
            final AppointmentModel.Result mObject = getItem(position);
            setMyViewHolder(mMyViewHolder,mObject,position);

        }else if (holder instanceof ViewHolderEmpty) {
            ViewHolderEmpty mViewHolderEmpty = (ViewHolderEmpty) holder;
            setmViewHolderEmpty(mViewHolderEmpty);
        }





    }

    private void setmViewHolderEmpty(ViewHolderEmpty mViewHolderEmpty) {
        mViewHolderEmpty.rootLayout.setBackgroundColor(mContext.getResources().getColor(R.color.appointment_no_click));
    }

    private void setMyViewHolder(MyViewHolder holder, AppointmentModel.Result mObject, int position) {

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
                            holder.ImageView_PrevistNote.setVisibility(View.VISIBLE);
                            if (mObject.getRejected())
                            {
                                holder.mButton_Cancel.setVisibility(View.GONE);
                                holder.mButton_RejestReason.setVisibility(View.VISIBLE);
                                holder.call_layout.setVisibility(View.GONE);
                                holder.mainLayout.setBackgroundColor(mContext.getResources().getColor(R.color.appointment_no_click));
                            }
                            else if(isValid(mObject.getCancelled()) && mObject.getCancelled()==true)
                            {
                                //cancelled
                                holder.mButton_Cancel.setVisibility(View.GONE);
                                holder.mButton_Cancel.setText("Cancelled");
                                holder.TextView_cancelled.setVisibility(View.VISIBLE);
                                holder.ImageView_PrevistNote.setVisibility(View.GONE);
                                holder.accepted_textView.setVisibility(View.GONE);
                                if (mObject.getStatusText().toString().equals(mContext.getResources().getString(R.string.cancelled_provider_not_respond)))
                                {
                                    holder.TextView_cancelled.setText("");
                                    holder.mTextView_auto_cancel_status.setText(mContext.getResources().getString(R.string.cancelled_provider_message));
                                    holder.mTextView_auto_cancel_status.setVisibility(View.VISIBLE);
                                }
                                if (mObject.getStatusText().toString().equals(mContext.getResources().getString(R.string.cancelled_provider_not_respond_two)))
                                {
                                    holder.TextView_cancelled.setText("");
                                    holder.mTextView_auto_cancel_status.setText(mContext.getResources().getString(R.string.cancelled_provider_message_two));
                                    holder.mTextView_auto_cancel_status.setVisibility(View.VISIBLE);
                                }

                            }
                            else if (isValid(mObject.getConfirmed())&&mObject.getConfirmed()
                                    && isValid(mObject.getCompleted())&&mObject.getCompleted())
                            {
                                holder.call_layout.setVisibility(View.GONE);
                                holder.ImageView_PrevistNote.setVisibility(View.GONE);
                                holder.mTextView_Completed.setVisibility(View.VISIBLE);
                                holder.accepted_textView.setVisibility(View.GONE);
                            }
                            else
                            {
                                holder.mButton_Cancel.setVisibility(View.GONE);
                                holder.call_layout.setVisibility(View.VISIBLE);

                            }


                            if(isValid(mObject.getRejected()) && mObject.getRejected())
                            {
                                holder.accepted_textView.setVisibility(View.GONE);
                            }
                            else
                            {
                                holder.accepted_textView.setVisibility(View.VISIBLE);
                                if (isValid(mObject.getCancelled()) && mObject.getCancelled()==true)
                                    holder.accepted_textView.setVisibility(View.GONE);
                                if (isValid(mObject.getConfirmed())&&mObject.getConfirmed()
                                        && isValid(mObject.getCompleted())&&mObject.getCompleted())
                                {
                                    holder.call_layout.setVisibility(View.GONE);
                                    holder.ImageView_PrevistNote.setVisibility(View.GONE);
                                    holder.mTextView_Completed.setVisibility(View.VISIBLE);
                                    holder.accepted_textView.setVisibility(View.GONE);
                                }
                            }

                            holder.videocall_rlay.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (!ConnectionCheckBroadcast.CheckInternetConnection(mContext)) {
                                        Toast.makeText(mContext, "No Internet available", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    ((PacifyrActivity)mContext).startSessionWebforAppoinmetCall(mObject.getPacifyr(), true,mObject.getId());
                                }
                            });

                            holder.audiocall_rlay.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (!ConnectionCheckBroadcast.CheckInternetConnection(mContext)) {
                                        Toast.makeText(mContext, "No Internet available", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    ((PacifyrActivity)mContext).startSessionWebforAppoinmetCall(mObject.getPacifyr(), false,mObject.getId());

                                }
                            });

                            holder.chat_rlay.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (!ConnectionCheckBroadcast.CheckInternetConnection(mContext)) {
                                        Toast.makeText(mContext, "No Internet available", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
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
                                if (mObject.getConfirmed()!=null && mObject.getConfirmed())
                                {
                                    holder.ImageView_PrevistNote.setVisibility(View.VISIBLE);
                                    if (isValid(mObject.getConfirmed())&&mObject.getConfirmed()
                                            && isValid(mObject.getCompleted())&&mObject.getCompleted())
                                    {
                                        holder.ImageView_PrevistNote.setVisibility(View.GONE);
                                    }

                                }
                                else if (mObject.getCancelled()!=null && mObject.getCancelled())
                                {
                                    /*if (mObject.getStatusText().toString().equals(mContext.getResources().getString(R.string.cancelled_provider_not_respond)))
                                    {
                                        holder.TextView_cancelled.setText("");
                                        holder.TextView_cancelled.setVisibility(View.VISIBLE);
                                        holder.mTextView_auto_cancel_status.setText(mContext.getResources().getString(R.string.cancelled_provider_message));
                                        holder.mTextView_auto_cancel_status.setVisibility(View.VISIBLE);
                                    }*/
                                }
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



            if (isValid(mObject.getCancelled())&& mObject.getCancelled())
            {
                holder.mButton_Cancel.setText("Cancelled");
                holder.mButton_Cancel.setVisibility(View.GONE);
                holder.TextView_cancelled.setVisibility(View.VISIBLE);
                holder.mainLayout.setBackgroundColor(mContext.getResources().getColor(R.color.appointment_no_click));
                holder.ImageView_PrevistNote.setVisibility(View.GONE);

                if (mObject.getStatusText().toString().equals(mContext.getResources().getString(R.string.cancelled_provider_not_respond)))
                {
                    holder.TextView_cancelled.setText("");
                    holder.mTextView_auto_cancel_status.setText(mContext.getResources().getString(R.string.cancelled_provider_message));
                    holder.mTextView_auto_cancel_status.setVisibility(View.VISIBLE);
                }
                if (mObject.getStatusText().toString().equals(mContext.getResources().getString(R.string.cancelled_provider_not_respond_two)))
                {
                    holder.TextView_cancelled.setText("");
                    holder.mTextView_auto_cancel_status.setText(mContext.getResources().getString(R.string.cancelled_provider_message_two));
                    holder.mTextView_auto_cancel_status.setVisibility(View.VISIBLE);
                }

            }
            else if (isValid(mObject.getRejected())&&mObject.getRejected())
            {
                holder.mButton_Cancel.setText("Rejected");
                holder.mButton_Cancel.setVisibility(View.GONE);
                holder.mButton_RejestReason.setVisibility(View.VISIBLE);
                holder.ImageView_PrevistNote.setVisibility(View.GONE);
                holder.mainLayout.setBackgroundColor(mContext.getResources().getColor(R.color.appointment_no_click));

            }
            else if (isValid(mObject.getConfirmed())&&mObject.getConfirmed()
                    && isValid(mObject.getCompleted())&&mObject.getCompleted())
            {
                //complted
                holder.mTextView_Completed.setVisibility(View.VISIBLE);
                holder.mButton_RejestReason.setVisibility(View.GONE);
                holder.mButton_Cancel.setVisibility(View.GONE);

            }

            else
            {

                if (isValid(mObject.getConfirmed()) && mObject.getConfirmed()==true)
                {

                    if (!scheduleIsFullyPassed(mObject.getStartTimeGMT()))
                    {
                        holder.ImageView_PrevistNote.setVisibility(View.VISIBLE);
                        holder.accepted_textView.setVisibility(View.VISIBLE);
                        if (holder.call_layout.getVisibility() != View.VISIBLE)
                            holder.mButton_Cancel.setVisibility(View.VISIBLE);
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


            holder.ImageView_PrevistNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isValid(mObject.getPreVisitNote()))
                        showPreVistNotPopUp(mObject.getPreVisitNote());
                }
            });


            holder.mTextView_Completed.setOnTouchListener(new View.OnTouchListener() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        int[] textLocation = new int[2];
                        holder.mTextView_Completed.getLocationOnScreen(textLocation);

                        if (event.getRawX() <=  textLocation[0] + holder.mTextView_Completed.getTotalPaddingLeft()) {
                            if (isValid(mObject))
                                showPreVistNotPopUp(mObject.getPreVisitNote());
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
                            if (isValid(mObject.getCancelReason())){
                                showCancelledNotPopUp(mObject.getCancelReason());
                            }

                            else{
                                if (mObject.getStatusText().toString().equals(mContext.getResources().getString(R.string.cancelled_provider_not_respond)))
                                {
                                    showCancelledNotPopUp(mContext.getResources().getString(R.string.cancelled_provider_message));
                                }
                                else if(mObject.getStatusText().toString().equals(mContext.getResources().getString(R.string.cancelled_provider_not_respond_two)))
                                {
                                    showCancelledNotPopUp(mContext.getResources().getString(R.string.cancelled_provider_message_two));
                                }
                                else
                                {
                                    showCancelledNotPopUp(mContext.getResources().getString(R.string.Cancelled_since_you_did_not_call_the_provider));
                                }

                            }

                            return true;
                        }

                    }
                    return true;
                }
            });


            holder.mButton_RejestReason.setOnTouchListener(new View.OnTouchListener() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        int[] textLocation = new int[2];
                        holder.mButton_RejestReason.getLocationOnScreen(textLocation);

                        if (event.getRawX() <=  textLocation[0] + holder.mButton_RejestReason.getTotalPaddingLeft()) {
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


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent=new Intent(mContext, AppointmentDetailsActivity.class);
                    intent.putExtra("object",mObject);
                    mContext.startActivity(intent);
                }
            });

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

    private void showCompleteNotPopUp(AppointmentModel.Result mObject) {
        View myView = LayoutInflater.from(mContext).inflate(R.layout.reasons_and_notes_popup, null);
        final TextView title = myView.findViewById(R.id.title);
        final TextView message_edt = myView.findViewById(R.id.message_edt);
        final LinearLayout close_layout = myView.findViewById(R.id.close_layout);
        StringBuffer stringBuffer=new StringBuffer();
        if (isValid(mObject.getVisitNoteInternal()) && !mObject.getVisitNoteInternal().equals(""))
        {
            stringBuffer.append("Internal visit notes : "+mObject.getVisitNoteInternal()+"\n\n");
        }

        if (isValid(mObject.getVisitNote()) && !mObject.getVisitNote().equals(""))
        {
            stringBuffer.append("Visit notes : "+mObject.getVisitNote());
        }
        title.setText("Notes");
        message_edt.setText(stringBuffer);
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
        title.setText(mContext.getResources().getString(R.string.reason_for_rejection));
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

        public class ViewHolderEmpty extends RecyclerView.ViewHolder {
            private RelativeLayout rootLayout;
            public ViewHolderEmpty(View view) {
                super(view);
                rootLayout = view.findViewById(R.id.rootLayout);
            }
        }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView mCircleImageView_Appointment_pacifyr;
        private TextView mTextView_PacifyrName;
        private TextView mTextView_PacifyrSpecialities;
        private TextView mTextView_ScheduleTime;
        private TextView mButton_RejestReason,mTextView_warning_Confirm;
        private Button mButton_Cancel;
        private ImageView ImageView_PrevistNote;
        private LinearLayout mainLayout,call_layout;
        private RelativeLayout videocall_rlay,audiocall_rlay,chat_rlay;
        private TextView accepted_textView,TextView_cancelled,mTextView_Completed;
        private TextView mTextView_auto_cancel_status;


        public MyViewHolder(View itemView) {
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
            TextView_cancelled = itemView.findViewById(R.id.TextView_cancelled);
            mTextView_Completed = itemView.findViewById(R.id.mTextView_Completed);
            mTextView_auto_cancel_status= itemView.findViewById(R.id.mTextView_auto_cancel_status);

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
