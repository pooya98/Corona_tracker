package com.example.corona_tracker.AdminClass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.corona_tracker.DAO;
import com.example.corona_tracker.EnterData;
import com.example.corona_tracker.R;

import java.util.ArrayList;

public class LogListAdapter extends RecyclerView.Adapter<LogListAdapter.ViewHolder> {

    private ArrayList<LogListItem> aData = null;
    private ArrayList<EnterData> datalist;
    Context context;
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView text_title, text_content, text_date;
        ViewHolder(View itemView) {
            super(itemView) ;

            text_title = itemView.findViewById(R.id.l_log_title);
            text_content = itemView.findViewById(R.id.l_log_content);
            text_date = itemView.findViewById(R.id.l_log_date);
            DAO dao = new DAO();
        }
    }

    public LogListAdapter(Context context, ArrayList<LogListItem> list, ArrayList<EnterData> datalist){
        this.context = context;
        aData = list;
        this.datalist = datalist;
    }

    @NonNull
    @Override
    public LogListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_log_list, viewGroup, false);
        LogListAdapter.ViewHolder vh = new LogListAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final LogListAdapter.ViewHolder viewHolder, int i) {
        final int idx = i;
        final EnterData data = datalist.get(i);
        if(data == null) return;

        String title = aData.get(i).getTitle();
        String content = aData.get(i).getContent();
        String time = aData.get(i).getDate();

        viewHolder.text_title.setText(title);
        viewHolder.text_content.setText(content);
        viewHolder.text_date.setText(time);
    }

    @Override
    public int getItemCount() {
        return aData.size();
    }
}
