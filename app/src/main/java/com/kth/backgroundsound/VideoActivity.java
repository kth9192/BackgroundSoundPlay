package com.kth.backgroundsound;

import android.net.Uri;
import android.os.Bundle;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.kth.backgroundsound.databinding.ActivityStreamingBinding;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class VideoActivity extends AppCompatActivity {

    private ActivityStreamingBinding activityStreamingBinding;
    private SimpleExoPlayer player;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityStreamingBinding = DataBindingUtil.setContentView(this, R.layout.activity_streaming);
        player = ExoPlayerFactory.newSimpleInstance(this);

        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(this, Util.getUserAgent(this, getPackageName()));

        MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse("https://www.radiantmediaplayer.com/media/bbb-360p.mp4"));

        LoopingMediaSource loopingMediaSource = new LoopingMediaSource(mediaSource);

        player.prepare(loopingMediaSource);
        player.setPlayWhenReady(true);
        activityStreamingBinding.player.setPlayer(player);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        player.setPlayWhenReady(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        player.setPlayWhenReady(false);
    }
}
