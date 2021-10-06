package com.codepath.apps.restclienttemplate.models;

import android.util.Log;

import com.codepath.apps.restclienttemplate.TimeFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Tweet {
    public String body;
    public String createdAt;
    public User user;

    public String imageEmbedded;

    public long id;

    //Empty constructor for Parceler
    public Tweet(){}

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet =  new Tweet();

        tweet.body = jsonObject.getString("text");
        tweet.createdAt = TimeFormatter.getTimeDifference(jsonObject.getString("created_at"));
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        tweet.id = jsonObject.getLong("id");


        JSONObject tweet_entities = jsonObject.getJSONObject("entities");

        if (tweet_entities.has("media")) {

            JSONArray media = tweet_entities.getJSONArray("media");

            for (int e = 0; e < media.length(); e++){
                //Log.i("media_length", String.valueOf(media.length()));
                JSONObject media_entities = media.getJSONObject(e);

                if (media_entities.getString("type").equals("photo")) {
                    //Log.i("media_entities", media_entities.getString("media_url_https"));
                    tweet.imageEmbedded = media_entities.getString("media_url_https");
                }
            }
        } else {
            tweet.imageEmbedded = "no-image";
        }

        return tweet;
    }

    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();

        for(int i = 0; i < jsonArray.length(); i++){
            tweets.add(fromJson(jsonArray.getJSONObject(i)));
        }
        return tweets;
    }
}
