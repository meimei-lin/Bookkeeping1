package com.example.bookkeeping.utils;

import android.app.Dialog;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.bookkeeping.R;

public class BudgetDialog extends Dialog implements View.OnClickListener {
    ImageView cancalIv;
    Button ensureBtn;
    EditText moneyEt;
    public interface OnEnsureListener{
        public void onEnsure(float money);
    }
    OnEnsureListener onEnsureListener;
    public void setOnEnsureListener(OnEnsureListener onEnsureListener){
        this.onEnsureListener = onEnsureListener;
    }
    public BudgetDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_budget);
        cancalIv = findViewById(R.id.dialog_budget_iv_error);
        ensureBtn = findViewById(R.id.dialog_budget_btn_ensure);
        moneyEt = findViewById(R.id.dialog_budget_et);
        cancalIv.setOnClickListener(this);
        ensureBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dialog_budget_iv_error:
                cancel();
                break;
            case R.id.dialog_budget_btn_ensure:
                String data = moneyEt.getText().toString();
                if(TextUtils.isEmpty(data)){
                    Toast.makeText(getContext(),"輸入不能為空",Toast.LENGTH_LONG).show();
                    return;
                }
                float money = Float.parseFloat(data);
                if(money <= 0){
                    Toast.makeText(getContext(),"預算金額需大於0",Toast.LENGTH_LONG).show();
                    return;
                }
                if(onEnsureListener != null){
                    onEnsureListener.onEnsure(money);
                }
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
