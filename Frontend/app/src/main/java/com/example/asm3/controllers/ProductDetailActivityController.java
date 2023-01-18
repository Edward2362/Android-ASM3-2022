package com.example.asm3.controllers;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.FragmentActivity;

import com.example.asm3.base.controller.BaseController;
import com.example.asm3.base.networking.services.AsyncTaskCallBack;
import com.example.asm3.base.networking.services.GetData;
import com.example.asm3.config.Constant;
import com.example.asm3.models.ApiData;
import com.example.asm3.models.Book;

public class ProductDetailActivityController extends BaseController implements AsyncTaskCallBack {
    private GetData getData;
    private Book book;

    public ProductDetailActivityController(Context context, FragmentActivity activity) {
        super(context, activity);
    }

    // Render functions
    @Override
    public void onInit() {

    }

    // Helpers


    // Request functions
    public void getProduct(String productId) {
        getData = new GetData(getContext(), this);
        getData.setEndPoint(Constant.getProduct + "/" + productId);
        getData.setTaskType(Constant.getProductTaskType);
        getData.execute();
    }

    // Navigation functions


    // Callback functions
    @Override
    public void onFinished(String message, String taskType) {
        if (taskType.equals(Constant.getProductTaskType)) {
            ApiData<Book> apiData = ApiData.fromJSON(ApiData.getData(message), Book.class);
            book = apiData.getData();
        }
    }

    @Override
    public void onError(String taskType) {

    }

    // Getter and Setter
}
