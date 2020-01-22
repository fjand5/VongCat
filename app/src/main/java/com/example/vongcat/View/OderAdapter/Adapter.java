package com.example.vongcat.View.OderAdapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.vongcat.Presenter.ListOder;
import com.example.vongcat.R;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends ArrayAdapter<Item>  {

    public Adapter(@NonNull Context context, int resource, @NonNull List<Item> objects) {
        super(context, resource, objects);

            ListOder.getInstance().setListItem(objects).setOnListOderChange(new ListOder.OnListOderChange() {
                @Override
                public void callBack(List<Item> itemList) {
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
         v = LayoutInflater.from(getContext()).inflate(R.layout.item_oder,parent,false);

        TextView nameTableTxt=v.findViewById(R.id.nameOderTxt);
        TextView toltalTableTxt=v.findViewById(R.id.valueOderTxt);


        List<Item> itemList = ListOder.getInstance().getListItem();
        nameTableTxt.setText( itemList.get(position).getName());
        toltalTableTxt.setText(String.valueOf(itemList.get(position).getValue()));
        return v;
    }
}
