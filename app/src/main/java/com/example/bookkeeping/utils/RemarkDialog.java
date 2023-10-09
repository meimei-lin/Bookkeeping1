package com.example.bookkeeping.utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.bookkeeping.R;

public class RemarkDialog extends Dialog implements View.OnClickListener {
    EditText et;
    Button cancelBtn, ensureBtn;
    OnEnsureListener onEnsureListener;

    public void setOnEnsureListener(OnEnsureListener onEnsureListener){
        this.onEnsureListener = onEnsureListener;
    }
    public RemarkDialog(@NonNull Context context) {
        super(context);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_remark);
        et = findViewById(R.id.dialog_remark_et);
        cancelBtn = findViewById(R.id.dialog_remark_btn_cancel);
        ensureBtn = findViewById(R.id.dialog_remark_btn_ensure);
        cancelBtn.setOnClickListener(this);
        ensureBtn.setOnClickListener(this);
    }
    public interface OnEnsureListener{
        public void onEnsure();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dialog_remark_btn_cancel:
                cancel();
                break;

            case R.id.dialog_remark_btn_ensure:
                if(onEnsureListener != null){
                    onEnsureListener.onEnsure();
                }
                break;
        }
    }

    public String getEditText(){
        return et.getText().toString().trim();
    }

    //設置dialog的size和手機螢幕相同
    public void setDialogSize(){
        //獲取當前窗口
        Window window = getWindow();
        //獲取窗口物件的參數
        WindowManager.LayoutParams wlp = window.getAttributes();
        Display d = window.getWindowManager().getDefaultDisplay();
        wlp.width = (int)(d.getWidth()); //對話框窗口為螢幕窗口
        wlp.gravity = Gravity.BOTTOM;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(wlp);
        handler.sendEmptyMessageDelayed(1,100);
    }

    Handler handler = new Handler(){
        public void handleMessage(Message msg){
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
        }
    };
}
