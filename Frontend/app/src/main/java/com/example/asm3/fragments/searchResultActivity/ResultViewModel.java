package com.example.asm3.fragments.searchResultActivity;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.progressindicator.LinearProgressIndicator;

public class ResultViewModel extends ViewModel {
    private final MutableLiveData<String> filterType = new MutableLiveData<>("");
    private final MutableLiveData<FragmentManager> fragmentManager = new MutableLiveData<>();
    private final MutableLiveData<BottomSheetDialogFragment> filterBottomSheetFragment = new MutableLiveData<>();
    private final MutableLiveData<LinearProgressIndicator> filterProgressBar = new MutableLiveData<>();

    public ResultViewModel() {
    }

    public MutableLiveData<BottomSheetDialogFragment> getFilterBottomSheetFragment() {
        return filterBottomSheetFragment;
    }

    public MutableLiveData<String> getFilterType() {
        return filterType;
    }

    public MutableLiveData<FragmentManager> getFragmentManager() {
        return fragmentManager;
    }

    public MutableLiveData<LinearProgressIndicator> getFilterProgressBar() {
        return filterProgressBar;
    }

    public void setFilterType(String newFilterType) {
        filterType.setValue(newFilterType);
    }

    public void setFilterProgressBar(LinearProgressIndicator newProgressBar) {
        filterProgressBar.setValue(newProgressBar);
    }
}
