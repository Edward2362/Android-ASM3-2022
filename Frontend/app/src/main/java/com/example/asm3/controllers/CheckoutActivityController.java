package com.example.asm3.controllers;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.FragmentActivity;

import com.example.asm3.base.controller.BaseController;
import com.example.asm3.base.networking.services.AsyncTaskCallBack;

public class CheckoutActivityController extends BaseController implements AsyncTaskCallBack {
    public CheckoutActivityController(Context context, FragmentActivity activity){
        super(context,activity);

    }

    @Override
    public void onInit(){

    }

    @Override
    public void onFinished(String message, String taskType){
        
    }

    @Override
    public void onError(String taskType) {

    }
}
