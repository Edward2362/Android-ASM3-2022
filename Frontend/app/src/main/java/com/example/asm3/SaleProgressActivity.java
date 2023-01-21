package com.example.asm3;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;

import com.example.asm3.controllers.SaleProgressActivityController;

public class SaleProgressActivity extends AppCompatActivity {
    private SaleProgressActivityController saleProgressActivityController;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_progress);

        onInit();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onInit() {
        saleProgressActivityController = new SaleProgressActivityController(this,this);
        saleProgressActivityController.onInit();
    }
}