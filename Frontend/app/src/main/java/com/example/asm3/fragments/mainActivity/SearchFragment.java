package com.example.asm3.fragments.mainActivity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.asm3.R;
import com.example.asm3.controllers.MainActivityController;


public class SearchFragment extends Fragment {
    private MainActivityController mainActivityController;
    private MainViewModel mainViewModel;
    private int menuItemId;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        View search = inflater.inflate(R.layout.activity_main_fragment_search, container, false);
        menuItemId = R.id.searchNav;
        // Inflate the layout for this fragment
        mainViewModel.setSelectedItemId(menuItemId);
        mainActivityController.onInitSearchFragment(search);
        return search;
    }

    public void setController(MainActivityController mainActivityController) {
        this.mainActivityController = mainActivityController;
    }
}