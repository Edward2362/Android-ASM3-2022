package com.example.asm3.base.controller;

import android.app.Activity;
import android.content.Context;

import com.example.asm3.base.localStorage.LocalFileController;
import com.example.asm3.config.Constant;

import java.util.ArrayList;

public abstract class BaseController {
    private Context context;
    private Activity activity;
    private LocalFileController<String> localFileController;

    public BaseController(Context context, Activity activity) {
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

    public Activity getActivity() {
        return this.activity;
    }

    public void setActivity(Activity activity) {
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
        return isTokenStored;
    }
    public abstract void onInit();
}
