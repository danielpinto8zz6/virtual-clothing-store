package com.sample.model;

import com.sample.Constants.Gender;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class Item implements Serializable, Comparable<Item> {
    private String id;
    private String name;
    private Gender gender;
    private double price;
    private Item complementaryItem;
    private boolean isRecommendation = false;

    public Item() {
        this.id = UUID.randomUUID().toString();
    }

    public Item(String name, Gender gender, double price) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.gender = gender;
        this.price = price;
    }

    public Item(String name, Gender gender, double price, Item complementary) {
        this(name, gender, price);
        this.complementaryItem = complementary;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return the gender
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * @param gender the gender to set
     */
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    public Item getComplementaryItem() {
        return complementaryItem;
    }

    public void setComplementaryItem(Item complementaryItem) {
        this.complementaryItem = complementaryItem;
    }

    @Override
    public String toString() {
        return name + " (" + gender + ") - " + price + "$";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Item))
            return false;
        Item item = (Item) o;
        return name.equals(item.name) && gender == item.gender;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, gender);
    }

    public boolean isRecommendation() {
        return isRecommendation;
    }

    public void setRecommendation(boolean recommendation) {
        isRecommendation = recommendation;
    }

    @Override
    public int compareTo(Item item) {
        int comp = this.getName().compareTo(item.getName());

        if (comp != 0) {
            return comp;
        }

        return this.getGender().compareTo(item.getGender());
    }

    public String getId() {
        return id;
    }
}