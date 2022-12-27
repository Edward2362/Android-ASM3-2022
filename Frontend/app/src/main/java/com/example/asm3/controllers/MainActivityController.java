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
import android.widget.Toast;

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
import com.example.asm3.base.networking.services.GetData;
import com.example.asm3.config.Constant;
import com.example.asm3.fragments.mainActivity.HomeFragment;
import com.example.asm3.fragments.mainActivity.ProfileFragment;
import com.example.asm3.config.Helper;
import com.example.asm3.models.ApiData;
import com.example.asm3.models.ApiList;
import com.example.asm3.models.Category;
import com.example.asm3.models.Customer;
import com.google.android.material.navigation.NavigationBarView;
import com.example.asm3.models.Notification;
import com.example.asm3.models.SubCategory;

import java.util.ArrayList;


public class MainActivityController extends BaseController implements AsyncTaskCallBack, NavigationBarView.OnItemSelectedListener {
    private boolean isAuth = false;
    private Customer customer;
    private String token;
    private LinearLayout linearLayout;
    private GetAuthenticatedData getAuthenticatedData;
    private ArrayList<Category> categories;
    private GetData getData;
    private ArrayList<SubCategory> subCategories;
    private ArrayList<Notification> notifications;

    private NavigationBarView menu;
    private HomeFragment homeFragment;
    private ProfileFragment profileFragment;

    public MainActivityController(Context context, FragmentActivity activity) {
        super(context, activity);

        homeFragment = new HomeFragment();
        profileFragment = new ProfileFragment();
        homeFragment.setController(this);

        categories = new ArrayList<Category>();
        subCategories = new ArrayList<SubCategory>();
        notifications = new ArrayList<Notification>();
        getData = new GetData(context, this);
        getAuthenticatedData = new GetAuthenticatedData(context, this);
    }

    // Render functions
    @Override
    public void onInit() {
        menu = getActivity().findViewById(R.id.menu);
        menu.setOnItemSelectedListener(this);
        if (!isAuth()) {
//            setLoginLayout();
        } else {
            token = getToken();
            getAuthenticatedData.setEndPoint(Constant.getCustomerData);
            getAuthenticatedData.setToken(token);

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

            TextView username = new TextView(getContext());
            username.setLayoutParams(params);
            username.setText(customer.getUsername());
            username.setTextSize(17);


            linearLayout.addView(username);
        }
    }

    public void loadFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.mainActivity_frameLayout, fragment)
                .commit();
    }

    // Helpers


    // Request functions
    public void getAllCategories() {
        getData.setEndPoint(Constant.getAllCategories);
        getData.setTaskType(Constant.getAllCategoriesTaskType);
        getData.execute();
    }

    public void getSubCategories(String categoryName) {
        getData.setEndPoint(Constant.getSubCategories + Helper.getQueryEndpoint(Constant.categoryKey, categoryName));
        getData.setTaskType(Constant.getSubCategoriesTaskType);
        getData.execute();
    }

    public void getNotification() {
        getAuthenticatedData.setEndPoint(Constant.getNotifications);
        getAuthenticatedData.setTaskType(Constant.getNotificationsTaskType);
        getAuthenticatedData.setToken(token);
        getAuthenticatedData.execute();
    }

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
        } else if (taskType.equals(Constant.getAllCategoriesTaskType)) {
            ApiList<Category> apiList = ApiList.fromJSON(ApiList.getData(message), Category.class);
            categories = apiList.getList();
        } else if (taskType.equals(Constant.getSubCategoriesTaskType)) {
            ApiList<SubCategory> apiList = ApiList.fromJSON(ApiList.getData(message), SubCategory.class);
            subCategories = apiList.getList();
            Log.d(TAG, "onFinished: " + subCategories.size());
            for (int i = 0; i < subCategories.size(); i++) {
                Toast.makeText(getContext(), subCategories.get(i).getName(), Toast.LENGTH_SHORT).show();
            }
        } else if (taskType.equals(Constant.getNotificationsTaskType)) {
            ApiList<Notification> apiList = ApiList.fromJSON(ApiList.getData(message), Notification.class);
            notifications = apiList.getList();
        }
    }
}
