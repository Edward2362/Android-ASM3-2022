package com.example.asm3.base.adapter;

import android.view.View;

import com.google.android.material.checkbox.MaterialCheckBox;

public interface OnSelectListener {
    void onSearchSuggestionClick(int position, View view, String suggestionText);
}
