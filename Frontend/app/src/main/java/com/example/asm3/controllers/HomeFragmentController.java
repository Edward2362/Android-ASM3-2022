package com.example.asm3.controllers;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm3.ManageBookActivity;
import com.example.asm3.R;
import com.example.asm3.SearchResultActivity;
import com.example.asm3.base.adapter.GenericAdapter;
import com.example.asm3.base.adapter.viewHolder.SubCategoryHolder;
import com.example.asm3.base.controller.BaseController;
import com.example.asm3.config.Constant;
import com.example.asm3.config.Helper;
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
    private String selectedCategory;

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
                if (!categories.isEmpty()) {
                    foreign = categories.get(0).getSubCategories();
                    domestic = categories.get(1).getSubCategories();
                    text = categories.get(2).getSubCategories();
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

        // put this observe after find view by id

        categoriesBtnGrp.addOnButtonCheckedListener(this);
        subCateRecView.setAdapter(subCateAdapter);
        subCateRecView.setLayoutManager(new LinearLayoutManager(getContext()));

        postBookBtn.setOnClickListener(this);
        findBookBtn.setOnClickListener(this);
        loginNavBtn.setOnClickListener(this);
    }

    public void onResume() {
        authCustomer.observe(getActivity(), new Observer<Customer>() {
            @Override
            public void onChanged(Customer customer) {
                if (authCustomer.getValue() != null) {
                    helloTxt.setText("Hi " + customer.getUsername() + ", mahhh!");
                    loginNavBtn.setVisibility(View.GONE);
                    postBookBtn.setVisibility(View.VISIBLE);
                } else {
                    helloTxt.setText("Hello Stranger, mahhh!");
                    loginNavBtn.setVisibility(View.VISIBLE);
                    postBookBtn.setVisibility(View.GONE);
                }
            }
        });
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
                selectedCategory = "Foreign+Book";
                break;
            case R.id.domesticCateBtn:
                displayList.clear();
                displayList.addAll(domestic);
                subCateAdapter.notifyDataSetChanged();
                selectedCategory = "Domestic+Book";
                break;
            case R.id.textCateBtn:
                displayList.clear();
                displayList.addAll(text);
                subCateAdapter.notifyDataSetChanged();
                selectedCategory = "Text+Book";
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
            case R.id.addresses:
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
                subCategoryHolder.getSubCateTxt().setText(item.getName().replace("+", " "));
                subCategoryHolder.getSubCateCheckBox().setChecked(item.isChosen());
            }
        };
    }

    @Override
    public void onClick(View view) {
        if (!isOnline()) {
            showConnectDialog();
            return;
        }
        switch (view.getId()) {
            case R.id.loginNavBtn:
                Helper.goToLogin(getContext(), getActivity());
                break;
            case R.id.postBookBtn:
                goToManageBook();
                break;
            case R.id.findBookBtn:
                Intent intent = new Intent(getContext(), SearchResultActivity.class);
                ArrayList<String> subCategoryArrayList = new ArrayList<String>();
                for (int i = 0; i < displayList.size(); i++) {
                    if (displayList.get(i).isChosen()) {
                        subCategoryArrayList.add(displayList.get(i).getName());
                    }
                }
                intent.putExtra(Constant.categorySearchKey, selectedCategory);
                intent.putExtra(Constant.subCategorySearchKey, subCategoryArrayList);
                intent.putExtra(Constant.isCategorySearchKey, Constant.isCategorySearchCode);
                getActivity().startActivityForResult(intent, Constant.searchResultActivityCode);
                break;
        }
    }

    // Request functions


    // Navigation functions
    public void goToManageBook() {
        Intent intent = new Intent(getContext(), ManageBookActivity.class);
        intent.putExtra(Constant.isUploadKey, Constant.uploadCode);
        getActivity().startActivityForResult(intent, Constant.manageBookActivityCode);
    }

    // Callback functions


    // Getter and Setter
}
