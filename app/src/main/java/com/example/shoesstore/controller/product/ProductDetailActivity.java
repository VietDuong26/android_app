package com.example.shoesstore.controller.product;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.shoesstore.R;
import com.example.shoesstore.controller.HomeActivity;
import com.example.shoesstore.dto.ProductDto;
import com.example.shoesstore.dto.ProductOption;
import com.example.shoesstore.entity.ProductImage;
import com.example.shoesstore.entity.Sku;
import com.example.shoesstore.model.CartModel;
import com.example.shoesstore.service.IProductService;
import com.example.shoesstore.service.impl.CartService;
import com.example.shoesstore.service.impl.ProductService;
import com.example.shoesstore.service.impl.SkuService;
import com.example.shoesstore.util.CheckLogin;
import com.example.shoesstore.util.CommonUtil;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailActivity extends AppCompatActivity {

    IProductService productService;
    CartService cartService;

    SkuService skuService;
    ProductDto productDto;
    ImageSlider imageSlider;

    TextView productName, productPrice, productDescription;


    String sizeName, colorName;
    LinearLayout productSizes, productColors, bottomAppBar;

    LinearLayout.LayoutParams params;

    ProductOption option;

    MaterialButton btnAddToCart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        cartService = new CartService(this);

        sizeName = "";
        colorName = "";

        imageSlider = findViewById(R.id.productImages);

        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);

        productSizes = findViewById(R.id.productSizes);
        productColors = findViewById(R.id.productColors);
        productDescription = findViewById(R.id.productDescription);

        productService = new ProductService(this);
        productDto = productService.findById(getIntent().getIntExtra("productId", 0));

        skuService = new SkuService(this);

        params = new LinearLayout.LayoutParams(
                250,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        btnAddToCart = findViewById(R.id.btnAddToCart);

        option = skuService.getAllSizeAndColorByProduct(productDto.getId());
        actionOnSizeOrColorPick();

        bottomAppBar = findViewById(R.id.bottomAppBar);

        customView();

    }

    public void customView() {
        List<SlideModel> slideModels = new ArrayList<>();
        for (ProductImage image : productDto.getProductImages()) {
            slideModels.add(new SlideModel(image.getLink(), ScaleTypes.FIT));
        }
        imageSlider.setImageList(slideModels);

        productName.setText(productDto.getName());
        productPrice.setText(CommonUtil.formatCurrency(productDto.getSkus().get(0).getPrice()));
        productDescription.setText(productDto.getDescription());
    }

    public void actionOnSizeOrColorPick() {
        if (!sizeName.equals("") && colorName.equals("")) {
            List<String> colors = skuService.getAllSizeOrColor(productDto.getId(), sizeName, 1);
            productColors.removeAllViews();
            for (String color : colors
            ) {
                addColorButton(color);
            }
        } else if (sizeName.equals("") && !colorName.equals("")) {
            productSizes.removeAllViews();
            List<String> sizes = skuService.getAllSizeOrColor(productDto.getId(), colorName, 2);
            productSizes.removeAllViews();
            for (String size : sizes
            ) {
                addSizeButton(size);
            }
        } else if (sizeName.equals("") && colorName.equals("")) {
            productColors.removeAllViews();
            productSizes.removeAllViews();
            for (String color : option.getColors()
            ) {
                addColorButton(color);
            }
            for (String size : option.getSizes()
            ) {
                addSizeButton(size);
            }
        } else {
            Sku sku = skuService.findAllBySizeAndColor(3, sizeName, colorName, productDto.getId());
            if (sku != null) {
                productPrice.setText(CommonUtil.formatCurrency(sku.getPrice()));
                if (sku.getQuantity() == 0 && CheckLogin.getUserId(ProductDetailActivity.this) == -1) {
                    btnAddToCart.setBackgroundColor(Color.parseColor("#B1A7A7"));
                    btnAddToCart.setTextColor(Color.WHITE);
                    btnAddToCart.setText("Hết hàng");
                    btnAddToCart.setEnabled(false);
                } else if (sku.getQuantity() != 0 && CheckLogin.getUserId(ProductDetailActivity.this) != -1) {
                    btnAddToCart.setBackgroundColor(Color.parseColor("#FF5722"));
                    btnAddToCart.setTextColor(Color.WHITE);
                    btnAddToCart.setText("Thêm vào giỏ hàng");
                    btnAddToCart.setEnabled(true);
                    addCounter(sku);

                } else if (sku.getQuantity() != 0 && CheckLogin.getUserId(ProductDetailActivity.this) == -1) {
                    btnAddToCart.setBackgroundColor(Color.parseColor("#B1A7A7"));
                    btnAddToCart.setTextColor(Color.WHITE);
                    btnAddToCart.setText("Vui lòng đăng nhập");
                    btnAddToCart.setEnabled(false);
                }
            } else {
                btnAddToCart.setBackgroundColor(Color.parseColor("#B1A7A7"));
                btnAddToCart.setTextColor(Color.WHITE);
                btnAddToCart.setText("Không có sản phẩm");
                btnAddToCart.setEnabled(false);
            }
        }

    }


    public void addSizeButton(String size) {
        Button buttonSize = new Button(this);
        buttonSize.setText(size);
        buttonSize.setGravity(Gravity.CENTER);
        buttonSize.setLayoutParams(params);
        buttonSize.setPadding(0, 0, 8, 0);
        buttonSize.setBackgroundColor(Color.parseColor("#FFFFFF"));
        buttonSize.setTag(false);
        buttonSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSelected = !(boolean) buttonSize.getTag();

                // Bỏ chọn tất cả
                for (int i = 0; i < productSizes.getChildCount(); i++) {
                    View child = productSizes.getChildAt(i);
                    if (child instanceof Button) {
                        child.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        ((Button) child).setTextColor(Color.BLACK);
                        child.setTag(false);
                    }
                }

                if (isSelected) {
                    buttonSize.setBackgroundColor(Color.parseColor("#FF5722"));
                    buttonSize.setTextColor(Color.WHITE);
                    buttonSize.setTag(true);
                    sizeName = size;
                } else {
                    sizeName = "";
                }

                actionOnSizeOrColorPick();
            }
        });
        productSizes.addView(buttonSize);
    }

    public void addColorButton(String color) {
        Button buttonColor = new Button(this);
        buttonColor.setText(color);
        buttonColor.setGravity(Gravity.CENTER);
        buttonColor.setLayoutParams(params);
        buttonColor.setBackgroundColor(Color.parseColor("#FFFFFF"));
        buttonColor.setTextColor(Color.BLACK);
        buttonColor.setPadding(0, 0, 8, 0);
        buttonColor.setTag(false);
        buttonColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSelected = !(boolean) buttonColor.getTag();

                // Bỏ chọn tất cả
                for (int i = 0; i < productColors.getChildCount(); i++) {
                    View child = productColors.getChildAt(i);
                    if (child instanceof Button) {
                        child.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        ((Button) child).setTextColor(Color.BLACK);
                        child.setTag(false);
                    }
                }

                if (isSelected) {
                    buttonColor.setBackgroundColor(Color.parseColor("#FF5722"));
                    buttonColor.setTextColor(Color.WHITE);
                    buttonColor.setTag(true);
                    colorName = color;
                } else {
                    colorName = "";
                }

                actionOnSizeOrColorPick();
            }
        });
        productColors.addView(buttonColor);
    }

    public void addCounter(Sku sku) {
        if (bottomAppBar.getChildCount() > 1) {
            bottomAppBar.removeViewAt(0);
        }
        LinearLayout quantityLayout = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, dpToPx(40), 2f);
        quantityLayout.setLayoutParams(layoutParams);
        quantityLayout.setOrientation(LinearLayout.HORIZONTAL);
        quantityLayout.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
        quantityLayout.setPadding(0, 0, 0, 0);


        Button btnDecrease = new Button(this);
        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(dpToPx(28), dpToPx(36));
        btnDecrease.setLayoutParams(btnParams);
        btnDecrease.setText("-");
        btnDecrease.setTextColor(ContextCompat.getColor(this, R.color.white));
        btnDecrease.setTextSize(16);
        btnDecrease.setGravity(Gravity.CENTER);
        btnDecrease.setPadding(0, 0, 0, 0);
        btnDecrease.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.black));


        com.google.android.material.textfield.TextInputEditText edtQuantity = new com.google.android.material.textfield.TextInputEditText(this);
        LinearLayout.LayoutParams edtParams = new LinearLayout.LayoutParams(dpToPx(36), dpToPx(43));
        edtParams.setMarginStart(dpToPx(6));
        edtParams.setMarginEnd(dpToPx(6));
        edtQuantity.setLayoutParams(edtParams);
        edtQuantity.setGravity(Gravity.CENTER);
        edtQuantity.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtQuantity.setText("1");
        edtQuantity.setTextSize(14);
        edtQuantity.setBackground(ContextCompat.getDrawable(this, R.drawable.bordered_edittext));

        btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.parseInt(String.valueOf(edtQuantity.getText()));
                if (num > 1) {
                    edtQuantity.setText(String.valueOf(num - 1));
                }
            }
        });

        Button btnIncrease = new Button(this);
        btnIncrease.setLayoutParams(btnParams);
        btnIncrease.setText("+");
        btnIncrease.setTextColor(ContextCompat.getColor(this, R.color.white));
        btnIncrease.setTextSize(16);
        btnIncrease.setGravity(Gravity.CENTER);
        btnIncrease.setPadding(0, 0, 0, 0);
        btnIncrease.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.black));

        btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.parseInt(String.valueOf(edtQuantity.getText()));
                if (num < sku.getQuantity()) {
                    edtQuantity.setText(String.valueOf(num + 1));
                }
            }
        });

        quantityLayout.addView(btnDecrease);
        quantityLayout.addView(edtQuantity);
        quantityLayout.addView(btnIncrease);

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartModel model = new CartModel();
                model.setQuantity(Integer.parseInt(String.valueOf(edtQuantity.getText())));
                model.setSkuId(sku.getId());
                model.setUserId(CheckLogin.getUserId(ProductDetailActivity.this));
                if (cartService.addToCart(model) > 0) {
                    Toast.makeText(ProductDetailActivity.this, "Thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProductDetailActivity.this, "Thêm vào giỏ hàng thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bottomAppBar.addView(quantityLayout, 0);


    }

    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ProductDetailActivity.this, HomeActivity.class));
        super.onBackPressed();
    }
}