package com.example.asm3.controllers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.asm3.base.controller.BaseController;
import com.example.asm3.base.networking.services.AsyncTaskCallBack;
import com.example.asm3.base.networking.services.PostAuthenticatedData;
import com.example.asm3.config.Constant;
import com.example.asm3.models.Customer;

import com.example.asm3.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class AccountSettingActivityController extends BaseController implements AsyncTaskCallBack {

    private PostAuthenticatedData postAuthenticatedData;
    private Bitmap photo;
    private Button testButton;
    private ImageView testImageView;
    private Button getImageButton;

    public AccountSettingActivityController(Context context, FragmentActivity activity){
        super(context, activity);
    }

    @Override
    public void onInit(){
        testButton = (Button) getActivity().findViewById(R.id.accountSettingActivity_testButton);
        testImageView = (ImageView) getActivity().findViewById(R.id.accountSettingActivity_testImageView);
        getImageButton = (Button) getActivity().findViewById(R.id.accountSettingActivity_getImageButton);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera();
            }
        });
        getImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImageFromGallery();
            }
        });
    }

    public void updateCustomerInfo(String username,String address){
        try {
            if(isAuth()){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Customer.usernameKey,username);
            jsonObject.put(Customer.addressKey,address);
            postAuthenticatedData = new PostAuthenticatedData(getContext(),this);
            postAuthenticatedData.setEndPoint(Constant.setCustomerData);
            postAuthenticatedData.setTaskType(Constant.setCustomerDataTaskType);
            postAuthenticatedData.setToken(getToken());
            postAuthenticatedData.execute(jsonObject);
            }
        } catch (JSONException exception){
            exception.printStackTrace();
        }
    }

    public void changePassword(String newPassword){
        try {
            if(isAuth()){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(Customer.passwordKey,newPassword);
                postAuthenticatedData = new PostAuthenticatedData(getContext(),this);
                postAuthenticatedData.setEndPoint(Constant.changePassword);
                postAuthenticatedData.setTaskType(Constant.changePasswordTaskType);
                postAuthenticatedData.setToken(getToken());
                postAuthenticatedData.execute(jsonObject);
            }
        } catch (JSONException exception){
            exception.printStackTrace();
        }
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
    public void onRequestPermissionsResult(int requestCode, @NonNull int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent;
            switch (requestCode){
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
                    photo = (Bitmap) data.getExtras().get("data");
                    testImageView.setImageBitmap(photo);
                    break;
                case Constant.galleryRequest:
                    try {
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                        photo = BitmapFactory.decodeStream(imageStream);
                        testImageView.setImageBitmap(photo);
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

    @Override
    public void onFinished(String message,String taskType){
        if (taskType.equals(Constant.setCustomerDataTaskType)){

        }

    }

    @Override
    public void onError(String taskType) {

    }
}
