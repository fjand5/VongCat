package com.example.vongcat.View;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.annotation.Nullable;

import com.example.vongcat.R;
import com.example.vongcat.View.TableAdapter.Adapter;
import com.example.vongcat.View.TableAdapter.Item;

import java.util.ArrayList;
import java.util.List;

public class AddOderActivity extends Activity {

    ListView tableLsv;
    Adapter tableAdapter;

    ListView beverageLsv;
    com.example.vongcat.View.BeverageAdapter.Adapter beverageAdapter;

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_oder);

    }

    private void initView() {
        tableLsv= findViewById(R.id.tableLsv);

        List<Item> items = new ArrayList<>();
        tableAdapter = new Adapter(this,R.layout.item_table,items);
        tableLsv.setAdapter(tableAdapter);

//
        beverageLsv = findViewById(R.id.beverageLsv);

        List<com.example.vongcat.View.BeverageAdapter.Item> itemsBeverage = new ArrayList<>();
        beverageAdapter = new com.example.vongcat.View.BeverageAdapter.Adapter(this,R.layout.item_beverage,itemsBeverage);
        beverageLsv.setAdapter(beverageAdapter);

    }
}
