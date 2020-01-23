package com.example.vongcat.View.TableAdapter;

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

import com.example.vongcat.Presenter.ListTable;
import com.example.vongcat.R;

import java.util.List;
import java.util.Set;

public class Adapter extends ArrayAdapter<Item> {

    public Adapter(@NonNull Context context, int resource, @NonNull List<Item> objects) {
        super(context, resource, objects);
        ListTable.getInstance().setListItem(objects).setOnListTableChange(new ListTable.OnListTableChange() {
            @Override
            public void callBack(List<Item> listItem) {
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
        TextView toltalTableTxt=v.findViewById(R.id.totalTableTxt);

        nameTableTxt.setText(ListTable.getInstance().getListItem().get(position).getName());
        toltalTableTxt.setText(String.valueOf(ListTable.getInstance().getListItem().get(position).getTotal()));
        return v;
    }
}
