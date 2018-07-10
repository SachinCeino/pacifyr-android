package com.pacifyr.omhealthcare.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ceino.utilitylibrary.CustomFontTextView;
import com.ceino.utilitylibrary.model.pacifyr.Education;
import com.ceino.utilitylibrary.model.pacifyr.ModelUserProfile;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.model.ModelPacifyrEducation;

import java.util.ArrayList;

/**
 * Created by ceino on 17/4/18.
 */

public class ListEducationAdapter  extends RecyclerView.Adapter<ListEducationAdapter.EducationViewHolder> {

    ArrayList<Education> edList;
    Context mContext;

    public ListEducationAdapter(ArrayList<Education> edList, Context context) {
        this.edList = edList;
        mContext = context;
    }

   /* public ListEducationAdapter(ArrayList<ModelPacifyrEducation> edList, Context context) {
        this.edList = edList;
        mContext = context;
    }*/
    @Override
    public EducationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_add_pacifyr_education, parent, false);
        EducationViewHolder viewHolder = new EducationViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EducationViewHolder holder, int position) {

        holder.title.setText(edList.get(position).getTitle()+"  "+edList.get(position).getLocation());
        holder.duration.setText(edList.get(position).getDuration());

    }

    @Override
    public int getItemCount() {
        return  edList.size();
    }

    public static class EducationViewHolder extends RecyclerView.ViewHolder {

        protected CustomFontTextView title, duration;

        public EducationViewHolder(View itemView) {
            super(itemView);
            title = (CustomFontTextView) itemView.findViewById(R.id.cert_title_ctxv);
            duration = (CustomFontTextView) itemView.findViewById(R.id.cert_dur_ctxv);
        }
    }
}
