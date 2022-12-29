package com.example.asm3.fragments.mainActivity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.asm3.MainActivity;
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
        Log.d(TAG, "onCreateView: Hi home test");
        mainActivityController.setSelectedItemId(menuItemId);
        mainActivityController.loadMenu();
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    public void setController(MainActivityController mainActivityController) {
        this.mainActivityController = mainActivityController;
    }
}