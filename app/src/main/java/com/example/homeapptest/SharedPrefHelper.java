package com.example.homeapptest;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;

public class SharedPrefHelper {
    private static final String SP_NAME = "TestHomeSP";
    private static final String KEY_NEED_SET_HOME = "need_set_home";
    private static final String KEY_WHITELIST_APPS = "white_list_apps";

    public static void setHome(Context context, boolean status) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        sp.edit().putBoolean(KEY_NEED_SET_HOME, status).apply();
    }

    @SuppressWarnings("squid:S6291")
    public static boolean isNeedSetHome(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(KEY_NEED_SET_HOME, false);
    }

    public static Set<String> getAppCheckedList(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getStringSet(KEY_WHITELIST_APPS, null);
    }

    public static void setAppChecked(Context context, String packageName, boolean isChecked) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);

        // clone 一份，避免直接修改原始 reference
        Set<String> selectedApps = new HashSet<>(sp.getStringSet(KEY_WHITELIST_APPS, new HashSet<>()));

        if (isChecked) {
            selectedApps.add(packageName);
        } else {
            selectedApps.remove(packageName);
        }

        // 安全地存回去
        sp.edit().putStringSet(KEY_WHITELIST_APPS, selectedApps).apply();
    }
}
