package com.coen268.moviemate.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.util.Log;

import com.coen268.moviemate.MovieInfoActivity;
import com.coen268.moviemate.data.MateContract.MateEntry;

import java.util.Random;

import static com.coen268.moviemate.data.MateContract.MateEntry.COLUMN_MATE_EMAIL;
import static com.coen268.moviemate.data.MateContract.MateEntry.COLUMN_MATE_NAME;
import static com.coen268.moviemate.data.MateContract.MateEntry.COLUMN_MOVIE_MATE_NAME;
import static com.coen268.moviemate.data.MateContract.MateEntry.COLUMN_MOVIE_NAME;
import static com.coen268.moviemate.data.MateContract.MateEntry.COLUMN_WATCH_MOVIE_MATE_NAME;
import static com.coen268.moviemate.data.MateContract.MateEntry.COLUMN_WATCH_MOVIE_NAME;
import static com.coen268.moviemate.data.MateContract.MateEntry.MATE_WATCH_MOVIE_ID;
import static com.coen268.moviemate.data.MateContract.MateEntry.TABLE_NAME_MATE;
import static com.coen268.moviemate.data.MateContract.MateEntry.TABLE_NAME_MATE_MOVIE;
import static com.coen268.moviemate.data.MateContract.MateEntry.TABLE_NAME_MATE_WATCH_MOVIE;

/**
 * {@link ContentProvider} for Movie Mate app.
 */
public class MateProvider extends ContentProvider {

    /** Tag for the log messages */
    public static final String LOG_TAG = MateProvider.class.getSimpleName();

    /** URI matcher code for the content URI for the mates table */
    private static final int MATES = 100;

    /** URI matcher code for the content URI for a single mate in the mates table */
    private static final int MATE_ID = 101;

    /** URI matcher code for the content URI for the user movie table */
    private static final int MATE_MOVIES = 200;

    /** URI matcher code for the content URI for a single movie in the user movie table */
    private static final int MATE_MOVIE_ID = 201;

    private static final int WATCH_MATE_MOVIE = 300;
    private static final int MATE_WATCH_MOVIE_ID = 301;


    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passedgit into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher;

    // Static initializer. This is run the first time anything is called from this class.
    static {

        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(MateContract.CONTENT_AUTHORITY, MateContract.PATH_MATES, MATES);
        sUriMatcher.addURI(MateContract.CONTENT_AUTHORITY, MateContract.PATH_MATES + "/#", MATE_ID);
        sUriMatcher.addURI(MateContract.CONTENT_AUTHORITY, MateContract.PATH_MATES2, MATE_MOVIES);
        sUriMatcher.addURI(MateContract.CONTENT_AUTHORITY, MateContract.PATH_MATES2 + "/#", MATE_MOVIE_ID);
        sUriMatcher.addURI(MateContract.CONTENT_AUTHORITY, MateContract.PATH_MATES3, WATCH_MATE_MOVIE);
        sUriMatcher.addURI(MateContract.CONTENT_AUTHORITY, MateContract.PATH_MATES3 + "/#", MATE_WATCH_MOVIE_ID);
    }



    /** Database helper object */
    private MateDbHelper mDbHelper;
    SQLiteDatabase database;

    @Override
    public boolean onCreate() {
        mDbHelper = new MateDbHelper(getContext());
        database = mDbHelper.getReadableDatabase();
        return true;
    }

    public void open(Context context) {
        mDbHelper = new MateDbHelper(context);
        database = mDbHelper.getReadableDatabase();
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Get readable database
        //SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        Log.i(LOG_TAG, uri.toString() + "URI" + "match" + match);
        switch (match) {
            case MATES:
                // For the MATES code, query the mate table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the mate table.
                cursor = database.query(TABLE_NAME_MATE, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case MATE_ID:
                // For the MATE_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.android.mate/mate/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = MateEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                // This will perform a query on the mate table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(TABLE_NAME_MATE, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;

            case MATE_MOVIES:
                cursor = database.query(TABLE_NAME_MATE_MOVIE, projection, selection, selectionArgs,
                        null, null, sortOrder);

            case WATCH_MATE_MOVIE:
                selection = MateEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                cursor = database.query(TABLE_NAME_MATE_WATCH_MOVIE, projection, selection, selectionArgs,
                        null, null, sortOrder);

            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MATES:
                return insertUser(uri, contentValues);
            case MATE_MOVIES:
                return insertUserMovie(uri, contentValues);
            case WATCH_MATE_MOVIE:
                return insertUserWatchMovie(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /**
     * Insert a user into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertUser(Uri uri, ContentValues values) {

        String name = values.getAsString(MateEntry.COLUMN_MATE_NAME);
        if (name == null) {
            throw new IllegalArgumentException("User requires a name");
        }

        String email  = values.getAsString(COLUMN_MATE_EMAIL);
        if (email == null) {
            throw new IllegalArgumentException("User requires email");
        }

        String password = values.getAsString(MateEntry.COLUMN_MATE_PASSWORD);
        if (password == null) {
            throw new IllegalArgumentException("User requires a password");
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Insert the new mate with the given values
        long id = database.insert(TABLE_NAME_MATE, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    /**
     * Insert a movie into the user movie database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertUserMovie(Uri uri, ContentValues values) {

        String name = values.getAsString(MateEntry.COLUMN_MOVIE_MATE_NAME);
        if (name == null) {
            throw new IllegalArgumentException("User requires a name");
        }

        String movieName = values.getAsString(MateEntry.COLUMN_MOVIE_NAME);
        if (movieName == null) {
            throw new IllegalArgumentException("User requires a movie name");
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Insert the new movie with the given values
        long id = database.insert(MateEntry.TABLE_NAME_MATE_MOVIE, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    private Uri insertUserWatchMovie(Uri uri, ContentValues values) {

        String name = values.getAsString(MateEntry.COLUMN_WATCH_MOVIE_MATE_NAME);
        if (name == null) {
            throw new IllegalArgumentException("User requires a name");
        }

        String movieName = values.getAsString(COLUMN_WATCH_MOVIE_NAME);
        if (movieName == null) {
            throw new IllegalArgumentException("User requires a movie name");
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Insert the new movie with the given values
        long id = database.insert(MateEntry.TABLE_NAME_MATE_WATCH_MOVIE, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MATES:
                return updateUser(uri, contentValues, selection, selectionArgs);
            case MATE_ID:
                // For the MATE_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = MateEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateUser(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    /**
     * Update mates in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more mates).
     * Return the number of rows that were successfully updated.
     */
    private int updateUser(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.containsKey(MateEntry.COLUMN_MATE_NAME)) {
            String name = values.getAsString(MateEntry.COLUMN_MATE_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Mate requires a name");
            }
        }

        if (values.containsKey(COLUMN_MATE_EMAIL)) {
            String email = values.getAsString(COLUMN_MATE_EMAIL);
            if (email == null) {
                throw new IllegalArgumentException("Mate requires a email address");
            }
        }

        if (values.containsKey(MateEntry.COLUMN_MATE_PASSWORD)) {
            // Check that the weight is greater than or equal to 0 kg
            String password = values.getAsString(MateEntry.COLUMN_MATE_PASSWORD);
            if (password == null) {
                throw new IllegalArgumentException("Mate requires valid password");
            }
        }

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Returns the number of database rows affected by the update statement
        return database.update(TABLE_NAME_MATE, values, selection, selectionArgs);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MATES:
                // Delete all rows that match the selection and selection args
                return database.delete(TABLE_NAME_MATE, selection, selectionArgs);
            case MATE_ID:
                // Delete a single row given by the ID in the URI
                selection = MateEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return database.delete(TABLE_NAME_MATE, selection, selectionArgs);
            case MATE_MOVIES:
                return database.delete(TABLE_NAME_MATE_MOVIE, selection, selectionArgs);
            case WATCH_MATE_MOVIE:
                return database.delete(TABLE_NAME_MATE_WATCH_MOVIE, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MATES:
                return MateEntry.CONTENT_LIST_TYPE;
            case MATE_ID:
                return MateEntry.CONTENT_ITEM_TYPE;
            case MATE_MOVIES:
                return MateEntry.CONTENT_LIST_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    public Cursor getUserMovies(String name) {

        //database = mDbHelper.getWritableDatabase();
        String[] columns = { MateEntry.MATE_MOVIE_ID, MateEntry.COLUMN_MOVIE_NAME };
        String[] args = { name };

        Cursor cursor = database.query(TABLE_NAME_MATE_MOVIE, columns, COLUMN_MOVIE_MATE_NAME + "=?", args,
                null, null, null);

        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor getUserWatchMovies(String name) {

        //database = mDbHelper.getWritableDatabase();
        String[] columns = { MateEntry.MATE_WATCH_MOVIE_ID, COLUMN_WATCH_MOVIE_NAME };
        String[] args = { name };

        Cursor cursor = database.query(TABLE_NAME_MATE_WATCH_MOVIE, columns, COLUMN_WATCH_MOVIE_MATE_NAME + "=?", args,
                null, null, null);

        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public String getUserMovieName(String name) {
        String movie = null;
        String[] columns = { MateEntry.MATE_MOVIE_ID, MateEntry.COLUMN_MOVIE_NAME };
        String[] args = { name };

        Cursor cursor = database.query(TABLE_NAME_MATE_MOVIE, columns, COLUMN_MOVIE_MATE_NAME + "=?", args,
                null, null, null);

        if(cursor.getCount() > 0) {
            cursor.moveToLast();
            movie = cursor.getString(1);
        }
        return movie;
    }

    public boolean dubUpCheck(String name, String movie) {
        String[] args = {name, movie};
        Cursor cur = database.query(TABLE_NAME_MATE_MOVIE, null, COLUMN_MOVIE_MATE_NAME+ "=? AND " + COLUMN_MOVIE_NAME+ "=?",
                args, null, null, null, null);
        if (cur != null && cur.getCount()>0) {
            return true;
        }
        return false;
    }

    public boolean dubUpWatchCheck(String name, String movie) {
        String[] args = {name, movie};
        Cursor cur = database.query(TABLE_NAME_MATE_WATCH_MOVIE, null, COLUMN_WATCH_MOVIE_MATE_NAME+ "=? AND " + COLUMN_WATCH_MOVIE_NAME+ "=?",
                args, null, null, null, null);
        if (cur != null && cur.getCount()>0) {
            return true;
        }
        return false;
    }

    public boolean dubUserCheck(String name) {
        String[] args = {name};
        Cursor cur = database.query(TABLE_NAME_MATE, null, COLUMN_MATE_NAME+ "=?",
                args, null, null, null, null);
        if (cur != null && cur.getCount()>0) {
            return true;
        }
        return false;
    }

    public boolean dubEmailCheck(String name) {
        String[] args = {name};
        Cursor cur = database.query(TABLE_NAME_MATE, null, COLUMN_MATE_EMAIL+ "=?",
                args, null, null, null, null);
        if (cur != null && cur.getCount()>0) {
            return true;
        }
        return false;
    }

}

