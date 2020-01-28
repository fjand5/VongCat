package com.example.vongcat.Presenter;

import android.util.Log;

import com.example.vongcat.Model.ListBeverageFirebase;
import com.example.vongcat.View.BeverageAdapter.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListBeverage {
    private static final ListBeverage ourInstance = new ListBeverage();

    JSONArray mJsonArray;
    public static ListBeverage getInstance() {

        return ourInstance;
    }

    OnListBeverageChange onListBeverageChange;
    List<Item> listBeverage;
    public void setOnListBeverageChange(OnListBeverageChange onListBeverageChange) {
        this.onListBeverageChange = onListBeverageChange;
    }

    private ListBeverage() {
        mJsonArray = new JSONArray();
        listBeverage = new ArrayList<>();
        ListBeverageFirebase.getInstance();
    }
    public ListBeverage setListItem(List<Item> list){
        listBeverage = list;
        return ourInstance;
    }

    public JSONArray getmJsonArray() {
        return mJsonArray;
    }

    public void setmJsonArray(JSONArray mJsonArray) {
        this.mJsonArray = mJsonArray;
    }

    public void updateData(JSONArray arrBeverage){
        mJsonArray = arrBeverage;
        listBeverage.clear();
        for (int i=0; i < arrBeverage.length(); i++) {
            try {
                JSONObject bvr = arrBeverage.getJSONObject(i);
                if(!bvr.has("color")){
                    bvr.put("color","#000000");
                }
                listBeverage.add(new Item(bvr.getString("name")
                        ,bvr.getInt("val")
                 ,bvr.getString("color")));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(onListBeverageChange != null)
            onListBeverageChange.callBack(listBeverage);

    };
    public interface OnListBeverageChange{
        void callBack(List<Item>  listBeverage);
    }
}
