package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

public class TweetDetailsActivity extends AppCompatActivity {

    Tweet tweet;

    ImageView ivDetailProfilePic;
    TextView tvTweetName;
    TextView tvTweetScreenName;
    TextView tvTweetBody;
    ImageView ivEmbedded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_details);

        ivDetailProfilePic = findViewById(R.id.ivDetailProfilePic);
        tvTweetName = findViewById(R.id.tvTweetName);
        tvTweetScreenName = findViewById(R.id.tvTweetScreenName);
        tvTweetBody = findViewById(R.id.tvTweetBody);
        ivEmbedded = findViewById(R.id.ivEmbedded);

        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra("tweetDetail"));

        Glide.with(this).load(tweet.user.profileImageURL).into(ivDetailProfilePic);
        tvTweetName.setText(tweet.user.name);
        tvTweetScreenName.setText(tweet.user.screenName);
        tvTweetBody.setText(tweet.body);

        if(!tweet.imageEmbedded.equals("no-image")) {
            Glide.with(this).load(tweet.imageEmbedded).centerCrop().transform(new RoundedCorners(30)).into(ivEmbedded);
        }
    }
}