package me.jwill2385.natville.Models;

public class Place {

    //initialize variables
    private String name;
    private String location;
    private String pictureURL;
    private String summary;
    private String distance;
    private double latitutude;
    private double longitude;


    //create getter method such that other activities can access these variables
    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getPictureURL() {
        return pictureURL;
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
