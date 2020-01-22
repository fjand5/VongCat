package com.example.vongcat.Presenter;

import com.example.vongcat.Model.ListTableFirebase;
import com.example.vongcat.View.OderAdapter.Item;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;
import java.util.Set;

public class ListTable{
    private static ListTable ourInstance;

    OnListTableChange onListTableChange;

    Set<String> listItem;
    public void setOnListTableChange(OnListTableChange onListTableChange) {
        this.onListTableChange = onListTableChange;
    }

    public static ListTable getInstance(List<String> objects) {
        if (ourInstance == null)
            ourInstance = new ListTable();
        return ourInstance;
    }
    public static ListTable getInstance() {
        return ourInstance;
    }

    public Set<String> getListItem() {
        return listItem;
    }

    private ListTable() {
    ListTableFirebase.getInstance();

    }
    public void updateData(JSONArray listTable){

       for (int i=0; i < listTable.length(); i++) {
            try {
                String name = listTable.get(i).toString();
                listItem.add(name);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(onListTableChange != null)
            onListTableChange.callBack(listItem);

    };
    public interface OnListTableChange{
        void callBack(Set<String> listItem);
    }
}
