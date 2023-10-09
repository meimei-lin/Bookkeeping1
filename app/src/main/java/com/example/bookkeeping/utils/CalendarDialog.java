package com.example.bookkeeping.utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.bookkeeping.R;
import com.example.bookkeeping.adapter.CalendarAdapter;
import com.example.bookkeeping.db.DBManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarDialog extends Dialog implements View.OnClickListener{
    ImageView errorIv;
    GridView gv;
    LinearLayout barLayout;
    List<TextView> barViewList;
    List<Integer> yearList;
    int selectPos = -1; //正在被點擊年份的位置
    private CalendarAdapter adapter;
    int selectMonth = -1;
    public interface OnRefreshListener{
        public void onRefresh(int selPos,int year,int month);
    }
    OnRefreshListener onRefreshListener;
    public void setOnRefreshListener(OnRefreshListener onRefreshListener){
        this.onRefreshListener = onRefreshListener;
    }
    public CalendarDialog(@NonNull Context context,int selectPos,int selectMonth) {
        super(context);
        this.selectPos = selectPos;
        this.selectMonth = selectMonth;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_calendar);
        gv = findViewById(R.id.dialog_calendar_gv);
        errorIv = findViewById(R.id.dialog_calendar_iv);
        barLayout = findViewById(R.id.dialog_calendar_layout);

        errorIv.setOnClickListener(this);
        addViewToLayout();
        initGridView();
        //設置GridView中每個item的點擊事件
        setGVListener();
    }

    private void setGVListener() {
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.selPos = position;
                adapter.notifyDataSetInvalidated();
                int month = position + 1;
                int year = adapter.year;
                // 获取到被选中的年份和月份
                onRefreshListener.onRefresh(selectPos,year,month);
                cancel();
            }
        });
    }

    private void initGridView() {
        int selYear = yearList.get(selectPos);
        adapter = new CalendarAdapter(getContext(), selYear);
        if (selectMonth == -1) {
            int month = Calendar.getInstance().get(Calendar.MONTH);
            adapter.selPos = month;
        }else {
            adapter.selPos = selectMonth-1;
        }
        gv.setAdapter(adapter);
    }

    private void addViewToLayout() {
        barViewList = new ArrayList<>();
        yearList = DBManager.getYearListFromAccounttb(); //獲取資料庫中存了多少年份

        if (yearList.size() == 0) {
            int year = Calendar.getInstance().get(Calendar.YEAR);
            yearList.add(year);
        }
        //遍歷年份
        for(int i=0;i<yearList.size();i++){
           int year = yearList.get(i);
           View view = getLayoutInflater().inflate(R.layout.item_dialogcal_bar,null);
           barLayout.addView(view);
           TextView barTv = view.findViewById(R.id.item_dialogcal_bar_tv);
           barTv.setText(year+"");
           barViewList.add(barTv);
        }
        if(selectPos == -1){
            selectPos = barViewList.size()-1;
        }
        changeTvbg(selectPos);
        setHSVClickListener(); //設置每一個view的監聽事件
    }
    //給橫向的scrollview中的每一個textview設置點擊事件
    private void setHSVClickListener() {
        for (int i=0;i<barViewList.size();i++){
            TextView view = barViewList.get(i);
            final int pos = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeTvbg(pos);
                    selectPos = pos;
                    //獲取被選中的年份，下面的Gridview顯示數據源會發生變化
                    int year = yearList.get(selectPos);
                    adapter.setYear(year);
                }
            });
        }
    }

    //傳入被選中的位置，改變位置上的背景和文字顏色
    private void changeTvbg(int selectPos) {
        for(int i=0;i<barViewList.size();i++){
            TextView tv = barViewList.get(i);
            tv.setBackgroundResource(R.drawable.diaglog_btn_bg);
            tv.setTextColor(Color.BLACK);
        }
        TextView selView = barViewList.get(selectPos);
        selView.setBackgroundResource(R.drawable.main_recordbtn_bg);
        selView.setTextColor(Color.WHITE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dialog_calendar_iv:
                cancel();
                break;
        }
    }

    //設置dialog的size和手機螢幕相同
    public void setDialogSize(){
        //獲取當前窗口
        Window window = getWindow();
        //獲取窗口物件的參數
        WindowManager.LayoutParams wlp = window.getAttributes();
        Display d = window.getWindowManager().getDefaultDisplay();
        wlp.width = (int)(d.getWidth()); //對話框窗口為螢幕窗口
        wlp.gravity = Gravity.TOP;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(wlp);
    }
}
