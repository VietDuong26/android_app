package com.example.shoesstore.controller.product;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shoesstore.R;
import com.example.shoesstore.config.cloudinary.CloudinaryManager;
import com.example.shoesstore.dto.CategoryDto;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class AddProductActivity extends AppCompatActivity {
    ICategoryService categoryService;
    IProductService productService;

    ProductImageService productImageService;
    SkuService skuService;

    MaterialButton btnSelectFiles, btnClearFiles, circleButton, btnSaveProduct;

    TextInputEditText edtProductName, edtProductDescription;
    LinearLayout llImagesContainer;

    TableLayout tableSku;

    AutoCompleteTextView tvCategory;

    CloudinaryManager cloudinaryManager = new CloudinaryManager();


    int categoryId;
    List<Uri> uris = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        categoryService = new CategoryService(this);
        productService = new ProductService(this);
        productImageService = new ProductImageService(this);
        skuService = new SkuService(this);

        edtProductName = findViewById(R.id.edtProductName);
        edtProductDescription = findViewById(R.id.edtProductDescription);

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

        llImagesContainer = findViewById(R.id.llImagesContainer);

        btnClearFiles = findViewById(R.id.btnClearFiles);
        btnClearFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearPreviewImage();
            }
        });

        tvCategory = findViewById(R.id.category_list);
        tvCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCategoryList(tvCategory);
            }
        });

        tableSku = findViewById(R.id.tableSku);

        circleButton = findViewById(R.id.circleButton);
        circleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRowToTable();
            }
        });

        btnSaveProduct = findViewById(R.id.btnSaveProduct);
        btnSaveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductModel productModel = new ProductModel();
                String name = edtProductName.getText().toString();
                String description = edtProductDescription.getText().toString();
                List<SkuModel> skus = getSkuData();
                if (!name.equals("") && !description.equals("") && llImagesContainer.getChildCount() > 0 && skus.size() > 0 && categoryId != 0) {

                    ProgressDialog progressDialog = new ProgressDialog(AddProductActivity.this);
                    progressDialog.setMessage("Đang lưu sản phẩm...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    productModel.setName(name);
                    productModel.setDescription(description);
                    productModel.setCategoryId(categoryId);
                    long id = productService.add(productModel);
                    for (SkuModel skuModel : skus
                    ) {
                        skuModel.setProductId((int) id);
                        skuService.add(skuModel);
                    }
                    getImageData((int) id, new OnImagesUploadedListener() {
                        @Override
                        public void onUploaded(List<ProductImageModel> imageModels) {
                            for (ProductImageModel imageModel : imageModels
                            ) {
                                productImageService.add(imageModel);
                            }
                            progressDialog.dismiss();
                            Toast.makeText(AddProductActivity.this, "Thêm mới thành công", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AddProductActivity.this, AdminProductActivity.class));
                        }
                    });
                } else {
                    Toast.makeText(AddProductActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void addRowToTable() {
        TableRow tableRow = new TableRow(this);

        EditText size = new EditText(this);
        size.setGravity(Gravity.CENTER);
        size.setInputType(InputType.TYPE_CLASS_NUMBER);
        size.setLayoutParams(new TableRow.LayoutParams(
                dpToPx(80),
                dpToPx(48)
        ));

        EditText color = new EditText(this);
        color.setGravity(Gravity.CENTER);
        color.setInputType(InputType.TYPE_CLASS_TEXT);
        color.setLayoutParams(new TableRow.LayoutParams(
                dpToPx(120),
                dpToPx(48)
        ));

        EditText quantity = new EditText(this);
        quantity.setGravity(Gravity.CENTER);
        quantity.setInputType(InputType.TYPE_CLASS_NUMBER);
        quantity.setLayoutParams(new TableRow.LayoutParams(
                dpToPx(100),
                dpToPx(48)
        ));

        EditText price = new EditText(this);
        price.setGravity(Gravity.CENTER);
        price.setInputType(InputType.TYPE_CLASS_NUMBER);
        price.setLayoutParams(new TableRow.LayoutParams(
                dpToPx(140), // width giống header "Giá"
                dpToPx(48)
        ));


        Button deleteButton = new Button(this);
        deleteButton.setText("Xóa");
        deleteButton.setTextColor(Color.WHITE);
        deleteButton.setBackgroundResource(R.drawable.rounded_button_delete);

        TableRow.LayoutParams btnParams = new TableRow.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        btnParams.setMargins(8, -8, 8, 0);
        deleteButton.setLayoutParams(btnParams);


        tableRow.addView(size);
        tableRow.addView(color);
        tableRow.addView(quantity);
        tableRow.addView(price);
        tableRow.addView(deleteButton);
        tableRow.setPadding(0, 8, 0, 0);
        tableSku.addView(tableRow);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        clearPreviewImage();
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
        for (Uri uri : uris
        ) {
            ImageView imageView = new ImageView(this);
            imageView.setImageURI(uri);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(800, 1080); // px, nên chuyển dp sang px
            imageView.setLayoutParams(params);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ImageButton btnClose = new ImageButton(this);

            FrameLayout frameLayout = new FrameLayout(this);
            FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(
                    800,
                    1080
            );

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

    public void getImageData(int productId, OnImagesUploadedListener listener) {
        List<ProductImageModel> imageModels = new ArrayList<>();
        AtomicInteger count = new AtomicInteger(0);
        for (Uri uri : uris
        ) {
            new Thread(() -> {
                String url = "";
                try {
                    File temp = FileUtil.convertUriToFile(AddProductActivity.this, uri, productId);
                    url = cloudinaryManager.uploadImage(temp, "product/" + productId + "/" + temp.getName());
                } catch (IOException e) {
                    Log.d("Error when upload", e.getMessage());
                }
                String finalUrl = url;
                runOnUiThread(() -> {
                    imageModels.add(new ProductImageModel(finalUrl, productId));
                    if (count.incrementAndGet() == uris.size()) {
                        listener.onUploaded(imageModels);
                    }
                });
            }).start();
        }
    }

    public interface OnImagesUploadedListener {
        void onUploaded(List<ProductImageModel> imageModels);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AddProductActivity.this, AdminProductActivity.class));
        super.onBackPressed();
    }
}
