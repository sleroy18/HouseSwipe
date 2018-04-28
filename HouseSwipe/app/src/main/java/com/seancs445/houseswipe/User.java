package com.seancs445.houseswipe;

import java.io.Serializable;

public class User implements Serializable{

    private String UID;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private double priceMin;
    private double priceMax;
    private int distance;
    private int zipCode;
    private boolean isBuying;
    private boolean isRenting;
    private boolean hasPet;
    private int minBedNumber;
    private int minBathNumber;

    public User() {

    }

    public User(String UID) {
        this.UID = UID;
    }

    public User(String firstName, String lastName, String email, String password){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public double getPriceMin() {
        return priceMin;
    }

    public void setPriceMin(double priceMin) {
        this.priceMin = priceMin;
    }

    public double getPriceMax() {
        return priceMax;
    }

    public void setPriceMax(double priceMax) {
        this.priceMax = priceMax;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public boolean isBuying() {
        return isBuying;
    }

    public void setBuying(boolean buying) {
        isBuying = buying;
    }

    public boolean isRenting() {
        return isRenting;
    }

    public void setRenting(boolean renting) {
        isRenting = renting;
    }

    public boolean isHasPet() {
        return hasPet;
    }

    public void setHasPet(boolean hasPet) {
        this.hasPet = hasPet;
    }

    public int getMinBedNumber() {
        return minBedNumber;
    }

    public void setMinBedNumber(int minBedNumber) {
        this.minBedNumber = minBedNumber;
    }

    public int getMinBathNumber() {
        return minBathNumber;
    }

    public void setMinBathNumber(int minBathNumber) {
        this.minBathNumber = minBathNumber;
    }
}