package com.example.shoesstore.controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.shoesstore.R;
import com.example.shoesstore.dto.CartDto;
import com.example.shoesstore.model.CartModel;
import com.example.shoesstore.service.impl.CartService;
import com.example.shoesstore.util.CheckLogin;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    CartService cartService;
    LinearLayout cartList;

    List<CartDto> checkedList;

    TextView txtTotalSelected;

    MaterialButton btnCheckout;

    int sum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);
        cartService = new CartService(this);
        cartList = findViewById(R.id.cart_list);
        checkedList = new ArrayList<>();

        btnCheckout = findViewById(R.id.btnCheckout);
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkedList.size() > 0) {
                    List<Integer> cartIds = new ArrayList<>();
                    for (CartDto cart : checkedList
                    ) {
                        cartIds.add(cart.getId());
                    }
                    Intent intent = new Intent(CartActivity.this, CheckOutActivity.class);
                    intent.putIntegerArrayListExtra("cartIds", (ArrayList<Integer>) cartIds);
                    startActivity(intent);
                } else {
                    Toast.makeText(CartActivity.this, "Vui lòng chọn sản phẩm để thanh toán", Toast.LENGTH_SHORT).show();
                }
            }
        });

        sum = 0;
        txtTotalSelected = findViewById(R.id.txtTotalSelected);
        customView();
    }

    public void customView() {
        loadCartList();
    }

    public int getTotal() {
        sum = 0;
        for (CartDto cart : checkedList) {
            sum += cart.getSku().getPrice() * cart.getQuantity();
        }
        return sum;
    }

    public void loadCartList() {
        cartList.removeAllViews();
        List<CartDto> carts = cartService.findAllByUserId(CheckLogin.getUserId(CartActivity.this));
        for (CartDto cart : carts) {

            LinearLayout itemLayout = new LinearLayout(this);
            itemLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            itemLayout.setOrientation(LinearLayout.HORIZONTAL);
            itemLayout.setGravity(Gravity.CENTER_VERTICAL);
            itemLayout.setPadding(dpToPx(12), dpToPx(12), dpToPx(12), dpToPx(12));
            LinearLayout.LayoutParams itemParams = (LinearLayout.LayoutParams) itemLayout.getLayoutParams();
            itemParams.setMargins(0, 0, 0, dpToPx(12));
            itemLayout.setLayoutParams(itemParams);


            CheckBox checkBox = new CheckBox(this);
            LinearLayout.LayoutParams checkBoxParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            checkBoxParams.setMarginEnd(dpToPx(8));
            checkBox.setLayoutParams(checkBoxParams);
            itemLayout.addView(checkBox);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        checkedList.add(cart);
                    } else {
                        if (checkedList.contains(cart)) {
                            checkedList.remove(cart);
                        }
                    }
                    txtTotalSelected.setText("Tổng: " + String.valueOf(getTotal()));
                }
            });

            ImageView imgProduct = new ImageView(this);
            LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(dpToPx(80), dpToPx(80));
            imgProduct.setLayoutParams(imgParams);
            imgProduct.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imgProduct.setImageResource(R.drawable.ic_launcher_background);
            Glide.with(this).load(cart.getProduct().getProductImages().get(0).getLink()).into(imgProduct);
            itemLayout.addView(imgProduct);


            LinearLayout infoLayout = new LinearLayout(this);
            LinearLayout.LayoutParams infoParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            infoParams.setMarginStart(dpToPx(8));
            infoParams.setMarginEnd(dpToPx(8));
            infoLayout.setLayoutParams(infoParams);
            infoLayout.setOrientation(LinearLayout.VERTICAL);


            TextView txtProductName = new TextView(this);
            txtProductName.setText(cart.getProduct().getName());
            txtProductName.setTextColor(ContextCompat.getColor(this, R.color.black));
            txtProductName.setTextSize(18);
            txtProductName.setTypeface(null, Typeface.BOLD);
            infoLayout.addView(txtProductName);


            TextView txtProductOption = new TextView(this);
            txtProductOption.setText("Màu: " + cart.getSku().getColor() + ", Size: " + cart.getSku().getSize());
            txtProductOption.setTextSize(14);
            infoLayout.addView(txtProductOption);


            LinearLayout counterLayout = new LinearLayout(this);
            LinearLayout.LayoutParams counterParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, dpToPx(40));
            counterParams.topMargin = dpToPx(8);
            counterLayout.setLayoutParams(counterParams);
            counterLayout.setOrientation(LinearLayout.HORIZONTAL);
            counterLayout.setGravity(Gravity.CENTER_VERTICAL);


            Button btnDecrease = new Button(this);
            LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(dpToPx(32), dpToPx(40));
            btnDecrease.setLayoutParams(btnParams);
            btnDecrease.setText("-");
            btnDecrease.setTextColor(ContextCompat.getColor(this, R.color.white));
            btnDecrease.setTextSize(16);
            btnDecrease.setGravity(Gravity.CENTER);
            btnDecrease.setPadding(0, 0, 0, 0);
            btnDecrease.setBackgroundTintList(ContextCompat.getColorStateList(this, com.cloudinary.android.R.color.design_default_color_secondary));
            counterLayout.addView(btnDecrease);

            btnDecrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cart.getQuantity() > 1) {
                        cartService.removeOneFromCart(cart.getId());
                        loadCartList();
                    } else {
                        new AlertDialog.Builder(CartActivity.this)
                                .setTitle("Xác nhận")
                                .setMessage("Bạn có chắc muốn xóa sản phẩm này khỏi giỏ hàng?")
                                .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (cartService.removeFromCart(cart.getId()) > 0) {
                                            Toast.makeText(CartActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                                            loadCartList();
                                        } else {
                                            Toast.makeText(CartActivity.this, "Thất bại", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                                .setNegativeButton("Hủy", null)
                                .show();
                    }
                }
            });


            TextView txtQuantity = new TextView(this);
            LinearLayout.LayoutParams qtyParams = new LinearLayout.LayoutParams(dpToPx(40), dpToPx(32));
            qtyParams.setMarginStart(dpToPx(6));
            qtyParams.setMarginEnd(dpToPx(6));
            txtQuantity.setLayoutParams(qtyParams);
            txtQuantity.setGravity(Gravity.CENTER);
            txtQuantity.setText(String.valueOf(cart.getQuantity()));
            txtQuantity.setTextSize(16);
            txtQuantity.setBackground(ContextCompat.getDrawable(this, R.drawable.bordered_edittext));
            counterLayout.addView(txtQuantity);


            Button btnIncrease = new Button(this);
            btnIncrease.setLayoutParams(btnParams);
            btnIncrease.setText("+");
            btnIncrease.setTextColor(ContextCompat.getColor(this, R.color.white));
            btnIncrease.setTextSize(16);
            btnIncrease.setGravity(Gravity.CENTER);
            btnIncrease.setPadding(0, 0, 0, 0);
            btnIncrease.setBackgroundTintList(ContextCompat.getColorStateList(this, com.cloudinary.android.R.color.design_default_color_secondary));
            counterLayout.addView(btnIncrease);

            btnIncrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cart.getQuantity() < cart.getSku().getQuantity()) {
                        CartModel model = new CartModel();
                        model.setId(cart.getId());
                        cartService.addToCart(model);
                        loadCartList();
                    }
                }
            });

            infoLayout.addView(counterLayout);
            itemLayout.addView(infoLayout);

            ImageButton btnRemove = new ImageButton(this);
            LinearLayout.LayoutParams removeParams = new LinearLayout.LayoutParams(dpToPx(36), dpToPx(36));
            btnRemove.setLayoutParams(removeParams);
            TypedValue outValue = new TypedValue();
            this.getTheme().resolveAttribute(android.R.attr.selectableItemBackgroundBorderless, outValue, true);
            btnRemove.setBackgroundResource(outValue.resourceId);
            btnRemove.setImageResource(R.drawable.baseline_delete_24);
            btnRemove.setColorFilter(ContextCompat.getColor(this, R.color.red));
            btnRemove.setContentDescription("Xóa");
            itemLayout.addView(btnRemove);

            btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(CartActivity.this)
                            .setTitle("Xác nhận")
                            .setMessage("Bạn có chắc muốn xóa sản phẩm này khỏi giỏ hàng?")
                            .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (cartService.removeFromCart(cart.getId()) > 0) {
                                        Toast.makeText(CartActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                                        loadCartList();
                                    } else {
                                        Toast.makeText(CartActivity.this, "Thất bại", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setNegativeButton("Hủy", null)
                            .show();
                }
            });

            cartList.addView(itemLayout);
        }
    }

    private int dpToPx(int dp) {
        return (int) (dp * this.getResources().getDisplayMetrics().density);
    }
}