package com.bharatramnani.mymovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by B on 25-01-2016.
 */
public class Trailer implements Parcelable {

    Integer movie_id;
    String trailer_name;
    String trailer_url;
    String trailer_thumbnail;

    @Override
    public String toString() {
        return movie_id.toString() + trailer_name + trailer_url;
    }

    public Trailer(Integer movie_id, String trailer_name, String trailer_url, String trailer_thumbnail) {
        this.movie_id = movie_id;
        this.trailer_name = trailer_name;
        this.trailer_url = trailer_url;
        this.trailer_thumbnail = trailer_thumbnail;
    }

    private Trailer (Parcel parcel) {
        this.movie_id = parcel.readInt();
        this.trailer_name = parcel.readString();
        this.trailer_url = parcel.readString();
        this.trailer_thumbnail = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(movie_id);
        parcel.writeString(trailer_name);
        parcel.writeString(trailer_url);
        parcel.writeString(trailer_thumbnail);
    }

    public static Parcelable.Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel source) {
            return new Trailer(source);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };
}
