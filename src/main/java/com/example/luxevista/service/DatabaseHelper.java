package com.example.luxevista.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "LuxeVista.db";
    private static final int DATABASE_VERSION = 2;

    // Table names
    public static final String TABLE_USERS = "users";
    public static final String TABLE_ROOMS = "rooms";
    public static final String TABLE_BOOKINGS = "bookings";

    // User table columns
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USER_NAME = "name";
    public static final String COLUMN_USER_EMAIL = "email";
    public static final String COLUMN_USER_ADDRESS = "address";
    public static final String COLUMN_USER_PASSWORD = "password";

    // Room table columns
    public static final String COLUMN_ROOM_ID = "id";
    public static final String COLUMN_ROOM_NUMBER = "room_number";
    public static final String COLUMN_ROOM_TYPE = "room_type";
    public static final String COLUMN_ROOM_PRICE = "price";
    public static final String COLUMN_ROOM_CAPACITY = "capacity";
    public static final String COLUMN_ROOM_DESCRIPTION = "description";
    public static final String COLUMN_ROOM_IS_AVAILABLE = "is_available";
    public static final String COLUMN_ROOM_AMENITIES = "amenities";
    public static final String COLUMN_ROOM_IMAGE_URL = "image_url";

    // Booking table columns
    public static final String COLUMN_BOOKING_ID = "id";
    public static final String COLUMN_BOOKING_USER_ID = "user_id";
    public static final String COLUMN_BOOKING_ROOM_ID = "room_id";
    public static final String COLUMN_BOOKING_DATE = "booking_date";
    public static final String COLUMN_BOOKING_DURATION = "duration";
    public static final String COLUMN_BOOKING_STATUS = "status";

    // Create users table query
    private static final String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_USER_NAME + " TEXT NOT NULL, "
            + COLUMN_USER_EMAIL + " TEXT UNIQUE NOT NULL, "
            + COLUMN_USER_ADDRESS + " TEXT, "
            + COLUMN_USER_PASSWORD + " TEXT NOT NULL"
            + ")";

    // Create rooms table query
    private static final String CREATE_ROOMS_TABLE = "CREATE TABLE " + TABLE_ROOMS + "("
            + COLUMN_ROOM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_ROOM_NUMBER + " TEXT NOT NULL, "
            + COLUMN_ROOM_TYPE + " TEXT NOT NULL, "
            + COLUMN_ROOM_PRICE + " REAL NOT NULL, "
            + COLUMN_ROOM_CAPACITY + " INTEGER NOT NULL, "
            + COLUMN_ROOM_DESCRIPTION + " TEXT, "
            + COLUMN_ROOM_IS_AVAILABLE + " INTEGER NOT NULL, "
            + COLUMN_ROOM_AMENITIES + " TEXT, "
            + COLUMN_ROOM_IMAGE_URL + " TEXT"
            + ")";

    // Create bookings table query
    private static final String CREATE_BOOKINGS_TABLE = "CREATE TABLE " + TABLE_BOOKINGS + "("
            + COLUMN_BOOKING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_BOOKING_USER_ID + " INTEGER NOT NULL, "
            + COLUMN_BOOKING_ROOM_ID + " INTEGER NOT NULL, "
            + COLUMN_BOOKING_DATE + " TEXT NOT NULL, "
            + COLUMN_BOOKING_DURATION + " INTEGER NOT NULL, "
            + COLUMN_BOOKING_STATUS + " TEXT NOT NULL, "
            + "FOREIGN KEY(" + COLUMN_BOOKING_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "), "
            + "FOREIGN KEY(" + COLUMN_BOOKING_ROOM_ID + ") REFERENCES " + TABLE_ROOMS + "(" + COLUMN_ROOM_ID + ")"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_ROOMS_TABLE);
        db.execSQL(CREATE_BOOKINGS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROOMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKINGS);
        onCreate(db);
    }
}
