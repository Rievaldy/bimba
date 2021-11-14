package com.example.bimba;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    private SessionManagement sessionManagement;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //menghilangkan ActionBar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash_screen);

        sessionManagement = new SessionManagement(getApplicationContext());

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(sessionManagement.getUserAccessSession() == 0){
                    startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                    finish();
                }else{
                    switch (sessionManagement.getUserAccessSession()){
                        case 1 : modeAdmin();break;
                        case 2 : modeOwner();break;
                        case 3 : modeUser();break;
                    }
                }
            }
        }, 3000L); //3000 L = 3 detik
    }

    public void modeUser(){
        Intent intent = new Intent(SplashScreen.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void modeAdmin(){
        Intent intent = new Intent(SplashScreen.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void modeOwner(){
        Intent intent = new Intent(SplashScreen.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
