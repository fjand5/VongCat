package com.example.vongcat.View.TableAdapter;

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

import com.example.vongcat.Presenter.ListTable;
import com.example.vongcat.R;
import com.example.vongcat.View.AddOderActivity;

import java.util.List;
import java.util.Set;

public class Adapter extends ArrayAdapter<Item> {
Item selectedItem = null;
int lastItemId=-1;
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
        ListTable.getInstance().updateData(
                ListTable.getInstance().getmJsonArray()
        );

    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        View v = convertView;
        final Item item = getItem(position);
        if (v == null)
            v = LayoutInflater.from(getContext()).inflate(R.layout.item_table,parent,false);

        TextView nameTableTxt=v.findViewById(R.id.nameTableTxt);

        nameTableTxt.setText(ListTable.getInstance().getListItem().get(position).getName());
        if(item.isChoose())
            v.setBackgroundColor(Color.BLUE);
        else
            v.setBackgroundColor(Color.WHITE);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(lastItemId>=0)
                    getItem(lastItemId).setChoose(false);
                selectedItem =item;
                lastItemId = position;
                item.setChoose(true);
                view.setBackgroundColor(Color.BLUE);
                notifyDataSetChanged();

            }
        });
        return v;
    }

    public Item getSelectedItem() {
        return selectedItem;
    }
}
