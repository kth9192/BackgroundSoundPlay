package com.kth.backgroundsound;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import androidx.annotation.Nullable;

public class MusicService extends Service {

    //TODO : 알림을 없애는 시점의 최적화 필요 : 현재는 뒤로가기 버튼으로만 앱을 종료할 때 없어지고 강제종료시키는 경우에는 서비스 종료처리가 안되는 상태.
    //TODO : player의 재생상태를 관찰해 알림을 올릴 경우와 내리는 경우에 대한 로직 필요.
    private static String TAG = MusicService.class.getSimpleName();

    private SimpleExoPlayer player;
    private final IBinder binder = new MyBinder();
    private TrackSelector trackSelector;
    private PlayerNotificationManager playerNotificationManager;

    public MusicService() {
    }

    @Override
    public void onCreate() {
        // 서비스에서 가장 먼저 호출됨(최초에 한번만)
        super.onCreate();
        trackSelector = new DefaultTrackSelector();

        player = ExoPlayerFactory.newSimpleInstance(this, new DefaultRenderersFactory(this),
                trackSelector, new DefaultLoadControl());

        setPlayerNotificationManager();

        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(this, Util.getUserAgent(this, getPackageName()));

        MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse("http://podcast.synapsetech.co.kr/Download/mmm0472/16411/2018.1.26..mp3"));

        player.prepare(mediaSource);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 서비스가 호출될 때마다 실행
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        // 서비스가 종료될 때 실행
        super.onDestroy();
        killTheNotificaiton();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Service 객체와 (화면단 Activity 사이에서)
        // 통신(데이터를 주고받을) 때 사용하는 메서드
        // 데이터를 전달할 필요가 없으면 return null;

        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public SimpleExoPlayer getPlayer() {
        return player;
    }

    public PlayerNotificationManager getPlayerNotificationManager() {
        return playerNotificationManager;
    }

    private void setPlayerNotificationManager(){

        playerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(this,
                "TEST channel", R.string.channel_id, 1, new PlayerNotificationManager.MediaDescriptionAdapter() {
                    @Override
                    public String getCurrentContentTitle(Player player) {
                        return "test title";
                    }

                    @Nullable
                    @Override
                    public PendingIntent createCurrentContentIntent(Player player) {
                        return null;
                    }

                    @Nullable
                    @Override
                    public String getCurrentContentText(Player player) {
                        return "test";
                    }

                    @Nullable
                    @Override
                    public Bitmap getCurrentLargeIcon(Player player, PlayerNotificationManager.BitmapCallback callback) {
                        return null;
                    }
                });

        playerNotificationManager.setNotificationListener(new PlayerNotificationManager.NotificationListener() {
            @Override
            public void onNotificationStarted(int notificationId, Notification notification) {
                startForeground(notificationId, notification);
            }

            @Override
            public void onNotificationCancelled(int notificationId) {
                Log.d(TAG, "noti cancel : " + notificationId);
                stopSelf();
            }
        });
        playerNotificationManager.setPlayer(player);

    }

    public void killTheNotificaiton(){
        if (player != null) {
            playerNotificationManager.setPlayer(null);
            player.release();
            player = null;
        }
    }

    public class MyBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }
}
