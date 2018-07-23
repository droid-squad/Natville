package me.jwill2385.natville;

import android.content.Context;
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
        Double latitude = HomeFragment.mLatLng.latitude;
        Double longitude = HomeFragment.mLatLng.longitude;

        listener.getTrails(latitude,longitude);
        myPlaces.addAll(MainActivity.places);
        Log.d("counter", " we have " + myPlaces.size());
        placeAdapter.notifyDataSetChanged();




    }

    public interface  OnItemSelectedListener{
         void getTrails(double lat, double lon);
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
}
