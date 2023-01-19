package com.example.asm3.fragments.mainActivity;

import android.os.Bundle;

import static android.content.ContentValues.TAG;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.asm3.R;
import com.example.asm3.controllers.HomeFragmentController;
import com.example.asm3.controllers.MainActivityController;
import com.example.asm3.models.Category;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private HomeFragmentController homeFragmentController;
    private MainViewModel mainViewModel;
    private int menuItemId;

    public HomeFragment() {
    }

    public static HomeFragment newInstance(int menuItemId, MainActivityController mainActivityController) {
        HomeFragment fragment = new HomeFragment();
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
    public void onResume() {
        super.onResume();
        homeFragmentController.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View home = inflater.inflate(R.layout.activity_main_fragment_home, container, false);
        menuItemId = R.id.homeNav;
        mainViewModel.setSelectedItemId(menuItemId);
        onInit(home, mainViewModel);
        return home;
    }

    public void onInit(View view, ViewModel viewModel) {
        homeFragmentController = new HomeFragmentController(requireContext(), requireActivity(), view, viewModel);
        homeFragmentController.onInit();
    }
}