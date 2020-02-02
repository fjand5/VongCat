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
import android.widget.Toast;

import com.example.vongcat.Model.ListBeverageFirebase;
import com.example.vongcat.Model.ListExpenseFirebase;
import com.example.vongcat.Model.ListMaterialFirebase;
import com.example.vongcat.Model.ListOderFirebase;
import com.example.vongcat.Model.ListSupInStoreFirebase;
import com.example.vongcat.Model.ListTableFirebase;
import com.example.vongcat.Presenter.ListExpense;
import com.example.vongcat.Presenter.ListOder;
import com.example.vongcat.Presenter.ListSupInStore;
import com.example.vongcat.R;
import com.example.vongcat.View.OderAdapter.Adapter;
import com.example.vongcat.View.OderAdapter.Item;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
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
    static Button payOderBtn;


    static List<Item> item4Pay;

    static int sumSold=0;
    static int sumReceived=0;

    static int sum=0;


    MenuItem statusMnI;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        statusMnI = menu.findItem(R.id.statusTxt);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        item4Pay = new ArrayList<>();

        initView();
        addEvent();




    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.storeManagerBtn:
                Intent intent = new Intent(this,StoreManagerActivity.class);
                startActivity(intent);
                return true;
            case R.id.logBtn:
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

                final AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                alert.setTitle("danh sách món");
                alert.setMessage(tmp);
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        for (Item item :
                                item4Pay) {
                            Task<Void> task = ListOder.getInstance().setIsPaidOder(item.getKey());
                            task.addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(alert.getContext(),"đã thanh toán xong",Toast.LENGTH_LONG).show();
                                }
                            });
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
                if(sumReceived == sumSold){
                    statusMnI.setTitle("Đã thanh toán hết " + sumSold+"k");
                }else{

                    statusMnI.setTitle("Còn "+ (sumReceived)+"k/"+sumSold+"k ");
                }

            }
        });

    }

    @Override
    protected void onResume() {
        ListOder.getInstance().refesh();
        super.onResume();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);

    }

    private void initView() {
        oderLsv = findViewById(R.id.oderLsv);
        addOderBtn = findViewById(R.id.addOderBtn);

        payOderBtn= findViewById(R.id.payOderBtn);

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
        if(sum>0)
        payOderBtn.setText("thanh toán: "+String.valueOf(sum) + "k");
        else
            payOderBtn.setText("thanh toán");
    }
    public static List<Item> getOder4Pay(){
        return  item4Pay;
    }
}
