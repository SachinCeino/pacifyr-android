package com.pacifyr.omhealthcare.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.activity.TermsOfUseActivity;
import com.pacifyr.omhealthcare.model.ModelResources;
import com.pacifyr.omhealthcare.network.NetworkService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ceino on 18/5/18.
 */

public class ResourcesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    ArrayList<ModelResources.Results> resourcesList;
    Context mContext;

    public ResourcesListAdapter(ArrayList<ModelResources.Results> fileList, Context context) {
        this.resourcesList=fileList;
        this.mContext=context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_resources_items, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    private ModelResources.Results getItem(int position){
        return resourcesList.get(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final ModelResources.Results item = getItem(position);

        viewHolder.resourcesDate.setText(item.getCreatedAt());
        viewHolder.resourcesTitle.setText(item.getTitle());
        Picasso.with(mContext).load(NetworkService.GLOBAL_IMAGE_URL + item.getImage())
                .placeholder(R.drawable.placeholder).into(viewHolder.resourcesImv);

        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(mContext,TermsOfUseActivity.class);
                i.putExtra("spurl", item.getUrl());
                mContext.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return resourcesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView resourcesImv;
        private TextView resourcesTitle;
        private TextView resourcesDate;

        LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.top_llay);
            resourcesImv = (ImageView) itemView.findViewById(R.id.resources_imv);
            resourcesTitle = (TextView) itemView.findViewById(R.id.header_txv);
            resourcesDate = (TextView) itemView.findViewById(R.id.date_txv);
        }
    }
}
