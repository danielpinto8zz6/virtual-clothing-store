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
    private boolean haveChildren;
    private List<Item> cart;
    private List<Item> recommendations;

    public Client(String name, Gender gender, MaritalStatus maritalStatus, boolean haveChildren) {
        this.name = name;
        this.gender = gender;
        this.maritalStatus = maritalStatus;
        this.haveChildren = haveChildren;
        this.cart = new ArrayList<>();
        this.recommendations = new ArrayList<>();
    }

    /**
     * @return the haveChildren
     */
    public boolean isHaveChildren() {
        return haveChildren;
    }

    /**
     * @param haveChildren the haveChildren to set
     */
    public void setHaveChildren(boolean haveChildren) {
        this.haveChildren = haveChildren;
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
}