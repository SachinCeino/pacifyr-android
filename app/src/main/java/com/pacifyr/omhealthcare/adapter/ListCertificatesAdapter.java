package com.pacifyr.omhealthcare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.ceino.utilitylibrary.CustomFontTextView;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.model.ModelPacifyrCertificates;

import java.util.ArrayList;

/**
 * Created by ceino on 17/4/18.
 */

public class ListCertificatesAdapter extends RecyclerView.Adapter<ListCertificatesAdapter.ViewHolder> {

    ArrayList<ModelPacifyrCertificates> certlist;
    Context mContext;

    public ListCertificatesAdapter(ArrayList<ModelPacifyrCertificates> edList, Context context) {
        this.certlist = edList;
        mContext = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_add_pacifyr_education, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.title.setText(certlist.get(position).getTitle());
        holder.duration.setText(certlist.get(position).getDuration());

    }

    @Override
    public int getItemCount() {
        return  certlist.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        protected CustomFontTextView title, duration;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.cert_title_ctxv);
            duration = itemView.findViewById(R.id.cert_dur_ctxv);
        }
    }
}
