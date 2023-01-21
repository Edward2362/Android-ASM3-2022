package com.example.asm3.base.adapter.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm3.R;
import com.google.android.material.card.MaterialCardView;

public class BookHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private MaterialCardView productBody;
    private ImageView bookImg;
    private TextView bookNameTxt, bookConditionTxt, bookPriceTxt;
    private OnSelectListener onSelectListener;

    public BookHolder(@NonNull View itemView, OnSelectListener onSelectListener) {
        super(itemView);
        productBody = itemView.findViewById(R.id.productBody);
        bookImg = itemView.findViewById(R.id.bookImg);
        bookNameTxt = itemView.findViewById(R.id.bookNameTxt);
        bookConditionTxt = itemView.findViewById(R.id.bookConditionTxt);
        bookPriceTxt = itemView.findViewById(R.id.bookPriceTxt);
        this.onSelectListener = onSelectListener;

        productBody.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        onSelectListener.onBookClick(getAdapterPosition(), view);
    }

    public ImageView getBookImg() {
        return bookImg;
    }

    public TextView getBookNameTxt() {
        return bookNameTxt;
    }

    public TextView getBookConditionTxt() {
        return bookConditionTxt;
    }

    public TextView getBookPriceTxt() {
        return bookPriceTxt;
    }

    public interface OnSelectListener {
        void onBookClick(int position, View view);
    }
}
