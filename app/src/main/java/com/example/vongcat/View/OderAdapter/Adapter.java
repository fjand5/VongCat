package com.example.vongcat.View.OderAdapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import com.example.vongcat.Presenter.ListOder;
import com.example.vongcat.R;
import com.example.vongcat.View.MainActivity;

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
        ListOder.getInstance().updateData(
                ListOder.getInstance().getmJsonObject()
        );

    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View v = convertView;
        if (v == null)
         v = LayoutInflater.from(getContext()).inflate(R.layout.item_oder,parent,false);

        TextView nameOderTxt=v.findViewById(R.id.nameOderTxt);
        TextView valueOderTxt=v.findViewById(R.id.valueOderTxt);
        TextView tableOderTxt = v.findViewById(R.id.tableOderTxt);
        final CheckBox isPaidChb= v.findViewById(R.id.isPaidChb);


        final List<Item> itemList = ListOder.getInstance().getListItem();
        nameOderTxt.setText( itemList.get(position).getName());
        valueOderTxt.setText(String.valueOf(itemList.get(position).getValue()));
        tableOderTxt.setText(itemList.get(position).getTable());
        if(itemList.get(position).isPaid() == true)
            v.setBackgroundColor(Color.RED);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = isPaidChb.isChecked();
                if(isChecked){ // add
                    isPaidChb.setChecked(false);
                    view.setBackgroundColor(Color.WHITE);
                    MainActivity.removeOder4Pay(itemList.get(position));
                }else{  // remove
                    isPaidChb.setChecked(true);
                    view.setBackgroundColor(Color.BLUE);
                    MainActivity.addOder4Pay(itemList.get(position));

                }




            }
        });
        return v;
    }

}
