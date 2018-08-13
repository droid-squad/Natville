package me.jwill2385.natville;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.parse.ParseUser;

import java.util.ArrayList;

import me.jwill2385.natville.Models.Place;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailedViewFragment extends Fragment {

    private static final String PLACE = "place";

    //initializing the Place object received from any previous fragment
    Place place;
    Context context;

    //initializing all the views
    public ImageView ivPicDetailed;
    public TextView tvNameDetailed;
    public TextView tvSummaryDetailed;
    public TextView tvLocationDetailed;
    public TextView tvLengthDetailed;
    public TextView tvDifficultyDetailed;
    public TextView tvConditionStatDetailed;
    public TextView tvConditionDetDetailed;
    public TextView tvConditionDateDetailed;
    public TextView tvAscentDetailed;
    public TextView tvHighDetailed;
    public TextView tvDescentDetailed;
    public TextView tvLowDetailed;
    public TextView tvUrlDetailed;
    public ImageView ivBack;
    public ImageView ivCompleteTrail;
    public RatingBar rbRatingDetailed;

    public DetailedViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detailed_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Bundle bundle = getArguments();
        place = (Place) bundle.getSerializable("clicked_place");

        //connecting views to the layout
        ivPicDetailed = view.findViewById(R.id.ivPlaceDetailed);
        tvNameDetailed = view.findViewById(R.id.tvNameDetailed);
        tvSummaryDetailed = view.findViewById(R.id.tvSummaryDetailed);
        tvLocationDetailed = view.findViewById(R.id.tvLocationDetailed);
        tvLengthDetailed = view.findViewById(R.id.tvLengthDetailed);
        tvDifficultyDetailed = view.findViewById(R.id.tvDifficultyDetailed);
        tvConditionStatDetailed = view.findViewById(R.id.tvCondStatDetailed);
        tvConditionDetDetailed = view.findViewById(R.id.tvCondDetDetailed);
        tvConditionDateDetailed = view.findViewById(R.id.tvCondiUpDetailed);
        tvAscentDetailed = view.findViewById(R.id.tvAscentDetailed);
        tvHighDetailed = view.findViewById(R.id.tvHighDetailed);
        tvDescentDetailed = view.findViewById(R.id.tvDescentDetailed);
        tvLowDetailed = view.findViewById(R.id.tvLowDetailed);
        tvUrlDetailed = view.findViewById(R.id.tvUrlDetailed);
        ivBack = view.findViewById(R.id.ivBack);
        ivCompleteTrail = view.findViewById(R.id.ivComplete);
        rbRatingDetailed = view.findViewById(R.id.tvRatingDetailed);

        //setting all information from place object
        tvNameDetailed.setText("  " + place.getName());
        tvSummaryDetailed.setText(place.getSummary());
        tvLocationDetailed.setText(place.getLocation());
        tvLengthDetailed.setText( Double.toString(place.getDistance()) + " miles");
        rbRatingDetailed.setRating((float) place.getRating());
        tvDifficultyDetailed.setText("Difficulty: " + newDifficulty(place.getDifficulty()));
        tvConditionStatDetailed.setText("Condition Status: " + place.getConditionStatus());
        //if condition details empty show N/A, else show the details
        if (place.getConditionDetails().equals("") || place.getConditionDetails() == null) {
            tvConditionDetDetailed.setText("Condition Details: N/A");
        } else {
            tvConditionDetDetailed.setText("Condition Details: " + place.getConditionDetails());
        }
        tvConditionDateDetailed.setText("Conditions Last Updated On: " + place.getConditionUpdated());
        tvAscentDetailed.setText("Ascent: " + Double.toString(place.getAscent()) + " feet");
        tvDescentDetailed.setText("Descent: " + Double.toString(place.getDescent()) + " feet");
        tvHighDetailed.setText("High: " + Double.toString(place.getHigh()) + " feet");
        tvLowDetailed.setText("Low: " + Double.toString(place.getLow()) + " feet");
        tvUrlDetailed.setText("For more information visit: " + place.getUrlDetails());

        Glide.with(getActivity()).load(place.getPictureLargeURL()).apply(RequestOptions.centerCropTransform()).into(ivPicDetailed);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                context = getActivity();
                MainActivity mainActivity = (MainActivity) context;
                //get the id of the current selected screen from navigation view
                int currentId = mainActivity.bottomNavigationView.getSelectedItemId();
                if (currentId == R.id.ic_recommendations) {
                    // if you were on recommendations tab and looked at details then switch back

                    mainActivity.fragmentManager.popBackStackImmediate();
                }
                else if (currentId == R.id.ic_search){
                    // if you were on search tab and looked at details, then switch back
                    mainActivity.fragmentManager.popBackStackImmediate(); //gets last item on backstack

                }
                else if (currentId==R.id.ic_home){

                    // if you were on home tab and looked at details then switch back
                    mainActivity.bottomNavigationView.setSelectedItemId(R.id.ic_home);
                }

            }
        });

        ivCompleteTrail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser currentUser = ParseUser.getCurrentUser();
                ArrayList<ArrayList<String>> profArr= (ArrayList<ArrayList<String>>) currentUser.get("placesVisited");
                String parkName=place.getName();
                ivCompleteTrail.setImageResource(R.drawable.baseline_done_filled_black_24dp);
                //check if that park was already added to our array
                if(profArr==null){
                    addToUserList(profArr, place, currentUser);
                    Toast.makeText(getActivity(), "Hike added to Places Visited!", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(!inArray(profArr, parkName)){
                        addToUserList(profArr, place, currentUser);
                        Toast.makeText(getActivity(), "Hike added to Places Visited!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getActivity(), "Hike already added!", Toast.LENGTH_SHORT).show();
                    }
                }
                //if so make a toast telling the user it's already been counted
                //if not add the park to our array and update the legacy increasing it by the distance listed
            }
        });

    }

    //search function to check if park is already in array
    public boolean inArray(ArrayList<ArrayList<String>> profArr, String placeName){
        for(int i=0; i<profArr.size(); i++){
            ArrayList<String> hold = profArr.get(i);
            if(hold.contains(placeName)){
                return true;
            }
        }
        return false;
    }

    //create a function that adds the park to the parse server list of visited parks
    public void addToUserList(ArrayList<ArrayList<String>> profArr, Place place, ParseUser currentUser){
        double legacy= currentUser.getDouble("legacy");
        legacy+= new Double(place.getDistance());
        currentUser.put("legacy", legacy);

        ArrayList<String> currentPlace= new ArrayList<String>();
        currentPlace.add(place.getPictureSmallURL());
        currentPlace.add(place.getName());
        profArr.add(0, currentPlace);
        currentUser.put("placesVisited", profArr);

        currentUser.saveInBackground();
    }


    public String newDifficulty(String color){
        if(color.equals("green") ){
            return "Green (Easy)";
        }
        else if(color.equals("greenBlue")){
            return "Green-Blue (Moderate)";
        }
        else if(color.equals("blue")){
            return "Blue (Intermediate)";
        }
        else if(color.equals("blueBlack")){
            return "Blue-Black (Challenging)";
        }
        else if(color.equals("black")){
            return "Black (Difficult)";
        }
        else if(color.equals("dblack")){
            return "Double-Black (Extreme)";
        }else {
            //if rank is unknown/null
            return "Unknown";
        }
    }
}
