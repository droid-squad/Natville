package me.jwill2385.natville;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import me.jwill2385.natville.Models.Place;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder>{

    ArrayList<Place> mPlaces;
    Context context;

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
        Place place = mPlaces.get(position);
        //populate the view
        holder.tvPlaceName.setText(place.getName());
        holder.tvPlaceLocation.setText(place.getLocation());
        holder.tvPlaceRating.setText(String.format("%f", place.getRating()));
        holder.tvPlaceSummary.setText(place.getSummary());
        holder.tvPlaceDistance.setText(String.format("%f",place.getDistance()));

        Glide.with(context).load(place.getPictureSmallURL())
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(holder.ivPlacePic);
    }

    @Override
    public int getItemCount() {
        return mPlaces.size(); //how many items are in Adapter
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //initiate variables
        public ImageView ivPlacePic;
        public TextView tvPlaceName;
        public TextView tvPlaceLocation;
        public TextView tvPlaceRating;
        public TextView tvPlaceSummary;
        public TextView tvPlaceDistance;

        public ViewHolder(View itemView) {
            super(itemView);
            //perform findViewById lookups

            //reference id of each view from item_place.xml and cast to type
            ivPlacePic = (ImageView) itemView.findViewById(R.id.ivPlacePic);
            tvPlaceName = (TextView) itemView.findViewById(R.id.tvPlaceName);
            tvPlaceLocation= (TextView) itemView.findViewById(R.id.tvPlaceLocation);
            tvPlaceRating = (TextView) itemView.findViewById(R.id.tvPlaceRating);
            tvPlaceSummary = (TextView) itemView.findViewById(R.id.tvPlaceSummary);
            tvPlaceDistance = (TextView) itemView.findViewById(R.id.tvPlaceDistance);
        }
    }

    // Clean all elements of the recycler
    public void clear(){
        mPlaces.clear();
        notifyDataSetChanged();
    }


}
