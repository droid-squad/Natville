package me.jwill2385.natville;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class PlaceCardAdapter extends RecyclerView.Adapter<PlaceCardAdapter.ViewHolder> {
    ArrayList<ArrayList<String>> mPlaces;
    Context context;
    ArrayList<String> place;

    public PlaceCardAdapter(ArrayList<ArrayList<String>> places){
        this.mPlaces = places;
    }

    @NonNull
    @Override
    public PlaceCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context= parent.getContext();
        LayoutInflater inflater=LayoutInflater.from(context);
        View placeCardView= inflater.inflate(R.layout.item_place_visit, parent, false);
        ViewHolder viewHolder= new ViewHolder(placeCardView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceCardAdapter.ViewHolder holder, int position) {
        place = mPlaces.get(position);

        holder.tvPlaceVisitedName.setText(place.get(1));

        Glide.with(context).load(place.get(0)).into(holder.ivPlaceVisitedPic);

    }

    @Override
    public int getItemCount() {
        return mPlaces.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //initiate variables
        public ImageView ivPlaceVisitedPic;
        public TextView tvPlaceVisitedName;


        public ViewHolder(View itemView) {
            super(itemView);

            ivPlaceVisitedPic = itemView.findViewById(R.id.ivPlaceVisitedPic);
            tvPlaceVisitedName = itemView.findViewById(R.id.tvPlaceVisitedName);
        }
    }
}
