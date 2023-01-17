package com.example.asm3.config;
import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModel;

import com.example.asm3.AuthenticationActivity;
import com.example.asm3.R;
import com.example.asm3.fragments.mainActivity.MainViewModel;
import com.example.asm3.models.Notification;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
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
            Log.d(TAG, "onChanged: Noti test");
            notifBadge.setVisible(false);
            notifBadge.clearNumber();
        }
    }

    public static void goToLogin(Context context, Activity activity) {
        Intent intent = new Intent(context, AuthenticationActivity.class);
        activity.startActivityForResult(intent, Constant.authActivityCode);
    }
}
