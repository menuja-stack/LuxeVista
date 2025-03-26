package com.example.luxevista.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.luxevista.models.Room;
import java.util.ArrayList;
import java.util.List;

public class RoomService {
    private DatabaseHelper dbHelper;
    private Context context;

    public RoomService(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    // Create
    public long createRoom(Room room) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COLUMN_ROOM_NUMBER, room.getRoomNumber());
        values.put(DatabaseHelper.COLUMN_ROOM_TYPE, room.getRoomType());
        values.put(DatabaseHelper.COLUMN_ROOM_PRICE, room.getPrice());
        values.put(DatabaseHelper.COLUMN_ROOM_CAPACITY, room.getCapacity());
        values.put(DatabaseHelper.COLUMN_ROOM_DESCRIPTION, room.getDescription());
        values.put(DatabaseHelper.COLUMN_ROOM_IS_AVAILABLE, room.isAvailable() ? 1 : 0);
        values.put(DatabaseHelper.COLUMN_ROOM_AMENITIES, room.getAmenities());
        values.put(DatabaseHelper.COLUMN_ROOM_IMAGE_URL, room.getImageUrl());

        long id = db.insert(DatabaseHelper.TABLE_ROOMS, null, values);
        db.close();
        return id;
    }

    // Read single room
    public Room getRoom(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(DatabaseHelper.TABLE_ROOMS,
                null,
                DatabaseHelper.COLUMN_ROOM_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null);

        Room room = null;
        if (cursor != null && cursor.moveToFirst()) {
            room = cursorToRoom(cursor);
            cursor.close();
        }
        db.close();
        return room;
    }

    // Read all rooms
    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(DatabaseHelper.TABLE_ROOMS,
                null,
                null,
                null,
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                rooms.add(cursorToRoom(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return rooms;
    }

    // Update
    public int updateRoom(Room room) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COLUMN_ROOM_NUMBER, room.getRoomNumber());
        values.put(DatabaseHelper.COLUMN_ROOM_TYPE, room.getRoomType());
        values.put(DatabaseHelper.COLUMN_ROOM_PRICE, room.getPrice());
        values.put(DatabaseHelper.COLUMN_ROOM_CAPACITY, room.getCapacity());
        values.put(DatabaseHelper.COLUMN_ROOM_DESCRIPTION, room.getDescription());
        values.put(DatabaseHelper.COLUMN_ROOM_IS_AVAILABLE, room.isAvailable() ? 1 : 0);
        values.put(DatabaseHelper.COLUMN_ROOM_AMENITIES, room.getAmenities());
        values.put(DatabaseHelper.COLUMN_ROOM_IMAGE_URL, room.getImageUrl());

        int rowsAffected = db.update(DatabaseHelper.TABLE_ROOMS,
                values,
                DatabaseHelper.COLUMN_ROOM_ID + "=?",
                new String[]{String.valueOf(room.getId())});
        db.close();
        return rowsAffected;
    }

    // Delete
    public boolean deleteRoom(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsAffected = db.delete(DatabaseHelper.TABLE_ROOMS,
                DatabaseHelper.COLUMN_ROOM_ID + "=?",
                new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected > 0;
    }

    // Get available rooms
    public List<Room> getAvailableRooms() {
        List<Room> rooms = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(DatabaseHelper.TABLE_ROOMS,
                null,
                DatabaseHelper.COLUMN_ROOM_IS_AVAILABLE + "=?",
                new String[]{"1"},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                rooms.add(cursorToRoom(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return rooms;
    }

    // Get rooms by price range
    public List<Room> getRoomsByPriceRange(double minPrice, double maxPrice) {
        List<Room> rooms = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(DatabaseHelper.TABLE_ROOMS,
                null,
                DatabaseHelper.COLUMN_ROOM_PRICE + " BETWEEN ? AND ?",
                new String[]{String.valueOf(minPrice), String.valueOf(maxPrice)},
                null, null, DatabaseHelper.COLUMN_ROOM_PRICE + " ASC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                rooms.add(cursorToRoom(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return rooms;
    }

    // Helper method to convert Cursor to Room object
    private Room cursorToRoom(Cursor cursor) {
        return new Room(
                cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ROOM_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ROOM_NUMBER)),
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ROOM_TYPE)),
                cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ROOM_PRICE)),
                cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ROOM_CAPACITY)),
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ROOM_DESCRIPTION)),
                cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ROOM_IS_AVAILABLE)) == 1,
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ROOM_AMENITIES)),
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ROOM_IMAGE_URL))
        );
    }
}