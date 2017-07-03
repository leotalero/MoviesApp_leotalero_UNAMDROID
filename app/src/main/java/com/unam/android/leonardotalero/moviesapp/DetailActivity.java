package com.unam.android.leonardotalero.moviesapp;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import com.unam.android.leonardotalero.moviesapp.data.MovieFavContract;
import com.unam.android.leonardotalero.moviesapp.utilities.MoviesUtils;
import com.unam.android.leonardotalero.moviesapp.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements
        VideoFragment.OnListFragmentInteractionListener,ReviewFragment.OnListFragmentInteractionListener,
        LoaderManager.LoaderCallbacks<Cursor> {


    private MovieClass mMovie;
    @BindView(R.id.title) TextView mtitle_original;
    @BindView(R.id.movie_art) ImageView mImage;
    @BindView(R.id.movie_over) TextView mOverview;
    @BindView(R.id.release_date) TextView mRelease;
    @BindView(R.id.vote_ave) TextView mVote;
    @BindView(R.id.popularity) TextView mPopularity;
    @BindView(R.id.movie_id) TextView mId;
    private List<Video> mVideos=new ArrayList<Video>();
    private List<Review> mReview=new ArrayList<Review>();
    private int mIdmovie;
    private VideoRecyclerViewAdapter adapter;
    private ArrayList<Video> resultVideos;
    private ArrayList<Review> resultReviews;
    @BindView(R.id.myToggleButton) ToggleButton mFavoritebutton;
    private static final int CURSOR_LOADER_ID = 7;
    private Cursor mDetailCursor;
    private boolean isInFavorites =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        mtitle_original=(TextView)findViewById(R.id.title);
        mImage=(ImageView)findViewById(R.id.movie_art);
        mOverview=(TextView)findViewById(R.id.movie_over);
        mRelease=(TextView)findViewById(R.id.release_date);
        mVote=(TextView)findViewById(R.id.vote_ave);
        mPopularity=(TextView)findViewById(R.id.popularity);
        mId=(TextView)findViewById(R.id.movie_id);
        mFavoritebutton=(ToggleButton) findViewById(R.id.myToggleButton);

        Intent intentThatStartedThisActivity = getIntent();

        resultVideos=new ArrayList<Video>();
        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("MovieObject")) {
                mMovie = intentThatStartedThisActivity.getExtras().getParcelable("MovieObject");
                fillData(mMovie);
            }
        }

        if(savedInstanceState == null || !savedInstanceState.containsKey("resultVideos") ) {
            resultVideos=new ArrayList<Video>();
            // query="movie/popular";
            //searchQuery(query,page);
            if(mMovie.mvideos==null || mMovie.mvideos.isEmpty()){
                URL  UrlVideos = NetworkUtils.buildUrlVideos(mMovie.mid);
                new VideosTask().execute(UrlVideos);
            }else{
                try {

                    setVideoList(MoviesUtils.getListVideosFromJson(this,mMovie.mvideos));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
        else {
            resultVideos = savedInstanceState.getParcelableArrayList("resultVideos");
            videoFragmentLoad();

        }
        if(savedInstanceState == null || !savedInstanceState.containsKey("resultReviews") ) {
            resultReviews=new ArrayList<Review>();
            // query="movie/popular";
            //searchQuery(query,page);
            //URL UrlReviews = NetworkUtils.buildUrlReviews(mMovie.mid);
            //new ReviewTask().execute(UrlReviews);
            if(mMovie.mreviews  ==null ||mMovie.mreviews.isEmpty()){
                URL UrlReviews = NetworkUtils.buildUrlReviews(mMovie.mid);
                new ReviewTask().execute(UrlReviews);
            }else{
                try {

                    setReviewList(MoviesUtils.getListReviewsFromJson(this,mMovie.mreviews));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            resultReviews = savedInstanceState.getParcelableArrayList("resultReviews");
            reviewFragmentLoad();
        }

        if(savedInstanceState == null || !savedInstanceState.containsKey("isInFavorites") ) {
            isInFavorites=false;
        }
        else {
            isInFavorites = savedInstanceState.getBoolean("isInFavorites");
        }


        Bundle args=new Bundle();
        getLoaderManager().initLoader(CURSOR_LOADER_ID,args,DetailActivity.this);



        mFavoritebutton.setChecked(false);
        mFavoritebutton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_grey));
        mFavoritebutton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mFavoritebutton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_yellow));
                   if(!isInFavorites){
                       saveInFavorites(mMovie, mVideos, mReview);
                   }

                }else{
                    if(isInFavorites){
                        deleteInFavorites(mMovie, mVideos, mReview);
                    }

                     mFavoritebutton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_grey));
                }

            }
        });



    }



    private void checkIfFavorite() {

        String stringId = Integer.toString(mMovie.mid);
        Uri uri = MovieFavContract.MovieFavEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(stringId).build();

        //String[] projection={MovieFavContract.MovieFavEntry.COLUMN_ID_MOVIE};
        Cursor cursor =getContentResolver().query(uri,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                String uid= cursor.getString(0);

            } while(cursor.moveToNext());
            mFavoritebutton.setChecked(true);
            mFavoritebutton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_yellow));

        }
        cursor.close();
    }

    private void deleteInFavorites(MovieClass mMovie, List<Video> mVideos, List<Review> mReview) {

        isInFavorites=false;
        String stringId = Integer.toString(mMovie.mid);
        Uri uri = MovieFavContract.MovieFavEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(stringId).build();

        // COMPLETED (2) Delete a single row of data using a ContentResolver
       getContentResolver().delete(uri, null, null);
        if(uri != null) {
            Toast.makeText(getBaseContext(), R.string.del_fav, Toast.LENGTH_LONG).show();
        }
        // COMPLETED (3) Restart the loader to re-query for all tasks after a deletion
        //getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, MainActivity.this);

    }

    private void saveInFavorites(MovieClass mMovie, List<Video> mVideos, List<Review> mReview) {


        ContentValues contentValues = new ContentValues();

        contentValues.put(MovieFavContract.MovieFavEntry.COLUMN_ID_MOVIE, mMovie.mid);
        contentValues.put(MovieFavContract.MovieFavEntry.COLUMN_TITLE, mMovie.mtitle);
        contentValues.put(MovieFavContract.MovieFavEntry.COLUMN_DESCRIPTION, mMovie.moverview);
        contentValues.put(MovieFavContract.MovieFavEntry.COLUMN_TIMESTAMP_RELEASE_DATE, mMovie.mrelease_date);
        contentValues.put(MovieFavContract.MovieFavEntry.COLUMN_VOTE_AVERAGE, mMovie.mvote_average);
        contentValues.put(MovieFavContract.MovieFavEntry.COLUMN_VOTE_AVERAGE, mMovie.mvote_average);
        contentValues.put(MovieFavContract.MovieFavEntry.COLUMN_POSTER, mMovie.mimage);
        contentValues.put(MovieFavContract.MovieFavEntry.COLUMN_TITLE_ORIGINAL, mMovie.moriginal_title);
        contentValues.put(MovieFavContract.MovieFavEntry.COLUMN_POPULARITY, mMovie.mpopularity);
        if(mVideos!=null && !mVideos.isEmpty()){
            contentValues.put(MovieFavContract.MovieFavEntry.COLUMN_VIDEOS_JSON, mVideos.get(0).mLink);
        }
        if(mReview!=null && !mReview.isEmpty()){
            contentValues.put(MovieFavContract.MovieFavEntry.COLUMN_REVIEWS_JSON, mReview.get(0).marray);
        }

        // Insert the content values via a ContentResolver
        Uri uri = getContentResolver().insert(MovieFavContract.MovieFavEntry.CONTENT_URI, contentValues);

        // Display the URI that's returned with a Toast
        // [Hint] Don't forget to call finish() to return to MainActivity after this insert is complete
        if(uri != null) {
            isInFavorites =true;
            Toast.makeText(getBaseContext(),  R.string.save_fav, Toast.LENGTH_LONG).show();
        }
    }

    private void fillData(MovieClass mMovie) {
        mtitle_original.setText(mMovie.mtitle.toString());
        mOverview.setText(mMovie.moverview.toString());
        mRelease.setText("Release Date:"+mMovie.mrelease_date.toString());
        mVote.setText("Vote Average: "+ mMovie.mvote_average);
        mPopularity.setText("Popularity: "+ mMovie.mpopularity);
        mId.setText(String.valueOf(mMovie.mid));

        MoviesUtils.setImagePicasso(mMovie.mimage,this,mImage);
        if(isInFavorites){
            mFavoritebutton.setChecked(true);
            mFavoritebutton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_yellow));

        }
    }


    @Override
    public void onListFragmentInteraction(Video item) {

    }
    @Override
    public void onListFragmentInteraction(Review item) {

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection = null;
        String [] selectionArgs = null;

            selection = MovieFavContract.MovieFavEntry.COLUMN_ID_MOVIE;
            //selectionArgs = new String[]{String.valueOf(mMovie.mid)};
        String stringId = Integer.toString(mMovie.mid);
        Uri uri = MovieFavContract.MovieFavEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(stringId).build();

        return new CursorLoader(this,
                uri,
                null,
                selection,
                selectionArgs,
                null);


    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Boolean cursorHasValidData = false;
        if (data != null && data.moveToFirst()) {
            /* We have valid data, continue on to bind the data to the UI */
            cursorHasValidData = true;

        }

        if (!cursorHasValidData) {
            /* No data to display, simply return and do nothing */

            return;
        }

        data.moveToFirst();
        DatabaseUtils.dumpCursor(data);
        //mImageView.setImageResource(mDetailCursor.getInt(3));
        //mTextView.setText(mDetailCursor.getString(2));
        // set Uri to be displayed
        //mUriText.setText(mUri.toString());
        MovieClass movie=new MovieClass(data.getInt(data.getColumnIndex(MovieFavContract.MovieFavEntry.COLUMN_ID_MOVIE))
                ,data.getString(data.getColumnIndex(MovieFavContract.MovieFavEntry.COLUMN_TITLE_ORIGINAL))
                ,data.getString(data.getColumnIndex(MovieFavContract.MovieFavEntry.COLUMN_TIMESTAMP_RELEASE_DATE))
                ,data.getString(data.getColumnIndex(MovieFavContract.MovieFavEntry.COLUMN_POSTER))
                ,data.getDouble(data.getColumnIndex(MovieFavContract.MovieFavEntry.COLUMN_VOTE_AVERAGE))
                ,data.getString(data.getColumnIndex(MovieFavContract.MovieFavEntry.COLUMN_DESCRIPTION))
                ,data.getString(data.getColumnIndex(MovieFavContract.MovieFavEntry.COLUMN_TITLE_ORIGINAL))
                ,data.getDouble(data.getColumnIndex(MovieFavContract.MovieFavEntry.COLUMN_POPULARITY))
                ,data.getString(data.getColumnIndex(MovieFavContract.MovieFavEntry.COLUMN_VIDEOS_JSON))
                ,data.getString(data.getColumnIndex(MovieFavContract.MovieFavEntry.COLUMN_REVIEWS_JSON))
        );

            isInFavorites =true;


        fillData(movie);

    }



    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }










    /////////////////Load Videos
    public class VideosTask extends AsyncTask<URL, Void, List<Video>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Video> doInBackground(URL... params) {
            URL searchUrl = params[0];
            String searchResults = null;
            if (params.length == 0) {
                return null;
            }

            try {

                if(NetworkUtils.isOnline()){
                    String jsonWeatherResponse = NetworkUtils
                            .getResponseFromHttpUrl(searchUrl);

                    List<Video> videos = MoviesUtils
                            .getListVideosFromJson(DetailActivity.this, jsonWeatherResponse);

                    return videos;
                }else{
                    return null;
                }



            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }


        @Override
        protected void onPostExecute(List<Video> videos) {
            super.onPostExecute(videos);
            if (videos != null && !videos.equals("")) {

                setVideoList(videos);

            } else {
                showErrorMessage();
            }
        }
    }

    private void setVideoList(List<Video> videos) {

        for(Video video:videos) {

            mVideos.add(video);
        }
        listToArray(mVideos);
        //adapter.notifyDataSetChanged();
       videoFragmentLoad();



    }

    private void videoFragmentLoad() {
        VideoFragment videoFragment = VideoFragment.newInstance(1, mMovie.mid,resultVideos);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_videoList, videoFragment).commit();
    }

    private void showErrorMessage() {
        Toast.makeText(this,
                "Error loading data", Toast.LENGTH_LONG).show();
    }

    public  void listToArray(List<Video> listVideos) {
        //resultMovies= new ArrayList<MovieClass>();
        resultVideos.addAll(listVideos);
    }

///////////////////////load Reviews////////

    public class ReviewTask extends AsyncTask<URL, Void, List<Review>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Review> doInBackground(URL... params) {
            URL searchUrl = params[0];
            String searchResults = null;
            if (params.length == 0) {
                return null;
            }

            try {

                if(NetworkUtils.isOnline()){
                    String jsonWeatherResponse = NetworkUtils
                            .getResponseFromHttpUrl(searchUrl);

                    List<Review> reviews = MoviesUtils
                            .getListReviewsFromJson(DetailActivity.this, jsonWeatherResponse);

                    return reviews;
                }else{
                    return null;
                }



            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }


        @Override
        protected void onPostExecute(List<Review> reviews) {
            super.onPostExecute(reviews);
            if (reviews != null && !reviews.equals("")) {

                setReviewList(reviews);

            } else {
                showErrorMessage();
            }
        }
    }

    private void setReviewList(List<Review> reviews) {

        for(Review review:reviews) {

            mReview.add(review);
        }
        listToArrayReview(mReview);
        reviewFragmentLoad();
       // adapter.notifyDataSetChanged();

    }


    public  void listToArrayReview(List<Review> listReview) {
        //resultMovies= new ArrayList<MovieClass>();
        resultReviews.addAll(listReview);
    }
    private void reviewFragmentLoad() {
        ReviewFragment reviewFragment = ReviewFragment.newInstance(1, mMovie.mid,resultReviews);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_reviewList, reviewFragment ).commit();
    }



    //////////////////


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("resultVideos", resultVideos);
        outState.putParcelableArrayList("resultReviews", resultReviews);
        outState.putBoolean("isInFavorites", isInFavorites);

        super.onSaveInstanceState(outState);
    }

}
