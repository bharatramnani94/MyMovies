package com.bharatramnani.mymovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.bharatramnani.mymovies.data.MovieContract.MoviesEntry;

/**
 * Created by B on 15-02-2016.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    static final String LOG_TAG = MovieDbHelper.class.getSimpleName();

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 4;

    public static final String DATABASE_NAME = "movies.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createMoviesTable(sqLiteDatabase);
//        createReviewsTable(sqLiteDatabase);
//        createTrailersTable(sqLiteDatabase);
    }

    private void createMoviesTable(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " + MoviesEntry.TABLE_NAME + " (" +
                MoviesEntry._ID + " INTEGER PRIMARY KEY," +
                MoviesEntry.COLUMN_TITLE+ " TEXT NOT NULL, " +
                MoviesEntry.COLUMN_POSTER + " TEXT NOT NULL, " +
                MoviesEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                MoviesEntry.COLUMN_VOTE_AVG + " REAL NOT NULL," +
                MoviesEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL" +
                ");";

        Log.v(LOG_TAG, "Executing SQL Query: " + SQL_CREATE_MOVIES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE);
    }

//    private void createReviewsTable(SQLiteDatabase sqLiteDatabase) {
//        final String SQL_CREATE_REVIEWS_TABLE = "CREATE TABLE " + ReviewEntry.TABLE_NAME + " (" +
//                ReviewEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
//                ReviewEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL," +
//                ReviewEntry.COLUMN_REVIEW_AUTHOR + " TEXT NOT NULL, " +
//                ReviewEntry.COLUMN_REVIEW_CONTENT + " TEXT NOT NULL, " +
//                " FOREIGN KEY (" + ReviewEntry._ID+ ") REFERENCES " +
//                MoviesEntry.TABLE_NAME + " (" + MoviesEntry._ID + ") " +
//                ");";
//
//        Log.v(LOG_TAG, "Executing SQL Query: " + SQL_CREATE_REVIEWS_TABLE);
//        sqLiteDatabase.execSQL(SQL_CREATE_REVIEWS_TABLE);
//    }
//
//    private void createTrailersTable(SQLiteDatabase sqLiteDatabase) {
//        final String SQL_CREATE_TRAILERS_TABLE = "CREATE TABLE " + TrailerEntry.TABLE_NAME + " (" +
//                TrailerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
//                TrailerEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL," +
//                TrailerEntry.COLUMN_TRAILER_NAME + " TEXT NOT NULL, " +
//                TrailerEntry.COLUMN_TRAILER_URL + " TEXT NOT NULL, " +
//                TrailerEntry.COLUMN_TRAILER_THUMBNAIL + " TEXT NOT NULL, " +
//                " FOREIGN KEY (" + TrailerEntry._ID+ ") REFERENCES " +
//                MoviesEntry.TABLE_NAME + " (" + MoviesEntry._ID + ") " +
//                ");";
//
//        Log.v(LOG_TAG, "Executing SQL Query: " + SQL_CREATE_TRAILERS_TABLE);
//        sqLiteDatabase.execSQL(SQL_CREATE_TRAILERS_TABLE);
//    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesEntry.TABLE_NAME);
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ReviewEntry.TABLE_NAME);
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TrailerEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}

