package com.example.asm3.controllers;

import static android.content.Context.LOCATION_SERVICE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm3.AccountSettingActivity;
import com.example.asm3.TestActivity;
import com.example.asm3.base.adapter.GenericAdapter;
import com.example.asm3.base.adapter.viewHolder.AddressHolder;
import com.example.asm3.base.controller.BaseController;
import com.example.asm3.base.networking.services.AsyncTaskCallBack;
import com.example.asm3.base.networking.services.PostAuthenticatedData;
import com.example.asm3.config.Constant;
import com.example.asm3.models.Customer;

import com.example.asm3.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AccountSettingActivityController extends BaseController implements AsyncTaskCallBack,
        SearchView.OnQueryTextListener, AddressHolder.OnSelectListener {

    private PostAuthenticatedData postAuthenticatedData;
    private Bitmap photo;
    private Button testButton;
    private ImageView testImageView;
    private Button getImageButton;
    private EditText userName, userAddress, userEmail;
    private Customer authCustomer;
    private long lastTextEdit = 0;
    private RecyclerView addressesRecView;
    private SearchView searchView;
    private Handler handler = new Handler();
    private ArrayList<String> addressesList;
    private GenericAdapter<String> addressAdapter;
    private LocationManager locationManager;

    public AccountSettingActivityController(Context context, FragmentActivity activity){
        super(context, activity);
    }

    @Override
    public void onInit(){
        testButton = (Button) getActivity().findViewById(R.id.accountSettingActivity_testButton);
        testImageView = (ImageView) getActivity().findViewById(R.id.accountSettingActivity_testImageView);
        getImageButton = (Button) getActivity().findViewById(R.id.accountSettingActivity_getImageButton);
        userName = (EditText) getActivity().findViewById(R.id.userName);
        userAddress = (EditText) getActivity().findViewById(R.id.userAddress);
        searchView = (SearchView) getActivity().findViewById(R.id.searchViewAddress);
        addressesRecView = (RecyclerView) getActivity().findViewById(R.id.addressResults);
        Intent intent = getActivity().getIntent();
        authCustomer = (Customer) intent.getSerializableExtra("data");
        Log.d("", "onInit: test" + authCustomer.getUsername());
        addressesList = new ArrayList<>();
        addressAdapter = generateAddressAdaptor();
        addressesRecView.setAdapter(addressAdapter);
        addressesRecView.setLayoutManager(new LinearLayoutManager(getContext()));
        //set searchView to be visible and set Text in the searchView by default => user address
        searchView.setIconifiedByDefault(false);
        searchView.setQuery("test",false);
        searchView.setOnQueryTextListener(this);


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

    @Override
    public boolean onQueryTextSubmit(String newText) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (!newText.isEmpty()) {
            Log.d("TAG", "onQueryTextChange: TextNoteEmpty " + newText);
            // progressBar.setVisibility(View.VISIBLE);
            addressesRecView.setVisibility(View.GONE);
            lastTextEdit = System.currentTimeMillis();
            // TODO: implement function fetching
            handler.removeCallbacksAndMessages(null);
            handler.postDelayed(new Runnable() {
                @Override
                public void run(){
                    fetchUrl(newText);
                    //return func here
                    //getSuggestions(newText);
                    Log.d("error","huhu");
                    // TODO: put these 2 lines in onFinished, a fetching function will be called here
                }
            }, 1000);
        } else {
            //progressBar.setVisibility(View.GONE);
            addressesRecView.setVisibility(View.GONE);
        }
        return true;
    }
    // Helper
    private GenericAdapter<String> generateAddressAdaptor(){
        return new GenericAdapter<String>(addressesList) {
            @Override
            public RecyclerView.ViewHolder setViewHolder(ViewGroup parent) {
                final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_items, parent, false);
                return new AddressHolder(view, AccountSettingActivityController.this);
            }

            @Override
            public void onBindData(RecyclerView.ViewHolder holder, String item) {
                AddressHolder addressHolder = (AddressHolder) holder;
                addressHolder.getAddressTextView().setText(item);
            }
        };
    }

    @Override
    public void onAddressClick(int position, View view, TextView textView) {
        switch (view.getId()){
            case R.id.addressResults:
                Log.d("TAG", "onAddressClick: test" + position);
                searchView.setQuery(textView.getText().toString(),false);
        }
    }
    public void fetchUrl(String query){
        OkHttpClient client = new OkHttpClient();
        String myString = query.replaceAll(" ", "%2C");
        Log.d("TAG", "fetchUrl: query" + myString);
        ArrayList<String> newAddressesList = new ArrayList<>();
        String url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query="+query+"&location=15.9031%2C-105.8067&radius=9999999&key=AIzaSyCYVs0ybSzlvpLQ6VoaNfAVsh7YhG4gk18";

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
//                    Log.d("TAG", "onResponse: Test2 " + myObject.get("result")[0]);
                    final String myResponse = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(myResponse);
                        JSONArray jsonArray = jsonObject.getJSONArray("results");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            String temp = jsonArray.getJSONObject(i).getString("formatted_address");
                            newAddressesList.add(temp);
                            Log.d("TAG", "onResponse: test" +  jsonArray.getJSONObject(i).getString("formatted_address"));
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            addressesList.clear();
                            ArrayList<String> results;
                            if(newAddressesList.size()>=5){
                                results = (ArrayList<String>) newAddressesList.subList(0,5);
                                addressesList.addAll(results);
                            }else{
                                addressesList.addAll(newAddressesList);
                            }
                            addressAdapter.notifyDataSetChanged();
                            addressesRecView.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });
    }
}
