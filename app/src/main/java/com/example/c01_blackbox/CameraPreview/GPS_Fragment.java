package com.example.c01_blackbox.CameraPreview;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.c01_blackbox.R;


public class GPS_Fragment extends Fragment implements LocationListener {

    TextView textView;

    public GPS_Fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gps, container, false);
    }


    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        textView = getActivity().findViewById(R.id.textView);

        String text = "위도 : " + latitude + ", 경도 : " + longitude;

        textView.setText(text);
    }
}