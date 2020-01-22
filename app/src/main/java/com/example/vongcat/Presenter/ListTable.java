package com.example.vongcat.Presenter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.vongcat.Model.ListTableFirebase;
import com.example.vongcat.View.TableAdapter.Item;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ListTable{
    private static ListTable ourInstance;

    OnListTableChange onListTableChange;

    List<Item> listItem;
    public void setOnListTableChange(OnListTableChange onListTableChange) {
        this.onListTableChange = onListTableChange;
    }

    public static ListTable getInstance(List<Item> objects) {
        if (ourInstance == null)
            ourInstance = new ListTable(objects);
        return ourInstance;
    }
    public static ListTable getInstance() {
        return ourInstance;
    }

    public List<Item> getListItem() {
        return listItem;
    }

    private ListTable(List<Item> objects) {
    ListTableFirebase.getInstance();
    listItem = objects;

    }
    public void updateData(JSONArray listTable){

        outer: for (int i=0; i < listTable.length(); i++) {
            try {
                String name = listTable.get(i).toString();

                for (Item table:listItem) {
                    if(table.getName().equals(name)){
                        continue outer;
                    }
                }
                listItem.add(new Item(name,0));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        onListTableChange.callBack();

    };
    public interface OnListTableChange{
        void callBack();
    }
}
