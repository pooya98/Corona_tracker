package com.example.corona_tracker.AdminClass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.corona_tracker.AdminClass.Static.ImageCompute;
import com.example.corona_tracker.R;

import java.util.ArrayList;

public class NotiListAdapter extends RecyclerView.Adapter<NotiListAdapter.ViewHolder> {
    private ArrayList<NotiListItem> aData = null;
    private ArrayList<NotiData> datalist;
    Context context;
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView text_title, text_content, text_date;
        ImageView thumbnailImage;
        ViewHolder(View itemView) {
            super(itemView) ;

            thumbnailImage = itemView.findViewById(R.id.l_item_image);
            text_title = itemView.findViewById(R.id.l_item_title);
            text_content = itemView.findViewById(R.id.l_item_content);
            text_date = itemView.findViewById(R.id.l_item_date);

            // 리스트 아이템을 터치하면 상세보기 & 수정 가능
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ModifyNotificationActivity.class);
                    intent.putExtra("idx", getAdapterPosition());
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

        // 썸네일이 있으면 이미지를 지정하고 VISIBLE 설정
        if(aData.get(i).getThumbnailPath() != null) {
            viewHolder.thumbnailImage.setImageBitmap(ImageCompute.getBmpFromPathWithRotate(aData.get(i).getThumbnailPath()));
            viewHolder.thumbnailImage.setVisibility(View.VISIBLE);
        }
        // 썸네일이 없으면 GONE
        else {
            viewHolder.thumbnailImage.setVisibility(View.GONE);
        }
        viewHolder.text_title.setText(title);
        viewHolder.text_content.setText(content);
        viewHolder.text_date.setText(time);
    }

    @Override
    public int getItemCount() {
        return aData.size();
    }
}
