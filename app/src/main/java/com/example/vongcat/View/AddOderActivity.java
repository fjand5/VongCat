package com.example.vongcat.View;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vongcat.Presenter.ListOder;
import com.example.vongcat.R;
import com.example.vongcat.View.TableAdapter.Adapter;
import com.example.vongcat.View.TableAdapter.Item;

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
    private static TextView tableChoiceTxt;
    private static TextView beverageChoiceTxt;
    private static Button doneBtn;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actviti_add_oder,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem itemMenu) {
        switch (itemMenu.getItemId()) {
            case R.id.checkBtn:
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


                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("danh sách món");
                alert.setMessage(tmp);
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (com.example.vongcat.View.BeverageAdapter.Item item:
                                mItemBeverage) {
                            ListOder.getInstance().addOder(mItemTable,item);
                        }
                        Intent i = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(i);
                    }
                });
                alert.setNegativeButton("Hủy",null);
                alert.show();
                return true;
        }
        return super.onOptionsItemSelected(itemMenu);
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
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (com.example.vongcat.View.BeverageAdapter.Item item:
                        mItemBeverage) {
                    ListOder.getInstance().addOder(mItemTable,item);
                }
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
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

        tableChoiceTxt=findViewById(R.id.tableChoiceTxt);
        beverageChoiceTxt=findViewById(R.id.beverageChoiceTxt);

        doneBtn = findViewById(R.id.doneBtn);
        doneBtn.setEnabled(false);

    }

    public static void setChoice(com.example.vongcat.View.TableAdapter.Item itemTable){
        mItemTable = itemTable;
        if(tableChoiceTxt == null)
            return;
        tableChoiceTxt.setText(mItemTable.getName());
        if(mItemTable != null
                && mItemBeverage.size()>0){
            doneBtn.setEnabled(true);
        }
    };
    public static void addBeverage(com.example.vongcat.View.BeverageAdapter.Item itemBeverage ){
        mItemBeverage.add(itemBeverage);
        beverageChoiceTxt.setText(itemBeverage.getName());
        if(mItemTable != null
                && mItemBeverage.size()>0){
            doneBtn.setEnabled(true);
        }else{
            doneBtn.setEnabled(false);

        }
    };
    public static void removeBeverage(com.example.vongcat.View.BeverageAdapter.Item itemBeverage ){
        mItemBeverage.remove(itemBeverage);

        beverageChoiceTxt.setText(itemBeverage.getName());
        if(mItemTable != null
                && mItemBeverage.size()>0){
            doneBtn.setEnabled(true);
        }else{
            doneBtn.setEnabled(false);

        }
    };
    public static List<com.example.vongcat.View.BeverageAdapter.Item> getBeverage(){
       return mItemBeverage;
    };


}
