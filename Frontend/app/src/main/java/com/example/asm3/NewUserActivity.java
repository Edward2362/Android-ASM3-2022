package com.example.asm3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.asm3.controllers.NewUserActivityController;

public class NewUserActivity extends AppCompatActivity {
    private NewUserActivityController newUserActivityController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        onInit();
    }

    private void onInit() {
        newUserActivityController = new NewUserActivityController(this, this);
        newUserActivityController.onInit();
    }


}