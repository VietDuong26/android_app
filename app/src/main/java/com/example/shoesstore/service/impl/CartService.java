package com.example.shoesstore.service.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.shoesstore.dto.CartDto;
import com.example.shoesstore.entity.Cart;
import com.example.shoesstore.mapper.CartMapper;
import com.example.shoesstore.model.CartModel;
import com.example.shoesstore.util.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CartService {

    private DatabaseHelper dbHelper;

    private CartMapper cartMapper;

    public CartService(Context context) {
        this.dbHelper = new DatabaseHelper(context);
        this.cartMapper = new CartMapper(context);
    }

    public long addToCart(CartModel model) {
        try {
            SQLiteDatabase database = dbHelper.openConnect();
            ContentValues cartData = new ContentValues();
            Cart cart = cartMapper.toEntity(model);

            if (cart.getId() == 0) {//th1. thêm sản phẩm từ trang chi tiết sản phẩm
                Cart sltCart = findBySkuIdAndUserId(database, model.getSkuId(), model.getUserId());

                if (sltCart != null) {
                    cartData.put("quantity", sltCart.getQuantity() + cart.getQuantity());
                    long result = database.update("cart", cartData, "id=?", new String[]{String.valueOf(sltCart.getId())});
                    return result;
                } else {
                    cartData.put("user_id", cart.getUserId());
                    cartData.put("sku_id", cart.getSkuId());
                    cartData.put("quantity", cart.getQuantity());
                    long id = database.insert("cart", null, cartData);
                    return id;
                }
            } else { //th2. thêm sản phẩm từ trang cart
                Cart selectedCart = findById(database, cart.getId());
                cartData.put("quantity", selectedCart.getQuantity() + 1);
                long result = database.update("cart", cartData, "id=?", new String[]{String.valueOf(cart.getId())});
                return result;
            }
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            return 0;
        } finally {
            dbHelper.closeConnect();
        }
    }

    public long removeFromCart(int cartId) {
        try {
            SQLiteDatabase database = dbHelper.openConnect();
            long result = database.delete("cart", "id=?", new String[]{String.valueOf(cartId)});
            return result;
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            return 0;
        } finally {
            dbHelper.closeConnect();
        }
    }

    public long removeOneFromCart(int cartId) {
        try {

            SQLiteDatabase database = dbHelper.openConnect();
            Cart cart = findById(database, cartId);
            ContentValues cartData = new ContentValues();
            cartData.put("quantity", cart.getQuantity() - 1);
            long result = database.update("cart", cartData, "id=?", new String[]{
                    String.valueOf(cartId)
            });
            return result;
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            return 0;
        } finally {
            dbHelper.closeConnect();
        }
    }

    public Cart findById(SQLiteDatabase database, int cartId) {
        Cursor cursor = null;
        try {
            cursor = database.rawQuery("SELECT * FROM cart WHERE id = ?", new String[]{String.valueOf(cartId)});
            if (cursor.moveToFirst()) {
                return new Cart(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("user_id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("sku_id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("quantity"))
                );
            }
            return null;
        } catch (Exception e) {
            Log.e("findById", "Lỗi khi truy vấn cartId: " + cartId, e);
            return null;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    public CartDto findDtoById(int cartId) {
        Cursor cursor = null;
        try {
            SQLiteDatabase database = dbHelper.openConnect();
            cursor = database.rawQuery("SELECT * FROM cart WHERE id = ?", new String[]{String.valueOf(cartId)});
            if (cursor.moveToFirst()) {
                return cartMapper.toDto(new Cart(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("user_id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("sku_id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("quantity"))
                ));
            }
            return null;
        } catch (Exception e) {
            Log.e("findById", "Lỗi khi truy vấn cartId: " + cartId, e);
            return null;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    public Cart findBySkuIdAndUserId(SQLiteDatabase database, int skuId, int userId) {
        Cursor cursor = null;
        try {
            cursor = database.rawQuery(
                    "SELECT * FROM cart WHERE sku_id = ? AND user_id = ?",
                    new String[]{String.valueOf(skuId), String.valueOf(userId)}
            );
            if (cursor.moveToFirst()) {
                return new Cart(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("user_id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("sku_id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("quantity"))
                );
            }
            return null;
        } catch (Exception e) {
            Log.e("findBySkuIdAndUserId", "Lỗi khi truy vấn skuId: " + skuId + ", userId: " + userId, e);
            return null;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    public List<CartDto> findAllByUserId(int userId) {
        try {
            SQLiteDatabase database = dbHelper.openConnect();
            List<Cart> carts = new ArrayList<>();
            Cursor cursor = database.rawQuery("select * from cart where user_id=?",
                    new String[]{String.valueOf(userId)});
            if (cursor.moveToFirst()) {
                do {
                    carts.add(new Cart(cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                            cursor.getInt(cursor.getColumnIndexOrThrow("user_id")),
                            cursor.getInt(cursor.getColumnIndexOrThrow("sku_id")),
                            cursor.getInt(cursor.getColumnIndexOrThrow("quantity"))));
                } while (cursor.moveToNext());
            }
            return carts.stream().map(x -> cartMapper.toDto(x)).collect(Collectors.toList());
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            return null;
        } finally {
            dbHelper.closeConnect();
        }
    }

    public long removeCartItem(int cartId, int userId) {
        try {
            SQLiteDatabase database = dbHelper.openConnect();
            long result = database.delete("cart", "id=? and user_id=?", new String[]{String.valueOf(cartId), String.valueOf(userId)});
            return result;
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            return 0;
        } finally {
            dbHelper.closeConnect();
        }
    }

}
