package com.pacifyr.omhealthcare.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ceino.utilitylibrary.CustomFontTextView;
import com.ceino.utilitylibrary.DeviceFitImageView;
import com.ceino.utilitylibrary.model.pacifyr.MEmotion;
import com.ceino.utilitylibrary.model.pacifyr.MListPositiveEmotions;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.event.BusProvider;
import com.pacifyr.omhealthcare.event.NotifyEvent;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.pacifyr.omhealthcare.home.FeelSelectActivity.IS_POS_SELECTED;
import static com.pacifyr.omhealthcare.home.FeelSelectActivity.PAC_EMOTIONS_IMAGE;
import static com.pacifyr.omhealthcare.home.FeelSelectActivity.PAC_EMOTIONS_TITLE;
import static com.pacifyr.omhealthcare.network.NetworkService.GLOBAL_IMAGE_URL;


public class PostiveEmotions extends RecyclerView.Adapter<PostiveEmotions.ViewHolder> {

    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    ArrayList<MEmotion> list=new ArrayList<>();
    public  Context mContext;
    Activity mActivity;



    public PostiveEmotions(Context context,Activity activity,  ArrayList<MEmotion> mListPositiveEmotions) {
        this.mInflater = LayoutInflater.from(context);
        this.list = mListPositiveEmotions;
        this.mActivity=activity;
        mContext=context;
    }

    public PostiveEmotions(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    // inflates the cell layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.emotion_poitive_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //holder.imageView.setBackgroundResource(R.drawable.amusement);
        holder.emotion_ctxv.setText(list.get(position).getTitle());
        final String imgUrl=GLOBAL_IMAGE_URL+list.get(position).getImage();
        //String imgUrl=
        Picasso.with(mContext).load(imgUrl).into(holder.imageView);
      //  holder.imageView.getDrawable().setColorFilter(new ColorFilter());
        //holder.imageView.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_OVER));
        int color = Color.parseColor("#73969696"); //The color u want
        //holder.imageView.setColorFilter(color, PorterDuff.Mode.DST_OUT);
        //holder.imageView.setColorFilter(mContext.getResources().getColor(R.color.transparent_black_hex_77),PorterDuff.Mode.DST);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PAC_EMOTIONS_IMAGE=imgUrl;
                PAC_EMOTIONS_TITLE=list.get(position).getTitle();
                Picasso.with(mContext).load(imgUrl).into(holder.imageView);
                Drawable d=holder.imageView.getDrawable();
                d.clearColorFilter();
                IS_POS_SELECTED=true;
                postEventOnMainThread(new NotifyEvent(true, false,true));
                //holder.imageView.setItemIconTintList(null);
                //holder.imageView.setColorFilter(mContext.getResources().getColor(R.color.transparent_black_hex_77),PorterDuff.Mode.DST_OUT);
                //mActivity.finish();

            }
        });

    }
    void postEventOnMainThread(final Object event) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                BusProvider.getInstance().post(event);
                // bus.post(event);
            }
        });

    }

    // total number of cells
    @Override
    public int getItemCount() {
        return list.size();
    }
    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        public DeviceFitImageView imageView;
        public CustomFontTextView emotion_ctxv;

        public ViewHolder(View itemView) {
            super(itemView);
            emotion_ctxv =(CustomFontTextView) itemView.findViewById(R.id.emotion_ctxv);
            imageView = (DeviceFitImageView) itemView.findViewById(R.id.emotion_dimv);

        }
    }


    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}