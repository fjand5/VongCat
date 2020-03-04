package com.example.vongcat.View.OderAdapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.vongcat.Presenter.ListOder;
import com.example.vongcat.R;
import com.example.vongcat.View.MainActivity;
import com.example.vongcat.View.MoreActivity;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Adapter extends ArrayAdapter<Item>  {

    OnMoreButtonClick onMoreButtonClick;


    List<Item> itemList;
    public void setOnMoreButtonClick(OnMoreButtonClick onMoreButtonClick) {
        this.onMoreButtonClick = onMoreButtonClick;
    }



    public Adapter(@NonNull Context context, int resource, @NonNull List<Item> objects) {
        super(context, resource, objects);
        itemList = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View v = convertView;
        if (v == null)
         v = LayoutInflater.from(getContext()).inflate(R.layout.item_oder,parent,false);

        TextView valueOderTxt=v.findViewById(R.id.valueOderTxt);
        TextView tableOderTxt = v.findViewById(R.id.tableOderTxt);
        CheckBox isPaidChb= v.findViewById(R.id.isPaidChb);
        Button moreBtn = v.findViewById(R.id.moreBtn);

        isPaidChb.setText(itemList.get(position).getName());
        valueOderTxt.setText(String.valueOf(itemList.get(position).getValue())+"k");
        tableOderTxt.setText(itemList.get(position).getTable());


        isPaidChb.setChecked(false);
        v.setBackgroundColor(Color.WHITE);
        for (Item item:
             MainActivity.getOder4Pay()) {
            if(item.getKey().equals(itemList.get(position).getKey())){
                isPaidChb.setChecked(true);
                v.setBackgroundColor(Color.GRAY);
            }
        }




        if(itemList.get(position).isPaid() == true)
            v.setBackgroundColor(Color.GREEN);
        tableOderTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (Item item :
                        itemList) {
                    if(item.getTable().equals(itemList.get(position).getTable())){
                        if (MainActivity.getOder4Pay().contains(item)){
                            MainActivity.clearOder4Pay();
                            break;
                        }
                        MainActivity.addOder4Pay(item);
                    }
                }
                notifyDataSetChanged();
            }
        });
        isPaidChb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){ // add
                    MainActivity.addOder4Pay(itemList.get(position));
                }else{  // remove
                    MainActivity.removeOder4Pay(itemList.get(position));
                }
                notifyDataSetChanged();
            }
        });
        moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onMoreButtonClick != null)
                    onMoreButtonClick.callBack(view, itemList.get(position));


            }
        });

        return v;
    }

    public interface OnMoreButtonClick{
        void callBack(View v, Item item);
    }

}
