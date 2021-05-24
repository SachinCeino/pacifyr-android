package com.pacifyr.client.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pacifyr.client.R;
import com.pacifyr.client.activity.ResourceAllActivity;
import com.pacifyr.client.model.ResourceCategory;
import com.pacifyr.client.model.ResourceMain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ceino on 18/5/18.
 */

public class ResourcesCategoryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    ArrayList<ResourceCategory> resourcesList;
    Context mContext;
    ItemClickedListenr mItemClickedListenr;

    public ResourcesCategoryListAdapter(ArrayList<ResourceCategory> fileList, Context context,ItemClickedListenr mItemClickedListenr) {
        this.resourcesList=fileList;
        this.mContext=context;
        this.mItemClickedListenr=mItemClickedListenr;
    }

    public interface ItemClickedListenr {
        void onItemClicked(int position);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_resources_category_items, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    private ResourceCategory getItem(int position){
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
        final ResourceCategory item = getItem(position);

        if (item!=null)
        {
            viewHolder.TextViewTitle.setText(item.getTitle()+"");
            if (item.isSelected())
            {
                viewHolder.TextViewTitle.setTextColor(mContext.getResources().getColor(R.color.white));
                viewHolder.itemView.setBackground(mContext.getResources().getDrawable(R.drawable.resource_fill_curved_blue));
            }
            else
            {
                viewHolder.TextViewTitle.setTextColor(mContext.getResources().getColor(R.color.grey));
                viewHolder.itemView.setBackground(mContext.getResources().getDrawable(R.drawable.resource_unfill_curve));

            }
        }


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item.setSelected(true);
                for (int i=0;i<resourcesList.size();i++)
                {
                    if (i!=position)
                    {
                        resourcesList.get(i).setSelected(false);
                    }
                }
                notifyDataSetChanged();
                mItemClickedListenr.onItemClicked(position);
            }
        });


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
            TextViewTitle = itemView.findViewById(R.id.TextViewTitle);
        }
    }
}
