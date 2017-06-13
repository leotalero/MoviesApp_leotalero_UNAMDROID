package com.unam.android.leonardotalero.moviesapp;

/**
 * Created by leonardotalero on 6/11/17.
 */


import android.app.Activity;
import android.net.Network;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.unam.android.leonardotalero.moviesapp.utilities.NetworkUtils;

import java.net.URI;
import java.net.URL;
import java.util.List;

import static android.R.attr.onClick;
import static java.lang.System.load;

public class GridAdapter extends ArrayAdapter<MovieClass>  {
    private static final String LOG_TAG = GridAdapter.class.getSimpleName();

    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the List is the data we want
     * to populate into the lists
     *
     * @param context        The current context. Used to inflate the layout file.
     * @param MovieClass A List of MovieClass objects to display in a list
     */
    public GridAdapter(Activity context, List<MovieClass> MovieClass) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, MovieClass);
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    The AdapterView position that is requesting a view
     * @param convertView The recycled view to populate.
     *                    (search online for "android view recycling" to learn more)
     * @param parent The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Gets the MovieClass object from the ArrayAdapter at the appropriate position
        MovieClass movieClass = getItem(position);

        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.movie_item, parent, false);
        }

        ImageView iconView = (ImageView) convertView.findViewById(R.id.movie_image);
        TextView versionNameView = (TextView) convertView.findViewById(R.id.movie_text);
        String url_path = movieClass.mimage;
        URL url =NetworkUtils.buildUrlImage(url_path);
        Picasso.with(getContext()).load(url.toString())
                .placeholder(R.drawable.default_poster)
                .error(R.drawable.default_poster)
                .into(iconView);




        versionNameView.setText("Release date:"+movieClass.mrelease_date
                + " - " + movieClass.mtitle +" Vote average:"+movieClass.mvote_average+" Popularity: "+movieClass.mpopularity);

        return convertView;
    }





}

