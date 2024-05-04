package com.example.fishingwebshop.models;

import java.io.Serializable;

public class ShowAllModel implements Serializable {
    String description;
    String name;
    int price;
    String type;
    String img_url;

    public ShowAllModel()  {
    }

    public ShowAllModel(String description, String name, int price, String type) {
        this.description = description;
        this.name = name;
        this.price = price;
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}
