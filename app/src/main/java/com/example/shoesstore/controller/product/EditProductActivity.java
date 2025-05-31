package com.example.shoesstore.controller.product;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.shoesstore.R;
import com.example.shoesstore.config.cloudinary.CloudinaryManager;
import com.example.shoesstore.dto.CategoryDto;
import com.example.shoesstore.dto.ProductDto;
import com.example.shoesstore.entity.ProductImage;
import com.example.shoesstore.entity.Sku;
import com.example.shoesstore.model.ProductImageModel;
import com.example.shoesstore.model.ProductModel;
import com.example.shoesstore.model.SkuModel;
import com.example.shoesstore.service.ICategoryService;
import com.example.shoesstore.service.IProductService;
import com.example.shoesstore.service.impl.CategoryService;
import com.example.shoesstore.service.impl.ProductImageService;
import com.example.shoesstore.service.impl.ProductService;
import com.example.shoesstore.service.impl.SkuService;
import com.example.shoesstore.util.FileUtil;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class EditProductActivity extends AppCompatActivity {

    IProductService productService;
    ICategoryService categoryService;

    ProductImageService productImageService;

    SkuService skuService;

    TableLayout tableSku;

    LinearLayout llImagesContainer;

    MaterialButton btnSelectFiles, btnClearFiles, circleButton, btnUpdateProduct;

    TextInputEditText edtProductName, edtProductDescription;

    AutoCompleteTextView tvCategory;
    ProductDto productDto;

    int categoryId;

    List<Uri> uris = new ArrayList<>();

    CloudinaryManager cloudinaryManager = new CloudinaryManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        productService = new ProductService(this);
        categoryService = new CategoryService(this);
        skuService = new SkuService(this);
        productImageService = new ProductImageService(this);

        int productId = getIntent().getIntExtra("productId", 0);
        productDto = productService.findById(productId);

        categoryId = productDto.getCategoryId();

        llImagesContainer = findViewById(R.id.llImagesContainer);

        loadProductImage(productDto.getProductImages());

        edtProductName = findViewById(R.id.edtProductName);
        edtProductDescription = findViewById(R.id.edtProductDescription);

        edtProductName.setText(productDto.getName());
        edtProductDescription.setText(productDto.getDescription());

        tvCategory = findViewById(R.id.tvCategory);
        tvCategory.setText(productDto.getCategoryName());
        tvCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCategoryList(tvCategory);
            }
        });

        tableSku = findViewById(R.id.tableSku);
        loadProductSku(productDto.getSkus());

        btnSelectFiles = findViewById(R.id.btnSelectFiles);
        btnSelectFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, 1);
            }
        });

        btnClearFiles = findViewById(R.id.btnClearFiles);
        btnClearFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearPreviewImage();
            }
        });

        circleButton = findViewById(R.id.circleButton);
        circleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRowToTable(null);
            }
        });

        btnUpdateProduct = findViewById(R.id.btnUpdateProduct);
        btnUpdateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtProductName.getText().toString();
                String description = edtProductDescription.getText().toString();
                List<SkuModel> skus = getSkuData();
                if (!name.equals("") && !description.equals("") && skus.size() > 0 && llImagesContainer.getChildCount() > 0) {

                    ProgressDialog progressDialog = new ProgressDialog(EditProductActivity.this);
                    progressDialog.setMessage("Đang lưu sản phẩm...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    long result = productService.update(new ProductModel(name, description, categoryId), productId);
                    if (skuService.deleteAllByProductId(productId) > 0) {
                        for (SkuModel sku : skus
                        ) {
                            sku.setProductId(productId);
                            skuService.add(sku);
                        }
                    }
                    if (productImageService.deleteAllByProductId(productId) > 0) {
                        getImageData(new OnImagesUploadedListener() {
                            @Override
                            public void onUploaded(List<ProductImageModel> imageModels) {
                                for (ProductImageModel imageModel : imageModels
                                ) {
                                    productImageService.add(imageModel);
                                }
                                progressDialog.dismiss();
                                Toast.makeText(EditProductActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(EditProductActivity.this, AdminProductActivity.class));
                            }
                        });

                    }
                }
            }
        });

    }

    private void addRowToTable(Sku sku) {
        TableRow tableRow = new TableRow(this);

        EditText size = new EditText(this);
        size.setGravity(Gravity.CENTER);
        size.setInputType(InputType.TYPE_CLASS_NUMBER);
        size.setLayoutParams(new TableRow.LayoutParams(dpToPx(80), dpToPx(48)));

        EditText color = new EditText(this);
        color.setGravity(Gravity.CENTER);
        color.setInputType(InputType.TYPE_CLASS_TEXT);
        color.setLayoutParams(new TableRow.LayoutParams(dpToPx(120), dpToPx(48)));

        EditText quantity = new EditText(this);
        quantity.setGravity(Gravity.CENTER);
        quantity.setInputType(InputType.TYPE_CLASS_NUMBER);
        quantity.setLayoutParams(new TableRow.LayoutParams(dpToPx(100), dpToPx(48)));

        EditText price = new EditText(this);
        price.setGravity(Gravity.CENTER);
        price.setInputType(InputType.TYPE_CLASS_NUMBER);
        price.setLayoutParams(new TableRow.LayoutParams(dpToPx(140), // width giống header "Giá"
                dpToPx(48)));


        Button deleteButton = new Button(this);
        deleteButton.setText("Xóa");
        deleteButton.setTextColor(Color.WHITE);
        deleteButton.setBackgroundResource(R.drawable.rounded_button_delete);

        TableRow.LayoutParams btnParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        btnParams.setMargins(8, -8, 8, 0);
        deleteButton.setLayoutParams(btnParams);


        tableRow.addView(size);
        tableRow.addView(color);
        tableRow.addView(quantity);
        tableRow.addView(price);
        tableRow.addView(deleteButton);
        tableRow.setPadding(0, 8, 0, 0);
        if (sku != null) {
            size.setText(String.valueOf(sku.getSize()));
            color.setText(String.valueOf(sku.getColor()));
            quantity.setText(String.valueOf(sku.getQuantity()));
            price.setText(String.valueOf(sku.getPrice()));
        }
        tableSku.addView(tableRow);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    uris.add(imageUri);
                }
                loadPreviewImage(uris);
            }
        }
    }

    public void loadPreviewImage(List<Uri> uris) {
        for (Uri uri : uris) {
            ImageView imageView = new ImageView(this);
            imageView.setImageURI(uri);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(800, 1080); // px, nên chuyển dp sang px
            imageView.setLayoutParams(params);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ImageButton btnClose = new ImageButton(this);

            FrameLayout frameLayout = new FrameLayout(this);
            FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(800, 1080);

            FrameLayout.LayoutParams btnParams = new FrameLayout.LayoutParams(60, 60);
            btnParams.gravity = Gravity.TOP | Gravity.END;
            btnClose.setLayoutParams(btnParams);
            btnClose.setBackgroundColor(Color.TRANSPARENT); // Xóa nền
            btnClose.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
            btnClose.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            btnClose.setPadding(10, 10, 10, 10);
            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeOnePreviewImage(uri, frameLayout);
                }
            });


            frameLayout.setLayoutParams(frameParams);
            frameLayout.addView(imageView);
            frameLayout.addView(btnClose);

            llImagesContainer.addView(frameLayout);
        }
    }

    public void clearPreviewImage() {
        llImagesContainer.removeAllViews();
        uris.clear();
    }

    public void removeOnePreviewImage(Uri uri, View v) {
        uris.remove(uri);
        llImagesContainer.removeView(v);
    }

    public void showCategoryList(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        for (CategoryDto category : categoryService.getAll()) {
            popupMenu.getMenu().add(0, category.getId(), 0, category.getName());
        }
        popupMenu.setOnMenuItemClickListener(item -> {
            int selectedId = item.getItemId();
            String selectedName = item.getTitle().toString();
            tvCategory.setText(selectedName);
            categoryId = selectedId;
            return true;
        });
        popupMenu.show();
    }

    public int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    public List<SkuModel> getSkuData() {
        List<SkuModel> skuModels = new ArrayList<>();
        for (int i = 1; i < tableSku.getChildCount(); i++) {
            View row = tableSku.getChildAt(i);
            SkuModel skuModel = new SkuModel();
            if (row instanceof TableRow) {
                for (int j = 0; j < ((TableRow) row).getChildCount(); j++) {
                    View cell = ((TableRow) row).getChildAt(j);
                    if (cell instanceof EditText) {
                        switch (j) {
                            case 0:
                                skuModel.setSize(((EditText) cell).getText().toString());
                                break;
                            case 1:
                                skuModel.setColor(((EditText) cell).getText().toString());
                                break;
                            case 2:
                                skuModel.setQuantity(Integer.parseInt(((EditText) cell).getText().toString()));
                                break;
                            case 3:
                                skuModel.setPrice(Long.parseLong(((EditText) cell).getText().toString()));
                                break;
                        }
                    }
                }
            }
            skuModels.add(skuModel);
        }
        return skuModels;
    }

    public void getImageData(OnImagesUploadedListener listener) {
        List<ProductImageModel> images = new ArrayList<>();
        AtomicInteger count = new AtomicInteger(0);
        for (int i = 0; i < llImagesContainer.getChildCount(); i++) {
            View imageView = llImagesContainer.getChildAt(i);
            if (imageView instanceof FrameLayout) {
                FrameLayout frameLayout = (FrameLayout) imageView;
                ImageView imgView = (ImageView) frameLayout.getChildAt(0);
                if (imgView.getDrawable() instanceof BitmapDrawable) {
                    Bitmap bitmap = ((BitmapDrawable) imgView.getDrawable()).getBitmap();
                    int index = i;
                    new Thread(() -> {
                        String url = "";
                        try {
                            String uniqueFileName = String.format("image_product_%d_%d_%s_%d", productDto.getId(), System.currentTimeMillis(), UUID.randomUUID().toString().substring(0, 8), index);
                            File temp = FileUtil.bitmapToFile(EditProductActivity.this, bitmap, uniqueFileName);
                            url = cloudinaryManager.uploadImage(temp, "product/" + productDto.getId() + "/" + temp.getName());
                        } catch (Exception e) {
                            Log.d("Error when uploading image", e.getMessage());
                        }
                        String finalUrl = url;
                        runOnUiThread(() -> {
                            images.add(new ProductImageModel(finalUrl, productDto.getId()));
                            if (count.incrementAndGet() == llImagesContainer.getChildCount()) {
                                listener.onUploaded(images);
                            }
                        });
                    }).start();
                }
            }
        }
    }


    public void loadProductImage(List<ProductImage> productImages) {
        llImagesContainer.removeAllViews();
        for (ProductImage image : productImages) {
            ImageView imageView = new ImageView(this);
            Glide.with(this).load(image.getLink()).into(imageView);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(800, 1080); // px, nên chuyển dp sang px
            imageView.setLayoutParams(params);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ImageButton btnClose = new ImageButton(this);

            FrameLayout frameLayout = new FrameLayout(this);
            FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(800, 1080);
            FrameLayout.LayoutParams btnParams = new FrameLayout.LayoutParams(60, 60);
            btnParams.gravity = Gravity.TOP | Gravity.END;

            btnClose.setLayoutParams(btnParams);
            btnClose.setBackgroundColor(Color.TRANSPARENT);
            btnClose.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
            btnClose.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            btnClose.setPadding(10, 10, 10, 10);
            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    llImagesContainer.removeView(frameLayout);
                }
            });


            frameLayout.setLayoutParams(frameParams);
            frameLayout.addView(imageView);
            frameLayout.addView(btnClose);

            llImagesContainer.addView(frameLayout);
        }
    }


    public void loadProductSku(List<Sku> skus) {
        for (Sku sku : skus) {
            addRowToTable(sku);
        }
    }

    public interface OnImagesUploadedListener {
        void onUploaded(List<ProductImageModel> imageModels);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(EditProductActivity.this, AdminProductActivity.class));
        super.onBackPressed();
    }
}