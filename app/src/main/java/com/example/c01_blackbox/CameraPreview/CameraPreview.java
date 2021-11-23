package com.example.c01_blackbox.CameraPreview;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.c01_blackbox.R;

public class CameraPreview extends AppCompatActivity {

    CameraSurfaceView cameraSurfaceView;
    Button button_record;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_preview);

        hideSystemUI();

        cameraSurfaceView = new CameraSurfaceView(this);

        CoordinatorLayout layout_camera = findViewById(R.id.layout_camera);
        layout_camera.addView(cameraSurfaceView);

        button_record = findViewById(R.id.button_record);
        button_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button_record.setVisibility(View.INVISIBLE);
                cameraSurfaceView.startRecording();
            }
        });

        cameraSurfaceView.file = cameraSurfaceView.getOutputFile();
        if (cameraSurfaceView.file != null) {
            cameraSurfaceView.filename = cameraSurfaceView.file.getAbsolutePath();
        }

    }

    public void hideSystemUI() {
//        https://medium.com/marojuns-android/android-immersive-mode-a4ae2065c0a2
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (cameraSurfaceView.isRecording) {
            try {
                cameraSurfaceView.stopRecording();
                Log.d("CameraPreview", "onPause()");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
