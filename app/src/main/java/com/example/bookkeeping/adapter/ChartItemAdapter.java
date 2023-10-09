package com.example.bookkeeping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bookkeeping.R;
import com.example.bookkeeping.db.ChartItemBean;
import com.example.bookkeeping.utils.FloatUtils;

import java.util.List;

//帳單詳情介面，listview的適配器
public class ChartItemAdapter extends BaseAdapter {
    Context context;
    List<ChartItemBean> mDatas;
    LayoutInflater inflater;

    public ChartItemAdapter(Context context,List<ChartItemBean> mDatas){
        this.context = context;
        this.mDatas = mDatas;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_chartfrag_lv,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        ChartItemBean bean = mDatas.get(position);
        holder.iv.setImageResource(bean.getsImageId());
        holder.typeTv.setText(bean.getType());
        float radio = bean.getRadio();
        String pert = FloatUtils.radioToPercent(radio);
        holder.radioTv.setText(pert);
        holder.totalTv.setText("$"+bean.getTotalMoney());
        return convertView;
    }

    class ViewHolder{
        TextView typeTv,radioTv,totalTv;
        ImageView iv;
        public ViewHolder(View view){
            typeTv = view.findViewById(R.id.item_chartfrag_tv_type);
            radioTv = view.findViewById(R.id.item_chartfrag_tv_pert);
            totalTv = view.findViewById(R.id.item_chartfrag_tv_sum);
            iv = view.findViewById(R.id.item_chartfrag_iv);
        }
    }
}
