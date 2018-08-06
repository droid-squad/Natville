package me.jwill2385.natville.Models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Place implements Serializable{

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
    private double ascent;
    private double descent;
    private double high;
    private double low;;
    private String conditionStatus;
    private String conditionDetails;
    private String conditionUpdated;
    private String urlDetails;
    public String difficulty;


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
        ascent = object.getDouble("ascent");
        descent = object.getDouble("descent");
        high = object.getDouble("high");
        low = object.getDouble("low");
        conditionStatus = object.getString("conditionStatus");
        conditionDetails = object.getString("conditionDetails");
        conditionUpdated = object.getString("conditionDate");
        urlDetails = object.getString("url");
        difficulty = object.getString("difficulty");

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

    public double getAscent() { return ascent; }

    public double getDescent() { return descent; }

    public double getHigh() { return high; }

    public double getLow() { return low; }

    public String getConditionStatus() { return conditionStatus; }

    public String getConditionDetails() { return conditionDetails; }

    public String getConditionUpdated() { return conditionUpdated; }

    public String getUrlDetails() { return urlDetails; }

    public String getDifficulty() { return difficulty; }

}
