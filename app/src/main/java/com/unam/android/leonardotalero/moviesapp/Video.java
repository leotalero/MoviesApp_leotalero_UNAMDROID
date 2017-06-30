package com.unam.android.leonardotalero.moviesapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by leonardotalero on 6/29/17.
 */

public class Video implements Parcelable {

    public String mId;
    public String mKey;
    public String mName;
    public String mSite;
    public String mType;
    public String mSize;
    public String mLink;


    public Video() {
    }


    public Video(String mId, String mKey, String mName, String mSite, String mSize,String mType, String mLink) {
        this.mId = mId;
        this.mKey = mKey;
        this.mName = mName;
        this.mSite = mSite;
        this.mType = mType;
        this.mSize = mSize;
        this.mLink = mLink;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mId);
        dest.writeString(this.mKey);
        dest.writeString(this.mName);
        dest.writeString(this.mSite);
        dest.writeString(this.mType);
        dest.writeString(this.mSize);
        dest.writeString(this.mLink);
    }

    protected Video(Parcel in) {
        this.mId = in.readString();
        this.mKey = in.readString();
        this.mName = in.readString();
        this.mSite = in.readString();
        this.mType = in.readString();
        this.mSize = in.readString();
        this.mLink = in.readString();
    }

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel source) {
            return new Video(source);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };
}
