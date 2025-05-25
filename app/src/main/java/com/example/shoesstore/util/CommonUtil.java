package com.example.shoesstore.util;

import java.text.NumberFormat;
import java.util.Locale;

public class CommonUtil {
    public static String formatCurrency(long price) {
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        return formatter.format(price) + " ₫";
    }
}
