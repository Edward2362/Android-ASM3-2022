package com.example.asm3.controllers;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm3.R;
import com.example.asm3.SearchResultActivity;
import com.example.asm3.base.adapter.GenericAdapter;
import com.example.asm3.base.adapter.viewHolder.SearchSuggestionHolder;
import com.example.asm3.base.controller.BaseController;
import com.example.asm3.base.networking.services.AsyncTaskCallBack;
import com.example.asm3.base.networking.services.GetData;
import com.example.asm3.config.Constant;
import com.example.asm3.custom.components.TopBarView;
import com.example.asm3.fragments.mainActivity.MainViewModel;
import com.example.asm3.fragments.mainActivity.SearchFragment;
import com.example.asm3.models.ApiList;
import com.example.asm3.models.Book;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.ArrayList;

public class SearchFragmentController extends BaseController implements
        AsyncTaskCallBack,
        SearchView.OnQueryTextListener,
        SearchSuggestionHolder.OnSelectListener{

    private SearchView searchView;
    private LinearProgressIndicator progressBar;
    private GenericAdapter<String> searchAdapter;
    private RecyclerView searchSuggestionRecView;
    private View view, searchResultNotifyLayout;
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


        searchSuggestions = new ArrayList<>();
        topBar = mainViewModel.getTopBarView().getValue();
    }

    // Render functions
    @Override
    public void onInit() {
        searchView = topBar.getSearchView();
        searchSuggestionRecView = view.findViewById(R.id.searchSuggestionRecView);
        progressBar = view.findViewById(R.id.progressBar);
        searchResultNotifyLayout = view.findViewById(R.id.searchResultNotifyLayout);
        searchView.setOnQueryTextListener(this);
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
        goToSearchResult(suggestionText);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if(!isOnline()) {
            showConnectDialog();
            return false;
        }
        goToSearchResult(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(!isOnline()) {
            showConnectDialog();
            return false;
        }
        if (!newText.isEmpty()) {
            progressBar.setVisibility(View.VISIBLE);
            searchSuggestionRecView.setVisibility(View.GONE);
            // TODO: implement function fetching
            handler.removeCallbacksAndMessages(null);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getSuggestions(newText);
                    Log.d("error","newText " + newText);
                    // TODO: put these 2 lines in onFinished, a fetching function will be called here
                }
            }, 1000);
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
    private void goToSearchResult(String query) {
        Intent intent = new Intent(getContext(), SearchResultActivity.class);
        intent.putExtra("query", query);
        Log.d(TAG, "goToSearchResult: activity: " + getActivity());
        getActivity().startActivity(intent);
    }


    // Callback functions
    @Override
    public void onFinished(String message, String taskType) {
        if(taskType.equals(Constant.suggestProductTaskType)) {
            ApiList<Book> apiList = ApiList.fromJSON(ApiList.getData(message),Book.class);
            ArrayList<Book> products = apiList.getList();
            ArrayList<String> newSuggestion = new ArrayList<>();
            newSuggestion.clear();
            for (int i=0; i<products.size(); i++) {
                newSuggestion.add(products.get(i).getName());
            }
            searchSuggestions.clear();
            searchSuggestions.addAll(newSuggestion);
            searchAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
            searchSuggestionRecView.setVisibility(View.VISIBLE);

//            if (searchSuggestions.isEmpty()){
//                searchResultNotifyLayout.setVisibility(View.VISIBLE);
//            } else {
//                searchSuggestionRecView.setVisibility(View.VISIBLE);
//            }
        }
    }

    @Override
    public void onError(String taskType) {

    }


    // Getter and Setter
}
