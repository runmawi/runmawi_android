package com.atbuys.runmawi.Vdocipher;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "runmawitv.db";
    private static final int DATABASE_VERSION = 8; // Keep this version to 8 or above to avoid downgrade

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create your tables here, e.g.,
        String CREATE_TABLE = "CREATE TABLE sample_table (id INTEGER PRIMARY KEY, name TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrade logic here, if any schema changes
        if (oldVersion < newVersion) {
            // Example of upgrading by dropping and recreating
            db.execSQL("DROP TABLE IF EXISTS sample_table");
            onCreate(db);
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Prevent downgrading by ignoring the version change
        Log.w("DatabaseHelper", "Database downgrade attempt ignored.");
        // Optionally, you could add logic to reset the DB to avoid errors
    }
}
