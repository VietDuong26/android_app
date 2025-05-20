package com.example.shoesstore.service.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.shoesstore.entity.Sku;
import com.example.shoesstore.mapper.SkuMapper;
import com.example.shoesstore.model.SkuModel;
import com.example.shoesstore.util.DatabaseHelper;

public class SkuService {
    DatabaseHelper dbHelper;
    SkuMapper skuMapper=new SkuMapper();
    public SkuService(Context context){
        this.dbHelper=new DatabaseHelper(context);
    }
    public long add(SkuModel skuModel){
        try{
            Sku sku=skuMapper.toEntity(skuModel);
            SQLiteDatabase database=dbHelper.openConnect();
            ContentValues skuData = new ContentValues();
            skuData.put("size", sku.getSize());
            skuData.put("color", sku.getColor());
            skuData.put("quantity", sku.getQuantity());
            skuData.put("price", sku.getPrice());
            skuData.put("product_id", sku.getProductId());
            long id=database.insert("sku", null, skuData);
            return id;
        }catch(Exception e){
            Log.d("Error:",e.getMessage());
            return 0;
        }finally {
            dbHelper.closeConnect();
        }
    }
}
