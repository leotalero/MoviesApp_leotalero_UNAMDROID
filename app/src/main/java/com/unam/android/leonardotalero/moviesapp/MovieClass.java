package com.unam.android.leonardotalero.moviesapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by leonardotalero on 6/11/17.
 */

public class MovieClass implements Parcelable {

    private int mData;

    public String mtitle;
    public  String mrelease_date;
    public double mvote_average;
    public String mimage; // drawable reference id
    public String moverview;
    public String moriginal_title;
    public double mpopularity;
        public MovieClass(String vTitle, String vDate, String vimage,double vote_average,String vOverview,
                          String original_title,double popularity)
        {
            this.mtitle = vTitle;
            this.mrelease_date = vDate;
            this.mimage = vimage;
            this.mvote_average=vote_average;
            this.moverview=vOverview;
            this.moriginal_title=original_title;
            this.mpopularity=popularity;
        }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mData);
        dest.writeString(this.mtitle);
        dest.writeString(this.mrelease_date);
        dest.writeDouble(this.mvote_average);
        dest.writeString(this.mimage);
        dest.writeString(this.moverview);
        dest.writeString(this.moriginal_title);
        dest.writeDouble(this.mpopularity);
    }

    protected MovieClass(Parcel in) {
        this.mData = in.readInt();
        this.mtitle = in.readString();
        this.mrelease_date = in.readString();
        this.mvote_average = in.readDouble();
        this.mimage = in.readString();
        this.moverview = in.readString();
        this.moriginal_title = in.readString();
        this.mpopularity = in.readDouble();
    }

    public static final Creator<MovieClass> CREATOR = new Creator<MovieClass>() {
        @Override
        public MovieClass createFromParcel(Parcel source) {
            return new MovieClass(source);
        }

        @Override
        public MovieClass[] newArray(int size) {
            return new MovieClass[size];
        }
    };
}
