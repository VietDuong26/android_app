package com.example.shoesstore.util;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ShoeStore.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TAG = "DatabaseHelper";
    private SQLiteDatabase database;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public SQLiteDatabase openConnect() {
        return database = this.getWritableDatabase();
    }

    public void closeConnect() {
        this.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            Log.d(TAG, "Creating database tables...");
            db.execSQL("CREATE TABLE IF NOT EXISTS user (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name VARCHAR(45), " +
                    "email VARCHAR(45), " +
                    "password VARCHAR(45), " +
                    "role INT)");
            db.execSQL("CREATE TABLE IF NOT EXISTS category (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name VARCHAR(45))");
            db.execSQL("CREATE TABLE IF NOT EXISTS product (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name VARCHAR(45), " +
                    "description VARCHAR(500)," +
                    "category_id INT, " +
                    "FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE CASCADE ON UPDATE CASCADE)");
            db.execSQL("CREATE TABLE IF NOT EXISTS sku (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "size VARCHAR(45), " +
                    "color VARCHAR(45), " +
                    "quantity INT, " +
                    "price BIGINT, " +
                    "product_id INT, " +
                    "FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE ON UPDATE CASCADE)");
            db.execSQL("CREATE TABLE IF NOT EXISTS product_image (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "link VARCHAR(500), " +
                    "product_id INT, " +
                    "FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE ON UPDATE CASCADE)");
            db.execSQL("CREATE TABLE IF NOT EXISTS cart (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "user_id INT, " +
                    "sku_id INT, " +
                    "quantity INT, " +
                    "FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE ON UPDATE CASCADE, " +
                    "FOREIGN KEY (sku_id) REFERENCES sku(id) ON DELETE CASCADE ON UPDATE CASCADE)");
            db.execSQL("CREATE TABLE IF NOT EXISTS orders (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "user_id INT, " +
                    "created_at TIMESTAMP, " +
                    "status INT, " +
                    "name VARCHAR(200), " +
                    "phone VARCHAR(10), " +
                    "address VARCHAR(200), " +
                    "FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE ON UPDATE CASCADE)");
            db.execSQL("CREATE TABLE IF NOT EXISTS orders_item (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "sku_id INT, " +
                    "orders_id INT, " +
                    "quantity INT, " +
                    "FOREIGN KEY (sku_id) REFERENCES sku(id) ON DELETE CASCADE ON UPDATE CASCADE, " +
                    "FOREIGN KEY (orders_id) REFERENCES orders(id) ON DELETE CASCADE ON UPDATE CASCADE)");
            db.execSQL("INSERT INTO user(name,email,password,role) VALUES('admin','admin','admin',0)");
            Log.d(TAG, "Database tables created successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error creating database tables: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP TABLE IF EXISTS orders_item");
            db.execSQL("DROP TABLE IF EXISTS orders");
            db.execSQL("DROP TABLE IF EXISTS cart");
            db.execSQL("DROP TABLE IF EXISTS product_image");
            db.execSQL("DROP TABLE IF EXISTS sku");
            db.execSQL("DROP TABLE IF EXISTS product");
            db.execSQL("DROP TABLE IF EXISTS category");
            db.execSQL("DROP TABLE IF EXISTS user");
            onCreate(db);
            Log.d(TAG, "Database upgrade completed successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error upgrading database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }
}
