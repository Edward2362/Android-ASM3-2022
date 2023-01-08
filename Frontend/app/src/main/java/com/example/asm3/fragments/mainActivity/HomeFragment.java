package com.example.asm3.fragments.mainActivity;

import android.os.Bundle;

import static android.content.ContentValues.TAG;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.asm3.R;
import com.example.asm3.controllers.MainActivityController;

public class HomeFragment extends Fragment {
    private MainActivityController mainActivityController;
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
        menuItemId = R.id.homeNav;
        if (getArguments() != null) {
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View home = inflater.inflate(R.layout.activity_main_fragment_home, container, false);
        Log.d(TAG, "onCreateView: test " + mainActivityController);
        mainActivityController.setSelectedItemId(menuItemId);
        mainActivityController.loadMenu();
        mainActivityController.onInitFragment(home);
        return home;
    }

    public void setController(MainActivityController mainActivityController) {
        this.mainActivityController = mainActivityController;
    }
}