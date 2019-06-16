package com.sample.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.sample.Constants.Gender;
import com.sample.Constants.MaritalStatus;

public class Client implements Serializable {
    private String name;
    private Gender gender;
    private MaritalStatus maritalStatus;
    private boolean hasChildren;
    private List<Item> cart;
    private List<Item> recommendations;

    public Client(String name, Gender gender, MaritalStatus maritalStatus, boolean hasChildren) {
        this.name = name;
        this.gender = gender;
        this.maritalStatus = maritalStatus;
        this.hasChildren = hasChildren;
        this.cart = new ArrayList<>();
        this.recommendations = new ArrayList<>();
    }

    /**
     * @return the hasChildren
     */
    public boolean isHasChildren() {
        return hasChildren;
    }

    /**
     * @param hasChildren the hasChildren to set
     */
    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    /**
     * @return the maritalStatus
     */
    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    /**
     * @param maritalStatus the maritalStatus to set
     */
    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
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
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    /**
     * @return List<Item> return the cart
     */
    public List<Item> getCart() {
        return cart;
    }

    /**
     * @param cart the cart to set
     */
    public void setCart(List<Item> cart) {
        this.cart = cart;
    }

    public boolean addToCart(Item item) {
        return this.cart.add(item);
    }

    public boolean removeFromCart(Item item) {
        return this.cart.remove(item);
    }

    public List<Item> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<Item> recommendations) {
        this.recommendations = recommendations;
    }

    public void addRecommendation(Item item) {
        this.recommendations.add(item);
    }

    public double getPurchaseValue() {
        double total = 0.0;
        for (Item item : cart) {
            total += item.getPrice();
        }

        // Encut to 2 decimal places
        total = ((double) ((int) (total * 100.0))) / 100.0;

        return total;
    }

    public int getTotalCartItems() {
        return cart.size();
    }
}