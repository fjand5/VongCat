package com.example.vongcat.View;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vongcat.R;
import com.example.vongcat.View.StoreManagerAdapter.Adapter;
import com.example.vongcat.View.StoreManagerAdapter.Item;

import java.util.ArrayList;
import java.util.List;

public class StoreManagerActivity extends AppCompatActivity {
    ListView storeManagerLsv;
    Adapter storeManagerAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_manager);
        initView();
    }

    private void initView() {
        storeManagerLsv = findViewById(R.id.storeManagerLsv);

        List<Item> listItem = new ArrayList<>();
        storeManagerAdapter = new Adapter(this,R.layout.item_supply,listItem);
        storeManagerLsv.setAdapter(storeManagerAdapter);
    }
}
