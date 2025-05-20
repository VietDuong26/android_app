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

public class EditCategoryActivity extends AppCompatActivity {
    ICategoryService categoryService;
    TextInputEditText edtCategoryName;
    MaterialButton btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);
        categoryService = new CategoryService(this);
        int categoryId = getIntent().getIntExtra("categoryId", 0);
        edtCategoryName = findViewById(R.id.edtCategoryName);
        edtCategoryName.setText(getIntent().getStringExtra("categoryName"));
        btnUpdate = findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newCategoryName = edtCategoryName.getText().toString();
                if (!newCategoryName.equals("")) {
                    if (categoryService.update(new CategoryModel(newCategoryName), categoryId) > 0) {
                        Toast.makeText(EditCategoryActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(EditCategoryActivity.this, AdminCategoryActivity.class));
                    } else {
                        Toast.makeText(EditCategoryActivity.this, "Thất bại", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditCategoryActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}