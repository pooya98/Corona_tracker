package com.example.corona_tracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.corona_tracker.AdminClass.AdminActivity;

public class login extends AppCompatActivity {
    boolean islogin;
    String mode = "student";
    String studentID, adminID, password; // 학번, 관리자ID, 관리자 비밀번호
    EditText edit_studentID, edit_adminID, edit_password;
    private boolean saveLoginData;
    private SharedPreferences appData;


    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //디바이스 id 권한 허용
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_PHONE_STATE)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
            }
        }


        appData = getSharedPreferences("appData", MODE_PRIVATE);
        load();

        if(saveLoginData == true){
            complete(true);
        }

        islogin = false;
        mode = "student";
        changeMode(R.id.login_student);
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

    public void setMode(View view) { // 학생(관리자)로 모드 전환 버튼 이벤트
        int id = view.getId();
        switch(id){
            case R.id.login_student:
                mode = "student";
                adminID = password = null;
                break;
            case R.id.login_admin:
                mode = "admin";
                studentID = null;
                break;
        }
        changeMode(id);
    }

    public void changeMode(int id){ // 학생(관리자)로 모드 전환(frame)
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.login_frame);

        if(frameLayout.getChildCount() > 0){
            frameLayout.removeViewAt(0);
        }

        View view = null;
        switch(id){
            case R.id.login_student:
                view = inflater.inflate(R.layout.login_student_frame, frameLayout, false);
                studentID = null;
                break;

            case R.id.login_admin:
                view = inflater.inflate(R.layout.login_admin_frame, frameLayout, false);
                adminID = password = null;
                break;
        }

        if(view != null){
            frameLayout.addView(view);
        }
    }

    public void login(View view) { // 입력된 정보로 로그인 시도
        int id = view.getId();
        DAO dao = new DAO();

        switch (id){
            case R.id.confirm_student: // 학생으로 로그인
                edit_studentID = (EditText) findViewById(R.id.editText_studentID);
                studentID = edit_studentID.getText().toString();
                if(studentID.length() == 0){ // 입력되어 있지 않음
                    Toast.makeText(this,"학번을 입력해주세요.",Toast.LENGTH_SHORT).show();
                    islogin = false;
                    return;
                }
                if(studentID.length()!= 10){
                    Toast.makeText(this,"유효하지 않은 학번입니다.\n 다시 학번을 입력해주세요.",Toast.LENGTH_SHORT).show();
                    edit_studentID.setText("");
                    islogin = false;
                    return;
                }
                break;
            case R.id.confirm_admin: // 관리자로 로그인
                edit_adminID = (EditText)findViewById(R.id.editText_adminID);
                edit_password = (EditText)findViewById(R.id.editText_password);
                adminID = edit_adminID.getText().toString();
                password = edit_password.getText().toString();
                if(adminID.length() == 0 || password.length() == 0){ // 입력되어 있지 않음
                    Toast.makeText(this,"ID/password가 입력되지 않았습니다.",Toast.LENGTH_SHORT).show();
                    islogin = false;
                    return;
                }

                if(!dao.check_admin_account(adminID, password)){
                    Toast.makeText(this,"존재하지 않는 ID 이거나 Password가 틀렸습니다.",Toast.LENGTH_SHORT).show();
                    islogin = false;
                    edit_password.setText("");
                    return;
                }
                break;
        }

        islogin = true; // 로그인 완료
        complete(islogin);
    }

    public void complete(boolean islogin){ // 로그인 완료 함수 ( finish )

        if(mode.equals("student")) {
            SharedPreferences.Editor editor = appData.edit();
            editor.putBoolean("SAVE_LOGIN_DATA", true);
            editor.putString("SAVE_LOGIN_MODE", "student");
            editor.putString("studentID", studentID);
            editor.putString("adminID","");
            editor.putString("password","");

            editor.apply();
            Toast.makeText(this,"로그인 성공",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();

        }else{
            SharedPreferences.Editor editor = appData.edit();
            editor.putBoolean("SAVE_LOGIN_DATA", true);
            editor.putString("SAVE_LOGIN_MODE", "admin");
            editor.putString("adminID", adminID);
            editor.putString("password",password);
            editor.putString("studentID", "");

            editor.apply();
            Toast.makeText(this,"로그인 성공",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, AdminActivity.class);
            startActivity(intent);
            finish();
        }
    }

    // 설정값을 불러오는 함수
    private void load() {
        // SharedPreferences 객체.get타입( 저장된 이름, 기본값 )
        // 저장된 이름이 존재하지 않을 시 기본값
        saveLoginData = appData.getBoolean("SAVE_LOGIN_DATA", false);
        mode = appData.getString("SAVE_LOGIN_MODE", "");
        adminID = appData.getString("adminID", "");
        password = appData.getString("password", "");
        studentID = appData.getString("studentID","");
    }
}