package me.jwill2385.natville.Models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.HashMap;

@ParseClassName("LocationMap")
public class LocationMap extends ParseObject {
    private static final String KEY_MAP = "HashMapData";


    // this function will get the map stored in the Parse Object
    public ParseObject getMap() {
        return getParseObject(KEY_MAP);
    }

    //this function will fill the HashMapData object in parse with the object passed in.
    public void setMap(HashMap map){
        put(KEY_MAP, map);
    }
}
