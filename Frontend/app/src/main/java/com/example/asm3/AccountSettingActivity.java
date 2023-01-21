package com.example.asm3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        accountSettingActivityController.onRequestPermissionsResult(requestCode, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        accountSettingActivityController.onActivityResult(requestCode, resultCode, data);
    }
}