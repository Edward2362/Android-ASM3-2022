package com.example.asm3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.asm3.controllers.CartActivityController;

public class CartActivity extends AppCompatActivity {
     private CartActivityController cartActivityController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        onInit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cartActivityController.onResume();
    }

    public void onInit(){
        cartActivityController = new CartActivityController(CartActivity.this,this);
        cartActivityController.onInit();
    }
}