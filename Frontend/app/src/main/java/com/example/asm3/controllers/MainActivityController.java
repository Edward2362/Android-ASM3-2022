package com.example.asm3.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asm3.AuthenticationActivity;
import com.example.asm3.R;

import com.example.asm3.base.controller.BaseController;
import com.example.asm3.base.localStorage.LocalFileController;
import com.example.asm3.base.networking.api.ApiService;
import com.example.asm3.base.networking.services.AuthApi;
import com.example.asm3.config.Constant;
import com.example.asm3.models.ApiData;
import com.example.asm3.models.Customer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivityController extends BaseController {
    private LocalFileController localFileController;
    private boolean isAuth = false;
    private Customer customer;
    private String token;
    private LinearLayout linearLayout;
    private ApiService apiService;

    public MainActivityController(Context context, Activity activity) {
        super(context, activity);

        localFileController = new LocalFileController("token.txt", context);
        linearLayout = (LinearLayout) getActivity().findViewById(R.id.mainActivity_layout);
        apiService = new ApiService(Constant.baseDomain);
    }

    @Override
    public void onInit() {
        ArrayList<String> list = new ArrayList<String>();
        list = localFileController.readFile();
        if (list.isEmpty() || list.get(0).equals("")) {
            setLoginLayout();
        } else {
            GetCustomer getCustomerTask = new GetCustomer();
            getCustomerTask.execute(list.get(0));
        }
    }

    public void setLoginLayout() {
        linearLayout.removeAllViews();

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        Button loginButton = new Button(getContext());
        loginButton.setLayoutParams(params);
        loginButton.setText(Constant.login);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLogin();
            }
        });

        Button registerButton = new Button(getContext());
        registerButton.setLayoutParams(params);
        registerButton.setText(Constant.register);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToRegister();
            }
        });

        linearLayout.addView(loginButton);
        linearLayout.addView(registerButton);
    }

    public void setCustomerDataLayout() {
        if (customer != null) {
            linearLayout.removeAllViews();

            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );

            TextView firstName = new TextView(getContext());
            firstName.setLayoutParams(params);
            firstName.setText(customer.getFirstName());
            firstName.setTextColor(getActivity().getResources().getColor(R.color.black));
            firstName.setTextSize(17);

            TextView lastName = new TextView(getContext());
            lastName.setLayoutParams(params);
            lastName.setText(customer.getLastName());
            lastName.setTextColor(getActivity().getResources().getColor(R.color.black));
            lastName.setTextSize(17);

            linearLayout.addView(firstName);
            linearLayout.addView(lastName);
        }
    }

    public void goToRegister() {
        Intent intent = new Intent(getContext(), AuthenticationActivity.class);
        intent.putExtra(Constant.mainFragment, Constant.register);
        getActivity().startActivityForResult(intent, 200);
    }

    public void goToLogin() {
        Intent intent = new Intent(getContext(), AuthenticationActivity.class);
        intent.putExtra(Constant.mainFragment, Constant.login);
        getActivity().startActivityForResult(intent, 500);
    }

    public void onActivityFinished(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 500) {
                if (data.getExtras().getSerializable("customerData") != null) {
                    customer = (Customer) data.getExtras().getSerializable("customerData");

                    setCustomerDataLayout();
                }
            }
        }
    }

    private class GetCustomer extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... data) {
            String message = apiService.getJSON(Constant.getCustomerData, data[0]);
            return message;
        }

        @Override
        protected void onPostExecute(String message) {
            onFinished(message);
        }
    }

    public void onFinished(String message) {
        try {
            JSONObject jsonObject = new JSONObject(message);
            if (jsonObject.getBoolean("error")) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            } else {
                ApiData<Customer> apiData = ApiData.fromJSON(ApiData.getData(message), Customer.class);

                customer = apiData.getData();
                setCustomerDataLayout();
            }
        } catch(JSONException jsonException) {
            jsonException.printStackTrace();
            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
        }
    }
}
