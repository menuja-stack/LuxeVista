package com.example.luxevista.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.luxevista.models.AppUser;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    private DatabaseHelper dbHelper;
    private Context context;

    public UserService(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    // Create user
    public long createUser(AppUser user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_USER_NAME, user.getName());
        values.put(DatabaseHelper.COLUMN_USER_EMAIL, user.getEmail());
        values.put(DatabaseHelper.COLUMN_USER_ADDRESS, user.getAddress());
        values.put(DatabaseHelper.COLUMN_USER_PASSWORD, user.getPassword());

        long id = db.insert(DatabaseHelper.TABLE_USERS, null, values);
        db.close();
        return id;
    }

    // Read user by ID
    public AppUser getUser(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(DatabaseHelper.TABLE_USERS,
                new String[]{DatabaseHelper.COLUMN_USER_ID, DatabaseHelper.COLUMN_USER_NAME,
                        DatabaseHelper.COLUMN_USER_EMAIL, DatabaseHelper.COLUMN_USER_ADDRESS,
                        DatabaseHelper.COLUMN_USER_PASSWORD},
                DatabaseHelper.COLUMN_USER_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        AppUser user = null;
        if (cursor != null && cursor.moveToFirst()) {
            user = new AppUser(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4)
            );
            cursor.close();
        }
        db.close();
        return user;
    }

    // Get all users
    public List<AppUser> getAllUsers() {
        List<AppUser> users = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_USERS;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                AppUser user = new AppUser(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4)
                );
                users.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return users;
    }

    // Update user
    public int updateUser(AppUser user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_USER_NAME, user.getName());
        values.put(DatabaseHelper.COLUMN_USER_EMAIL, user.getEmail());
        values.put(DatabaseHelper.COLUMN_USER_ADDRESS, user.getAddress());
        values.put(DatabaseHelper.COLUMN_USER_PASSWORD, user.getPassword());

        int rowsAffected = db.update(DatabaseHelper.TABLE_USERS, values,
                DatabaseHelper.COLUMN_USER_ID + "=?",
                new String[]{String.valueOf(user.getId())});
        db.close();
        return rowsAffected;
    }

    // Delete user
    public void deleteUser(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_USERS,
                DatabaseHelper.COLUMN_USER_ID + "=?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    // Login method
    public AppUser login(String email, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(DatabaseHelper.TABLE_USERS,
                new String[]{DatabaseHelper.COLUMN_USER_ID, DatabaseHelper.COLUMN_USER_NAME,
                        DatabaseHelper.COLUMN_USER_EMAIL, DatabaseHelper.COLUMN_USER_ADDRESS,
                        DatabaseHelper.COLUMN_USER_PASSWORD},
                DatabaseHelper.COLUMN_USER_EMAIL + "=? AND " + DatabaseHelper.COLUMN_USER_PASSWORD + "=?",
                new String[]{email, password},
                null, null, null);

        AppUser user = null;
        if (cursor != null && cursor.moveToFirst()) {
            user = new AppUser(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4)
            );
            cursor.close();
        }
        db.close();
        return user;
    }

    // Check if email exists
    public boolean isEmailExists(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_USERS,
                new String[]{DatabaseHelper.COLUMN_USER_ID},
                DatabaseHelper.COLUMN_USER_EMAIL + "=?",
                new String[]{email},
                null, null, null);

        boolean exists = cursor != null && cursor.moveToFirst();
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return exists;
    }
}