package com.example.vongcat.View.TableAdapter;

public class Item {
    String name;
    int total;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Item(String name, int total) {
        this.name = name;
        this.total = total;
    }
}
