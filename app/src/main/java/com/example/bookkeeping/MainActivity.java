package com.example.bookkeeping;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bookkeeping.adapter.AccountAdapter;
import com.example.bookkeeping.db.AccountBean;
import com.example.bookkeeping.db.DBManager;
import com.example.bookkeeping.utils.BudgetDialog;
import com.example.bookkeeping.utils.MoreDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    ListView todayLv;
    ImageView searchIv;
    Button editBtn;
    ImageButton moreBtn;
    List<AccountBean> mDatas;
    private AccountAdapter adapter;
    int year,month,day;
    View headerView;
    TextView topOutTv,topInTv,topbudgetTv,topConTv;
    ImageView topShowIv;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTime();
        initView();
        preferences = getSharedPreferences("budget", Context.MODE_PRIVATE);
        addLVHeaderView();
        mDatas = new ArrayList<>();

        adapter = new AccountAdapter(this,mDatas);
        todayLv.setAdapter(adapter);
    }

    private void initView() {
        todayLv = findViewById(R.id.main_lv);
        editBtn = findViewById(R.id.main_btn_edit);
        moreBtn = findViewById(R.id.main_btn_more);
        searchIv = findViewById(R.id.main_iv_search);
        editBtn.setOnClickListener(this);
        moreBtn.setOnClickListener(this);
        searchIv.setOnClickListener(this);
        setLVLongClickListener();
    }
    //設置lv長按事件
    private void setLVLongClickListener() {
        todayLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
               if(position == 0){
                   return false;
               }
               int pos = position - 1;
               AccountBean clickBean = mDatas.get(pos); //獲取正在被點擊的信息
               //彈出提示用戶是否刪除的對話框
                showDeleteItemDialog(clickBean);
                return false;
            }
        });
    }
    //彈出是否刪除某條紀錄的對話框
    private void showDeleteItemDialog(final AccountBean clickBean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示信息").setMessage("確定要刪除這條紀錄嗎?")
                .setNegativeButton("取消",null)
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int click_id = clickBean.getId();
                        //執行刪除的操作
                        DBManager.deleteItemFromAccounttbById(click_id);
                        mDatas.remove(clickBean);
                        adapter.notifyDataSetChanged();
                        setTopTvShow(); //改變頭布局textview顯示的內容
                    }
                });
        builder.create().show(); //顯示對話框
    }
    private void addLVHeaderView() {
        headerView = getLayoutInflater().inflate(R.layout.item_mainlv_top,null);
        todayLv.addHeaderView(headerView);
        topOutTv = headerView.findViewById(R.id.item_mainlv_top_out);
        topInTv = headerView.findViewById(R.id.item_mainlv_top_tv_in);
        topbudgetTv = headerView.findViewById(R.id.item_mainlv_top_tv_bodget);
        topConTv = headerView.findViewById(R.id.item_mainlv_top_tv_day);
        topShowIv = headerView.findViewById(R.id.item_mainlv_top_iv_hide);

        topbudgetTv.setOnClickListener(this);
        headerView.setOnClickListener(this);
        topShowIv.setOnClickListener(this);

    }

    protected void onResume() {
        super.onResume();
        loadDBData();
        setTopTvShow();
    }
    //設置頭布局中TextView內容顯示
    private void setTopTvShow() {
        float incomeOneDay = DBManager.getSumMoneyOneDay(year,month,day,1);
        float outcomeOneDay = DBManager.getSumMoneyOneDay(year,month,day,0);
        String infoOneDay = "今日支出 $"+outcomeOneDay+" 收入 $"+ incomeOneDay;
        topConTv.setText(infoOneDay);

        float incomeOneMonth = DBManager.getSumMoneyOneMonth(year,month,1);
        float outcomeOneMonth = DBManager.getSumMoneyOneMonth(year,month,0);
        topInTv.setText("$"+incomeOneMonth);
        topOutTv.setText("$"+outcomeOneMonth);

        //設置顯示預算剩餘
        float bmoney = preferences.getFloat("bmoney",0); //預算
        if(bmoney == 0){
            topbudgetTv.setText("$ 0");
        }else{
            float syMoney = bmoney - outcomeOneMonth;
            topbudgetTv.setText("$"+syMoney);
        }
    }

    //獲取今日的具體時間
    private void initTime(){
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }
    private void loadDBData() {
        List<AccountBean> list = DBManager.getAccountListOneDayFromAccounttb(year,month,day);
        mDatas.clear();
        mDatas.addAll(list);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_iv_search:
                Intent it = new Intent(this,SearchActivity.class);
                startActivity(it);
                break;
            case R.id.main_btn_edit:
                Intent it1 = new Intent(this,RecordActivity.class);
                startActivity(it1);
                break;
            case R.id.main_btn_more:
                MoreDialog moreDialog = new MoreDialog(this);
                moreDialog.show();
                moreDialog.setDialogSize();
                break;
            case R.id.item_mainlv_top_tv_bodget:
                showBudgetDialog();
                break;
            case R.id.item_mainlv_top_iv_hide:
                toggleShow();
                break;

        }
        if(v == headerView){
            Intent intent = new Intent();
            intent.setClass(this,MonthChartActivity.class);
            startActivity(intent);
        }
    }

    private void showBudgetDialog() {
        BudgetDialog dialog = new BudgetDialog(this);
        dialog.show();
        dialog.setDialogSize();
        dialog.setOnEnsureListener(new BudgetDialog.OnEnsureListener() {
            @Override
            public void onEnsure(float money) {
                //將預算金額寫入共享參數
                SharedPreferences.Editor editor = preferences.edit();
                editor.putFloat("bmoney",money);
                editor.commit();

                //計算剩餘金額
                float outcomeOneMonth = DBManager.getSumMoneyOneMonth(year,month,0);

                float syMoney = money - outcomeOneMonth; //預算剩餘 = 預算 - 支出
                topbudgetTv.setText("$"+syMoney);
            }
        });
    }

    boolean isShow = true;
    private void toggleShow() {
        if(isShow){
            PasswordTransformationMethod passwordMethod = PasswordTransformationMethod.getInstance();
            topInTv.setTransformationMethod(passwordMethod); //設置隱藏
            topOutTv.setTransformationMethod(passwordMethod);
            topbudgetTv.setTransformationMethod(passwordMethod);
            topShowIv.setImageResource(R.drawable.ic_hide);
            isShow = false;
        }else{
            HideReturnsTransformationMethod hideMethod = HideReturnsTransformationMethod.getInstance();
            topInTv.setTransformationMethod(hideMethod); //設置顯示
            topOutTv.setTransformationMethod(hideMethod);
            topbudgetTv.setTransformationMethod(hideMethod);
            topShowIv.setImageResource(R.drawable.eyes);
            isShow = true;
        }
    }
}