package com.example.robogram.adapters;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.robogram.R;
import com.example.robogram.data.model.Post;
import com.example.robogram.fragments.HomeFragment;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{
    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(ViewHolder, int, List)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(ViewHolder, int)
     */
    Context context;
    List<Post> posts;
    HomeFragment fragment;

    public interface OnClickBtnMoreListeners{
        public void onBtnMoreClicked(int position);
        public void onGotoProfileClicked(int position);
    }

    public PostAdapter(Context context, List<Post> posts, HomeFragment fragment) {
        this.context = context;
        this.posts = posts;
        this.fragment = fragment;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     * <p>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link ViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p>
     * Override {@link #onBindViewHolder(ViewHolder, int, List)} instead if Adapter can
     * handle efficient partial bind.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvDescription;
        private TextView tvUsername;
        private ImageView ivImagePost;
        private TextView tvCreatedAt;
        private ImageButton btnLike;
        private TextView tvLikes;
        private ImageButton btnMore;
        private ImageButton btnReply;
        private ImageView ivProfile;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            ivImagePost = itemView.findViewById(R.id.ivImagePost);
            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);
            btnLike = itemView.findViewById(R.id.btnLike);
            tvLikes = itemView.findViewById(R.id.tvLikes);
            btnMore = itemView.findViewById(R.id.btnMore);
            btnReply = itemView.findViewById(R.id.btnComment);
            ivProfile = itemView.findViewById(R.id.ivProfile);

            //set a listener on the btnLike button
            btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Post post = posts.get(getAdapterPosition());
                    int likes = post.getLikes() + 1;
                    post.setLikes(likes);
                    btnLike.setImageDrawable(context.getDrawable(R.drawable.ufi_heart_active));
                    tvLikes.setText(Integer.toString(likes) + " likes");
                    post.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e != null){
                                Log.e("HomeActivity", "Error Saving likes" + e);
                                Toast.makeText(context, "Did not like", Toast.LENGTH_SHORT).show();
                                btnLike.setImageDrawable(context.getDrawable(R.drawable.ufi_heart));
                            }
                        }
                    });
                }
            });

            //Set a listener on the view in another screen button
            btnMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragment.onBtnMoreClicked(getAdapterPosition());
                }
            });

            //Set a listener on the reply button
            btnReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragment.onBtnMoreClicked(getAdapterPosition());
                }
            });

            //set a listener on the profile and username place

            View.OnClickListener goToProfile = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragment.onGotoProfileClicked(getAdapterPosition());
                }
            };
            tvUsername.setOnClickListener(goToProfile);
            ivProfile.setOnClickListener(goToProfile);

        }

        public void bind(Post post) {
            String description = "<b>" + post.getUser().getUsername() + "</b> " + post.getDescription();
            tvDescription.setText(Html.fromHtml(description));
            tvUsername.setText(post.getUser().getUsername());
            tvLikes.setText(post.getLikes()+ " likes");
            Date date = post.getCreatedAt();
            String dateWithMonthAndDayOnly = new SimpleDateFormat("MMMM dd.").format(date);
            tvCreatedAt.setText(dateWithMonthAndDayOnly);
            ParseFile imageProfile = post.getUser().getParseFile("image");
            if(imageProfile != null){
                Glide.with(context).load(imageProfile.getUrl()).into(ivProfile);
            }
            ParseFile image = post.getImage();
            if(image != null){
                Glide.with(context).load(post.getImage().getUrl()).into(ivImagePost);
            }
        }
    }
    // Clean all elements of the recycler
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> objects) {
        posts.addAll(objects);
        notifyDataSetChanged();
    }
}
