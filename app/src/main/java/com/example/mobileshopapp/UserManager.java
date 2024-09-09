package com.example.mobileshopapp;

import static com.example.mobileshopapp.DatabaseHelper.COLUMN_ADDRESS;
import static com.example.mobileshopapp.DatabaseHelper.COLUMN_AVATAR;
import static com.example.mobileshopapp.DatabaseHelper.COLUMN_BIRTHDATE;
import static com.example.mobileshopapp.DatabaseHelper.COLUMN_EMAIL;
import static com.example.mobileshopapp.DatabaseHelper.COLUMN_FIRSTNAME;
import static com.example.mobileshopapp.DatabaseHelper.COLUMN_ID_USER;
import static com.example.mobileshopapp.DatabaseHelper.COLUMN_LASTNAME;
import static com.example.mobileshopapp.DatabaseHelper.COLUMN_PHONE;
import static com.example.mobileshopapp.DatabaseHelper.COLUMN_ROLE;
import static com.example.mobileshopapp.DatabaseHelper.COLUMN_USERNAME;
import static com.example.mobileshopapp.DatabaseHelper.TABLE_NAME;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserManager {
    private DatabaseHelper dbHelper;
    public UserManager(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // lấy user hieenj tại
    public User getUser() {
        User user = dbHelper.getUser();
        return user;
    }

    public User getFirstUser() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] columns = {
                COLUMN_ID_USER,
                COLUMN_USERNAME,
                COLUMN_ROLE,
                COLUMN_FIRSTNAME,
                COLUMN_LASTNAME,
                COLUMN_EMAIL,
                COLUMN_PHONE,
                COLUMN_AVATAR,
                COLUMN_ADDRESS,
                COLUMN_BIRTHDATE
        };

        Cursor cursor = db.query(
                TABLE_NAME,    // Bảng cần truy vấn
                columns,       // Các cột cần lấy
                null,          // Không có điều kiện WHERE
                null,          // Không có tham số của điều kiện WHERE
                null,          // Group by
                null,          // Having
                null,          // Order by
                "1"            // Giới hạn lấy 1 hàng (user đầu tiên)
        );

        if (cursor != null && cursor.moveToFirst()) {
            User user = new User(
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID_USER)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FIRSTNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LASTNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AVATAR)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BIRTHDATE))
            );
            cursor.close();
            return user;
        }

        return null;
    }


}
