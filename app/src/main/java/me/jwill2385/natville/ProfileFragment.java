package me.jwill2385.natville;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import me.jwill2385.natville.Models.User;

/*
For each fragment you must create
onCreate
onCreateView
A public interface listener to interact with main activity
Initialize that listener at the top
then define the functions it makes in the main activity

Also an onAttach function is needed to connect the fragment to the activity

 */

public class ProfileFragment extends Fragment {

    ParseUser currentUser;

    private Button logOut;
    private TextView username;
    private TextView rank;
    private TextView legacy;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currentUser= ParseUser.getCurrentUser();

        logOut = view.findViewById(R.id.bvLogout);
        username = view.findViewById(R.id.tvUsername);
        rank = view.findViewById(R.id.tvRank);
        legacy = view.findViewById(R.id.tvLegacy);

        username.setText(currentUser.getUsername());
        rank.setText("Rank: "+ currentUser.getString("rank"));
        legacy.setText("Legacy: "+  Double.toString(currentUser.getDouble("legacy"))+ " miles");

        logOut.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser();

                Intent intent = new Intent(getActivity(), loginActivity.class);
                startActivity(intent);
            }
        });

    }


}
