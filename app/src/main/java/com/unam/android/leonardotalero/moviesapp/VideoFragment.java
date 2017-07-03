package com.unam.android.leonardotalero.moviesapp;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.unam.android.leonardotalero.moviesapp.utilities.MoviesUtils;
import com.unam.android.leonardotalero.moviesapp.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class VideoFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_ID_MOVIE = "id_movie";

    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private List<Video> mVideos=new ArrayList<Video>();
    private int mIdmovie;
    private VideoRecyclerViewAdapter adapter;
    private ArrayList<Video> resultVideos=new ArrayList<Video>();
    private RecyclerView recyclerView;
    private Parcelable mListState;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public VideoFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static VideoFragment newInstance(int columnCount,int idMovie,ArrayList<Video> listado) {
        VideoFragment fragment = new VideoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putInt(ARG_ID_MOVIE, idMovie);
        args.putParcelableArrayList("listado", listado);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            mIdmovie=getArguments().getInt(ARG_ID_MOVIE);
            mVideos= getArguments().getParcelableArrayList("listado");
        }



    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            mIdmovie=getArguments().getInt(ARG_ID_MOVIE);
            mVideos= getArguments().getParcelableArrayList("listado");
        }

        if(savedInstanceState == null || !savedInstanceState.containsKey("resultVideo") ) {
               // URL  UrlVideos = NetworkUtils.buildUrlVideos(mIdmovie);
               // new VideosTask().execute(UrlVideos);

        }else{

           // resultVideos = savedInstanceState.getParcelableArrayList("resultVideo");
           // mVideos.addAll(resultVideos);
             mListState = savedInstanceState.getParcelable("recyclerViewState");
            // getting recyclerview items
            resultVideos = savedInstanceState.getParcelableArrayList("resultVideo");

            // Restoring adapter items
            // Restoring recycler view position
           // recyclerView.getLayoutManager().onRestoreInstanceState(mListState);
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_video_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
             recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            adapter= new VideoRecyclerViewAdapter(
                    mVideos, mListener);
            resultVideos.addAll(mVideos);
            recyclerView.setAdapter(adapter);

            if(mListState!=null){
               // recyclerView.getLayoutManager().onRestoreInstanceState(mListState);
            }

        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Video item);
    }












    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("resultVideo", resultVideos);
        Parcelable listState = recyclerView.getLayoutManager().onSaveInstanceState();
        // putting recyclerview position
        outState.putParcelable("recyclerViewState", listState);

        super.onSaveInstanceState(outState);
    }


}
