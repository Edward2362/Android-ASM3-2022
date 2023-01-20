package com.example.asm3.controllers;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm3.R;
import com.example.asm3.base.adapter.GenericAdapter;
import com.example.asm3.base.adapter.viewHolder.OrderHolder;
import com.example.asm3.base.controller.BaseController;
import com.example.asm3.base.networking.services.AsyncTaskCallBack;
import com.example.asm3.base.networking.services.GetAuthenticatedData;
import com.example.asm3.base.networking.services.PostAuthenticatedData;
import com.example.asm3.config.Constant;
import com.example.asm3.config.Helper;
import com.example.asm3.custom.components.TopBarView;
import com.example.asm3.fragments.mainActivity.ReviewDialogBody;
import com.example.asm3.models.ApiList;
import com.example.asm3.models.Customer;
import com.example.asm3.models.Order;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SaleProgressActivityController extends BaseController implements
        View.OnClickListener,
        OrderHolder.OnSelectListener,
        AsyncTaskCallBack,
        DialogInterface.OnClickListener {
    private TopBarView salesTopBar;
    private Button backBtn;
    private LinearProgressIndicator salesProgressBar;
    private RecyclerView salesRecView;
    private GenericAdapter<Order> salesAdapter;
    private ArrayList<Order> sales;
    private MaterialAlertDialogBuilder builder;
    private String token;
    private GetAuthenticatedData getAuthenticatedData;
    private PostAuthenticatedData postAuthenticatedData;
    private int saleClicked = -1;
    private String status;

    public SaleProgressActivityController(Context context, FragmentActivity activity) {
        super(context, activity);
        sales = new ArrayList<>();
    }

    // Render functions
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onInit() {
        //findViewById
        salesTopBar = getActivity().findViewById(R.id.salesTopBar);
        salesRecView = getActivity().findViewById(R.id.salesRecView);
        backBtn = salesTopBar.getBackButton();
        salesProgressBar = getActivity().findViewById(R.id.salesProgressBar);

        salesTopBar.setSubPage("Sales progress");

        // onClickListener
        backBtn.setOnClickListener(this);

        // testing
        //String _id, String timestamp, String status, Customer buyer, Customer seller, String bookImage, String bookName, float bookPrice, int quantity, boolean hasReview
//        sales.add(new Order("00120", "12/02/2022", "packaging", new Customer(), new Customer(), "", "Doraemon", 20000F, 1, false));
//        sales.add(new Order("00110", "15/08/2022", "shipping", new Customer(), new Customer(), "", "Cinderella", 100000F, 1, false));
//        sales.add(new Order("00153", "08/10/2022", "completed", new Customer(), new Customer(), "", "Wonder", 235000F, 1, false));
//        sales.add(new Order("00120", "30/12/2022", "reviewed", new Customer(), new Customer(), "", "Pokemon", 40000F, 1, true));
        salesRecView.setVisibility(View.VISIBLE);
        // end test

        // adapter
        salesAdapter = generateOrderAdapter();
        salesRecView.setAdapter(salesAdapter);
        salesRecView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (isOnline()) {
            if (!isAuth()) {

            } else {
                token = getToken();
                getSalesInProgress();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                getActivity().finish();
                break;
        }
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        Log.d(TAG, "onClick: in dialog " + i);
        switch (i) {
            case 0: // packaging
                status = "Packaging";
                break;
            case 1: // shipping
                status = "Shipping";
                break;
            case 2: // completed
                status = "Completed";
                break;
            case -1: // submit button
                if (status != null) {
                    sales.get(saleClicked).setStatus(status);
                    salesAdapter.notifyItemChanged(saleClicked);
                    updateStatusOrder(sales.get(saleClicked).get_id(),status);
                }
                break;
            case -2: // cancel button
                break;
        }
    }

    @Override
    public void onOrderClick(int position, View view) {
        switch (view.getId()) {
            case R.id.orderBody:
                saleClicked = position;
                showDialog(sales.get(position).getStatus());
                break;
            case R.id.orderLocationBtn:
                Log.d(TAG, "onOrderClick: test "+ position);
                String uri = "geo:0,0?q=1600+Amphitheatre+Parkway%2C+CA";
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                getActivity().startActivity(intent);
                break;
        }

    }

    // Helpers
    private GenericAdapter<Order> generateOrderAdapter() {
        return new GenericAdapter<Order>(sales) {
            @Override
            public RecyclerView.ViewHolder setViewHolder(ViewGroup parent) {
                final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
                return new OrderHolder(view, SaleProgressActivityController.this);
            }

            @Override
            public void onBindData(RecyclerView.ViewHolder holder, Order item) {
                OrderHolder orderHolder = (OrderHolder) holder;
                orderHolder.getOrderBookTxt().setText(item.getBookName());
                orderHolder.getOrderQuantityTxt().setText("Quantity: " + item.getQuantity());
                orderHolder.getOrderPriceTxt().setText(item.getBookPrice() + " đ");
                orderHolder.getOrderBookImg().setImageBitmap(Helper.stringToBitmap(item.getBookImage()));
                if (item.isHasReview()) {
                    orderHolder.getOrderStatusTxt().setText("Status: Reviewed");
                } else {
                    orderHolder.getOrderStatusTxt().setText("Status: " + item.getStatus());
                }
                orderHolder.getOrderStatusTxt().setVisibility(View.VISIBLE);
                orderHolder.getOrderDeleteBtn().setVisibility(View.GONE);
                orderHolder.getOrderLocationLayout().setVisibility(View.VISIBLE);
                if (item.getStatus().equalsIgnoreCase("completed") ||
                        item.isHasReview()) {
                    orderHolder.getOrderBody().setOnClickListener(null);
                }
                if (Helper.isDarkTheme(getContext())) {
                    orderHolder.getOrderPriceTxt().setTextColor(getActivity().getResources().getColor(R.color.md_theme_dark_onPrimaryContainer));
                    if (item.getStatus().equalsIgnoreCase("packaging"))
                        orderHolder.getOrderBody().setCardBackgroundColor(getActivity().getResources().getColor(R.color.dark_sale_status_packaging));
                    if (item.getStatus().equalsIgnoreCase("shipping"))
                        orderHolder.getOrderBody().setCardBackgroundColor(getActivity().getResources().getColor(R.color.dark_sale_status_shipping));
                    if (item.getStatus().equalsIgnoreCase("completed"))
                        orderHolder.getOrderBody().setCardBackgroundColor(getActivity().getResources().getColor(R.color.dark_sale_status_completed));
                    if (item.isHasReview())
                        orderHolder.getOrderBody().setCardBackgroundColor(getActivity().getResources().getColor(R.color.dark_sale_status_reviewed));
                } else {
                    orderHolder.getOrderPriceTxt().setTextColor(getActivity().getResources().getColor(R.color.md_theme_light_onSurface));
                    if (item.getStatus().equalsIgnoreCase("packaging"))
                        orderHolder.getOrderBody().setCardBackgroundColor(getActivity().getResources().getColor(R.color.light_sale_status_packaging));
                    if (item.getStatus().equalsIgnoreCase("shipping"))
                        orderHolder.getOrderBody().setCardBackgroundColor(getActivity().getResources().getColor(R.color.light_sale_status_shipping));
                    if (item.getStatus().equalsIgnoreCase("completed"))
                        orderHolder.getOrderBody().setCardBackgroundColor(getActivity().getResources().getColor(R.color.light_sale_status_completed));
                    if (item.isHasReview())
                        orderHolder.getOrderBody().setCardBackgroundColor(getActivity().getResources().getColor(R.color.light_sale_status_reviewed));
                }
            }
        };
    }

    private void showDialog(String status) {
        int checkedItem = 0;
        if (status.equalsIgnoreCase("packaging")) checkedItem = 0;
        else if (status.equalsIgnoreCase("shipping")) checkedItem = 1;
        else if (status.equalsIgnoreCase("completed")) checkedItem = 2;
        final String[] listChoices = new String[]{"Packaging", "Shipping", "Completed"};
        builder = new MaterialAlertDialogBuilder(getContext());
        builder.setTitle("Update sale status").
                setSingleChoiceItems(listChoices, checkedItem, this).
                setPositiveButton(R.string.submit, this).
                setNegativeButton(R.string.cancel, this).
                create().show();
    }

    // Request functions
    private void getSalesInProgress() {
        getAuthenticatedData = new GetAuthenticatedData(getContext(), this);
        getAuthenticatedData.setEndPoint(Constant.getSellingOrders);
        getAuthenticatedData.setTaskType(Constant.getSellingOrdersTaskType);
        getAuthenticatedData.setToken(token);
        getAuthenticatedData.execute();
    }

    public void updateStatusOrder(String orderId,String status) {
        try {
            postAuthenticatedData = new PostAuthenticatedData(getContext(), this);
            postAuthenticatedData.setEndPoint(Constant.updateStatusOrder);
            postAuthenticatedData.setTaskType(Constant.updateStatusOrderTaskType);
            postAuthenticatedData.setToken(token);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constant.orderIdKey,orderId);
            jsonObject.put(Order.statusKey,status);
            postAuthenticatedData.execute(jsonObject);
        } catch (JSONException jsonException){
            jsonException.printStackTrace();
        }

    }
    // Navigation functions


    // Calback functions
    @Override
    public void onFinished(String message, String taskType) {
        if (taskType.equals(Constant.getSellingOrdersTaskType)) {
            ApiList<Order> apiList = ApiList.fromJSON(ApiList.getData(message), Order.class);
            sales.clear();
            sales.addAll(apiList.getList());
            salesAdapter.notifyDataSetChanged();
            salesProgressBar.setVisibility(View.GONE);
        } else if (taskType.equals(Constant.updateStatusOrderTaskType)){

        }
    }

    @Override
    public void onError(String taskType) {

    }

    // Getters and setters
}
