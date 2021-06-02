package com.pacifyr.client.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.pacifyr.client.model.ResourceMain;
import com.pacifyr.client.model.ResourceSpecialityList;
import com.pacifyr.client.network.NetworkService;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ceino on 18/5/18.
 */

public class ResourcesListMainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    List<ResourceSpecialityList.Result> resourcesList;
    Context mContext;

    public ResourcesListMainAdapter(List<ResourceSpecialityList.Result> fileList, Context context) {
        this.resourcesList=fileList;
        this.mContext=context;
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
                .inflate(R.layout.list_resources_sub_items_articles_main, parent, false);

        if (viewType == 1) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.list_resources_sub_items_video_main, parent, false);
            viewHolder = new ViewHolderVideo(itemView);
        } else {
            viewHolder = new ViewHolderArticle(itemView);
        }

        return viewHolder;
    }

    private ResourceSpecialityList.Result getItem(int position){
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

        final ResourceSpecialityList.Result item = getItem(position);

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


    private void setUpViewHolderArticle(ViewHolderArticle viewHolder, ResourceSpecialityList.Result item) {
        if (isValid(item))
        {
            if (item!=null)
            {
                viewHolder.TextViewTitle.setText(item.getTitle());
                viewHolder.TextViewAuther.setText(item.getAuthor());

                if (isValid(item.getImage()))
                {
                    Picasso.with(mContext).load(NetworkService.GLOBAL_IMAGE_URL + item.getImage())
                            .placeholder(R.drawable.placeholder).into(viewHolder.ImageViewImage);


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
    }

    private void setUpViewHolderVideo(ViewHolderVideo viewHolder, ResourceSpecialityList.Result item) {
        if (isValid(item))
        {

            if (item!=null)
            {
                viewHolder.TextViewTitle.setText(item.getTitle());
                viewHolder.TextViewAuther.setText(item.getAuthor());

                if (isValid(item.getImage()))
                {
                    Picasso.with(mContext).load(NetworkService.GLOBAL_IMAGE_URL + item.getImage())
                            .placeholder(R.drawable.placeholder).into(viewHolder.ImageViewImage);


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

    }

    private class ViewHolderVideo extends RecyclerView.ViewHolder {
        private RecyclerView RecyclerViewResources;
        private TextView TextViewTitle;
        private TextView TextViewAuther;
        private ImageView ImageViewImage;

        LinearLayout linearLayout;
        public ViewHolderVideo(View itemView) {
            super(itemView);
            TextViewTitle = itemView.findViewById(R.id.TextViewTitle);
            TextViewAuther = itemView.findViewById(R.id.TextViewAuther);
            ImageViewImage = itemView.findViewById(R.id.ImageViewImage);
        }

    }

    private class ViewHolderArticle extends RecyclerView.ViewHolder {
        private RecyclerView RecyclerViewResources;
        private TextView TextViewTitle;
        private TextView TextViewAuther;
        private ImageView ImageViewImage;

        LinearLayout linearLayout;

        public ViewHolderArticle(View itemView) {
            super(itemView);
            TextViewTitle = itemView.findViewById(R.id.TextViewTitle);
            TextViewAuther = itemView.findViewById(R.id.TextViewAuther);
            ImageViewImage = itemView.findViewById(R.id.ImageViewImage);
        }
    }


   /* private void setUpResourceAdapter(ViewHolder viewHolder, List<ResourceMain.Resource> resources) {

        ResourcesSubListAdapter mResourcesSubListAdapter=new ResourcesSubListAdapter(mContext,resources);
        LinearLayoutManager mLinearLayoutManager=new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false);
        viewHolder.RecyclerViewResources.setLayoutManager(mLinearLayoutManager);
        viewHolder.RecyclerViewResources.setAdapter(mResourcesSubListAdapter);

    }*/

    @Override
    public int getItemCount() {
        return resourcesList.size();
    }

}
