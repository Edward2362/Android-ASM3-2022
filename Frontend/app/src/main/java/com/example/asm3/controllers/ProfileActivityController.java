package com.example.asm3.controllers;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm3.R;
import com.example.asm3.base.adapter.GenericAdapter;
import com.example.asm3.base.adapter.viewHolder.BookHolder;
import com.example.asm3.base.adapter.viewHolder.ReviewHolder;
import com.example.asm3.base.controller.BaseController;
import com.example.asm3.base.networking.services.AsyncTaskCallBack;
import com.example.asm3.base.networking.services.GetData;
import com.example.asm3.config.Constant;
import com.example.asm3.config.Helper;
import com.example.asm3.custom.components.TopBarView;
import com.example.asm3.models.ApiData;
import com.example.asm3.models.ApiList;
import com.example.asm3.models.Book;
import com.example.asm3.models.CartItem;
import com.example.asm3.models.Category;
import com.example.asm3.models.Customer;
import com.example.asm3.models.Review;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.ArrayList;

public class ProfileActivityController extends BaseController implements
        View.OnClickListener,
        MaterialButtonToggleGroup.OnButtonCheckedListener,
        BookHolder.OnSelectListener,
        ReviewHolder.OnSelectListener,
        AsyncTaskCallBack {
    // views
    private View publicProfileNotifyLayout;
    private TopBarView publicProfileTopBar;
    private Button backBtn;
    private LinearProgressIndicator profileProgressBar;
    private LinearLayout publicProfileBody;
    private CardView publicProfileAvatarLayout;
    private ImageView publicProfileAvatarImg;
    private TextView publicProfileUsernameTxt, publicProfileEmailTxt;
    private RatingBar publicProfileRatingBar;
    private MaterialButtonToggleGroup publicProfileDataBtnGrp;
    private Button publicProfileSellingBtn, publicProfileFeedbackBtn;
    private RecyclerView publicProfileSellingRecView, publicProfileFeedbackRecView;
    private MutableLiveData<ArrayList<Book>> sellingBooks = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<ArrayList<Review>> reviews = new MutableLiveData<>(new ArrayList<>());
    private ArrayList<Book> displayBooks;
    private ArrayList<Review> displayReviews;
    private GenericAdapter<Book> bookAdapter;
    private GenericAdapter<Review> reviewAdapter;
    private GetData getData;

    // data
    private String publicCustomerId;
    private Customer publicCustomer;

    public ProfileActivityController(Context context, FragmentActivity activity) {
        super(context, activity);

        displayBooks = sellingBooks.getValue();
        displayReviews = reviews.getValue();

    }

    // Render functions
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onInit() {
        publicProfileNotifyLayout = getActivity().findViewById(R.id.publicProfileNotifyLayout);
        publicProfileTopBar = getActivity().findViewById(R.id.publicProfileTopBar);
        backBtn = publicProfileTopBar.getBackButton();
        profileProgressBar = getActivity().findViewById(R.id.profileProgressBar);
        publicProfileBody = getActivity().findViewById(R.id.publicProfileBody);
        publicProfileAvatarLayout = getActivity().findViewById(R.id.publicProfileAvatarLayout);
        publicProfileAvatarImg = getActivity().findViewById(R.id.publicProfileAvatarImg);
        publicProfileUsernameTxt = getActivity().findViewById(R.id.publicProfileUsernameTxt);
        publicProfileEmailTxt = getActivity().findViewById(R.id.publicProfileEmailTxt);
        publicProfileRatingBar = getActivity().findViewById(R.id.publicProfileRatingBar);
        publicProfileDataBtnGrp = getActivity().findViewById(R.id.publicProfileDataBtnGrp);
        publicProfileSellingBtn = getActivity().findViewById(R.id.publicProfileSellingBtn);
        publicProfileFeedbackBtn = getActivity().findViewById(R.id.publicProfileFeedbackBtn);
        publicProfileSellingRecView = getActivity().findViewById(R.id.publicProfileSellingRecView);
        publicProfileFeedbackRecView = getActivity().findViewById(R.id.publicProfileFeedbackRecView);

        publicProfileTopBar.setSubPage("");

        bookAdapter = generateBookAdapter();
        reviewAdapter = generateReviewAdapter();

        sellingBooks.observe(getActivity(), new Observer<ArrayList<Book>>() {
            @Override
            public void onChanged(ArrayList<Book> books) {
                if (books.isEmpty()) {
                    publicProfileSellingRecView.setVisibility(View.GONE);
                    publicProfileNotifyLayout.setVisibility(View.VISIBLE);
                } else {
                    displayBooks.clear();
                    displayBooks.addAll(books);
                    bookAdapter.notifyDataSetChanged();
                    publicProfileSellingRecView.setVisibility(View.VISIBLE);
                    publicProfileNotifyLayout.setVisibility(View.GONE);
                }
            }
        });

        reviews.observe(getActivity(), new Observer<ArrayList<Review>>() {
            @Override
            public void onChanged(ArrayList<Review> reviews) {
                if (reviews.isEmpty()) {
                    publicProfileSellingRecView.setVisibility(View.GONE);
                    publicProfileNotifyLayout.setVisibility(View.VISIBLE);
                } else {
                    displayReviews.clear();
                    displayReviews.addAll(reviews);
                    reviewAdapter.notifyDataSetChanged();
                    publicProfileFeedbackRecView.setVisibility(View.VISIBLE);
                    publicProfileNotifyLayout.setVisibility(View.GONE);
                }
            }
        });

        // get extras
        Intent intent = getActivity().getIntent();
        publicCustomerId = intent.getStringExtra(Constant.publicProfileIdKey);

//        // testing
//        publicCustomer = new Customer("001", "roo@gmal.com", "nijndc@1", "Roo", "12 Baker St", "user", 4, new ArrayList<CartItem>(), "");
//        bindData();

        backBtn.setOnClickListener(this);
        publicProfileAvatarLayout.setOnClickListener(this);
        publicProfileDataBtnGrp.addOnButtonCheckedListener(this);
        publicProfileEmailTxt.setOnClickListener(this);

        loadSelling();
        loadFeedback();


        if (isOnline()) {
//             books = getBooks();
//             reviews = getReviews();
            getProfileCustomer(publicCustomerId);
            getPublicCustomerProducts(publicCustomerId);
            getAllPublicCustomerReviews(publicCustomerId);
        }
    }

    @Override
    public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
        publicProfileNotifyLayout.setVisibility(View.VISIBLE);
        publicProfileSellingRecView.setVisibility(View.GONE);
        publicProfileFeedbackRecView.setVisibility(View.GONE);
        Log.d(TAG, "onButtonChecked: test " + isChecked);
        switch (group.getCheckedButtonId()) {
            case R.id.publicProfileSellingBtn:
                if (!sellingBooks.getValue().isEmpty()) {
                    publicProfileSellingRecView.setVisibility(View.VISIBLE);
                    publicProfileNotifyLayout.setVisibility(View.GONE);
                }
                break;
            case R.id.publicProfileFeedbackBtn:
                if (!reviews.getValue().isEmpty()) {
                    publicProfileFeedbackRecView.setVisibility(View.VISIBLE);
                    publicProfileNotifyLayout.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                getActivity().finish();
                break;
            case R.id.publicProfileAvatarLayout:
                break;
            case R.id.publicProfileEmailTxt:
                String uriText =
                        "mailto:" + publicProfileEmailTxt.getText().toString() +
                                "?subject=" + Uri.encode("See you on Go Goat") +
                                "&body=" + Uri.encode("Hi, I'm " + publicProfileUsernameTxt.getText().toString());

                Uri uri = Uri.parse(uriText);

                Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
                sendIntent.setData(uri);
                getActivity().startActivity(Intent.createChooser(sendIntent, "Send email"));
                break;
        }
    }

    @Override
    public void onBookClick(int position, View view) {
        if (!isOnline()) {
            showConnectDialog();
            return;
        }
        switch (view.getId()) {
            case R.id.productBody:
                Helper.goToBookDetail(getContext(), getActivity(), displayBooks.get(position).get_id(), position);
                break;
        }
    }

    @Override
    public void onAvatarClick(int position, View view) {
        if (!isOnline()) {
            showConnectDialog();
            return;
        }
        switch (view.getId()) {
            case R.id.reviewUserImgLayout:
                Log.d(TAG, "onAvatarClick: test " + position);
                break;
        }
    }

    // Helpers
    private GenericAdapter<Review> generateReviewAdapter() {
        return new GenericAdapter<Review>(displayReviews) {
            @Override
            public RecyclerView.ViewHolder setViewHolder(ViewGroup parent) {
                final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
                return new ReviewHolder(view, ProfileActivityController.this);
            }

            @Override
            public void onBindData(RecyclerView.ViewHolder holder, Review item) {
                ReviewHolder reviewHolder = (ReviewHolder) holder;
//                reviewHolder.getReviewUserImg().setImageBitmap(Helper.stringToBitmap(item.getCustomer().getAvatar()));
                reviewHolder.getReviewUserNameTxt().setText(item.getCustomer().getUsername());
                reviewHolder.getReviewContentTxt().setText(item.getContent());
                reviewHolder.getReviewRatingBar().setRating(item.getRating());
                reviewHolder.getReviewDateTxt().setText(item.getDate());
            }
        };
    }

    private GenericAdapter<Book> generateBookAdapter() {
        return new GenericAdapter<Book>(displayBooks) {
            @Override
            public RecyclerView.ViewHolder setViewHolder(ViewGroup parent) {
                final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
                return new BookHolder(view, ProfileActivityController.this);
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

    private void loadSelling() {
        publicProfileSellingRecView.setAdapter(bookAdapter);
        publicProfileSellingRecView.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        publicProfileSellingRecView.addItemDecoration(new RecyclerView.ItemDecoration() {
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

    private void loadFeedback() {
        publicProfileFeedbackRecView.setAdapter(reviewAdapter);
        publicProfileFeedbackRecView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void bindData() {
        publicProfileBody.setVisibility(View.VISIBLE);
        publicProfileUsernameTxt.setText(publicCustomer.getUsername());
        publicProfileEmailTxt.setText(publicCustomer.getEmail());
        publicProfileAvatarImg.setImageBitmap(Helper.stringToBitmap(publicCustomer.getAvatar()));
        publicProfileDataBtnGrp.check(R.id.publicProfileSellingBtn);
        publicProfileSellingRecView.setVisibility(View.VISIBLE);
    }

    // Request functions
    public void getProfileCustomer(String id) {
        getData = new GetData(getContext(),this);
        getData.setEndPoint(Constant.getProfileCustomer + "/" + id);
        getData.setTaskType(Constant.getProfileCustomerTaskType);
        getData.execute();

    }

    public void getPublicCustomerProducts(String id) {
        getData = new GetData(getContext(),this);
        getData.setEndPoint(Constant.getPublicCustomerProducts + "/" + id);
        getData.setTaskType(Constant.getPublicCustomerProductsTaskType);
        getData.execute();
    }

    public void getAllPublicCustomerReviews(String id){
        getData = new GetData(getContext(),this);
        getData.setEndPoint(Constant.getAllPublicCustomerReviews + "/" + id);
        getData.setTaskType(Constant.getAllCustomerReviewsTaskType);
        getData.execute();
    }

    // Navigation functions


    // Callback functions
    @Override
    public void onFinished(String message, String taskType) {
        if (taskType.equals(Constant.getProfileCustomerTaskType)){
            ApiData<Customer> apiData = ApiData.fromJSON(ApiData.getData(message),Customer.class);
            publicCustomer = apiData.getData();
            bindData();
            profileProgressBar.setVisibility(View.GONE);
        } else if (taskType.equals(Constant.getPublicCustomerProductsTaskType)){
            ApiList<Book> apiList = ApiList.fromJSON(ApiList.getData(message),Book.class);
            sellingBooks.setValue(apiList.getList());
        } else if (taskType.equals(Constant.getAllCustomerReviewsTaskType)){
            Log.d("dadadsa ","test " + message);
            ApiList<Review> apiList = ApiList.fromJSON(ApiList.getData(message),Review.class);
            reviews.setValue(apiList.getList());
        }
    }

    @Override
    public void onError(String taskType) {

    }

    // Getter and setter
}
