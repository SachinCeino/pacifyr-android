package com.pacifyr.client.quickblox.chat.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.pacifyr.client.R;
import com.pacifyr.client.activity.AppointmentDetailsActivity;
import com.pacifyr.client.fragment.schedule.AppointmentModel;
import com.pacifyr.client.network.NetworkService;
import com.pacifyr.client.utils.Utility;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MUser;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 *
 */


public class AttachmentPreviewAdapterView extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<AppointmentModel.Result> mAppointmentList;
    private final Context mContext;


    public AttachmentPreviewAdapterView(List<AppointmentModel.Result> mAppointmentList, Context mContext) {
        this.mAppointmentList = mAppointmentList;
        this.mContext = mContext;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder rcv = null;
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_appointmen_cancel_reject_item, parent, false);

        if (viewType == 1) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event_empty, parent, false);
            rcv = new ViewHolderEmpty(itemView);
        } else {
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
        if (mAppointmentList.get(position).getPacifyr() == null) {
            ViewType = 1;
        }
        return ViewType;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        if (holder instanceof MyViewHolder) {
            MyViewHolder mMyViewHolder = (MyViewHolder) holder;
            final AppointmentModel.Result mObject = getItem(position);
            setMyViewHolder(mMyViewHolder, mObject, position);

        } else if (holder instanceof ViewHolderEmpty) {
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
                        String t = mObject.getStartTimeGMT();
                        String time = Utility.getStartFormatDate(t, mContext);
                        holder.mTextView_ScheduleTime.setText(time);
                    }

                    if (isValid(mObject.getCancelled()) && mObject.getCancelled()) {
                        holder.TextView_cancelled.setVisibility(View.VISIBLE);
                        holder.TextView_Rejected.setVisibility(View.GONE);
                        if (isValid(mObject.getStatusText())) {
                            holder.TextView_cancelled.setText(" ");
                            holder.mTextView_auto_cancel_status.setText(mObject.getStatusText());
                            holder.mTextView_auto_cancel_status.setVisibility(View.VISIBLE);
                            holder.TextView_cancelled.setVisibility(View.GONE);
                        }
                        /*if (mObject.getStatusText().toString().equals(mContext.getResources().getString(R.string.cancelled_provider_not_respond)))
                        {
                            holder.TextView_cancelled.setText(" ");
                            holder.mTextView_auto_cancel_status.setText(mContext.getResources().getString(R.string.cancelled_provider_message));
                            holder.mTextView_auto_cancel_status.setVisibility(View.VISIBLE);
                            holder.TextView_cancelled.setVisibility(View.GONE);                }
                        if (mObject.getStatusText().toString().equals(mContext.getResources().getString(R.string.cancelled_provider_not_respond_two)))
                        {
                            holder.TextView_cancelled.setText(" ");
                            holder.mTextView_auto_cancel_status.setText(mContext.getResources().getString(R.string.cancelled_provider_message_two));
                            holder.mTextView_auto_cancel_status.setVisibility(View.VISIBLE);
                            holder.TextView_cancelled.setVisibility(View.GONE);
                        }

                        if (mObject.getStatusText().toString().equals(mContext.getResources().getString(R.string.client_cancelled_the_appointment)))
                        {
                            holder.TextView_cancelled.setText(" ");
                            holder.mTextView_auto_cancel_status.setText(mContext.getResources().getString(R.string.client_cancelled_the_appointment));
                            holder.mTextView_auto_cancel_status.setVisibility(View.VISIBLE);
                            holder.TextView_cancelled.setVisibility(View.GONE);
                        }*/

                    }

                    if (isValid(mObject.getRejected()) && mObject.getRejected()) {
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

                        if (event.getRawX() <= textLocation[0] + holder.TextView_Rejected.getTotalPaddingLeft()) {
                            if (isValid(mObject.getRejectReason())) {
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

                        if (event.getRawX() <= textLocation[0] + holder.TextView_cancelled.getTotalPaddingLeft()) {
                            if (isValid(mObject.getCancelReason())) {
                                showCancelledNotPopUp(mObject.getCancelReason());
                            } else {
                                if (isValid(mObject.getStatusText())) {
                                    showCancelledNotPopUp(mObject.getStatusText());
                                }
                               /* if (mObject.getStatusText().toString().equals(mContext.getResources().getString(R.string.cancelled_provider_not_respond)))
                                {
                                    showCancelledNotPopUp(mContext.getResources().getString(R.string.cancelled_provider_message));
                                }
                                else if (mObject.getStatusText().toString().equals(mContext.getResources().getString(R.string.cancelled_provider_not_respond_two)))
                                {
                                    showCancelledNotPopUp(mContext.getResources().getString(R.string.cancelled_provider_message_two));
                                }
                                else
                                {
                                    showCancelledNotPopUp(mContext.getResources().getString(R.string.Cancelled_since_you_did_not_call_the_provider));
                                }*/

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

                    Intent intent = new Intent(mContext, AppointmentDetailsActivity.class);
                    intent.putExtra("object", mObject);
                    mContext.startActivity(intent);
                }
            });


            holder.mTextView_auto_cancel_status.setOnTouchListener(new View.OnTouchListener() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        int[] textLocation = new int[2];
                        holder.mTextView_auto_cancel_status.getLocationOnScreen(textLocation);

                        if (event.getRawX() <= textLocation[0] + holder.mTextView_auto_cancel_status.getTotalPaddingLeft()) {
                            if (isValid(mObject.getCancelReason())) {
                                showCancelledNotPopUp(mObject.getCancelReason());
                            } else {
                                if (isValid(mObject.getStatusText())) {
                                    showCancelledNotPopUp(mObject.getStatusText());
                                }

                               /* if (mObject.getStatusText().toString().equals(mContext.getResources().getString(R.string.cancelled_provider_not_respond)))
                                {
                                    showCancelledNotPopUp(mContext.getResources().getString(R.string.cancelled_provider_message));
                                }
                                else if (mObject.getStatusText().toString().equals(mContext.getResources().getString(R.string.cancelled_provider_not_respond_two)))
                                {
                                    showCancelledNotPopUp(mContext.getResources().getString(R.string.cancelled_provider_message_two));
                                }
                                else
                                {
                                    showCancelledNotPopUp(mContext.getResources().getString(R.string.Cancelled_since_you_did_not_call_the_provider));
                                }*/

                            }
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
        title.setText(mContext.getResources().getString(R.string.reason_for_rejection));
        message_edt.setText(rejectReason.toString());
        message_edt.setMovementMethod(new ScrollingMovementMethod());
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
        long mcurrentMilliSeconds = 900000 + Utility.getCurrentDateinMilliSeconds();
        long mServerMilliSeconds = Utility.getLocalTimeFromUtc(date);
        return mcurrentMilliSeconds >= mServerMilliSeconds;

    }

    public boolean scheduleIsFullyPassed(String date) {
        //add 15 minuts more
        long mcurrentMilliSeconds = Utility.getCurrentDateinMilliSeconds();
        long mServerMilliSeconds = Utility.getLocalTimeFromUtc(date);
        return mcurrentMilliSeconds >= mServerMilliSeconds;

    }

    public boolean AppointMentCalUIForAppointMentTime(String date) {
        long mcurrentMilliSeconds = Utility.getCurrentDateinMilliSeconds();
        long mServerMilliSeconds = Utility.getLocalTimeFromUtc(date);
        long TimDifference = mcurrentMilliSeconds - mServerMilliSeconds;
        return TimDifference < 1800000;

    }

    private void showCancelledNotPopUp(Object preVisitNote) {
        View myView = LayoutInflater.from(mContext).inflate(R.layout.reasons_and_notes_popup, null);
        final TextView title = myView.findViewById(R.id.title);
        final TextView message_edt = myView.findViewById(R.id.message_edt);
        final LinearLayout close_layout = myView.findViewById(R.id.close_layout);
        title.setText(mContext.getResources().getString(R.string.cancelled_reason));
        message_edt.setText(preVisitNote.toString());
        message_edt.setMovementMethod(new ScrollingMovementMethod());
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

    public class ViewHolderEmpty extends RecyclerView.ViewHolder {
        private final RelativeLayout rootLayout;

        public ViewHolderEmpty(View view) {
            super(view);
            rootLayout = view.findViewById(R.id.rootLayout);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final CircleImageView mCircleImageView_Appointment_pacifyr;
        private final TextView mTextView_PacifyrName;
        private final TextView mTextView_PacifyrSpecialities;
        private final TextView mTextView_ScheduleTime;
        private final TextView TextView_Rejected;
        private final TextView TextView_cancelled;
        private final TextView mTextView_auto_cancel_status;


        public MyViewHolder(View itemView) {
            super(itemView);
            mCircleImageView_Appointment_pacifyr = itemView.findViewById(R.id.mCircleImageView_Appointment_pacifyr);
            mTextView_PacifyrName = itemView.findViewById(R.id.mTextView_PacifyrName);
            mTextView_PacifyrSpecialities = itemView.findViewById(R.id.mTextView_PacifyrSpecialities);
            mTextView_ScheduleTime = itemView.findViewById(R.id.mTextView_ScheduleTime);

            TextView_Rejected = itemView.findViewById(R.id.TextView_Rejected);
            TextView_cancelled = itemView.findViewById(R.id.TextView_cancelled);
            mTextView_auto_cancel_status = itemView.findViewById(R.id.mTextView_auto_cancel_status);

        }
    }


}
