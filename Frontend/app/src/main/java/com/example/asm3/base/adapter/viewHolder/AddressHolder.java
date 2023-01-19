package com.example.asm3.base.adapter.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm3.R;

public class AddressHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView addressTextView;
    private OnSelectListener onSelectListener;

    public AddressHolder(@NonNull View itemView, OnSelectListener onSelectListener) {
        super(itemView);
        addressTextView = itemView.findViewById(R.id.addressResults);

        this.onSelectListener = onSelectListener;

        addressTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        onSelectListener.onAddressClick(getAdapterPosition(), view, addressTextView);
    }

    public TextView getAddressTextView() {
        return addressTextView;
    }


    public interface OnSelectListener {
        void onAddressClick(int position, View view, TextView textView);
    }

}
