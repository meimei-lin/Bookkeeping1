package com.example.bookkeeping.frag_chart;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.bookkeeping.R;
import com.example.bookkeeping.adapter.ChartItemAdapter;
import com.example.bookkeeping.db.BarChartItemBean;
import com.example.bookkeeping.db.ChartItemBean;
import com.example.bookkeeping.db.DBManager;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.StackedValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.List;

public class IncomeChartFragment extends BaseChartFragment {
    int kind = 1;
    public void onResume() {
        super.onResume();
        loadData(year,month,kind);
    }

  //  @Override
 /*  protected void setAxisData(int year, int month) {
        List<IBarDataSet> sets = new ArrayList<>();
        //獲取這個月每天的收入總金額
        List<BarChartItemBean> list = DBManager.getSumMoneyOneDayInMonth(year,month,kind);
        if(list.size() == 0){
            barChart.setVisibility(View.GONE);
            chartTv.setVisibility(View.VISIBLE);
        }else{
            barChart.setVisibility(View.VISIBLE);
            chartTv.setVisibility(View.GONE);

            List<BarEntry>barEntries = new ArrayList<>();
            for(int i=0;i<31;i++){
                //初始化每根柱子，添加到柱狀圖中
               // BarEntry entry = ;
                barEntries.add(new BarEntry(i,0.0f));
            }
            for (int i=0;i<list.size();i++){
                BarChartItemBean itemBean = list.get(i);
                int day = itemBean.getDay(); //獲取日期
                //根據天數，獲取x軸位置
                int xIndex = day-1;
                BarEntry barEntry = barEntries.get(xIndex);
                barEntry.setY(itemBean.getSummoney());
            }
            BarDataSet barDataSet = new BarDataSet(barEntries,"TEST");
            barDataSet.setValueTextColor(Color.BLACK);
            barDataSet.setValueTextSize(8f);
            barDataSet.setColor(Color.parseColor("#006400")); //柱子的顏色
            //設置柱子上數據顯示的格式
           barDataSet.setValueFormatter((value, entry, dataSetIndex, viewPortHandler) -> {
                if(value == 0){
                    return "";
                }
                return value+"";
            });
            sets.add(barDataSet);
            BarData barData = new BarData(sets);
            barData.setBarWidth(0.2f);
           // barChart.setData(barData);

        }
    }

    @Override
    protected void setYAxis(int year, int month) {
        //獲取本月收入最高的一天為多少，將他設置為y軸最大值
        float maxMoney = DBManager.getMaxMoneyOneDayInMonth(year,month,kind);
        float max = (float) Math.ceil(maxMoney); //將最大金額向上取整數
        //設置y軸
        YAxis yAxis_right = barChart.getAxisRight();
        yAxis_right.setAxisMaximum(max);
        yAxis_right.setAxisMinimum(0f);
        yAxis_right.setEnabled(false);

        YAxis yAxis_left = barChart.getAxisLeft();
        yAxis_left.setAxisMaximum(max);
        yAxis_left.setAxisMinimum(0f);
        yAxis_left.setEnabled(false);

        //設置不顯示圖例
        Legend legend = barChart.getLegend();
        legend.setEnabled(false);
    }*/


    @Override
    public void setDate(int year, int month) {
        super.setDate(year, month);
        loadData(year,month,kind);
    }
}