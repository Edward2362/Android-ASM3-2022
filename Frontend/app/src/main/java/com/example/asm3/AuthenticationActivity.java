package com.example.asm3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.example.asm3.config.Helper;
import com.example.asm3.controllers.AuthenticationActivityController;
import com.example.asm3.fragments.authenticationActivity.LoginFragment;
import com.example.asm3.fragments.authenticationActivity.RegisterFragment;

public class AuthenticationActivity extends AppCompatActivity {
    private AuthenticationActivityController authenticationActivityController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        onInit();
        if (savedInstanceState == null) {
            Helper.loadFragment(authenticationActivityController.getFragmentManager(), authenticationActivityController.getLoginFragment(), "login", authenticationActivityController.getAuthLayoutId());
        }
    }

    public void onInit() {
        authenticationActivityController = new AuthenticationActivityController(getApplicationContext(), this);
        authenticationActivityController.onInit();
    }

    @Override
    public void onBackPressed() {
        if (authenticationActivityController.getFragmentManager().findFragmentByTag("register") instanceof RegisterFragment) {
            Fragment loginFragment = authenticationActivityController.getLoginFragment();
            Helper.loadFragment(authenticationActivityController.getFragmentManager(), loginFragment, "home", authenticationActivityController.getAuthLayoutId());
        } else {
            super.onBackPressed();
        }
    }
}