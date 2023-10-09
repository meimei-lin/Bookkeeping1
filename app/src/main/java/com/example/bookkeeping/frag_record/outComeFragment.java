package com.example.bookkeeping.frag_record;

import com.example.bookkeeping.R;
import com.example.bookkeeping.db.DBManager;
import com.example.bookkeeping.db.TypeBean;

import java.util.List;

public class outComeFragment extends BaseRecordFragment{
    @Override
    public void loadDataToGV() {
        super.loadDataToGV();
        //獲取資料庫中的數據源
        List<TypeBean> outlist = DBManager.getTypeList(0);
        typeList.addAll(outlist);
        adapter.notifyDataSetChanged();
        typeTv.setText("其他");
        typeIv.setImageResource(R.drawable.ic_other_fs);
    }

    @Override
    public void saveAccountToDB() {
        accountBean.setKind(0);
        DBManager.insertItemToAccounttb(accountBean);
    }
}
