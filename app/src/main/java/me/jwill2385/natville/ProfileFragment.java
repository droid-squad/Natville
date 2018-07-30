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

import com.parse.ParseUser;

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

    private Button logOut;

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


}
