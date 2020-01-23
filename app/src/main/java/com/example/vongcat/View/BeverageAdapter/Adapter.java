package com.example.vongcat.View.BeverageAdapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.vongcat.Presenter.ListBeverage;
import com.example.vongcat.R;

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
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v == null)
            v = LayoutInflater.from(getContext()).inflate(R.layout.item_beverage,parent,false);

        TextView nameBeverageTxt=v.findViewById(R.id.nameBeverageTxt);
        TextView valueBeverageTxt=v.findViewById(R.id.valueBeverageTxt);

        nameBeverageTxt.setText(getItem(position).getName());
        return v;
    }
}
