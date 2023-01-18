package com.example.asm3.fragments.searchResultActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.asm3.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.radiobutton.MaterialRadioButton;

public class FilterBottomSheetFragment extends BottomSheetDialogFragment implements RadioGroup.OnCheckedChangeListener {
    private RadioGroup filterRadioGroup;
    private MaterialRadioButton conditionNewUsedBtn, conditionUsedNewBtn, ratingHighLowBtn, ratingLowHighBtn, priceHighLowBtn, priceLowHighBtn;
    private ResultViewModel resultViewModel;

    public FilterBottomSheetFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resultViewModel = new ViewModelProvider(requireActivity()).get(ResultViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.filter_bottom_sheet, container, false);
        onInit(v, resultViewModel);
        return v;
    }

    public void onInit(View view, ViewModel viewModel) {
        filterRadioGroup = view.findViewById(R.id.filterRadioGroup);
        conditionNewUsedBtn = view.findViewById(R.id.conditionNewUsedBtn);
        conditionUsedNewBtn = view.findViewById(R.id.conditionUsedNewBtn);
        ratingHighLowBtn = view.findViewById(R.id.ratingHighLowBtn);
        ratingLowHighBtn = view.findViewById(R.id.ratingLowHighBtn);
        priceHighLowBtn = view.findViewById(R.id.priceHighLowBtn);
        priceLowHighBtn = view.findViewById(R.id.priceLowHighBtn);

        filterRadioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case 0:
                resultViewModel.setFilterType("conditionNewUsed");
                break;
            case 1:
                resultViewModel.setFilterType("conditionUsedNew");
                break;
            case 2:
                resultViewModel.setFilterType("ratingHighLow");
                break;
            case 3:
                resultViewModel.setFilterType("ratingLowHigh");
                break;
            case 4:
                resultViewModel.setFilterType("priceHighLow");
                break;
            case 5:
                resultViewModel.setFilterType("priceLowHigh");
                break;
        }

        resultViewModel.getFilterProgressBar().getValue().setVisibility(View.VISIBLE);
        dismiss();
    }
}
