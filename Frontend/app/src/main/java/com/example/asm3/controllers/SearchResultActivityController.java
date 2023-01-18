package com.example.asm3.controllers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm3.R;
import com.example.asm3.base.adapter.GenericAdapter;
import com.example.asm3.base.adapter.viewHolder.BookHolder;
import com.example.asm3.base.controller.BaseController;
import com.example.asm3.base.networking.services.AsyncTaskCallBack;
import com.example.asm3.config.Helper;
import com.example.asm3.custom.components.TopBarView;
import com.example.asm3.fragments.searchResultActivity.FilterBottomSheetFragment;
import com.example.asm3.fragments.searchResultActivity.ResultViewModel;
import com.example.asm3.models.Book;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.ArrayList;

public class SearchResultActivityController extends BaseController implements
        View.OnClickListener,
        BookHolder.OnSelectListener,
        View.OnFocusChangeListener,
        AsyncTaskCallBack {
    // top bar view
    private TopBarView topBar;
    private MaterialButton backBtn;
    private SearchView searchView;
    private MaterialButton cartBtn;
    private int searchEditTextId;
    // search result view
    private MaterialButton filterBtn;
    private LinearProgressIndicator filterProgressBar;
    private RecyclerView searchResultRecView;
    private GenericAdapter<Book> searchResultAdapter;
    private ArrayList<Book> searchResults;

    private ResultViewModel resultViewModel;

    public SearchResultActivityController(Context context, FragmentActivity activity) {
        super(context, activity);
        searchResults = new ArrayList<>();
        resultViewModel = new ViewModelProvider(getActivity()).get(ResultViewModel.class);
        resultViewModel.setFilterProgressBar(filterProgressBar);
    }

    // Render functions
    @Override
    public void onInit() {
        // findViewById here
        topBar = getActivity().findViewById(R.id.searchResultTopBar);
        backBtn = topBar.getBackButton();
        cartBtn = topBar.getCartButton();
        searchView = topBar.getSearchView();
        filterBtn = getActivity().findViewById(R.id.filterBtn);
        filterProgressBar = getActivity().findViewById(R.id.filterProgressBar);
        searchResultRecView = getActivity().findViewById(R.id.searchResultRecView);

        // set topBar here
        topBar.setSearchResultPage();

        Intent intent = getActivity().getIntent();
        searchView.setQuery(intent.getExtras().getString("query"), false);

        // set on click listener here
        filterBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        cartBtn.setOnClickListener(this);
        searchView.setOnClickListener(this);
        searchView.setOnQueryTextFocusChangeListener(this);

        // for test
        searchResults.add(new Book("Lord of the ring", "J. R. R. Tolkien", null, 100000, 0, null, null, null, null, null, true, null));
        searchResults.add(new Book("Lord of the ring", "J. R. R. Tolkien", null, 100000, 0, null, null, null, null, null, true, null));
        searchResults.add(new Book("Lord of the ring", "J. R. R. Tolkien", null, 100000, 0, null, null, null, null, null, true, null));
        searchResults.add(new Book("Lord of the ring", "J. R. R. Tolkien", null, 100000, 0, null, null, null, null, null, true, null));
        searchResults.add(new Book("Lord of the ring", "J. R. R. Tolkien", null, 100000, 0, null, null, null, null, null, true, null));
        searchResults.add(new Book("Lord of the ring", "J. R. R. Tolkien", null, 100000, 0, null, null, null, null, null, true, null));
        searchResults.add(new Book("Lord of the ring", "J. R. R. Tolkien", null, 100000, 0, null, null, null, null, null, true, null));
        searchResults.add(new Book("Lord of the ring", "J. R. R. Tolkien", null, 100000, 0, null, null, null, null, null, true, null));
        searchResults.add(new Book("Lord of the ring", "J. R. R. Tolkien", null, 100000, 0, null, null, null, null, null, true, null));
        searchResults.add(new Book("Lord of the ring", "J. R. R. Tolkien", null, 100000, 0, null, null, null, null, null, true, null));
        // end test

        //adapter
        searchResultAdapter = generateSearchResultAdapter();
        searchResultRecView.setAdapter(searchResultAdapter);
        searchResultRecView.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        searchResultRecView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                if (parent.getChildLayoutPosition(view) % 2 == 0) {
                    outRect.set(0, 0, 10, 20);
                } else {
                    outRect.set(10, 0, 0, 20);
                }
            }
        });

        filterProgressBar.setVisibility(View.VISIBLE);
        getSearchResults();
    }

    @Override
    public void onBookClick(int position, View view) {

    }

    @Override
    public void onFocusChange(View view, boolean b) {
        getActivity().finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:
            case R.id.searchView:
                // send back query
                getActivity().finish();
                break;
            case R.id.cartButton:
                Helper.goToCart(getContext(), getActivity());
                break;
            case R.id.filterBtn:
                FilterBottomSheetFragment bottomSheet = new FilterBottomSheetFragment();
                bottomSheet.show(getActivity().getSupportFragmentManager(), "filterBottomSheet");
                break;
        }
    }

    // Helpers
    private GenericAdapter<Book> generateSearchResultAdapter() {
        return new GenericAdapter<Book>(searchResults) {
            @Override
            public RecyclerView.ViewHolder setViewHolder(ViewGroup parent) {
                final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
                return new BookHolder(view, SearchResultActivityController.this);
            }

            @Override
            public void onBindData(RecyclerView.ViewHolder holder, Book item) {
                BookHolder bookHolder = (BookHolder) holder;
                bookHolder.getBookNameTxt().setText(item.getName());
                bookHolder.getBookConditionTxt().setText("Condition: New");
                bookHolder.getBookPriceTxt().setText(item.getPrice() + " đ");
            }
        };
    }

    // Request functions
    public void getSearchResults() {

    }

    // Navigation functions


    // Callback functions
    @Override
    public void onFinished(String message, String taskType) {

        filterProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onError(String taskType) {

    }

    // Getter and Setter
}
