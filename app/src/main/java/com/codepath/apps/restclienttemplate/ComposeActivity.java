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
import org.parceler.Parcels;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {

    public static final int MAX_TWEET_LENGTH = 280;
    public static final String TAG = "ComposeActivity";

    EditText etCompose;
    Button btntweet;
    TextView tvwordCount;

    TwitterClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        client = TwitterApplication.getRestClient(this);

        etCompose = findViewById(R.id.etCompose);
        btntweet = findViewById(R.id.btnTweet);
        tvwordCount = findViewById(R.id.tvwordCount);

        //adding a text change listener
        etCompose.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Fires right before text is changing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Fires right as the text is being changed (even supplies the range of text)
                int tweetLength = etCompose.getText().toString().length();
                int charsLeft = MAX_TWEET_LENGTH - tweetLength;
                String charsLeftMessage = Integer.toString(charsLeft) + "/280 characters left";
                tvwordCount.setText(charsLeftMessage);

            }

            @Override
            public void afterTextChanged(Editable editable) {


            }

        });

        //Add a click listener on button
        btntweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String tweetContent = etCompose.getText().toString();
                //exception handaling
                if(tweetContent.isEmpty()){
                    Toast.makeText(ComposeActivity.this, "Sorry, tweets can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(tweetContent.length() > MAX_TWEET_LENGTH){
                  Toast.makeText(ComposeActivity.this, "Sorry, your tweet is to long :(", Toast.LENGTH_SHORT).show();
                  return;
                }
                //Toast.makeText(ComposeActivity.this, tweetContent, Toast.LENGTH_LONG).show();
                //make an API to Twitter to publish the tweet
                client.publishTweet(tweetContent, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.i(TAG, "onSuccess to publish tweet");
                        //we get back a json tweet object
                        try {
                            Tweet tweet = Tweet.fromJson(json.jsonObject);
                            Log.i(TAG, "Published tweet says: " + tweet.body);
                            Intent i = new Intent();
                            i.putExtra("tweet", Parcels.wrap(tweet));
                            //set result code and bundle data for response
                            setResult(RESULT_OK, i);
                            //closes activity and passes data to parent(onComposeActivity)
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e(TAG, "onFailure to publish tweet", throwable);

                    }
                });

            }
        });



    }
}