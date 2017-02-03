package com.mkd.zappos.love.ilovezappos.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.mkd.zappos.love.ilovezappos.R;


public class SplashScreenActivity extends AppCompatActivity {
    // Timeout interaval 3s
    private static final int SPLASH_TIME_OUT = 3000;
    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i =new Intent(SplashScreenActivity.this, MainActivity.class);
                // Start the main activity after splash screen completes running for 3s
                startActivity(i);

                // Finish this splash screen activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
