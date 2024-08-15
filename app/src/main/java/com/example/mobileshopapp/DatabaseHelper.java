package com.example.mobileshopapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "userdatabase.db";
    private static final int DATABASE_VERSION = 1;

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

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
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
    public User getUserById(String idUser) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_ID_USER, COLUMN_USERNAME, COLUMN_ROLE,
                        COLUMN_FIRSTNAME, COLUMN_LASTNAME, COLUMN_EMAIL, COLUMN_PHONE,
                        COLUMN_AVATAR, COLUMN_ADDRESS, COLUMN_BIRTHDATE},
                COLUMN_ID_USER + "=?", new String[]{idUser},
                null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        User user = new User(
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

        cursor.close();
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

}
