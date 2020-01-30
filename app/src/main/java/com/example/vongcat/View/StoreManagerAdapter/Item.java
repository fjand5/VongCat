package com.example.vongcat.View.StoreManagerAdapter;

public class Item {
    String name;
    double quan;


    public Item(String name, double quan) {
        this.name = name;
        this.quan = quan;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQuan() {
        return quan;
    }

    public void setQuan(double quan) {
        this.quan = quan;
    }
}
