package com.example.asm3.fragments.searchResultActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.asm3.R;
import com.example.asm3.config.Constant;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.radiobutton.MaterialRadioButton;

public class FilterBottomSheetFragment extends BottomSheetDialogFragment implements RadioGroup.OnCheckedChangeListener {
    private RadioGroup filterRadioGroup;
    private MaterialRadioButton conditionNewUsedBtn, conditionUsedNewBtn, ratingHighLowBtn, ratingLowHighBtn, priceHighLowBtn, priceLowHighBtn;
    private ResultViewModel resultViewModel;
    private String currentFilter;
    private TextView currentFilterTxt;

    public FilterBottomSheetFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resultViewModel = new ViewModelProvider(requireActivity()).get(ResultViewModel.class);
        currentFilter = resultViewModel.getFilterType().getValue();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.filter_bottom_sheet, container, false);
        onInit(v, resultViewModel);
        return v;
    }

    public void onInit(View view, ViewModel viewModel) {
        currentFilterTxt = view.findViewById(R.id.currentFilterTxt);
        filterRadioGroup = view.findViewById(R.id.filterRadioGroup);
        conditionNewUsedBtn = view.findViewById(R.id.conditionNewUsedBtn);
        conditionUsedNewBtn = view.findViewById(R.id.conditionUsedNewBtn);
        ratingHighLowBtn = view.findViewById(R.id.ratingHighLowBtn);
        ratingLowHighBtn = view.findViewById(R.id.ratingLowHighBtn);
        priceHighLowBtn = view.findViewById(R.id.priceHighLowBtn);
        priceLowHighBtn = view.findViewById(R.id.priceLowHighBtn);

        filterRadioGroup.setOnCheckedChangeListener(this);
        if (currentFilter.equals(Constant.conditionNewUsedKey)) {
            currentFilterTxt.setText("Current filter condition: " + getActivity().getResources().getString(R.string.filter_new_used));
        } else if (currentFilter.equals(Constant.conditionUsedNewKey)) {
            currentFilterTxt.setText("Current filter condition: " + getActivity().getResources().getString(R.string.filter_used_new));
        } else if (currentFilter.equals(Constant.ratingHighLowKey)) {
            currentFilterTxt.setText("Current filter rating: " + getActivity().getResources().getString(R.string.filter_high_low));
        } else if (currentFilter.equals(Constant.ratingLowHighKey)) {
            currentFilterTxt.setText("Current filter rating: " + getActivity().getResources().getString(R.string.filter_low_high));
        } else if (currentFilter.equals(Constant.priceHighLowKey)) {
            currentFilterTxt.setText("Current filter price: " + getActivity().getResources().getString(R.string.filter_high_low));
        } else if (currentFilter.equals(Constant.priceLowHighKey)) {
            currentFilterTxt.setText("Current filter price: " + getActivity().getResources().getString(R.string.filter_low_high));
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.conditionNewUsedBtn:
                resultViewModel.setFilterType(Constant.conditionNewUsedKey);
                break;
            case R.id.conditionUsedNewBtn:
                resultViewModel.setFilterType(Constant.conditionUsedNewKey);
                break;
            case R.id.ratingHighLowBtn:
                resultViewModel.setFilterType(Constant.ratingHighLowKey);
                break;
            case R.id.ratingLowHighBtn:
                resultViewModel.setFilterType(Constant.ratingLowHighKey);
                break;
            case R.id.priceHighLowBtn:
                resultViewModel.setFilterType(Constant.priceHighLowKey);
                break;
            case R.id.priceLowHighBtn:
                resultViewModel.setFilterType(Constant.priceLowHighKey);
                break;
        }
        dismiss();
    }
}
