package me.jwill2385.natville.Models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("User")
public class User extends ParseObject {

    private static final String KEY_USERNAME = "username";
    private static final String KEY_PROFILEIMAGE = "profileImage";
    private static final String KEY_RANK = "rank";
    private static final String KEY_LEGACY = "legacy";


    //setters and getters
    public String getUsername() {
        return getString(KEY_USERNAME);
    }

    public void setUsername(String username) {
        put(KEY_USERNAME, username);
    }

    public ParseFile getProfileImage() {
        return getParseFile(KEY_PROFILEIMAGE);
    }

    public void setProfileImage(ParseFile image) {
        put(KEY_PROFILEIMAGE, image);
    }

    public Integer getLegacy() {
        return getInt(KEY_LEGACY);
    }

    public void setLegacy(Integer miles) {
        put(KEY_LEGACY, miles);
    }

}
