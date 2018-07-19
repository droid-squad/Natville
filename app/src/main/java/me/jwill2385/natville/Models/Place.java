package me.jwill2385.natville.Models;

import org.json.JSONException;
import org.json.JSONObject;

public class Place {

    //initialize variables
    private String name;
    private String location;
    private String pictureSmallURL;
    private String pictureLargeURL;
    private String summary;
    private double rating;
    private double distance;
    private double latitude;
    private double longitude;

    //define variables from JSON object
    public Place(JSONObject object) throws JSONException{
        name = object.getString("name");
        location = object.getString("location");
        pictureSmallURL = object.getString("imgSqSmall");
        pictureLargeURL = object.getString("imgSmallMed");
        summary = object.getString("summary");
        rating = object.getDouble("stars");
        distance = object.getDouble("length"); //distance is in miles
        latitude = object.getDouble("latitude");
        longitude = object.getDouble("longitude");

    }

    //create getter method such that other activities can access these variables
    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getPictureSmallURL() {
        return pictureSmallURL;
    }

    public String getPictureLargeURL() {
        return pictureLargeURL;
    }

    public String getSummary() {
        return summary;
    }

    public double getRating() {
        return rating;
    }

    public double getDistance() {
        return distance;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
