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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import com.pacifyr.client.R;
import com.pacifyr.client.fragment.SlideTutorialFragment;
import com.pacifyr.client.model.ModelObject;
import com.pacifyr.client.utils.Constants;

public class CustomPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private boolean prepared = false;
    private ImageView icon_play_pause;
    private ProgressBar progress_video;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;



    public CustomPagerAdapter(Context mContext,@NonNull FragmentManager fm) {
        super(fm);
        this.mContext=mContext;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return SlideTutorialFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        if (mContext.getPackageName().equals("com.pacifyr.pacifyrapp"))
            return ModelObject.values().length;
        else return 3;
    }




}