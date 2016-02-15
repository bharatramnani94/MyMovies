package com.bharatramnani.mymovies;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.bharatramnani.mymovies.data.MovieContract;

/**
 * Created by B on 05-12-2015.
 */
public class Movie implements Parcelable {
    Integer movie_id;
    String movie_title;
    String movie_poster;
    String movie_overview;
    Double movie_vote_average;
    String movie_release_date;

    public Movie(Integer id, String title, String posterImage, String overview, Double voteAvg, String releaseDate) {
        movie_id = id;
        movie_title = title;
        movie_poster = posterImage;
        movie_overview = overview;
        movie_vote_average = voteAvg;
        movie_release_date = releaseDate;
    }

    private Movie (Parcel parcel) {
        movie_id = parcel.readInt();
        movie_title = parcel.readString();
        movie_poster = parcel.readString();
        movie_overview = parcel.readString();
        movie_vote_average = parcel.readDouble();
        movie_release_date = parcel.readString();
    }

    public Movie(Cursor cursor) {
        int idx_movie_id = cursor.getColumnIndex(MovieContract.MoviesEntry._ID);
        int idx_movie_title = cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_TITLE);
        int idx_movie_poster = cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_POSTER);
        int idx_movie_overview = cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_OVERVIEW);
        int idx_movie_vote_average = cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_VOTE_AVG);
        int idx_movie_release_date = cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_RELEASE_DATE);

        this.movie_id = cursor.getInt(idx_movie_id);
        this.movie_title = cursor.getString(idx_movie_title);
        this.movie_poster = cursor.getString(idx_movie_poster);
        this.movie_overview = cursor.getString(idx_movie_overview);
        this.movie_vote_average = cursor.getDouble(idx_movie_vote_average);
        this.movie_release_date = cursor.getString(idx_movie_release_date);
    }

    @Override
    public String toString() {
        return movie_title + movie_release_date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(movie_id);
        parcel.writeString(movie_title);
        parcel.writeString(movie_poster);
        parcel.writeString(movie_overview);
        parcel.writeDouble(movie_vote_average);
        parcel.writeString(movie_release_date);
    }

    public static Parcelable.Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

}
