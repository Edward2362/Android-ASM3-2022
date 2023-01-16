package com.example.asm3;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.asm3.config.Helper;
import com.example.asm3.controllers.MainActivityController;
import com.example.asm3.fragments.mainActivity.MainViewModel;

public class MainActivity extends AppCompatActivity {
    private MainActivityController mainActivityController;
    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        setContentView(R.layout.activity_main);
        onInit();
        if (savedInstanceState == null) {
            Helper.loadFragment(mainActivityController.getFragmentManager(), mainActivityController.getHomeFragment(), "home", mainActivityController.getMainLayoutId());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainActivityController.onResume();
    }

    public void onInit() {
        mainActivityController = new MainActivityController(getApplicationContext(), this);
        mainActivityController.onInit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mainActivityController.onActivityFinished(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (mainActivityController.getSelectedItemId() != R.id.homeNav) {
            Fragment homeFragment = mainActivityController.getHomeFragment();
            Helper.loadFragment(mainActivityController.getFragmentManager(), homeFragment, "home", mainActivityController.getMainLayoutId());
            mainActivityController.loadMenu();
            mainActivityController.getTopBar().setMainPage("GoGoat");
        } else {
            super.onBackPressed();
        }
    }
}