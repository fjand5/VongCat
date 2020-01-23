package com.example.vongcat.Presenter;

import android.util.Log;

import com.example.vongcat.Model.ListTableFirebase;
import com.example.vongcat.View.TableAdapter.Item;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;
import java.util.Set;

public class ListTable{
    private static ListTable ourInstance;

    OnListTableChange onListTableChange;

    List<Item> listItem;
    public void setOnListTableChange(OnListTableChange onListTableChange) {
        this.onListTableChange = onListTableChange;
    }

    public static ListTable getInstance() {
        if (ourInstance == null)
            ourInstance = new ListTable();
        return ourInstance;
    }
    public ListTable setListItem(List<Item> list){
        listItem = list;
        return ourInstance;
    }

    public List<Item> getListItem() {
        return listItem;
    }

    private ListTable() {
    ListTableFirebase.getInstance();

    }
    public void updateData(JSONArray listTable){
        listItem.clear();
       for (int i=0; i < listTable.length(); i++) {
            try {
                String name = listTable.get(i).toString();
                listItem.add(new Item(name,0));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(onListTableChange != null)
            onListTableChange.callBack(listItem);

    };
    public interface OnListTableChange{
        void callBack(List<Item> listItem);
    }
}
