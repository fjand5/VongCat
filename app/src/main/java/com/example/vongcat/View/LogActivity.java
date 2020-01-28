package com.example.vongcat.View;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.vongcat.Presenter.ListBeverage;
import com.example.vongcat.Presenter.ListOder;
import com.example.vongcat.R;
import com.example.vongcat.View.OderAdapter.Adapter;
import com.example.vongcat.View.OderAdapter.Item;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LogActivity extends Activity {
    String year;
    String month;
    String day;
    List<Item> listOder = new ArrayList<>();
    List<com.example.vongcat.View.BeverageAdapter.Item> listBeverage = new ArrayList<>();
    List<String> stringData = new ArrayList<>();
    private TextView logTxt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        year=""+getIntent().getIntExtra("year",0);
        month=""+getIntent().getIntExtra("month",0);
        if(month.length()==1)
            month="0"+month;
        day=getIntent().getIntExtra("day",0)+"-"+month+"-"+year;



        initView();

        ListOder.getInstance().setOnListOderChange(new ListOder.OnListOderChange() {
            @Override
            public void OnTodayChange(List<Item> listItem, List<Item> listAllItem) {

            }

            @Override
            public void OnAllDayChange(JSONObject data) {
                listOder.clear();
                JSONObject listOderAllDay= new JSONObject();
                try {
                    listOderAllDay = data.getJSONObject(year).getJSONObject(month).getJSONObject(day);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
                            listOder.add(item);

//                        }
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
    }

    private void initView() {
        logTxt = findViewById(R.id.logTxt);
    }

    void updaData(){
        int total=0;
        stringData.clear();
        logTxt.setText("");

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
        stringData.add(String.valueOf(total)+" k");
        for (String e:
             stringData) {
            logTxt.setText(logTxt.getText()+"\n\r"+e);
        }

    }
}
