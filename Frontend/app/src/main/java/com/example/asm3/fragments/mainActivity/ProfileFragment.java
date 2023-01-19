package com.example.asm3.fragments.mainActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.asm3.R;
import com.example.asm3.controllers.HomeFragmentController;
import com.example.asm3.controllers.MainActivityController;
import com.example.asm3.controllers.ProfileFragmentController;


public class ProfileFragment extends Fragment {
    private ProfileFragmentController profileFragmentController;
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
    public void onResume() {
        super.onResume();
        Log.d("TAG", "onResume: test " );
        profileFragmentController.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View profile = inflater.inflate(R.layout.activity_main_fragment_profile, container, false);
        menuItemId = R.id.profileNav;
        mainViewModel.setSelectedItemId(menuItemId);
        onInit(profile, mainViewModel);
        Log.d("TAG", "onCreateView: test + running");
        return profile;
    }

    public void onInit(View view, ViewModel viewModel) {
        profileFragmentController = new ProfileFragmentController(requireContext(), requireActivity(), view, viewModel);
        profileFragmentController.onInit();
    }
}