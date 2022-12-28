package com.example.asm3.controllers;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.FragmentActivity;

import com.example.asm3.ManageBookActivity;
import com.example.asm3.base.controller.BaseController;
import com.example.asm3.base.localStorage.LocalFileController;
import com.example.asm3.base.networking.services.AsyncTaskCallBack;
import com.example.asm3.base.networking.services.DeleteAuthenticatedData;
import com.example.asm3.base.networking.services.PostAuthenticatedData;
import com.example.asm3.config.Constant;
import com.example.asm3.models.Book;

import java.util.ArrayList;

public class ManageBookActivityController extends BaseController implements AsyncTaskCallBack {

    private Book book;
    private PostAuthenticatedData postAuthenticatedData;
    private DeleteAuthenticatedData deleteAuthenticatedData;

    private String token;

    public ManageBookActivityController(Context context, FragmentActivity activity) {
        super(context, activity);
        postAuthenticatedData = new PostAuthenticatedData(context, this);
        deleteAuthenticatedData = new DeleteAuthenticatedData(context, this);
    }

    @Override
    public void onInit(){

        if (!isAuth()) {

        } else {
            token = getToken();
        }

        uploadBook(new Book("Harry Potter","Hai", "hahaha",100, 10, "2008",null,null,null,""));
    }

    public void uploadBook(Book inputBook){
        postAuthenticatedData.setEndPoint(Constant.uploadBook);
        postAuthenticatedData.setTaskType(Constant.uploadBookTaskType);
        postAuthenticatedData.setToken(token);
        postAuthenticatedData.execute(Book.toJSON(inputBook));
    }

    public void updateBook(Book inputBook) {
        postAuthenticatedData.setEndPoint(Constant.updateBook);
        postAuthenticatedData.setTaskType(Constant.updateBookTaskType);
        postAuthenticatedData.setToken(token);
        postAuthenticatedData.execute(Book.toJSON(inputBook));
    }

    public void deleteBook(String bookId) {
        deleteAuthenticatedData.setEndPoint(Constant.deleteBook + "/" + bookId);
        deleteAuthenticatedData.setTaskType(Constant.deleteBookTaskType);
        deleteAuthenticatedData.setToken(token);
        deleteAuthenticatedData.execute();
    }

    @Override
    public void onFinished(String message,String taskType){
        if (taskType.equals(Constant.uploadBookTaskType)){

        }
    }

}
