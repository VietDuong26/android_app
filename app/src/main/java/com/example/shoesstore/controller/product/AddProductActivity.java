package com.example.shoesstore.controller.product;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoesstore.R;
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
import com.example.shoesstore.util.CategoryAdapter;
import com.example.shoesstore.util.ProductImageAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class AddProductActivity extends AppCompatActivity {
    IProductService productService;
    ICategoryService categoryService;
    SkuService skuService;

    ProductImageService productImageService;

    RecyclerView rvImages;
    MaterialButton btnSelectImages, circleButton, btnSaveProduct;

    TextInputEditText edtProductName, edtProductDescription;
    TableLayout tableSku;

    ProductImageAdapter productImageAdapter;
    AutoCompleteTextView actvCategory;
    int selectedId;

    private ActivityResultLauncher<Intent> imagePickerLauncher;
    List<Uri> imageLinks = new ArrayList<>();// KHỞI TẠO ở đây

    List<SkuModel> skus = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        productService = new ProductService(this);
        skuService = new SkuService(this);
        productImageService = new ProductImageService(this);
        categoryService = new CategoryService(this);
        edtProductName = findViewById(R.id.edtProductName);
        edtProductDescription = findViewById(R.id.edtProductDescription);

        actvCategory = findViewById(R.id.actvCategory);
        CategoryAdapter categoryAdapter = new CategoryAdapter(this, categoryService.getAll());
        actvCategory.setAdapter(categoryAdapter);
        actvCategory.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                actvCategory.showDropDown();
            }
        });
        actvCategory.setOnItemClickListener((parent, view, position, id) -> {
            Object item = parent.getItemAtPosition(position);
            if (item instanceof CategoryDto) {
                CategoryDto selectedCategory = (CategoryDto) item;
                selectedId = selectedCategory.getId();
                actvCategory.setText(selectedCategory.getName(), false);
            }
        });

        rvImages = findViewById(R.id.rvImages);
        rvImages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        productImageAdapter = new ProductImageAdapter(imageLinks);
        rvImages.setAdapter(productImageAdapter);

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();

                        if (data.getClipData() != null) {
                            int count = data.getClipData().getItemCount();
                            for (int i = 0; i < count; i++) {
                                Uri uri = data.getClipData().getItemAt(i).getUri();
                                imageLinks.add(uri);
                                Log.d("uri test", String.valueOf(uri));
                            }
                        } else if (data.getData() != null) {
                            Uri uri = data.getData();
                            imageLinks.add(uri);
                            Log.d("uri test", String.valueOf(uri));
                        }
                        productImageAdapter.notifyDataSetChanged();
                    }
                });

        btnSelectImages = findViewById(R.id.btnSelectImages);
        btnSelectImages.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            imagePickerLauncher.launch(intent);  // PHẢI dùng launcher để nhận kết quả
        });
        tableSku = findViewById(R.id.tableSku);
        circleButton = findViewById(R.id.circleButton);
        circleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TableRow tableRow = new TableRow(AddProductActivity.this);
                EditText size = new EditText(AddProductActivity.this);
                size.setInputType(InputType.TYPE_CLASS_NUMBER);
                size.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                EditText color = new EditText(AddProductActivity.this);
                color.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                EditText quantity = new EditText(AddProductActivity.this);
                quantity.setInputType(InputType.TYPE_CLASS_NUMBER);
                quantity.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                EditText price = new EditText(AddProductActivity.this);
                price.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                price.setInputType(InputType.TYPE_CLASS_NUMBER);
                TextView remove = new TextView(AddProductActivity.this);
                remove.setText("Xóa");
                remove.setTextColor(Color.RED);
                remove.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tableRow.addView(size);
                tableRow.addView(color);
                tableRow.addView(quantity);
                tableRow.addView(price);
                tableRow.addView(remove);
                tableSku.addView(tableRow);
                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tableSku.removeView(tableRow);
                    }
                });
            }
        });


        btnSaveProduct = findViewById(R.id.btnSaveProduct);
        btnSaveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 1; i <= tableSku.getChildCount(); i++) {
                    View row = tableSku.getChildAt(i);
                    if (row instanceof TableRow) {
                        TableRow selectRow = (TableRow) row;
                        SkuModel sku = new SkuModel();
                        for (int j = 0; j < selectRow.getChildCount() - 1; j++) {
                            View cell = selectRow.getChildAt(j);
                            if (cell instanceof EditText) {
                                String input = ((EditText) cell).getText().toString();
                                switch (j) {
                                    case 0:
                                        sku.setSize(input);
                                        break;
                                    case 1:
                                        sku.setColor(input);
                                        break;
                                    case 2:
                                        sku.setQuantity(Integer.parseInt(input));
                                        break;
                                    case 3:
                                        sku.setPrice(Long.parseLong(input));
                                        break;
                                }
                            }
                        }
                        skus.add(sku);
                    }
                }
                long id = productService.add(new ProductModel(edtProductName.getText().toString(), edtProductDescription.getText().toString(), selectedId));
                if (id > 0) {
                    for (SkuModel sku : skus) {
                        sku.setProductId((int) id);
                        skuService.add(sku);
                    }
                    for (Uri uri : imageLinks) {
                        productImageService.add(new ProductImageModel(uri.toString(), id));
                    }
                    Toast.makeText(AddProductActivity.this, "Thêm mới thành công", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddProductActivity.this, AdminProductActivity.class));
                }else{
                    Toast.makeText(AddProductActivity.this, "Thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
