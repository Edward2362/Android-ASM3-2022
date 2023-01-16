package com.example.asm3.base.adapter.viewHolder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm3.R;
import com.google.android.material.card.MaterialCardView;

public class NotificationHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private MaterialCardView notifCard;
    private LinearLayout notifBody;
    private TextView contentText;
    private TextView timeStampText;
    private OnSelectListener onSelectListener;

    public NotificationHolder(@NonNull View itemView, OnSelectListener onSelectListener) {
        super(itemView);
        notifCard = itemView.findViewById(R.id.notifCard);
        notifBody = itemView.findViewById(R.id.notifBody);
        contentText = itemView.findViewById(R.id.contentText);
        timeStampText = itemView.findViewById(R.id.timeStampText);
        this.onSelectListener = onSelectListener;

        notifBody.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        onSelectListener.onNotificationClick(getAdapterPosition(), view, notifCard);
    }

    public MaterialCardView getNotifCard() {
        return notifCard;
    }

    public TextView getContentText() {
        return contentText;
    }

    public TextView getTimeStampText() {
        return timeStampText;
    }

    public interface OnSelectListener {
        void onNotificationClick(int position, View view, MaterialCardView notifCard);
    }
}
