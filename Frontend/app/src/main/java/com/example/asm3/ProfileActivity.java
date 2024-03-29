package com.example.asm3;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import com.example.asm3.controllers.ProfileActivityController;

public class ProfileActivity extends AppCompatActivity {
    private ProfileActivityController profileActivityController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            onInit();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onInit() {
        profileActivityController = new ProfileActivityController(this, this);
        profileActivityController.onInit();
    }
}