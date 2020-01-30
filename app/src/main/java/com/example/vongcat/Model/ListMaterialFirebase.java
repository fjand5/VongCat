package com.example.vongcat.Model;

import com.example.vongcat.Presenter.ListBeverage;
import com.example.vongcat.Presenter.ListMaterial;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;

public class ListMaterialFirebase {
    DatabaseReference myRef;
    private static final ListMaterialFirebase ourInstance = new ListMaterialFirebase();

    static public ListMaterialFirebase getInstance() {
        return ourInstance;
    }

    public ListMaterialFirebase() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("ListMaterial");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null)
                    return;
                try {
                    JSONArray jsonArray = new JSONArray(dataSnapshot.getValue().toString());

                    ListMaterial.getInstance().updateData(jsonArray);
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
