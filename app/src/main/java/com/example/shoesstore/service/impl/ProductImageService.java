package com.example.shoesstore.service.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.shoesstore.entity.ProductImage;
import com.example.shoesstore.mapper.ProductImageMapper;
import com.example.shoesstore.model.ProductImageModel;
import com.example.shoesstore.util.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class ProductImageService {
    DatabaseHelper dbHelper;
    ProductImageMapper mapper;


    public ProductImageService(Context context) {
        this.mapper = new ProductImageMapper(context);
        this.dbHelper = new DatabaseHelper(context);
    }

    public long add(ProductImageModel model) {
        try {
            SQLiteDatabase database = dbHelper.openConnect();
            ProductImage productImage = mapper.toEntity(model);
            ContentValues imageData = new ContentValues();
            imageData.put("link", productImage.getLink());
            imageData.put("product_id", productImage.getProductId());
            long id = database.insert("product_image", null, imageData);
            return id;
        } catch (Exception e) {
            Log.d("Error:", e.getMessage());
            return 0;
        } finally {
            dbHelper.closeConnect();
        }
    }

    public List<ProductImage> findAllByProductId(int id) {
        try {
            SQLiteDatabase database = dbHelper.openConnect();
            List<ProductImage> images = new ArrayList<>();
            Cursor cursor = database.rawQuery("select * from product_image where product_id=?", new String[]{String.valueOf(id)});
            if (cursor.moveToFirst()) {
                do {
                    images.add(new ProductImage(
                            cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getInt(2)
                    ));
                } while (cursor.moveToNext());
            }

            return images;
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
                long result = database.delete("product_image", "product_id=?", new String[]{String.valueOf(id)});
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
