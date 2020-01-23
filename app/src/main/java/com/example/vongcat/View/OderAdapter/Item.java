package com.example.vongcat.View.OderAdapter;

public class Item {
    String key;
    String table;
    String name;
    int value;
    boolean isPaid=false;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Item(String name,String table, int value, boolean isPaid) {
        this.name = name;
        this.value = value;
        this.isPaid = isPaid;
        this.table = table;

    }
}
