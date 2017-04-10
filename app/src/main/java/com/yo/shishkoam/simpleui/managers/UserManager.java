package com.yo.shishkoam.simpleui.managers;

import android.content.Context;
import android.database.sqlite.SQLiteException;

import com.yo.shishkoam.simpleui.db.UserDbHelper;

import java.util.HashMap;

/**
 * Created by User on 08.04.2017
 */

public enum UserManager {
    INSTANCE;

    private HashMap<String, String> userMap;
    private UserDbHelper dbHelper;

    public void init(Context context) {
        dbHelper = new UserDbHelper(context);
        try {
            userMap = dbHelper.readData();
        } catch (SQLiteException ex) {
            userMap = new HashMap<>();
        }
    }

    public boolean registrUser(String username, String password) {
        if (userMap.containsKey(username)) {
            return false;
        }
        userMap.put(username, password);
        dbHelper.addUser(username, password);
        return true;
    }

    public boolean authorize(String username, String password) {
        return userMap.containsKey(username) && userMap.get(username).equals(password);
    }
}
