package com.example.shoesstore.controller.category;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shoesstore.R;
import com.example.shoesstore.dto.CategoryDto;
import com.example.shoesstore.service.ICategoryService;
import com.example.shoesstore.service.impl.CategoryService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class AdminCategoryActivity extends AppCompatActivity {
    ICategoryService categoryService;

    MaterialButton btnFilter, btnSort, btnAddCategory;
    TableLayout tableCategories;
    TextInputEditText edtSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);
        categoryService = new CategoryService(this);

        edtSearch = findViewById(R.id.edtSearch);
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String nameSearch = edtSearch.getText().toString();
                if (!nameSearch.equals("") && event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    loadTable(categoryService.findByName(nameSearch));
                    return true;
                } else if (nameSearch.equals("")) {
                    loadTable(categoryService.getAll());
                    return true;
                }
                return false;
            }
        });

        btnFilter = findViewById(R.id.btnFilter);
        btnSort = findViewById(R.id.btnSort);
        btnAddCategory = findViewById(R.id.btnAddCategory);

        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminCategoryActivity.this, AddCategoryActivity.class));
            }
        });
        tableCategories = findViewById(R.id.tableCategories);
        List<CategoryDto> categories = categoryService.getAll();
        loadTable(categories);
    }

    public void loadTable(List<CategoryDto> categories) {
        for (int i = tableCategories.getChildCount() - 1; i > 0; i--) {
            tableCategories.removeViewAt(i);
        }
        for (CategoryDto category : categories) {
            TableRow tableRow = new TableRow(this);

            TextView index = new TextView(this);
            index.setText(String.valueOf(categories.indexOf(category) + 1));
            index.setGravity(Gravity.CENTER);

            TextView categoryName = new TextView(this);
            categoryName.setGravity(Gravity.CENTER);
            categoryName.setText(category.getName());

            float scale = getResources().getDisplayMetrics().density;
            int width = (int) (120 * scale + 0.5f);
            int height = (int) (50 * scale + 0.5f);

            TableRow.LayoutParams btnParams = new TableRow.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            btnParams.setMargins(8, -8, 8, 0);

            Button editBtn = new Button(this);
            editBtn.setGravity(Gravity.CENTER);
            editBtn.setText("Sửa");
            editBtn.setBackgroundResource(R.drawable.rounded_button_edit);
            editBtn.setTextColor(Color.WHITE);
            editBtn.setLayoutParams(btnParams);
            editBtn.setTextSize(14);

            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AdminCategoryActivity.this, EditCategoryActivity.class);
                    intent.putExtra("categoryId", category.getId());
                    intent.putExtra("categoryName", category.getName());
                    startActivity(intent);
                }
            });

            Button deleteBtn = new Button(this);
            deleteBtn.setGravity(Gravity.CENTER);
            deleteBtn.setText("Xóa");
            deleteBtn.setBackgroundResource(R.drawable.rounded_button_delete);
            deleteBtn.setTextColor(Color.WHITE);
            deleteBtn.setLayoutParams(btnParams);
            deleteBtn.setTextSize(14);

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(AdminCategoryActivity.this)
                            .setTitle("Xác nhận xóa")
                            .setMessage("Bạn có chắc chắn muốn xóa không?")
                            .setPositiveButton("Xóa", (dialog, which) -> {
                                if (categoryService.delete(category.getId()) > 0) {
                                    Toast.makeText(AdminCategoryActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                                    loadTable(categoryService.getAll());
                                } else {
                                    Toast.makeText(AdminCategoryActivity.this, "Thất bại", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("Hủy", null)
                            .show();
                }
            });

            tableRow.addView(index);
            tableRow.addView(categoryName);
            tableRow.addView(editBtn);
            tableRow.addView(deleteBtn);
            tableRow.setPadding(0, 8, 0, 0);
            tableCategories.addView(tableRow);
        }
    }
}