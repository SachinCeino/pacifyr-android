package com.pacifyr.client.adapter;

/**
 * Created by ceino on 16/11/17.
 */

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import androidx.viewpager.widget.PagerAdapter;

import com.pacifyr.client.R;
import com.pacifyr.client.model.ModelObject;
import com.pacifyr.client.utils.Constants;

public class CustomPagerAdapter extends PagerAdapter {

    private Context mContext;
    private boolean prepared = false;
    private ImageView icon_play_pause;
    private ProgressBar progress_video;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;

    public CustomPagerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        ModelObject modelObject = ModelObject.values()[position];
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(modelObject.getLayoutResId(), collection, false);
        icon_play_pause = layout.findViewById(R.id.icon_play_pause);
        progress_video = layout.findViewById(R.id.progress_video);
       if (position==3)
       {
       VideoView mVideoView=layout.findViewById(R.id.mVideoView);
          /*  MediaController mc = new MediaController(mContext);
           mc.setAnchorView(mVideoView);
           mc.setMediaPlayer(mVideoView);
           Uri video = Uri.parse(Constants.getPacifyrVideoTutorialLink(mContext));
           mVideoView.setMediaController(mc);
           mVideoView.setVideoURI(video);
           mVideoView.start();
          */

/*
           ProgressDialog pDialog = new ProgressDialog(mContext);

           // Set progressbar message
           pDialog.setMessage("Buffering...");
           pDialog.setIndeterminate(false);
           pDialog.setCancelable(false);
           // Show progressbar
           pDialog.show();*/

           try {
               // Start the MediaController
               MediaController mediacontroller = new MediaController(mContext);
               mediacontroller.setAnchorView(mVideoView);

               Uri videoUri = Uri.parse(Constants.getPacifyrVideoTutorialLink(mContext));
               mVideoView.setVideoURI(videoUri);

           } catch (Exception e) {

               e.printStackTrace();
           }

           mVideoView.requestFocus();
           progress_video.setVisibility(View.VISIBLE);
           icon_play_pause.setVisibility(View.GONE);
           mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
               // Close the progress bar and play the video
               public void onPrepared(MediaPlayer mp) {
                   prepared = true;
                   mediaPlayer = mp;
                   icon_play_pause.setVisibility(View.VISIBLE);
                   progress_video.setVisibility(View.GONE);
               }
           });
           mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

               public void onCompletion(MediaPlayer mp) {

                   icon_play_pause.setImageDrawable(mContext.getResources().getDrawable(android.R.drawable.ic_media_play));
                   icon_play_pause.setVisibility(View.VISIBLE);

               }
           });

           icon_play_pause.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   if (mediaPlayer != null) {
                       try {
                           if (isPlaying) {
                               mediaPlayer.pause();
                               icon_play_pause.animate().alpha(1f).setDuration(500);
                               icon_play_pause.setImageDrawable(mContext.getResources().getDrawable(android.R.drawable.ic_media_pause));
                           } else {
                               icon_play_pause.setImageDrawable(mContext.getResources().getDrawable(android.R.drawable.ic_media_play));
                               mediaPlayer.start();
                               new Handler().postDelayed(new Runnable() {
                                   @Override
                                   public void run() {
                                       icon_play_pause.animate().alpha(0f).setDuration(500);
                                   }
                               }, 1500);
                           }
                           isPlaying = !isPlaying;
                       } catch (Exception e) {
                           isPlaying = false;
                           e.printStackTrace();
                       }
                   }
               }
           });


       }
        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        if (mContext.getPackageName().equals("com.pacifyr.pacifyrapp"))
        return ModelObject.values().length;
        else return 3;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        ModelObject customPagerEnum = ModelObject.values()[position];
        return mContext.getString(customPagerEnum.getTitleResId());
    }

}