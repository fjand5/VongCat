package com.example.vongcat.View.StoreManagerAdapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.vongcat.Presenter.ListBeverage;
import com.example.vongcat.Presenter.ListSupInStore;
import com.example.vongcat.R;
import com.example.vongcat.View.AddOderActivity;

import java.util.List;

public class Adapter extends ArrayAdapter<Item> {
    public Adapter(@NonNull Context context, int resource, @NonNull List<Item> objects) {
        super(context, resource, objects);

        ListSupInStore.getInstance().setListItem(objects).setOnListSupInStoreChange(new ListSupInStore.OnListSupInStoreChange() {
            @Override
            public void callBack(List<Item> listBeverage) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
            }
        });
        ListSupInStore.getInstance().updateData(
                ListSupInStore.getInstance().getmJsonObject()
        );


    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v == null)
            v = LayoutInflater.from(getContext()).inflate(R.layout.item_supply,parent,false);

        TextView curQuanTxt = v.findViewById(R.id.curQuanTxt);
        TextView nameSupTxt = v.findViewById(R.id.nameSupTxt);
        final EditText importQuanTxt = v.findViewById(R.id.importQuanTxt);
        Button importBtn =  v.findViewById(R.id.importBtn);
        importQuanTxt.setText("0");
        nameSupTxt.setText(getItem(position).getName() );
        curQuanTxt.setText(Double.toString(getItem(position).getQuan()));

        importBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListSupInStore.getInstance().importSub(getItem(position).getName(),
                        Integer.valueOf(importQuanTxt.getText().toString()));
            }
        });
        return v;
    }
}
