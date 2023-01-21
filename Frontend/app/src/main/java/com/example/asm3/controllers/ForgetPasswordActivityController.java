package com.example.asm3.controllers;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import com.example.asm3.R;
import com.example.asm3.base.controller.BaseController;
import com.example.asm3.base.networking.services.AsyncTaskCallBack;

public class ForgetPasswordActivityController extends BaseController implements
        AsyncTaskCallBack,
        View.OnClickListener {
    private TextView loginForgetPwTxt;
    private Button requestPwBtn;

    public ForgetPasswordActivityController(Context context, FragmentActivity activity) {
        super(context, activity);


    }

    // Render functions
    @Override
    public void onInit() {
        requestPwBtn = getActivity().findViewById(R.id.requestPwBtn);
        loginForgetPwTxt = getActivity().findViewById(R.id.testingTxt);
        requestPwBtn.setOnClickListener(this);
        loginForgetPwTxt.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.testingTxt:
                getActivity().finish();
                break;
            case R.id.requestPwBtn:
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
