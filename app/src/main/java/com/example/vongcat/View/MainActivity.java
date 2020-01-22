package com.example.vongcat.View;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.vongcat.R;
import com.example.vongcat.View.TableAdapter.Adapter;
import com.example.vongcat.View.TableAdapter.Item;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView tableLsv;
    Adapter adapterTable;
    List<Item> itemTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        tableLsv = findViewById(R.id.tableLsv);

        itemTable = new ArrayList<>();
        adapterTable = new Adapter(this,R.layout.item_table,itemTable);
        tableLsv.setAdapter(adapterTable);
    }
}
