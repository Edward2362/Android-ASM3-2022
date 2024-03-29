package com.example.asm3.controllers;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.asm3.ProfileActivity;
import com.example.asm3.R;
import com.example.asm3.SaleProgressActivity;
import com.example.asm3.base.adapter.GenericAdapter;
import com.example.asm3.base.adapter.viewHolder.BookHolder;
import com.example.asm3.base.adapter.viewHolder.OrderHolder;
import com.example.asm3.base.adapter.viewHolder.ReviewHolder;
import com.example.asm3.base.controller.BaseController;
import com.example.asm3.base.networking.services.AsyncTaskCallBack;
import com.example.asm3.base.networking.services.GetAuthenticatedData;
import com.example.asm3.base.networking.services.PostAuthenticatedData;
import com.example.asm3.config.Constant;
import com.example.asm3.config.Helper;
import com.example.asm3.fragments.mainActivity.MainViewModel;
import com.example.asm3.fragments.mainActivity.ReviewDialogBody;
import com.example.asm3.models.ApiList;
import com.example.asm3.models.Book;
import com.example.asm3.models.Customer;
import com.example.asm3.models.Order;
import com.example.asm3.models.Review;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
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
    private Bitmap customerPhoto;
    private String selectedOrderId;

    private MainViewModel mainViewModel;
    private LiveData<Customer> authCustomer;
    private MutableLiveData<ArrayList<Book>> sellingBooks;
    private MutableLiveData<ArrayList<Order>> orders;
    private MutableLiveData<ArrayList<Review>> reviews;
    private int mainLayoutId = R.id.mainActivity_fragmentContainerView;
    private int checkedBtnId = R.id.sellingBtn;
    private int selectedOrderIdInt = 0;

    private ArrayList<Book> displayBooks;
    private ArrayList<Order> displayOrders;
    private ArrayList<Review> displayReviews;

    private String token;
    private GetAuthenticatedData getAuthenticatedData;
    private PostAuthenticatedData postAuthenticatedData;

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
        if (isOnline()) {
            getCustomerProducts();
            getCustomerOrders();
            getAllCustomerReviews();
            authCustomer.observe(getActivity(), new Observer<Customer>() {
                @Override
                public void onChanged(Customer customer) {
                    if (authCustomer.getValue() != null) {

                        profileUsernameTxt.setText(authCustomer.getValue().getUsername());
                        profileEmailTxt.setText(authCustomer.getValue().getEmail());

                        if (!authCustomer.getValue().getAvatar().equals("")) {
                            profileAvatarImg.setImageBitmap(Helper.stringToBitmap(authCustomer.getValue().getAvatar()));
                        }

                        ratingBar.setRating(authCustomer.getValue().getRatings());
                    }
                }
            });
        }
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
                    if (checkedBtnId == R.id.sellingBtn) {
                        sellingRecView.setVisibility(View.VISIBLE);
                        profileNotifyLayout.setVisibility(View.GONE);
                    }
                }
            }
        });

        orders.observe(getActivity(), new Observer<ArrayList<Order>>() {
            @Override
            public void onChanged(ArrayList<Order> customerOrders) {
                displayOrders.clear();
                displayOrders.addAll(customerOrders);
                orderAdapter.notifyDataSetChanged();
                if (checkedBtnId == R.id.purchasedBtn) {
                    purchasedRecView.setVisibility(View.VISIBLE);
                    profileNotifyLayout.setVisibility(View.GONE);
                }
            }
        });

        reviews.observe(getActivity(), new Observer<ArrayList<Review>>() {
            @Override
            public void onChanged(ArrayList<Review> reviews) {
                displayReviews.clear();
                displayReviews.addAll(reviews);
                reviewAdapter.notifyDataSetChanged();
                if (checkedBtnId == R.id.feedbackBtn) {
                    feedbackRecView.setVisibility(View.VISIBLE);
                    profileNotifyLayout.setVisibility(View.GONE);
                }
            }
        });
        profileDataBtnGrp.check(R.id.sellingBtn);
    }

    public void getImageFromGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                getActivity().requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constant.galleryPermissionCode);
            } else {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                getActivity().startActivityForResult(intent, Constant.galleryRequest);
            }
        }
    }

    public void uploadReview(String content, float rating, String orderId) {
        try {
            postAuthenticatedData = new PostAuthenticatedData(getContext(), this);
            postAuthenticatedData.setEndPoint(Constant.uploadReview);
            postAuthenticatedData.setTaskType(Constant.uploadReviewTaskType);
            postAuthenticatedData.setToken(token);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Review.contentKey, content);
            jsonObject.put(Review.ratingKey, rating);
            jsonObject.put(Constant.orderIdKey, orderId);
            postAuthenticatedData.execute(jsonObject);
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
    }

    @Override
    public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
        profileNotifyLayout.setVisibility(View.VISIBLE);
        sellingRecView.setVisibility(View.GONE);
        feedbackRecView.setVisibility(View.GONE);
        purchasedRecView.setVisibility(View.GONE);
        switch (group.getCheckedButtonId()) {
            case R.id.sellingBtn:
                checkedBtnId = R.id.sellingBtn;
                if (!sellingBooks.getValue().isEmpty()) {
                    sellingRecView.setVisibility(View.VISIBLE);
                    profileNotifyLayout.setVisibility(View.GONE);
                }
                break;
            case R.id.purchasedBtn:
                checkedBtnId = R.id.purchasedBtn;
                if (!orders.getValue().isEmpty()) {
                    purchasedRecView.setVisibility(View.VISIBLE);
                    profileNotifyLayout.setVisibility(View.GONE);
                }
                break;
            case R.id.feedbackBtn:
                checkedBtnId = R.id.feedbackBtn;
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
                showDialog(displayOrders.get(position).getSeller().getUsername());
                selectedOrderId = displayOrders.get(position).get_id();
                selectedOrderIdInt = position;
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
                goToSellerProfile(displayReviews.get(position).getCustomer().get_id());
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
                getImageFromGallery();
                break;
            case R.id.userLogoutBtn:
                getLocalFileController().writeFile(new ArrayList<>());
                mainViewModel.setAuthCustomer(null);
                Helper.loadFragment(mainViewModel.getFragmentManager().getValue(),
                        mainViewModel.getHomeFragment().getValue(),
                        "home", mainLayoutId);
                Helper.goToLogin(getContext(), getActivity());
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
        switch (i) {
            case -1: // submit
                float rating = reviewDialogBody.getRatingBar().getRating();
                String userReviewTxt = reviewDialogBody.getReviewTxt().getText().toString();
                uploadReview(userReviewTxt, rating, selectedOrderId);
                displayOrders.get(selectedOrderIdInt).setHasReview(true);
                orderAdapter.notifyItemChanged(selectedOrderIdInt);
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
                orderHolder.getOrderBookTxt().setText(item.getBookName());
                orderHolder.getOrderQuantityTxt().setText("Quantity: " + item.getQuantity());
                if (item.isHasReview()) {
                    orderHolder.getOrderStatusTxt().setText("Status: Reviewed");
                } else {
                    orderHolder.getOrderStatusTxt().setText("Status: " + item.getStatus());
                }
                orderHolder.getOrderPriceTxt().setText(item.getBookPrice() + " đ");
                orderHolder.getOrderDeleteBtn().setVisibility(View.GONE);
                orderHolder.getOrderStatusTxt().setVisibility(View.VISIBLE);
                orderHolder.getOrderBookImg().setImageBitmap(Helper.stringToBitmap(item.getBookImage()));
                if (!item.getStatus().equalsIgnoreCase("completed") || item.isHasReview()) {
                    orderHolder.getOrderBody().setOnClickListener(null);
                }
                if (Helper.isDarkTheme(getContext())) {
                    orderHolder.getOrderPriceTxt().setTextColor(getActivity().getResources().getColor(R.color.md_theme_dark_onPrimaryContainer));
                    if (item.getStatus().equalsIgnoreCase("packaging"))
                        orderHolder.getOrderBody().setCardBackgroundColor(getActivity().getResources().getColor(R.color.dark_sale_status_packaging));
                    if (item.getStatus().equalsIgnoreCase("shipping"))
                        orderHolder.getOrderBody().setCardBackgroundColor(getActivity().getResources().getColor(R.color.dark_sale_status_shipping));
                    if (item.getStatus().equalsIgnoreCase("completed"))
                        orderHolder.getOrderBody().setCardBackgroundColor(getActivity().getResources().getColor(R.color.dark_sale_status_completed));
                    if (item.isHasReview())
                        orderHolder.getOrderBody().setCardBackgroundColor(getActivity().getResources().getColor(R.color.dark_sale_status_reviewed));
                } else {
                    orderHolder.getOrderPriceTxt().setTextColor(getActivity().getResources().getColor(R.color.md_theme_light_onSurface));
                    if (item.getStatus().equalsIgnoreCase("packaging"))
                        orderHolder.getOrderBody().setCardBackgroundColor(getActivity().getResources().getColor(R.color.light_sale_status_packaging));
                    if (item.getStatus().equalsIgnoreCase("shipping"))
                        orderHolder.getOrderBody().setCardBackgroundColor(getActivity().getResources().getColor(R.color.light_sale_status_shipping));
                    if (item.getStatus().equalsIgnoreCase("completed"))
                        orderHolder.getOrderBody().setCardBackgroundColor(getActivity().getResources().getColor(R.color.light_sale_status_completed));
                    if (item.isHasReview())
                        orderHolder.getOrderBody().setCardBackgroundColor(getActivity().getResources().getColor(R.color.light_sale_status_reviewed));
                }
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
                if (!item.getCustomer().getAvatar().equals("")) {
                    reviewHolder.getReviewUserImg().setImageBitmap(Helper.stringToBitmap(item.getCustomer().getAvatar()));
                }
                reviewHolder.getReviewUserNameTxt().setText(item.getCustomer().getUsername());
                reviewHolder.getReviewContentTxt().setText(item.getContent());
                reviewHolder.getReviewRatingBar().setRating(item.getRating());
                reviewHolder.getReviewDateTxt().setText(item.getDate());
            }
        };
    }

    private void goToSellerProfile(String sellerId) {
        Intent intent = new Intent(getContext(), ProfileActivity.class);
        intent.putExtra(Constant.publicProfileIdKey, sellerId);
        getActivity().startActivity(intent);
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

    private void showDialog(String sellerName) {
        reviewDialogBody = new ReviewDialogBody(getContext());
        reviewDialogBody.getReviewUsername().setText(sellerName);
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

    public void getCustomerOrders() {
        getAuthenticatedData = new GetAuthenticatedData(getContext(), this);
        getAuthenticatedData.setEndPoint(Constant.getCustomerOrders);
        getAuthenticatedData.setTaskType(Constant.getCustomerOrdersTaskType);
        getAuthenticatedData.setToken(token);
        getAuthenticatedData.execute();
    }

    public void changeAvatar(String customerAvatar) {
        try {
            postAuthenticatedData = new PostAuthenticatedData(getContext(), this);
            postAuthenticatedData.setEndPoint(Constant.changeAvatar);
            postAuthenticatedData.setTaskType(Constant.changeAvatarTaskType);
            postAuthenticatedData.setToken(token);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Customer.avatarKey, customerAvatar);
            postAuthenticatedData.execute(jsonObject);
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
    }

    public void getAllCustomerReviews() {
        getAuthenticatedData = new GetAuthenticatedData(getContext(), this);
        getAuthenticatedData.setEndPoint(Constant.getAllCustomerReviews);
        getAuthenticatedData.setTaskType(Constant.getAllCustomerReviewsTaskType);
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
        } else if (taskType.equals(Constant.getCustomerOrdersTaskType)) {
            ApiList<Order> apiList = ApiList.fromJSON(ApiList.getData(message), Order.class);
            mainViewModel.setOrders(apiList.getList());
        } else if (taskType.equals(Constant.changeAvatarTaskType)) {

        } else if (taskType.equals(Constant.uploadReviewTaskType)) {

        } else if (taskType.equals(Constant.getAllCustomerReviewsTaskType)) {
            ApiList<Review> apiList = ApiList.fromJSON(ApiList.getData(message), Review.class);
            mainViewModel.setReviews(apiList.getList());
        }
    }

    @Override
    public void onError(String taskType) {

    }

    public void onProfileFragmentActivityResult(Intent data) {
        try {
            final Uri imageUri = data.getData();
            final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
            customerPhoto = BitmapFactory.decodeStream(imageStream);
            customerPhoto = Bitmap.createScaledBitmap(customerPhoto, (int) (customerPhoto.getWidth() * 0.3), (int) (customerPhoto.getHeight() * 0.3), true);
            profileAvatarImg.setImageBitmap(customerPhoto);
            changeAvatar(Helper.bitmapToString(customerPhoto));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
        }
    }
    // Getter and Setter
}
