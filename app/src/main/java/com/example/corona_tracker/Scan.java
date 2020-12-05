package com.example.corona_tracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.corona_tracker.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// QR코드 입장시 QR코드 입력받고 고유번호를 추출하는 액티비티
// layout file : activity_enter_qrcode.xml

public class Scan extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 1;

    // QR코드 스캔하는 객체
    private IntentIntegrator qrScan;
    String qrcode_result;  // QR코드 스캔 결과를 저장하는 문자열 ( QR 코드의 내용은 입장자 고유번호 )

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        // QR 코드 스캐너 설정 및 실행
        qrScan = new IntentIntegrator(this);
        qrScan.setCameraId(0);   // 전면 카메라로 설정
        qrScan.setOrientationLocked(true);   // default가 세로모드인데 휴대폰 방향에 따라 가로, 세로로 자동 변경됩니다.
        qrScan.setPrompt("QR코드 인식");   // QR 스캐너 실행되는 동안 띄우는 안내 문구 설정
        qrScan.initiateScan();    // QR코드 스캐너 실행


        //디바이스 id 권한 허용
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(Scan.this,
                    Manifest.permission.READ_PHONE_STATE)) {

            } else {
                ActivityCompat.requestPermissions(Scan.this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
            }
        }


    }

    // 디바이스 id 권한 허용
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_PHONE_STATE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;
            }
        }
    }

    // QR 코드 스캔 완료 후 실행되는 메소드
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        DAO dao = new DAO();

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);  // QR 스캔 결과 저장

        if (result != null) {  // QR 코드가 정상적으로 스캔된 경우

            if (result.getContents() == null) {   // QR 코드 스캔 값이 null인 경우
                Toast.makeText(Scan.this, "취소!", Toast.LENGTH_SHORT).show();
                finish();   // 액티비티 종료

            } else {  // QR 코드 스캔 값이 null이 아닌 경우
                Toast.makeText(Scan.this, "스캔완료!", Toast.LENGTH_SHORT).show();

                try {
                    qrcode_result = result.getContents();    // QR 코드 스캔 값을 문자열에 저장

                    System.out.println("show scan result : " + qrcode_result);

                    if (dao.check_building_id(qrcode_result)) {
                        Intent myintent = new Intent(Scan.this, Scan_result.class);
                        myintent.putExtra("parameter_building_id", qrcode_result);
                        startActivity(myintent);
                        finish();
                    } else {
                        showAlert_WrongQRcode();
                    }
                }catch (Exception E){
                    E.printStackTrace();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    // 잘못된 QR 코드가 스캔된 경우 출력되는 팝업메시지
    public void showAlert_WrongQRcode() {
        Scan.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(Scan.this);
                builder.setTitle(" ");
                builder.setMessage("잘못된 QR코드입니다.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    // 네트워크 연결 없을 경우 출력되는 팝업메시지
    public void showAlert_NoConnection() {
        Scan.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(Scan.this);
                builder.setTitle(" ");
                builder.setMessage("네트워크 연결을 확인하십시오.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }
}