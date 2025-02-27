package com.example.android_api_demo.models;

import java.util.Date;

public class Food {
    private int id;
    private String image;
    private String name;
    private String description;
    private double price;
    private Date createdAt;

    public Food(String image, String name, String description, double price) {
        this.image = image;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

}
