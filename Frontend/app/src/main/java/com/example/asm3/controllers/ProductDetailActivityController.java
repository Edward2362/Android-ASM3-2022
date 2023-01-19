package com.example.asm3.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.example.asm3.R;
import com.example.asm3.base.controller.BaseController;
import com.example.asm3.base.networking.services.AsyncTaskCallBack;
import com.example.asm3.base.networking.services.GetAuthenticatedData;
import com.example.asm3.base.networking.services.GetData;
import com.example.asm3.config.Constant;
import com.example.asm3.config.Helper;
import com.example.asm3.custom.components.TopBarView;
import com.example.asm3.models.ApiData;
import com.example.asm3.models.Book;
import com.example.asm3.models.Customer;

public class ProductDetailActivityController extends BaseController implements
        AsyncTaskCallBack,
        View.OnClickListener {
    private GetData getData;

    private TopBarView detailTopBar;
    private ImageView detailBookImg;
    private TextView detailBookYearTxt, detailBookNameTxt, detailBookPriceTxt, detailSellerTxt, detailSellerRatingTxt,detailBookConditionTxt, detailBookDescriptionTxt;
    private Button detailAddCartBtn, detailUpdateCartBtn;

    private Book book;
    private int bookId;
    private Intent intent;
    private String token;
    private Customer customer;
    private GetAuthenticatedData getAuthenticatedData;

    public ProductDetailActivityController(Context context, FragmentActivity activity) {
        super(context, activity);

//        bookId = intent.getExtras()
    }

    // Render functions
    @Override
    public void onInit() {
        detailTopBar = getActivity().findViewById(R.id.detailTopBar);
        detailBookImg = getActivity().findViewById(R.id.detailBookImg);
        detailBookNameTxt = getActivity().findViewById(R.id.detailBookNameTxt);
        detailBookYearTxt = getActivity().findViewById(R.id.detailBookYearTxt);
        detailBookPriceTxt = getActivity().findViewById(R.id.detailBookPriceTxt);
        detailSellerTxt = getActivity().findViewById(R.id.detailSellerTxt);
        detailSellerRatingTxt = getActivity().findViewById(R.id.detailSellerRatingTxt);
        detailBookConditionTxt = getActivity().findViewById(R.id.detailBookConditionTxt);
        detailBookDescriptionTxt = getActivity().findViewById(R.id.detailBookDescriptionTxt);
        detailAddCartBtn = getActivity().findViewById(R.id.detailAddCartBtn);
        detailUpdateCartBtn = getActivity().findViewById(R.id.detailUpdateCartBtn);

        detailTopBar.setSubPage("");
        detailSellerTxt.setOnClickListener(this);
        detailAddCartBtn.setOnClickListener(this);
        detailUpdateCartBtn.setOnClickListener(this);

        if (getActivity().getIntent().getExtras().get(Constant.productIdKey) == null) {

        } else {
            if (isAuth()) {
                token = getToken();
                getAuthCustomer();
            }

            getProduct(getActivity().getIntent().getExtras().get(Constant.productIdKey).toString());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.detailSellerTxt:
                Log.d("TAG", "onClick: test seller ");
                break;
            case R.id.detailAddCartBtn:
                Log.d("TAG", "onClick: test cart ");
                break;
            case R.id.detailUpdateCartBtn:
                Log.d("TAG", "onClick: test update ");
                break;
        }
    }

    // Helpers


    // Request functions
    public void getProduct(String productId) {
        getData = new GetData(getContext(), this);
        getData.setEndPoint(Constant.getProduct + "/" + productId);
        getData.setTaskType(Constant.getProductTaskType);
        getData.execute();
    }

    public void getAuthCustomer() {
        getAuthenticatedData = new GetAuthenticatedData(getContext(), this);
        getAuthenticatedData.setEndPoint(Constant.getCustomerData);
        getAuthenticatedData.setToken(token);
        getAuthenticatedData.setTaskType(Constant.getCustomer);
        getAuthenticatedData.execute();
    }

    // Navigation functions


    // Callback functions
    @Override
    public void onFinished(String message, String taskType) {
        if (taskType.equals(Constant.getProductTaskType)) {
            ApiData<Book> apiData = ApiData.fromJSON(ApiData.getData(message), Book.class);
            book = apiData.getData();

            if (customer != null) {
                if (customer.get_id().equals(book.getCustomer().get_id())) {
                    detailUpdateCartBtn.setVisibility(View.VISIBLE);
                    detailAddCartBtn.setVisibility(View.GONE);
                }
            }

            detailBookNameTxt.setText(book.getName());
            detailBookYearTxt.setText(String.valueOf(book.getPublishedAt()));
            detailBookDescriptionTxt.setText(book.getDescription());
            detailBookPriceTxt.setText(String.valueOf(book.getPrice()));
            detailBookImg.setImageBitmap(Helper.stringToBitmap(book.getImage()));
            if (book.isNew()) {
                detailBookConditionTxt.setText("new");
            } else {
                detailBookConditionTxt.setText("used");
            }
        } else if (taskType.equals(Constant.getCustomer)) {
            ApiData<Customer> apiData = ApiData.fromJSON(ApiData.getData(message), Customer.class);
            customer = apiData.getData();
        }
    }

    @Override
    public void onError(String taskType) {

    }

    // Getter and Setter
}
