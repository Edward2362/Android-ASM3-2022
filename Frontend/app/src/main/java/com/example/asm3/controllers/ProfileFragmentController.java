package com.example.asm3.controllers;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;

import com.example.asm3.AuthenticationActivity;
import com.example.asm3.base.controller.BaseController;
import com.example.asm3.config.Constant;
import com.example.asm3.fragments.mainActivity.MainViewModel;

public class ProfileFragmentController extends BaseController {
    private View view;

    private MainViewModel mainViewModel;

    public ProfileFragmentController(Context context, FragmentActivity activity, View view, ViewModel viewModel) {
        super(context, activity);
        this.view = view;
        this.mainViewModel = (MainViewModel) viewModel;
    }

    // Render functions
    @Override
    public void onInit() {
        if (!isAuth()) {
            goToLogin();
        }
    }

    // Helpers


    // Request functions


    // Navigation functions
    public void goToLogin() {
        Intent intent = new Intent(getContext(), AuthenticationActivity.class);
        getActivity().startActivityForResult(intent, Constant.authActivityCode);
    }

    // Callback functions


    // Getter and Setter
}
