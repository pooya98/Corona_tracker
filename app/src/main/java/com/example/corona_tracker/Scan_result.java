package com.example.corona_tracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

public class Scan_result extends AppCompatActivity {
    TextView TV_building_name, TV_enter_date, TV_enter_time, TV_student_id, TV_student_phone;
    Button BT_save, BT_cancel;
    String building_id;
    String phone;
    private SharedPreferences appData;
    TelephonyManager tele;
    DAO dao = new DAO();
    boolean save_success;

    String date;
    String time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);

        TV_building_name = (TextView)findViewById(R.id.TV_building_name);
        TV_enter_date = (TextView)findViewById(R.id.TV_enter_date);
        TV_enter_time = (TextView)findViewById(R.id.TV_enter_time);
        TV_student_id = (TextView)findViewById(R.id.TV_student_id);
        TV_student_phone = (TextView)findViewById(R.id.TV_student_phone);

        Intent intent = getIntent();
        building_id = intent.getStringExtra("parameter_building_id");

        TV_building_name.setText(dao.get_building_name(building_id));

        SimpleDateFormat format2 = new SimpleDateFormat ( "yyyy년 MM월 dd일 HH시 mm분 ss초");

        String format_time2 = format2.format (System.currentTimeMillis());

        System.out.println(format_time2);

        TV_enter_date.setText(format_time2.substring(0,13));
        TV_enter_time.setText(format_time2.substring(13));

        date = format_time2.substring(0,4)+"-"+format_time2.substring(6,8)+"-"+format_time2.substring(10,12);
        time = format_time2.substring(14,16)+":"+format_time2.substring(18,20)+":"+format_time2.substring(22,24);

        System.out.println("ffff"+date);
        System.out.println("ffff"+time);

        appData = getSharedPreferences("appData", MODE_PRIVATE);
        TV_student_id.setText(appData.getString("studentID",""));


        tele = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        }
        else{
            phone = tele.getLine1Number(); // TelephonyManager ?댁슜?섏뿬 String?쇰줈 ?꾪솚.
            if(phone.startsWith("+82")) phone = phone.replace("+82", "0"); // ?꾪솕踰덊샇 "+82"濡??쒖옉??+82 ??0 ?쇰줈 諛붽퓞.
//            else if(phone.startsWith("+15"))   phone = phone.replace("+15", "0");
        }
        TV_student_phone.setText(phone);
    }

    public void onclick_BT_cancel(View view) {
        Toast.makeText(this,"취소",Toast.LENGTH_SHORT).show();
        finish();
    }

    public void onclick_BT_save(View view) {

        if(dao.insert_enter_info(building_id,date,time,TV_student_id.getText().toString(),TV_student_phone.getText().toString())){
            Toast.makeText(this,"저장 성공",Toast.LENGTH_SHORT).show();
            finish();
        }else{
            Toast.makeText(this,"저장 실패",Toast.LENGTH_SHORT).show();
            finish();
        }

    }

}