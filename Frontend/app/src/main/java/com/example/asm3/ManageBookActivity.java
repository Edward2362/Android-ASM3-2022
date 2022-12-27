package com.example.asm3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.asm3.controllers.ManageBookActivityController;

public class ManageBookActivity extends AppCompatActivity {

    private ManageBookActivityController manageBookActivityController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_book);
        onInit();
    }

    public void onInit() {
        manageBookActivityController = new ManageBookActivityController(ManageBookActivity.this, this);
        manageBookActivityController.onInit();
    }

}