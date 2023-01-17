package com.example.asm3.controllers;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm3.AccountSettingActivity;
import com.example.asm3.AuthenticationActivity;
import com.example.asm3.R;
import com.example.asm3.base.adapter.GenericAdapter;
import com.example.asm3.base.adapter.viewHolder.BookHolder;
import com.example.asm3.base.controller.BaseController;
import com.example.asm3.config.Constant;
import com.example.asm3.fragments.mainActivity.MainViewModel;
import com.example.asm3.models.Book;
import com.example.asm3.models.Customer;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.ArrayList;

public class ProfileFragmentController extends BaseController implements
        MaterialButtonToggleGroup.OnButtonCheckedListener,
        View.OnClickListener,
        BookHolder.OnSelectListener {

    private View view;
    private ImageView profileAvatarImg;
    private TextView profileUsernameTxt;
    private RatingBar ratingBar;
    private MaterialButtonToggleGroup profileDataBtnGrp;
    private Button settingProfileBtn, sellingBtn, purchasedBtn, feedbackBtn;
    private RecyclerView profileRecView;
    private GenericAdapter<Book> bookAdapter;

    private MainViewModel mainViewModel;
    private LiveData<Customer> authCustomer;
    private ArrayList<Book> sellingBooks;

    public ProfileFragmentController(Context context, FragmentActivity activity, View view, ViewModel viewModel) {
        super(context, activity);
        this.view = view;
        this.mainViewModel = (MainViewModel) viewModel;

        authCustomer = mainViewModel.getAuthCustomer();
        sellingBooks = new ArrayList<>();
    }

    // Render functions
    @Override
    public void onInit() {
        if (!isAuth()) {
            goToLogin();
        } else {
            // for testing

            sellingBooks.add(new Book("Lord of the ring", "J. R. R. Tolkien", null, 100000, 0, null, null, null, null, null));
            sellingBooks.add(new Book("Lord of the ring", "J. R. R. Tolkien", null, 100000, 0, null, null, null, null, null));
            sellingBooks.add(new Book("Lord of the ring", "J. R. R. Tolkien", null, 100000, 0, null, null, null, null, null));
            sellingBooks.add(new Book("Lord of the ring", "J. R. R. Tolkien", null, 100000, 0, null, null, null, null, null));
            sellingBooks.add(new Book("Lord of the ring", "J. R. R. Tolkien", null, 100000, 0, null, null, null, null, null));
            sellingBooks.add(new Book("Lord of the ring", "J. R. R. Tolkien", null, 100000, 0, null, null, null, null, null));
            sellingBooks.add(new Book("Lord of the ring", "J. R. R. Tolkien", null, 100000, 0, null, null, null, null, null));
            sellingBooks.add(new Book("Lord of the ring", "J. R. R. Tolkien", null, 100000, 0, null, null, null, null, null));
            sellingBooks.add(new Book("Lord of the ring", "J. R. R. Tolkien", null, 100000, 0, null, null, null, null, null));
            sellingBooks.add(new Book("Lord of the ring", "J. R. R. Tolkien", null, 100000, 0, null, null, null, null, null));

            // end testing

            profileAvatarImg = view.findViewById(R.id.profileAvatarImg);
            profileUsernameTxt = view.findViewById(R.id.profileUsernameTxt);
            ratingBar = view.findViewById(R.id.ratingBar);
            settingProfileBtn = view.findViewById(R.id.settingProfileBtn);
            profileDataBtnGrp = view.findViewById(R.id.profileDataBtnGrp);
            sellingBtn = view.findViewById(R.id.sellingBtn);
            purchasedBtn = view.findViewById(R.id.purchasedBtn);
            feedbackBtn = view.findViewById(R.id.feedbackBtn);
            profileRecView = view.findViewById(R.id.profileRecView);
            bookAdapter = generateBookAdapter();

            profileUsernameTxt.setText(authCustomer.getValue().getUsername());

            settingProfileBtn.setOnClickListener(this);
            profileDataBtnGrp.addOnButtonCheckedListener(this);
            profileRecView.setAdapter(bookAdapter);
            profileRecView.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
            profileRecView.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                    super.getItemOffsets(outRect, view, parent, state);
                    if (parent.getChildLayoutPosition(view) % 2 == 0) {
                        outRect.set(0, 0, 20, 0);
                    } else {
                        outRect.set(20, 0, 0, 0);
                    }
                }
            });
        }
    }

    @Override
    public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {

    }

    @Override
    public void onBookClick(int position, View view) {
        switch (view.getId()) {
            case R.id.productBody:
                Log.d(TAG, "onBookClick: test " + position);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.settingProfileBtn:
                goToSetting();
                break;
        }

    }

    // Helpers
    private GenericAdapter<Book> generateBookAdapter() {
        return new GenericAdapter<Book>(sellingBooks) {
            @Override
            public RecyclerView.ViewHolder setViewHolder(ViewGroup parent) {
                final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
                return new BookHolder(view, ProfileFragmentController.this);
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


    // Navigation functions
    public void goToLogin() {
        Intent intent = new Intent(getContext(), AuthenticationActivity.class);
        getActivity().startActivityForResult(intent, Constant.authActivityCode);
    }

    public void goToSetting(){
        Intent intent =  new Intent(getContext(), AccountSettingActivity.class);
        getActivity().startActivityForResult(intent, Constant.accSettingActivityCode);
    }

    // Callback functions


    // Getter and Setter
}
