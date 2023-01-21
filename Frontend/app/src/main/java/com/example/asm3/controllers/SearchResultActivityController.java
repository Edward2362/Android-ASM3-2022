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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm3.R;
import com.example.asm3.base.adapter.GenericAdapter;
import com.example.asm3.base.adapter.viewHolder.BookHolder;
import com.example.asm3.base.controller.BaseController;
import com.example.asm3.base.networking.services.AsyncTaskCallBack;
import com.example.asm3.base.networking.services.GetData;
import com.example.asm3.config.Constant;
import com.example.asm3.config.Helper;
import com.example.asm3.custom.components.TopBarView;
import com.example.asm3.fragments.searchResultActivity.FilterBottomSheetFragment;
import com.example.asm3.fragments.searchResultActivity.ResultViewModel;
import com.example.asm3.models.ApiList;
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
    // search result view
    private MaterialButton filterBtn;
    private LinearProgressIndicator filterProgressBar;
    private RecyclerView searchResultRecView;
    private GenericAdapter<Book> searchResultAdapter;
    private ArrayList<Book> searchResults;
    private ArrayList<String> searchSubcategories;
    private String searchCategory;
    private View searchResultNotifyLayout;
    private boolean isCategorySearch = false;


    private ResultViewModel resultViewModel;

    private GetData getData;
    private String queryInput;

    public SearchResultActivityController(Context context, FragmentActivity activity) {
        super(context, activity);
        searchResults = new ArrayList<>();
        resultViewModel = new ViewModelProvider(getActivity()).get(ResultViewModel.class);
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
        searchResultNotifyLayout = getActivity().findViewById(R.id.searchResultNotifyLayout);

        resultViewModel.setFilterProgressBar(filterProgressBar);
        // set topBar here
        topBar.setSearchResultPage();

        Intent intent = getActivity().getIntent();

        if (intent.getExtras().get(Constant.isCategorySearchKey) == null) {
            isCategorySearch = false;
            queryInput = intent.getExtras().getString("query");
            searchView.setQuery(queryInput, false);
        } else {
            isCategorySearch = true;
            searchCategory = intent.getExtras().getString(Constant.categorySearchKey);
            searchSubcategories = intent.getExtras().getStringArrayList(Constant.subCategorySearchKey);
        }
        // set on click listener here
        filterBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        cartBtn.setOnClickListener(this);
        searchView.setOnClickListener(this);
        searchView.setOnQueryTextFocusChangeListener(this);

        // for test
        // end test
        resultViewModel.getFilterType().observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String currentFilter) {
                ArrayList<Book> newBooksOrder = new ArrayList<>();
                if (currentFilter.equals(Constant.conditionNewUsedKey)) {
                    newBooksOrder.addAll(searchResults);
                    for (int position = 0; position < newBooksOrder.size() - 1; position++) {
                        for (int observee = 0; observee < newBooksOrder.size() - 1 - position; observee++) {
                            if (!newBooksOrder.get(observee).isNew() && newBooksOrder.get(observee + 1).isNew()) {
                                swap(newBooksOrder, observee);
                            }
                        }
                    }
                    resetAdapter(newBooksOrder);
                } else if (currentFilter.equals(Constant.conditionUsedNewKey)) {
                    newBooksOrder.addAll(searchResults);
                    for (int position = 0; position < searchResults.size() - 1; position++) {
                        for (int observee = 0; observee < searchResults.size() - 1 - position; observee++) {
                            if (newBooksOrder.get(observee).isNew() && !newBooksOrder.get(observee + 1).isNew()) {
                                swap(newBooksOrder, observee);
                            }
                        }
                    }
                    resetAdapter(newBooksOrder);
                } else if (currentFilter.equals(Constant.ratingHighLowKey)) {
                    newBooksOrder.addAll(searchResults);
                    for (int position = 0; position < searchResults.size() - 1; position++) {
                        for (int observee = 0; observee < searchResults.size() - 1 - position; observee++) {
                            if (newBooksOrder.get(observee).getCustomer().getRatings() < newBooksOrder.get(observee + 1).getCustomer().getRatings()) {
                                swap(newBooksOrder, observee);
                            }
                        }
                    }
                    resetAdapter(newBooksOrder);
                } else if (currentFilter.equals(Constant.ratingLowHighKey)) {
                    newBooksOrder.addAll(searchResults);
                    for (int position = 0; position < searchResults.size() - 1; position++) {
                        for (int observee = 0; observee < searchResults.size() - 1 - position; observee++) {
                            if (newBooksOrder.get(observee).getCustomer().getRatings() > newBooksOrder.get(observee + 1).getCustomer().getRatings()) {
                                swap(newBooksOrder, observee);
                            }
                        }
                    }
                    resetAdapter(newBooksOrder);
                } else if (currentFilter.equals(Constant.priceHighLowKey)) {
                    newBooksOrder.addAll(searchResults);
                    for (int position = 0; position < searchResults.size() - 1; position++) {
                        for (int observee = 0; observee < searchResults.size() - 1 - position; observee++) {
                            if (newBooksOrder.get(observee).getPrice() < newBooksOrder.get(observee + 1).getPrice()) {
                                swap(newBooksOrder, observee);
                            }
                        }
                    }
                    resetAdapter(newBooksOrder);
                } else if (currentFilter.equals(Constant.priceLowHighKey)) {
                    newBooksOrder.addAll(searchResults);
                    for (int position = 0; position < searchResults.size() - 1; position++) {
                        for (int observee = 0; observee < searchResults.size() - 1 - position; observee++) {
                            if (newBooksOrder.get(observee).getPrice() > newBooksOrder.get(observee + 1).getPrice()) {
                                swap(newBooksOrder, observee);
                            }
                        }
                    }
                    resetAdapter(newBooksOrder);
                }
            }
        });

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

    }

    public void onResume() {
        filterProgressBar.setVisibility(View.VISIBLE);
        searchResultRecView.setVisibility(View.INVISIBLE);
        if (isOnline()) {
            if (isCategorySearch) {
                getProductsByCategoryAndSubcategories(searchCategory, searchSubcategories);
            } else {
                getSearchResults(queryInput);
            }
        }
    }

    @Override
    public void onBookClick(int position, View view) {
        if (!isOnline()) {
            showConnectDialog();
            return;
        }
        Helper.goToBookDetail(getContext(), getActivity(), searchResults.get(position).get_id(), position);
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        getActivity().finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:
            case R.id.searchViewAddress:
                // send back query
                getActivity().finish();
                break;
            case R.id.cartButton:
                if (!isOnline()) {
                    showConnectDialog();
                    return;
                }
                Helper.goToCart(getContext(), getActivity());
                break;
            case R.id.filterBtn:
                if (!isOnline()) {
                    showConnectDialog();
                    return;
                }
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
                bookHolder.getBookImg().setImageBitmap(Helper.stringToBitmap(item.getImage()));
                bookHolder.getBookNameTxt().setText(item.getName());
                bookHolder.getBookConditionTxt().setText("Condition: " + (item.isNew() ? "new" : "used"));
                bookHolder.getBookPriceTxt().setText(item.getPrice() + " đ");
            }
        };
    }

    private void swap(ArrayList<Book> newBooksOrder, int observee) {
        Book temp = newBooksOrder.get(observee);
        newBooksOrder.set(observee, newBooksOrder.get(observee + 1));
        newBooksOrder.set(observee + 1, temp);
    }

    private void resetAdapter(ArrayList<Book> newBooksOrder) {
        searchResults.clear();
        searchResults.addAll(newBooksOrder);
        searchResultAdapter.notifyDataSetChanged();
    }

    private void sorting(ArrayList<Book> newBooksOrder, boolean condition) {
        newBooksOrder.addAll(searchResults);
        for (int position = 0; position < searchResults.size() - 1; position++) {
            for (int observee = 0; observee < searchResults.size() - 1 - position; observee++) {
                if (condition) {
                    swap(newBooksOrder, observee);
                }
            }
        }
        resetAdapter(newBooksOrder);
    }


//    private void sorting(ArrayList<Book> books, String filter) {}


    // Request functions
    public void getSearchResults(String textInput) {
        getData = new GetData(getContext(), this);
        getData.setEndPoint(Constant.searchProduct + "?queryInput=" + textInput);
        getData.setTaskType(Constant.searchProductTaskType);
        getData.execute();
    }

    public void getProductsByCategoryAndSubcategories(String categoryName, ArrayList<String> subCategoriesList) {
        String subCategoriesQuery = "";
        for (int i = 0; i < subCategoriesList.size(); i++) {
            subCategoriesQuery = subCategoriesQuery + "&subCategory=" + subCategoriesList.get(i);
        }
        getData = new GetData(getContext(), this);
        getData.setEndPoint(Constant.getProducts + "?category=" + categoryName + subCategoriesQuery);
        getData.setTaskType(Constant.getProductsTaskType);
        getData.execute();
    }

    // Navigation functions


    // Callback functions
    @Override
    public void onFinished(String message, String taskType) {
        filterProgressBar.setVisibility(View.INVISIBLE);
        if (taskType.equals(Constant.searchProductTaskType)) {
            ApiList<Book> apiList = ApiList.fromJSON(ApiList.getData(message), Book.class);
            searchResults.clear();
            searchResults.addAll(apiList.getList());
            searchResultAdapter.notifyDataSetChanged();
        } else if (taskType.equals(Constant.getProductsTaskType)) {
            ApiList<Book> apiList = ApiList.fromJSON(ApiList.getData(message), Book.class);
            searchResults.clear();
            searchResults.addAll(apiList.getList());
            searchResultAdapter.notifyDataSetChanged();
        }
        if (searchResults.isEmpty()) {
            searchResultNotifyLayout.setVisibility(View.VISIBLE);
        } else {
            searchResultRecView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onError(String taskType) {

    }

    // Getter and Setter
}
