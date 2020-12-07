package com.example.corona_tracker.AdminClass;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.corona_tracker.R;

public class AdminActivity extends AppCompatActivity {
    final long FINISH_INTERVAL_TIME = 2000;
    long backPressedTime = 0;
    NotiFragment notiFragment;
    LogFragment logFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        final FragmentManager fm = getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fm.beginTransaction();
        notiFragment = new NotiFragment();
        logFragment = new LogFragment();

        fragmentTransaction.add(R.id.main_frame, notiFragment).commit();

        final Button noti_b = findViewById(R.id.noti_button);
        final Button log_b = findViewById(R.id.log_button);
        noti_b.setPaintFlags(noti_b.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        noti_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noti_b.setPaintFlags(noti_b.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                log_b.setPaintFlags(noti_b.getPaintFlags() & (-1 ^ Paint.UNDERLINE_TEXT_FLAG));
                replaceFragment(notiFragment);
            }
        });

        log_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                log_b.setPaintFlags(log_b.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                noti_b.setPaintFlags(noti_b.getPaintFlags() & (-1 ^ Paint.UNDERLINE_TEXT_FLAG));
                replaceFragment(logFragment);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && resultCode == RESULT_OK){
            notiFragment.set_item();
        }
    }

    void replaceFragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        //fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;
        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            super.onBackPressed();
        }
        else {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "뒤로가기 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }
}