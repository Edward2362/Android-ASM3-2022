package com.example.asm3.base.adapter;

import android.view.View;

import com.google.android.material.checkbox.MaterialCheckBox;

public interface OnSelectListener {
    void onItemClick(int position, View view, MaterialCheckBox subCateCheckBox);
}
