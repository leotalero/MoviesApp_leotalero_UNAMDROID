package com.unam.android.leonardotalero.moviesapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcelable;
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

import com.unam.android.leonardotalero.moviesapp.utilities.MoviesUtils;
import com.unam.android.leonardotalero.moviesapp.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

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





    }


    private void searchQuery(String query,int page) {
        //String query = mSearchBoxEditText.getText().toString();
        URL SearchUrl = NetworkUtils.buildUrl(query.toString(),page);
       // mUrlDisplayTextView.setText(SearchUrl.toString());
            flagLoadingData=true;
            new QueryTask().execute(SearchUrl);


    }

    private void searchQueryMovie(String query) {
        //String query = mSearchBoxEditText.getText().toString();
        URL SearchUrl = NetworkUtils.buildUrlSearch(query.toString());
        // mUrlDisplayTextView.setText(SearchUrl.toString());
        flagLoadingData=true;
        new QueryTask().execute(SearchUrl);


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
        //buildUrlSearch
        return super.onOptionsItemSelected(item);
    }

    public  void listToArray(List<MovieClass> listData) {
        //resultMovies= new ArrayList<MovieClass>();
        resultMovies.addAll(listData);
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
                if (myLastVisiblePos>=(posible-5)) {
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
