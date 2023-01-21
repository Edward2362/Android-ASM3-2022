package com.example.asm3.fragments.mainActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.asm3.R;
import com.example.asm3.controllers.SearchFragmentController;


public class SearchFragment extends Fragment {
    private SearchFragmentController searchFragmentController;
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
        // Inflate the layout for this fragment
        View search = inflater.inflate(R.layout.activity_main_fragment_search, container, false);
        menuItemId = R.id.searchNav;
        mainViewModel.setSelectedItemId(menuItemId);
        onInit(search, mainViewModel);
        return search;
    }

    public void onInit(View view, ViewModel viewModel) {
        searchFragmentController = new SearchFragmentController(requireContext(), requireActivity(), view, viewModel);
        searchFragmentController.onInit();
    }
}