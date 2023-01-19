package com.example.asm3.controllers;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm3.AccountSettingActivity;
import com.example.asm3.AuthenticationActivity;
import com.example.asm3.R;
import com.example.asm3.SaleProgressActivity;
import com.example.asm3.base.adapter.GenericAdapter;
import com.example.asm3.base.adapter.viewHolder.BookHolder;
import com.example.asm3.base.adapter.viewHolder.OrderHolder;
import com.example.asm3.base.adapter.viewHolder.ReviewHolder;
import com.example.asm3.base.controller.BaseController;
import com.example.asm3.base.networking.services.AsyncTaskCallBack;
import com.example.asm3.base.networking.services.GetAuthenticatedData;
import com.example.asm3.config.Constant;
import com.example.asm3.config.Helper;
import com.example.asm3.fragments.mainActivity.MainViewModel;
import com.example.asm3.models.ApiList;
import com.example.asm3.fragments.mainActivity.ReviewDialogBody;
import com.example.asm3.models.Book;
import com.example.asm3.models.Customer;
import com.example.asm3.models.Order;
import com.example.asm3.models.OrderDetail;
import com.example.asm3.models.Review;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;

public class ProfileFragmentController extends BaseController implements
        MaterialButtonToggleGroup.OnButtonCheckedListener,
        View.OnClickListener,
        BookHolder.OnSelectListener,
        OrderHolder.OnSelectListener,
        ReviewHolder.OnSelectListener,
        AsyncTaskCallBack,
        DialogInterface.OnClickListener {

    private View view, profileNotifyLayout;
    private CardView profileAvatarLayout;
    private ImageView profileAvatarImg;
    private TextView profileUsernameTxt, profileEmailTxt;
    private RatingBar ratingBar;
    private MaterialButtonToggleGroup profileDataBtnGrp;
    private Button settingProfileBtn, sellingBtn, purchasedBtn, feedbackBtn, userLogoutBtn, salesBtn;
    private RecyclerView sellingRecView, purchasedRecView, feedbackRecView;
    private MaterialAlertDialogBuilder builder;
    private ReviewDialogBody reviewDialogBody;
    private GenericAdapter<Book> bookAdapter;
    private GenericAdapter<Order> orderAdapter;
    private GenericAdapter<Review> reviewAdapter;

    private MainViewModel mainViewModel;
    private LiveData<Customer> authCustomer;
    private MutableLiveData<ArrayList<Book>> sellingBooks;
    private MutableLiveData<ArrayList<Order>> orders;
    private MutableLiveData<ArrayList<Review>> reviews;
    private int mainLayoutId = R.id.mainActivity_fragmentContainerView;

    private ArrayList<Book> displayBooks;
    private ArrayList<Order> displayOrders;
    private ArrayList<Review> displayReviews;

    private String token;
    private GetAuthenticatedData getAuthenticatedData;

    public ProfileFragmentController(Context context, FragmentActivity activity, View view, ViewModel viewModel) {
        super(context, activity);
        this.view = view;
        this.mainViewModel = (MainViewModel) viewModel;

        if (isOnline()) {
            authCustomer = mainViewModel.getAuthCustomer();
            sellingBooks = mainViewModel.getBooks();
            orders = mainViewModel.getOrders();
            reviews = mainViewModel.getReviews();

            displayBooks = sellingBooks.getValue();
            displayOrders = orders.getValue();
            displayReviews = reviews.getValue();
        }
    }

    // Render functions
    @Override
    public void onInit() {
        if (!isOnline()) return;
        if (!isAuth()) {
            Helper.goToLogin(getContext(), getActivity());
        } else {
            // for testing
            token = getToken();

//            displayOrders.add(new Order("Lord of the ring", "23/06/2000", "dang giao", new Customer(), "New Book", 100000, 2, true));
//            displayOrders.add(new Order("Lord of the ring", "23/06/2000", "dang giao", new Customer(), "New Book", 100000, 2, true));
//            displayOrders.add(new Order("Lord of the ring", "23/06/2000", "dang giao", new Customer(), "New Book", 100000, 2, true));
//            displayOrders.add(new Order("Lord of the ring", "23/06/2000", "dang giao", new Customer(), "New Book", 100000, 2, true));
//            displayOrders.add(new Order("Lord of the ring", "23/06/2000", "dang giao", new Customer(), "New Book", 100000, 2, true));
//            displayOrders.add(new Order("Lord of the ring", "23/06/2000", "dang giao", new Customer(), "New Book", 100000, 2, true));
//            displayOrders.add(new Order("Lord of the ring", "23/06/2000", "dang giao", new Customer(), "New Book", 100000, 2, true));

            displayReviews.add(new Review("0", "hahahah no dai ghe truyen hya lam nha truyen hya lam nha truyen hya lam nha truyen hya lam nha truyen hya lam nha truyen hya lam nha", "22/02/2022", 3, new Customer("Quang"), new Order()));
            displayReviews.add(new Review("0", "hahahah no dai ghe truyen hya lam nha truyen hya lam nha truyen hya lam nha truyen hya lam nha truyen hya lam nha truyen hya lam nha", "22/02/2022", 3, new Customer("Quang"), new Order()));
            displayReviews.add(new Review("0", "hahahah no dai ghe truyen hya lam nha truyen hya lam nha truyen hya lam nha truyen hya lam nha truyen hya lam nha truyen hya lam nha", "22/02/2022", 3, new Customer("Quang"), new Order()));
            displayReviews.add(new Review("0", "ha lam nha truyen hya lam nha truyen hya lam nha truyen hya lam nha", "22/02/2022", 3, new Customer("Quang"), new Order()));
            displayReviews.add(new Review("0", "hh no dai ghe truyen hya lam nha truyen hya lam nha truyen hya lam nha truyen hya lam nha truyen hya lam nha truyen hya lam nha", "22/02/2022", 3, new Customer("Quang"), new Order()));
            displayReviews.add(new Review("0", "hahahah no dan hya lam nha truyen hya lam nha truyen hya lam nha truyen hya lam nha truyen hya lam nha truyen hya lam nha", "22/02/2022", 3, new Customer("Quang"), new Order()));
            displayReviews.add(new Review("0", "hahahah no dai ghe truyen hya lam nha truyen hya lam nha truyen hya lam nha truyen hya lam nha truyen hya lam nha truyen hya lam nha", "22/02/2022", 3, new Customer("Quang"), new Order()));
            // end testing

            profileAvatarLayout = view.findViewById(R.id.profileAvatarLayout);
            profileAvatarImg = view.findViewById(R.id.profileAvatarImg);
            profileUsernameTxt = view.findViewById(R.id.profileUsernameTxt);
            ratingBar = view.findViewById(R.id.ratingBar);
            settingProfileBtn = view.findViewById(R.id.settingProfileBtn);
            profileDataBtnGrp = view.findViewById(R.id.profileDataBtnGrp);
            sellingBtn = view.findViewById(R.id.sellingBtn);
            purchasedBtn = view.findViewById(R.id.purchasedBtn);
            feedbackBtn = view.findViewById(R.id.feedbackBtn);
            salesBtn = view.findViewById(R.id.salesBtn);
            sellingRecView = view.findViewById(R.id.sellingRecView);
            purchasedRecView = view.findViewById(R.id.purchasedRecView);
            feedbackRecView = view.findViewById(R.id.feedbackRecView);
            userLogoutBtn = view.findViewById(R.id.userLogoutBtn);
            profileEmailTxt = view.findViewById(R.id.profileEmailTxt);
            profileNotifyLayout = view.findViewById(R.id.profileNotifyLayout);
            bookAdapter = generateBookAdapter();
            orderAdapter = generateOrderAdapter();
            reviewAdapter = generateReviewAdapter();

            sellingBooks.observe(getActivity(), new Observer<ArrayList<Book>>() {
                @Override
                public void onChanged(ArrayList<Book> books) {
                    if (books.isEmpty()) {
                        sellingRecView.setVisibility(View.GONE);
                        profileNotifyLayout.setVisibility(View.VISIBLE);
                    } else {
                        displayBooks.clear();
                        displayBooks.addAll(books);
                        bookAdapter.notifyDataSetChanged();
                        sellingRecView.setVisibility(View.VISIBLE);
                        profileNotifyLayout.setVisibility(View.GONE);
                    }
                }
            });

            profileUsernameTxt.setText(authCustomer.getValue().getUsername());
            profileEmailTxt.setText(authCustomer.getValue().getEmail());
            profileDataBtnGrp.check(R.id.sellingBtn);

            profileAvatarLayout.setOnClickListener(this);
            settingProfileBtn.setOnClickListener(this);
            salesBtn.setOnClickListener(this);
            profileDataBtnGrp.addOnButtonCheckedListener(this);
            userLogoutBtn.setOnClickListener(this);
            profileEmailTxt.setOnClickListener(this);

            loadSelling();
            loadPurchased();
            loadFeedback();
        }
    }

    public void onResume() {
        if (isOnline()) getCustomerProducts();
    }

    @Override
    public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
        profileNotifyLayout.setVisibility(View.VISIBLE);
        sellingRecView.setVisibility(View.GONE);
        feedbackRecView.setVisibility(View.GONE);
        purchasedRecView.setVisibility(View.GONE);
        switch (group.getCheckedButtonId()) {
            case R.id.sellingBtn:
                if (!sellingBooks.getValue().isEmpty()) {
                    sellingRecView.setVisibility(View.VISIBLE);
                    profileNotifyLayout.setVisibility(View.GONE);
                }
                break;
            case R.id.purchasedBtn:
                if (!orders.getValue().isEmpty()) {
                    purchasedRecView.setVisibility(View.VISIBLE);
                    profileNotifyLayout.setVisibility(View.GONE);
                }
                break;
            case R.id.feedbackBtn:
                if (!reviews.getValue().isEmpty()) {
                    feedbackRecView.setVisibility(View.VISIBLE);
                    profileNotifyLayout.setVisibility(View.GONE);
                }
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
                Helper.goToBookDetail(getContext(), getActivity(), sellingBooks.getValue().get(position).get_id(), position);
                break;
        }
    }

    @Override
    public void onOrderClick(int position, View view) {
        if (!isOnline()) {
            showConnectDialog();
            return;
        }
        switch (view.getId()) {
            case R.id.orderBody:
                Log.d(TAG, "onOrderClick: test " + position);
                showDialog();
                break;
            case R.id.orderDeleteBtn:
                Log.d(TAG, "onOrderClick: test delete " + position);
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

    @Override
    public void onClick(View view) {
        if (!isOnline()) {
            showConnectDialog();
            return;
        }
        switch (view.getId()) {
            case R.id.settingProfileBtn:
                goToSetting();
                break;
            case R.id.profileAvatarLayout:
                break;
            case R.id.userLogoutBtn:
                getLocalFileController().writeFile(new ArrayList<>());
                mainViewModel.setAuthCustomer(null);
                Helper.loadFragment(mainViewModel.getFragmentManager().getValue(),
                        mainViewModel.getHomeFragment().getValue(),
                        "home", mainLayoutId);
                Helper.goToLogin(getContext(),getActivity());
                break;
            case R.id.profileEmailTxt:
                String uriText =
                        "mailto:" + profileEmailTxt.getText().toString() +
                                "?subject=" + Uri.encode("See you on Go Goat") +
                                "&body=" + Uri.encode("Hi, I'm " + profileUsernameTxt.getText().toString());

                Uri uri = Uri.parse(uriText);

                Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
                sendIntent.setData(uri);
                getActivity().startActivity(Intent.createChooser(sendIntent, "Send email"));
                break;
            case R.id.salesBtn:
                // TODO: go to sales activity
                Intent intent = new Intent(getContext(), SaleProgressActivity.class);
                intent.putExtra("data", authCustomer.getValue());
                getActivity().startActivityForResult(intent, Constant.salesProgressActivityCode);
                break;
        }

    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) { // onClick for dialog
        Log.d(TAG, "onClick: dialog click " + i);
        switch (i) {
            case -1: // submit
                float rating = reviewDialogBody.getRatingBar().getRating();
                String userReviewTxt = reviewDialogBody.getReviewTxt().getText().toString();
                Log.d(TAG, "onClick: dialog rating = " + rating);
                Log.d(TAG, "onClick: dialog user review text = " + userReviewTxt);
                break;
            case -2: // cancel
                break;
        }
    }

    // Helpers
    private GenericAdapter<Book> generateBookAdapter() {
        return new GenericAdapter<Book>(displayBooks) {
            @Override
            public RecyclerView.ViewHolder setViewHolder(ViewGroup parent) {
                final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
                return new BookHolder(view, ProfileFragmentController.this);
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

    private GenericAdapter<Order> generateOrderAdapter() {
        return new GenericAdapter<Order>(displayOrders) {
            @Override
            public RecyclerView.ViewHolder setViewHolder(ViewGroup parent) {
                final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
                return new OrderHolder(view, ProfileFragmentController.this);
            }

            @Override
            public void onBindData(RecyclerView.ViewHolder holder, Order item) {
                OrderHolder orderHolder = (OrderHolder) holder;
//                orderHolder.getOrderBookImg().setImageBitmap(Helper.stringToBitmap(item.get));
                orderHolder.getOrderBookTxt().setText(item.getBookName());
                orderHolder.getOrderQuantityTxt().setText("Quantity: " + item.getQuantity());
                orderHolder.getOrderPriceTxt().setText(item.getBookPrice() + " đ");
                orderHolder.getOrderDeleteBtn().setVisibility(View.GONE);
            }
        };
    }

    private GenericAdapter<Review> generateReviewAdapter() {
        return new GenericAdapter<Review>(displayReviews) {
            @Override
            public RecyclerView.ViewHolder setViewHolder(ViewGroup parent) {
                final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
                return new ReviewHolder(view, ProfileFragmentController.this);
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

    private void loadSelling() {
        sellingRecView.setAdapter(bookAdapter);
        sellingRecView.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        sellingRecView.addItemDecoration(new RecyclerView.ItemDecoration() {
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

    private void loadPurchased() {
        purchasedRecView.setAdapter(orderAdapter);
        purchasedRecView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void loadFeedback() {
        feedbackRecView.setAdapter(reviewAdapter);
        feedbackRecView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void showDialog() {
        reviewDialogBody = new ReviewDialogBody(getContext());
        builder = new MaterialAlertDialogBuilder(getContext());
        builder.setView(reviewDialogBody).
                setPositiveButton(R.string.submit, this).
                setNegativeButton(R.string.cancel, this).show();
    }

    // Request functions
    public void getCustomerProducts() {
        getAuthenticatedData = new GetAuthenticatedData(getContext(), this);
        getAuthenticatedData.setEndPoint(Constant.getUploadedProducts);
        getAuthenticatedData.setTaskType(Constant.getUploadedProductsTaskType);
        getAuthenticatedData.setToken(token);
        getAuthenticatedData.execute();
    }

    // Navigation functions
    public void goToSetting() {
        Intent intent = new Intent(getContext(), AccountSettingActivity.class);
        intent.putExtra("data", authCustomer.getValue());
        getActivity().startActivityForResult(intent, Constant.accSettingActivityCode);
    }

    // Callback functions
    @Override
    public void onFinished(String message, String taskType) {
        if (taskType.equals(Constant.getUploadedProductsTaskType)) {
            ApiList<Book> apiList = ApiList.fromJSON(ApiList.getData(message), Book.class);
            mainViewModel.setBooks(apiList.getList());
        }
    }

    @Override
    public void onError(String taskType) {

    }

    // Getter and Setter
}
