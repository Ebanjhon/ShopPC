package com.example.mobileshopapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "userdatabase.db";
    private static final int DATABASE_VERSION = 2;

    // thông tin các trường cho user
    public static final String TABLE_NAME = "users";
    public static final String COLUMN_ID_USER = "idUser";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_ROLE = "role";
    public static final String COLUMN_FIRSTNAME = "firstname";
    public static final String COLUMN_LASTNAME = "lastname";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_AVATAR = "avatar";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_BIRTHDATE = "birthDate";

    // thông tin các trường cho CART
    public static final String TABLE_CART_NAME = "carts";
    public static final String COLUMN_ID_PRODUCT = "idProduct";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_QUANTITY = "quantity";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // tạo bảng user
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID_USER + " TEXT PRIMARY KEY,"
                + COLUMN_USERNAME + " TEXT,"
                + COLUMN_ROLE + " TEXT,"
                + COLUMN_FIRSTNAME + " TEXT,"
                + COLUMN_LASTNAME + " TEXT,"
                + COLUMN_EMAIL + " TEXT,"
                + COLUMN_PHONE + " TEXT,"
                + COLUMN_AVATAR + " TEXT,"
                + COLUMN_ADDRESS + " TEXT,"
                + COLUMN_BIRTHDATE + " TEXT" + ")";
        db.execSQL(CREATE_USER_TABLE);

        // Tạo bảng cart
        String CREATE_CART_TABLE = "CREATE TABLE " + TABLE_CART_NAME + "("
                + COLUMN_ID_PRODUCT + " TEXT PRIMARY KEY,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_IMAGE + " TEXT,"
                + COLUMN_PRICE + " REAL,"
                + COLUMN_QUANTITY + " INTEGER"
                + ")";
        db.execSQL(CREATE_CART_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART_NAME);
        onCreate(db);
    }

    // hàm lưu dữ liệu user
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_ID_USER, user.getIdUser());
        values.put(COLUMN_USERNAME, user.getUsername());
        values.put(COLUMN_ROLE, user.getRole());
        values.put(COLUMN_FIRSTNAME, user.getFirstname());
        values.put(COLUMN_LASTNAME, user.getLastname());
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_PHONE, user.getPhone());
        values.put(COLUMN_AVATAR, user.getAvatar());
        values.put(COLUMN_ADDRESS, user.getAddress());
        values.put(COLUMN_BIRTHDATE, user.getBirthDate());

        // Chèn dữ liệu vào bảng
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    // lấy dữ liệu user
    public User getUser() {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;

        Cursor cursor = db.query(TABLE_NAME, new String[]{
                        COLUMN_ID_USER, COLUMN_USERNAME, COLUMN_ROLE,
                        COLUMN_FIRSTNAME, COLUMN_LASTNAME, COLUMN_EMAIL,
                        COLUMN_PHONE, COLUMN_AVATAR, COLUMN_ADDRESS,
                        COLUMN_BIRTHDATE},
                null, null, null, null, null, "1"); // Giới hạn kết quả là 1 dòng

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                user = new User(
                        cursor.getString(0),  // idUser
                        cursor.getString(1),  // username
                        cursor.getString(2),  // role
                        cursor.getString(3),  // firstname
                        cursor.getString(4),  // lastname
                        cursor.getString(5),  // email
                        cursor.getString(6),  // phone
                        cursor.getString(7),  // avatar
                        cursor.getString(8),  // address
                        cursor.getString(9)   // birthDate
                );
            }
            cursor.close();
        }

        db.close();
        return user;
    }

    // xóa bảng user
    public void deleteTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
        db.close();
    }

     // cập nhật user
     public void updateAvatar(String userId, String newAvatarUrl) {
         SQLiteDatabase db = this.getWritableDatabase();
         ContentValues values = new ContentValues();

         values.put(COLUMN_AVATAR, newAvatarUrl);

         // Điều kiện cập nhật dựa trên ID người dùng
         String selection = COLUMN_ID_USER + " = ?";
         String[] selectionArgs = { userId };

         // Cập nhật dữ liệu vào bảng
         int count = db.update(TABLE_NAME, values, selection, selectionArgs);
         db.close();
     }

    // Xóa người dùng theo ID
    public void deleteUserById(String userId) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Điều kiện xóa dựa trên ID người dùng
        String selection = COLUMN_ID_USER + " = ?";
        String[] selectionArgs = { userId };

        // Xóa dữ liệu khỏi bảng
        db.delete(TABLE_NAME, selection, selectionArgs);
        db.close();
    }


}
