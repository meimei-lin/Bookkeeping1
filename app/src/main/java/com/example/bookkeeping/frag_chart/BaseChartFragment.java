package com.example.bookkeeping.frag_chart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.bookkeeping.R;
import com.example.bookkeeping.adapter.ChartItemAdapter;
import com.example.bookkeeping.db.ChartItemBean;
import com.example.bookkeeping.db.DBManager;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

abstract public class BaseChartFragment extends Fragment {
    ListView chartLv;
    int year;
    int month;
    List<ChartItemBean> mDatas;
    ChartItemAdapter itemAdapter;
    TextView chartTv; //沒有收支情況顯示的
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_income_chart,container,false);
        chartLv = view.findViewById(R.id.frag_chart_lv);
        //獲取Activity傳遞的數據
        Bundle bundle = getArguments();
        year = bundle.getInt("year");
        month = bundle.getInt("month");
        //設置數據源
        mDatas = new ArrayList<>();
        //設置適配器
        itemAdapter = new ChartItemAdapter(getContext(),mDatas);
        chartLv.setAdapter(itemAdapter);
        //添加頭佈局
        addLVHeaderView();
        return view;

    }

    private void addLVHeaderView() {
        //將布局轉成VIEW對象
        View headerView = getLayoutInflater().inflate(R.layout.item_chartfrag_top,null);
        chartLv.addHeaderView(headerView);
        chartTv = headerView.findViewById(R.id.item_chartfrag_top_tv);
    }

    public void loadData(int year,int month,int kind) {
        List<ChartItemBean> list = DBManager.getChartListFromAccounttb(year,month,kind);
        mDatas.clear();
        mDatas.addAll(list);
        itemAdapter.notifyDataSetChanged();
    }

     public void setDate(int year,int month){
        this.year = year;
        this.month = month;
     }
}