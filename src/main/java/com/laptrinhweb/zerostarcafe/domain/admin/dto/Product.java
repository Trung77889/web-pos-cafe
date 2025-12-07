package com.laptrinhweb.zerostarcafe.domain.admin.dto;

public class Product {
    private int id;
    private String name;
    private String picUrl;
    private int price;
    private String description;
    private double inventory;
    private boolean is_active;
    private String unit;

    public Product() {
    }

    public Product(int id, String unit, double inventory, String description, int price, String picUrl, String name) {
        this.id = id;
        this.unit = unit;
        this.inventory = inventory;
        this.description = description;
        this.price = price;
        this.picUrl = picUrl;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getInventory() {
        return inventory;
    }

    public void setInventory(double inventory) {
        this.inventory = inventory;
    }

    public boolean isIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

}
