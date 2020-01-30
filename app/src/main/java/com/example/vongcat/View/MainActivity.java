package com.example.vongcat.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.vongcat.Model.ListSupInStoreFirebase;
import com.example.vongcat.Presenter.ListExpense;
import com.example.vongcat.Presenter.ListOder;
import com.example.vongcat.Presenter.ListSupInStore;
import com.example.vongcat.R;
import com.example.vongcat.View.OderAdapter.Adapter;
import com.example.vongcat.View.OderAdapter.Item;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ListView oderLsv;
    Adapter adapterOder;
    List<Item> itemOder;


    FloatingActionButton addOderBtn;
    Button payOderBtn;
    private static TextView sumOderTxt;


    static List<Item> item4Pay;
    private TextView soldTxt;
    private TextView receivedTxt;
    static int sumSold=0;
    static int sumReceived=0;

    static int sum=0;
    private Button logBtn;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        item4Pay = new ArrayList<>();
        initView();
        addEvent();

        ListSupInStore.getInstance().setOnListSupInStoreChange(new ListSupInStore.OnListSupInStoreChange() {
            @Override
            public void callBack(List<com.example.vongcat.View.StoreManagerAdapter.Item> listSup) {

            }
        });



    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.storeManagerBtn:
                Intent intent = new Intent(this,StoreManagerActivity.class);
                startActivity(intent);
                return true;
            case R.id.logBtn:
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("name","cafe");
                    jsonObject.put("quan",111);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ListSupInStoreFirebase.getInstance().importSup(jsonObject);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Intent i = new Intent(view.getContext(),LogActivity.class);
                        i.putExtra("year",year);
                        i.putExtra("month",month+1);
                        i.putExtra("day",dayOfMonth);
                        startActivity(i);
                    }
                }, Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DATE));
                datePickerDialog.show();
                return true;
            case R.id.expsBtn:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Nhập số tiền xuất");

                View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_input_exps, null, false);
                final EditText moneyTxt = viewInflated.findViewById(R.id.moneyTxt);
                final TextView textTxt = viewInflated.findViewById(R.id.textTxt);
                builder.setView(viewInflated);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        int moneyExps = Integer.parseInt(moneyTxt.getText().toString());
                        ListExpense.getInstance().addExps(moneyExps,textTxt.getText().toString());
                    }
                });
                builder.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
                String tmp = "Chưa chọn món";
                Map<String,Integer> data = new HashMap<>();
                int _sum=0;
                for (Item item :
                        item4Pay) {
                    _sum+=item.getValue();
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
                tmp+="\n"+
                        "--------------"+"\n"+
                        _sum +" k";

                AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                alert.setTitle("danh sách món");
                alert.setMessage(tmp);
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (Item item :
                                item4Pay) {
                            ListOder.getInstance().setIsPaidOder(item.getKey());
                        }
                        item4Pay.clear();
                    }
                });
                alert.setNegativeButton("Hủy",null);
                alert.show();

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

    }

    @Override
    protected void onResume() {
        soldTxt.setText("Đã bán: " + String.valueOf(sumSold)+"k");
        receivedTxt.setText("Đã thu: " + String.valueOf(sumReceived)+"k");
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
