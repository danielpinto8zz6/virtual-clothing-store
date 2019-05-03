package com.sample.model;

import com.sample.Constants.Gender;
import com.sample.Constants.MaritalStatus;

public class Client {
    private String name;
    private Gender gender;
    private MaritalStatus maritalStatus;
    private boolean haveChildren;

    public Client(String name, Gender gender, MaritalStatus maritalStatus, boolean haveChildren) {
        this.name = name;
        this.gender = gender;
        this.maritalStatus = maritalStatus;
        this.haveChildren = haveChildren;
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
}