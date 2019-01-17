package com.example.joshuamugisha.udacitybakingapp.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.joshuamugisha.udacitybakingapp.R;
import com.example.joshuamugisha.udacitybakingapp.model.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class DetailActivityFragment extends Fragment {

    private TextView shortDescription, description;
    private ImageView imageUrl;
    private String videoUrl, mImageUrl;
    private Step steps;

    private PlayerView mPlayerView;
    private SimpleExoPlayer mPlayer;

    // autoplay = false
    private boolean autoPlay = true;

    // used to remember the playback position
    private int currentWindow;
    private long playbackPosition;

    // constant fields for saving and restoring bundle
    public static final String AUTOPLAY = "autoplay";
    public static final String CURRENT_WINDOW_INDEX = "current_window_index";
    public static final String PLAYBACK_POSITION = "playback_position";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // if we have saved player state, restore it
        if (savedInstanceState != null) {
            playbackPosition = savedInstanceState.getLong(PLAYBACK_POSITION, 0);
            currentWindow = savedInstanceState.getInt(CURRENT_WINDOW_INDEX, 0);
            autoPlay = savedInstanceState.getBoolean(AUTOPLAY, false);
        }
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.activity_video, container, false);

        mPlayerView = rootView.findViewById(R.id.video_view);
        shortDescription = rootView.findViewById(R.id.shortDescription);
        description = rootView.findViewById(R.id.description);
        Bundle bundle = this.getArguments();


        if (bundle != null) {

            steps = bundle.getParcelable("Steps");

            shortDescription.setText(steps.getShortDescription());
            description.setText(steps.getDescription());
            videoUrl = steps.getVideoURL();
            mImageUrl = steps.getThumbnailURL();

            if(videoUrl.equals("")){
                mPlayerView.setVisibility(View.GONE);
                if(mImageUrl.equals("")){
                    imageUrl.setVisibility(View.VISIBLE);
                }
                if(!mImageUrl.equals("")){
                    if(mImageUrl.endsWith(".mp4")){
                        videoUrl=mImageUrl;
                        imageUrl.setVisibility(View.GONE);
                        mPlayerView.setVisibility(View.VISIBLE);
                    }else{
                        Glide.with(getActivity()).load(mImageUrl).into(imageUrl);
                    }
                }

            }
//            initializePlayer();
        } else {
            Toast.makeText(getActivity(), "Data not available", Toast.LENGTH_SHORT).show();
        }
        return rootView;

    }

    public void initializePlayer() {
        // create a new instance of SimpleExoPlayer
        mPlayer = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getActivity()),
                new DefaultTrackSelector(),
                new DefaultLoadControl());

        // attach the just created player to the view responsible for displaying the media (i.e. media controls, visual feedback)
        mPlayerView.setPlayer(mPlayer);
        mPlayer.setPlayWhenReady(autoPlay);

        // resume playback position
        mPlayer.seekTo(currentWindow, playbackPosition);

        Uri uri = Uri.parse(videoUrl);
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
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                createMediaSource(uri);
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
    public void onSaveInstanceState(Bundle outState) {
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
        if (mPlayer != null) {
            //resuming properly
            mPlayer.setPlayWhenReady(autoPlay);
            mPlayer.seekTo(playbackPosition);
        } else {
            //Correctly initialize and play properly fromm seekTo function
//            initializeMedia();
            initializePlayer();
            mPlayer.setPlayWhenReady(autoPlay);
            mPlayer.seekTo(playbackPosition);
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
        if (mPlayer != null) {
            playbackPosition = mPlayer.getCurrentPosition();
            autoPlay = mPlayer.getPlayWhenReady();
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
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
