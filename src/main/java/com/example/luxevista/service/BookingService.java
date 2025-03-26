package com.example.luxevista.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.luxevista.models.Booking;

import java.util.ArrayList;
import java.util.List;

public class BookingService {
    private DatabaseHelper dbHelper;
    private Context context;

    public BookingService(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    // Create booking
    public long createBooking(Booking booking) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_BOOKING_USER_ID, booking.getUserId());
        values.put(DatabaseHelper.COLUMN_BOOKING_ROOM_ID, booking.getRoomId());
        values.put(DatabaseHelper.COLUMN_BOOKING_DATE, booking.getBookingDate());
        values.put(DatabaseHelper.COLUMN_BOOKING_DURATION, booking.getDuration());
        values.put(DatabaseHelper.COLUMN_BOOKING_STATUS, booking.getStatus());

        long id = db.insert(DatabaseHelper.TABLE_BOOKINGS, null, values);
        db.close();
        return id;
    }

    // Read booking by ID
    public Booking getBooking(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(DatabaseHelper.TABLE_BOOKINGS,
                new String[]{DatabaseHelper.COLUMN_BOOKING_ID, DatabaseHelper.COLUMN_BOOKING_USER_ID,
                        DatabaseHelper.COLUMN_BOOKING_ROOM_ID, DatabaseHelper.COLUMN_BOOKING_DATE,
                        DatabaseHelper.COLUMN_BOOKING_DURATION, DatabaseHelper.COLUMN_BOOKING_STATUS},
                DatabaseHelper.COLUMN_BOOKING_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);

        Booking booking = null;
        if (cursor != null && cursor.moveToFirst()) {
            booking = new Booking(
                    cursor.getInt(0),
                    cursor.getInt(1),
                    cursor.getInt(2),
                    cursor.getString(3),
                    cursor.getInt(4),
                    cursor.getString(5)
            );
            cursor.close();
        }
        db.close();
        return booking;
    }

    // Get all bookings
    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_BOOKINGS;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Booking booking = new Booking(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getInt(2),
                        cursor.getString(3),
                        cursor.getInt(4),
                        cursor.getString(5)
                );
                bookings.add(booking);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return bookings;
    }

    // Update booking
    public int updateBooking(Booking booking) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_BOOKING_USER_ID, booking.getUserId());
        values.put(DatabaseHelper.COLUMN_BOOKING_ROOM_ID, booking.getRoomId());
        values.put(DatabaseHelper.COLUMN_BOOKING_DATE, booking.getBookingDate());
        values.put(DatabaseHelper.COLUMN_BOOKING_DURATION, booking.getDuration());
        values.put(DatabaseHelper.COLUMN_BOOKING_STATUS, booking.getStatus());

        int rowsAffected = db.update(DatabaseHelper.TABLE_BOOKINGS, values,
                DatabaseHelper.COLUMN_BOOKING_ID + "=?",
                new String[]{String.valueOf(booking.getId())});
        db.close();
        return rowsAffected;
    }

    // Delete booking
    public void deleteBooking(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_BOOKINGS,
                DatabaseHelper.COLUMN_BOOKING_ID + "=?",
                new String[]{String.valueOf(id)});
        db.close();
    }
}
