package com.bharatramnani.mymovies;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
//
///**
// * Created by B on 31-12-2015.
// */
public class ReviewsListAdapter extends ArrayAdapter<Review> {

    static final String LOG_TAG = ReviewsListAdapter.class.getSimpleName();

    List<Review> reviewList;

    public ReviewsListAdapter(Activity context, List<Review> reviewList) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, reviewList);
        this.reviewList = reviewList;
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    The AdapterView position that is requesting a view
     * @param convertView The recycled view to populate.
     *                    (search online for "android view recycling" to learn more)
     * @param parent The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Gets the AndroidFlavor object from the ArrayAdapter at the appropriate position
        Review review = getItem(position);

        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_review_layout, parent, false);
        }

        TextView review_author = (TextView) convertView.findViewById(R.id.review_author);
        review_author.setText(review.review_author);

        TextView review_content = (TextView) convertView.findViewById(R.id.review_content);
        review_content.setText(review.review_content);

        return convertView;
    }
}