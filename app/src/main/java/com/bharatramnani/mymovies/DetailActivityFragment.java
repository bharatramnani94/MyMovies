package com.bharatramnani.mymovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    Movie movie;

    public DetailActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movie = (Movie) getActivity().getIntent().getParcelableExtra("MovieDetails");

        getActivity().setTitle(movie.movie_title);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView textview_ratings = (TextView)view.findViewById(R.id.rating_value_text);
        textview_ratings.setText(movie.movie_vote_average.toString());

        TextView textview_releaseDate = (TextView)view.findViewById(R.id.releasedate_value_text);
        textview_releaseDate.setText(movie.movie_release_date);

        TextView textview_description = (TextView)view.findViewById(R.id.description_text);
        textview_description.setText(movie.movie_overview);

        ImageView imageView_poster = (ImageView)view.findViewById(R.id.poster_imageview);
        Picasso.with(getContext()).load(movie.movie_poster).into(imageView_poster);

    }
}
