package com.example.asm3.controllers;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm3.R;
import com.example.asm3.base.adapter.GenericAdapter;
import com.example.asm3.base.adapter.viewHolder.SearchSuggestionHolder;
import com.example.asm3.base.controller.BaseController;
import com.example.asm3.base.networking.services.AsyncTaskCallBack;
import com.example.asm3.base.networking.services.GetData;
import com.example.asm3.config.Constant;
import com.example.asm3.custom.components.TopBarView;
import com.example.asm3.fragments.mainActivity.MainViewModel;
import com.example.asm3.models.ApiList;
import com.example.asm3.models.Book;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.ArrayList;

public class SearchFragmentController extends BaseController implements
        AsyncTaskCallBack,
        SearchView.OnQueryTextListener,
        SearchSuggestionHolder.OnSelectListener {

    private LinearProgressIndicator progressBar;
    private GenericAdapter<String> searchAdapter;
    private RecyclerView searchSuggestionRecView;
    private View view;
    private TopBarView topBar;
    private GetData getData;

    private MainViewModel mainViewModel;
    private long lastTextEdit = 0;
    private Handler handler = new Handler();
    private ArrayList<String> searchSuggestions;

    public SearchFragmentController(Context context, FragmentActivity activity, View view, ViewModel viewModel) {
        super(context, activity);
        this.view = view;
        this.mainViewModel = (MainViewModel) viewModel;

        getData = new GetData(getContext(),this);
        searchSuggestions = new ArrayList<>();
        topBar = mainViewModel.getTopBarView().getValue();
    }

    // Render functions
    @Override
    public void onInit() {
        topBar.getSearchView().setOnQueryTextListener(this);
        searchSuggestionRecView = view.findViewById(R.id.searchSuggestionRecView);
        progressBar = view.findViewById(R.id.progressBar);
        searchSuggestionRecView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        // for test
//        searchSuggestions.add("lord of the rings");
//        searchSuggestions.add("lord of the rings j. r. r. tolkien");
//        searchSuggestions.add("lord of the rings fellowship of the rings");
//        searchSuggestions.add("lord of the rings the return of the king");
//        searchSuggestions.add("lord of the rings the two towers");
//        searchSuggestions.add("lord of the rings set");
        // oki no more testing
        searchAdapter = generateSearchAdapter();
        searchSuggestionRecView.setAdapter(searchAdapter);
        searchSuggestionRecView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onSearchSuggestionClick(int position, View view, String suggestionText) {
        // TODO: get text and go to search result
        Log.d(TAG, "onSearchSuggestionClick: clicked!");
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        //TODO: go to search result activity
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (!newText.isEmpty()) {
            progressBar.setVisibility(View.VISIBLE);
            searchSuggestionRecView.setVisibility(View.GONE);
            lastTextEdit = System.currentTimeMillis();
            // TODO: implement function fetching
            handler.removeCallbacksAndMessages(null);
            do {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getSuggestions(newText);
                        // TODO: put these 2 lines in onFinished, a fetching function will be called here
                    }
                }, 1000);
            } while (!searchSuggestions.isEmpty());
        } else {
            progressBar.setVisibility(View.GONE);
            searchSuggestionRecView.setVisibility(View.GONE);
        }
        return false;
    }

    public void getSuggestions(String textInput) {
        getData = new GetData(getContext(),this);
        getData.setEndPoint(Constant.suggestProduct + "/" + "?queryInput=" + textInput);
        getData.setTaskType(Constant.suggestProductTaskType);
        getData.execute();
    }

    // Helpers
    private GenericAdapter<String> generateSearchAdapter() {
        return new GenericAdapter<String>(searchSuggestions) {
            @Override
            public RecyclerView.ViewHolder setViewHolder(ViewGroup parent) {
                final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_suggestion_item, parent, false);
                return new SearchSuggestionHolder(view, SearchFragmentController.this);
            }

            @Override
            public void onBindData(RecyclerView.ViewHolder holder, String suggest) {
                SearchSuggestionHolder searchSuggestionHolder = (SearchSuggestionHolder) holder;
                searchSuggestionHolder.getSuggestionText().setText(suggest);
            }
        };
    }// end generateSearchAdapter


    // Request functions


    // Navigation functions


    // Callback functions
    @Override
    public void onFinished(String message, String taskType) {
        if(taskType.equals(Constant.suggestProductTaskType)) {
            ApiList<Book> apiList = ApiList.fromJSON(ApiList.getData(message),Book.class);
            ArrayList<Book> products = apiList.getList();
            for (int i=0; i<products.size(); i++) {
                searchSuggestions.add(products.get(i).getName());
            }
            Log.d("error","haha");
            progressBar.setVisibility(View.GONE);
            searchSuggestionRecView.setVisibility(View.VISIBLE);
        }
    }

    // Getter and Setter
}
