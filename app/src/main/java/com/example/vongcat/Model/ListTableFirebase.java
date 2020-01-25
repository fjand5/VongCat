package com.example.vongcat.Model;

import android.util.JsonReader;
import android.util.Log;

import com.example.vongcat.Presenter.ListTable;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Reader;

public class ListTableFirebase {
    DatabaseReference myRef;
    private static final ListTableFirebase ourInstance = new ListTableFirebase();

    static public ListTableFirebase getInstance() {
        return ourInstance;
    }

    public ListTableFirebase() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("ListTable");
//        myRef.goOffline();

        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    JSONArray jsonArray = new JSONArray(dataSnapshot.getValue().toString());
                    ListTable.getInstance().updateData(jsonArray);
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
