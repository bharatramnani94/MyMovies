package com.bharatramnani.mymovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by B on 25-01-2016.
 */
public class Review implements Parcelable {

    Integer movie_id;
    String review_author;
    String review_content;

    public Review(Integer movie_id, String review_author, String review_content) {
        this.movie_id = movie_id;
        this.review_author = review_author;
        this.review_content = review_content;
    }

    private Review (Parcel parcel) {
        this.movie_id = parcel.readInt();
        this.review_author = parcel.readString();
        this.review_content = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(movie_id);
        parcel.writeString(review_author);
        parcel.writeString(review_content);
    }

    public static Parcelable.Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel source) {
            return new Review(source);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    @Override
    public String toString() {
        return this.review_author + " : " + this.review_content;
    }
}
