package com.example.vongcat.View;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vongcat.Presenter.ListOder;
import com.example.vongcat.R;
import com.example.vongcat.View.OderAdapter.Item;
import com.example.vongcat.View.TableAdapter.Adapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MoreActivity extends AppCompatActivity {
    EditText nameOderMoreTxt;
    Item oldItem;
    ListView tableOderMoreLsv;
    Adapter tableAdapter;
    private Button doneMoreBtn;
    private EditText valueOderMoreTxt;
    private Button deleteMoreBtn;

    String table="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String item = getIntent().getStringExtra("item");
        table = getIntent().getStringExtra("table");
        Gson gson = new Gson();
        oldItem = gson.fromJson(item, Item.class);
        setContentView(R.layout.activity_more);

        initView();
        addEvent();
    }

    private void addEvent() {
        doneMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(tableAdapter.getSelectedItem() != null)
                    table=tableAdapter.getSelectedItem().getName();
                Item newItem = new Item(
                        oldItem.getName(),
                        table,
                        oldItem.getValue(),
                        oldItem.isPaid());
                newItem.setKey(oldItem.getKey());
                Task<Void> task = ListOder.getInstance().editOder(oldItem,newItem);
                task.addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(v.getContext(),"đã thay đổi xong",Toast.LENGTH_LONG).show();
                                finish();
                            }
                        });


            }
        });
        deleteMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                    Task<Void> task = ListOder.getInstance().removeOder(oldItem.getKey());
                    task.addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(v.getContext(),"đã xóa xong",Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            });


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
