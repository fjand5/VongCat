package com.example.vongcat.Model;

import android.util.Log;

import com.example.vongcat.Presenter.ListMaterial;
import com.example.vongcat.Presenter.ListSupInStore;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ListSupInStoreFirebase {

    public JSONObject mJsonObject = null;
    DatabaseReference myRef;


    private static final ListSupInStoreFirebase ourInstance = new ListSupInStoreFirebase();

    static public ListSupInStoreFirebase getInstance() {
        return ourInstance;
    }

    public ListSupInStoreFirebase() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("SupInStore");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mJsonObject = null;
                if(dataSnapshot.getValue() == null)
                    return;

                JSONObject jsonObject = new JSONObject();
                for (DataSnapshot child:
                        dataSnapshot.getChildren()) {
                    try {
                        jsonObject.put(child.getKey(),child.getValue());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                mJsonObject=jsonObject;
                ListSupInStore.getInstance().updateData(jsonObject);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void importSup(JSONObject sup){
        double quan=0;
        String name=null;
        try {
            quan = sup.getDouble("quan");
            name = sup.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(name == null)
            return;
        if (mJsonObject == null){
            return;
        }
        try {
            myRef.child(name).setValue(
                    mJsonObject.getDouble(name)+quan
            );
        } catch (JSONException e) {
            myRef.child(name).setValue(
                    quan
            );
            e.printStackTrace();
        }
    }
    public void exportSup(JSONObject sup){
        double quan=0;
        String name=null;
        try {
            quan = sup.getDouble("quan");
            name = sup.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("htl","quan: " + quan
                + "name: " + name);
        if (mJsonObject == null)
            return;

        if(name == null
                || !mJsonObject.has(name)
        )
            return;
        try {
            myRef.child(name).setValue(
                    mJsonObject.getDouble(name)-quan
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
