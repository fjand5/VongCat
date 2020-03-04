package com.example.vongcat.View;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vongcat.Presenter.ListBeverage;
import com.example.vongcat.Presenter.ListExpense;
import com.example.vongcat.Presenter.ListOder;
import com.example.vongcat.R;
import com.example.vongcat.View.OderAdapter.Adapter;
import com.example.vongcat.View.OderAdapter.Item;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class LogActivity extends AppCompatActivity {
    volatile String year;
    volatile String month;
    volatile String day;
    List<Item> listOder = new ArrayList<>();
    List<com.example.vongcat.View.BeverageAdapter.Item> listBeverage = new ArrayList<>();
    List<String> stringData = new ArrayList<>();

    int sumOfMonth =0;
    int expsToday =0;
    int expsThisMonth =0;
    private TextView revenueTxt;
    private TextView spendTxt;


    HorizontalCalendar horizontalCalendar;

    ListView logOderLsv;
    Adapter adapterOder;
    List<Item> itemOderList;

    boolean detailLogFlag = false;


    MenuItem toggleMni;
    View calendarView;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_log, menu);

        toggleMni = menu.findItem(R.id.logToggleItm);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logToggleItm:
                if(detailLogFlag){
                    toggleMni.setTitle("Xem chi tiết");
                    detailLogFlag = false;
                    updaData();
                }else {
                    toggleMni.setTitle("Xem tổng quát");
                    detailLogFlag = true;
                    updaData();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        initView();
        addEvent();
        ListOder.getInstance().setOnListOderChange(new ListOder.OnListOderChange() {
            @Override
            public void OnTodayChange(List<Item> listItem, List<Item> listAllItem) {

            }

            @Override
            public void OnAllDayChange(JSONObject data) {
                JSONObject listOderThisMonth= new JSONObject();
                JSONObject listOderOneDay= new JSONObject();

                try {
                    listOderThisMonth = data.getJSONObject(year).getJSONObject(month);
                    listOderOneDay = listOderThisMonth.getJSONObject(day);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                listOder = getListOneDay(listOderOneDay);

                sumOfMonth =0;
                Iterator<String> keys = listOderThisMonth.keys();
                while(keys.hasNext()) {
                    String key = keys.next();
                    try {
                        JSONObject jsonObject = new JSONObject(listOderThisMonth.get(key).toString());
                        List<Item> subItem = new ArrayList<>();
                        subItem = getListOneDay(jsonObject);
                        for (Item item:
                             subItem) {
                            sumOfMonth +=item.getValue();
                        }
                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                }
                updaData();

            }
        });
        ListOder.getInstance().updateData(
                ListOder.getInstance().getmJsonObject()

        );

        ListBeverage.getInstance().updateData(
                ListBeverage.getInstance().getmJsonArray()
        );

        ListExpense.getInstance().updateData(
                ListExpense.getInstance().getmJsonObject()
        );
    }

    private void addEvent() {

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {

                year=""+date.get(Calendar.YEAR);
                month=""+(date.get(Calendar.MONTH)+1);
                if(month.length()==1)
                    month="0"+month;
                String dayTmp = ""+date.get(Calendar.DAY_OF_MONTH);
                if(dayTmp.length()==1)
                    dayTmp="0"+dayTmp;

                day=dayTmp+"-"+month+"-"+year;

                ListOder.getInstance().updateData(
                        ListOder.getInstance().getmJsonObject()

                );
                ListExpense.getInstance().updateData(
                        ListExpense.getInstance().getmJsonObject()
                );
            }
        });

        ListBeverage.getInstance().setOnListBeverageChange(new ListBeverage.OnListBeverageChange() {
            @Override
            public void callBack(List<com.example.vongcat.View.BeverageAdapter.Item> data) {
                listBeverage = data;
                updaData();
            }
        });

        ListExpense.getInstance().setOnListExpsChange(new ListExpense.OnListExpsChange() {
            @Override
            public void OnTodayChange(int val) {

            }

            @Override
            public void OnThisMonthChange(int val) {

            }

            @Override
            public void OnDataChange(JSONObject jsonObject) {
                try {
                    expsToday =ListExpense.getSumValueOneDay( jsonObject.getJSONObject(year)
                            .getJSONObject(month)
                            .getJSONObject(day));
                } catch (JSONException e) {
                    expsToday = 0;
                    e.printStackTrace();
                }
                try {
                    expsThisMonth = ListExpense.getSumValueOneMonth(jsonObject.getJSONObject(year)
                            .getJSONObject(month));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                updaData();
            }
        });
        horizontalCalendar.goToday(true);
    }

    private void initView() {
        calendarView = findViewById(R.id.calendarView);
        /* starts before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.YEAR, -1);

        /* ends after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.YEAR, 0);
        horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(5)
                .configure()
                .showTopText(false)
                .formatBottomText("MM")
                .end()
                .build();




        revenueTxt = findViewById(R.id.revenueTxt);
        spendTxt = findViewById(R.id.spendTxt);
        logOderLsv = findViewById(R.id.logOderLsv);


        itemOderList = new ArrayList<>();
        adapterOder=new Adapter(this,R.layout.item_oder,itemOderList);
        logOderLsv.setAdapter(adapterOder);

    }

    void updaData(){
        int total=0;
        stringData.clear();


        itemOderList.clear();


        for (com.example.vongcat.View.BeverageAdapter.Item item :
                listBeverage) {
            int sum = 0;
            for (Item itemOder:
                    listOder) {
                if(itemOder.getName().equals(item.getName())){
                    sum++;
                }

            }
            if(sum > 0){
                int value= (item.getValue()*sum);
                if(detailLogFlag == false)
                    itemOderList.add(new Item(item.getName(),"x"+String.valueOf(sum),item.getValue(),false));
                total+=value;
            }
        }

        Collections.sort(listOder, new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                    return o1.getKey().compareTo(o2.getKey());
            }
        });

        for (Item itemOder:
                listOder) {
            if(detailLogFlag == true){
                itemOderList.add(itemOder);
            }

        }
        adapterOder.notifyDataSetChanged();
        revenueTxt.setText("Doanh thu (H.nay/Tháng): "+ total+"k/"+sumOfMonth+"k");
        spendTxt.setText("Chi tiêu (H.nay/Tháng): "+ expsToday+"k/"+expsThisMonth+"k");
    }
    List<Item> getListOneDay(JSONObject listOderAllDay){
        List<Item> ret = new ArrayList<>();
        Iterator<String> keys = listOderAllDay.keys();
        while(keys.hasNext()) {
            String key = keys.next();
            try {
//                        if (listOderAllDay.get(key) instanceof JSONObject) {
                JSONObject jsonObject = new JSONObject(listOderAllDay.get(key).toString());
                String name = jsonObject.getString("name");
                String table = jsonObject.getString("table");
                int value = jsonObject.getInt("value");
                boolean isPaid = jsonObject.getBoolean("isPaid");
                String mkey = jsonObject.getString("key");

                Item item = new Item(name,table,value,isPaid);
                item.setKey(mkey);
                ret.add(item);

//                        }
            } catch (JSONException e) {

                e.printStackTrace();
            }
        }
        return ret;
    }

}
