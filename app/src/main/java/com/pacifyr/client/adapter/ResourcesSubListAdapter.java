package com.pacifyr.client.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pacifyr.client.R;
import com.pacifyr.client.model.ResourceMain;
import com.pacifyr.client.network.NetworkService;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ceino on 18/5/18.
 */

public class ResourcesSubListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    List<ResourceMain.Resource> resourcesList;
    Context mContext;

    public ResourcesSubListAdapter(Context mContext, List<ResourceMain.Resource> resourcesList) {
        this.resourcesList=resourcesList;
        this.mContext=mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_resources_sub_items, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
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
        ViewHolder viewHolder = (ViewHolder) holder;
        final ResourceMain.Resource item = getItem(position);

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

        }

    }

    @Override
    public int getItemCount() {
        return resourcesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ImageViewImage;
        private TextView TextViewAuther;
        private TextView TextViewTitle;


        public ViewHolder(View itemView) {
            super(itemView);
            ImageViewImage = itemView.findViewById(R.id.ImageViewImage);
            TextViewAuther = itemView.findViewById(R.id.TextViewAuther);
            TextViewTitle = itemView.findViewById(R.id.TextViewTitle);
        }
    }
}
