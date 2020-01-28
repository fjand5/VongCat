package com.example.vongcat.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.example.vongcat.Presenter.ListOder;
import com.example.vongcat.R;
import com.example.vongcat.View.OderAdapter.Adapter;
import com.example.vongcat.View.OderAdapter.Item;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView oderLsv;
    Adapter adapterOder;
    List<Item> itemOder;


    Button addOderBtn;
    Button payOderBtn;
    private static TextView sumOderTxt;


    static List<Item> item4Pay;
    private TextView loadingTxt;
    private TextView soldTxt;
    private TextView receivedTxt;
    static int sumSold=0;
    static int sumReceived=0;

    static int sum=0;
    private Button logBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        item4Pay = new ArrayList<>();
        initView();
        addEvent();



    }

    private void addEvent() {
        addOderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),AddOderActivity.class);
                startActivity(i);
            }
        });

        payOderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (Item item :
                        item4Pay) {
                    ListOder.getInstance().setIsPaidOder(item.getKey());
                }
                item4Pay.clear();
            }
        });
        adapterOder.setOnDataChange(new Adapter.OnDataChange() {
            @Override
            public void callBack(List<Item> itemList, List<Item> listAllItem) {
                sumSold=0;
                sumReceived=0;

                for (Item item :
                        listAllItem) {
                    sumSold+=item.getValue();
                    if(item.isPaid())
                        sumReceived+=item.getValue();
                }
                soldTxt.setText("Đã bán: " + String.valueOf(sumSold)+"k");
                receivedTxt.setText("Đã thu: " + String.valueOf(sumReceived)+"k");
            }
        });
        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Intent i = new Intent(v.getContext(),LogActivity.class);
                        i.putExtra("year",year);
                        i.putExtra("month",month+1);
                        i.putExtra("day",dayOfMonth);
                        startActivity(i);
                    }
                }, Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DATE));
                datePickerDialog.show();
            }
        });
    }

    @Override
    protected void onResume() {
        soldTxt.setText("Đã bán: " + String.valueOf(sumSold)+"k");
        receivedTxt.setText("Đã thu: " + String.valueOf(sumReceived)+"k");

        loadingTxt.setVisibility(View.VISIBLE);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("check");
        myRef.setValue("ok").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                loadingTxt.setVisibility(View.INVISIBLE);
            }
        });
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void initView() {
        oderLsv = findViewById(R.id.oderLsv);
        addOderBtn = findViewById(R.id.addOderBtn);
        sumOderTxt = findViewById(R.id.sumOderTxt);
        payOderBtn= findViewById(R.id.payOderBtn);
        loadingTxt = findViewById(R.id.loadingTxt);
        soldTxt = findViewById(R.id.soldTxt);
        receivedTxt = findViewById(R.id.receivedTxt);
        logBtn = findViewById(R.id.logBtn);

        itemOder = new ArrayList<>();
        adapterOder = new Adapter(this,R.layout.item_oder,itemOder);
        oderLsv.setAdapter(adapterOder);




    }
    public static void addOder4Pay(Item item){
        item4Pay.remove(item);
        item4Pay.add(item);
        setSumOderTxt();
    }
    public static void removeOder4Pay(Item item){
        item4Pay.remove(item);
        setSumOderTxt();
    }
    public static void clearOder4Pay(){
        item4Pay.clear();
        setSumOderTxt();
    }
    static void setSumOderTxt(){
        sum =0;
        for (Item e:
                item4Pay) {
            sum+=e.getValue();
        }
        sumOderTxt.setText(String.valueOf(sum));
    }
    public static List<Item> getOder4Pay(){
        return  item4Pay;
    }
}
