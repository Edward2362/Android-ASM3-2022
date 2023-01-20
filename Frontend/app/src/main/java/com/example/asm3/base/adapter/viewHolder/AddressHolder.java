package com.example.asm3.base.adapter.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm3.R;

public class AddressHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private LinearLayout addressBody;
    private TextView addressTextView;
    private OnSelectListener onSelectListener;

    public AddressHolder(@NonNull View itemView, OnSelectListener onSelectListener) {
        super(itemView);
        addressTextView = itemView.findViewById(R.id.addressResults);
        addressBody = itemView.findViewById(R.id.addresses);
        this.onSelectListener = onSelectListener;

        addressBody.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        onSelectListener.onAddressClick(getAdapterPosition(), view, addressTextView.getText().toString());
    }

    public TextView getAddressTextView() {
        return addressTextView;
    }


    public interface OnSelectListener {
        void onAddressClick(int position, View view, String address);
    }

}
