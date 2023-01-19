package com.example.asm3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.asm3.controllers.CartActivityController;
import com.example.asm3.controllers.ForgetPasswordActivityController;

public class ForgetPasswordActivity extends AppCompatActivity {
    private ForgetPasswordActivityController forgetPasswordActivityController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        onInit();
    }

    public void onInit(){
        forgetPasswordActivityController = new ForgetPasswordActivityController(ForgetPasswordActivity.this,this);
        forgetPasswordActivityController.onInit();

    }
}