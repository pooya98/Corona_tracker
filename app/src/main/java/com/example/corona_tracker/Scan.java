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


    // http 통신 관련 객체
    HttpPost httppost;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;


    // QR코드 스캔하는 객체
    private IntentIntegrator qrScan;

    String user_name;       // 입장자 이름
    String user_birth;      // 입장자 생년월일
    String user_phone;      // 입장자 전화번호
    String user_purpose;    // 입장자 방문목적
    String user_m_number;   // 입장자 고유번호
    String user_createtime; // 입장자 등록일시

    String company_number;  // 업체 고유번호 ( 업체 구분을 위함 )
    String company_type;    // 업체 type
    String company_id;      // 업체 ID ( 업체 내 출입문 구분을 위함 )

    boolean search_flag = false;  // DB에서 고유번호 검색 성공 여부 flag
    String qrcode_result;  // QR코드 스캔 결과를 저장하는 문자열 ( QR 코드의 내용은 입장자 고유번호 )

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        // 전달받은 인자 저장
        Intent intent = getIntent();
        company_number = intent.getStringExtra("parameter_company_number");
        company_type = intent.getStringExtra("parameter_company_type");
        company_id = intent.getStringExtra("parameter_company_id");

        System.out.println("전달된 인자 확인 : "+ company_number+company_type+company_id);



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

        final int qrcode_correct_length = 20;    // 정상적인 QR코드 결과의 문자열 길이


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

                        int length = qrcode_result.length();

                        if (length == qrcode_correct_length) {   // QR 코드 스캔 문자열 길이 확인

                            // 필요없는 부분 떼어내기 (실제 qrcode 스캔 값 예시 - /!/200813000004/!@!/ )
                            // 앞 부분의 /!/와 뒷부분의 /!@!/를 뗴어냄
                            // 필요없는 부분 떼어내고 사용자 고유번호를 추출
                            qrcode_result = qrcode_result.substring(3, length - 5);


                            // http 통신을 위한 스레드 생성
                            Thread mythread = new Thread() {
                                public void run() {
                                    contact_server();
                                }
                            };

                            // 스레드 start
                            mythread.start();
                            System.out.println("---스레드 시작 명령");

                            // 스레드 join
                            try {
                                mythread.join();
                                System.out.println("---스레드 종료 확인");
                            } catch (Exception e) {
                                e.getMessage();
                            }

                            // DB에서 고유번호 검색이 성공한 경우
                            if (search_flag == true) {

                                // Enter_QrCodeResult ( Qr코드 조회 결과 표출 ) 를 전환할 액티비티로 설정
                                Intent myintent = new Intent(Scan.this, Scan_result.class);

                                // Enter_QrCodeResult 로 전달할 인자 설정
                                myintent.putExtra("parameter_user_name", user_name);
                                myintent.putExtra("parameter_user_birth", user_birth);
                                myintent.putExtra("parameter_user_phone", user_phone);
                                myintent.putExtra("parameter_user_purpose", user_purpose);
                                myintent.putExtra("parameter_user_m_number", user_m_number);
                                myintent.putExtra("parameter_user_createtime", user_createtime);
                                myintent.putExtra("parameter_company_number", company_number);
                                myintent.putExtra("parameter_company_type", company_type);
                                myintent.putExtra("parameter_company_id", company_id);

                                // 액티비티 전환 실행
                                startActivity(myintent);
                                finish();

                            }

                        } else {  // QR 코드 문자열 길이가 올바르지 않은 경우 처리 ( 잘못된 QR코드인 경우 )
                            showAlert_WrongQRcode();
                        }
                    } catch (Exception e) {

                    }
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }

    }

    // apache tomcat 서버와 통신하는 메서드 ( 스레드로 실행 )
    void contact_server() {
        final int parameter_number = 6;

        System.out.println("---스레드 시작");
        try {

            httpclient = new DefaultHttpClient();
            httppost = new HttpPost("http://iamsol.kr/jsol/identify_app2");  // jsp주소 설정

            // jsp로 전달할 인자 설정
            nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("qrimage", qrcode_result));
            nameValuePairs.add(new BasicNameValuePair("c_number", company_number));
            nameValuePairs.add(new BasicNameValuePair("c_type", company_type));

            // httpclient 실행
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            response = httpclient.execute(httppost);



            // ---------------- 반환 받은 String 파싱 과정 시작 ---------------- //
            Scanner scan = new Scanner(response.getEntity().getContent());

            String response_string = "";
            while (scan.hasNext()) {
                response_string = scan.nextLine();
            }
            response_string = response_string.trim();
            System.out.println("Response!!! : " + response_string);

            String[] token;
            token = response_string.split(",");

            for (int i = 0; i < token.length; i++) {
                System.out.println("token " + i + " : " + token[i]);
            }
            // ---------------- 반환 받은 String 파싱 과정 완료 ---------------- //




            // 올바르게 반환을 받은 경우 처리
            if (token.length == parameter_number) {
                search_flag = true;
                user_name = token[0];
                user_birth = token[1];
                user_phone = token[2];
                user_purpose = token[3];
                user_m_number = token[4];
                user_createtime = token[5];
            }
            else {
                if (token[0].equals("2")) {  // httpclient 실행 결과 "2"인 경우 처리 ( 고유번호 조회 결과 없음 )
                    search_flag = false;
                    showAlert_WrongQRcode();
                } else if (token[0].equals("중복")) {  // httpclient 실행 결과 "중복"인 경우 처리 ( 5초 이내 입장자 조회한 경우 )
                    search_flag = false;
                    showAlert_EnterAgain();
                }
            }

            System.out.println("---스레드 종료");

        } catch (Exception e) {
            System.out.println("Exception! : " + e.getMessage());
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

    // 고유번호 조회 결과가 없는 경우 출력 되는 팝업메시지
    public void showAlert_NoResult() {
        Scan.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(Scan.this);
                //Toast.makeText(Phoneread.this,textView_Phone.getText().toString(), Toast.LENGTH_SHORT).show();
                builder.setTitle("정보확인 실패");
                builder.setMessage("미등록된 사용자입니다. 등록 후 사용해주세요.")
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

    // 5초 이내 입장한 사용자가 다시 조회한 경우 출력되는 팝업메시지
    public void showAlert_EnterAgain() {
        Scan.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(Scan.this);
                builder.setTitle("중복 입장!");
                builder.setMessage("5초 이내에 입장한 사용자 입니다.")
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