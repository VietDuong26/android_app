package com.example.shoesstore.service.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.shoesstore.dto.OrderItemDto;
import com.example.shoesstore.entity.OrderItem;
import com.example.shoesstore.mapper.OrderItemMapper;
import com.example.shoesstore.model.OrderItemModel;
import com.example.shoesstore.util.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderItemService {
    DatabaseHelper dbHelper;
    OrderItemMapper itemMapper;

    public OrderItemService(Context context) {
        this.dbHelper = new DatabaseHelper(context);
        this.itemMapper = new OrderItemMapper(context);
    }

    public long addNew(OrderItemModel model) {
        try {
            SQLiteDatabase database = dbHelper.openConnect();
            ContentValues orderItemData = new ContentValues();
            OrderItem item = itemMapper.toEntity(model);
            orderItemData.put("sku_id", item.getSkuId());
            orderItemData.put("orders_id", item.getOrdersId());
            orderItemData.put("quantity", item.getQuantity());
            long id = database.insert("orders_item", null, orderItemData);
            return id;
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            return 0;
        } finally {
            dbHelper.closeConnect();
        }
    }

    public List<OrderItemDto> getAllByOrder(int id) {
        try {
            List<OrderItem> itemDtos = new ArrayList<>();
            SQLiteDatabase database = dbHelper.openConnect();
            Cursor cursor = database.rawQuery("select * from orders_item where orders_id=?", new String[]{
                    String.valueOf(id)
            });
            if (cursor.moveToFirst()) {
                do {
                    itemDtos.add(new OrderItem(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getInt(3)));
                } while (cursor.moveToNext());
            }
            return itemDtos.stream().map(x -> itemMapper.toDto(x)).collect(Collectors.toList());
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            return null;
        } finally {
            dbHelper.closeConnect();
        }
    }
}
