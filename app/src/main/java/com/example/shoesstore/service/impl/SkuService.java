package com.example.shoesstore.service.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.shoesstore.dto.ProductOption;
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


    public Sku findAllBySizeAndColor(int hex, String size, String color, int productId) {
        try {
            SQLiteDatabase database = dbHelper.openConnect();
            String sql = "select * from sku where product_id=? and ";
            String[] args;
            if (hex == 1) {
                //tìm theo màu
                sql += " color=?";
                args = new String[]{String.valueOf(productId), color};
            } else if (hex == 2) {
                //tìm theo size
                sql += " size=?";
                args = new String[]{String.valueOf(productId), size};
            } else {
                sql += " size=? and color=?";
                args = new String[]{String.valueOf(productId), size, color};
            }
            Cursor cursor = database.rawQuery(sql, args);
            if (cursor.moveToFirst()) {
                do {
                    return new Sku(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5));
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

    public ProductOption getAllSizeAndColorByProduct(int productId) {
        try {
            ProductOption option = new ProductOption();
            List<String> sizes = new ArrayList<>();
            List<String> colors = new ArrayList<>();
            List<Sku> skus = findAllByProductId(productId);
            for (Sku sku : skus) {
                if (!sizes.contains(sku.getSize())) {
                    sizes.add(sku.getSize());
                }
                if (!colors.contains(sku.getColor())) {
                    colors.add(sku.getColor());
                }
            }
            option.setSizes(sizes);
            option.setColors(colors);
            return option;
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            return null;
        }
    }

    public List<String> getAllSizeOrColor(int productId, String name, int hex) {
        List<Sku> skus = findAllByProductId(productId);
        List<String> sizes = new ArrayList<>();
        List<String> colors = new ArrayList<>();

        if (hex == 1) {
            //tìm màu theo size
            for (Sku sku : skus
            ) {
                if (!colors.contains(name) && sku.getSize().equals(name)) {
                    colors.add(sku.getColor());
                }
            }
            return colors;
        } else if (hex == 2) {
            //tìm size theo màu
            for (Sku sku : skus
            ) {
                if (!sizes.contains(name) && sku.getColor().equals(name)) {
                    sizes.add(sku.getSize());
                }
            }
            return sizes;
        }
        return null;
    }

    public Sku findById(int skuId) {
        try {
            SQLiteDatabase database = dbHelper.openConnect();
            Cursor cursor = database.rawQuery("select * from sku where id=?", new String[]{String.valueOf(skuId)});
            if (cursor.moveToFirst()) {
                do {
                    return new Sku(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5));
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
}
