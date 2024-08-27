package com.example.mobileshopapp;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.FirebaseFirestore;

public class Product implements Parcelable {
    private String id, name, image, company, category, detail;
    private int price;

    public Product(String id, String name, String image, String company, String category, String detail, int price) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.company = company;
        this.category = category;
        this.detail = detail;
        this.price = price;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    // Parcelable implementation
    protected Product(Parcel in) {
        id = in.readString();
        name = in.readString();
        image = in.readString();
        company = in.readString();
        category = in.readString();
        detail = in.readString();
        price = in.readInt();
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int flags) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(image);
        parcel.writeString(company);
        parcel.writeString(category);
        parcel.writeString(detail);
        parcel.writeInt(price);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
