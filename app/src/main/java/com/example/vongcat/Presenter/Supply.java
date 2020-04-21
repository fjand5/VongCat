package com.example.vongcat.Presenter;

import android.util.Log;

import com.example.vongcat.Model.SupplyFirebase;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONObject;


public class Supply {
    private static Supply ourInstance;
    OnSupplyChange onSupplyChange;
    JSONObject lastListSupply;
    public void setOnSupplyChange(OnSupplyChange onSupplyChange) {
        this.onSupplyChange = onSupplyChange;
    }

    public static Supply getInstance() {
        if (ourInstance == null)
            ourInstance = new Supply();
        return ourInstance;
    }


    private Supply() {

        SupplyFirebase.getInstance();

    }
    public void trigUpdate(){

        if(lastListSupply != null)
            updateData(lastListSupply);
    }
    public void updateData(JSONObject listSupply){
        lastListSupply = listSupply;
        if(onSupplyChange != null)
            onSupplyChange.callBack(listSupply);
    };
    public Task<Void> importSupply(String name, double value){
        return SupplyFirebase.getInstance().importSup(name,value);
    }
    public Task<Void> exportSupply(String name, double value){

        return SupplyFirebase.getInstance().exportSup(name,value);
    }
    public interface OnSupplyChange{
        void callBack(JSONObject data);
    }
}
