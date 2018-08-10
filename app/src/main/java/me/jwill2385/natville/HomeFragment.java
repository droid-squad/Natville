package me.jwill2385.natville;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.jwill2385.natville.Models.Place;

public class HomeFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener,GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "HomeFragment";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 10f;
    public static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(new LatLng(-40,-168),new LatLng(71,136));

    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private AutoCompleteTextView etSearch;
    private ImageView ivGPS;
    private PlaceAutocompleteAdapter placeAutocompleteAdapter;
    private static GoogleApiClient mGoogleApiClient;
    private Button btnClear;

    public static LatLng mLatLng;
    public static ArrayList<Place> mPlaces;
    public MainActivityListener listener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        //On Start requests the users permission to use the location services
        super.onCreate(savedInstanceState);
        getLocationPermission();
        mPlaces = new ArrayList<>();
        mGoogleApiClient = new GoogleApiClient
                .Builder(getContext())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage((FragmentActivity) getContext(),this)
                .build();

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
        etSearch = (AutoCompleteTextView) view.findViewById(R.id.etSearch);
        btnClear = (Button) view.findViewById(R.id.btnClear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etSearch.getText().clear();
                mMap.clear();
            }
        });
        ivGPS = (ImageView) view.findViewById(R.id.iv_gps);
        ivGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDeviceLocation();
            }
        });
        initSearch();

    }

    @Override
    public void onPause() {
        super.onPause();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }

    private void getLocationPermission() {
        String[] permissions = {FINE_LOCATION, COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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

    private void initSearch() {
        placeAutocompleteAdapter = new PlaceAutocompleteAdapter(getActivity(),mGoogleApiClient,LAT_LNG_BOUNDS,null);
        etSearch.setAdapter(placeAutocompleteAdapter);

        etSearch.setSingleLine();
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {


                    //TODO: CREATE A METHOD HERE THAT DECIDES WHAT TO DO WHEN USER PRESSES ENTER
                    geoLocate();
                }
                return false;
            }
        });
        hideSoftKeyboard();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getActivity(), "Connection for autocomplete failed",Toast.LENGTH_SHORT).show();
    }

    private void geoLocate() {
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
            mMap.setOnInfoWindowClickListener(this);
        }

    }

    private void moveCamera(LatLng latLng, float zoom) {
        hideSoftKeyboard();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    private void getDeviceLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        try {
            if (mLocationPermissionsGranted) {
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Location currentLocation = (Location) task.getResult();
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM);
                            mLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                            //gets trails and populates their markers
                            new asyncTrails().execute();
                        } else {
                            Toast.makeText(getActivity(), "Current Location Unavailable", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        } catch (SecurityException e) {
            Log.e(TAG, "Security exception in get device location method:" + e.getMessage());
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        //created a new instance of the fragment to change to and a new instance of a transaction
        DetailedViewFragment details = new DetailedViewFragment();

        //need to cast the context since in the adapter we're not directly accessing the main activity
        //this way we are and we can set a fragment manager
        FragmentManager fragmentManager= getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //pass the object that is being clicked to the new fragment by getting that object then bundling it and serializing it
        Bundle bundle = new Bundle();
        Place clickedPlace= (Place) marker.getTag();
        bundle.putSerializable("clicked_place", clickedPlace);
        details.setArguments(bundle);

        //replace the current fragment with a new one, add the transaction to the back stack of transactions, and apply
        fragmentTransaction.replace(R.id.flContainer, details).commit();

    }

    @SuppressLint("StaticFieldLeak")
    private class asyncTrails extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            listener.getTrails(mLatLng.latitude, mLatLng.longitude,
                    MainActivity.maxDistance, MainActivity.maxResults / 10);

            //need to wait for places to finish updating in main activity
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mPlaces.addAll(MainActivity.places);

            for (int i = 0; i < mPlaces.size(); i++) {
                Place place = mPlaces.get(i);
                LatLng trailMark = new LatLng(place.getLatitude(), place.getLongitude());
                Marker tMark = mMap.addMarker(new MarkerOptions()
                        .position(trailMark)
                        .title(place.getName()));
                tMark.setTag(place);

                //Changes Color of the markers on the Map

                if(place.difficulty.equals("green") ){
                    tMark.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.green_tree));
                }
                else if(place.difficulty.equals("greenBlue")){
                    tMark.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.greenblue_tree));
                }
                else if(place.difficulty.equals("blue")){
                    tMark.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.blue_tree));
                }
                else if(place.difficulty.equals("blueBlack")){
                    tMark.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.blueblack_tree));
                }
                else if(place.difficulty.equals("black")){
                    tMark.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.black_tree));
                }
                else if(place.difficulty.equals("dblack")){
                    tMark.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.dblack_tree));
                }else {
                    //if rank is unknown/null
                    tMark.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.tree4));
                }
            }

        }

    }

    public interface MainActivityListener {
        void getTrails(double lat, double lon, double range, double results);

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

