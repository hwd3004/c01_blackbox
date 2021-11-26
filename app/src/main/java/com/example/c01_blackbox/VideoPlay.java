package com.example.c01_blackbox;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class VideoPlay extends AppCompatActivity {
    Bundle extras;

    VideoView videoView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_play);

        extras = getIntent().getExtras();

        if (extras != null) {
            try {
                String videoPath = String.valueOf(extras.get("video"));
                videoView = findViewById(R.id.videoView);
                Uri uri = Uri.parse(videoPath);

                MediaController mc = new MediaController(this);
                videoView.setMediaController(mc);
                videoView.setVideoURI(uri);
                videoView.requestFocus();
                videoView.start();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
