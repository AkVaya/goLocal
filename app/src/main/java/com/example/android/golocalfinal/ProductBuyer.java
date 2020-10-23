package com.example.android.golocalfinal;

public class ProductBuyer {

    String Name;
    String Price,Desc;
    String ImageURL;

    public ProductBuyer(String name, String price, String desc, String image) {
        Name = name;
        Price = price;
        Desc = desc;
        ImageURL = image;
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

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public String getImageURL() {
        return ImageURL;
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
