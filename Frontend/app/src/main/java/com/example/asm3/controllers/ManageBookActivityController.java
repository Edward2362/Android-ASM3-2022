package com.example.asm3.controllers;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm3.ManageBookActivity;
import com.example.asm3.R;
import com.example.asm3.base.adapter.GenericAdapter;
import com.example.asm3.base.adapter.viewHolder.SubCategoryHolder;
import com.example.asm3.base.controller.BaseController;
import com.example.asm3.base.localStorage.LocalFileController;
import com.example.asm3.base.networking.services.AsyncTaskCallBack;
import com.example.asm3.base.networking.services.DeleteAuthenticatedData;
import com.example.asm3.base.networking.services.GetData;
import com.example.asm3.base.networking.services.PostAuthenticatedData;
import com.example.asm3.config.Constant;
import com.example.asm3.models.ApiList;
import com.example.asm3.models.Book;
import com.example.asm3.models.Category;
import com.example.asm3.models.SubCategory;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.ArrayList;

public class ManageBookActivityController extends BaseController implements
        AsyncTaskCallBack,
        MaterialButtonToggleGroup.OnButtonCheckedListener,
        SubCategoryHolder.OnSelectListener {

    private Book book;
    private PostAuthenticatedData postAuthenticatedData;
    private DeleteAuthenticatedData deleteAuthenticatedData;
    private GetData getData;
    private ArrayList<Category> categories;
    private ArrayList<SubCategory> foreign = new ArrayList<>();
    private ArrayList<SubCategory> domestic = new ArrayList<>();
    private ArrayList<SubCategory> text = new ArrayList<>();
    private ArrayList<SubCategory> displayList = new ArrayList<>();

    private MaterialButtonToggleGroup categoriesBtnGrp;
    private TextView cateNotifyTxt;
    private View subCateTopDivider;
    private GenericAdapter<SubCategory> subCateAdapter;
    private RecyclerView subCateRecView;

    private String token;

    public ManageBookActivityController(Context context, FragmentActivity activity) {
        super(context, activity);
    }

    @Override
    public void onInit(){

        if (!isAuth()) {

        } else {
            token = getToken();
            getAllCategories();

            categoriesBtnGrp = getActivity().findViewById(R.id.manageProductCategoriesBtnGrp);
            subCateRecView = getActivity().findViewById(R.id.manageProductSubCateRecView);
            cateNotifyTxt = getActivity().findViewById(R.id.manageProductCateNotifyTxt);
            subCateTopDivider = getActivity().findViewById(R.id.manageProductSubCateTopDivider);
            subCateAdapter = generateSubCateAdapter();

            categoriesBtnGrp.addOnButtonCheckedListener(this);
            subCateRecView.setAdapter(subCateAdapter);
            subCateRecView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
    }

    private GenericAdapter<SubCategory> generateSubCateAdapter() {
        return new GenericAdapter<SubCategory>(displayList) {
            @Override
            public RecyclerView.ViewHolder setViewHolder(ViewGroup parent) {
                final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_category_item, parent, false);
                return new SubCategoryHolder(view, ManageBookActivityController.this);
            }

            @Override
            public void onBindData(RecyclerView.ViewHolder holder, SubCategory item) {
                SubCategoryHolder subCategoryHolder = (SubCategoryHolder) holder;
                subCategoryHolder.getSubCateTxt().setText(item.getName());
                subCategoryHolder.getSubCateCheckBox().setChecked(item.isChosen());
            }
        };
    }

    public boolean isUpload() {
        Intent intent = getActivity().getIntent();
        if (intent.getExtras().get(Constant.isUploadKey) != null) {
            if ((Integer.parseInt(intent.getExtras().get(Constant.isUploadKey).toString()) == Constant.uploadCode)){
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void uploadBook(Book inputBook){
        postAuthenticatedData = new PostAuthenticatedData(getContext(), this);
        postAuthenticatedData.setEndPoint(Constant.uploadBook);
        postAuthenticatedData.setTaskType(Constant.uploadBookTaskType);
        postAuthenticatedData.setToken(token);
        postAuthenticatedData.execute(Book.toJSON(inputBook));
    }

    public void updateBook(Book inputBook) {
        postAuthenticatedData = new PostAuthenticatedData(getContext(), this);
        postAuthenticatedData.setEndPoint(Constant.updateBook);
        postAuthenticatedData.setTaskType(Constant.updateBookTaskType);
        postAuthenticatedData.setToken(token);
        postAuthenticatedData.execute(Book.toJSON(inputBook));
    }

    public void deleteBook(String bookId) {
        deleteAuthenticatedData = new DeleteAuthenticatedData(getContext(), this);
        deleteAuthenticatedData.setEndPoint(Constant.deleteBook + "/" + bookId);
        deleteAuthenticatedData.setTaskType(Constant.deleteBookTaskType);
        deleteAuthenticatedData.setToken(token);
        deleteAuthenticatedData.execute();
    }

    public void getAllCategories() {
        getData = new GetData(getContext(), this);
        getData.setEndPoint(Constant.getAllCategories);
        getData.setTaskType(Constant.getAllCategoriesTaskType);
        getData.execute();
    }

    @Override
    public void onFinished(String message,String taskType){
        if (taskType.equals(Constant.uploadBookTaskType)){

        } else if (taskType.equals(Constant.getAllCategoriesTaskType)) {
            ApiList<Category> apiList = ApiList.fromJSON(ApiList.getData(message),Category.class);
            categories = apiList.getList();
            foreign = categories.get(0).getSubCategories();
            domestic = categories.get(1).getSubCategories();
            text = categories.get(2).getSubCategories();
        }
    }

    @Override
    public void onError(String taskType) {

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
    public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
        subCateTopDivider.setVisibility(View.VISIBLE);
        cateNotifyTxt.setVisibility(View.GONE);
        subCateRecView.setVisibility(View.VISIBLE);
        switch (group.getCheckedButtonId()) {
            case R.id.manageProductForeignCateBtn:
                displayList.clear();
                displayList.addAll(foreign);
                subCateAdapter.notifyDataSetChanged();
                break;
            case R.id.manageProductDomesticCateBtn:
                displayList.clear();
                displayList.addAll(domestic);
                subCateAdapter.notifyDataSetChanged();
                break;
            case R.id.manageProductTextCateBtn:
                displayList.clear();
                displayList.addAll(text);
                subCateAdapter.notifyDataSetChanged();
                break;
        }
        if (group.getCheckedButtonId() == -1) {
            subCateTopDivider.setVisibility(View.GONE);
            cateNotifyTxt.setVisibility(View.VISIBLE);
            subCateRecView.setVisibility(View.GONE);
        }
    }
}
