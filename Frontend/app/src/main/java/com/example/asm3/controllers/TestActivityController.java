package com.example.asm3.controllers;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.example.asm3.base.controller.BaseController;
import com.example.asm3.base.networking.services.AsyncTaskCallBack;
import com.example.asm3.base.networking.services.GetData;
import com.example.asm3.config.Constant;
import com.example.asm3.models.ApiList;
import com.example.asm3.models.Category;

import java.util.ArrayList;

public class TestActivityController extends BaseController implements AsyncTaskCallBack {
    private ArrayList<Category> categories;
    private GetData getData;

    public TestActivityController(Context context, FragmentActivity activity) {
        super(context, activity);
        categories = new ArrayList<Category>();
        getData = new GetData(context, this);
    }

    public void getAllCategories(){
        getData.setEndPoint(Constant.getAllCategories);
        getData.setTaskType(Constant.getAllCategoriesTaskType);
        getData.execute();
    }
    @Override
    public void onInit() {
        getAllCategories();
    }

    @Override
    public void onFinished(String message, String taskType) {
        if (taskType.equals(Constant.getAllCategoriesTaskType)) {
            ApiList<Category> apiList = ApiList.fromJSON(ApiList.getData(message),Category.class);
            categories = apiList.getList();
            for (int i=0;i < categories.size(); i++) {
                Toast.makeText(getContext(), categories.get(i).getName(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onError(String taskType) {

    }
}
