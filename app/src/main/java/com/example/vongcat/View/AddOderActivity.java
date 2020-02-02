package com.example.vongcat.View;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vongcat.Presenter.ListOder;
import com.example.vongcat.R;
import com.example.vongcat.View.TableAdapter.Adapter;
import com.example.vongcat.View.TableAdapter.Item;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AddOderActivity extends AppCompatActivity {

    ListView tableLsv;
    Adapter tableAdapter;

    ListView beverageLsv;
    com.example.vongcat.View.BeverageAdapter.Adapter beverageAdapter;

    static com.example.vongcat.View.TableAdapter.Item mItemTable=null;
    static List<com.example.vongcat.View.BeverageAdapter.Item> mItemBeverage=null;

    FloatingActionButton submitOderFab;
    static MenuItem statusMni;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actviti_add_oder,menu);
        statusMni = menu.findItem(R.id.statusMni);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_oder);
        mItemTable = null;
        mItemBeverage = new ArrayList<>();
        initView();
        addEvent();
    }

    private void addEvent() {
        submitOderFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmp = "Chưa chọn món";
                Map<String,Integer> data = new HashMap<>();

                for (com.example.vongcat.View.BeverageAdapter.Item item :
                        mItemBeverage) {
                    if(data.containsKey(item.getName())){
                        data.put(item.getName(),data.get(item.getName())+1);
                    }else {
                        data.put(item.getName(),1);
                    }
                }
                if(data.size()>0)
                    tmp=data.toString()
                            .replace("{","")
                            .replace("}","")
                            .replace(", ","\n")
                            .replace("=",": ");


                final AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setTitle("danh sách món");
                alert.setMessage(tmp);
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(mItemTable == null){
                            Toast toast = new Toast(alert.getContext());
                            TextView textView = new TextView(alert.getContext());
                            textView.setText("chưa chọn bàn");
                            textView.setBackgroundColor(Color.RED);
                            textView.setTextColor(Color.WHITE);
                            toast.setView(textView);
                            toast.show();
                        }else{
                            for (com.example.vongcat.View.BeverageAdapter.Item item:
                                    mItemBeverage) {
                                Task<Void> task  = ListOder.getInstance().addOder(mItemTable,item);
                                task.addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast toast = new Toast(alert.getContext());
                                        TextView textView = new TextView(alert.getContext());
                                        textView.setText("đã thay đổi xong");
                                        textView.setBackgroundColor(Color.GREEN);
                                        textView.setTextColor(Color.WHITE);
                                        toast.setView(textView);
                                        toast.show();
                                    }

                                });
                            }
                            finish();
                        }
                    }
                });
                alert.setNegativeButton("Hủy",null);
                alert.show();
            }
        });

    }

    private void initView() {
        tableLsv= findViewById(R.id.tableLsv);
        TextView headerViewTableLsv = new TextView(this);
        headerViewTableLsv.setText("Chọn bàn");
        tableLsv.addHeaderView(headerViewTableLsv);

        List<Item> items = new ArrayList<>();
        tableAdapter = new Adapter(this,R.layout.item_table,items);
        tableLsv.setAdapter(tableAdapter);

        beverageLsv = findViewById(R.id.beverageLsv);
        TextView headerViewBeverageLsv = new TextView(this);
        headerViewBeverageLsv.setText("Chọn món");
        beverageLsv.addHeaderView(headerViewBeverageLsv);

        List<com.example.vongcat.View.BeverageAdapter.Item> itemsBeverage = new ArrayList<>();
        beverageAdapter = new com.example.vongcat.View.BeverageAdapter.Adapter(this,R.layout.item_beverage,itemsBeverage);
        beverageLsv.setAdapter(beverageAdapter);

        submitOderFab = findViewById(R.id.submitOderFab);

    }

    public static void setChoice(com.example.vongcat.View.TableAdapter.Item itemTable){
        mItemTable = itemTable;
        statusMni.setTitle("Chọn bàn " + itemTable.getName());
    };
    public static void addBeverage(com.example.vongcat.View.BeverageAdapter.Item itemBeverage ){
        mItemBeverage.add(itemBeverage);
        statusMni.setTitle("+ " + itemBeverage.getName() + " còn "+mItemBeverage.size() +" món");
    };
    public static void removeBeverage(com.example.vongcat.View.BeverageAdapter.Item itemBeverage ){
        mItemBeverage.remove(itemBeverage);
        statusMni.setTitle("- " + itemBeverage.getName() + " còn "+mItemBeverage.size() +" món");
    };
    public static List<com.example.vongcat.View.BeverageAdapter.Item> getBeverage(){
       return mItemBeverage;
    };


}
