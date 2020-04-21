package com.example.vongcat.View;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.example.vongcat.Presenter.ListBeverage;
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
    Spinner nameOderMoreSpn;
    Item oldItem;
    ListView tableOderMoreLsv;
    Adapter tableAdapter;
    private BootstrapButton doneMoreBtn;
    private BootstrapEditText valueOderMoreTxt;
    private BootstrapButton deleteMoreBtn;

    List<String> arrStringNameBeve;
    ArrayAdapter<String> adapterBeve;
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

        ListBeverage.getInstance().setOnListBeverageChange(new ListBeverage.OnListBeverageChange() {
            @Override
            public void callBack(List<com.example.vongcat.View.BeverageAdapter.Item> listBeverage) {
                arrStringNameBeve.clear();
                arrStringNameBeve.add(oldItem.getName());
                for (com.example.vongcat.View.BeverageAdapter.Item item:
                        listBeverage) {
                    if(!item.getName().equals(oldItem.getName()))
                        arrStringNameBeve.add(item.getName());
                }
            }
        });
        ListBeverage.getInstance().trigUpdate();


    }

    private void addEvent() {
        doneMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(tableAdapter.getSelectedItem() != null)
                    table=tableAdapter.getSelectedItem().getName();

                String name = nameOderMoreSpn.getSelectedItem().toString();
                Item newItem = new Item(
                        name,
                        table,
                        oldItem.getValue(),
                        oldItem.isPaid());
                newItem.setKey(oldItem.getKey());
                Task<Void> task = ListOder.getInstance().editOder(oldItem,newItem);
                task.addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(v.getContext(),"đã thay đổi xong",Toast.LENGTH_LONG).show();

                            }
                        });
                finish();

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

                                }
                            });

                finish();
            }
        });
    }

    private void initView() {
        nameOderMoreSpn = findViewById(R.id.nameOderMoreSpn);
        arrStringNameBeve = new ArrayList<>();
        arrStringNameBeve.add(oldItem.getName());
        adapterBeve = new ArrayAdapter<>(this,
                R.layout.spiner_item,
                arrStringNameBeve);




        nameOderMoreSpn.setAdapter(adapterBeve);
        valueOderMoreTxt=findViewById(R.id.valueOderMoreTxt);
        valueOderMoreTxt.setText(String.valueOf(oldItem.getValue()));
        valueOderMoreTxt.setEnabled(false);


        tableOderMoreLsv = findViewById(R.id.tableOderMoreLsv);
        List<com.example.vongcat.View.TableAdapter.Item> items = new ArrayList<>();
        tableAdapter = new Adapter(this,R.layout.item_table,items);
        tableOderMoreLsv.setAdapter(tableAdapter);

        doneMoreBtn=findViewById(R.id.doneMoreBtn);
        deleteMoreBtn=findViewById(R.id.deleteMoreBtn);
    }
}
