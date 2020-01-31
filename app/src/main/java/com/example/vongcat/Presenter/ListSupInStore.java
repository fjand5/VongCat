package com.example.vongcat.Presenter;

import android.util.Log;

import com.example.vongcat.Model.ListBeverageFirebase;
import com.example.vongcat.Model.ListMaterialFirebase;
import com.example.vongcat.Model.ListSupInStoreFirebase;
import com.example.vongcat.View.StoreManagerAdapter.Item;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ListSupInStore {
    private static final ListSupInStore ourInstance = new ListSupInStore();

    JSONObject mJsonObject;
    List<Item> mItemList;


    public static ListSupInStore getInstance() {

        return ourInstance;
    }

    OnListSupInStoreChange onListSupInStoreChange;
    public void setOnListSupInStoreChange(OnListSupInStoreChange onListSupInStoreChange) {
        this.onListSupInStoreChange = onListSupInStoreChange;
    }

    private ListSupInStore() {
        mJsonObject = new JSONObject();
        mItemList = new ArrayList<>();

        ListMaterial.getInstance().setOnListMaterialChange(new ListMaterial.OnListMaterialChange() {
            @Override
            public void callBack(List<String> listMaterial) {
                for (String name:
                        listMaterial) {
                    JSONObject jsonObject= new JSONObject();
                    try {
                        jsonObject.put("name",name);
                        jsonObject.put("quan",0);
                        ListSupInStoreFirebase.getInstance().importSup(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        ListSupInStoreFirebase.getInstance();
        updateData(new JSONObject());
    }

    public JSONObject getmJsonObject() {
        return mJsonObject;
    }

    public ListSupInStore setListItem(List<Item> list){
        mItemList = list;
        return ourInstance;
    }

    public void updateData(JSONObject objSup){
        mJsonObject = objSup;
        mItemList.clear();
        Iterator<String> keys = objSup.keys();
        ListMaterial.getInstance().updateData(
                ListMaterial.getInstance().getmJsonArray()
        );
        while(keys.hasNext()) {
            String key = keys.next();
            try {
                mItemList.add(new Item(key,objSup.getDouble(key)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(onListSupInStoreChange != null)
            onListSupInStoreChange.callBack(mItemList);

    };
    public void importSub(String name, double quan){

            JSONObject jsonObject= new JSONObject();
            try {
                jsonObject.put("name",name);
                jsonObject.put("quan",quan);
                ListSupInStoreFirebase.getInstance().importSup(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }


    }
    public void exportSub(String name, double quan){

        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("name",name);
            jsonObject.put("quan",quan);
            ListSupInStoreFirebase.getInstance().exportSup(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
    public interface OnListSupInStoreChange{
        void callBack(List<Item> listSup);
    }

}
