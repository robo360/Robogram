package com.example.robogram.data.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Like")
public class Comment extends ParseObject {
    public static final String KEY_TEXT = "text";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_POST = "Post";
    public static final String KEY_CREATED_AT = "createdAt";

    public ParseUser getUser() {
        return getParseUser(KEY_USERNAME);
    }

    public void setUser(ParseUser user) {
        put(KEY_USERNAME, user);
    }

    public String getText(){
        return getString(KEY_TEXT);
    }

    public void setText(String text){
        put(KEY_TEXT,text);
    }

    public ParseObject getPost(){
        return getParseObject(KEY_POST);
    }
    public void setPost(ParseObject post){
        put(KEY_POST, post);
    }
}
