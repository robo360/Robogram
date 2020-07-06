package com.example.robogram.data.model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Post")
public class Post extends ParseObject {
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_IMAGE = "image";


    public ParseUser getUser() {
        return getParseUser(KEY_USERNAME);
    }

    public void setUser(ParseUser user) {
        put(KEY_USERNAME, user);
    }

    public ParseFile getImage(){
        return getParseFile(KEY_IMAGE);
    }
    public void setImage(ParseFile image){
        put(KEY_IMAGE, image);
    }

    public String getDescription(){
        return getString(KEY_DESCRIPTION);
    }
    public void setDescription(String description){
        put(KEY_DESCRIPTION, description);
    }
}
