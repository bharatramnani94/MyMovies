package com.bharatramnani.mymovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.Callback {

    String LOG_TAG = MainActivity.class.getSimpleName();

    Bundle savedInstanceState;
    boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "MainActivity launched.");
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.movie_details_container) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
               getSupportFragmentManager().beginTransaction()
                       .replace(R.id.movie_details_container, new DetailActivityFragment(), DetailActivityFragment.TAG)
                       .commit();
                Log.d(LOG_TAG, "Replaced details fragment");
            }
        }
        else {
            mTwoPane = false;
        }


    }

    @Override
    public void onItemSelected(Movie movie) {
        if (mTwoPane) {
            Bundle args = new Bundle();
            args.putParcelable(DetailActivityFragment.DETAIL_MOVIE, movie);
            DetailActivityFragment fragment = new DetailActivityFragment();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_details_container, fragment, DetailActivityFragment.TAG)
                    .commit();
        }
        else {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(DetailActivityFragment.DETAIL_MOVIE, movie);
            startActivity(intent);
        }
    }

}
