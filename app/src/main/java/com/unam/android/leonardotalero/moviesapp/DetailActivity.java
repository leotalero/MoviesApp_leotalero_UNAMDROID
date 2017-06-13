package com.unam.android.leonardotalero.moviesapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.unam.android.leonardotalero.moviesapp.utilities.MoviesUtils;

public class DetailActivity extends AppCompatActivity {

    private TextView mtitle_original;
    private ImageView mImage;
    private MovieClass mMovie;
    private TextView mOverview;
    private TextView mRelease;
    private TextView mVote;
    private TextView mPopularity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mtitle_original=(TextView)findViewById(R.id.title);
        mImage=(ImageView)findViewById(R.id.movie_art);
        mOverview=(TextView)findViewById(R.id.movie_over);
        mRelease=(TextView)findViewById(R.id.release_date);
        mVote=(TextView)findViewById(R.id.vote_ave);
        mPopularity=(TextView)findViewById(R.id.popularity);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("MovieObject")) {
                mMovie = (MovieClass) intentThatStartedThisActivity.getExtras().getParcelable("MovieObject");
                mtitle_original.setText(mMovie.mtitle.toString());
                MoviesUtils.setImagePicasso(mMovie.mimage,this,mImage);
                mOverview.setText(mMovie.moverview.toString());
                mRelease.setText("Release Date:"+mMovie.mrelease_date.toString());
                mVote.setText("Vote Average: "+ mMovie.mvote_average);
                mPopularity.setText("Popularity: "+ mMovie.mpopularity);
            }
        }
    }
}
