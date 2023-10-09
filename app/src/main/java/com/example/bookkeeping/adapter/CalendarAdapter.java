package com.example.bookkeeping.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bookkeeping.R;

import java.util.ArrayList;
import java.util.List;

//歷史紀錄頁面，點擊日曆，彈出對話框當中的GridView對應的適配器
public class CalendarAdapter extends BaseAdapter {
    Context context;
    List<String> mDatas;
    public int year;
    public int selPos = -1;

    public void setYear(int year){
        this.year = year;
        mDatas.clear();
        loadDatas(year);
        notifyDataSetChanged();
    }
    public CalendarAdapter(Context context,int year){
        this.context = context;
        this.year = year;
        mDatas = new ArrayList<>();
        loadDatas(year);
    }

    private void loadDatas(int year) {
        for (int i=1;i<13;i++){
            String data = year +"/"+i;
            mDatas.add(data);
        }
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_dialogcal_gv,parent,false);
        TextView tv = convertView.findViewById(R.id.item_dialogcal_gv_tv);
        tv.setText(mDatas.get(position));
        tv.setBackgroundResource(R.color.purple_200);
        tv.setTextColor(Color.BLACK);
        if(position == selPos){
            tv.setBackgroundResource(R.color.purple_ba55d3);
            tv.setTextColor(Color.WHITE);
        }
        return convertView;
    }
}
