package com.example.shoesstore.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import com.example.shoesstore.R;
import com.example.shoesstore.controller.category.AdminCategoryActivity;
import com.example.shoesstore.controller.product.AdminProductActivity;
import com.example.shoesstore.controller.user.AdminUserActivity;
import com.google.android.material.card.MaterialCardView;

public class AdminHomeActivity extends AppCompatActivity {
    MaterialCardView productCard,ordersCard,userCard,categoryCard;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        productCard=findViewById(R.id.manageProductsCard);
        ordersCard=findViewById(R.id.manageOrdersCard);
        userCard=findViewById(R.id.manageUsersCard);
        categoryCard=findViewById(R.id.manageCategoriesCard);
        productCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminHomeActivity.this, AdminProductActivity.class));
            }
        });
        ordersCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
}
