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

        TextView nameBeverageTxt=v.findViewById(R.id.nameBeverageTxt);
        final TextView valueBeverageTxt=v.findViewById(R.id.valueBeverageTxt);
        CheckBox isSelectBeverageChb=v.findViewById(R.id.isSelectBeverageChb);

        nameBeverageTxt.setText(getItem(position).getName());
        valueBeverageTxt.setText(String.valueOf(getItem(position).getValue()));

        isSelectBeverageChb.setChecked(false);
        for (Item item:
                AddOderActivity.getBeverage()) {
            if(item.getName().equals(getItem(position).getName()))
                isSelectBeverageChb.setChecked(true);
        }

        isSelectBeverageChb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((CheckBox)view).isChecked()){ // add
                    AddOderActivity.addBeverage(getItem(position));

                }else{  // remove
                    AddOderActivity.removeBeverage(getItem(position));


                }

            }
        });
        return v;
    }
}
