package com.example.asm3.controllers;

import android.app.Activity;
import android.content.Context;

import com.example.asm3.ManageBookActivity;
import com.example.asm3.base.controller.BaseController;
import com.example.asm3.base.localStorage.LocalFileController;
import com.example.asm3.base.networking.services.AsyncTaskCallBack;
import com.example.asm3.base.networking.services.PostAuthenticatedData;
import com.example.asm3.config.Constant;
import com.example.asm3.models.Book;

import java.util.ArrayList;

public class ManageBookActivityController extends BaseController implements AsyncTaskCallBack {

    private Book book;
    private PostAuthenticatedData postAuthenticatedData;
    private final LocalFileController<String> localFileController;
    private String token;

    public ManageBookActivityController(Context context, Activity activity) {
        super(context, activity);
        postAuthenticatedData = new PostAuthenticatedData(context, this);
        localFileController = new LocalFileController<String>(Constant.tokenFile,context);
    }

    @Override
    public void onInit(){
        ArrayList<String> list = new ArrayList<String>();
        list = localFileController.readFile();

        if (list.isEmpty()) {

        } else {
            token = list.get(0);
        }

        uploadBook(new Book("Harry Potter","Hai", "hahaha",100, 10, "2008",null,null,null,""));
    }

    public void uploadBook(Book inputBook){
        postAuthenticatedData.setEndPoint(Constant.uploadBook);
        postAuthenticatedData.setTaskType(Constant.uploadBookTaskType);
        postAuthenticatedData.setToken(token);
        postAuthenticatedData.execute(Book.toJSON(inputBook));
    }

    @Override
    public void onFinished(String message,String taskType){
        if (taskType.equals(Constant.uploadBookTaskType)){

        }
    }

}
