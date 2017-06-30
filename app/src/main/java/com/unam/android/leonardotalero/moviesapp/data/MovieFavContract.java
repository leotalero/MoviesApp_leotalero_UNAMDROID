package com.unam.android.leonardotalero.moviesapp.data;

/**
 * Created by leonardotalero on 6/29/17.
 */

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class MovieFavContract {


    public static final String CONTENT_AUTHORITY = "com.unam.android.leonardotalero.moviesapp";


    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final class MovieFavEntry implements BaseColumns {
        public static final String TABLE_NAME = "favorite";
        public static final String COLUMN_ID_MOVIE = "id_movie";
        public static final String COLUMN_TIMESTAMP="timestamp";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_TIMESTAMP_RELEASE_DATE = "release_date";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_POSTER = "poster_path";
        public static final String COLUMN_TITLE_ORIGINAL = "title_original";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_VIDEOS_JSON = "videos";
        public static final String COLUMN_REVIEWS_JSON = "reviewss";


        // create content uri
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(TABLE_NAME).build();
        // create cursor of base type directory for multiple entries
        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
        // create cursor of base type item for single entry
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE +"/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;

        // for building URIs on insertion
        public static Uri buildUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }


}