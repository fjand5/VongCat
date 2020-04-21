package com.example.vongcat.Model;

import android.util.Log;

import com.example.vongcat.Presenter.ListBeverage;
import com.example.vongcat.Presenter.ListTable;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ListBeverageFirebase {
    static FirebaseDatabase database;
    DatabaseReference myRef;
    private static final ListBeverageFirebase ourInstance = new ListBeverageFirebase();

    static public ListBeverageFirebase getInstance() {
        database = FirebaseDatabase.getInstance();
        database.goOnline();
        return ourInstance;
    }

    public ListBeverageFirebase() {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("ListBeverage");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                JSONArray jsonArray = new JSONArray();
                for (DataSnapshot elmt:
                        dataSnapshot.getChildren()) {
                    Gson gson = new Gson();
                    String s1 = gson.toJson(elmt.getValue());
                    try {
                        JSONObject object = new JSONObject(s1);
                        jsonArray.put(object);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                ListBeverage.getInstance().updateData(jsonArray);



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }
}
