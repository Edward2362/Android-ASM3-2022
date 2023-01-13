package com.example.asm3.fragments.mainActivity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.asm3.R;
import com.example.asm3.controllers.MainActivityController;
import com.example.asm3.models.Category;

import java.util.ArrayList;

public class MainViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<Category>> categories = new MutableLiveData<>();
    private final MutableLiveData<Integer> selectedItemId = new MutableLiveData<>();

    public MainViewModel() {
        selectedItemId.setValue(R.id.homeNav);
    }

    public LiveData<ArrayList<Category>> getCateArray() {
        return categories;
    }

    public LiveData<Integer> getSelectedItemId() {
        return selectedItemId;
    }

    public void setCateArray(ArrayList<Category> newCate) {
        categories.setValue(newCate);
    }

    public void setSelectedItemId(int newId) {
        selectedItemId.setValue(newId);
    }
}
