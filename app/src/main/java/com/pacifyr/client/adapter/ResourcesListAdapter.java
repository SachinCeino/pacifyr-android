package com.pacifyr.client.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pacifyr.client.R;
import com.pacifyr.client.activity.ResourceAllActivity;
import com.pacifyr.client.activity.TermsOfUseActivity;

import com.pacifyr.client.home.DashboardActivity;
import com.pacifyr.client.model.ModelResources;
import com.pacifyr.client.model.ResourceMain;
import com.pacifyr.client.network.NetworkService;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ceino on 18/5/18.
 */

public class ResourcesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    List<ResourceMain.Result> resourcesList;
    Context mContext;

    public ResourcesListAdapter(List<ResourceMain.Result> fileList, Context context) {
        this.resourcesList=fileList;
        this.mContext=context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_resources_items, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    private ResourceMain.Result getItem(int position){
        return resourcesList.get(position);
    }

    public Boolean isValid(String text) {
        if (text != null) return !text.trim().equalsIgnoreCase("");
        return false;

    }

    public Boolean isValid(List list) {
        if (list != null) return list.size() > 0;
        return false;

    }

    public Boolean isValid(List list,int pos) {
        if (isValid(list))
            return list.size() >= pos;
        return false;

    }
    public Boolean isValidPos(List list,int pos) {
        if (isValid(list))
            return list.size() >= pos;
        return false;

    }
    public Boolean isValidNotNull(List list) {
        return list != null;

    }

    public Boolean isValidSize(List list) {
        if (list != null) return list.size() > 0;
        return false;

    }

    public Boolean isValid(Object object) {
        return object != null;

    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final ResourceMain.Result item = getItem(position);

        if (item!=null)
        {
            viewHolder.TextViewTitle.setText(item.getSpeciality());
            if (isValid(item.getResources())){
                setUpResourceAdapter(viewHolder,item.getResources());
            }
            viewHolder.TextViewAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent mIntent=new Intent(mContext, ResourceAllActivity.class);
                    mIntent.putExtra("object",(Serializable)item);
                    mContext.startActivity(mIntent);
                }
            });
        }


       /* viewHolder.resourcesDate.setText(item.getCreatedAt());
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
*/
    }

    private void setUpResourceAdapter(ViewHolder viewHolder, List<ResourceMain.Resource> resources) {

        ResourcesSubListAdapter mResourcesSubListAdapter=new ResourcesSubListAdapter(mContext,resources);
        LinearLayoutManager mLinearLayoutManager=new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false);
        viewHolder.RecyclerViewResources.setLayoutManager(mLinearLayoutManager);
        viewHolder.RecyclerViewResources.setAdapter(mResourcesSubListAdapter);

    }

    @Override
    public int getItemCount() {
        return resourcesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RecyclerView RecyclerViewResources;
        private TextView TextViewAll;
        private TextView TextViewTitle;

        LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.top_llay);
            RecyclerViewResources = itemView.findViewById(R.id.RecyclerViewResources);
            TextViewAll = itemView.findViewById(R.id.TextViewAll);
            TextViewTitle = itemView.findViewById(R.id.TextViewTitle);
        }
    }
}
