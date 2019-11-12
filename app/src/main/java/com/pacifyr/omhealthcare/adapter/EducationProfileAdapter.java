package com.pacifyr.omhealthcare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ceino.utilitylibrary.CustomFontTextView;
import com.ceino.utilitylibrary.model.pacifyr.ModelUserProfile;
import com.pacifyr.omhealthcare.R;

import java.util.ArrayList;

/**
 * Created by Samsad on 1/8/17.
 */

public class EducationProfileAdapter extends RecyclerView.Adapter<EducationProfileAdapter.EducationViewHolder> {

    ArrayList<ModelUserProfile.Education> eduList;

    public EducationProfileAdapter(ArrayList<ModelUserProfile.Education> planetList, Context context) {
        this.eduList = planetList;
    }

    @Override
    public EducationProfileAdapter.EducationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.education_profile_list_item,parent,false);
        EducationViewHolder viewHolder=new EducationViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EducationProfileAdapter.EducationViewHolder holder, int position) {
        //holder.image.setImageResource(R.drawable.planetimage);
        holder.title.setText(eduList.get(position).getTitle());
        holder.edu_duration_ctxv.setText(eduList.get(position).getDuration());
        holder.location.setText(eduList.get(position).getLocation());
    }

    @Override
    public int getItemCount() {
        return eduList.size();
    }

    public static class EducationViewHolder extends RecyclerView.ViewHolder{

        protected CustomFontTextView title,location,edu_duration_ctxv;

        public EducationViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.edu_title_ctxv);
            location = itemView.findViewById(R.id.edu_desc_ctxv);
            edu_duration_ctxv = itemView.findViewById(R.id.edu_duration_ctxv);
        }
    }
}