package com.example.corona_tracker.AdminClass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.corona_tracker.DAO;
import com.example.corona_tracker.R;

import java.util.ArrayList;

public class NotiListAdapter extends RecyclerView.Adapter<NotiListAdapter.ViewHolder> {
    private ArrayList<NotiListItem> aData = null;
    private ArrayList<NotiData> datalist;
    Context context;
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView text_title, text_date;
        ViewHolder(View itemView) {
            super(itemView) ;

            text_title = itemView.findViewById(R.id.l_item_title);
            text_date = itemView.findViewById(R.id.l_item_date);
            DAO dao = new DAO();
            // 리스트 아이템을 터치하면 상세보기 & 수정 가능
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ModifyNotificationActivity.class);
                    intent.putExtra("idx", datalist.get(getAdapterPosition()).getId());
                    ((Activity)v.getContext()).startActivityForResult(intent, 100);
                }
            });
        }
    }

    public  NotiListAdapter(Context context, ArrayList<NotiListItem> list, ArrayList<NotiData> datalist){
        this.context = context;
        aData = list;
        this.datalist = datalist;
    }

    @NonNull
    @Override
    public NotiListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_noti_list, viewGroup, false);
        NotiListAdapter.ViewHolder vh = new NotiListAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final int idx = i;
        final NotiData data = datalist.get(i);
        if(data == null) return;

        String title = aData.get(i).getTitle();
        String content = aData.get(i).getContent();
        String time = aData.get(i).getDate();

        viewHolder.text_title.setText(title);
        viewHolder.text_date.setText(time);
    }

    @Override
    public int getItemCount() {
        return aData.size();
    }
}
