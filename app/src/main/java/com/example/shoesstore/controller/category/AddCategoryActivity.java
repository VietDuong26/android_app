package com.example.shoesstore.controller.category;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shoesstore.R;
import com.example.shoesstore.model.CategoryModel;
import com.example.shoesstore.service.ICategoryService;
import com.example.shoesstore.service.impl.CategoryService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class AddCategoryActivity extends AppCompatActivity {
    TextInputEditText edtCategoryName;
    MaterialButton btnSave;
    ICategoryService categoryService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        edtCategoryName = findViewById(R.id.edtCategoryName);
        categoryService = new CategoryService(this);
        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String categoryName = edtCategoryName.getText().toString();
                if (!categoryName.equals("")) {
                    if (categoryService.add(new CategoryModel(categoryName)) > 0) {
                        Toast.makeText(AddCategoryActivity.this, "Thêm mới thành công", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddCategoryActivity.this, AdminCategoryActivity.class));
                    } else {
                        Toast.makeText(AddCategoryActivity.this, "Thất bại", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddCategoryActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AddCategoryActivity.this, AdminCategoryActivity.class));
        super.onBackPressed();
    }
}