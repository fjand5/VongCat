package com.example.vongcat.Presenter;

import com.example.vongcat.Model.ListExpenseFirebase;
import com.example.vongcat.Model.ListOderFirebase;
import com.example.vongcat.View.OderAdapter.Item;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class ListExpense {
    private static ListExpense ourInstance;

    OnListExpsChange onListExpsChange;

    JSONObject mJsonObject= null;
    public void setOnListExpsChange(OnListExpsChange onListExpsChange) {
        this.onListExpsChange = onListExpsChange;
    }

    public static ListExpense getInstance() {
        if (ourInstance == null)
            ourInstance = new ListExpense();
        return ourInstance;
    }

    public JSONObject getmJsonObject() {
       return this.mJsonObject;
    }


    private ListExpense() {
        mJsonObject = new JSONObject();
        ListExpenseFirebase.getInstance();
    }
    public void updateData(JSONObject jsonData){
        mJsonObject = jsonData;
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String currentMonth = new SimpleDateFormat("MM", Locale.getDefault()).format(new Date());
        String currentYear = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
        if(onListExpsChange == null)
            return;
        onListExpsChange.OnDataChange(jsonData);
        try {
            onListExpsChange.OnTodayChange(
                    getSumValueOneDay( jsonData.getJSONObject(currentYear)
                            .getJSONObject(currentMonth)
                            .getJSONObject(currentDate)
                    )


            );
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            onListExpsChange.OnThisMonthChange(getSumValueOneMonth(jsonData.getJSONObject(currentYear)
                    .getJSONObject(currentMonth)));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    };
    public interface OnListExpsChange{
        void OnTodayChange(int val);
        void OnThisMonthChange(int val);
        void OnDataChange(JSONObject jsonObject);

    }
    public void addExps(int value,
                        String text){

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("value",value);
            jsonObject.put("text",text);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ListExpenseFirebase.getInstance().addExps(jsonObject);
    }


    public void removeExps(String key){
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("key",key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ListExpenseFirebase.getInstance().removeExps(jsonObject);
    }
    static public int getSumValueOneDay(JSONObject listOderAllDay){
        int ret = 0;
        Iterator<String> keys = listOderAllDay.keys();
        while(keys.hasNext()) {
            String key = keys.next();
            try {

                JSONObject jsonObject = new JSONObject(listOderAllDay.get(key).toString());

                int value = jsonObject.getInt("value");

                ret+=value;

//                        }
            } catch (JSONException e) {

                e.printStackTrace();
            }
        }
        return ret;
    }
    static public int getSumValueOneMonth(JSONObject monthJson){
        int sumMonth=0;
        Iterator<String> keys = monthJson.keys();
        while(keys.hasNext()) {
            String key = keys.next();
            try {

                JSONObject jsonObject = new JSONObject(monthJson.get(key).toString());
                sumMonth+=getSumValueOneDay(jsonObject);
//                        }
            } catch (JSONException e) {

                e.printStackTrace();
            }
        }
        return sumMonth;
    }
}
