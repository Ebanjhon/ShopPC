package com.example.mobileshopapp;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Order {
    private String orderID;
    private String userID;
    private String orderDate;
    private boolean state;
    private double total;

    public Order(String orderID, String userID, String orderDate, boolean state, double total) {
        this.orderID = orderID;
        this.userID = userID;
        this.orderDate = orderDate;
        this.state = state;
        this.total = total;
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

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public boolean getState() {
        return state;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Date getOrderDateAsDate() {
        // Cập nhật định dạng ngày tháng để phù hợp với định dạng lưu trên Firebase
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            return dateFormat.parse(orderDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getFormattedTotal() {
        // Sử dụng NumberFormat cho định dạng tiền tệ
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return currencyFormat.format(total);
    }
}
