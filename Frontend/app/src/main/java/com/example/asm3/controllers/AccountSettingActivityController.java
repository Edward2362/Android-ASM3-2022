package com.example.asm3.controllers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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

public class AccountSettingActivityController extends BaseController implements AsyncTaskCallBack {

    private PostAuthenticatedData postAuthenticatedData;
    private Bitmap photo;
    private Button testButton;
    private ImageView testImageView;

    public AccountSettingActivityController(Context context, FragmentActivity activity){
        super(context, activity);
        postAuthenticatedData = new PostAuthenticatedData(context,this);

    }

    @Override
    public void onInit(){
        testButton = (Button) getActivity().findViewById(R.id.accountSettingActivity_testButton);
        testImageView = (ImageView) getActivity().findViewById(R.id.accountSettingActivity_testImageView);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera();
            }
        });
    }

    public void updateCustomerInfo(String username,String address){
        try {
            if(isAuth()){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Customer.usernameKey,username);
            jsonObject.put(Customer.addressKey,address);
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

    public void onRequestPermissionsResult(int requestCode, @NonNull int[] grantResults) {
        if (requestCode == Constant.cameraPermissionCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                getActivity().startActivityForResult(intent, Constant.cameraRequest);
            } else {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constant.cameraRequest) {
                photo = (Bitmap) data.getExtras().get("data");
                testImageView.setImageBitmap(photo);
            }
        }
    }

    @Override
    public void onFinished(String message,String taskType){
        if (taskType.equals(Constant.setCustomerDataTaskType)){

        }

    }
}
