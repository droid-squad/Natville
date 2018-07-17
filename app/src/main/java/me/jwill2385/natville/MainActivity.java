package me.jwill2385.natville;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    final FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final HomeFragment fragmentHome = new HomeFragment();
        final SearchFragment fragmentSearch = new SearchFragment();
        final ProfileFragment fragmentProfile = new ProfileFragment();

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        //handle navigation selection
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.ic_home:
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.flContainer, fragmentHome).commit();
                                return true;
                            case R.id.ic_search:
                                FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction();
                                fragmentTransaction2.replace(R.id.flContainer, fragmentSearch).commit();
                                return true;
                            case R.id.ic_profile:
                                FragmentTransaction fragmentTransaction3 = fragmentManager.beginTransaction();
                                fragmentTransaction3.replace(R.id.flContainer, fragmentProfile).commit();
                                return true;
                        }
                        return false;
                    }
                });
    }
}
