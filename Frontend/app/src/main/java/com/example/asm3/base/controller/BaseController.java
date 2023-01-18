package com.example.asm3.base.controller;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.example.asm3.base.localStorage.LocalFileController;
import com.example.asm3.config.Constant;

import java.util.ArrayList;

public abstract class BaseController {
    private Context context;
    private FragmentActivity activity;
    private LocalFileController<String> localFileController;

    public BaseController(Context context, FragmentActivity activity) {
        this.context = context;
        this.activity = activity;
        this.localFileController = new LocalFileController<String>(Constant.tokenFile,context);
    }

    public LocalFileController<String> getLocalFileController() {
        return localFileController;
    }

    public void setLocalFileController(LocalFileController<String> localFileController) {
        this.localFileController = localFileController;
    }

    public Context getContext() {
        return this.context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public FragmentActivity getActivity() {
        return this.activity;
    }

    public void setActivity(FragmentActivity activity) {
        this.activity = activity;
    }

    public String getToken(){
        ArrayList<String> arrayList = new ArrayList<String>();
        String token ="";
        arrayList = localFileController.readFile();
        if (!arrayList.isEmpty()){
            token = arrayList.get(0);
        }
        return token;
    }

    public boolean isAuth(){
        ArrayList<String> arrayList = new ArrayList<String>();
        Boolean isTokenStored = false;
        arrayList = localFileController.readFile();
        if (!arrayList.isEmpty() && !arrayList.get(0).equals("")){
            isTokenStored = true;
        }
        Log.d("TAG", "getToken: test " + isTokenStored);
        return isTokenStored;
    }

    public abstract void onInit();
}
