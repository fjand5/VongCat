package com.example.vongcat.Model;

import android.util.Log;

import com.example.vongcat.Presenter.ListBeverage;
import com.example.vongcat.Presenter.ListExpense;
import com.example.vongcat.Presenter.ListOder;
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

public class ListExpenseFirebase {
    static FirebaseDatabase database;

    DatabaseReference myRef;
    private static ListExpenseFirebase ourInstance;

    static public ListExpenseFirebase getInstance() {
        database = FirebaseDatabase.getInstance();
        database.goOnline();
        if(ourInstance == null)
            ourInstance = new ListExpenseFirebase();
        return ourInstance;
    }

    public ListExpenseFirebase() {

       database = FirebaseDatabase.getInstance();
        myRef = database.getReference("ListExpense");
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
    public void addExps(JSONObject exps){

       DatabaseReference databaseReference = getTodayDatabaseReference().push();
        try {
            exps.put("key",databaseReference.getKey());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        databaseReference .setValue(exps.toString());
    }
    public void removeExps(JSONObject exps){
        try {
            getTodayDatabaseReference().child(exps.getString("key")).setValue(null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean editExpsOder(JSONObject OldExps,JSONObject NewExps){
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());


            try {
                if(OldExps.get("key").equals(NewExps.get("key"))){

                    getTodayDatabaseReference().child(OldExps.getString("key")).setValue(NewExps.toString());
                    return true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return false;
    }
    private DatabaseReference getTodayDatabaseReference(){
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String currentMonth = new SimpleDateFormat("MM", Locale.getDefault()).format(new Date());
        String currentYear = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());

        DatabaseReference databaseReference = myRef.child(currentYear).child(currentMonth).child(currentDate);
        return databaseReference;
    }
}
