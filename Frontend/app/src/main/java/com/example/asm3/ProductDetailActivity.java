package com.example.asm3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.asm3.controllers.ProductDetailActivityController;

public class ProductDetailActivity extends AppCompatActivity {

    private ProductDetailActivityController productDetailActivityController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        onInit();
    }

    public void onInit(){
        productDetailActivityController = new ProductDetailActivityController(ProductDetailActivity.this,this);
        productDetailActivityController.onInit();
    }
}