package com.example.asm3.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.example.asm3.MainActivity;
import com.example.asm3.ManageBookActivity;
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
import com.google.android.material.progressindicator.LinearProgressIndicator;

public class ProductDetailActivityController extends BaseController implements
        AsyncTaskCallBack,
        View.OnClickListener {
    private GetData getData;

    private TopBarView detailTopBar;
    private LinearProgressIndicator detailProgressBar;
    private LinearLayout productDetailBody;
    private LinearLayout detailActionBody;
    private Button backButton;
    private ImageView detailBookImg;
    private TextView detailBookYearTxt, detailBookNameTxt, detailBookPriceTxt, detailSellerTxt, detailSellerRatingTxt, detailBookConditionTxt, detailBookDescriptionTxt;
    private Button detailAddCartBtn, detailUpdateCartBtn;

    private Book book;
    private int bookId;
    private Intent intent;
    private String token;
    private Customer customer;
    private GetAuthenticatedData getAuthenticatedData;
    private String productId;
    private int bookPosition;

    public ProductDetailActivityController(Context context, FragmentActivity activity) {
        super(context, activity);

//        bookId = intent.getExtras()
    }

    // Render functions
    @Override
    public void onInit() {
        detailTopBar = getActivity().findViewById(R.id.detailTopBar);
        backButton = detailTopBar.getBackButton();
        productDetailBody = getActivity().findViewById(R.id.productDetailBody);
        detailActionBody = getActivity().findViewById(R.id.detailActionBody);
        detailProgressBar = getActivity().findViewById(R.id.detailProgressBar);
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
        backButton.setOnClickListener(this);
        detailSellerTxt.setOnClickListener(this);
        detailAddCartBtn.setOnClickListener(this);
        detailUpdateCartBtn.setOnClickListener(this);

        if (getActivity().getIntent().getExtras().get(Constant.productIdKey) == null) {
            getActivity().finish();
        } else {
            if (isAuth()) {
                token = getToken();
                getAuthCustomer();
            }
            bookPosition = getActivity().getIntent().getIntExtra(Constant.booksArrPositionKey, 0);
            productId = getActivity().getIntent().getExtras().get(Constant.productIdKey).toString();
            getProduct(productId);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                getActivity().finish();
                break;
            case R.id.detailSellerTxt:
                Log.d("TAG", "onClick: test seller ");
                break;
            case R.id.detailAddCartBtn:
                Log.d("TAG", "onClick: test cart ");
                break;
            case R.id.detailUpdateCartBtn:
                Log.d("TAG", "onClick: test update ");
                goToManageBookActivity();
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
    public void goToManageBookActivity() {
        Intent intent = new Intent(getContext(), ManageBookActivity.class);
        intent.putExtra(Constant.productIdKey, book.get_id());
        getActivity().startActivityForResult(intent, Constant.manageBookActivityCode);
    }

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
            productDetailBody.setVisibility(View.VISIBLE);
            detailActionBody.setVisibility(View.VISIBLE);
            detailProgressBar.setVisibility(View.GONE);
        } else if (taskType.equals(Constant.getCustomer)) {
            ApiData<Customer> apiData = ApiData.fromJSON(ApiData.getData(message), Customer.class);
            customer = apiData.getData();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == Constant.manageBookActivityCode) {
                if (data.getExtras().get(Constant.isUpdateKey) != null) {
                    getProduct(productId);
                } else if (data.getExtras().get(Constant.isRemoveKey) != null) {
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    intent.putExtra(Constant.isRemoveKey, Constant.isRemovedCode);
                    intent.putExtra(Constant.booksArrPositionKey, bookPosition);
                    getActivity().setResult(getActivity().RESULT_OK, intent);
                    getActivity().finish();
                }
            }
        }
    }

    @Override
    public void onError(String taskType) {

    }

    // Getter and Setter
}
