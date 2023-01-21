package com.example.asm3.controllers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm3.ManageBookActivity;
import com.example.asm3.ProductDetailActivity;

import com.example.asm3.R;
import com.example.asm3.base.adapter.GenericAdapter;
import com.example.asm3.base.adapter.viewHolder.SubCategoryHolder;
import com.example.asm3.base.controller.BaseController;
import com.example.asm3.base.networking.services.AsyncTaskCallBack;
import com.example.asm3.base.networking.services.DeleteAuthenticatedData;
import com.example.asm3.base.networking.services.GetData;
import com.example.asm3.base.networking.services.PostAuthenticatedData;
import com.example.asm3.config.Constant;
import com.example.asm3.config.Helper;
import com.example.asm3.custom.components.TopBarView;
import com.example.asm3.models.ApiData;
import com.example.asm3.models.ApiList;
import com.example.asm3.models.Book;
import com.example.asm3.models.Category;
import com.example.asm3.models.Customer;
import com.example.asm3.models.SubCategory;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class ManageBookActivityController extends BaseController implements
        AsyncTaskCallBack,
        MaterialButtonToggleGroup.OnButtonCheckedListener,
        SubCategoryHolder.OnSelectListener,
        View.OnClickListener {

    private Book book;
    private PostAuthenticatedData postAuthenticatedData;
    private DeleteAuthenticatedData deleteAuthenticatedData;
    private GetData getData;
    private ArrayList<Category> categories;
    private ArrayList<SubCategory> foreign = new ArrayList<>();
    private ArrayList<SubCategory> domestic = new ArrayList<>();
    private ArrayList<SubCategory> text = new ArrayList<>();
    private ArrayList<SubCategory> displayList = new ArrayList<>();
    private Category selectedCategory;
    private Bitmap productPhoto;

    private MaterialButtonToggleGroup categoriesBtnGrp;
    private TopBarView manageTopBar;
    private Button backBtn;
    private TextView cateNotifyTxt, manageProductCateErrorTxt, manageProductSubCateErrorTxt;
    private View subCateTopDivider, bookUpdateLayout, bookInfoLayout, manageProgressBar;
    private GenericAdapter<SubCategory> subCateAdapter;
    private RecyclerView subCateRecView;
    private CardView productImageLayout;
    private ImageView productView;
    private Button uploadProduct;
    private Button updateProduct;
    private Button removeProduct;
    private TextInputLayout productNameLayout, authorLayout, publishedAtLayout, descriptionLayout, quantityLayout, priceLayout;
    private TextInputEditText productNameEt, authorRegisEt, descriptionEt, priceEt, quantityEt, publishedAtEt;
    private RadioButton newProduct;
    private RadioButton usedProduct;
    private String productId;

    private String customerId;

    private String token;

    public ManageBookActivityController(Context context, FragmentActivity activity) {
        super(context, activity);
    }

    // render functions
    @Override
    public void onInit() {
        if (!isAuth()) {
            Helper.goToLogin(getContext(), getActivity());
        } else {
            token = getToken();
            getAllCategories();

            manageTopBar = getActivity().findViewById(R.id.manageTopBar);
            backBtn = getActivity().findViewById(R.id.backButton);
            categoriesBtnGrp = getActivity().findViewById(R.id.manageProductCategoriesBtnGrp);
            subCateRecView = getActivity().findViewById(R.id.manageProductSubCateRecView);
            cateNotifyTxt = getActivity().findViewById(R.id.manageProductCateNotifyTxt);
            manageProductCateErrorTxt = getActivity().findViewById(R.id.manageProductCateErrorTxt);
            manageProductSubCateErrorTxt = getActivity().findViewById(R.id.manageProductSubCateErrorTxt);
            subCateTopDivider = getActivity().findViewById(R.id.manageProductSubCateTopDivider);
            productView = getActivity().findViewById(R.id.productImage);
            productImageLayout = getActivity().findViewById(R.id.productImageLayout);
            uploadProduct = getActivity().findViewById(R.id.uploadProduct);
            updateProduct = getActivity().findViewById(R.id.updateProduct);
            removeProduct = getActivity().findViewById(R.id.removeProduct);
            productNameLayout = getActivity().findViewById(R.id.productNameLayout);
            authorLayout = getActivity().findViewById(R.id.authorLayout);
            descriptionLayout = getActivity().findViewById(R.id.descriptionLayout);
            priceLayout = getActivity().findViewById(R.id.priceLayout);
            quantityLayout = getActivity().findViewById(R.id.quantityLayout);
            publishedAtLayout = getActivity().findViewById(R.id.publishedAtLayout);
            productNameEt = getActivity().findViewById(R.id.productNameEt);
            authorRegisEt = getActivity().findViewById(R.id.authorRegisEt);
            descriptionEt = getActivity().findViewById(R.id.descriptionEt);
            priceEt = getActivity().findViewById(R.id.priceEt);
            quantityEt = getActivity().findViewById(R.id.quantityEt);
            publishedAtEt = getActivity().findViewById(R.id.publishedAtEt);
            newProduct = getActivity().findViewById(R.id.newProduct);
            usedProduct = getActivity().findViewById(R.id.usedProduct);
            bookInfoLayout = getActivity().findViewById(R.id.bookInfoLayout);
            bookUpdateLayout = getActivity().findViewById(R.id.bookUpdateLayout);
            manageProgressBar = getActivity().findViewById(R.id.manageProgressBar);
            subCateAdapter = generateSubCateAdapter();

            categoriesBtnGrp.addOnButtonCheckedListener(this);
            subCateRecView.setAdapter(subCateAdapter);
            subCateRecView.setLayoutManager(new LinearLayoutManager(getContext()));

            productImageLayout.setOnClickListener(this);
            backBtn.setOnClickListener(this);
            uploadProduct.setOnClickListener(this);
            updateProduct.setOnClickListener(this);
            removeProduct.setOnClickListener(this);

            if (isUpload()) {
                manageTopBar.setSubPage("Post Book");
                productPhoto = ((BitmapDrawable) productView.getDrawable()).getBitmap();
                uploadProduct.setVisibility(View.VISIBLE);
            } else {
                manageTopBar.setSubPage("Update Book");
                bookInfoLayout.setVisibility(View.INVISIBLE);
                bookUpdateLayout.setVisibility(View.INVISIBLE);
                manageProgressBar.setVisibility(View.VISIBLE);
                setProductId();
                getProduct(productId);
            }
        }
    }

    @Override
    public void onSubCateClick(int position, View view, MaterialCheckBox subCateCheckBox) {
        boolean newStatus = false;
        switch (view.getId()) {
            case R.id.addresses:
                newStatus = !subCateCheckBox.isChecked();
                subCateCheckBox.setChecked(newStatus);
                break;
            case R.id.subCateCheckBox:
                newStatus = subCateCheckBox.isChecked();
                break;
        }
        displayList.get(position).setChosen(newStatus);
    }

    @Override
    public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
        manageProductCateErrorTxt.setVisibility(View.GONE);
        subCateTopDivider.setVisibility(View.VISIBLE);
        cateNotifyTxt.setVisibility(View.GONE);
        subCateRecView.setVisibility(View.VISIBLE);
        switch (group.getCheckedButtonId()) {
            case R.id.manageProductForeignCateBtn:
                displayList.clear();
                displayList.addAll(foreign);
                selectedCategory = categories.get(0);
                subCateAdapter.notifyDataSetChanged();
                break;
            case R.id.manageProductDomesticCateBtn:
                displayList.clear();
                displayList.addAll(domestic);
                selectedCategory = categories.get(1);
                subCateAdapter.notifyDataSetChanged();
                break;
            case R.id.manageProductTextCateBtn:
                displayList.clear();
                displayList.addAll(text);
                selectedCategory = categories.get(2);
                subCateAdapter.notifyDataSetChanged();
                break;
        }
        if (group.getCheckedButtonId() == -1) {
            subCateTopDivider.setVisibility(View.GONE);
            cateNotifyTxt.setVisibility(View.VISIBLE);
            subCateRecView.setVisibility(View.GONE);
        }
    }

    // helper functions
    private GenericAdapter<SubCategory> generateSubCateAdapter() {
        return new GenericAdapter<SubCategory>(displayList) {
            @Override
            public RecyclerView.ViewHolder setViewHolder(ViewGroup parent) {
                final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_category_item, parent, false);
                return new SubCategoryHolder(view, ManageBookActivityController.this);
            }

            @Override
            public void onBindData(RecyclerView.ViewHolder holder, SubCategory item) {
                SubCategoryHolder subCategoryHolder = (SubCategoryHolder) holder;
                subCategoryHolder.getSubCateTxt().setText(item.getName().replace("+", " "));
                subCategoryHolder.getSubCateCheckBox().setChecked(item.isChosen());
            }
        };
    }

    public boolean isUpload() {
        Intent intent = getActivity().getIntent();
        if (intent.getExtras().get(Constant.isUploadKey) != null) {
            if ((Integer.parseInt(intent.getExtras().get(Constant.isUploadKey).toString()) == Constant.uploadCode)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void setProductId() {
        Intent intent = getActivity().getIntent();
        productId = intent.getExtras().get(Constant.productIdKey).toString();
    }

    public void openCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                getActivity().requestPermissions(new String[]{Manifest.permission.CAMERA}, Constant.cameraPermissionCode);
            } else {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                getActivity().startActivityForResult(intent, Constant.cameraRequest);
            }
        }
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

    private void setCategoriesGrp(int cateBtn) {
        categoriesBtnGrp.check(cateBtn);
    }

    public void onUploadProduct() {
        if (validated()) {
            manageProgressBar.setVisibility(View.VISIBLE);
            uploadProduct.setEnabled(false);

            String productName = "";
            String productAuthor = "";
            String productDescription = "";
            float productPrice = 0F;
            int productQuantity = 0;
            String productPublishedAt = "";
            Category productCategory = new Category();
            ArrayList<SubCategory> productSubCategory = new ArrayList<SubCategory>();
            Customer productCustomer = new Customer();
            productId = "";
            boolean isProductNew = false;
            String productImage = "";

            productName = productNameEt.getText().toString();
            productAuthor = authorRegisEt.getText().toString();
            productDescription = descriptionEt.getText().toString();
            productPrice = Float.parseFloat(priceEt.getText().toString());
            productQuantity = Integer.parseInt(quantityEt.getText().toString());
            productPublishedAt = publishedAtEt.getText().toString();
            productCategory.set_id(selectedCategory.get_id());
            for (int i = 0; i < displayList.size(); ++i) {
                if (displayList.get(i).isChosen()) {
                    SubCategory subCategory = new SubCategory();
                    subCategory.set_id(displayList.get(i).get_id());
                    productSubCategory.add(subCategory);
                }
            }

            if (newProduct.isChecked()) {
                isProductNew = true;
            }

            productImage = Helper.bitmapToString(productPhoto);

            Book product = new Book(productName, productAuthor, productDescription, productPrice, productQuantity, productPublishedAt, productCategory, productSubCategory, productCustomer, productId, isProductNew, productImage);
            uploadBook(product);
        }
    }

    public void onUpdateProduct() {
        if (validated()) {
            manageProgressBar.setVisibility(View.VISIBLE);
            updateProduct.setEnabled(false);
            removeProduct.setEnabled(false);

            String productName = "";
            String productAuthor = "";
            String productDescription = "";
            float productPrice = 0F;
            int productQuantity = 0;
            String productPublishedAt = "";
            Category productCategory = new Category();
            ArrayList<SubCategory> productSubCategory = new ArrayList<SubCategory>();
            Customer productCustomer = new Customer();
            productCustomer.set_id(customerId);
            boolean isProductNew = false;
            String productImage = "";

            productName = productNameEt.getText().toString();
            productAuthor = authorRegisEt.getText().toString();
            productDescription = descriptionEt.getText().toString();
            productPrice = Float.parseFloat(priceEt.getText().toString());
            productQuantity = Integer.parseInt(quantityEt.getText().toString());
            productPublishedAt = publishedAtEt.getText().toString();
            productCategory.set_id(selectedCategory.get_id());
            for (int i = 0; i < displayList.size(); ++i) {
                if (displayList.get(i).isChosen()) {
                    SubCategory subCategory = new SubCategory();
                    subCategory.set_id(displayList.get(i).get_id());
                    productSubCategory.add(subCategory);
                }
            }

            if (newProduct.isChecked()) {
                isProductNew = true;
            }

            productImage = Helper.bitmapToString(productPhoto);
            Book product = new Book(productName, productAuthor, productDescription, productPrice, productQuantity, productPublishedAt, productCategory, productSubCategory, productCustomer, productId, isProductNew, productImage);
            updateBook(product);
        }
    }

    public void onRemoveProduct() {
        manageProgressBar.setVisibility(View.VISIBLE);
        updateProduct.setEnabled(false);
        removeProduct.setEnabled(false);
        deleteBook(productId);
    }

    private boolean validated() {
        int selectedCate = 0;
        int selectedSubCate = 0;

        for (SubCategory subCate : displayList) {
            if (subCate.isChosen()) {
                selectedSubCate = 1;
            }
        }

        if (selectedCategory != null) {
            selectedCate = 1;
            manageProductCateErrorTxt.setVisibility(View.GONE);
        } else {
            manageProductCateErrorTxt.setVisibility(View.VISIBLE);
            cateNotifyTxt.setVisibility(View.GONE);
        }

        if (selectedSubCate == 0 && selectedCategory != null) {
            manageProductSubCateErrorTxt.setVisibility(View.VISIBLE);
        } else {
            manageProductSubCateErrorTxt.setVisibility(View.GONE);
        }

        return Helper.inputChecked(productNameEt, productNameLayout, null, null) +
                Helper.inputChecked(authorRegisEt, authorLayout, null, null) +
                Helper.inputChecked(priceEt, priceLayout, null, null) +
                Helper.inputChecked(quantityEt, quantityLayout, null, null) +
                Helper.publishedYearChecked(publishedAtEt, publishedAtLayout) +
                selectedCate +
                selectedSubCate == 7;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                getActivity().finish();
                break;
            case R.id.productImageLayout:
                getImageFromGallery();
                break;
            case R.id.uploadProduct:
                if(!isOnline()) {
                    showConnectDialog();
                    return;
                }
                onUploadProduct();
                break;
            case R.id.updateProduct:
                if(!isOnline()) {
                    showConnectDialog();
                    return;
                }
                onUpdateProduct();
                break;
            case R.id.removeProduct:
                if(!isOnline()) {
                    showConnectDialog();
                    return;
                }
                onRemoveProduct();
                break;
        }
    }

    // request functions
    public void uploadBook(Book inputBook) {
        postAuthenticatedData = new PostAuthenticatedData(getContext(), this);
        postAuthenticatedData.setEndPoint(Constant.uploadBook);
        postAuthenticatedData.setTaskType(Constant.uploadBookTaskType);
        postAuthenticatedData.setToken(token);
        postAuthenticatedData.execute(Book.toJSON(inputBook));
    }

    public void updateBook(Book inputBook) {
        postAuthenticatedData = new PostAuthenticatedData(getContext(), this);
        postAuthenticatedData.setEndPoint(Constant.updateBook);
        postAuthenticatedData.setTaskType(Constant.updateBookTaskType);
        postAuthenticatedData.setToken(token);
        postAuthenticatedData.execute(Book.toJSON(inputBook));
    }

    public void deleteBook(String bookId) {
        deleteAuthenticatedData = new DeleteAuthenticatedData(getContext(), this);
        deleteAuthenticatedData.setEndPoint(Constant.deleteBook + "/" + bookId);
        deleteAuthenticatedData.setTaskType(Constant.deleteBookTaskType);
        deleteAuthenticatedData.setToken(token);
        deleteAuthenticatedData.execute();
    }

    public void getAllCategories() {
        getData = new GetData(getContext(), this);
        getData.setEndPoint(Constant.getAllCategories);
        getData.setTaskType(Constant.getAllCategoriesTaskType);
        getData.execute();
    }

    public void getProduct(String id) {
        getData = new GetData(getContext(), this);
        getData.setEndPoint(Constant.getProduct + "/" + id);
        getData.setTaskType(Constant.getProductTaskType);
        getData.execute();
    }

    // callback functions
    @Override
    public void onFinished(String message, String taskType) {
        if (taskType.equals(Constant.uploadBookTaskType)) {
            ApiData<Book> apiData = ApiData.fromJSON(ApiData.getData(message), Book.class);
            Intent intent = getActivity().getIntent();
            Log.d("TAG", "onFinished: test " + apiData.getData().getName());
            intent.putExtra(Constant.productKey, apiData.getData());
            intent.putExtra(Constant.isUploadKey, Constant.isUploadKey);
            getActivity().setResult(Activity.RESULT_OK, intent);
            getActivity().finish();
        } else if (taskType.equals(Constant.getAllCategoriesTaskType)) {
            ApiList<Category> apiList = ApiList.fromJSON(ApiList.getData(message), Category.class);
            categories = apiList.getList();
            foreign = categories.get(0).getSubCategories();
            domestic = categories.get(1).getSubCategories();
            text = categories.get(2).getSubCategories();
        } else if (taskType.equals(Constant.getProductTaskType)) {

            ApiData<Book> apiData = ApiData.fromJSON(ApiData.getData(message), Book.class);
            Book product = apiData.getData();
            productNameEt.setText(product.getName());
            authorRegisEt.setText(product.getAuthor());
            descriptionEt.setText(product.getDescription());
            priceEt.setText(String.valueOf(product.getPrice()));
            quantityEt.setText(String.valueOf(product.getQuantity()));
            publishedAtEt.setText(String.valueOf(product.getPublishedAt()));
            if (categories.get(0).get_id().equals(product.getCategory().get_id())) {
                categoriesBtnGrp.check(R.id.manageProductForeignCateBtn);
            } else if (categories.get(1).get_id().equals(product.getCategory().get_id())) {
                categoriesBtnGrp.check(R.id.manageProductDomesticCateBtn);
            } else if (categories.get(2).get_id().equals(product.getCategory().get_id())) {
                categoriesBtnGrp.check(R.id.manageProductTextCateBtn);
            }

            ArrayList<SubCategory> productSubCategory = product.getSubCategory();
            for (int i = 0; i < displayList.size(); ++i) {
                for (int index = 0; index < productSubCategory.size(); ++index) {
                    if (displayList.get(i).get_id().equals(productSubCategory.get(index).get_id())) {
                        displayList.get(i).setChosen(true);
                    }
                }
            }

            subCateAdapter.notifyDataSetChanged();
            customerId = product.getCustomer().get_id();

            if (product.isNew()) {
                newProduct.setChecked(true);
                usedProduct.setChecked(false);
            } else {
                newProduct.setChecked(false);
                usedProduct.setChecked(true);
            }

            productPhoto = Helper.stringToBitmap(product.getImage());
            productView.setImageBitmap(productPhoto);
            manageProgressBar.setVisibility(View.INVISIBLE);
            bookInfoLayout.setVisibility(View.VISIBLE);
            bookUpdateLayout.setVisibility(View.VISIBLE);
            updateProduct.setVisibility(View.VISIBLE);
            removeProduct.setVisibility(View.VISIBLE);
        } else if (taskType.equals(Constant.updateBookTaskType)) {
            Intent intent = new Intent(getContext(), ProductDetailActivity.class);
            intent.putExtra(Constant.isUpdateKey, Constant.isUpdateKey);
            getActivity().setResult(getActivity().RESULT_OK, intent);
            getActivity().finish();
        } else if (taskType.equals(Constant.deleteBookTaskType)) {
            Intent intent = new Intent(getContext(), ProductDetailActivity.class);
            intent.putExtra(Constant.isRemoveKey, Constant.isRemoveKey);
            getActivity().setResult(getActivity().RESULT_OK, intent);
            getActivity().finish();
        }
    }

    @Override
    public void onError(String taskType) {

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent;
            switch (requestCode) {
                case Constant.cameraPermissionCode:
                    intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    getActivity().startActivityForResult(intent, Constant.cameraRequest);
                    break;
                case Constant.galleryPermissionCode:
                    intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    getActivity().startActivityForResult(intent, Constant.galleryRequest);
                    break;
                default:
                    break;
            }
        } else {
            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constant.cameraRequest:
                    productPhoto = (Bitmap) data.getExtras().get("data");
                    productView.setImageBitmap(productPhoto);
                    break;
                case Constant.galleryRequest:
                    try {
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                        productPhoto = BitmapFactory.decodeStream(imageStream);
                        productPhoto = Bitmap.createScaledBitmap(productPhoto, (int) (productPhoto.getWidth() * 0.3), (int) (productPhoto.getHeight() * 0.3), true);
                        productView.setImageBitmap(productPhoto);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
