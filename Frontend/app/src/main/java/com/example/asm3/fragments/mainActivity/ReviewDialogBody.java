package com.example.asm3.fragments.mainActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.asm3.R;

public class ReviewDialogBody extends LinearLayout {
    private RatingBar ratingBar;
    private EditText reviewTxt;
    private TextView reviewUsername;

    public ReviewDialogBody(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.rating_dialog, this, true);

        ratingBar = findViewById(R.id.ratingBarDialog);
        reviewTxt = findViewById(R.id.userReviewTxt);
        reviewUsername = findViewById(R.id.reviewUsername);
    }

    public TextView getReviewUsername() {
        return reviewUsername;
    }

    public EditText getReviewTxt() {
        return reviewTxt;
    }

    public RatingBar getRatingBar() {
        return ratingBar;
    }

    public void setReviewUsername(TextView reviewUsername) {
        this.reviewUsername = reviewUsername;
    }

    public void setRatingBar(RatingBar ratingBar) {
        this.ratingBar = ratingBar;
    }

    public void setReviewTxt(EditText reviewTxt) {
        this.reviewTxt = reviewTxt;
    }
}
