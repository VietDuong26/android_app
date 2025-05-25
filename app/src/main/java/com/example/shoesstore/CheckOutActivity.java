package com.example.shoesstore;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.shoesstore.dto.CartDto;
import com.example.shoesstore.service.impl.CartService;
import com.example.shoesstore.util.CommonUtil;
import com.example.shoesstore.util.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class CheckOutActivity extends AppCompatActivity {
    TextView countProduct, txtTotalPrice;
    List<Integer> ids;

    LinearLayout cartItems;

    CartService cartService;

    DatabaseHelper dbHelper;

    List<CartDto> cartDtos;
    int sum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_check_out);
        dbHelper = new DatabaseHelper(this);
        cartService = new CartService(this);

        sum = 0;
        cartDtos = new ArrayList<>();

        txtTotalPrice = findViewById(R.id.txtTotalPrice);
        countProduct = findViewById(R.id.countProduct);
        cartItems = findViewById(R.id.cart_items);

        ids = getIntent().getIntegerArrayListExtra("cartIds");
        customView();
    }

    public int getTotal() {
        sum = 0;
        for (CartDto cart : cartDtos) {
            sum += cart.getSku().getPrice() * cart.getQuantity();
        }
        return sum;
    }

    public void customView() {
        countProduct.setText("Sản phẩm đã chọn (" + ids.size() + ")");
        for (Integer id : ids
        ) {
            CartDto cart = cartService.findDtoById(id);
            LinearLayout itemLayout = new LinearLayout(CheckOutActivity.this);
            itemLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            itemLayout.setOrientation(LinearLayout.HORIZONTAL);
            itemLayout.setGravity(Gravity.CENTER_VERTICAL);
            itemLayout.setPadding(dpToPx(12), dpToPx(12), dpToPx(12), dpToPx(12));
            LinearLayout.LayoutParams itemParams = (LinearLayout.LayoutParams) itemLayout.getLayoutParams();
            itemParams.setMargins(0, 0, 0, dpToPx(8));
            itemLayout.setLayoutParams(itemParams);


            ImageView imgProduct = new ImageView(CheckOutActivity.this);
            LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(dpToPx(60), dpToPx(60));
            imgProduct.setLayoutParams(imgParams);
            imgProduct.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imgProduct.setImageResource(R.drawable.ic_launcher_background);
            Glide.with(CheckOutActivity.this).load(cart.getProduct().getProductImages().get(0).getLink()).into(imgProduct);
            itemLayout.addView(imgProduct);


            LinearLayout infoLayout = new LinearLayout(CheckOutActivity.this);
            LinearLayout.LayoutParams infoParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            infoParams.setMarginStart(dpToPx(12));
            infoLayout.setLayoutParams(infoParams);
            infoLayout.setOrientation(LinearLayout.VERTICAL);


            TextView txtProductName = new TextView(CheckOutActivity.this);
            txtProductName.setText(cart.getProduct().getName());
            txtProductName.setTextColor(ContextCompat.getColor(CheckOutActivity.this, R.color.black));
            txtProductName.setTextSize(16);
            txtProductName.setTypeface(null, Typeface.BOLD);
            infoLayout.addView(txtProductName);


            TextView txtProductOption = new TextView(CheckOutActivity.this);
            txtProductOption.setText("Màu: " + cart.getSku().getColor() + ", Size: " + cart.getSku().getSize());
            txtProductOption.setTextSize(14);
            infoLayout.addView(txtProductOption);


            TextView txtQuantity = new TextView(CheckOutActivity.this);
            txtQuantity.setText("Số lượng: " + cart.getQuantity());
            txtQuantity.setTextColor(ContextCompat.getColor(CheckOutActivity.this, R.color.black));
            txtQuantity.setTextSize(14);
            infoLayout.addView(txtQuantity);

            itemLayout.addView(infoLayout);


            TextView txtPrice = new TextView(CheckOutActivity.this);
            txtPrice.setText(CommonUtil.formatCurrency(cart.getSku().getPrice() * cart.getQuantity()));
            txtPrice.setTextColor(ContextCompat.getColor(CheckOutActivity.this, com.cloudinary.android.ui.R.color.design_default_color_secondary));
            txtPrice.setTextSize(16);
            txtPrice.setTypeface(null, Typeface.BOLD);
            itemLayout.addView(txtPrice);

            cartDtos.add(cart);
            cartItems.addView(itemLayout);
        }
        txtTotalPrice.setText(CommonUtil.formatCurrency(getTotal()));
    }

    private int dpToPx(int dp) {
        return (int) (dp * CheckOutActivity.this.getResources().getDisplayMetrics().density);
    }
}