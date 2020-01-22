package com.example.vongcat.Presenter;

import com.example.vongcat.Model.ListBeverageFirebase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ListBeverage {
    private static final ListBeverage ourInstance = new ListBeverage();

    public static ListBeverage getInstance() {

        return ourInstance;
    }

    OnListBeverageChange onListBeverageChange;
    Map<String, Integer> listBeverage;
    public void setOnListBeverageChange(OnListBeverageChange onListBeverageChange) {
        this.onListBeverageChange = onListBeverageChange;
    }

    private ListBeverage() {
        listBeverage = new HashMap<>();
        ListBeverageFirebase.getInstance();
    }
    public void updateData(JSONArray arrBeverage){

        listBeverage.clear();
        for (int i=0; i < arrBeverage.length(); i++) {
            try {
                JSONObject bvr = arrBeverage.getJSONObject(i);

                listBeverage.put(bvr.getString("name"),bvr.getInt("val"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(onListBeverageChange != null)
            onListBeverageChange.callBack(listBeverage);

    };
    public interface OnListBeverageChange{
        void callBack(Map<String, Integer> listBeverage);
    }
}
