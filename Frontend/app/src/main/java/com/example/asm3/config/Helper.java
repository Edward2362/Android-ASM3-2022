package com.example.asm3.config;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.asm3.AuthenticationActivity;
import com.example.asm3.CartActivity;
import com.example.asm3.ProductDetailActivity;
import com.example.asm3.R;
import com.example.asm3.fragments.mainActivity.MainViewModel;
import com.example.asm3.models.Notification;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.Normalizer;
import java.time.Year;
import java.util.ArrayList;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helper {
    public static String getQueryEndpoint(String key, String value) {
        return "?" + key + "=" + value;
    }

    public static void loadFragment(FragmentManager fragmentManager, Fragment fragment, String tag, int fragmentLayoutId) {
        fragmentManager.beginTransaction()
                .replace(fragmentLayoutId, fragment, tag)
                .setReorderingAllowed(true)
                .commit();
    }

    public static int inputChecked(TextInputEditText textInputEditText, TextInputLayout textInputLayout, @Nullable Pattern pattern, @Nullable String notification) {
        int checked = 1;
        String input = textInputEditText.getText().toString();
        textInputLayout.setErrorEnabled(false);
        if (input.equals("")) {
            textInputLayout.setErrorEnabled(true);
            textInputLayout.setError("Cannot be empty!");
            checked = 0;
        } else if (pattern != null) {
            Matcher matcher = pattern.matcher(input);
            if (!matcher.find()) {
                textInputLayout.setErrorEnabled(true);
                textInputLayout.setError(notification);
                checked = 0;
            }
        }
        return checked;
    }

    public static int publishedYearChecked(TextInputEditText textInputEditText, TextInputLayout textInputLayout) {
        int checked = 1;
        String input = textInputEditText.getText().toString();
        textInputLayout.setErrorEnabled(false);
        if (input.equals("")) {
            textInputLayout.setErrorEnabled(true);
            textInputLayout.setError("Cannot be empty!");
            checked = 0;
        } else {
            int intInput = Integer.parseInt(input);
            int currentYear = 2023;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                currentYear = Year.now().getValue();
            }
            if (intInput < 1700 || intInput > currentYear) {
                textInputLayout.setErrorEnabled(true);
                textInputLayout.setError("Invalid year!");
                checked = 0;
            }
        }
        return checked;
    }

    public static void setBadge(ArrayList<Notification> notifications, MainViewModel viewModel) {
        int countUnread = 0;
        for (Notification notification : notifications) {
            if (!notification.isRead())
                countUnread++;
        }
        BadgeDrawable notifBadge = viewModel.getMenu().getValue().getOrCreateBadge(R.id.notiNav);
        if (countUnread != 0) {
            notifBadge.setVisible(true);
            notifBadge.setNumber(countUnread);
        } else {
            notifBadge.setVisible(false);
            notifBadge.clearNumber();
        }
    }

    public static void goToLogin(Context context, Activity activity) {
        Intent intent = new Intent(context, AuthenticationActivity.class);
        activity.startActivityForResult(intent, Constant.authActivityCode);
    }

    public static void goToCart(Context context, Activity activity) {
        Intent intent = new Intent(context, CartActivity.class);
        activity.startActivity(intent);
    }

    public static void goToBookDetail(Context context, Activity activity, String bookId, int listPosition) {
        Intent intent = new Intent(context, ProductDetailActivity.class);
        intent.putExtra(Constant.productIdKey, bookId);
        intent.putExtra(Constant.booksArrPositionKey, listPosition);
        activity.startActivityForResult(intent, Constant.productDetailActivityCode);
    }

    public static String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        String data = "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            data = Base64.getEncoder().encodeToString(bytes);
        }

        return data;
    }

    public static Bitmap stringToBitmap(String encodedData) {
        Bitmap bitmap = null;
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                byte[] encodeByte = Base64.getDecoder().decode(encodedData);
                bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            }
        } catch (Exception exception) {
            exception.getMessage();
        }
        return bitmap;
    }

    public static boolean isReceivedDataEmpty(String message) {
        boolean isEmpty = false;
        try {
            JSONObject jsonObject = new JSONObject(message);
            JSONArray jsonArray = jsonObject.getJSONArray("data");

            if (jsonArray.length() == 0) {
                isEmpty = true;
            }
        } catch (JSONException jsonException) {

        }
        return isEmpty;
    }

    public static String deAccent(String str) {
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);

        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }

    public static boolean isDarkTheme(Context context) {
        int nightModeFlags = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                return true;
            case Configuration.UI_MODE_NIGHT_NO:
                return false;
        }

        return false;
    }

}
