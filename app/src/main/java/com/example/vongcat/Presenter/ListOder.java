package com.example.vongcat.Presenter;

import android.util.Log;

import com.example.vongcat.Model.ListTableFirebase;
import com.example.vongcat.View.OderAdapter.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ListOder {
    private static ListOder ourInstance;

    OnListOderChange onListOderChange;

    List<Item> listItem;
    public void setOnListOderChange(OnListOderChange onListOderChange) {
        this.onListOderChange = onListOderChange;
    }

    public static ListOder getInstance() {
        if (ourInstance == null)
            ourInstance = new ListOder();
        return ourInstance;
    }


    public List<Item>  getListItem() {
        return listItem;
    }

    private ListOder() {
    }
    public ListOder setListItem(List<Item> list){
        listItem = list;
        return ourInstance;
    }
    public void updateData(JSONObject listOder){
        Iterator<String> keys = listOder.keys();
        listItem.clear();
        while(keys.hasNext()) {
            String key = keys.next();
            try {
                if (listOder.get(key) instanceof JSONObject) {
                    JSONObject jsonObject = new JSONObject(listOder.get(key).toString());
                    String name = jsonObject.getString("name");
                    String table = jsonObject.getString("table");
                    int value = jsonObject.getInt("value");
                    boolean isPaid = jsonObject.getBoolean("isPaid");
                    listItem.add(new Item(name,table,value,isPaid));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        if(onListOderChange != null)
            onListOderChange.callBack(listItem);

    };
    public interface OnListOderChange{
        void callBack(List<Item> listItem);
    }
}
