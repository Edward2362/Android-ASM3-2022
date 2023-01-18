package com.example.asm3.base.adapter.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm3.R;

public class ReviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private CardView reviewUserImgLayout;
    private ImageView reviewUserImg;
    private TextView reviewUserNameTxt, reviewContentTxt, reviewDateTxt;
    private RatingBar reviewRatingBar;
    private OnSelectListener onSelectListener;

    public ReviewHolder(@NonNull View itemView, OnSelectListener onSelectListener) {
        super(itemView);
        reviewUserImgLayout = itemView.findViewById(R.id.reviewUserImgLayout);
        reviewUserImg = itemView.findViewById(R.id.reviewUserImg);
        reviewUserNameTxt = itemView.findViewById(R.id.reviewUserNameTxt);
        reviewContentTxt = itemView.findViewById(R.id.reviewContentTxt);
        reviewDateTxt = itemView.findViewById(R.id.reviewDateTxt);
        reviewRatingBar = itemView.findViewById(R.id.reviewRatingBar);
        this.onSelectListener = onSelectListener;

        reviewUserImgLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        onSelectListener.onAvatarClick(getAdapterPosition(), view);
    }

    public ImageView getReviewUserImg() {
        return reviewUserImg;
    }

    public TextView getReviewUserNameTxt() {
        return reviewUserNameTxt;
    }

    public TextView getReviewContentTxt() {
        return reviewContentTxt;
    }

    public TextView getReviewDateTxt() {
        return reviewDateTxt;
    }

    public RatingBar getReviewRatingBar() {
        return reviewRatingBar;
    }

    public interface OnSelectListener {
        void onAvatarClick(int position, View view);
    }
}
