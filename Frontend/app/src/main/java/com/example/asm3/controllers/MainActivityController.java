package com.example.asm3.controllers;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentContainerView;

import com.example.asm3.AuthenticationActivity;
import com.example.asm3.R;

import com.example.asm3.base.controller.BaseController;
import com.example.asm3.base.localStorage.LocalFileController;
import com.example.asm3.base.networking.services.AsyncTaskCallBack;
import com.example.asm3.base.networking.services.GetAuthenticatedData;
import com.example.asm3.config.Constant;
import com.example.asm3.fragments.mainActivity.HomeFragment;
import com.example.asm3.fragments.mainActivity.ProfileFragment;
import com.example.asm3.models.ApiData;
import com.example.asm3.models.Customer;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class MainActivityController extends BaseController implements AsyncTaskCallBack, NavigationBarView.OnItemSelectedListener {
    private final LocalFileController<String> localFileController;
    private boolean isAuth = false;
    private Customer customer;
    private String token;
    private LinearLayout linearLayout;
    private GetAuthenticatedData getAuthenticatedData;

    private NavigationBarView menu;
    private HomeFragment homeFragment;
    private ProfileFragment profileFragment;

    public MainActivityController(Context context, Activity activity) {
        super(context, activity);

        homeFragment = new HomeFragment();
        profileFragment = new ProfileFragment();
        homeFragment.setController(this);

        localFileController = new LocalFileController<String>(Constant.tokenFile, context);
        getAuthenticatedData = new GetAuthenticatedData(getContext(), this);
    }

    // Render functions
    @Override
    public void onInit() {
        ArrayList<String> list = new ArrayList<String>();
        list = localFileController.readFile();

        menu = getActivity().findViewById(R.id.menu);
        menu.setOnItemSelectedListener(this);
        if (list.isEmpty() || list.get(0).equals("")) {
//            setLoginLayout();
        } else {
            getAuthenticatedData.setEndPoint(Constant.getCustomerData);
            getAuthenticatedData.setToken(list.get(0));

            getAuthenticatedData.setTaskType(Constant.getCustomer);
            getAuthenticatedData.execute();
        }
    }

    public void setLoginLayout() {

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
            firstName.setTextSize(17);

            TextView lastName = new TextView(getContext());
            lastName.setLayoutParams(params);
            lastName.setText(customer.getLastName());
            lastName.setTextSize(17);

            linearLayout.addView(firstName);
            linearLayout.addView(lastName);
        }
    }

    public void loadFragment(Fragment fragment) {
        FragmentActivity activity = (FragmentActivity) getActivity();
        FragmentManager fm = getActivity().getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.mainActivity_frameLayout, fragment);

        fragmentTransaction.commit();
    }

    // Helpers


    // Request functions


    // Start activity functions
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.homeNav:
                loadFragment(homeFragment);
                return true;
        }

        return false;
    }

    public void goToRegister() {
        Intent intent = new Intent(getContext(), AuthenticationActivity.class);
        intent.putExtra(Constant.mainFragment, Constant.register);
        getActivity().startActivityForResult(intent, Constant.registerActivity);
    }

    public void goToLogin() {
        Intent intent = new Intent(getContext(), AuthenticationActivity.class);
        intent.putExtra(Constant.mainFragment, Constant.login);
        getActivity().startActivityForResult(intent, Constant.loginActivity);
    }

    // Callback functions
    public void onActivityFinished(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constant.loginActivity) {
                if (data.getExtras().getSerializable(Constant.customerKey) != null) {
                    customer = (Customer) data.getExtras().getSerializable(Constant.customerKey);

                    setCustomerDataLayout();
                }
            }
        }
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
