package com.example.novelsocial;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.example.novelsocial.databinding.ActivitySplashScreenBinding;
import com.parse.ParseUser;

import java.util.Timer;
import java.util.TimerTask;

@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivitySplashScreenBinding activitySplashScreenBinding = ActivitySplashScreenBinding.inflate(getLayoutInflater());

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (ParseUser.getCurrentUser() != null) {
                    // getCurrentUser would be null if user is not logged in already
                    Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 3000);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        setContentView(activitySplashScreenBinding.getRoot());
    }
}