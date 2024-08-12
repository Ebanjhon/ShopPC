package com.example.mobileshopapp;

public class Category {
    private String id, name;

    // constructor
    public Category(String id, String name) {
        this.id = id;
        this.name = name;
    }

    // getter and setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
