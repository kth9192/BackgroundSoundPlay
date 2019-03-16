package com.kth.backgroundsound;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.kth.backgroundsound.databinding.ActivityMusicBinding;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class MusicActivity extends AppCompatActivity {
    private static String TAG = MusicActivity.class.getSimpleName();
    private ActivityMusicBinding activityMusicBinding;
    private SimpleExoPlayer player;
    private TrackSelector trackSelector;
    private MusicService musicService;
    private Intent serviceIntent;
    private boolean isBind = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMusicBinding = DataBindingUtil.setContentView(this, R.layout.activity_music);

        trackSelector = new DefaultTrackSelector();
        serviceIntent = new Intent(this, MusicService.class);
        bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(serviceIntent);
        unbindService(mConnection);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            MusicService.MyBinder binder = (MusicService.MyBinder) service;
            musicService = binder.getService();
            player = musicService.getPlayer();

            activityMusicBinding.player.setPlayer(player);
            isBind = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.e(TAG, "service disconnected : " + arg0.getClassName());
        }
    };
}
