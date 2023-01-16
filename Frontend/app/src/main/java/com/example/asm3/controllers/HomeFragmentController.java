package com.example.asm3.controllers;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm3.AuthenticationActivity;
import com.example.asm3.R;
import com.example.asm3.base.adapter.GenericAdapter;
import com.example.asm3.base.adapter.viewHolder.SubCategoryHolder;
import com.example.asm3.base.controller.BaseController;
import com.example.asm3.config.Constant;
import com.example.asm3.fragments.mainActivity.MainViewModel;
import com.example.asm3.models.Category;
import com.example.asm3.models.Customer;
import com.example.asm3.models.SubCategory;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.ArrayList;

public class HomeFragmentController extends BaseController implements
        MaterialButtonToggleGroup.OnButtonCheckedListener,
        SubCategoryHolder.OnSelectListener,
        View.OnClickListener {

    private MaterialButtonToggleGroup categoriesBtnGrp;
    private TextView cateNotifyTxt, helloTxt;
    private Button postBookBtn, findBookBtn, loginNavBtn;
    private View view, subCateTopDivider, subCateBotDivider;
    private GenericAdapter<SubCategory> subCateAdapter;
    private RecyclerView subCateRecView;

    private MainViewModel mainViewModel;
    private LiveData<ArrayList<Category>> categories;
    private LiveData<Customer> authCustomer;
    private ArrayList<SubCategory> subCategories;
    private ArrayList<SubCategory> foreign = new ArrayList<>();
    private ArrayList<SubCategory> domestic = new ArrayList<>();
    private ArrayList<SubCategory> text = new ArrayList<>();
    private ArrayList<SubCategory> displayList = new ArrayList<>();

    public HomeFragmentController(Context context, FragmentActivity activity, View view, ViewModel viewModel) {
        super(context, activity);
        this.view = view;
        this.mainViewModel = (MainViewModel) viewModel;

        categories = mainViewModel.getCateArray();
        authCustomer = mainViewModel.getAuthCustomer();
        subCategories = new ArrayList<SubCategory>();
    }

    // Render functions
    @Override
    public void onInit() {
        categories.observe(getActivity(), new Observer<ArrayList<Category>>() {
            @Override
            public void onChanged(ArrayList<Category> categories) {
                Log.d(TAG, "onChanged controller: test " + categories);
                foreign = categories.get(0).getSubCategories();
                domestic = categories.get(1).getSubCategories();
                text = categories.get(2).getSubCategories();
            }
        });

        authCustomer.observe(getActivity(), new Observer<Customer>() {
            @Override
            public void onChanged(Customer customer) {
                if (customer != null) {
                    helloTxt.setText("Hello " + customer.getUsername() + ", mahhh!");
                    loginNavBtn.setVisibility(View.GONE);
                    postBookBtn.setVisibility(View.VISIBLE);
                } else {
                    helloTxt.setText("Hello Stranger, mahhh!");
                    loginNavBtn.setVisibility(View.VISIBLE);
                    postBookBtn.setVisibility(View.GONE);
                }
            }
        });

        helloTxt = view.findViewById(R.id.helloTxt);
        postBookBtn = view.findViewById(R.id.postBookBtn);
        findBookBtn = view.findViewById(R.id.findBookBtn);
        loginNavBtn = view.findViewById(R.id.loginNavBtn);
        categoriesBtnGrp = view.findViewById(R.id.categoriesBtnGrp);
        subCateRecView = view.findViewById(R.id.subCateRecView);
        cateNotifyTxt = view.findViewById(R.id.cateNotifyTxt);
        subCateTopDivider = view.findViewById(R.id.subCateTopDivider);
        subCateBotDivider = view.findViewById(R.id.subCateBotDivider);
        subCateAdapter = generateSubCateAdapter();

        categoriesBtnGrp.addOnButtonCheckedListener(this);
        subCateRecView.setAdapter(subCateAdapter);
        subCateRecView.setLayoutManager(new LinearLayoutManager(getContext()));
        postBookBtn.setOnClickListener(this);
        findBookBtn.setOnClickListener(this);
        loginNavBtn.setOnClickListener(this);
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

    // Helpers
    private GenericAdapter<SubCategory> generateSubCateAdapter() {
        return new GenericAdapter<SubCategory>(displayList) {
            @Override
            public RecyclerView.ViewHolder setViewHolder(ViewGroup parent) {
                final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_category_item, parent, false);
                return new SubCategoryHolder(view, HomeFragmentController.this);
            }

            @Override
            public void onBindData(RecyclerView.ViewHolder holder, SubCategory item) {
                SubCategoryHolder subCategoryHolder = (SubCategoryHolder) holder;
                subCategoryHolder.getSubCateTxt().setText(item.getName());
                subCategoryHolder.getSubCateCheckBox().setChecked(item.isChosen());
            }
        };
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginNavBtn:
                goToLogin();
                break;
            case R.id.postBookBtn:
                break;
            case R.id.findBookBtn:
                break;
        }
    }

    // Request functions


    // Navigation functions
    public void goToLogin() {
        Intent intent = new Intent(getContext(), AuthenticationActivity.class);
        getActivity().startActivityForResult(intent, Constant.authActivityCode);
    }

    // Callback functions


    // Getter and Setter
}
