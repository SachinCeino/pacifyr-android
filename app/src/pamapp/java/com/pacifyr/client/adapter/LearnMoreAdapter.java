package com.pacifyr.client.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pacifyr.client.R;
import com.pacifyr.client.model.LearnMore;
import com.pacifyrapp.utilitylibrary.network.UtilityNetworkService;

import java.util.List;

/**
 * Created by Sachin Thampan on 12/3/20.
 */
public class LearnMoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final List<LearnMore> learnmoreList;
    private int VIDEO = 0, TEXT = 1;

    public LearnMoreAdapter(Context context, List<LearnMore> learnMoreList) {
        this.context = context;
        this.learnmoreList = learnMoreList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIDEO) {
            View view = LayoutInflater.from(context).inflate(R.layout.recycler_row_video, null);
            return new VideoHolder(view);
        } else if (viewType == TEXT) {
            View view = LayoutInflater.from(context).inflate(R.layout.recycler_row_text, null);
            return new TextHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof VideoHolder) {
            setHolder(((VideoHolder) holder), position);
        } else if (holder instanceof TextHolder) {
            setHolder(((TextHolder) holder), position);
        }
    }

    private void alphaAnimateout(View view) {
        AlphaAnimation animation1 = new AlphaAnimation(1.0f, 0.0f);
        animation1.setDuration(1000);
        animation1.setStartOffset(5000);
        animation1.setFillAfter(true);
        view.startAnimation(animation1);
    }

    private void alphaAnimatein(View view) {
        AlphaAnimation animation1 = new AlphaAnimation(0.0f, 1.0f);
        animation1.setDuration(1000);
        animation1.setStartOffset(0);
        animation1.setFillAfter(true);
        view.startAnimation(animation1);
    }

    private void setHolder(VideoHolder holder, int position) {
        holder.text_title.setText(this.learnmoreList.get(position).getTitle());
        Glide.with(context).load(UtilityNetworkService.GLOBAL_IMAGE_URL + this.learnmoreList.get(position).getThumbnail()).into(holder.thumb_video);
        Uri video =
                Uri.parse(this.learnmoreList.get(position).getUrl());
        holder.videoView.setVideoURI(video);
        holder.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                holder.videoView.start();

                holder.thumb_video.setVisibility(View.GONE);
                holder.play_pause.setImageDrawable(context.getResources().getDrawable(android.R.drawable.ic_media_pause));
                alphaAnimateout(holder.play_pause);
                holder.play_pause.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            if (mp.isPlaying()) {
                                alphaAnimatein(v);
                                mp.pause();
                                holder.play_pause.setImageDrawable(context.getResources().getDrawable(android.R.drawable.ic_media_play));
                            } else {
                                alphaAnimateout(v);
                                mp.start();
                                holder.play_pause.setImageDrawable(context.getResources().getDrawable(android.R.drawable.ic_media_pause));
                            }
                        } catch (IllegalStateException ie) {
                            ie.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void setHolder(TextHolder holder, int position) {
        holder.text_title.setText(this.learnmoreList.get(position).getTitle());
        holder.text_content.setText(this.learnmoreList.get(position).getText());

        holder.text_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        holder.text_title.setTypeface(holder.text_title.getTypeface(), Typeface.BOLD);
        holder.text_title.setTextColor(context.getResources().getColor(R.color.design_default_color_primary_dark));
        holder.text_content.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        holder.text_content.setTextColor(context.getResources().getColor(R.color.transparent_black_5));
    }

    @Override
    public final int getItemViewType(int position) {
        if (this.learnmoreList.get(position).getType().equalsIgnoreCase("video")) {
            return VIDEO;
        } else if (this.learnmoreList.get(position).getType().equalsIgnoreCase("text")) {
            return TEXT;
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return this.learnmoreList != null ? this.learnmoreList.size() : 0;
    }

    private class VideoHolder extends RecyclerView.ViewHolder {

        private final TextView text_title;
        private final VideoView videoView;
        private final ImageView thumb_video;
        private final ImageView play_pause;

        public VideoHolder(@NonNull View itemView) {
            super(itemView);
            text_title = itemView.findViewById(R.id.text_learnmore_title);
            videoView = itemView.findViewById(R.id.mViewoView);
            thumb_video = itemView.findViewById(R.id.thumb_video);
            play_pause = itemView.findViewById(R.id.control_play_pause);
        }
    }

    private class TextHolder extends RecyclerView.ViewHolder {

        private final TextView text_title;
        private final TextView text_content;


        public TextHolder(@NonNull View itemView) {
            super(itemView);
            text_title = itemView.findViewById(R.id.text_learnmore_con_title);
            text_content = itemView.findViewById(R.id.text_learnmore_con_text);
        }
    }
}
