package me.jwill2385.natville;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

<<<<<<< HEAD
import me.jwill2385.natville.Models.User;

public class ParseApp extends Application{
=======
import me.jwill2385.natville.Models.LocationMap;

public class ParseApp extends Application {

>>>>>>> a2dfc60ddd81af4703479a75f5776b1c188cecbc
    @Override
    public void onCreate() {
        super.onCreate();

<<<<<<< HEAD
        ParseObject.registerSubclass(User.class);

        final Parse.Configuration configuration=new Parse.Configuration.Builder(this)
=======
        ParseObject.registerSubclass(LocationMap.class);

        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
>>>>>>> a2dfc60ddd81af4703479a75f5776b1c188cecbc
                .applicationId("SEA_park")
                .clientKey("seattleNature5")
                .server("http://parks-app.herokuapp.com/parse")
                .build();

        Parse.initialize(configuration);
<<<<<<< HEAD
=======

>>>>>>> a2dfc60ddd81af4703479a75f5776b1c188cecbc
    }
}
