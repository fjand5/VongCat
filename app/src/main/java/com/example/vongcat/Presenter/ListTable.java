package com.example.vongcat.Presenter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.example.vongcat.View.TableAdapter.Item;

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

        listItem = objects;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    listItem.add(new Item("ten",123));
                    onListTableChange.callBack();



                }


            }
        }).start();
    }
    public interface OnListTableChange{
        void callBack();
    }
}
