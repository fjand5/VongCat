package com.example.vongcat.View.OderAdapter;

public class Item {
    String table;
    String name;
    int value;
    boolean isPaid=false;
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

    public int getTotal() {
        return value;
    }

    public void setTotal(int total) {
        this.value = total;
    }

    public Item(String name,String table, int total, boolean isPaid) {
        this.name = name;
        this.value = total;
        this.isPaid = isPaid;
        this.table = table;
    }
}
