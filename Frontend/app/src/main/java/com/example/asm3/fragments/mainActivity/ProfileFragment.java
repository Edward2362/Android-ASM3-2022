package com.example.asm3.fragments.mainActivity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.asm3.R;
import com.example.asm3.controllers.MainActivityController;


public class ProfileFragment extends Fragment {

    private MainActivityController mainActivityController;
    private MainViewModel mainViewModel;
    private int menuItemId;

    public ProfileFragment() {
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View profile = inflater.inflate(R.layout.activity_main_fragment_profile, container, false);
        menuItemId = R.id.profileNav;
        mainViewModel.setSelectedItemId(menuItemId);
        return profile;
    }

    public void setController(MainActivityController mainActivityController) {
        this.mainActivityController = mainActivityController;
    }
}