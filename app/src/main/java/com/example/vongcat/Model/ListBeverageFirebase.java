package com.example.vongcat.Model;

import android.util.Log;

import com.example.vongcat.Presenter.ListBeverage;
import com.example.vongcat.Presenter.ListTable;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ListBeverageFirebase {
    DatabaseReference myRef;
    private static final ListBeverageFirebase ourInstance = new ListBeverageFirebase();

    static public ListBeverageFirebase getInstance() {
        return ourInstance;
    }

    public ListBeverageFirebase() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("ListBeverage");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    JSONArray jsonArray = new JSONArray(dataSnapshot.getValue().toString());
                    ListBeverage.getInstance().updateData(jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }
}
