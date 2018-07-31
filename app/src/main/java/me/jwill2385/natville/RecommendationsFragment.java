package me.jwill2385.natville;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import me.jwill2385.natville.Models.Place;


public class RecommendationsFragment extends Fragment {

    RecyclerView rvRecommendations;
    public ArrayList<Place> myPlaces;
    // adapter wired to recycler view
    PlaceAdapter placeAdapter;
    OnItemSelectedListener listener;
    Double latitude;
    Double longitude;
    private NavigationView nv_Recommendations;
    private DrawerLayout dl_Recommendations;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recommendations, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //get id of DrawerLayout
        dl_Recommendations = (DrawerLayout) view.findViewById(R.id.dl_Recommendations);

        //this will prevent vertical sliding to access filter options
        dl_Recommendations.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        dl_Recommendations.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View view, float v) {

            }

            @Override
            public void onDrawerOpened(@NonNull View view) {

            }

            @Override
            public void onDrawerClosed(@NonNull View view) {

                dl_Recommendations.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }

            @Override
            public void onDrawerStateChanged(int i) {

            }
        });


        //function to control when you click on each item in navigation view
        nv_Recommendations = (NavigationView) view.findViewById(R.id.nv_Recommendations);
        nv_Recommendations.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                selectDrawerItem(item);
                dl_Recommendations.closeDrawers();
                return true;
            }
        });

        Button btnFilter = view.findViewById(R.id.btnFilter);


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

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //opens Navigation Drawer and allows for swipe to close
                dl_Recommendations.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                dl_Recommendations.openDrawer(GravityCompat.START);
            }
        });

    }


    public void selectDrawerItem(MenuItem item) {
        //this is where clicking on different filter options will occur
        switch (item.getItemId()) {
            case R.id.nav_rating:
                filterByRating();
                break;

            case R.id.nav_distance:
                filterByDistance();
                break;

            case R.id.nav_difficulty_green:
                filterByDifficulty("green");
                break;
            case R.id.nav_difficulty_blueGreen:
                filterByDifficulty("blueGreen");
                break;
            case R.id.nav_difficulty_blue:
                filterByDifficulty("blue");
                break;
            case R.id.nav_difficulty_blueBlack:
                filterByDifficulty("blueBlack");
                break;
            case R.id.nav_difficulty_black:
                filterByDifficulty("black");
                break;
            case R.id.nav_difficulty_doubleBlack:
                filterByDifficulty("doubleBlack");
                break;

            default:
                break;
        }
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

    /*
    this function searches through array of all places nearby you,
    finds locations with given dificulty and then shows you that result
     */
    private void filterByDifficulty(String level) {
        ArrayList<Place> filteredList = new ArrayList<>();

        switch (level) {
            case "green":
                for (Place p : myPlaces){
                    if(p.getDifficulty().toLowerCase().equals("green")){
                        filteredList.add(p);
                    }
                }
                filterResults(filteredList);
                break;
            case "blueGreen":
                for (Place p : myPlaces){
                    if(p.getDifficulty().toLowerCase().equals("bluegreen")){
                        filteredList.add(p);
                    }
                }
                filterResults(filteredList);
                break;
            case "blue":
                for (Place p : myPlaces){
                    if(p.getDifficulty().toLowerCase().equals("blue")){
                        filteredList.add(p);
                    }
                }
                filterResults(filteredList);
                break;
            case "blueBlack":
                for (Place p : myPlaces){
                    if(p.getDifficulty().toLowerCase().equals("blueblack")){
                        filteredList.add(p);
                    }
                }
                filterResults(filteredList);
                break;
            case "black":
                for (Place p : myPlaces){
                    if(p.getDifficulty().toLowerCase().equals("black")){
                        filteredList.add(p);
                    }
                }
                filterResults(filteredList);
                break;
            case "doubleBlack":
                for (Place p : myPlaces){
                    if(p.getDifficulty().toLowerCase().equals("dblack")){
                        filteredList.add(p);
                    }
                }
                filterResults(filteredList);
                break;
            default:
                break;
        }
    }

    private void filterResults(ArrayList<Place> filteredList) {
        if(filteredList.isEmpty()){
            // show original list not modified
            Toast.makeText(getActivity(),"No Places of that difficulty type are nearby", Toast.LENGTH_SHORT).show();
        }else {
            myPlaces.clear();
            placeAdapter.clear();
            myPlaces.addAll(filteredList);
            placeAdapter.notifyDataSetChanged();
        }

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
                    MainActivity.maxDistance, MainActivity.maxResults);

            //need to wait for places to finish updating in main activity
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            myPlaces.addAll(MainActivity.places);
            Log.d("counter", " we have " + myPlaces.size());
//            LocationMap locationMap = new LocationMap();
//            locationMap.setArray(myPlaces);
//            locationMap.saveInBackground(new SaveCallback() {
//                @Override
//                public void done(ParseException e) {
//                    if(e == null){
//                        Log.d("Save ", "Successfull");
//                    }else{
//                        e.printStackTrace();
//                    }
//                }
//            });
            placeAdapter.notifyDataSetChanged();

        }

    }
}
