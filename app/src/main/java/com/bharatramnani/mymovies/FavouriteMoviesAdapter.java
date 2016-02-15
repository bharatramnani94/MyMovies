package com.bharatramnani.mymovies;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.bharatramnani.mymovies.data.MovieContract;
import com.squareup.picasso.Picasso;

/**
 * Created by B on 12-02-2016.
 */
public class FavouriteMoviesAdapter extends CursorAdapter {

    public FavouriteMoviesAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_item_movie_layout, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView imageView = (ImageView) view;
        Picasso.with(context).load(posterFromMovieCursorRow(cursor)).into(imageView);
    }

    private String posterFromMovieCursorRow(Cursor cursor) {
        int idx_movie_poster_url = cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_POSTER);
        String movie_poster_url = cursor.getString(idx_movie_poster_url);
        return movie_poster_url;
    }
}
