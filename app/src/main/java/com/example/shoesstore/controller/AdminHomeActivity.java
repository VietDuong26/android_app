package com.example.shoesstore.controller;

import static com.example.shoesstore.util.CheckLogin.clearUserId;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import com.example.shoesstore.R;
import com.example.shoesstore.controller.category.AdminCategoryActivity;
import com.example.shoesstore.controller.login_regist.LoginActivity;
import com.example.shoesstore.controller.orders.AdminOrdersActivity;
import com.example.shoesstore.controller.product.AdminProductActivity;
import com.example.shoesstore.controller.user.AdminUserActivity;
import com.example.shoesstore.service.IProductService;
import com.example.shoesstore.service.impl.OrderService;
import com.example.shoesstore.service.impl.ProductService;
import com.google.android.material.card.MaterialCardView;

public class AdminHomeActivity extends AppCompatActivity {
    MaterialCardView productCard, ordersCard, userCard, categoryCard;
    TextView totalOrdersText, totalRevenueText, totalProductsText;

    OrderService orderService;
    IProductService productService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        productCard = findViewById(R.id.manageProductsCard);
        ordersCard = findViewById(R.id.manageOrdersCard);
        userCard = findViewById(R.id.manageUsersCard);
        categoryCard = findViewById(R.id.manageCategoriesCard);

        orderService = new OrderService(this);
        productService = new ProductService(this);

        totalOrdersText = findViewById(R.id.totalOrdersText);
        totalRevenueText = findViewById(R.id.totalRevenueText);
        totalProductsText = findViewById(R.id.totalProductsText);

        totalOrdersText.setText(String.valueOf(orderService.getAllCompleted().size()));
        totalRevenueText.setText(String.valueOf(orderService.getRevenue() + " VNĐ"));
        totalProductsText.setText(String.valueOf(productService.getAll().size()));

        productCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminHomeActivity.this, AdminProductActivity.class));
            }
        });
        ordersCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminHomeActivity.this, AdminOrdersActivity.class));
            }
        });
        userCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminHomeActivity.this, AdminUserActivity.class));
            }
        });
        categoryCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminHomeActivity.this, AdminCategoryActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Đăng xuất")
                .setMessage("Bạn chắc chắn muốn đăng xuất?")
                .setPositiveButton("Đăng xuất", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearUserId(AdminHomeActivity.this);
                        startActivity(new Intent(AdminHomeActivity.this, LoginActivity.class));
                        finish();
                    }
                }).setNegativeButton("Hủy", null)
                .show();
    }
}
