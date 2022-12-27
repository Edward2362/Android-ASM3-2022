package com.example.asm3.controllers;

import android.app.Activity;
import android.content.Context;

import com.example.asm3.base.controller.BaseController;
import com.example.asm3.base.networking.services.AsyncTaskCallBack;
import com.example.asm3.base.networking.services.GetData;
import com.example.asm3.config.Constant;
import com.example.asm3.models.ApiData;
import com.example.asm3.models.Book;

public class ProductDetailActivityController extends BaseController implements AsyncTaskCallBack {
    private GetData getData;
    private Book book;
    public ProductDetailActivityController(Context context, Activity activity) {
        super(context,activity);
        getData = new GetData(context,this);

    }

    @Override
    public void onInit(){

    }

    public void getProduct(String productId){
        getData.setEndPoint(Constant.getProduct+"/"+productId);
        getData.setTaskType(Constant.getProductTaskType);
        getData.execute();
    }

    @Override
    public void onFinished(String message, String taskType){
        if (taskType.equals(Constant.getProductTaskType)){
            ApiData<Book> apiData = ApiData.fromJSON(ApiData.getData(message),Book.class);
            book = apiData.getData();
        }
    }
}
