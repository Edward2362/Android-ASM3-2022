package com.example.asm3.controllers;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
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
import com.example.asm3.fragments.mainActivity.HomeFragment;
import com.example.asm3.fragments.mainActivity.MainViewModel;
import com.example.asm3.fragments.mainActivity.NotificationFragment;
import com.example.asm3.fragments.mainActivity.ProfileFragment;
import com.example.asm3.fragments.mainActivity.SearchFragment;
import com.example.asm3.models.ApiData;
import com.example.asm3.models.ApiList;
import com.example.asm3.models.Book;
import com.example.asm3.models.Category;
import com.example.asm3.models.Customer;
import com.example.asm3.models.Notification;
import com.example.asm3.models.Order;
import com.example.asm3.models.Review;
import com.example.asm3.models.SubCategory;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivityController extends BaseController implements
        AsyncTaskCallBack,
        NavigationBarView.OnItemSelectedListener,
        NavigationBarView.OnItemReselectedListener,
        View.OnClickListener {

    // API
    private GetData getData;
    private GetAuthenticatedData getAuthenticatedData;

    // main activity view
    private TopBarView topBar;
    private NavigationBarView menu;
    private FragmentManager fragmentManager;
    private HomeFragment homeFragment;
    private SearchFragment searchFragment;
    private NotificationFragment notificationFragment;
    private ProfileFragment profileFragment;
    private int mainLayoutId = R.id.mainActivity_fragmentContainerView;
    private Button cartBtn;


    // main activity data
    private MainViewModel mainViewModel;
    private boolean isAuth = false;
    private Customer customer;
    private String token;
    private LiveData<Integer> selectedItemId;
    private LiveData<ArrayList<Notification>> notifications;
    private LiveData<ArrayList<Book>> books;
    private LiveData<ArrayList<Review>> reviews;
    private LiveData<ArrayList<Order>> orders;

    // homepage fragment data
    private ArrayList<SubCategory> subCategories;

    public MainActivityController(Context context, FragmentActivity activity) {
        super(context, activity);
        mainViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        mainViewModel.setTopBarView(getActivity().findViewById(R.id.mainTopBar));
        mainViewModel.setMenu(getActivity().findViewById(R.id.menu));
        selectedItemId = mainViewModel.getSelectedItemId();
        notifications = mainViewModel.getNotifications();
        books = mainViewModel.getBooks();
        reviews = mainViewModel.getReviews();
        orders = mainViewModel.getOrders();
        topBar = mainViewModel.getTopBarView().getValue();
        menu = mainViewModel.getMenu().getValue();
        fragmentManager = getActivity().getSupportFragmentManager();
        topBar.setMainPage("GoGoat");

        homeFragment = new HomeFragment();
        searchFragment = new SearchFragment();
        notificationFragment = new NotificationFragment();
        profileFragment = new ProfileFragment();

        getData = new GetData(context, this);
        getAuthenticatedData = new GetAuthenticatedData(context, this);
    }

    // Render functions
    @Override
    public void onInit() {
        cartBtn = topBar.getCartButton();
        cartBtn.setOnClickListener(this);
        menu.setOnItemSelectedListener(this);
        menu.setOnItemReselectedListener(this);

        notifications.observe(getActivity(), new Observer<ArrayList<Notification>>() {
            @Override
            public void onChanged(ArrayList<Notification> notifications) {
                Helper.setBadge(notifications, mainViewModel);
            }
        });
        if (isAuth()) {
            Log.d(TAG, "onInit: test customer");
            getAuthCustomer();
        }
        getAllCategories();
    }

    public void onResume() {
        if (getFragmentManager().findFragmentByTag("home") instanceof HomeFragment) {
            loadMenu();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cartButton:
                if (!isAuth()) {
                    Helper.goToLogin(getContext(), getActivity());
                } else {
                    Helper.goToCart(getContext(), getActivity());
                }
                break;
        }
    }

    // Helper
    public void loadMenu() {
        menu.getMenu().getItem(0).setChecked(true);
    }

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

    public void getAuthCustomer() {
        token = getToken();
        getAuthenticatedData = new GetAuthenticatedData(getContext(), this);
        getAuthenticatedData.setEndPoint(Constant.getCustomerData);
        getAuthenticatedData.setToken(token);
        getAuthenticatedData.setTaskType(Constant.getCustomer);
        getAuthenticatedData.execute();
    }

    public void getCustomerProducts() {
        getAuthenticatedData = new GetAuthenticatedData(getContext(), this);
        getAuthenticatedData.setEndPoint(Constant.getUploadedProducts);
        getAuthenticatedData.setTaskType(Constant.getUploadedProductsTaskType);
        getAuthenticatedData.setToken(token);
        getAuthenticatedData.execute();
    }

    // Navigation functions
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
                if (!isAuth()) {
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Helper.goToLogin(getContext(), getActivity());
                        }
                    }, 230);
                } else {
                    topBar.setMainPage("Notification");
                    Helper.loadFragment(fragmentManager, notificationFragment, "notification", mainLayoutId);
                }
                return true;
            case R.id.profileNav:
                if (!isAuth()) {
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Helper.goToLogin(getContext(), getActivity());
                        }
                    }, 230);
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

            if (customer != null) {
                getCustomerProducts();
            }
        } else if (taskType.equals(Constant.getAllCategoriesTaskType)) {
            ApiList<Category> apiList = ApiList.fromJSON(ApiList.getData(message), Category.class);
//            categories = apiList.getList();
            mainViewModel.setCateArray(apiList.getList());
//            Toast.makeText(getContext(), categories.getValue().get(0).getName(), Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onFinished: test " + mainViewModel.getCateArray().getValue().get(0).getSubCategories());
        } else if (taskType.equals(Constant.getSubCategoriesTaskType)) {
            ApiList<SubCategory> apiList = ApiList.fromJSON(ApiList.getData(message), SubCategory.class);
            subCategories = apiList.getList();
            Log.d(TAG, "onFinished: " + subCategories.size());
        } else if (taskType.equals(Constant.getNotificationsTaskType)) {
            ApiList<Notification> apiList = ApiList.fromJSON(ApiList.getData(message), Notification.class);
            mainViewModel.setNotifications(apiList.getList());
        } else if (taskType.equals(Constant.getUploadedProductsTaskType)) {
            ApiList<Book> apiList = ApiList.fromJSON(ApiList.getData(message), Book.class);
            mainViewModel.setBooks(apiList.getList());
        }
    }

    @Override
    public void onError(String taskType) {
        if (taskType.equals(Constant.getCustomer)) {
            getLocalFileController().writeFile(new ArrayList<>());
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
