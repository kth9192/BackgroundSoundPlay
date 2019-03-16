package com.kth.backgroundsound;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.kth.backgroundsound.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static String TAG = MainActivity.class.getSimpleName();
    private ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        activityMainBinding.goMusic.setOnClickListener(onClickListener);
        activityMainBinding.goVideo.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.go_music:
                    Intent musicIntent = new Intent(MainActivity.this, MusicActivity.class);
                    startActivity(musicIntent);
                    return;
                case R.id.go_video:
                    Intent videoIntent = new Intent(MainActivity.this, VideoActivity.class);
                    startActivity(videoIntent);
                    return;
            }
        }
    };

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
