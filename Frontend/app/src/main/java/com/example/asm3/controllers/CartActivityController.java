package com.example.asm3.controllers;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm3.R;
import com.example.asm3.base.controller.BaseController;
import com.example.asm3.base.networking.services.AsyncTaskCallBack;
import com.example.asm3.custom.components.TopBarView;

public class CartActivityController extends BaseController implements
        AsyncTaskCallBack,
        View.OnClickListener {
    private TopBarView topBar;
    private RecyclerView cartRecView;
    private Button backBtn;

    public CartActivityController(Context context, FragmentActivity activity) {
        super(context, activity);
        topBar = getActivity().findViewById(R.id.cartTopBar);
        topBar.setSubPage("Your Cart");
    }

    @Override
    public void onInit() {
        backBtn = topBar.getBackButton();
        backBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                getActivity().finish();
                break;
        }
    }

    @Override
    public void onFinished(String message, String taskType) {

    }

    @Override
    public void onError(String taskType) {

    }
}
