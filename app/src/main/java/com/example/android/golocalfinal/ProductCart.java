package com.example.android.golocalfinal;

public class ProductCart {
    String itemCategoryIndex,itemIndex,itemName,itemPrice,itemQuantity;
    Boolean check = false;

    public String getItemCategoryIndex() {
        return itemCategoryIndex;
    }

    public String getItemIndex() {
        return itemIndex;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public String getItemQuantity() {
        return itemQuantity;
    }

    public ProductCart(String itemCategoryIndex, String itemIndex, String itemName, String itemPrice, String itemQuantity,Boolean check) {
        this.itemCategoryIndex = itemCategoryIndex;
        this.itemIndex = itemIndex;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemQuantity = itemQuantity;
        this.check = check;
    }

    public void setItemQuantity(String itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public ProductCart() {
    }

    public Boolean getCheck() {
        return check;
    }
}
