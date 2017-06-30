package com.unam.android.leonardotalero.moviesapp;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.unam.android.leonardotalero.moviesapp.VideoFragment.OnListFragmentInteractionListener;


import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Video} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class VideoRecyclerViewAdapter extends RecyclerView.Adapter<VideoRecyclerViewAdapter.ViewHolder> {

    private final List<Video> mValues;
    private final OnListFragmentInteractionListener mListener;
    private  Context context;

    public VideoRecyclerViewAdapter(List<Video> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_video, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).mId);
        holder.mResultNameView.setText(mValues.get(position).mName);
        String detail=mValues.get(position).mSite+"-"+mValues.get(position).mSize+"-"+mValues.get(position).mType;
        holder.mResultDetailView.setText(detail);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    showVideo(context,mValues.get(position).mLink,mValues.get(position).mKey);
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    private void showVideo(Context context, String mLink, String key) {

        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + key));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mResultNameView;
        public final TextView mResultDetailView;
        public Video mItem;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mResultNameView = (TextView) view.findViewById(R.id.result_name);
            mResultDetailView = (TextView) view.findViewById(R.id.result_second_line);
            context = itemView.getContext();
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mResultNameView.getText() + "'";
        }
    }



}
