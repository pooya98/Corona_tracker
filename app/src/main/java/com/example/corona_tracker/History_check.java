package com.example.corona_tracker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.zip.Deflater;

public class History_check extends AppCompatActivity {

    ListView listView;
    ArrayList<String> aryList = new ArrayList<String>();
    private TextView textView_Date, textView_no_result;
    ArrayAdapter<String> adapter;

    private DatePickerDialog.OnDateSetListener callbackMethod;

    int select_year;
    int select_month;
    int select_day;
    String select_yoil;

    HttpPost httppost;
    HttpResponse response;
    HttpClient httpClient;
    List<NameValuePair> nameValuePairs;


    private SharedPreferences appData;
    TelephonyManager tele;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_check);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("HISTORY CHECK");
        ab.setDisplayHomeAsUpEnabled(true);

        textView_no_result = (TextView)findViewById(R.id.no_result);
        textView_no_result.setVisibility(View.INVISIBLE);

        this.InitializeView();
        this.InitializeListener();


        /* 현재 날짜 정보 저장 */
        Calendar cal = Calendar.getInstance();
        select_year = cal.get(Calendar.YEAR);
        select_month = cal.get(Calendar.MONTH) + 1;
        select_day = cal.get(Calendar.DAY_OF_MONTH);

        try {
            select_yoil = getDateDay(select_year + "-" + select_month + "-" + select_day, "yyyy-MM-dd");
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*                      */

        textView_Date.setText(select_year + "년 " + select_month + "월 " + select_day + "일" + " (" + select_yoil + ")");



        listView = (ListView) findViewById(R.id.listView);


        adapter = new ArrayAdapter<String>(
                this,
                R.layout.item,
                R.id.name,
                aryList
        );

    }


    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    ;

    public void InitializeView() {
        textView_Date = (TextView) findViewById(R.id.textView_date);
    }

    public void InitializeListener() {
        callbackMethod = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                select_year = year;
                select_month = monthOfYear + 1;
                select_day = dayOfMonth;

                try {
                    select_yoil = getDateDay(select_year + "-" + select_month + "-" + select_day, "yyyy-MM-dd");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                textView_Date.setText(select_year + "년 " + select_month + "월 " + select_day + "일" + " (" + select_yoil + ")");
            }
        };
    }

    public void onclick_date_bt(View view) {
        DatePickerDialog dialog = new DatePickerDialog(this, callbackMethod, select_year, select_month - 1, select_day);

        dialog.show();
    }

    public static String getDateDay(String date, String dateType) throws Exception {

        String day = "";

        SimpleDateFormat dateFormat = new SimpleDateFormat(dateType);
        Date nDate = dateFormat.parse(date);

        Calendar cal = Calendar.getInstance();
        cal.setTime(nDate);

        int dayNum = cal.get(Calendar.DAY_OF_WEEK);

        switch (dayNum) {
            case 1:
                day = "일";
                break;
            case 2:
                day = "월";
                break;
            case 3:
                day = "화";
                break;
            case 4:
                day = "수";
                break;
            case 5:
                day = "목";
                break;
            case 6:
                day = "금";
                break;
            case 7:
                day = "토";
                break;

        }

        return day;
    }

    public void onclick_BT_show_enter_info(View view) {
        aryList.clear();
        Thread searchThread = new Thread(){
            public void run(){
                contact_server_for_search();
            }
        };

        searchThread.start();
        System.out.println("--- loadThread go!");

        try{
            searchThread.join();
            System.out.println("--- loadThread done!");
        }catch(Exception e){
            e.getMessage();
        }


        listView.setAdapter(adapter);
    }

    void contact_server_for_search(){
        System.out.println("--- 스레드 시작");

        String date = select_year+"-"+select_month+"-"+select_day;
        String student_id;
        String student_phone="";

        appData = getSharedPreferences("appData", MODE_PRIVATE);
        student_id = appData.getString("studentID","");
        tele = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        }
        else{
            student_phone = tele.getLine1Number(); // TelephonyManager ?댁슜?섏뿬 String?쇰줈 ?꾪솚.
            if(student_phone.startsWith("+82")) student_phone = student_phone.replace("+82", "0"); // ?꾪솕踰덊샇 "+82"濡??쒖옉??+82 ??0 ?쇰줈 諛붽퓞.
        }



        try{
            httpClient = new DefaultHttpClient();
            httppost = new HttpPost("http://14.45.108.71:80/corona_tracker/show_enter_info.php");
            nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("date",date));
            nameValuePairs.add(new BasicNameValuePair("student_id",student_id));
            nameValuePairs.add(new BasicNameValuePair("student_phone",student_phone));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));
            response = httpClient.execute(httppost);

            Scanner scan = new Scanner(response.getEntity().getContent());

            String response_string = "";
            while(scan.hasNext()){
                response_string += scan.nextLine();
            }

            System.out.println("---Response : "+response_string);

            String[] token;
            token = response_string.split("\\^");

            if(token[0].equals("")){
                textView_no_result.setVisibility(View.VISIBLE);
            }
            else {
                textView_no_result.setVisibility(View.INVISIBLE);
                for (int i = 0; i < token.length; i++) {
                    System.out.println("token " + i + " : " + token[i]);
                    aryList.add("      "+token[i].substring(0,8)+"      "+token[i].substring(8));
                }
            }
        }catch (Exception e){
            System.out.println("Exception : "+e.getMessage());
        }
    }
}