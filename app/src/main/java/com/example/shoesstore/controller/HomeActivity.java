package com.example.shoesstore.controller;

import static com.example.shoesstore.util.CheckLogin.clearUserId;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.shoesstore.R;
import com.example.shoesstore.controller.login_regist.LoginActivity;
import com.example.shoesstore.controller.product.ProductDetailActivity;
import com.example.shoesstore.dto.CategoryDto;
import com.example.shoesstore.dto.ProductDto;
import com.example.shoesstore.service.ICategoryService;
import com.example.shoesstore.service.IProductService;
import com.example.shoesstore.service.impl.CategoryService;
import com.example.shoesstore.service.impl.ProductService;
import com.example.shoesstore.util.CheckLogin;
import com.example.shoesstore.util.ProductAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    RelativeLayout header;

    ImageSlider imageSlider;

    ImageButton cartButton, menuButton;

    RecyclerView bestSellingProductsSlider;

    LinearLayout collection_slider;

    IProductService productService;

    ICategoryService categoryService;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        productService = new ProductService(this);
        categoryService = new CategoryService(this);
        collection_slider = findViewById(R.id.collection_slider);

        menuButton = findViewById(R.id.menuButton);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(HomeActivity.this, v);
                popupMenu.getMenuInflater().inflate(R.menu.menu_home, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(item -> {
                    int id = item.getItemId();
                    if (id == R.id.nav_home) {
                        startActivity(new Intent(HomeActivity.this, HomeActivity.class));
                        return true;
                    } else if (id == R.id.nav_order_history) {
                        startActivity(new Intent(HomeActivity.this, OrderHistoryActivity.class));
                        return true;
                    } else {
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        cartButton = findViewById(R.id.cartButton);
        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckLogin.getUserId(HomeActivity.this) != -1) {
                    startActivity(new Intent(HomeActivity.this, CartActivity.class));
                } else {
                    Toast.makeText(HomeActivity.this, "Bạn cần đăng nhập", Toast.LENGTH_SHORT).show();
                }
            }
        });
        customView();

    }

    public void customView() {
        header = findViewById(R.id.header);
        header.setBackgroundResource(R.drawable.gradient_color);

        imageSlider = findViewById(R.id.imageSlider);
        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.banner1, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.banner1, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.banner1, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.banner1, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.banner1, ScaleTypes.FIT));
        imageSlider.setImageList(slideModels);

        bestSellingProductsSlider = findViewById(R.id.bestSellingProductsSlider);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        bestSellingProductsSlider.setLayoutManager(layoutManager);
        List<ProductDto> productDtos = productService.getAll();
        bestSellingProductsSlider.addItemDecoration(new HorizontalSpaceItemDecoration(Resources.getSystem().getDisplayMetrics().widthPixels * 1 / 100));
        bestSellingProductsSlider.setAdapter(new ProductAdapter(productDtos, new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ProductDto product) {
                Intent intent = new Intent(HomeActivity.this, ProductDetailActivity.class);
                intent.putExtra("productId", product.getId());
                startActivity(intent);
            }
        }));

        for (CategoryDto categoryDto : categoryService.getAll()) {
            addProductSliderByCategory(categoryDto);
        }


    }

    public static class HorizontalSpaceItemDecoration extends RecyclerView.ItemDecoration {
        private final int space;

        public HorizontalSpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.right = space;
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.left = 0;
            } else {
                outRect.left = space;
            }
        }
    }

    public void addProductSliderByCategory(CategoryDto categoryDto) {
        LinearLayout parentLayout = new LinearLayout(this);
        parentLayout.setOrientation(LinearLayout.VERTICAL);
        parentLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        parentLayout.setPadding(0, 8, 0, 0);

        // Tạo tiêu đề
        TextView textView = new TextView(this);
        textView.setText("Bộ Sưu Tập " + categoryDto.getName());
        textView.setTextSize(20);
        textView.setTypeface(null, Typeface.BOLD);

        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        textParams.setMargins(16, 16, 16, 16);
        textView.setLayoutParams(textParams);

        // Tạo RecyclerView
        RecyclerView recyclerView = new RecyclerView(this);


        int recyclerHeight = (int) (250 * getResources().getDisplayMetrics().density); // 200dp
        LinearLayout.LayoutParams recyclerParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                recyclerHeight
        );
        recyclerView.setLayoutParams(recyclerParams);
        recyclerView.setPadding(8, 8, 8, 8);

        // Layout manager horizontal
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(new ProductAdapter(productService.getAllProductByCategoryId(categoryDto.getId()), new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ProductDto product) {
                Intent intent = new Intent(HomeActivity.this, ProductDetailActivity.class);
                intent.putExtra("productId", product.getId());
                startActivity(intent);
            }
        }));
        recyclerView.addItemDecoration(new HorizontalSpaceItemDecoration(
                (int) (8 * getResources().getDisplayMetrics().density)
        ));

        parentLayout.addView(textView);
        parentLayout.addView(recyclerView);
        collection_slider.addView(parentLayout);
    }

    @Override
    public void onBackPressed() {
        if (CheckLogin.getUserId(this) != -1) {
            new AlertDialog.Builder(this)
                    .setTitle("Đăng xuất")
                    .setMessage("Bạn chắc chắn muốn đăng xuất?")
                    .setPositiveButton("Đăng xuất", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            clearUserId(HomeActivity.this);
                            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                            finish();
                        }
                    }).setNegativeButton("Hủy", null)
                    .show();
        }
    }
}
