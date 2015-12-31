package com.bharatramnani.mymovies;

/**
 * Created by B on 05-12-2015.
 */
public class Movie {
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

    @Override
    public String toString() {
        return movie_title + movie_release_date;
    }
}
