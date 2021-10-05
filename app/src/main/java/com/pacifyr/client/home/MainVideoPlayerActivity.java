package com.pacifyr.client.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.pacifyr.client.R;
import com.pacifyr.client.adapter.CustomPagerAdapter;
import com.pacifyr.client.base.PacifyrActivity;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;

public class MainVideoPlayerActivity extends PacifyrActivity {

    MainTextView skipCtxv;

    private SimpleExoPlayerView mPlayerView;
    private SimpleExoPlayer mPlayer;

    // autoplay = false
    private boolean autoPlay = false;

    // used to remember the playback position
    private int currentWindow;
    private long playbackPosition;

    // constant fields for saving and restoring bundle
    public static final String AUTOPLAY = "autoplay";
    public static final String CURRENT_WINDOW_INDEX = "current_window_index";
    public static final String PLAYBACK_POSITION = "playback_position";

    // sample audio for testing exoplayer
    public static String VIDEO_URL = "";

    // sample videos for testing exoplayer
    public static final String VIDEO_1 = "http://techslides.com/demos/sample-videos/small.mp4";
    public static final String VIDEO_2 = "http://clips.vorwaerts-gmbh.de/VfE_html5.mp4";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_video_player);
        // find the view responsible for visual feedback (media controls & display)
        mPlayerView = (SimpleExoPlayerView) findViewById(R.id.media_view);

        if (isValid(getIntent()) && isValid(getIntent().getExtras()) && isValid(getIntent().getExtras().getString("URL")))
        {
            VIDEO_URL=getIntent().getExtras().getString("URL");
        }
        if (savedInstanceState != null) {
            playbackPosition = savedInstanceState.getLong(PLAYBACK_POSITION, 0);
            currentWindow = savedInstanceState.getInt(CURRENT_WINDOW_INDEX, 0);
            autoPlay = savedInstanceState.getBoolean(AUTOPLAY, false);
        }


        onSetActionBar();
    }

    ///ACTION BAR FEATURES
    private LinearLayout actionBarTopLlay;
    private RelativeLayout bottomPanel;
    private LinearLayout leftLlay;
    private DeviceFitImageView backImv;
    private MainTextView leftTxv;
    private MainTextView titleTxv;
    private LinearLayout rightLlay;
    private MainTextView rightTxv;
    private DeviceFitImageView rightImv;

    public void onSetActionBar() {
        actionBarTopLlay = findViewById(R.id.action_bar_top_llay);
        bottomPanel = findViewById(R.id.bottom_panel);
        leftLlay = findViewById(R.id.left_llay);
        backImv = findViewById(R.id.back_imv);
        leftTxv = findViewById(R.id.left_txv);
        titleTxv = findViewById(R.id.title_txv);
        rightLlay = findViewById(R.id.right_llay);
        rightTxv = findViewById(R.id.right_txv);
        rightImv = findViewById(R.id.right_imv);
        rightImv.setVisibility(View.INVISIBLE);
        rightTxv.setVisibility(View.INVISIBLE);
        rightLlay.setVisibility(View.INVISIBLE);
        leftTxv.setVisibility(View.INVISIBLE);
        backImv.setVisibility(View.VISIBLE);
        titleTxv.setVisibility(View.VISIBLE);
        Typeface type = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.avantica_bold_header));
        titleTxv.setTypeface(type);
        titleTxv.setText("");
        leftLlay.setVisibility(View.VISIBLE);
        leftLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });
    }



    @Override
    protected View getSnackbarAnchorView() {
        return null;
    }

    void initializePlayer() {
        // create a new instance of SimpleExoPlayer
        mPlayer = ExoPlayerFactory.newSimpleInstance(this);

        // attach the just created player to the view responsible for displaying the media (i.e. media controls, visual feedback)
        mPlayerView.setPlayer(mPlayer);
        mPlayer.setPlayWhenReady(autoPlay);

        // resume playback position
        mPlayer.seekTo(currentWindow, playbackPosition);

        Uri uri = Uri.parse(VIDEO_URL);
        MediaSource mediaSource = buildMediaSource(uri);

        // now we are ready to start playing our media files
        mPlayer.prepare(mediaSource);
    }

    /*
     * This method returns ExtractorMediaSource or one of its compositions
     * ExtractorMediaSource is suitable for playing regular files like (mp4, mp3, webm etc.)
     * This is appropriate for the baking app project, since all recipe videos are not in adaptive formats (i.e. HLS, Dash etc)
     */
    private MediaSource buildMediaSource(Uri uri) {
        DefaultExtractorsFactory extractorSourceFactory = new DefaultExtractorsFactory();
        DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("ua");

        ExtractorMediaSource audioSource = new ExtractorMediaSource(uri, dataSourceFactory, extractorSourceFactory, null, null);

        // this return a single mediaSource object. i.e. no next, previous buttons to play next/prev media file
        return new ExtractorMediaSource(uri, dataSourceFactory, extractorSourceFactory, null, null);

        /*
         * Uncomment the line below to play multiple meidiaSources in sequence aka playlist (and totally without buffering!)
         * NOTE: you have to comment the return statement just above this comment
         */

        /*
          ExtractorMediaSource videoSource1 = new ExtractorMediaSource(Uri.parse(VIDEO_1), dataSourceFactory, extractorSourceFactory, null, null);
          ExtractorMediaSource videoSource2 = new ExtractorMediaSource(Uri.parse(VIDEO_2), dataSourceFactory, extractorSourceFactory, null, null);
          // returns a mediaSource collection
          return new ConcatenatingMediaSource(videoSource1, audioSource, videoSource2);
         */

    }

    private void releasePlayer() {
        if (mPlayer != null) {
            // save the player state before releasing its resources
            playbackPosition = mPlayer.getCurrentPosition();
            currentWindow = mPlayer.getCurrentWindowIndex();
            autoPlay = mPlayer.getPlayWhenReady();
            mPlayer.release();
            mPlayer = null;
        }
    }


    // save app state before all members are gone forever :D
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        /*
         * A simple configuration change such as screen rotation will destroy this activity
         * so we'll save the player state here in a bundle (that we can later access in onCreate) before everything is lost
         * NOTE: we cannot save player state in onDestroy like we did in onPause and onStop
         * the reason being our activity will be recreated from scratch and we would have lost all members (e.g. variables, objects) of this activity
         */
        if (mPlayer == null) {
            outState.putLong(PLAYBACK_POSITION, playbackPosition);
            outState.putInt(CURRENT_WINDOW_INDEX, currentWindow);
            outState.putBoolean(AUTOPLAY, autoPlay);
        }
    }

    // This is just an implementation detail to have a pure full screen experience. Nothing fancy here
    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        mPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    /*
     * NOTE: we initialize the player either in onStart or onResume according to API level
     * API level 24 introduced support for multiple windows to run side-by-side. So it's safe to initialize our player in onStart
     * more on Multi-Window Support here https://developer.android.com/guide/topics/ui/multi-window.html
     * Before API level 24, we wait as long as onResume (to grab system resources) before initializing player
     */
    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // start in pure full screen
        hideSystemUi();
        if ((Util.SDK_INT <= 23 || mPlayer == null)) {
            initializePlayer();
        }
    }


    /**
     * Before API level 24 we release player resources early
     * because there is no guarantee of onStop being called before the system terminates our app
     * remember onPause means the activity is partly obscured by something else (e.g. incoming call, or alert dialog)
     * so we do not want to be playing media while our activity is not in the foreground.
     */
    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    // API level 24+ we release the player resources when the activity is no longer visible (onStop)
    // NOTE: On API 24+, onPause is still visible!!! So we do not not want to release the player resources
    // this is made possible by the new Android Multi-Window Support https://developer.android.com/guide/topics/ui/multi-window.html
    // We stop playing media on API 24+ only when our activity is no longer visible aka onStop
    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }
}
