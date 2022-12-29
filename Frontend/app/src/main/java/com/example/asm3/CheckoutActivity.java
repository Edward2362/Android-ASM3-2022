package com.example.asm3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.asm3.controllers.CheckoutActivityController;

public class CheckoutActivity extends AppCompatActivity {

    private CheckoutActivityController checkoutActivityController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        onInit();
    }

    public void onInit(){
        checkoutActivityController = new CheckoutActivityController(CheckoutActivity.this,this);
        checkoutActivityController.onInit();
    }
}