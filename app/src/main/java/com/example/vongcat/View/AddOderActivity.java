package com.example.vongcat.View;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vongcat.Presenter.ListOder;
import com.example.vongcat.Presenter.ListTable;
import com.example.vongcat.R;
import com.example.vongcat.View.TableAdapter.Item;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddOderActivity extends AppCompatActivity {
    final static String TABLE_SPINER_LABEL = "Vui lòng chọn bàn trước";
    Spinner tableSpn;

    static ListView beverageLsv;
    static com.example.vongcat.View.BeverageAdapter.Adapter beverageAdapter;

    static com.example.vongcat.View.TableAdapter.Item mItemTable=null;
    static List<com.example.vongcat.View.BeverageAdapter.Item> mItemBeverage=null;

    FloatingActionButton submitOderFab;
    static MenuItem statusMni;

    List<String> arrStringNameTable;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actvity_add_oder,menu);
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
        ListTable.getInstance().setOnListTableChange(new ListTable.OnListTableChange() {
            @Override
            public void callBack(List<Item> listItem) {
                arrStringNameTable.clear();
                arrStringNameTable.add(TABLE_SPINER_LABEL);
                for (Item item :
                        listItem) {
                    arrStringNameTable.add(item.getName());
                }
            }
        });
        ListTable.getInstance().updateData(
                ListTable.getInstance().getmJsonArray()
        );
    }

    private void addEvent() {
        tableSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String nameTable = adapterView.getItemAtPosition(i).toString();
                Item item = new Item(nameTable,0);
                setChoice(item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
                        if(!tableSpn.getSelectedItem().toString().equals(TABLE_SPINER_LABEL))
                            mItemTable = new Item(tableSpn.getSelectedItem().toString(),0);
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
        tableSpn= findViewById(R.id.tableSpn);
        arrStringNameTable = new ArrayList<>();
        arrStringNameTable.add(TABLE_SPINER_LABEL);
        ArrayAdapter<String> adapterTable = new ArrayAdapter<String>(this,
                R.layout.spiner_item,
                arrStringNameTable);
        tableSpn.setAdapter(adapterTable);


        beverageLsv = findViewById(R.id.beverageLsv);
//        TextView headerViewBeverageLsv = new TextView(this);
//        headerViewBeverageLsv.setTextSize(TypedValue.COMPLEX_UNIT_PX,
//                getResources().getDimensionPixelSize(R.dimen.textSizeDefault));
//        headerViewBeverageLsv.setTextColor(Color.BLACK);
//        headerViewBeverageLsv.setAllCaps(true);
//        headerViewBeverageLsv.setText("Chọn món");
//        beverageLsv.addHeaderView(headerViewBeverageLsv);

        List<com.example.vongcat.View.BeverageAdapter.Item> itemsBeverage = new ArrayList<>();
        beverageAdapter = new com.example.vongcat.View.BeverageAdapter.Adapter(this,R.layout.item_beverage,itemsBeverage);
        beverageLsv.setAdapter(beverageAdapter);
        beverageLsv.setVisibility(View.INVISIBLE);

        submitOderFab = findViewById(R.id.submitOderFab);

    }

    public static void setChoice(com.example.vongcat.View.TableAdapter.Item itemTable){
        mItemTable = itemTable;

        if(itemTable.getName().equals(TABLE_SPINER_LABEL)){
            beverageLsv.setVisibility(View.INVISIBLE);
            mItemBeverage.clear();
            statusMni.setTitle("chưa chọn bàn");
        }else{
            beverageLsv.setVisibility(View.VISIBLE);
            statusMni.setTitle("Chọn bàn " + itemTable.getName());
        }
        beverageAdapter.notifyDataSetChanged();
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
