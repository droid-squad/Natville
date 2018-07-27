package me.jwill2385.natville;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

import org.w3c.dom.Text;

import me.jwill2385.natville.Models.Place;
import me.jwill2385.natville.R;

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
    public TextView tvRatingDetailed;
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
        tvRatingDetailed = view.findViewById(R.id.tvRatingDetailed);
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

        //setting all information from place object
        tvNameDetailed.setText(place.getName());
        tvSummaryDetailed.setText(place.getSummary());
        tvLocationDetailed.setText(place.getLocation());
        tvLengthDetailed.setText("Length: "+ Double.toString(place.getDistance()) +" miles");
        tvRatingDetailed.setText("Rating: "+ Double.toString(place.getRating()));
        tvDifficultyDetailed.setText("Difficulty: "+ place.getDifficulty());
        tvConditionStatDetailed.setText("Condition Status: "+ place.getConditionStatus());
        //if condition details empty show N/A, else show the details
        if(place.getConditionDetails().equals("") || place.getConditionDetails()==null) {
            tvConditionDetDetailed.setText("Condition Details: N/A");
        }
        else{tvConditionDetDetailed.setText("Condition Details: " + place.getConditionDetails());}
        tvConditionDateDetailed.setText("Conditions Last Updated On: "+ place.getConditionUpdated());
        tvAscentDetailed.setText("Ascent: "+ Double.toString(place.getAscent()) + " feet");
        tvDescentDetailed.setText("Descent: "+ Double.toString(place.getDescent()) +" feet");
        tvHighDetailed.setText("High: "+ Double.toString(place.getHigh()) +" feet");
        tvLowDetailed.setText("Low: "+ Double.toString(place.getLow()) +" feet");
        tvUrlDetailed.setText("For more information visit: " + place.getUrlDetails());

        Glide.with(getActivity()).load(place.getPictureLargeURL())
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(ivPicDetailed);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                context= getActivity();
                MainActivity mainActivity= (MainActivity) context;
                RecommendationsFragment fragmentRecommendation = new RecommendationsFragment();
                FragmentManager fragmentManager= mainActivity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.flContainer, fragmentRecommendation);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

    }
}
