package com.example.asm3.controllers;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm3.AuthenticationActivity;
import com.example.asm3.R;
import com.example.asm3.base.adapter.GenericAdapter;
import com.example.asm3.base.adapter.viewHolder.NotificationHolder;
import com.example.asm3.base.controller.BaseController;
import com.example.asm3.base.networking.services.AsyncTaskCallBack;
import com.example.asm3.config.Constant;
import com.example.asm3.config.Helper;
import com.example.asm3.custom.components.TopBarView;
import com.example.asm3.fragments.mainActivity.MainViewModel;
import com.example.asm3.fragments.mainActivity.NotificationFragment;
import com.example.asm3.models.Notification;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class NotificationFragmentController extends BaseController implements
        NotificationHolder.OnSelectListener,
        AsyncTaskCallBack {
    private View view, notifNotifyLayout;
    private GenericAdapter<Notification> notifAdapter;
    private RecyclerView notifRecView;

    private MainViewModel mainViewModel;
    private LiveData<ArrayList<Notification>> notifications;

    public NotificationFragmentController(Context context, FragmentActivity activity, View view, ViewModel viewModel) {
        super(context, activity);
        this.view = view;
        this.mainViewModel = (MainViewModel) viewModel;

        notifications = mainViewModel.getNotifications();
    }

    // Render functions
    @Override
    public void onInit() {
        notifNotifyLayout = view.findViewById(R.id.notifNotifyLayout);
        notifRecView = view.findViewById(R.id.notifRecView);
        if (!isAuth()) {
            Helper.goToLogin(getContext(), getActivity());
        } else {
            Helper.setBadge(notifications.getValue(), mainViewModel);

            notifAdapter = generateNotificationAdapter();
            notifRecView.setAdapter(notifAdapter);
            notifRecView.setLayoutManager(new LinearLayoutManager(getContext()));
            notifications.observe(getActivity(), new Observer<ArrayList<Notification>>() {
                @Override
                public void onChanged(ArrayList<Notification> notifications) {
                    Log.d(TAG, "onChanged: Noti test");
                    Helper.setBadge(notifications, mainViewModel);
                    if (notifications.isEmpty()) {
                        notifNotifyLayout.setVisibility(View.VISIBLE);
                        notifRecView.setVisibility(View.GONE);
                    } else {
                        notifNotifyLayout.setVisibility(View.GONE);
                        notifRecView.setVisibility(View.VISIBLE);
                        notifAdapter.notifyDataSetChanged();
                    }

                }
            });
//        //test
//        notificationsList = new ArrayList<>();
//        notificationsList.add(new Notification("00", "Karen", "Hello this is a notification", "11:00", true));
//        notificationsList.add(new Notification("01", "Karen", "Hello this is another notification", "10:00", false));
//
//        //end test
        }
    }

    @Override
    public void onNotificationClick(int position, View view) {
        notifications.getValue().get(position).setIsRead(true);
        ((MaterialCardView) view).setCardElevation(0);
    }

    // Helpers
    private GenericAdapter<Notification> generateNotificationAdapter() {
        return new GenericAdapter<Notification>(notifications.getValue()) {
            @Override
            public RecyclerView.ViewHolder setViewHolder(ViewGroup parent) {
                final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notif_item, parent, false);
                return new NotificationHolder(view, NotificationFragmentController.this);
            }

            @Override
            public void onBindData(RecyclerView.ViewHolder holder, Notification item) {
                NotificationHolder notificationHolder = (NotificationHolder) holder;
                if (!item.isRead()) {
//                    notificationHolder.getNotifCard().setCardBackgroundColor(getContext().getResources().getColor(R.color.md_theme_light_inversePrimary));
                    notificationHolder.getNotifCard().setCardElevation(12);
                } else {
//                    notificationHolder.getNotifCard().setCardBackgroundColor(getContext().getResources().getColor(R.color.md_theme_light_background));
                    notificationHolder.getNotifCard().setCardElevation(0);
                }
                notificationHolder.getContentText().setText(item.getContent());
                notificationHolder.getTimeStampText().setText(item.getTimestamp());
            }
        };
    }

    // Request functions
    // TODO: After onClick, notify the database that the notification is read

    // Navigation functions


    // Callback functions
    @Override
    public void onFinished(String message, String taskType) {

    }

    @Override
    public void onError(String taskType) {

    }

    // Getter and Setter
}
