package com.example.asm3.fragments.mainActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.asm3.R;
import com.example.asm3.controllers.NotificationFragmentController;


public class NotificationFragment extends Fragment {
    private NotificationFragmentController notificationFragmentController;
    private MainViewModel mainViewModel;
    private int menuItemId;

    public NotificationFragment() {
    }

    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View notification = inflater.inflate(R.layout.activity_main_fragment_notification, container, false);
        menuItemId = R.id.notiNav;
        mainViewModel.setSelectedItemId(menuItemId);
        onInit(notification, mainViewModel);
        return notification;
    }

    public void onInit(View view, ViewModel viewModel) {
        notificationFragmentController = new NotificationFragmentController(requireContext(), requireActivity(), view, viewModel);
        notificationFragmentController.onInit();
    }
}