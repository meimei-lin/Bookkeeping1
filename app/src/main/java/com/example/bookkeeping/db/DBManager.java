package com.example.bookkeeping.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.bookkeeping.utils.FloatUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

//管理資料庫的類，主要對表中的內容進行操作，增刪改查
public class DBManager {
    private static SQLiteDatabase db;

    public static void initDB(Context context) {
        DBOpenHelper helper = new DBOpenHelper(context); //得到幫助類對象
        db = helper.getWritableDatabase(); //得到資料庫對象
        //helper.deleteDatabase(context);
    }

    //讀取資料庫的數據，寫入記憶體
    //kind表示收入或支出
    public static List<TypeBean> getTypeList(int kind) {
        List<TypeBean> list = new ArrayList<>();
        String sql = "select * from typetb where kind = " + kind;
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String typename = cursor.getString(cursor.getColumnIndex("typename"));
            @SuppressLint("Range") int imageId = cursor.getInt(cursor.getColumnIndex("imageId"));
            @SuppressLint("Range") int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            @SuppressLint("Range") int kind1 = cursor.getInt(cursor.getColumnIndex("kind"));
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
            TypeBean typeBean = new TypeBean(id, typename, imageId, sImageId, kind);
            list.add(typeBean);
        }
        return list;
    }

    //向記帳表當中插入
    public static void insertItemToAccounttb(AccountBean bean) {
        ContentValues values = new ContentValues();
        values.put("typename", bean.getTypename());
        values.put("sImageId", bean.getsImageId());
        values.put("remark", bean.getRemark());
        values.put("money", bean.getMoney());
        values.put("time", bean.getTime());
        values.put("year", bean.getYear());
        values.put("month", bean.getMonth());
        values.put("day", bean.getDay());
        values.put("kind", bean.getKind());
        db.insert("accounttb", null, values);

        Log.i("animor", "insertItemToAccounttb:ok!!");
    }

    //獲取記帳表中某一天的所有支出或收入情況
    public static List<AccountBean> getAccountListOneDayFromAccounttb(int year, int month, int day) {
        List<AccountBean> list = new ArrayList<>();
        String sql = "select * from accounttb where year=? and month=? and day=? order by id desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", day + ""});
        while (cursor.moveToNext()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
            @SuppressLint("Range") String typename = cursor.getString(cursor.getColumnIndex("typename"));
            @SuppressLint("Range") int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            @SuppressLint("Range") String remark = cursor.getString(cursor.getColumnIndex("remark"));
            @SuppressLint("Range") float money = cursor.getFloat(cursor.getColumnIndex("money"));
            @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex("time"));
            @SuppressLint("Range") int kind = cursor.getInt(cursor.getColumnIndex("kind"));
            AccountBean accountBean = new AccountBean(id, typename, sImageId, remark, money, time, year, month, day, kind);
            list.add(accountBean);

        }
        return list;
    }

    //獲取記帳表中某一月的所有支出或收入情況
    public static List<AccountBean> getAccountListOneMonthFromAccounttb(int year, int month) {
        List<AccountBean> list = new ArrayList<>();
        String sql = "select * from accounttb where year=? and month=? order by id desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + ""});
        while (cursor.moveToNext()) {
            @SuppressLint("Range")  int id = cursor.getInt(cursor.getColumnIndex("id"));
            @SuppressLint("Range") String typename = cursor.getString(cursor.getColumnIndex("typename"));
            @SuppressLint("Range") String remark = cursor.getString(cursor.getColumnIndex("remark"));
            @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex("time"));
            @SuppressLint("Range") int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            @SuppressLint("Range") int kind = cursor.getInt(cursor.getColumnIndex("kind"));
            @SuppressLint("Range") float money = cursor.getFloat(cursor.getColumnIndex("money"));
            @SuppressLint("Range") int day = cursor.getInt(cursor.getColumnIndex("day"));
            AccountBean accountBean = new AccountBean(id, typename, sImageId, remark, money, time, year, month, day, kind);
            list.add(accountBean);

        }
        return list;
    }

    //獲取某日的支出或收入的總金額
    public static float getSumMoneyOneDay(int year, int month, int day, int kind) {
        float total = 0.0f;
        String sql = "select sum(money) from accounttb where year=? and month=? and day=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", day + "", kind + ""});
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") float money = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            total = money;
        }
        return total;
    }

    //獲取某月支出或收入
    public static float getSumMoneyOneMonth(int year, int month, int kind) {
        float total = 0.0f;
        String sql = "select sum(money) from accounttb where year=? and month=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") float money = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            total = money;
        }
        return total;
    }

    //統計某月份收支情況
    public static int getCountItemOneMonth(int year, int month, int kind) {
        int total = 0;
        String sql = "select count(money) from accounttb where year=? and month=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") int count = cursor.getInt(cursor.getColumnIndex("count(money)"));
            total = count;
        }
        return total;
    }

    //獲取某年支出或收入
    public static float getSumMoneyOneYear(int year, int kind) {
        float total = 0.0f;
        String sql = "select sum(money) from accounttb where year=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", kind + ""});
        while (cursor.moveToNext()) {
            @SuppressLint("Range") float money = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            total = money;
        }
        return total;
    }

    //根據傳入id，刪除accounttb表中的一條數據
    public static int deleteItemFromAccounttbById(int id) {
        int i = db.delete("accounttb", "id=?", new String[]{id + ""});
        return i;
    }

    //根據備註搜尋收入或支出列表
    public static List<AccountBean> getAccountListByRemarkFromAccounttb(String remark) {
        List<AccountBean> list = new ArrayList<>();
        String sql = "select * from accounttb where remark like '%" + remark + "%'";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
            @SuppressLint("Range") String typename = cursor.getString(cursor.getColumnIndex("typename"));
            @SuppressLint("Range") String rm = cursor.getString(cursor.getColumnIndex("remark"));
            @SuppressLint("Range") int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            @SuppressLint("Range") float money = cursor.getFloat(cursor.getColumnIndex("money"));
            @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex("time"));
            @SuppressLint("Range") int kind = cursor.getInt(cursor.getColumnIndex("kind"));
            @SuppressLint("Range") int year = cursor.getInt(cursor.getColumnIndex("year"));
            @SuppressLint("Range") int month = cursor.getInt(cursor.getColumnIndex("month"));
            @SuppressLint("Range") int day = cursor.getInt(cursor.getColumnIndex("day"));
            AccountBean accountBean = new AccountBean(id, typename, sImageId, rm, money, time, year, month, day, kind);
            list.add(accountBean);
        }
        return list;
    }

    //查詢記帳表中有幾個年份訊息
    public static List<Integer> getYearListFromAccounttb() {
        List<Integer> list = new ArrayList<>();
        String sql = "select distinct(year) from accounttb order by year asc";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") int year = cursor.getInt(cursor.getColumnIndex("year"));
            list.add(year);
        }
        return list;
    }

    //刪除accounttb表格中所有數據
    public static void deleteAllAccount() {
        String sql = "delete from accounttb";
        db.execSQL(sql);
    }

    //查詢指定年分和月份的收支每種類型的總錢數
    public static List<ChartItemBean> getChartListFromAccounttb(int year, int month, int kind) {
        List<ChartItemBean> list = new ArrayList<>();
        float sunMoneyOneMonth = getSumMoneyOneMonth(year, month, kind);
        String sql = "select typename,sImageId,sum(money)as total from accounttb where year=? and month=? and kind=? group by typename " +
                "order by total desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        while (cursor.moveToNext()) {
            @SuppressLint("Range") int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            @SuppressLint("Range") String typename = cursor.getString(cursor.getColumnIndex("typename"));
            @SuppressLint("Range") float total = cursor.getFloat(cursor.getColumnIndex("total"));
            //計算所佔比例
            float radio = FloatUtils.div(total,sunMoneyOneMonth);
            ChartItemBean bean = new ChartItemBean(sImageId,typename,radio,total);
            list.add(bean);
        }

        return list;

    }
    //獲取這個月中鍾某天收支最大金額
    public  static float getMaxMoneyOneDayInMonth(int year,int month,int kind){
        String sql = "select sum(money) from accounttb where year=? and month=? and kind=? group by day order by sum(money) desc";
        Cursor cursor = db.rawQuery(sql,new String[]{year+"",month+"",kind+""});
        if(cursor.moveToFirst()){
            @SuppressLint("Range") float money = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            return money;
        }

        return 0;
    }
    //根據指定月份每一天的收支總錢數集合
    public static List<BarChartItemBean> getSumMoneyOneDayInMonth(int year,int month,int kind){
        String sql = "select day,sum(money) from accounttb where year=? and month=? and kind=? group by day";
        Cursor cursor = db.rawQuery(sql,new String[]{year+"",month+"",kind+""});
        List<BarChartItemBean> list = new ArrayList<>();
        while (cursor.moveToNext()){
            @SuppressLint("Range") int day = cursor.getInt(cursor.getColumnIndex("day"));
            @SuppressLint("Range") float smoney = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            BarChartItemBean itemBean = new BarChartItemBean(year,month,day,smoney);
            list.add(itemBean);
        }
        return list;

    }

}
