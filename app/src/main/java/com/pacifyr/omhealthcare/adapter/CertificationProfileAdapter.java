package com.pacifyr.omhealthcare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.pacifyr.omhealthcare.customview.MainTextView;
import com.ceino.utilitylibrary.model.pacifyr.ModelUserProfile;
import com.pacifyr.omhealthcare.R;

import java.util.ArrayList;

/**
 * Created by Samsad on 1/8/17.
 */
    public class CertificationProfileAdapter extends RecyclerView.Adapter<CertificationProfileAdapter.CertificationViewHolder> {

    ArrayList<ModelUserProfile.Certification> certList;

    public CertificationProfileAdapter(ArrayList<ModelUserProfile.Certification> planetList, Context context) {
        this.certList = planetList;
    }

    @Override
    public com.pacifyr.omhealthcare.adapter.CertificationProfileAdapter.CertificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.certification_profile_list, parent, false);
        CertificationProfileAdapter.CertificationViewHolder viewHolder = new CertificationProfileAdapter.CertificationViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(com.pacifyr.omhealthcare.adapter.CertificationProfileAdapter.CertificationViewHolder holder, int position) {
        //holder.image.setImageResource(R.drawable.planetimage);
        holder.title.setText(certList.get(position).getTitle());
        holder.duration.setText(certList.get(position).getDuration());
    }

    @Override
    public int getItemCount() {
        return certList.size();
    }

    public static class CertificationViewHolder extends RecyclerView.ViewHolder {

        protected MainTextView title, duration;

        public CertificationViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.cert_title_ctxv);
            duration = itemView.findViewById(R.id.cert_dur_ctxv);
        }
    }
}