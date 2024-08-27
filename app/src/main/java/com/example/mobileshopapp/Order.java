package com.example.mobileshopapp;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private String orderID;
    private String userID;
    private String productID;
    private String orderDate;
    private String state;

    public Order(String orderID, String userID, String productID, String orderDate, String state) {
        this.orderID = orderID;
        this.userID = userID;
        this.productID = productID;
        this.orderDate = orderDate;
        this.state = state;
    }

    public Order() {
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<Order> orders(){
        List<Order> orders = new ArrayList<>();
        orders.add(new Order("1", "wRBRRjJvucTlvSfsm5QKdGavFPy1", "MVfWCQCZUmMzFqpizVzm", "1727315035000", "Đã giao"));
        orders.add(new Order("2", "wRBRRjJvucTlvSfsm5QKdGavFPy1", "lKl2HTv6k0JBlkRUYvj1", "1724636635000", "Đã giao"));
        orders.add(new Order("3", "wRBRRjJvucTlvSfsm5QKdGavFPy1", "lKl2HTv6k0JBlkRUYvj1", "1727401435000", "Đã giao"));
        orders.add(new Order("4", "wRBRRjJvucTlvSfsm5QKdGavFPy1", "MVfWCQCZUmMzFqpizVzm", "1726883035000", "Đã giao"));
        orders.add(new Order("5", "wRBRRjJvucTlvSfsm5QKdGavFPy1", "U8nMs2XFR66BCDGNXJD4", "1724204635000", "Đã giao"));
        return orders;
    }
}
