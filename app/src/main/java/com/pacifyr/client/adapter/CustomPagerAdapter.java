package com.pacifyr.client.adapter;

/**
 * Created by ceino on 16/11/17.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.viewpager.widget.PagerAdapter;

import com.pacifyr.client.R;
import com.pacifyr.client.model.ModelObject;
import com.pacifyr.client.utils.Constants;

public class CustomPagerAdapter extends PagerAdapter {

    private Context mContext;

    public CustomPagerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        ModelObject modelObject = ModelObject.values()[position];
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(modelObject.getLayoutResId(), collection, false);
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


           ProgressDialog pDialog = new ProgressDialog(mContext);

           // Set progressbar message
           pDialog.setMessage("Buffering...");
           pDialog.setIndeterminate(false);
           pDialog.setCancelable(false);
           // Show progressbar
           pDialog.show();

           try {
               // Start the MediaController
               MediaController mediacontroller = new MediaController(mContext);
               mediacontroller.setAnchorView(mVideoView);

               Uri videoUri = Uri.parse(Constants.getPacifyrVideoTutorialLink(mContext));
               mVideoView.setMediaController(mediacontroller);
               mVideoView.setVideoURI(videoUri);

           } catch (Exception e) {

               e.printStackTrace();
           }

           mVideoView.requestFocus();
           mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
               // Close the progress bar and play the video
               public void onPrepared(MediaPlayer mp) {
                   pDialog.dismiss();
                   mVideoView.start();
               }
           });
           mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

               public void onCompletion(MediaPlayer mp) {
                   if (pDialog.isShowing()) {
                       pDialog.dismiss();
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