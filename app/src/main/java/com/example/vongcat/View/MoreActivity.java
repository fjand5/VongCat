package com.example.vongcat.View;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.vongcat.Presenter.ListOder;
import com.example.vongcat.R;
import com.example.vongcat.View.OderAdapter.Item;
import com.example.vongcat.View.TableAdapter.Adapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MoreActivity extends Activity {
    EditText nameOderMoreTxt;
    Item oldItem;
    ListView tableOderMoreLsv;
    Adapter tableAdapter;
    private Button doneMoreBtn;
    private EditText valueOderMoreTxt;
    private Button deleteMoreBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String item = getIntent().getStringExtra("item");
        Gson gson = new Gson();
        oldItem = gson.fromJson(item, Item.class);
        setContentView(R.layout.activity_more);

        initView();
        addEvent();
    }

    private void addEvent() {
        doneMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Item newItem = new Item(
                        oldItem.getName(),
                        tableAdapter.getSelectedItem().getName(),
                        oldItem.getValue(),
                        oldItem.isPaid());
                newItem.setKey(oldItem.getKey());
                ListOder.getInstance().editOder(oldItem,newItem);
                finish();
            }
        });
        deleteMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListOder.getInstance().removeOder(oldItem.getKey());
                finish();
            }
        });
    }

    private void initView() {
        nameOderMoreTxt = findViewById(R.id.nameOderMoreTxt);
        nameOderMoreTxt.setText(oldItem.getName());

        valueOderMoreTxt=findViewById(R.id.valueOderMoreTxt);
        valueOderMoreTxt.setText(String.valueOf(oldItem.getValue()));


        tableOderMoreLsv = findViewById(R.id.tableOderMoreLsv);
        List<com.example.vongcat.View.TableAdapter.Item> items = new ArrayList<>();
        tableAdapter = new Adapter(this,R.layout.item_table,items);
        tableOderMoreLsv.setAdapter(tableAdapter);

        doneMoreBtn=findViewById(R.id.doneMoreBtn);
        deleteMoreBtn=findViewById(R.id.deleteMoreBtn);
    }
}
