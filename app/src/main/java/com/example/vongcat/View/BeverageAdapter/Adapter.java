package com.example.vongcat.View.BeverageAdapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.vongcat.Presenter.ListBeverage;
import com.example.vongcat.Presenter.ListTable;
import com.example.vongcat.R;
import com.example.vongcat.View.AddOderActivity;

import java.util.List;

public class Adapter extends ArrayAdapter<Item> {
    public Adapter(@NonNull Context context, int resource, @NonNull List<Item> objects) {
        super(context, resource, objects);

        ListBeverage.getInstance().setListItem(objects).setOnListBeverageChange(new ListBeverage.OnListBeverageChange() {
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
        ListBeverage.getInstance().updateData(
                ListBeverage.getInstance().getmJsonArray()
        );
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v == null)
            v = LayoutInflater.from(getContext()).inflate(R.layout.item_beverage,parent,false);

        TextView nameBeverageTxt = v.findViewById(R.id.nameBeverageTxt);
        TextView valueBeverageTxt = v.findViewById(R.id.valueBeverageTxt);
        Button incBeverageBtn = v.findViewById(R.id.incBeverageBtn);
        Button decBeverageBtn = v.findViewById(R.id.decBeverageBtn);
        TextView quanBeverageTxt = v.findViewById(R.id.quanBeverageTxt);


        nameBeverageTxt.setText(getItem(position).getName());
        nameBeverageTxt.setBackgroundColor(Color.WHITE);

            nameBeverageTxt.setTextColor(Color.parseColor(getItem(position).getColor()));

        valueBeverageTxt.setText(String.valueOf(getItem(position).getValue()) +"k");

        incBeverageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddOderActivity.addBeverage(getItem(position));
                notifyDataSetChanged();
            }
        });
        decBeverageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddOderActivity.removeBeverage(getItem(position));
                notifyDataSetChanged();
            }
        });

        int sum = 0;
        for (Item item:
                AddOderActivity.getBeverage()) {
            if(item.getName().equals(getItem(position).getName())){
                sum++;
            }
        }
        quanBeverageTxt.setText(" x" + sum);
        if(sum > 0){
            quanBeverageTxt.setTextColor(Color.RED);
        }else {
            quanBeverageTxt.setTextColor(Color.BLACK);
        }

        return v;
    }
}
