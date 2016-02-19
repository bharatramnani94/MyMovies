package com.bharatramnani.mymovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.bharatramnani.mymovies.data.MovieContract.MoviesEntry;

/**
 * Created by B on 15-02-2016.
 */
public class MovieProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper mOpenHelper;

    private static final SQLiteQueryBuilder sMoviesQueryBuilder;

    static {
        sMoviesQueryBuilder = new SQLiteQueryBuilder();
        sMoviesQueryBuilder.setTables(MoviesEntry.TABLE_NAME);
    }

    static final int MOVIES = 100;
    static final int MOVIE_ID = 101;
//    static final int REVIEWS = 200;
//    static final int REVIEWS_FOR_MOVIE = 201;
//    static final int TRAILERS = 300;
//    static final int TRAILERS_FOR_MOVIE = 301;

    private static final String sMoviesIdSelectionString = MoviesEntry.TABLE_NAME + "." + MoviesEntry._ID + "=?";


    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // "reviews/#"
//            case REVIEWS_FOR_MOVIE:
//            {
//                retCursor = getReviewsForMovie(uri, projection, sortOrder);
//                break;
//            }
            // "trailers/#"
//            case TRAILERS_FOR_MOVIE: {
//                retCursor = getTrailersForMovie(uri, projection, sortOrder);
//                break;
//            }
            // "movie"
            case MOVIE_ID: {
                retCursor = getMovieForId(uri, projection, selection, selectionArgs, sortOrder);
                break;
            }
            // "movies"
            case MOVIES: {
                retCursor = getMovies(uri, projection, selection, selectionArgs, sortOrder);
                break;
            }
            // "reviews"
//            case REVIEWS: {
//                retCursor = getReviews(uri, projection, sortOrder);
//                break;
//            }
            // "trailers"
//            case TRAILERS: {
//                retCursor = getTrailers(uri, projection, sortOrder);
//                break;
//            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
//        if (retCursor != null)

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

//    private Cursor getReviewsForMovie(Uri uri, String[] projection, String sortOrder) {
//        int movie_id = MoviesContract.getMovieIdFromUri(uri);
//
//        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
//
//        final String selection = MoviesContract.ReviewEntry.COLUMN_MOVIE_ID + " = ?";
//        final String[] selectionArgs = new String[] {Integer.toString(movie_id)};
//
//        Cursor cursor = db.query(MoviesContract.ReviewEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder );
//        return cursor;
//    }
//
//    private Cursor getTrailersForMovie(Uri uri, String[] projection, String sortOrder) {
//        int movie_id = MoviesContract.getMovieIdFromUri(uri);
//
//        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
//
//        final String selection = MoviesContract.TrailerEntry.COLUMN_MOVIE_ID + " = ?";
//        final String[] selectionArgs = new String[] {Integer.toString(movie_id)};
//
//        Cursor cursor = db.query(MoviesContract.TrailerEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder );
//        return cursor;
//    }

    private Cursor getMovies(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(MovieContract.MoviesEntry.TABLE_NAME,projection, selection, selectionArgs, null, null, sortOrder);
        return cursor;
    }

    private Cursor getMovieForId(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        String movie_id = MovieContract.getMovieIdFromUri(uri);

        SQLiteDatabase db = mOpenHelper.getReadableDatabase();

//        final String selection = sMoviesIdSelectionString;
//        final String[] selectionArgs = new String[] {movie_id};

        Cursor cursor = db.query(MovieContract.MoviesEntry.TABLE_NAME,projection, selection, selectionArgs, null, null, sortOrder);
        return cursor;
    }

//    private Cursor getReviews(Uri uri, String[] projection, String sortOrder) {
//
//        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
//        Cursor cursor = db.query(MoviesContract.ReviewEntry.TABLE_NAME, projection, null, null, null, null, sortOrder );
//        return cursor;
//    }
//
//    private Cursor getTrailers(Uri uri, String[] projection, String sortOrder) {
//
//        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
//        Cursor cursor = db.query(MoviesContract.TrailerEntry.TABLE_NAME, projection, null, null, null, null, sortOrder );
//        return cursor;
//    }



    @Nullable
    @Override
    public String getType(Uri uri) {

        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
//            case REVIEWS_FOR_MOVIE:
//                return MoviesContract.ReviewEntry.CONTENT_TYPE;
//            case TRAILERS_FOR_MOVIE:
//                return MoviesContract.TrailerEntry.CONTENT_TYPE;
            case MOVIE_ID:
                return MovieContract.MoviesEntry.CONTENT_ITEM_TYPE;
            case MOVIES:
                return MovieContract.MoviesEntry.CONTENT_TYPE;
//            case REVIEWS:
//                return MoviesContract.ReviewEntry.CONTENT_TYPE;
//            case TRAILERS:
//                return MoviesContract.TrailerEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
//            case TRAILERS: {
//                long _id = db.insert(MoviesContract.TrailerEntry.TABLE_NAME, null, values);
//                if ( _id > 0 )
//                    returnUri = MoviesContract.TrailerEntry.buildTrailersUri((int) _id);
//                else
//                    throw new android.database.SQLException("Failed to insert row into " + uri);
//                break;
//            }
//            case REVIEWS: {
//                long _id = db.insert(MoviesContract.ReviewEntry.TABLE_NAME, null, values);
//                if ( _id > 0 )
//                    returnUri = MoviesContract.ReviewEntry.buildReviewsUri((int) _id);
//                else
//                    throw new android.database.SQLException("Failed to insert row into " + uri);
//                break;
//            }
            case MOVIES: {
                long _id = db.insert(MovieContract.MoviesEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MovieContract.MoviesEntry.buildMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        String mov_id = MovieContract.getMovieIdFromUri(uri);
        int returnRowCount;

        if (null == selection)  selection = "1";

        switch (match) {
//            case TRAILERS: {
//                returnRowCount = db.delete(MoviesContract.TrailerEntry.TABLE_NAME, selection, selectionArgs);
//                break;
//            }
//            case REVIEWS: {
//                returnRowCount = db.delete(MoviesContract.ReviewEntry.TABLE_NAME, selection, selectionArgs);
//                break;
//            }
             case MOVIE_ID: {
                 returnRowCount = db.delete(MoviesEntry.TABLE_NAME, MoviesEntry.TABLE_NAME + "." + MoviesEntry._ID + "=?", new String[] {mov_id});
                 break;
             }
            case MOVIES: {
                returnRowCount = db.delete(MovieContract.MoviesEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (returnRowCount != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return returnRowCount;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int returnRowCount;

        switch (match) {
//            case TRAILERS: {
//                returnRowCount = db.update(MoviesContract.TrailerEntry.TABLE_NAME, values, selection, selectionArgs);
//                break;
//            }
//            case REVIEWS: {
//                returnRowCount = db.update(MoviesContract.ReviewEntry.TABLE_NAME, values, selection, selectionArgs);
//                break;
//            }
            case MOVIES: {
                returnRowCount = db.update(MovieContract.MoviesEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (returnRowCount != 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return returnRowCount;
    }

    //    Bulk Insert
    @Override
    public int bulkInsert(Uri uri, ContentValues[] contentValues) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                return bulkInsertX(db, uri, contentValues, MovieContract.MoviesEntry.TABLE_NAME);
//            case TRAILERS:
//                return bulkInsertX(db, uri, contentValues, MoviesContract.TrailerEntry.TABLE_NAME);
//            case REVIEWS:
//                return bulkInsertX(db, uri, contentValues, MoviesContract.ReviewEntry.TABLE_NAME);
            default:
                return super.bulkInsert(uri, contentValues);
        }
    }

    private int bulkInsertX(SQLiteDatabase db, Uri uri, ContentValues[] contentValues, String tableName) {
        db.beginTransaction();
        int returnCount = 0;
        try {
            for (ContentValues value : contentValues) {
                long _id = db.insert(tableName, null, value);
                if (_id != -1) {
                    returnCount++;
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnCount;
    }

    public static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.PATH_MOVIES, MOVIES);
//        matcher.addURI(authority, MovieContract.PATH_REVIEWS, REVIEWS);
//        matcher.addURI(authority, MovieContract.PATH_TRAILERS, TRAILERS);

//        matcher.addURI(authority, MovieContract.PATH_TRAILERS + "/#", TRAILERS_FOR_MOVIE);
//        matcher.addURI(authority, MovieContract.PATH_REVIEWS + "/#", REVIEWS_FOR_MOVIE);
        matcher.addURI(authority, MovieContract.PATH_MOVIES + "/#", MOVIE_ID);

        return matcher;
    }
}
