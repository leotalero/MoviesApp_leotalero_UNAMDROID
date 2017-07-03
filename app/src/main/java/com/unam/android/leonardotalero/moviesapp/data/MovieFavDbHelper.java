package com.unam.android.leonardotalero.moviesapp.data;

/**
 * Created by leonardotalero on 6/29/17.
 */


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.unam.android.leonardotalero.moviesapp.data.MovieFavContract.MovieFavEntry;


public class MovieFavDbHelper extends SQLiteOpenHelper {

    // The database name
    private static final String DATABASE_NAME = "movies.db";

    // If you change the database schema, you must increment the database version
    private static final int DATABASE_VERSION = 5;

    // Constructor
    public MovieFavDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Create a table to hold waitlist data
        final String SQL_CREATE_WAITLIST_TABLE = "CREATE TABLE " + MovieFavEntry.TABLE_NAME + " (" +
                MovieFavEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieFavEntry.COLUMN_TIMESTAMP+ " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                MovieFavEntry.COLUMN_ID_MOVIE+ " INTEGER NOT NULL, " +
                MovieFavEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieFavEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                MovieFavEntry.COLUMN_TIMESTAMP_RELEASE_DATE+ " TIMESTAMP, " +
                MovieFavEntry.COLUMN_VOTE_AVERAGE+ " DOUBLE , " +
                MovieFavEntry.COLUMN_POSTER+ " TEXT, " +
                MovieFavEntry.COLUMN_TITLE_ORIGINAL+ " TEXT, " +
                MovieFavEntry.COLUMN_POPULARITY+ " DOUBLE,  " +
                MovieFavEntry.COLUMN_VIDEOS_JSON+ " TEXT , " +
                MovieFavEntry.COLUMN_REVIEWS_JSON+ " TEXT  " +
        "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_WAITLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // For now simply drop the table and create a new one. This means if you change the
        // DATABASE_VERSION the table will be dropped.
        // In a production app, this method might be modified to ALTER the table
        // instead of dropping it, so that existing data is not deleted.
        sqLiteDatabase.execSQL("ALTER TABLE IF EXISTS " + MovieFavEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
