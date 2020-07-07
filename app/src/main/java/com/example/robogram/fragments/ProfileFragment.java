package com.example.robogram.fragments;

import android.util.Log;

import com.example.robogram.data.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

import static com.example.robogram.data.model.Post.KEY_CREATED_AT;
import static com.example.robogram.data.model.Post.KEY_USERNAME;

public class ProfileFragment extends HomeFragment {
    @Override
    protected void getPostsQuery() {
        //Specify which class to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        // Specify the object id
        query.include(KEY_USERNAME);
        query.setLimit(20);
        query.addDescendingOrder(KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Error in query"+e);
                }else{
                    posts.addAll(objects);
                    adapter.notifyDataSetChanged();
                    Log.i(TAG, "Query response" + objects.get(0).getDescription()+ "username:" + objects.get(0).getUser().getUsername());
                }
            }
        });

    }
}