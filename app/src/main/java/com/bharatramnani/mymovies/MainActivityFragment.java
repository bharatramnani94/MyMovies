package com.bharatramnani.mymovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class MainActivityFragment extends Fragment {

    private final String KEY_SAVED_MOVIES_LIST = "saved_movies_list";
    public ArrayAdapter<Movie> mMoviesAdapter;
    private ArrayList<Movie> moviesList;
    ProgressBar progressBar;
    GridView gridView;
    Movie[] movies;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        updateMovies();
//    }

    private void updateMovies(int sort_criteria) {

        new FetchMoviesTask().execute(sort_criteria);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(KEY_SAVED_MOVIES_LIST, moviesList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_movies_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_sort_by_popularity) {
            updateMovies(R.string.action_sort_by_popularity);
            return true;
        }
        else if (id == R.id.action_sort_by_ratings) {
            updateMovies(R.string.action_sort_by_ratings);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        gridView = (GridView) rootView.findViewById(R.id.movies_gridview);
        progressBar = (ProgressBar) rootView.findViewById(R.id.loading_progress_bar);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = mMoviesAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("MovieDetails", movie);
                startActivity(intent);

            }
        });

        if(savedInstanceState == null || !savedInstanceState.containsKey(KEY_SAVED_MOVIES_LIST)) {
            moviesList = new ArrayList<Movie>();
            updateMovies(R.string.action_sort_by_popularity);
        }
        else {
            moviesList = savedInstanceState.getParcelableArrayList(KEY_SAVED_MOVIES_LIST);
        }

        mMoviesAdapter = new MoviesListAdapter(
                getActivity(),
                moviesList
        );

        gridView.setAdapter(mMoviesAdapter);

        return rootView;
    }

    public class FetchMoviesTask extends AsyncTask<Integer, Void, Movie[]> {

        public String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        @Override
        protected Movie[] doInBackground(Integer... params) {

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String moviesJsonStr = null;

            String movie_sort_type;

            int sort_criteria = params[0];

            Log.i(LOG_TAG, String.valueOf(R.string.action_sort_by_ratings));

            if (sort_criteria == R.string.action_sort_by_ratings)
                movie_sort_type = "top_rated";
            else
                // Sort by popularity
                movie_sort_type = "popular";



            final String API_KEY = "INSERT_THE_MOVIE_DB_API_KEY_HERE";

            try {

                final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie/";
                final String API_KEY_PARAM = "api_key";

                Uri builtUri = Uri.parse(MOVIES_BASE_URL + movie_sort_type)
                        .buildUpon()
                        .appendQueryParameter(API_KEY_PARAM, API_KEY)
                        .build();

                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                Log.v(LOG_TAG, "Connecting again.");

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                moviesJsonStr = buffer.toString();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(LOG_TAG, "Error obtaining data from API.", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }

            }

            try {
                return getMoviesFromJson(moviesJsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (progressBar == null)
                progressBar = (ProgressBar) getView().findViewById(R.id.loading_progress_bar);
            if (gridView == null)
                gridView = (GridView) getView().findViewById(R.id.movies_gridview);

            gridView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(Movie[] movies) {
            if (movies != null) {

//                Option 1
//                mMoviesAdapter.clear();
//                mMoviesAdapter.addAll(movies);


//              Option 2
                moviesList.clear();
                for (Movie m : movies)
                    moviesList.add(m);
            }

            progressBar.setVisibility(View.GONE);
            gridView.setVisibility(View.VISIBLE);
            mMoviesAdapter.notifyDataSetChanged();

        }



        private Movie[] getMoviesFromJson(String moviesJsonStr) throws JSONException{

            final String TMDB_MOVIES = "results";
            final String TMDB_TITLE = "original_title";
            final String TMDB_OVERVIEW = "overview";
            final String TMDB_VOTE_AVG = "vote_average";
            final String TMDB_POSTER = "poster_path";
            final String TMDB_RELEASE_DATE = "release_date";

            final String POSTER_SIZE = "w185";
            final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";

            JSONObject moviesJson = new JSONObject(moviesJsonStr);
            JSONArray moviesArray = moviesJson.getJSONArray(TMDB_MOVIES);

            int numberOfMovies = moviesArray.length();

            movies = new Movie[numberOfMovies];

            for (int i=0; i<numberOfMovies; i++) {

                JSONObject movieObject = moviesArray.getJSONObject(i);

                String title = movieObject.getString(TMDB_TITLE);
                String overview = movieObject.getString(TMDB_OVERVIEW);
                Double voteAvg = movieObject.getDouble(TMDB_VOTE_AVG);
                String releaseDate = movieObject.getString(TMDB_RELEASE_DATE);
                String posterPartialUrl = movieObject.getString(TMDB_POSTER);

                String posterUrl = IMAGE_BASE_URL + POSTER_SIZE + posterPartialUrl;
//                      posterUrl = "http://i.imgur.com/rFLNqWI.jpg";

                Movie movie = new Movie(title, posterUrl, overview, voteAvg, releaseDate);
                movies[i] = movie;
            }



            return movies;
        }
    }

}
