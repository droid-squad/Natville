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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashMap;

import me.jwill2385.natville.Models.LocationMap;
import me.jwill2385.natville.Models.Place;


public class SearchFragment extends Fragment {
    private static final String TAG = "SearchFragment";
    private AutoCompleteTextView actvSearch;
    OnMainActivitySelectedListener listener;
    RecyclerView rvSearchBar;
    PlaceAdapter searchAdapter;
    public ArrayList<Place> sPlaces;
    public HashMap<String, ArrayList<Double>> placeMap;
    String searched;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialize arraylist (data source)
        sPlaces = new ArrayList<>();
        // Construct Adapter for this data source
        searchAdapter = new PlaceAdapter(sPlaces);
        placeMap = new HashMap<>();
        // specify which class to query
        ParseQuery<LocationMap> query = ParseQuery.getQuery(LocationMap.class);
        // Specify the object Id
        query.getInBackground("d1BfcOmfy5", new GetCallback<LocationMap>() {
            @Override
            public void done(LocationMap object, ParseException e) {
                if (e == null) {
                    // fill placeMap with map in parse
                    placeMap = object.getMap();
                    Log.d(TAG, "Size: " + placeMap.size());
                } else {
                    e.printStackTrace();
                }
            }
        });
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
        rvSearchBar = view.findViewById(R.id.rvSearchBar);

        // recyclerView setup (layout manager, use adapter)
        rvSearchBar.setLayoutManager(new LinearLayoutManager(view.getContext()));
        // set the adapter
        rvSearchBar.setAdapter(searchAdapter);
        actvSearch = (AutoCompleteTextView) view.findViewById(R.id.actvSearch);

        initSearch();





    }


    private void initSearch() {
//       Set<String> names = placeMap.keySet();
//        String[] nameArray = new String[1115] ;
//        nameArray = (String[]) names.toArray();
//
//        Arr



        actvSearch.setSingleLine();
        actvSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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
        searched = actvSearch.getText().toString().toLowerCase();
        if (placeMap.containsKey(searched)) {
            ArrayList<Double> location = placeMap.get(searched);
            LatLng spot = new LatLng(location.get(0), location.get(1));
            new asyncTrailsR().execute(spot);
            Toast.makeText(getActivity(), searched, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Error " + searched + " Not found", Toast.LENGTH_SHORT).show();
        }
    }


    public interface OnMainActivitySelectedListener {
        public void getTrails(double lat, double lon, double range, double results);

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
            listener.getTrails(currentLocation.latitude, currentLocation.longitude,
                    MainActivity.maxDistance / 20, MainActivity.maxResults / 50);
            //need to wait for places to finish updating in main activity
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            sPlaces.addAll(MainActivity.places);
            rearrangeArray();
            Log.d(TAG, "Size " + sPlaces.size());
            searchAdapter.notifyDataSetChanged();

        }

    }

    private void rearrangeArray() {
        int index = 0;
        if (sPlaces.get(index).getName().toLowerCase().equals(searched)) {
            //this means the place I want is already at top
            return;
        } else {
            for (int j = 0; j < sPlaces.size(); j++) {
                if (sPlaces.get(j).getName().toLowerCase().equals(searched)) {
                    index = j;
                }
            }
            Place temp = sPlaces.get(0);
            sPlaces.set(0, sPlaces.get(index));
            sPlaces.set(index, temp);


        }
    }
}
