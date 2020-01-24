package com.example.vongcat.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.vongcat.Model.ListOderFirebase;
import com.example.vongcat.Presenter.ListOder;
import com.example.vongcat.R;
import com.example.vongcat.View.OderAdapter.Adapter;
import com.example.vongcat.View.OderAdapter.Item;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView oderLsv;
    Adapter adapterTable;
    List<Item> itemOder;


    Button addOderBtn;
    Button payOderBtn;
    private static TextView sumOderTxt;


    static List<Item> item4Pay;
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
    }

    private void initView() {
        oderLsv = findViewById(R.id.oderLsv);
        addOderBtn = findViewById(R.id.addOderBtn);
        sumOderTxt = findViewById(R.id.sumOderTxt);
        payOderBtn= findViewById(R.id.payOderBtn);

        itemOder = new ArrayList<>();
        adapterTable = new Adapter(this,R.layout.item_oder,itemOder);
        oderLsv.setAdapter(adapterTable);
    }
    public static void addOder4Pay(Item item){
        int sum=0;
        Log.d("htl","add: " + item.getValue());
        item4Pay.add(item);
        for (Item e:
                item4Pay) {

            sum+=e.getValue();
        }
        sumOderTxt.setText(String.valueOf(sum));
    }
    public static void removeOder4Pay(Item item){
        int sum=0;
        Log.d("htl","removeOder4Pay: " + item.getValue());
        item4Pay.remove(item);
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
