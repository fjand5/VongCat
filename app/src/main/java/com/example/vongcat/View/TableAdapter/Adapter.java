package com.example.vongcat.View.TableAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;

import com.example.vongcat.Presenter.ListTable;
import com.example.vongcat.R;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends ArrayAdapter<Item>  {


    public Adapter(@NonNull Context context, int resource, @NonNull List<Item> objects) {
        super(context, resource, objects);

            ListTable.getInstance(objects).setOnListTableChange(new ListTable.OnListTableChange() {
                @Override
                public void callBack() {

                    Log.d("htl","callBack");
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
         v = LayoutInflater.from(getContext()).inflate(R.layout.item_table,parent,false);

        TextView nameTableTxt=v.findViewById(R.id.nameTableTxt);
        TextView toltalTableTxt=v.findViewById(R.id.toltalTableTxt);


        nameTableTxt.setText(ListTable.getInstance().getListItem().get(position).name);
        toltalTableTxt.setText(String.valueOf(ListTable.getInstance().getListItem().get(position).total));
        return v;
    }
}
