package com.example.android.golocalfinal;

public class Product {
    String Quantity;
    String Name;
    String Price,Desc;

    public Product(){}

    public Product(String quantity, String name,String price,String desc) {
        Quantity = quantity;
        Name = name;
        Price = price;
        Desc = desc;
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
