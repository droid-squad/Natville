package me.jwill2385.natville;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import me.jwill2385.natville.Models.Place;



public class MainActivity extends AppCompatActivity implements HomeFragment.MainActivityListener, RecommendationsFragment.OnItemSelectedListener {


    BottomNavigationView bottomNavigationView;
    final FragmentManager fragmentManager = getSupportFragmentManager();
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private final static String TAG = MainActivity.class.getSimpleName();
    //base URl for API
    public final static String API_BASE_URL = "https://www.hikingproject.com/data/get-trails?";
    // parameter name for API key
    public final static String API_KEY_PRAM = "key";
    //the API key -TODO move to secret location
    public final static String API_KEY = "200315482-a80ef1dd23c559d634a1b00537914ce8";
    public final static double maxDistance = 200;
    public  final static double maxResults = 500;
    public static ArrayList<Place> places;




    // instance fields
    AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        client = new SyncHttpClient();

        final HomeFragment fragmentHome = new HomeFragment();
        final SearchFragment fragmentSearch = new SearchFragment();
        final ProfileFragment fragmentProfile = new ProfileFragment();
        final RecommendationsFragment fragmentRecommendation = new RecommendationsFragment();

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        // initialize list of Places
        places = new ArrayList<>();
        //handle navigation selection
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.ic_home:
                                if(isServicesOK()){
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.flContainer, fragmentHome).commit();
                                }
                                return true;
                            case R.id.ic_search:
                                FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction();
                                fragmentTransaction2.replace(R.id.flContainer, fragmentSearch).commit();
                                return true;
                            case R.id.ic_profile:
                                FragmentTransaction fragmentTransaction3 = fragmentManager.beginTransaction();
                                fragmentTransaction3.replace(R.id.flContainer, fragmentProfile).commit();
                                return true;

                            case R.id.ic_recommendations:
                                FragmentTransaction fragmentTransaction4 = fragmentManager.beginTransaction();
                                fragmentTransaction4.replace(R.id.flContainer, fragmentRecommendation).commit();
                                return true;
                        }
                        return false;
                    }
                });

        //starts user on home screen
        bottomNavigationView.setSelectedItemId(R.id.ic_home);
    }

    public boolean isServicesOK(){ //checks google play services
        Log.d(TAG, "isServicesOK: checking google services version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);
        if(available == ConnectionResult.SUCCESS){
            //everything works user can make requests
            Log.d(TAG, "IsServicesOK: Google Play Services is working");
            return true;
        }else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //resolvable error
            Log.d(TAG, "isServiceOK: an error occured but it can be fixed");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this,available,ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(MainActivity.this, "You cannnot make requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    //get the trails from the API. pass in current latitude and longitude locations
    public void getTrails(double lat, double lon){
        RequestParams params = new RequestParams();
        /*these are all parameters in request
        latitude value
        longitude value
        maxDistance value
        then API key

         */
        //hardcoding for testing
        //automatically sets range to show 10 places unless you change maxResults caps at 500
        //maxDistance starts at 30miles and caps at 200 miles
        params.put("lat",lat);
        params.put("lon", lon);
        params.put("maxDistance", maxDistance);
        params.put("maxResults", maxResults / 10);
        params.put(API_KEY_PRAM, API_KEY);


        // execute get request expecting a JSONObject response
        client.get(API_BASE_URL, params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // get result places
                try {
                    JSONArray trails = response.getJSONArray("trails");
                    //iterate through result set
                    places.clear();
                    for (int i= 0; i < trails.length(); i++){
                        Place p = new Place(trails.getJSONObject(i));
                        places.add(p); // add each place (p) to places array
                        Log.d("Location "+ i , p.getName());
                        
                    }

                    Log.i(TAG, String.format("loaded %s Trails", trails.length()));
                } catch (JSONException e) {
                    logError("failed to parse Trail list", e, true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed getting Trails", throwable, true);
            }
        });


    }

    @Override
    public void getPlaces(){
        HomeFragment.mPlaces = places;
    }

    private void logError(String message, Throwable error, boolean alertUser){
        // always log the error
        Log.e(TAG, message, error);
        //alert the user to avoid silent errors
        if(alertUser){
            // show a long toast with the error message
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

        }

    }
}
