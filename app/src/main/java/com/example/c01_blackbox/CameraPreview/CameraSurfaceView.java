package com.example.c01_blackbox.CameraPreview;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    SurfaceHolder surfaceHolder;
    Camera camera;

    MediaRecorder recorder;

    File file;
    String filename;

    String APP_TITLE = "BlackBoxApp";

    boolean isRecording = false;

    public CameraSurfaceView(Context context) {
        super(context);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        camera = Camera.open();
        setCameraOrientation();
        try {
            camera.setPreviewDisplay(surfaceHolder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        camera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    void setCameraOrientation() {
//        https://slenderankle.tistory.com/60

        WindowManager manager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        int rotation = manager.getDefaultDisplay().getRotation();
        int degrees = 0;

        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;

            case Surface.ROTATION_90:
                degrees = 90;
                break;

            case Surface.ROTATION_180:
                degrees = 180;
                break;

            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result = (90 - degrees + 360) % 360;

        camera.setDisplayOrientation(result);
    }

    public void startRecording() {
        if (recorder == null) {
            recorder = new MediaRecorder();
        }

        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        recorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
        recorder.setVideoSize(1280, 720);

//        file = getOutputFile();
//        if (file != null) {
//            filename = file.getAbsolutePath();
//        }

        recorder.setOutputFile(filename);

        recorder.setPreviewDisplay(surfaceHolder.getSurface());

        try {
            recorder.prepare();
            recorder.start();
            isRecording = true;

            Log.d(APP_TITLE, "startRecording()");
        } catch (Exception e) {
            e.printStackTrace();

            recorder.release();
            recorder = null;
        }
    }

    public void stopRecording() {
        if (recorder == null) {
            return;
        }

        recorder.stop();
        recorder.reset();
        recorder.reset();
        recorder = null;

        ContentValues values = new ContentValues(10);

        values.put(MediaStore.MediaColumns.TITLE, APP_TITLE);
        values.put(MediaStore.Audio.Media.ALBUM, APP_TITLE + " Video");
        values.put(MediaStore.Audio.Media.ARTIST, APP_TITLE);
        values.put(MediaStore.Audio.Media.DISPLAY_NAME, APP_TITLE + " Recorded Video");
        values.put(MediaStore.MediaColumns.DATE_ADDED, System.currentTimeMillis() / 1000);
        values.put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4");
        values.put(MediaStore.Audio.Media.DATA, filename);

        Uri videoUri = getContext().getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);

        if (videoUri == null) {
            Log.d("BlackBoxApp", "Video insert failed.");
        }

        getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, videoUri));

        isRecording = false;
    }

    public File getOutputFile() {
        File mediaFile = null;

        try {
            File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), APP_TITLE);

            if (!storageDir.exists()) {
                if (!storageDir.mkdirs()) {
                    Log.d(APP_TITLE, "failed to create directory");
                    return null;
                }
            }

            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.KOREA);
            String getTime = dateFormat.format(date);

            mediaFile = new File(storageDir.getPath() + File.separator + getTime + ".mp4");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mediaFile;
    }
}