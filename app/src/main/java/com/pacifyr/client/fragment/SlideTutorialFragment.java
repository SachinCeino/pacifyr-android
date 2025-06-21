package com.pacifyr.client.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerControlView;

import com.pacifyr.client.R;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.model.ModelObject;
import com.pacifyr.client.utility.BaseFragment;
import com.pacifyr.client.utils.Constants;
import com.squareup.otto.Subscribe;

public class SlideTutorialFragment extends BaseFragment {


    private int position = 0;

    private PlayerControlView video_view;
    private ExoPlayer player;
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
            if (position==3) {
                video_view = view.findViewById(R.id.mVideoView);
                progress_video = view.findViewById(R.id.progress_video);
                player = new ExoPlayer.Builder(getActivity()).build();
                video_view.setPlayer(player);

                MediaItem mediaSource = MediaItem.fromUri(Uri.parse(Constants.getPacifyrVideoTutorialLink(getActivity())));
                player.setMediaItem(mediaSource);
                player.prepare();
                player.play();
            }
    }
}
