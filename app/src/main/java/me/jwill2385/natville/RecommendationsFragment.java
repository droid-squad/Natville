package me.jwill2385.natville;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import me.jwill2385.natville.Models.Place;


public class RecommendationsFragment extends Fragment {

    RecyclerView rvRecommendations;
    // adapter wired to recycler view
    public ArrayList<Place> myPlaces;
    PlaceAdapter placeAdapter;
    OnItemSelectedListener listener;
    Double latitude;
    Double longitude;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recommendations, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        rvRecommendations = (RecyclerView) view.findViewById(R.id.rvRecommendations);
        // initialize arraylist (data source)
        myPlaces = new ArrayList<>();
        // construct the adapter from this data source
        placeAdapter = new PlaceAdapter(myPlaces);
        // recyclerView setup (layout manager, use adapter)
        rvRecommendations.setLayoutManager(new LinearLayoutManager(view.getContext()));
        // set the adapter
        rvRecommendations.setAdapter(placeAdapter);
        latitude = HomeFragment.mLatLng.latitude;
        longitude = HomeFragment.mLatLng.longitude;

        // we have to call getTrails aSynchronously such that we make sure it completes before updating adapter
        new asyncTrailsR().execute();



    }

    public interface  OnItemSelectedListener{
         void getTrails( double lat,  double lon,  double results);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnItemSelectedListener) {
            listener = (OnItemSelectedListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement RecommendationsFragment.OnItemSelectedListener");
        }
    }


    @SuppressLint("StaticFieldLeak")
    private class asyncTrailsR extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            listener.getTrails(latitude, longitude, MainActivity.maxResults / 10 );
            //need to wait for places to finish updating in main activity
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            myPlaces.addAll(MainActivity.places);
            Log.d("counter", " we have " + myPlaces.size());
            placeAdapter.notifyDataSetChanged();

        }

    }
}
