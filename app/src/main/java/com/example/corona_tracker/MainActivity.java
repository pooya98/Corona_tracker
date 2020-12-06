package com.example.corona_tracker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.corona_tracker.AdminClass.NotiData;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private long lastTimeBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar ab = getSupportActionBar();

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

    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis() - lastTimeBackPressed < 2000){
            finish();
            return;
        }
        lastTimeBackPressed = System.currentTimeMillis();
        Toast.makeText(this,"'뒤로' 버튼을 한 번 더 누르면 종료됩니다.",Toast.LENGTH_SHORT).show();
    }
}