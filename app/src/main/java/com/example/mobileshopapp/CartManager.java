package com.example.mobileshopapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private DatabaseHelper dbHelper;

    public CartManager(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Thêm sản phẩm vào giỏ hàng
    public void addToCart(String productId, String name, String image, double price, int quantity) {
        if (isProductInCart(productId)) {
            // Sản phẩm đã tồn tại, có thể cập nhật số lượng hoặc thực hiện hành động khác nếu cần
            // Ví dụ: cập nhật số lượng sản phẩm
            updateCartItemQuantity(productId, quantity);
        } else {
            // Sản phẩm chưa tồn tại, thêm mới
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(DatabaseHelper.COLUMN_ID_PRODUCT, productId);
            values.put(DatabaseHelper.COLUMN_NAME, name);
            values.put(DatabaseHelper.COLUMN_IMAGE, image);
            values.put(DatabaseHelper.COLUMN_PRICE, price);
            values.put(DatabaseHelper.COLUMN_QUANTITY, quantity);

            db.insert(DatabaseHelper.TABLE_CART_NAME, null, values);
            db.close();
        }
    }


    // Lấy tất cả các sản phẩm trong giỏ hàng
    public Cursor getAllCartItems() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.query(DatabaseHelper.TABLE_CART_NAME, null, null, null, null, null, null);
    }

    // Cập nhật số lượng sản phẩm trong giỏ hàng
    public void updateCartItemQuantity(String productId, int newQuantity) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_QUANTITY, newQuantity);

        String selection = DatabaseHelper.COLUMN_ID_PRODUCT + " = ?";
        String[] selectionArgs = { productId };

        db.update(DatabaseHelper.TABLE_CART_NAME, values, selection, selectionArgs);
        db.close();
    }

    // Xóa sản phẩm khỏi giỏ hàng
    public void removeCartItem(String productId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = DatabaseHelper.COLUMN_ID_PRODUCT + " = ?";
        String[] selectionArgs = { productId };

        db.delete(DatabaseHelper.TABLE_CART_NAME, selection, selectionArgs);
        db.close();
    }

    // Xóa tất cả các sản phẩm khỏi giỏ hàng
    public void clearCart() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_CART_NAME, null, null);
        db.close();
    }


    // Lấy tất cả các sản phẩm trong giỏ hàng dưới dạng danh sách
    public List<Cart> getAllItemCart() {
        List<Cart> cartList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_CART_NAME, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String productId = cursor.getString(0);
                String name = cursor.getString(1);
                String image = cursor.getString(2);
                double price = cursor.getDouble(3);
                int quantity = cursor.getInt(4);
                Cart cartItem = new Cart(productId, name,image, quantity, price);
                cartList.add(cartItem);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return cartList;
    }

    public boolean isProductInCart(String productId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = { DatabaseHelper.COLUMN_ID_PRODUCT };
        String selection = DatabaseHelper.COLUMN_ID_PRODUCT + " = ?";
        String[] selectionArgs = { productId };

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_CART_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

}