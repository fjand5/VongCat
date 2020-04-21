package com.example.vongcat.View.BeverageAdapter;

import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

public class Item {
    String name;
    String color;
    JSONObject listSupply;


    public JSONObject getListSupply() {
        return listSupply;
    }

    public void setListSupply(JSONObject listSupply) {
        this.listSupply = listSupply;
    }



    int value;

    public Item(String name, int total, String color, JSONObject listSupply) {
        this.name = name;
        this.value = total;
        if(color == null
        || color == "" )
            color = "#000000";
        this.color = color;
        this.listSupply = listSupply;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setTotal(int total) {
        this.value = total;
    }
}
