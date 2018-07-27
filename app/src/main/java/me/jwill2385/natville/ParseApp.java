package me.jwill2385.natville;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

import me.jwill2385.natville.Models.LocationMap;

public class ParseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(LocationMap.class);

        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("SEA_park")
                .clientKey("seattleNature5")
                .server("http://parks-app.herokuapp.com/parse")
                .build();

        Parse.initialize(configuration);

    }
}
