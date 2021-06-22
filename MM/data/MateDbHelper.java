package com.coen268.moviemate.data;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.coen268.moviemate.data.MateContract.MateEntry;
/**
 * Database helper for Pets app. Manages database creation and version management.
 */
public class MateDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = MateDbHelper.class.getSimpleName();

    /** Name of the database file */
    private static final String DATABASE_NAME = "mate.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link MateDbHelper}.
     *
     * @param context of the app
     */
    public MateDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the mates table
        String SQL_CREATE_MATE_TABLE =  "CREATE TABLE " + MateEntry.TABLE_NAME_MATE + " ("
                + MateEntry.MATE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MateEntry.COLUMN_MATE_NAME + " TEXT NOT NULL, "
                + MateEntry.COLUMN_MATE_EMAIL + " TEXT NOT NULL, "
                + MateEntry.COLUMN_MATE_PASSWORD + " TEXT NOT NULL);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_MATE_TABLE);

        String SQL_CREATE_MATE_MOVIE_TABLE =  "CREATE TABLE " + MateEntry.TABLE_NAME_MATE_MOVIE + " ("
                + MateEntry.MATE_MOVIE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MateEntry.COLUMN_MOVIE_NAME + " TEXT NOT NULL, "
                + MateEntry.COLUMN_MOVIE_MATE_NAME + " TEXT NOT NULL);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_MATE_MOVIE_TABLE);

        String SQL_CREATE_MATE_WATCH_MOVIE_TABLE =  "CREATE TABLE " + MateEntry.TABLE_NAME_MATE_WATCH_MOVIE + " ("
                + MateEntry.MATE_WATCH_MOVIE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MateEntry.COLUMN_WATCH_MOVIE_NAME + " TEXT NOT NULL, "
                + MateEntry.COLUMN_WATCH_MOVIE_MATE_NAME + " TEXT NOT NULL);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_MATE_WATCH_MOVIE_TABLE);

    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }
}
