package com.example.vongcat.Model;

import android.util.Log;

import com.example.vongcat.Presenter.ListTable;
import com.example.vongcat.Presenter.Supply;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SupplyFirebase {
    static FirebaseDatabase database;
    DatabaseReference myRef;
    DataSnapshot curData;
    private static final SupplyFirebase ourInstance = new SupplyFirebase();

    static public SupplyFirebase getInstance() {
        database = FirebaseDatabase.getInstance();
        database.goOnline();
        return ourInstance;
    }

    public SupplyFirebase() {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Supply");
        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                curData = dataSnapshot;
                Gson gson = new Gson();
                                    String s1 = gson.toJson(dataSnapshot.getValue());
                                    try {
                        JSONObject object = new JSONObject(s1);
                                        Supply.getInstance().updateData(object);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }
    public boolean isReady(){
        if (curData == null)
            return false;
        return true;
    }
    public Task<Void> importSup(String name, double value){
        if(!isReady())
            return null;
        double newValue=0;
        if(curData.hasChild(name)){

            newValue = Float.parseFloat(curData.child(name).getValue().toString());
            newValue = newValue +value;
            return myRef.child(name).setValue(newValue);
        }
        return myRef.child(name).setValue(value);
    }
    public Task<Void> exportSup(String name, double value){
        if(!isReady())
            return null;
        double newValue=0;
        if(curData.hasChild(name)){
            newValue = Float.parseFloat(curData.child(name).getValue().toString());
            newValue = newValue - value;
            return myRef.child(name).setValue(newValue);
        }
        return myRef.child(name).setValue(0-value);
    }

}
