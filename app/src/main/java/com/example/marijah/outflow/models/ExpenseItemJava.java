package com.example.marijah.outflow.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ExpenseItemJava {

    private String key;
    private int price;
    private String place;
    private String category;
    private String date;
    private String comment;
    private String whoDidThePurchase;


    public ExpenseItemJava(String key, int price, String place, String category, String date, String comment, String whoDidThePurchase) {
        this.key = key;
        this.price = price;
        this.place = place;
        this.category = category;
        this.date = date;
        this.comment = comment;
        this.whoDidThePurchase = whoDidThePurchase;
    }

    public String getKey() {
        return key;
    }

    public int getPrice() {
        return price;
    }

    public String getPlace() {
        return place;
    }

    public String getCategory() {
        return category;
    }

    public String getDate() {
        return date;
    }

    public String getComment() {
        return comment;
    }

    public String getWhoDidThePurchase() {
        return whoDidThePurchase;
    }


    @NonNull
    @Override
    public String toString() {
        return "key: " + key + " place:" +place+ " category:" +category
                + "date:" + date + "comment:" +comment + "whoDidThePurchase:" + whoDidThePurchase;
    }


    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);

        // kod za poredjenje objekata
    }
}
