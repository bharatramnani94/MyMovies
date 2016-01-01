package com.bharatramnani.mymovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by B on 05-12-2015.
 */
public class Movie implements Parcelable {
    String movie_title;
    String movie_poster;
    String movie_overview;
    Double movie_vote_average;
    String movie_release_date;

    public Movie(String title, String posterImage, String overview, Double voteAvg, String releaseDate) {
        movie_title = title;
        movie_poster = posterImage;
        movie_overview = overview;
        movie_vote_average = voteAvg;
        movie_release_date = releaseDate;
    }

    private Movie (Parcel parcel) {
        movie_title = parcel.readString();
        movie_poster = parcel.readString();
        movie_overview = parcel.readString();
        movie_vote_average = parcel.readDouble();
        movie_release_date = parcel.readString();
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
