package com.example.shoesstore.util;

import android.content.Context;
import android.content.SharedPreferences;

public class CheckLogin {


    public static void setUserId(Context context, int userId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("app_pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("userId", userId);
        editor.apply();
    }

    public static int getUserId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("app_pref", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("userId", -1);
    }

    public static void clearUserId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("app_pref", Context.MODE_PRIVATE);
        if (getUserId(context) != -1) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("userId");
            editor.apply();
        }
    }
}
