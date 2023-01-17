package com.example.asm3.controllers;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.asm3.AuthenticationActivity;
import com.example.asm3.R;
import com.example.asm3.base.controller.BaseController;
import com.example.asm3.base.networking.services.AsyncTaskCallBack;
import com.example.asm3.base.networking.services.GetAuthenticatedData;
import com.example.asm3.base.networking.services.GetData;
import com.example.asm3.config.Constant;
import com.example.asm3.config.Helper;
import com.example.asm3.custom.components.TopBarView;
import com.example.asm3.fragments.authenticationActivity.RegisterFragment;
import com.example.asm3.fragments.mainActivity.HomeFragment;
import com.example.asm3.fragments.mainActivity.MainViewModel;
import com.example.asm3.fragments.mainActivity.NotificationFragment;
import com.example.asm3.fragments.mainActivity.ProfileFragment;
import com.example.asm3.fragments.mainActivity.SearchFragment;
import com.example.asm3.models.ApiData;
import com.example.asm3.models.ApiList;
import com.example.asm3.models.Category;
import com.example.asm3.models.Customer;
import com.example.asm3.models.Notification;
import com.example.asm3.models.Order;
import com.example.asm3.models.SubCategory;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivityController extends BaseController implements
        AsyncTaskCallBack,
        NavigationBarView.OnItemSelectedListener,
        NavigationBarView.OnItemReselectedListener {

    // API
    private GetData getData;
    private GetAuthenticatedData getAuthenticatedData;

    // main activity view
    private TopBarView topBar;
    private NavigationBarView menu;
    private LiveData<Integer> selectedItemId;
    private FragmentManager fragmentManager;
    private HomeFragment homeFragment;
    private SearchFragment searchFragment;
    private NotificationFragment notificationFragment;
    private ProfileFragment profileFragment;
    private int mainLayoutId = R.id.mainActivity_fragmentContainerView;

    // main activity data
    private MainViewModel mainViewModel;
    private boolean isAuth = false;
    private Customer customer;
    private String token;
    private ArrayList<Order> orders;

    // homepage fragment data
    private ArrayList<SubCategory> subCategories;

    // search fragment data

    // notification fragment data
    private ArrayList<Notification> notifications;

    // profile fragment data

    public MainActivityController(Context context, FragmentActivity activity) {
        super(context, activity);
        mainViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        mainViewModel.setTopBarView(getActivity().findViewById(R.id.mainTopBar));
        selectedItemId = mainViewModel.getSelectedItemId();
        topBar = mainViewModel.getTopBarView().getValue();

        homeFragment = new HomeFragment();
        searchFragment = new SearchFragment();
        notificationFragment = new NotificationFragment();
        profileFragment = new ProfileFragment();

        notifications = new ArrayList<Notification>();
        orders = new ArrayList<Order>();
    }

    // Render functions
    @Override
    public void onInit() {
        fragmentManager = getActivity().getSupportFragmentManager();
        topBar.setMainPage("GoGoat");
        menu = getActivity().findViewById(R.id.menu);
        menu.setOnItemSelectedListener(this);
        menu.setOnItemReselectedListener(this);

        if (isAuth()) {
            token = getToken();
            getAuthenticatedData = new GetAuthenticatedData(getContext(), this);
            getAuthenticatedData.setEndPoint(Constant.getCustomerData);
            getAuthenticatedData.setToken(token);

            getAuthenticatedData.setTaskType(Constant.getCustomer);
            getAuthenticatedData.execute();
        }
        getAllCategories();
    }

    public void onResume() {
        if (getFragmentManager().findFragmentByTag("home") instanceof HomeFragment) {
            loadMenu();
        }
    }

    public void loadMenu() {
        menu.getMenu().getItem(0).setChecked(true);
    }

    // Helpers

    // Request functions
    public void getAllCategories() {
        Log.d(TAG, "getAllCategories: test " + homeFragment);
        getData = new GetData(getContext(), this);
        getData.setEndPoint(Constant.getAllCategories);
        getData.setTaskType(Constant.getAllCategoriesTaskType);
        getData.execute();
    }

    public void getSubCategories(String categoryName) {
        getData = new GetData(getContext(), this);
        getData.setEndPoint(Constant.getSubCategories + Helper.getQueryEndpoint(Constant.categoryKey, categoryName));
        getData.setTaskType(Constant.getSubCategoriesTaskType);
        getData.execute();
    }

    public void getNotification() {
        getAuthenticatedData = new GetAuthenticatedData(getContext(), this);
        getAuthenticatedData.setEndPoint(Constant.getNotifications);
        getAuthenticatedData.setTaskType(Constant.getNotificationsTaskType);
        getAuthenticatedData.setToken(token);
        getAuthenticatedData.execute();
    }

    // Navigation functions
    public void goToLogin() {
        Intent intent = new Intent(getContext(), AuthenticationActivity.class);
        getActivity().startActivityForResult(intent, Constant.authActivityCode);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.homeNav:
                topBar.setMainPage("GoGoat");
                Helper.loadFragment(fragmentManager, homeFragment, "home", mainLayoutId);
                return true;
            case R.id.searchNav:
                topBar.setSearchPage();
                Helper.loadFragment(fragmentManager, searchFragment, "search", mainLayoutId);
                return true;
            case R.id.notiNav:
                topBar.setMainPage("Notification");
                Helper.loadFragment(fragmentManager, notificationFragment, "notification", mainLayoutId);
                return true;
            case R.id.profileNav:
                if (!isAuth()) {
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            goToLogin();
                        }
                    }, 250);
                } else {
                    topBar.setMainPage("Profile");
                    Helper.loadFragment(fragmentManager, profileFragment, "profile", mainLayoutId);
                }
                return true;
        }
        return false;
    }

    @Override
    public void onNavigationItemReselected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.homeNav:
                fragmentManager.beginTransaction().detach(homeFragment).attach(homeFragment).commit();
                break;
            case R.id.searchNav:
                fragmentManager.beginTransaction().detach(searchFragment).attach(searchFragment).commit();
                break;
            case R.id.notiNav:
                fragmentManager.beginTransaction().detach(notificationFragment).attach(notificationFragment).commit();
                break;
            case R.id.profileNav:
                fragmentManager.beginTransaction().detach(profileFragment).attach(profileFragment).commit();
                break;
        }
    }

    // Callback functions
    public void onActivityFinished(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constant.authActivityCode) {
                if (data.getExtras().getSerializable(Constant.customerKey) != null) {
                    customer = (Customer) data.getExtras().getSerializable(Constant.customerKey);
                    mainViewModel.setAuthCustomer(customer);
                }
            }
        }
    }

    @Override
    public void onFinished(String message, String taskType) {
        if (taskType.equals(Constant.getCustomer)) {
            ApiData<Customer> apiData = ApiData.fromJSON(ApiData.getData(message), Customer.class);
            customer = apiData.getData();
            mainViewModel.setAuthCustomer(customer);
        } else if (taskType.equals(Constant.getAllCategoriesTaskType)) {
            ApiList<Category> apiList = ApiList.fromJSON(ApiList.getData(message), Category.class);
//            categories = apiList.getList();
            mainViewModel.setCateArray(apiList.getList());
//            Toast.makeText(getContext(), categories.getValue().get(0).getName(), Toast.LENGTH_SHORT).show();
//            Log.d(TAG, "onFinished: test " + mainViewModel.getCateArray().getValue());
        } else if (taskType.equals(Constant.getSubCategoriesTaskType)) {
            ApiList<SubCategory> apiList = ApiList.fromJSON(ApiList.getData(message), SubCategory.class);
            subCategories = apiList.getList();
            Log.d(TAG, "onFinished: " + subCategories.size());
        } else if (taskType.equals(Constant.getNotificationsTaskType)) {
            ApiList<Notification> apiList = ApiList.fromJSON(ApiList.getData(message), Notification.class);
            notifications = apiList.getList();
        }
    }

    @Override
    public void onError(String taskType) {
        if (taskType.equals(Constant.getCustomer)) {

        }
    }

    // Getter and Setter
    public HomeFragment getHomeFragment() {
        return homeFragment;
    }

    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }

    public int getMainLayoutId() {
        return mainLayoutId;
    }

    public int getSelectedItemId() {
        return mainViewModel.getSelectedItemId().getValue();
    }

    public TopBarView getTopBar() {
        return topBar;
    }
}
