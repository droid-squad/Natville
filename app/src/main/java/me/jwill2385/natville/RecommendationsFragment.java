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
import android.widget.Button;

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

        Button btnRatingSort = view.findViewById(R.id.btnRatingSort);
        Button btnDistanceSort = view.findViewById(R.id.btnDistanceSort);


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

        btnRatingSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                filterByRating();
            }
        });

        btnDistanceSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterByDistance();
            }
        });


    }

    // organizes list from longest distance to shortest
    private void filterByDistance() {
        for (int i = 0; i < myPlaces.size(); i++) {
            for (int j = i + 1; j < myPlaces.size(); j++) {
                if (myPlaces.get(i).getDistance() < myPlaces.get(j).getDistance()) {
                    Place temp = myPlaces.get(i);
                    myPlaces.set(i, myPlaces.get(j));
                    myPlaces.set(j, temp);
                }
            }
        }
        placeAdapter.notifyDataSetChanged();
    }

    // organizes list from highest rating (5) to smallest
    private void filterByRating() {
        for (int i = 0; i < myPlaces.size(); i++) {
            for (int j = i + 1; j < myPlaces.size(); j++) {
                if (myPlaces.get(i).getRating() < myPlaces.get(j).getRating()) {
                    Place temp = myPlaces.get(i);
                    myPlaces.set(i, myPlaces.get(j));
                    myPlaces.set(j, temp);
                }
            }
        }
        placeAdapter.notifyDataSetChanged();

    }


    public interface OnItemSelectedListener {
        void getTrails(double lat, double lon, double range, double results);
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
            listener.getTrails(latitude, longitude,
                    MainActivity.maxDistance, MainActivity.maxResults / 10);

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
