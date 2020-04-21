package com.example.vongcat.Model;

import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;

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
import java.util.Map;

public class ListTableFirebase {
    static FirebaseDatabase database;
    DatabaseReference myRef;
    private static final ListTableFirebase ourInstance = new ListTableFirebase();

    static public ListTableFirebase getInstance() {
        database = FirebaseDatabase.getInstance();
        database.goOnline();
        return ourInstance;
    }

    public ListTableFirebase() {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("ListTable");

        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                JSONArray jsonArray = new JSONArray();
                for (DataSnapshot e:
                        dataSnapshot.getChildren()) {
                    jsonArray.put(e.getValue());
                }

                ListTable.getInstance().updateData(jsonArray);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }
}
