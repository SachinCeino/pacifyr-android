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

public class SlideTutorialFragment extends BaseFragment {



    private boolean prepared = false;
    private ImageView icon_play_pause;
    private ProgressBar progress_video;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    private int position=0;

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
            if (mediaPlayer!=null)
            {
                if (mediaPlayer.isPlaying())
                {
                    mediaPlayer.stop();
                    mediaPlayer.release();

                }
            }

        } catch (Exception e) {
        }

}

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
          if (mediaPlayer!=null)
            {
                if (mediaPlayer.isPlaying())
                {
                    mediaPlayer.stop();
                    mediaPlayer.release();

                }
            }

    } catch (IllegalStateException e) {
             }
    }

    @Override
    public void onDestroyView() {

        try {
            if (mediaPlayer!=null)
            {
                if (mediaPlayer.isPlaying())
                {
                    mediaPlayer.stop();
                    mediaPlayer.release();

                }
            }

        } catch (Exception e) {
        }


        super.onDestroyView();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
            if (position==3){
                    icon_play_pause = view.findViewById(R.id.icon_play_pause);
                    progress_video = view.findViewById(R.id.progress_video);


                    VideoView mVideoView=view.findViewById(R.id.mVideoView);
                    ProgressDialog pDialog = new ProgressDialog(getActivity());

                    try {
                        // Start the MediaController
                        MediaController mediacontroller = new MediaController(getActivity());
                        mediacontroller.setAnchorView(mVideoView);

                        Uri videoUri = Uri.parse(Constants.getPacifyrVideoTutorialLink(getActivity()));
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

                            icon_play_pause.setImageDrawable(getActivity().getResources().getDrawable(android.R.drawable.ic_media_play));
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
                                        icon_play_pause.setImageDrawable(getActivity().getResources().getDrawable(android.R.drawable.ic_media_pause));
                                    } else {
                                        icon_play_pause.setImageDrawable(getActivity().getResources().getDrawable(android.R.drawable.ic_media_play));
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
    }
}
