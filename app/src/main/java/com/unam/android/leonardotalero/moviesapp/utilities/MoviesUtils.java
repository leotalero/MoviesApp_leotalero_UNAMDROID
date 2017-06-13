package com.unam.android.leonardotalero.moviesapp.utilities;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.unam.android.leonardotalero.moviesapp.MovieClass;
import com.unam.android.leonardotalero.moviesapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by leonardotalero on 6/8/17.
 */

public class MoviesUtils {


    public static List<MovieClass>  getSimpleStringsFromJson(Context context, String forecastJsonStr)
            throws JSONException {

        /* Weather information. Each day's forecast info is an element of the "list" array */
        final String OWM_RESULTS= "results";
        final String OWM_PAGE= "page";
        final String OWM_TITTLE_original="original_title";
        /* All temperatures are children of the "temp" object */
        final String OWM_VOTE_COUNT = "vote_count";

        /* Max temperature for the day */
        final String OWM_ID = "id";
        final String OWM_TITLE = "title";
        final String OWM_DESCR= "description";
        final String OWM_POPULARITY= "popularity";
        final String OWM_POSTER = "poster_path";
        final String OWM_OVERVIEW = "overview";
        final String OWM_RELEASE= "release_date";
        final String OWM_MESSAGE_CODE = "cod";
        final String OWM_VOTE_AVER="vote_average";
        final String OWM_GENRE = "genre_ids";
        /* String array to hold each day's weather String */
        List<MovieClass> parsedData=new ArrayList<MovieClass>();



        JSONObject forecastJson = new JSONObject(forecastJsonStr);

        /* Is there an error? */
        if (forecastJson.has(OWM_MESSAGE_CODE)) {
            int errorCode = forecastJson.getInt(OWM_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }

        JSONArray array = forecastJson.getJSONArray(OWM_RESULTS);



        long localDate = System.currentTimeMillis();
        long utcDate = TimeUtils.getUTCDateFromLocal(localDate);
        long startDay = TimeUtils.normalizeDate(utcDate);

        for (int i = 0; i < array.length(); i++) {
            String date;
            String title;
            String date_release;
            /* These are the values that will be collected */
            double popularity;
            double  vote_average;
            int id;
            int  vote_count;
            String over_view;
            String backdrop_path;
            String poster_path;
            long dateTimeMillis;
            String original_title;


            /* Get the JSON object representing the day */
            JSONObject movie = array.getJSONObject(i);

            /*
             * We ignore all the datetime values embedded in the JSON and assume that
             * the values are returned in-order by day (which is not guaranteed to be correct).
             */
            dateTimeMillis = startDay + TimeUtils.DAY_IN_MILLIS * i;
            date = TimeUtils.getFriendlyDateString(context, dateTimeMillis, false);
            //JSONObject genreObject =
            //        movie.getJSONObject(OWM_GENRE);
            //description = weatherObject.getString(OWM_DESCRIPTION);


            title = movie.getString(OWM_TITLE);
            popularity = movie.getDouble(OWM_POPULARITY);
            vote_average = movie.getDouble(OWM_VOTE_AVER);
            id = movie.getInt(OWM_ID);
            vote_count = movie.getInt(OWM_VOTE_COUNT);
            over_view = movie.getString(OWM_OVERVIEW);
            date_release=movie.getString(OWM_RELEASE);
            poster_path=movie.getString(OWM_POSTER);
            original_title=movie.getString(OWM_TITTLE_original);



            MovieClass movieObject= new MovieClass(title,date_release,poster_path,vote_average,over_view,original_title,popularity);
            parsedData.add(movieObject);


        }

        return parsedData;
    }

public  static  void setImagePicasso(String urlString, Context context, ImageView imageView){
    String url_path = urlString;
    URL url =NetworkUtils.buildUrlImage(url_path);
    Picasso.with(context).load(url.toString())
            .placeholder(R.drawable.default_poster)
            .error(R.drawable.default_poster)
            .into(imageView);

}



}
