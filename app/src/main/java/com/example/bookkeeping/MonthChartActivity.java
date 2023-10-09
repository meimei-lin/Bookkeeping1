package com.example.bookkeeping;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bookkeeping.adapter.ChartVPAdapter;
import com.example.bookkeeping.db.DBManager;
import com.example.bookkeeping.frag_chart.IncomeChartFragment;
import com.example.bookkeeping.frag_chart.OutcomeChartFragment;
import com.example.bookkeeping.utils.CalendarDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MonthChartActivity extends AppCompatActivity {
    Button inBtn,outBtn;
    TextView dateTv,inTv,outTv;
    ViewPager chartVp;
    int year,month;
    int selectPos = -1;
    int selectMonth = -1;
    List<Fragment> chartFragList;
    private IncomeChartFragment incomeChartFragment;
    private OutcomeChartFragment outcomeChartFragment;
    private ChartVPAdapter chartVPAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_chart);
        initView();
        initTime();
        initStatistic(year,month);
        initFrag();
        setVPSelectListener();
    }

    private void setVPSelectListener() {
        chartVp.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                setButtonStyle(position);
            }
        });
    }

    private void initFrag() {
        chartFragList = new ArrayList<>();
        //添加Fragment對象
        incomeChartFragment = new IncomeChartFragment();
        outcomeChartFragment = new OutcomeChartFragment();
        //添加數據到fragment中
        Bundle bundle = new Bundle();
        bundle.putInt("year",year);
        bundle.putInt("month",month);
        incomeChartFragment.setArguments(bundle);
        outcomeChartFragment.setArguments(bundle);
        //將fragment添加到數據源中
        chartFragList.add(outcomeChartFragment);
        chartFragList.add(incomeChartFragment);
        //設置適配器
        chartVPAdapter = new ChartVPAdapter(getSupportFragmentManager(),chartFragList);
        chartVp.setAdapter(chartVPAdapter);
        //將fragment加載到Activity中

    }

    //統計某年某月的收支情況
    private void initStatistic(int year, int month) {
        float inMoneyOneMonth = DBManager.getSumMoneyOneMonth(year,month,1); //收入總錢數
        float outMoneyOneMoney = DBManager.getSumMoneyOneMonth(year,month,0);
        int incountItemOneMonth = DBManager.getCountItemOneMonth(year,month,1); //收入幾筆
        int outcountItemOneMonth = DBManager.getCountItemOneMonth(year,month,0);
        dateTv.setText(year+"年"+month+"月帳單");
        inTv.setText("共"+incountItemOneMonth+"筆收入， $"+ inMoneyOneMonth);
        outTv.setText("共"+outcountItemOneMonth+"筆支出， $"+outMoneyOneMoney);

    }

    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
    }


    private void initView() {
        inBtn = findViewById(R.id.chart_btn_in);
        outBtn = findViewById(R.id.chart_btn_out);
        dateTv = findViewById(R.id.chart_tv_date);
        inTv = findViewById(R.id.chart_tv_in);
        outTv = findViewById(R.id.chart_tv_out);
        chartVp = findViewById(R.id.chart_vp);
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.chart_iv_back:
                finish();
                break;
            case R.id.chart_iv_calendar:
                showCalendarDialog();
                break;
            case R.id.chart_btn_in:
                setButtonStyle(1);
                chartVp.setCurrentItem(1);
                break;
            case R.id.chart_btn_out:
                setButtonStyle(0);
                chartVp.setCurrentItem(0);
                break;
        }
    }

    private void showCalendarDialog() {
        CalendarDialog dialog = new CalendarDialog(this,selectPos,selectMonth);
        dialog.show();
        dialog.setDialogSize();
        dialog.setOnRefreshListener(new CalendarDialog.OnRefreshListener() {
            @Override
            public void onRefresh(int selPos, int year, int month) {
                MonthChartActivity.this.selectPos =  selPos;
                MonthChartActivity.this.selectMonth = month;
                initStatistic(year,month);
                incomeChartFragment.setDate(year,month);
                outcomeChartFragment.setDate(year,month);
            }
        });
    }

    //設置按鈕樣式改變
    private void setButtonStyle(int kind){
        if(kind == 0){
            outBtn.setBackgroundResource(R.drawable.main_recordbtn_bg);
            outBtn.setTextColor(Color.WHITE);
            inBtn.setBackgroundResource(R.drawable.diaglog_btn_bg);
            inBtn.setTextColor(Color.BLACK);
        } else if (kind == 1) {
            inBtn.setBackgroundResource(R.drawable.main_recordbtn_bg);
            inBtn.setTextColor(Color.WHITE);
            outBtn.setBackgroundResource(R.drawable.diaglog_btn_bg);
            outBtn.setTextColor(Color.BLACK);

        }
    }
}