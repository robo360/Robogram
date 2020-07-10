package com.example.robogram.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.robogram.R;
import com.example.robogram.adapters.PostAdapter;
import com.example.robogram.adapters.ProfileAdapter;
import com.example.robogram.data.model.Post;
import com.example.robogram.helpers.EndlessRecyclerViewScrollListener;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import static com.example.robogram.data.model.Post.KEY_CREATED_AT;
import static com.example.robogram.data.model.Post.KEY_USERNAME;

public class ProfileFragment extends Fragment {
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
    public static final String TAG = "ProfileFragment";

    protected ProfileAdapter adapter;
    protected RecyclerView rvUserPosts;
    protected List<Post> posts;
    protected ProfileFragment fragment;
    protected ImageView ivProfile;
    protected TextView tvUsername;
    protected  Post post;
    ParseUser user;

    public ProfileFragment(){

    }
    public static ProfileFragment newInstance(Post post) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putParcelable("post", Parcels.wrap(post));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvUserPosts = view.findViewById(R.id.rvUserPosts);
        ivProfile = view.findViewById(R.id.ivProfile);
        tvUsername = view.findViewById(R.id.tvUsername);
        if(getArguments() != null) {
            post = Parcels.unwrap(getArguments().getParcelable("post"));
            user = post.getUser();
        } else{
            user = ParseUser.getCurrentUser();
        }
        tvUsername.setText(user.getUsername());
        String image = user.getUsername();
        if(image != null){ Glide.with(getContext()).load(user.getParseFile("image").getUrl()).into(ivProfile);}
        posts = new ArrayList<>();
        fragment = this;
        adapter = new ProfileAdapter(getContext(), posts, fragment);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        rvUserPosts.setAdapter(adapter);
        rvUserPosts.setLayoutManager(gridLayoutManager);
        getPostsQuery();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }


    protected void getPostsQuery() {
        //Specify which class to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        //Specify the object id
        query.include(KEY_USERNAME);
        query.whereEqualTo(KEY_USERNAME, user);
        query.addDescendingOrder(KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Error in query"+e);
                }else{
                    posts.addAll(objects);
                    adapter.notifyDataSetChanged();
                    //Log.i(TAG, "Query response: " + objects.get(0).getCreatedAt());
                }
            }
        });

    }
}