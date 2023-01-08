package com.example.asm3.base.adapter.viewHolder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm3.R;
import com.example.asm3.base.adapter.OnSelectListener;

public class SearchSuggestionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private LinearLayout searchSuggestionBody;
    private TextView suggestionText;
    private OnSelectListener onSelectListener;

    public SearchSuggestionHolder(@NonNull View itemView, OnSelectListener onSelectListener) {
        super(itemView);
        searchSuggestionBody = itemView.findViewById(R.id.searchSuggestionBody);
        suggestionText = itemView.findViewById(R.id.suggestionText);
        this.onSelectListener = onSelectListener;

        searchSuggestionBody.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        onSelectListener.onSearchSuggestionClick(getAdapterPosition(), view, suggestionText.getText().toString());
    }// end of onClick

    public TextView getSuggestionText() {
        return suggestionText;
    }
}
