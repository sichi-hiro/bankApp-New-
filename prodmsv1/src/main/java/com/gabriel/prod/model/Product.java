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

    int balance;

    @Override
    public String toString(){
        return name;
    }
}