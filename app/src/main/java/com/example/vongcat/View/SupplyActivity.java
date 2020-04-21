package com.example.vongcat.View;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapLabel;
import com.beardedhen.androidbootstrap.BootstrapText;
import com.beardedhen.androidbootstrap.api.attributes.BootstrapHeading;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.example.vongcat.Presenter.ListBeverage;
import com.example.vongcat.Presenter.Supply;
import com.example.vongcat.View.BeverageAdapter.Item;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.InputType;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.vongcat.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SupplyActivity extends AppCompatActivity {
    Context mContext;
    LinearLayout layout;




    @Override
    protected void onResume() {
        super.onResume();


        Supply.getInstance().trigUpdate();
        ListBeverage.getInstance().setOnListBeverageChange(new ListBeverage.OnListBeverageChange() {
            @Override
            public void callBack(List<Item> listBeverage) {
                for (Item item :
                        listBeverage) {

                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(item.getListSupply().toString());
                        for(int i=0;i<jsonObject.length();i++){
                            try {
                                String name = jsonObject.names().getString(i);

                                Supply.getInstance().exportSupply(name,0);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }
        });
        ListBeverage.getInstance().trigUpdate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supply);
        layout = (LinearLayout) findViewById(R.id.SupplyLayout);
        mContext = this;

        addEvent();
    }

    private void addEvent() {
        Supply.getInstance().setOnSupplyChange(new Supply.OnSupplyChange() {
            @Override
            public void callBack(JSONObject data) {
                Log.d("jsonArray232222", data.toString());

                layout.removeAllViews();
                layout.setVerticalScrollBarEnabled(true);
                for(int i=0;i<data.length();i++){
                    try {
                        String name = data.names().getString(i);
                        double value =data.getDouble(name);
                        LinearLayout subLayout = new LinearLayout(mContext);
                        subLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.item_supply,layout,false);
                        BootstrapLabel nameSup = subLayout.findViewById(R.id.supplyNamelbl);
                        nameSup.setText(name);

                        BootstrapLabel valueSup = subLayout.findViewById(R.id.supplyValuelbl);
                        valueSup.setText(""+value);

                        valueSup.setOnClickListener(showDialogNumber);

                        valueSup.setTag(name);

                        layout.addView(subLayout);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        });
    }
    View.OnClickListener showDialogNumber = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            final EditText editText = new EditText(view.getContext());
            editText.setInputType(InputType.TYPE_CLASS_NUMBER |  InputType.TYPE_NUMBER_FLAG_DECIMAL);
            LinearLayout layout = new LinearLayout(view.getContext());
            editText.setWidth(200);
            layout.setGravity(Gravity.CENTER);
            layout.addView(editText);

                new AlertDialog.Builder(view.getContext())
                        .setTitle("Nhập lượng " + view.getTag().toString().toUpperCase() + ": ")
                        .setView(layout)
                        .setNeutralButton("Thoát",null)
                        .setNegativeButton("Nhập", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(!editText.getText().toString().equals(""))
                                Supply.getInstance().importSupply((String) view.getTag(),Double.parseDouble(editText.getText().toString()));
                            }
                        })
                        .setPositiveButton("Xuất",   new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(!editText.getText().toString().equals(""))
                                Supply.getInstance().exportSupply((String) view.getTag(),Double.parseDouble(editText.getText().toString()));
                            }
                        })
                        .create()
                        .show();



        }
    };

}
