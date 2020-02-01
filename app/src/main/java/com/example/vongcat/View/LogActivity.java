package com.example.vongcat.View;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vongcat.Presenter.ListBeverage;
import com.example.vongcat.Presenter.ListExpense;
import com.example.vongcat.Presenter.ListOder;
import com.example.vongcat.R;
import com.example.vongcat.View.OderAdapter.Adapter;
import com.example.vongcat.View.OderAdapter.Item;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LogActivity extends AppCompatActivity {
    String year;
    String month;
    String day;
    List<Item> listOder = new ArrayList<>();
    List<com.example.vongcat.View.BeverageAdapter.Item> listBeverage = new ArrayList<>();
    List<String> stringData = new ArrayList<>();

    int sumOfMonth =0;
    int expsToday =0;
    int expsThisMonth =0;
    private TextView logTxt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        year=""+getIntent().getIntExtra("year",0);
        month=""+getIntent().getIntExtra("month",0);
        if(month.length()==1)
            month="0"+month;
        String dayTmp = ""+getIntent().getIntExtra("day",0);
        if(dayTmp.length()==1)
            dayTmp="0"+dayTmp;
        day=dayTmp+"-"+month+"-"+year;



        initView();

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
        ListBeverage.getInstance().setOnListBeverageChange(new ListBeverage.OnListBeverageChange() {
            @Override
            public void callBack(List<com.example.vongcat.View.BeverageAdapter.Item> data) {
                listBeverage = data;
                updaData();
            }
        });
        ListBeverage.getInstance().updateData(
                ListBeverage.getInstance().getmJsonArray()
        );

        ListExpense.getInstance().setOnListExpsChange(new ListExpense.OnListExpsChange() {
            @Override
            public void OnTodayChange(int val) {
                expsToday = val;
                updaData();
            }

            @Override
            public void OnThisMonthChange(int val) {
                expsThisMonth = val;
                updaData();
            }
        });
        ListExpense.getInstance().updateData(
                ListExpense.getInstance().getmJsonObject()
        );
    }

    private void initView() {
        logTxt = findViewById(R.id.logTxt);
    }

    void updaData(){
        int total=0;
        stringData.clear();
        logTxt.setText("");
        stringData.add(day);
        stringData.add("---------------");
        for (com.example.vongcat.View.BeverageAdapter.Item item :
                listBeverage) {
            int sum = 0;
            for (Item itemOder:
                    listOder) {
                if(itemOder.getName().equals(item.getName())){
                    sum++;
//                            item.setName(item.getName() + (sum));
                }

            }
            if(sum > 0){
                int value= (item.getValue()*sum);
                stringData.add(item.getName() + " x "+ sum +" = "+ value);
                total+=value;
            }
        }
        stringData.add("---------------");
        stringData.add("Doanh thu hôm nay "+String.valueOf(total)+" k");
        stringData.add("---------------");
        stringData.add("Doanh thu tháng này "+String.valueOf(sumOfMonth)+" k");
        stringData.add("---------------");
        stringData.add("Chi tiêu hôm nay "+String.valueOf(expsToday)+" k");
        stringData.add("---------------");
        stringData.add("Chi tiêu tháng này "+String.valueOf(expsThisMonth)+" k");
        for (String e:
             stringData) {
            logTxt.setText(logTxt.getText()+"\n\r"+e);
        }

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
                ret.add(item);

//                        }
            } catch (JSONException e) {

                e.printStackTrace();
            }
        }
        return ret;
    }
}
