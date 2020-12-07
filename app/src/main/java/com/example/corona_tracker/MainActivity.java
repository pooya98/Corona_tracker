package com.example.corona_tracker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.corona_tracker.AdminClass.LogListAdapter;
import com.example.corona_tracker.AdminClass.LogListItem;
import com.example.corona_tracker.AdminClass.NotiData;
import com.example.corona_tracker.AdminClass.NotiListAdapter;
import com.example.corona_tracker.AdminClass.NotiListItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private long lastTimeBackPressed;

    ArrayList<NotiListItem> list;
    ArrayList<NotiData> datalist;
    RecyclerView recyclerView;
    NotiListAdapter listAdapter;
    DAO dao = null;

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

        dao = new DAO();
        init_list();
        set_item();
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

    private void init_list(){
        list = new ArrayList<>();
        datalist = new ArrayList<>();
        listAdapter = new NotiListAdapter(this, list, datalist, false);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);

        recyclerView = findViewById(R.id.student_noti);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(listAdapter);
    }

    public void set_item(){
        list.clear();
        setNotiItems();
        for(int i = 0; i < datalist.size(); i++){
            NotiListItem item = new NotiListItem();

            item.setTitle(datalist.get(i).getTitle());
            item.setContent(datalist.get(i).getContent());
            item.setDate(datalist.get(i).getTimeToText());

            list.add(item);
        }

        listAdapter.notifyDataSetChanged();
    }

    private void setNotiItems(){
        ArrayList<NotiData> list = dao.get_notiData_array();
        datalist.clear();
        if(list == null) return;
        for(NotiData d : list){
            datalist.add(d);
        }
    }
}