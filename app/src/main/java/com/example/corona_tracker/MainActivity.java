package com.example.corona_tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, com.example.corona_tracker.Loading.class);
        startActivity(intent);
    }

    public void onclick_scan(View view) {
        Intent intent = new Intent(this, com.example.corona_tracker.Scan.class);
        startActivity(intent);
    }

    public void onclick_Lookup(View view) {
        Intent intent = new Intent(this, com.example.corona_tracker.History_check.class);
        startActivity(intent);
    }
}