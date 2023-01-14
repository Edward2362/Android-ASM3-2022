package com.example.asm3.controllers;

import android.content.Context;
import android.os.Handler;
import android.view.View;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm3.base.adapter.GenericAdapter;
import com.example.asm3.base.controller.BaseController;
import com.example.asm3.fragments.mainActivity.MainViewModel;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.ArrayList;

public class SearchFragmentController extends BaseController {

    private LinearProgressIndicator progressBar;
    private GenericAdapter<String> searchAdapter;
    private RecyclerView searchSuggestionRecView;
    private View view;

    private MainViewModel mainViewModel;
    private long lastTextEdit = 0;
    private Handler handler = new Handler();
    private ArrayList<String> searchSuggestions;

    public SearchFragmentController(Context context, FragmentActivity activity, View view) {
        super(context, activity);
        this.view = view;
        mainViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        searchSuggestions = new ArrayList<>();
    }

    @Override
    public void onInit() {

    }
}
