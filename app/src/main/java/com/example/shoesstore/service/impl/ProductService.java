package com.example.shoesstore.service.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.shoesstore.dto.ProductDto;
import com.example.shoesstore.entity.Product;
import com.example.shoesstore.mapper.ProductMapper;
import com.example.shoesstore.model.ProductModel;
import com.example.shoesstore.service.IProductService;
import com.example.shoesstore.util.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductService implements IProductService {
    private DatabaseHelper dbHelper;
    ProductMapper mapper;

    public ProductService(Context context) {
        dbHelper = new DatabaseHelper(context);
        mapper = new ProductMapper(context);
    }


    @Override
    public long add(ProductModel productModel) {
        try {
            SQLiteDatabase database = dbHelper.openConnect();
            ContentValues values = new ContentValues();
            values.put("name", productModel.getName());
            values.put("description", productModel.getDescription());
            values.put("category_id", productModel.getCategoryId());
            long id = database.insert("product", null, values);
            return id;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 0;
        } finally {
            dbHelper.closeConnect();
        }
    }

    @Override
    public long update(ProductModel productModel, Integer integer) {
        try {
            SQLiteDatabase database = dbHelper.openConnect();
            ContentValues productData = new ContentValues();
            productData.put("name", productModel.getName());
            productData.put("description", productModel.getDescription());
            productData.put("category_id", productModel.getCategoryId());
            long result = database.update("product", productData, "id=?", new String[]{String.valueOf(integer)});
            return result;
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            return 0;
        } finally {
            dbHelper.closeConnect();
        }
    }

    @Override
    public long delete(Integer integer) {
        try {
            SQLiteDatabase database = dbHelper.openConnect();
            long result = database.delete("product", "id=?", new String[]{String.valueOf(integer)});
            return result;
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            return 0;
        } finally {
            dbHelper.closeConnect();
        }
    }

    @Override
    public List<ProductDto> getAll() {
        try {
            SQLiteDatabase database = dbHelper.openConnect();
            List<Product> products = new ArrayList<>();
            Cursor cursor = database.rawQuery("select * from product", null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    products.add(new Product(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3)));
                }
            }
            return products.stream().map(x -> mapper.toDto(x)).collect(Collectors.toList());
        } catch (Exception e) {
            Log.d("Error:", e.getMessage());
            return null;
        } finally {
            dbHelper.closeConnect();
        }
    }

    @Override
    public ProductDto findById(Integer integer) {
        try {
            SQLiteDatabase database = dbHelper.openConnect();
            Cursor cursor = database.rawQuery("select * from product where id=?", new String[]{String.valueOf(integer)});
            if (cursor.moveToFirst()) {
                return mapper.toDto(new Product(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3)));
            } else {
                return null;
            }
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            return null;
        } finally {
            dbHelper.closeConnect();
        }
    }
}
