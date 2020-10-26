package com.example.android.golocalfinal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;


public class SplashScreen extends Activity {
    Animation top,fade,bottom;
    ImageView image,bg;
    TextView logo,slogan;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen);
        image=findViewById(R.id.imageView6);
        bg=findViewById(R.id.imageView3);
        logo=findViewById(R.id.textView4);
        slogan = findViewById(R.id.textView36);
        top = AnimationUtils.loadAnimation(this,R.anim.top_anim);
        fade = AnimationUtils.loadAnimation(this,R.anim.fade_in);
        bottom = AnimationUtils.loadAnimation(this,R.anim.bottom_anim);
        image.setAnimation(top);
        logo.setAnimation(top);
        slogan.setAnimation(bottom);
        bg.setAnimation(bottom);

        int secondsDelayed = 4;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(SplashScreen.this, MainActivity.class));
                finish();
            }
        }, secondsDelayed * 1000);
    }
}