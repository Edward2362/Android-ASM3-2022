package com.example.asm3.controllers;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.asm3.models.CartItem;
import com.example.asm3.models.Customer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CartActivityController extends BaseController implements
        AsyncTaskCallBack,
        OrderHolder.OnSelectListener,
        View.OnClickListener {
    private TopBarView topBar;
    private View cartProgressBar, cartActionLayout;
    private RecyclerView cartRecView;
    private TextView totalOrderTxt, countItemsTxt;
    private Button backBtn, checkoutBtn;
    private ArrayList<CartItem> cartItems;
    private GenericAdapter<CartItem> cartItemsAdapter;
    private String token;
    private GetAuthenticatedData getAuthenticatedData;
    private PostAuthenticatedData postAuthenticatedData;
    private DeleteAuthenticatedData deleteAuthenticatedData;
    private Customer customer;
    private MutableLiveData<Float> totalPrice = new MutableLiveData<>();


    public CartActivityController(Context context, FragmentActivity activity) {
        super(context, activity);
        topBar = getActivity().findViewById(R.id.cartTopBar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            topBar.setSubPage("Your Cart");
        }
        cartItems = new ArrayList<>();
    }

    @Override
    public void onInit() {
        cartProgressBar = getActivity().findViewById(R.id.cartProgressBar);
        cartActionLayout = getActivity().findViewById(R.id.cartActionLayout);

        backBtn = topBar.getBackButton();
        backBtn.setOnClickListener(this);

        checkoutBtn = getActivity().findViewById(R.id.checkoutBtn);
        checkoutBtn.setOnClickListener(this);

        cartRecView = getActivity().findViewById(R.id.cartRecView);
        cartItemsAdapter = generateOrderAdapter();
        loadPurchased();

        countItemsTxt = getActivity().findViewById(R.id.countItemsTxt);
        totalOrderTxt = getActivity().findViewById(R.id.totalOrderTxt);
        totalPrice.observe(getActivity(), new Observer<Float>() {
            @Override
            public void onChanged(Float aFloat) {
                if (aFloat == 0) {
                    checkoutBtn.setEnabled(false);
                }
            }
        });
    }

    public void onResume() {
        if (!isAuth()) {
            Helper.goToLogin(getContext(), getActivity());
        } else {
            cartActionLayout.setVisibility(View.INVISIBLE);
            cartRecView.setVisibility(View.INVISIBLE);
            cartProgressBar.setVisibility(View.VISIBLE);
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

    public void increaseCartQuantity(String id) {
        try{
            postAuthenticatedData = new PostAuthenticatedData(getContext(), this);
            postAuthenticatedData.setEndPoint(Constant.increaseCartQuantity);
            postAuthenticatedData.setToken(token);
            postAuthenticatedData.setTaskType(Constant.increaseCartQuantityTaskType);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constant.productKey,id);
            postAuthenticatedData.execute(jsonObject);
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
    }

    public void decreaseCartQuantity(String id) {
        try{
            postAuthenticatedData = new PostAuthenticatedData(getContext(), this);
            postAuthenticatedData.setEndPoint(Constant.decreaseCartQuantity);
            postAuthenticatedData.setToken(token);
            postAuthenticatedData.setTaskType(Constant.decreaseCartQuantityTaskType);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constant.productKey,id);
            postAuthenticatedData.execute(jsonObject);
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
    }

    public void removeCart(String id) {
        deleteAuthenticatedData = new DeleteAuthenticatedData(getContext(), this);
        deleteAuthenticatedData.setEndPoint(Constant.removeCart + "/" + id);
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
                if (!isOnline()) {
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
        if (!isOnline()) {
            showConnectDialog();
            return;
        }
        CartItem chosenCart = cartItems.get(position);
        switch (view.getId()) {
            case R.id.orderBody:
                Helper.goToBookDetail(getContext(), getActivity(), chosenCart.getProduct().get_id(), position);
                break;
            case R.id.orderDeleteBtn:
                removeCart(chosenCart.getProduct().get_id());
                totalPrice.setValue(totalPrice.getValue() - chosenCart.getProduct().getPrice() * chosenCart.getQuantity());
                totalOrderTxt.setText("Total: " + totalPrice.getValue() + " đ");
                cartItems.remove(position);
                countItemsTxt.setText("You have " + cartItems.size() + " books in your cart.");
                cartItemsAdapter.notifyItemRemoved(position);
                break;
            case R.id.orderIncreaseBtn:
                if (chosenCart.getQuantity() < chosenCart.getProduct().getQuantity()) {
                    chosenCart.setQuantity(chosenCart.getQuantity() + 1);
                    totalPrice.setValue(totalPrice.getValue() + chosenCart.getProduct().getPrice());
                    totalOrderTxt.setText("Total: " + totalPrice.getValue() + " đ");
                    cartItems.set(position, chosenCart);
                    cartItemsAdapter.notifyItemChanged(position);
                    increaseCartQuantity(chosenCart.getProduct().get_id());
                } else {
                    Toast.makeText(getContext(), "Out of Stock", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.orderDownBtn:
                if (chosenCart.getQuantity() > 1) {
                    chosenCart.setQuantity(chosenCart.getQuantity() - 1);
                    totalPrice.setValue(totalPrice.getValue() - chosenCart.getProduct().getPrice());
                    totalOrderTxt.setText("Total: " + totalPrice.getValue() + " đ");
                    cartItems.set(position, chosenCart);
                    cartItemsAdapter.notifyItemChanged(position);
                    decreaseCartQuantity(chosenCart.getProduct().get_id());
                } else {
                    removeCart(chosenCart.getProduct().get_id());
                    totalPrice.setValue(totalPrice.getValue() - chosenCart.getProduct().getPrice() * chosenCart.getQuantity());
                    totalOrderTxt.setText("Total: " + totalPrice.getValue() + " đ");
                    cartItems.remove(position);
                    countItemsTxt.setText("You have " + cartItems.size() + " books in your cart.");
                    cartItemsAdapter.notifyItemRemoved(position);
                }
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
                orderHolder.getOrderQuantityActionLayout().setVisibility(View.VISIBLE);
                orderHolder.getOrderBookTxt().setText(cartItem.getProduct().getName());
                orderHolder.getOrderBookImg().setImageBitmap(Helper.stringToBitmap(cartItem.getProduct().getImage()));
                orderHolder.getOrderQuantityTxt().setText("Quantity: " + cartItem.getQuantity());
                orderHolder.getOrderPriceTxt().setText(cartItem.getProduct().getPrice() * cartItem.getQuantity() + " đ");
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
            ApiData<Customer> apiData = ApiData.fromJSON(ApiData.getData(message), Customer.class);
            customer = apiData.getData();
            getCart();
        } else if (taskType.equals(Constant.getCartTaskType)) {
            ApiList<CartItem> apiList = ApiList.fromJSON(ApiList.getData(message), CartItem.class);
            cartItems.clear();
            cartItems.addAll(apiList.getList());
            cartItemsAdapter.notifyDataSetChanged();

            float newTotalPrice = 0;
            for (int i = 0; i < cartItems.size(); i++) {
                newTotalPrice = newTotalPrice + cartItems.get(i).getProduct().getPrice() * cartItems.get(i).getQuantity();

            }
            totalPrice.setValue(newTotalPrice);
            totalOrderTxt.setText("Total: " + totalPrice.getValue() + " đ");
            countItemsTxt.setText("You have " + cartItems.size() + " books in your cart.");
            cartActionLayout.setVisibility(View.VISIBLE);
            cartRecView.setVisibility(View.VISIBLE);
            cartProgressBar.setVisibility(View.INVISIBLE);
        } else if (taskType.equals(Constant.removeCartTaskType)) {

        } else if (taskType.equals(Constant.increaseCartQuantityTaskType)) {

        } else if (taskType.equals(Constant.decreaseCartQuantityTaskType)) {

        }

    }

    @Override
    public void onError(String taskType) {

    }
}
