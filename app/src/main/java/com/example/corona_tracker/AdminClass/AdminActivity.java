package com.example.corona_tracker.AdminClass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.corona_tracker.DAO;
import com.example.corona_tracker.R;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {

    ArrayList<NotiListItem> list;
    ArrayList<NotiData> datalist;
    RecyclerView recyclerView;
    NotiListAdapter listAdapter;
    DAO dao = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        dao = new DAO();
        init_list();
        set_item();
    }

    private void init_list(){
        list = new ArrayList<>();
        datalist = new ArrayList<>();
        listAdapter = new NotiListAdapter(this, list, datalist);

        // 순서를 역순으로 설정
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        recyclerView = findViewById(R.id.admin_noti_list);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(listAdapter);
    }

    private void set_item(){
        list.clear();
        setNotiItems();
        for(int i = 0; i < datalist.size(); i++){
            NotiListItem item = new NotiListItem();

            item.setTitle(datalist.get(i).getTitle());
            item.setContent(datalist.get(i).getContent());
            item.setDate(datalist.get(i).getTimeToText());

            //ArrayList<String> imageList = ImageCompute.imageListStringToArray(dbData.getImageList());
            // 메모에 이미지가 첨부되어있으면 썸네일 설정
            //if(imageList.size() > 0) item.setthumbnailPath(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + imageList.get(0) + "_icon");

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