package com.example.bookkeeping;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.example.bookkeeping.adapter.RecordPagerAdapter;
import com.example.bookkeeping.frag_record.outComeFragment;
import com.example.bookkeeping.frag_record.inComeFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class RecordActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        tabLayout = findViewById(R.id.record_tabs);
        viewPager = findViewById(R.id.record_vp);

        initPager();
    }

    private void initPager() {
        List<Fragment> fragmentList = new ArrayList<>();
        //創建收入和支出頁面，放置在Fragment中
        outComeFragment outFrag = new outComeFragment(); //支出
        inComeFragment inFrag = new inComeFragment(); //收入
        fragmentList.add(outFrag);
        fragmentList.add(inFrag);
        //創建適配器
        RecordPagerAdapter pagerAdapter = new RecordPagerAdapter(getSupportFragmentManager(),fragmentList);
        //設置適配器
        viewPager.setAdapter(pagerAdapter);
        //將tabLayout和viewPager進行關聯
        tabLayout.setupWithViewPager(viewPager);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.record_iv_back:
                finish();
                break;
        }
    }
}