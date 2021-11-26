package com.example.c01_blackbox;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;

public class SelectDirectory extends AppCompatActivity {
    private File file;
    private ArrayList myList;
    private ArrayAdapter mAdapter;
    private ListView myListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_list);

        myList = new ArrayList();
        String rootSD = Util.storageDir.toString();
        file = new File(rootSD);

        File list[] = file.listFiles();

//        for (int i = 0; i < list.length; i++) {
//            myList.add(list[i].getName());
//            System.out.println(list[i].getName());
//        }

        mAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);

        myListView = findViewById(R.id.videoListView);
        myListView.setAdapter(mAdapter);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg4) {
//                타입 java.io.File
//                parent.getItemAtPosition(position).getClass().getName()

                String item = String.valueOf(parent.getItemAtPosition(position));

                Intent intent = new Intent(getApplicationContext(), VideoPlay.class);

                intent.putExtra("video", item);

                startActivity(intent);

            }
        });

    }
}
