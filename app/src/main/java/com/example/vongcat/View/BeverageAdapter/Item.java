package com.example.vongcat.View.BeverageAdapter;

public class Item {
    String name;
    String color;
    int value;

    public Item(String name, int total, String color) {
        this.name = name;
        this.value = total;
        if(color == null
        || color == "" )
            color = "#000000";
        this.color = color;
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
