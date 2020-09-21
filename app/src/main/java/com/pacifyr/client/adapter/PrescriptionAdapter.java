package com.pacifyr.client.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.krishna.fileloader.FileLoader;
import com.krishna.fileloader.listener.FileRequestListener;
import com.krishna.fileloader.pojo.FileResponse;
import com.krishna.fileloader.request.FileLoadRequest;
import com.pacifyr.client.R;
import com.pacifyr.client.activity.PrescriptionViewActivity;
import com.pacifyr.client.base.PacifyrActivity;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.fragment.schedule.AppointmentModel;
import com.pacifyr.client.home.LastSessionActivity;
import com.pacifyr.client.home.SessionSummaryActivity;
import com.pacifyr.client.model.MComments;
import com.pacifyr.client.model.PrescriptionList;
import com.pacifyr.client.model.interfaces.CommentClick;
import com.pacifyr.client.network.NetworkService;
import com.pacifyr.client.utility.PdfViewer;
import com.pacifyr.client.utils.Utility;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.GLOBAL_IMAGE_URL;

/**
 * Created by ceino on 14/12/17.
 */

public class PrescriptionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    List<PrescriptionList.Result> list;
    Context context;
    public PrescriptionAdapter(List<PrescriptionList.Result> list, Context context) {
        this.list=list;
        this.context=context;
    }

    @Override
    public int getItemViewType(int position) {
        int ViewType = 0;
        if (list.get(position).getPacifyr()==null)
        {
            ViewType =1;
        }
        return ViewType;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder rcv=null;
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_prescriptions, parent, false);

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
    public PrescriptionList.Result getItem (int position) {
        PrescriptionList.Result results = null;
        if (isValid(list, position))
            results = list.get(position);
        return results;
    }
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof MyViewHolder) {
            MyViewHolder mMyViewHolder = (MyViewHolder) holder;
            final PrescriptionList.Result object=getItem(position);
           setMyViewHolder(mMyViewHolder,object,position);

        }else if (holder instanceof ViewHolderEmpty) {
            ViewHolderEmpty mViewHolderEmpty = (ViewHolderEmpty) holder;
            setmViewHolderEmpty(mViewHolderEmpty);
        }

    }

    private void setmViewHolderEmpty(ViewHolderEmpty mViewHolderEmpty) {
       // mViewHolderEmpty.rootLayout.setBackgroundColor(context.getResources().getColor(R.color.appointment_no_click));
    }

    public class ViewHolderEmpty extends RecyclerView.ViewHolder {
        private RelativeLayout rootLayout;
        public ViewHolderEmpty(View view) {
            super(view);
            rootLayout = view.findViewById(R.id.rootLayout);
        }
    }
    private void setMyViewHolder(MyViewHolder holder, PrescriptionList.Result object, int position) {
        if(isValid(object)) {

            if (isValid(object.getPacifyr().getImage()))
            {
                if (isValid(object.getPacifyr().getImage()))
                    Picasso.with(context).load(NetworkService.GLOBAL_IMAGE_URL + object.getPacifyr().getImage())
                            .placeholder(R.drawable.placeholder).into(holder.mCircleImageView_Appointment_pacifyr);

                if (isValid(object.getPacifyr()))
                    holder.mTextView_PacifyrName.setText(object.getPacifyr().getFirstName()
                            + " " + object.getPacifyr().getLastName());

                if (isValid(object.getCreatedAt())) {
                  /*  String mDisplayDateAndTime = Utility.getDateAndTimeForAppointmentList(object.getCreatedAt());
                    holder.mTextView_ScheduleTime.setText(mDisplayDateAndTime);*/
                    String t =object.getCreatedAt();
                    String time = Utility.getStartFormatDate(t,context);
                    holder.mTextView_ScheduleTime.setText(time);
                }

                holder.mTextView_View_pres.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (isValid(object.getPrescriptionFile()))
                            ((PacifyrActivity)context).loadPDF(object.getPrescriptionFile());
                        else
                            Toast.makeText(context, context.getResources().getString(R.string.no_prescription), Toast.LENGTH_SHORT).show();

                    }
                });


            }
        }

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
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MainTextView mTextView_PacifyrName,mTextView_ScheduleTime,mTextView_View_pres;
        public CircleImageView mCircleImageView_Appointment_pacifyr;

        public MyViewHolder(View view) {
            super(view);
            mTextView_PacifyrName = view.findViewById(R.id.mTextView_PacifyrName);
            mTextView_ScheduleTime = view.findViewById(R.id.mTextView_ScheduleTime);
            mCircleImageView_Appointment_pacifyr = view.findViewById(R.id.mCircleImageView_Appointment_pacifyr);
            mTextView_View_pres = view.findViewById(R.id.mTextView_View_pres);
        }
    }
}
