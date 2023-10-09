package com.example.bookkeeping.utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.bookkeeping.R;

public class SelectTimeDialog extends Dialog implements View.OnClickListener {
    EditText hourEt,minuteEt;
    DatePicker datePicker;
    Button ensureBtn,cancelBtn;
    public interface OnEnsureListener{
        public void onEnsure(String time,int year, int month, int day);
    }
    OnEnsureListener onEnsureListener;

    public void setOnEnsureListener(OnEnsureListener onEnsureListener){
        this.onEnsureListener = onEnsureListener;
    }
    public SelectTimeDialog(@NonNull Context context) {
        super(context);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_time);
        hourEt = findViewById(R.id.dialog_time_ed_hour);
        minuteEt = findViewById(R.id.dialog_time_ed_minute);
        datePicker = findViewById(R.id.dialog_time_dp);
        ensureBtn = findViewById(R.id.dialog_time_btn_ensure);
        cancelBtn = findViewById(R.id.dialog_time_btn_cancel);
        ensureBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
      //  hideDatePickerHeader();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dialog_time_btn_cancel:
                cancel();
                break;
            case R.id.dialog_time_btn_ensure:
                int year = datePicker.getYear();
                int month = datePicker.getMonth()+1;
                int dayOfMonth = datePicker.getDayOfMonth();
                String monthStr = String.valueOf(month);

                if(month<10){
                    monthStr = "0" + month;
                }
                String dayStr = String.valueOf(dayOfMonth);
                if(dayOfMonth<10){
                    dayStr = "0" + dayOfMonth;
                }
                //獲取輸入的小時和分鐘
                String hourStr = hourEt.getText().toString();
                String minuteStr = minuteEt.getText().toString();
                int hour = 0;
                if(!TextUtils.isEmpty(hourStr)){
                    hour = Integer.parseInt(hourStr);
                    hour = hour % 24;
                }
                int minute = 0;
                if(!TextUtils.isEmpty(minuteStr)){
                    minute = Integer.parseInt(minuteStr);
                    minute = minute % 60;
                }

                hourStr = String.valueOf(hour);
                minuteStr = String.valueOf(minute);
                if(hour<10){
                    hourStr = "0"+hour;
                }
                if(minute<10){
                    minuteStr = "0" + minute;
                }
                String timeFormat = year +"年" + monthStr + "月" + dayStr + "日" + hourStr + ":" + minuteStr;
                if(onEnsureListener != null){
                    onEnsureListener.onEnsure(timeFormat,year,month,dayOfMonth);
                }
                cancel();
                break;
        }

    }

    public void hideDatePickerHeader(){
        ViewGroup rootView = (ViewGroup) datePicker.getChildAt(0);
        View headerView = rootView.getChildAt(0);
        int headerId = getContext().getResources().getIdentifier("dialog_time_dp","id","android");
        if(headerId == headerView.getId()){
            headerView.setVisibility(View.GONE);
            ViewGroup.LayoutParams layoutParamsRoot = rootView.getLayoutParams();
            layoutParamsRoot.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            rootView.setLayoutParams(layoutParamsRoot);

            ViewGroup animator = (ViewGroup) rootView.getChildAt(1);
            ViewGroup.LayoutParams layoutParamsAnimator = animator.getLayoutParams();
            layoutParamsAnimator.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            animator.setLayoutParams(layoutParamsAnimator);

            View child = animator.getChildAt(0);
            ViewGroup.LayoutParams layoutParamsChild = child.getLayoutParams();
            layoutParamsChild.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            child.setLayoutParams(layoutParamsChild);
            return;
        }
    }
}
