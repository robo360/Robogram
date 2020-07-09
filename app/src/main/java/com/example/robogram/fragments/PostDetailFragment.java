package com.example.robogram.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.robogram.R;
import com.example.robogram.data.model.Post;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostDetailFragment extends Fragment {
    TextView tvDescription;
    TextView tvUsername;
    ImageView ivImagePost;
    TextView tvCreatedAt;
    ImageButton btnLike;
    TextView tvLikes;
    Post post;


    public PostDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param post Parameter 1.
     * @return A new instance of fragment PostDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PostDetailFragment newInstance(Post post) {
        PostDetailFragment fragment = new PostDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable("post", Parcels.wrap(post));
        fragment.setArguments(args);
        return fragment;
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
        post = Parcels.unwrap(getArguments().getParcelable("post"));

        //find view in the layout
        tvDescription = view.findViewById(R.id.tvDescription);
        tvUsername = view.findViewById(R.id.tvUsername);
        ivImagePost = view.findViewById(R.id.ivImagePost);
        tvCreatedAt = view.findViewById(R.id.tvCreatedAt);
        btnLike = view.findViewById(R.id.btnLike);
        tvLikes = view.findViewById(R.id.tvLikes);

        //fill the layout with data
        String description = "<b>" + post.getUser().getUsername() + "</b> " + post.getDescription();
        tvDescription.setText(Html.fromHtml(description));
        tvUsername.setText(post.getUser().getUsername());
        tvLikes.setText(post.getLikes()+ " likes");
        Date date = post.getCreatedAt();
        String dateWithMonthAndDayOnly = new SimpleDateFormat("MMMM dd.").format(date);
        tvCreatedAt.setText(dateWithMonthAndDayOnly);
        ParseFile image = post.getImage();
        if(image != null){
            Glide.with(getContext()).load(post.getImage().getUrl()).into(ivImagePost);
        }
        //set a listener on the btnLike button
        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int likes = post.getLikes() + 1;
                post.setLikes(likes);
                btnLike.setImageDrawable(getContext().getDrawable(R.drawable.ufi_heart_active));
                tvLikes.setText(Integer.toString(likes) + " likes");
                post.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e != null){
                            Log.e("HomeActivity", "Error Saving likes" + e);
                            Toast.makeText(getContext(), "Did not like", Toast.LENGTH_SHORT).show();
                            btnLike.setImageDrawable(getContext().getDrawable(R.drawable.ufi_heart));
                        }
                    }
                });
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_detail, container, false);
    }
}