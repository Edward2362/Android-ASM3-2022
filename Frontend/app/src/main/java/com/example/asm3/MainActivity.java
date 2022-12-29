package com.example.asm3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import com.example.asm3.config.Constant;
import com.example.asm3.controllers.MainActivityController;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private MainActivityController mainActivityController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "before onInit: mainActivity test");
        setContentView(R.layout.activity_main);
        onInit();
        Log.d(TAG, "after onInit: mainActivity test");
    }

    public void onInit() {
        mainActivityController = new MainActivityController(MainActivity.this, this);
        mainActivityController.onInit();
//        mainActivityController.setSelectedItemId(R.id.profileNav);
//        mainActivityController.loadMenu();
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
            mainActivityController.loadFragment(homeFragment, "home");
        } else {
            super.onBackPressed();
        }
    }
}