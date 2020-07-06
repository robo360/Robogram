package com.example.robogram.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.robogram.R;
import com.example.robogram.data.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

import static com.example.robogram.data.model.Post.KEY_USERNAME;

public class HomeFragment extends Fragment {
    RecyclerView rvPosts;
    public static final String TAG = "HomeFragment";

    public HomeFragment(){

    }
    /**
     * Called immediately after {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * has returned, but before any saved state has been restored in to the view.
     * This gives subclasses a chance to initialize themselves once
     * they know their view hierarchy has been completely created.  The fragment's
     * view hierarchy is not however attached to its parent at this point.
     *
     * @param view               The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvPosts = view.findViewById(R.id.rvPosts);
        getPostsQuery();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
    private void getPostsQuery() {
        //Specify which class to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        // Specify the object id
        query.include(KEY_USERNAME);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Error in query"+e);
                }else{
                    Log.i(TAG, "Query response" + objects.get(0).getDescription()+ "username:" + objects.get(0).getUser().getUsername());
                }
            }
        });
    }
}