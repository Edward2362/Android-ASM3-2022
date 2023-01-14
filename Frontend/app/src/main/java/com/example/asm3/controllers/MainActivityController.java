package com.example.asm3.controllers;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm3.AuthenticationActivity;
import com.example.asm3.R;

import com.example.asm3.base.adapter.GenericAdapter;
import com.example.asm3.base.adapter.OnSelectListener;
import com.example.asm3.base.adapter.viewHolder.SearchSuggestionHolder;
import com.example.asm3.base.adapter.viewHolder.SubCategoryHolder;
import com.example.asm3.base.controller.BaseController;
import com.example.asm3.base.networking.services.AsyncTaskCallBack;
import com.example.asm3.base.networking.services.GetAuthenticatedData;
import com.example.asm3.base.networking.services.GetData;
import com.example.asm3.config.Constant;
import com.example.asm3.custom.components.TopBarView;
import com.example.asm3.fragments.mainActivity.HomeFragment;
import com.example.asm3.fragments.mainActivity.MainViewModel;
import com.example.asm3.fragments.mainActivity.NotificationFragment;
import com.example.asm3.fragments.mainActivity.ProfileFragment;
import com.example.asm3.config.Helper;
import com.example.asm3.fragments.mainActivity.SearchFragment;
import com.example.asm3.models.ApiData;
import com.example.asm3.models.ApiList;
import com.example.asm3.models.Category;
import com.example.asm3.models.Customer;
import com.example.asm3.models.Order;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.navigation.NavigationBarView;
import com.example.asm3.models.Notification;
import com.example.asm3.models.SubCategory;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.ArrayList;


public class MainActivityController extends BaseController implements
        AsyncTaskCallBack,
        NavigationBarView.OnItemSelectedListener,
        NavigationBarView.OnItemReselectedListener,
        OnSelectListener,
        SearchView.OnQueryTextListener {

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

    // homepage fragment view

    // search fragment view

    // notification fragment view


    // profile fragment view


    // main activity data
    private boolean isAuth = false;
    private Customer customer;
    private String token;
    private ArrayList<Order> orders;

    // homepage fragment data
//    private LiveData<ArrayList<Category>> categories;
    private ArrayList<SubCategory> subCategories;
//    private ArrayList<SubCategory> foreign = new ArrayList<>();
//    private ArrayList<SubCategory> domestic = new ArrayList<>();
//    private ArrayList<SubCategory> text = new ArrayList<>();
//    private ArrayList<SubCategory> displayList = new ArrayList<>();

    // search fragment data


    // notification fragment data
    private ArrayList<Notification> notifications;

    // profile fragment data

    // test
    private MainViewModel mainViewModel;

    public MainActivityController(Context context, FragmentActivity activity) {
        super(context, activity);
        mainViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        selectedItemId = mainViewModel.getSelectedItemId();

        homeFragment = new HomeFragment();
        searchFragment = new SearchFragment();
        notificationFragment = new NotificationFragment();
        profileFragment = new ProfileFragment();

        notificationFragment.setController(this);
        profileFragment.setController(this);

        notifications = new ArrayList<Notification>();
        orders = new ArrayList<Order>();
        getData = new GetData(context, this);
        getAuthenticatedData = new GetAuthenticatedData(context, this);
    }

    // Render functions
    @Override
    public void onInit() {
        fragmentManager = getActivity().getSupportFragmentManager();
        topBar = getActivity().findViewById(R.id.topBar);
        topBar.setMainPage("GoGoat");
        menu = getActivity().findViewById(R.id.menu);
        menu.setOnItemSelectedListener(this);
        menu.setOnItemReselectedListener(this);

        if (!isAuth()) {
//            setLoginLayout();
        } else {
            token = getToken();
            getAuthenticatedData.setEndPoint(Constant.getCustomerData);
            getAuthenticatedData.setToken(token);

            getAuthenticatedData.setTaskType(Constant.getCustomer);
            getAuthenticatedData.execute();
        }
        getAllCategories();
    }

    public void setLoginLayout() {

    }

    public void setCustomerDataLayout() {

    }

    public void loadFragment(Fragment fragment, String tag) {
        fragmentManager.beginTransaction()
                .replace(R.id.mainActivity_frameLayout, fragment, tag)
                .setReorderingAllowed(true)
                .commit();
    }

    public void loadMenu() {
        menu.getMenu().getItem(0).setChecked(true);
    }

    // Render fragment functions

    public void onInitSearchFragment(View view) {
//        topBar.setSearchPage();
        searchSuggestionRecView = view.findViewById(R.id.searchSuggestionRecView);
        progressBar = view.findViewById(R.id.progressBar);
        searchSuggestionRecView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
//        topBar.getSearchView().setOnQueryTextListener(this);
        // for test
        searchSuggestions.add("lord of the rings");
        searchSuggestions.add("lord of the rings j. r. r. tolkien");
        searchSuggestions.add("lord of the rings fellowship of the rings");
        searchSuggestions.add("lord of the rings the return of the king");
        searchSuggestions.add("lord of the rings the two towers");
        searchSuggestions.add("lord of the rings set");
        // oki no more testing
        searchAdapter = generateSearchAdapter();
        searchSuggestionRecView.setAdapter(searchAdapter);
        searchSuggestionRecView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onSearchSuggestionClick(int position, View view, String suggestionText) {
        // TODO: get text and go to search result
        Log.d(TAG, "onSearchSuggestionClick: clicked!");
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        //TODO: go to search result activity
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.d(TAG, "onQueryTextChange: text out " + newText);
        if (!newText.isEmpty()) {
            Log.d(TAG, "onQueryTextChange: text " + newText);
            progressBar.setVisibility(View.VISIBLE);
            searchSuggestionRecView.setVisibility(View.GONE);
            lastTextEdit = System.currentTimeMillis();
            // TODO: implement function fetching
//            getSuggestions();
            handler.removeCallbacksAndMessages(null);
            do {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // TODO: put these 2 lines in onFinished, a fetching function will be called here
                        progressBar.setVisibility(View.GONE);
                        searchSuggestionRecView.setVisibility(View.VISIBLE);
                    }
                }, 1000);
            } while (searchSuggestions.isEmpty());
        } else {
            progressBar.setVisibility(View.GONE);
            searchSuggestionRecView.setVisibility(View.GONE);
        }
        return false;
    }

    // Helpers
    private GenericAdapter<String> generateSearchAdapter() {
        return new GenericAdapter<String>(searchSuggestions) {
            @Override
            public RecyclerView.ViewHolder setViewHolder(ViewGroup parent) {
                final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_suggestion_item, parent, false);
                return new SearchSuggestionHolder(view, MainActivityController.this);
            }

            @Override
            public void onBindData(RecyclerView.ViewHolder holder, String suggest) {
                SearchSuggestionHolder searchSuggestionHolder = (SearchSuggestionHolder) holder;
                searchSuggestionHolder.getSuggestionText().setText(suggest);
            }
        };
    }// end generateSearchAdapter

    // Request functions
    public void getAllCategories() {
        Log.d(TAG, "getAllCategories: test " + homeFragment);
        getData.setEndPoint(Constant.getAllCategories);
        getData.setTaskType(Constant.getAllCategoriesTaskType);
        getData.execute();
//        Log.d(TAG, "getAllCategories: test " + getData.getStatus().name());
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

    // Navigation functions
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.homeNav:
                loadFragment(homeFragment, "home");
                return true;
            case R.id.searchNav:
                topBar.setSearchPage();
                topBar.getSearchView().setOnQueryTextListener(this);
                loadFragment(searchFragment, "search");
                return true;
            case R.id.notiNav:
                loadFragment(notificationFragment, "notification");
                return true;
            case R.id.profileNav:
                loadFragment(profileFragment, "profile");
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
                }
            }
        }
    }

    @Override
    public void onFinished(String message, String taskType) {
        Log.d(TAG, "onFinished: test " + homeFragment);
        if (taskType.equals(Constant.getCustomer)) {
            ApiData<Customer> apiData = ApiData.fromJSON(ApiData.getData(message), Customer.class);
            customer = apiData.getData();
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

    // Getter and Setter
    public HomeFragment getHomeFragment() {
        return homeFragment;
    }

    public int getSelectedItemId() {
        return mainViewModel.getSelectedItemId().getValue();
    }

    public TopBarView getTopBar() {
        return topBar;
    }
}
