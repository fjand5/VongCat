package com.example.vongcat.Model;

import android.util.Log;

import com.example.vongcat.Presenter.ListOder;
import com.example.vongcat.Presenter.ListTable;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ListOderFirebase {
    DatabaseReference myRef;
    private static  ListOderFirebase ourInstance;

    static public ListOderFirebase getInstance() {
        if(ourInstance == null)
            ourInstance = new ListOderFirebase();
        return ourInstance;
    }

    public ListOderFirebase() {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("ListOder");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("htl","onDataChange");

                String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

                DataSnapshot dataSnapshotChild = dataSnapshot.child(currentDate);
                String tmp="{}";
                if(dataSnapshotChild.getValue() != null)
                    tmp = dataSnapshotChild.getValue().toString();
                try {
                    JSONObject jsonObject = new JSONObject(tmp);

                    ListOder.getInstance().updateData(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("htl","onCancelled: " + databaseError.toString());

            }
        });
    }
    public void addOder(JSONObject oder){
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

       DatabaseReference databaseReference = myRef.child(currentDate).push();
        try {
            oder.put("key",databaseReference.getKey());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        databaseReference .setValue(oder.toString());
    }
    public void removeOder(JSONObject oder){
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        try {
            myRef.child(currentDate).child(oder.getString("key")).setValue(null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void setIsPaidOder(JSONObject oder){
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        try {
            oder.put("isPaid",true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            myRef.child(currentDate).child(oder.getString("key")).setValue(oder.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public boolean editOder(JSONObject OldOder,JSONObject NewOder){
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        Log.d("htl","OldOder: "+ OldOder.toString());
        Log.d("htl","NewOder: "+ NewOder.toString());

            try {
                if(OldOder.get("key").equals(NewOder.get("key"))){

                    myRef.child(currentDate).child(OldOder.getString("key")).setValue(NewOder.toString());
                    return true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return false;
    }
}
