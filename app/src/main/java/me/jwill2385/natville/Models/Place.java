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
    private String distance;
    private double latitutude;
    private double longitude;

    //define variables from JSON object
    public Place(JSONObject object) throws JSONException{
        name = object.getString("name");
        location = object.getString("location");
        pictureSmallURL = object.getString("imgSqSmall");
        pictureLargeURL = object.getString("imgSmallMed");
        summary = object.getString("summary");

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

    public String getDistance() {
        return distance;
    }

    public double getLatitutude() {
        return latitutude;
    }

    public double getLongitude() {
        return longitude;
    }
}
