package com.example.asm3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        productDetailActivityController.onActivityResult(requestCode, resultCode, data);
    }
}