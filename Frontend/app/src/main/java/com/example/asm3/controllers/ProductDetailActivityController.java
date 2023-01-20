package com.example.asm3.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import com.example.asm3.CartActivity;
import com.example.asm3.MainActivity;
import com.example.asm3.ManageBookActivity;
import com.example.asm3.ProfileActivity;
import com.example.asm3.R;
import com.example.asm3.base.controller.BaseController;
import com.example.asm3.base.networking.services.AsyncTaskCallBack;
import com.example.asm3.base.networking.services.GetAuthenticatedData;
import com.example.asm3.base.networking.services.GetData;
import com.example.asm3.base.networking.services.PostAuthenticatedData;
import com.example.asm3.config.Constant;
import com.example.asm3.config.Helper;
import com.example.asm3.custom.components.TopBarView;
import com.example.asm3.models.ApiData;
import com.example.asm3.models.Book;
import com.example.asm3.models.Customer;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
    private String token, sellerId;
    private Customer customer;
    private GetAuthenticatedData getAuthenticatedData;
    private PostAuthenticatedData postAuthenticatedData;
    private String productId;
    private int bookPosition;

    public ProductDetailActivityController(Context context, FragmentActivity activity) {
        super(context, activity);

//        bookId = intent.getExtras()
    }

    // Render functions
    @RequiresApi(api = Build.VERSION_CODES.M)
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

        if (Helper.isDarkTheme(getContext())) {
            detailBookPriceTxt.setTextColor(getActivity().getResources().getColor(R.color.md_theme_dark_primary));
            detailSellerTxt.setTextColor(getActivity().getResources().getColor(R.color.md_theme_dark_primary));
            detailSellerRatingTxt.setTextColor(getActivity().getResources().getColor(R.color.md_theme_dark_primary));
        } else {
            detailBookPriceTxt.setTextColor(getActivity().getResources().getColor(R.color.md_theme_light_primary));
            detailSellerTxt.setTextColor(getActivity().getResources().getColor(R.color.md_theme_light_primary));
            detailSellerRatingTxt.setTextColor(getActivity().getResources().getColor(R.color.md_theme_light_primary));

        }

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
                goToSellerProfile();
                break;
            case R.id.detailAddCartBtn:
                Log.d("TAG", "onClick: test cart ");
                if (!isAuth()) {
                    Helper.goToLogin(getContext(), getActivity());
                } else {
                    Log.d("TAG", "onClick: Test hehe");
                    saveProduct(productId, 1);
                }
                break;
            case R.id.detailUpdateCartBtn:
                Log.d("TAG", "onClick: test update ");
                goToManageBookActivity();
                break;
        }
    }

    // Helpers
    public boolean checkAddToCart() {
        boolean isAdd;
        isAdd = false;
        if (customer != null) {
            for (int i = 0; i < customer.getCart().size(); i++) {
                if (customer.getCart().get(i).getProduct().get_id().equals(productId)) {
                    isAdd = true;
                }
            }
        }
        return isAdd;
    }

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

    public void saveProduct(String id, int quantity) {
        postAuthenticatedData = new PostAuthenticatedData(getContext(), this);
        postAuthenticatedData.setEndPoint(Constant.saveProduct);
        postAuthenticatedData.setToken(token);
        postAuthenticatedData.setTaskType(Constant.saveProductTaskType);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.productKey, id);
            jsonObject.put(Constant.quantityKey, quantity);
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
        postAuthenticatedData.execute(jsonObject);
    }

    // Navigation functions
    public void goToManageBookActivity() {
        Intent intent = new Intent(getContext(), ManageBookActivity.class);
        intent.putExtra(Constant.productIdKey, book.get_id());
        getActivity().startActivityForResult(intent, Constant.manageBookActivityCode);
    }

    private void goToSellerProfile() {
        Intent intent = new Intent(getContext(), ProfileActivity.class);
        intent.putExtra(Constant.publicProfileIdKey, sellerId);
        getActivity().startActivity(intent);
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
            Log.d("tag",book.getCustomer().getUsername());

            sellerId = book.getCustomer().get_id();
            detailSellerTxt.setText(book.getCustomer().getUsername());
            detailBookNameTxt.setText(book.getName());
            detailBookYearTxt.setText("Published year: " + book.getPublishedAt());
            detailBookDescriptionTxt.setText("Description: " + book.getDescription());
            detailBookPriceTxt.setText(book.getPrice() + " đ");
            detailBookImg.setImageBitmap(Helper.stringToBitmap(book.getImage()));
            if (book.isNew()) {
                detailBookConditionTxt.setText("Condition: new");
            } else {
                detailBookConditionTxt.setText("Condition: used");
            }
            productDetailBody.setVisibility(View.VISIBLE);
            detailActionBody.setVisibility(View.VISIBLE);
            detailProgressBar.setVisibility(View.GONE);
        } else if (taskType.equals(Constant.getCustomer)) {
            ApiData<Customer> apiData = ApiData.fromJSON(ApiData.getData(message), Customer.class);
            customer = apiData.getData();
        } else if (taskType.equals(Constant.saveProductTaskType)) {
            Helper.goToCart(getContext(), getActivity());
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
