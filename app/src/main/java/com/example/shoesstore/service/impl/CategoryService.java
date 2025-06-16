package com.example.shoesstore.service.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.shoesstore.dto.CategoryDto;
import com.example.shoesstore.entity.Category;
import com.example.shoesstore.mapper.CategoryMapper;
import com.example.shoesstore.model.CategoryModel;
import com.example.shoesstore.service.ICategoryService;
import com.example.shoesstore.util.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryService implements ICategoryService {
    private DatabaseHelper dbHelper;
    CategoryMapper categoryMapper = new CategoryMapper();

    public CategoryService(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    @Override
    public long add(CategoryModel categoryModel) {
        try {
            SQLiteDatabase database = dbHelper.openConnect();
            ContentValues categoryData = new ContentValues();
            categoryData.put("name", categoryModel.getName());
            long id = database.insert("category", null, categoryData);
            return id;
        } catch (Exception e) {
            Log.d("Error:", e.getMessage());
            return 0;
        } finally {
            dbHelper.closeConnect();
        }
    }

    @Override
    public long update(CategoryModel categoryModel, Integer integer) {
        try {
            SQLiteDatabase database = dbHelper.openConnect();
            ContentValues categoryData = new ContentValues();
            categoryData.put("name", categoryModel.getName());
            long result = database.update("category", categoryData, "id=?", new String[]{String.valueOf(integer)});
            return result;
        } catch (Exception e) {
            Log.d("Error:", e.getMessage());
            return 0;
        } finally {
            dbHelper.closeConnect();
        }
    }

    @Override
    public long delete(Integer integer) {
        try {
            SQLiteDatabase database = dbHelper.openConnect();
            long result = database.delete("category", "id=?", new String[]{String.valueOf(integer)});
            return result;
        } catch (Exception e) {
            Log.d("Error:", e.getMessage());
            return 0;
        } finally {
            dbHelper.closeConnect();
        }
    }

    @Override
    public List<CategoryDto> getAll() {
        List<Category> categories = new ArrayList<>();
        try {
            dbHelper.openConnect();
            SQLiteDatabase database = dbHelper.openConnect();
            Cursor cursor = database.rawQuery("select * from category", null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    categories.add(new Category(cursor.getInt(0),
                            cursor.getString(1)));
                }
            }
            return categories.stream().map(x -> categoryMapper.toDto(x))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return null;
        } finally {
            dbHelper.closeConnect();
        }
    }

    @Override
    public CategoryDto findById(Integer integer) {
        try {
            SQLiteDatabase database = dbHelper.openConnect();
            Cursor cursor = database.rawQuery("select * from category where id=?", new String[]{String.valueOf(integer)});
            if (cursor.moveToFirst()) {
                return categoryMapper.toDto(new Category(cursor.getInt(0), cursor.getString(1)));
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

    @Override
    public List<CategoryDto> findByName(String name) {
        try {
            List<Category> categories = new ArrayList<>();
            SQLiteDatabase database = dbHelper.openConnect();
            Cursor cursor = database.rawQuery("select * from category where name like ?", new String[]{"%" + name + "%"});
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    categories.add(new Category(cursor.getInt(0), cursor.getString(1)));
                }
            }
            return categories.stream().map(x -> categoryMapper.toDto(x)).collect(Collectors.toList());
        } catch (Exception e) {
            return null;
        } finally {
            dbHelper.closeConnect();
        }
    }
}
