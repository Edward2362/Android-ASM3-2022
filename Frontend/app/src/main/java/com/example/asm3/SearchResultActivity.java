package com.example.asm3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import com.example.asm3.controllers.SearchResultActivityController;


public class SearchResultActivity extends AppCompatActivity {
    private SearchResultActivityController searchResultActivityController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        onInit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchResultActivityController.onResume();
    }

    public void onInit() {
        searchResultActivityController = new SearchResultActivityController(getApplicationContext(), this);
        searchResultActivityController.onInit();
    }
}