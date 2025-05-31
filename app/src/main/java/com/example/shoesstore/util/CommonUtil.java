package com.example.shoesstore.util;

import android.util.Log;

import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.Locale;

public class CommonUtil {
    public static String formatCurrency(long price) {
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        return formatter.format(price) + " ₫";
    }

    public static Timestamp convertToTimestamp(String time) {
        try {
            return Timestamp.valueOf(time);
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            return null;
        }
    }
}
