package com.example.asm3.controllers;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.asm3.AuthenticationActivity;
import com.example.asm3.R;

import com.example.asm3.base.controller.BaseController;
import com.example.asm3.base.localStorage.LocalFileController;
import com.example.asm3.base.networking.services.AsyncTaskCallBack;
import com.example.asm3.base.networking.services.GetAuthenticatedData;
import com.example.asm3.config.Constant;
import com.example.asm3.custom.components.TopBarView;
import com.example.asm3.models.ApiData;
import com.example.asm3.models.Customer;

import java.util.ArrayList;

public class MainActivityController extends BaseController implements AsyncTaskCallBack {
//    private final LocalFileController<String> localFileController;
    private boolean isAuth = false;
    private Customer customer;
    private String token;
    private LinearLayout linearLayout;
    private GetAuthenticatedData getAuthenticatedData;
    private TopBarView topBar;

    public MainActivityController(Context context, Activity activity) {
        super(context, activity);

//        localFileController = new LocalFileController<String>(Constant.tokenFile, context);
//        linearLayout = (LinearLayout) getActivity().findViewById(R.id.mainActivity_layout);
//        getAuthenticatedData = new GetAuthenticatedData(getContext(), this);
        topBar = getActivity().findViewById(R.id.topBar);
        topBar.setSearchResultPage("Henry the goat");
    }

    // Render functions
    @Override
    public void onInit() {

//        LocalFileController<String> test = new LocalFileController<String>("test.txt", getContext());
//        ArrayList<String> testArr = new ArrayList<String>();
//        testArr.add("Quang");
//
//        test.writeFile(testArr);
//
//        ArrayList<String> result = test.readFile();
//
//        Log.d(TAG, "onInit: "  + result.get(0));
//
//        ArrayList<String> list = new ArrayList<String>();
//        list = localFileController.readFile();
//
//        if (list.isEmpty() || list.get(0).equals("")) {
//            setLoginLayout();
//        } else {
//            getAuthenticatedData.setEndPoint(Constant.getCustomerData);
//            getAuthenticatedData.setToken(list.get(0));
//
//            getAuthenticatedData.setTaskType(Constant.getCustomer);
//            getAuthenticatedData.execute();
//        }
    }

    public void setLoginLayout() {
//        linearLayout.removeAllViews();
//
//        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT
//        );

//        Button loginButton = new Button(getContext());
//        loginButton.setLayoutParams(params);
//        loginButton.setText(Constant.login);
//
//        loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                goToLogin();
//            }
//        });

//        Button registerButton = new Button(getContext());
//        registerButton.setLayoutParams(params);
//        registerButton.setText(Constant.register);
//
//        registerButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                goToRegister();
//            }
//        });
//
//        linearLayout.addView(loginButton);
//        linearLayout.addView(registerButton);
    }

    public void setCustomerDataLayout() {
//        if (customer != null) {
//            linearLayout.removeAllViews();
//
//            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
//                    ViewGroup.LayoutParams.WRAP_CONTENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT
//            );
//
//            TextView firstName = new TextView(getContext());
//            firstName.setLayoutParams(params);
//            firstName.setText(customer.getFirstName());
//            firstName.setTextSize(17);
//
//            TextView lastName = new TextView(getContext());
//            lastName.setLayoutParams(params);
//            lastName.setText(customer.getLastName());
//            lastName.setTextSize(17);
//
//            linearLayout.addView(firstName);
//            linearLayout.addView(lastName);
//        }
    }

    // Helpers


    // Request functions


    // Start activity functions
//    public void goToRegister() {
//        Intent intent = new Intent(getContext(), AuthenticationActivity.class);
//        intent.putExtra(Constant.mainFragment, Constant.register);
//        getActivity().startActivityForResult(intent, Constant.registerActivity);
//    }
//
//    public void goToLogin() {
//        Intent intent = new Intent(getContext(), AuthenticationActivity.class);
//        intent.putExtra(Constant.mainFragment, Constant.login);
//        getActivity().startActivityForResult(intent, Constant.loginActivity);
//    }

    // Callback functions
    public void onActivityFinished(int requestCode, int resultCode, Intent data) {
//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == Constant.loginActivity) {
//                if (data.getExtras().getSerializable(Constant.customerKey) != null) {
//                    customer = (Customer) data.getExtras().getSerializable(Constant.customerKey);
//
//                    setCustomerDataLayout();
//                }
//            }
//        }
    }

    @Override
    public void onFinished(String message, String taskType) {
        if (taskType.equals(Constant.getCustomer)) {
            ApiData<Customer> apiData = ApiData.fromJSON(ApiData.getData(message), Customer.class);
            customer = apiData.getData();
            setCustomerDataLayout();
        }
    }
}
