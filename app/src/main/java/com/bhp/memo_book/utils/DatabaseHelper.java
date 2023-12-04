package com.bhp.memo_book.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.bhp.memo_book.MainApplication;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "mydatabase.db";
    private static final int DATABASE_VERSION = 1;

    private static final String USER_TABLE_NAME = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_GENDER = "gender";
    private static final String COLUMN_SIGN = "sign";
    private static final String TAG = "DatabaseHelper";

    private static DatabaseHelper instance = null;

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DatabaseHelper getInstance(Context context) {
        return new DatabaseHelper(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + USER_TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_PHONE + " TEXT, "
                + COLUMN_PASSWORD + " TEXT, "
                + COLUMN_USERNAME + " TEXT, "
                + COLUMN_EMAIL + " TEXT,"
                + COLUMN_GENDER + " INTEGER default 0,"
                + COLUMN_SIGN + " TEXT"
                + ")";
        db.execSQL(createTableQuery);

        db.execSQL("create table res (_NAME varchar(20) ,_DATE varchar(20),_HEAD varchar(20),_MSG varchar(20),_TYPE int);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropTableQuery = "DROP TABLE IF EXISTS " + USER_TABLE_NAME;
        db.execSQL(dropTableQuery);
        db.execSQL("drop table if exists res");
        onCreate(db);
    }

    public long insertUserInfo(String phone, String password, String userName, String email) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_USERNAME, userName);
            values.put(COLUMN_PHONE, phone);
            values.put(COLUMN_PASSWORD, password);
            values.put(COLUMN_EMAIL, email);
            values.put(COLUMN_GENDER, 0);
            values.put(COLUMN_SIGN, "");
            long uid = db.insert(USER_TABLE_NAME, null, values);
            db.close();
            return uid;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return -1;
        }
    }

    public long validateLogin(String phone, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ID};
        String selection = COLUMN_PHONE + " = ? AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {phone, password};
        Cursor cursor = db.query(USER_TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        long uid = -1;

        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            uid = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID));
            cursor.close();
        }

        db.close();
        return uid;
    }

    public String getColumnUsername(long uid) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT " + COLUMN_USERNAME + " FROM " + USER_TABLE_NAME +
                " WHERE " + COLUMN_ID + " = '" + uid + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME));
            cursor.close();
            return name;
        }
        db.close();
        return "";
    }


}
