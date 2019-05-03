package com.sample.model;

import com.sample.Constants.Gender;

public class Item {
    private String name;
    private Gender gender;
    private double price;

    public Item(String name, Gender gender, double price) {
        this.name = name;
        this.gender = gender;
        this.price = price;
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

    @Override
    public String toString() {
        return "Item: " + name + " Gender: " + gender + " Price: " + price;
    }
}