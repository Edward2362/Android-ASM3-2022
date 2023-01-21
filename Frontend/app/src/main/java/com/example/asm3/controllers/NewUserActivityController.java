package com.example.asm3.controllers;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.FragmentActivity;

import com.example.asm3.R;
import com.example.asm3.base.controller.BaseController;

public class NewUserActivityController extends BaseController implements View.OnClickListener {
    private Button startBtn;

    public NewUserActivityController(Context context, FragmentActivity activity) {
        super(context, activity);
    }

    @Override
    public void onInit() {
        startBtn = getActivity().findViewById(R.id.startBtn);

        startBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        getActivity().finish();
    }
}
