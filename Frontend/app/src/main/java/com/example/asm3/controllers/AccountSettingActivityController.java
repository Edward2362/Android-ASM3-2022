package com.example.asm3.controllers;

import android.app.Activity;
import android.content.Context;

import com.example.asm3.base.controller.BaseController;
import com.example.asm3.base.networking.services.AsyncTaskCallBack;
import com.example.asm3.base.networking.services.PostAuthenticatedData;
import com.example.asm3.config.Constant;
import com.example.asm3.models.Customer;

import org.json.JSONException;
import org.json.JSONObject;

public class AccountSettingActivityController extends BaseController implements AsyncTaskCallBack {

    private PostAuthenticatedData postAuthenticatedData;

    public AccountSettingActivityController(Context context, Activity activity){
        super(context, activity);
        postAuthenticatedData = new PostAuthenticatedData(context,this);

    }

    @Override
    public void onInit(){

    }

    public void updateCustomerInfo(String username,String address){
        try {
            if(isAuth()){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Customer.usernameKey,username);
            jsonObject.put(Customer.addressKey,address);
            postAuthenticatedData.setEndPoint(Constant.setCustomerData);
            postAuthenticatedData.setTaskType(Constant.setCustomerDataTaskType);
            postAuthenticatedData.setToken(getToken());
            postAuthenticatedData.execute(jsonObject);
            }
        } catch (JSONException exception){
            exception.printStackTrace();
        }
    }

    public void changePassword(String newPassword){
        try {
            if(isAuth()){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(Customer.passwordKey,newPassword);
                postAuthenticatedData.setEndPoint(Constant.changePassword);
                postAuthenticatedData.setTaskType(Constant.changePasswordTaskType);
                postAuthenticatedData.setToken(getToken());
                postAuthenticatedData.execute(jsonObject);
            }
        } catch (JSONException exception){
            exception.printStackTrace();
        }
    }
    @Override
    public void onFinished(String message,String taskType){
        if (taskType.equals(Constant.setCustomerDataTaskType)){

        }

    }
}
