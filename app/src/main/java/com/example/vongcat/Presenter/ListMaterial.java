package com.example.vongcat.Presenter;

import android.util.Log;

import com.example.vongcat.Model.ListBeverageFirebase;
import com.example.vongcat.Model.ListMaterialFirebase;
import com.example.vongcat.View.BeverageAdapter.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListMaterial {
    private static final ListMaterial ourInstance = new ListMaterial();

    JSONArray mJsonArray;
    public static ListMaterial getInstance() {

        return ourInstance;
    }

    OnListMaterialChange onListMaterialChange;
    List<String> listMaterial;
    public void setOnListMaterialChange(OnListMaterialChange onListMaterialChange) {
        this.onListMaterialChange = onListMaterialChange;
    }

    private ListMaterial() {
        mJsonArray = new JSONArray();
        listMaterial = new ArrayList<>();
        ListMaterialFirebase.getInstance();

    }
    public ListMaterial setListItem(List<String> list){
        listMaterial = list;
        return ourInstance;
    }

    public JSONArray getmJsonArray() {
        return mJsonArray;
    }

    public void setmJsonArray(JSONArray mJsonArray) {
        this.mJsonArray = mJsonArray;
    }

    public void updateData(JSONArray arrMaterial){
        mJsonArray = arrMaterial;
        listMaterial.clear();
        for (int i=0; i < arrMaterial.length(); i++) {
            try {
                String name = arrMaterial.getString(i);

                listMaterial.add(name);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(onListMaterialChange != null)
            onListMaterialChange.callBack(listMaterial);

    };
    public interface OnListMaterialChange{
        void callBack(List<String> listMaterial);
    }
}
