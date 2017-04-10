package com.yo.shishkoam.simpleui.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

/**
 * Created by User on 08.04.2017
 */

public class UserDbHelper extends SQLiteOpenHelper {

    private final static String ID = "id";
    String USERNAME = "adult";
    String PASSWORD = "overview";
    String DB_NAME = "users";

    public UserDbHelper(Context context) {
        super(context, "myDB2", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createDatabase(DB_NAME, db);
    }

    private void createDatabase(String databaseName, SQLiteDatabase db) {
        // create table
        db.execSQL("create table " + databaseName + " ("
                + ID + " integer primary key autoincrement,"
                + USERNAME + " string,"
                + PASSWORD + " string"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addUser(String user, String password) {
        SQLiteDatabase db = getWritableDatabase();
        // create object for data
        ContentValues cv = new ContentValues();
        // prepare pairs to insert
        cv.put(USERNAME, user);
        cv.put(PASSWORD, password);
        //insert object to db
        db.insert(DB_NAME, null, cv);
        close();
    }

    public HashMap<String, String> readData() throws SQLiteException {
        HashMap<String, String> userMap = new HashMap<>();
        SQLiteDatabase db = getWritableDatabase();
        // request all data (cursor) from table
        Cursor c = db.query(DB_NAME, null, null, null, null, null, null);
        // check that table has data
        if (c.moveToFirst()) {
            // get column index by name
            int idColIndex = c.getColumnIndex(ID);
            int usernameIdColIndex = c.getColumnIndex(USERNAME);
            int passwordColIndex = c.getColumnIndex(PASSWORD);
            do {

                // get data by column indexes
                int id = c.getInt(idColIndex);
                String username = c.getString(usernameIdColIndex);
                String password = c.getString(passwordColIndex);
                userMap.put(username, password);
            } while (c.moveToNext());
        }
        c.close();
        close();
        return userMap;
    }
}
