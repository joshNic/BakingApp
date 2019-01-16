package com.example.joshuamugisha.udacitybakingapp.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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

    // bandwidth meter to measure and estimate bandwidth
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    private static final String TAG = "PlayerActivity";

    private SimpleExoPlayer player;
    private PlayerView playerView;
    //    private ComponentListener componentListener;
    private TextView shortDescription, description;

    private long playbackPosition;
    private int currentWindow;
    private boolean playWhenReady = true;
    private String videoUrl, desc, shortDesc;
    private Step steps;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.activity_video, container, false);

        playerView = rootView.findViewById(R.id.video_view);
        shortDescription = rootView.findViewById(R.id.shortDescription);
        description = rootView.findViewById(R.id.description);
        Bundle bundle = this.getArguments();


        if (bundle != null) {

            steps = bundle.getParcelable("Steps");

            shortDescription.setText(steps.getShortDescription());
            description.setText(steps.getDescription());
            videoUrl = steps.getVideoURL();
            initializePlayer(videoUrl);
        } else {
            Toast.makeText(getActivity(), "Data not available", Toast.LENGTH_SHORT).show();
        }
        return rootView;

    }

    private void initializePlayer(String vide) {
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getActivity()),
                new DefaultTrackSelector(), new DefaultLoadControl());

        playerView.setPlayer(player);

        player.setPlayWhenReady(true);
//        player.seekTo(currentWindow, playbackPosition);

        Uri uri = Uri.parse(vide);
        MediaSource mediaSource = buildMediaSource(uri);
        player.prepare(mediaSource, true, false);
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                createMediaSource(uri);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer(videoUrl);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        hideSystemUi();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer(videoUrl);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void releasePlayer() {
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
    }
}
