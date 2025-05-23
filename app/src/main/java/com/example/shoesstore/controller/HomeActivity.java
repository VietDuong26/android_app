package com.example.shoesstore.controller;

import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.shoesstore.R;
import com.example.shoesstore.dto.CategoryDto;
import com.example.shoesstore.dto.ProductDto;
import com.example.shoesstore.service.ICategoryService;
import com.example.shoesstore.service.IProductService;
import com.example.shoesstore.service.impl.CategoryService;
import com.example.shoesstore.service.impl.ProductService;
import com.example.shoesstore.util.ProductAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    RelativeLayout header;

    ImageSlider imageSlider;

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
        bestSellingProductsSlider.setAdapter(new ProductAdapter(productDtos));

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

        recyclerView.setAdapter(new ProductAdapter(productService.getAllProductByCategoryId(categoryDto.getId())));
        recyclerView.addItemDecoration(new HorizontalSpaceItemDecoration(
                (int) (8 * getResources().getDisplayMetrics().density)
        ));

        parentLayout.addView(textView);
        parentLayout.addView(recyclerView);

        collection_slider.addView(parentLayout);
    }

}
