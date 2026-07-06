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

    // THE MISSING LINK: Must be in the frontend's model package folder too!
    int balance;

    @Override
    public String toString(){
        return name;
    }
}