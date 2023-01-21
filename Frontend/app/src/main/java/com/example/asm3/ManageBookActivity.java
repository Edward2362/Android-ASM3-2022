package com.example.asm3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        manageBookActivityController.onRequestPermissionsResult(requestCode, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        manageBookActivityController.onActivityResult(requestCode, resultCode, data);
    }

}