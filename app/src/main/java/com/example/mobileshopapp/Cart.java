package com.example.mobileshopapp;

public class Cart {
    private String idCart, name,image;
    private int quantity;
    private double price;

    public Cart(String idCart, String name, String image, int quantity, double price) {
        this.idCart = idCart;
        this.name = name;
        this.image = image;
        this.quantity = quantity;
        this.price = price;
    }

    public String getIdCart() {
        return idCart;
    }

    public void setIdCart(String idCart) {
        this.idCart = idCart;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
