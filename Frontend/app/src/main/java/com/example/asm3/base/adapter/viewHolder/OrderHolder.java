package com.example.asm3.base.adapter.viewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm3.R;

public class OrderHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private CardView orderBody;
    private ImageView orderBookImg;
    private TextView orderBookTxt, orderQuantityTxt, orderStatusTxt, orderPriceTxt;
    private Button orderDeleteBtn, orderIncreaseBtn, orderDownBtn, orderLocationBtn;
    private View orderQuantityActionLayout, orderDeleteLayout, orderLocationLayout;
    private OnSelectListener onSelectListener;

    public OrderHolder(@NonNull View itemView, OnSelectListener onSelectListener) {
        super(itemView);
        orderBody = itemView.findViewById(R.id.orderBody);
        orderBookImg = itemView.findViewById(R.id.orderBookImg);
        orderBookTxt = itemView.findViewById(R.id.orderBookTxt);
        orderQuantityTxt = itemView.findViewById(R.id.orderQuantityTxt);
        orderStatusTxt = itemView.findViewById(R.id.orderStatusTxt);
        orderPriceTxt = itemView.findViewById(R.id.orderPriceTxt);
        orderDeleteBtn = itemView.findViewById(R.id.orderDeleteBtn);
        orderQuantityActionLayout = itemView.findViewById(R.id.orderQuantityActionLayout);
        orderDeleteLayout = itemView.findViewById(R.id.orderDeleteLayout);
        orderIncreaseBtn = itemView.findViewById(R.id.orderIncreaseBtn);
        orderDownBtn = itemView.findViewById(R.id.orderDownBtn);
        orderLocationBtn = itemView.findViewById(R.id.orderLocationBtn);
        orderLocationLayout = itemView.findViewById(R.id.orderLocationLayout);
        this.onSelectListener = onSelectListener;

        orderBody.setOnClickListener(this);
        orderDeleteBtn.setOnClickListener(this);
        orderIncreaseBtn.setOnClickListener(this);
        orderDownBtn.setOnClickListener(this);
        orderLocationBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        onSelectListener.onOrderClick(getAdapterPosition(), view);
    }

    public ImageView getOrderBookImg() {
        return orderBookImg;
    }

    public TextView getOrderBookTxt() {
        return orderBookTxt;
    }

    public TextView getOrderQuantityTxt() {
        return orderQuantityTxt;
    }

    public TextView getOrderStatusTxt() {
        return orderStatusTxt;
    }

    public TextView getOrderPriceTxt() {
        return orderPriceTxt;
    }

    public Button getOrderDeleteBtn() {
        return orderDeleteBtn;
    }

    public View getOrderQuantityActionLayout() {
        return orderQuantityActionLayout;
    }

    public CardView getOrderBody() {
        return orderBody;
    }

    public View getOrderDeleteLayout() {
        return orderDeleteLayout;
    }

    public View getOrderLocationLayout() {
        return orderLocationLayout;
    }

    public interface OnSelectListener {
        void onOrderClick(int position, View view);
    }
}
