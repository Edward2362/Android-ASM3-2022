package com.example.asm3.config;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class Helper {
    public static String getQueryEndpoint(String key,String value){
        return "?"+key+"="+value;
    }

    public static void loadFragment(FragmentManager fragmentManager, Fragment fragment, String tag, int fragmentLayoutId) {
        fragmentManager.beginTransaction()
                .replace(fragmentLayoutId, fragment, tag)
                .setReorderingAllowed(true)
                .commit();
    }
}
