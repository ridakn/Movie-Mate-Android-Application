package com.coen268.moviemate.data;

import android.net.Uri;
import android.content.ContentResolver;
import android.provider.BaseColumns;

/**
 * API Contract for the MovieMate app.
 */
public final class MateContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private MateContract() {}

    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY = "com.coen268.moviemate";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Possible path (appended to base content URI for possible URI's)
     * For instance, content://com.example.android.mates/mates/ is a valid path for
     * looking at mate data. content://com.example.android.mates/staff/ will fail,
     * as the ContentProvider hasn't been given any information on what to do with "staff".
     */
    public static final String PATH_MATES = "mates";
    public static final String PATH_MATES2 = "mate_movie";
    public static final String PATH_MATES3 = "mate_watch_movie";


    /**
     * Inner class that defines constant values for the mates database table.
     * Each entry in the table represents a single mate.
     */
    public static final class MateEntry implements BaseColumns {

        /** The content URI to access the mate data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MATES);
        public static final Uri CONTENT_URI2 = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MATES2);
        public static final Uri CONTENT_URI3 = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MATES3);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of user.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MATES;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single user.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MATES;

        /** Name of database table for movie */
        public final static String TABLE_NAME_MATE = "mates";
        public final static String TABLE_NAME_MATE_MOVIE = "mate_movie";
        public final static String TABLE_NAME_MATE_WATCH_MOVIE = "watch_mate_movie";


        /**
         * Colunm for mate table
         */
        public final static String MATE_ID = BaseColumns._ID;
        public final static String COLUMN_MATE_NAME ="name";
        public final static String COLUMN_MATE_EMAIL = "email";
        public final static String COLUMN_MATE_PASSWORD = "password";

        /**
         * Colunm for mate_movie table
         */
        public final static String MATE_MOVIE_ID = BaseColumns._ID;
        public final static String COLUMN_MOVIE_NAME ="movie_name";
        public final static String COLUMN_MOVIE_MATE_NAME = "mate_name";


        public final static String MATE_WATCH_MOVIE_ID = BaseColumns._ID;
        public final static String COLUMN_WATCH_MOVIE_NAME ="movie_name";
        public final static String COLUMN_WATCH_MOVIE_MATE_NAME = "mate_name";

    }

}
