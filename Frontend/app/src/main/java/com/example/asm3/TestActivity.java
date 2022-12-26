package com.example.asm3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.asm3.controllers.TestActivityController;

public class TestActivity extends AppCompatActivity {

    private TestActivityController testActivityController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        onInit();
    }

    public void onInit(){
        testActivityController = new TestActivityController(TestActivity.this,this);
        testActivityController.onInit();
    }

}