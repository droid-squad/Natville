package me.jwill2385.natville;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import me.jwill2385.natville.Models.Place;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder>{

    ArrayList<Place> mPlaces;
    Context context;
    Place place;

    //will fill Adapter Place array with place array inserted
    public PlaceAdapter(ArrayList<Place> places){
        this.mPlaces = places;
    }

    //creates and inflates a new view
    @NonNull
    @Override
    public PlaceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //get the context and create the inflator
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // create the view using the item_place layout
        View placeView = inflater.inflate(R.layout.item_place, parent, false);
        // return a new ViewHolder
        return new ViewHolder(placeView);
    }

    // creates and inflates a new view
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        //get the location data at the specific place
        place = mPlaces.get(position);
        //populate the view
        holder.tvPlaceName.setText(place.getName());
        holder.tvPlaceLocation.setText(place.getLocation());
        holder.rbPlaceRating.setRating((float) place.getRating());
        holder.tvPlaceSummary.setText(place.getSummary());
        holder.tvPlaceDistance.setText(Double.toString(place.getDistance())+ " miles");
        Glide.with(context).load(place.getPictureSmallURL())
                .apply(new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(50)))
                .into(holder.ivPlacePic);
    }

    @Override
    public int getItemCount() {
        return mPlaces.size(); //how many items are in Adapter
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //initiate variables
        public ImageView ivPlacePic;
        public TextView tvPlaceName;
        public TextView tvPlaceLocation;
        public TextView tvPlaceSummary;
        public TextView tvPlaceDistance;
        public RatingBar rbPlaceRating;


        public ViewHolder(View itemView) {
            super(itemView);
            //perform findViewById lookups

            //reference id of each view from item_place.xml and cast to type
            ivPlacePic = itemView.findViewById(R.id.ivPlacePic);
            tvPlaceName = itemView.findViewById(R.id.tvPlaceName);
            tvPlaceLocation= itemView.findViewById(R.id.tvPlaceLocation);
            tvPlaceSummary = itemView.findViewById(R.id.tvPlaceSummary);
            tvPlaceDistance = itemView.findViewById(R.id.tvPlaceDistance);
            rbPlaceRating = itemView.findViewById(R.id.tvPlaceRating);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //created a new instance of the fragment to change to and a new instance of a transaction
            DetailedViewFragment details = new DetailedViewFragment();

            //need to cast the context since in the adapter we're not directly accessing the main activity
            //this way we are and we can set a fragment manager
            MainActivity mainActivity= (MainActivity) context;
            FragmentManager fragmentManager= mainActivity.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            //pass the object that is being clicked to the new fragment by getting that object then bundling it and serializing it
            Bundle bundle = new Bundle();
            Place clickedPlace= mPlaces.get(getAdapterPosition());
            bundle.putSerializable("clicked_place", clickedPlace);
            details.setArguments(bundle);

            //replace the current fragment with a new one, add the transaction to the back stack of transactions, and apply
            fragmentTransaction.replace(R.id.flContainer, details);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }
    }

    // Clean all elements of the recycler
    public void clear(){
        mPlaces.clear();
        notifyDataSetChanged();
    }


}
