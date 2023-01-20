package com.example.asm3.controllers;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
import com.example.asm3.models.ApiData;
import com.example.asm3.models.ApiList;
import com.example.asm3.models.CartItem;
import com.example.asm3.models.Customer;
import com.example.asm3.models.Order;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.util.ArrayList;

public class CheckoutActivityController extends BaseController implements
        AsyncTaskCallBack,
        OrderHolder.OnSelectListener,
        View.OnClickListener {
    private TopBarView topBar;
    private Button backBtn, checkoutBtn;
    private String token;
    private TextInputEditText couponEt;
    private TextInputLayout couponLayout;
    private RecyclerView orderRecView;
    private TextView orderTotalPriceTxt, addressDetailTxt;
    private GenericAdapter<Order> orderAdapter;
    private GetAuthenticatedData getAuthenticatedData;
    private PostAuthenticatedData postAuthenticatedData;
    private Customer customer;
    private View checkoutProgressBar;
    private ArrayList<Order> orders;

    public CheckoutActivityController(Context context, FragmentActivity activity){
        super(context,activity);
        topBar = getActivity().findViewById(R.id.checkoutTopBar);
        topBar.setSubPage("Checkout");
        orders = new ArrayList<Order>();
    }

    @Override
    public void onInit(){
        backBtn = topBar.getBackButton();
        backBtn.setOnClickListener(this);

        couponEt = getActivity().findViewById(R.id.couponEt);
        couponLayout = getActivity().findViewById(R.id.couponLayout);
        addressDetailTxt = getActivity().findViewById(R.id.addressDetailTxt);
        orderTotalPriceTxt = getActivity().findViewById(R.id.orderTotalPriceTxt);
        checkoutProgressBar = getActivity().findViewById(R.id.checkoutProgressBar);
        checkoutBtn = getActivity().findViewById(R.id.checkoutBtn);
        checkoutBtn.setOnClickListener(this);
        orderRecView = getActivity().findViewById(R.id.orderRecView);
        orderAdapter = generateOrderAdapter();
        loadOrders();

        if (!isAuth()){
            Helper.goToLogin(getContext(),getActivity());
        } else {
            token = getToken();
            getAuthCustomer();

        }

        couponEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

            }
        });
    }

    public void orderProducts() {
        postAuthenticatedData = new PostAuthenticatedData(getContext(), this);
        postAuthenticatedData.setEndPoint(Constant.orderProducts);
        postAuthenticatedData.setToken(token);
        postAuthenticatedData.setTaskType(Constant.orderProductsTaskType);
        JSONObject jsonObject = new JSONObject();
        postAuthenticatedData.execute(jsonObject);
    }

    public void generateOrders() {
        getAuthenticatedData = new GetAuthenticatedData(getContext(), this);
        getAuthenticatedData.setEndPoint(Constant.generateOrders);
        getAuthenticatedData.setToken(token);
        getAuthenticatedData.setTaskType(Constant.generateOrdersTaskType);
        getAuthenticatedData.execute();
    }

    public void getAuthCustomer() {
        getAuthenticatedData = new GetAuthenticatedData(getContext(), this);
        getAuthenticatedData.setEndPoint(Constant.getCustomerData);
        getAuthenticatedData.setToken(token);
        getAuthenticatedData.setTaskType(Constant.getCustomer);
        getAuthenticatedData.execute();
    }

    private GenericAdapter<Order> generateOrderAdapter() {
        return new GenericAdapter<Order>(orders) {
            @Override
            public RecyclerView.ViewHolder setViewHolder(ViewGroup parent) {
                final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
                return new OrderHolder(view, CheckoutActivityController.this);
            }

            @Override
            public void onBindData(RecyclerView.ViewHolder holder, Order order) {
                OrderHolder orderHolder = (OrderHolder) holder;
                orderHolder.getOrderBody().setCardBackgroundColor(getActivity().getResources().getColor(R.color.md_theme_light_onPrimary));
                orderHolder.getOrderDeleteLayout().setVisibility(View.GONE);
                orderHolder.getOrderBookTxt().setText(order.getBookName());
                orderHolder.getOrderBookImg().setImageBitmap(Helper.stringToBitmap(order.getBookImage()));
                orderHolder.getOrderQuantityTxt().setText("Quantity: " + order.getQuantity());
                orderHolder.getOrderPriceTxt().setText(order.getBookPrice()*order.getQuantity() + " đ");
            }
        };
    }

    private void loadOrders() {
        orderRecView.setLayoutManager(new LinearLayoutManager(getContext()));
        orderRecView.setAdapter(orderAdapter);

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                getActivity().finish();
                break;
            case R.id.checkoutBtn:
                orderProducts();
                break;
        }
    }

    @Override
    public void onFinished(String message, String taskType){
        if (taskType.equals(Constant.getCustomer)) {
            ApiData<Customer> apiData = ApiData.fromJSON(ApiData.getData(message),Customer.class);
            customer = apiData.getData();
            generateOrders();
        } else if (taskType.equals(Constant.orderProductsTaskType)) {
            getActivity().finish();
        } else if (taskType.equals(Constant.generateOrdersTaskType)) {
            ApiList<Order> apiList = ApiList.fromJSON(ApiList.getData(message),Order.class);
            orders.clear();
            orders.addAll(apiList.getList());
            orderAdapter.notifyDataSetChanged();
            float newTotalPrice = 0;
            for (int i = 0; i < apiList.getList().size(); i++) {
                newTotalPrice = newTotalPrice + apiList.getList().get(i).getBookPrice()* apiList.getList().get(i).getQuantity();
            }
            orderTotalPriceTxt.setText("Order total: " + newTotalPrice + " đ");
            checkoutProgressBar.setVisibility(View.INVISIBLE);
            orderRecView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onError(String taskType) {

    }

    @Override
    public void onOrderClick(int position, View view) {
        if(!isOnline()) {
            showConnectDialog();
            return;
        }
        switch (view.getId()) {
            case R.id.orderBody:
                break;
            case R.id.orderDeleteBtn:
                break;
        }
    }
}
