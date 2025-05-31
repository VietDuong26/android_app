package com.example.shoesstore.controller;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.shoesstore.R;
import com.example.shoesstore.dto.OrderDto;
import com.example.shoesstore.dto.OrderItemDto;
import com.example.shoesstore.service.impl.OrderService;
import com.example.shoesstore.util.CheckLogin;
import com.example.shoesstore.util.OrderStatus;

import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {

    OrderService orderService;

    LinearLayout orders_list;

    List<OrderDto> ordersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        orderService = new OrderService(this);
        orders_list = findViewById(R.id.orders_list);
        ordersList = orderService.findAllByUser(CheckLogin.getUserId(this));
        customView();
    }

    public void customView() {
        for (OrderDto orderDto : ordersList) {
            LinearLayout orderLayout = new LinearLayout(OrderHistoryActivity.this);
            orderLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            orderLayout.setOrientation(LinearLayout.VERTICAL);
            orderLayout.setPadding(dpToPx(16), dpToPx(16), dpToPx(16), dpToPx(16));
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) orderLayout.getLayoutParams();
            params.setMargins(0, 0, 0, dpToPx(12));
            orderLayout.setLayoutParams(params);
            orderLayout.setBackground(ContextCompat.getDrawable(OrderHistoryActivity.this, R.drawable.border_rect));
            orderLayout.setElevation(dpToPx(2));

            TextView tvOrderId = new TextView(OrderHistoryActivity.this);
            tvOrderId.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            tvOrderId.setText("Mã đơn hàng: #" + orderDto.getId());
            tvOrderId.setTypeface(null, Typeface.BOLD);
            tvOrderId.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            orderLayout.addView(tvOrderId);

            TextView tvOrderDate = new TextView(OrderHistoryActivity.this);
            tvOrderDate.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            tvOrderDate.setText("Ngày đặt: " + orderDto.getCreatedAt());
            LinearLayout.LayoutParams dateParams = (LinearLayout.LayoutParams) tvOrderDate.getLayoutParams();
            dateParams.topMargin = dpToPx(4);
            tvOrderDate.setLayoutParams(dateParams);
            tvOrderDate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            orderLayout.addView(tvOrderDate);

            LinearLayout productListLayout = new LinearLayout(OrderHistoryActivity.this);
            productListLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            productListLayout.setOrientation(LinearLayout.VERTICAL);

            for (int i = 0; i < orderDto.getItems().size(); i++) {
                TextView tvProduct = new TextView(OrderHistoryActivity.this);
                tvProduct.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                OrderItemDto item = orderDto.getItems().get(i);
                tvProduct.setText((i + 1) + ". " + item.getProduct().getName() + " Phân loại: Màu: " + item.getSku().getColor() + " Kích cỡ: " + item.getSku().getSize() + " SL: " + item.getQuantity());
                productListLayout.addView(tvProduct);
            }
            orderLayout.addView(productListLayout);

            TextView tvTotal = new TextView(OrderHistoryActivity.this);
            tvTotal.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            LinearLayout.LayoutParams totalParams = (LinearLayout.LayoutParams) tvTotal.getLayoutParams();
            totalParams.topMargin = dpToPx(4);
            tvTotal.setLayoutParams(totalParams);
            tvTotal.setText("Tổng tiền: " + getTotal(orderDto.getItems()));
            tvTotal.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            orderLayout.addView(tvTotal);

            TextView tvStatus = new TextView(OrderHistoryActivity.this);
            tvStatus.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            LinearLayout.LayoutParams statusParams = (LinearLayout.LayoutParams) tvStatus.getLayoutParams();
            statusParams.topMargin = dpToPx(4);
            tvStatus.setLayoutParams(statusParams);
            tvStatus.setText("Trạng thái: " + OrderStatus.getStatusName(orderDto.getStatus()));
            if (orderDto.getStatus() == 0) {
                tvStatus.setTextColor(Color.parseColor("#0047AB"));
            } else if (orderDto.getStatus() == 1) {
                tvStatus.setTextColor(Color.parseColor("#eaf600"));
            } else {
                tvStatus.setTextColor(Color.parseColor("#0deb0d"));
            }
            tvStatus.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            orderLayout.addView(tvStatus);

            Button btnDetail = new Button(OrderHistoryActivity.this);
            LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            btnParams.topMargin = dpToPx(8);
            btnParams.gravity = Gravity.END;
            btnDetail.setLayoutParams(btnParams);
            btnDetail.setText("Xem chi tiết");
            orderLayout.addView(btnDetail);


            orders_list.addView(orderLayout);
        }


    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, OrderHistoryActivity.this.getResources().getDisplayMetrics());
    }

    public int getTotal(List<OrderItemDto> items) {
        int sum = 0;
        for (OrderItemDto item : items) {
            sum += item.getQuantity() * item.getSku().getPrice();
        }
        return sum;
    }
}