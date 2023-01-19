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

import com.example.asm3.CartActivity;
import com.example.asm3.CheckoutActivity;
import com.example.asm3.R;
import com.example.asm3.base.adapter.GenericAdapter;
import com.example.asm3.base.adapter.viewHolder.OrderHolder;
import com.example.asm3.base.controller.BaseController;
import com.example.asm3.base.networking.services.AsyncTaskCallBack;
import com.example.asm3.config.Helper;
import com.example.asm3.custom.components.TopBarView;
import com.example.asm3.models.Book;
import com.example.asm3.models.CartItem;
import com.example.asm3.models.OrderDetail;

import java.util.ArrayList;

public class CartActivityController extends BaseController implements
        AsyncTaskCallBack,
        OrderHolder.OnSelectListener,
        View.OnClickListener {
    private TopBarView topBar;
    private RecyclerView cartRecView;
    private TextView totalOrderTxt, countItemsTxt;
    private Button backBtn, checkoutBtn;
    private ArrayList<CartItem> cartItems;
    private GenericAdapter<CartItem> cartItemsAdapter;
    private float totalPrice;


    public CartActivityController(Context context, FragmentActivity activity) {
        super(context, activity);
        topBar = getActivity().findViewById(R.id.cartTopBar);
        topBar.setSubPage("Your Cart");
        cartItems = new ArrayList<>();

    }

    @Override
    public void onInit() {
        backBtn = topBar.getBackButton();
        backBtn.setOnClickListener(this);

        checkoutBtn = getActivity().findViewById(R.id.checkoutBtn);
        checkoutBtn.setOnClickListener(this);

        cartItems.add(new CartItem(2, new Book("Lord of the ring", "J. R. R. Tolkien", null, 100000, 0, null, null, null, null, null, true, null)));
        cartItems.add(new CartItem(2, new Book("Lord of the ring", "J. R. R. Tolkien", null, 100000, 0, null, null, null, null, null, true, null)));
        cartItems.add(new CartItem(2, new Book("Lord of the ring", "J. R. R. Tolkien", null, 100000, 0, null, null, null, null, null, true, null)));
        cartItems.add(new CartItem(2, new Book("Lord of the ring", "J. R. R. Tolkien", null, 100000, 0, null, null, null, null, null, true, null)));
        cartItems.add(new CartItem(2, new Book("Lord of the ring", "J. R. R. Tolkien", null, 100000, 0, null, null, null, null, null, true, null)));
        cartItems.add(new CartItem(2, new Book("Lord of the ring", "J. R. R. Tolkien", null, 100000, 0, null, null, null, null, null, true, null)));

        cartRecView = getActivity().findViewById(R.id.cartRecView);
        cartItemsAdapter = generateOrderAdapter();
        loadPurchased();

        countItemsTxt = getActivity().findViewById(R.id.countItemsTxt);
        countItemsTxt.setText("You have "+cartItems.size()+" books in your cart.");
        totalOrderTxt = getActivity().findViewById(R.id.totalOrderTxt);
        for (int i=0; i< cartItems.size();i++){
            totalPrice = totalPrice + cartItems.get(i).getProduct().getPrice()*cartItems.get(i).getQuantity();

        }
        totalOrderTxt.setText("Total: "+ totalPrice);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                getActivity().finish();
                break;
            case R.id.checkoutBtn:
                Intent intent = new Intent(getContext(), CheckoutActivity.class);
                getActivity().startActivity(intent);
                break;
        }
    }

    @Override
    public void onOrderClick(int position, View view) {
        switch (view.getId()) {
            case R.id.orderBody:
                Helper.goToBookDetail(getContext(),getActivity(), cartItems.get(position).getProduct().get_id(), position);
                break;
            case R.id.orderDeleteBtn:
                totalPrice = totalPrice - cartItems.get(position).getProduct().getPrice()*cartItems.get(position).getQuantity();
                totalOrderTxt.setText("Total: "+ totalPrice);
                cartItems.remove(position);
                countItemsTxt.setText("You have "+cartItems.size()+" books in your cart.");
                cartItemsAdapter.notifyItemRemoved(position);
                break;
        }
    }

    private GenericAdapter<CartItem> generateOrderAdapter() {
        return new GenericAdapter<CartItem>(cartItems) {
            @Override
            public RecyclerView.ViewHolder setViewHolder(ViewGroup parent) {
                final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
                return new OrderHolder(view, CartActivityController.this);
            }

            @Override
            public void onBindData(RecyclerView.ViewHolder holder, CartItem cartItem) {
                OrderHolder orderHolder = (OrderHolder) holder;
                orderHolder.getOrderBookTxt().setText(cartItem.getProduct().getName());
                orderHolder.getOrderQuantityTxt().setText("Quantity: " + cartItem.getQuantity());
                orderHolder.getOrderPriceTxt().setText(cartItem.getProduct().getPrice()*cartItem.getQuantity() + " đ");
            }
        };
    }

    private void loadPurchased() {
        cartRecView.setLayoutManager(new LinearLayoutManager(getContext()));
        cartRecView.setAdapter(cartItemsAdapter);

    }

    @Override
    public void onFinished(String message, String taskType) {

    }

    @Override
    public void onError(String taskType) {

    }
}
