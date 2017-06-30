package com.unam.android.leonardotalero.moviesapp.utilities;

/**
 * Created by leonardotalero on 6/8/17.
 */


import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the network.
 */
public class NetworkUtils {
    //"http://api.themoviedb.org/3/movie/popular?api_key=[YOUR_API_KEY]";
    final static String API_KEY="647f581dfe7f1faf564e531821cf1657";
    final static String BASE_URL =
            "http://api.themoviedb.org/3/";
    final static  String BASE_URL_IMAGE="http://image.tmdb.org/t/p/w342/";

    final static String PARAM_API= "api_key";
    final static String PARAM_LANGUAGE="language";
    final static String PARAM_LANGUAGE_default="en-US";
    final static String PARAM_PAGE="page";
    final static String PARAM_REGION="region";
    final static String PARAM_QUERY="query";

    /*
     * The sort field. One of stars, forks, or updated.
     * Default: results are sorted by best match if no field is specified.
     */
    //final static String PARAM_SORT = "sort";
    //final static String sortBy = "stars";

    /**
     * Builds the URL used to query GitHub.
     *
     * @param searchQuery The keyword that will be queried for.
     * @return The URL to use to query .
     * movie/popular
     */
    public static URL buildUrl(String searchQuery,int page) {

        Uri builtUri = Uri.parse(BASE_URL+searchQuery)
                .buildUpon()
                .appendQueryParameter(PARAM_API, API_KEY)
                .appendQueryParameter(PARAM_PAGE,String.valueOf(page))
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * Builds the URL used to query GitHub.
     *
     * @param searchQuery The keyword that will be queried for.
     * @return The URL to use to query .
     * movie/popular
     */
    public static URL buildUrlComplete(String searchQuery,String language,Integer page,String region) {

        Uri builtUri = Uri.parse(BASE_URL+searchQuery)
                .buildUpon()
                .appendQueryParameter(PARAM_API, API_KEY)
                .appendQueryParameter(PARAM_LANGUAGE,language)
                .appendQueryParameter(PARAM_PAGE,page.toString())
                .appendQueryParameter(PARAM_REGION,region)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }


    //http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg

    public static URL buildUrlImage(String idposter) {

        Uri builtUri = Uri.parse(BASE_URL_IMAGE+idposter)
                .buildUpon()
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    //https://api.themoviedb.org/3/search/movie?api_key={api_key}&query=Jack+Reacher

    public static URL buildUrlSearch(String searchQuery) {

        Uri builtUri = Uri.parse(BASE_URL+"search/movie")
                .buildUpon()
                .appendQueryParameter(PARAM_API, API_KEY)
                .appendQueryParameter(PARAM_QUERY,searchQuery)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }


    //https://api.themoviedb.org/3/movie/297762/videos
    public static URL buildUrlVideos(int idMovie) {

        Uri builtUri = Uri.parse(BASE_URL+"movie/"+idMovie+"/videos")
                .buildUpon()
                .appendQueryParameter(PARAM_API, API_KEY)
                .appendQueryParameter(PARAM_LANGUAGE,PARAM_LANGUAGE_default )
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }


    //https://api.themoviedb.org/3/movie/297762/reviews
    public static URL buildUrlReviews(int idMovie) {

        Uri builtUri = Uri.parse(BASE_URL+"movie/"+idMovie+"/reviews")
                .buildUpon()
                .appendQueryParameter(PARAM_API, API_KEY)
                .appendQueryParameter(PARAM_LANGUAGE,PARAM_LANGUAGE_default )
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }
    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static Boolean isOnline() {
        try {
            Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com");
            int returnVal = p1.waitFor();
            boolean reachable = (returnVal==0);
            return reachable;
        } catch (Exception e) {

            e.printStackTrace();
        }
        return false;
    }
}