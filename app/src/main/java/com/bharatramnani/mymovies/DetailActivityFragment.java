package com.bharatramnani.mymovies;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bharatramnani.mymovies.data.MovieContract;
import com.squareup.picasso.Picasso;

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

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends android.support.v4.app.Fragment {

    private static final String LOG_TAG = DetailActivityFragment.class.getSimpleName();

    private static final String KEY_SAVED_TRAILERS_LIST = "saved_trailers_list";
    private static final String KEY_SAVED_REVIEWS_LIST = "saved_reviews_list";

    private static final int REVIEWS_LOADER = 0;
    private static final int TRAILERS_LOADER = 1;
    private static final int MOVIES_LOADER = 2;
    public static final String TAG = "DETAIL_FRAGMENT_TAG";
    public static final String DETAIL_MOVIE = "MovieDetails";

    public ArrayAdapter<Trailer> mTrailersAdapter;
    public ArrayAdapter<Review> mReviewsAdapter;
    Movie movie;
    Trailer[] trailers;
    Review[] reviews;
    CustomGridView trailers_listview;
    CustomListView reviews_listview;
    ArrayList<Trailer> trailerList;
    ArrayList<Review> reviewsList;
    Cursor favouriteMovieCursor;
    Toast toast;
    ImageButton favourites_button;
    MainActivityFragment mainActivityFragment;
    ShareActionProvider mShareActionProvider;


    public DetailActivityFragment() {
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (movie != null) {
            inflater.inflate(R.menu.fragment_details_menu, menu);
            MenuItem item = menu.findItem(R.id.action_share_movie_trailer);
            mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
            mShareActionProvider.setShareIntent(createShareMovieIntent());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mainActivityFragment = (MainActivityFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_movies);

        if(savedInstanceState == null || !savedInstanceState.containsKey(KEY_SAVED_TRAILERS_LIST)) {
            trailerList = new ArrayList<Trailer>();
        }
        else {
            trailerList = savedInstanceState.getParcelableArrayList(KEY_SAVED_TRAILERS_LIST);
        }

        if(savedInstanceState == null || !savedInstanceState.containsKey(KEY_SAVED_REVIEWS_LIST)) {
            reviewsList = new ArrayList<Review>();
        }
        else {
            reviewsList = savedInstanceState.getParcelableArrayList(KEY_SAVED_REVIEWS_LIST);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
//        getLoaderManager().initLoader(REVIEWS_LOADER, null, this);
//        getLoaderManager().initLoader(TRAILERS_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        trailers_listview = (CustomGridView) rootView.findViewById(R.id.trailers_gridview);
        reviews_listview = (CustomListView) rootView.findViewById(R.id.reviews_listview);

        trailers_listview.setExpanded(true);

        Bundle arguments = getArguments();
        if (arguments != null) {
            movie = arguments.getParcelable(DetailActivityFragment.DETAIL_MOVIE);
        }

        if (movie != null) {
            getActivity().setTitle(movie.movie_title);
            new FetchTrailerTask().execute(movie.movie_id);
            new FetchReviewsTask().execute(movie.movie_id);
        }

        mTrailersAdapter = new TrailersListAdapter(getActivity(), trailerList);
        mReviewsAdapter = new ReviewsListAdapter(getActivity(), reviewsList);

        trailers_listview.setAdapter(mTrailersAdapter);
        reviews_listview.setAdapter(mReviewsAdapter);

        ScrollView detailFragmentLayout = (ScrollView) rootView.findViewById(R.id.detail_fragment_layout);
        if (movie != null)
            detailFragmentLayout.setVisibility(View.VISIBLE);
        else
            detailFragmentLayout.setVisibility(View.INVISIBLE);

        trailers_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Trailer trailer = mTrailersAdapter.getItem(position);
                String video_url = trailer.trailer_url;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(video_url));
                startActivity(intent);
            }
        });


        trailers_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Trailer trailer = mTrailersAdapter.getItem(position);
                String video_url = trailer.trailer_url;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(video_url));
                startActivity(intent);
            }
        });

        return rootView;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(KEY_SAVED_TRAILERS_LIST, trailerList);
        outState.putParcelableArrayList(KEY_SAVED_REVIEWS_LIST, reviewsList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView textview_ratings = (TextView) view.findViewById(R.id.rating_value_text);
        TextView textview_releaseDate = (TextView) view.findViewById(R.id.releasedate_value_text);
        TextView textview_description = (TextView) view.findViewById(R.id.description_text);
        ImageView imageView_poster = (ImageView) view.findViewById(R.id.poster_imageview);
        favourites_button = (ImageButton) view.findViewById(R.id.button_add_favourite);

        if (movie != null) {
            textview_ratings.setText(movie.movie_vote_average.toString());
            textview_releaseDate.setText(movie.movie_release_date);
            textview_description.setText(movie.movie_overview);
            Picasso.with(getContext()).load(movie.movie_poster).into(imageView_poster);
            if (isFavourite(getContext(), movie.movie_id))
                favourites_button.setImageResource(R.drawable.is_favorite);
            else
                favourites_button.setImageResource(R.drawable.is_not_favorite);


        }



        favourites_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean isAlreadyFavourite = isFavourite(getContext(), movie.movie_id);
                if (isAlreadyFavourite)
                    new DeleteFavourite().execute();
                else
                    new AddFavourite().execute();
            }
        });

    }


    public static boolean isFavourite(Context context, int movie_id) {
        Cursor cursor = context.getContentResolver().query(
                MovieContract.MoviesEntry.CONTENT_URI,
                null,
                MovieContract.MoviesEntry._ID + " = ?",
                new String[]{Integer.toString(movie_id)},
                null
        );


        int numRows = cursor.getCount();
        cursor.close();
        if (numRows>0) return true;
        else return false;
    }

    private class DeleteFavourite extends AsyncTask<Void, Void, Integer> {
        @Override
        protected Integer doInBackground(Void... params) {
//            Approach 1
//           int rowsDeleted = getActivity().getContentResolver().delete(
//                   MovieContract.MoviesEntry.CONTENT_URI,
//                   MovieContract.MoviesEntry.TABLE_NAME + "." + MovieContract.MoviesEntry._ID + " = ?",
//                   new String[]{Integer.toString(movie.movie_id)}
//           );
//            Approach 2
           // int rowsDeleted = getActivity().getContentResolver().delete(
           //         MovieContract.MoviesEntry.CONTENT_URI,
           //         MovieContract.MoviesEntry.TABLE_NAME + "." + MovieContract.MoviesEntry._ID + " = " + Integer.toString(movie.movie_id),
           //         null
//           );
//            Approach 3 - Works
             int rowsDeleted = getActivity().getContentResolver().delete(
                     MovieContract.MoviesEntry.buildMovieUri(movie.movie_id),
                     null,
                     null
             );


            Log.d(LOG_TAG, rowsDeleted + " rows deleted.");
            return rowsDeleted;
        }

        @Override
        protected void onPostExecute(Integer rowsDeleted) {
            favourites_button.setImageResource(R.drawable.ic_play_arrow_pink_24px);
            if (toast != null) {
                toast.cancel();
            }
            toast = Toast.makeText(getActivity(), getString(R.string.removed_movie_from_favorites), Toast.LENGTH_SHORT);
            toast.show();
            favourites_button.setImageResource(R.drawable.is_not_favorite);
            mainActivityFragment.refreshList();

        }
    }

    private class AddFavourite extends AsyncTask<Void, Void, Uri> {

        @Override
        protected Uri doInBackground(Void... params) {

            ContentValues values = new ContentValues();

            values.put(MovieContract.MoviesEntry._ID, movie.movie_id);
            values.put(MovieContract.MoviesEntry.COLUMN_TITLE, movie.movie_title);
            values.put(MovieContract.MoviesEntry.COLUMN_POSTER, movie.movie_poster);
            values.put(MovieContract.MoviesEntry.COLUMN_OVERVIEW, movie.movie_overview);
            values.put(MovieContract.MoviesEntry.COLUMN_VOTE_AVG, movie.movie_vote_average);
            values.put(MovieContract.MoviesEntry.COLUMN_RELEASE_DATE, movie.movie_release_date);


            Uri returnedUri = getActivity().getContentResolver().insert(MovieContract.MoviesEntry.CONTENT_URI,
                    values);
            Log.d(LOG_TAG, "Inserted movie into favourites db: " + returnedUri.toString());
            return returnedUri;
        }

        @Override
        protected void onPostExecute(Uri returnUri) {
            if (toast != null) {
                toast.cancel();
            }
            toast = Toast.makeText(getActivity(), getString(R.string.added_movie_to_favorites), Toast.LENGTH_SHORT);
            toast.show();
            favourites_button.setImageResource(R.drawable.is_favorite);
            mainActivityFragment.refreshList();

        }
    }



    private Intent createShareMovieIntent() {
        String trailerUrl;
        if (trailers != null) {
            try {
                trailerUrl = trailers[0].trailer_url;
            }
            catch (Exception e) {
                trailerUrl = " [No trailer] ";
            }
        }
        else
            trailerUrl = " [No trailer] ";
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        shareIntent.putExtra(Intent.EXTRA_TEXT, movie.movie_title + " : " +
                 trailerUrl);
        return shareIntent;
    }


//    @Override
//    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//
//        if (id == REVIEWS_LOADER) {
//            Uri reviewsForMovieUri = MoviesContract.ReviewEntry.buildReviewsForMovie(movie.movie_id);
//            Cursor reviewsCursor = getActivity().getContentResolver().query(reviewsForMovieUri, null, null, null, null);
//            return new CursorLoader(getContext(), reviewsForMovieUri, null, null, null, null);
//        }
//        else if (id == TRAILERS_LOADER) {
//            Uri trailersForMovieUri = MoviesContract.TrailerEntry.buildTrailersForMovie(movie.movie_id);
//            Cursor trailersCursor = getActivity().getContentResolver().query(trailersForMovieUri, null, null, null, null);
//            return new CursorLoader(getContext(), trailersForMovieUri, null, null, null, null);
//        }
//        else
//            return null;
//    }
//
//    @Override
//    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
//        if (loader.getId() == TRAILERS_LOADER)
//            mfavouriteTrailersAdapter.swapCursor(cursor);
//        else if (loader.getId() == REVIEWS_LOADER)
//            mfavouriteReviewsAdapter.swapCursor(cursor);
//    }
//
//    @Override
//    public void onLoaderReset(Loader<Cursor> loader) {
//        if (loader.getId() == TRAILERS_LOADER)
//            mfavouriteTrailersAdapter.swapCursor(null);
//        else if (loader.getId() == REVIEWS_LOADER)
//            mfavouriteReviewsAdapter.swapCursor(null);
//    }
//
//


//    public void saveMovieAsFavourite() {
//        int rowId = saveMovie();
//        if (rowId != -1) {
//            saveTrailers();
//            saveReviews();
//        }
//        else
//            Toast.makeText(getContext(), "Movie already exists in db.", Toast.LENGTH_LONG).show();
//    }


//    private int saveMovie() {
//
//        Cursor cursor = getActivity().getContentResolver().query(MovieContract.MoviesEntry.buildMovieUri(movie.movie_id),null,null,null,null);
//        if (cursor.getCount() <= 0) {
//            ContentValues movieContentValue = new ContentValues();
//            movieContentValue.put(MovieEntry._ID, movie.movie_id);
//            movieContentValue.put(MoviesEntry.COLUMN_TITLE, movie.movie_title);
//            movieContentValue.put(MoviesEntry.COLUMN_OVERVIEW, movie.movie_overview);
//            movieContentValue.put(MoviesEntry.COLUMN_VOTE_AVG, movie.movie_vote_average);
//            movieContentValue.put(MoviesEntry.COLUMN_RELEASE_DATE, movie.movie_release_date);
//            movieContentValue.put(MoviesEntry.COLUMN_POSTER, movie.movie_poster);
//            getContext().getContentResolver().insert(MoviesEntry.CONTENT_URI, movieContentValue);
//            Log.d(LOG_TAG, "Inserted movie to db.");
//            return movie.movie_id;
//        }
//        else
//            return -1;          // Movie already exists in db
//    }


//    private void saveReviews() {
//
//        int numberOfReviews = reviews.length;
//
//        Vector<ContentValues> cVVector = new Vector<ContentValues>(numberOfReviews);
//
//        for (Review review : reviews) {
//
//            int movie_id = review.movie_id;
//            String review_author = review.review_author;
//            String review_content = review.review_content;
//
//            ContentValues reviewContentValue = new ContentValues();
//            reviewContentValue.put(MoviesContract.ReviewEntry.COLUMN_MOVIE_ID, movie_id);
//            reviewContentValue.put(MoviesContract.ReviewEntry.COLUMN_REVIEW_AUTHOR, review_author);
//            reviewContentValue.put(MoviesContract.ReviewEntry.COLUMN_REVIEW_CONTENT, review_content);
//            cVVector.add(reviewContentValue);
//        }
//
//        // add to database
//        if ( cVVector.size() > 0 ) {
//            ContentValues[] cvArray = new ContentValues[cVVector.size()];
//            cVVector.toArray(cvArray);
//            getContext().getContentResolver().bulkInsert(MoviesContract.ReviewEntry.CONTENT_URI, cvArray);
//        }
//
//    }
//
//    private void saveTrailers() {
//
//        int numberOfTrailers = trailers.length;
//
//        Vector<ContentValues> cVVector = new Vector<ContentValues>(numberOfTrailers);
//
//        for (Trailer trailer : trailers) {
//
//            int movie_id = trailer.movie_id;
//            String trailer_name = trailer.trailer_name;
//            String trailer_url = trailer.trailer_url;
//            String trailer_thumbnail = trailer.trailer_thumbnail;
//
//            ContentValues trailerContentValue = new ContentValues();
//            trailerContentValue.put(MoviesContract.TrailerEntry.COLUMN_MOVIE_ID, movie_id);
//            trailerContentValue.put(MoviesContract.TrailerEntry.COLUMN_TRAILER_NAME, trailer_name);
//            trailerContentValue.put(MoviesContract.TrailerEntry.COLUMN_TRAILER_URL, trailer_url);
//            trailerContentValue.put(MoviesContract.TrailerEntry.COLUMN_TRAILER_THUMBNAIL, trailer_thumbnail);
//            cVVector.add(trailerContentValue);
//        }
//
//        // add to database
//        if ( cVVector.size() > 0 ) {
//            ContentValues[] cvArray = new ContentValues[cVVector.size()];
//            cVVector.toArray(cvArray);
//            getContext().getContentResolver().bulkInsert(MoviesContract.TrailerEntry.CONTENT_URI, cvArray);
//
//        }
//
//    }
//


    public class FetchTrailerTask extends AsyncTask<Integer, Void, Trailer[]> {

        public String LOG_TAG = FetchTrailerTask.class.getSimpleName();

        @Override
        protected Trailer[] doInBackground(Integer... params) {

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String trailersJsonStr = null;

            int movie_id = params[0];

            final String API_KEY = BuildConfig.THE_MOVIE_DB_API_KEY;

            try {

                final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie/" + movie_id + "/videos";
                final String API_KEY_PARAM = "api_key";

                Uri builtUri = Uri.parse(MOVIES_BASE_URL)
                        .buildUpon()
                        .appendQueryParameter(API_KEY_PARAM, API_KEY)
                        .build();

                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                Log.d(LOG_TAG, "Fetching trailers from API for movie " + movie_id);

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
                trailersJsonStr = buffer.toString();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(LOG_TAG, "Error obtaining trailers data from API.", e);
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
                return getTrailersFromJson(trailersJsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(Trailer[] trailers) {
            if (trailers != null) {
//                mTrailersAdapter.clear();
//                mTrailersAdapter.addAll(trailers);
//                mTrailersAdapter.notifyDataSetChanged();

                trailerList.clear();
                for (Trailer t : trailers)
                    trailerList.add(t);

                mTrailersAdapter.notifyDataSetChanged();

                if (mShareActionProvider != null) {
                    mShareActionProvider.setShareIntent(createShareMovieIntent());
                }

            }
            else {
                Toast.makeText(getContext(), "Unable to fetch trailers, Try again later.", Toast.LENGTH_SHORT).show();
            }

        }


        private Trailer[] getTrailersFromJson(String trailersJsonStr) throws JSONException{

            final String TMDB_TRAILER_KEY = "key";
            final String TMDB_TRAILER_NAME = "name";
            final String TMDB_TRAILERS = "results";

            final String TRAILER_BASE_URL = "https://www.youtube.com/watch?v=";

            JSONObject trailersJson = new JSONObject(trailersJsonStr);
            JSONArray trailersArray = trailersJson.getJSONArray(TMDB_TRAILERS);

            int numberOfTrailers = trailersArray.length();

            trailers = new Trailer[numberOfTrailers];

            for (int i=0; i<numberOfTrailers; i++) {

                JSONObject movieObject = trailersArray.getJSONObject(i);

                String trailer_name = movieObject.getString(TMDB_TRAILER_NAME);
                String url_partial = movieObject.getString(TMDB_TRAILER_KEY);

                String trailer_url = TRAILER_BASE_URL + url_partial;
                String trailer_thumbnail = getTrailerThumbnailFromKey(url_partial);

                Trailer trailer = new Trailer(movie.movie_id, trailer_name, trailer_url, trailer_thumbnail);
                trailers[i] = trailer;
            }

            return trailers;
        }

        private String getTrailerThumbnailFromKey(String url_partial) {
            return "http://i.ytimg.com/vi/" + url_partial + "/mqdefault.jpg";
        }
    }




    public class FetchReviewsTask extends AsyncTask<Integer, Void, Review[]> {

        public String LOG_TAG = FetchReviewsTask.class.getSimpleName();

        @Override
        protected Review[] doInBackground(Integer... params) {

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String reviewsJsonStr = null;

            int movie_id = params[0];

            final String API_KEY = BuildConfig.THE_MOVIE_DB_API_KEY;

            try {

                final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie/" + movie_id + "/reviews";
                final String API_KEY_PARAM = "api_key";

                Uri builtUri = Uri.parse(MOVIES_BASE_URL)
                        .buildUpon()
                        .appendQueryParameter(API_KEY_PARAM, API_KEY)
                        .build();

                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                Log.d(LOG_TAG, "Fetching reviews from API for movie " + movie_id);

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
                reviewsJsonStr = buffer.toString();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(LOG_TAG, "Error obtaining reviews data from API.", e);
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
                return getReviewsFromJson(reviewsJsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(Review[] reviews) {
            if (reviews != null) {

                reviewsList.clear();
                for (Review r : reviews) {
                    reviewsList.add(r);
//                    Log.d(LOG_TAG, "Review: " + r);
                }

                mReviewsAdapter.notifyDataSetChanged();

            }
            else {
                Toast.makeText(getContext(), "Unable to fetch reviews, Try again later.", Toast.LENGTH_SHORT).show();
            }

        }


        private Review[] getReviewsFromJson(String reviewsJsonStr) throws JSONException{

            final String TMDB_REVIEW_AUTHOR = "author";
            final String TMDB_REVIEW_CONTENT = "content";
            final String TMDB_REVIEWS = "results";

            JSONObject reviewsJson = new JSONObject(reviewsJsonStr);
            JSONArray reviewsArray = reviewsJson.getJSONArray(TMDB_REVIEWS);

            int numberOfReviews = reviewsArray.length();
            Log.d(LOG_TAG, "No of reviews: " + numberOfReviews);

            reviews = new Review[numberOfReviews];

            for (int i=0; i<numberOfReviews; i++) {

                JSONObject movieObject = reviewsArray.getJSONObject(i);

                String review_author = movieObject.getString(TMDB_REVIEW_AUTHOR);
                String review_content = movieObject.getString(TMDB_REVIEW_CONTENT);

                Review review = new Review(movie.movie_id, review_author, review_content);
                reviews[i] = review;
            }

            return reviews;
        }
    }

}
