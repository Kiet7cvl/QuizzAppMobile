package com.coloful.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.coloful.R;
import com.coloful.datalocal.DataLocalManager;
import com.coloful.model.Account;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        DataLocalManager.init(this);
        Account account = null;
        try {
            account = DataLocalManager.getAccount();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (account == null) {
            Intent landing = new Intent(SplashActivity.this, LandingActivity.class);
            startActivity(landing);
        } else {
            Intent main = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(main);
        }
    }
}