package me.jwill2385.natville;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.jwill2385.natville.Models.Place;


///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link HomeFragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link HomeFragment#newInstance} factory method to
// * create an instance of this fragment.
// */

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "HomeFragment";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 10f;

    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private EditText etSearch;
    private ImageView ivGPS;

    public static LatLng mLatLng;
    public MainActivityListener listener;
    public static ArrayList<Place> mPlaces;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        //On Start requests the users permission to use the location services
        super.onCreate(savedInstanceState);
        getLocationPermission();
        mPlaces = new ArrayList<>();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etSearch = (EditText) view.findViewById(R.id.etSearch);
        ivGPS = (ImageView) view.findViewById(R.id.iv_gps);
        ivGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDeviceLocation();
            }
        });
        initSearch();

    }

    private void getLocationPermission() {
        String[] permissions = {FINE_LOCATION, COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Permission Granted map should init");
                mLocationPermissionsGranted = true;
                initMap();

            } else {
                requestPermissions(permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            requestPermissions(permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //once permission has been received, initiate the map
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            return;
                        }
                    }
                }
                mLocationPermissionsGranted = true;
                initMap();
            }
        }
    }

    private void initSearch(){
        etSearch.setSingleLine();
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                    || actionId == EditorInfo.IME_ACTION_DONE
                    || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                    || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){


                    //TODO: CREATE A METHOD HERE THAT DECIDES WHAT TO DO WHEN USER PRESSES ENTER
                    geoLocate();
                }
                return false;
            }
        });
        hideSoftKeyboard();
    }

    private void geoLocate(){
        Log.d(TAG, "geoLocate: geolocating");
        String searched = etSearch.getText().toString();
        Geocoder geocoder = new Geocoder(getActivity());
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searched,1);
        }catch (IOException e) {
            Log.e(TAG, "geoLocate: IOException: "+e.getMessage());
        }
        if(list.size()>0){
            Address address = list.get(0);
            moveCamera(new LatLng(address.getLatitude(),address.getLongitude()),DEFAULT_ZOOM);
            mLatLng = new LatLng(address.getLatitude(),address.getLongitude());
            new asyncTrails().execute();

        }else{
            Toast.makeText(getActivity(),"Location not found",Toast.LENGTH_SHORT);
        }
    }

    private void initMap() {
        //puts map fragment in container
        FragmentManager cfm = getChildFragmentManager();
        SupportMapFragment mapFragment = ((SupportMapFragment) cfm.findFragmentById(R.id.map_fragment));
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            cfm.beginTransaction().replace(R.id.map_fragment, mapFragment).commit();
        }
        //call the on map ready function
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //TODO Change this toast to something more related to the app
        Toast.makeText(getActivity(), "Map is ready", Toast.LENGTH_SHORT).show();
        mMap = googleMap;

        if (mLocationPermissionsGranted) {

            //Async Task Starts: gets client first
            getDeviceLocation();

            //if permission was denied or not given do not set location
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            //Waits until getDeviceLocation is done to apply, other UI settings available
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        }

    }

    private void moveCamera(LatLng latLng, float zoom) {
        hideSoftKeyboard();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting the devices current location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        try {
            if (mLocationPermissionsGranted) {
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: found location");
                            Location currentLocation = (Location) task.getResult();
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM);
                            mLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                            //gets trails and populates their markers
                            new asyncTrails().execute();
                        } else {
                            Log.d(TAG, "onComplete: current location unavailable");
                            Toast.makeText(getActivity(), "Current Location Unavailable", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        } catch (SecurityException e) {
            Log.e(TAG, "Security exception in get device location method:" + e.getMessage());
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class asyncTrails extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            listener.getTrails(mLatLng.latitude, mLatLng.longitude, MainActivity.maxResults / 10 );
            //need to wait for places to finish updating in main activity
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mPlaces.addAll(MainActivity.places);
            Log.d("getPlaces test line", "Size: " + mPlaces.size());
            for (int i = 0; i < mPlaces.size(); i++) {
                Place place = mPlaces.get(i);
                Log.d("OnComplete", "Trail: " + i + " is " + place.getName());
                LatLng trailMark = new LatLng(place.getLatitude(), place.getLongitude());
                mMap.addMarker(new MarkerOptions()
                        .position(trailMark)
                        .title(place.getName())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.tree4)));
            }
        }

    }

    public interface MainActivityListener {
        void getTrails( double lat,  double lon,  double results);

        void getPlaces();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivityListener) {
            listener = (MainActivityListener) context;

        } else {
            throw new ClassCastException(context.toString() + "must implement main activity listener");
        }
    }

    private void hideSoftKeyboard() {
       getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


}

