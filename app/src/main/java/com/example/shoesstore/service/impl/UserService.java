package com.example.shoesstore.service.impl;

import static com.example.shoesstore.constants.Role.ROLE_USER;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.shoesstore.constants.Role;
import com.example.shoesstore.dto.UserDto;
import com.example.shoesstore.entity.User;
import com.example.shoesstore.mapper.UserMapper;
import com.example.shoesstore.model.UserModel;
import com.example.shoesstore.service.IUserService;
import com.example.shoesstore.util.DatabaseHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class UserService implements IUserService {
    private DatabaseHelper dbHelper;
    private UserMapper userMapper = new UserMapper();

    public UserService(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    @Override
    public long add(UserModel userModel) {
        try {
            User user = userMapper.toEntity(userModel);
            if (userModel.getRole() != null) {
                user.setRole(userModel.getRole().equals(Role.getRoleName(0)) ? 0 : 1);
            } else {
                user.setRole(ROLE_USER.getValue());
            }
            if (!user.getName().equals("admin") && !user.getEmail().equals("admin") && !user.getPassword().equals("admin")) {
                SQLiteDatabase database = dbHelper.openConnect();
                ContentValues values = new ContentValues();
                values.put("name", user.getName());
                values.put("email", user.getEmail());
                values.put("password", user.getPassword());
                values.put("role", user.getRole());
                long id = database.insert("user", null, values);
                return id;
            } else {
                System.out.println("Tài khoản không hợp lệ");
                return 0;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 0;
        } finally {
            dbHelper.closeConnect();
        }
    }

    @Override
    public long update(UserModel userModel, Integer integer) {
        try {
            SQLiteDatabase database = dbHelper.openConnect();
            ContentValues userData = new ContentValues();
            userData.put("name", userModel.getName());
            userData.put("email", userModel.getEmail());
            userData.put("password", userModel.getPassword());
            userData.put("role", userModel.getRole().equals(Role.getRoleName(0)) ? 0 : 1);
            long result = database.update("user", userData, "id=?", new String[]{String.valueOf(integer)});
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
            long result = database.delete("user", "id=?", new String[]{String.valueOf(integer)});
            return result;
        } catch (Exception e) {
            Log.d("Error:", e.getMessage());
            return 0;
        } finally {
            dbHelper.openConnect();
        }
    }

    @Override
    public List<UserDto> getAll() {
        try {
            SQLiteDatabase database = dbHelper.openConnect();
            Cursor cursor = database.rawQuery("select * from user", null);
            List<User> users = new ArrayList<>();
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    User user = new User(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4));
                    users.add(user);
                }
            }
            return users.stream().map(x -> userMapper.toDto(x)).collect(Collectors.toList());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        } finally {
            dbHelper.closeConnect();
        }
    }

    @Override
    public UserDto findById(Integer integer) {
        return null;
    }

    @Override
    public UserDto login(String username, String password) {
        try {
            SQLiteDatabase database = dbHelper.openConnect();
            User user = new User();
            Cursor cursor = database.rawQuery("SELECT * FROM user WHERE email=? AND password=?", new String[]{username, password});
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                user.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                user.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
                user.setRole(cursor.getInt(cursor.getColumnIndexOrThrow("role")));
                cursor.close();
                return userMapper.toDto(user);
            } else {
                cursor.close();
                return null;
            }
        } catch (Exception e) {
            Log.e("UserService", "Login error: " + e.getMessage());
            return null;
        } finally {
            dbHelper.closeConnect();
        }
    }

    @Override
    public List<UserDto> findByName(String name) {
        try {
            List<User> users = new ArrayList<>();
            SQLiteDatabase database = dbHelper.openConnect();
            Cursor cursor = database.rawQuery("select * from user where name like ?", new String[]{"%" + name + "%"});
            if (cursor.getCount() > 0) {
                if (cursor.moveToNext()) {
                    users.add(new User(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4)));
                }
            }
            return users.stream().map(x->userMapper.toDto(x)).collect(Collectors.toList());
        } catch (Exception e) {
            Log.d("Error:", e.getMessage());
            return null;
        } finally {
            dbHelper.closeConnect();
        }
    }
}
