package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcel;
import org.parceler.Parcels;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {

    public static final int MAX_TWEET_LENGTH = 140;
    public static final String TAG = "ComposeActivity";

    EditText etCompose;
    Button btnTweet;
    TextView tvCharacterCount;

    TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        client = TwitterApp.getRestClient(ComposeActivity.this);

        etCompose = findViewById(R.id.etCompose);
        btnTweet = findViewById(R.id.btnTweet);

        tvCharacterCount = findViewById(R.id.tvCharacterCount);

        etCompose.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String etComposeContent = etCompose.getText().toString();

                btnTweet.setEnabled(etComposeContent.length() <= MAX_TWEET_LENGTH);

                String etComposeContentFormat = etComposeContent.length() + "/" + MAX_TWEET_LENGTH;

                tvCharacterCount.setText(etComposeContentFormat);

                if (etComposeContent.length() == 0) {
                    tvCharacterCount.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tweetContent = etCompose.getText().toString();

                if(tweetContent.isEmpty()){
                    Toast.makeText(ComposeActivity.this, "Error: Tweet Empty", Toast.LENGTH_LONG).show();
                    return;
                }

                if(tweetContent.length() > MAX_TWEET_LENGTH) {
                    Toast.makeText(ComposeActivity.this, "Error: Tweet too Long", Toast.LENGTH_LONG).show();
                    return;
                }

                //Make API call to Twitter.
                client.postUserTweet(new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.i(TAG, "onSuccess: Tweet Published");
                        try {
                            Tweet tweet = Tweet.fromJson(json.jsonObject);
                            Log.i(TAG, String.valueOf(tweet.body));

                            Intent intent = new Intent();
                            intent.putExtra("tweet", Parcels.wrap(tweet));
                            setResult(RESULT_OK, intent);

                            finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e(TAG, "onFail: Compose Tweet", throwable);
                    }
                }, tweetContent);
            }
        });
    }
}