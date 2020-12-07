package com.example.corona_tracker.AdminClass;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.corona_tracker.DAO;
import com.example.corona_tracker.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class NotiFragment extends Fragment {
    View v;

    ArrayList<NotiListItem> list;
    ArrayList<NotiData> datalist;
    RecyclerView recyclerView;
    NotiListAdapter listAdapter;
    DAO dao = null;
    FloatingActionButton add_button;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.frame_noti_list, container, false);

        dao = new DAO();
        init_list();
        set_item();
        return v;
    }

    private void init_list(){
        list = new ArrayList<>();
        datalist = new ArrayList<>();
        listAdapter = new NotiListAdapter(v.getContext(), list, datalist, true);
        add_button = v.findViewById(R.id.add_noti_button);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(v.getContext());

        recyclerView = v.findViewById(R.id.admin_noti_list);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(listAdapter);

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNoti();
            }
        });
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

    public void addNoti() {
        Intent intent = new Intent(v.getContext(), ModifyNotificationActivity.class);
        intent.putExtra("idx", -1);
        startActivityForResult(intent, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && resultCode == RESULT_OK){
            set_item();
        }
    }
}
