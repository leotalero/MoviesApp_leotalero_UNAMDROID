package com.unam.android.leonardotalero.moviesapp;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.unam.android.leonardotalero.moviesapp.data.MovieFavContract;
import com.unam.android.leonardotalero.moviesapp.utilities.MoviesUtils;
import com.unam.android.leonardotalero.moviesapp.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        LoaderCallbacks<List<MovieClass>>,FavoriteFragment.OnFragmentInteractionListener{

    private EditText mSearchBoxEditText;
    private TextView mUrlDisplayTextView;
    private GridView mSearchResultsGridView;
    private TextView mErrorMessageDisplay;



    private ProgressBar mLoadingIndicator;
    private ArrayList<MovieClass> resultMovies;

    private String  query="movie/popular";
    private int page=1;
    private Boolean flagLoadingData=false;
    private Boolean adapterLoad=false;
    private static int LOADER_ID=10;
    private  URL SearchUrl;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int TASK_LOADER_ID = 10;
    private Loader<List<MovieClass>> loader;
    private List<MovieClass> moviesFavorite=new ArrayList<MovieClass>();
    private boolean favoriteView=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchBoxEditText = (EditText) findViewById(R.id.et_search_box);
        //mUrlDisplayTextView = (TextView) findViewById(R.id.tv_url_display);
        mSearchResultsGridView = (GridView) findViewById(R.id.movies_grid);

        // COMPLETED (13) Get a reference to the error TextView using findViewById
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        // COMPLETED (25) Get a reference to the ProgressBar using findViewById
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);


                if(savedInstanceState == null || !savedInstanceState.containsKey("resultMovies") ) {
                    resultMovies=new ArrayList<MovieClass>();
                   // query="movie/popular";
                    searchQuery(query,page);

                }
                else {
                    resultMovies = savedInstanceState.getParcelableArrayList("resultMovies");
                    showJsonDataView();
                    setData(resultMovies);
                }


        int loaderId = LOADER_ID;

        /*
         * From MainActivity, we have implemented the LoaderCallbacks interface with the type of
         * String array. (implements LoaderCallbacks<String[]>) The variable callback is passed
         * to the call to initLoader below. This means that whenever the loaderManager has
         * something to notify us of, it will do so through this callback.
         */
        LoaderCallbacks<List<MovieClass>> callback = MainActivity.this;

        /*
         * The second parameter of the initLoader method below is a Bundle. Optionally, you can
         * pass a Bundle to initLoader that you can then access from within the onCreateLoader
         * callback. In our case, we don't actually use the Bundle, but it's here in case we wanted
         * to.
         */
        Bundle bundleForLoader = null;

        /*
         * Ensures a loader is initialized and active. If the loader doesn't already exist, one is
         * created and (if the activity/fragment is currently started) starts the loader. Otherwise
         * the last created loader is re-used.
         */
        getSupportLoaderManager().initLoader(loaderId, bundleForLoader, callback);


    }


    private void searchQuery(String query,int page) {

        SearchUrl = NetworkUtils.buildUrl(query.toString(),page);
        Bundle queryBundle = new Bundle();
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<List<MovieClass>> SearchLoader = loaderManager.getLoader(LOADER_ID);
        if (SearchLoader == null) {
            loaderManager.initLoader(LOADER_ID, queryBundle, this);
        } else {
            loaderManager.restartLoader(LOADER_ID, queryBundle, this);
        }

            //new QueryTask().execute(SearchUrl);


    }

    private void searchQueryMovie(String query) {
        //String query = mSearchBoxEditText.getText().toString();
        SearchUrl = NetworkUtils.buildUrlSearch(query.toString());
        Bundle queryBundle = new Bundle();
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<List<MovieClass>> SearchLoader = loaderManager.getLoader(LOADER_ID);
        if (SearchLoader == null) {
            loaderManager.initLoader(LOADER_ID, queryBundle, this);
        } else {
            loaderManager.restartLoader(LOADER_ID, queryBundle, this);
        }



    }


    private void showJsonDataView() {
        // First, make sure the error is invisible
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        // Then, make sure the JSON data is visible
        mSearchResultsGridView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        // First, hide the currently visible data
        mSearchResultsGridView.setVisibility(View.INVISIBLE);
        // Then, show the error
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("resultMovies", resultMovies);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_order, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        resultMovies= new ArrayList<MovieClass>();
        if (itemThatWasClickedId == R.id.action_search_p) {
            query="movie/popular";
            adapterLoad=false;
            searchQuery(query,1);
            return true;
        }
        if (itemThatWasClickedId == R.id.action_search_t) {
            query="movie/top_rated";
            adapterLoad=false;
            searchQuery(query,1);
            return true;
        }
        if (itemThatWasClickedId == R.id.search) {
            query=mSearchBoxEditText.getText().toString();
            adapterLoad=false;
            searchQueryMovie(query);
            return true;
        }
        if (itemThatWasClickedId == R.id.favorites) {
            query="movie/top_rated";
            adapterLoad=false;
            searchFavorites();
            return true;
        }
        //buildUrlSearch
        return super.onOptionsItemSelected(item);
    }

    private void searchFavorites() {

     /*   FavoriteFragment favFragment = FavoriteFragment.newInstance("a","b");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_fragment_fav, favFragment ).commit();*/

        favoriteView=true;
        String selection=null;
        String[] selectionArgs=null;
        Uri uri = MovieFavContract.MovieFavEntry.CONTENT_URI;
        CursorLoader cursorLoader=new CursorLoader(MainActivity.this,
                uri,
                null,
                selection,
                selectionArgs,
                null);

      Cursor cursor=  cursorLoader.loadInBackground();

        if (cursor != null && cursor.moveToFirst()) {
            /* We have valid data, continue on to bind the data to the UI */
            if (cursor.moveToFirst()) {
                do {

                    MovieClass movie=new MovieClass(cursor.getInt(cursor.getColumnIndex(MovieFavContract.MovieFavEntry.COLUMN_ID_MOVIE))
                            ,cursor.getString(cursor.getColumnIndex(MovieFavContract.MovieFavEntry.COLUMN_TITLE_ORIGINAL))
                            ,cursor.getString(cursor.getColumnIndex(MovieFavContract.MovieFavEntry.COLUMN_TIMESTAMP_RELEASE_DATE))
                            ,cursor.getString(cursor.getColumnIndex(MovieFavContract.MovieFavEntry.COLUMN_POSTER))
                            ,cursor.getDouble(cursor.getColumnIndex(MovieFavContract.MovieFavEntry.COLUMN_VOTE_AVERAGE))
                            ,cursor.getString(cursor.getColumnIndex(MovieFavContract.MovieFavEntry.COLUMN_DESCRIPTION))
                            ,cursor.getString(cursor.getColumnIndex(MovieFavContract.MovieFavEntry.COLUMN_TITLE_ORIGINAL)),
                            cursor.getDouble(cursor.getColumnIndex(MovieFavContract.MovieFavEntry.COLUMN_POPULARITY)),
                            cursor.getString(cursor.getColumnIndex(MovieFavContract.MovieFavEntry.COLUMN_VIDEOS_JSON)),
                            cursor.getString(cursor.getColumnIndex(MovieFavContract.MovieFavEntry.COLUMN_REVIEWS_JSON))

                    );

                    moviesFavorite.add(movie);
                } while (cursor.moveToNext());

            }
            showJsonDataView();
            resultMovies.clear();
            listToArray(moviesFavorite);
            setData(resultMovies);
        }else{

        }

    }




    public  void listToArray(List<MovieClass> listData) {
        //resultMovies= new ArrayList<MovieClass>();
        resultMovies.addAll(listData);
    }



    @Override
    public Loader<List<MovieClass>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<List<MovieClass>>(this) {


            /* This String array will hold and help cache our weather data */

            public List<MovieClass> mDataCache = null;
            @Override
            protected void onStartLoading() {
                if (mDataCache != null) {
                    deliverResult(mDataCache);
                } else {
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }


            }

            @Override
            public List<MovieClass> loadInBackground() {


                //URL searchUrl = NetworkUtils.buildUrlSearch(query.toString());
                flagLoadingData=true;
                try {

                    if(NetworkUtils.isOnline()){
                        String jsonWeatherResponse = NetworkUtils
                                .getResponseFromHttpUrl(SearchUrl);

                        List<MovieClass> moviesData = MoviesUtils
                                .getSimpleStringsFromJson(MainActivity.this, jsonWeatherResponse);

                        return moviesData;
                    }else{
                        return null;
                    }



                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void deliverResult(List<MovieClass> data) {
                mDataCache = data;
                super.deliverResult(data);
            }
        };

    }


    @Override
    public void onLoadFinished(Loader<List<MovieClass>> loader, List<MovieClass> data) {

        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (data != null && !data.equals("")) {

            showJsonDataView();
            listToArray(data);
            if (!adapterLoad) {
                setData(resultMovies);
            }
        } else {
            showErrorMessage();
        }
        flagLoadingData=false;
    }

    @Override
    public void onLoaderReset(Loader<List<MovieClass>> loader) {


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    public class QueryTask extends AsyncTask<URL, Void, List<MovieClass>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<MovieClass> doInBackground(URL... params) {
            URL searchUrl = params[0];
            String searchResults = null;
            if (params.length == 0) {
                return null;
            }

            try {

                if(NetworkUtils.isOnline()){
                    String jsonWeatherResponse = NetworkUtils
                            .getResponseFromHttpUrl(searchUrl);

                    List<MovieClass> moviesData = MoviesUtils
                            .getSimpleStringsFromJson(MainActivity.this, jsonWeatherResponse);

                    return moviesData;
                }else{
                    return null;
                }



            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(List<MovieClass> SearchResults) {

            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (SearchResults != null && !SearchResults.equals("")) {

                showJsonDataView();
                listToArray(SearchResults);
                if (!adapterLoad) {
                    setData(resultMovies);
                }
            } else {
                showErrorMessage();
            }
            flagLoadingData=false;
        }


    }

    public void setData(ArrayList<MovieClass> SearchResults){
        final GridAdapter gridAdapter;

        gridAdapter = new GridAdapter(MainActivity.this, SearchResults);
        adapterLoad=true;

        // Get a reference to the ListView, and attach this adapter to it.
        GridView gridView = (GridView) findViewById(R.id.movies_grid);
        gridView.setAdapter(gridAdapter);




        gridView.setOnItemClickListener(new GridView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Context context=MainActivity.this;
                Class destinationClass = DetailActivity.class;
                Intent intentToStartDetailActivity = new Intent(context, destinationClass);
                MovieClass movieSelected=resultMovies.get(position);
                intentToStartDetailActivity.putExtra("MovieObject" , (Parcelable) movieSelected);
                startActivity(intentToStartDetailActivity);

            }
        });



        gridView.setOnScrollListener(new GridView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int FirstVisPos = view.getFirstVisiblePosition();
                int posible=resultMovies.size();
                int myLastVisiblePos=view.getLastVisiblePosition();
                if (myLastVisiblePos>=(posible-5) && posible>=6 && !favoriteView) {
                    //seardata next page
                    if(!isLoading()){
                        page++;
                        searchQuery(query,page);
                        gridAdapter.notifyDataSetChanged();
                        //mSearchBoxEditText.setText(String.valueOf(resultMovies.size()));

                    }

                }
                //Log.i("scrollposition", "first-"+String.valueOf(FirstVisPos)+"last-"+String.valueOf(myLastVisiblePos));
            }
        });





    }
    public boolean isLoading(){
        if(flagLoadingData){
            return true;
        }else {
            return false;
        }
    }



}
