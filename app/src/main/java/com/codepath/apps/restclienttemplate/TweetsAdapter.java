package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    Context context;
    List<Tweet> tweets;
    TextView timestamp;

    //pass in context and list of tweets
    public TweetsAdapter (Context context, List<Tweet> tweets){
        this.context = context;
        this.tweets = tweets;
    }

    @NonNull
    @Override
    // for each row, inflate the layout
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view);
    }

    //bind values based on position of element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //Get data at position
        Tweet tweet = tweets.get(position);

        //Bind tweet with Viewholder
        holder.bind(tweet);


    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    //Clean elements when refreshed
    //make sure this modifies and not creates a whole new list, that will mess with the recycler view
    public void clear(){
        tweets.clear();
        notifyDataSetChanged();
    }

    //Add a (new) list of items
    public void addAll(List<Tweet> tweetList){
        tweets.addAll(tweetList);
        notifyDataSetChanged();
    }


    //define a viewholder
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivprofilepic;
        TextView tvbody, tvscreenname;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivprofilepic = itemView.findViewById(R.id.ivProfilePic);
            tvbody = itemView.findViewById(R.id.tvBody);
            tvscreenname = itemView.findViewById(R.id.tvScreenName);
            timestamp = itemView.findViewById(R.id.tvTimestamp);
        }

        public void bind(Tweet tweet) {
            tvbody.setText(tweet.body);
            tvscreenname.setText(tweet.user.screenName);
            timestamp.setText(tweet.getFormattedTimestamp());
            Glide.with(context).load(tweet.user.profileImageURL).into(ivprofilepic);

        }
    }

}
