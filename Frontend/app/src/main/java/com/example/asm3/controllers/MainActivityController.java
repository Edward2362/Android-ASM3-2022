package com.example.asm3.controllers;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
        MaterialButtonToggleGroup.OnButtonCheckedListener,
        SearchView.OnQueryTextListener {

    // API
    private GetData getData;
    private GetAuthenticatedData getAuthenticatedData;

    // main activity view
    private TopBarView topBar;
    private NavigationBarView menu;
    private int selectedItemId;

    // homepage fragment view
    private MaterialButtonToggleGroup categoriesBtnGrp;
    private TextView cateNotifyTxt, helloTxt;
    private Button postBookBtn, findBookBtn;
    private View subCateTopDivider, subCateBotDivider;

    // search fragment view
    private LinearProgressIndicator progressBar;
    private GenericAdapter<String> searchAdapter;


    // notification fragment view


    // profile fragment view


    // main activity data
    private boolean isAuth = false;
    private Customer customer;
    private String token;

    private ArrayList<Category> categories;
    private ArrayList<SubCategory> subCategories;
    private ArrayList<Notification> notifications;
    private ArrayList<Order> orders;
    private ArrayList<String> searchSuggestions;

    private ArrayList<SubCategory> foreign = new ArrayList<>();
    private ArrayList<SubCategory> domestic = new ArrayList<>();
    private ArrayList<SubCategory> text = new ArrayList<>();
    private ArrayList<SubCategory> displayList = new ArrayList<>();

    private FragmentManager fragmentManager;
    private HomeFragment homeFragment;
    private SearchFragment searchFragment;
    private NotificationFragment notificationFragment;
    private ProfileFragment profileFragment;

    private RecyclerView subCateRecView;
    private RecyclerView searchSuggestionRecView;
    private GenericAdapter<SubCategory> subCateAdapter;

    private long lastTextEdit = 0;
    private Handler handler = new Handler();

    public MainActivityController(Context context, FragmentActivity activity) {
        super(context, activity);

        homeFragment = new HomeFragment();
        searchFragment = new SearchFragment();
        notificationFragment = new NotificationFragment();
        profileFragment = new ProfileFragment();

        homeFragment.setController(this);
        searchFragment.setController(this);
        notificationFragment.setController(this);
        profileFragment.setController(this);

        categories = new ArrayList<Category>();
        subCategories = new ArrayList<SubCategory>();
        searchSuggestions = new ArrayList<>();
        notifications = new ArrayList<Notification>();
        orders = new ArrayList<Order>();
        getData = new GetData(context, this);
        getAuthenticatedData = new GetAuthenticatedData(context, this);
    }

    // Render activity functions
    @Override
    public void onInit() {
        fragmentManager = getActivity().getSupportFragmentManager();

        topBar = getActivity().findViewById(R.id.topBar);
        topBar.setMainPage("GoGoat");

        menu = getActivity().findViewById(R.id.menu);
        menu.setOnItemSelectedListener(this);
        menu.setOnItemReselectedListener(this);

        // For testing

        foreign.add(new SubCategory("hello", "test"));
        foreign.add(new SubCategory("hi", "test"));
        foreign.add(new SubCategory("asd", "test"));
        foreign.add(new SubCategory("test", "test"));

        domestic.add(new SubCategory("domestic", "test"));
        domestic.add(new SubCategory("domestic", "test"));
        domestic.add(new SubCategory("domestic", "test"));
        domestic.add(new SubCategory("domestic", "test"));
        domestic.add(new SubCategory("domestic", "test"));
        domestic.add(new SubCategory("domestic", "test"));
        domestic.add(new SubCategory("domestic", "test"));

        text.add(new SubCategory("text hehe", "test"));
        text.add(new SubCategory("text hehe", "test"));
        text.add(new SubCategory("text hehe", "test"));
        text.add(new SubCategory("text hehe", "test"));
        text.add(new SubCategory("text hehe", "test"));
        // End for testing

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
    public void onInitHomeFragment(View view) {
        foreign = categories.get(0).getSubCategories();
        domestic = categories.get(1).getSubCategories();
        text = categories.get(2).getSubCategories();

        helloTxt = view.findViewById(R.id.helloTxt);
        postBookBtn = view.findViewById(R.id.postBookBtn);
        findBookBtn = view.findViewById(R.id.findBookBtn);
        categoriesBtnGrp = view.findViewById(R.id.categoriesBtnGrp);
        subCateRecView = view.findViewById(R.id.subCateRecView);
        cateNotifyTxt = view.findViewById(R.id.cateNotifyTxt);
        subCateTopDivider = view.findViewById(R.id.subCateTopDivider);
        subCateBotDivider = view.findViewById(R.id.subCateBotDivider);
        subCateAdapter = generateSubCateAdapter();

        categoriesBtnGrp.addOnButtonCheckedListener(this);
        subCateRecView.setAdapter(subCateAdapter);
        subCateRecView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void onInitSearchFragment(View view) {
        topBar.setSearchPage();
        searchSuggestionRecView = view.findViewById(R.id.searchSuggestionRecView);
        progressBar = view.findViewById(R.id.progressBar);
        searchSuggestionRecView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        topBar.getSearchView().setOnQueryTextListener(this);
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
    public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
        subCateTopDivider.setVisibility(View.VISIBLE);
        cateNotifyTxt.setVisibility(View.GONE);
        subCateRecView.setVisibility(View.VISIBLE);
        findBookBtn.setVisibility(View.VISIBLE);
        subCateBotDivider.setVisibility(View.VISIBLE);
        switch (group.getCheckedButtonId()) {
            case R.id.foreignCateBtn:
                displayList.clear();
                displayList.addAll(foreign);
                subCateAdapter.notifyDataSetChanged();
                break;
            case R.id.domesticCateBtn:
                displayList.clear();
                displayList.addAll(domestic);
                subCateAdapter.notifyDataSetChanged();
                break;
            case R.id.textCateBtn:
                displayList.clear();
                displayList.addAll(text);
                subCateAdapter.notifyDataSetChanged();
                break;
        }
        if (group.getCheckedButtonId() == -1) {
            subCateTopDivider.setVisibility(View.GONE);
            cateNotifyTxt.setVisibility(View.VISIBLE);
            subCateRecView.setVisibility(View.GONE);
            findBookBtn.setVisibility(View.GONE);
            subCateBotDivider.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSubCateClick(int position, View view, MaterialCheckBox subCateCheckBox) {
        boolean newStatus = false;
        switch (view.getId()) {
            case R.id.subCateBody:
                newStatus = !subCateCheckBox.isChecked();
                subCateCheckBox.setChecked(newStatus);
                break;
            case R.id.subCateCheckBox:
                newStatus = subCateCheckBox.isChecked();
                break;
        }
        displayList.get(position).setChosen(newStatus);
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
    private GenericAdapter<SubCategory> generateSubCateAdapter() {
        return new GenericAdapter<SubCategory>(displayList) {
            @Override
            public RecyclerView.ViewHolder setViewHolder(ViewGroup parent) {
                final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_category_item, parent, false);
                return new SubCategoryHolder(view, MainActivityController.this);
            }

            @Override
            public void onBindData(RecyclerView.ViewHolder holder, SubCategory item) {
                SubCategoryHolder subCategoryHolder = (SubCategoryHolder) holder;
                subCategoryHolder.getSubCateTxt().setText(item.getName());
                subCategoryHolder.getSubCateCheckBox().setChecked(item.isChosen());
            }
        };
    }

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

    // Navigation functions
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.homeNav:
                loadFragment(homeFragment, "home");
                return true;
            case R.id.searchNav:
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
        if (taskType.equals(Constant.getCustomer)) {
            ApiData<Customer> apiData = ApiData.fromJSON(ApiData.getData(message), Customer.class);
            customer = apiData.getData();
        } else if (taskType.equals(Constant.getAllCategoriesTaskType)) {
            ApiList<Category> apiList = ApiList.fromJSON(ApiList.getData(message), Category.class);
            categories = apiList.getList();
            Toast.makeText(getContext(), categories.get(0).getName(), Toast.LENGTH_SHORT).show();
            loadFragment(homeFragment, "home");
            for (SubCategory category : categories.get(0).getSubCategories()
            ) {
                Log.d(TAG, "onFinished: test " + category.getName());
            }
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

    // Getter and Setter
    public HomeFragment getHomeFragment() {
        return homeFragment;
    }

    public int getSelectedItemId() {
        return selectedItemId;
    }

    public TopBarView getTopBar() {
        return topBar;
    }

    public void setSelectedItemId(int selectedItemId) {
        this.selectedItemId = selectedItemId;
    }
}
