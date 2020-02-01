package com.example.vongcat.Presenter;

import android.util.Log;

import com.example.vongcat.Model.ListOderFirebase;
import com.example.vongcat.View.OderAdapter.Item;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class ListOder {
    private static ListOder ourInstance;

    OnListOderChange onListOderChange;

    List<Item> listItem;
    List<Item> listAllItem;
    JSONObject mJsonObject= null;
    public void setOnListOderChange(OnListOderChange onListOderChange) {
        this.onListOderChange = onListOderChange;
    }

    public static ListOder getInstance() {
        if (ourInstance == null)
            ourInstance = new ListOder();
        return ourInstance;
    }

    public JSONObject getmJsonObject() {
       return this.mJsonObject;
    }

    public List<Item>  getListItem() {
        return listItem;
    }

    private ListOder() {
        listAllItem = new ArrayList<>();
        mJsonObject = new JSONObject();
        ListOderFirebase.getInstance();
    }
    public ListOder setListItem(List<Item> list){
        listItem = list;
        return ourInstance;
    }
    public void refesh(){
        ListOderFirebase.getInstance().refesh();
    }
    public void updateData(JSONObject listOderAllDay){
        mJsonObject = listOderAllDay;
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String currentMonth = new SimpleDateFormat("MM", Locale.getDefault()).format(new Date());
        String currentYear = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
//
        JSONObject listOder= new JSONObject();
        try {
            listOder = listOderAllDay.getJSONObject(currentYear).getJSONObject(currentMonth).getJSONObject(currentDate);


        } catch (JSONException e) {
            e.printStackTrace();
        }




        listItem.clear();
        listAllItem.clear();
        Iterator<String> keys = listOder.keys();
        while(keys.hasNext()) {

            String key = keys.next();

            try {
//                if (listOder.get(key) instanceof JSONObject) {
                    JSONObject jsonObject = new JSONObject(listOder.get(key).toString());
                    String name = jsonObject.getString("name");
                    String table = jsonObject.getString("table");
                    int value = jsonObject.getInt("value");
                    boolean isPaid = jsonObject.getBoolean("isPaid");
                    String mkey = jsonObject.getString("key");

                    Item item = new Item(name,table,value,isPaid);
                    item.setKey(mkey);
                    if(isPaid == false){

                        listItem.add(item);
                    }
                    listAllItem.add(item);

//                }
            } catch (JSONException e) {

                e.printStackTrace();
            }
        }
        Collections.sort(listItem, new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                int cmpTable = o1.getTable().compareTo(o2.getTable());
                int cmpName = o1.getName().compareTo(o2.getName());
                if( cmpTable== 0){
                    return cmpName;
                }else
                return cmpTable;
            }
        });


     
     
        
        


        if(onListOderChange != null){

            onListOderChange.OnTodayChange(listItem, listAllItem);
            onListOderChange.OnAllDayChange(listOderAllDay);
        }

    };
    public interface OnListOderChange{
        void OnTodayChange(List<Item> listItem, List<Item> listAllItem);
        void OnAllDayChange(JSONObject jsonObject);

    }
    public Task<Void> addOder(com.example.vongcat.View.TableAdapter.Item table,
                              com.example.vongcat.View.BeverageAdapter.Item beverage){
        Item item = new Item(beverage.getName(),table.getName(),beverage.getValue(),false);

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("name",item.getName());
            jsonObject.put("table",item.getTable());
            jsonObject.put("value",item.getValue());
            jsonObject.put("isPaid",item.isPaid());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ListOderFirebase.getInstance().addOder(jsonObject);
    }
    public  Task<Void> editOder(Item OldOder,
                         Item NewOder){

        return ListOderFirebase.getInstance().editOder(OldOder.getJson(),NewOder.getJson());
    }


    public  Task<Void> removeOder(String key){
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("key",key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ListOderFirebase.getInstance().removeOder(jsonObject);
    }
    public  Task<Void> setIsPaidOder(String key){
        JSONObject jsonObject = new JSONObject();
        for (Item item:
        listItem) {
            if(item.getKey().equals(key)){
                try {
                    jsonObject.put("name",item.getName());
                    jsonObject.put("table",item.getTable());
                    jsonObject.put("value",item.getValue());
                    jsonObject.put("isPaid",item.isPaid());
                    jsonObject.put("key",item.getKey());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        return ListOderFirebase.getInstance().setIsPaidOder(jsonObject);
    }
}
