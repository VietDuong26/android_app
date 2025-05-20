package com.example.shoesstore.controller.product;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.TableLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoesstore.R;
import com.example.shoesstore.dto.ProductDto;
import com.example.shoesstore.service.IProductService;
import com.example.shoesstore.service.impl.ProductService;
import com.example.shoesstore.util.ProductImageAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EditProductActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1001;
    RecyclerView rvImages;
    MaterialButton btnSelectImages, btnUpdateProduct;
    ProductImageAdapter productImageAdapter;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    List<Uri> imageLinks = new ArrayList<>();
    TextInputEditText edtProductName, edtProductDescription;
    AutoCompleteTextView actvCategory;
    TableLayout tableSku;
    IProductService productService;
    private int productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        productService = new ProductService(this);
        productId = getIntent().getIntExtra("productId", 0);
        
        if (productId == 0) {
            Toast.makeText(this, "Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ProductDto productDto = productService.findById(productId);
        if (productDto == null) {
            Toast.makeText(this, "Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initializeViews();
        loadProductData(productDto);
        setupImagePicker();
        setupSaveButton();
    }

    private void initializeViews() {
        rvImages = findViewById(R.id.rvImages);
        btnSelectImages = findViewById(R.id.btnSelectImages);
        btnUpdateProduct = findViewById(R.id.btnUpdateProduct);
        edtProductName = findViewById(R.id.edtProductName);
        edtProductDescription = findViewById(R.id.edtProductDescription);
        actvCategory = findViewById(R.id.actvCategory);
        tableSku = findViewById(R.id.tableSku);

        rvImages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        productImageAdapter = new ProductImageAdapter(imageLinks);
        rvImages.setAdapter(productImageAdapter);
    }

    private void loadProductData(ProductDto productDto) {
        edtProductName.setText(productDto.getName());
        edtProductDescription.setText(productDto.getDescription());
        // TODO: Set category and SKU data
        
        if (productDto.getProductImages() != null) {
            imageLinks.addAll(productDto.getProductImages().stream()
                    .map(x -> Uri.parse(x.getLink()))
                    .collect(Collectors.toList()));
            productImageAdapter.notifyDataSetChanged();
        }
    }

    private void setupImagePicker() {
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

        btnSelectImages.setOnClickListener(v -> {
            if (checkAndRequestPermissions()) {
                openImagePicker();
            }
        });
    }

    private boolean checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) 
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                        PERMISSION_REQUEST_CODE);
                return false;
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) 
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST_CODE);
                return false;
            }
        }
        return true;
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        imagePickerLauncher.launch(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                         @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker();
            } else {
                Toast.makeText(this, "Cần quyền truy cập để chọn ảnh", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setupSaveButton() {
        btnUpdateProduct.setOnClickListener(v -> {
            String name = edtProductName.getText().toString().trim();
            String description = edtProductDescription.getText().toString().trim();
            
            if (name.isEmpty()) {
                edtProductName.setError("Vui lòng nhập tên sản phẩm");
                return;
            }

//            ProductDto updatedProduct = new ProductDto();
//            updatedProduct.setId(productId);
//            updatedProduct.setName(name);
//            updatedProduct.setDescription(description);
//            // TODO: Set category and SKU data
//
//            boolean success = productService.update(updatedProduct);
//            if (success) {
//                Toast.makeText(this, "Cập nhật sản phẩm thành công", Toast.LENGTH_SHORT).show();
//                finish();
//            } else {
//                Toast.makeText(this, "Cập nhật sản phẩm thất bại", Toast.LENGTH_SHORT).show();
//            }
        });
    }
}