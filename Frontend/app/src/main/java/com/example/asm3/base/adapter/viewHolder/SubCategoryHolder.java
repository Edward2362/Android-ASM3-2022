package com.example.asm3.base.adapter.viewHolder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm3.R;
import com.example.asm3.base.adapter.OnSelectListener;
import com.google.android.material.checkbox.MaterialCheckBox;

public class SubCategoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private LinearLayout subCateBody;
    private TextView subCateTxt;
    private MaterialCheckBox subCateCheckBox;
    private OnSelectListener onSelectListener;

    public SubCategoryHolder(@NonNull View itemView, OnSelectListener onSelectListener) {
        super(itemView);
        subCateBody = itemView.findViewById(R.id.subCateBody);
        subCateTxt = itemView.findViewById(R.id.subCateTxt);
        subCateCheckBox = itemView.findViewById(R.id.subCateCheckBox);
        this.onSelectListener = onSelectListener;

        subCateBody.setOnClickListener(this);
        subCateCheckBox.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        onSelectListener.onSubCateClick(getAdapterPosition(), view, subCateCheckBox);
    }

    public TextView getSubCateTxt() {
        return subCateTxt;
    }

    public MaterialCheckBox getSubCateCheckBox() {
        return subCateCheckBox;
    }

    public interface OnSelectListener {
        void onSubCateClick(int position, View view, MaterialCheckBox subCateCheckBox);
    }
}
