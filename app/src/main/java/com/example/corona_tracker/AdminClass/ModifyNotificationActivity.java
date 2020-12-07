package com.example.corona_tracker.AdminClass;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.corona_tracker.DAO;
import com.example.corona_tracker.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ModifyNotificationActivity extends AppCompatActivity {

    TextView dateTextView = null;
    EditText titleEdit = null;
    EditText contentEdit = null;

    MenuItem editMenu, saveMenu;

    private int idx;
    private boolean isAdmin;
    DAO dao;

    private boolean isModified = false, isSaved = false;
    private boolean isReadOnly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_noti);
        dao = new DAO();

        initView();
    }

    // 뷰 초기 설정
    private void initView(){
        // 현재 공지의 index를 불러온다; 새로 만드는 공지일 경우 -1을 반환
        Intent intent = getIntent();
        idx = intent.getExtras().getInt("idx", -1);
        isAdmin = intent.getExtras().getBoolean("admin", false);

        dateTextView = findViewById(R.id.noti_recent_modified);

        titleEdit = findViewById(R.id.edit_title);
        titleEdit.addTextChangedListener(watcher);
        contentEdit = findViewById(R.id.edit_content);
        contentEdit.addTextChangedListener(watcher);

        // 화면을 세로 고정하고 actionbar 을 커스텀
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().setElevation(0f);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // 공지를 불러온것이면 기존 데이터를 채워넣는다
        if(idx != -1){
            inputEditData();
            isReadOnly = true;
        }
        else {
            isReadOnly = false;
            dateTextView.setVisibility(View.GONE);
        }

        if(!isAdmin){
            titleEdit.setFocusable(false);
            contentEdit.setFocusable(false);
            isReadOnly = true;
            getSupportActionBar().setTitle("상세보기");
        }

        isModified = false;
    }

    // 기존 공지를 TextView에 채워넣는 메소드; 공지를 새로 만드는 경우에는 호출하지 않는다
    private void inputEditData(){
        NotiData data = dao.get_notiData(idx);
        titleEdit.setText(data.getTitle());
        data.setContent(data.getContent().replaceAll("!@#////", System.getProperty("line.separator")));
        contentEdit.setText(data.getContent() + "");
        String dateString = "작성일 : " + new SimpleDateFormat("yyyy.MM.dd HH:mm").format(data.getTime().getTime());
        if(data.getLastTime() != null && !data.getTime().equals(data.getLastTime())) dateString += "\n" + "수정일 : " + new SimpleDateFormat("yyyy.MM.dd HH:mm").format(data.getLastTime().getTime());

        dateTextView.setText(dateString);
    }

    // 액션바에 있는 메뉴 설정; 공지를 처음 만들경우 WritableMode, 기존 공지를 확인하는 경우 ReadOnlyMoce
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(isAdmin){
            getMenuInflater().inflate(R.menu.menu_noti, menu);
            editMenu = menu.findItem(R.id.m_edit_noti);
            saveMenu = menu.findItem(R.id.m_save_noti);

            if(idx == -1){        // when adding noti
                changeToWritableMode();
            }
            else{                   //when seeing noti
                changeToReadOnlyMode();
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    // 상단 ActionBar의 메뉴의 기능 설정
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        hideKeyboard();
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.m_edit_noti:
                changeToWritableMode();
                return true;

            case R.id.m_save_noti:
                if(isModified) {
                    if(checkCanSave() == 0){
                        // 공지가 수정되었고 저장 가능하면 저장 후 ReadOnlyMode
                        saveNoti();
                        changeToReadOnlyMode();
                    }
                    else{
                        // 저장 불가시 (제목 또는 내용이 비어있을 때)
                        showToast("제목과 내용을 확인해주세요.", Toast.LENGTH_SHORT);
                    }
                }
                else {
                    // 수정되지 않았으면 바로 ReadOnlyMode
                    showToast("변경사항이 없어 저장되지 않았습니다.", Toast.LENGTH_SHORT);
                    changeToReadOnlyMode();
                }
                return true ;

            case R.id.m_delete_noti:
                DialogInterface.OnClickListener positive = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(idx != -1) {
                            dao.delete_notice(idx);
                            setResult(RESULT_OK);
                        }
                        showToast("공지를 삭제하였습니다.", Toast.LENGTH_SHORT);
                        finish();
                    }
                };
                createDialog("공지 삭제", "공지를 삭제하시겠습니까?", "예", positive, "아니요", null).show();
                return true;
            default :
                return super.onOptionsItemSelected(item);
        }
    }

    //공지 상세보기 <-> 공지 수정
    private void changeToReadOnlyMode(){
        hideKeyboard();
        editMenu.setVisible(true);
        saveMenu.setVisible(false);
        titleEdit.setFocusable(false);
        contentEdit.setFocusable(false);
        isReadOnly = true;
        getSupportActionBar().setTitle("상세보기");
    }

    private void changeToWritableMode(){
        titleEdit.setFocusableInTouchMode(true);
        contentEdit.setFocusableInTouchMode(true);
        titleEdit.setFocusable(true);
        contentEdit.setFocusable(true);

        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        titleEdit.requestFocus();
        imm.showSoftInput(titleEdit, InputMethodManager.SHOW_IMPLICIT);

        editMenu.setVisible(false);
        saveMenu.setVisible(true);
        isReadOnly = false;
        if(idx != -1) getSupportActionBar().setTitle("공지 수정");
        else getSupportActionBar().setTitle("공지 추가");
    }

    // 제목 또는 내용이 비어있으면 저장 불가; 그 외에는 저장가능
    private int checkCanSave(){
        if(titleEdit.getText().toString().equals("")) return 1;
        else if(contentEdit.getText().toString().equals("")) return 2;
        else return 0;
    }

    // 공지를 저장하는 메소드
    private void saveNoti(){
        boolean result;
        int canSave = checkCanSave();
        if(canSave != 0){
            return;
        }

        isModified = false;
        isSaved = true;

        // DB에 공지를 저장; 작성일 이외의 데이터를 업데이트
        Calendar toSaveCalendar = Calendar.getInstance(), lastDate = Calendar.getInstance();

        if(idx != -1){
            NotiData oldData = dao.get_notiData(idx);
            toSaveCalendar = oldData.getTime();
            lastDate = Calendar.getInstance();
        }
        NotiData data = new NotiData(toSaveCalendar, lastDate, titleEdit.getText().toString(), contentEdit.getText().toString().replaceAll("\n", "!@#////"), "manager", idx);

        // 공지가 처음 만든것이면 DB에 추가; 수정중이면 업데이트
        if(idx == -1) {
            result = dao.insert_notice(data);
        }
        else{
            result = dao.update_notice(data);
        }

        String dateString = "작성일 : " + new SimpleDateFormat("yyyy.MM.dd HH:mm").format(data.getTime().getTime());

        dateTextView.setVisibility(View.VISIBLE);
        if(result){
            dateTextView.setText(dateString);
            showToast("공지를 저장하였습니다.", Toast.LENGTH_SHORT);
        }
        else{
            showToast("오류가 생겨 저장에 실패했습니다.", Toast.LENGTH_SHORT);
        }
        return;
    }

    // 현재 액티비티 종료 관련
    @Override
    public void onBackPressed() {
        hideKeyboard();
        String message = "";
        int canSave = checkCanSave();
        if(canSave == 1) message = "제목";
        else if (canSave == 2) message = "내용";

        // 공지 내용이 수정되었을때 저장가능한지 체크하고 저장 / 폐기한다
        if (isModified) {

            // 저장할 수 없을때
            if(canSave != 0){
                DialogInterface.OnClickListener positive = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setResult(RESULT_CANCELED);
                        finish();
                    }
                };
                createDialog("나가기", message + "이 비어있어 저장할 수 없습니다.\n저장하지 않고 나가시겠습니까?", "예", positive, "아니요", null).show();
            }

            // 저장 가능할 때
            else {
                onModiFiedAndCanSave();
            }
        }

        // 수정되지 않았을 시 현재 액티비티를 종료
        else {
            exitActivity();
        }
    }

    // 공지가 수정 & 저장 되었는지 체크 후 종료
    private void exitActivity(){
        if(isSaved) { setResult(RESULT_OK); }
        else if (!isSaved && !isReadOnly){
            showToast("변경사항이 없어 저장되지 않았습니다.", Toast.LENGTH_SHORT);
            setResult(RESULT_CANCELED);
        }
        finish();
    }

    // 공지가 수정되었고 저장가능할 때 호출
    private void onModiFiedAndCanSave(){
        // 공지를 처음 만들었으면 저장하고 나간다
        if(idx == -1) {
            saveNoti();
            setResult(RESULT_OK);
            finish();
        }

        // 공지를 수정중이면 물어보고 결정
        else {
            final CharSequence[] items =  {"저장하고 나가기", "저장하지 않고 나가기", "취소"};
            AlertDialog.Builder oDialog = new AlertDialog.Builder(this)
                    .setTitle("공지를 저장하시겠습니까?")
                    .setItems(items, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int pos) {
                            switch (pos){
                                case 0:
                                    saveNoti();
                                    setResult(RESULT_OK);
                                    finish();
                                    break;
                                case 1:
                                    if(isSaved)
                                        setResult(RESULT_OK);
                                    else
                                        setResult(RESULT_CANCELED);
                                    finish();
                                    break;
                            }
                        }
                    })
                    .setCancelable(false);
            oDialog.show();
        }
    }

    // 키보드를 내리는 메소드
    private void hideKeyboard(){
        if(getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    // Toast를 띄우는 메소드
    private void showToast(String str, int duration){
        Toast t = Toast.makeText(getApplicationContext(), str, duration);
        t.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, t.getYOffset());
        t.show();
    }

    // Dialog를 띄우는 메소드
    private AlertDialog createDialog(String title, String msg, String posMsg, DialogInterface.OnClickListener positive, String nagMsg, DialogInterface.OnClickListener negative){
        AlertDialog.Builder oDialog = new AlertDialog.Builder(this);
        oDialog.setTitle(title)
                .setMessage(msg)
                .setNegativeButton(posMsg, positive)
                .setCancelable(false);
        if(nagMsg != null) oDialog.setPositiveButton(nagMsg, negative);
        return oDialog.create();
    }

    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            isModified = true;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}