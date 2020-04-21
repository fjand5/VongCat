package com.example.vongcat.Model;

import android.util.Log;

import com.example.vongcat.Presenter.ListOder;
import com.example.vongcat.Presenter.ListTable;
import com.google.android.gms.tasks.Task;
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
import java.util.Map;

public class ListOderFirebase {
    static FirebaseDatabase database;
    DatabaseReference myRef;
    private static  ListOderFirebase ourInstance;

    static public ListOderFirebase getInstance() {
        database = FirebaseDatabase.getInstance();
        database.goOnline();
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
                JSONObject jsonObject = null;


                    jsonObject = new JSONObject((Map)dataSnapshot.getValue());

                ListOder.getInstance().updateData(jsonObject);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public void refesh(){

        myRef.child("refesh").setValue(System.currentTimeMillis());
    }
    public Task<Void> addOder(JSONObject oder){

       DatabaseReference databaseReference = getTodayDatabaseReference().push();
        try {
            oder.put("key",databaseReference.getKey());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return databaseReference .setValue(oder.toString());
    }
    public Task<Void> removeOder(JSONObject oder){
        try {
            return getTodayDatabaseReference().child(oder.getString("key")).setValue(null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Task<Void> setIsPaidOder(JSONObject oder){
        try {
            oder.put("isPaid",true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            return getTodayDatabaseReference().child(oder.getString("key")).setValue(oder.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public  Task<Void> editOder(JSONObject OldOder,JSONObject NewOder){
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());


            try {
                if(OldOder.get("key").equals(NewOder.get("key"))){

                   return getTodayDatabaseReference().child(OldOder.getString("key")).setValue(NewOder.toString());

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return null;
    }
    private DatabaseReference getTodayDatabaseReference(){
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String currentMonth = new SimpleDateFormat("MM", Locale.getDefault()).format(new Date());
        String currentYear = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());

        DatabaseReference databaseReference = myRef.child(currentYear).child(currentMonth).child(currentDate);
        return databaseReference;
    }
}
