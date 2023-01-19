package com.example.asm3.controllers;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.DialogInterface;
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
import com.example.asm3.custom.components.TopBarView;
import com.example.asm3.fragments.mainActivity.ReviewDialogBody;
import com.example.asm3.models.Customer;
import com.example.asm3.models.Order;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.LinearProgressIndicator;

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

        sales.add(new Order("000", "10/10/2022", "Delivering",
                new Customer("Kiki"), "The Lake", 140000, 3, false));
        // adapter
        salesAdapter = generateOrderAdapter();
        salesRecView.setAdapter(salesAdapter);
        salesRecView.setLayoutManager(new LinearLayoutManager(getContext()));

        if(isOnline()) getSalesInProgress();
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
        switch (i){
            case 0: // packaging
                break;
            case 1: // processing
                break;
            case 2: // shipping
                break;
            case -1: // submit button
                break;
            case -2: // cancel button
                break;
        }
    }

    @Override
    public void onOrderClick(int position, View view) {
        showDialog();
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
                orderHolder.getOrderStatusTxt().setText("Status: " + item.getStatus());
                orderHolder.getOrderStatusTxt().setVisibility(View.VISIBLE);
                orderHolder.getOrderDeleteBtn().setVisibility(View.GONE);
            }
        };
    }

    private void showDialog() {
        final String[] listChoices = new String[]{"Packaging", "Processing", "Shipping"};
        builder = new MaterialAlertDialogBuilder(getContext());
        builder.setTitle("Update sale status").
                setSingleChoiceItems(listChoices, 0, this).
                setPositiveButton(R.string.submit, this).
                setNegativeButton(R.string.cancel, this).
                create().show();
    }

    // Request functions
    private void getSalesInProgress() {
    }

    // Navigation functions


    // Calback functions
    @Override
    public void onFinished(String message, String taskType) {

        salesProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onError(String taskType) {

    }

    // Getters and setters
}