package com.example.shoesstore.service.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.shoesstore.entity.Sku;
import com.example.shoesstore.mapper.SkuMapper;
import com.example.shoesstore.model.SkuModel;
import com.example.shoesstore.util.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class SkuService {
    DatabaseHelper dbHelper;
    SkuMapper skuMapper = new SkuMapper();

    public SkuService(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    public long add(SkuModel skuModel) {
        try {
            Sku sku = skuMapper.toEntity(skuModel);
            SQLiteDatabase database = dbHelper.openConnect();
            ContentValues skuData = new ContentValues();
            skuData.put("size", sku.getSize());
            skuData.put("color", sku.getColor());
            skuData.put("quantity", sku.getQuantity());
            skuData.put("price", sku.getPrice());
            skuData.put("product_id", sku.getProductId());
            long id = database.insert("sku", null, skuData);
            return id;
        } catch (Exception e) {
            Log.d("Error:", e.getMessage());
            return 0;
        } finally {
            dbHelper.closeConnect();
        }
    }

    public List<Sku> findAllByProductId(int id) {
        try {
            SQLiteDatabase database = dbHelper.openConnect();
            List<Sku> skus = new ArrayList<>();
            Cursor cursor = database.rawQuery("select * from sku where product_id=?", new String[]{String.valueOf(id)});
            if (cursor.moveToFirst()) {
                do {
                    skus.add(new Sku(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5)));
                } while (cursor.moveToNext());
            }
            return skus;
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            return null;
        } finally {
            dbHelper.closeConnect();
        }
    }

    public long deleteAllByProductId(int id) {
        try {
            if (findAllByProductId(id).size() > 0) {
                SQLiteDatabase database = dbHelper.openConnect();
                long result = database.delete("sku", "product_id=?", new String[]{String.valueOf(id)});
                return result;
            } else {
                return 1;
            }
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            return 0;
        } finally {
            dbHelper.closeConnect();
        }
    }
}
