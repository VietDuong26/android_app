package com.example.shoesstore.controller.product;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shoesstore.R;
import com.example.shoesstore.controller.AdminHomeActivity;
import com.example.shoesstore.dto.ProductDto;
import com.example.shoesstore.service.IProductService;
import com.example.shoesstore.service.impl.ProductService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class AdminProductActivity extends AppCompatActivity {
    IProductService productService;

    MaterialButton btnFilter, btnSort, btnAdd;
    TableLayout tableProducts;
    TextInputEditText edtSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_product);
        productService = new ProductService(this);

        btnFilter = findViewById(R.id.btnFilter);
        btnSort = findViewById(R.id.btnSort);
        btnAdd = findViewById(R.id.btnAddProduct);
        tableProducts = findViewById(R.id.tableProducts);
        loadTable(productService.getAll());

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(AdminProductActivity.this, v);
                popupMenu.getMenuInflater().inflate(R.menu.filter_menu, popupMenu.getMenu());
                popupMenu.show();
            }
        });

        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(AdminProductActivity.this, v);
                popupMenu.getMenuInflater().inflate(R.menu.sort_menu, popupMenu.getMenu());
                popupMenu.show();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminProductActivity.this, AddProductActivity.class));
            }
        });
        edtSearch = findViewById(R.id.edtSearch);
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String nameSearch = edtSearch.getText().toString();
                if (!nameSearch.equals("") && event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    loadTable(productService.findByName(nameSearch));
                    return true;
                } else if (nameSearch.equals("")) {
                    loadTable(productService.getAll());
                    return true;
                }
                return false;
            }
        });
    }

    public void loadTable(List<ProductDto> products) {
        for (int i = tableProducts.getChildCount() - 1; i > 0; i--) {
            tableProducts.removeViewAt(i);
        }
        for (ProductDto product : products) {
            TableRow tableRow = new TableRow(this);

            TextView index = new TextView(this);
            index.setText(String.valueOf(products.indexOf(product) + 1));
            index.setGravity(Gravity.CENTER);

            TextView productName = new TextView(this);
            productName.setGravity(Gravity.CENTER);
            productName.setText(product.getName());

            TextView categoryName = new TextView(this);
            categoryName.setGravity(Gravity.CENTER);
            categoryName.setText(product.getCategoryName());

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
                    Intent intent = new Intent(AdminProductActivity.this, EditProductActivity.class);
                    intent.putExtra("productId", product.getId());
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
                    new AlertDialog.Builder(AdminProductActivity.this)
                            .setTitle("Xác nhận xóa")
                            .setMessage("Bạn có chắc chắn muốn xóa không?")
                            .setPositiveButton("Xóa", (dialog, which) -> {
                                if (productService.delete(product.getId()) > 0) {
                                    Toast.makeText(AdminProductActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                                    loadTable(productService.getAll());
                                } else {
                                    Toast.makeText(AdminProductActivity.this, "Thất bại", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("Hủy", null)
                            .show();
                }
            });

            tableRow.addView(index);
            tableRow.addView(productName);
            tableRow.addView(categoryName);
            tableRow.addView(editBtn);
            tableRow.addView(deleteBtn);
            tableRow.setPadding(0, 8, 0, 0);
            tableProducts.addView(tableRow);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AdminProductActivity.this, AdminHomeActivity.class));
        super.onBackPressed();
    }
}