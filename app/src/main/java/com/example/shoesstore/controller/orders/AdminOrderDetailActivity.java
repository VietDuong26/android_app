package com.example.shoesstore.controller.orders;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.shoesstore.R;
import com.example.shoesstore.controller.AdminHomeActivity;
import com.example.shoesstore.dto.OrderDto;
import com.example.shoesstore.dto.OrderItemDto;
import com.example.shoesstore.service.impl.OrderService;
import com.example.shoesstore.util.CommonUtil;
import com.example.shoesstore.util.OrderStatus;

import java.util.List;

public class AdminOrderDetailActivity extends AppCompatActivity {
    OrderService orderService;
    OrderDto order;

    Button btnConfirmOrder, btnCancelOrder;

    TextView tvOrderDate, tvName, tvAddress, tvPhone, tvOrderStatus, tvOrderTotal;
    LinearLayout itemsLayoutList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        orderService = new OrderService(this);
        order = orderService.findById(getIntent().getIntExtra("orderId", 0));
        tvOrderDate = findViewById(R.id.tvOrderDate);
        tvName = findViewById(R.id.tvName);
        tvAddress = findViewById(R.id.tvAddress);
        tvPhone = findViewById(R.id.tvPhone);
        tvOrderStatus = findViewById(R.id.tvOrderStatus);
        tvOrderTotal = findViewById(R.id.tvOrderTotal);
        itemsLayoutList = findViewById(R.id.items);
        btnConfirmOrder = findViewById(R.id.btnConfirmOrder);
        btnCancelOrder = findViewById(R.id.btnCancelOrder);
        customView();
    }


    public void customView() {
        tvOrderDate.setText(String.valueOf(order.getCreatedAt()));
        tvName.setText(order.getUsername());
        tvAddress.setText(order.getAddress());
        tvPhone.setText(String.valueOf(order.getPhone()));
        tvOrderStatus.setText(OrderStatus.getStatusName(order.getStatus()));
        if (order.getStatus() == 0) {
            btnCancelOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(AdminOrderDetailActivity.this)
                            .setTitle("Thông báo")
                            .setMessage("Xác nhận hủy đơn hàng?")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    if (orderService.deleteOrder(order.getId()) > 0) {
                                        Toast.makeText(AdminOrderDetailActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(AdminOrderDetailActivity.this, AdminOrdersActivity.class));
                                    } else {
                                        Toast.makeText(AdminOrderDetailActivity.this, "Thất bại", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setNegativeButton("Hủy", null)
                            .show();
                }
            });
            btnConfirmOrder.setText(OrderStatus.getStatusName(1));
            btnConfirmOrder.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#eaf600")));
            btnConfirmOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(AdminOrderDetailActivity.this)
                            .setTitle("Thông báo")
                            .setMessage("Thay đổi trạng thái đơn hàng?")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    if (orderService.changeStatus(OrderStatus.SHIPPING.getValue(), order.getId()) > 0) {
                                        Toast.makeText(AdminOrderDetailActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(AdminOrderDetailActivity.this, AdminOrderDetailActivity.class));
                                    } else {
                                        Toast.makeText(AdminOrderDetailActivity.this, "Thất bại", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setNegativeButton("Hủy", null)
                            .show();

                }
            });
            tvOrderStatus.setTextColor(Color.parseColor("#0047AB"));
        } else if (order.getStatus() == 1) {
            btnCancelOrder.setVisibility(View.GONE);
            btnConfirmOrder.setText(OrderStatus.getStatusName(2));
            btnConfirmOrder.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#0deb0d")));
            btnConfirmOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (orderService.changeStatus(OrderStatus.SHIPPED.getValue(), order.getId()) > 0) {
                        Toast.makeText(AdminOrderDetailActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AdminOrderDetailActivity.this, AdminOrderDetailActivity.class));
                    } else {
                        Toast.makeText(AdminOrderDetailActivity.this, "Thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            tvOrderStatus.setTextColor(Color.parseColor("#eaf600"));
        } else {
            btnCancelOrder.setVisibility(View.GONE);
            btnConfirmOrder.setVisibility(View.GONE);
            tvOrderStatus.setTextColor(Color.parseColor("#0deb0d"));
        }

        tvOrderTotal.setText(CommonUtil.formatCurrency(getTotal(order.getItems())));
        for (OrderItemDto item : order.getItems()) {
            LinearLayout itemLayout = new LinearLayout(AdminOrderDetailActivity.this);
            itemLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            itemLayout.setOrientation(LinearLayout.HORIZONTAL);
            itemLayout.setGravity(Gravity.CENTER_VERTICAL);
            itemLayout.setPadding(dpToPx(12), dpToPx(12), dpToPx(12), dpToPx(12));
            LinearLayout.LayoutParams itemParams = (LinearLayout.LayoutParams) itemLayout.getLayoutParams();
            itemParams.setMargins(0, 0, 0, dpToPx(8));
            itemLayout.setLayoutParams(itemParams);


            ImageView imgProduct = new ImageView(AdminOrderDetailActivity.this);
            LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(dpToPx(60), dpToPx(60));
            imgProduct.setLayoutParams(imgParams);
            imgProduct.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imgProduct.setImageResource(R.drawable.ic_launcher_background);
            Glide.with(AdminOrderDetailActivity.this).load(item.getProduct().getProductImages().get(0).getLink()).into(imgProduct);
            itemLayout.addView(imgProduct);


            LinearLayout infoLayout = new LinearLayout(AdminOrderDetailActivity.this);
            LinearLayout.LayoutParams infoParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            infoParams.setMarginStart(dpToPx(12));
            infoLayout.setLayoutParams(infoParams);
            infoLayout.setOrientation(LinearLayout.VERTICAL);


            TextView txtProductName = new TextView(AdminOrderDetailActivity.this);
            txtProductName.setText(item.getProduct().getName());
            txtProductName.setTextColor(ContextCompat.getColor(AdminOrderDetailActivity.this, R.color.black));
            txtProductName.setTextSize(16);
            txtProductName.setTypeface(null, Typeface.BOLD);
            infoLayout.addView(txtProductName);


            TextView txtProductOption = new TextView(AdminOrderDetailActivity.this);
            txtProductOption.setText("Màu: " + item.getSku().getColor() + ", Size: " + item.getSku().getSize());
            txtProductOption.setTextSize(14);
            infoLayout.addView(txtProductOption);


            TextView txtQuantity = new TextView(AdminOrderDetailActivity.this);
            txtQuantity.setText("Số lượng: " + item.getQuantity());
            txtQuantity.setTextColor(ContextCompat.getColor(AdminOrderDetailActivity.this, R.color.black));
            txtQuantity.setTextSize(14);
            infoLayout.addView(txtQuantity);

            itemLayout.addView(infoLayout);


            TextView txtPrice = new TextView(AdminOrderDetailActivity.this);
            txtPrice.setText(CommonUtil.formatCurrency(item.getSku().getPrice() * item.getQuantity()));
            txtPrice.setTextColor(ContextCompat.getColor(AdminOrderDetailActivity.this, R.color.black));
            txtPrice.setTextSize(16);
            txtPrice.setTypeface(null, Typeface.BOLD);
            itemLayout.addView(txtPrice);

            itemsLayoutList.addView(itemLayout);
        }
    }

    public int getTotal(List<OrderItemDto> items) {
        int sum = 0;
        for (OrderItemDto item : items) {
            sum += item.getQuantity() * item.getSku().getPrice();
        }
        return sum;
    }

    private int dpToPx(int dp) {
        return (int) (dp * AdminOrderDetailActivity.this.getResources().getDisplayMetrics().density);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AdminOrderDetailActivity.this, AdminHomeActivity.class));
        super.onBackPressed();
    }
}
