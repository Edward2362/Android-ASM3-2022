package com.example.asm3.fragments.mainActivity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.asm3.R;
import com.example.asm3.custom.components.TopBarView;
import com.example.asm3.models.Category;
import com.example.asm3.models.Customer;

import java.util.ArrayList;

public class MainViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<Category>> categories = new MutableLiveData<>();
    private final MutableLiveData<Integer> selectedItemId = new MutableLiveData<>(R.id.homeNav);
    private final MutableLiveData<TopBarView> topBar = new MutableLiveData<>();
    private final MutableLiveData<Customer> authCustomer = new MutableLiveData<>();

    public MainViewModel() {
    }

    public LiveData<ArrayList<Category>> getCateArray() {
        return categories;
    }

    public LiveData<Integer> getSelectedItemId() {
        return selectedItemId;
    }

    public LiveData<TopBarView> getTopBarView() {
        return topBar;
    }

    public LiveData<Customer> getAuthCustomer() {return authCustomer;}

    public void setCateArray(ArrayList<Category> newCate) {
        categories.setValue(newCate);
    }

    public void setSelectedItemId(int newId) {
        selectedItemId.setValue(newId);
    }

    public void setTopBarView(TopBarView newTopBar) {
        topBar.setValue(newTopBar);
    }

    public void setAuthCustomer(Customer newAuthCustomer) {
        authCustomer.setValue(newAuthCustomer);}
}
