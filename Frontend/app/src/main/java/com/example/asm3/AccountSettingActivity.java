package com.example.asm3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.asm3.controllers.AccountSettingActivityController;

public class AccountSettingActivity extends AppCompatActivity {

    private AccountSettingActivityController accountSettingActivityController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);
        onInit();
    }

    public void onInit(){
        accountSettingActivityController = new AccountSettingActivityController(AccountSettingActivity.this,this);
        accountSettingActivityController.onInit();
    }

}