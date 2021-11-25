package com.example.c01_blackbox;

import android.os.Environment;

import com.hbisoft.hbrecorder.HBRecorder;

import java.io.File;

public class Util {

    public static String APP_TITLE = "BlackBoxApp";
    public static File storageDir = new File(Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_MOVIES), APP_TITLE);


    public static void setOutputPath(HBRecorder hbRecorder) {
        try {
            if (!storageDir.exists()) {
                storageDir.mkdirs();
            }

            hbRecorder.setOutputPath(storageDir.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
