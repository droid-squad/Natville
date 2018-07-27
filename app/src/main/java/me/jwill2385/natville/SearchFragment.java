package me.jwill2385.natville;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;

import me.jwill2385.natville.Models.Place;


public class SearchFragment extends Fragment {
    private static final String TAG = "SearchFragment";
    private EditText etSearchTab;
    boolean test = false;


    OnMainActivitySelectedListener listener;
    public ArrayList<Place> sPlaces;
    public HashMap<String, ArrayList<Double>> placeMap;
    HashMap<String, ArrayList<Double>> placeMap2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sPlaces = new ArrayList<>();
        placeMap = new HashMap<>();
        placeMap2 = new HashMap<>();

        // iterate through lat lon of seattle

        for (int l = 46; l <= 49; l++) {
            for (int ln = -123; ln <= -119; ln++) {
                LatLng spot = new LatLng(l, ln);
                // this will get you trails throughout whole state of washington
                new asyncTrailsR().execute(spot);
                Log.d(TAG, "Size: " + placeMap.size());

            }
        }


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
        etSearchTab = (EditText) view.findViewById(R.id.etSearch2);
        initSearch();


    }

    private void initSearch() {
        etSearchTab.setSingleLine();
        etSearchTab.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {


                    //TODO: CREATE A METHOD HERE THAT DECIDES WHAT TO DO WHEN USER PRESSES ENTER
                    filterTrails();
                }
                return false;
            }
        });
    }

    private void filterTrails() {
        Log.d(TAG, "filterTrails: filtering");
        String searched = etSearchTab.getText().toString();
        Toast.makeText(getActivity(), searched, Toast.LENGTH_SHORT).show();
    }


    public interface OnMainActivitySelectedListener {
        public void getTrails(double lat, double lon, double results);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMainActivitySelectedListener) {
            listener = (OnMainActivitySelectedListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " Must implement SearchFragment.OnMainActivitySelectedListener");
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class asyncTrailsR extends AsyncTask<LatLng, Void, Void> {

        @Override
        protected Void doInBackground(LatLng... params) {
            LatLng currentLocation = params[0];
            listener.getTrails(currentLocation.latitude, currentLocation.longitude, MainActivity.maxResults / 10);
            //need to wait for places to finish updating in main activity
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            sPlaces.addAll(MainActivity.places);
            // Log.d("counter", " we have " + sPlaces.size());
            for (int i = 0; i < sPlaces.size(); i++) {
                if (placeMap.isEmpty()) {
                    // if map is  empty add the 1st place to map
                    ArrayList<Double> location = new ArrayList<>();
                    location.add(sPlaces.get(i).getLatitude());
                    location.add(sPlaces.get(i).getLongitude());
                    placeMap.put(sPlaces.get(i).getName(), location);
                } else {
                    //if not empty then check if it is not already in map and then add it
                    if (!placeMap.containsKey(sPlaces.get(i).getName())) {
                        if (!sPlaces.get(i).getName().contains(".")){
                            //if the name of place isn't already in map add the lat long to an array and add array to map
                            ArrayList<Double> location = new ArrayList<>();
                            location.add(sPlaces.get(i).getLatitude());
                            location.add(sPlaces.get(i).getLongitude());
                            placeMap.put(sPlaces.get(i).getName(), location);

                        }
                    }
                }
            }
            // map is populated so clear out array
            sPlaces.clear();
            Log.d("MAP", " Has " + placeMap.size());

            if (placeMap.size() >= 120) {

                Log.d(TAG, "size: " + placeMap.size());

                Log.d(TAG, "Map is complete");
                  MainActivity.fillMap(placeMap);


            }
        }

    }
}
