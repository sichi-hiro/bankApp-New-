package com.gabriel.prod.model;

import lombok.Data;

@Data
public class Product {
    int id;
    String name;
    String description;
    int uomId;
    String uomName;
    String photo;

    // ADDED FIELD: Tracks the live customer account balance natively in the database
    int balance;

    @Override
    public String toString(){
        return name;
    }
}