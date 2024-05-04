package com.example.fishingwebshop.models;

import java.io.Serializable;

public class MyCartModel implements Serializable {
    String productImg_url;
    String currentDate;
    String currentTime;
    String productName;
    String productPrice;
    int totalPrice;
    String totalQuantity;

    public MyCartModel() {
    }

    public MyCartModel(String productImg_url,String currentDate, String currentTime, String productName, String productPrice, int totalPrice, String totalQuantity) {
        this.productImg_url = productImg_url;
        this.currentDate = currentDate;
        this.currentTime = currentTime;
        this.productName = productName;
        this.productPrice = productPrice;
        this.totalPrice = totalPrice;
        this.totalQuantity = totalQuantity;
    }

    public String getProductImg_url() {
        return productImg_url;
    }

    public void setProductImg_url(String productImg_url) {
        this.productImg_url = productImg_url;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(String totalQuantity) {
        this.totalQuantity = totalQuantity;
    }
}
