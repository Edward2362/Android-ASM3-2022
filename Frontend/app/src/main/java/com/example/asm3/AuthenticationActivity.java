package com.example.asm3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Fragment;
import android.os.Bundle;

import com.example.asm3.controllers.AuthenticationActivityController;
import com.example.asm3.fragments.authenticationActivity.LoginFragment;

public class AuthenticationActivity extends AppCompatActivity {
    private AuthenticationActivityController authenticationActivityController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        onInit();
    }

    public void onInit() {
        authenticationActivityController = new AuthenticationActivityController(AuthenticationActivity.this, this);
        authenticationActivityController.onInit();
    }
}