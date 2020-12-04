package com.example.corona_tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, Loading.class);
        startActivity(intent);

        Intent noti = new Intent(this, NotiService.class);

        if(Build.VERSION.SDK_INT >= 26)
            startForegroundService(noti);
        else
            startService(noti);
    }

    public void onclick_scan(View view) {
        Intent intent = new Intent(this, Scan.class);
        startActivity(intent);
    }

    public void onclick_Lookup(View view) {
        Intent intent = new Intent(this, History_check.class);
        startActivity(intent);
    }
}