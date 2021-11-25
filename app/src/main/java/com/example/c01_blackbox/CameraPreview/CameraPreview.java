package com.example.c01_blackbox.CameraPreview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.c01_blackbox.R;
import com.example.c01_blackbox.Util;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.hbisoft.hbrecorder.HBRecorder;
import com.hbisoft.hbrecorder.HBRecorderListener;

import java.io.File;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class CameraPreview extends AppCompatActivity implements HBRecorderListener {

    CameraSurfaceView cameraSurfaceView;
    Button button_record;
    GPS_Fragment gps_fragment;
    SupportMapFragment mapFragment;


    String APP_TITLE = "BlackBoxApp";
    File file;
    String filename;

    HBRecorder hbRecorder;

    private static final int SCREEN_RECORD_REQUEST_CODE = 777;
    private static final int PERMISSION_REQ_ID_RECORD_AUDIO = 22;
    private static final int PERMISSION_REQ_ID_WRITE_EXTERNAL_STORAGE = PERMISSION_REQ_ID_RECORD_AUDIO + 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_preview);


//        https://github.com/HBiSoft/HBRecorder

        hbRecorder = new HBRecorder(this, this);
        hbRecorder.setScreenDimensions(720, 1280);

        gps_fragment = new GPS_Fragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.gpsFragment, gps_fragment).commit();

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.gpsFragment);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                gps_fragment.setMap(googleMap);
            }
        });
        startLocationService(gps_fragment);


        hideSystemUI();

        cameraSurfaceView = new CameraSurfaceView(this);

        CoordinatorLayout layout_camera = findViewById(R.id.layout_camera);
        layout_camera.addView(cameraSurfaceView);

        button_record = findViewById(R.id.button_record);
        button_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button_record.setVisibility(View.INVISIBLE);
                if (hbRecorder.isBusyRecording()) {
                    hbRecorder.stopScreenRecording();
                }
                startRecordingScreen();
            }
        });

        Util.setOutputPath(hbRecorder);
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
        hbRecorder.stopScreenRecording();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @SuppressLint("MissingPermission")
    public void startLocationService(GPS_Fragment gps_fragment) {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        try {
            long minTime = 1000;
            float minDistance = 0;

            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, gps_fragment);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void startRecordingScreen() {
        MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        Intent permissionIntent = mediaProjectionManager != null ? mediaProjectionManager.createScreenCaptureIntent() : null;
        startActivityForResult(permissionIntent, SCREEN_RECORD_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SCREEN_RECORD_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                //Start screen recording
                hbRecorder.startScreenRecording(data, resultCode, this);
            }
        }
    }

    @Override
    public void HBRecorderOnStart() {
        hideSystemUI();
    }

    @Override
    public void HBRecorderOnComplete() {

    }

    @Override
    public void HBRecorderOnError(int errorCode, String reason) {

    }
}
