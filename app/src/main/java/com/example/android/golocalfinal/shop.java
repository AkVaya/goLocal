package com.example.android.golocalfinal;

public class shop {
    String name, locality,email;

    public shop(){}

    public shop(String name, String locality,String email) {
        this.name = name;
        this.locality = locality;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getLocality() {
        return locality;
    }
}
