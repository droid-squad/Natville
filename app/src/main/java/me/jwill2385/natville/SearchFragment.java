package me.jwill2385.natville;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import me.jwill2385.natville.Models.Place;


public class SearchFragment extends Fragment {

    HashMap<String, ArrayList<Double> > placeMap;
    OnMainActivitySelectedListener listener;
    public  ArrayList<Place> sPlaces;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // iterate through lat lon of seattle


        for(int l = 45; l <= 49; l ++){
            for (int ln = -123; ln <= -117; ln ++){
                listener.getTrails(l, ln);
                sPlaces.addAll(MainActivity.places);

                for( Place p : sPlaces){
                    if(!placeMap.containsKey(p.getName())){
                        //if the name of place isn't already in map add the lat long to an array and add array to map
                        ArrayList<Double> location = new ArrayList<>();
                        location.add(p.getLatitude());
                        location.add(p.getLongitude());
                        placeMap.put(p.getName(), location);

                    }
                }
                // map is populated so clear out array
                sPlaces.clear();
                Toast.makeText(getActivity(), "Map has " + placeMap.size(), Toast.LENGTH_LONG);
            }

        }


    }

    public interface OnMainActivitySelectedListener{
        public void getTrails(double lat, double lon);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnMainActivitySelectedListener){
            listener = (OnMainActivitySelectedListener) context;
        }else{
            throw new ClassCastException(context.toString()
                    + " Must implement SearchFragment.OnMainActivitySelectedListener");
        }
    }
}
