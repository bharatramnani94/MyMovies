package com.bharatramnani.mymovies;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
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
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bharatramnani.mymovies.data.MovieContract;

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


public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private String LOG_TAG = MainActivityFragment.class.getSimpleName();

    private static final String KEY_SAVED_MOVIES_LIST = "saved_movies_list";
    private static final String KEY_SHARED_PREFERENCES = "shared_preferences";
    private static final String KEY_PREFERENCE_SORT_ORDER = "preference_sort_order";
    private static final String SORT_TYPE_POPULAR = "popularity.desc";
    private static final String SORT_TYPE_RATINGS = "vote_average.desc";
    private static final String SORT_TYPE_FAVOURITES = "sort_by_favourites";

    private static final int MOVIES_LOADER = 1;

    public FavouriteMoviesAdapter mfavouriteMoviesAdapter;
    public ArrayAdapter<Movie> mMoviesAdapter;
    private ArrayList<Movie> moviesList;
    SharedPreferences sharedPreferences;
    LinearLayout cannot_connect_layout;
    ProgressBar progressBar;
    GridView gridView;
    Movie[] movies;
    String sort_preference;



    public MainActivityFragment() {
    }

    public void refreshList() {
        if (mfavouriteMoviesAdapter != null) {
            Cursor newCursor = getActivity().getContentResolver().query(MovieContract.MoviesEntry.CONTENT_URI,null,null,null,null);
            mfavouriteMoviesAdapter.changeCursor(newCursor);
            Log.d(LOG_TAG, "Updated movies list");
        }
    }


    // Must be implemented by the activities containing this fragment
    public interface Callback {
        void onItemSelected(Movie movie);
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        //            Retrieve the sort order saved
        sharedPreferences = getActivity().getSharedPreferences(KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
//            If no sort order preference found, then default to sort by popularity
        sort_preference = sharedPreferences.getString(KEY_PREFERENCE_SORT_ORDER, SORT_TYPE_POPULAR);

        if(savedInstanceState == null || !savedInstanceState.containsKey(KEY_SAVED_MOVIES_LIST)) {
            moviesList = new ArrayList<Movie>();

            if (sort_preference.equals(SORT_TYPE_POPULAR))       // Sort by popularity
                updateMovies(R.string.action_sort_by_popularity);
            else if (sort_preference.equals(SORT_TYPE_RATINGS))                                                 // Sort by ratings
                updateMovies(R.string.action_sort_by_ratings);
            else if (sort_preference.equals(SORT_TYPE_FAVOURITES))
                updateMovies(R.string.action_view_favourites);
        }
        else {
            moviesList = savedInstanceState.getParcelableArrayList(KEY_SAVED_MOVIES_LIST);
        }

    }



    private void updateMovies(int sort_criteria) {

        if (sort_criteria == R.string.action_view_favourites)
            new FetchFavoriteMoviesTask().execute();
        else
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

        // Save sort criteria
        SharedPreferences.Editor preferenceEditor;
        preferenceEditor = sharedPreferences.edit();

        switch (id) {

            case R.id.action_sort_by_popularity:
                updateMovies(R.string.action_sort_by_popularity);
                preferenceEditor.putString(KEY_PREFERENCE_SORT_ORDER, SORT_TYPE_POPULAR);
                preferenceEditor.commit();
                Log.d(LOG_TAG, "Popularity menu button clicked.");
                return true;

            case R.id.action_sort_by_ratings:
                updateMovies(R.string.action_sort_by_ratings);
                preferenceEditor.putString(KEY_PREFERENCE_SORT_ORDER, SORT_TYPE_RATINGS);
                preferenceEditor.commit();
                Log.d(LOG_TAG, "Ratings menu button clicked.");
                return true;

            case R.id.action_view_favourites:
                updateMovies(R.string.action_view_favourites);
                preferenceEditor.putString(KEY_PREFERENCE_SORT_ORDER, SORT_TYPE_FAVOURITES);
                preferenceEditor.commit();
                Log.d(LOG_TAG, "Favourites menu button clicked.");
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
        cannot_connect_layout = (LinearLayout) rootView.findViewById(R.id.container_cannot_connect);

        if (sort_preference.equals(R.string.action_view_favourites)) {
            // TODO
            // QUERYING FAVOURITES FROM DATABASE
//            mfavouriteMoviesAdapter = new FavouriteMoviesAdapter(getContext(), null, 0);

//            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView adapterView, View view, int position, long id) {
//                    Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
//                    if (cursor != null) {
//
//                        int idx_movie_id = cursor.getColumnIndex(MovieContract.MoviesEntry._ID);
//                        int movie_id = cursor.getInt(idx_movie_id);
//                        Intent intent = new Intent(getActivity(), DetailActivity.class)
//                                .setData(MovieContract.MoviesEntry.buildMovieUri(movie_id));
//                        startActivity(intent);
//                    }
//                }
//            });

//            Cursor moviesCursor = getActivity().getContentResolver().query(MovieContract.MoviesEntry.CONTENT_URI, null, null, null, null);
            updateMovies(R.string.action_view_favourites);

        }
        else {
            // QUERYING FROM NETWORK
//            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    Movie movie = mMoviesAdapter.getItem(position);
//                    Intent intent = new Intent(getActivity(), DetailActivity.class);
//                    intent.putExtra(DetailActivityFragment.DETAIL_MOVIE, movie);
//                    startActivity(intent);
//                }
//            });
//
//            mMoviesAdapter = new MoviesListAdapter(
//                    getActivity(),
//                    moviesList
//            );
//            gridView.setAdapter(mMoviesAdapter);
        }







        //        Setting onclick to refresh button
        Button refresh_button = (Button)cannot_connect_layout.findViewById(R.id.button_cannot_connect);
        refresh_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cannot_connect_layout.setVisibility(View.GONE);
                if (sort_preference.equals(SORT_TYPE_POPULAR))       // Sort by popularity
                    updateMovies(R.string.action_sort_by_popularity);
                else if (sort_preference.equals(SORT_TYPE_RATINGS))                                                 // Sort by ratings
                    updateMovies(R.string.action_sort_by_ratings);
                else if (sort_preference.equals(SORT_TYPE_FAVOURITES))
                    updateMovies(R.string.action_view_favourites);
            }
        });

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == MOVIES_LOADER) {
            Uri moviesUri = MovieContract.MoviesEntry.CONTENT_URI;
//            Cursor moviesCursor = getActivity().getContentResolver().query(moviesUri, null, null, null, null);
            return new CursorLoader(getContext(), moviesUri, null, null, null, null);
        }
        else
            return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (loader.getId() == MOVIES_LOADER)
            mfavouriteMoviesAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (loader.getId() == MOVIES_LOADER)
            mfavouriteMoviesAdapter.swapCursor(null);
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

            if (sort_criteria == R.string.action_sort_by_ratings)
                // Sort by ratings
                movie_sort_type = SORT_TYPE_RATINGS;
            else
                // Sort by popularity
                movie_sort_type = SORT_TYPE_POPULAR;



            final String API_KEY = BuildConfig.THE_MOVIE_DB_API_KEY;

            try {

                final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/discover/movie/";
                final String API_KEY_PARAM = "api_key";
                final String SORT_BY_PARAM = "sort_by";
                final String VOTE_COUNT_PARAM = "vote_count.gte";
                final String VOTE_COUNT_VALUE = "50";

                Uri builtUri = Uri.parse(MOVIES_BASE_URL)
                        .buildUpon()
                        .appendQueryParameter(API_KEY_PARAM, API_KEY)
                        .appendQueryParameter(SORT_BY_PARAM, movie_sort_type)
                        .appendQueryParameter(VOTE_COUNT_PARAM, VOTE_COUNT_VALUE)
                        .build();

                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                Log.d(LOG_TAG, "Fetching data from API.");

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
//                    displayCannotConnectError();
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

//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            if (progressBar == null)
//                progressBar = (ProgressBar) getView().findViewById(R.id.loading_progress_bar);
//            if (gridView == null)
//                gridView = (GridView) getView().findViewById(R.id.movies_gridview);
//
//            gridView.setVisibility(View.GONE);
//            progressBar.setVisibility(View.VISIBLE);
//
//        }

        @Override
        protected void onPostExecute(Movie[] movies) {



            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Movie movie = mMoviesAdapter.getItem(position);
//                    Intent intent = new Intent(getActivity(), DetailActivity.class);
//                    intent.putExtra(DetailActivityFragment.DETAIL_MOVIE, movie);
//                    startActivity(intent);
                    ((Callback) getActivity()).onItemSelected(movie);
                }
            });

            mMoviesAdapter = new MoviesListAdapter(
                    getActivity(),
                    moviesList
            );
            gridView.setAdapter(mMoviesAdapter);









            if (movies != null) {

//                Option 1
//                mMoviesAdapter.clear();
//                mMoviesAdapter.addAll(movies);


//              Option 2
                moviesList.clear();
                for (Movie m : movies)
                    moviesList.add(m);

//                gridView.setVisibility(View.VISIBLE);
                mMoviesAdapter.notifyDataSetChanged();

            }
            else {
//                Unable to fetch data
                Toast.makeText(getContext(), "Unable to fetch data, Try again later.", Toast.LENGTH_SHORT).show();
//                cannot_connect_layout.setVisibility(View.VISIBLE);
            }

//            progressBar.setVisibility(View.GONE);

        }


        private Movie[] getMoviesFromJson(String moviesJsonStr) throws JSONException{

            final String TMDB_ID = "id";
            final String TMDB_MOVIES = "results";
            final String TMDB_TITLE = "original_title";
            final String TMDB_OVERVIEW = "overview";
            final String TMDB_VOTE_AVG = "vote_average";
            final String TMDB_POSTER = "poster_path";
            final String TMDB_RELEASE_DATE = "release_date";

            final String POSTER_SIZE = "w185";
            final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
            final String TRAILER_BASE_URL = "https://www.youtube.com/watch?v=";

            JSONObject moviesJson = new JSONObject(moviesJsonStr);
            JSONArray moviesArray = moviesJson.getJSONArray(TMDB_MOVIES);

            int numberOfMovies = moviesArray.length();

            movies = new Movie[numberOfMovies];

            for (int i=0; i<numberOfMovies; i++) {

                JSONObject movieObject = moviesArray.getJSONObject(i);

                Integer id = movieObject.getInt(TMDB_ID);
                String title = movieObject.getString(TMDB_TITLE);
                String overview = movieObject.getString(TMDB_OVERVIEW);
                Double voteAvg = movieObject.getDouble(TMDB_VOTE_AVG);
                String releaseDate = movieObject.getString(TMDB_RELEASE_DATE);
                String posterPartialUrl = movieObject.getString(TMDB_POSTER);


                String posterUrl = IMAGE_BASE_URL + POSTER_SIZE + posterPartialUrl;
//                      posterUrl = "http://i.imgur.com/rFLNqWI.jpg";

                Movie movie = new Movie(id, title, posterUrl, overview, voteAvg, releaseDate);
                movies[i] = movie;
            }

            return movies;
        }
    }



//    public class FetchFavoriteMoviesTask extends AsyncTask<Void, Void, List<Movie>> {
//
//        private Context mContext;
//
//        public FetchFavoriteMoviesTask(Context context) {
//            mContext = context;
//        }
//
//        private List<Movie> getFavoriteMoviesDataFromCursor(Cursor cursor) {
//            List<Movie> results = new ArrayList<>();
//            if (cursor != null && cursor.moveToFirst()) {
//                do {
//                    Movie movie = new Movie(cursor);
//                    results.add(movie);
//                } while (cursor.moveToNext());
//                cursor.close();
//            }
//            return results;
//        }
//
//        @Override
//        protected List<Movie> doInBackground(Void... params) {
//            Cursor cursor = mContext.getContentResolver().query(
//                    MovieContract.MoviesEntry.CONTENT_URI,
//                    null,
//                    null,
//                    null,
//                    null
//            );
//            return getFavoriteMoviesDataFromCursor(cursor);
//        }
//
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
////            if (progressBar == null)
////                progressBar = (ProgressBar) getView().findViewById(R.id.loading_progress_bar);
////            if (gridView == null)
////                gridView = (GridView) getView().findViewById(R.id.movies_gridview);
////
////            gridView.setVisibility(View.GONE);
////            progressBar.setVisibility(View.VISIBLE);
//
//        }
//
//
//        @Override
//        protected void onPostExecute(List<Movie> movies) {
//            if (movies != null) {
//                if (mfavouriteMoviesAdapter != null) {
//                    moviesList.clear();
//                    for (Movie m : movies)
//                        moviesList.add(m);
//
////                    gridView.setVisibility(View.VISIBLE);
//                    mfavouriteMoviesAdapter.notifyDataSetChanged();
//                }
//                else {
//                    moviesList = new ArrayList<>();
//                    moviesList.addAll(movies);
////                    cannot_connect_layout.setVisibility(View.VISIBLE);
//                }
//
////                progressBar.setVisibility(View.GONE);
//            }
//        }


//    public class FetchFavoriteMoviesTask extends AsyncTask<Void, Void, Movie[]> {
//
//        public String LOG_TAG = FetchFavoriteMoviesTask.class.getSimpleName();
//
//        private Movie[] getFavoriteMoviesFromCursor(Cursor cursor) {
//            if (cursor != null && cursor.moveToFirst()) {
//                int no_of_favourites = cursor.getCount();
//                movies = new Movie[no_of_favourites];
//                do {
//                    Movie movie = new Movie(cursor);
//                    moviesList.add(movie);
//                } while (cursor.moveToNext());
//                cursor.close();
//            }
//            movies = (Movie[]) moviesList.toArray();
//            return movies;
//        }
//
//        @Override
//        protected Movie[] doInBackground(Void... params) {
//            Cursor cursor = getContext().getContentResolver().query(
//                    MovieContract.MoviesEntry.CONTENT_URI,
//                    null,
//                    null,
//                    null,
//                    null
//            );
//            return getFavoriteMoviesFromCursor(cursor);
//        }
//
//        @Override
//        protected void onPostExecute(Movie[] movies) {
//            if (movies != null) {
//                if (mfavouriteMoviesAdapter != null) {
//                    View lv = (View)mfavouriteMoviesAdapter.newView()
//                    Log.d(LOG_TAG, "Added all movies from favourites");
//                }
//            }
//        }
//    }


    public class FetchFavoriteMoviesTask extends AsyncTask<Void, Void, Cursor> {

        public String LOG_TAG = FetchFavoriteMoviesTask.class.getSimpleName();

        @Override
        protected Cursor doInBackground(Void... params) {
            Cursor cursor = getContext().getContentResolver().query(
                    MovieContract.MoviesEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    null
            );

//            Cursor cursor = new MovieDbHelper(getContext()).getReadableDatabase().rawQuery("SELECT * FROM " + MovieContract.MoviesEntry.TABLE_NAME, null);
            Log.d(LOG_TAG, "Fetched " + cursor.getCount() + "favourites in FetchFavouriteMoviesTask");
            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView adapterView, View view, int position, long id) {
                    Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                    if (cursor != null) {

                        Movie movie = new Movie(cursor);
//                        Intent intent = new Intent(getActivity(), DetailActivity.class);
//                        intent.putExtra(DetailActivityFragment.DETAIL_MOVIE, movie);
//                        startActivity(intent);
                        ((Callback) getActivity()).onItemSelected(movie);
                    }
                }
            });

            Log.d(LOG_TAG, "Count of cursor favourites: " + cursor.getCount());

            mfavouriteMoviesAdapter = new FavouriteMoviesAdapter(getContext(), cursor, 0);
            gridView.setAdapter(mfavouriteMoviesAdapter);
            mfavouriteMoviesAdapter.notifyDataSetChanged();
            Log.d(LOG_TAG, "Added all movies from favourites");

        }
    }

}