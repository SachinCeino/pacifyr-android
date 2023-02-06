package com.pacifyr.client.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.PlaybackPreparer;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.pacifyr.client.R;
import com.pacifyr.client.activity.TermsOfUseActivity;
import com.pacifyr.client.adapter.ListSavedCardAdapter;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.model.ModelObject;
import com.pacifyr.client.utility.BaseFragment;
import com.pacifyr.client.utils.Constants;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.squareup.otto.Subscribe;

public class SlideTutorialFragment extends BaseFragment implements PlaybackPreparer {



    private int position=0;

    private PlayerView video_view;
    private SimpleExoPlayer player;
    private ProgressBar progress_video;

    public SlideTutorialFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(int i) {
        SlideTutorialFragment fragment = new SlideTutorialFragment();
        Bundle bundle = new Bundle();//create bundle instance
        bundle.putString("position", String.valueOf(i));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Subscribe
    public void onNotifyEvent(NotifyEvent event) {

    }

    @Override
    public void onSetValues(View view) throws Exception {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        if (isValid(getArguments().get("position")))
            position= Integer.parseInt(getArguments().getString("position"));

        ModelObject modelObject = ModelObject.values()[position];
        return inflater.inflate(modelObject.getLayoutResId(), container, false);

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        try {
            if (player!=null)
            {
                if (player.isPlaying())
                {
                    player.stop();
                    player.release();

                }
            }

        } catch (Exception e) {
        }

}

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
          if (player!=null)
            {
                player.stop();
                    player.release();

            }

    } catch (IllegalStateException e) {
             }
    }


    @Override
    public void onPause() {
        super.onPause();

        if (video_view!=null)
        video_view.onPause();
        if (player!=null)
        {
            player.stop();

        }
    }

    @Override
    public void onDestroyView() {

        try {
            if (player!=null)
            {
                player.release();
                    player=null;

            }

        } catch (Exception e) {
        }


        super.onDestroyView();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
            if (position==3){
                video_view = view.findViewById(R.id.mVideoView);
                progress_video = view.findViewById(R.id.progress_video);
                player = new SimpleExoPlayer.Builder(getActivity()).build();

                DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getActivity(), Util.getUserAgent(getActivity(), ""));
                final MediaSource videoSource = new ProgressiveMediaSource.Factory(dataSourceFactory).
                        createMediaSource(Uri.parse(Constants.getPacifyrVideoTutorialLink(getActivity())));

                video_view.setPlayer(player);
                if (isValid(player) &&isValid(videoSource))
                {
                    player.prepare(videoSource);
                }

                video_view.setPlaybackPreparer(this);
                video_view.setPlayer(player);


                player.addListener(new ExoPlayer.EventListener() {

                    @Override
                    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {}

                    @Override
                    public void onLoadingChanged(boolean isLoading) {}

                    @Override
                    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                        if (playbackState == ExoPlayer.STATE_BUFFERING){
                            progress_video.setVisibility(View.VISIBLE);
                        } else {
                            progress_video.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onPlayerError(ExoPlaybackException error) {}


                    @Override
                    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {}
                });

            }
    }

    @Override
    public void preparePlayback() {
        if (player!=null)
        player.retry();

    }
}
