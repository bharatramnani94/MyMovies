package com.bharatramnani.mymovies.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by B on 25-01-2016.
 */
public class MovieContract {


    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website
    public static final String CONTENT_AUTHORITY = "com.bharatramnani.mymovies";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    public static final String PATH_MOVIES = "movies";
//    public static final String PATH_REVIEWS = "reviews";
//    public static final String PATH_TRAILERS = "trailers";


    /* Inner class that defines the table contents of the movies table */
    public static final class MoviesEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;


//        public static Uri buildMovieUri(int id) {
//            return ContentUris.withAppendedId(CONTENT_URI, id);
//        }

        public static Uri buildMovieUri(long movie_id) {
            return CONTENT_URI.buildUpon().appendPath(String.valueOf(movie_id)).build();
        }



        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_VOTE_AVG = "vote_average";
        public static final String COLUMN_RELEASE_DATE = "release_date";

    }



    /* Inner class that defines the table contents of the reviews table */
//    public static final class ReviewEntry implements BaseColumns {
//
//
//        public static final Uri CONTENT_URI =
//                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEWS).build();
//
//        public static final String CONTENT_TYPE =
//                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEWS;
//        public static final String CONTENT_ITEM_TYPE =
//                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEWS;
//
//        public static Uri buildReviewsUri(int id) {
//            return ContentUris.withAppendedId(CONTENT_URI, id);
//        }
//
//        public static Uri buildReviewsForMovie(int movie_id) {
//            return CONTENT_URI.buildUpon().appendPath(String.valueOf(movie_id)).build();
//        }
//
//
//        public static final String TABLE_NAME = "reviews";
//        public static final String COLUMN_MOVIE_ID = "movie_id";
//        public static final String COLUMN_REVIEW_AUTHOR = "author";
//        public static final String COLUMN_REVIEW_CONTENT = "content";
//
//    }



    /* Inner class that defines the table contents of the trailers table */
//    public static final class TrailerEntry implements BaseColumns {
//
//
//        public static final Uri CONTENT_URI =
//                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRAILERS).build();
//
//        public static final String CONTENT_TYPE =
//                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILERS;
//        public static final String CONTENT_ITEM_TYPE =
//                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILERS;
//
//        public static Uri buildTrailersUri(int id) {
//            return ContentUris.withAppendedId(CONTENT_URI, id);
//        }
//
//        public static Uri buildTrailersForMovie(int movie_id) {
//            return CONTENT_URI.buildUpon().appendPath(String.valueOf(movie_id)).build();
//        }
//
//
//        public static final String TABLE_NAME = "trailers";
//        public static final String COLUMN_MOVIE_ID = "movie_id";
//        public static final String COLUMN_TRAILER_NAME = "name";
//        public static final String COLUMN_TRAILER_URL = "url";
//        public static final String COLUMN_TRAILER_THUMBNAIL = "thumbnail";
//
//    }

    public static String getMovieIdFromUri (Uri uri) {
        return uri.getPathSegments().get(1);
    }

}
