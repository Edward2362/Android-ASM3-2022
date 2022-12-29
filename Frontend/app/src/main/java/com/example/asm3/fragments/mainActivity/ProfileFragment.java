package com.example.asm3.fragments.mainActivity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.asm3.R;
import com.example.asm3.controllers.MainActivityController;


public class ProfileFragment extends Fragment {

    private MainActivityController mainActivityController;
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
        menuItemId = R.id.profileNav;

        if (getArguments() != null) {
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainActivityController.setSelectedItemId(menuItemId);
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    public void setController(MainActivityController mainActivityController) {
        this.mainActivityController = mainActivityController;
    }
}