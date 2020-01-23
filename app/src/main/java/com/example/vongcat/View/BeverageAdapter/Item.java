package com.example.vongcat.View.BeverageAdapter;

public class Item {
    String name;
    int value;

    public Item(String name, int total) {
        this.name = name;
        this.value = total;
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
