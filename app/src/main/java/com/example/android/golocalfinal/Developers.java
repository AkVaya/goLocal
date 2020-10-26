package com.example.android.golocalfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class Developers extends AppCompatActivity {

    ImageView vasu,vaya;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.developers);

        vasu = (ImageView) findViewById(R.id.imageView13);
        vaya = (ImageView) findViewById(R.id.imageView12);
        Glide.with(vasu.getContext())
                .load("https://firebasestorage.googleapis.com/v0/b/golocalfinal.appspot.com/o/products%2FPicsArt_10-18-02.21.57-01-01.jpeg?alt=media&token=c47b3b88-5e59-444f-b9e6-2b3a8e45ac48")
                .into(vasu);

        Glide.with(vaya.getContext())
                .load("https://firebasestorage.googleapis.com/v0/b/golocalfinal.appspot.com/o/products%2FIMG-20200222-WA0033-01.jpeg?alt=media&token=0957602d-d24f-4972-84df-10e556c22b5c")
                .into(vaya);


    }


}