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
import com.example.asm3.base.networking.services.DeleteAuthenticatedData;
import com.example.asm3.base.networking.services.GetAuthenticatedData;
import com.example.asm3.base.networking.services.PostAuthenticatedData;
import com.example.asm3.config.Constant;
import com.example.asm3.config.Helper;
import com.example.asm3.custom.components.TopBarView;
import com.example.asm3.models.ApiData;
import com.example.asm3.models.ApiList;
import com.example.asm3.models.Book;
import com.example.asm3.models.CartItem;
import com.example.asm3.models.Customer;
import com.example.asm3.models.OrderDetail;

import org.json.JSONException;
import org.json.JSONObject;

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
    private String token;
    private float totalPrice;
    private GetAuthenticatedData getAuthenticatedData;
    private DeleteAuthenticatedData deleteAuthenticatedData;
    private Customer customer;


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

        cartRecView = getActivity().findViewById(R.id.cartRecView);
        cartItemsAdapter = generateOrderAdapter();
        loadPurchased();

        countItemsTxt = getActivity().findViewById(R.id.countItemsTxt);
        totalOrderTxt = getActivity().findViewById(R.id.totalOrderTxt);
        if (!isAuth()){
            Helper.goToLogin(getContext(),getActivity());
        } else {
            token = getToken();
            getAuthCustomer();
        }
    }

    public void getAuthCustomer() {
        getAuthenticatedData = new GetAuthenticatedData(getContext(), this);
        getAuthenticatedData.setEndPoint(Constant.getCustomerData);
        getAuthenticatedData.setToken(token);
        getAuthenticatedData.setTaskType(Constant.getCustomer);
        getAuthenticatedData.execute();
    }

    public void getCart() {
        getAuthenticatedData = new GetAuthenticatedData(getContext(), this);
        getAuthenticatedData.setEndPoint(Constant.getCart);
        getAuthenticatedData.setToken(token);
        getAuthenticatedData.setTaskType(Constant.getCartTaskType);
        getAuthenticatedData.execute();
    }

    public void removeCart(String id) {
        deleteAuthenticatedData = new DeleteAuthenticatedData(getContext(),this);
        deleteAuthenticatedData.setEndPoint(Constant.removeCart+"/"+id);
        deleteAuthenticatedData.setToken(token);
        deleteAuthenticatedData.setTaskType(Constant.removeCartTaskType);
        deleteAuthenticatedData.execute();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                getActivity().finish();
                break;
            case R.id.checkoutBtn:
                if(!isOnline()) {
                    showConnectDialog();
                    return;
                }
                Intent intent = new Intent(getContext(), CheckoutActivity.class);
                getActivity().startActivity(intent);
                break;
        }
    }

    @Override
    public void onOrderClick(int position, View view) {
        if(!isOnline()) {
            showConnectDialog();
            return;
        }
        switch (view.getId()) {
            case R.id.orderBody:
                Helper.goToBookDetail(getContext(),getActivity(), cartItems.get(position).getProduct().get_id(), position);
                break;
            case R.id.orderDeleteBtn:
                removeCart(cartItems.get(position).getProduct().get_id());
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
        if (taskType.equals(Constant.getCustomer)) {
            ApiData<Customer> apiData = ApiData.fromJSON(ApiData.getData(message),Customer.class);
            customer = apiData.getData();
            getCart();
        } else if (taskType.equals(Constant.getCartTaskType)) {
            ApiList<CartItem> apiList = ApiList.fromJSON(ApiList.getData(message),CartItem.class);
            cartItems.clear();
            cartItems.addAll(apiList.getList());
            cartItemsAdapter.notifyDataSetChanged();
            totalPrice = 0;
            for (int i=0; i< cartItems.size();i++){
                totalPrice = totalPrice + cartItems.get(i).getProduct().getPrice()*cartItems.get(i).getQuantity();

            }
            totalOrderTxt.setText("Total: "+ totalPrice);
            countItemsTxt.setText("You have "+cartItems.size()+" books in your cart.");
        } else if (taskType.equals(Constant.removeCartTaskType)) {

        }

    }

    @Override
    public void onError(String taskType) {

    }
}
