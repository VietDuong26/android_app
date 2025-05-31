package com.example.shoesstore.service.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.shoesstore.dto.OrderDto;
import com.example.shoesstore.dto.OrderItemDto;
import com.example.shoesstore.entity.Order;
import com.example.shoesstore.mapper.OrderMapper;
import com.example.shoesstore.model.OrderModel;
import com.example.shoesstore.util.CommonUtil;
import com.example.shoesstore.util.DatabaseHelper;
import com.example.shoesstore.util.OrderStatus;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderService {
    DatabaseHelper dbHelper;
    OrderMapper orderMapper;

    public OrderService(Context context) {
        this.dbHelper = new DatabaseHelper(context);
        this.orderMapper = new OrderMapper(context);
    }

    public long addNew(OrderModel model) {
        try {
            SQLiteDatabase database = dbHelper.openConnect();
            Order order = orderMapper.toEntity(model);
            ContentValues orderData = new ContentValues();
            orderData.put("user_id", order.getUserId());
            orderData.put("created_at", String.valueOf(new Timestamp(System.currentTimeMillis())));
            orderData.put("status", String.valueOf(OrderStatus.ORDERED.getValue()));
            orderData.put("name", order.getName());
            orderData.put("phone", order.getPhone());
            orderData.put("address", order.getAddress());
            long id = database.insert("orders", null, orderData);
            return id;
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            return 0;
        } finally {
            dbHelper.closeConnect();
        }
    }

    public OrderDto findById(int id) {
        try {
            SQLiteDatabase database = dbHelper.openConnect();
            Cursor cursor = database.rawQuery("select * from orders where id=?", new String[]{
                    String.valueOf(id)
            });
            if (cursor.moveToFirst()) {
                do {
                    Order order = new Order();
                    order.setId(cursor.getInt(0));
                    order.setUserId(cursor.getInt(1));
                    order.setCreatedAt(CommonUtil.convertToTimestamp(cursor.getString(2)));
                    order.setStatus(cursor.getInt(3));
                    order.setName(cursor.getString(4));
                    order.setPhone(cursor.getString(5));
                    order.setAddress(cursor.getString(6));
                    return orderMapper.toDto(order);
                } while (cursor.moveToNext());
            }
            return null;
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            return null;
        } finally {
            dbHelper.closeConnect();
        }
    }

    public List<Order> getAll() {
        try {
            SQLiteDatabase database = dbHelper.openConnect();
            List<Order> orders = new ArrayList<>();
            Cursor cursor = database.rawQuery("select * from orders order by id desc", null);
            if (cursor.moveToFirst()) {
                do {
                    orders.add(new Order(cursor.getInt(0), cursor.getInt(1), CommonUtil.convertToTimestamp(cursor.getString(2)), cursor.getInt(3), cursor.getString(4), cursor.getString(5), cursor.getString(6)));
                } while (cursor.moveToNext());
            }
            return orders;
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            return null;
        } finally {
            dbHelper.closeConnect();
        }
    }

    public long changeStatus(int status, int id) {
        try {
            SQLiteDatabase database = dbHelper.openConnect();
            ContentValues orderData = new ContentValues();
            if (status > 0) {
                orderData.put("status", status);
            }
            long result = database.update("orders", orderData, "id=?", new String[]{String.valueOf(id)});
            return result;
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            return 0;
        } finally {
            dbHelper.closeConnect();
        }
    }

    public long deleteOrder(int id) {
        try {
            SQLiteDatabase database = dbHelper.openConnect();
            long result = database.delete("orders", "id=?", new String[]{String.valueOf(id)});
            return result;
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            return 0;
        } finally {
            dbHelper.closeConnect();
        }
    }

    public List<OrderDto> findAllByUser(int userId) {
        try {
            SQLiteDatabase database = dbHelper.openConnect();
            List<Order> orders = new ArrayList<>();
            Cursor cursor = database.rawQuery("select * from orders where user_id=?", new String[]{String.valueOf(userId)});
            if (cursor.moveToFirst()) {
                do {
                    orders.add(new Order(
                            cursor.getInt(0),
                            cursor.getInt(1),
                            CommonUtil.convertToTimestamp(cursor.getString(2)),
                            cursor.getInt(3),
                            cursor.getString(4),
                            cursor.getString(5),
                            cursor.getString(6)));
                } while (cursor.moveToNext());
            }
            return orders.stream().map(x -> orderMapper.toDto(x)).collect(Collectors.toList());
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            return null;
        } finally {
            dbHelper.closeConnect();
        }
    }

    public List<OrderDto> getAllCompleted() {
        try {
            SQLiteDatabase database = dbHelper.openConnect();
            List<Order> orders = new ArrayList<>();
            Cursor cursor = database.rawQuery("select * from orders where status=? order by id desc", new String[]{String.valueOf(2)});
            if (cursor.moveToFirst()) {
                do {
                    orders.add(new Order(cursor.getInt(0), cursor.getInt(1), CommonUtil.convertToTimestamp(cursor.getString(2)), cursor.getInt(3), cursor.getString(4), cursor.getString(5), cursor.getString(6)));
                } while (cursor.moveToNext());
            }
            return orders.stream().map(x -> orderMapper.toDto(x)).collect(Collectors.toList());
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            return null;
        } finally {
            dbHelper.closeConnect();
        }
    }

    public int getRevenue() {
        int revenue = 0;
        for (OrderDto orderDto : getAllCompleted()) {
            int sum = 0;
            for (OrderItemDto item : orderDto.getItems()) {
                sum += item.getSku().getPrice() * item.getQuantity();
            }
            revenue += sum;
        }
        return revenue;
    }
}
