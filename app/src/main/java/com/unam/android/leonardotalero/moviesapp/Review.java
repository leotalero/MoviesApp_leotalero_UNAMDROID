package com.unam.android.leonardotalero.moviesapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by leonardotalero on 6/29/17.
 */

public class Review implements Parcelable {

    public String mId;
    public String mAuthor;
    public String mContent;
    public String mUrl;
    public String marray;


    public Review() {
    }

    public Review(String mId, String mAuthor, String mContent, String mUrl,String mjson) {
        this.mId = mId;
        this.mAuthor = mAuthor;
        this.mContent = mContent;
        this.mUrl = mUrl;
        this.marray = mjson;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mId);
        dest.writeString(this.mAuthor);
        dest.writeString(this.mContent);
        dest.writeString(this.mUrl);
        dest.writeString(this.marray);
    }

    protected Review(Parcel in) {
        this.mId = in.readString();
        this.mAuthor = in.readString();
        this.mContent = in.readString();
        this.mUrl = in.readString();
        this.marray = in.readString();
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel source) {
            return new Review(source);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };
}
