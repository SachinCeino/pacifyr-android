package com.pacifyr.client.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pacifyr.client.R;
import com.pacifyr.client.activity.TermsOfUseActivity;
import com.pacifyr.client.model.ResourceMain;
import com.pacifyr.client.network.NetworkService;
import com.squareup.picasso.Picasso;

import java.util.List;

/**

 */

public class ResourcesSubListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    List<ResourceMain.Resource> resourcesList;
    Context mContext;

    public ResourcesSubListAdapter(Context mContext, List<ResourceMain.Resource> resourcesList) {
        this.resourcesList=resourcesList;
        this.mContext=mContext;
    }


    @Override
    public int getItemViewType(int position) {
        int pos=0;
        String type = resourcesList.get(position).getType();
        if (type.equals("video"))
        {
            pos = 1;

        }else{

            pos =0;

        }

        return pos;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder=null;
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_resources_sub_items_articles, parent, false);

        if (viewType == 1) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.list_resources_sub_items_video, parent, false);
            viewHolder = new ViewHolderVideo(itemView);
        } else {
            viewHolder = new ViewHolderArticle(itemView);
        }

        return viewHolder;
    }

    private ResourceMain.Resource getItem(int position){
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

        final ResourceMain.Resource item = getItem(position);

        if (holder instanceof ViewHolderVideo)
        {
            ViewHolderVideo viewHolder = (ViewHolderVideo) holder;
            setUpViewHolderVideo(viewHolder,item);
        }
        else
        {
            ViewHolderArticle viewHolder = (ViewHolderArticle) holder;
            setUpViewHolderArticle(viewHolder,item);

        }



    }

    private void setUpViewHolderArticle(ViewHolderArticle viewHolder, ResourceMain.Resource item) {
        if (isValid(item))
        {
            if (isValid(item.getImage()))
            {
                Picasso.with(mContext).load(NetworkService.GLOBAL_IMAGE_URL + item.getImage())
                        .placeholder(R.drawable.placeholder).into(viewHolder.ImageViewImage);


            }

            if (isValid(item.getAuthor()))
            {
                viewHolder.TextViewAuther.setText(item.getAuthor()+"");
            }

            if (isValid(item.getTitle()))
            {
                viewHolder.TextViewTitle.setText(item.getTitle()+"");
            }

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(mContext, TermsOfUseActivity.class);
                    i.putExtra("spurl", item.getUrl());
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(i);
                }
            });

        }
    }

    private void setUpViewHolderVideo(ViewHolderVideo viewHolder, ResourceMain.Resource item) {
        if (isValid(item))
        {
            if (isValid(item.getImage()))
            {
                Picasso.with(mContext).load(NetworkService.GLOBAL_IMAGE_URL + item.getImage())
                        .placeholder(R.drawable.placeholder).into(viewHolder.ImageViewImage);


            }

            if (isValid(item.getAuthor()))
            {
                viewHolder.TextViewAuther.setText(item.getAuthor()+"");
            }

            if (isValid(item.getTitle()))
            {
                viewHolder.TextViewTitle.setText(item.getTitle()+"");
            }

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.getVideo()==null) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getUrl()));
                        browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(browserIntent);
                    }
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return resourcesList.size();
    }

    private class ViewHolderVideo extends RecyclerView.ViewHolder {
        private ImageView ImageViewImage;
        private TextView TextViewAuther;
        private TextView TextViewTitle;
        public ViewHolderVideo(View itemView) {
            super(itemView);
            ImageViewImage = itemView.findViewById(R.id.ImageViewImage);
            TextViewAuther = itemView.findViewById(R.id.TextViewAuther);
            TextViewTitle = itemView.findViewById(R.id.TextViewTitle);
        }

    }

    private class ViewHolderArticle extends RecyclerView.ViewHolder {
        private ImageView ImageViewImage;
        private TextView TextViewAuther;
        private TextView TextViewTitle;

        public ViewHolderArticle(View itemView) {
            super(itemView);
            ImageViewImage = itemView.findViewById(R.id.ImageViewImage);
            TextViewAuther = itemView.findViewById(R.id.TextViewAuther);
            TextViewTitle = itemView.findViewById(R.id.TextViewTitle);
        }
    }
}
