package com.skDevs.noteease;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Database extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "notes.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "notes";
    public static final String COLUMN_ID = "id";

    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_DATE = "date";

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_TITLE
                + " TEXT, " + COLUMN_DESCRIPTION + " TEXT, " + COLUMN_DATE + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

     public boolean addData(String title, String description, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TITLE, title);
        contentValues.put(COLUMN_DESCRIPTION, description);
        contentValues.put(COLUMN_DATE, date);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }


    public ArrayList<Map<String,String>> getData() {
        ArrayList<Map<String,String>> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        while (cursor.moveToNext()) {
            Map<String,String> item = new HashMap<String,String>();
            item.put("id", cursor.getString(0));
            item.put("title", cursor.getString(1));
            item.put("description", cursor.getString(2));
            item.put("date", cursor.getString(3));
            list.add(item);
        }
        cursor.close();
        return list;
    }

    public ArrayList<Map<String,String>> getData(String title) {
        ArrayList<Map<String,String>> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_TITLE + " LIKE ?", new String[]{"%" + title + "%"});
        while (cursor.moveToNext()) {
            Map<String,String> item = new HashMap<String,String>();
            item.put("id", cursor.getString(0));
            item.put("title", cursor.getString(1));
            item.put("description", cursor.getString(2));
            item.put("date", cursor.getString(3));
            list.add(item);
        }
        cursor.close();
        return list;
    }

    public boolean updateData(String id, String title, String description, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TITLE, title);
        contentValues.put(COLUMN_DESCRIPTION, description);
        contentValues.put(COLUMN_DATE, date);
        long result = db.update(TABLE_NAME, contentValues, COLUMN_ID + " = ?", new String[]{id});
        return result != -1;
    }

    public boolean deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{id});
        return result != -1;
    }

    Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

}
