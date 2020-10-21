package com.example.android.golocalfinal;

public class ProductBuyer {

    String Name;
    String Price,Desc;

    public ProductBuyer(String name, String price, String desc) {
        Name = name;
        Price = price;
        Desc = desc;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public String getName() {
        return Name;
    }

    public String getPrice() {
        return Price;
    }

    public String getDesc() {
        return Desc;
    }
}
