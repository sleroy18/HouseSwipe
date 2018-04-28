package com.seancs445.houseswipe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.security.keystore.KeyNotYetValidException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "Authentication";


    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "UserInfo";
    private static final String TABLE_SETTINGS = "settings";
    private static final String KEY_UID = "userId";
    private static final String KEY_FIRST_NAME = "firstName";
    private static final String KEY_LAST_NAME = "lastName";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_DISTANCE = "distance";
    private static final String KEY_PRICE_MIN = "priceMin";
    private static final String KEY_PRICE_MAX = "priceMAX";
    private static final String KEY_ZIP_CODE = "zipCode";
    private static final String KEY_IS_RENTING = "isRenting";
    private static final String KEY_IS_BUYING = "isBuying";
    private static final String KEY_HAS_PET = "hasPet";
    private static final String KEY_MIN_ROOMS = "minRooms";
    private static final String KEY_MIN_BATHS = "minBaths";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SCORE_TABLE = "CREATE TABLE " + TABLE_SETTINGS + "("
                + KEY_UID + " TEXT PRIMARY KEY ,"
                + KEY_FIRST_NAME + " TEXT , "
                + KEY_LAST_NAME + " TEXT , "
                + KEY_EMAIL + " TEXT , "
                + KEY_PASSWORD + " TEXT , "
                + KEY_DISTANCE + " INTEGER , "
                + KEY_PRICE_MIN + " REAL ,"
                + KEY_PRICE_MAX + " REAL ,"
                + KEY_ZIP_CODE + " REAL ,"
                + KEY_IS_RENTING + " TEXT ,"
                + KEY_IS_BUYING + " TEXT ,"
                + KEY_HAS_PET + " TEXT ,"
                + KEY_MIN_ROOMS + " INTEGER ,"
                + KEY_MIN_BATHS + " INTEGER " + ")";
        db.execSQL(CREATE_SCORE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);
        onCreate(db);
    }

    long addUser(User info) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_UID, info.getUID());
        values.put(KEY_FIRST_NAME, info.getFirstName());
        values.put(KEY_LAST_NAME, info.getLastName());
        values.put(KEY_EMAIL, info.getEmail());
        values.put(KEY_PASSWORD, info.getPassword());
        values.put(KEY_DISTANCE, info.getDistance());
        values.put(KEY_PRICE_MIN, info.getPriceMin());
        values.put(KEY_PRICE_MAX, info.getPriceMax());
        values.put(KEY_ZIP_CODE, info.getZipCode());
        values.put(KEY_IS_RENTING, info.isRenting());
        values.put(KEY_IS_BUYING, info.isBuying());
        values.put(KEY_HAS_PET, info.isHasPet());
        values.put(KEY_MIN_ROOMS, info.getMinBedNumber());
        values.put(KEY_MIN_BATHS, info.getMinBathNumber());

        long rowId = db.insert(TABLE_SETTINGS, null, values);
        db.close();
        return rowId;
    }

    public User getUser(String UID) {
        String selectQuery = "SELECT * FROM " + TABLE_SETTINGS + " WHERE " + KEY_UID + " ='" + UID + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        User info = new User();
        //if(cursor != null){
        while (cursor.moveToNext()) {
            info.setUID(cursor.getString(cursor.getColumnIndex(KEY_UID)));
            info.setFirstName(cursor.getString(cursor.getColumnIndex(KEY_FIRST_NAME)));
            info.setLastName(cursor.getString(cursor.getColumnIndex(KEY_LAST_NAME)));
            info.setEmail(cursor.getString(cursor.getColumnIndex(KEY_EMAIL)));
            info.setPassword(cursor.getString(cursor.getColumnIndex(KEY_PASSWORD)));
            info.setDistance(cursor.getInt(cursor.getColumnIndex(KEY_DISTANCE)));
            info.setPriceMin(cursor.getDouble(cursor.getColumnIndex(KEY_PRICE_MIN)));
            info.setPriceMax(cursor.getDouble(cursor.getColumnIndex(KEY_PRICE_MAX)));
            info.setZipCode(cursor.getInt(cursor.getColumnIndex(KEY_ZIP_CODE)));
            info.setRenting(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(KEY_IS_RENTING))));
            info.setBuying(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(KEY_IS_BUYING))));
            info.setHasPet(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(KEY_HAS_PET))));
            info.setMinBedNumber(cursor.getInt(cursor.getColumnIndex(KEY_MIN_ROOMS)));
            info.setMinBathNumber(cursor.getInt(cursor.getColumnIndex(KEY_MIN_BATHS)));
        }
        cursor.close();
        db.close();
        return info;
    }

    public int UpdateUser(User user)  {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_FIRST_NAME, user.getFirstName());
        contentValues.put(KEY_LAST_NAME, user.getLastName());
        contentValues.put(KEY_MIN_BATHS, user.getMinBathNumber());
        contentValues.put(KEY_MIN_ROOMS, user.getMinBedNumber());
        contentValues.put(KEY_PRICE_MIN, user.getPriceMin());
        contentValues.put(KEY_PRICE_MAX, user.getPriceMax());
        contentValues.put(KEY_HAS_PET, user.isHasPet());
        contentValues.put(KEY_IS_BUYING, String.valueOf(user.isBuying()));
        contentValues.put(KEY_IS_RENTING, user.isRenting());
        contentValues.put(KEY_ZIP_CODE, user.getZipCode());
        contentValues.put(KEY_DISTANCE, user.getDistance());
        String [] whereArgs = {user.getUID()};
        int count = db.update(
                TABLE_SETTINGS,
                contentValues,
                KEY_UID+"=?",
                whereArgs
        );
        return count;
    }
}