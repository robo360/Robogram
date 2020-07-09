package com.example.robogram.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.robogram.R;
import com.example.robogram.adapters.PostAdapter;
import com.example.robogram.data.model.Post;
import com.example.robogram.helpers.EndlessRecyclerViewScrollListener;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import static com.example.robogram.data.model.Post.KEY_CREATED_AT;
import static com.example.robogram.data.model.Post.KEY_USERNAME;

public class HomeFragment extends Fragment implements PostAdapter.OnClickBtnMoreListener {
    public static final String TAG = "HomeFragment";

    public PostAdapter adapter;
    private RecyclerView rvPosts;
    protected List<Post> posts;
    private SwipeRefreshLayout swipeRefreshLayout;
    private EndlessRecyclerViewScrollListener scrollListener;
    HomeFragment fragment;

    public HomeFragment() {

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
        swipeRefreshLayout = view.findViewById(R.id.SwipeContainer);
        fragment = this;
        posts = new ArrayList<>();
        adapter = new PostAdapter(getContext(), posts, fragment);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvPosts.setAdapter(adapter);
        rvPosts.setLayoutManager(linearLayoutManager);


        //add on a scrollListener
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                getPostsQueryMore(totalItemsCount);
            }
        };
        // Adds the scroll listener to RecyclerView
        rvPosts.addOnScrollListener(scrollListener);

        getPostsQuery();

        //add a listener on the swipeContainer
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                posts.clear();
                getPostsQuery();
            }
        });
        // Configure the refreshing colors
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }

    private void getPostsQueryMore(int totalItemsCount) {
        //Specify which class to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        // Specify the object id
        query.include(Post.KEY_USERNAME);
        query.whereEqualTo(Post.KEY_USERNAME, ParseUser.getCurrentUser());
        query.setLimit(20);
        query.whereLessThan(KEY_CREATED_AT, posts.get(totalItemsCount - 1).getCreatedAt());
        query.addDescendingOrder(Post.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error in query" + e);
                } else {
                    posts.addAll(objects);
                    adapter.notifyDataSetChanged();
                    Log.i(TAG, "New Query" + posts.size());
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    protected void getPostsQuery() {
        //Specify which class to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        // Specify the object id
        query.include(Post.KEY_USERNAME);
        query.whereEqualTo(Post.KEY_USERNAME, ParseUser.getCurrentUser());
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error in query" + e);
                } else {
                    posts.addAll(objects);
                    adapter.notifyDataSetChanged();
                    //Log.i(TAG, "Query response" + objects.get(0).getDescription()+ "username:" + objects.get(0).getUser().getUsername());
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onBtnMoreClicked(int position) {
        Post post = posts.get(position);
        Fragment fragment = PostDetailFragment.newInstance(post);
        getParentFragmentManager().beginTransaction().replace(R.id.flContainer, fragment).commit();
    }
}