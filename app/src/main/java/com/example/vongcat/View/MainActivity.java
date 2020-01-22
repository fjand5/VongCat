package com.example.vongcat.View;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.vongcat.Model.ListOderFirebase;
import com.example.vongcat.R;
import com.example.vongcat.View.OderAdapter.Adapter;
import com.example.vongcat.View.OderAdapter.Item;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView oderLsv;
    Adapter adapterTable;
    List<Item> itemOder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initView();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name","Cafe đen");
            jsonObject.put("table","Bàn 1");
            jsonObject.put("value",9000);
            jsonObject.put("isPaid",false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ListOderFirebase.getInstance().addOder(jsonObject);

        jsonObject = new JSONObject();
        try {
            jsonObject.put("name","Cafe sữa");
            jsonObject.put("table","Bàn 2");
            jsonObject.put("value",10000);
            jsonObject.put("isPaid",false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ListOderFirebase.getInstance().addOder(jsonObject);



    }

    private void initView() {
        oderLsv = findViewById(R.id.oderLsv);

        itemOder = new ArrayList<>();
        adapterTable = new Adapter(this,R.layout.item_oder,itemOder);
        oderLsv.setAdapter(adapterTable);
    }
}
