package com.example.corona_tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

public class login extends AppCompatActivity {
    boolean islogin;
    String mode = "student";
    String studentID, adminID, password; // 학번, 관리자ID, 관리자 비밀번호
    EditText edit_studentID, edit_adminID, edit_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        islogin = false;
        changeMode(R.id.login_student);
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
        switch (id){
            case R.id.confirm_student: // 학생으로 로그인
                edit_studentID = (EditText) findViewById(R.id.editText_studentID);
                studentID = edit_studentID.getText().toString();
                if(studentID.length() == 0){ // 입력되어 있지 않음
                    Toast.makeText(this,"학번을 입력해주세요.",Toast.LENGTH_SHORT).show();
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
                break;
        }

        islogin = true; // 로그인 완료
        complete(islogin);
    }

    public void complete(boolean islogin){ // 로그인 완료 함수 ( finish )
        if(islogin == true) finish();
    }

}