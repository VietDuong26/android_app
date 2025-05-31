package com.example.shoesstore.controller.orders;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.shoesstore.R;
import com.example.shoesstore.controller.AdminHomeActivity;
import com.example.shoesstore.entity.Order;
import com.example.shoesstore.service.impl.OrderService;
import com.example.shoesstore.util.OrderStatus;

import java.util.List;

public class AdminOrdersActivity extends AppCompatActivity {

    OrderService orderService;

    TableLayout tableOrders;

    List<Order> orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_orders);

        orderService = new OrderService(this);
        tableOrders = findViewById(R.id.tableOrders);
        orders = orderService.getAll();

        loadTable(orders);
    }

    public void loadTable(List<Order> orders) {
        for (Order order : orders) {
            addRowToTable(order);
        }
    }

    public void addRowToTable(Order order) {
        TableRow tableRow = new TableRow(AdminOrdersActivity.this);
        tableRow.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        tableRow.setBackgroundColor(Color.TRANSPARENT);

        TextView tvSTT = new TextView(AdminOrdersActivity.this);
        tvSTT.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        tvSTT.setText(String.valueOf(orders.indexOf(order) + 1));
        tvSTT.setGravity(Gravity.CENTER);
        tvSTT.setTextColor(ContextCompat.getColor(AdminOrdersActivity.this, R.color.black));
        tvSTT.setTypeface(null, Typeface.BOLD);
        tvSTT.setPadding(16, 16, 16, 16);

        TextView tvNgayDat = new TextView(AdminOrdersActivity.this);
        tvNgayDat.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        tvNgayDat.setText(String.valueOf(order.getCreatedAt()));
        tvNgayDat.setGravity(Gravity.CENTER);
        tvNgayDat.setTextColor(ContextCompat.getColor(AdminOrdersActivity.this, R.color.black));
        tvNgayDat.setTypeface(null, Typeface.BOLD);
        tvNgayDat.setPadding(16, 16, 16, 16);

        TextView tvTrangThai = new TextView(AdminOrdersActivity.this);
        tvTrangThai.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        tvTrangThai.setText(OrderStatus.getStatusName(order.getStatus()));
        tvTrangThai.setGravity(Gravity.CENTER);
        tvTrangThai.setTextColor(ContextCompat.getColor(AdminOrdersActivity.this, R.color.black));
        tvTrangThai.setTypeface(null, Typeface.BOLD);
        tvTrangThai.setPadding(16, 16, 16, 16);

        Button btnChiTiet = new Button(AdminOrdersActivity.this);
        btnChiTiet.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        btnChiTiet.setText("Chi tiết");
        btnChiTiet.setGravity(Gravity.CENTER);
        btnChiTiet.setTextColor(ContextCompat.getColor(AdminOrdersActivity.this, R.color.white));
        btnChiTiet.setTypeface(null, Typeface.BOLD);
        int padding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 16, AdminOrdersActivity.this.getResources().getDisplayMetrics());
        btnChiTiet.setPadding(padding, padding, padding, padding);
        btnChiTiet.setBackgroundResource(R.drawable.rounded_button_edit);
        btnChiTiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminOrdersActivity.this, AdminOrderDetailActivity.class);
                intent.putExtra("orderId", order.getId());
                startActivity(intent);
            }
        });

        tableRow.addView(tvSTT);
        tableRow.addView(tvNgayDat);
        tableRow.addView(tvTrangThai);
        tableRow.addView(btnChiTiet);

        tableRow.setPadding(0, 8, 0, 8);
        tableOrders.addView(tableRow);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AdminOrdersActivity.this, AdminHomeActivity.class));
        super.onBackPressed();
    }
}