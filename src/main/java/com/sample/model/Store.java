package com.sample.model;

import java.util.ArrayList;
import java.util.List;

public class Store {
    List<Item> items;

    public Store() {
        items = new ArrayList<>();
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public List<Item> getItems() {
        return this.items;
    }
    
    public void addItem(Item item) {
        this.items.add(item);
    }
}