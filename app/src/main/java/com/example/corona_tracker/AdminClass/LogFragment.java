package com.example.corona_tracker.AdminClass;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.corona_tracker.DAO;
import com.example.corona_tracker.EnterData;
import com.example.corona_tracker.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class LogFragment extends Fragment {
    private final static String DATE_PAT = "yyyy-MM-dd";

    View v;
    Spinner spinner;
    ArrayList<LogListItem> list;
    ArrayList<EnterData> datalist;
    ArrayList<String> buildings;

    Calendar date;
    RecyclerView recyclerView;
    LogListAdapter listAdapter;
    DAO dao = null;
    Button dateButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.frame_log_list, container, false);

        dao = new DAO();
        date = Calendar.getInstance();

        set_building_spinner();
        dateButton = v.findViewById(R.id.set_date_button);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate();
            }
        });

        SimpleDateFormat format = new SimpleDateFormat(DATE_PAT);
        dateButton.setText(format.format(date.getTime()));
        init_list();
        set_item();
        return v;
    }

    private void set_building_spinner(){
        spinner = v.findViewById(R.id.building_spinner);
        buildings = dao.get_building_array();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(v.getContext(), android.R.layout.simple_spinner_dropdown_item, buildings);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                set_item();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void init_list(){
        list = new ArrayList<>();
        datalist = new ArrayList<>();
        listAdapter = new LogListAdapter(v.getContext(), list, datalist);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(v.getContext());

        recyclerView = v.findViewById(R.id.admin_log_list);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(listAdapter);
    }

    public void set_item(){
        list.clear();
        setLogItems();
        for(int i = 0; i < datalist.size(); i++){
            LogListItem item = new LogListItem();

            item.setTitle(datalist.get(i).getUser_id());
            item.setContent(datalist.get(i).getUser_phone());
            item.setDate(datalist.get(i).getEnter_time());

            list.add(item);
        }

        listAdapter.notifyDataSetChanged();
    }

    private void setLogItems(){
        SimpleDateFormat format = new SimpleDateFormat(DATE_PAT);
        Log.d("mTag", buildings.get(spinner.getSelectedItemPosition()) + format.format(date.getTime()));
        ArrayList<EnterData> list = dao.get_EnterData_array(buildings.get(spinner.getSelectedItemPosition()), format.format(date.getTime()));
        datalist.clear();
        if(list == null) return;
        Log.d("mTag", "" + list.size());
        for(EnterData d : list){
            datalist.add(d);
        }
    }

    public void setDate(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        date.set(year, month, dayOfMonth);
                        dateButton.setText(date.get(Calendar.YEAR) + "-" + (date.get(Calendar.MONTH)+1) + "-" + date.get(Calendar.DAY_OF_MONTH));
                        set_item();
                    }
                }, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
}
