package com.example.android.golocalfinal;

import android.net.Uri;

public class Product {
    String Quantity;
    String Name;
    String Price,Desc;
    String imageURL;

    public Product(){}

    public Product(String quantity, String name,String price,String desc,String imageURL) {
        Quantity = quantity;
        Name = name;
        Price = price;
        Desc = desc;
        this.imageURL = imageURL;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getQuantity() {
        return Quantity;
    }

    public String getName() {
        return Name;
    }

    public String getPrice() { return Price; }

    public String getDesc() { return Desc; }
}
